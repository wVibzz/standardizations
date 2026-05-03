package net.vibzz.standardizations.enderEye.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EyeOfEnderEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.EnderEyeItem;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.vibzz.standardizations.enderEye.EnderEyeStandardization;
import net.vibzz.standardizations.silverfish.mixin.EntityUuidAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.UUID;

@Mixin(EnderEyeItem.class)
public abstract class EnderEyeItemMixin {

    @Redirect(method = "use",
              at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z"))
    private boolean standardizations$assignAttemptUuid(World instance, Entity entity, World useWorld, PlayerEntity user, Hand hand) {
        if (entity instanceof EyeOfEnderEntity && EnderEyeStandardization.isActive(instance) && user instanceof ServerPlayerEntity) {
            int attempt = ((ServerPlayerEntity) user).getStatHandler()
                    .getStat(Stats.USED.getOrCreateStat(Items.ENDER_EYE));
            UUID uuid = EnderEyeStandardization.uuidForAttempt(EnderEyeStandardization.seedOf(instance), attempt);
            ((EntityUuidAccessor) entity).setUuidDirect(uuid);
            ((EntityUuidAccessor) entity).setUuidStringDirect(uuid.toString());
        }
        return instance.spawnEntity(entity);
    }
}
