package net.vibzz.standardizations.enderEye.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EyeOfEnderEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.vibzz.standardizations.enderEye.EnderEyeStandardization;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EyeOfEnderEntity.class)
public abstract class EyeOfEnderEntityMixin {

    @Redirect(method = "tick",
              at = @At(value = "NEW",
                       target = "(Lnet/minecraft/world/World;DDDLnet/minecraft/item/ItemStack;)Lnet/minecraft/entity/ItemEntity;"))
    private ItemEntity standardizations$deterministicEyeDrop(World world, double x, double y, double z, ItemStack stack) {
        ItemEntity entity = new ItemEntity(world, x, y, z, stack);
        if (EnderEyeStandardization.isActive(world)) {
            EnderEyeStandardization.applyStandardMotion(entity, ((Entity) (Object) this).getUuid());
        }
        return entity;
    }
}
