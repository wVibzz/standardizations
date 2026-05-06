package net.vibzz.standardizations.spawners;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.vibzz.standardizations.Standardizations;
import net.vibzz.standardizations.Rng;

public final class SpawnerStandardization {

    private static final long SALT_WAVE = Rng.GAMMA;
    private static final long SALT_GENERATION = Rng.SALT_A;
    private static final long SALT_DELAY = Rng.SALT_B;
    private static final long SALT_MOB_INDEX = 0x517CC1B727220A95L;

    private SpawnerStandardization() {}

    public static boolean isActive(World world) {
        return world instanceof ServerWorld
                && world.getGameRules().getBoolean(Standardizations.STANDARDIZE_SILVERFISH_SPAWNER);
    }

    public static long seedOf(World world) {
        return ((ServerWorld) world).getSeed();
    }

    public static long waveSeed(long worldSeed, BlockPos pos, int generation) {
        return Rng.mix(worldSeed
                ^ (pos.asLong() * SALT_WAVE)
                ^ ((long) generation * SALT_GENERATION));
    }

    public static long mobSeed(long waveSeed, int slot) {
        return Rng.mix(waveSeed ^ ((long) slot * SALT_MOB_INDEX));
    }

    public static long delaySeed(long waveSeed) {
        return Rng.mix(waveSeed ^ SALT_DELAY);
    }
}
