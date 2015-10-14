/*     */ package org.millenaire.client.gui;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.gui.GuiButton;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import org.millenaire.client.network.ClientSender;
/*     */ import org.millenaire.common.Culture;
/*     */ import org.millenaire.common.MLN;
/*     */ import org.millenaire.common.Point;
/*     */ import org.millenaire.common.UserProfile;
/*     */ import org.millenaire.common.VillageType;
/*     */ import org.millenaire.common.forge.CommonProxy;
/*     */ import org.millenaire.common.forge.Mill;
/*     */ 
/*     */ 
/*     */ public class GuiNewVillage
/*     */   extends GuiText
/*     */ {
/*  21 */   private List<VillageType> possibleVillages = new ArrayList();
/*     */   
/*     */   private final Point pos;
/*     */   private final EntityPlayer player;
/*  25 */   ResourceLocation background = new ResourceLocation("millenaire", "textures/gui/ML_panel.png");
/*     */   
/*     */   public GuiNewVillage(EntityPlayer player, Point p) {
/*  28 */     this.pos = p;
/*  29 */     this.player = player;
/*     */   }
/*     */   
/*     */   protected void func_146284_a(GuiButton guibutton)
/*     */   {
/*  34 */     if (!guibutton.field_146124_l) {
/*  35 */       return;
/*     */     }
/*     */     
/*  38 */     VillageType village = (VillageType)this.possibleVillages.get(guibutton.field_146127_k);
/*     */     
/*  40 */     closeWindow();
/*     */     
/*  42 */     if (village.customCentre == null) {
/*  43 */       ClientSender.newVillageCreation(this.player, this.pos, village.culture.key, village.key);
/*     */     } else {
/*  45 */       DisplayActions.displayNewCustomBuildingGUI(this.player, this.pos, village);
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
/*     */   public int getLineSizeInPx()
/*     */   {
/*  60 */     return 195;
/*     */   }
/*     */   
/*     */   public int getPageSize()
/*     */   {
/*  65 */     return 19;
/*     */   }
/*     */   
/*     */   public ResourceLocation getPNGPath()
/*     */   {
/*  70 */     return this.background;
/*     */   }
/*     */   
/*     */   public int getXSize()
/*     */   {
/*  75 */     return 204;
/*     */   }
/*     */   
/*     */   public int getYSize()
/*     */   {
/*  80 */     return 220;
/*     */   }
/*     */   
/*     */   public void initData()
/*     */   {
/*  85 */     List<GuiText.Line> text = new ArrayList();
/*     */     
/*  87 */     text.add(new GuiText.Line(MLN.string("ui.selectavillage"), false));
/*  88 */     text.add(new GuiText.Line(false));
/*  89 */     text.add(new GuiText.Line(MLN.string("ui.leadershipstatus") + ":"));
/*  90 */     text.add(new GuiText.Line());
/*     */     
/*  92 */     boolean notleader = false;
/*     */     
/*  94 */     UserProfile profile = Mill.proxy.getClientProfile();
/*     */     
/*  96 */     for (Culture culture : Culture.ListCultures) {
/*  97 */       if (profile.isTagSet("culturecontrol_" + culture.key)) {
/*  98 */         text.add(new GuiText.Line(MLN.string("ui.leaderin", new String[] { culture.getCultureGameName() })));
/*     */       } else {
/* 100 */         text.add(new GuiText.Line(MLN.string("ui.notleaderin", new String[] { culture.getCultureGameName() })));
/* 101 */         notleader = true;
/*     */       }
/*     */     }
/*     */     
/* 105 */     if (notleader) {
/* 106 */       text.add(new GuiText.Line());
/* 107 */       text.add(new GuiText.Line(MLN.string("ui.leaderinstruction")));
/*     */     }
/* 109 */     text.add(new GuiText.Line());
/*     */     
/* 111 */     this.possibleVillages = VillageType.spawnableVillages(this.player);
/*     */     
/* 113 */     for (int i = 0; i < this.possibleVillages.size(); i++) {
/* 114 */       String controlled = "";
/* 115 */       if (((VillageType)this.possibleVillages.get(i)).playerControlled) {
/* 116 */         if (((VillageType)this.possibleVillages.get(i)).customCentre != null) {
/* 117 */           controlled = ", " + MLN.string("ui.controlledcustom");
/*     */         } else {
/* 119 */           controlled = ", " + MLN.string("ui.controlled");
/*     */         }
/*     */       }
/*     */       
/* 123 */       text.add(new GuiText.Line(new GuiText.MillGuiButton(i, 0, 0, 0, 0, ((VillageType)this.possibleVillages.get(i)).name + " (" + ((VillageType)this.possibleVillages.get(i)).culture.getCultureGameName() + controlled + ")")));
/* 124 */       text.add(new GuiText.Line(false));
/* 125 */       text.add(new GuiText.Line());
/*     */     }
/*     */     
/* 128 */     List<List<GuiText.Line>> pages = new ArrayList();
/* 129 */     pages.add(text);
/* 130 */     this.descText = adjustText(pages);
/*     */   }
/*     */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\client\gui\GuiNewVillage.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */