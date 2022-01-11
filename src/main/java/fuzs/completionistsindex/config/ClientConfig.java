package fuzs.completionistsindex.config;

import fuzs.puzzleslib.config.AbstractConfig;
import fuzs.puzzleslib.config.annotation.Config;

public class ClientConfig extends AbstractConfig {
    @Config(description = {"Modifier key required to be held to allow for cycling slots by scrolling.", "Setting to \"NONE\" will overwrite hotbar scrolling, making the hotbar only usable with number keys."})
    public ModifierKey scrollingModifierKey = ModifierKey.ALT;

    public ClientConfig() {
        super("");
    }

    public enum ModifierKey {
        NONE, CONTROL, SHIFT, ALT
    }
}
