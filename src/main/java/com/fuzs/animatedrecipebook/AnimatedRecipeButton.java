package com.fuzs.animatedrecipebook;

import com.fuzs.animatedrecipebook.gui.widget.button.RecipeBookButton;
import net.minecraft.client.gui.recipebook.IRecipeShownListener;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SuppressWarnings("unused")
@Mod(AnimatedRecipeButton.MODID)
public class AnimatedRecipeButton {

    public static final String MODID = "animatedrecipebook";
    public static final String NAME = "Animated Recipe Book";
    public static final Logger LOGGER = LogManager.getLogger(AnimatedRecipeButton.NAME);

    public AnimatedRecipeButton() {

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onClientSetup);
    }

    private void onClientSetup(final FMLClientSetupEvent evt) {

        MinecraftForge.EVENT_BUS.addListener(this::onInitGui);
    }

    private void onInitGui(final GuiScreenEvent.InitGuiEvent.Post evt) {

        if (evt.getGui() instanceof IRecipeShownListener) {

            evt.getWidgetList().stream().filter(widget -> widget instanceof ImageButton).findFirst().ifPresent(widget -> {

                evt.removeWidget(widget);
                evt.addWidget(new RecipeBookButton((ImageButton) widget, (IRecipeShownListener) evt.getGui()));
            });
        }
    }

}
