/*     */ package org.millenaire.common.building;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import org.millenaire.common.MLN;
/*     */ import org.millenaire.common.core.MillCommonUtilities;
/*     */ import org.millenaire.common.core.MillCommonUtilities.WeightedChoice;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BuildingProject
/*     */   implements MillCommonUtilities.WeightedChoice
/*     */ {
/*     */   public static enum EnumProjects
/*     */   {
/*  30 */     CENTRE(0, "ui.buildingscentre"),  START(1, "ui.buildingsstarting"),  PLAYER(2, "ui.buildingsplayer"),  CORE(3, "ui.buildingskey"),  SECONDARY(4, "ui.buildingssecondary"),  EXTRA(5, "ui.buildingsextra"), 
/*  31 */     CUSTOMBUILDINGS(6, "ui.buildingcustom");
/*     */     
/*     */     public static EnumProjects getById(int id) {
/*  34 */       for (EnumProjects ep : ) {
/*  35 */         if (ep.id == id) {
/*  36 */           return ep;
/*     */         }
/*     */       }
/*  39 */       return null;
/*     */     }
/*     */     
/*     */     public final int id;
/*     */     public final String labelKey;
/*     */     private EnumProjects(int id, String labelKey)
/*     */     {
/*  46 */       this.id = id;
/*  47 */       this.labelKey = labelKey;
/*     */     }
/*     */   }
/*     */   
/*     */   public static BuildingProject getRandomProject(List<BuildingProject> possibleProjects)
/*     */   {
/*  53 */     return (BuildingProject)MillCommonUtilities.getWeightedChoice(possibleProjects, null);
/*     */   }
/*     */   
/*  56 */   public BuildingPlanSet planSet = null;
/*     */   
/*  58 */   public BuildingLocation location = null;
/*     */   
/*  60 */   public BuildingCustomPlan customBuildingPlan = null;
/*     */   
/*     */   public String key;
/*     */   
/*  64 */   public boolean isCustomBuilding = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public BuildingProject() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public BuildingProject(BuildingCustomPlan customPlan, BuildingLocation location)
/*     */   {
/*  77 */     this.customBuildingPlan = customPlan;
/*  78 */     this.key = this.customBuildingPlan.buildingKey;
/*  79 */     this.location = location;
/*  80 */     this.isCustomBuilding = true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public BuildingProject(BuildingPlanSet planSet)
/*     */   {
/*  91 */     this.planSet = planSet;
/*     */     try {
/*  93 */       this.key = ((BuildingPlan[])planSet.plans.get(0))[0].buildingKey;
/*     */     } catch (Exception e) {
/*  95 */       MLN.printException("Error when getting projet for " + this.key + ", " + planSet + ":", e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public int getChoiceWeight(EntityPlayer player)
/*     */   {
/* 102 */     if (this.planSet == null) {
/* 103 */       return 0;
/*     */     }
/*     */     
/* 106 */     if ((this.location == null) || (this.location.level < 0)) {
/* 107 */       return ((BuildingPlan[])this.planSet.plans.get(0))[0].priority;
/*     */     }
/* 109 */     if (this.location.level + 1 < ((BuildingPlan[])this.planSet.plans.get(this.location.getVariation())).length) {
/* 110 */       return ((BuildingPlan[])this.planSet.plans.get(this.location.getVariation()))[(this.location.level + 1)].priority;
/*     */     }
/* 112 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */   public String getFullName(EntityPlayer player)
/*     */   {
/* 118 */     if (this.planSet != null)
/* 119 */       return this.planSet.getFullName(player);
/* 120 */     if (this.customBuildingPlan != null) {
/* 121 */       return this.customBuildingPlan.getFullDisplayName();
/*     */     }
/* 123 */     return null;
/*     */   }
/*     */   
/*     */   public String getGameName()
/*     */   {
/* 128 */     if (this.planSet != null)
/* 129 */       return this.planSet.getGameName();
/* 130 */     if (this.customBuildingPlan != null) {
/* 131 */       return this.customBuildingPlan.getGameName();
/*     */     }
/* 133 */     return null;
/*     */   }
/*     */   
/*     */   public int getLevelsNumber(int variation)
/*     */   {
/* 138 */     if (this.planSet == null) {
/* 139 */       return 0;
/*     */     }
/*     */     
/* 142 */     if (variation >= this.planSet.plans.size()) {
/* 143 */       return 1;
/*     */     }
/* 145 */     return ((BuildingPlan[])this.planSet.plans.get(variation)).length;
/*     */   }
/*     */   
/*     */   public String getNativeName() {
/* 149 */     if (this.planSet != null)
/* 150 */       return this.planSet.getNativeName();
/* 151 */     if (this.customBuildingPlan != null) {
/* 152 */       return this.customBuildingPlan.nativeName;
/*     */     }
/* 154 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public BuildingPlan getNextBuildingPlan()
/*     */   {
/* 160 */     if (this.planSet == null) {
/* 161 */       return null;
/*     */     }
/*     */     
/* 164 */     if (this.location == null) {
/* 165 */       return this.planSet.getRandomStartingPlan();
/*     */     }
/*     */     
/* 168 */     if (this.location.level < ((BuildingPlan[])this.planSet.plans.get(this.location.getVariation())).length) {
/* 169 */       return ((BuildingPlan[])this.planSet.plans.get(this.location.getVariation()))[(this.location.level + 1)];
/*     */     }
/* 171 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public BuildingPlan getPlan(int variation, int level)
/*     */   {
/* 177 */     if (this.planSet == null) {
/* 178 */       return null;
/*     */     }
/*     */     
/* 181 */     if (variation >= this.planSet.plans.size()) {
/* 182 */       return null;
/*     */     }
/* 184 */     if (level >= ((BuildingPlan[])this.planSet.plans.get(variation)).length) {
/* 185 */       return null;
/*     */     }
/* 187 */     return ((BuildingPlan[])this.planSet.plans.get(variation))[level];
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 192 */     return "Project " + this.key + " location: " + this.location;
/*     */   }
/*     */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\building\BuildingProject.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */