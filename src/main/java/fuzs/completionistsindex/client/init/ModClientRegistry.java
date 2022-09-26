package fuzs.completionistsindex.client.init;

import fuzs.completionistsindex.CompletionistsIndex;
import fuzs.puzzleslib.client.model.geom.ModelLayerRegistry;
import net.minecraft.client.model.geom.ModelLayerLocation;

public class ModClientRegistry {
    private static final ModelLayerRegistry REGISTRY = ModelLayerRegistry.of(CompletionistsIndex.MOD_ID);
    public static final ModelLayerLocation WINGS = REGISTRY.register("wings");
}
