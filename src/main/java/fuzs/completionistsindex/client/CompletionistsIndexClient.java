package fuzs.completionistsindex.client;

import fuzs.completionistsindex.CompletionistsIndex;
import fuzs.completionistsindex.client.handler.FirstPersonWingsHandler;
import fuzs.completionistsindex.client.init.ModClientRegistry;
import fuzs.completionistsindex.client.model.WingsModel;
import fuzs.completionistsindex.client.renderer.entity.layers.WingsLayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;

import java.util.Objects;

@Mod.EventBusSubscriber(modid = CompletionistsIndex.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class CompletionistsIndexClient {

    @SubscribeEvent
    public static void onConstructMod(final FMLConstructModEvent evt) {
        registerHandlers();
    }

    private static void registerHandlers() {
        MinecraftForge.EVENT_BUS.addListener(FirstPersonWingsHandler::onRenderHand);
    }

    @SubscribeEvent
    public static void onClientSetup(final FMLClientSetupEvent evt) {
        FirstPersonWingsHandler.init();
    }

    @SubscribeEvent
    public static void onAddLayers(final EntityRenderersEvent.AddLayers evt) {
        final Minecraft minecraft = Minecraft.getInstance();
        // add wings rendering layer to all player renderers
        evt.getSkins().stream()
                .map(evt::getSkin)
                .filter(Objects::nonNull)
                .map(e -> ((LivingEntityRenderer<?, ?>) e))
                .forEach(e -> {
                    e.addLayer(new WingsLayer<>(e, minecraft.getEntityModels()));
                });
    }

    @SubscribeEvent
    public static void onRegisterLayerDefinitions(final EntityRenderersEvent.RegisterLayerDefinitions evt) {
        evt.registerLayerDefinition(ModClientRegistry.WINGS, WingsModel::createLayer);
    }
}
