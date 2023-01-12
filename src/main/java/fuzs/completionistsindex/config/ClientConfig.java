package fuzs.completionistsindex.config;

import fuzs.puzzleslib.config.AbstractConfig;
import fuzs.puzzleslib.config.annotation.Config;

public class ClientConfig extends AbstractConfig {
    @Config(description = {"Modifier key required to be held to allow for cycling slots by scrolling.", "Setting to \"NONE\" will overwrite hotbar scrolling, making the hotbar only usable with number keys."})
    public ModifierKey scrollingModifierKey = ModifierKey.ALT;
    @Config(description = "Render rings of flight when equipped in offhand. A simply convenience feature to free up valuable screen estate.")
    public boolean renderOffhandRing = false;
    @Config(description = "Show endurance bar for flying all the time, even when it is full.")
    public boolean alwaysRenderEnduranceBar = false;
    @Config(description = "Skip showing the mods screen when only Minecraft is present (meaning there are no other mods that add any items, like when playing on a vanilla server, or all other items are blacklisted).")
    public boolean skipSingleModScreen = false;

    public ClientConfig() {
        super("");
    }

    public enum ModifierKey {
        NONE, CONTROL, SHIFT, ALT
    }
}
