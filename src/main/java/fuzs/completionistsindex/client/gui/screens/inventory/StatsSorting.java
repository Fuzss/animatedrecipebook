package fuzs.completionistsindex.client.gui.screens.inventory;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

public enum StatsSorting {
    CREATIVE("creative"), ALPHABETICALLY("alphabetically"), COMPLETED_FIRST("completed_first"), COMPLETED_LAST("completed_last");

    public final Component component;

    StatsSorting(String translationId) {
        this.component = new TranslatableComponent("completionistsindex.gui.index.sorting." + translationId);
    }

    public StatsSorting cycle() {
        StatsSorting[] values = StatsSorting.values();
        return values[(this.ordinal() + 1) % values.length];
    }
}
