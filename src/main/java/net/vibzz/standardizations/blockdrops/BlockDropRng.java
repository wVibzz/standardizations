package net.vibzz.standardizations.blockdrops;

import net.minecraft.entity.ItemEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.vibzz.standardizations.Standardizations;

public final class BlockDropRng {

    private static final long SALT_OFFSET_X = 0xA1L;
    private static final long SALT_OFFSET_Y = 0xA2L;
    private static final long SALT_OFFSET_Z = 0xA3L;
    private static final long SALT_YAW      = 0xA4L;
    private static final long SALT_VEL_X    = 0xA5L;
    private static final long SALT_VEL_Z    = 0xA6L;

    // extra salts for sites that need more than 3 offsets / share keyPos
    public static final long SALT_SCATTER_X = 0xB1L;
    public static final long SALT_SCATTER_Y = 0xB2L;
    public static final long SALT_SCATTER_Z = 0xB3L;
    public static final long SALT_SCATTER_VX = 0xB4L;
    public static final long SALT_SCATTER_VY = 0xB5L;
    public static final long SALT_SCATTER_VZ = 0xB6L;
    public static final long SALT_CHUNK_SIZE = 0xB7L;
    public static final long SALT_YAW_ALT    = 0xB8L;
    public static final long SALT_PUSH_OUT   = 0xB9L;
    public static final long SALT_TNT_ANGLE  = 0xC1L;
    public static final long SALT_TNT_FUSE   = 0xC2L;
    public static final long SALT_EXPLOSION_RAY = 0xC3L;

    private BlockDropRng() {}

    public static boolean isActive(World world) {
        return world instanceof ServerWorld
                && world.getGameRules().getBoolean(Standardizations.STANDARDIZE_BLOCK_DROPS);
    }

    public static boolean isExplosionActive(World world) {
        return world instanceof ServerWorld
                && world.getGameRules().getBoolean(Standardizations.STANDARDIZE_EXPLOSIONS);
    }

    public static long seedOf(World world) {
        return ((ServerWorld) world).getSeed();
    }

    public static Offsets compute(long worldSeed, BlockPos pos) {
        long base = baseKey(worldSeed, pos);
        double ox = uniform(mix(base ^ SALT_OFFSET_X)) * 0.5 + 0.25;
        double oy = uniform(mix(base ^ SALT_OFFSET_Y)) * 0.5 + 0.25;
        double oz = uniform(mix(base ^ SALT_OFFSET_Z)) * 0.5 + 0.25;
        float yaw = (float) (uniform(mix(base ^ SALT_YAW)) * 360.0);
        double vx = uniform(mix(base ^ SALT_VEL_X)) * 0.2 - 0.1;
        double vz = uniform(mix(base ^ SALT_VEL_Z)) * 0.2 - 0.1;
        return new Offsets(ox, oy, oz, yaw, vx, 0.2, vz);
    }

    public static double uniform01(long worldSeed, BlockPos pos, long salt) {
        return uniform(mix(baseKey(worldSeed, pos) ^ salt));
    }

    public static double uniform01(long worldSeed, BlockPos pos, long salt, long extraKey) {
        return uniform(mix(baseKey(worldSeed, pos) ^ salt ^ mix(extraKey)));
    }

    // Box-Muller gaussian, zero-mean unit-variance
    public static double gaussian(long worldSeed, BlockPos pos, long salt, long extraKey) {
        double u1 = Math.max(uniform01(worldSeed, pos, salt ^ 0xC1L, extraKey), 1.0E-300);
        double u2 = uniform01(worldSeed, pos, salt ^ 0xC2L, extraKey);
        return Math.sqrt(-2.0 * Math.log(u1)) * Math.cos(2.0 * Math.PI * u2);
    }

    // sets yaw + xz velocity deterministically; y velocity stays at vanilla 0.2
    public static void applyStandardMotion(ItemEntity entity, long worldSeed, BlockPos keyPos) {
        long base = baseKey(worldSeed, keyPos);
        entity.yaw = (float) (uniform(mix(base ^ SALT_YAW)) * 360.0);
        double vx = uniform(mix(base ^ SALT_VEL_X)) * 0.2 - 0.1;
        double vz = uniform(mix(base ^ SALT_VEL_Z)) * 0.2 - 0.1;
        entity.setVelocity(vx, 0.2, vz);
    }

    private static long baseKey(long worldSeed, BlockPos pos) {
        return mix(worldSeed ^ mix(pos.asLong()));
    }

    private static long mix(long z) {
        z = (z ^ (z >>> 30)) * 0xbf58476d1ce4e5b9L;
        z = (z ^ (z >>> 27)) * 0x94d049bb133111ebL;
        return z ^ (z >>> 31);
    }

    private static double uniform(long bits) {
        return (bits >>> 11) * 0x1.0p-53;
    }

    public static final class Offsets {
        public final double x;
        public final double y;
        public final double z;
        public final float yaw;
        public final double vx;
        public final double vy;
        public final double vz;

        public Offsets(double x, double y, double z, float yaw, double vx, double vy, double vz) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.yaw = yaw;
            this.vx = vx;
            this.vy = vy;
            this.vz = vz;
        }
    }
}
