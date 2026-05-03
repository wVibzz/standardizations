package net.vibzz.standardizations.spawners.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.collection.WeightedPicker;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.MobSpawnerEntry;
import net.minecraft.world.MobSpawnerLogic;
import net.minecraft.world.World;
import net.vibzz.standardizations.spawners.SpawnerStandardization;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Mixin(MobSpawnerLogic.class)
public abstract class MobSpawnerLogicMixin {

    @Shadow private int spawnDelay;
    @Shadow private int spawnCount;
    @Shadow private int minSpawnDelay;
    @Shadow private int maxSpawnDelay;
    @Shadow private int spawnRange;
    @Shadow private int maxNearbyEntities;
    @Shadow private MobSpawnerEntry spawnEntry;
    @Shadow private List<MobSpawnerEntry> spawnPotentials;

    @Shadow public abstract World getWorld();
    @Shadow public abstract BlockPos getPos();
    @Shadow public abstract void sendStatus(int status);
    @Shadow public abstract void setSpawnEntry(MobSpawnerEntry spawnEntry);

    @Shadow private boolean isPlayerInRange() { throw new AssertionError(); }

    @Unique private int standardizations$generation = 0;
    @Unique private boolean standardizations$waveAborted;

    @Inject(method = "update", at = @At("HEAD"), cancellable = true)
    private void standardizations$deterministicUpdate(CallbackInfo ci) {
        World world = this.getWorld();
        if (world.isClient) return;
        if (!SpawnerStandardization.isActive(world)) return;
        if (!"minecraft:silverfish".equals(this.spawnEntry.getEntityTag().getString("id"))) return;
        if (!this.isPlayerInRange()) return;

        BlockPos pos = this.getPos();
        long worldSeed = SpawnerStandardization.seedOf(world);

        if (this.spawnDelay == -1) {
            this.standardizations$resetDelay(worldSeed, pos);
        }

        if (this.spawnDelay > 0) {
            this.spawnDelay--;
            ci.cancel();
            return;
        }

        long waveSeed = SpawnerStandardization.waveSeed(worldSeed, pos, this.standardizations$generation);
        int spawned = this.standardizations$spawnWave((ServerWorld) world, pos, waveSeed);

        if (spawned > 0 || this.standardizations$waveAborted) {
            this.standardizations$resetDelay(worldSeed, pos);
            if (spawned > 0) {
                this.standardizations$generation++;
            }
        }
        ci.cancel();
    }

    @Unique
    private int standardizations$spawnWave(ServerWorld world, BlockPos pos, long waveSeed) {
        this.standardizations$waveAborted = false;
        int spawned = 0;

        for (int i = 0; i < this.spawnCount; i++) {
            long mobSeed = SpawnerStandardization.mobSeed(waveSeed, i);
            Random rng = new Random(mobSeed);

            CompoundTag entityTag = this.spawnEntry.getEntityTag();
            Optional<EntityType<?>> optType = EntityType.fromTag(entityTag);
            if (!optType.isPresent()) {
                this.standardizations$waveAborted = true;
                return spawned;
            }

            ListTag nbtPos = entityTag.getList("Pos", 6);
            int posSize = nbtPos.size();
            double sx = posSize >= 1 ? nbtPos.getDouble(0)
                    : pos.getX() + (rng.nextDouble() - rng.nextDouble()) * this.spawnRange + 0.5;
            double sy = posSize >= 2 ? nbtPos.getDouble(1)
                    : pos.getY() + rng.nextInt(3) - 1;
            double sz = posSize >= 3 ? nbtPos.getDouble(2)
                    : pos.getZ() + (rng.nextDouble() - rng.nextDouble()) * this.spawnRange + 0.5;

            EntityType<?> type = optType.get();
            if (!world.doesNotCollide(type.createSimpleBoundingBox(sx, sy, sz))) continue;
            if (!SpawnRestriction.canSpawn(type, world.getWorld(), SpawnReason.SPAWNER, new BlockPos(sx, sy, sz), rng)) continue;

            Entity entity = EntityType.loadEntityWithPassengers(entityTag, world, e -> {
                e.refreshPositionAndAngles(sx, sy, sz, e.yaw, e.pitch);
                return e;
            });
            if (entity == null) {
                this.standardizations$waveAborted = true;
                return spawned;
            }

            int near = world.getNonSpectatingEntities(
                    entity.getClass(),
                    new Box(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1).expand(this.spawnRange)
            ).size();
            if (near >= this.maxNearbyEntities) {
                this.standardizations$waveAborted = true;
                return spawned;
            }

            float yaw = rng.nextFloat() * 360.0F;
            entity.refreshPositionAndAngles(entity.getX(), entity.getY(), entity.getZ(), yaw, 0.0F);

            UUID uuid = new UUID(mobSeed, mobSeed ^ 0xBF58476D1CE4E5B9L);
            ((EntityUuidAccessor) entity).setUuidDirect(uuid);
            ((EntityUuidAccessor) entity).setUuidStringDirect(uuid.toString());

            if (entity instanceof MobEntity) {
                MobEntity mob = (MobEntity) entity;
                if (!mob.canSpawn(world, SpawnReason.SPAWNER) || !mob.canSpawn(world)) continue;

                ((EntityRandomAccessor) entity).getRandom().setSeed(mobSeed);

                if (entityTag.getSize() == 1 && entityTag.contains("id", 8)) {
                    mob.initialize(world, world.getLocalDifficulty(entity.getBlockPos()), SpawnReason.SPAWNER, null, null);
                }
            }

            if (world.spawnEntity(entity)) {
                for (Entity passenger : entity.getPassengerList()) {
                    world.spawnEntity(passenger);
                }
            }
            world.syncWorldEvent(2004, pos, 0);
            if (entity instanceof MobEntity) {
                ((MobEntity) entity).playSpawnEffects();
            }
            spawned++;
        }

        return spawned;
    }

    @Unique
    private void standardizations$resetDelay(long worldSeed, BlockPos pos) {
        long waveSeed = SpawnerStandardization.waveSeed(worldSeed, pos, this.standardizations$generation);
        Random rng = new Random(SpawnerStandardization.delaySeed(waveSeed));

        if (this.maxSpawnDelay <= this.minSpawnDelay) {
            this.spawnDelay = this.minSpawnDelay;
        } else {
            this.spawnDelay = this.minSpawnDelay + rng.nextInt(this.maxSpawnDelay - this.minSpawnDelay);
        }

        if (!this.spawnPotentials.isEmpty()) {
            this.setSpawnEntry(WeightedPicker.getRandom(rng, this.spawnPotentials));
        }

        this.sendStatus(1);
    }

    @Inject(method = "toTag", at = @At("RETURN"))
    private void standardizations$writeGen(CompoundTag tag, CallbackInfoReturnable<CompoundTag> cir) {
        cir.getReturnValue().putShort("StandardizationsGen", (short) this.standardizations$generation);
    }

    @Inject(method = "fromTag", at = @At("RETURN"))
    private void standardizations$readGen(CompoundTag tag, CallbackInfo ci) {
        if (tag.contains("StandardizationsGen", 99)) {
            this.standardizations$generation = tag.getShort("StandardizationsGen");
        }
    }
}
