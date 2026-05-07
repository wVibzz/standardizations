package net.vibzz.standardizations.villagerTrades;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.world.PersistentState;

import java.util.HashMap;
import java.util.Map;

public class VillagerTradeCounters extends PersistentState {

    private static final String KEY = "standardizations_villager_trades";

    private final Map<String, Integer> counters = new HashMap<>();

    public VillagerTradeCounters() {
        super(KEY);
    }

    public static VillagerTradeCounters get(ServerWorld world) {
        return world.getServer().getOverworld().getPersistentStateManager()
                .getOrCreate(VillagerTradeCounters::new, KEY);
    }

    private static String keyFor(Identifier profession, int level) {
        return profession.toString() + "@" + level;
    }

    public int getAndIncrement(Identifier profession, int level) {
        String k = keyFor(profession, level);
        int v = counters.getOrDefault(k, 0);
        counters.put(k, v + 1);
        markDirty();
        return v;
    }

    @Override
    public void fromTag(CompoundTag tag) {
        counters.clear();
        for (String k : tag.getKeys()) {
            counters.put(k, tag.getInt(k));
        }
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        for (Map.Entry<String, Integer> e : counters.entrySet()) {
            tag.putInt(e.getKey(), e.getValue());
        }
        return tag;
    }
}
