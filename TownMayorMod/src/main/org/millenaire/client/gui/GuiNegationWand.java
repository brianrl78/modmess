/*     */ package org.millenaire.client.gui;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.GuiButton;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import org.lwjgl.input.Keyboard;
/*     */ import org.millenaire.client.network.ClientSender;
/*     */ import org.millenaire.common.MLN;
/*     */ import org.millenaire.common.VillageType;
/*     */ import org.millenaire.common.building.Building;
/*     */ 
/*     */ 
/*     */ public class GuiNegationWand
/*     */   extends GuiText
/*     */ {
/*     */   private final Building th;
/*     */   private final EntityPlayer player;
/*  21 */   ResourceLocation background = new ResourceLocation("millenaire", "textures/gui/ML_quest.png");
/*     */   
/*     */   public GuiNegationWand(EntityPlayer player, Building th) {
/*  24 */     this.th = th;
/*  25 */     this.player = player;
/*     */   }
/*     */   
/*     */   protected void func_146284_a(GuiButton guibutton)
/*     */   {
/*  30 */     if (!guibutton.field_146124_l) {
/*  31 */       return;
/*     */     }
/*     */     
/*  34 */     if (guibutton.field_146127_k == 0) {
/*  35 */       ClientSender.negationWand(this.player, this.th);
/*     */     }
/*     */     
/*  38 */     this.field_146297_k.func_147108_a(null);
/*  39 */     this.field_146297_k.func_71381_h();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void buttonPagination()
/*     */   {
/*  46 */     super.buttonPagination();
/*     */     
/*  48 */     int xStart = (this.field_146294_l - getXSize()) / 2;
/*  49 */     int yStart = (this.field_146295_m - getYSize()) / 2;
/*     */     
/*  51 */     this.field_146292_n.add(new GuiButton(1, xStart + getXSize() / 2 - 100, yStart + getYSize() - 40, 95, 20, MLN.string("negationwand.cancel")));
/*  52 */     this.field_146292_n.add(new GuiButton(0, xStart + getXSize() / 2 + 5, yStart + getYSize() - 40, 95, 20, MLN.string("negationwand.confirm")));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void customDrawBackground(int i, int j, float f) {}
/*     */   
/*     */ 
/*     */ 
/*     */   protected void customDrawScreen(int i, int j, float f) {}
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean func_73868_f()
/*     */   {
/*  68 */     return false;
/*     */   }
/*     */   
/*     */   public int getLineSizeInPx()
/*     */   {
/*  73 */     return 240;
/*     */   }
/*     */   
/*     */   public int getPageSize()
/*     */   {
/*  78 */     return 16;
/*     */   }
/*     */   
/*     */   public ResourceLocation getPNGPath()
/*     */   {
/*  83 */     return this.background;
/*     */   }
/*     */   
/*     */   public int getXSize()
/*     */   {
/*  88 */     return 256;
/*     */   }
/*     */   
/*     */   public int getYSize()
/*     */   {
/*  93 */     return 220;
/*     */   }
/*     */   
/*     */ 
/*     */   public void initData()
/*     */   {
/*  99 */     this.descText = new ArrayList();
/*     */     
/* 101 */     List<GuiText.Line> page = new ArrayList();
/* 102 */     page.add(new GuiText.Line(MLN.string("negationwand.confirmmessage", new String[] { this.th.villageType.name })));
/* 103 */     this.descText.add(page);
/*     */     
/* 105 */     this.descText = adjustText(this.descText);
/*     */     
/* 107 */     this.pageNum = 0;
/*     */     
/* 109 */     Keyboard.enableRepeatEvents(true);
/*     */   }
/*     */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\client\gui\GuiNegationWand.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */