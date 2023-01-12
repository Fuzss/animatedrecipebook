package fuzs.completionistsindex.client.gui.screens.inventory;

import com.google.common.collect.Ordering;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

import java.util.Comparator;

public enum StatsSorting {
    CREATIVE("creative"), ALPHABETICALLY("alphabetically"), COLLECTED("collected");

    public final Component component;

    StatsSorting(String translationId) {
        this.component = new TranslatableComponent("completionistsindex.gui.index.sorting." + translationId);
    }

    public StatsSorting cycle() {
        StatsSorting[] values = StatsSorting.values();
        return values[(this.ordinal() + 1) % values.length];
    }

    public Comparator<IndexViewScreen.Page.StatsItemEntry> comparing() {
        return switch (this) {
            case CREATIVE -> Ordering.allEqual()::compare;
            case ALPHABETICALLY -> Comparator.comparing(entry -> Registry.ITEM.getKey(entry.getItem().getItem()).getPath());
            case COLLECTED -> Comparator.comparing(IndexViewScreen.Page.StatsItemEntry::isCollected).reversed().thenComparing(entry -> Registry.ITEM.getKey(entry.getItem().getItem()).getPath());
        };
    }
}
