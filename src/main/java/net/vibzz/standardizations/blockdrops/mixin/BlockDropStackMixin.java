package net.vibzz.standardizations.blockdrops.mixin;

import net.minecraft.block.Block;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.vibzz.standardizations.Standardizations;
import net.vibzz.standardizations.blockdrops.BlockDropRng;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public abstract class BlockDropStackMixin {

    @Inject(method = "dropStack(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/item/ItemStack;)V",
            at = @At("HEAD"),
            cancellable = true)
    private static void standardizations$deterministicDropStack(World world, BlockPos pos, ItemStack stack, CallbackInfo ci) {
        if (world.isClient || stack.isEmpty()) {
            return;
        }
        if (!(world instanceof ServerWorld)) {
            return;
        }
        if (!world.getGameRules().getBoolean(Standardizations.STANDARDIZE_BLOCK_DROPS)) {
            return;
        }
        if (!world.getGameRules().getBoolean(GameRules.DO_TILE_DROPS)) {
            ci.cancel();
            return;
        }

        long seed = ((ServerWorld) world).getSeed();
        BlockDropRng.Offsets o = BlockDropRng.compute(seed, pos);

        ItemEntity itemEntity = new ItemEntity(
                world,
                pos.getX() + o.x,
                pos.getY() + o.y,
                pos.getZ() + o.z,
                stack);
        itemEntity.yaw = o.yaw;
        itemEntity.setVelocity(o.vx, o.vy, o.vz);
        itemEntity.setToDefaultPickupDelay();
        world.spawnEntity(itemEntity);

        ci.cancel();
    }
}
