package com.fuzs.animatedrecipebook.mixin.accessor;

import net.minecraft.client.gui.widget.button.Button;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Button.class)
public interface IButtonAccessor {

    @Accessor
    Button.IPressable getOnPress();

}
