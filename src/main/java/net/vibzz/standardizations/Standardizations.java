package net.vibzz.standardizations;

import net.fabricmc.api.ModInitializer;
import net.minecraft.world.GameRules;

public class Standardizations implements ModInitializer {

    public static GameRules.Key<GameRules.BooleanRule> STANDARDIZE_BLOCK_DROPS;
    public static GameRules.Key<GameRules.BooleanRule> STANDARDIZE_EXPLOSIONS;
    public static GameRules.Key<GameRules.BooleanRule> STANDARDIZE_SILVERFISH_SPAWNER;
    public static GameRules.Key<GameRules.BooleanRule> STANDARDIZE_ENDER_EYE;
    public static GameRules.Key<GameRules.BooleanRule> STANDARDIZE_THROWABLES;
    public static GameRules.Key<GameRules.BooleanRule> STANDARDIZE_VILLAGER_TRADES;
    public static GameRules.Key<GameRules.BooleanRule> STANDARDIZE_INITIAL_SPAWNS;
    public static GameRules.Key<GameRules.BooleanRule> STANDARDIZE_GLOWSTONE_DROPS;
    public static GameRules.Key<GameRules.BooleanRule> STANDARDIZE_CROP_DROPS;
    public static GameRules.Key<GameRules.BooleanRule> STANDARDIZE_ORE_DROPS;
    public static GameRules.Key<GameRules.BooleanRule> STANDARDIZE_DROPS_BY_XYZ;
    public static GameRules.Key<GameRules.BooleanRule> STANDARDIZE_DEBUG_NAMETAGS;

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
        STANDARDIZE_VILLAGER_TRADES = GameRules.register(
                "standardizeVillagerTrades",
                GameRules.Category.MISC,
                GameRules.BooleanRule.create(true)
        );
        STANDARDIZE_INITIAL_SPAWNS = GameRules.register(
                "standardizeInitialSpawns",
                GameRules.Category.MISC,
                GameRules.BooleanRule.create(true)
        );
        STANDARDIZE_GLOWSTONE_DROPS = GameRules.register(
                "standardizeGlowstoneDrops",
                GameRules.Category.MISC,
                GameRules.BooleanRule.create(true)
        );
        STANDARDIZE_CROP_DROPS = GameRules.register(
                "standardizeCropDrops",
                GameRules.Category.MISC,
                GameRules.BooleanRule.create(true)
        );
        STANDARDIZE_ORE_DROPS = GameRules.register(
                "standardizeOreDrops",
                GameRules.Category.MISC,
                GameRules.BooleanRule.create(true)
        );
        STANDARDIZE_DROPS_BY_XYZ = GameRules.register(
                "standardizeDropsByXyz",
                GameRules.Category.MISC,
                GameRules.BooleanRule.create(false)
        );
        STANDARDIZE_DEBUG_NAMETAGS = GameRules.register(
                "standardizeDebugNametags",
                GameRules.Category.MISC,
                GameRules.BooleanRule.create(false)
        );
    }
}
