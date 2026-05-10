package net.vibzz.standardizations.mobSpawns.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundTag;
import net.vibzz.standardizations.mobSpawns.InitialSpawnMarker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityInitialSpawnFlagMixin implements InitialSpawnMarker {

    @Unique private static final String NBT_KEY = "StandardizationsInitialSpawn";

    @Unique private boolean standardizations$initialSpawn;

    @Override
    public boolean standardizations$isInitialSpawn() {
        return this.standardizations$initialSpawn;
    }

    @Override
    public void standardizations$setInitialSpawn(boolean value) {
        this.standardizations$initialSpawn = value;
    }

    @Inject(method = "toTag", at = @At("RETURN"))
    private void standardizations$writeInitialSpawnFlag(CompoundTag tag, CallbackInfoReturnable<CompoundTag> cir) {
        if (this.standardizations$initialSpawn) {
            cir.getReturnValue().putBoolean(NBT_KEY, true);
        }
    }

    @Inject(method = "fromTag", at = @At("RETURN"))
    private void standardizations$readInitialSpawnFlag(CompoundTag tag, CallbackInfo ci) {
        if (tag.contains(NBT_KEY)) {
            this.standardizations$initialSpawn = tag.getBoolean(NBT_KEY);
        }
    }
}
