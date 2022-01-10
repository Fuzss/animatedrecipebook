package fuzs.completionistsindex.config;

import fuzs.completionistsindex.client.handler.MouseScrollHandler;
import fuzs.puzzleslib.config.AbstractConfig;
import fuzs.puzzleslib.config.annotation.Config;

public class ClientConfig extends AbstractConfig {
    @Config(description = {"Modifier key required to be held to allow for cycling slots by scrolling.", "Setting to \"NONE\" will overwrite hotbar scrolling, making the hotbar only usable with number keys."})
    public MouseScrollHandler.ModifierKey scrollingModifierKey = MouseScrollHandler.ModifierKey.ALT;

    public ClientConfig() {
        super("");
    }
}
