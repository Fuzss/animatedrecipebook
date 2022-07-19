package fuzs.completionistsindex.client.registry;

import fuzs.completionistsindex.CompletionistsIndex;
import fuzs.puzzleslib.client.model.geom.ModelLayerRegistry;
import net.minecraft.client.model.geom.ModelLayerLocation;

public class ModClientRegistry {
    private static final ModelLayerRegistry LAYER_REGISTRY = ModelLayerRegistry.of(CompletionistsIndex.MOD_ID);
    public static final ModelLayerLocation WINGS = LAYER_REGISTRY.register("wings");
    public static final ModelLayerLocation TELEPORT_RAY = LAYER_REGISTRY.register("teleport_ray");
}
