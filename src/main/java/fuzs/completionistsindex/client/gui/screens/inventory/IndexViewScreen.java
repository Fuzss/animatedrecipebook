package fuzs.completionistsindex.client.gui.screens.inventory;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import fuzs.completionistsindex.CompletionistsIndex;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.achievement.StatsUpdateListener;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.NonNullList;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.*;
import net.minecraft.network.protocol.game.ServerboundClientCommandPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.Stats;
import net.minecraft.stats.StatsCounter;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class IndexViewScreen extends Screen implements StatsUpdateListener {
   public static final ResourceLocation INDEX_LOCATION = CompletionistsIndex.id("textures/gui/index.png");

   private final Screen lastScreen;
   private boolean isLoading = true;
   private int leftPos;
   private int topPos;
   private Button turnPageBackwards;
   private Button turnPageForwards;
   private StatsSorting statsSorting = StatsSorting.CREATIVE;
   private int currentPage;
   private Component leftPageIndicator;
   private Component rightPageIndicator;
   private List<Page> statsPages;

   public IndexViewScreen(Screen lastScreen) {
      super(new TranslatableComponent("gui.stats"));
      this.lastScreen = lastScreen;
   }

   @Override
   public void onStatsUpdated() {
      if (!this.isLoading) return;
      this.sortPages();
   }

   private void sortPages() {
      NonNullList<ItemStack> searchTabItems = NonNullList.create();
      CreativeModeTab.TAB_SEARCH.fillItemList(searchTabItems);
      StatsCounter stats = this.minecraft.player.getStats();
      List<Page.StatsItemEntry> entries = searchTabItems.stream().map(ItemStack::getItem).distinct().map(ItemStack::new).map(stack -> Page.StatsItemEntry.create(stack, stats, this.font)).sorted(this.statsSorting.comparing()).toList();
      this.statsPages = this.createPages(entries);
      this.setCurrentPage(0);
   }

   private List<Page> createPages(List<Page.StatsItemEntry> entries) {
      ImmutableList.Builder<Page> builder = ImmutableList.builder();
      Page page = null;
      int itemsCount = 0;
      for (Page.StatsItemEntry entry : entries) {
         if (page == null) {
            page = new Page();
            builder.add(page);
         }
         page.items[itemsCount] = entry;
         if (++itemsCount >= 14) {
            itemsCount = 0;
            page = null;
         }
      }
      return builder.build();
   }

   @Override
   protected void init() {
      this.leftPos = (this.width - 316) / 2;
      this.topPos = 2;
      this.isLoading = true;
      this.minecraft.getConnection().send(new ServerboundClientCommandPacket(ServerboundClientCommandPacket.Action.REQUEST_STATS));
      this.addRenderableWidget(new ImageButton(this.leftPos + 17, this.topPos + 11, 16, 13, 42, 202, 20, INDEX_LOCATION, 512, 256, button -> {
         this.onClose();
      }));
      this.addRenderableWidget(new ImageButton(this.leftPos + 316 - 17 - 16, this.topPos + 11, 16, 13, 62, 202, 20, INDEX_LOCATION, 512, 256, button -> {
         this.statsSorting = this.statsSorting.cycle();
         this.sortPages();
      }, (Button button, PoseStack poseStack, int mouseX, int mouseY) -> {
         this.renderTooltip(poseStack, this.statsSorting.component, mouseX, mouseY);
      }, TextComponent.EMPTY));
      this.turnPageBackwards = this.addRenderableWidget(new ImageButton(this.leftPos + 27, this.topPos + 173, 18, 10, 1, 203, 20, INDEX_LOCATION, 512, 256, button -> {
         this.decrementPage();
      }));
      this.turnPageForwards = this.addRenderableWidget(new ImageButton(this.leftPos + 316 - 27 - 18, this.topPos + 173, 18, 10, 21, 203, 20, INDEX_LOCATION, 512, 256, button -> {
         this.incrementPage();
      }));
      this.setCurrentPage(this.currentPage);
   }

   @Override
   public void render(PoseStack poseStack, int mouseX, int mouseY, float tickDelta) {
      this.renderBackground(poseStack);
      this.setFocused(null);
      RenderSystem.setShader(GameRenderer::getPositionTexShader);
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      RenderSystem.setShaderTexture(0, INDEX_LOCATION);
      blit(poseStack, this.leftPos, this.topPos, 0, 0, 316, 198, 512, 256);
      this.font.draw(poseStack, this.leftPageIndicator, this.leftPos + 82 - this.font.width(this.leftPageIndicator) / 2, 15, 0xB8A48A);
      this.font.draw(poseStack, this.rightPageIndicator, this.leftPos + 233 - this.font.width(this.rightPageIndicator) / 2, 15, 0xB8A48A);
      super.render(poseStack, mouseX, mouseY, tickDelta);
      if (this.statsPages != null) {
         this.statsPages.get(this.currentPage).render(poseStack, mouseX, mouseY, tickDelta);
      }
   }

   private void decrementPage() {
      if (this.currentPage > 0) this.setCurrentPage(this.currentPage - 1);
   }

   private void incrementPage() {
      if (this.currentPage < this.getAllPages() - 1) this.setCurrentPage(this.currentPage + 1);
   }

   private void setCurrentPage(int newPage) {
      this.currentPage = newPage;
      this.turnPageBackwards.visible = this.turnPageForwards.visible = true;
      if (newPage == 0) this.turnPageBackwards.visible = false;
      if (newPage >= this.getAllPages() - 1) this.turnPageForwards.visible = false;
      this.leftPageIndicator = new TranslatableComponent("book.pageIndicator", newPage * 2 + 1, this.getAllPages() * 2);
      this.rightPageIndicator = new TranslatableComponent("book.pageIndicator", newPage * 2 + 2, this.getAllPages() * 2);
   }

   private int getAllPages() {
      return this.statsPages != null ? this.statsPages.size() : 1;
   }

   @Override
   public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
      if (delta > 0.0) {
         this.decrementPage();
         return true;
      } else if (delta < 0.0) {
         this.incrementPage();
         return true;
      } else {
         return super.mouseScrolled(mouseX, mouseY, delta);
      }
   }

   @Override
   public void onClose() {
      this.minecraft.setScreen(this.lastScreen);
   }

   public class Page implements Widget {
      final StatsItemEntry[] items = new StatsItemEntry[14];
      @Nullable
      private List<Component> tooltipLines;

      @Override
      public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
         this.tooltipLines = null;
         this.renderPageHalf(poseStack, mouseX, mouseY, partialTick, IndexViewScreen.this.leftPos + 16, IndexViewScreen.this.topPos + 26, 0, 7);
         this.renderPageHalf(poseStack, mouseX, mouseY, partialTick, IndexViewScreen.this.leftPos + 167, IndexViewScreen.this.topPos + 26, 7, 14);
         if (this.tooltipLines != null) {
            renderTooltip(poseStack, this.tooltipLines, Optional.empty(), mouseX, mouseY);
         }
      }

      private void renderPageHalf(PoseStack poseStack, int mouseX, int mouseY, float partialTick, int startX, int startY, int startIndex, int endIndex) {
         poseStack.pushPose();
         poseStack.translate(startX, startY, 0.0F);
         for (int i = startIndex; i < endIndex; i++) {
            StatsItemEntry entry = this.items[i];
            if (entry == null) break;
            entry.render(IndexViewScreen.this.minecraft, poseStack, mouseX - startX, mouseY - startY - i % 7 * 21, partialTick, getBlitOffset(), lines -> this.tooltipLines = lines);
            poseStack.translate(0.0F, 21.0F, 0.0F);
         }
         poseStack.popPose();
      }

      public static class StatsItemEntry {
         private final ItemStack item;
         private final FormattedCharSequence displayName;
         private final boolean collected;
         private final List<Component> tooltipLines;

         private StatsItemEntry(ItemStack item, FormattedCharSequence displayName, boolean collected, List<Component> tooltipLines) {
            this.item = item;
            this.displayName = displayName;
            this.collected = collected;
            this.tooltipLines = tooltipLines;
         }

         public ItemStack getItem() {
            return this.item;
         }

         public boolean isCollected() {
            return this.collected;
         }

         public static StatsItemEntry create(ItemStack stack, StatsCounter statsCounter, Font font) {
            int pickedUp = statsCounter.getValue(Stats.ITEM_PICKED_UP, stack.getItem());
            int crafted = statsCounter.getValue(Stats.ITEM_CRAFTED, stack.getItem());
            boolean collected = pickedUp > 0 || crafted > 0;
            Style nameStyle = Style.EMPTY.withColor(collected ? 0x4BA52F : ChatFormatting.BLACK.getColor());
            Component displayName = stack.getItem().getName(stack);
            FormattedText formattedName;
            if (font.width(displayName) > 95) {
               formattedName = FormattedText.composite(font.getSplitter().headByWidth(displayName, 95 - font.width("..."), nameStyle), new TextComponent("...").withStyle(nameStyle));
            } else {
               formattedName = new TextComponent("").append(displayName).withStyle(nameStyle);
            }
            ImmutableList.Builder<Component> builder = ImmutableList.builder();
            builder.add(new TextComponent("").append(stack.getItem().getName(stack)).withStyle(stack.getRarity().color));
            if (pickedUp > 0) {
               builder.add(new TextComponent(String.valueOf(pickedUp)).append(" ").append(new TranslatableComponent("stat_type.minecraft.picked_up")).withStyle(ChatFormatting.BLUE));
            }
            if (crafted > 0) {
               builder.add(new TextComponent(String.valueOf(crafted)).append(" ").append(new TranslatableComponent("stat_type.minecraft.crafted")).withStyle(ChatFormatting.BLUE));
            }
            return new Page.StatsItemEntry(stack, Language.getInstance().getVisualOrder(formattedName), collected, builder.build());
         }

         public void render(Minecraft minecraft, PoseStack poseStack, int mouseX, int mouseY, float partialTick, int blitOffset, Consumer<List<Component>> tooltipConsumer) {
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.setShaderTexture(0, INDEX_LOCATION);
            blit(poseStack, 0, 0, 120, 208, 18, 18, 512, 256);
            blit(poseStack, 124, 4, 120 + (this.collected ? 10 : 0), 198, 10, 10, 512, 256);
            RenderHelper.renderItemStackInGui(poseStack, this.item, 1, 1);
            minecraft.font.draw(poseStack, this.displayName, 23, 5, 0x000000);
            if (this.isHovering(0, 0, 18, 18, mouseX, mouseY)) {
               AbstractContainerScreen.renderSlotHighlight(poseStack, 1, 1, blitOffset);
               tooltipConsumer.accept(this.tooltipLines);
            }
         }

         private boolean isHovering(int minX, int minY, int maxX, int maxY, int mouseX, int mouseY) {
            return mouseX > minX && mouseX <= maxX && mouseY > minY && mouseY <= maxY;
         }
      }
   }
}