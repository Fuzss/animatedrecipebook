package fuzs.completionistsindex.client.handler;

import fuzs.completionistsindex.client.gui.screens.inventory.IndexViewScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class PauseMenuHandler {

    public static void onScreenInit$Post(Screen screen, Minecraft minecraft, int width, int height, List<Widget> renderables, Consumer<AbstractWidget> addWidget) {
        int buttonX = width / 2 + 4 + 98 + 4;
        int buttonY = height / 4 + 48 + -16;
        String[] vanillaButtons = {"gui.stats", "menu.returnToGame", "menu.reportBugs", "menu.shareToLan"};
        for (String buttonKey : vanillaButtons) {
            final Optional<Button> menuButton = getButton(renderables, buttonKey);
            if (menuButton.isPresent()) {
                final Button otherButton = menuButton.get();
                buttonX = otherButton.x + otherButton.getWidth() + 4;
                buttonY = otherButton.y;
                break;
            }
        }
        addWidget.accept(new ImageButton(buttonX, buttonY, 20, 20, 80, 198, 20, IndexViewScreen.INDEX_LOCATION, 512, 256, button -> {
            minecraft.setScreen(new IndexViewScreen(screen));
        }, TextComponent.EMPTY));
    }

    private static Optional<Button> getButton(List<Widget> renderables, String translationKey) {
        for (Widget widget : renderables) {
            if (widget instanceof Button button && containsKey(button, translationKey)) {
                return Optional.of(button);
            }
        }
        return Optional.empty();
    }

    private static boolean containsKey(Button button, String translationKey) {
        return button.getMessage() instanceof TranslatableComponent component && component.getKey().equals(translationKey);
    }
}
