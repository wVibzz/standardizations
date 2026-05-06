package net.vibzz.standardizations.explosion;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.vibzz.standardizations.Rng;
import net.vibzz.standardizations.Standardizations;

public final class ExplosionRng {

    public static final long SALT_TNT_ANGLE = 0xC1L;
    public static final long SALT_TNT_FUSE = 0xC2L;
    public static final long SALT_RAY = 0xC3L;
    public static final long SALT_FIRE = 0xC4L;
    public static final long SALT_MINECART_FUSE_A = 0xC5L;
    public static final long SALT_MINECART_FUSE_B = 0xC6L;
    public static final long SALT_MINECART_POWER = 0xC7L;

    private ExplosionRng() {}

    public static boolean isActive(World world) {
        return world instanceof ServerWorld
                && world.getGameRules().getBoolean(Standardizations.STANDARDIZE_EXPLOSIONS);
    }

    public static long seedOf(World world) {
        return ((ServerWorld) world).getSeed();
    }

    public static double uniform01(long worldSeed, BlockPos pos, long salt) {
        return Rng.uniform(Rng.mix(Rng.mix(worldSeed ^ Rng.mix(pos.asLong())) ^ salt));
    }

    public static double uniform01(long worldSeed, BlockPos pos, long salt, long extraKey) {
        return Rng.uniform(Rng.mix(Rng.mix(worldSeed ^ Rng.mix(pos.asLong())) ^ salt ^ Rng.mix(extraKey)));
    }
}
