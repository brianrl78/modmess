/*     */ package org.millenaire.client.gui;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import net.minecraft.client.gui.GuiButton;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import org.millenaire.client.network.ClientSender;
/*     */ import org.millenaire.common.Culture;
/*     */ import org.millenaire.common.MLN;
/*     */ import org.millenaire.common.Point;
/*     */ import org.millenaire.common.VillageType;
/*     */ import org.millenaire.common.building.Building;
/*     */ import org.millenaire.common.building.BuildingCustomPlan;
/*     */ import org.millenaire.common.building.BuildingCustomPlan.TypeRes;
/*     */ import org.millenaire.common.building.BuildingLocation;
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
/*     */ public class GuiCustomBuilding
/*     */   extends GuiText
/*     */ {
/*     */   private static final int BUTTON_CONFIRM = 1;
/*     */   private final Building townHall;
/*     */   private final Building existingBuilding;
/*     */   private final Point pos;
/*     */   private final VillageType villageType;
/*     */   private final BuildingCustomPlan customBuilding;
/*     */   private final EntityPlayer player;
/*     */   private final Map<BuildingCustomPlan.TypeRes, List<Point>> resources;
/*  40 */   ResourceLocation background = new ResourceLocation("millenaire", "textures/gui/ML_panel.png");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public GuiCustomBuilding(EntityPlayer player, Building building)
/*     */   {
/*  49 */     this.townHall = building.getTownHall();
/*  50 */     this.existingBuilding = building;
/*  51 */     this.villageType = null;
/*  52 */     this.pos = building.getPos();
/*  53 */     this.player = player;
/*  54 */     this.customBuilding = building.location.getCustomPlan();
/*  55 */     this.resources = this.customBuilding.findResources(this.townHall.worldObj, this.pos, this.townHall, building.location);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public GuiCustomBuilding(EntityPlayer player, Building th, Point p, BuildingCustomPlan customBuilding)
/*     */   {
/*  63 */     this.townHall = th;
/*  64 */     this.villageType = null;
/*  65 */     this.existingBuilding = null;
/*  66 */     this.pos = p;
/*  67 */     this.player = player;
/*  68 */     this.customBuilding = customBuilding;
/*  69 */     this.resources = customBuilding.findResources(th.worldObj, this.pos, th, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public GuiCustomBuilding(EntityPlayer player, Point p, VillageType villageType)
/*     */   {
/*  77 */     this.townHall = null;
/*  78 */     this.existingBuilding = null;
/*  79 */     this.villageType = villageType;
/*  80 */     this.pos = p;
/*  81 */     this.player = player;
/*  82 */     this.customBuilding = villageType.customCentre;
/*  83 */     this.resources = this.customBuilding.findResources(player.field_70170_p, this.pos, null, null);
/*     */   }
/*     */   
/*     */   protected void func_146284_a(GuiButton guibutton)
/*     */   {
/*  88 */     if (!guibutton.field_146124_l) {
/*  89 */       return;
/*     */     }
/*     */     
/*  92 */     if (guibutton.field_146127_k == 1) {
/*  93 */       if (this.townHall != null) {
/*  94 */         if (this.existingBuilding == null) {
/*  95 */           ClientSender.newCustomBuilding(this.player, this.townHall, this.pos, this.customBuilding.buildingKey);
/*     */         } else {
/*  97 */           ClientSender.updateCustomBuilding(this.player, this.existingBuilding);
/*     */         }
/*     */       } else {
/* 100 */         ClientSender.newVillageCreation(this.player, this.pos, this.villageType.culture.key, this.villageType.key);
/*     */       }
/* 102 */       closeWindow();
/*     */     } else {
/* 104 */       closeWindow();
/* 105 */       if (this.townHall != null) {
/* 106 */         DisplayActions.displayNewBuildingProjectGUI(this.player, this.townHall, this.pos);
/*     */       } else {
/* 108 */         DisplayActions.displayNewVillageGUI(this.player, this.pos);
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
/* 126 */     super.decrementPage();
/* 127 */     buttonPagination();
/*     */   }
/*     */   
/*     */   public int getLineSizeInPx()
/*     */   {
/* 132 */     return 195;
/*     */   }
/*     */   
/*     */   public int getPageSize()
/*     */   {
/* 137 */     return 19;
/*     */   }
/*     */   
/*     */   public ResourceLocation getPNGPath()
/*     */   {
/* 142 */     return this.background;
/*     */   }
/*     */   
/*     */   public int getXSize()
/*     */   {
/* 147 */     return 204;
/*     */   }
/*     */   
/*     */   public int getYSize()
/*     */   {
/* 152 */     return 220;
/*     */   }
/*     */   
/*     */   public void incrementPage()
/*     */   {
/* 157 */     super.incrementPage();
/* 158 */     buttonPagination();
/*     */   }
/*     */   
/*     */ 
/*     */   public void initData()
/*     */   {
/* 164 */     boolean validBuild = true;
/*     */     
/* 166 */     for (BuildingCustomPlan.TypeRes res : this.customBuilding.minResources.keySet()) {
/* 167 */       if ((!this.resources.containsKey(res)) || (((List)this.resources.get(res)).size() < ((Integer)this.customBuilding.minResources.get(res)).intValue())) {
/* 168 */         validBuild = false;
/*     */       }
/*     */     }
/*     */     
/* 172 */     List<List<GuiText.Line>> pages = new ArrayList();
/* 173 */     List<GuiText.Line> text = new ArrayList();
/*     */     
/* 175 */     if (this.townHall != null) {
/* 176 */       text.add(new GuiText.Line(this.townHall.getVillageQualifiedName()));
/*     */     } else {
/* 178 */       text.add(new GuiText.Line(MLN.string("ui.custombuilding_newvillage")));
/*     */     }
/*     */     
/* 181 */     text.add(new GuiText.Line());
/* 182 */     if (this.existingBuilding != null) {
/* 183 */       text.add(new GuiText.Line(MLN.string("ui.custombuilding_edit", new String[] { this.customBuilding.getFullDisplayName() })));
/* 184 */     } else if (validBuild) {
/* 185 */       text.add(new GuiText.Line(MLN.string("ui.custombuilding_confirm", new String[] { this.customBuilding.getFullDisplayName() })));
/*     */     } else {
/* 187 */       text.add(new GuiText.Line(MLN.string("ui.custombuilding_cantconfirm", new String[] { this.customBuilding.getFullDisplayName() })));
/*     */     }
/*     */     
/* 190 */     text.add(new GuiText.Line());
/* 191 */     text.add(new GuiText.Line(MLN.string("ui.custombuilding_radius", new String[] { "" + this.customBuilding.radius, "" + this.customBuilding.heightRadius })));
/*     */     
/* 193 */     if ((this.resources.containsKey(BuildingCustomPlan.TypeRes.SIGN)) && (((List)this.resources.get(BuildingCustomPlan.TypeRes.SIGN)).size() > 1)) {
/* 194 */       text.add(new GuiText.Line());
/* 195 */       text.add(new GuiText.Line(MLN.string("ui.custombuilding_signnumber", new String[] { "" + ((List)this.resources.get(BuildingCustomPlan.TypeRes.SIGN)).size() })));
/*     */     }
/*     */     
/* 198 */     text.add(new GuiText.Line());
/* 199 */     text.add(new GuiText.Line(MLN.string("ui.custombuilding_resneededintro")));
/* 200 */     text.add(new GuiText.Line());
/* 201 */     for (BuildingCustomPlan.TypeRes res : this.customBuilding.minResources.keySet())
/*     */     {
/* 203 */       int resFound = 0;
/* 204 */       if (this.resources.containsKey(res)) {
/* 205 */         resFound = ((List)this.resources.get(res)).size();
/*     */       }
/*     */       
/* 208 */       text.add(new GuiText.Line(MLN.string("ui.custombuilding_resneeded", new String[] { MLN.string("custombuilding." + res.key), "" + resFound, "" + this.customBuilding.minResources.get(res), "" + this.customBuilding.maxResources.get(res) })));
/*     */     }
/*     */     
/*     */ 
/* 212 */     text.add(new GuiText.Line());
/*     */     
/* 214 */     if (validBuild) {
/* 215 */       text.add(new GuiText.Line(new GuiText.MillGuiButton(MLN.string("ui.close"), 0), new GuiText.MillGuiButton(MLN.string("ui.confirm"), 1)));
/*     */     } else {
/* 217 */       text.add(new GuiText.Line(new GuiText.MillGuiButton(MLN.string("ui.close"), 0)));
/*     */     }
/*     */     
/* 220 */     pages.add(text);
/*     */     
/* 222 */     this.descText = adjustText(pages);
/*     */   }
/*     */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\client\gui\GuiCustomBuilding.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */