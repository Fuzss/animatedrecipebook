package fuzs.completionistsindex.data;

import fuzs.completionistsindex.init.ModRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;

import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider {

    public ModRecipeProvider(DataGenerator dataGenerator) {
        super(dataGenerator);
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> recipeConsumer) {
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
                .save(recipeConsumer);
    }
}
