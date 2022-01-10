package fuzs.completionistsindex.client.util;

import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;

public class ScreenUtil {
    public static FormattedText getTruncatedText(Font font, Component component, int maxWidth, Style style) {
        // trim component when too long
        if (isTextTooLong(font, component, maxWidth)) {
            return FormattedText.composite(font.getSplitter().headByWidth(component, maxWidth - font.width("..."), style), FormattedText.of("..."));
        } else {
            return component;
        }
    }

    public static boolean isTextTooLong(Font font, Component component, int maxWidth) {
        return font.width(component) > maxWidth;
    }
}
