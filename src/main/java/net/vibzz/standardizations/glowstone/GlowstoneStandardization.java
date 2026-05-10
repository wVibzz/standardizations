package net.vibzz.standardizations.glowstone;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.vibzz.standardizations.Rng;
import net.vibzz.standardizations.Standardizations;

import java.util.Random;

public final class GlowstoneStandardization {

    private static final long SALT_DROP = 0x18L;

    private GlowstoneStandardization() {}

    public static boolean isActive(World world) {
        return world instanceof ServerWorld
                && world.getGameRules().getBoolean(Standardizations.STANDARDIZE_GLOWSTONE_DROPS);
    }

    public static Random dropRandom(long worldSeed, int counter) {
        long seed = Rng.mix(worldSeed
                ^ Rng.mix((long) counter * Rng.SALT_A)
                ^ SALT_DROP);
        return new Random(seed);
    }

    public static Random dropRandomByPos(long worldSeed, BlockPos pos) {
        long seed = Rng.mix(worldSeed
                ^ (pos.asLong() * Rng.GAMMA)
                ^ SALT_DROP);
        return new Random(seed);
    }
}
