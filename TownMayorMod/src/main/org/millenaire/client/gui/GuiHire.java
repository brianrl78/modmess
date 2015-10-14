/*     */ package org.millenaire.client.gui;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.GuiButton;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import net.minecraft.world.World;
/*     */ import org.lwjgl.input.Keyboard;
/*     */ import org.millenaire.client.network.ClientSender;
/*     */ import org.millenaire.common.MLN;
/*     */ import org.millenaire.common.MillVillager;
/*     */ import org.millenaire.common.building.Building;
/*     */ import org.millenaire.common.core.MillCommonUtilities;
/*     */ 
/*     */ 
/*     */ public class GuiHire
/*     */   extends GuiText
/*     */ {
/*     */   private static final int REPUTATION_NEEDED = 4096;
/*     */   public static final int BUTTON_CLOSE = 0;
/*     */   public static final int BUTTON_HIRE = 1;
/*     */   public static final int BUTTON_EXTEND = 2;
/*     */   public static final int BUTTON_RELEASE = 3;
/*     */   private final MillVillager villager;
/*     */   private final EntityPlayer player;
/*  28 */   ResourceLocation background = new ResourceLocation("millenaire", "textures/gui/ML_quest.png");
/*     */   
/*     */   public GuiHire(EntityPlayer player, MillVillager villager) {
/*  31 */     this.villager = villager;
/*  32 */     this.player = player;
/*     */   }
/*     */   
/*     */   protected void func_146284_a(GuiButton guibutton)
/*     */   {
/*  37 */     if (!guibutton.field_146124_l) {
/*  38 */       return;
/*     */     }
/*     */     
/*  41 */     if (guibutton.field_146127_k == 0) {
/*  42 */       this.field_146297_k.func_147108_a(null);
/*  43 */       this.field_146297_k.func_71381_h();
/*  44 */     } else if (guibutton.field_146127_k == 1) {
/*  45 */       ClientSender.hireHire(this.player, this.villager);
/*  46 */       refreshContent();
/*  47 */     } else if (guibutton.field_146127_k == 2) {
/*  48 */       ClientSender.hireExtend(this.player, this.villager);
/*  49 */       refreshContent();
/*  50 */     } else if (guibutton.field_146127_k == 3) {
/*  51 */       ClientSender.hireRelease(this.player, this.villager);
/*  52 */       refreshContent();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void buttonPagination()
/*     */   {
/*  60 */     super.buttonPagination();
/*     */     
/*  62 */     int xStart = (this.field_146294_l - getXSize()) / 2;
/*  63 */     int yStart = (this.field_146295_m - getYSize()) / 2;
/*     */     
/*  65 */     if (this.villager.hiredBy != null) {
/*  66 */       if (MillCommonUtilities.countMoney(this.player.field_71071_by) >= this.villager.getHireCost(this.player)) {
/*  67 */         this.field_146292_n.add(new GuiButton(2, xStart + getXSize() / 2 - 100, yStart + getYSize() - 40, 63, 20, MLN.string("hire.extend")));
/*     */       }
/*  69 */       this.field_146292_n.add(new GuiButton(3, xStart + getXSize() / 2 - 32, yStart + getYSize() - 40, 64, 20, MLN.string("hire.release")));
/*  70 */       this.field_146292_n.add(new GuiButton(0, xStart + getXSize() / 2 + 37, yStart + getYSize() - 40, 63, 20, MLN.string("hire.close")));
/*     */     }
/*     */     else {
/*  73 */       if ((this.villager.getTownHall().getReputation(this.player.getDisplayName()) >= 4096) && (MillCommonUtilities.countMoney(this.player.field_71071_by) >= this.villager.getHireCost(this.player))) {
/*  74 */         this.field_146292_n.add(new GuiButton(1, xStart + getXSize() / 2 - 100, yStart + getYSize() - 40, 95, 20, MLN.string("hire.hire")));
/*     */       }
/*  76 */       this.field_146292_n.add(new GuiButton(0, xStart + getXSize() / 2 + 5, yStart + getYSize() - 40, 95, 20, MLN.string("hire.close")));
/*     */     }
/*     */   }
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
/*     */   public boolean func_73868_f()
/*     */   {
/*  91 */     return false;
/*     */   }
/*     */   
/*     */   private List<List<GuiText.Line>> getData()
/*     */   {
/*  96 */     List<GuiText.Line> text = new ArrayList();
/*     */     
/*  98 */     text.add(new GuiText.Line(this.villager.getName() + ", " + this.villager.getNativeOccupationName()));
/*  99 */     text.add(new GuiText.Line());
/*     */     
/* 101 */     if (this.villager.hiredBy != null) {
/* 102 */       text.add(new GuiText.Line(MLN.string("hire.hiredvillager", new String[] { "" + Math.round((float)((this.villager.hiredUntil - this.villager.field_70170_p.func_72820_D()) / 1000L)), Keyboard.getKeyName(MLN.keyAggressiveEscorts) })));
/* 103 */     } else if (this.villager.getTownHall().getReputation(this.player.getDisplayName()) >= 4096) {
/* 104 */       text.add(new GuiText.Line(MLN.string("hire.hireablevillager")));
/*     */     } else {
/* 106 */       text.add(new GuiText.Line(MLN.string("hire.hireablevillagernoreputation")));
/*     */     }
/* 108 */     text.add(new GuiText.Line());
/* 109 */     text.add(new GuiText.Line(MLN.string("hire.health") + ": " + this.villager.func_110143_aJ() * 0.5D + "/" + this.villager.func_110138_aP() * 0.5D));
/* 110 */     text.add(new GuiText.Line(MLN.string("hire.strength") + ": " + this.villager.getAttackStrength()));
/* 111 */     text.add(new GuiText.Line(MLN.string("hire.cost") + ": " + MillCommonUtilities.getShortPrice(this.villager.getHireCost(this.player))));
/*     */     
/* 113 */     List<List<GuiText.Line>> ftext = new ArrayList();
/* 114 */     ftext.add(text);
/*     */     
/* 116 */     return adjustText(ftext);
/*     */   }
/*     */   
/*     */   public int getLineSizeInPx()
/*     */   {
/* 121 */     return 240;
/*     */   }
/*     */   
/*     */   public int getPageSize()
/*     */   {
/* 126 */     return 16;
/*     */   }
/*     */   
/*     */   public ResourceLocation getPNGPath()
/*     */   {
/* 131 */     return this.background;
/*     */   }
/*     */   
/*     */   public int getXSize()
/*     */   {
/* 136 */     return 256;
/*     */   }
/*     */   
/*     */   public int getYSize()
/*     */   {
/* 141 */     return 220;
/*     */   }
/*     */   
/*     */   public void initData()
/*     */   {
/* 146 */     refreshContent();
/*     */   }
/*     */   
/*     */   private void refreshContent() {
/* 150 */     this.descText = getData();
/* 151 */     buttonPagination();
/*     */   }
/*     */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\client\gui\GuiHire.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */