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

    @Config(description = "Cooldown in ticks after using a portable hole.")
    @Config.IntRange(min = 0, max = 72000)
    public int portableHoleCooldown = 40;
    @Config(description = "How deep does the hole go created by a portable hole.")
    @Config.IntRange(min = 1)
    public int temporaryHoleDepth = 12;
    @Config(description = "For how long does a temporary hole stay open.")
    @Config.IntRange(min = 0)
    public int temporaryHoleDuration = 100;
    @Config(description = {"Maximum block hardness a temporary hole can replace.", "Check out the Minecraft Wiki for more information: https://minecraft.fandom.com/wiki/Breaking#Blocks_by_hardness"})
    public double maxBlockHardness = 20.0;
    @Config(description = "Render spark particles to indicate the block replaced by a temporary hole. May want to disable this feature to increase performance when a large hole depth is set.")
    public boolean sparkParticles = true;

    public ServerConfig() {
        super("");
    }
}
