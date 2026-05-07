package net.vibzz.standardizations.mobSpawns;

import net.minecraft.entity.mob.MobEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.vibzz.standardizations.Rng;
import net.vibzz.standardizations.Standardizations;

import java.util.UUID;

public final class MobSpawnStandardization {

    private static final long SALT_VARIANT = 0x14L;
    private static final long SALT_UUID    = 0x15L;
    private static final long SALT_GOAL    = 0x16L;

    private MobSpawnStandardization() {}

    public static boolean isActive(WorldAccess world) {
        if (!(world instanceof ServerWorldAccess)) return false;
        World w = world.getWorld();
        return w instanceof ServerWorld
                && w.getGameRules().getBoolean(Standardizations.STANDARDIZE_INITIAL_SPAWNS);
    }

    public static boolean isActiveForMob(MobEntity mob) {
        return mob.world instanceof ServerWorld
                && mob.world.getGameRules().getBoolean(Standardizations.STANDARDIZE_INITIAL_SPAWNS);
    }

    public static long seedOf(WorldAccess world) {
        return ((ServerWorldAccess) world).getSeed();
    }

    public static long variantSeed(long worldSeed, BlockPos pos) {
        return Rng.mix(worldSeed ^ (pos.asLong() * Rng.GAMMA) ^ SALT_VARIANT);
    }

    public static UUID uuidForMob(long variantSeed) {
        return new UUID(variantSeed ^ SALT_UUID, Rng.mix(variantSeed ^ Rng.GAMMA));
    }

    public static void seedForGoalCall(MobEntity mob, Class<?> goalClass) {
        UUID uuid = mob.getUuid();
        long seed = Rng.mix(uuid.getMostSignificantBits()
                ^ Rng.mix(uuid.getLeastSignificantBits() * Rng.GAMMA)
                ^ Rng.mix(mob.world.getTime() * Rng.SALT_A)
                ^ Rng.mix((long) goalClass.getName().hashCode() * Rng.SALT_B)
                ^ SALT_GOAL);
        mob.getRandom().setSeed(seed);
    }

    private static final ThreadLocal<MobEntity> CURRENT_MOB = new ThreadLocal<>();

    public static void setCurrentMob(MobEntity mob) { CURRENT_MOB.set(mob); }
    public static MobEntity getCurrentMob() { return CURRENT_MOB.get(); }
    public static void clearCurrentMob() { CURRENT_MOB.remove(); }
}
