package net.vibzz.standardizations.spawners;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.vibzz.standardizations.Standardizations;

public final class SpawnerStandardization {

    private static final long MIX_C1 = 0xBF58476D1CE4E5B9L;
    private static final long MIX_C2 = 0x94D049BB133111EBL;

    private static final long SALT_WAVE = 0x9E3779B97F4A7C15L;
    private static final long SALT_GENERATION = 0x6C3671D916B4A139L;
    private static final long SALT_DELAY = 0xA24BAED4963EE407L;
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
        return mix(worldSeed
                ^ (pos.asLong() * SALT_WAVE)
                ^ ((long) generation * SALT_GENERATION));
    }

    public static long mobSeed(long waveSeed, int slot) {
        return mix(waveSeed ^ ((long) slot * SALT_MOB_INDEX));
    }

    public static long delaySeed(long waveSeed) {
        return mix(waveSeed ^ SALT_DELAY);
    }

    private static long mix(long z) {
        z = (z ^ (z >>> 30)) * MIX_C1;
        z = (z ^ (z >>> 27)) * MIX_C2;
        return z ^ (z >>> 31);
    }
}
