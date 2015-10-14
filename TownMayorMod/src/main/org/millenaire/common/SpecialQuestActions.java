/*     */ package org.millenaire.common;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.item.EntityTNTPrimed;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.biome.BiomeGenBase;
/*     */ import net.minecraft.world.biome.WorldChunkManager;
/*     */ import org.millenaire.common.building.Building;
/*     */ import org.millenaire.common.building.BuildingLocation;
/*     */ import org.millenaire.common.core.MillCommonUtilities;
/*     */ import org.millenaire.common.core.MillCommonUtilities.VillageList;
/*     */ import org.millenaire.common.entity.EntityTargetedBlaze;
/*     */ import org.millenaire.common.entity.EntityTargetedGhast;
/*     */ import org.millenaire.common.entity.EntityTargetedWitherSkeleton;
/*     */ import org.millenaire.common.network.ServerSender;
/*     */ 
/*     */ public class SpecialQuestActions
/*     */ {
/*     */   public static final String COMPLETE = "_complete";
/*     */   public static final String EXPLORE_TAG = "action_explore_";
/*     */   public static final String ENCHANTMENTTABLE = "action_build_enchantment_table";
/*     */   public static final String UNDERWATER_GLASS = "action_underwater_glass";
/*     */   public static final String UNDERWATER_DIVE = "action_underwater_dive";
/*     */   public static final String TOPOFTHEWORLD = "action_topoftheworld";
/*     */   public static final String BOTTOMOFTHEWORLD = "action_bottomoftheworld";
/*     */   public static final String BOREHOLE = "action_borehole";
/*     */   public static final String BOREHOLETNT = "action_boreholetnt";
/*     */   public static final String BOREHOLETNTLIT = "action_boreholetntlit";
/*     */   public static final String THEVOID = "action_thevoid";
/*     */   public static final String MAYANSIEGE = "action_mayansiege";
/*     */   
/*     */   private static void handleBorehole(MillWorld mw, EntityPlayer player)
/*     */   {
/*  39 */     if ((!mw.getProfile(player.getDisplayName()).isTagSet("action_borehole")) || (mw.getProfile(player.getDisplayName()).isTagSet("action_borehole_complete"))) {
/*  40 */       return;
/*     */     }
/*     */     
/*  43 */     if (player.field_70163_u > 10.0D) {
/*  44 */       return;
/*     */     }
/*     */     
/*  47 */     int nbok = 0;
/*  48 */     for (int x = (int)(player.field_70165_t - 2.0D); x < (int)player.field_70165_t + 3; x++) {
/*  49 */       for (int z = (int)(player.field_70161_v - 2.0D); z < (int)player.field_70161_v + 3; z++)
/*     */       {
/*  51 */         boolean ok = true;boolean stop = false;
/*     */         
/*  53 */         for (int y = 127; (y > 0) && (!stop); y--) {
/*  54 */           Block block = mw.world.func_147439_a(x, y, z);
/*  55 */           if (block == Blocks.field_150357_h) {
/*  56 */             stop = true;
/*  57 */           } else if (block != Blocks.field_150350_a) {
/*  58 */             stop = true;
/*  59 */             ok = false;
/*     */           }
/*     */         }
/*     */         
/*  63 */         if (ok) {
/*  64 */           nbok++;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*  69 */     if (nbok >= 25) {
/*  70 */       ServerSender.sendTranslatedSentence(player, '7', "actions.borehole_success", new String[0]);
/*  71 */       mw.getProfile(player.getDisplayName()).clearTag("action_borehole");
/*  72 */       mw.getProfile(player.getDisplayName()).setTag("action_borehole_complete");
/*  73 */       mw.getProfile(player.getDisplayName()).setActionData("action_borehole_pos", new Point(player).getIntString());
/*  74 */       return;
/*     */     }
/*     */     
/*  77 */     String maxKnownStr = mw.getProfile(player.getDisplayName()).getActionData("action_borehole_max");
/*     */     
/*  79 */     int maxKnown = 0;
/*     */     
/*  81 */     if (maxKnownStr != null) {
/*  82 */       maxKnown = Integer.parseInt(maxKnownStr);
/*     */     }
/*     */     
/*  85 */     if (nbok > maxKnown) {
/*  86 */       ServerSender.sendTranslatedSentence(player, '7', "actions.borehole_nblineok", new String[] { "" + nbok });
/*  87 */       mw.getProfile(player.getDisplayName()).setActionData("action_borehole_max", "" + nbok);
/*     */     }
/*     */   }
/*     */   
/*     */   private static void handleBoreholeTNT(MillWorld mw, EntityPlayer player) {
/*  92 */     if ((!mw.getProfile(player.getDisplayName()).isTagSet("action_boreholetnt")) || (mw.getProfile(player.getDisplayName()).isTagSet("action_boreholetnt_complete"))) {
/*  93 */       return;
/*     */     }
/*     */     
/*  96 */     String pStr = mw.getProfile(player.getDisplayName()).getActionData("action_borehole_pos");
/*     */     
/*  98 */     if (pStr == null) {
/*  99 */       return;
/*     */     }
/*     */     
/* 102 */     Point p = new Point(pStr);
/*     */     
/* 104 */     if (p.distanceToSquared(player) > 25.0D) {
/* 105 */       return;
/*     */     }
/*     */     
/* 108 */     int nbTNT = 0;
/*     */     
/* 110 */     for (int x = p.getiX() - 2; x < p.getiX() + 3; x++) {
/* 111 */       for (int z = p.getiZ() - 2; z < p.getiZ() + 3; z++) {
/* 112 */         boolean obsidian = false;
/* 113 */         for (int y = 6; y > 0; y--) {
/* 114 */           Block block = mw.world.func_147439_a(x, y, z);
/* 115 */           if (block == Blocks.field_150343_Z) {
/* 116 */             obsidian = true;
/* 117 */           } else if ((obsidian) && (block == Blocks.field_150335_W)) {
/* 118 */             nbTNT++;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 124 */     if (nbTNT >= 20) {
/* 125 */       ServerSender.sendTranslatedSentence(player, '7', "actions.boreholetnt_success", new String[0]);
/* 126 */       mw.getProfile(player.getDisplayName()).clearTag("action_boreholetnt");
/* 127 */       mw.getProfile(player.getDisplayName()).setTag("action_boreholetnt_complete");
/* 128 */       mw.getProfile(player.getDisplayName()).setTag("action_boreholetntlit");
/* 129 */       mw.getProfile(player.getDisplayName()).clearActionData("action_boreholetnt_max");
/* 130 */       return; }
/* 131 */     if (nbTNT == 0) {
/* 132 */       return;
/*     */     }
/*     */     
/* 135 */     String maxKnownStr = mw.getProfile(player.getDisplayName()).getActionData("action_boreholetnt_max");
/*     */     
/* 137 */     int maxKnown = 0;
/*     */     
/* 139 */     if (maxKnownStr != null) {
/* 140 */       maxKnown = Integer.parseInt(maxKnownStr);
/*     */     }
/*     */     
/* 143 */     if (nbTNT > maxKnown) {
/* 144 */       ServerSender.sendTranslatedSentence(player, '7', "actions.boreholetnt_nbtnt", new String[] { "" + nbTNT });
/* 145 */       mw.getProfile(player.getDisplayName()).setActionData("action_boreholetnt_max", "" + nbTNT);
/*     */     }
/*     */   }
/*     */   
/*     */   private static void handleBoreholeTNTLit(MillWorld mw, EntityPlayer player)
/*     */   {
/* 151 */     if ((!mw.getProfile(player.getDisplayName()).isTagSet("action_boreholetntlit")) || (mw.getProfile(player.getDisplayName()).isTagSet("action_boreholetntlit_complete"))) {
/* 152 */       return;
/*     */     }
/*     */     
/* 155 */     Point p = new Point(mw.getProfile(player.getDisplayName()).getActionData("action_borehole_pos"));
/*     */     
/* 157 */     int nbtnt = mw.world.func_72872_a(EntityTNTPrimed.class, AxisAlignedBB.func_72330_a(p.x, p.y, p.z, p.x + 1.0D, p.y + 1.0D, p.z + 1.0D).func_72314_b(8.0D, 4.0D, 8.0D)).size();
/*     */     
/* 159 */     if (nbtnt > 0) {
/* 160 */       ServerSender.sendTranslatedSentence(player, '7', "actions.boreholetntlit_success", new String[0]);
/* 161 */       mw.getProfile(player.getDisplayName()).clearTag("action_boreholetntlit");
/* 162 */       mw.getProfile(player.getDisplayName()).setTag("action_boreholetntlit_complete");
/* 163 */       return;
/*     */     }
/*     */   }
/*     */   
/*     */   private static void handleBottomOfTheWorld(MillWorld mw, EntityPlayer player)
/*     */   {
/* 169 */     if ((!mw.getProfile(player.getDisplayName()).isTagSet("action_bottomoftheworld")) || (mw.getProfile(player.getDisplayName()).isTagSet("action_bottomoftheworld_complete"))) {
/* 170 */       return;
/*     */     }
/*     */     
/* 173 */     if (player.field_70163_u < 4.0D) {
/* 174 */       ServerSender.sendTranslatedSentence(player, '7', "actions.bottomoftheworld_success", new String[0]);
/* 175 */       mw.getProfile(player.getDisplayName()).clearTag("action_bottomoftheworld");
/* 176 */       mw.getProfile(player.getDisplayName()).setTag("action_bottomoftheworld_complete");
/* 177 */       return;
/*     */     }
/*     */   }
/*     */   
/*     */   private static void handleContinuousExplore(MillWorld mw, EntityPlayer player, long worldTime, String biome, String mob, int nbMob, int minTravel)
/*     */   {
/* 183 */     if ((!mw.getProfile(player.getDisplayName()).isTagSet("action_explore_" + biome)) || (mw.getProfile(player.getDisplayName()).isTagSet("action_explore_" + biome + "_complete"))) {
/* 184 */       return;
/*     */     }
/*     */     
/* 187 */     if (mw.world.func_72935_r()) {
/* 188 */       return;
/*     */     }
/*     */     
/* 191 */     String biomeName = mw.world.func_72959_q().func_76935_a((int)player.field_70165_t, (int)player.field_70161_v).field_76791_y.toLowerCase();
/* 192 */     if (biomeName.equals("extreme hills")) {
/* 193 */       biomeName = "mountain";
/*     */     }
/* 195 */     if (!biomeName.equals(biome)) {
/* 196 */       return;
/*     */     }
/*     */     
/* 199 */     int surface = MillCommonUtilities.findTopSoilBlock(mw.world, (int)player.field_70165_t, (int)player.field_70161_v);
/* 200 */     if (player.field_70163_u <= surface - 2) {
/* 201 */       return;
/*     */     }
/*     */     
/* 204 */     String testnbstr = mw.getProfile(player.getDisplayName()).getActionData(biome + "_explore_nbcomplete");
/*     */     
/* 206 */     int nbtest = 0;
/* 207 */     if (testnbstr != null) {
/* 208 */       nbtest = Integer.parseInt(testnbstr);
/*     */       
/* 210 */       for (int i = 1; i <= nbtest; i++) {
/* 211 */         String pointstr = mw.getProfile(player.getDisplayName()).getActionData(biome + "_explore_point" + i);
/* 212 */         if (pointstr != null) {
/* 213 */           Point p = new Point(pointstr);
/* 214 */           if (p.horizontalDistanceTo(player) < minTravel) {
/* 215 */             return;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 221 */     nbtest++;
/*     */     
/* 223 */     if (nbtest >= 20) {
/* 224 */       ServerSender.sendTranslatedSentence(player, '7', "actions." + biome + "_success", new String[0]);
/* 225 */       mw.getProfile(player.getDisplayName()).clearActionData(biome + "_explore_nbcomplete");
/* 226 */       for (int i = 1; i <= 10; i++) {
/* 227 */         mw.getProfile(player.getDisplayName()).clearActionData(biome + "_explore_point" + i);
/*     */       }
/* 229 */       mw.getProfile(player.getDisplayName()).clearTag("action_explore_" + biome);
/* 230 */       mw.getProfile(player.getDisplayName()).setTag("action_explore_" + biome + "_complete");
/* 231 */       return;
/*     */     }
/*     */     
/* 234 */     mw.getProfile(player.getDisplayName()).setActionData(biome + "_explore_point" + nbtest, new Point(player).getIntString());
/* 235 */     mw.getProfile(player.getDisplayName()).setActionData(biome + "_explore_nbcomplete", "" + nbtest);
/* 236 */     ServerSender.sendTranslatedSentence(player, '7', "actions." + biome + "_continue", new String[] { "" + nbtest * 5 });
/*     */     
/* 238 */     MillCommonUtilities.spawnMobsAround(mw.world, new Point(player), 20, mob, 2, 4);
/*     */   }
/*     */   
/*     */ 
/*     */   private static void handleEnchantmentTable(MillWorld mw, EntityPlayer player)
/*     */   {
/* 244 */     if ((!mw.getProfile(player.getDisplayName()).isTagSet("action_build_enchantment_table")) || (mw.getProfile(player.getDisplayName()).isTagSet("action_build_enchantment_table_complete"))) {
/* 245 */       return;
/*     */     }
/*     */     
/* 248 */     boolean closeEnough = false;
/*     */     
/* 250 */     for (int i = 0; i < mw.loneBuildingsList.types.size(); i++) {
/* 251 */       if ((((String)mw.loneBuildingsList.types.get(i)).equals("sadhutree")) && 
/* 252 */         (((Point)mw.loneBuildingsList.pos.get(i)).distanceToSquared(player) < 100.0D)) {
/* 253 */         closeEnough = true;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 258 */     if (!closeEnough) {
/* 259 */       return;
/*     */     }
/*     */     
/* 262 */     for (int x = (int)player.field_70165_t - 5; x < (int)player.field_70165_t + 5; x++) {
/* 263 */       for (int z = (int)player.field_70161_v - 5; z < (int)player.field_70161_v + 5; z++) {
/* 264 */         for (int y = (int)player.field_70163_u - 3; y < (int)player.field_70163_u + 3; y++)
/*     */         {
/* 266 */           Block block = mw.world.func_147439_a(x, y, z);
/*     */           
/* 268 */           if (block == Blocks.field_150381_bn)
/*     */           {
/* 270 */             int nbBookShelves = 0;
/*     */             
/* 272 */             for (int dx = -1; dx <= 1; dx++) {
/* 273 */               for (int dz = -1; dz <= 1; dz++) {
/* 274 */                 if (((dx != 0) || (dz != 0)) && (mw.world.func_147437_c(x + dx, y, z + dz)) && (mw.world.func_147437_c(x + dx, y + 1, z + dz)))
/*     */                 {
/*     */ 
/*     */ 
/* 278 */                   if (mw.world.func_147439_a(x + dx * 2, y, z + dz * 2) == Blocks.field_150342_X) {
/* 279 */                     nbBookShelves++;
/*     */                   }
/*     */                   
/* 282 */                   if (mw.world.func_147439_a(x + dx * 2, y + 1, z + dz * 2) == Blocks.field_150342_X) {
/* 283 */                     nbBookShelves++;
/*     */                   }
/*     */                   
/* 286 */                   if ((dz != 0) && (dx != 0))
/*     */                   {
/*     */ 
/*     */ 
/* 290 */                     if (mw.world.func_147439_a(x + dx * 2, y, z + dz) == Blocks.field_150342_X) {
/* 291 */                       nbBookShelves++;
/*     */                     }
/*     */                     
/* 294 */                     if (mw.world.func_147439_a(x + dx * 2, y + 1, z + dz) == Blocks.field_150342_X) {
/* 295 */                       nbBookShelves++;
/*     */                     }
/*     */                     
/* 298 */                     if (mw.world.func_147439_a(x + dx, y, z + dz * 2) == Blocks.field_150342_X) {
/* 299 */                       nbBookShelves++;
/*     */                     }
/*     */                     
/* 302 */                     if (mw.world.func_147439_a(x + dx, y + 1, z + dz * 2) == Blocks.field_150342_X)
/* 303 */                       nbBookShelves++;
/*     */                   }
/*     */                 }
/*     */               }
/*     */             }
/* 308 */             if (nbBookShelves > 0) {
/* 309 */               ServerSender.sendTranslatedSentence(player, '7', "actions.enchantmenttable_success", new String[0]);
/* 310 */               mw.getProfile(player.getDisplayName()).clearTag("action_build_enchantment_table");
/* 311 */               mw.getProfile(player.getDisplayName()).setTag("action_build_enchantment_table_complete");
/* 312 */               return;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private static void handleMayanSiege(MillWorld mw, EntityPlayer player)
/*     */   {
/* 323 */     if ((!mw.getProfile(player.getDisplayName()).isTagSet("action_mayansiege")) || (mw.getProfile(player.getDisplayName()).isTagSet("action_mayansiege_complete"))) {
/* 324 */       return;
/*     */     }
/*     */     
/* 327 */     String siegeStatus = mw.getProfile(player.getDisplayName()).getActionData("mayan_siege_status");
/*     */     
/* 329 */     if (siegeStatus == null) {
/* 330 */       for (Point p : mw.loneBuildingsList.pos) {
/* 331 */         Building b = mw.getBuilding(p);
/* 332 */         if ((b != null) && 
/* 333 */           (b.villageType.key.equals("questpyramid")) && (p.distanceTo(player) < 50.0D))
/*     */         {
/* 335 */           int nbGhasts = 0;int nbBlazes = 0;int nbSkel = 0;
/*     */           
/* 337 */           for (int i = 0; i < 12; i++) {
/* 338 */             Point spawn = b.location.pos.getRelative(-10 + MillCommonUtilities.randomInt(20), 20.0D, -10 + MillCommonUtilities.randomInt(20));
/* 339 */             EntityTargetedGhast ent = (EntityTargetedGhast)MillCommonUtilities.spawnMobsSpawner(mw.world, spawn, "MillGhast");
/* 340 */             if (ent != null) {
/* 341 */               ent.target = b.location.pos.getRelative(0.0D, 20.0D, 0.0D);
/* 342 */               nbGhasts++;
/*     */             }
/*     */           }
/*     */           
/* 346 */           for (int i = 0; i < 12; i++)
/*     */           {
/* 348 */             Point spawn = b.location.pos.getRelative(-5 + MillCommonUtilities.randomInt(10), 15.0D, -5 + MillCommonUtilities.randomInt(10));
/* 349 */             EntityTargetedBlaze ent = (EntityTargetedBlaze)MillCommonUtilities.spawnMobsSpawner(mw.world, spawn, "MillBlaze");
/* 350 */             if (ent != null) {
/* 351 */               ent.target = b.location.pos.getRelative(0.0D, 10.0D, 0.0D);
/* 352 */               nbBlazes++;
/*     */             }
/*     */           }
/*     */           
/* 356 */           for (int i = 0; i < 5; i++) {
/* 357 */             Point spawn = b.location.pos.getRelative(5.0D, 12.0D, -5 + MillCommonUtilities.randomInt(10));
/* 358 */             Entity ent = MillCommonUtilities.spawnMobsSpawner(mw.world, spawn, "MillWitherSkeleton");
/* 359 */             if (ent != null) {
/* 360 */               nbSkel++;
/*     */             }
/* 362 */             spawn = b.location.pos.getRelative(-5.0D, 12.0D, -5 + MillCommonUtilities.randomInt(10));
/* 363 */             ent = MillCommonUtilities.spawnMobsSpawner(mw.world, spawn, "MillWitherSkeleton");
/* 364 */             if (ent != null) {
/* 365 */               nbSkel++;
/*     */             }
/*     */           }
/*     */           
/* 369 */           mw.getProfile(player.getDisplayName()).setActionData("mayan_siege_status", "started");
/* 370 */           mw.getProfile(player.getDisplayName()).setActionData("mayan_siege_ghasts", "" + nbGhasts);
/* 371 */           mw.getProfile(player.getDisplayName()).setActionData("mayan_siege_blazes", "" + nbBlazes);
/* 372 */           mw.getProfile(player.getDisplayName()).setActionData("mayan_siege_skeletons", "" + nbSkel);
/*     */           
/* 374 */           ServerSender.sendTranslatedSentence(player, '7', "actions.mayan_siege_start", new String[] { "" + nbGhasts, "" + nbBlazes, "" + nbSkel });
/*     */         }
/*     */         
/*     */       }
/* 378 */     } else if (siegeStatus.equals("started"))
/*     */     {
/* 380 */       for (Point p : mw.loneBuildingsList.pos) {
/* 381 */         Building b = mw.getBuilding(p);
/* 382 */         if ((b != null) && 
/* 383 */           (b.villageType.key.equals("questpyramid")) && (p.distanceTo(player) < 50.0D)) {
/* 384 */           List<Entity> mobs = MillCommonUtilities.getEntitiesWithinAABB(mw.world, EntityTargetedGhast.class, b.location.pos, 128, 128);
/*     */           
/* 386 */           int nbGhasts = mobs.size();
/*     */           
/* 388 */           mobs = MillCommonUtilities.getEntitiesWithinAABB(mw.world, EntityTargetedBlaze.class, b.location.pos, 128, 128);
/*     */           
/* 390 */           int nbBlazes = mobs.size();
/*     */           
/* 392 */           mobs = MillCommonUtilities.getEntitiesWithinAABB(mw.world, EntityTargetedWitherSkeleton.class, b.location.pos, 128, 128);
/*     */           
/* 394 */           int nbSkel = mobs.size();
/*     */           
/* 396 */           if ((nbGhasts == 0) && (nbBlazes == 0) && (nbSkel == 0)) {
/* 397 */             mw.getProfile(player.getDisplayName()).setActionData("mayan_siege_status", "finished");
/* 398 */             mw.getProfile(player.getDisplayName()).setTag("action_mayansiege_complete");
/* 399 */             ServerSender.sendTranslatedSentence(player, '7', "actions.mayan_siege_success", new String[0]);
/*     */           } else {
/* 401 */             int oldGhasts = Integer.parseInt(mw.getProfile(player.getDisplayName()).getActionData("mayan_siege_ghasts"));
/* 402 */             int oldBlazes = Integer.parseInt(mw.getProfile(player.getDisplayName()).getActionData("mayan_siege_blazes"));
/* 403 */             int oldSkel = Integer.parseInt(mw.getProfile(player.getDisplayName()).getActionData("mayan_siege_skeletons"));
/*     */             
/* 405 */             if ((oldGhasts != nbGhasts) || (oldBlazes != nbBlazes) || (oldSkel != nbSkel)) {
/* 406 */               ServerSender.sendTranslatedSentence(player, '7', "actions.mayan_siege_update", new String[] { "" + nbGhasts, "" + nbBlazes, "" + nbSkel });
/* 407 */               mw.getProfile(player.getDisplayName()).setActionData("mayan_siege_ghasts", "" + nbGhasts);
/* 408 */               mw.getProfile(player.getDisplayName()).setActionData("mayan_siege_blazes", "" + nbBlazes);
/* 409 */               mw.getProfile(player.getDisplayName()).setActionData("mayan_siege_skeletons", "" + nbSkel);
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static void handleTheVoid(MillWorld mw, EntityPlayer player)
/*     */   {
/* 421 */     if ((!mw.getProfile(player.getDisplayName()).isTagSet("action_thevoid")) || (mw.getProfile(player.getDisplayName()).isTagSet("action_thevoid_complete"))) {
/* 422 */       return;
/*     */     }
/*     */     
/* 425 */     if (player.field_70163_u > 30.0D) {
/* 426 */       return;
/*     */     }
/*     */     
/* 429 */     for (int i = -5; i < 5; i++) {
/* 430 */       for (int j = -5; j < 5; j++)
/*     */       {
/* 432 */         Block block = mw.world.func_147439_a((int)player.field_70165_t + i, 0, (int)player.field_70161_v + j);
/*     */         
/* 434 */         if (block == Blocks.field_150350_a) {
/* 435 */           ServerSender.sendTranslatedSentence(player, '7', "actions.thevoid_success", new String[0]);
/* 436 */           mw.getProfile(player.getDisplayName()).clearTag("action_thevoid");
/* 437 */           mw.getProfile(player.getDisplayName()).setTag("action_thevoid_complete");
/* 438 */           return;
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private static void handleTopOfTheWorld(MillWorld mw, EntityPlayer player)
/*     */   {
/* 446 */     if ((!mw.getProfile(player.getDisplayName()).isTagSet("action_topoftheworld")) || (mw.getProfile(player.getDisplayName()).isTagSet("action_topoftheworld_complete"))) {
/* 447 */       return;
/*     */     }
/*     */     
/* 450 */     if (player.field_70163_u > 250.0D) {
/* 451 */       ServerSender.sendTranslatedSentence(player, '7', "actions.topoftheworld_success", new String[0]);
/* 452 */       mw.getProfile(player.getDisplayName()).clearTag("action_topoftheworld");
/* 453 */       mw.getProfile(player.getDisplayName()).setTag("action_topoftheworld_complete");
/* 454 */       return;
/*     */     }
/*     */   }
/*     */   
/*     */   private static void handleUnderwaterDive(MillWorld mw, EntityPlayer player) {
/* 459 */     if ((!mw.getProfile(player.getDisplayName()).isTagSet("action_underwater_dive")) || (mw.getProfile(player.getDisplayName()).isTagSet("action_underwater_dive_complete"))) {
/* 460 */       return;
/*     */     }
/*     */     
/* 463 */     Point p = new Point(player);
/*     */     
/* 465 */     int nbWater = 0;
/*     */     
/* 467 */     while (MillCommonUtilities.getBlock(mw.world, p) == Blocks.field_150355_j) {
/* 468 */       nbWater++;
/* 469 */       p = p.getAbove();
/*     */     }
/*     */     
/* 472 */     if (nbWater > 12) {
/* 473 */       ServerSender.sendTranslatedSentence(player, '7', "actions.underwaterdive_success", new String[0]);
/* 474 */       mw.getProfile(player.getDisplayName()).clearTag("action_underwater_dive");
/* 475 */       mw.getProfile(player.getDisplayName()).setTag("action_underwater_dive_complete");
/* 476 */       return;
/*     */     }
/*     */   }
/*     */   
/*     */   private static void handleUnderwaterGlass(MillWorld mw, EntityPlayer player) {
/* 481 */     if ((!mw.getProfile(player.getDisplayName()).isTagSet("action_underwater_glass")) || (mw.getProfile(player.getDisplayName()).isTagSet("action_underwater_glass_complete"))) {
/* 482 */       return;
/*     */     }
/*     */     
/* 485 */     Point p = new Point(player);
/*     */     
/* 487 */     Block block = MillCommonUtilities.getBlock(mw.world, p);
/*     */     
/* 489 */     while ((block != null) && (!MillCommonUtilities.isBlockOpaqueCube(block)) && (block != Blocks.field_150359_w) && (block != Blocks.field_150410_aZ)) {
/* 490 */       p = p.getAbove();
/* 491 */       block = MillCommonUtilities.getBlock(mw.world, p);
/*     */     }
/*     */     
/* 494 */     block = MillCommonUtilities.getBlock(mw.world, p);
/*     */     
/* 496 */     if ((block != Blocks.field_150359_w) && (block != Blocks.field_150410_aZ)) {
/* 497 */       return;
/*     */     }
/* 499 */     p = p.getAbove();
/* 500 */     int nbWater = 0;
/*     */     
/* 502 */     while (MillCommonUtilities.getBlock(mw.world, p) == Blocks.field_150355_j) {
/* 503 */       nbWater++;
/* 504 */       p = p.getAbove();
/*     */     }
/*     */     
/* 507 */     if (nbWater > 15) {
/* 508 */       ServerSender.sendTranslatedSentence(player, '7', "actions.underwaterglass_success", new String[0]);
/* 509 */       mw.getProfile(player.getDisplayName()).clearTag("action_underwater_glass");
/* 510 */       mw.getProfile(player.getDisplayName()).setTag("action_underwater_glass_complete");
/* 511 */       return;
/*     */     }
/*     */     
/* 514 */     if (nbWater > 1) {
/* 515 */       ServerSender.sendTranslatedSentence(player, '7', "actions.underwaterglass_notdeepenough", new String[0]);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void onTick(MillWorld mw, EntityPlayer player) {
/*     */     long startTime;
/*     */     long startTime;
/* 522 */     if (mw.lastWorldUpdate > 0L) {
/* 523 */       startTime = Math.max(mw.lastWorldUpdate + 1L, mw.world.func_72820_D() - 10L);
/*     */     } else {
/* 525 */       startTime = mw.world.func_72820_D();
/*     */     }
/*     */     
/* 528 */     for (long worldTime = startTime; worldTime <= mw.world.func_72820_D(); worldTime += 1L) {
/* 529 */       if (worldTime % 250L == 0L) {
/* 530 */         handleContinuousExplore(mw, player, worldTime, MLN.questBiomeForest, "Zombie", 2, 15);
/* 531 */         handleContinuousExplore(mw, player, worldTime, MLN.questBiomeDesert, "Skeleton", 2, 15);
/* 532 */         handleContinuousExplore(mw, player, worldTime, MLN.questBiomeMountain, "Spider", 2, 10);
/*     */       }
/* 534 */       if (worldTime % 500L == 0L) {
/* 535 */         handleUnderwaterGlass(mw, player);
/*     */       }
/* 537 */       if (worldTime % 100L == 0L) {
/* 538 */         handleUnderwaterDive(mw, player);
/* 539 */         handleTopOfTheWorld(mw, player);
/* 540 */         handleBottomOfTheWorld(mw, player);
/* 541 */         handleBorehole(mw, player);
/* 542 */         handleBoreholeTNT(mw, player);
/* 543 */         handleTheVoid(mw, player);
/* 544 */         handleEnchantmentTable(mw, player);
/*     */       }
/*     */       
/* 547 */       if (worldTime % 10L == 0L) {
/* 548 */         handleBoreholeTNTLit(mw, player);
/* 549 */         handleMayanSiege(mw, player);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\SpecialQuestActions.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */