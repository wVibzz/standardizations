package net.vibzz.standardizations.villagerTrades.mixin;

import net.minecraft.entity.passive.AbstractTraderEntity;
import net.minecraft.village.TraderOfferList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AbstractTraderEntity.class)
public interface AbstractTraderEntityAccessor {
    @Accessor("offers")
    TraderOfferList standardizations$getOffersField();
}
