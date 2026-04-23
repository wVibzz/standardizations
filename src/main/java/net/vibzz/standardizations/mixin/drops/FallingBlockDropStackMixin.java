package net.vibzz.standardizations.mixin.drops;

import net.minecraft.entity.Entity;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.vibzz.standardizations.Standardizations;
import net.vibzz.standardizations.util.BlockDropRng;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class FallingBlockDropStackMixin {

    @Inject(method = "dropStack(Lnet/minecraft/item/ItemStack;F)Lnet/minecraft/entity/ItemEntity;",
            at = @At("HEAD"),
            cancellable = true)
    private void standardizations$deterministicFallingBlockDrop(ItemStack stack, float yOffset, CallbackInfoReturnable<ItemEntity> cir) {
        Entity self = (Entity) (Object) this;
        if (!(self instanceof FallingBlockEntity)) return;
        if (stack.isEmpty() || self.world.isClient) return;
        if (!(self.world instanceof ServerWorld)) return;
        if (!self.world.getGameRules().getBoolean(Standardizations.STANDARDIZE_BLOCK_DROPS)) return;

        BlockPos pos = self.getBlockPos();
        long seed = ((ServerWorld) self.world).getSeed();
        BlockDropRng.Offsets o = BlockDropRng.compute(seed, pos);

        ItemEntity itemEntity = new ItemEntity(
                self.world,
                self.getX(),
                self.getY() + yOffset,
                self.getZ(),
                stack);
        itemEntity.yaw = o.yaw;
        itemEntity.setVelocity(o.vx, o.vy, o.vz);
        itemEntity.setToDefaultPickupDelay();
        self.world.spawnEntity(itemEntity);
        cir.setReturnValue(itemEntity);
    }
}
