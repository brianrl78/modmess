/*     */ package org.millenaire.common.core;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.player.InventoryPlayer;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.pathfinding.PathPoint;
/*     */ import net.minecraft.util.EnumChatFormatting;
/*     */ import net.minecraft.world.World;
/*     */ import org.millenaire.common.MillVillager;
/*     */ import org.millenaire.common.MillWorld;
/*     */ import org.millenaire.common.Point;
/*     */ import org.millenaire.common.Quest.QuestInstance;
/*     */ import org.millenaire.common.UserProfile;
/*     */ import org.millenaire.common.VillagerRecord;
/*     */ import org.millenaire.common.forge.Mill;
/*     */ import org.millenaire.common.network.ServerSender;
/*     */ import org.millenaire.common.pathing.atomicstryker.AS_PathEntity;
/*     */ import org.millenaire.common.pathing.atomicstryker.AStarConfig;
/*     */ import org.millenaire.common.pathing.atomicstryker.AStarNode;
/*     */ import org.millenaire.common.pathing.atomicstryker.AStarPathPlanner;
/*     */ 
/*     */ public class DevModUtilities
/*     */ {
/*     */   private static class DevPathedEntity implements org.millenaire.common.pathing.atomicstryker.IAStarPathedEntity
/*     */   {
/*     */     World world;
/*     */     EntityPlayer caller;
/*     */     
/*     */     DevPathedEntity(World w, EntityPlayer p)
/*     */     {
/*  35 */       this.world = w;
/*  36 */       this.caller = p;
/*     */     }
/*     */     
/*     */ 
/*     */     public void onFoundPath(List<AStarNode> result)
/*     */     {
/*  42 */       int meta = MillCommonUtilities.randomInt(16);
/*     */       
/*  44 */       for (AStarNode node : result) {
/*  45 */         if ((node != result.get(0)) && (node != result.get(result.size() - 1))) {
/*  46 */           MillCommonUtilities.setBlockAndMetadata(this.world, new Point(node).getBelow(), Blocks.field_150325_L, meta);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     public void onNoPathAvailable()
/*     */     {
/*  53 */       ServerSender.sendChat(this.caller, EnumChatFormatting.DARK_RED, "No path available.");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*  58 */   private static HashMap<EntityPlayer, Integer> autoMoveDirection = new HashMap();
/*     */   
/*  60 */   private static HashMap<EntityPlayer, Integer> autoMoveTarget = new HashMap();
/*     */   
/*     */   public static void fillInFreeGoods(EntityPlayer player)
/*     */   {
/*  64 */     MillCommonUtilities.putItemsInChest(player.field_71071_by, Mill.japaneseWarriorBlueLegs, 1);
/*  65 */     MillCommonUtilities.putItemsInChest(player.field_71071_by, Mill.japaneseWarriorBlueBoots, 1);
/*  66 */     MillCommonUtilities.putItemsInChest(player.field_71071_by, Mill.japaneseWarriorBlueHelmet, 1);
/*  67 */     MillCommonUtilities.putItemsInChest(player.field_71071_by, Mill.japaneseWarriorBluePlate, 1);
/*     */     
/*  69 */     MillCommonUtilities.putItemsInChest(player.field_71071_by, Mill.japaneseWarriorRedLegs, 1);
/*  70 */     MillCommonUtilities.putItemsInChest(player.field_71071_by, Mill.japaneseWarriorRedBoots, 1);
/*  71 */     MillCommonUtilities.putItemsInChest(player.field_71071_by, Mill.japaneseWarriorRedHelmet, 1);
/*  72 */     MillCommonUtilities.putItemsInChest(player.field_71071_by, Mill.japaneseWarriorRedPlate, 1);
/*     */     
/*  74 */     MillCommonUtilities.putItemsInChest(player.field_71071_by, Mill.japaneseGuardLegs, 1);
/*  75 */     MillCommonUtilities.putItemsInChest(player.field_71071_by, Mill.japaneseGuardBoots, 1);
/*  76 */     MillCommonUtilities.putItemsInChest(player.field_71071_by, Mill.japaneseGuardHelmet, 1);
/*  77 */     MillCommonUtilities.putItemsInChest(player.field_71071_by, Mill.japaneseGuardPlate, 1);
/*     */     
/*  79 */     MillCommonUtilities.putItemsInChest(player.field_71071_by, Mill.summoningWand, 1);
/*  80 */     MillCommonUtilities.putItemsInChest(player.field_71071_by, Mill.skoll_hati_amulet, 1);
/*  81 */     MillCommonUtilities.putItemsInChest(player.field_71071_by, net.minecraft.init.Items.field_151113_aN, 1);
/*  82 */     MillCommonUtilities.putItemsInChest(player.field_71071_by, Mill.normanAxe, 1);
/*  83 */     MillCommonUtilities.putItemsInChest(player.field_71071_by, Mill.normanPickaxe, 1);
/*  84 */     MillCommonUtilities.putItemsInChest(player.field_71071_by, Mill.normanShovel, 1);
/*  85 */     MillCommonUtilities.putItemsInChest(player.field_71071_by, Blocks.field_150340_R, 0, 64);
/*  86 */     MillCommonUtilities.putItemsInChest(player.field_71071_by, Blocks.field_150364_r, 64);
/*  87 */     MillCommonUtilities.putItemsInChest(player.field_71071_by, net.minecraft.init.Items.field_151044_h, 64);
/*  88 */     MillCommonUtilities.putItemsInChest(player.field_71071_by, Blocks.field_150347_e, 128);
/*  89 */     MillCommonUtilities.putItemsInChest(player.field_71071_by, Blocks.field_150348_b, 512);
/*  90 */     MillCommonUtilities.putItemsInChest(player.field_71071_by, Blocks.field_150354_m, 128);
/*  91 */     MillCommonUtilities.putItemsInChest(player.field_71071_by, Blocks.field_150325_L, 64);
/*     */     
/*  93 */     MillCommonUtilities.putItemsInChest(player.field_71071_by, Mill.calva, 0, 2);
/*  94 */     MillCommonUtilities.putItemsInChest(player.field_71071_by, Mill.chickencurry, 2);
/*  95 */     MillCommonUtilities.putItemsInChest(player.field_71071_by, Mill.rice, 0, 64);
/*  96 */     MillCommonUtilities.putItemsInChest(player.field_71071_by, Mill.maize, 0, 64);
/*  97 */     MillCommonUtilities.putItemsInChest(player.field_71071_by, Mill.turmeric, 0, 64);
/*     */   }
/*     */   
/*     */   public static void runAutoMove(World world)
/*     */   {
/* 102 */     for (Object o : world.field_73010_i)
/*     */     {
/* 104 */       if ((o instanceof EntityPlayer)) {
/* 105 */         EntityPlayer p = (EntityPlayer)o;
/*     */         
/* 107 */         if (autoMoveDirection.containsKey(p))
/*     */         {
/* 109 */           if (((Integer)autoMoveDirection.get(p)).intValue() == 1) {
/* 110 */             if (((Integer)autoMoveTarget.get(p)).intValue() < p.field_70165_t) {
/* 111 */               autoMoveDirection.put(p, Integer.valueOf(-1));
/* 112 */               autoMoveTarget.put(p, Integer.valueOf((int)(p.field_70165_t - 100000.0D)));
/* 113 */               ServerSender.sendChat(p, EnumChatFormatting.GREEN, "Auto-move: turning back.");
/*     */             }
/* 115 */           } else if ((((Integer)autoMoveDirection.get(p)).intValue() == -1) && 
/* 116 */             (((Integer)autoMoveTarget.get(p)).intValue() > p.field_70165_t)) {
/* 117 */             autoMoveDirection.put(p, Integer.valueOf(1));
/* 118 */             autoMoveTarget.put(p, Integer.valueOf((int)(p.field_70165_t + 100000.0D)));
/* 119 */             ServerSender.sendChat(p, EnumChatFormatting.GREEN, "Auto-move: turning back again.");
/*     */           }
/*     */           
/*     */ 
/* 123 */           ((net.minecraft.entity.player.EntityPlayerMP)p).field_71135_a.func_147364_a(p.field_70165_t + ((Integer)autoMoveDirection.get(p)).intValue() * 0.5D, p.field_70163_u, p.field_70161_v, p.field_70177_z, p.field_70125_A);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static void testPaths(EntityPlayer player)
/*     */   {
/* 131 */     Point centre = new Point(player);
/*     */     
/* 133 */     org.millenaire.common.MLN.temp(null, "Attempting test path around: " + player);
/*     */     
/* 135 */     Point start = null;
/* 136 */     Point end = null;
/* 137 */     int toleranceMode = 0;
/*     */     
/* 139 */     for (int i = 0; (i < 100) && ((start == null) || (end == null)); i++) {
/* 140 */       for (int j = 0; (j < 100) && ((start == null) || (end == null)); j++) {
/* 141 */         for (int k = 0; (k < 100) && ((start == null) || (end == null)); k++) {
/* 142 */           for (int l = 0; (l < 8) && ((start == null) || (end == null)); l++) {
/* 143 */             Point p = centre.getRelative(i * (1 - (l & 0x1) * 2), j * (1 - (l & 0x2)), k * (1 - (l & 0x4) / 2));
/*     */             
/* 145 */             net.minecraft.block.Block block = MillCommonUtilities.getBlock(player.field_70170_p, p);
/*     */             
/* 147 */             if ((start == null) && (block == Blocks.field_150340_R)) {
/* 148 */               start = p;
/*     */             }
/*     */             
/* 151 */             if ((end == null) && (block == Blocks.field_150339_S)) {
/* 152 */               end = p.getAbove();
/* 153 */               toleranceMode = 0;
/* 154 */             } else if ((end == null) && (block == Blocks.field_150484_ah)) {
/* 155 */               end = p.getAbove();
/* 156 */               toleranceMode = 1;
/* 157 */             } else if ((end == null) && (block == Blocks.field_150368_y)) {
/* 158 */               end = p.getAbove();
/* 159 */               toleranceMode = 2;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 166 */     if ((start != null) && (end != null))
/*     */     {
/* 168 */       DevPathedEntity pathedEntity = new DevPathedEntity(player.field_70170_p, player);
/*     */       
/*     */       AStarConfig jpsConfig;
/*     */       AStarConfig jpsConfig;
/* 172 */       if (toleranceMode == 1) {
/* 173 */         jpsConfig = new AStarConfig(true, false, false, true, 2, 2); } else { AStarConfig jpsConfig;
/* 174 */         if (toleranceMode == 2) {
/* 175 */           jpsConfig = new AStarConfig(true, false, false, true, 2, 20);
/*     */         } else {
/* 177 */           jpsConfig = new AStarConfig(true, false, false, true);
/*     */         }
/*     */       }
/* 180 */       ServerSender.sendChat(player, EnumChatFormatting.DARK_GREEN, "Calculating path. Tolerance H: " + jpsConfig.toleranceHorizontal);
/*     */       
/* 182 */       AStarPathPlanner jpsPathPlanner = new AStarPathPlanner(player.field_70170_p, pathedEntity);
/* 183 */       jpsPathPlanner.getPath(start.getiX(), start.getiY(), start.getiZ(), end.getiX(), end.getiY(), end.getiZ(), jpsConfig);
/*     */     } else {
/* 185 */       ServerSender.sendChat(player, EnumChatFormatting.DARK_RED, "Could not find start or end: " + start + " - " + end);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public static void toggleAutoMove(EntityPlayer player)
/*     */   {
/* 192 */     if (autoMoveDirection.containsKey(player)) {
/* 193 */       autoMoveDirection.remove(player);
/* 194 */       autoMoveTarget.remove(player);
/* 195 */       ServerSender.sendChat(player, EnumChatFormatting.GREEN, "Auto-move disabled");
/*     */     } else {
/* 197 */       autoMoveDirection.put(player, Integer.valueOf(1));
/* 198 */       autoMoveTarget.put(player, Integer.valueOf((int)(player.field_70165_t + 100000.0D)));
/* 199 */       ServerSender.sendChat(player, EnumChatFormatting.GREEN, "Auto-move enabled");
/*     */     }
/*     */   }
/*     */   
/*     */   public static void villagerInteractDev(EntityPlayer entityplayer, MillVillager villager) {
/* 204 */     if (villager.func_70631_g_()) {
/* 205 */       villager.growSize();
/* 206 */       ServerSender.sendChat(entityplayer, EnumChatFormatting.GREEN, villager.getName() + ": Size: " + villager.size + " gender: " + villager.gender);
/* 207 */       if ((entityplayer.field_71071_by.func_70448_g() != null) && (entityplayer.field_71071_by.func_70448_g().func_77973_b() == Mill.summoningWand)) {
/* 208 */         villager.size = 20;
/* 209 */         villager.growSize();
/*     */       }
/*     */     }
/* 212 */     if (entityplayer.field_71071_by.func_70448_g() == null) {
/* 213 */       ServerSender.sendChat(entityplayer, EnumChatFormatting.GREEN, villager.getName() + ": Current goal: " + villager.getGoalLabel(villager.goalKey) + " Current pos: " + villager.getPos());
/* 214 */       ServerSender.sendChat(entityplayer, EnumChatFormatting.GREEN, villager.getName() + ": House: " + villager.housePoint + " Town Hall: " + villager.townHallPoint);
/* 215 */       ServerSender.sendChat(entityplayer, EnumChatFormatting.GREEN, villager.getName() + ": ID: " + villager.villager_id);
/* 216 */       if (villager.getRecord() != null) {
/* 217 */         ServerSender.sendChat(entityplayer, EnumChatFormatting.GREEN, villager.getName() + ": Spouse: " + villager.getRecord().spousesName);
/*     */       }
/*     */       
/* 220 */       if ((villager.getPathDestPoint() != null) && (villager.pathEntity != null) && (villager.pathEntity.func_75874_d() > 1)) {
/* 221 */         ServerSender.sendChat(entityplayer, EnumChatFormatting.GREEN, villager.getName() + ": Dest: " + villager.getPathDestPoint() + " distance: " + villager.getPathDestPoint().distanceTo(villager) + " stuck: " + villager.longDistanceStuck + " jump:" + villager.pathEntity.getNextTargetPathPoint());
/*     */       }
/*     */       else
/*     */       {
/* 225 */         ServerSender.sendChat(entityplayer, EnumChatFormatting.GREEN, villager.getName() + ": No dest point.");
/*     */       }
/*     */       
/* 228 */       String s = "";
/*     */       
/* 230 */       if (villager.getRecord() != null) {
/* 231 */         for (String tag : villager.getRecord().questTags) {
/* 232 */           s = s + tag + " ";
/*     */         }
/*     */       }
/*     */       
/* 236 */       if (villager.mw.getProfile(entityplayer.getDisplayName()).villagersInQuests.containsKey(Long.valueOf(villager.villager_id))) {
/* 237 */         s = s + " quest: " + ((Quest.QuestInstance)villager.mw.getProfile(entityplayer.getDisplayName()).villagersInQuests.get(Long.valueOf(villager.villager_id))).quest.key + "/" + ((Quest.QuestInstance)villager.mw.getProfile(entityplayer.getDisplayName()).villagersInQuests.get(Long.valueOf(villager.villager_id))).getCurrentVillager().id;
/*     */       }
/*     */       
/*     */ 
/* 241 */       if ((s != null) && (s.length() > 0)) {
/* 242 */         ServerSender.sendChat(entityplayer, EnumChatFormatting.GREEN, "Tags: " + s);
/*     */       }
/*     */       
/* 245 */       s = "";
/*     */       
/* 247 */       for (org.millenaire.common.InvItem key : villager.inventory.keySet()) {
/* 248 */         s = s + key + ":" + villager.inventory.get(key) + " ";
/*     */       }
/*     */       
/* 251 */       if ((s != null) && (s.length() > 0)) {
/* 252 */         ServerSender.sendChat(entityplayer, EnumChatFormatting.GREEN, "Inv: " + s);
/*     */       }
/*     */     }
/* 255 */     else if ((entityplayer.field_71071_by.func_70448_g() != null) && (entityplayer.field_71071_by.func_70448_g().func_77973_b() == Item.func_150898_a(Blocks.field_150354_m))) {
/* 256 */       if (villager.hiredBy == null) {
/* 257 */         villager.hiredBy = entityplayer.getDisplayName();
/* 258 */         ServerSender.sendChat(entityplayer, EnumChatFormatting.GREEN, "Hired: " + entityplayer.getDisplayName());
/*     */       } else {
/* 260 */         villager.hiredBy = null;
/* 261 */         ServerSender.sendChat(entityplayer, EnumChatFormatting.GREEN, "No longer hired");
/*     */       }
/* 263 */     } else if ((entityplayer.field_71071_by.func_70448_g() != null) && (entityplayer.field_71071_by.func_70448_g().func_77973_b() == Item.func_150898_a(Blocks.field_150346_d)) && (villager.pathEntity != null)) {
/* 264 */       int meta = MillCommonUtilities.randomInt(16);
/*     */       
/* 266 */       for (PathPoint p : villager.pathEntity.pointsCopy) {
/* 267 */         if (villager.field_70170_p.func_147439_a(p.field_75839_a, p.field_75837_b - 1, p.field_75838_c) != Mill.lockedChest) {
/* 268 */           MillCommonUtilities.setBlockAndMetadata(villager.field_70170_p, new Point(p).getBelow(), Blocks.field_150325_L, meta);
/*     */         }
/*     */       }
/* 271 */       PathPoint p = villager.pathEntity.getCurrentTargetPathPoint();
/* 272 */       if ((p != null) && (villager.field_70170_p.func_147439_a(p.field_75839_a, p.field_75837_b - 1, p.field_75838_c) != Mill.lockedChest)) {
/* 273 */         MillCommonUtilities.setBlockAndMetadata(villager.field_70170_p, new Point(p).getBelow(), Blocks.field_150340_R, 0);
/*     */       }
/* 275 */       p = villager.pathEntity.getNextTargetPathPoint();
/* 276 */       if ((p != null) && (villager.field_70170_p.func_147439_a(p.field_75839_a, p.field_75837_b - 1, p.field_75838_c) != Mill.lockedChest)) {
/* 277 */         MillCommonUtilities.setBlockAndMetadata(villager.field_70170_p, new Point(p).getBelow(), Blocks.field_150484_ah, 0);
/*     */       }
/* 279 */       p = villager.pathEntity.getPreviousTargetPathPoint();
/* 280 */       if ((p != null) && (villager.field_70170_p.func_147439_a(p.field_75839_a, p.field_75837_b - 1, p.field_75838_c) != Mill.lockedChest)) {
/* 281 */         MillCommonUtilities.setBlockAndMetadata(villager.field_70170_p, new Point(p).getBelow(), Blocks.field_150339_S, 0);
/*     */       }
/*     */     }
/*     */     
/* 285 */     if ((villager.hasChildren()) && (entityplayer.field_71071_by.func_70448_g() != null) && (entityplayer.field_71071_by.func_70448_g().func_77973_b() == Mill.summoningWand)) {
/* 286 */       MillVillager child = villager.getHouse().createChild(villager, villager.getTownHall(), villager.getRecord().spousesName);
/* 287 */       if (child != null) {
/* 288 */         child.size = 20;
/* 289 */         child.growSize();
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\core\DevModUtilities.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */