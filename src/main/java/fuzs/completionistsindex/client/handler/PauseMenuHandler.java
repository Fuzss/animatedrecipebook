package fuzs.completionistsindex.client.handler;

import fuzs.completionistsindex.CompletionistsIndex;
import fuzs.completionistsindex.client.gui.screens.inventory.ModsIndexViewScreen;
import fuzs.completionistsindex.client.gui.widget.AnimatedIconButton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;
import java.util.Optional;

public class PauseMenuHandler {
    private static final ResourceLocation BOOK_BUTTON_LOCATION = new ResourceLocation(CompletionistsIndex.MOD_ID, "textures/gui/animated_book.png");

    private AnimatedIconButton indexButton;

    @SubscribeEvent
    public void onInitGui(final ScreenEvent.InitScreenEvent.Post evt) {
        Screen screen = evt.getScreen();
        if (screen instanceof PauseScreen) {
            int buttonX = screen.width / 2 + 4 + 98 + 4;
            int buttonY = screen.height / 4 + 48 + -16;
            String[] vanillaButtons = {"gui.stats", "menu.returnToGame", "menu.reportBugs", "menu.shareToLan"};
            for (String translationKey : vanillaButtons) {
                final Optional<Button> menuButton = this.getButton(evt.getListenersList(), translationKey);
                if (menuButton.isPresent()) {
                    final Button otherButton = menuButton.get();
                    buttonX = otherButton.x + otherButton.getWidth() + 4;
                    buttonY = otherButton.y;
                    break;
                }
            }
            this.indexButton = new AnimatedIconButton(buttonX, buttonY, 20, 20, 0, 0, BOOK_BUTTON_LOCATION, button -> {
                screen.getMinecraft().setScreen(new ModsIndexViewScreen(screen));
            }, Button.NO_TOOLTIP).setAnimationData(8, 2);
            evt.addListener(this.indexButton);
        }
    }

    @SubscribeEvent
    public void onClientTick(final TickEvent.ClientTickEvent evt) {
        if (evt.phase != TickEvent.Phase.END) return;
        final Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.screen instanceof PauseScreen && this.indexButton != null) {
            this.indexButton.tick();
        }
    }

    private Optional<Button> getButton(List<GuiEventListener> widgets, String s) {
        for (GuiEventListener widget : widgets) {
            if (widget instanceof Button button && this.containsKey(button, s)) {
                return Optional.of(button);
            }
        }
        return Optional.empty();
    }

    private boolean containsKey(Button button, String key) {
        final Component message = button.getMessage();
        return message instanceof TranslatableComponent && ((TranslatableComponent) message).getKey().equals(key);
    }
}