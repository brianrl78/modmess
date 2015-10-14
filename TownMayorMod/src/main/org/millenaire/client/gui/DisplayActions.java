/*     */ package org.millenaire.client.gui;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.world.World;
/*     */ import org.millenaire.client.MillClientUtilities;
/*     */ import org.millenaire.common.MillVillager;
/*     */ import org.millenaire.common.MillWorld;
/*     */ import org.millenaire.common.Point;
/*     */ import org.millenaire.common.UserProfile;
/*     */ import org.millenaire.common.VillageType;
/*     */ import org.millenaire.common.building.Building;
/*     */ import org.millenaire.common.building.BuildingCustomPlan;
/*     */ import org.millenaire.common.forge.Mill;
/*     */ 
/*     */ public class DisplayActions
/*     */ {
/*     */   public static void displayChunkGUI(EntityPlayer player, World world)
/*     */   {
/*  21 */     MillClientUtilities.displayChunkPanel(world, player);
/*     */   }
/*     */   
/*     */   public static void displayConfigGUI() {
/*  25 */     Minecraft.func_71410_x().func_147108_a(new GuiConfig());
/*     */   }
/*     */   
/*     */   public static void displayControlledMilitaryGUI(EntityPlayer player, Building townHall) {
/*  29 */     Minecraft.func_71410_x().func_147108_a(new GuiControlledMilitary(player, townHall));
/*     */   }
/*     */   
/*     */   public static void displayControlledProjectGUI(EntityPlayer player, Building townHall) {
/*  33 */     Minecraft.func_71410_x().func_147108_a(new GuiControlledProjects(player, townHall));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static void displayEditCustomBuildingGUI(EntityPlayer player, Building building)
/*     */   {
/*  40 */     Minecraft.func_71410_x().func_147108_a(new GuiCustomBuilding(player, building));
/*     */   }
/*     */   
/*     */   public static void displayHelpGUI() {
/*  44 */     Minecraft.func_71410_x().func_147108_a(new GuiHelp());
/*     */   }
/*     */   
/*     */   public static void displayHireGUI(EntityPlayer player, MillVillager villager) {
/*  48 */     Minecraft.func_71410_x().func_147108_a(new GuiHire(player, villager));
/*     */   }
/*     */   
/*     */   public static void displayInfoPanel(EntityPlayer player, World world) {
/*  52 */     MillClientUtilities.displayInfoPanel(world, player);
/*     */   }
/*     */   
/*     */   public static void displayNegationWandGUI(EntityPlayer player, Building townHall) {
/*  56 */     Minecraft.func_71410_x().func_147108_a(new GuiNegationWand(player, townHall));
/*     */   }
/*     */   
/*     */   public static void displayNewBuildingProjectGUI(EntityPlayer player, Building townHall, Point pos) {
/*  60 */     Minecraft.func_71410_x().func_147108_a(new GuiNewBuildingProject(player, townHall, pos));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static void displayNewCustomBuildingGUI(EntityPlayer player, Building townHall, Point pos, BuildingCustomPlan customBuilding)
/*     */   {
/*  67 */     Minecraft.func_71410_x().func_147108_a(new GuiCustomBuilding(player, townHall, pos, customBuilding));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static void displayNewCustomBuildingGUI(EntityPlayer player, Point pos, VillageType villageType)
/*     */   {
/*  74 */     Minecraft.func_71410_x().func_147108_a(new GuiCustomBuilding(player, pos, villageType));
/*     */   }
/*     */   
/*     */   public static void displayNewVillageGUI(EntityPlayer player, Point pos) {
/*  78 */     Minecraft.func_71410_x().func_147108_a(new GuiNewVillage(player, pos));
/*     */   }
/*     */   
/*     */   public static void displayParchmentPanelGUI(EntityPlayer player, List<List<String>> pages, Building building, int mapType, boolean isParchment) {
/*  82 */     Minecraft.func_71410_x().func_147108_a(new GuiPanelParchment(player, pages, building, mapType, isParchment));
/*     */   }
/*     */   
/*     */   public static void displayQuestGUI(EntityPlayer player, MillVillager villager)
/*     */   {
/*  87 */     UserProfile profile = Mill.clientWorld.getProfile(player.getDisplayName());
/*  88 */     if (profile.villagersInQuests.containsKey(Long.valueOf(villager.villager_id))) {
/*  89 */       Minecraft.func_71410_x().func_147108_a(new GuiQuest(player, villager));
/*     */     }
/*     */   }
/*     */   
/*     */   public static void displayStartupOrError(EntityPlayer player, boolean error) {
/*  94 */     MillClientUtilities.displayStartupText(error);
/*     */   }
/*     */   
/*     */   public static void displayVillageBookGUI(EntityPlayer player, Point p) {
/*  98 */     MillClientUtilities.displayVillageBook(Mill.clientWorld.world, player, p);
/*     */   }
/*     */   
/*     */   public static void displayVillageChiefGUI(EntityPlayer player, MillVillager chief) {
/* 102 */     Minecraft.func_71410_x().func_147108_a(new GuiVillageHead(player, chief));
/*     */   }
/*     */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\client\gui\DisplayActions.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */