/*     */ package org.millenaire.client.gui;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.client.gui.GuiButton;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import org.millenaire.client.network.ClientSender;
/*     */ import org.millenaire.common.MLN;
/*     */ import org.millenaire.common.building.Building;
/*     */ import org.millenaire.common.building.BuildingLocation;
/*     */ import org.millenaire.common.building.BuildingPlanSet;
/*     */ import org.millenaire.common.building.BuildingProject;
/*     */ 
/*     */ public class GuiControlledProjects extends GuiText
/*     */ {
/*     */   private final Building townHall;
/*     */   private List<BuildingProject> projects;
/*     */   private final net.minecraft.entity.player.EntityPlayer player;
/*     */   
/*     */   public static class GuiButtonProject extends GuiText.MillGuiButton
/*     */   {
/*     */     public static final int ALLOW_UPGRADES = 1;
/*     */     public static final int FORBID_UPGRADES = 2;
/*     */     public static final int CANCEL_BUILDING = 3;
/*     */     public BuildingProject project;
/*     */     
/*     */     public GuiButtonProject(BuildingProject project, int i, String s)
/*     */     {
/*  28 */       super(0, 0, 0, 0, s);
/*  29 */       this.project = project;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  37 */   ResourceLocation background = new ResourceLocation("millenaire", "textures/gui/ML_panel.png");
/*     */   
/*     */   public GuiControlledProjects(net.minecraft.entity.player.EntityPlayer player, Building th)
/*     */   {
/*  41 */     this.townHall = th;
/*  42 */     this.projects = this.townHall.getFlatProjectList();
/*  43 */     this.player = player;
/*     */   }
/*     */   
/*     */   protected void func_146284_a(GuiButton guibutton)
/*     */   {
/*  48 */     if (!guibutton.field_146124_l) {
/*  49 */       return;
/*     */     }
/*     */     
/*  52 */     GuiButtonProject gbp = (GuiButtonProject)guibutton;
/*     */     
/*  54 */     if (gbp.field_146127_k == 1) {
/*  55 */       ClientSender.controlledBuildingsToggleUpgrades(this.player, this.townHall, gbp.project, true);
/*  56 */     } else if (gbp.field_146127_k == 2) {
/*  57 */       ClientSender.controlledBuildingsToggleUpgrades(this.player, this.townHall, gbp.project, false);
/*  58 */     } else if (gbp.field_146127_k == 3) {
/*  59 */       ClientSender.controlledBuildingsForgetBuilding(this.player, this.townHall, gbp.project);
/*     */       
/*  61 */       this.projects = this.townHall.getFlatProjectList();
/*     */     }
/*     */     
/*  64 */     fillData();
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
/*     */   private void fillData()
/*     */   {
/*  78 */     List<GuiText.Line> text = new java.util.ArrayList();
/*     */     
/*  80 */     text.add(new GuiText.Line(this.townHall.getVillageQualifiedName(), false));
/*  81 */     text.add(new GuiText.Line(false));
/*  82 */     text.add(new GuiText.Line(MLN.string("ui.controlbuildingprojects")));
/*  83 */     text.add(new GuiText.Line());
/*     */     
/*  85 */     for (int i = 0; i < this.projects.size(); i++) {
/*  86 */       BuildingProject project = (BuildingProject)this.projects.get(i);
/*     */       
/*  88 */       if (project.planSet != null)
/*     */       {
/*     */         String status;
/*     */         String status;
/*  92 */         if (project.location.level < 0) {
/*  93 */           status = MLN.string("ui.notyetbuilt");
/*     */         } else {
/*  95 */           status = MLN.string("ui.level") + ": " + (project.location.level + 1) + "/" + ((org.millenaire.common.building.BuildingPlan[])project.planSet.plans.get(project.location.getVariation())).length;
/*     */         }
/*     */         
/*  98 */         text.add(new GuiText.Line(project.getFullName(this.player) + " (" + (char)(65 + project.location.getVariation()) + "):", false));
/*  99 */         text.add(new GuiText.Line(status + ", " + this.townHall.getPos().distanceDirectionShort(project.location.pos), false));
/*     */         
/* 101 */         int nbInhabitants = 0;
/*     */         
/* 103 */         if ((project.location != null) && (project.location.chestPos != null)) {
/* 104 */           for (org.millenaire.common.VillagerRecord vr : this.townHall.vrecords) {
/* 105 */             if (project.location.chestPos.equals(vr.housePos)) {
/* 106 */               nbInhabitants++;
/*     */             }
/*     */           }
/*     */         }
/*     */         
/* 111 */         text.add(new GuiText.Line(MLN.string("ui.nbinhabitants", new String[] { "" + nbInhabitants })));
/*     */         
/* 113 */         GuiText.MillGuiButton firstButton = null;
/* 114 */         GuiText.MillGuiButton secondButton = null;
/*     */         
/* 116 */         if ((project.location.level < ((org.millenaire.common.building.BuildingPlan[])project.planSet.plans.get(project.location.getVariation())).length - 1) && (((org.millenaire.common.building.BuildingPlan[])project.planSet.plans.get(project.location.getVariation())).length > 1)) {
/* 117 */           if (project.location.upgradesAllowed) {
/* 118 */             firstButton = new GuiButtonProject(project, 2, MLN.string("ui.forbidupgrades"));
/*     */           } else {
/* 120 */             firstButton = new GuiButtonProject(project, 1, MLN.string("ui.allowupgrades"));
/*     */           }
/*     */         }
/*     */         
/*     */         boolean canForget;
/*     */         boolean canForget;
/* 126 */         if ((project.location == null) || ((project.location.getBuilding(this.townHall.worldObj) != null) && (project.location.getBuilding(this.townHall.worldObj).isTownhall))) {
/* 127 */           canForget = false;
/*     */         } else {
/* 129 */           canForget = nbInhabitants == 0;
/*     */         }
/*     */         
/* 132 */         if (canForget) {
/* 133 */           secondButton = new GuiButtonProject(project, 3, MLN.string("ui.cancelbuilding"));
/*     */         }
/*     */         
/* 136 */         text.add(new GuiText.Line(firstButton, secondButton));
/* 137 */         text.add(new GuiText.Line(false));
/* 138 */         text.add(new GuiText.Line());
/* 139 */         text.add(new GuiText.Line());
/*     */       }
/*     */     }
/*     */     
/* 143 */     List<List<GuiText.Line>> pages = new java.util.ArrayList();
/* 144 */     pages.add(text);
/* 145 */     this.descText = adjustText(pages);
/*     */     
/* 147 */     buttonPagination();
/*     */   }
/*     */   
/*     */   public int getLineSizeInPx()
/*     */   {
/* 152 */     return 195;
/*     */   }
/*     */   
/*     */   public int getPageSize()
/*     */   {
/* 157 */     return 19;
/*     */   }
/*     */   
/*     */   public ResourceLocation getPNGPath()
/*     */   {
/* 162 */     return this.background;
/*     */   }
/*     */   
/*     */   public int getXSize()
/*     */   {
/* 167 */     return 204;
/*     */   }
/*     */   
/*     */   public int getYSize()
/*     */   {
/* 172 */     return 220;
/*     */   }
/*     */   
/*     */   public void initData()
/*     */   {
/* 177 */     fillData();
/*     */   }
/*     */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\client\gui\GuiControlledProjects.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */