package fuzs.completionistsindex.data;

import fuzs.completionistsindex.registry.ModRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

public class ModItemModelProvider extends ItemModelProvider {

    public ModItemModelProvider(DataGenerator generator, String modId, ExistingFileHelper existingFileHelper) {
        super(generator, modId, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        this.basicItem(ModRegistry.BUTTERFlY_WINGS_ITEM.get());
        this.basicItem(ModRegistry.CHICKEN_WINGS_ITEM.get());
        this.basicItem(ModRegistry.DRAGON_WINGS_ITEM.get());
        this.basicItem(ModRegistry.FAIRY_WINGS_ITEM.get());
        this.basicItem(ModRegistry.ANGEL_WINGS_ITEM.get());
        this.basicItem(ModRegistry.INVISIBLE_WINGS_ITEM.get());
        this.basicItem(ModRegistry.PORTABLE_HOLE_ITEM.get());
    }

    public ItemModelBuilder basicItem(Item item)
    {
        return basicItem(Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(item)));
    }

    public ItemModelBuilder basicItem(ResourceLocation item)
    {
        return getBuilder(item.toString())
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", new ResourceLocation(item.getNamespace(), "item/" + item.getPath()));
    }
}
