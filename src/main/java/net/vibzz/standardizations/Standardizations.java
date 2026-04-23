package net.vibzz.standardizations;

import net.fabricmc.api.ModInitializer;
import net.minecraft.world.GameRules;

public class Standardizations implements ModInitializer {

    public static GameRules.Key<GameRules.BooleanRule> STANDARDIZE_BLOCK_DROPS;
    public static GameRules.Key<GameRules.BooleanRule> STANDARDIZE_EXPLOSIONS;

    @Override
    public void onInitialize() {
        STANDARDIZE_BLOCK_DROPS = GameRules.register(
                "standardizeBlockDrops",
                GameRules.Category.MISC,
                GameRules.BooleanRule.create(true)
        );
        STANDARDIZE_EXPLOSIONS = GameRules.register(
                "standardizeExplosions",
                GameRules.Category.MISC,
                GameRules.BooleanRule.create(true)
        );
    }
}
