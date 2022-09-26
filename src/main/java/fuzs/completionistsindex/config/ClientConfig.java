package fuzs.completionistsindex.config;

import fuzs.puzzleslib.config.AbstractConfig;
import fuzs.puzzleslib.config.annotation.Config;

public class ClientConfig extends AbstractConfig {
    @Config(description = "Render rings of flight when equipped in offhand. A simply convenience feature to free up valuable screen estate.")
    public boolean renderOffhandRing = false;
    @Config(description = "Show endurance bar for flying all the time, even when it is full.")
    public boolean alwaysRenderEnduranceBar = false;

    public ClientConfig() {
        super("");
    }
}
