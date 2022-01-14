package fuzs.completionistsindex.data;

import fuzs.completionistsindex.CompletionistsIndex;
import fuzs.completionistsindex.registry.ModRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;

import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider {
    public ModRecipeProvider(DataGenerator p_125973_) {
        super(p_125973_);
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> p_176532_) {
        ShapedRecipeBuilder.shaped(ModRegistry.INVISIBLE_WINGS_ITEM.get())
                .group("invisible_wings")
                .define('S', Items.NETHER_STAR)
                .define('G', Items.GOLD_INGOT)
                .define('P', Items.PHANTOM_MEMBRANE)
                .define('F', Items.FEATHER)
                .pattern("FGP")
                .pattern("GSG")
                .pattern("PGF")
                .unlockedBy("has_nether_star", has(Items.NETHER_STAR))
                .save(p_176532_);
        ShapedRecipeBuilder.shaped(ModRegistry.INVISIBLE_WINGS_ITEM.get())
                .group("invisible_wings")
                .define('S', Items.NETHER_STAR)
                .define('G', Items.GOLD_INGOT)
                .define('P', Items.PHANTOM_MEMBRANE)
                .define('F', Items.FEATHER)
                .pattern("PGF")
                .pattern("GSG")
                .pattern("FGP")
                .unlockedBy("has_nether_star", has(Items.NETHER_STAR))
                .save(p_176532_, new ResourceLocation(CompletionistsIndex.MOD_ID, "invisible_wings_mirrored"));
    }
}
