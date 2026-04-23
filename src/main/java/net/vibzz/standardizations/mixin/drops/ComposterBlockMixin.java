package net.vibzz.standardizations.mixin.drops;

import net.minecraft.block.BlockState;
import net.minecraft.block.ComposterBlock;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.vibzz.standardizations.util.BlockDropRng;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ComposterBlock.class)
public abstract class ComposterBlockMixin {

    @Inject(method = "emptyFullComposter", at = @At("HEAD"), cancellable = true)
    private static void standardizations$deterministicComposterPop(BlockState state, World world, BlockPos pos, CallbackInfoReturnable<BlockState> cir) {
        if (world.isClient || !BlockDropRng.isActive(world)) return;

        long seed = BlockDropRng.seedOf(world);
        double d = BlockDropRng.uniform01(seed, pos, BlockDropRng.SALT_SCATTER_X) * 0.7 + 0.15;
        double e = BlockDropRng.uniform01(seed, pos, BlockDropRng.SALT_SCATTER_Y) * 0.7 + 0.060000002 + 0.6;
        double g = BlockDropRng.uniform01(seed, pos, BlockDropRng.SALT_SCATTER_Z) * 0.7 + 0.15;
        ItemEntity itemEntity = new ItemEntity(world, pos.getX() + d, pos.getY() + e, pos.getZ() + g, new ItemStack(Items.BONE_MEAL));
        BlockDropRng.applyStandardMotion(itemEntity, seed, pos);
        itemEntity.setToDefaultPickupDelay();
        world.spawnEntity(itemEntity);

        BlockState emptied = state.with(ComposterBlock.LEVEL, 0);
        world.setBlockState(pos, emptied, 3);
        world.playSound(null, pos, SoundEvents.BLOCK_COMPOSTER_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
        cir.setReturnValue(emptied);
    }
}
