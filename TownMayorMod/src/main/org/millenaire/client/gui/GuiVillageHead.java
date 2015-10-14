/*     */ package org.millenaire.client.gui;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import org.millenaire.client.network.ClientSender;
/*     */ import org.millenaire.common.Culture;
/*     */ import org.millenaire.common.MLN;
/*     */ import org.millenaire.common.MillVillager;
/*     */ import org.millenaire.common.Point;
/*     */ import org.millenaire.common.UserProfile;
/*     */ import org.millenaire.common.building.Building;
/*     */ import org.millenaire.common.building.BuildingPlan;
/*     */ import org.millenaire.common.building.BuildingProject;
/*     */ import org.millenaire.common.building.BuildingProject.EnumProjects;
/*     */ import org.millenaire.common.core.MillCommonUtilities;
/*     */ 
/*     */ public class GuiVillageHead extends GuiText
/*     */ {
/*     */   public static final int ACTION_ICON_LENGTH = 15;
/*     */   public static final int ACTION_ICON_HEIGHT = 13;
/*     */   private final MillVillager chief;
/*     */   private final EntityPlayer player;
/*     */   
/*     */   public static class GuiButtonChief extends GuiText.MillGuiButton
/*     */   {
/*     */     public static final String PRAISE = "PRAISE";
/*     */     public static final String SLANDER = "SLANDER";
/*     */     public static final String BUILDING = "BUILDING";
/*     */     public static final String VILLAGE_SCROLL = "VILLAGE_SCROLL";
/*     */     public static final String CULTURE_CONTROL = "CULTURE_CONTROL";
/*     */     public static final String CROP = "CROP";
/*     */     public Point village;
/*     */     public String value;
/*     */     public String key;
/*     */     
/*     */     public GuiButtonChief(String key, String label)
/*     */     {
/*  41 */       super(0, 0, 0, 0, label);
/*  42 */       this.key = key;
/*     */     }
/*     */     
/*     */     public GuiButtonChief(String key, String label, Point v) {
/*  46 */       super(0, 0, 0, 0, label);
/*  47 */       this.village = v;
/*  48 */       this.key = key;
/*     */     }
/*     */     
/*     */     public GuiButtonChief(String key, String label, String plan) {
/*  52 */       super(0, 0, 0, 0, label);
/*  53 */       this.key = key;
/*  54 */       this.value = plan;
/*     */     }
/*     */   }
/*     */   
/*     */   private class VillageRelation implements Comparable<VillageRelation>
/*     */   {
/*     */     int relation;
/*     */     Point pos;
/*     */     String name;
/*     */     
/*     */     VillageRelation(Point p, int r, String name) {
/*  65 */       this.relation = r;
/*  66 */       this.pos = p;
/*  67 */       this.name = name;
/*     */     }
/*     */     
/*     */     public int compareTo(VillageRelation arg0)
/*     */     {
/*  72 */       return this.name.compareTo(arg0.name);
/*     */     }
/*     */     
/*     */     public boolean equals(Object o)
/*     */     {
/*  77 */       if ((o == null) || (!(o instanceof VillageRelation))) {
/*  78 */         return false;
/*     */       }
/*     */       
/*  81 */       return this.pos.equals(((VillageRelation)o).pos);
/*     */     }
/*     */     
/*     */     public int hashCode()
/*     */     {
/*  86 */       return this.pos.hashCode();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  97 */   ResourceLocation background = new ResourceLocation("millenaire", "textures/gui/ML_village_chief.png");
/*     */   
/*     */   public GuiVillageHead(EntityPlayer player, MillVillager chief) {
/* 100 */     this.chief = chief;
/* 101 */     this.player = player;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void func_146284_a(net.minecraft.client.gui.GuiButton guibutton)
/*     */   {
/* 108 */     if ((guibutton instanceof GuiButtonChief))
/*     */     {
/* 110 */       GuiButtonChief gb = (GuiButtonChief)guibutton;
/*     */       
/* 112 */       boolean close = false;
/*     */       
/* 114 */       if (gb.key == "PRAISE") {
/* 115 */         ClientSender.villageChiefPerformDiplomacy(this.player, this.chief, gb.village, true);
/* 116 */       } else if (gb.key == "SLANDER") {
/* 117 */         ClientSender.villageChiefPerformDiplomacy(this.player, this.chief, gb.village, false);
/* 118 */       } else if (gb.key == "VILLAGE_SCROLL") {
/* 119 */         ClientSender.villageChiefPerformVillageScroll(this.player, this.chief);
/* 120 */         close = true;
/* 121 */       } else if (gb.key == "CULTURE_CONTROL") {
/* 122 */         ClientSender.villageChiefPerformCultureControl(this.player, this.chief);
/* 123 */         close = true;
/* 124 */       } else if (gb.key == "BUILDING") {
/* 125 */         ClientSender.villageChiefPerformBuilding(this.player, this.chief, gb.value);
/* 126 */         close = true;
/* 127 */       } else if (gb.key == "CROP") {
/* 128 */         ClientSender.villageChiefPerformCrop(this.player, this.chief, gb.value);
/* 129 */         close = true;
/*     */       }
/*     */       
/* 132 */       if (close) {
/* 133 */         closeWindow();
/*     */       } else {
/* 135 */         this.descText = getData();
/* 136 */         buttonPagination();
/*     */       }
/*     */     }
/*     */     
/* 140 */     super.func_146284_a(guibutton);
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
/*     */ 
/*     */   public boolean func_73868_f()
/*     */   {
/* 155 */     return false;
/*     */   }
/*     */   
/*     */   private List<List<GuiText.Line>> getData()
/*     */   {
/* 160 */     List<GuiText.Line> text = new ArrayList();
/*     */     
/* 162 */     String game = "";
/* 163 */     if (this.chief.getGameOccupationName(this.player.getDisplayName()).length() > 0) {
/* 164 */       game = " (" + this.chief.getGameOccupationName(this.player.getDisplayName()) + ")";
/*     */     }
/*     */     
/* 167 */     text.add(new GuiText.Line(this.chief.getName() + ", " + this.chief.getNativeOccupationName() + game, false));
/* 168 */     text.add(new GuiText.Line(MLN.string("ui.villagechief").replace("<0>", this.chief.getTownHall().getVillageQualifiedName())));
/* 169 */     text.add(new GuiText.Line());
/*     */     
/* 171 */     String col = "";
/*     */     
/* 173 */     if (this.chief.getTownHall().getReputation(this.player.getDisplayName()) >= 32768) {
/* 174 */       col = "<darkgreen>";
/* 175 */     } else if (this.chief.getTownHall().getReputation(this.player.getDisplayName()) >= 4096) {
/* 176 */       col = "<darkblue>";
/* 177 */     } else if (this.chief.getTownHall().getReputation(this.player.getDisplayName()) < 65280) {
/* 178 */       col = "<darkred>";
/* 179 */     } else if (this.chief.getTownHall().getReputation(this.player.getDisplayName()) < 0) {
/* 180 */       col = "<lightred>";
/*     */     }
/*     */     
/* 183 */     text.add(new GuiText.Line(col + MLN.string("ui.yourstatus") + ": " + this.chief.getTownHall().getReputationLevelLabel(this.player.getDisplayName()), false));
/* 184 */     text.add(new GuiText.Line(col + this.chief.getTownHall().getReputationLevelDesc(this.player.getDisplayName()).replaceAll("\\$name", this.player.getDisplayName())));
/* 185 */     text.add(new GuiText.Line());
/* 186 */     text.add(new GuiText.Line(MLN.string("ui.possiblehousing") + ":"));
/* 187 */     text.add(new GuiText.Line());
/* 188 */     Map<BuildingProject.EnumProjects, List<BuildingProject>> projects = this.chief.getTownHall().buildingProjects;
/*     */     
/* 190 */     UserProfile profile = org.millenaire.common.forge.Mill.proxy.getClientProfile();
/*     */     
/* 192 */     int reputation = this.chief.getTownHall().getReputation(this.player.getDisplayName());
/*     */     
/* 194 */     for (BuildingProject.EnumProjects ep : BuildingProject.EnumProjects.values()) {
/* 195 */       if (this.chief.getTownHall().buildingProjects.containsKey(ep)) {
/* 196 */         List<BuildingProject> projectsLevel = (List)this.chief.getTownHall().buildingProjects.get(ep);
/* 197 */         for (BuildingProject project : projectsLevel) {
/* 198 */           if (project.planSet != null) {
/* 199 */             BuildingPlan plan = project.planSet.getRandomStartingPlan();
/* 200 */             if ((plan != null) && (plan.price > 0) && (!plan.isgift)) {
/* 201 */               String status = "";
/*     */               
/* 203 */               boolean buyButton = false;
/*     */               
/* 205 */               if (project.location != null) {
/* 206 */                 status = MLN.string("ui.alreadybuilt") + ".";
/* 207 */               } else if (this.chief.getTownHall().buildingsBought.contains(project.key)) {
/* 208 */                 status = MLN.string("ui.alreadyrequested") + ".";
/* 209 */               } else if (plan.reputation > reputation) {
/* 210 */                 status = MLN.string("ui.notavailableyet") + ".";
/* 211 */               } else if (plan.price > MillCommonUtilities.countMoney(this.player.field_71071_by)) {
/* 212 */                 status = MLN.string("ui.youaremissing", new String[] { "" + MillCommonUtilities.getShortPrice(plan.price - MillCommonUtilities.countMoney(this.player.field_71071_by)) });
/*     */               } else {
/* 214 */                 status = MLN.string("ui.available") + ".";
/* 215 */                 buyButton = true;
/*     */               }
/* 217 */               text.add(new GuiText.Line(plan.nativeName + ": " + status, false));
/*     */               
/* 219 */               if (buyButton) {
/* 220 */                 text.add(new GuiText.Line(new GuiButtonChief("BUILDING", MLN.string("ui.buybuilding", new String[] { plan.nativeName, MillCommonUtilities.getShortPrice(plan.price) }), plan.buildingKey)));
/*     */                 
/* 222 */                 text.add(new GuiText.Line(false));
/* 223 */                 text.add(new GuiText.Line());
/*     */               }
/* 225 */             } else if ((plan.isgift) && (MLN.bonusEnabled) && (!org.millenaire.common.forge.Mill.proxy.isTrueClient())) {
/* 226 */               String status = "";
/*     */               
/* 228 */               boolean buyButton = false;
/*     */               
/* 230 */               if (project.location != null) {
/* 231 */                 status = MLN.string("ui.alreadybuilt") + ".";
/* 232 */               } else if (this.chief.getTownHall().buildingsBought.contains(project.key)) {
/* 233 */                 status = MLN.string("ui.alreadyrequested") + ".";
/*     */               } else {
/* 235 */                 status = MLN.string("ui.bonusavailable") + ".";
/* 236 */                 buyButton = true;
/*     */               }
/* 238 */               text.add(new GuiText.Line(plan.nativeName + ": " + status, false));
/*     */               
/* 240 */               if (buyButton) {
/* 241 */                 text.add(new GuiText.Line(new GuiButtonChief("BUILDING", MLN.string("ui.buybonusbuilding", new String[] { plan.nativeName }), plan.buildingKey)));
/* 242 */                 text.add(new GuiText.Line(false));
/* 243 */                 text.add(new GuiText.Line());
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 251 */     if (8192 > reputation) {
/* 252 */       text.add(new GuiText.Line(MLN.string("ui.scrollsnoreputation")));
/* 253 */     } else if (128 > MillCommonUtilities.countMoney(this.player.field_71071_by)) {
/* 254 */       text.add(new GuiText.Line(MLN.string("ui.scrollsnotenoughmoney", new String[] { "" + MillCommonUtilities.getShortPrice(128 - MillCommonUtilities.countMoney(this.player.field_71071_by)) })));
/*     */     } else {
/* 256 */       text.add(new GuiText.Line(MLN.string("ui.scrollsok"), false));
/* 257 */       text.add(new GuiText.Line(new GuiButtonChief("VILLAGE_SCROLL", MLN.string("ui.buyscroll"), MillCommonUtilities.getShortPrice(128))));
/* 258 */       text.add(new GuiText.Line());
/* 259 */       text.add(new GuiText.Line());
/*     */     }
/*     */     
/* 262 */     if (this.chief.getCulture().knownCrops.size() > 0) {
/* 263 */       text.add(new GuiText.Line(MLN.string("ui.cropsknown")));
/* 264 */       text.add(new GuiText.Line());
/*     */       
/* 266 */       for (String crop : this.chief.getCulture().knownCrops) {
/* 267 */         if (profile.isTagSet("cropplanting_" + crop)) {
/* 268 */           text.add(new GuiText.Line(MLN.string("ui.cropknown", new String[] { MLN.string("item." + crop) })));
/* 269 */         } else if (8192 > reputation) {
/* 270 */           text.add(new GuiText.Line(MLN.string("ui.cropinsufficientreputation", new String[] { MLN.string("item." + crop) })));
/* 271 */         } else if (512 > MillCommonUtilities.countMoney(this.player.field_71071_by)) {
/* 272 */           text.add(new GuiText.Line(MLN.string("ui.cropnotenoughmoney", new String[] { MLN.string("item." + crop), "" + MillCommonUtilities.getShortPrice(512 - MillCommonUtilities.countMoney(this.player.field_71071_by)) })));
/*     */         }
/*     */         else {
/* 275 */           text.add(new GuiText.Line(MLN.string("ui.cropoktolearn", new String[] { MLN.string("item." + crop) }), false));
/* 276 */           text.add(new GuiText.Line(new GuiButtonChief("CROP", MLN.string("ui.croplearn", new String[] { "" + MillCommonUtilities.getShortPrice(512) }), crop)));
/* 277 */           text.add(new GuiText.Line(false));
/* 278 */           text.add(new GuiText.Line());
/*     */         }
/*     */       }
/*     */       
/* 282 */       text.add(new GuiText.Line());
/*     */     }
/*     */     
/* 285 */     if (profile.isTagSet("culturecontrol_" + this.chief.getCulture().key)) {
/* 286 */       text.add(new GuiText.Line(MLN.string("ui.control_alreadydone", new String[] { this.chief.getCulture().getCultureGameName() })));
/* 287 */     } else if (131072 > reputation) {
/* 288 */       text.add(new GuiText.Line(MLN.string("ui.control_noreputation", new String[] { this.chief.getCulture().getCultureGameName() })));
/*     */     } else {
/* 290 */       text.add(new GuiText.Line(MLN.string("ui.control_ok", new String[] { this.chief.getCulture().getCultureGameName() }), false));
/* 291 */       text.add(new GuiText.Line(new GuiButtonChief("CULTURE_CONTROL", MLN.string("ui.control_get"))));
/* 292 */       text.add(new GuiText.Line(false));
/* 293 */       text.add(new GuiText.Line());
/*     */     }
/*     */     
/* 296 */     List<List<GuiText.Line>> pages = new ArrayList();
/* 297 */     pages.add(text);
/*     */     
/* 299 */     text = new ArrayList();
/*     */     
/* 301 */     text.add(new GuiText.Line(MLN.string("ui.relationlist")));
/* 302 */     text.add(new GuiText.Line());
/* 303 */     text.add(new GuiText.Line(MLN.string("ui.relationpoints", new String[] { "" + profile.getDiplomacyPoints(this.chief.getTownHall()) })));
/* 304 */     text.add(new GuiText.Line());
/*     */     
/* 306 */     List<VillageRelation> relations = new ArrayList();
/*     */     
/* 308 */     for (Point p : this.chief.getTownHall().getKnownVillages()) {
/* 309 */       Building b = this.chief.getTownHall().mw.getBuilding(p);
/* 310 */       if (b != null) {
/* 311 */         relations.add(new VillageRelation(p, this.chief.getTownHall().getRelationWithVillage(p), b.getVillageQualifiedName()));
/*     */       }
/*     */     }
/*     */     
/* 315 */     java.util.Collections.sort(relations);
/*     */     
/* 317 */     for (VillageRelation vr : relations) {
/* 318 */       Building b = this.chief.getTownHall().mw.getBuilding(vr.pos);
/* 319 */       if (b != null) {
/* 320 */         col = "";
/*     */         
/* 322 */         if (vr.relation > 70) {
/* 323 */           col = "<darkgreen>";
/* 324 */         } else if (vr.relation > 30) {
/* 325 */           col = "<darkblue>";
/* 326 */         } else if (vr.relation <= -90) {
/* 327 */           col = "<darkred>";
/* 328 */         } else if (vr.relation <= -30) {
/* 329 */           col = "<lightred>";
/*     */         }
/*     */         
/* 332 */         text.add(new GuiText.Line(col + MLN.string("ui.villagerelations", new String[] { b.getVillageQualifiedName(), b.villageType.name, b.culture.getCultureGameName(), MLN.string(MillCommonUtilities.getRelationName(vr.relation)) + " (" + vr.relation + ")" }), false));
/*     */         
/*     */ 
/*     */ 
/* 336 */         GuiButtonChief praise = null;
/* 337 */         GuiButtonChief slander = null;
/*     */         
/* 339 */         if ((profile.getDiplomacyPoints(this.chief.getTownHall()) > 0) && (reputation > 0)) {
/* 340 */           if (vr.relation < 100) {
/* 341 */             praise = new GuiButtonChief("PRAISE", MLN.string("ui.relationpraise"), vr.pos);
/*     */           }
/* 343 */           if (vr.relation > -100) {
/* 344 */             slander = new GuiButtonChief("SLANDER", MLN.string("ui.relationslander"), vr.pos);
/*     */           }
/* 346 */           text.add(new GuiText.Line(praise, slander));
/* 347 */           text.add(new GuiText.Line(false));
/* 348 */           text.add(new GuiText.Line());
/* 349 */           text.add(new GuiText.Line());
/*     */         } else {
/* 351 */           text.add(new GuiText.Line("<darkred>" + MLN.string("ui.villagerelationsnobutton")));
/* 352 */           text.add(new GuiText.Line());
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 357 */     pages.add(text);
/*     */     
/* 359 */     text = new ArrayList();
/* 360 */     text.add(new GuiText.Line(MLN.string("ui.relationhelp")));
/* 361 */     pages.add(text);
/*     */     
/* 363 */     return adjustText(pages);
/*     */   }
/*     */   
/*     */   public int getLineSizeInPx()
/*     */   {
/* 368 */     return 240;
/*     */   }
/*     */   
/*     */   public int getPageSize()
/*     */   {
/* 373 */     return 16;
/*     */   }
/*     */   
/*     */   public ResourceLocation getPNGPath()
/*     */   {
/* 378 */     return this.background;
/*     */   }
/*     */   
/*     */   public int getXSize()
/*     */   {
/* 383 */     return 256;
/*     */   }
/*     */   
/*     */   public int getYSize()
/*     */   {
/* 388 */     return 200;
/*     */   }
/*     */   
/*     */   public void initData()
/*     */   {
/* 393 */     this.descText = getData();
/*     */   }
/*     */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\client\gui\GuiVillageHead.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */