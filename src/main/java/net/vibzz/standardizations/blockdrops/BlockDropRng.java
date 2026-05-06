package net.vibzz.standardizations.blockdrops;

import net.minecraft.entity.ItemEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.vibzz.standardizations.Rng;
import net.vibzz.standardizations.Standardizations;

public final class BlockDropRng {

    private static final long SALT_OFFSET_X = 0xA1L;
    private static final long SALT_OFFSET_Y = 0xA2L;
    private static final long SALT_OFFSET_Z = 0xA3L;
    private static final long SALT_YAW      = 0xA4L;
    private static final long SALT_VEL_X    = 0xA5L;
    private static final long SALT_VEL_Z    = 0xA6L;

    public static final long SALT_SCATTER_X = 0xB1L;
    public static final long SALT_SCATTER_Y = 0xB2L;
    public static final long SALT_SCATTER_Z = 0xB3L;
    public static final long SALT_SCATTER_VX = 0xB4L;
    public static final long SALT_SCATTER_VY = 0xB5L;
    public static final long SALT_SCATTER_VZ = 0xB6L;
    public static final long SALT_CHUNK_SIZE = 0xB7L;
    public static final long SALT_YAW_ALT    = 0xB8L;
    public static final long SALT_PUSH_OUT   = 0xB9L;

    private BlockDropRng() {}

    public static boolean isActive(World world) {
        return world instanceof ServerWorld
                && world.getGameRules().getBoolean(Standardizations.STANDARDIZE_BLOCK_DROPS);
    }

    public static long seedOf(World world) {
        return ((ServerWorld) world).getSeed();
    }

    public static Offsets compute(long worldSeed, BlockPos pos) {
        long base = baseKey(worldSeed, pos);
        double ox = Rng.uniform(Rng.mix(base ^ SALT_OFFSET_X)) * 0.5 + 0.25;
        double oy = Rng.uniform(Rng.mix(base ^ SALT_OFFSET_Y)) * 0.5 + 0.25;
        double oz = Rng.uniform(Rng.mix(base ^ SALT_OFFSET_Z)) * 0.5 + 0.25;
        float yaw = (float) (Rng.uniform(Rng.mix(base ^ SALT_YAW)) * 360.0);
        double vx = Rng.uniform(Rng.mix(base ^ SALT_VEL_X)) * 0.2 - 0.1;
        double vz = Rng.uniform(Rng.mix(base ^ SALT_VEL_Z)) * 0.2 - 0.1;
        return new Offsets(ox, oy, oz, yaw, vx, 0.2, vz);
    }

    public static double uniform01(long worldSeed, BlockPos pos, long salt) {
        return Rng.uniform(Rng.mix(baseKey(worldSeed, pos) ^ salt));
    }

    public static double uniform01(long worldSeed, BlockPos pos, long salt, long extraKey) {
        return Rng.uniform(Rng.mix(baseKey(worldSeed, pos) ^ salt ^ Rng.mix(extraKey)));
    }

    public static double gaussian(long worldSeed, BlockPos pos, long salt, long extraKey) {
        double u1 = Math.max(uniform01(worldSeed, pos, salt ^ 0xC1L, extraKey), 1.0E-300);
        double u2 = uniform01(worldSeed, pos, salt ^ 0xC2L, extraKey);
        return Math.sqrt(-2.0 * Math.log(u1)) * Math.cos(2.0 * Math.PI * u2);
    }

    public static void applyStandardMotion(ItemEntity entity, long worldSeed, BlockPos keyPos) {
        long base = baseKey(worldSeed, keyPos);
        entity.yaw = (float) (Rng.uniform(Rng.mix(base ^ SALT_YAW)) * 360.0);
        double vx = Rng.uniform(Rng.mix(base ^ SALT_VEL_X)) * 0.2 - 0.1;
        double vz = Rng.uniform(Rng.mix(base ^ SALT_VEL_Z)) * 0.2 - 0.1;
        entity.setVelocity(vx, 0.2, vz);
    }

    private static long baseKey(long worldSeed, BlockPos pos) {
        return Rng.mix(worldSeed ^ Rng.mix(pos.asLong()));
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
