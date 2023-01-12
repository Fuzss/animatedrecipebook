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
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.Registry;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.Stats;
import net.minecraft.stats.StatsCounter;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.ModList;
import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormat;
import java.util.*;

public abstract class IndexViewScreen extends Screen {
   public static final ResourceLocation INDEX_LOCATION = CompletionistsIndex.id("textures/gui/index.png");

   private final Screen lastScreen;
   private int leftPos;
   private int topPos;
   private Button turnPageBackwards;
   private Button turnPageForwards;
   private StatsSorting statsSorting = StatsSorting.CREATIVE;
   private int currentPage;
   private Component leftPageIndicator;
   private Component rightPageIndicator;
   private List<IndexViewPage> pages;

   protected IndexViewScreen(Screen lastScreen) {
      super(new TranslatableComponent("gui.stats"));
      this.lastScreen = lastScreen;
   }

   public Comparator<IndexViewPage.Entry> getComparator() {
      return this.statsSorting.getComparator();
   }

   abstract List<IndexViewPage.Entry> getPageEntries();

   final void rebuildPages() {
      this.pages = IndexViewPage.createPages(this, this.getPageEntries());
      this.setCurrentPage(0);
   }

   @Override
   protected void init() {
      this.leftPos = (this.width - 316) / 2;
      this.topPos = 2;
      this.addRenderableWidget(new ImageButton(this.leftPos + 17, this.topPos + 11, 16, 13, 42, 202, 20, INDEX_LOCATION, 512, 256, button -> {
         this.onClose();
      }));
      this.addRenderableWidget(new ImageButton(this.leftPos + 316 - 17 - 16, this.topPos + 11, 16, 13, 62, 202, 20, INDEX_LOCATION, 512, 256, button -> {
         this.statsSorting = this.statsSorting.cycle();
         this.rebuildPages();
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
      if (this.pages != null) {
         this.pages.get(this.currentPage).render(poseStack, mouseX, mouseY, tickDelta);
      }
   }

   @Override
   public boolean mouseClicked(double mouseX, double mouseY, int buttonId) {
      if (!super.mouseClicked(mouseX, mouseY, buttonId) && this.pages != null) {
         return this.pages.get(this.currentPage).mouseClicked((int) mouseX, (int) mouseY, buttonId);
      }
      return false;
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
      return this.pages != null ? this.pages.size() : 1;
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

   public static class IndexViewPage implements Widget {
      private static final DecimalFormat PERCENTAGE_FORMAT = new DecimalFormat("#.##");

      private final Entry[] entries = new Entry[14];
      private final IndexViewScreen screen;
      @Nullable
      private List<Component> tooltipLines;

      private IndexViewPage(IndexViewScreen screen) {
         this.screen = screen;
      }

      public static List<IndexViewPage> createPages(IndexViewScreen screen, List<Entry> entries) {
         ImmutableList.Builder<IndexViewPage> builder = ImmutableList.builder();
         IndexViewPage page = null;
         int itemsCount = 0;
         for (Entry entry : entries) {
            if (page == null) {
               page = new IndexViewPage(screen);
               builder.add(page);
            }
            page.entries[itemsCount] = entry;
            entry.page = page;
            if (++itemsCount >= 14) {
               itemsCount = 0;
               page = null;
            }
         }
         return builder.build();
      }

      void setTooltipLines(List<Component> tooltipLines) {
         this.tooltipLines = tooltipLines;
      }

      @Override
      public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
         this.tooltipLines = null;
         this.renderPageHalf(poseStack, mouseX, mouseY, partialTick, this.screen.leftPos + 16, this.screen.topPos + 26, 0, 7);
         this.renderPageHalf(poseStack, mouseX, mouseY, partialTick, this.screen.leftPos + 167, this.screen.topPos + 26, 7, 14);
         if (this.tooltipLines != null) {
            this.screen.renderTooltip(poseStack, this.tooltipLines, Optional.empty(), mouseX, mouseY);
         }
      }

      private void renderPageHalf(PoseStack poseStack, int mouseX, int mouseY, float partialTick, int startX, int startY, int startIndex, int endIndex) {
         poseStack.pushPose();
         poseStack.translate(startX, startY, 0.0F);
         for (int i = startIndex; i < endIndex; i++) {
            Entry entry = this.entries[i];
            if (entry == null) break;
            entry.render(this.screen.getMinecraft(), poseStack, mouseX - startX, mouseY - startY - i % 7 * 21, partialTick, this.screen.getBlitOffset());
            poseStack.translate(0.0F, 21.0F, 0.0F);
         }
         poseStack.popPose();
      }

      public boolean mouseClicked(int mouseX, int mouseY, int buttonId) {
         for (int i = 0; i < this.entries.length; i++) {
            int startX = i >= 7 ? this.screen.leftPos + 167 : this.screen.leftPos + 16;
            int startY = this.screen.topPos + 26;
            if (this.entries[i].isMouseOver(mouseX - startX, mouseY - startY - i % 7 * 21)) {
               return true;
            }
         }
         return false;
      }

      public static Entry statsItemEntry(ItemStack stack, StatsCounter statsCounter, Font font) {
         int pickedUp = statsCounter.getValue(Stats.ITEM_PICKED_UP, stack.getItem());
         int crafted = statsCounter.getValue(Stats.ITEM_CRAFTED, stack.getItem());
         boolean collected = pickedUp > 0 || crafted > 0;
         Component displayName = stack.getItem().getName(stack);
         FormattedCharSequence formattedName = formatDisplayName(font, displayName, collected);
         ImmutableList.Builder<Component> builder = ImmutableList.builder();
         builder.add(new TextComponent("").append(stack.getItem().getName(stack)).withStyle(stack.getRarity().color));
         if (pickedUp > 0) {
            builder.add(new TextComponent(String.valueOf(pickedUp)).append(" ").append(new TranslatableComponent("stat_type.minecraft.picked_up")).withStyle(ChatFormatting.BLUE));
         }
         if (crafted > 0) {
            builder.add(new TextComponent(String.valueOf(crafted)).append(" ").append(new TranslatableComponent("stat_type.minecraft.crafted")).withStyle(ChatFormatting.BLUE));
         }
         return new StatsItemEntry(stack, formattedName, collected, builder.build());
      }

      public static Entry modItemEntry(String modId, Collection<ItemStack> items, StatsCounter statsCounter, Font font) {
         if (items.isEmpty()) throw new IllegalArgumentException("items cannot be empty");
         Component modName = new TextComponent(ModList.get().getModContainerById(modId).orElseThrow().getModInfo().getDisplayName());
         ItemStack displayItem = items.stream().skip((long) (Math.random() * items.size())).findFirst().orElseThrow();
         long collectedCount = items.stream().filter(stack -> {
            int pickedUp = statsCounter.getValue(Stats.ITEM_PICKED_UP, stack.getItem());
            int crafted = statsCounter.getValue(Stats.ITEM_CRAFTED, stack.getItem());
            return pickedUp + crafted > 0;
         }).count();
         boolean collected = collectedCount == items.size();
         float collectionProgress = collectedCount / (float) items.size();
         Component tooltipComponent = new TextComponent("").append(modName).append(new TextComponent(" (" + PERCENTAGE_FORMAT.format(collectionProgress * 100.0F) + "%)").withStyle(ChatFormatting.BLUE));
         return new ModItemEntry(displayItem, formatDisplayName(font, modName, collected), collected, List.of(tooltipComponent), new TextComponent(collectedCount + "/" + items.size()), collectionProgress);
      }

      private static FormattedCharSequence formatDisplayName(Font font, Component displayName, boolean collected) {
         Style style = Style.EMPTY.withColor(collected ? 0x4BA52F : ChatFormatting.BLACK.getColor());
         FormattedText formattedText;
         if (font.width(displayName) > 95) {
            formattedText = FormattedText.composite(font.getSplitter().headByWidth(displayName, 95 - font.width("..."), style), new TextComponent("...").withStyle(style));
         } else {
            formattedText = new TextComponent("").append(displayName).withStyle(style);
         }
         return Language.getInstance().getVisualOrder(formattedText);
      }

      public static abstract class Entry {
         final ItemStack item;
         final FormattedCharSequence displayName;
         private final boolean collected;
         private final List<Component> tooltipLines;
         IndexViewPage page;

         private Entry(ItemStack item, FormattedCharSequence displayName, boolean collected, List<Component> tooltipLines) {
            this.item = item;
            this.displayName = displayName;
            this.collected = collected;
            this.tooltipLines = tooltipLines;
         }

         public abstract <T extends Comparable<? super T>> T toComparableKey();

         public boolean isCollected() {
            return this.collected;
         }

         public void render(Minecraft minecraft, PoseStack poseStack, int mouseX, int mouseY, float partialTick, int blitOffset) {
            this.renderBackground(poseStack);
            this.renderForeground(minecraft.font, poseStack);
            if (this.isHovering(0, 0, 18, 18, mouseX, mouseY)) {
               AbstractContainerScreen.renderSlotHighlight(poseStack, 1, 1, blitOffset);
               this.page.setTooltipLines(this.tooltipLines);
            }
         }

         public boolean isMouseOver(int mouseX, int mouseY) {
            return this.isHovering(0, 0, 134, 18, mouseX, mouseY);
         }

         private boolean isHovering(int minX, int minY, int maxX, int maxY, int mouseX, int mouseY) {
            return mouseX > minX && mouseX <= maxX && mouseY > minY && mouseY <= maxY;
         }

         public void renderBackground(PoseStack poseStack) {
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.setShaderTexture(0, INDEX_LOCATION);
            blit(poseStack, 0, 0, 120, 208, 18, 18, 512, 256);
            blit(poseStack, 124, 4, 120 + (this.collected ? 10 : 0), 198, 10, 10, 512, 256);
         }

         public void renderForeground(Font font, PoseStack poseStack) {
            RenderHelper.renderItemStackInGui(poseStack, this.item, 1, 1);
         }
      }

      private static class StatsItemEntry extends Entry {

         public StatsItemEntry(ItemStack item, FormattedCharSequence displayName, boolean collected, List<Component> tooltipLines) {
            super(item, displayName, collected, tooltipLines);
         }

         @Override
         public <T extends Comparable<? super T>> T toComparableKey() {
            return (T) Registry.ITEM.getKey(this.item.getItem()).getPath();
         }

         @Override
         public void renderForeground(Font font, PoseStack poseStack) {
            super.renderForeground(font, poseStack);
            font.draw(poseStack, this.displayName, 23, 5, 0x000000);
         }
      }

      public static class ModItemEntry extends Entry {
         private final Component collection;
         private final float collectionProgress;

         public ModItemEntry(ItemStack item, FormattedCharSequence displayName, boolean collected, List<Component> tooltipLines, Component collection, float collectionProgress) {
            super(item, displayName, collected, tooltipLines);
            this.collection = collection;
            this.collectionProgress = collectionProgress;
         }

         @Override
         public <T extends Comparable<? super T>> T toComparableKey() {
            return (T) this.collection.getString();
         }

         @Override
         public void renderBackground(PoseStack poseStack) {
            super.renderBackground(poseStack);
            blit(poseStack, 24, 11, 140, 198, 91, 5, 512, 256);
            blit(poseStack, 24, 11, 140, 203, (int) (91 * this.collectionProgress), 5, 512, 256);
         }

         @Override
         public void renderForeground(Font font, PoseStack poseStack) {
            super.renderForeground(font, poseStack);
            font.draw(poseStack, this.displayName, 70 - font.width(this.displayName) / 2, 0, 0x000000);
            font.draw(poseStack, this.collection, 70 - font.width(this.collection) / 2 - 1, 10, 0x000000);
            font.draw(poseStack, this.collection, 70 - font.width(this.collection) / 2, 10 - 1, 0x000000);
            font.draw(poseStack, this.collection, 70 - font.width(this.collection) / 2, 10 + 1, 0x000000);
            font.draw(poseStack, this.collection, 70 - font.width(this.collection) / 2 + 1, 10, 0x000000);
            font.draw(poseStack, this.collection, 70 - font.width(this.collection) / 2, 10, 0xFFC700);
         }
      }
   }
}