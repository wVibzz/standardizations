package net.vibzz.standardizations.ores;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.vibzz.standardizations.Rng;
import net.vibzz.standardizations.Standardizations;

import java.util.Random;

public final class OreStandardization {

    private static final long SALT_DROP = 0x1AL;

    private OreStandardization() {}

    public static boolean isActive(World world) {
        return world instanceof ServerWorld
                && world.getGameRules().getBoolean(Standardizations.STANDARDIZE_ORE_DROPS);
    }

    public static boolean isOre(BlockState state) {
        Block b = state.getBlock();
        return b == Blocks.COAL_ORE
                || b == Blocks.IRON_ORE
                || b == Blocks.GOLD_ORE
                || b == Blocks.DIAMOND_ORE
                || b == Blocks.EMERALD_ORE
                || b == Blocks.LAPIS_ORE
                || b == Blocks.REDSTONE_ORE
                || b == Blocks.NETHER_QUARTZ_ORE
                || b == Blocks.NETHER_GOLD_ORE
                || b == Blocks.ANCIENT_DEBRIS;
    }

    public static Identifier blockId(BlockState state) {
        return Registry.BLOCK.getId(state.getBlock());
    }

    public static Random dropRandom(long worldSeed, Identifier blockId, int counter) {
        long seed = Rng.mix(worldSeed
                ^ Rng.mix(blockId.toString().hashCode())
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
