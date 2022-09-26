package fuzs.completionistsindex.data;

import fuzs.completionistsindex.CompletionistsIndex;
import fuzs.completionistsindex.registry.ModRegistry;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModBlockTagsProvider extends TagsProvider<Block> {

    public ModBlockTagsProvider(DataGenerator p_126546_, ExistingFileHelper fileHelperIn) {
        super(p_126546_, Registry.BLOCK, CompletionistsIndex.MOD_ID, fileHelperIn);
    }

    @Override
    protected void addTags() {
        this.tag(ModRegistry.PORTABLE_HOLE_IMMUNE_TAG);
    }

    @Override
    public String getName() {
        return "Block Tags";
    }
}
