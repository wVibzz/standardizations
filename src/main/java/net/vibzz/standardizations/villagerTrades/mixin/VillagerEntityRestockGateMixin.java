package net.vibzz.standardizations.villagerTrades.mixin;

import net.minecraft.entity.passive.VillagerEntity;
import net.vibzz.standardizations.villagerTrades.VillagerTradeStandardization;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(VillagerEntity.class)
public abstract class VillagerEntityRestockGateMixin {

    @Inject(method = "canRestock", at = @At("HEAD"), cancellable = true)
    private void standardizations$blockBrainFill(CallbackInfoReturnable<Boolean> cir) {
        VillagerEntity self = (VillagerEntity) (Object) this;
        if (!VillagerTradeStandardization.isActive(self.world)) return;
        if (((AbstractTraderEntityAccessor) self).standardizations$getOffersField() == null) {
            cir.setReturnValue(false);
        }
    }
}
