package net.vibzz.standardizations;

import net.fabricmc.api.ModInitializer;
import net.minecraft.world.GameRules;

public class Standardizations implements ModInitializer {

    public static GameRules.Key<GameRules.BooleanRule> STANDARDIZE_BLOCK_DROPS;
    public static GameRules.Key<GameRules.BooleanRule> STANDARDIZE_EXPLOSIONS;
    public static GameRules.Key<GameRules.BooleanRule> STANDARDIZE_SILVERFISH_SPAWNER;
    public static GameRules.Key<GameRules.BooleanRule> STANDARDIZE_ENDER_EYE;
    public static GameRules.Key<GameRules.BooleanRule> STANDARDIZE_THROWABLES;

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
        STANDARDIZE_SILVERFISH_SPAWNER = GameRules.register(
                "standardizeSilverfishSpawner",
                GameRules.Category.MISC,
                GameRules.BooleanRule.create(true)
        );
        STANDARDIZE_ENDER_EYE = GameRules.register(
                "standardizeEnderEye",
                GameRules.Category.MISC,
                GameRules.BooleanRule.create(true)
        );
        STANDARDIZE_THROWABLES = GameRules.register(
                "standardizeThrowables",
                GameRules.Category.MISC,
                GameRules.BooleanRule.create(true)
        );
    }
}
