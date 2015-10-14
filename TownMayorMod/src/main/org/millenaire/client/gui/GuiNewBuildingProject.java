/*     */ package org.millenaire.client.gui;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.gui.GuiButton;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import org.millenaire.common.Culture;
/*     */ import org.millenaire.common.MLN;
/*     */ import org.millenaire.common.building.Building;
/*     */ import org.millenaire.common.building.BuildingCustomPlan;
/*     */ import org.millenaire.common.building.BuildingPlanSet;
/*     */ import org.millenaire.common.building.IBuildingPlan;
/*     */ 
/*     */ public class GuiNewBuildingProject extends GuiText
/*     */ {
/*     */   private final Building townHall;
/*     */   private final org.millenaire.common.Point pos;
/*     */   private final net.minecraft.entity.player.EntityPlayer player;
/*     */   
/*     */   public static class GuiButtonNewBuilding extends GuiText.MillGuiButton
/*     */   {
/*     */     private final String key;
/*     */     private final boolean custom;
/*     */     
/*     */     public GuiButtonNewBuilding(String key, String label, boolean custom)
/*     */     {
/*  27 */       super(0, 0, 0, 0, label);
/*  28 */       this.key = key;
/*  29 */       this.custom = custom;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  37 */   ResourceLocation background = new ResourceLocation("millenaire", "textures/gui/ML_panel.png");
/*     */   
/*     */   public GuiNewBuildingProject(net.minecraft.entity.player.EntityPlayer player, Building th, org.millenaire.common.Point p)
/*     */   {
/*  41 */     this.townHall = th;
/*  42 */     this.pos = p;
/*  43 */     this.player = player;
/*     */   }
/*     */   
/*     */   protected void func_146284_a(GuiButton guibutton)
/*     */   {
/*  48 */     if (!guibutton.field_146124_l) {
/*  49 */       return;
/*     */     }
/*     */     
/*  52 */     GuiButtonNewBuilding button = (GuiButtonNewBuilding)guibutton;
/*     */     
/*  54 */     if (!button.custom) {
/*  55 */       org.millenaire.client.network.ClientSender.newBuilding(this.player, this.townHall, this.pos, button.key);
/*     */     }
/*     */     else {
/*  58 */       closeWindow();
/*     */       
/*  60 */       BuildingCustomPlan customBuilding = this.townHall.culture.getBuildingCustom(button.key);
/*     */       
/*  62 */       if (customBuilding != null) {
/*  63 */         DisplayActions.displayNewCustomBuildingGUI(this.player, this.townHall, this.pos, customBuilding);
/*     */       }
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
/*     */   public void decrementPage()
/*     */   {
/*  81 */     super.decrementPage();
/*  82 */     buttonPagination();
/*     */   }
/*     */   
/*     */   private String getCustomPlanDesc(IBuildingPlan customPlan) {
/*  86 */     String desc = "";
/*     */     
/*  88 */     if ((customPlan.getGameName() != null) && (customPlan.getGameName().length() > 0)) {
/*  89 */       desc = desc + customPlan.getGameName() + ". ";
/*     */     }
/*     */     
/*  92 */     if ((customPlan.getMaleResident().size() > 0) || (customPlan.getFemaleResident().size() > 0)) {
/*  93 */       desc = desc + MLN.string("ui.inhabitants") + ": ";
/*  94 */       boolean first = true;
/*  95 */       for (String inhabitant : customPlan.getMaleResident()) {
/*  96 */         if (first) {
/*  97 */           first = false;
/*     */         } else {
/*  99 */           desc = desc + ", ";
/*     */         }
/* 101 */         desc = desc + customPlan.getCulture().getVillagerType(inhabitant).name;
/*     */       }
/* 103 */       for (String inhabitant : customPlan.getFemaleResident()) {
/* 104 */         if (first) {
/* 105 */           first = false;
/*     */         } else {
/* 107 */           desc = desc + ", ";
/*     */         }
/* 109 */         desc = desc + customPlan.getCulture().getVillagerType(inhabitant).name;
/*     */       }
/* 111 */       desc = desc + ". ";
/*     */     }
/*     */     
/* 114 */     return desc;
/*     */   }
/*     */   
/*     */   public int getLineSizeInPx()
/*     */   {
/* 119 */     return 195;
/*     */   }
/*     */   
/*     */   public int getPageSize()
/*     */   {
/* 124 */     return 19;
/*     */   }
/*     */   
/*     */   public ResourceLocation getPNGPath()
/*     */   {
/* 129 */     return this.background;
/*     */   }
/*     */   
/*     */   public int getXSize()
/*     */   {
/* 134 */     return 204;
/*     */   }
/*     */   
/*     */   public int getYSize()
/*     */   {
/* 139 */     return 220;
/*     */   }
/*     */   
/*     */   public void incrementPage()
/*     */   {
/* 144 */     super.incrementPage();
/* 145 */     buttonPagination();
/*     */   }
/*     */   
/*     */ 
/*     */   public void initData()
/*     */   {
/* 151 */     List<List<GuiText.Line>> pages = new ArrayList();
/* 152 */     List<GuiText.Line> text = new ArrayList();
/*     */     
/* 154 */     text.add(new GuiText.Line(this.townHall.getVillageQualifiedName()));
/* 155 */     text.add(new GuiText.Line());
/* 156 */     text.add(new GuiText.Line(MLN.string("ui.selectabuildingproject")));
/*     */     
/* 158 */     text.add(new GuiText.Line());
/* 159 */     text.add(new GuiText.Line(MLN.string("ui.selectabuildingproject_custom")));
/*     */     
/*     */ 
/*     */ 
/* 163 */     for (BuildingCustomPlan customBuilding : this.townHall.villageType.customBuildings) {
/* 164 */       text.add(new GuiText.Line(new GuiButtonNewBuilding(customBuilding.buildingKey, customBuilding.getNativeName(), true)));
/* 165 */       text.add(new GuiText.Line(false));
/* 166 */       String desc = getCustomPlanDesc(customBuilding).trim();
/* 167 */       if (desc.length() > 0) {
/* 168 */         text.add(new GuiText.Line(desc));
/*     */       }
/* 170 */       text.add(new GuiText.Line());
/*     */     }
/*     */     
/* 173 */     pages.add(text);
/* 174 */     text = new ArrayList();
/*     */     
/* 176 */     text.add(new GuiText.Line());
/* 177 */     text.add(new GuiText.Line(MLN.string("ui.selectabuildingproject_standard")));
/*     */     
/* 179 */     for (BuildingPlanSet planSet : this.townHall.villageType.coreBuildings) {
/* 180 */       if (this.townHall.isValidProject(planSet.getBuildingProject())) {
/* 181 */         text.add(new GuiText.Line(new GuiButtonNewBuilding(planSet.key, planSet.getNativeName(), false)));
/* 182 */         text.add(new GuiText.Line(false));
/* 183 */         String desc = getCustomPlanDesc(planSet.getFirstStartingPlan()).trim();
/* 184 */         if (desc.length() > 0) {
/* 185 */           text.add(new GuiText.Line(desc));
/*     */         }
/* 187 */         text.add(new GuiText.Line());
/*     */       }
/*     */     }
/*     */     
/* 191 */     pages.add(text);
/*     */     
/* 193 */     this.descText = adjustText(pages);
/*     */   }
/*     */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\client\gui\GuiNewBuildingProject.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */