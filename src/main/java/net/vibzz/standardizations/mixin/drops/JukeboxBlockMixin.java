package net.vibzz.standardizations.mixin.drops;

import net.minecraft.block.JukeboxBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.JukeboxBlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.vibzz.standardizations.util.BlockDropRng;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(JukeboxBlock.class)
public abstract class JukeboxBlockMixin {

    @Inject(method = "removeRecord", at = @At("HEAD"), cancellable = true)
    private void standardizations$deterministicRecordEject(World world, BlockPos pos, CallbackInfo ci) {
        if (world.isClient || !BlockDropRng.isActive(world)) return;

        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (!(blockEntity instanceof JukeboxBlockEntity)) {
            ci.cancel();
            return;
        }
        JukeboxBlockEntity jukebox = (JukeboxBlockEntity) blockEntity;
        ItemStack itemStack = jukebox.getRecord();
        if (itemStack.isEmpty()) {
            ci.cancel();
            return;
        }
        world.syncWorldEvent(1010, pos, 0);
        jukebox.clear();

        long seed = BlockDropRng.seedOf(world);
        double d = BlockDropRng.uniform01(seed, pos, BlockDropRng.SALT_SCATTER_X) * 0.7 + 0.15;
        double e = BlockDropRng.uniform01(seed, pos, BlockDropRng.SALT_SCATTER_Y) * 0.7 + 0.060000002 + 0.6;
        double g = BlockDropRng.uniform01(seed, pos, BlockDropRng.SALT_SCATTER_Z) * 0.7 + 0.15;
        ItemEntity itemEntity = new ItemEntity(world, pos.getX() + d, pos.getY() + e, pos.getZ() + g, itemStack.copy());
        BlockDropRng.applyStandardMotion(itemEntity, seed, pos);
        itemEntity.setToDefaultPickupDelay();
        world.spawnEntity(itemEntity);
        ci.cancel();
    }
}
