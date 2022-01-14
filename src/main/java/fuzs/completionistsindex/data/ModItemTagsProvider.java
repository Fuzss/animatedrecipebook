package fuzs.completionistsindex.data;

import fuzs.completionistsindex.CompletionistsIndex;
import fuzs.completionistsindex.registry.ModRegistry;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.nio.file.Path;

public class ModItemTagsProvider extends TagsProvider<Item> {
    public ModItemTagsProvider(DataGenerator p_126546_, ExistingFileHelper fileHelperIn) {
        super(p_126546_, Registry.ITEM, CompletionistsIndex.MOD_ID, fileHelperIn);
    }

    @Override
    protected void addTags() {
        this.tag(ModRegistry.WINGS_TAG).add(ModRegistry.BUTTERFlY_WINGS_ITEM.get(), ModRegistry.CHICKEN_WINGS_ITEM.get(), ModRegistry.DRAGON_WINGS_ITEM.get(), ModRegistry.FAIRY_WINGS_ITEM.get(), ModRegistry.ANGEL_WINGS_ITEM.get(), ModRegistry.INVISIBLE_WINGS_ITEM.get());
    }

    @Override
    protected Path getPath(ResourceLocation p_126537_) {
        return this.generator.getOutputFolder().resolve("data/" + p_126537_.getNamespace() + "/tags/items/" + p_126537_.getPath() + ".json");
    }

    @Override
    public String getName() {
        return "Item Tags";
    }
}
