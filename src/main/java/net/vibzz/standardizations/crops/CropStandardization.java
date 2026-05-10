package net.vibzz.standardizations.crops;

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

public final class CropStandardization {

    private static final long SALT_DROP = 0x19L;

    private CropStandardization() {}

    public static boolean isActive(World world) {
        return world instanceof ServerWorld
                && world.getGameRules().getBoolean(Standardizations.STANDARDIZE_CROP_DROPS);
    }

    public static boolean isCrop(BlockState state) {
        Block b = state.getBlock();
        return b == Blocks.WHEAT
                || b == Blocks.CARROTS
                || b == Blocks.POTATOES
                || b == Blocks.BEETROOTS
                || b == Blocks.MELON
                || b == Blocks.SWEET_BERRY_BUSH
                || b == Blocks.NETHER_WART
                || b == Blocks.MELON_STEM
                || b == Blocks.ATTACHED_MELON_STEM
                || b == Blocks.PUMPKIN_STEM
                || b == Blocks.ATTACHED_PUMPKIN_STEM;
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
