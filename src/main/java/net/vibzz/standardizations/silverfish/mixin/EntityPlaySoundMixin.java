package net.vibzz.standardizations.silverfish.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Entity.class)
public abstract class EntityPlaySoundMixin {

    @Redirect(method = "playSound(Lnet/minecraft/sound/SoundEvent;FF)V",
              at = @At(value = "INVOKE",
                       target = "Lnet/minecraft/world/World;playSound(Lnet/minecraft/entity/player/PlayerEntity;DDDLnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;FF)V"))
    private void standardizations$entityTrackedSound(World world, PlayerEntity excluded, double x, double y, double z, SoundEvent sound, SoundCategory category, float volume, float pitch) {
        Entity self = (Entity) (Object) this;
        if (self instanceof LivingEntity) {
            world.playSoundFromEntity(excluded, self, sound, category, volume, pitch);
        } else {
            world.playSound(excluded, x, y, z, sound, category, volume, pitch);
        }
    }
}
