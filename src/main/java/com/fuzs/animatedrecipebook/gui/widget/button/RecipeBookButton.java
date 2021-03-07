package com.fuzs.animatedrecipebook.gui.widget.button;

import com.fuzs.animatedrecipebook.AnimatedRecipeButton;
import com.fuzs.animatedrecipebook.mixin.client.accessor.IButtonAccessor;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.recipebook.IRecipeShownListener;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class RecipeBookButton extends ImageButton {

    private static final ResourceLocation BOOK_BUTTON = new ResourceLocation(AnimatedRecipeButton.MODID, "textures/gui/recipe_button.png");

    private final Minecraft mc = Minecraft.getInstance();

    private boolean isBookOpen;
    private float animationTicks;

    public RecipeBookButton(Button oldButton, IRecipeShownListener parent) {

        super(oldButton.x, oldButton.y, 20, 18, 0, 0, 18, BOOK_BUTTON, button -> {

            ((IButtonAccessor) oldButton).getOnPress().onPress(button);
            ((RecipeBookButton) button).isBookOpen = parent.getRecipeGui().isVisible();
            ((RecipeBookButton) button).animationTicks = !parent.getRecipeGui().isVisible() ? 9.0F : 0.0F;
        });

        this.isBookOpen = parent.getRecipeGui().isVisible();
    }

    @Override
    public void renderButton(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {

        if (this.visible) {

            if (this.isHovered()) {

                this.animationTicks = Math.min(9.0F - 1.0F, this.animationTicks + partialTicks);
            } else {

                this.animationTicks = Math.max(0.0F, this.animationTicks - partialTicks);
            }

            int posX = Math.round(this.animationTicks) * this.width;
            int posY = this.isBookOpen ? this.height : 0;

            this.mc.getTextureManager().bindTexture(BOOK_BUTTON);
            RenderSystem.enableDepthTest();
            // vanilla position usually isn't centered in relation to surroundings, + 1 will fix that
            blit(matrixStack, this.x + 1, this.y, posX, posY, this.width, this.height, 256, 256);
        }
    }

}