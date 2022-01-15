package fuzs.completionistsindex.client.handler;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;

import java.util.Random;

public class HudEnduranceRenderer {
    protected final Random random = new Random();
    protected int lastHealth;
    protected int displayHealth;
    protected long lastHealthTime;
    protected long healthBlinkTime;

    public void renderPlayerHealth(Minecraft minecraft, Gui gui, PoseStack poseStack, int screenWidth, int screenHeight) {
        Player player = this.getCameraPlayer(minecraft);
        if (player == null) return;
        int tickCount = gui.getGuiTicks();
        int playerHealth = Mth.ceil(player.getHealth());
        boolean mayBlink = this.healthBlinkTime > (long) tickCount && (this.healthBlinkTime - (long) tickCount) / 3L % 2L == 1L;
        long millis = Util.getMillis();
        if (playerHealth < this.lastHealth && player.invulnerableTime > 0) {
            this.lastHealthTime = millis;
            this.healthBlinkTime = tickCount + 20;
        } else if (playerHealth > this.lastHealth && player.invulnerableTime > 0) {
            this.lastHealthTime = millis;
            this.healthBlinkTime = tickCount + 10;
        }

        if (millis - this.lastHealthTime > 1000L) {
            this.displayHealth = playerHealth;
            this.lastHealthTime = millis;
        }

        this.lastHealth = playerHealth;
        int displayHealth = this.displayHealth;
        this.random.setSeed(tickCount * 312871);
        int startX = screenWidth / 2 - 91;
        int startY = screenHeight - 39;
        float maxHealth = Math.max((float)player.getAttributeValue(Attributes.MAX_HEALTH), (float)Math.max(displayHealth, playerHealth));
        int playerAbsorption = Mth.ceil(player.getAbsorptionAmount());
        int totalHearts = Mth.ceil((maxHealth + (float)playerAbsorption) / 2.0F / 10.0F);
        int heartColumns = Math.max(10 - (totalHearts - 2), 3);
        int jumpOffsetY = -1;
        this.renderHearts(gui, poseStack, player, startX, startY, heartColumns, jumpOffsetY, maxHealth, playerHealth, displayHealth, playerAbsorption, mayBlink);
    }

    protected void renderHearts(Gui gui, PoseStack poseStack, Player player, int startX, int startY, int heartColumns, int heartJumpOffset, float maxHealth, int playerHealth, int displayHealth, int playerAbsorption, boolean blinking) {
        int normalHearts = Mth.ceil((double)maxHealth / 2.0D);
        int absorptionHearts = Mth.ceil((double)playerAbsorption / 2.0D);
        int l = normalHearts * 2;

        for (int i1 = normalHearts + absorptionHearts - 1; i1 >= 0; --i1) {
            int j1 = i1 / 10;
            int k1 = i1 % 10;
            int posX = startX + k1 * 8;
            int posY = startY - j1 * heartColumns;
            if (playerHealth + playerAbsorption <= 4) {
                posY += this.random.nextInt(2);
            }

            if (i1 < normalHearts && i1 == heartJumpOffset) {
                posY -= 2;
            }

            this.renderHeart(gui, poseStack, posX, posY, blinking ? 9 : 0, 0);
            int j2 = i1 * 2;
            boolean flag = i1 >= normalHearts;
            if (flag) {
                int k2 = j2 - l;
                if (k2 < playerAbsorption) {
                    boolean flag1 = k2 + 1 == playerAbsorption;
                    this.renderHeart(gui, poseStack, posX, posY, 0, 0);
                }
            }

            if (blinking && j2 < displayHealth) {
                boolean flag2 = j2 + 1 == displayHealth;
                this.renderHeart(gui, poseStack, posX, posY, 0, 0);
            }

            if (j2 < playerHealth) {
                boolean flag3 = j2 + 1 == playerHealth;
                this.renderHeart(gui, poseStack, posX, posY, 0, 0);
            }
        }

    }

    private void renderHeart(Gui gui, PoseStack poseStack, int posX, int posY, int textureX, int textureY) {
        gui.blit(poseStack, posX, posY, textureX, textureY, 9, 9);
    }

    private Player getCameraPlayer(Minecraft minecraft) {
        return !(minecraft.getCameraEntity() instanceof Player) ? null : (Player)minecraft.getCameraEntity();
    }
}
