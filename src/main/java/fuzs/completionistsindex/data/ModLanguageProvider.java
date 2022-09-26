package fuzs.completionistsindex.data;

import fuzs.completionistsindex.registry.ModRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;

public class ModLanguageProvider extends LanguageProvider {

    public ModLanguageProvider(DataGenerator gen, String modId) {
        super(gen, modId, "en_us");
    }

    @Override
    protected void addTranslations() {
        this.add(ModRegistry.PORTABLE_HOLE_ITEM.get(), "Portable Hole");
        this.add("item.completionistsindex.portable_hole.description", "Click on a block and see what happens!");
        this.add(ModRegistry.BUTTERFlY_WINGS_ITEM.get(), "Butterfly Ring of Flight");
        this.add(ModRegistry.CHICKEN_WINGS_ITEM.get(), "Chicken Ring of Flight");
        this.add(ModRegistry.DRAGON_WINGS_ITEM.get(), "Dragon Ring of Flight");
        this.add(ModRegistry.FAIRY_WINGS_ITEM.get(), "Fairy Ring of Flight");
        this.add(ModRegistry.ANGEL_WINGS_ITEM.get(), "Angel Ring of Flight");
        this.add(ModRegistry.INVISIBLE_WINGS_ITEM.get(), "Ring of Flight");
        this.add(ModRegistry.AERIAL_AFFINITY_ENCHANTMENT.get(), "Aerial Affinity");
        this.add(ModRegistry.ENDURANCE_ENCHANTMENT.get(), "Endurance");
        this.add("key.cycleLeft", "Cycle Hotbar Slot Left");
        this.add("key.cycleRight", "Cycle Hotbar Slot Right");
        this.add("completionistsindex.tooltip.slot", "Slot: %s");
        this.add("completionistsindex.tooltip.offhand", "Offhand");
        this.add("completionistsindex.advancements.get_wings.title", "Rings Of Flight");
        this.add("completionistsindex.advancements.get_wings.description", "Construct a ring of flight");
        this.add("completionistsindex.advancements.angle_wings.title", "Like An Angle");
    }
}
