package net.vibzz.standardizations.mobSpawns.mixin;

import net.minecraft.entity.EntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.SpawnHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.vibzz.standardizations.mobSpawns.InitialSpawnMarker;
import net.vibzz.standardizations.mobSpawns.MobSpawnStandardization;
import net.vibzz.standardizations.spawners.mixin.EntityRandomAccessor;
import net.vibzz.standardizations.spawners.mixin.EntityUuidAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Random;
import java.util.UUID;

@Mixin(SpawnHelper.class)
public abstract class SpawnHelperPopulateMixin {

    @Redirect(
            method = "populateEntities",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/mob/MobEntity;initialize(Lnet/minecraft/world/WorldAccess;Lnet/minecraft/world/LocalDifficulty;Lnet/minecraft/entity/SpawnReason;Lnet/minecraft/entity/EntityData;Lnet/minecraft/nbt/CompoundTag;)Lnet/minecraft/entity/EntityData;"
            )
    )
    private static EntityData standardizations$deterministicInit(
            MobEntity mob, WorldAccess world, LocalDifficulty difficulty, SpawnReason reason,
            EntityData data, CompoundTag tag) {
        if (!MobSpawnStandardization.isActive(world)) {
            return mob.initialize(world, difficulty, reason, data, tag);
        }

        World w = world.getWorld();
        if (!(w instanceof ServerWorld)) {
            return mob.initialize(world, difficulty, reason, data, tag);
        }
        BlockPos spawnPos = ((ServerWorld) w).getSpawnPos();
        ChunkPos mobChunk = new ChunkPos(mob.getBlockPos());
        ChunkPos spawnChunk = new ChunkPos(spawnPos);
        int dx = Math.abs(mobChunk.x - spawnChunk.x);
        int dz = Math.abs(mobChunk.z - spawnChunk.z);
        if (Math.max(dx, dz) > 12) {
            return mob.initialize(world, difficulty, reason, data, tag);
        }

        long seed = MobSpawnStandardization.variantSeed(
                MobSpawnStandardization.seedOf(world), mob.getBlockPos());
        ((EntityRandomAccessor) mob).getRandom().setSeed(seed);

        UUID uuid = MobSpawnStandardization.uuidForMob(seed);
        ((EntityUuidAccessor) mob).setUuidDirect(uuid);
        ((EntityUuidAccessor) mob).setUuidStringDirect(uuid.toString());
        ((InitialSpawnMarker) mob).standardizations$setInitialSpawn(true);

        EntityData result = mob.initialize(world, difficulty, reason, data, tag);

        if (mob instanceof SheepEntity) {
            Random rng = new Random(seed ^ 0xC010L);
            ((SheepEntity) mob).setColor(SheepEntity.generateDefaultColor(rng));
        }

        return result;
    }
}
