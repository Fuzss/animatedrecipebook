package com.fuzs.animatedrecipebook;

import com.fuzs.animatedrecipebook.gui.widget.button.RecipeBookButton;
import com.fuzs.animatedrecipebook.mixin.client.accessor.IImageButtonAccessor;
import net.minecraft.client.gui.recipebook.IRecipeShownListener;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.FMLNetworkConstants;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SuppressWarnings("unused")
@Mod(AnimatedRecipeButton.MODID)
public class AnimatedRecipeButton {

    public static final String MODID = "animatedrecipebook";
    public static final String NAME = "Animated Recipe Book";
    public static final Logger LOGGER = LogManager.getLogger(AnimatedRecipeButton.NAME);

    private static final ResourceLocation RECIPE_BUTTON_TEXTURE = new ResourceLocation("textures/gui/recipe_button.png");

    public AnimatedRecipeButton() {

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onClientSetup);

        // clientSideOnly = true
        ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.DISPLAYTEST, () -> Pair.of(() -> FMLNetworkConstants.IGNORESERVERONLY, (remote, isServer) -> true));
    }

    private void onClientSetup(final FMLClientSetupEvent evt) {

        MinecraftForge.EVENT_BUS.addListener(this::onInitGui);
    }

    private void onInitGui(final GuiScreenEvent.InitGuiEvent.Post evt) {

        if (evt.getGui() instanceof IRecipeShownListener) {

            evt.getWidgetList().stream()
                    .filter(widget -> widget instanceof ImageButton)
                    .filter(widget -> ((IImageButtonAccessor) widget).getResourceLocation().equals(RECIPE_BUTTON_TEXTURE))
                    .findFirst()
                    .ifPresent(widget -> {

                evt.removeWidget(widget);
                evt.addWidget(new RecipeBookButton((ImageButton) widget, (IRecipeShownListener) evt.getGui()));
            });
        }
    }

}
