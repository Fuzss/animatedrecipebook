package com.fuzs.animatedrecipebook.mixin.client.accessor;

import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ImageButton.class)
public interface IImageButtonAccessor {

    @Accessor
    ResourceLocation getResourceLocation();

}
