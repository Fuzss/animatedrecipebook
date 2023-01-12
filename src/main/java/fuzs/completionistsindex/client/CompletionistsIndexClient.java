package fuzs.completionistsindex.client;

import fuzs.completionistsindex.CompletionistsIndex;
import fuzs.completionistsindex.client.handler.*;
import fuzs.completionistsindex.client.model.WingsModel;
import fuzs.completionistsindex.client.registry.ModClientRegistry;
import fuzs.completionistsindex.client.renderer.entity.layers.TeleportChargeEffectLayer;
import fuzs.completionistsindex.client.renderer.entity.layers.WingsLayer;
import fuzs.puzzleslib.PuzzlesLib;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ScreenEvent;
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
        PuzzlesLib.setSideOnly();
        registerHandlers();
    }

    @SubscribeEvent
    public static void onClientSetup(final FMLClientSetupEvent evt) {
        SlotRendererHandler.init();
        final KeyBindingHandler keyBindingHandler = new KeyBindingHandler();
        keyBindingHandler.init();
        MinecraftForge.EVENT_BUS.addListener(keyBindingHandler::onClientTick);
        final Minecraft minecraft = Minecraft.getInstance();
        final MouseScrollHandler mouseScrollHandler = new MouseScrollHandler(minecraft);
        MinecraftForge.EVENT_BUS.addListener(mouseScrollHandler::onMouseScroll);
        FirstPersonWingsHandler.init();
    }

    private static void registerHandlers() {
        MinecraftForge.EVENT_BUS.addListener((final ScreenEvent.InitScreenEvent.Post evt) -> {
            Screen screen = evt.getScreen();
            if (evt.getScreen() instanceof InventoryScreen) {
                IndexButtonHandler.onScreenInit$Post$1(screen, Minecraft.getInstance(), screen.width, screen.height, screen.renderables, evt::addListener);
            }
            if (evt.getScreen() instanceof PauseScreen) {
                IndexButtonHandler.onScreenInit$Post$2(screen, Minecraft.getInstance(), screen.width, screen.height, screen.renderables, evt::addListener);
            }
        });
        MinecraftForge.EVENT_BUS.addListener((final ScreenEvent.MouseClickedEvent.Post evt) -> {
            if (evt.getScreen() instanceof InventoryScreen && evt.wasHandled()) {
                IndexButtonHandler.onMouseClicked$Post(evt.getScreen(), evt.getMouseX(), evt.getMouseY(), evt.getButton());
            }
        });
//        final PauseMenuHandler pauseMenuHandler = new PauseMenuHandler();
//        MinecraftForge.EVENT_BUS.addListener(pauseMenuHandler::onClientTick);
//        MinecraftForge.EVENT_BUS.addListener(pauseMenuHandler::onInitGui);
        final FirstPersonWingsHandler firstPersonWingsHandler = new FirstPersonWingsHandler();
        MinecraftForge.EVENT_BUS.addListener(firstPersonWingsHandler::onRenderHand);
        final SweepingHandler sweepingHandler = new SweepingHandler();
        MinecraftForge.EVENT_BUS.addListener(sweepingHandler::onLeftClickEmpty);
        final AttributesTooltipHandler attributesTooltipHandler = new AttributesTooltipHandler();
        MinecraftForge.EVENT_BUS.addListener(attributesTooltipHandler::onItemTooltip);
        final AttackIndicatorOptionHandler attackIndicatorOptionHandler = new AttackIndicatorOptionHandler();
        MinecraftForge.EVENT_BUS.addListener(attackIndicatorOptionHandler::onScreenInit);
        MinecraftForge.EVENT_BUS.addListener(attackIndicatorOptionHandler::onDrawScreen);
        final ItemInHandHandler itemInHandHandler = new ItemInHandHandler();
        MinecraftForge.EVENT_BUS.addListener(itemInHandHandler::onRenderHand);
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
                    e.addLayer(new TeleportChargeEffectLayer<>(e, minecraft.getEntityModels()));
                });
    }

    @SubscribeEvent
    public static void onRegisterLayerDefinitions(final EntityRenderersEvent.RegisterLayerDefinitions evt) {
        evt.registerLayerDefinition(ModClientRegistry.WINGS, WingsModel::createLayer);
        evt.registerLayerDefinition(ModClientRegistry.TELEPORT_RAY, TeleportChargeEffectLayer::createLayer);
    }
}
