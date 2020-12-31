package com.fuzs.animatedrecipebook.mixin;

import com.fuzs.animatedrecipebook.AnimatedRecipeButton;
import org.spongepowered.asm.mixin.Mixins;
import org.spongepowered.asm.mixin.connect.IMixinConnector;

public class MixinConnector implements IMixinConnector {

    @Override
    public void connect() {

        Mixins.addConfiguration("META-INF/" + AnimatedRecipeButton.MODID + ".mixins.json");
    }

}
