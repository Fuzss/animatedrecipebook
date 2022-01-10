package fuzs.completionistsindex.client.gui.widget;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.resources.ResourceLocation;

public class AnimatedIconButton extends IconButton {
    private final int origXTexStart;
    private int tickTime;
    private int frames;
    private int speed;
    private boolean cycling;

    public AnimatedIconButton(int x, int y, int width, int height, int xTexStart, int yTexStart, ResourceLocation resourceLocation, Button.OnPress onPress, Button.OnTooltip onTooltip) {
        super(x, y, width, height, xTexStart, yTexStart, resourceLocation, onPress, onTooltip);
        this.origXTexStart = xTexStart;
    }

    public AnimatedIconButton setAnimationData(int frameAmount, int speedInTicks) {
        if (frameAmount <= 0 || speedInTicks <= 0) throw new IllegalArgumentException("animation data must be greater than 0");
        this.frames = frameAmount;
        this.speed = speedInTicks;
        return this;
    }

    public AnimatedIconButton setCycling() {
        this.cycling = true;
        return this;
    }

    @Override
    public void setTexture(int textureX, int textureY) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected int getIconYImage(boolean hovered) {
        return 0;
    }

    public void tick() {
        if (this.cycling) {
            if (this.isHoveredOrFocused() || this.tickTime > 0) {
                this.tickTime++;
                this.tickTime %= this.frames * this.speed;
            }
        } else {
            if (this.isHoveredOrFocused()) {
                if (this.tickTime < this.frames * this.speed) {
                    this.tickTime++;
                }
            } else if (this.tickTime > 0) {
                this.tickTime--;
            }
        }
    }

    @Override
    public void renderButton(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        this.xTexStart = this.origXTexStart + (int) ((this.tickTime + partialTicks) / this.speed) * this.width;
        super.renderButton(poseStack, mouseX, mouseY, partialTicks);
    }
}