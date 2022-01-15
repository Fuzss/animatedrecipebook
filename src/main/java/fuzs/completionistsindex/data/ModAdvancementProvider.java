package fuzs.completionistsindex.data;

import fuzs.completionistsindex.CompletionistsIndex;
import fuzs.completionistsindex.registry.ModRegistry;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.advancements.AdvancementProvider;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.function.Consumer;

public class ModAdvancementProvider extends AdvancementProvider {
    public ModAdvancementProvider(DataGenerator p_123966_, ExistingFileHelper fileHelperIn) {
        super(p_123966_, fileHelperIn);
    }

    @Override
    protected void registerAdvancements(Consumer<Advancement> consumer, ExistingFileHelper fileHelper) {
        Advancement advancement = Advancement.Builder.advancement()
                .display(ModRegistry.INVISIBLE_WINGS_ITEM.get(), new TranslatableComponent("completionistsindex.advancements.get_wings.title"), new TranslatableComponent("completionistsindex.advancements.get_wings.description"), new ResourceLocation(CompletionistsIndex.MOD_ID, "textures/gui/advancements/backgrounds/gold_block.png"), FrameType.TASK, true, true, false)
                .addCriterion("get_wings", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(ModRegistry.WINGS_TAG).build()))
                .build(new ResourceLocation(CompletionistsIndex.MOD_ID, "get_wings"));
        consumer.accept(advancement);
    }
}
