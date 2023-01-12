package fuzs.completionistsindex.client.gui.screens.inventory;

import com.google.common.collect.ImmutableMap;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.achievement.StatsUpdateListener;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.network.protocol.game.ServerboundClientCommandPacket;
import net.minecraft.stats.StatsCounter;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ModsIndexViewScreen extends IndexViewScreen implements StatsUpdateListener {
    private final Map<String, List<ItemStack>> allItemsByMod = getAllItemsByMod();
    private boolean isLoading = true;

    public ModsIndexViewScreen(Screen lastScreen) {
        super(lastScreen);
    }

    @Override
    public void onStatsUpdated() {
        if (!this.isLoading) return;
        this.rebuildPages();
    }

    @Override
    List<IndexViewPage.Entry> getPageEntries() {
        StatsCounter stats = this.minecraft.player.getStats();
        return this.allItemsByMod.entrySet().stream().map(entry -> {
            return IndexViewScreen.IndexViewPage.modItemEntry(entry.getKey(), entry.getValue(), stats, this.font);
        }).sorted(this.getComparator()).toList();
    }

    @Override
    protected void init() {
        this.isLoading = true;
        this.minecraft.getConnection().send(new ServerboundClientCommandPacket(ServerboundClientCommandPacket.Action.REQUEST_STATS));
        super.init();
    }

    private static Map<String, List<ItemStack>> getAllItemsByMod() {
        NonNullList<ItemStack> searchTabItems = NonNullList.create();
        CreativeModeTab.TAB_SEARCH.fillItemList(searchTabItems);
        Map<String, List<ItemStack>> allItems = searchTabItems.stream().map(ItemStack::getItem).distinct().collect(Collectors.groupingBy(item -> Registry.ITEM.getKey(item).getNamespace(), LinkedHashMap::new, Collectors.mapping(ItemStack::new, Collectors.toList())));
        return ImmutableMap.copyOf(allItems);
    }
}
