package net.vibzz.standardizations.enderEye;

import net.minecraft.entity.ItemEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import net.vibzz.standardizations.Standardizations;

import java.util.UUID;

public final class EnderEyeStandardization {

    private static final long MIX_C1 = 0xBF58476D1CE4E5B9L;
    private static final long MIX_C2 = 0x94D049BB133111EBL;

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
        long base = mix(worldSeed ^ ((long) attemptIndex * 0x9E3779B97F4A7C15L) ^ SALT_ATTEMPT);
        return new UUID(base, mix(base ^ MIX_C1));
    }

    public static void applyStandardMotion(ItemEntity entity, UUID uuid) {
        long base = mix(uuid.getMostSignificantBits() ^ (uuid.getLeastSignificantBits() * 0x9E3779B97F4A7C15L));
        entity.yaw = (float) (uniform(mix(base ^ SALT_YAW)) * 360.0);
        double vx = uniform(mix(base ^ SALT_VEL_X)) * 0.2 - 0.1;
        double vz = uniform(mix(base ^ SALT_VEL_Z)) * 0.2 - 0.1;
        entity.setVelocity(vx, 0.2, vz);
    }

    private static long mix(long z) {
        z = (z ^ (z >>> 30)) * MIX_C1;
        z = (z ^ (z >>> 27)) * MIX_C2;
        return z ^ (z >>> 31);
    }

    private static double uniform(long bits) {
        return (bits >>> 11) * 0x1.0p-53;
    }
}
