package net.vibzz.standardizations.silverfish.mixin;

import net.minecraft.client.sound.EntityTrackingSoundInstance;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EntityTrackingSoundInstance.class)
public interface EntityTrackingSoundInstanceAccessor {

    @Accessor("entity")
    Entity getEntity();
}
