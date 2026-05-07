package net.vibzz.standardizations.villagerTrades;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.vibzz.standardizations.Rng;
import net.vibzz.standardizations.Standardizations;

import java.util.Random;

public final class VillagerTradeStandardization {

    private static final long SALT_ROLL = 0xF1L;

    private VillagerTradeStandardization() {}

    public static boolean isActive(World world) {
        return world instanceof ServerWorld
                && world.getGameRules().getBoolean(Standardizations.STANDARDIZE_VILLAGER_TRADES);
    }

    public static long seedOf(World world) {
        return ((ServerWorld) world).getSeed();
    }

    public static Random rollRandom(long worldSeed, Identifier profession, int level, int counter) {
        long seed = Rng.mix(worldSeed
                ^ Rng.mix(profession.toString().hashCode())
                ^ Rng.mix((long) level * Rng.SALT_A)
                ^ Rng.mix((long) counter * Rng.SALT_B)
                ^ SALT_ROLL);
        return new Random(seed);
    }
}
