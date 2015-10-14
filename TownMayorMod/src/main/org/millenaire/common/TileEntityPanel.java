/*      */ package org.millenaire.common;
/*      */ 
/*      */ import java.util.ArrayList;
/*      */ import java.util.HashMap;
/*      */ import java.util.List;
/*      */ import net.minecraft.entity.player.EntityPlayer;
/*      */ import org.millenaire.common.building.Building;
/*      */ import org.millenaire.common.building.BuildingLocation;
/*      */ import org.millenaire.common.building.BuildingPlan;
/*      */ import org.millenaire.common.building.BuildingProject;
/*      */ import org.millenaire.common.forge.Mill;
/*      */ 
/*      */ public class TileEntityPanel extends net.minecraft.tileentity.TileEntitySign
/*      */ {
/*      */   public static final int VILLAGE_MAP = 1;
/*      */   public static final int etatCivil = 1;
/*      */   public static final int constructions = 2;
/*      */   public static final int projects = 3;
/*      */   public static final int controlledProjects = 4;
/*      */   public static final int house = 5;
/*      */   public static final int resources = 6;
/*      */   public static final int archives = 7;
/*      */   public static final int villageMap = 8;
/*      */   public static final int military = 9;
/*      */   public static final int tradeGoods = 10;
/*      */   public static final int innVisitors = 11;
/*      */   public static final int marketMerchants = 12;
/*      */   public static final int controlledMilitary = 13;
/*      */   
/*      */   public static class PanelPacketInfo
/*      */   {
/*      */     Point pos;
/*      */     Point buildingPos;
/*      */     String[][] lines;
/*      */     long villager_id;
/*      */     int panelType;
/*      */     
/*      */     public PanelPacketInfo(Point pos, String[][] lines, Point buildingPos, int panelType, long village_id)
/*      */     {
/*   40 */       this.pos = pos;
/*   41 */       this.lines = lines;
/*   42 */       this.buildingPos = buildingPos;
/*   43 */       this.panelType = panelType;
/*   44 */       this.villager_id = village_id;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static void addProjectToList(EntityPlayer player, BuildingProject project, Building townHall, List<String> page)
/*      */   {
/*   69 */     if (project.planSet != null) {
/*   70 */       if ((project.location == null) || (project.location.level < 0)) {
/*   71 */         BuildingPlan plan = project.planSet.getRandomStartingPlan();
/*      */         
/*   73 */         page.add(plan.getFullDisplayName() + ": " + MLN.string("panels.notyetbuilt") + ".");
/*      */       }
/*   75 */       else if (project.location.level + 1 < project.getLevelsNumber(project.location.getVariation()))
/*      */       {
/*   77 */         BuildingPlan plan = project.getPlan(project.location.getVariation(), project.location.level + 1);
/*      */         
/*   79 */         BuildingLocation l = project.location;
/*   80 */         page.add(plan.getFullDisplayName() + " (" + net.minecraft.util.MathHelper.func_76128_c(l.pos.distanceTo(townHall.getPos())) + "m " + townHall.getPos().directionToShort(l.pos) + "): " + MLN.string("panels.nbupgradesleft", new String[] { "" + (project.getLevelsNumber(project.location.getVariation()) - project.location.level - 1) }));
/*      */       }
/*      */       else {
/*   83 */         BuildingPlan plan = project.getPlan(project.location.getVariation(), project.location.level);
/*      */         
/*   85 */         BuildingLocation l = project.location;
/*   86 */         page.add(plan.getFullDisplayName() + " (" + net.minecraft.util.MathHelper.func_76128_c(l.pos.distanceTo(townHall.getPos())) + "m " + townHall.getPos().directionToShort(l.pos) + "): " + MLN.string("panels.finished") + ".");
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static List<List<String>> generateArchives(EntityPlayer player, Building townHall, long villager_id)
/*      */   {
/*   95 */     if (townHall == null) {
/*   96 */       return null;
/*      */     }
/*      */     
/*   99 */     VillagerRecord vr = townHall.getVillagerRecordById(villager_id);
/*      */     
/*  101 */     if (vr == null) {
/*  102 */       return null;
/*      */     }
/*      */     
/*  105 */     List<List<String>> text = new ArrayList();
/*  106 */     List<String> page = new ArrayList();
/*      */     
/*  108 */     page.add(vr.getName());
/*  109 */     page.add(vr.getGameOccupation(player.getDisplayName()));
/*  110 */     page.add("");
/*      */     
/*  112 */     if ((vr.mothersName != null) && (vr.mothersName.length() > 0)) {
/*  113 */       page.add(MLN.string("panels.mother") + ": " + vr.mothersName);
/*      */     }
/*  115 */     if ((vr.fathersName != null) && (vr.fathersName.length() > 0)) {
/*  116 */       page.add(MLN.string("panels.father") + ": " + vr.fathersName);
/*      */     }
/*  118 */     if ((vr.spousesName != null) && (vr.spousesName.length() > 0)) {
/*  119 */       page.add(MLN.string("panels.spouse") + ": " + vr.spousesName);
/*      */     }
/*      */     
/*  122 */     page.add("");
/*      */     
/*  124 */     MillVillager villager = null;
/*      */     
/*  126 */     for (MillVillager v : townHall.villagers) {
/*  127 */       if (v.villager_id == vr.id) {
/*  128 */         villager = v;
/*      */       }
/*      */     }
/*      */     
/*  132 */     page.add("");
/*      */     
/*  134 */     if (villager == null) {
/*  135 */       if (vr.killed) {
/*  136 */         page.add(MLN.string("panels.dead"));
/*  137 */       } else if (vr.awayraiding) {
/*  138 */         page.add(MLN.string("panels.awayraiding"));
/*  139 */       } else if (vr.awayraiding) {
/*  140 */         page.add(MLN.string("panels.awayhired"));
/*  141 */       } else if ((vr.raidingVillage) && (townHall.worldObj.func_72820_D() < vr.raiderSpawn + 500L)) {
/*  142 */         page.add(MLN.string("panels.invaderincoming"));
/*      */       } else {
/*  144 */         page.add(MLN.string("panels.missing"));
/*      */       }
/*      */     } else {
/*  147 */       String occupation = "";
/*      */       
/*  149 */       if ((villager.goalKey != null) && (org.millenaire.common.goal.Goal.goals.containsKey(villager.goalKey))) {
/*  150 */         occupation = ((org.millenaire.common.goal.Goal)org.millenaire.common.goal.Goal.goals.get(villager.goalKey)).gameName(villager);
/*      */       }
/*  152 */       page.add(MLN.string("panels.currentoccupation") + ": " + occupation);
/*      */     }
/*      */     
/*  155 */     text.add(page);
/*  156 */     return text;
/*      */   }
/*      */   
/*      */   public static List<List<String>> generateConstructions(EntityPlayer player, Building townHall)
/*      */   {
/*  161 */     List<String> page = new ArrayList();
/*      */     
/*  163 */     page.add(MLN.string("panels.constructions") + " : " + townHall.getVillageQualifiedName());
/*      */     
/*  165 */     page.add("");
/*      */     
/*  167 */     for (org.millenaire.common.building.BuildingProject.EnumProjects ep : org.millenaire.common.building.BuildingProject.EnumProjects.values()) {
/*  168 */       if (townHall.buildingProjects.containsKey(ep)) {
/*  169 */         List<BuildingProject> projectsLevel = (List)townHall.buildingProjects.get(ep);
/*  170 */         for (BuildingProject project : projectsLevel) {
/*  171 */           if (project.location != null) {
/*  172 */             String level = null;
/*  173 */             if (project.location.level < 0) {
/*  174 */               level = MLN.string("ui.notyetbuilt");
/*      */             }
/*  176 */             if (project.location.level > 0) {
/*  177 */               level = MLN.string("panels.upgrade") + " " + project.location.level;
/*      */             }
/*      */             
/*  180 */             List<String> effects = project.location.getBuildingEffects(townHall.worldObj);
/*      */             
/*  182 */             String effect = null;
/*  183 */             if (effects.size() > 0) {
/*  184 */               effect = "";
/*  185 */               for (String s : effects) {
/*  186 */                 if (effect.length() > 0) {
/*  187 */                   effect = effect + ", ";
/*      */                 }
/*  189 */                 effect = effect + s;
/*      */               }
/*      */             }
/*      */             
/*  193 */             page.add(project.location.getFullDisplayName() + ": " + net.minecraft.util.MathHelper.func_76128_c(project.location.pos.distanceTo(townHall.getPos())) + "m " + townHall.getPos().directionToShort(project.location.pos));
/*      */             
/*  195 */             if (level != null) {
/*  196 */               page.add(level);
/*      */             }
/*  198 */             if (effect != null) {
/*  199 */               page.add(effect);
/*      */             }
/*  201 */             page.add("");
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  207 */     List<List<String>> text = new ArrayList();
/*  208 */     text.add(page);
/*      */     
/*  210 */     return text;
/*      */   }
/*      */   
/*      */   public static List<List<String>> generateEtatCivil(EntityPlayer player, Building townHall)
/*      */   {
/*  215 */     if (townHall == null) {
/*  216 */       return null;
/*      */     }
/*      */     
/*  219 */     List<List<String>> text = new ArrayList();
/*  220 */     List<String> page = new ArrayList();
/*  221 */     List<String> visitorsPage = new ArrayList();
/*      */     
/*  223 */     page.add(MLN.string("ui.population") + " " + townHall.getVillageQualifiedName());
/*  224 */     page.add("");
/*      */     
/*  226 */     visitorsPage.add(MLN.string("panels.visitors") + ":");
/*  227 */     visitorsPage.add("");
/*      */     
/*  229 */     for (VillagerRecord vr : townHall.vrecords) {
/*  230 */       int nbFound = 0;
/*      */       
/*  232 */       boolean belongsToVillage = true;
/*      */       
/*  234 */       for (MillVillager villager : townHall.villagers) {
/*  235 */         if (villager.villager_id == vr.id) {
/*  236 */           nbFound++;
/*  237 */           belongsToVillage = !villager.isVisitor();
/*      */         }
/*      */       }
/*  240 */       String error = "";
/*      */       
/*  242 */       if (nbFound == 0) {
/*  243 */         if (vr.killed) {
/*  244 */           error = " (" + MLN.string("panels.dead").toLowerCase() + ")";
/*  245 */         } else if (vr.awayraiding) {
/*  246 */           error = " (" + MLN.string("panels.awayraiding").toLowerCase() + ")";
/*  247 */         } else if (vr.awayhired) {
/*  248 */           error = " (" + MLN.string("panels.awayhired").toLowerCase() + ")";
/*  249 */         } else if ((vr.raidingVillage) && (townHall.worldObj.func_72820_D() < vr.raiderSpawn + 500L)) {
/*  250 */           error = " (" + MLN.string("panels.invaderincoming").toLowerCase() + ")";
/*  251 */         } else if (vr.raidingVillage) {
/*  252 */           error = " (" + MLN.string("panels.raider").toLowerCase() + ")";
/*      */         } else {
/*  254 */           error = " (" + MLN.string("panels.missing").toLowerCase() + ")";
/*      */         }
/*      */         
/*  257 */         if ((MLN.DEV) && (Mill.serverWorlds.size() > 0))
/*      */         {
/*  259 */           Building thServer = ((MillWorld)Mill.serverWorlds.get(0)).getBuilding(townHall.getPos());
/*      */           
/*  261 */           if (thServer != null) {
/*  262 */             int nbOnServer = 0;
/*  263 */             for (MillVillager villager : thServer.villagers) {
/*  264 */               if (villager.villager_id == vr.id) {
/*  265 */                 nbOnServer++;
/*      */               }
/*      */             }
/*      */             
/*  269 */             error = error + " nbOnServer:" + nbOnServer;
/*      */           }
/*      */         }
/*      */       }
/*  273 */       else if (nbFound > 1) {
/*  274 */         error = " (" + MLN.string("panels.multiple", new String[] { "" + nbFound }).toLowerCase() + ")";
/*      */       }
/*      */       
/*  277 */       if (belongsToVillage) {
/*  278 */         page.add(vr.getName() + ", " + vr.getGameOccupation(player.getDisplayName()).toLowerCase() + error);
/*      */       } else {
/*  280 */         visitorsPage.add(vr.getName() + ", " + vr.getGameOccupation(player.getDisplayName()).toLowerCase() + error);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  285 */     if ((MLN.DEV) && (Mill.serverWorlds.size() > 0)) {
/*  286 */       int nbClient = org.millenaire.common.core.MillCommonUtilities.getEntitiesWithinAABB(townHall.worldObj, MillVillager.class, townHall.getPos(), 64, 16).size();
/*  287 */       Building thServer = ((MillWorld)Mill.serverWorlds.get(0)).getBuilding(townHall.getPos());
/*  288 */       int nbServer = org.millenaire.common.core.MillCommonUtilities.getEntitiesWithinAABB(thServer.worldObj, MillVillager.class, townHall.getPos(), 64, 16).size();
/*      */       
/*  290 */       page.add("Client: " + nbClient + ", server: " + nbServer);
/*      */     }
/*      */     
/*      */ 
/*  294 */     text.add(page);
/*  295 */     text.add(visitorsPage);
/*  296 */     return text;
/*      */   }
/*      */   
/*      */   public static List<List<String>> generateHouse(EntityPlayer player, Building house)
/*      */   {
/*  301 */     List<String> page = new ArrayList();
/*      */     
/*  303 */     page.add("House : " + house.getNativeBuildingName());
/*      */     
/*  305 */     page.add("");
/*      */     
/*  307 */     VillagerRecord wife = null;VillagerRecord husband = null;
/*      */     
/*  309 */     for (VillagerRecord vr : house.vrecords) {
/*  310 */       if ((vr.gender == 2) && (!vr.getType().isChild)) {
/*  311 */         wife = vr;
/*      */       }
/*  313 */       if ((vr.gender == 1) && (!vr.getType().isChild)) {
/*  314 */         husband = vr;
/*      */       }
/*      */     }
/*      */     
/*  318 */     if ((wife == null) && (husband == null)) {
/*  319 */       page.add(MLN.string("panels.houseunoccupied"));
/*  320 */     } else if (wife == null)
/*      */     {
/*  322 */       page.add(MLN.string("panels.man") + ": " + husband.getName() + ", " + husband.getGameOccupation(player.getDisplayName()));
/*  323 */       page.add("");
/*  324 */       if (house.location.femaleResident.size() == 0) {
/*  325 */         page.add(MLN.string("panels.nofemaleresident"));
/*      */       } else {
/*  327 */         page.add(MLN.string("panels.bachelor"));
/*      */       }
/*  329 */     } else if (husband == null)
/*      */     {
/*  331 */       page.add(MLN.string("panels.woman") + ": " + wife.getName() + ", " + wife.getGameOccupation(player.getDisplayName()));
/*  332 */       page.add("");
/*  333 */       if ((house.location.maleResident == null) || (house.location.maleResident.size() == 0)) {
/*  334 */         page.add(MLN.string("panels.nomaleresident"));
/*      */       } else {
/*  336 */         page.add(MLN.string("panels.spinster"));
/*      */       }
/*      */     }
/*      */     else {
/*  340 */       page.add(MLN.string("panels.woman") + ": " + wife.getName() + ", " + wife.getGameOccupation(player.getDisplayName()).toLowerCase());
/*      */       
/*  342 */       page.add(MLN.string("panels.man") + ": " + husband.getName() + ", " + husband.getGameOccupation(player.getDisplayName()).toLowerCase());
/*      */       
/*  344 */       if (house.vrecords.size() > 2) {
/*  345 */         page.add("");
/*  346 */         page.add(MLN.string("panels.children") + ":");
/*  347 */         page.add("");
/*  348 */         for (VillagerRecord vr : house.vrecords) {
/*  349 */           if (vr.getType().isChild)
/*      */           {
/*  351 */             page.add(vr.getName() + ", " + vr.getGameOccupation(player.getDisplayName()).toLowerCase());
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  358 */     List<List<String>> text = new ArrayList();
/*  359 */     text.add(page);
/*      */     
/*  361 */     return text;
/*      */   }
/*      */   
/*      */   public static List<List<String>> generateInnVisitors(Building house)
/*      */   {
/*  366 */     List<String> page = new ArrayList();
/*      */     
/*  368 */     page.add(MLN.string("panels.innvisitors", new String[] { house.getNativeBuildingName() }) + ":");
/*      */     
/*  370 */     page.add("");
/*  371 */     for (int i = house.visitorsList.size() - 1; i > -1; i--) {
/*  372 */       String s = (String)house.visitorsList.get(i);
/*      */       
/*  374 */       if (s.split(";").length > 1) {
/*  375 */         if (s.startsWith("storedexports;")) {
/*  376 */           String[] v = s.split(";");
/*      */           
/*  378 */           String taken = "";
/*      */           
/*  380 */           for (int j = 2; j < v.length; j++) {
/*      */             try
/*      */             {
/*  383 */               InvItem iv = new InvItem(v[j]);
/*      */               
/*  385 */               if (taken.length() > 0) {
/*  386 */                 taken = taken + ", ";
/*      */               }
/*      */               
/*  389 */               taken = taken + iv.getName() + ": " + v[j].split("/")[2];
/*      */             } catch (MLN.MillenaireException e) {
/*  391 */               MLN.printException(e);
/*      */             }
/*      */           }
/*      */           
/*      */ 
/*  396 */           page.add(MLN.string("panels.storedexports", new String[] { v[1], taken }));
/*      */         }
/*  398 */         else if (s.startsWith("broughtimport;")) {
/*  399 */           String[] v = s.split(";");
/*      */           
/*  401 */           String taken = "";
/*      */           
/*  403 */           for (int j = 2; j < v.length; j++) {
/*      */             try
/*      */             {
/*  406 */               InvItem iv = new InvItem(v[j]);
/*      */               
/*  408 */               if (taken.length() > 0) {
/*  409 */                 taken = taken + ", ";
/*      */               }
/*      */               
/*  412 */               taken = taken + iv.getName() + ": " + v[j].split("/")[2];
/*      */             } catch (MLN.MillenaireException e) {
/*  414 */               MLN.printException(e);
/*      */             }
/*      */           }
/*      */           
/*      */ 
/*  419 */           page.add(MLN.string("panels.broughtimport", new String[] { v[1], taken }));
/*      */         } else {
/*  421 */           page.add(MLN.string(s.split(";")));
/*      */         }
/*      */       } else {
/*  424 */         page.add(s);
/*      */       }
/*      */       
/*  427 */       page.add("");
/*      */     }
/*      */     
/*  430 */     List<List<String>> text = new ArrayList();
/*  431 */     text.add(page);
/*      */     
/*  433 */     return text;
/*      */   }
/*      */   
/*      */   public static List<List<String>> generateMarketGoods(Building house)
/*      */   {
/*  438 */     List<String> page = new ArrayList();
/*      */     
/*  440 */     page.add(MLN.string("panels.goodstraded") + ":");
/*      */     
/*  442 */     page.add("");
/*  443 */     page.add(MLN.string("panels.goodsimported") + ":");
/*  444 */     page.add("");
/*  445 */     for (InvItem good : house.imported.keySet()) {
/*  446 */       page.add(good.getName() + ": " + house.imported.get(good));
/*      */     }
/*      */     
/*  449 */     page.add("");
/*  450 */     page.add(MLN.string("panels.goodsexported") + ":");
/*  451 */     page.add("");
/*  452 */     for (InvItem good : house.exported.keySet()) {
/*  453 */       page.add(good.getName() + ": " + house.exported.get(good));
/*      */     }
/*      */     
/*  456 */     List<List<String>> text = new ArrayList();
/*  457 */     text.add(page);
/*      */     
/*  459 */     return text;
/*      */   }
/*      */   
/*      */   public static List<List<String>> generateMarketMerchants(Building market)
/*      */   {
/*  464 */     if (market == null) {
/*  465 */       return null;
/*      */     }
/*      */     
/*  468 */     List<List<String>> text = new ArrayList();
/*  469 */     List<String> page = new ArrayList();
/*      */     
/*  471 */     page.add(MLN.string("panels.merchantlist") + ": ");
/*  472 */     page.add("(" + MLN.string("panels.capacity") + ": " + market.getResManager().stalls.size() + ")");
/*  473 */     page.add("");
/*      */     
/*  475 */     for (VillagerRecord vr : market.vrecords) {
/*  476 */       MillVillager v = null;
/*  477 */       for (MillVillager av : market.villagers) {
/*  478 */         if (vr.matches(av)) {
/*  479 */           v = av;
/*      */         }
/*      */       }
/*  482 */       page.add(vr.getName());
/*  483 */       if (v == null) {
/*  484 */         if (vr.killed) {
/*  485 */           page.add(MLN.string("panels.dead"));
/*      */         } else {
/*  487 */           page.add(MLN.string("panels.missing"));
/*      */         }
/*      */       } else {
/*  490 */         page.add(v.getNativeOccupationName());
/*  491 */         page.add(MLN.string("panels.nbnightsin", new String[] { "" + v.foreignMerchantNbNights }));
/*  492 */         page.add("");
/*      */       }
/*      */     }
/*      */     
/*  496 */     text.add(page);
/*  497 */     return text;
/*      */   }
/*      */   
/*      */   public static List<List<String>> generateMilitary(EntityPlayer player, Building townHall)
/*      */   {
/*  502 */     List<List<String>> text = new ArrayList();
/*  503 */     List<String> page = new ArrayList();
/*      */     
/*  505 */     page.add(MLN.string("panels.military") + " : " + townHall.getVillageQualifiedName());
/*  506 */     page.add("");
/*      */     
/*  508 */     int nbAttackers = 0;
/*  509 */     Point attackingVillagePos = null;
/*      */     
/*  511 */     if (townHall.raidTarget != null) {
/*  512 */       Building target = Mill.clientWorld.getBuilding(townHall.raidTarget);
/*      */       
/*  514 */       if (target != null) {
/*  515 */         if (townHall.raidStart > 0L) {
/*  516 */           page.add(MLN.string("panels.raidinprogresslong", new String[] { target.getVillageQualifiedName(), "" + Math.round((float)((townHall.worldObj.func_72820_D() - townHall.raidStart) / 1000L)) }));
/*      */         } else {
/*  518 */           page.add(MLN.string("panels.planningraidlong", new String[] { target.getVillageQualifiedName(), "" + Math.round((float)((townHall.worldObj.func_72820_D() - townHall.raidPlanningStart) / 1000L)) }));
/*      */         }
/*      */         
/*  521 */         page.add("");
/*      */       }
/*      */     }
/*      */     else {
/*  525 */       for (VillagerRecord vr : townHall.vrecords) {
/*  526 */         if (vr.raidingVillage) {
/*  527 */           nbAttackers++;
/*  528 */           attackingVillagePos = vr.originalVillagePos;
/*      */         }
/*      */       }
/*  531 */       if (nbAttackers > 0) {
/*  532 */         Building attackingVillage = Mill.clientWorld.getBuilding(attackingVillagePos);
/*      */         String attackedBy;
/*      */         String attackedBy;
/*  535 */         if (attackingVillage != null) {
/*  536 */           attackedBy = attackingVillage.getVillageQualifiedName();
/*      */         } else {
/*  538 */           attackedBy = MLN.string("panels.unknownattacker");
/*      */         }
/*      */         
/*  541 */         page.add(MLN.string("panels.underattacklong", new String[] { "" + nbAttackers, "" + townHall.getVillageAttackerStrength(), attackedBy }));
/*  542 */         page.add("");
/*      */       }
/*      */     }
/*      */     
/*  546 */     page.add(MLN.string("panels.offenselong", new String[] { "" + townHall.getVillageRaidingStrength() }));
/*  547 */     page.add(MLN.string("panels.defenselong", new String[] { "" + townHall.getVillageDefendingStrength() }));
/*      */     
/*  549 */     text.add(page);
/*      */     
/*  551 */     page = new ArrayList();
/*      */     
/*  553 */     page.add(MLN.string("panels.villagefighters"));
/*  554 */     page.add("");
/*      */     
/*  556 */     for (VillagerRecord vr : townHall.vrecords) {
/*  557 */       if (((vr.getType().isRaider) || (vr.getType().helpInAttacks)) && (!vr.raidingVillage)) {
/*  558 */         String status = "";
/*      */         
/*  560 */         if (vr.getType().helpInAttacks) {
/*  561 */           status = status + MLN.string("panels.defender");
/*      */         }
/*      */         
/*  564 */         if (vr.getType().isRaider) {
/*  565 */           if (status.length() > 0) {
/*  566 */             status = status + ", ";
/*      */           }
/*  568 */           status = status + MLN.string("panels.raider");
/*      */         }
/*      */         
/*  571 */         if (vr.awayraiding) {
/*  572 */           status = status + ", " + MLN.string("panels.awayraiding");
/*  573 */         } else if (vr.awayhired) {
/*  574 */           status = status + ", " + MLN.string("panels.awayhired");
/*  575 */         } else if ((vr.raidingVillage) && (townHall.worldObj.func_72820_D() < vr.raiderSpawn + 500L)) {
/*  576 */           status = status + ", " + MLN.string("panels.invaderincoming");
/*  577 */         } else if (vr.killed) {
/*  578 */           status = status + ", " + MLN.string("panels.dead");
/*      */         }
/*      */         
/*  581 */         String weapon = "";
/*      */         
/*  583 */         net.minecraft.item.Item bestMelee = vr.getBestMeleeWeapon();
/*  584 */         if (bestMelee != null) {
/*  585 */           weapon = Mill.proxy.getItemName(bestMelee, 0);
/*      */         }
/*      */         
/*  588 */         if ((vr.getType().isArcher) && (vr.countInv(net.minecraft.init.Items.field_151031_f) > 0)) {
/*  589 */           if (weapon.length() > 0) {
/*  590 */             weapon = weapon + ", ";
/*      */           }
/*  592 */           weapon = weapon + Mill.proxy.getItemName(net.minecraft.init.Items.field_151031_f, 0);
/*      */         }
/*      */         
/*  595 */         page.add(vr.getName() + ", " + vr.getGameOccupation(player.getDisplayName()));
/*  596 */         page.add(status);
/*  597 */         page.add(MLN.string("panels.health") + ": " + vr.getMaxHealth() + ", " + MLN.string("panels.armour") + ": " + vr.getTotalArmorValue() + ", " + MLN.string("panels.weapons") + ": " + weapon + ", " + MLN.string("panels.militarystrength") + ": " + vr.getMilitaryStrength());
/*      */         
/*  599 */         page.add("");
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  604 */     text.add(page);
/*      */     
/*  606 */     if (nbAttackers > 0)
/*      */     {
/*  608 */       page = new ArrayList();
/*  609 */       page.add(MLN.string("panels.attackers"));
/*  610 */       page.add("");
/*      */       
/*  612 */       for (VillagerRecord vr : townHall.vrecords) {
/*  613 */         if (vr.raidingVillage) {
/*  614 */           String status = "";
/*  615 */           if (vr.killed) {
/*  616 */             status = MLN.string("panels.dead");
/*      */           }
/*      */           
/*  619 */           String weapon = "";
/*      */           
/*  621 */           net.minecraft.item.Item bestMelee = vr.getBestMeleeWeapon();
/*  622 */           if (bestMelee != null) {
/*  623 */             weapon = Mill.proxy.getItemName(bestMelee, 0);
/*      */           }
/*      */           
/*  626 */           if ((vr.getType().isArcher) && (vr.countInv(net.minecraft.init.Items.field_151031_f) > 0)) {
/*  627 */             if (weapon.length() > 0) {
/*  628 */               weapon = weapon + ", ";
/*      */             }
/*  630 */             weapon = weapon + Mill.proxy.getItemName(net.minecraft.init.Items.field_151031_f, 0);
/*      */           }
/*      */           
/*  633 */           page.add(vr.getName() + ", " + vr.getGameOccupation(player.getDisplayName()));
/*  634 */           page.add(status);
/*  635 */           page.add(MLN.string("panels.health") + ": " + vr.getMaxHealth() + ", " + MLN.string("panels.armour") + ": " + vr.getTotalArmorValue() + ", " + MLN.string("panels.weapons") + ": " + weapon + ", " + MLN.string("panels.militarystrength") + ": " + vr.getMilitaryStrength());
/*      */           
/*  637 */           page.add("");
/*      */         }
/*      */       }
/*  640 */       text.add(page);
/*      */     }
/*      */     
/*  643 */     if (townHall.raidsPerformed.size() > 0)
/*      */     {
/*  645 */       page = new ArrayList();
/*      */       
/*  647 */       page.add(MLN.string("panels.raidsperformed"));
/*  648 */       page.add("");
/*      */       
/*  650 */       for (int i = townHall.raidsPerformed.size() - 1; i >= 0; i--) {
/*  651 */         String s = (String)townHall.raidsPerformed.get(i);
/*      */         
/*  653 */         if (s.split(";").length > 1) {
/*  654 */           if (s.split(";")[0].equals("failure")) {
/*  655 */             page.add(MLN.string("raid.historyfailure", new String[] { s.split(";")[1] }));
/*      */           }
/*      */           else {
/*  658 */             String[] v = s.split(";");
/*  659 */             String taken = "";
/*      */             
/*  661 */             for (int j = 2; j < v.length; j++) {
/*      */               try
/*      */               {
/*  664 */                 InvItem iv = new InvItem(v[j]);
/*      */                 
/*  666 */                 if (taken.length() > 0) {
/*  667 */                   taken = taken + ", ";
/*      */                 }
/*      */                 
/*  670 */                 taken = taken + iv.getName() + ": " + v[j].split("/")[2];
/*      */               } catch (MLN.MillenaireException e) {
/*  672 */                 MLN.printException(e);
/*      */               }
/*      */             }
/*      */             
/*      */ 
/*  677 */             if (taken.length() == 0) {
/*  678 */               taken = MLN.string("raid.nothing");
/*      */             }
/*      */             
/*  681 */             page.add(MLN.string("raid.historysuccess", new String[] { s.split(";")[1], taken }));
/*      */           }
/*      */         }
/*      */         else {
/*  685 */           page.add(townHall.raidsPerformed.get(i));
/*      */         }
/*  687 */         page.add("");
/*      */       }
/*      */       
/*  690 */       text.add(page);
/*      */     }
/*      */     
/*  693 */     if (townHall.raidsSuffered.size() > 0)
/*      */     {
/*  695 */       page = new ArrayList();
/*      */       
/*  697 */       page.add(MLN.string("panels.raidssuffered"));
/*  698 */       page.add("");
/*      */       
/*  700 */       for (int i = townHall.raidsSuffered.size() - 1; i >= 0; i--)
/*      */       {
/*  702 */         String s = (String)townHall.raidsSuffered.get(i);
/*      */         
/*  704 */         if (s.split(";").length > 1) {
/*  705 */           if (s.split(";")[0].equals("failure")) {
/*  706 */             page.add(MLN.string("raid.historydefended", new String[] { s.split(";")[1] }));
/*      */           }
/*      */           else {
/*  709 */             String[] v = s.split(";");
/*  710 */             String taken = "";
/*      */             
/*  712 */             for (int j = 2; j < v.length; j++) {
/*      */               try
/*      */               {
/*  715 */                 InvItem iv = new InvItem(v[j]);
/*      */                 
/*  717 */                 if (taken.length() > 0) {
/*  718 */                   taken = taken + ", ";
/*      */                 }
/*      */                 
/*  721 */                 taken = taken + iv.getName() + ": " + v[j].split("/")[2];
/*      */               } catch (MLN.MillenaireException e) {
/*  723 */                 MLN.printException(e);
/*      */               }
/*      */             }
/*      */             
/*      */ 
/*  728 */             if (taken.length() == 0) {
/*  729 */               taken = MLN.string("raid.nothing");
/*      */             }
/*      */             
/*  732 */             page.add(MLN.string("raid.historyraided", new String[] { s.split(";")[1], taken }));
/*      */           }
/*      */         }
/*      */         else {
/*  736 */           page.add(townHall.raidsSuffered.get(i));
/*      */         }
/*  738 */         page.add("");
/*      */       }
/*      */       
/*  741 */       text.add(page);
/*      */     }
/*      */     
/*      */ 
/*  745 */     return text;
/*      */   }
/*      */   
/*      */   public static List<List<String>> generateProjects(EntityPlayer player, Building townHall)
/*      */   {
/*  750 */     if (townHall.villageType == null) {
/*  751 */       return null;
/*      */     }
/*      */     
/*  754 */     List<String> page = new ArrayList();
/*      */     
/*  756 */     page.add(MLN.string("panels.buildingprojects") + " : " + townHall.getVillageQualifiedName());
/*      */     
/*  758 */     page.add("");
/*      */     
/*  760 */     for (org.millenaire.common.building.BuildingProject.EnumProjects ep : org.millenaire.common.building.BuildingProject.EnumProjects.values()) {
/*  761 */       if ((townHall.buildingProjects.containsKey(ep)) && (
/*  762 */         (!townHall.villageType.playerControlled) || (ep == org.millenaire.common.building.BuildingProject.EnumProjects.CENTRE) || (ep == org.millenaire.common.building.BuildingProject.EnumProjects.START) || (ep == org.millenaire.common.building.BuildingProject.EnumProjects.CORE)))
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  769 */         List<BuildingProject> projectsLevel = (List)townHall.buildingProjects.get(ep);
/*  770 */         page.add(MLN.string(ep.labelKey) + ":");
/*  771 */         page.add("");
/*      */         
/*  773 */         for (BuildingProject project : projectsLevel) {
/*  774 */           if (townHall.isDisplayableProject(project)) {
/*  775 */             addProjectToList(player, project, townHall, page);
/*      */           }
/*      */         }
/*  778 */         page.add("");
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  783 */     List<List<String>> text = new ArrayList();
/*  784 */     text.add(page);
/*      */     
/*  786 */     return text;
/*      */   }
/*      */   
/*      */   public static List<List<String>> generateResources(EntityPlayer player, Building house)
/*      */   {
/*  791 */     List<String> page = new ArrayList();
/*      */     
/*  793 */     List<List<String>> text = new ArrayList();
/*      */     
/*  795 */     page.add(MLN.string("panels.resources") + ": " + house.getNativeBuildingName());
/*      */     
/*  797 */     page.add("");
/*      */     
/*  799 */     BuildingPlan goalPlan = house.getCurrentGoalBuildingPlan();
/*      */     
/*  801 */     List<InvItem> res = new ArrayList();
/*  802 */     HashMap<InvItem, Integer> resCost = new HashMap();
/*  803 */     HashMap<InvItem, Integer> resHas = new HashMap();
/*      */     
/*  805 */     if (goalPlan != null) {
/*  806 */       for (InvItem key : goalPlan.resCost.keySet()) {
/*  807 */         res.add(key);
/*  808 */         resCost.put(key, goalPlan.resCost.get(key));
/*  809 */         int has = house.countGoods(key.getItem(), key.meta);
/*  810 */         if ((house.builder != null) && (house.buildingLocationIP != null) && (house.buildingLocationIP.planKey.equals(house.buildingGoal))) {
/*  811 */           has += house.builder.countInv(key.getItem(), key.meta);
/*      */         }
/*  813 */         if (has > ((Integer)goalPlan.resCost.get(key)).intValue()) {
/*  814 */           has = ((Integer)goalPlan.resCost.get(key)).intValue();
/*      */         }
/*      */         
/*  817 */         resHas.put(key, Integer.valueOf(has));
/*      */       }
/*      */       
/*  820 */       page.add(MLN.string("panels.resourcesneeded") + ":");
/*      */       
/*  822 */       String gameName = goalPlan.getGameName();
/*  823 */       String name; String name; if ((goalPlan.nativeName != null) && (goalPlan.nativeName.length() > 0)) {
/*  824 */         name = goalPlan.nativeName;
/*  825 */       } else if ((goalPlan.getGameName() != null) && (goalPlan.getGameName().length() > 0)) {
/*  826 */         String name = goalPlan.getGameName();
/*  827 */         gameName = "";
/*      */       } else {
/*  829 */         name = "";
/*      */       }
/*      */       
/*  832 */       if ((gameName != null) && (gameName.length() > 0)) {
/*  833 */         name = name + " (" + gameName + ")";
/*      */       }
/*      */       
/*  836 */       String status = "";
/*  837 */       if ((house.buildingLocationIP != null) && (house.buildingLocationIP.planKey.equals(house.buildingGoal))) {
/*  838 */         if (house.buildingLocationIP.level == 0) {
/*  839 */           status = MLN.string("ui.inconstruction");
/*      */         } else {
/*  841 */           status = MLN.string("ui.upgrading") + " (" + house.buildingLocationIP.level + ")";
/*      */         }
/*      */       } else {
/*  844 */         status = MLN.string(house.buildingGoalIssue);
/*      */       }
/*      */       
/*  847 */       page.add(name + " - " + status);
/*      */       
/*  849 */       page.add("");
/*      */       
/*  851 */       java.util.Collections.sort(res, new MillVillager.InvItemAlphabeticalComparator());
/*      */       
/*  853 */       for (int i = 0; i < res.size(); i++) {
/*  854 */         page.add(((InvItem)res.get(i)).getName() + ": " + resHas.get(res.get(i)) + "/" + resCost.get(res.get(i)));
/*      */       }
/*      */       
/*  857 */       text.add(page);
/*      */       
/*  859 */       page = new ArrayList();
/*      */     }
/*      */     
/*      */ 
/*  863 */     page.add(MLN.string("panels.resourcesavailable") + ":");
/*      */     
/*  865 */     page.add("");
/*      */     
/*  867 */     HashMap<InvItem, Integer> contents = house.getResManager().getChestsContent();
/*      */     
/*  869 */     List<InvItem> keys = new ArrayList(contents.keySet());
/*      */     
/*  871 */     java.util.Collections.sort(keys, new MillVillager.InvItemAlphabeticalComparator());
/*      */     
/*  873 */     for (InvItem key : keys) {
/*  874 */       page.add(key.getName() + ": " + contents.get(key));
/*      */     }
/*      */     
/*  877 */     text.add(page);
/*      */     
/*  879 */     return text;
/*      */   }
/*      */   
/*      */   public static List<List<String>> generateSummary(EntityPlayer player, Building townHall) {
/*  883 */     List<String> page = new ArrayList();
/*      */     
/*  885 */     List<List<String>> text = new ArrayList();
/*      */     
/*  887 */     page.add(MLN.string("panels.villagesummary") + ": " + townHall.getVillageQualifiedName());
/*  888 */     page.add("");
/*      */     
/*  890 */     int nbMen = 0;int nbFemale = 0;int nbGrownBoy = 0;int nbGrownGirl = 0;int nbBoy = 0;int nbGirl = 0;
/*      */     
/*  892 */     for (VillagerRecord vr : townHall.vrecords) {
/*  893 */       boolean belongsToVillage = (vr.getType() != null) && (!vr.getType().visitor) && (!vr.raidingVillage);
/*      */       
/*  895 */       if (belongsToVillage)
/*      */       {
/*  897 */         if (!vr.getType().isChild) {
/*  898 */           if (vr.gender == 1) {
/*  899 */             nbMen++;
/*      */           } else {
/*  901 */             nbFemale++;
/*      */           }
/*      */         }
/*  904 */         else if (vr.villagerSize == 20) {
/*  905 */           if (vr.gender == 1) {
/*  906 */             nbGrownBoy++;
/*      */           } else {
/*  908 */             nbGrownGirl++;
/*      */           }
/*      */         }
/*  911 */         else if (vr.gender == 1) {
/*  912 */           nbBoy++;
/*      */         } else {
/*  914 */           nbGirl++;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  922 */     page.add(MLN.string("ui.populationnumber", new String[] { "" + (nbMen + nbFemale + nbGrownBoy + nbGrownGirl + nbBoy + nbGirl) }));
/*  923 */     page.add(MLN.string("ui.adults", new String[] { "" + (nbMen + nbFemale), "" + nbMen, "" + nbFemale }));
/*  924 */     page.add(MLN.string("ui.teens", new String[] { "" + (nbGrownBoy + nbGrownGirl), "" + nbGrownBoy, "" + nbGrownGirl }));
/*  925 */     page.add(MLN.string("ui.children", new String[] { "" + (nbBoy + nbGirl), "" + nbBoy, "" + nbGirl }));
/*      */     
/*  927 */     page.add("");
/*      */     
/*  929 */     if (townHall.buildingGoal == null) {
/*  930 */       page.add(MLN.string("ui.goalscompleted1") + " " + MLN.string("ui.goalscompleted2"));
/*      */     } else {
/*  932 */       BuildingPlan goal = townHall.getCurrentGoalBuildingPlan();
/*      */       String status;
/*      */       String status;
/*  935 */       if ((townHall.buildingLocationIP != null) && (townHall.buildingLocationIP.planKey.equals(townHall.buildingGoal))) { String status;
/*  936 */         if (townHall.buildingLocationIP.level == 0) {
/*  937 */           status = MLN.string("ui.inconstruction");
/*      */         } else {
/*  939 */           status = MLN.string("ui.upgrading", new String[] { "" + townHall.buildingLocationIP.level });
/*      */         }
/*      */       } else {
/*  942 */         status = MLN.string(townHall.buildingGoalIssue);
/*      */       }
/*  944 */       page.add(MLN.string("panels.buildingproject") + " " + goal.nativeName + " " + goal.getGameName() + ": " + status);
/*      */       
/*  946 */       List<InvItem> res = new ArrayList();
/*  947 */       HashMap<InvItem, Integer> resCost = new HashMap();
/*  948 */       HashMap<InvItem, Integer> resHas = new HashMap();
/*      */       
/*  950 */       for (InvItem key : goal.resCost.keySet()) {
/*  951 */         res.add(key);
/*  952 */         resCost.put(key, goal.resCost.get(key));
/*  953 */         int has = townHall.countGoods(key.getItem(), key.meta);
/*  954 */         if ((townHall.builder != null) && (townHall.buildingLocationIP != null) && (townHall.buildingLocationIP.planKey.equals(townHall.buildingGoal))) {
/*  955 */           has += townHall.builder.countInv(key.getItem(), key.meta);
/*      */         }
/*  957 */         if (has > ((Integer)goal.resCost.get(key)).intValue()) {
/*  958 */           has = ((Integer)goal.resCost.get(key)).intValue();
/*      */         }
/*      */         
/*  961 */         resHas.put(key, Integer.valueOf(has));
/*      */       }
/*  963 */       page.add("");
/*  964 */       page.add(MLN.string("panels.resourcesneeded") + ":");
/*  965 */       page.add("");
/*      */       
/*  967 */       java.util.Collections.sort(res, new MillVillager.InvItemAlphabeticalComparator());
/*      */       
/*  969 */       for (int i = 0; i < res.size(); i++) {
/*  970 */         page.add(((InvItem)res.get(i)).getName() + ": " + resHas.get(res.get(i)) + "/" + resCost.get(res.get(i)));
/*      */       }
/*      */     }
/*      */     
/*  974 */     page.add("");
/*  975 */     page.add(MLN.string("panels.currentconstruction"));
/*      */     
/*  977 */     if (townHall.buildingLocationIP == null) {
/*  978 */       page.add(MLN.string("ui.noconstruction1") + " " + MLN.string("ui.noconstruction2"));
/*      */     } else {
/*  980 */       String planName = townHall.culture.getBuildingPlanSet(townHall.buildingLocationIP.planKey).getNativeName();
/*      */       String status;
/*      */       String status;
/*  983 */       if (townHall.buildingLocationIP.level == 0) {
/*  984 */         status = MLN.string("ui.inconstruction");
/*      */       } else {
/*  986 */         status = MLN.string("ui.upgrading", new String[] { "" + townHall.buildingLocationIP.level });
/*      */       }
/*      */       
/*      */       String loc;
/*      */       String loc;
/*  991 */       if (townHall.buildingLocationIP != null)
/*      */       {
/*  993 */         int distance = net.minecraft.util.MathHelper.func_76128_c(townHall.getPos().distanceTo(townHall.buildingLocationIP.pos));
/*      */         
/*  995 */         String direction = MLN.string(townHall.getPos().directionTo(townHall.buildingLocationIP.pos));
/*      */         
/*  997 */         loc = MLN.string("other.shortdistancedirection", new String[] { "" + distance, "" + direction });
/*      */       } else {
/*  999 */         loc = "";
/*      */       }
/*      */       
/* 1002 */       page.add(planName + ": " + status + " - " + loc);
/*      */     }
/*      */     
/* 1005 */     text.add(page);
/*      */     
/* 1007 */     return text;
/*      */   }
/*      */   
/*      */   public static List<List<String>> generateVillageMap(Building house)
/*      */   {
/* 1012 */     List<List<String>> text = new ArrayList();
/*      */     
/* 1014 */     List<String> page = new ArrayList();
/*      */     
/* 1016 */     page.add(MLN.string("ui.villagemap") + ": " + house.getNativeBuildingName());
/*      */     
/* 1018 */     text.add(page);
/* 1019 */     page = new ArrayList();
/*      */     
/* 1021 */     page.add(MLN.string("panels.mappurple"));
/* 1022 */     page.add(MLN.string("panels.mapblue"));
/* 1023 */     page.add(MLN.string("panels.mapgreen"));
/* 1024 */     page.add(MLN.string("panels.maplightgreen"));
/* 1025 */     page.add(MLN.string("panels.mapred"));
/* 1026 */     page.add(MLN.string("panels.mapyellow"));
/* 1027 */     page.add(MLN.string("panels.maporange"));
/* 1028 */     page.add(MLN.string("panels.maplightblue"));
/* 1029 */     page.add(MLN.string("panels.mapbrown"));
/* 1030 */     text.add(page);
/*      */     
/* 1032 */     return text;
/*      */   }
/*      */   
/*      */   public static void readPacket(io.netty.buffer.ByteBufInputStream ds)
/*      */   {
/*      */     try
/*      */     {
/* 1039 */       Point pos = org.millenaire.common.network.StreamReadWrite.readNullablePoint(ds);
/* 1040 */       int panelType = ds.read();
/* 1041 */       Point buildingPos = org.millenaire.common.network.StreamReadWrite.readNullablePoint(ds);
/* 1042 */       long villager_id = ds.readLong();
/* 1043 */       String[][] lines = org.millenaire.common.network.StreamReadWrite.readStringStringArray(ds);
/*      */       
/* 1045 */       org.millenaire.client.MillClientUtilities.updatePanel(pos, lines, panelType, buildingPos, villager_id);
/*      */       
/* 1047 */       if (MLN.LogNetwork >= 3) {
/* 1048 */         MLN.debug(null, "Receiving panel packet.");
/*      */       }
/*      */     }
/*      */     catch (java.io.IOException e) {
/* 1052 */       MLN.printException(e);
/*      */     }
/*      */   }
/*      */   
/* 1056 */   public Point buildingPos = null;
/*      */   
/* 1058 */   public long villager_id = 0L;
/*      */   
/* 1060 */   public int panelType = 0;
/*      */   
/*      */   public boolean func_145914_a()
/*      */   {
/* 1064 */     return false;
/*      */   }
/*      */   
/*      */   public List<List<String>> getFullText(EntityPlayer player)
/*      */   {
/* 1069 */     if ((this.panelType == 0) || (this.buildingPos == null)) {
/* 1070 */       return null;
/*      */     }
/*      */     
/* 1073 */     Building building = Mill.clientWorld.getBuilding(this.buildingPos);
/*      */     
/* 1075 */     if (this.panelType == 1)
/* 1076 */       return generateEtatCivil(player, building);
/* 1077 */     if (this.panelType == 2)
/* 1078 */       return generateConstructions(player, building);
/* 1079 */     if (this.panelType == 3)
/* 1080 */       return generateProjects(player, building);
/* 1081 */     if (this.panelType == 5)
/* 1082 */       return generateHouse(player, building);
/* 1083 */     if (this.panelType == 7)
/* 1084 */       return generateArchives(player, building, this.villager_id);
/* 1085 */     if (this.panelType == 6)
/* 1086 */       return generateResources(player, building);
/* 1087 */     if (this.panelType == 8)
/* 1088 */       return generateVillageMap(building);
/* 1089 */     if (this.panelType == 9)
/* 1090 */       return generateMilitary(player, building);
/* 1091 */     if (this.panelType == 10)
/* 1092 */       return generateMarketGoods(building);
/* 1093 */     if (this.panelType == 11)
/* 1094 */       return generateInnVisitors(building);
/* 1095 */     if (this.panelType == 12) {
/* 1096 */       return generateMarketMerchants(building);
/*      */     }
/* 1098 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */   public int getMapType()
/*      */   {
/* 1104 */     if (this.panelType == 8) {
/* 1105 */       return 1;
/*      */     }
/*      */     
/* 1108 */     return 0;
/*      */   }
/*      */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\TileEntityPanel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */