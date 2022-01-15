package fuzs.completionistsindex.config;

import fuzs.puzzleslib.config.AbstractConfig;
import fuzs.puzzleslib.config.annotation.Config;

public class ServerConfig extends AbstractConfig {
    @Config(description = "Enable this to allow for unlimited creative mode flight when using a ring of flight. The whole endurance system will be disabled.")
    public boolean unlimitedFlight = false;
    @Config(description = {"How many seconds flying is possible for.", "Every level of the endurance enchantment on a ring of flight item will add this value to the base."})
    @Config.IntRange(min = 1)
    public int flightSeconds = 60;
    @Config(description = "Multiplier for how fast endurance (the flight bar) recovers when on the ground.")
    public double recoverEnduranceMultiplier = 4.0;
    @Config(description = "Flight speed multiplier every level of the flight speed enchantment adds.")
    @Config.DoubleRange(min = 0.0, max = 0.5)
    public double flightSpeedSteps = 0.25;
    public boolean damageReducesEndurance = true;

    public ServerConfig() {
        super("");
    }
}
