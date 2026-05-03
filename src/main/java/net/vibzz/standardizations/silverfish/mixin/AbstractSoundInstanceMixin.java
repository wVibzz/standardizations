package net.vibzz.standardizations.silverfish.mixin;

import net.minecraft.client.sound.AbstractSoundInstance;
import net.minecraft.client.sound.EntityTrackingSoundInstance;
import net.minecraft.client.sound.Sound;
import net.minecraft.client.sound.SoundContainer;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.client.sound.WeightedSoundSet;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.UUID;

@Mixin(AbstractSoundInstance.class)
public abstract class AbstractSoundInstanceMixin {

    @Shadow protected Sound sound;
    @Shadow protected Identifier id;

    @Inject(method = "getSoundSet", at = @At("RETURN"))
    private void standardizations$deterministicVariant(SoundManager manager, CallbackInfoReturnable<WeightedSoundSet> cir) {
        WeightedSoundSet set = cir.getReturnValue();
        if (set == null || this.id == null) return;
        if (!standardizations$shouldStandardize(this.id)) return;

        Object self = this;
        if (!(self instanceof EntityTrackingSoundInstance)) return;
        Entity entity = ((EntityTrackingSoundInstanceAccessor) self).getEntity();
        if (entity == null) return;

        List<SoundContainer<Sound>> sounds = ((WeightedSoundSetAccessor) set).getSounds();
        if (sounds.isEmpty()) return;
        int totalWeight = set.getWeight();
        if (totalWeight <= 0) return;

        UUID uuid = entity.getUuid();
        long key = standardizations$mix(
                uuid.getMostSignificantBits()
                        ^ (uuid.getLeastSignificantBits() * 0x9E3779B97F4A7C15L)
                        ^ ((long) this.id.hashCode() * 0xBF58476D1CE4E5B9L)
                        ^ ((long) entity.age * 0x94D049BB133111EBL));

        int pick = (int) Math.floorMod(key, totalWeight);
        for (SoundContainer<Sound> container : sounds) {
            pick -= container.getWeight();
            if (pick < 0) {
                this.sound = container.getSound();
                return;
            }
        }
    }

    @Unique
    private static boolean standardizations$shouldStandardize(Identifier id) {
        return id.getPath().startsWith("entity.silverfish.");
    }

    @Unique
    private static long standardizations$mix(long z) {
        z = (z ^ (z >>> 30)) * 0xBF58476D1CE4E5B9L;
        z = (z ^ (z >>> 27)) * 0x94D049BB133111EBL;
        return z ^ (z >>> 31);
    }
}
