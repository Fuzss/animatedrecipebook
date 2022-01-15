package fuzs.completionistsindex.client.handler;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import fuzs.completionistsindex.CompletionistsIndex;
import fuzs.completionistsindex.world.entity.EnduranceEntity;
import fuzs.completionistsindex.world.item.WingsItem;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.gui.ForgeIngameGui;

public class HudEnduranceRenderer {
    public static final ResourceLocation GUI_ICONS_LOCATION = new ResourceLocation(CompletionistsIndex.MOD_ID, "textures/gui/icons.png");

    private int lastEndurance;
    private int displayEndurance;
    private long lastEnduranceTime;
    private long enduranceBlinkTime;

    public void renderPlayerEndurance(Minecraft minecraft, ForgeIngameGui gui, PoseStack poseStack, int screenWidth, int screenHeight) {
        Player player = this.getCameraPlayer(minecraft);
        if (player == null) return;
        EnduranceEntity endurance = (EnduranceEntity) player;
        if (!(player.getOffhandItem().getItem() instanceof WingsItem) || !CompletionistsIndex.CONFIG.client().alwaysRenderEnduranceBar && endurance.isEnduranceFull()) return;
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, GUI_ICONS_LOCATION);
        minecraft.getProfiler().push(new ResourceLocation(CompletionistsIndex.MOD_ID, "endurance").toString());
        RenderSystem.enableBlend();
        int tickCount = gui.getGuiTicks();
        int playerEndurance = Mth.ceil(endurance.getEndurance() * 20.0F / endurance.getMaxEndurance());
        boolean blinking = this.enduranceBlinkTime > (long) tickCount && (this.enduranceBlinkTime - (long) tickCount) / 3L % 2L == 1L;
        long millis = Util.getMillis();
        if (playerEndurance < this.lastEndurance && player.invulnerableTime > 0) {
            this.lastEnduranceTime = millis;
            this.enduranceBlinkTime = tickCount + 20;
        } else if (playerEndurance > this.lastEndurance && player.invulnerableTime > 0) {
            this.lastEnduranceTime = millis;
            this.enduranceBlinkTime = tickCount + 10;
        }
        if (millis - this.lastEnduranceTime > 1000L) {
            this.displayEndurance = playerEndurance;
            this.lastEnduranceTime = millis;
        }
        this.lastEndurance = playerEndurance;
        int startX = screenWidth / 2 + 91;
        int startY = screenHeight - gui.right_height;
        gui.right_height += 10;
        this.renderWings(gui, poseStack, startX, startY, playerEndurance, this.displayEndurance, blinking);
        minecraft.getProfiler().pop();
        RenderSystem.disableBlend();
    }

    private void renderWings(Gui gui, PoseStack poseStack, int startX, int startY, int playerWings, int displayWings, boolean blinking) {
        for (int i = 0; i < 10; i++) {
            int posX = startX - i * 8 - 9;
            int posY = startY;
            this.renderWing(gui, poseStack, posX, posY, blinking ? 9 : 0, 0);
            int j2 = i * 2;
            if (blinking && j2 < displayWings) {
                boolean halfHeart = j2 + 1 == displayWings;
                this.renderWing(gui, poseStack, posX, posY, 36 + (halfHeart ? 9 : 0), 0);
            }

            if (j2 < playerWings) {
                boolean halfHeart = j2 + 1 == playerWings;
                this.renderWing(gui, poseStack, posX, posY, 18 + (halfHeart ? 9 : 0), 0);
            }
        }
    }

    private void renderWing(Gui gui, PoseStack poseStack, int posX, int posY, int textureX, int textureY) {
        gui.blit(poseStack, posX, posY, textureX, textureY, 9, 9);
    }

    private Player getCameraPlayer(Minecraft minecraft) {
        return !(minecraft.getCameraEntity() instanceof Player) ? null : (Player)minecraft.getCameraEntity();
    }
}
