/*     */ package org.millenaire.client.gui;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.gui.FontRenderer;
/*     */ import net.minecraft.client.gui.GuiButton;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ import org.millenaire.common.MLN;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GuiHelp
/*     */   extends GuiText
/*     */ {
/*     */   public static final int NB_CHAPTERS = 13;
/*  17 */   int helpDisplayed = 1;
/*     */   
/*  19 */   ResourceLocation background = new ResourceLocation("millenaire", "textures/gui/ML_help.png");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void func_146284_a(GuiButton guibutton) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void customDrawBackground(int mouseX, int mouseY, float f)
/*     */   {
/*  32 */     int xStart = (this.field_146294_l - getXSize()) / 2;
/*  33 */     int yStart = (this.field_146295_m - getYSize()) / 2;
/*     */     
/*  35 */     GL11.glDisable(2896);
/*  36 */     GL11.glDisable(2929);
/*     */     
/*  38 */     for (int i = 0; i < 7; i++) {
/*  39 */       if (this.helpDisplayed - 1 != i) {
/*  40 */         int extraFirstRow = i == 0 ? 1 : 0;
/*  41 */         func_73733_a(xStart, yStart - extraFirstRow + 32 * i + 1, xStart + 32, yStart + 32 * i + 32, -1610612736, -1610612736);
/*     */       }
/*  43 */       if (this.helpDisplayed - 8 != i) {
/*  44 */         int extraFirstRow = i == 0 ? 1 : 0;
/*  45 */         func_73733_a(xStart + 224, yStart - extraFirstRow + 32 * i + 1, xStart + 32 + 224, yStart + 32 * i + 32, -1610612736, -1610612736);
/*     */       }
/*     */     }
/*     */     
/*  49 */     GL11.glEnable(2896);
/*  50 */     GL11.glEnable(2929);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void customDrawScreen(int mouseX, int mouseY, float f)
/*     */   {
/*  57 */     int xStart = (this.field_146294_l - getXSize()) / 2;
/*  58 */     int yStart = (this.field_146295_m - getYSize()) / 2;
/*     */     
/*  60 */     GL11.glDisable(2896);
/*  61 */     GL11.glDisable(2929);
/*     */     
/*  63 */     mouseX -= xStart;
/*  64 */     mouseY -= yStart;
/*     */     
/*  66 */     if ((mouseX > 0) && (mouseX < 32)) {
/*  67 */       int pos = mouseY / 32;
/*  68 */       if ((pos >= 0) && (pos < 13)) {
/*  69 */         int stringlength = this.field_146289_q.func_78256_a(MLN.string("help.tab_" + (pos + 1)));
/*  70 */         func_73733_a(mouseX + 10 - 3, mouseY + 10 - 3, mouseX + 10 + stringlength + 3, mouseY + 10 + 14, -1073741824, -1073741824);
/*  71 */         this.field_146289_q.func_78276_b(MLN.string("help.tab_" + (pos + 1)), mouseX + 10, mouseY + 10, 9474192);
/*     */       }
/*     */     }
/*     */     
/*  75 */     if ((mouseX > 224) && (mouseX < 256)) {
/*  76 */       int pos = mouseY / 32;
/*  77 */       if ((pos >= 0) && (pos < 6)) {
/*  78 */         int stringlength = this.field_146289_q.func_78256_a(MLN.string("help.tab_" + (pos + 8)));
/*  79 */         func_73733_a(mouseX + 10 - 3, mouseY + 10 - 3, mouseX + 10 + stringlength + 3, mouseY + 10 + 14, -1073741824, -1073741824);
/*  80 */         this.field_146289_q.func_78276_b(MLN.string("help.tab_" + (pos + 8)), mouseX + 10, mouseY + 10, 9474192);
/*     */       }
/*     */     }
/*     */     
/*  84 */     GL11.glEnable(2896);
/*  85 */     GL11.glEnable(2929);
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean func_73868_f()
/*     */   {
/*  91 */     return true;
/*     */   }
/*     */   
/*     */   public int getLineSizeInPx()
/*     */   {
/*  96 */     return 180;
/*     */   }
/*     */   
/*     */   public int getPageSize()
/*     */   {
/* 101 */     return 20;
/*     */   }
/*     */   
/*     */   public ResourceLocation getPNGPath()
/*     */   {
/* 106 */     return this.background;
/*     */   }
/*     */   
/*     */   public int getTextXStart()
/*     */   {
/* 111 */     return 36;
/*     */   }
/*     */   
/*     */   public int getXSize()
/*     */   {
/* 116 */     return 256;
/*     */   }
/*     */   
/*     */   public int getYSize()
/*     */   {
/* 121 */     return 224;
/*     */   }
/*     */   
/*     */ 
/*     */   public void initData()
/*     */   {
/* 127 */     List<List<String>> baseText = MLN.getHelp(this.helpDisplayed);
/*     */     
/* 129 */     if (baseText != null) {
/* 130 */       this.descText = convertAdjustText(baseText);
/*     */     } else {
/* 132 */       this.descText = new ArrayList();
/*     */       
/* 134 */       List<GuiText.Line> page = new ArrayList();
/* 135 */       page.add(new GuiText.Line("Il n'y a malheuresement pas d'aide disponible dans votre langue."));
/* 136 */       page.add(new GuiText.Line(""));
/* 137 */       page.add(new GuiText.Line("Unfortunately there is no help available in your language."));
/* 138 */       this.descText.add(page);
/* 139 */       this.descText = adjustText(this.descText);
/*     */     }
/*     */   }
/*     */   
/*     */   protected void func_73864_a(int mouseX, int mouseY, int k)
/*     */   {
/* 145 */     int xStart = (this.field_146294_l - getXSize()) / 2;
/* 146 */     int yStart = (this.field_146295_m - getYSize()) / 2;
/*     */     
/* 148 */     int ai = mouseX - xStart;
/* 149 */     int aj = mouseY - yStart;
/*     */     
/* 151 */     if ((aj > getYSize() - 14) && (aj < getYSize())) {
/* 152 */       if ((ai > 36) && (ai < 64)) {
/* 153 */         decrementPage();
/* 154 */       } else if ((ai > getXSize() - 64) && (ai < getXSize() - 36)) {
/* 155 */         incrementPage();
/*     */       }
/*     */     }
/*     */     
/* 159 */     if ((ai > 0) && (ai < 32)) {
/* 160 */       int pos = aj / 32;
/* 161 */       if ((pos >= 0) && (pos < 13)) {
/* 162 */         pos++;
/*     */         
/* 164 */         this.pageNum = 0;
/*     */         
/* 166 */         if (pos != this.helpDisplayed) {
/* 167 */           this.helpDisplayed = pos;
/* 168 */           initData();
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 173 */     if ((ai > 224) && (ai < 256)) {
/* 174 */       int pos = aj / 32;
/* 175 */       if ((pos >= 0) && (pos < 6)) {
/* 176 */         pos += 8;
/*     */         
/* 178 */         this.pageNum = 0;
/*     */         
/* 180 */         if (pos != this.helpDisplayed) {
/* 181 */           this.helpDisplayed = pos;
/* 182 */           initData();
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 187 */     super.func_73864_a(mouseX, mouseY, k);
/*     */   }
/*     */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\client\gui\GuiHelp.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */