package net.vibzz.standardizations.silverfish.mixin;

import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.UUID;

@Mixin(Entity.class)
public interface EntityUuidAccessor {

    @Accessor("uuid")
    void setUuidDirect(UUID uuid);

    @Accessor("uuidString")
    void setUuidStringDirect(String uuidString);
}
