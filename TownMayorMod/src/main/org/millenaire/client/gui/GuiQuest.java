/*     */ package org.millenaire.client.gui;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.GuiButton;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import org.millenaire.client.network.ClientSender;
/*     */ import org.millenaire.common.MLN;
/*     */ import org.millenaire.common.MillVillager;
/*     */ import org.millenaire.common.Quest.QuestInstance;
/*     */ import org.millenaire.common.Quest.QuestStep;
/*     */ import org.millenaire.common.UserProfile;
/*     */ import org.millenaire.common.forge.CommonProxy;
/*     */ import org.millenaire.common.forge.Mill;
/*     */ 
/*     */ public class GuiQuest extends GuiText
/*     */ {
/*     */   private final MillVillager villager;
/*     */   private final EntityPlayer player;
/*  23 */   private boolean showOk = false;
/*     */   
/*     */   private int type;
/*     */   private boolean firstStep;
/*  27 */   ResourceLocation background = new ResourceLocation("millenaire", "textures/gui/ML_quest.png");
/*     */   
/*     */   public GuiQuest(EntityPlayer player, MillVillager villager) {
/*  30 */     this.villager = villager;
/*  31 */     this.player = player;
/*     */   }
/*     */   
/*     */   protected void func_146284_a(GuiButton guibutton)
/*     */   {
/*  36 */     if (!guibutton.field_146124_l) {
/*  37 */       return;
/*     */     }
/*     */     
/*  40 */     UserProfile profile = Mill.proxy.getClientProfile();
/*     */     
/*  42 */     if (guibutton.field_146127_k == 0) {
/*  43 */       Quest.QuestInstance qi = (Quest.QuestInstance)profile.villagersInQuests.get(Long.valueOf(this.villager.villager_id));
/*  44 */       boolean firstStep = qi.currentStep == 0;
/*  45 */       String res = qi.completeStep(this.player, this.villager);
/*  46 */       ClientSender.questCompleteStep(this.player, this.villager);
/*  47 */       initStatus(1, res, firstStep);
/*  48 */     } else if (guibutton.field_146127_k == 1) {
/*  49 */       Quest.QuestInstance qi = (Quest.QuestInstance)profile.villagersInQuests.get(Long.valueOf(this.villager.villager_id));
/*  50 */       boolean firstStep = qi.currentStep == 0;
/*  51 */       String res = qi.refuseQuest(this.player, this.villager);
/*  52 */       ClientSender.questRefuse(this.player, this.villager);
/*  53 */       initStatus(2, res, firstStep);
/*     */     } else {
/*  55 */       this.field_146297_k.func_147108_a(null);
/*  56 */       this.field_146297_k.func_71381_h();
/*  57 */       ClientSender.villagerInteractSpecial(this.player, this.villager);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void buttonPagination()
/*     */   {
/*  65 */     super.buttonPagination();
/*     */     
/*  67 */     int xStart = (this.field_146294_l - getXSize()) / 2;
/*  68 */     int yStart = (this.field_146295_m - getYSize()) / 2;
/*     */     
/*  70 */     if (this.type == 0) {
/*  71 */       if (this.firstStep) {
/*  72 */         if (this.showOk) {
/*  73 */           this.field_146292_n.add(new GuiButton(1, xStart + getXSize() / 2 - 100, yStart + getYSize() - 40, 95, 20, MLN.string("quest.refuse")));
/*  74 */           this.field_146292_n.add(new GuiButton(0, xStart + getXSize() / 2 + 5, yStart + getYSize() - 40, 95, 20, MLN.string("quest.accept")));
/*     */         } else {
/*  76 */           this.field_146292_n.add(new GuiButton(1, xStart + getXSize() / 2 - 100, yStart + getYSize() - 40, 95, 20, MLN.string("quest.refuse")));
/*  77 */           this.field_146292_n.add(new GuiButton(2, xStart + getXSize() / 2 + 5, yStart + getYSize() - 40, 95, 20, MLN.string("quest.close")));
/*     */         }
/*     */       }
/*  80 */       else if (this.showOk) {
/*  81 */         this.field_146292_n.add(new GuiButton(0, xStart + getXSize() / 2 - 100, yStart + getYSize() - 40, MLN.string("quest.continue")));
/*     */       } else {
/*  83 */         this.field_146292_n.add(new GuiButton(2, xStart + getXSize() / 2 - 100, yStart + getYSize() - 40, MLN.string("quest.close")));
/*     */       }
/*     */     }
/*     */     else {
/*  87 */       this.field_146292_n.add(new GuiButton(2, xStart + getXSize() / 2 - 100, yStart + getYSize() - 40, MLN.string("quest.close")));
/*     */     }
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
/* 104 */     return false;
/*     */   }
/*     */   
/*     */   private List<List<GuiText.Line>> getData(int type, String baseText)
/*     */   {
/* 109 */     List<GuiText.Line> text = new ArrayList();
/*     */     
/* 111 */     String game = "";
/* 112 */     if (this.villager.getGameOccupationName(this.player.getDisplayName()).length() > 0) {
/* 113 */       game = " (" + this.villager.getGameOccupationName(this.player.getDisplayName()) + ")";
/*     */     }
/*     */     
/* 116 */     text.add(new GuiText.Line(this.villager.getName() + ", " + this.villager.getNativeOccupationName() + game));
/* 117 */     text.add(new GuiText.Line());
/* 118 */     text.add(new GuiText.Line(baseText.replaceAll("\\$name", this.player.getDisplayName())));
/*     */     
/* 120 */     UserProfile profile = Mill.proxy.getClientProfile();
/*     */     
/* 122 */     if (type == 0) {
/* 123 */       Quest.QuestStep step = ((Quest.QuestInstance)profile.villagersInQuests.get(Long.valueOf(this.villager.villager_id))).getCurrentStep();
/*     */       
/* 125 */       String error = step.lackingConditions(this.player);
/*     */       
/* 127 */       if (error != null) {
/* 128 */         text.add(new GuiText.Line());
/* 129 */         text.add(new GuiText.Line(error));
/* 130 */         this.showOk = false;
/*     */       } else {
/* 132 */         this.showOk = true;
/*     */       }
/*     */     }
/*     */     
/* 136 */     List<List<GuiText.Line>> ftext = new ArrayList();
/* 137 */     ftext.add(text);
/*     */     
/* 139 */     return adjustText(ftext);
/*     */   }
/*     */   
/*     */   public int getLineSizeInPx()
/*     */   {
/* 144 */     return 240;
/*     */   }
/*     */   
/*     */   public int getPageSize()
/*     */   {
/* 149 */     return 16;
/*     */   }
/*     */   
/*     */   public ResourceLocation getPNGPath()
/*     */   {
/* 154 */     return this.background;
/*     */   }
/*     */   
/*     */   public int getXSize()
/*     */   {
/* 159 */     return 256;
/*     */   }
/*     */   
/*     */   public int getYSize()
/*     */   {
/* 164 */     return 220;
/*     */   }
/*     */   
/*     */   public void initData()
/*     */   {
/* 169 */     UserProfile profile = Mill.proxy.getClientProfile();
/*     */     
/* 171 */     String baseText = ((Quest.QuestInstance)profile.villagersInQuests.get(Long.valueOf(this.villager.villager_id))).getDescription(profile);
/* 172 */     boolean firstStep = ((Quest.QuestInstance)profile.villagersInQuests.get(Long.valueOf(this.villager.villager_id))).currentStep == 0;
/*     */     
/* 174 */     initStatus(0, baseText, firstStep);
/*     */   }
/*     */   
/*     */   private void initStatus(int type, String baseText, boolean firstStep)
/*     */   {
/* 179 */     this.pageNum = 0;
/* 180 */     this.type = type;
/* 181 */     this.firstStep = firstStep;
/* 182 */     this.descText = getData(type, baseText);
/* 183 */     buttonPagination();
/*     */   }
/*     */   
/*     */   protected void func_73869_a(char c, int i)
/*     */   {
/* 188 */     if (i == 1) {
/* 189 */       this.field_146297_k.func_147108_a(null);
/* 190 */       this.field_146297_k.func_71381_h();
/*     */       
/* 192 */       ClientSender.villagerInteractSpecial(this.player, this.villager);
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\client\gui\GuiQuest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */