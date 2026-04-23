package net.vibzz.standardizations.mixin.drops;

import net.minecraft.block.BlockState;
import net.minecraft.block.PumpkinBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.vibzz.standardizations.util.BlockDropRng;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Random;

@Mixin(PumpkinBlock.class)
public abstract class PumpkinBlockMixin {

    @Redirect(method = "onUse",
              at = @At(value = "INVOKE", target = "Ljava/util/Random;nextDouble()D", ordinal = 0))
    private double standardizations$carveJitterX(Random random, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (BlockDropRng.isActive(world)) {
            return BlockDropRng.uniform01(BlockDropRng.seedOf(world), pos, BlockDropRng.SALT_SCATTER_VX);
        }
        return random.nextDouble();
    }

    @Redirect(method = "onUse",
              at = @At(value = "INVOKE", target = "Ljava/util/Random;nextDouble()D", ordinal = 1))
    private double standardizations$carveJitterZ(Random random, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (BlockDropRng.isActive(world)) {
            return BlockDropRng.uniform01(BlockDropRng.seedOf(world), pos, BlockDropRng.SALT_SCATTER_VZ);
        }
        return random.nextDouble();
    }
}
