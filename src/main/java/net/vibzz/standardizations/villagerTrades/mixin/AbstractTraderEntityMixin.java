package net.vibzz.standardizations.villagerTrades.mixin;

import net.minecraft.entity.passive.AbstractTraderEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOffers;
import net.minecraft.village.TraderOfferList;
import net.minecraft.village.VillagerData;
import net.vibzz.standardizations.villagerTrades.VillagerTradeCounters;
import net.vibzz.standardizations.villagerTrades.VillagerTradeStandardization;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

@Mixin(AbstractTraderEntity.class)
public abstract class AbstractTraderEntityMixin {

    @Inject(method = "fillRecipesFromPool", at = @At("HEAD"), cancellable = true)
    private void standardizations$globalCounterFill(TraderOfferList recipeList, TradeOffers.Factory[] pool, int count, CallbackInfo ci) {
        AbstractTraderEntity self = (AbstractTraderEntity) (Object) this;
        if (!VillagerTradeStandardization.isActive(self.world)) return;
        if (!(self instanceof VillagerEntity)) return;

        VillagerData data = ((VillagerEntity) self).getVillagerData();
        Identifier profId = Registry.VILLAGER_PROFESSION.getId(data.getProfession());
        int level = data.getLevel();

        VillagerTradeCounters counters = VillagerTradeCounters.get((ServerWorld) self.world);
        int idx = counters.getAndIncrement(profId, level);

        Random rng = VillagerTradeStandardization.rollRandom(
                VillagerTradeStandardization.seedOf(self.world), profId, level, idx);

        Set<Integer> set = new HashSet<>();
        if (pool.length > count) {
            while (set.size() < count) {
                set.add(rng.nextInt(pool.length));
            }
        } else {
            for (int i = 0; i < pool.length; i++) {
                set.add(i);
            }
        }

        for (Integer integer : set) {
            TradeOffer offer = pool[integer].create(self, rng);
            if (offer != null) {
                recipeList.add(offer);
            }
        }
        ci.cancel();
    }
}
