package fuzs.completionistsindex.client.handler;

import fuzs.completionistsindex.client.gui.screens.inventory.IndexViewScreen;
import fuzs.completionistsindex.client.gui.screens.inventory.ModsIndexViewScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class IndexButtonHandler {
    @Nullable
    private static AbstractWidget recipeBookButton;
    @Nullable
    private static AbstractWidget collectorsLogButton;

    public static void onScreenInit$Post$1(Screen screen, Minecraft minecraft, int width, int height, List<Widget> renderables, Consumer<AbstractWidget> addWidget) {
        recipeBookButton = findRecipeBookButton(renderables);
        if (recipeBookButton == null) return;
        collectorsLogButton = new ImageButton(recipeBookButton.x + recipeBookButton.getWidth() + 8, recipeBookButton.y, 20, 18, 100, 198, 18, IndexViewScreen.INDEX_LOCATION, 512, 256, button -> {
            minecraft.setScreen(new ModsIndexViewScreen(screen));
        });
        addWidget.accept(collectorsLogButton);
    }

    private static AbstractWidget findRecipeBookButton(List<Widget> renderables) {
        for (Widget renderable : renderables) {
            if (renderable instanceof ImageButton imageButton) {
                return imageButton;
            }
        }
        return null;
    }

    public static void onMouseClicked$Post(Screen screen, double mouseX, double mouseY, int buttonId) {
        if (collectorsLogButton != null && recipeBookButton != null) {
            collectorsLogButton.x = recipeBookButton.x + recipeBookButton.getWidth() + 8;
            collectorsLogButton.y = recipeBookButton.y;
        }
    }

    public static void onScreenInit$Post$2(Screen screen, Minecraft minecraft, int width, int height, List<Widget> renderables, Consumer<AbstractWidget> addWidget) {
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
            minecraft.setScreen(new ModsIndexViewScreen(screen));
        }, TextComponent.EMPTY));
    }

    private static Optional<Button> getButton(List<Widget> renderables, String translationKey) {
        for (Widget widget : renderables) {
            if (widget instanceof Button button && matchesTranslationKey(button, translationKey)) {
                return Optional.of(button);
            }
        }
        return Optional.empty();
    }

    private static boolean matchesTranslationKey(Button button, String translationKey) {
        return button.getMessage() instanceof TranslatableComponent component && component.getKey().equals(translationKey);
    }
}
