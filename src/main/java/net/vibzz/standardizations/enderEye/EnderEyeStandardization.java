package net.vibzz.standardizations.enderEye;

import net.minecraft.entity.ItemEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import net.vibzz.standardizations.Standardizations;
import net.vibzz.standardizations.Rng;

import java.util.UUID;

public final class EnderEyeStandardization {

    private static final long SALT_ATTEMPT = 0xD0L;
    private static final long SALT_YAW     = 0xD1L;
    private static final long SALT_VEL_X   = 0xD2L;
    private static final long SALT_VEL_Z   = 0xD3L;

    private EnderEyeStandardization() {}

    public static boolean isActive(World world) {
        return world instanceof ServerWorld
                && world.getGameRules().getBoolean(Standardizations.STANDARDIZE_ENDER_EYE);
    }

    public static long seedOf(World world) {
        return ((ServerWorld) world).getSeed();
    }

    public static UUID uuidForAttempt(long worldSeed, int attemptIndex) {
        long base = Rng.mix(worldSeed ^ ((long) attemptIndex * Rng.GAMMA) ^ SALT_ATTEMPT);
        return new UUID(base, Rng.mix(base ^ Rng.MIX_C1));
    }

    public static void applyStandardMotion(ItemEntity entity, UUID uuid) {
        long base = Rng.mix(uuid.getMostSignificantBits() ^ (uuid.getLeastSignificantBits() * Rng.GAMMA));
        entity.yaw = (float) (Rng.uniform(Rng.mix(base ^ SALT_YAW)) * 360.0);
        double vx = Rng.uniform(Rng.mix(base ^ SALT_VEL_X)) * 0.2 - 0.1;
        double vz = Rng.uniform(Rng.mix(base ^ SALT_VEL_Z)) * 0.2 - 0.1;
        entity.setVelocity(vx, 0.2, vz);
    }
}
