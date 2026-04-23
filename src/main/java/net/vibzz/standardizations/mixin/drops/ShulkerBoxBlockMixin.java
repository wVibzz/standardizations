package net.vibzz.standardizations.mixin.drops;

import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.vibzz.standardizations.util.BlockDropRng;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ShulkerBoxBlock.class)
public abstract class ShulkerBoxBlockMixin {

    @Redirect(method = "onBreak",
              at = @At(value = "NEW",
                       target = "(Lnet/minecraft/world/World;DDDLnet/minecraft/item/ItemStack;)Lnet/minecraft/entity/ItemEntity;"))
    private ItemEntity standardizations$deterministicShulkerDrop(World world, double x, double y, double z, ItemStack stack) {
        ItemEntity entity = new ItemEntity(world, x, y, z, stack);
        if (BlockDropRng.isActive(world)) {
            BlockDropRng.applyStandardMotion(entity, BlockDropRng.seedOf(world), new BlockPos(x, y, z));
        }
        return entity;
    }
}
