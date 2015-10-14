/*     */ package org.millenaire.common;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.player.InventoryPlayer;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.tileentity.TileEntitySign;
/*     */ import net.minecraft.world.World;
/*     */ import org.millenaire.common.building.Building;
/*     */ import org.millenaire.common.building.BuildingCustomPlan;
/*     */ import org.millenaire.common.building.BuildingLocation;
/*     */ import org.millenaire.common.building.BuildingPlan;
/*     */ import org.millenaire.common.building.BuildingPlan.LocationReturn;
/*     */ import org.millenaire.common.building.BuildingPlanSet;
/*     */ import org.millenaire.common.building.BuildingProject;
/*     */ import org.millenaire.common.core.MillCommonUtilities;
/*     */ import org.millenaire.common.core.MillCommonUtilities.VillageList;
/*     */ import org.millenaire.common.forge.Mill;
/*     */ import org.millenaire.common.forge.MillAchievements;
/*     */ import org.millenaire.common.network.ServerSender;
/*     */ 
/*     */ public class GuiActions
/*     */ {
/*     */   public static final int VILLAGE_SCROLL_PRICE = 128;
/*     */   public static final int VILLAGE_SCROLL_REPUTATION = 8192;
/*     */   public static final int CROP_REPUTATION = 8192;
/*     */   public static final int CROP_PRICE = 512;
/*     */   public static final int CULTURE_CONTROL_REPUTATION = 131072;
/*     */   
/*     */   public static void activateMillChest(EntityPlayer player, Point p)
/*     */   {
/*  36 */     World world = player.field_70170_p;
/*     */     
/*  38 */     if (MLN.DEV)
/*     */     {
/*  40 */       MillWorld mw = Mill.getMillWorld(world);
/*     */       
/*  42 */       if (mw.buildingExists(p)) {
/*  43 */         Building ent = mw.getBuilding(p);
/*     */         
/*  45 */         if ((player.field_71071_by.func_70448_g() != null) && (player.field_71071_by.func_70448_g().func_77973_b() == Item.func_150898_a(Blocks.field_150354_m))) {
/*  46 */           ent.testModeGoods();
/*  47 */           return;
/*     */         }
/*     */         
/*  50 */         if ((player.field_71071_by.func_70448_g() != null) && (player.field_71071_by.func_70448_g().func_77973_b() == Item.func_150898_a(Mill.path))) {
/*  51 */           ent.clearOldPaths();
/*  52 */           ent.constructCalculatedPaths();
/*  53 */           return;
/*     */         }
/*     */         
/*  56 */         if ((player.field_71071_by.func_70448_g() != null) && (player.field_71071_by.func_70448_g().func_77973_b() == Item.func_150898_a(Mill.pathSlab))) {
/*  57 */           ent.recalculatePaths(true);
/*  58 */           return;
/*     */         }
/*     */         
/*  61 */         if ((player.field_71071_by.func_70448_g() != null) && (player.field_71071_by.func_70448_g().func_77973_b() == Mill.denier_or)) {
/*  62 */           ent.displayInfos(player);
/*  63 */           return;
/*     */         }
/*     */         
/*  66 */         if ((player.field_71071_by.func_70448_g() != null) && (player.field_71071_by.func_70448_g().func_77973_b() == net.minecraft.init.Items.field_151069_bo)) {
/*  67 */           mw.setGlobalTag("alchemy");
/*  68 */           MLN.major(mw, "Set alchemy tag.");
/*  69 */           return;
/*     */         }
/*     */         
/*  72 */         if ((player.field_71071_by.func_70448_g() != null) && (player.field_71071_by.func_70448_g().func_77973_b() == Mill.summoningWand))
/*     */         {
/*  74 */           ent.displayInfos(player);
/*     */           try
/*     */           {
/*  77 */             if (ent.isTownhall) {
/*  78 */               ent.rushBuilding();
/*     */             }
/*  80 */             if (ent.isInn) {
/*  81 */               ent.attemptMerchantMove(true);
/*     */             }
/*  83 */             if (ent.isMarket) {
/*  84 */               ent.updateMarket(true);
/*     */             }
/*     */           } catch (MLN.MillenaireException e) {
/*  87 */             MLN.printException(e);
/*     */           }
/*  89 */           return;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*  94 */     ServerSender.displayMillChest(player, p);
/*     */   }
/*     */   
/*     */   public static void controlledBuildingsForgetBuilding(EntityPlayer player, Building townHall, BuildingProject project)
/*     */   {
/*  99 */     townHall.cancelBuilding(project.location);
/*     */   }
/*     */   
/*     */   public static void controlledBuildingsToggleUpgrades(EntityPlayer player, Building townHall, BuildingProject project, boolean allow) {
/* 103 */     project.location.upgradesAllowed = allow;
/* 104 */     if (allow) {
/* 105 */       townHall.noProjectsLeft = false;
/*     */     }
/*     */   }
/*     */   
/*     */   public static void controlledMilitaryCancelRaid(EntityPlayer player, Building townHall) {
/* 110 */     if (townHall.raidStart == 0L) {
/* 111 */       townHall.cancelRaid();
/* 112 */       if (!townHall.worldObj.field_72995_K) {
/* 113 */         townHall.sendBuildingPacket(player, false);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static void controlledMilitaryDiplomacy(EntityPlayer player, Building townHall, Point target, int level) {
/* 119 */     townHall.adjustRelation(target, level, true);
/* 120 */     if (!townHall.worldObj.field_72995_K) {
/* 121 */       townHall.sendBuildingPacket(player, false);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void controlledMilitaryPlanRaid(EntityPlayer player, Building townHall, Building target) {
/* 126 */     if (townHall.raidStart == 0L) {
/* 127 */       townHall.adjustRelation(target.getPos(), -100, true);
/* 128 */       townHall.planRaid(target);
/* 129 */       if (!townHall.worldObj.field_72995_K) {
/* 130 */         townHall.sendBuildingPacket(player, false);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static void hireExtend(EntityPlayer player, MillVillager villager) {
/* 136 */     villager.hiredBy = player.getDisplayName();
/* 137 */     villager.hiredUntil += 24000L;
/* 138 */     MillCommonUtilities.changeMoney(player.field_71071_by, -villager.getHireCost(player), player);
/*     */   }
/*     */   
/*     */   public static void hireHire(EntityPlayer player, MillVillager villager) {
/* 142 */     villager.hiredBy = player.getDisplayName();
/* 143 */     villager.hiredUntil = (villager.field_70170_p.func_72820_D() + 24000L);
/* 144 */     VillagerRecord vr = villager.getTownHall().getVillagerRecordById(villager.villager_id);
/* 145 */     if (vr != null) {
/* 146 */       vr.awayhired = true;
/*     */     }
/*     */     
/* 149 */     player.func_71064_a(MillAchievements.hired, 1);
/*     */     
/* 151 */     MillCommonUtilities.changeMoney(player.field_71071_by, -villager.getHireCost(player), player);
/*     */   }
/*     */   
/*     */   public static void hireRelease(EntityPlayer player, MillVillager villager) {
/* 155 */     villager.hiredBy = null;
/* 156 */     villager.hiredUntil = 0L;
/* 157 */     VillagerRecord vr = villager.getTownHall().getVillagerRecordById(villager.villager_id);
/* 158 */     if (vr != null) {
/* 159 */       vr.awayhired = false;
/*     */     }
/*     */   }
/*     */   
/*     */   public static void newBuilding(EntityPlayer player, Building townHall, Point pos, String planKey)
/*     */   {
/* 165 */     BuildingPlanSet set = townHall.culture.getBuildingPlanSet(planKey);
/*     */     
/* 167 */     if (set == null) {
/* 168 */       return;
/*     */     }
/*     */     
/* 171 */     BuildingPlan plan = set.getRandomStartingPlan();
/*     */     
/* 173 */     BuildingPlan.LocationReturn lr = plan.testSpot(townHall.winfo, townHall.pathing, townHall.getPos(), pos.getiX() - townHall.winfo.mapStartX, pos.getiZ() - townHall.winfo.mapStartZ, MillCommonUtilities.getRandom(), -1);
/*     */     
/*     */ 
/* 176 */     if (lr.location == null) {
/* 177 */       String error = null;
/* 178 */       if (lr.errorCode == 3) {
/* 179 */         error = "ui.constructionforbidden";
/* 180 */       } else if (lr.errorCode == 2) {
/* 181 */         error = "ui.locationclash";
/* 182 */       } else if (lr.errorCode == 1) {
/* 183 */         error = "ui.outsideradius";
/* 184 */       } else if (lr.errorCode == 4) {
/* 185 */         error = "ui.wrongelevation";
/* 186 */       } else if (lr.errorCode == 5) {
/* 187 */         error = "ui.danger";
/* 188 */       } else if (lr.errorCode == 4) {
/* 189 */         error = "ui.notreachable";
/*     */       } else {
/* 191 */         error = "ui.unknownerror";
/*     */       }
/*     */       
/* 194 */       if (MLN.DEV) {
/* 195 */         MillCommonUtilities.setBlock(townHall.mw.world, lr.errorPos.getRelative(0.0D, 30.0D, 0.0D), Blocks.field_150351_n);
/*     */       }
/*     */       
/* 198 */       ServerSender.sendTranslatedSentence(player, '6', "ui.problemat", new String[] { pos.distanceDirectionShort(lr.errorPos), error });
/*     */     } else {
/* 200 */       lr.location.level = -1;
/* 201 */       BuildingProject project = new BuildingProject(set);
/* 202 */       project.location = lr.location;
/*     */       
/* 204 */       setSign(townHall, lr.location.minx, lr.location.minz, project);
/* 205 */       setSign(townHall, lr.location.maxx, lr.location.minz, project);
/* 206 */       setSign(townHall, lr.location.minx, lr.location.maxz, project);
/* 207 */       setSign(townHall, lr.location.maxx, lr.location.maxz, project);
/*     */       
/* 209 */       ((List)townHall.buildingProjects.get(org.millenaire.common.building.BuildingProject.EnumProjects.CORE)).add(project);
/* 210 */       townHall.noProjectsLeft = false;
/* 211 */       ServerSender.sendTranslatedSentence(player, '2', "ui.projectadded", new String[0]);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void newCustomBuilding(EntityPlayer player, Building townHall, Point pos, String planKey)
/*     */   {
/* 220 */     BuildingCustomPlan customBuilding = townHall.culture.getBuildingCustom(planKey);
/*     */     
/* 222 */     if (customBuilding != null) {
/*     */       try
/*     */       {
/* 225 */         townHall.addCustomBuilding(customBuilding, pos);
/*     */       } catch (Exception e) {
/* 227 */         MLN.printException("Exception when creation custom building: " + planKey, e);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public static void newVillageCreation(EntityPlayer player, Point pos, String cultureKey, String villageTypeKey)
/*     */   {
/* 235 */     Culture culture = Culture.getCultureByName(cultureKey);
/*     */     
/* 237 */     if (culture == null) {
/* 238 */       return;
/*     */     }
/*     */     
/* 241 */     VillageType villageType = culture.getVillageType(villageTypeKey);
/*     */     
/* 243 */     if (villageType == null) {
/* 244 */       return;
/*     */     }
/*     */     
/* 247 */     WorldGenVillage genVillage = new WorldGenVillage();
/* 248 */     boolean result = genVillage.generateVillageAtPoint(player.field_70170_p, MillCommonUtilities.random, pos.getiX(), pos.getiY(), pos.getiZ(), player, false, true, 0, villageType, null, null);
/*     */     
/* 250 */     if (result) {
/* 251 */       player.func_71064_a(MillAchievements.summoningwand, 1);
/* 252 */       if (villageType.playerControlled) {
/* 253 */         player.func_71064_a(MillAchievements.villageleader, 1);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static void pujasChangeEnchantment(EntityPlayer player, Building temple, int enchantmentId)
/*     */   {
/* 260 */     if ((temple != null) && (temple.pujas != null)) {
/* 261 */       temple.pujas.changeEnchantment(enchantmentId);
/* 262 */       player.func_71064_a(MillAchievements.puja, 1);
/* 263 */       temple.sendBuildingPacket(player, false);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void questCompleteStep(EntityPlayer player, MillVillager villager) {
/* 268 */     UserProfile profile = Mill.getMillWorld(player.field_70170_p).getProfile(player.getDisplayName());
/* 269 */     Quest.QuestInstance qi = (Quest.QuestInstance)profile.villagersInQuests.get(Long.valueOf(villager.villager_id));
/*     */     
/* 271 */     if (qi == null) {
/* 272 */       MLN.error(villager, "Could not find quest instance for this villager.");
/*     */     } else {
/* 274 */       qi.completeStep(player, villager);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void questRefuse(EntityPlayer player, MillVillager villager) {
/* 279 */     UserProfile profile = Mill.getMillWorld(player.field_70170_p).getProfile(player.getDisplayName());
/* 280 */     Quest.QuestInstance qi = (Quest.QuestInstance)profile.villagersInQuests.get(Long.valueOf(villager.villager_id));
/* 281 */     if (qi == null) {
/* 282 */       MLN.error(villager, "Could not find quest instance for this villager.");
/*     */     } else {
/* 284 */       qi.refuseQuest(player, villager);
/*     */     }
/*     */   }
/*     */   
/*     */   private static void setSign(Building townHall, int i, int j, BuildingProject project) {
/* 289 */     MillCommonUtilities.setBlockAndMetadata(townHall.worldObj, i, MillCommonUtilities.findTopSoilBlock(townHall.worldObj, i, j), j, Blocks.field_150472_an, 0, true, false);
/* 290 */     TileEntitySign sign = (TileEntitySign)townHall.worldObj.func_147438_o(i, MillCommonUtilities.findTopSoilBlock(townHall.worldObj, i, j), j);
/* 291 */     if (sign != null) {
/* 292 */       sign.field_145915_a = new String[] { project.getNativeName(), "", project.getGameName(), "" };
/*     */     }
/*     */   }
/*     */   
/*     */   public static void updateCustomBuilding(EntityPlayer player, Building building) {
/* 297 */     if (building.location.getCustomPlan() != null) {
/* 298 */       building.location.getCustomPlan().registerResources(building, building.location);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void useNegationWand(EntityPlayer player, Building townHall) {
/* 303 */     ServerSender.sendTranslatedSentence(player, '4', "negationwand.destroyed", new String[] { townHall.villageType.name });
/*     */     
/* 305 */     if (!townHall.villageType.lonebuilding) {
/* 306 */       player.func_71064_a(MillAchievements.scipio, 1);
/*     */     }
/*     */     
/* 309 */     townHall.destroyVillage();
/*     */   }
/*     */   
/*     */   public static void useSummoningWand(EntityPlayer player, Point pos) {
/* 313 */     MillWorld mw = Mill.getMillWorld(player.field_70170_p);
/*     */     
/* 315 */     Building closestVillage = mw.getClosestVillage(pos);
/*     */     
/* 317 */     if ((closestVillage != null) && (pos.squareRadiusDistance(closestVillage.getPos()) < closestVillage.villageType.radius + 10)) {
/* 318 */       if (closestVillage.controlledBy(player.getDisplayName())) {
/* 319 */         Building b = closestVillage.getBuildingAtCoordPlanar(pos);
/*     */         
/* 321 */         if (b != null) {
/* 322 */           if (b.location.isCustomBuilding) {
/* 323 */             ServerSender.displayNewBuildingProjectGUI(player, closestVillage, pos);
/*     */           } else {
/* 325 */             ServerSender.sendTranslatedSentence(player, 'e', "ui.wand_locationinuse", new String[0]);
/*     */           }
/*     */         } else {
/* 328 */           ServerSender.displayNewBuildingProjectGUI(player, closestVillage, pos);
/*     */         }
/*     */         
/* 331 */         return;
/*     */       }
/* 333 */       ServerSender.sendTranslatedSentence(player, 'e', "ui.wand_invillagerange", new String[] { closestVillage.getVillageQualifiedName() });
/* 334 */       return;
/*     */     }
/*     */     
/*     */ 
/* 338 */     Block block = MillCommonUtilities.getBlock(player.field_70170_p, pos);
/*     */     
/* 340 */     if (block == Blocks.field_150343_Z) {
/* 341 */       WorldGenVillage genVillage = new WorldGenVillage();
/* 342 */       genVillage.generateVillageAtPoint(player.field_70170_p, MillCommonUtilities.random, pos.getiX(), pos.getiY(), pos.getiZ(), player, false, true, 0, null, null, null);
/*     */     }
/*     */     
/* 345 */     if (block == Blocks.field_150340_R) {
/* 346 */       ServerSender.displayNewVillageGUI(player, pos);
/*     */     }
/*     */     
/*     */ 
/* 350 */     ServerSender.sendTranslatedSentence(player, 'f', "ui.wandinstruction", new String[0]);
/*     */   }
/*     */   
/*     */   public static void villageChiefPerformBuilding(EntityPlayer player, MillVillager chief, String planKey) {
/* 354 */     BuildingPlan plan = chief.getTownHall().culture.getBuildingPlanSet(planKey).getRandomStartingPlan();
/* 355 */     chief.getTownHall().buildingsBought.add(planKey);
/* 356 */     MillCommonUtilities.changeMoney(player.field_71071_by, -plan.price, player);
/* 357 */     ServerSender.sendTranslatedSentence(player, 'f', "ui.housebought", new String[] { chief.getName(), plan.nativeName });
/*     */   }
/*     */   
/*     */   public static void villageChiefPerformCrop(EntityPlayer player, MillVillager chief, String value) {
/* 361 */     UserProfile profile = Mill.getMillWorld(player.field_70170_p).getProfile(player.getDisplayName());
/* 362 */     profile.setTag("cropplanting_" + value);
/* 363 */     MillCommonUtilities.changeMoney(player.field_71071_by, 65024, player);
/* 364 */     ServerSender.sendTranslatedSentence(player, 'f', "ui.croplearned", new String[] { chief.getName(), "item." + value });
/*     */   }
/*     */   
/*     */   public static void villageChiefPerformCultureControl(EntityPlayer player, MillVillager chief) {
/* 368 */     UserProfile profile = Mill.getMillWorld(player.field_70170_p).getProfile(player.getDisplayName());
/* 369 */     profile.setTag("culturecontrol_" + chief.getCulture().key);
/* 370 */     ServerSender.sendTranslatedSentence(player, 'f', "ui.control_gotten", new String[] { chief.getName(), "culture." + chief.getCulture().key });
/*     */   }
/*     */   
/*     */   public static void villageChiefPerformDiplomacy(EntityPlayer player, MillVillager chief, Point village, boolean praise) {
/* 374 */     float effect = 0.0F;
/*     */     
/* 376 */     if (praise) {
/* 377 */       effect = 10.0F;
/*     */     } else {
/* 379 */       effect = -10.0F;
/*     */     }
/*     */     
/* 382 */     int reputation = Math.min(chief.getTownHall().getReputation(player.getDisplayName()), 32768);
/*     */     
/*     */ 
/*     */ 
/* 386 */     float coeff = (float)((Math.log(reputation) / Math.log(32768.0D) * 2.0D + reputation / 32768) / 3.0D);
/*     */     
/* 388 */     effect *= coeff;
/*     */     
/* 390 */     effect = (float)(effect * ((MillCommonUtilities.randomInt(40) + 80) / 100.0D));
/*     */     
/* 392 */     chief.getTownHall().adjustRelation(village, (int)effect, false);
/*     */     
/* 394 */     UserProfile profile = Mill.getMillWorld(player.field_70170_p).getProfile(player.getDisplayName());
/* 395 */     profile.adjustDiplomacyPoint(chief.getTownHall(), -1);
/*     */     
/* 397 */     if (MLN.LogVillage >= 1) {
/* 398 */       MLN.major(chief.getTownHall(), "Adjusted relation by " + effect + " (coef: " + coeff + ")");
/*     */     }
/*     */   }
/*     */   
/*     */   public static void villageChiefPerformVillageScroll(EntityPlayer player, MillVillager chief)
/*     */   {
/* 404 */     for (int i = 0; i < Mill.getMillWorld(player.field_70170_p).villagesList.pos.size(); i++) {
/* 405 */       Point p = (Point)Mill.getMillWorld(player.field_70170_p).villagesList.pos.get(i);
/* 406 */       if (chief.getTownHall().getPos().sameBlock(p)) {
/* 407 */         MillCommonUtilities.changeMoney(player.field_71071_by, -128, player);
/* 408 */         MillCommonUtilities.putItemsInChest(player.field_71071_by, Mill.parchmentVillageScroll, i, 1);
/* 409 */         ServerSender.sendTranslatedSentence(player, 'f', "ui.scrollbought", new String[] { chief.getName() });
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\GuiActions.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */