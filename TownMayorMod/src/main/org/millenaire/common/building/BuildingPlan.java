/*      */ package org.millenaire.common.building;
/*      */ 
/*      */ import net.minecraft.init.Blocks;
/*      */ 
/*      */ public class BuildingPlan implements IBuildingPlan { public static final String bempty = "empty";
/*      */   public static final String bpreserveground = "preserveground";
/*      */   public static final String ballbuttrees = "allbuttrees";
/*      */   public static final String bgrass = "grass";
/*      */   public static final String bsoil = "soil";
/*      */   public static final String bricesoil = "ricesoil";
/*      */   public static final String bturmericsoil = "turmericsoil";
/*      */   public static final String bmaizesoil = "maizesoil";
/*      */   public static final String bcarrotsoil = "carrotsoil";
/*      */   public static final String bpotatosoil = "potatosoil";
/*      */   public static final String bsugarcanesoil = "sugarcanesoil";
/*      */   public static final String bnetherwartsoil = "netherwartsoil";
/*      */   public static final String bvinesoil = "vinesoil";
/*      */   public static final String bsilkwormblock = "silkwormblock";
/*      */   public static final String bcacaospot = "cacaospot";
/*      */   public static final String blockedchest = "lockedchest";
/*      */   public static final String bmainchest = "mainchest";
/*      */   public static final String bsleepingPos = "sleepingPos";
/*      */   public static final String bsellingPos = "sellingPos";
/*      */   public static final String bcraftingPos = "craftingPos";
/*      */   public static final String bdefendingPos = "defendingPos";
/*      */   public static final String bshelterPos = "shelterPos";
/*      */   public static final String bpathStartPos = "pathStartPos";
/*      */   public static final String bleasurePos = "leasurePos";
/*      */   public static final String blogoakvert = "logoakvert";
/*      */   public static final String blogoakhor = "logoakhor";
/*      */   public static final String blogpinevert = "logpinevert";
/*      */   public static final String blogpinehor = "logpinehor";
/*      */   public static final String blogbirchvert = "logbirchvert";
/*      */   public static final String blogbirchhor = "logbirchhor";
/*      */   public static final String blogjunglevert = "logjunglevert";
/*      */   public static final String blogjunglehor = "logjunglehor";
/*      */   public static final String bstonestairGuess = "stonestairGuess";
/*      */   public static final String bladderGuess = "ladderGuess";
/*      */   public static final String bsignwallGuess = "signwallGuess";
/*      */   public static final String bwoodstairsOakGuess = "woodstairsOakGuess";
/*      */   public static final String bwoodstairsOakTop = "woodstairsOakTop";
/*      */   public static final String bwoodstairsOakBottom = "woodstairsOakBottom";
/*      */   public static final String bwoodstairsOakLeft = "woodstairsOakLeft";
/*      */   public static final String bwoodstairsOakRight = "woodstairsOakRight";
/*      */   public static final String bwoodstairsPineTop = "woodstairsPineTop";
/*      */   public static final String bwoodstairsPineBottom = "woodstairsPineBottom"; public static final String bwoodstairsPineLeft = "woodstairsPineLeft"; public static final String bwoodstairsPineRight = "woodstairsPineRight";
/*      */   public static class LocationBuildingPair { public Building building; public BuildingLocation location;
/*   48 */     public LocationBuildingPair(Building b, BuildingLocation l) { this.building = b;
/*   49 */       this.location = l; }
/*      */     
/*      */      }
/*      */   
/*      */   public static final String bwoodstairsBirchTop = "woodstairsBirchTop";
/*      */   public static final String bwoodstairsBirchBottom = "woodstairsBirchBottom";
/*      */   public static final String bwoodstairsBirchLeft = "woodstairsBirchLeft";
/*      */   public static final String bwoodstairsBirchRight = "woodstairsBirchRight";
/*      */   public static final String bwoodstairsJungleTop = "woodstairsJungleTop";
/*      */   public static final String bwoodstairsJungleBottom = "woodstairsJungleBottom";
/*      */   public static final String bwoodstairsJungleLeft = "woodstairsJungleLeft";
/*      */   public static final String bwoodstairsJungleRight = "woodstairsJungleRight";
/*      */   public static final String bstonestairsTop = "stonestairsTop";
/*      */   public static final String bstonestairsBottom = "stonestairsBottom";
/*      */   public static final String bstonestairsLeft = "stonestairsLeft";
/*      */   public static final String bstonestairsRight = "stonestairsRight";
/*      */   public static final String bstonebrickstairsTop = "stonebrickstairsTop";
/*      */   public static final String bstonebrickstairsBottom = "stonebrickstairsBottom"; public static final String bstonebrickstairsLeft = "stonebrickstairsLeft"; public static final String bstonebrickstairsRight = "stonebrickstairsRight"; public static final String bbrickstairsTop = "brickstairsTop"; public static final String bbrickstairsBottom = "brickstairsBottom"; public static final String bbrickstairsLeft = "brickstairsLeft"; public static final String bbrickstairsRight = "brickstairsRight"; public static final String bsandstonestairsTop = "sandstonestairsTop"; public static final String bsandstonestairsBottom = "sandstonestairsBottom";
/*   67 */   public static class LocationReturn { public LocationReturn(BuildingLocation l) { this.location = l;
/*   68 */       this.errorCode = 0;
/*   69 */       this.errorPos = null; }
/*      */     
/*      */     public static final int OUTSIDE_RADIUS = 1;
/*      */     public static final int LOCATION_CLASH = 2;
/*   73 */     public static final int CONSTRUCTION_FORBIDEN = 3; public static final int WRONG_ALTITUDE = 4; public static final int DANGER = 5; public static final int NOT_REACHABLE = 4; public BuildingLocation location; public int errorCode; public org.millenaire.common.Point errorPos; public LocationReturn(int error, org.millenaire.common.Point p) { this.location = null;
/*   74 */       this.errorCode = error;
/*   75 */       this.errorPos = p; } }
/*      */   
/*      */   public static final String bsandstonestairsLeft = "sandstonestairsLeft";
/*      */   public static final String bsandstonestairsRight = "sandstonestairsRight";
/*      */   public static final String bwoodstairsOakInvTop = "woodstairsOakInvTop";
/*      */   public static final String bwoodstairsOakInvBottom = "woodstairsOakInvBottom";
/*      */   public static final String bwoodstairsOakInvLeft = "woodstairsOakInvLeft";
/*      */   public static final String bwoodstairsOakInvRight = "woodstairsOakInvRight";
/*      */   public static final String bwoodstairsPineInvTop = "woodstairsPineInvTop";
/*      */   public static final String bwoodstairsPineInvBottom = "woodstairsPineInvBottom";
/*      */   public static final String bwoodstairsPineInvLeft = "woodstairsPineInvLeft";
/*      */   public static final String bwoodstairsPineInvRight = "woodstairsPineInvRight"; public static final String bwoodstairsBirchInvTop = "woodstairsBirchInvTop"; public static final String bwoodstairsBirchInvBottom = "woodstairsBirchInvBottom"; public static final String bwoodstairsBirchInvLeft = "woodstairsBirchInvLeft"; public static final String bwoodstairsBirchInvRight = "woodstairsBirchInvRight"; public static final String bwoodstairsJungleInvTop = "woodstairsJungleInvTop"; public static final String bwoodstairsJungleInvBottom = "woodstairsJungleInvBottom"; public static final String bwoodstairsJungleInvLeft = "woodstairsJungleInvLeft"; public static final String bwoodstairsJungleInvRight = "woodstairsJungleInvRight"; public static final String bstonestairsInvTop = "stonestairsInvTop"; public static final String bstonestairsInvBottom = "stonestairsInvBottom"; public static final String bstonestairsInvLeft = "stonestairsInvLeft"; public static final String bstonestairsInvRight = "stonestairsInvRight"; public static final String bstonebrickstairsInvTop = "stonebrickstairsInvTop"; public static class StartingGood { public org.millenaire.common.InvItem item; public double probability; public int fixedNumber; public int randomNumber;
/*   87 */     StartingGood(org.millenaire.common.InvItem item, double probability, int fixedNumber, int randomNumber) { this.item = item;
/*   88 */       this.probability = probability;
/*   89 */       this.fixedNumber = fixedNumber;
/*   90 */       this.randomNumber = randomNumber; } }
/*      */   
/*      */   public static final String bstonebrickstairsInvBottom = "stonebrickstairsInvBottom";
/*      */   public static final String bstonebrickstairsInvLeft = "stonebrickstairsInvLeft";
/*      */   public static final String bstonebrickstairsInvRight = "stonebrickstairsInvRight";
/*      */   public static final String bbrickstairsInvTop = "brickstairsInvTop";
/*      */   public static final String bbrickstairsInvBottom = "brickstairsInvBottom";
/*      */   public static final String bbrickstairsInvLeft = "brickstairsInvLeft";
/*      */   public static final String bbrickstairsInvRight = "brickstairsInvRight";
/*      */   public static final String bsandstonestairsInvTop = "sandstonestairsInvTop";
/*      */   public static final String bsandstonestairsInvBottom = "sandstonestairsInvBottom";
/*      */   public static final String bsandstonestairsInvLeft = "sandstonestairsInvLeft";
/*      */   public static final String bsandstonestairsInvRight = "sandstonestairsInvRight";
/*      */   public static final String bbyzantinetiles_bottomtop = "byzantinetiles_bottomtop";
/*      */   public static final String bbyzantinetiles_leftright = "byzantinetiles_leftright";
/*      */   public static final String bbyzantinestonetiles_bottomtop = "byzantinestonetiles_bottomtop";
/*      */   public static final String bbyzantinestonetiles_leftright = "byzantinestonetiles_leftright";
/*      */   public static final String bbyzantineslab_bottomtop = "byzantinetileslab_bottomtop";
/*      */   public static final String bbyzantineslab_leftright = "byzantinetileslab_leftright";
/*      */   public static final String bbyzantineslab_bottomtop_inv = "byzantinetileslab_bottomtop_inv";
/*      */   public static final String bbyzantineslab_leftright_inv = "byzantinetileslab_leftright_inv";
/*      */   public static final String bsignpostTop = "signpostTop";
/*      */   public static final String bsignpostBottom = "signpostBottom";
/*      */   public static final String bsignpostLeft = "signpostLeft";
/*      */   public static final String bsignpostRight = "signpostRight";
/*      */   public static final String bsignwallTop = "signwallTop";
/*      */   public static final String bsignwallBottom = "signwallBottom";
/*      */   public static final String bsignwallLeft = "signwallLeft";
/*      */   public static final String bsignwallRight = "signwallRight";
/*      */   public static final String bladderTop = "ladderTop";
/*      */   public static final String bladderBottom = "ladderBottom";
/*      */   public static final String bladderLeft = "ladderLeft";
/*      */   public static final String bladderRight = "ladderRight";
/*      */   public static final String bcowspawn = "cowspawn";
/*      */   public static final String bsheepspawn = "sheepspawn";
/*      */   public static final String bchickenspawn = "chickenspawn";
/*      */   public static final String bpigspawn = "pigspawn";
/*      */   public static final String bsquidspawn = "squidspawn";
/*      */   public static final String bwolfspawn = "wolfspawn";
/*      */   public static final String bstonesource = "stonesource";
/*      */   public static final String bsandsource = "sandsource";
/*      */   public static final String bsandstonesource = "sandstonesource";
/*      */   public static final String bclaysource = "claysource";
/*      */   public static final String bgravelsource = "gravelsource";
/*      */   public static final String bfurnace = "furnace";
/*      */   public static final String bfreestone = "freestone";
/*      */   public static final String bfreesand = "freesand";
/*      */   public static final String bfreesandstone = "freesandstone";
/*      */   public static final String bfreegravel = "freegravel";
/*      */   public static final String btapestry = "tapestry";
/*      */   public static final String bstall = "stall";
/*      */   public static final String bfreewool = "freewool";
/*      */   public static final String bdoorTop = "doorTop";
/*      */   public static final String bdoorBottom = "doorBottom";
/*      */   public static final String bdoorRight = "doorRight";
/*      */   public static final String bdoorLeft = "doorLeft";
/*      */   public static final String birondoorTop = "irondoorTop";
/*      */   public static final String birondoorBottom = "irondoorBottom";
/*      */   public static final String birondoorRight = "irondoorRight";
/*      */   public static final String birondoorLeft = "irondoorLeft";
/*      */   public static final String btrapdoorTop = "trapdoorTop";
/*  151 */   public static final String btrapdoorBottom = "trapdoorBottom"; public static final String btrapdoorRight = "trapdoorRight"; public static final String btrapdoorLeft = "trapdoorLeft"; public static final String bfenceGateHorizontal = "fencegateHorizontal"; public static final String bfenceGateVertical = "fencegateVertical"; public static final String bbedTop = "bedTop"; public static final String bbedBottom = "bedBottom"; public static final String bbedRight = "bedRight"; public static final String bbedLeft = "bedLeft"; public static final String boakspawn = "oakspawn"; public static final String bpinespawn = "pinespawn"; public static final String bbirchspawn = "birchspawn"; public static final String bjunglespawn = "junglespawn"; public static final String bbrickspot = "brickspot"; public static final String bindianstatue = "indianstatue"; public static final String bmayanstatue = "mayanstatue"; public static final String bbyzantineiconsmall = "byzantineiconsmall"; public static final String bbyzantineiconmedium = "byzantineiconmedium"; public static final String bbyzantineiconlarge = "byzantineiconlarge"; public static final String bfishingspot = "fishingspot"; public static final String bspawnerskeleton = "spawnerskeleton"; public static final String bspawnerzombie = "spawnerzombie"; public static final String bspawnerspider = "spawnerspider"; public static final String bspawnercavespider = "spawnercavespider"; public static final String bspawnercreeper = "spawnercreeper"; public static final String bspawnerblaze = "spawnerblaze"; public static final String bdispenserunknownpowder = "dispenserunknownpowder"; public static final String bhealingspot = "healingspot"; public static final String bplainSignGuess = "plainSignGuess"; public static final String bbrewingstand = "brewingstand"; public static java.util.HashMap<Character, PointType> charPoints; public static java.util.HashMap<Integer, PointType> colourPoints = new java.util.HashMap();
/*      */   
/*  153 */   public static java.util.HashMap<Integer, PointType> reverseColourPoints = new java.util.HashMap();
/*      */   
/*      */   public static final int EAST_FACING = 3;
/*      */   
/*  157 */   private static final String EOL = System.getProperty("line.separator");
/*      */   
/*  159 */   public static boolean loadingDone = false;
/*      */   public static final int NORTH_FACING = 0;
/*      */   private static final String prefixWoodstairOak = "woodstairsOak";
/*      */   private static final String prefixWoodstairPine = "woodstairsPine";
/*      */   private static final String prefixWoodstairBirch = "woodstairsBirch";
/*      */   private static final String prefixWoodstairJungle = "woodstairsJungle";
/*      */   private static final String prefixStonestair = "stonestair";
/*      */   private static final String prefixBrickstair = "brickstairs";
/*      */   private static final String prefixBrickStonestair = "stonebrickstairs";
/*      */   private static final String prefixSandStoneStair = "standstonestairs";
/*  169 */   private static final String prefixLadder = "ladder"; private static final String prefixSign = "sign"; private static final String prefixDoor = "door"; private static final String prefixTrapdoor = "trapdoor"; private static final String prefixBed = "bed"; public static final int SOUTH_FACING = 2; public static final int WEST_FACING = 1; public static String TYPE_SUBBUILDING = "subbuilding";
/*      */   public int startLevel;
/*      */   
/*  172 */   public static org.millenaire.common.Point adjustForOrientation(int x, int y, int z, int xoffset, int zoffset, int orientation) { org.millenaire.common.Point pos = null;
/*  173 */     if (orientation == 0) {
/*  174 */       pos = new org.millenaire.common.Point(x + xoffset, y, z + zoffset);
/*  175 */     } else if (orientation == 1) {
/*  176 */       pos = new org.millenaire.common.Point(x + zoffset, y, z - xoffset);
/*  177 */     } else if (orientation == 2) {
/*  178 */       pos = new org.millenaire.common.Point(x - xoffset, y, z - zoffset);
/*  179 */     } else if (orientation == 3) {
/*  180 */       pos = new org.millenaire.common.Point(x - zoffset, y, z + xoffset);
/*      */     }
/*      */     
/*  183 */     return pos;
/*      */   }
/*      */   
/*      */   public int areaToClear;
/*      */   public static void exportBuilding(net.minecraft.entity.player.EntityPlayer player, net.minecraft.world.World world, org.millenaire.common.Point startPoint)
/*      */   {
/*      */     try
/*      */     {
/*  191 */       net.minecraft.tileentity.TileEntitySign sign = startPoint.getSign(world);
/*      */       
/*  193 */       if (sign == null) {
/*  194 */         return;
/*      */       }
/*      */       
/*  197 */       if ((sign.field_145915_a[0] == null) || (sign.field_145915_a[0].length() == 0)) {
/*  198 */         org.millenaire.common.forge.Mill.proxy.localTranslatedSentence(org.millenaire.common.forge.Mill.proxy.getTheSinglePlayer(), '6', "export.errornoname", new String[0]);
/*  199 */         return;
/*      */       }
/*      */       
/*  202 */       String planName = sign.field_145915_a[0];
/*      */       
/*  204 */       int xEnd = startPoint.getiX() + 1;
/*  205 */       boolean found = false;
/*  206 */       while ((!found) && (xEnd < startPoint.getiX() + 257)) {
/*  207 */         net.minecraft.block.Block block = world.func_147439_a(xEnd, startPoint.getiY(), startPoint.getiZ());
/*      */         
/*  209 */         if (block == Blocks.field_150472_an) {
/*  210 */           found = true;
/*  211 */           break;
/*      */         }
/*  213 */         xEnd++;
/*      */       }
/*      */       
/*  216 */       if (!found) {
/*  217 */         org.millenaire.common.forge.Mill.proxy.localTranslatedSentence(org.millenaire.common.forge.Mill.proxy.getTheSinglePlayer(), '6', "export.errornoendsigneast", new String[0]);
/*  218 */         return;
/*      */       }
/*      */       
/*  221 */       int zEnd = startPoint.getiZ() + 1;
/*  222 */       found = false;
/*  223 */       while ((!found) && (zEnd < startPoint.getiZ() + 257)) {
/*  224 */         net.minecraft.block.Block block = world.func_147439_a(startPoint.getiX(), startPoint.getiY(), zEnd);
/*      */         
/*  226 */         if (block == Blocks.field_150472_an) {
/*  227 */           found = true;
/*  228 */           break;
/*      */         }
/*  230 */         zEnd++;
/*      */       }
/*      */       
/*  233 */       if (!found) {
/*  234 */         org.millenaire.common.forge.Mill.proxy.localTranslatedSentence(org.millenaire.common.forge.Mill.proxy.getTheSinglePlayer(), '6', "export.errornoendsignsouth", new String[0]);
/*  235 */         return;
/*      */       }
/*      */       
/*  238 */       int length = xEnd - startPoint.getiX() - 1;
/*  239 */       int width = zEnd - startPoint.getiZ() - 1;
/*      */       
/*  241 */       java.io.File exportDir = new java.io.File(org.millenaire.common.forge.Mill.proxy.getCustomDir(), "exports");
/*  242 */       if (!exportDir.exists()) {
/*  243 */         exportDir.mkdirs();
/*      */       }
/*      */       
/*  246 */       java.io.File buildingFile = new java.io.File(exportDir, planName + "_A.txt");
/*      */       
/*  248 */       PointType[][][] existingPoints = (PointType[][][])null;
/*  249 */       int existingMinLevel = 0;
/*  250 */       int upgradeLevel = 0;
/*      */       
/*  252 */       if (buildingFile.exists())
/*      */       {
/*  254 */         BuildingPlanSet existingSet = new BuildingPlanSet(null, buildingFile.getName().substring(0, buildingFile.getName().length() - 6), exportDir);
/*  255 */         existingSet.loadPictPlans(true);
/*      */         
/*  257 */         if (((BuildingPlan[])existingSet.plans.get(0))[0].length != length) {
/*  258 */           org.millenaire.common.forge.Mill.proxy.localTranslatedSentence(org.millenaire.common.forge.Mill.proxy.getTheSinglePlayer(), '6', "export.errorlength", new String[] { "" + length, "" + ((BuildingPlan[])existingSet.plans.get(0))[0].length });
/*  259 */           return;
/*      */         }
/*  261 */         if (((BuildingPlan[])existingSet.plans.get(0))[0].width != width) {
/*  262 */           org.millenaire.common.forge.Mill.proxy.localTranslatedSentence(org.millenaire.common.forge.Mill.proxy.getTheSinglePlayer(), '6', "export.errorwidth", new String[] { "" + width, "" + ((BuildingPlan[])existingSet.plans.get(0))[0].width });
/*  263 */           return;
/*      */         }
/*      */         
/*  266 */         existingPoints = existingSet.getConsolidatedPlan(0, ((BuildingPlan[])existingSet.plans.get(0)).length - 1);
/*  267 */         existingMinLevel = existingSet.getMinLevel(0, ((BuildingPlan[])existingSet.plans.get(0)).length - 1);
/*  268 */         upgradeLevel = ((BuildingPlan[])existingSet.plans.get(0)).length;
/*      */       }
/*      */       
/*  271 */       int startLevel = -1;
/*      */       
/*  273 */       if ((sign.field_145915_a[2] != null) && (sign.field_145915_a[2].length() > 0)) {
/*      */         try {
/*  275 */           startLevel = Integer.parseInt(sign.field_145915_a[2]);
/*      */         } catch (Exception e) {
/*  277 */           org.millenaire.common.forge.Mill.proxy.localTranslatedSentence(org.millenaire.common.forge.Mill.proxy.getTheSinglePlayer(), '6', "export.errorstartinglevel", new String[0]);
/*      */         }
/*      */       } else {
/*  280 */         org.millenaire.common.forge.Mill.proxy.localTranslatedSentence(org.millenaire.common.forge.Mill.proxy.getTheSinglePlayer(), 'f', "export.defaultstartinglevel", new String[0]);
/*      */       }
/*      */       
/*  283 */       boolean exportSnow = false;
/*      */       
/*  285 */       if ((sign.field_145915_a[3] != null) && (sign.field_145915_a[3].equals("snow"))) {
/*  286 */         exportSnow = true;
/*      */       }
/*      */       
/*  289 */       java.util.List<PointType[][]> export = new java.util.ArrayList();
/*  290 */       boolean stop = false;
/*      */       
/*  292 */       int j = 0;
/*  293 */       while (!stop) {
/*  294 */         PointType[][] level = new PointType[length][width];
/*      */         
/*  296 */         boolean blockFound = false;
/*      */         
/*  298 */         for (int i = 0; i < length; i++) {
/*  299 */           for (int k = 0; k < width; k++) {
/*  300 */             level[i][k] = null;
/*      */             
/*  302 */             net.minecraft.block.Block block = world.func_147439_a(i + startPoint.getiX() + 1, j + startPoint.getiY() + startLevel, k + startPoint.getiZ() + 1);
/*  303 */             int meta = world.func_72805_g(i + startPoint.getiX() + 1, j + startPoint.getiY() + startLevel, k + startPoint.getiZ() + 1);
/*      */             
/*  305 */             if (block != Blocks.field_150350_a) {
/*  306 */               blockFound = true;
/*      */             }
/*      */             
/*  309 */             PointType pt = (PointType)reverseColourPoints.get(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(block, meta)));
/*      */             
/*  311 */             if (pt != null)
/*      */             {
/*  313 */               if ((exportSnow) || (pt.block != Blocks.field_150433_aE))
/*      */               {
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
/*  330 */                 PointType existing = null;
/*      */                 
/*  332 */                 if ((existingPoints != null) && (j + startLevel >= existingMinLevel) && (j + startLevel < existingMinLevel + existingPoints.length)) {
/*  333 */                   existing = existingPoints[(j + startLevel - existingMinLevel)][i][k];
/*  334 */                   if (existing == null) {
/*  335 */                     org.millenaire.common.MLN.major(null, "Existing pixel is null");
/*      */                   }
/*      */                 }
/*      */                 
/*  339 */                 if (existing == null) {
/*  340 */                   if ((pt.name != null) || (pt.block != Blocks.field_150350_a) || (upgradeLevel != 0)) {
/*  341 */                     level[i][k] = pt;
/*      */                   }
/*      */                 }
/*  344 */                 else if ((existing != pt) && ((!existing.isType("empty")) || (pt.block == Blocks.field_150350_a))) {
/*  345 */                   level[i][k] = pt;
/*      */                 }
/*      */               }
/*      */             }
/*      */             else {
/*  350 */               org.millenaire.common.forge.Mill.proxy.localTranslatedSentence(org.millenaire.common.forge.Mill.proxy.getTheSinglePlayer(), '6', "export.errorunknownblockid", new String[] { "" + block + "/" + meta + "/" + org.millenaire.common.core.MillCommonUtilities.getPointHash(block, meta) });
/*      */             }
/*      */           }
/*      */         }
/*      */         
/*      */ 
/*  356 */         if (blockFound) {
/*  357 */           export.add(level);
/*      */         } else {
/*  359 */           stop = true;
/*      */         }
/*      */         
/*  362 */         j++;
/*      */         
/*  364 */         if (j + startPoint.getiY() + startLevel >= 256) {
/*  365 */           stop = true;
/*      */         }
/*      */       }
/*      */       
/*  369 */       java.awt.image.BufferedImage pict = new java.awt.image.BufferedImage(export.size() * width + export.size() - 1, length, 1);
/*  370 */       java.awt.Graphics2D graphics = pict.createGraphics();
/*      */       
/*  372 */       graphics.setColor(new java.awt.Color(11730865));
/*  373 */       graphics.fillRect(0, 0, pict.getWidth(), pict.getHeight());
/*      */       
/*  375 */       for (j = 0; j < export.size(); j++) {
/*  376 */         PointType[][] level = (PointType[][])export.get(j);
/*  377 */         for (int i = 0; i < length; i++) {
/*  378 */           for (int k = 0; k < width; k++) {
/*  379 */             int colour = 16777215;
/*  380 */             PointType pt = level[i][k];
/*  381 */             if (pt != null) {
/*  382 */               colour = pt.colour;
/*      */             }
/*      */             
/*  385 */             graphics.setColor(new java.awt.Color(colour));
/*  386 */             graphics.fillRect(j * width + j + width - k - 1, i, 1, 1);
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*  391 */       String fileName = planName + "_A" + upgradeLevel + ".png";
/*      */       
/*  393 */       javax.imageio.ImageIO.write(pict, "png", new java.io.File(exportDir, fileName));
/*      */       
/*  395 */       if (upgradeLevel == 0) {
/*  396 */         java.io.BufferedWriter writer = org.millenaire.common.core.MillCommonUtilities.getWriter(new java.io.File(exportDir, planName + "_A.txt"));
/*      */         
/*  398 */         writer.write("native:nameinvillagelangue;name_en:inenglish;name_fr:enfrancais;around:4;startLevel:" + startLevel + ";orientation:3;width:" + width + ";length:" + length);
/*  399 */         writer.close();
/*      */       } else {
/*  401 */         java.io.BufferedReader reader = org.millenaire.common.core.MillCommonUtilities.getReader(new java.io.File(exportDir, planName + "_A.txt"));
/*      */         
/*  403 */         java.util.List<String> existing = new java.util.ArrayList();
/*  404 */         String line = reader.readLine();
/*      */         
/*  406 */         while (line != null) {
/*  407 */           existing.add(line);
/*  408 */           line = reader.readLine();
/*      */         }
/*  410 */         reader.close();
/*      */         
/*  412 */         for (int i = existing.size(); i <= upgradeLevel; i++) {
/*  413 */           existing.add("");
/*      */         }
/*      */         
/*  416 */         existing.add(upgradeLevel, "startLevel:" + startLevel);
/*      */         
/*  418 */         java.io.BufferedWriter writer = org.millenaire.common.core.MillCommonUtilities.getWriter(new java.io.File(exportDir, planName + "_A.txt"));
/*      */         
/*  420 */         for (String s : existing) {
/*  421 */           writer.write(s + org.millenaire.common.MLN.EOL);
/*      */         }
/*      */         
/*  424 */         writer.close();
/*      */       }
/*      */       
/*  427 */       org.millenaire.common.forge.Mill.proxy.localTranslatedSentence(org.millenaire.common.forge.Mill.proxy.getTheSinglePlayer(), 'f', "export.buildingexported", new String[] { planName });
/*      */     }
/*      */     catch (Exception e) {
/*  430 */       org.millenaire.common.MLN.printException("Error when trying to store a building: ", e);
/*      */     }
/*      */   }
/*      */   
/*      */   public static void generateBuildingRes() {
/*  435 */     java.io.File file = new java.io.File(org.millenaire.common.forge.Mill.proxy.getBaseDir(), "resources used.txt");
/*      */     try
/*      */     {
/*  438 */       java.io.BufferedWriter writer = org.millenaire.common.core.MillCommonUtilities.getWriter(file);
/*      */       
/*  440 */       if (org.millenaire.common.MLN.DEV) {
/*  441 */         generateSignBuildings(writer);
/*      */       }
/*      */       
/*  444 */       for (org.millenaire.common.Culture culture : org.millenaire.common.Culture.ListCultures) {
/*  445 */         writer.write(culture.key + ": " + EOL);
/*  446 */         generateVillageTypeListing(writer, culture.listVillageTypes);
/*  447 */         writer.write(EOL);
/*  448 */         generateVillageTypeListing(writer, culture.listLoneBuildingTypes);
/*      */       }
/*      */       
/*  451 */       writer.write(EOL);
/*  452 */       writer.write(EOL);
/*      */       
/*  454 */       for (org.millenaire.common.Culture culture : org.millenaire.common.Culture.ListCultures)
/*      */       {
/*  456 */         for (i$ = culture.ListPlanSets.iterator(); i$.hasNext();) { set = (BuildingPlanSet)i$.next();
/*      */           
/*  458 */           writer.write(((BuildingPlan[])set.plans.get(0))[0].nativeName + EOL + ((BuildingPlan[])set.plans.get(0))[0].buildingKey + EOL + EOL);
/*  459 */           writer.write("==Requirements==" + EOL);
/*      */           
/*  461 */           for (BuildingPlan[] plans : set.plans) {
/*  462 */             if (set.plans.size() > 1) {
/*  463 */               writer.write("===Variation " + (char)(65 + plans[0].variation) + "===" + EOL);
/*      */             }
/*      */             
/*  466 */             for (BuildingPlan plan : plans) {
/*  467 */               if (plan.level == 0) {
/*  468 */                 writer.write("Initial Construction" + EOL + EOL);
/*      */               } else {
/*  470 */                 writer.write("Upgrade " + plan.level + EOL + EOL);
/*      */               }
/*      */               
/*  473 */               writer.write("{| border=\"1\" cellpadding=\"1\" cellspacing=\"1\" style=\"width: 300px;\"" + EOL);
/*  474 */               writer.write("! scope=\"col\"|Resource" + EOL);
/*  475 */               writer.write("! scope=\"col\"|Quantity" + EOL);
/*  476 */               for (org.millenaire.common.InvItem key : plan.resCost.keySet()) {
/*  477 */                 writer.write("|-" + EOL);
/*  478 */                 writer.write("| style=\"text-align: center; \"|" + key.getName() + EOL);
/*  479 */                 writer.write("| style=\"text-align: center; \"|" + plan.resCost.get(key) + EOL);
/*      */               }
/*  481 */               writer.write("|}" + EOL + EOL + EOL);
/*      */             }
/*      */           }
/*      */         } }
/*      */       java.util.Iterator i$;
/*      */       BuildingPlanSet set;
/*  487 */       writer.close();
/*      */     }
/*      */     catch (Exception e) {
/*  490 */       org.millenaire.common.MLN.printException(e);
/*      */     }
/*      */     
/*  493 */     if (org.millenaire.common.MLN.LogBuildingPlan >= 1) {
/*  494 */       org.millenaire.common.MLN.major(null, "Wrote resources used.txt");
/*      */     }
/*      */   }
/*      */   
/*      */   private static void generateColourSheet()
/*      */   {
/*  500 */     if (org.millenaire.common.MLN.LogBuildingPlan >= 1) {
/*  501 */       org.millenaire.common.MLN.major(null, "Generating colour sheet.");
/*      */     }
/*      */     
/*  504 */     java.awt.image.BufferedImage pict = new java.awt.image.BufferedImage(200, colourPoints.size() * 20 + 25, 1);
/*  505 */     java.awt.Graphics2D graphics = pict.createGraphics();
/*      */     
/*  507 */     graphics.setColor(new java.awt.Color(16777215));
/*  508 */     graphics.fillRect(0, 0, pict.getWidth(), pict.getHeight());
/*      */     
/*  510 */     graphics.setColor(new java.awt.Color(0));
/*  511 */     graphics.drawString("Generated colour sheet.", 5, 20);
/*      */     
/*  513 */     int pos = 1;
/*      */     
/*  515 */     for (java.io.File loadDir : org.millenaire.common.forge.Mill.loadingDirs)
/*      */     {
/*  517 */       java.io.File mainList = new java.io.File(loadDir, "blocklist.txt");
/*      */       
/*  519 */       if (mainList.exists()) {
/*  520 */         pos = generateColourSheetHandleFile(graphics, pos, mainList);
/*      */       }
/*      */     }
/*      */     try
/*      */     {
/*  525 */       javax.imageio.ImageIO.write(pict, "png", new java.io.File(org.millenaire.common.forge.Mill.proxy.getBaseDir(), "Colour Sheet.png"));
/*      */     }
/*      */     catch (Exception e) {
/*  528 */       org.millenaire.common.MLN.printException(e);
/*      */     }
/*      */     
/*  531 */     if (org.millenaire.common.MLN.LogBuildingPlan >= 1) {
/*  532 */       org.millenaire.common.MLN.major(null, "Finished generating colour sheet.");
/*      */     }
/*      */   }
/*      */   
/*      */   private static int generateColourSheetHandleFile(java.awt.Graphics2D graphics, int pos, java.io.File file)
/*      */   {
/*      */     try
/*      */     {
/*  540 */       java.io.BufferedReader reader = org.millenaire.common.core.MillCommonUtilities.getReader(file);
/*      */       
/*      */       String line;
/*      */       
/*  544 */       while ((line = reader.readLine()) != null) {
/*  545 */         if ((line.trim().length() > 0) && (!line.startsWith("//")))
/*      */         {
/*  547 */           String[] params = line.split(";", -1);
/*  548 */           String[] rgb = params[4].split("/", -1);
/*      */           
/*  550 */           int colour = (Integer.parseInt(rgb[0]) << 16) + (Integer.parseInt(rgb[1]) << 8) + (Integer.parseInt(rgb[2]) << 0);
/*      */           
/*  552 */           graphics.setColor(new java.awt.Color(0));
/*  553 */           graphics.drawString(params[0], 20, 17 + 20 * pos);
/*      */           
/*  555 */           graphics.setColor(new java.awt.Color(colour));
/*  556 */           graphics.fillRect(0, 5 + 20 * pos, 15, 15);
/*      */           
/*  558 */           pos++;
/*      */         }
/*      */       }
/*      */     }
/*      */     catch (Exception e)
/*      */     {
/*  564 */       org.millenaire.common.MLN.printException(e);
/*      */     }
/*      */     
/*  567 */     return pos;
/*      */   }
/*      */   
/*      */   private static void generateSignBuildings(java.io.BufferedWriter writer) throws Exception
/*      */   {
/*  572 */     writer.write(EOL + EOL + EOL + "Buildings with signs (not panels):" + EOL + EOL + EOL);
/*      */     
/*  574 */     for (org.millenaire.common.Culture culture : org.millenaire.common.Culture.ListCultures)
/*      */     {
/*  576 */       for (BuildingPlanSet set : culture.ListPlanSets) {
/*  577 */         for (BuildingPlan[] plans : set.plans) {
/*  578 */           for (BuildingPlan plan : plans) {
/*  579 */             if (!plan.tags.contains("hof")) {
/*  580 */               for (PointType[][] level : plan.plan) {
/*  581 */                 for (PointType[] row : level) {
/*  582 */                   for (PointType pt : row) {
/*  583 */                     if ((pt != null) && (pt.name != null) && (pt.name.startsWith("plainSignGuess"))) {
/*  584 */                       writer.write("Sign in " + plan.toString() + EOL);
/*      */                     }
/*      */                   }
/*      */                 }
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*  594 */     writer.write(EOL + EOL + EOL);
/*      */   }
/*      */   
/*      */   private static void generateVillageTypeListing(java.io.BufferedWriter writer, java.util.List<org.millenaire.common.VillageType> villages) throws java.io.IOException {
/*  598 */     for (org.millenaire.common.VillageType villageType : villages)
/*      */     {
/*  600 */       java.util.HashMap<org.millenaire.common.InvItem, Integer> cultureRes = new java.util.HashMap();
/*      */       
/*  602 */       for (java.util.List<BuildingProject> projects : villageType.getBuildingProjects().values()) {
/*  603 */         for (BuildingProject project : projects) {
/*  604 */           if (project.planSet != null) {
/*  605 */             for (BuildingPlan[] plans : project.planSet.plans) { BuildingPlan plan;
/*  606 */               for (plan : plans) {
/*  607 */                 for (org.millenaire.common.InvItem key : plan.resCost.keySet()) {
/*  608 */                   if (cultureRes.containsKey(key)) {
/*  609 */                     cultureRes.put(key, Integer.valueOf(((Integer)cultureRes.get(key)).intValue() + ((Integer)plan.resCost.get(key)).intValue()));
/*      */                   } else {
/*  611 */                     cultureRes.put(key, plan.resCost.get(key));
/*      */                   }
/*      */                 }
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*  620 */       writer.write(villageType.key + " resource use: " + EOL);
/*  621 */       for (org.millenaire.common.InvItem key : cultureRes.keySet()) {
/*  622 */         writer.write(key.getName() + ": " + cultureRes.get(key) + EOL);
/*      */       }
/*  624 */       writer.write(EOL);
/*      */     }
/*      */   }
/*      */   
/*      */   public static void generateWikiTable() throws org.millenaire.common.MLN.MillenaireException
/*      */   {
/*  630 */     java.util.HashMap<org.millenaire.common.InvItem, String> picts = new java.util.HashMap();
/*  631 */     java.util.HashMap<org.millenaire.common.InvItem, String> links = new java.util.HashMap();
/*      */     
/*  633 */     picts.put(new org.millenaire.common.InvItem(Blocks.field_150364_r, -1), "Wood_Any.gif");
/*  634 */     picts.put(new org.millenaire.common.InvItem(Blocks.field_150364_r, 0), "Wood.png");
/*  635 */     picts.put(new org.millenaire.common.InvItem(Blocks.field_150364_r, 1), "Wood_Pine.png");
/*  636 */     picts.put(new org.millenaire.common.InvItem(Blocks.field_150364_r, 2), "Wood_Birch.png");
/*  637 */     picts.put(new org.millenaire.common.InvItem(Blocks.field_150364_r, 3), "Wood_Jungle.png");
/*      */     
/*  639 */     picts.put(new org.millenaire.common.InvItem(Blocks.field_150348_b, 0), "Stone.png");
/*  640 */     picts.put(new org.millenaire.common.InvItem(Blocks.field_150359_w, 0), "Glass.png");
/*  641 */     picts.put(new org.millenaire.common.InvItem(Blocks.field_150325_L, 0), "White_Wool.png");
/*  642 */     picts.put(new org.millenaire.common.InvItem(Blocks.field_150322_A, 0), "Sandstone.png");
/*  643 */     picts.put(new org.millenaire.common.InvItem(Blocks.field_150347_e, 0), "Cobblestone.png");
/*  644 */     picts.put(new org.millenaire.common.InvItem(Blocks.field_150336_V, 0), "Brick.png");
/*  645 */     picts.put(new org.millenaire.common.InvItem(Blocks.field_150354_m, 0), "Sand.png");
/*  646 */     picts.put(new org.millenaire.common.InvItem(Blocks.field_150426_aN, 0), "Glowstone_(Block).png");
/*  647 */     picts.put(new org.millenaire.common.InvItem(Blocks.field_150342_X, 0), "Bookshelf.png");
/*  648 */     picts.put(new org.millenaire.common.InvItem(Blocks.field_150351_n, 0), "Gravel.png");
/*  649 */     picts.put(new org.millenaire.common.InvItem(Blocks.field_150322_A, 2), "SmoothSandstone.png");
/*  650 */     picts.put(new org.millenaire.common.InvItem(Blocks.field_150417_aV, 3), "ChiselledStoneBricks.png");
/*  651 */     picts.put(new org.millenaire.common.InvItem(Blocks.field_150417_aV, 2), "CrackedStoneBricks.png");
/*  652 */     picts.put(new org.millenaire.common.InvItem(Blocks.field_150329_H, 1), "TallGrass.png");
/*  653 */     picts.put(new org.millenaire.common.InvItem(Blocks.field_150329_H, 2), "Fern.png");
/*  654 */     picts.put(new org.millenaire.common.InvItem(Blocks.field_150341_Y, 0), "MossyCobblestone.png");
/*  655 */     picts.put(new org.millenaire.common.InvItem(Blocks.field_150417_aV, 1), "MossyStoneBricks.png");
/*  656 */     picts.put(new org.millenaire.common.InvItem(Blocks.field_150366_p, 0), "Ore_Iron.png");
/*  657 */     picts.put(new org.millenaire.common.InvItem(Blocks.field_150365_q, 0), "Ore_Coal.png");
/*  658 */     picts.put(new org.millenaire.common.InvItem(Blocks.field_150352_o, 0), "Ore_Gold.png");
/*  659 */     picts.put(new org.millenaire.common.InvItem(Blocks.field_150450_ax, 0), "Ore_Redstone.png");
/*  660 */     picts.put(new org.millenaire.common.InvItem(Blocks.field_150369_x, 0), "Ore_Lapis_Lazuli.png");
/*  661 */     picts.put(new org.millenaire.common.InvItem(Blocks.field_150482_ag, 0), "Ore_Diamond.png");
/*  662 */     picts.put(new org.millenaire.common.InvItem(Blocks.field_150428_aP, 0), "Jack-O-Lantern.png");
/*  663 */     picts.put(new org.millenaire.common.InvItem(Blocks.field_150440_ba, 0), "Melon (Block).png");
/*  664 */     picts.put(new org.millenaire.common.InvItem(Blocks.field_150368_y, 0), "Lapis_Lazuli_(Block).png");
/*  665 */     picts.put(new org.millenaire.common.InvItem(Blocks.field_150437_az, 0), "Redstone_Torch.png");
/*  666 */     picts.put(new org.millenaire.common.InvItem(Blocks.field_150357_h, 0), "Bedrock.png");
/*  667 */     picts.put(new org.millenaire.common.InvItem(Blocks.field_150388_bm, 0), "Nether_Wart.png");
/*  668 */     picts.put(new org.millenaire.common.InvItem(Blocks.field_150353_l, 0), "Lava.png");
/*  669 */     picts.put(new org.millenaire.common.InvItem(Blocks.field_150356_k, 0), "Lava.png");
/*  670 */     picts.put(new org.millenaire.common.InvItem(Blocks.field_150430_aB, 0), "Stone_Button.png");
/*  671 */     picts.put(new org.millenaire.common.InvItem(Blocks.field_150488_af, 0), "Redstone_Dust.png");
/*  672 */     picts.put(new org.millenaire.common.InvItem(Blocks.field_150348_b, 0), "Stone.png");
/*      */     
/*  674 */     picts.put(new org.millenaire.common.InvItem(net.minecraft.init.Items.field_151042_j, 0), "Ironitm.png");
/*  675 */     picts.put(new org.millenaire.common.InvItem(net.minecraft.init.Items.field_151043_k, 0), "Golditm.png");
/*      */     
/*  677 */     picts.put(new org.millenaire.common.InvItem(org.millenaire.common.forge.Mill.wood_decoration, 0), "ML_colombages_plain.png");
/*  678 */     links.put(new org.millenaire.common.InvItem(org.millenaire.common.forge.Mill.wood_decoration, 0), "|link=Norman:Colombages");
/*  679 */     picts.put(new org.millenaire.common.InvItem(org.millenaire.common.forge.Mill.wood_decoration, 1), "ML_colombages_cross.png");
/*  680 */     links.put(new org.millenaire.common.InvItem(org.millenaire.common.forge.Mill.wood_decoration, 1), "|link=Norman:Colombages");
/*  681 */     picts.put(new org.millenaire.common.InvItem(org.millenaire.common.forge.Mill.wood_decoration, 2), "ML_Thatch.png");
/*  682 */     links.put(new org.millenaire.common.InvItem(org.millenaire.common.forge.Mill.wood_decoration, 2), "|link=Japanese:Thatch");
/*      */     
/*  684 */     picts.put(new org.millenaire.common.InvItem(org.millenaire.common.forge.Mill.stone_decoration, 0), "ML_whitewashedbricks.png");
/*  685 */     links.put(new org.millenaire.common.InvItem(org.millenaire.common.forge.Mill.stone_decoration, 0), "|link=Hindi:Cooked brick");
/*  686 */     picts.put(new org.millenaire.common.InvItem(org.millenaire.common.forge.Mill.stone_decoration, 1), "ML_mudbrick.png");
/*  687 */     links.put(new org.millenaire.common.InvItem(org.millenaire.common.forge.Mill.stone_decoration, 1), "|link=Hindi:Mud brick");
/*  688 */     picts.put(new org.millenaire.common.InvItem(org.millenaire.common.forge.Mill.stone_decoration, 2), "ML_Mayan_Gold.png");
/*  689 */     links.put(new org.millenaire.common.InvItem(org.millenaire.common.forge.Mill.stone_decoration, 2), "|link=Mayan:Gold Ornament");
/*      */     
/*  691 */     picts.put(new org.millenaire.common.InvItem(org.millenaire.common.forge.Mill.paperWall, 0), "ML_paperwall.png");
/*  692 */     links.put(new org.millenaire.common.InvItem(org.millenaire.common.forge.Mill.paperWall, 0), "|link=Japanese:Paper Wall");
/*      */     
/*  694 */     picts.put(new org.millenaire.common.InvItem(org.millenaire.common.forge.Mill.tapestry, 0), "ML_tapestry.png");
/*  695 */     links.put(new org.millenaire.common.InvItem(org.millenaire.common.forge.Mill.tapestry, 0), "|link=Norman:Tapisserie");
/*      */     
/*  697 */     picts.put(new org.millenaire.common.InvItem(org.millenaire.common.forge.Mill.indianstatue, 0), "ML_IndianStatue.png");
/*  698 */     links.put(new org.millenaire.common.InvItem(org.millenaire.common.forge.Mill.indianstatue, 0), "|link=Hindi:Statue");
/*      */     
/*  700 */     picts.put(new org.millenaire.common.InvItem(org.millenaire.common.forge.Mill.mayanstatue, 0), "ML_MayanStatue.png");
/*  701 */     links.put(new org.millenaire.common.InvItem(org.millenaire.common.forge.Mill.mayanstatue, 0), "|link=Mayan:Carving");
/*      */     
/*  703 */     picts.put(new org.millenaire.common.InvItem(org.millenaire.common.forge.Mill.byzantineiconsmall, 0), "ML_ByzantineIconSmall.png");
/*  704 */     links.put(new org.millenaire.common.InvItem(org.millenaire.common.forge.Mill.byzantineiconsmall, 0), "|link=Byzantine:IIcon");
/*  705 */     picts.put(new org.millenaire.common.InvItem(org.millenaire.common.forge.Mill.byzantineiconmedium, 0), "ML_ByzantineIconMedium.png");
/*  706 */     links.put(new org.millenaire.common.InvItem(org.millenaire.common.forge.Mill.byzantineiconmedium, 0), "|link=Byzantine:IIcon");
/*  707 */     picts.put(new org.millenaire.common.InvItem(org.millenaire.common.forge.Mill.byzantineiconlarge, 0), "ML_ByzantineIconLarge.png");
/*  708 */     links.put(new org.millenaire.common.InvItem(org.millenaire.common.forge.Mill.byzantineiconlarge, 0), "|link=Byzantine:IIcon");
/*      */     
/*  710 */     picts.put(new org.millenaire.common.InvItem(org.millenaire.common.forge.Mill.byzantine_tiles, 0), "ML_byzSlab.png");
/*  711 */     links.put(new org.millenaire.common.InvItem(org.millenaire.common.forge.Mill.byzantine_tiles, 0), "|link=Blocks#Byzantine");
/*      */     
/*      */     try
/*      */     {
/*  715 */       java.util.HashMap<String, Integer> nameCount = new java.util.HashMap();
/*  716 */       java.util.HashMap<BuildingPlanSet, String> uniqueNames = new java.util.HashMap();
/*      */       
/*  718 */       for (org.millenaire.common.Culture culture : org.millenaire.common.Culture.ListCultures) {
/*  719 */         for (BuildingPlanSet set : culture.ListPlanSets) {
/*  720 */           String name = ((BuildingPlan[])set.plans.get(0))[0].nativeName;
/*  721 */           if (!nameCount.containsKey(name)) {
/*  722 */             nameCount.put(name, Integer.valueOf(1));
/*      */           } else {
/*  724 */             nameCount.put(name, Integer.valueOf(((Integer)nameCount.get(name)).intValue() + 1));
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*  729 */       for (org.millenaire.common.Culture culture : org.millenaire.common.Culture.ListCultures) {
/*  730 */         for (BuildingPlanSet set : culture.ListPlanSets) {
/*  731 */           if (((Integer)nameCount.get(((BuildingPlan[])set.plans.get(0))[0].nativeName)).intValue() > 1) {
/*  732 */             uniqueNames.put(set, ((BuildingPlan[])set.plans.get(0))[0].nativeName + "~" + set.key);
/*      */           } else {
/*  734 */             uniqueNames.put(set, ((BuildingPlan[])set.plans.get(0))[0].nativeName);
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*  739 */       java.io.File file = new java.io.File(org.millenaire.common.forge.Mill.proxy.getBaseDir(), "resources used wiki building list.txt");
/*  740 */       java.io.BufferedWriter writer = org.millenaire.common.core.MillCommonUtilities.getWriter(file);
/*      */       
/*  742 */       writer.write("{| class=\"wikitable\"" + EOL);
/*  743 */       writer.write("|-" + EOL);
/*  744 */       writer.write("! Requirements Template Building Name" + EOL);
/*  745 */       writer.write("|-" + EOL);
/*      */       
/*  747 */       for (org.millenaire.common.Culture culture : org.millenaire.common.Culture.ListCultures) {
/*  748 */         for (BuildingPlanSet set : culture.ListPlanSets) {
/*  749 */           writer.write("! " + (String)uniqueNames.get(set) + EOL);
/*  750 */           writer.write("|-" + EOL);
/*      */         }
/*      */       }
/*      */       
/*  754 */       writer.write("|}");
/*      */       
/*  756 */       writer.close();
/*      */       
/*  758 */       file = new java.io.File(org.millenaire.common.forge.Mill.proxy.getBaseDir(), "resources used wiki.txt");
/*  759 */       writer = org.millenaire.common.core.MillCommonUtilities.getWriter(file);
/*      */       
/*  761 */       writer.write("{{#switch: {{{1|{{BASEPAGENAME}}}}}" + EOL);
/*      */       
/*  763 */       for (org.millenaire.common.Culture culture : org.millenaire.common.Culture.ListCultures) {
/*  764 */         for (BuildingPlanSet set : culture.ListPlanSets)
/*      */         {
/*  766 */           writer.write("|" + (String)uniqueNames.get(set) + " = <table><tr><td style=\"vertical-align:top;\">" + EOL);
/*      */           
/*  768 */           for (BuildingPlan[] plans : set.plans) {
/*  769 */             if (set.plans.size() > 1) {
/*  770 */               writer.write("<table class=\"reqirements\"><tr><th scope=\"col\" style=\"text-align:center;\">Variation " + (char)(65 + plans[0].variation) + "</th>");
/*      */             } else {
/*  772 */               writer.write("<table class=\"reqirements\"><tr><th scope=\"col\" style=\"text-align:center;\"></th>");
/*      */             }
/*      */             
/*  775 */             java.util.List<org.millenaire.common.InvItem> items = new java.util.ArrayList();
/*      */             
/*  777 */             for (BuildingPlan plan : plans) {
/*  778 */               for (org.millenaire.common.InvItem key : plan.resCost.keySet()) {
/*  779 */                 if (!items.contains(key)) {
/*  780 */                   items.add(key);
/*      */                 }
/*      */               }
/*      */             }
/*      */             
/*  785 */             java.util.Collections.sort(items);
/*      */             
/*  787 */             for (org.millenaire.common.InvItem key : items) {
/*  788 */               String pict = "Unknown Pict:" + key.item + "/" + key.meta;
/*  789 */               String link = "";
/*      */               
/*  791 */               if (picts.containsKey(key)) {
/*  792 */                 pict = (String)picts.get(key);
/*      */               }
/*      */               
/*  795 */               if (links.containsKey(key)) {
/*  796 */                 link = (String)links.get(key);
/*      */               }
/*      */               
/*  799 */               writer.write("<td>[[File:" + pict + "|32px" + link + "|" + key.getName() + "]]</td>");
/*      */             }
/*      */             
/*  802 */             writer.write("</tr>" + EOL);
/*      */             
/*  804 */             for (BuildingPlan plan : plans) {
/*  805 */               if (plan.level == 0) {
/*  806 */                 writer.write("<tr><th scope=\"row\">Construction</th>");
/*      */               } else {
/*  808 */                 writer.write("<tr><th scope=\"row\">Upgrade " + plan.level + "</th>");
/*      */               }
/*  810 */               for (org.millenaire.common.InvItem key : items) {
/*  811 */                 if (plan.resCost.containsKey(key)) {
/*  812 */                   writer.write("<td>" + plan.resCost.get(key) + "</td>");
/*      */                 } else {
/*  814 */                   writer.write("<td></td>");
/*      */                 }
/*      */               }
/*  817 */               writer.write("</tr>" + EOL);
/*      */             }
/*  819 */             writer.write("</table>" + EOL);
/*      */           }
/*  821 */           writer.write("</table>" + EOL + EOL);
/*      */         }
/*      */       }
/*      */       
/*  825 */       writer.write("| #default = {{msgbox | title = Requirements not found| text = The requirements template couldn't find the upgrade table of the building you were looking for.Please consult the building list at [[Template:Requirements|this page]] to find the correct name.}}}}<noinclude>[[Category:Templates formatting|{{PAGENAME}}]]{{documentation}}</noinclude>");
/*      */       
/*      */ 
/*      */ 
/*  829 */       writer.close();
/*      */     }
/*      */     catch (Exception e) {
/*  832 */       org.millenaire.common.MLN.printException(e);
/*      */     }
/*      */     
/*  835 */     if (org.millenaire.common.MLN.LogBuildingPlan >= 1) {
/*  836 */       org.millenaire.common.MLN.major(null, "Wrote resources used wiki.txt");
/*      */     }
/*      */   }
/*      */   
/*      */   static String getColourString(int colour) {
/*  841 */     return ((colour & 0xFF0000) >> 16) + "/" + ((colour & 0xFF00) >> 8) + "/" + ((colour & 0xFF) >> 0) + "/" + Integer.toHexString(colour);
/*      */   }
/*      */   
/*      */   public static void importBuilding(net.minecraft.entity.player.EntityPlayer player, net.minecraft.world.World world, org.millenaire.common.Point startPoint)
/*      */   {
/*      */     try
/*      */     {
/*  848 */       net.minecraft.tileentity.TileEntitySign sign = startPoint.getSign(world);
/*      */       
/*  850 */       if (sign == null) {
/*  851 */         return;
/*      */       }
/*      */       
/*  854 */       if ((sign.field_145915_a[0] == null) || (sign.field_145915_a[0].length() == 0)) {
/*  855 */         org.millenaire.common.network.ServerSender.sendTranslatedSentence(player, '6', "import.errornoname", new String[0]);
/*  856 */         return;
/*      */       }
/*      */       
/*  859 */       String planName = sign.field_145915_a[0];
/*      */       
/*  861 */       java.io.File exportDir = new java.io.File(org.millenaire.common.forge.Mill.proxy.getCustomDir(), "exports");
/*  862 */       if (!exportDir.exists()) {
/*  863 */         exportDir.mkdirs();
/*      */       }
/*      */       
/*  866 */       java.io.File buildingFile = new java.io.File(exportDir, planName + "_A.txt");
/*      */       
/*  868 */       if (!buildingFile.exists()) {
/*  869 */         org.millenaire.common.network.ServerSender.sendTranslatedSentence(player, '6', "import.errornotfound", new String[0]);
/*  870 */         return;
/*      */       }
/*      */       
/*  873 */       BuildingPlanSet existingSet = new BuildingPlanSet(null, buildingFile.getName().substring(0, buildingFile.getName().length() - 6), exportDir);
/*  874 */       existingSet.loadPictPlans(true);
/*      */       
/*  876 */       int upgradeLevel = 0;
/*      */       
/*  878 */       if ((sign.field_145915_a[1] != null) && (sign.field_145915_a[1].length() > 0)) {
/*      */         try {
/*  880 */           upgradeLevel = Integer.parseInt(sign.field_145915_a[1]);
/*  881 */           org.millenaire.common.network.ServerSender.sendTranslatedSentence(player, 'f', "import.buildingupto", new String[] { "" + upgradeLevel });
/*      */         } catch (Exception e) {
/*  883 */           org.millenaire.common.network.ServerSender.sendTranslatedSentence(player, '6', "import.errorinvalidupgradelevel", new String[0]);
/*  884 */           return;
/*      */         }
/*      */       } else {
/*  887 */         org.millenaire.common.network.ServerSender.sendTranslatedSentence(player, 'f', "import.buildinginitialphase", new String[0]);
/*      */       }
/*      */       
/*  890 */       if (upgradeLevel >= ((BuildingPlan[])existingSet.plans.get(0)).length) {
/*  891 */         org.millenaire.common.network.ServerSender.sendTranslatedSentence(player, '6', "import.errorupgradeleveltoohigh", new String[0]);
/*  892 */         return;
/*      */       }
/*      */       
/*  895 */       if ((sign.field_145915_a[2] != null) && (sign.field_145915_a[2].equals("x2"))) {
/*  896 */         for (BuildingPlan[] plans : existingSet.plans) {
/*  897 */           for (BuildingPlan plan : plans) {
/*  898 */             PointType[][][] newPlan = new PointType[plan.plan.length * 2][plan.plan[0].length][plan.plan[0][0].length];
/*      */             
/*  900 */             for (int i = 0; i < plan.plan.length; i++) {
/*  901 */               for (int j = 0; j < plan.plan[0].length; j++) {
/*  902 */                 for (int k = 0; k < plan.plan[0][0].length; k++) {
/*  903 */                   newPlan[(i * 2)][j][k] = plan.plan[i][j][k];
/*  904 */                   newPlan[(i * 2 + 1)][j][k] = plan.plan[i][j][k];
/*      */                 }
/*      */               }
/*      */             }
/*      */             
/*  909 */             plan.plan = newPlan;
/*  910 */             plan.nbfloors *= 2;
/*      */           }
/*      */         }
/*  913 */         org.millenaire.common.network.ServerSender.sendTranslatedSentence(player, 'f', "import.doublevertical", new String[0]);
/*  914 */         org.millenaire.common.MLN.major(null, "Building height: " + ((BuildingPlan[])existingSet.plans.get(0))[0].plan.length);
/*      */       }
/*      */       
/*  917 */       BuildingPlan plan = ((BuildingPlan[])existingSet.plans.get(0))[0];
/*  918 */       BuildingLocation location = new BuildingLocation(plan, startPoint.getRelative(plan.length / 2 + 1, 0.0D, plan.width / 2 + 1), 0);
/*      */       
/*  920 */       for (int i = 0; i <= upgradeLevel; i++) {
/*  921 */         org.millenaire.common.network.ServerSender.sendTranslatedSentence(player, 'f', "import.buildinglevel", new String[] { "" + i });
/*  922 */         existingSet.buildLocation(org.millenaire.common.forge.Mill.getMillWorld(world), null, location, true, false, null, true, null);
/*  923 */         location.level += 1;
/*      */       }
/*      */     }
/*      */     catch (Exception e) {
/*  927 */       org.millenaire.common.MLN.printException("Error when importing a building:", e);
/*      */     }
/*      */   }
/*      */   
/*      */   public static boolean loadBuildingPoints()
/*      */   {
/*  933 */     for (java.io.File loadDir : org.millenaire.common.forge.Mill.loadingDirs)
/*      */     {
/*  935 */       java.io.File mainList = new java.io.File(loadDir, "blocklist.txt");
/*      */       
/*  937 */       if ((mainList.exists()) && 
/*  938 */         (loadBuildingPointsFile(mainList))) {
/*  939 */         return true;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  944 */     loadReverseBuildingPoints();
/*      */     
/*  946 */     if (org.millenaire.common.MLN.generateColourSheet) {
/*  947 */       generateColourSheet();
/*      */     }
/*      */     
/*  950 */     return false;
/*      */   }
/*      */   
/*      */   private static boolean loadBuildingPointsFile(java.io.File file)
/*      */   {
/*      */     try
/*      */     {
/*  957 */       java.io.BufferedReader reader = org.millenaire.common.core.MillCommonUtilities.getReader(file);
/*      */       
/*      */       String line;
/*      */       
/*  961 */       while ((line = reader.readLine()) != null) {
/*  962 */         if ((line.trim().length() > 0) && (!line.startsWith("//"))) {
/*  963 */           PointType cp = PointType.readColourPoint(line);
/*  964 */           for (PointType cp2 : colourPoints.values()) {
/*  965 */             if (cp2.colour == cp.colour) {
/*  966 */               throw new org.millenaire.common.MLN.MillenaireException("Colour " + getColourString(cp.colour) + " in line <" + line + "> is already taken.");
/*      */             }
/*      */           }
/*  969 */           colourPoints.put(Integer.valueOf(cp.colour), cp);
/*      */         }
/*      */       }
/*      */     } catch (Exception e) {
/*  973 */       org.millenaire.common.MLN.printException(e);
/*  974 */       return true;
/*      */     }
/*      */     
/*  977 */     charPoints = new java.util.HashMap();
/*  978 */     charPoints.put(Character.valueOf('-'), new PointType('-', "empty"));
/*  979 */     charPoints.put(Character.valueOf('*'), new PointType('*', "mainchest"));
/*  980 */     charPoints.put(Character.valueOf('G'), new PointType('G', "grass"));
/*  981 */     charPoints.put(Character.valueOf('s'), new PointType('s', "soil"));
/*  982 */     charPoints.put(Character.valueOf('H'), new PointType('H', "lockedchest"));
/*  983 */     charPoints.put(Character.valueOf('X'), new PointType('X', "sleepingPos"));
/*  984 */     charPoints.put(Character.valueOf('t'), new PointType('t', "woodstairsOakGuess"));
/*  985 */     charPoints.put(Character.valueOf('a'), new PointType('a', "stonestairGuess"));
/*  986 */     charPoints.put(Character.valueOf('L'), new PointType('L', "ladderGuess"));
/*  987 */     charPoints.put(Character.valueOf('S'), new PointType('S', "signwallGuess"));
/*      */     
/*  989 */     charPoints.put(Character.valueOf('/'), new PointType('/', Blocks.field_150350_a, 0, false));
/*  990 */     charPoints.put(Character.valueOf('d'), new PointType('d', Blocks.field_150346_d, 0, false));
/*  991 */     charPoints.put(Character.valueOf('p'), new PointType('p', Blocks.field_150344_f, 0, false));
/*  992 */     charPoints.put(Character.valueOf('g'), new PointType('g', Blocks.field_150359_w, 0, false));
/*  993 */     charPoints.put(Character.valueOf('c'), new PointType('c', Blocks.field_150347_e, 0, false));
/*  994 */     charPoints.put(Character.valueOf('C'), new PointType('C', Blocks.field_150462_ai, 0, false));
/*  995 */     charPoints.put(Character.valueOf('F'), new PointType('F', Blocks.field_150460_al, 0, false));
/*  996 */     charPoints.put(Character.valueOf('W'), new PointType('W', Blocks.field_150325_L, 0, false));
/*  997 */     charPoints.put(Character.valueOf('o'), new PointType('o', Blocks.field_150348_b, 0, false));
/*  998 */     charPoints.put(Character.valueOf('h'), new PointType('h', Blocks.field_150446_ar, 0, false));
/*  999 */     charPoints.put(Character.valueOf('I'), new PointType('I', Blocks.field_150339_S, 0, false));
/* 1000 */     charPoints.put(Character.valueOf('l'), new PointType('h', Blocks.field_150333_U, 0, false));
/* 1001 */     charPoints.put(Character.valueOf('T'), new PointType('T', Blocks.field_150478_aa, 0, true));
/* 1002 */     charPoints.put(Character.valueOf('f'), new PointType('f', Blocks.field_150422_aJ, 0, true));
/* 1003 */     charPoints.put(Character.valueOf('w'), new PointType('w', Blocks.field_150355_j, 0, true));
/* 1004 */     return false;
/*      */   }
/*      */   
/*      */   public static java.util.HashMap<String, BuildingPlanSet> loadPlans(java.util.List<java.io.File> culturesDirs, org.millenaire.common.Culture culture)
/*      */   {
/* 1009 */     java.util.HashMap<String, BuildingPlanSet> plans = new java.util.HashMap();
/*      */     
/* 1011 */     java.util.List<java.io.File> dirs = new java.util.ArrayList();
/* 1012 */     java.util.List<Boolean> isolatedDirs = new java.util.ArrayList();
/*      */     
/* 1014 */     for (java.io.File cultureDir : culturesDirs)
/*      */     {
/* 1016 */       java.io.File buildingsDir = new java.io.File(cultureDir, "buildings");
/*      */       
/* 1018 */       java.io.File coreDir = new java.io.File(buildingsDir, "core");
/* 1019 */       java.io.File extraDir = new java.io.File(buildingsDir, "extra");
/* 1020 */       java.io.File isolatedDir = new java.io.File(buildingsDir, "lone");
/*      */       
/* 1022 */       if (coreDir.exists()) {
/* 1023 */         dirs.add(coreDir);
/* 1024 */         isolatedDirs.add(Boolean.valueOf(false));
/*      */       }
/*      */       
/* 1027 */       if (extraDir.exists()) {
/* 1028 */         dirs.add(extraDir);
/* 1029 */         isolatedDirs.add(Boolean.valueOf(false));
/*      */       }
/*      */       
/* 1032 */       if (isolatedDir.exists()) {
/* 1033 */         dirs.add(isolatedDir);
/* 1034 */         isolatedDirs.add(Boolean.valueOf(true));
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1039 */     java.io.File customDir = new java.io.File(new java.io.File(new java.io.File(org.millenaire.common.forge.Mill.proxy.getCustomDir(), "cultures"), culture.key), "custom buildings");
/*      */     
/* 1041 */     if (customDir.exists()) {
/* 1042 */       dirs.add(customDir);
/* 1043 */       isolatedDirs.add(Boolean.valueOf(false));
/*      */     }
/*      */     
/* 1046 */     BuildingFileFiler textPlans = new BuildingFileFiler("_A0.txt");
/* 1047 */     BuildingFileFiler pictPlans = new BuildingFileFiler("_A.txt");
/*      */     
/* 1049 */     for (int i = 0; i < dirs.size(); i++)
/*      */     {
/* 1051 */       for (java.io.File file : ((java.io.File)dirs.get(i)).listFiles(textPlans)) {
/*      */         try
/*      */         {
/* 1054 */           if (org.millenaire.common.MLN.LogBuildingPlan >= 1) {
/* 1055 */             org.millenaire.common.MLN.major(file, "Loading building: " + file.getAbsolutePath());
/*      */           }
/*      */           
/* 1058 */           BuildingPlanSet set = new BuildingPlanSet(culture, file.getName().split("_")[0], (java.io.File)dirs.get(i));
/* 1059 */           set.loadPlans(culture, false);
/* 1060 */           if (((Boolean)isolatedDirs.get(i)).booleanValue()) {
/* 1061 */             set.max = 0;
/*      */           }
/* 1063 */           plans.put(set.key, set);
/*      */         } catch (Exception e) {
/* 1065 */           org.millenaire.common.MLN.printException("Error when loading " + file.getAbsolutePath(), e);
/*      */         }
/*      */       }
/*      */       
/* 1069 */       for (java.io.File file : ((java.io.File)dirs.get(i)).listFiles(pictPlans)) {
/*      */         try {
/* 1071 */           if (org.millenaire.common.MLN.LogBuildingPlan >= 1) {
/* 1072 */             org.millenaire.common.MLN.major(file, "Loading pict building: " + file.getAbsolutePath());
/*      */           }
/*      */           
/* 1075 */           BuildingPlanSet set = new BuildingPlanSet(culture, file.getName().substring(0, file.getName().length() - 6), (java.io.File)dirs.get(i));
/* 1076 */           set.loadPictPlans(false);
/* 1077 */           if (((Boolean)isolatedDirs.get(i)).booleanValue()) {
/* 1078 */             set.max = 0;
/*      */           }
/* 1080 */           plans.put(set.key, set);
/*      */         } catch (Exception e) {
/* 1082 */           org.millenaire.common.MLN.printException("Exception when loading " + file.getName() + " plan set in culture " + culture.key + ":", e);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1088 */     return plans;
/*      */   }
/*      */   
/*      */   public static java.util.HashMap<String, BuildingPlanSet> loadPlanSetMap(java.util.List<BuildingPlanSet> planSets)
/*      */   {
/* 1093 */     java.util.HashMap<String, BuildingPlanSet> map = new java.util.HashMap();
/*      */     
/* 1095 */     for (BuildingPlanSet set : planSets) {
/* 1096 */       map.put(set.key, set);
/*      */     }
/*      */     
/* 1099 */     return map;
/*      */   }
/*      */   
/*      */ 
/*      */   private static void loadReverseBuildingPoints()
/*      */   {
/* 1105 */     for (PointType pt : colourPoints.values()) {
/* 1106 */       if (pt.name == null)
/*      */       {
/* 1108 */         net.minecraft.block.Block block = pt.getBlock();
/*      */         
/* 1110 */         reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(pt.block, pt.meta)), pt);
/*      */         
/* 1112 */         if ((block == Blocks.field_150478_aa) || (block == Blocks.field_150437_az) || (block == Blocks.field_150362_t)) {
/* 1113 */           for (int i = 0; i < 16; i++) {
/* 1114 */             reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(pt.block, i)), pt);
/*      */           }
/* 1116 */         } else if ((block == org.millenaire.common.forge.Mill.path) || (block == org.millenaire.common.forge.Mill.pathSlab)) {
/* 1117 */           reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(pt.block, pt.meta & 0x7)), pt);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1124 */     for (PointType pt : colourPoints.values()) {
/* 1125 */       if (pt.name != null) {
/* 1126 */         if (pt.name.equals("preserveground")) {
/* 1127 */           reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(Blocks.field_150346_d, 0)), pt);
/* 1128 */           reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(Blocks.field_150349_c, 0)), pt);
/* 1129 */         } else if (pt.name.equals("lockedchest")) {
/* 1130 */           for (int i = 0; i < 16; i++) {
/* 1131 */             reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(Blocks.field_150486_ae, i)), pt);
/*      */           }
/* 1133 */           for (int i = 0; i < 16; i++) {
/* 1134 */             reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(org.millenaire.common.forge.Mill.lockedChest, i)), pt);
/*      */           }
/*      */         }
/* 1137 */         else if (pt.name.equals("logoakhor")) {
/* 1138 */           reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(Blocks.field_150364_r, 8)), pt);
/* 1139 */         } else if (pt.name.equals("logoakvert")) {
/* 1140 */           reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(Blocks.field_150364_r, 4)), pt);
/*      */         }
/* 1142 */         else if (pt.name.equals("logpinehor")) {
/* 1143 */           reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(Blocks.field_150364_r, 9)), pt);
/* 1144 */         } else if (pt.name.equals("logpinevert")) {
/* 1145 */           reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(Blocks.field_150364_r, 5)), pt);
/*      */         }
/* 1147 */         else if (pt.name.equals("logbirchhor")) {
/* 1148 */           reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(Blocks.field_150364_r, 10)), pt);
/* 1149 */         } else if (pt.name.equals("logbirchvert")) {
/* 1150 */           reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(Blocks.field_150364_r, 6)), pt);
/*      */         }
/* 1152 */         else if (pt.name.equals("logjunglehor")) {
/* 1153 */           reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(Blocks.field_150364_r, 11)), pt);
/* 1154 */         } else if (pt.name.equals("logjunglevert")) {
/* 1155 */           reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(Blocks.field_150364_r, 7)), pt);
/*      */ 
/*      */         }
/* 1158 */         else if (pt.name.equals("woodstairsOakTop")) {
/* 1159 */           reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(Blocks.field_150476_ad, 1)), pt);
/* 1160 */         } else if (pt.name.equals("woodstairsOakBottom")) {
/* 1161 */           reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(Blocks.field_150476_ad, 0)), pt);
/* 1162 */         } else if (pt.name.equals("woodstairsOakLeft")) {
/* 1163 */           reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(Blocks.field_150476_ad, 2)), pt);
/* 1164 */         } else if (pt.name.equals("woodstairsOakRight")) {
/* 1165 */           reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(Blocks.field_150476_ad, 3)), pt);
/* 1166 */         } else if (pt.name.equals("woodstairsPineTop")) {
/* 1167 */           reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(Blocks.field_150485_bF, 1)), pt);
/* 1168 */         } else if (pt.name.equals("woodstairsPineBottom")) {
/* 1169 */           reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(Blocks.field_150485_bF, 0)), pt);
/* 1170 */         } else if (pt.name.equals("woodstairsPineLeft")) {
/* 1171 */           reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(Blocks.field_150485_bF, 2)), pt);
/* 1172 */         } else if (pt.name.equals("woodstairsPineRight")) {
/* 1173 */           reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(Blocks.field_150485_bF, 3)), pt);
/*      */         }
/* 1175 */         else if (pt.name.equals("woodstairsBirchTop")) {
/* 1176 */           reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(Blocks.field_150487_bG, 1)), pt);
/* 1177 */         } else if (pt.name.equals("woodstairsBirchBottom")) {
/* 1178 */           reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(Blocks.field_150487_bG, 0)), pt);
/* 1179 */         } else if (pt.name.equals("woodstairsBirchLeft")) {
/* 1180 */           reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(Blocks.field_150487_bG, 2)), pt);
/* 1181 */         } else if (pt.name.equals("woodstairsBirchRight")) {
/* 1182 */           reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(Blocks.field_150487_bG, 3)), pt);
/*      */         }
/* 1184 */         else if (pt.name.equals("woodstairsJungleTop")) {
/* 1185 */           reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(Blocks.field_150481_bH, 1)), pt);
/* 1186 */         } else if (pt.name.equals("woodstairsJungleBottom")) {
/* 1187 */           reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(Blocks.field_150481_bH, 0)), pt);
/* 1188 */         } else if (pt.name.equals("woodstairsJungleLeft")) {
/* 1189 */           reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(Blocks.field_150481_bH, 2)), pt);
/* 1190 */         } else if (pt.name.equals("woodstairsJungleRight")) {
/* 1191 */           reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(Blocks.field_150481_bH, 3)), pt);
/*      */         }
/* 1193 */         else if (pt.name.equals("stonestairsTop")) {
/* 1194 */           reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(Blocks.field_150446_ar, 1)), pt);
/* 1195 */         } else if (pt.name.equals("stonestairsBottom")) {
/* 1196 */           reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(Blocks.field_150446_ar, 0)), pt);
/* 1197 */         } else if (pt.name.equals("stonestairsLeft")) {
/* 1198 */           reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(Blocks.field_150446_ar, 2)), pt);
/* 1199 */         } else if (pt.name.equals("stonestairsRight")) {
/* 1200 */           reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(Blocks.field_150446_ar, 3)), pt);
/*      */         }
/* 1202 */         else if (pt.name.equals("stonebrickstairsTop")) {
/* 1203 */           reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(Blocks.field_150390_bg, 1)), pt);
/* 1204 */         } else if (pt.name.equals("stonebrickstairsBottom")) {
/* 1205 */           reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(Blocks.field_150390_bg, 0)), pt);
/* 1206 */         } else if (pt.name.equals("stonebrickstairsLeft")) {
/* 1207 */           reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(Blocks.field_150390_bg, 2)), pt);
/* 1208 */         } else if (pt.name.equals("stonebrickstairsRight")) {
/* 1209 */           reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(Blocks.field_150390_bg, 3)), pt);
/*      */         }
/* 1211 */         else if (pt.name.equals("brickstairsTop")) {
/* 1212 */           reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(Blocks.field_150389_bf, 1)), pt);
/* 1213 */         } else if (pt.name.equals("brickstairsBottom")) {
/* 1214 */           reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(Blocks.field_150389_bf, 0)), pt);
/* 1215 */         } else if (pt.name.equals("brickstairsLeft")) {
/* 1216 */           reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(Blocks.field_150389_bf, 2)), pt);
/* 1217 */         } else if (pt.name.equals("brickstairsRight")) {
/* 1218 */           reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(Blocks.field_150389_bf, 3)), pt);
/*      */         }
/* 1220 */         else if (pt.name.equals("sandstonestairsTop")) {
/* 1221 */           reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(Blocks.field_150372_bz, 1)), pt);
/* 1222 */         } else if (pt.name.equals("sandstonestairsBottom")) {
/* 1223 */           reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(Blocks.field_150372_bz, 0)), pt);
/* 1224 */         } else if (pt.name.equals("sandstonestairsLeft")) {
/* 1225 */           reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(Blocks.field_150372_bz, 2)), pt);
/* 1226 */         } else if (pt.name.equals("sandstonestairsRight")) {
/* 1227 */           reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(Blocks.field_150372_bz, 3)), pt);
/*      */ 
/*      */         }
/* 1230 */         else if (pt.name.equals("woodstairsOakInvTop")) {
/* 1231 */           reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(Blocks.field_150476_ad, 5)), pt);
/* 1232 */         } else if (pt.name.equals("woodstairsOakInvBottom")) {
/* 1233 */           reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(Blocks.field_150476_ad, 4)), pt);
/* 1234 */         } else if (pt.name.equals("woodstairsOakInvLeft")) {
/* 1235 */           reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(Blocks.field_150476_ad, 6)), pt);
/* 1236 */         } else if (pt.name.equals("woodstairsOakInvRight")) {
/* 1237 */           reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(Blocks.field_150476_ad, 7)), pt);
/*      */         }
/* 1239 */         else if (pt.name.equals("woodstairsPineInvTop")) {
/* 1240 */           reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(Blocks.field_150485_bF, 5)), pt);
/* 1241 */         } else if (pt.name.equals("woodstairsPineInvBottom")) {
/* 1242 */           reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(Blocks.field_150485_bF, 4)), pt);
/* 1243 */         } else if (pt.name.equals("woodstairsPineInvLeft")) {
/* 1244 */           reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(Blocks.field_150485_bF, 6)), pt);
/* 1245 */         } else if (pt.name.equals("woodstairsPineInvRight")) {
/* 1246 */           reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(Blocks.field_150485_bF, 7)), pt);
/*      */         }
/* 1248 */         else if (pt.name.equals("woodstairsBirchInvTop")) {
/* 1249 */           reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(Blocks.field_150487_bG, 5)), pt);
/* 1250 */         } else if (pt.name.equals("woodstairsBirchInvBottom")) {
/* 1251 */           reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(Blocks.field_150487_bG, 4)), pt);
/* 1252 */         } else if (pt.name.equals("woodstairsBirchInvLeft")) {
/* 1253 */           reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(Blocks.field_150487_bG, 6)), pt);
/* 1254 */         } else if (pt.name.equals("woodstairsBirchInvRight")) {
/* 1255 */           reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(Blocks.field_150487_bG, 7)), pt);
/*      */         }
/* 1257 */         else if (pt.name.equals("woodstairsJungleInvTop")) {
/* 1258 */           reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(Blocks.field_150481_bH, 5)), pt);
/* 1259 */         } else if (pt.name.equals("woodstairsJungleInvBottom")) {
/* 1260 */           reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(Blocks.field_150481_bH, 4)), pt);
/* 1261 */         } else if (pt.name.equals("woodstairsJungleInvLeft")) {
/* 1262 */           reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(Blocks.field_150481_bH, 6)), pt);
/* 1263 */         } else if (pt.name.equals("woodstairsJungleInvRight")) {
/* 1264 */           reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(Blocks.field_150481_bH, 7)), pt);
/*      */         }
/* 1266 */         else if (pt.name.equals("stonestairsInvTop")) {
/* 1267 */           reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(Blocks.field_150446_ar, 5)), pt);
/* 1268 */         } else if (pt.name.equals("stonestairsInvBottom")) {
/* 1269 */           reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(Blocks.field_150446_ar, 4)), pt);
/* 1270 */         } else if (pt.name.equals("stonestairsInvLeft")) {
/* 1271 */           reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(Blocks.field_150446_ar, 6)), pt);
/* 1272 */         } else if (pt.name.equals("stonestairsInvRight")) {
/* 1273 */           reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(Blocks.field_150446_ar, 7)), pt);
/*      */         }
/* 1275 */         else if (pt.name.equals("stonebrickstairsInvTop")) {
/* 1276 */           reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(Blocks.field_150390_bg, 5)), pt);
/* 1277 */         } else if (pt.name.equals("stonebrickstairsInvBottom")) {
/* 1278 */           reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(Blocks.field_150390_bg, 4)), pt);
/* 1279 */         } else if (pt.name.equals("stonebrickstairsInvLeft")) {
/* 1280 */           reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(Blocks.field_150390_bg, 6)), pt);
/* 1281 */         } else if (pt.name.equals("stonebrickstairsInvRight")) {
/* 1282 */           reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(Blocks.field_150390_bg, 7)), pt);
/*      */         }
/* 1284 */         else if (pt.name.equals("brickstairsInvTop")) {
/* 1285 */           reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(Blocks.field_150389_bf, 5)), pt);
/* 1286 */         } else if (pt.name.equals("brickstairsInvBottom")) {
/* 1287 */           reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(Blocks.field_150389_bf, 4)), pt);
/* 1288 */         } else if (pt.name.equals("brickstairsInvLeft")) {
/* 1289 */           reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(Blocks.field_150389_bf, 6)), pt);
/* 1290 */         } else if (pt.name.equals("brickstairsInvRight")) {
/* 1291 */           reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(Blocks.field_150389_bf, 7)), pt);
/*      */         }
/* 1293 */         else if (pt.name.equals("sandstonestairsInvTop")) {
/* 1294 */           reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(Blocks.field_150372_bz, 5)), pt);
/* 1295 */         } else if (pt.name.equals("sandstonestairsInvBottom")) {
/* 1296 */           reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(Blocks.field_150372_bz, 4)), pt);
/* 1297 */         } else if (pt.name.equals("sandstonestairsInvLeft")) {
/* 1298 */           reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(Blocks.field_150372_bz, 6)), pt);
/* 1299 */         } else if (pt.name.equals("sandstonestairsInvRight")) {
/* 1300 */           reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(Blocks.field_150372_bz, 7)), pt);
/*      */         }
/* 1302 */         else if (pt.name.equals("byzantinetiles_bottomtop")) {
/* 1303 */           reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(org.millenaire.common.forge.Mill.byzantine_tiles, 1)), pt);
/* 1304 */         } else if (pt.name.equals("byzantinetiles_leftright")) {
/* 1305 */           reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(org.millenaire.common.forge.Mill.byzantine_tiles, 0)), pt);
/* 1306 */         } else if (pt.name.equals("byzantinestonetiles_bottomtop")) {
/* 1307 */           reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(org.millenaire.common.forge.Mill.byzantine_stone_tiles, 1)), pt);
/* 1308 */         } else if (pt.name.equals("byzantinestonetiles_leftright")) {
/* 1309 */           reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(org.millenaire.common.forge.Mill.byzantine_stone_tiles, 0)), pt);
/*      */         }
/* 1311 */         else if (pt.name.equals("byzantinetileslab_bottomtop")) {
/* 1312 */           reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(org.millenaire.common.forge.Mill.byzantine_tile_slab, 1)), pt);
/* 1313 */         } else if (pt.name.equals("byzantinetileslab_leftright")) {
/* 1314 */           reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(org.millenaire.common.forge.Mill.byzantine_tile_slab, 0)), pt);
/* 1315 */         } else if (pt.name.equals("byzantinetileslab_bottomtop_inv")) {
/* 1316 */           reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(org.millenaire.common.forge.Mill.byzantine_tile_slab, 9)), pt);
/* 1317 */         } else if (pt.name.equals("byzantinetileslab_leftright_inv")) {
/* 1318 */           reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(org.millenaire.common.forge.Mill.byzantine_tile_slab, 8)), pt);
/*      */         }
/* 1320 */         else if (pt.name.equals("signpostTop")) {
/* 1321 */           reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(Blocks.field_150472_an, 5)), pt);
/* 1322 */         } else if (pt.name.equals("signpostBottom")) {
/* 1323 */           reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(Blocks.field_150472_an, 4)), pt);
/* 1324 */         } else if (pt.name.equals("signpostLeft")) {
/* 1325 */           reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(Blocks.field_150472_an, 2)), pt);
/* 1326 */         } else if (pt.name.equals("signpostRight")) {
/* 1327 */           reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(Blocks.field_150472_an, 3)), pt);
/*      */         }
/* 1329 */         else if (pt.name.equals("signwallTop")) {
/* 1330 */           reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(Blocks.field_150444_as, 5)), pt);
/* 1331 */         } else if (pt.name.equals("signwallBottom")) {
/* 1332 */           reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(Blocks.field_150444_as, 4)), pt);
/* 1333 */         } else if (pt.name.equals("signwallLeft")) {
/* 1334 */           reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(Blocks.field_150444_as, 2)), pt);
/* 1335 */         } else if (pt.name.equals("signwallRight")) {
/* 1336 */           reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(Blocks.field_150444_as, 3)), pt);
/*      */         }
/* 1338 */         else if (pt.name.equals("ladderTop")) {
/* 1339 */           reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(Blocks.field_150468_ap, 5)), pt);
/* 1340 */         } else if (pt.name.equals("ladderBottom")) {
/* 1341 */           reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(Blocks.field_150468_ap, 4)), pt);
/* 1342 */         } else if (pt.name.equals("ladderLeft")) {
/* 1343 */           reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(Blocks.field_150468_ap, 2)), pt);
/* 1344 */         } else if (pt.name.equals("ladderRight")) {
/* 1345 */           reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(Blocks.field_150468_ap, 3)), pt);
/*      */         }
/* 1347 */         else if (pt.name.equals("furnace")) {
/* 1348 */           for (int i = 0; i < 16; i++) {
/* 1349 */             reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(Blocks.field_150460_al, i)), pt);
/* 1350 */             reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(Blocks.field_150470_am, i)), pt);
/*      */           }
/* 1352 */         } else if (pt.name.equals("brewingstand")) {
/* 1353 */           for (int i = 0; i < 16; i++) {
/* 1354 */             reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(Blocks.field_150382_bo, i)), pt);
/*      */           }
/* 1356 */         } else if (pt.name.equals("doorTop")) {
/* 1357 */           reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(Blocks.field_150466_ao, 0)), pt);
/* 1358 */           reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(Blocks.field_150466_ao, 7)), pt);
/* 1359 */         } else if (pt.name.equals("doorBottom")) {
/* 1360 */           reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(Blocks.field_150466_ao, 2)), pt);
/* 1361 */           reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(Blocks.field_150466_ao, 5)), pt);
/* 1362 */         } else if (pt.name.equals("doorLeft")) {
/* 1363 */           reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(Blocks.field_150466_ao, 3)), pt);
/* 1364 */           reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(Blocks.field_150466_ao, 6)), pt);
/* 1365 */         } else if (pt.name.equals("doorRight")) {
/* 1366 */           reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(Blocks.field_150466_ao, 1)), pt);
/* 1367 */           reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(Blocks.field_150466_ao, 4)), pt);
/*      */         }
/* 1369 */         else if (pt.name.equals("irondoorTop")) {
/* 1370 */           reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(Blocks.field_150454_av, 0)), pt);
/* 1371 */           reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(Blocks.field_150454_av, 7)), pt);
/* 1372 */         } else if (pt.name.equals("irondoorBottom")) {
/* 1373 */           reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(Blocks.field_150454_av, 2)), pt);
/* 1374 */           reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(Blocks.field_150454_av, 5)), pt);
/* 1375 */         } else if (pt.name.equals("irondoorLeft")) {
/* 1376 */           reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(Blocks.field_150454_av, 3)), pt);
/* 1377 */           reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(Blocks.field_150454_av, 6)), pt);
/* 1378 */         } else if (pt.name.equals("irondoorRight")) {
/* 1379 */           reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(Blocks.field_150454_av, 1)), pt);
/* 1380 */           reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(Blocks.field_150454_av, 4)), pt);
/*      */         }
/* 1382 */         else if (pt.name.equals("trapdoorTop")) {
/* 1383 */           reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(Blocks.field_150415_aT, 1)), pt);
/* 1384 */         } else if (pt.name.equals("trapdoorBottom")) {
/* 1385 */           reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(Blocks.field_150415_aT, 0)), pt);
/* 1386 */         } else if (pt.name.equals("trapdoorLeft")) {
/* 1387 */           reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(Blocks.field_150415_aT, 3)), pt);
/* 1388 */         } else if (pt.name.equals("trapdoorRight")) {
/* 1389 */           reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(Blocks.field_150415_aT, 2)), pt);
/*      */         }
/* 1391 */         else if (pt.name.equals("fencegateHorizontal")) {
/* 1392 */           reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(Blocks.field_150396_be, 1)), pt);
/* 1393 */           reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(Blocks.field_150396_be, 3)), pt);
/* 1394 */           reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(Blocks.field_150396_be, 5)), pt);
/* 1395 */           reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(Blocks.field_150396_be, 7)), pt);
/* 1396 */         } else if (pt.name.equals("fencegateVertical")) {
/* 1397 */           reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(Blocks.field_150396_be, 0)), pt);
/* 1398 */           reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(Blocks.field_150396_be, 2)), pt);
/* 1399 */           reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(Blocks.field_150396_be, 4)), pt);
/* 1400 */           reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(Blocks.field_150396_be, 6)), pt);
/*      */         }
/* 1402 */         else if (pt.name.equals("bedTop")) {
/* 1403 */           reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(Blocks.field_150324_C, 8)), pt);
/* 1404 */         } else if (pt.name.equals("bedBottom")) {
/* 1405 */           reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(Blocks.field_150324_C, 10)), pt);
/* 1406 */         } else if (pt.name.equals("bedLeft")) {
/* 1407 */           reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(Blocks.field_150324_C, 9)), pt);
/* 1408 */         } else if (pt.name.equals("bedRight")) {
/* 1409 */           reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(Blocks.field_150324_C, 11)), pt);
/*      */         }
/* 1411 */         else if (pt.name.equals("plainSignGuess")) {
/* 1412 */           for (int i = 0; i < 16; i++)
/* 1413 */             reverseColourPoints.put(Integer.valueOf(org.millenaire.common.core.MillCommonUtilities.getPointHash(Blocks.field_150444_as, i)), pt);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public int length;
/*      */   public int width;
/*      */   public int nbfloors;
/*      */   public int lengthOffset;
/*      */   public int widthOffset;
/*      */   public int buildingOrientation;
/*      */   public String nativeName;
/*      */   public String shop;
/* 1427 */   public final java.util.HashMap<String, String> names = new java.util.HashMap();
/* 1428 */   public java.util.List<String> maleResident = new java.util.ArrayList();
/* 1429 */   public java.util.List<String> femaleResident = new java.util.ArrayList();
/* 1430 */   public boolean isUpdate = false;
/*      */   public int level;
/*      */   public int max;
/* 1433 */   public int priority; public int reputation; public int price; public int priorityMoveIn; public boolean isgift = false;
/*      */   public float minDistance;
/* 1435 */   public float maxDistance; public String requiredTag = null;
/* 1436 */   public int nbBlocksToPut = 0;
/* 1437 */   public PointType[][][] plan = (PointType[][][])null;
/* 1438 */   public String planName = ""; public String buildingKey; public String type = null;
/*      */   
/*      */   public java.util.HashMap<org.millenaire.common.InvItem, Integer> resCost;
/*      */   
/*      */   public int[] signOrder;
/*      */   
/*      */   public java.util.List<String> tags;
/*      */   
/*      */   public int variation;
/*      */   
/*      */   public java.util.List<String> subBuildings;
/*      */   public java.util.List<String> startingSubBuildings;
/* 1450 */   public boolean showTownHallSigns = true;
/*      */   public java.util.List<StartingGood> startingGoods;
/* 1452 */   public String exploreTag = null;
/* 1453 */   public int irrigation = 0;
/*      */   
/*      */   public org.millenaire.common.Culture culture;
/* 1456 */   public int pathLevel = 0; public int pathWidth = 2;
/* 1457 */   public boolean rebuildPath = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public BuildingPlan parent;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public BuildingPlan(java.io.File dir, String buildingKey, int level, int variation, BuildingPlan parent, org.millenaire.common.Culture c, boolean importPlan)
/*      */     throws Exception
/*      */   {
/* 1475 */     char varChar = 'A';
/* 1476 */     varChar = (char)(varChar + variation);
/* 1477 */     this.planName = (buildingKey + "_" + varChar + "" + level);
/* 1478 */     this.buildingKey = buildingKey;
/* 1479 */     this.isUpdate = (level > 0);
/* 1480 */     this.level = level;
/* 1481 */     this.variation = variation;
/* 1482 */     this.culture = c;
/*      */     
/* 1484 */     java.io.File file = new java.io.File(dir, this.planName + ".txt");
/*      */     
/* 1486 */     initialiseConfig(parent);
/*      */     
/* 1488 */     java.io.BufferedReader reader = org.millenaire.common.core.MillCommonUtilities.getReader(file);
/*      */     
/* 1490 */     String line = null;
/*      */     
/* 1492 */     line = reader.readLine();
/*      */     
/* 1494 */     readConfigLine(file, line, importPlan);
/*      */     
/* 1496 */     line = reader.readLine();
/*      */     
/* 1498 */     java.util.List<java.util.List<String>> textPlan = new java.util.ArrayList();
/*      */     
/* 1500 */     java.util.List<String> v = new java.util.ArrayList();
/* 1501 */     textPlan.add(v);
/*      */     
/* 1503 */     while ((line = reader.readLine()) != null) {
/* 1504 */       if (line.trim().equals("")) {
/* 1505 */         v = new java.util.ArrayList();
/* 1506 */         textPlan.add(v);
/*      */       } else {
/* 1508 */         v.add(line);
/*      */       }
/*      */     }
/*      */     
/* 1512 */     this.length = ((java.util.List)textPlan.get(0)).size();
/* 1513 */     this.lengthOffset = ((int)Math.floor(this.length * 0.5D));
/* 1514 */     this.width = ((String)((java.util.List)textPlan.get(0)).get(0)).length();
/* 1515 */     this.widthOffset = ((int)Math.floor(this.width * 0.5D));
/* 1516 */     this.nbfloors = textPlan.size();
/*      */     
/* 1518 */     int i = 0;
/* 1519 */     for (java.util.List<String> floor : textPlan) {
/* 1520 */       if (floor.size() != this.length) {
/* 1521 */         throw new org.millenaire.common.MLN.MillenaireException(this.planName + ": " + "Floor " + i + " is " + floor.size() + " long, " + this.length + " expected.");
/*      */       }
/*      */       
/* 1524 */       int j = 0;
/* 1525 */       for (String s : floor)
/*      */       {
/* 1527 */         if (s.length() != this.width) {
/* 1528 */           throw new org.millenaire.common.MLN.MillenaireException(this.planName + ": " + "Line " + j + " in floor " + i + " is " + s.length() + " wide, " + this.width + " expected.");
/*      */         }
/*      */         
/* 1531 */         j++;
/*      */       }
/* 1533 */       i++;
/*      */     }
/*      */     
/* 1536 */     reader.close();
/*      */     
/* 1538 */     this.plan = new PointType[this.nbfloors][this.length][this.width];
/*      */     
/* 1540 */     for (i = 0; i < textPlan.size(); i++) {
/* 1541 */       for (int j = 0; j < this.length; j++) {
/* 1542 */         for (int k = 0; k < this.width; k++) {
/* 1543 */           if (!charPoints.containsKey(Character.valueOf(((String)((java.util.List)textPlan.get(i)).get(j)).charAt(this.width - k - 1)))) {
/* 1544 */             throw new org.millenaire.common.MLN.MillenaireException(this.planName + ": In floor " + i + " line " + j + " char " + (this.width - k - 1) + " unknow character: " + ((String)((java.util.List)textPlan.get(i)).get(j)).charAt(this.width - k - 1));
/*      */           }
/* 1546 */           this.plan[i][j][k] = ((PointType)charPoints.get(Character.valueOf(((String)((java.util.List)textPlan.get(i)).get(j)).charAt(this.width - k - 1))));
/* 1547 */           if (this.plan[i][j][k] == null) {
/* 1548 */             throw new org.millenaire.common.MLN.MillenaireException(this.planName + ": In floor " + i + " line " + j + " char " + (this.width - k - 1) + " null PointType for: " + ((String)((java.util.List)textPlan.get(i)).get(j)).charAt(this.width - k - 1));
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 1554 */     computeCost();
/*      */     
/* 1556 */     if (org.millenaire.common.MLN.LogBuildingPlan >= 1) {
/* 1557 */       org.millenaire.common.MLN.major(this, "Loaded plan " + buildingKey + "_" + level + ": " + this.nativeName + " pop: " + this.maleResident + "/" + this.femaleResident + " / priority: " + this.priority);
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
/*      */   public BuildingPlan(java.io.File dir, String buildingKey, int level, int variation, BuildingPlan parent, String configLine, org.millenaire.common.Culture c, boolean importPlan)
/*      */     throws Exception
/*      */   {
/* 1577 */     char varChar = 'A';
/* 1578 */     varChar = (char)(varChar + variation);
/* 1579 */     this.planName = (buildingKey + "_" + varChar + "" + level);
/* 1580 */     this.buildingKey = buildingKey;
/* 1581 */     this.isUpdate = (level > 0);
/* 1582 */     this.level = level;
/* 1583 */     this.variation = variation;
/* 1584 */     this.culture = c;
/*      */     
/* 1586 */     initialiseConfig(parent);
/*      */     
/* 1588 */     java.io.File file = new java.io.File(dir, this.planName + ".png");
/*      */     
/* 1590 */     if (configLine != null) {
/* 1591 */       readConfigLine(file, configLine, importPlan);
/*      */     }
/*      */     
/* 1594 */     java.awt.image.BufferedImage PNGFile = javax.imageio.ImageIO.read(file);
/*      */     
/* 1596 */     java.awt.image.BufferedImage pictPlan = new java.awt.image.BufferedImage(PNGFile.getWidth(), PNGFile.getHeight(), 6);
/* 1597 */     java.awt.Graphics2D fig = pictPlan.createGraphics();
/* 1598 */     fig.drawImage(PNGFile, 0, 0, null);
/* 1599 */     fig.dispose();
/* 1600 */     pictPlan.flush();
/*      */     
/* 1602 */     this.lengthOffset = ((int)Math.floor(this.length * 0.5D));
/* 1603 */     this.widthOffset = ((int)Math.floor(this.width * 0.5D));
/*      */     
/* 1605 */     if (pictPlan.getHeight() != this.length) {
/* 1606 */       throw new org.millenaire.common.MLN.MillenaireException(this.planName + ": " + "Expected length is " + this.length + " but file height is " + pictPlan.getHeight());
/*      */     }
/*      */     
/* 1609 */     float fnbfloors = (pictPlan.getWidth() + 1.0F) / (this.width + 1.0F);
/*      */     
/* 1611 */     if (Math.round(fnbfloors) != fnbfloors) {
/* 1612 */       throw new org.millenaire.common.MLN.MillenaireException(this.planName + ": With a width of " + this.width + ", getting non-integer floor number: " + fnbfloors);
/*      */     }
/*      */     
/* 1615 */     this.nbfloors = ((int)fnbfloors);
/*      */     
/* 1617 */     this.plan = new PointType[this.nbfloors][this.length][this.width];
/*      */     
/* 1619 */     if ((pictPlan.getType() != 5) && (pictPlan.getType() != 6)) {
/* 1620 */       org.millenaire.common.MLN.error(this, "Picture " + this.planName + ".png could not be loaded as type TYPE_3BYTE_BGR or TYPE_4BYTE_ABGR but instead as: " + pictPlan.getType());
/*      */     }
/*      */     
/* 1623 */     boolean alphaLayer = false;
/*      */     
/* 1625 */     if (pictPlan.getType() == 6) {
/* 1626 */       alphaLayer = true;
/*      */     }
/*      */     
/* 1629 */     boolean sleepingPos = false;
/*      */     
/* 1631 */     for (int i = 0; i < this.nbfloors; i++) {
/* 1632 */       for (int j = 0; j < this.length; j++) {
/* 1633 */         for (int k = 0; k < this.width; k++)
/*      */         {
/* 1635 */           int colour = pictPlan.getRGB(i * this.width + i + this.width - k - 1, j);
/*      */           
/* 1637 */           if (alphaLayer) {
/* 1638 */             if ((colour & 0xFF000000) != -16777216)
/*      */             {
/*      */ 
/*      */ 
/* 1642 */               colour = 16777215;
/*      */             } else {
/* 1644 */               colour &= 0xFFFFFF;
/*      */             }
/*      */           } else {
/* 1647 */             colour &= 0xFFFFFF;
/*      */           }
/*      */           
/* 1650 */           if (!colourPoints.containsKey(Integer.valueOf(colour))) {
/* 1651 */             org.millenaire.common.MLN.error(this, this.planName + ": Unknown colour " + getColourString(colour) + " at: " + (i * this.width + i + this.width - k - 1) + "/" + j + ", skipping it.");
/* 1652 */             colour = 16777215;
/*      */           }
/*      */           
/* 1655 */           this.plan[i][j][k] = ((PointType)colourPoints.get(Integer.valueOf(colour)));
/*      */           
/* 1657 */           if ("sleepingPos".equals(this.plan[i][j][k].name)) {
/* 1658 */             sleepingPos = true;
/*      */           }
/*      */           
/* 1661 */           if ((this.plan[i][j][k].name != null) && (this.plan[i][j][k].name.equals("mainchest")) && (level > 0)) {
/* 1662 */             org.millenaire.common.MLN.error(this, "Main chest detected at " + (i * this.width + i + this.width - k - 1) + "/" + j + " but we are in an upgrade. Removing it.");
/* 1663 */             this.plan[i][j][k] = ((PointType)colourPoints.get(Integer.valueOf(16777215)));
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 1669 */     computeCost();
/*      */     
/* 1671 */     if (org.millenaire.common.MLN.LogBuildingPlan >= 1) {
/* 1672 */       org.millenaire.common.MLN.major(this, "Loaded plan " + buildingKey + "_" + level + ": " + this.nativeName + " pop: " + this.maleResident + "/" + this.femaleResident + "/priority:" + this.priority);
/*      */     }
/*      */     
/* 1675 */     if ((this.maleResident.size() > 0) && (this.femaleResident.size() > 0) && (level == 0) && (!sleepingPos)) {
/* 1676 */       org.millenaire.common.MLN.error(this, "Has residents but the sleeping pos is missing!");
/*      */     }
/*      */     
/* 1679 */     int pigs = 0;int sheep = 0;int chicken = 0;int cow = 0;
/*      */     
/* 1681 */     for (int i = 0; i < this.nbfloors; i++) {
/* 1682 */       for (int j = 0; j < this.length; j++) {
/* 1683 */         for (int k = 0; k < this.width; k++) {
/* 1684 */           if ((this.plan[i][j][k].isType("chickenspawn")) && (!this.tags.contains("chicken"))) {
/* 1685 */             org.millenaire.common.MLN.warning(this, "Building has chicken spawn but no chicken tag.");
/* 1686 */           } else if ((this.plan[i][j][k].isType("cowspawn")) && (!this.tags.contains("cattle"))) {
/* 1687 */             org.millenaire.common.MLN.warning(this, "Building has cattle spawn but no cattle tag.");
/* 1688 */           } else if ((this.plan[i][j][k].isType("sheepspawn")) && (!this.tags.contains("sheeps"))) {
/* 1689 */             org.millenaire.common.MLN.warning(this, "Building has sheeps spawn but no sheeps tag.");
/* 1690 */           } else if ((this.plan[i][j][k].isType("pigspawn")) && (!this.tags.contains("pigs"))) {
/* 1691 */             org.millenaire.common.MLN.warning(this, "Building has pig spawn but no pig tag.");
/* 1692 */           } else if ((this.plan[i][j][k].isType("squidspawn")) && (!this.tags.contains("squids"))) {
/* 1693 */             org.millenaire.common.MLN.warning(this, "Building has squid spawn but no squid tag.");
/*      */           }
/* 1695 */           if (this.plan[i][j][k].isType("chickenspawn")) {
/* 1696 */             chicken++;
/* 1697 */           } else if (this.plan[i][j][k].isType("cowspawn")) {
/* 1698 */             cow++;
/* 1699 */           } else if (this.plan[i][j][k].isType("sheepspawn")) {
/* 1700 */             sheep++;
/* 1701 */           } else if (this.plan[i][j][k].isType("pigspawn")) {
/* 1702 */             pigs++;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 1708 */     if (chicken % 2 == 1) {
/* 1709 */       org.millenaire.common.MLN.warning(this, "Odd number of chicken spawn: " + chicken);
/*      */     }
/* 1711 */     if (sheep % 2 == 1) {
/* 1712 */       org.millenaire.common.MLN.warning(this, "Odd number of sheep spawn: " + sheep);
/*      */     }
/* 1714 */     if (cow % 2 == 1) {
/* 1715 */       org.millenaire.common.MLN.warning(this, "Odd number of cow spawn: " + cow);
/*      */     }
/* 1717 */     if (pigs % 2 == 1) {
/* 1718 */       org.millenaire.common.MLN.warning(this, "Odd number of pigs spawn: " + pigs);
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
/*      */   public BuildingPlan(String buildingKey, int level, int variation, org.millenaire.common.Culture c)
/*      */   {
/* 1732 */     this.buildingKey = buildingKey;
/* 1733 */     this.isUpdate = (level > 0);
/* 1734 */     this.level = level;
/* 1735 */     this.variation = variation;
/* 1736 */     this.culture = c;
/*      */   }
/*      */   
/*      */   private void addToCost(net.minecraft.block.Block block, int nb) {
/* 1740 */     addToCost(block, 0, nb);
/*      */   }
/*      */   
/*      */   private void addToCost(net.minecraft.block.Block block, int meta, int nb) {
/*      */     try {
/* 1745 */       org.millenaire.common.InvItem key = new org.millenaire.common.InvItem(block, meta);
/*      */       
/* 1747 */       if (this.resCost.containsKey(key)) {
/* 1748 */         nb += ((Integer)this.resCost.get(key)).intValue();
/* 1749 */         this.resCost.put(key, Integer.valueOf(nb));
/*      */       } else {
/* 1751 */         this.resCost.put(key, Integer.valueOf(nb));
/*      */       }
/*      */     } catch (Exception e) {
/* 1754 */       org.millenaire.common.MLN.printException("Exception when calculating cost of: " + this, e);
/*      */     }
/*      */   }
/*      */   
/*      */   private void addToCost(net.minecraft.item.Item item, int nb) {
/* 1759 */     addToCost(item, 0, nb);
/*      */   }
/*      */   
/*      */   private void addToCost(net.minecraft.item.Item item, int meta, int nb) {
/*      */     try {
/* 1764 */       org.millenaire.common.InvItem key = new org.millenaire.common.InvItem(item, meta);
/*      */       
/* 1766 */       if (this.resCost.containsKey(key)) {
/* 1767 */         nb += ((Integer)this.resCost.get(key)).intValue();
/* 1768 */         this.resCost.put(key, Integer.valueOf(nb));
/*      */       } else {
/* 1770 */         this.resCost.put(key, Integer.valueOf(nb));
/*      */       }
/*      */     } catch (Exception e) {
/* 1773 */       org.millenaire.common.MLN.printException("Exception when calculating cost of: " + this, e);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public java.util.List<LocationBuildingPair> build(org.millenaire.common.MillWorld mw, org.millenaire.common.VillageType villageType, BuildingLocation location, boolean villageGeneration, boolean townHall, org.millenaire.common.Point townHallPos, boolean wandimport, net.minecraft.entity.player.EntityPlayer owner, boolean rushBuilding)
/*      */   {
/* 1780 */     if ((!townHall) && (townHallPos == null) && (!wandimport)) {
/* 1781 */       org.millenaire.common.MLN.error(this, "Building is not TH and does not have TH's position.");
/*      */     }
/*      */     
/* 1784 */     net.minecraft.world.World worldObj = mw.world;
/*      */     
/* 1786 */     java.util.List<LocationBuildingPair> buildings = new java.util.ArrayList();
/*      */     
/* 1788 */     BuildingBlock[] bblocks = getBuildingPoints(worldObj, location, villageGeneration);
/*      */     
/* 1790 */     for (BuildingBlock bblock : bblocks) {
/* 1791 */       bblock.build(worldObj, villageGeneration, wandimport);
/*      */     }
/*      */     
/* 1794 */     if (this.tags.contains("hof"))
/*      */     {
/* 1796 */       int signNb = 0;
/* 1797 */       java.util.List<String> hofData = org.millenaire.common.MLN.getHoFData();
/*      */       
/* 1799 */       for (int z = location.pos.getiZ() - this.width / 2; z < location.pos.getiZ() + this.width / 2; z++) {
/* 1800 */         for (int x = location.pos.getiX() + this.length / 2; x >= location.pos.getiX() - this.length / 2; x--)
/*      */         {
/* 1802 */           for (int y = location.pos.getiY() + this.plan.length; y >= location.pos.getiY(); y--) {
/* 1803 */             if (worldObj.func_147439_a(x, y, z) == Blocks.field_150444_as) {
/* 1804 */               net.minecraft.tileentity.TileEntitySign sign = new org.millenaire.common.Point(x, y, z).getSign(worldObj);
/* 1805 */               if (sign != null) {
/* 1806 */                 if (signNb < hofData.size()) {
/* 1807 */                   String[] lines = ((String)hofData.get(signNb)).split(";");
/* 1808 */                   for (int i = 0; i < Math.min(4, lines.length); i++)
/*      */                   {
/*      */ 
/* 1811 */                     if ((i == 0) || (lines[i].length() == 0)) {
/* 1812 */                       sign.field_145915_a[i] = lines[i];
/*      */                     } else {
/* 1814 */                       sign.field_145915_a[i] = org.millenaire.common.MLN.string(lines[i]);
/*      */                     }
/*      */                   }
/*      */                 }
/*      */                 
/* 1819 */                 signNb++;
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 1827 */     if ((bblocks.length > 0) && (!wandimport) && (location.level == 0))
/*      */     {
/* 1829 */       Building building = new Building(mw, this.culture, villageType, location, townHall, villageGeneration, bblocks[(bblocks.length - 1)].p, townHallPos);
/*      */       
/* 1831 */       if (org.millenaire.common.MLN.LogWorldGeneration >= 2) {
/* 1832 */         org.millenaire.common.MLN.minor(this, "Building " + this.planName + " at " + location);
/*      */       }
/*      */       
/* 1835 */       referenceBuildingPoints(worldObj, building, location);
/* 1836 */       building.initialise(owner, (villageGeneration) || (rushBuilding));
/*      */       
/* 1838 */       building.fillStartingGoods();
/*      */       
/* 1840 */       buildings.add(new LocationBuildingPair(building, location));
/*      */       
/*      */ 
/* 1843 */       if (townHall) {
/* 1844 */         townHallPos = building.getPos();
/*      */       }
/*      */     }
/*      */     
/* 1848 */     if (this.culture != null)
/*      */     {
/* 1850 */       for (String sb : this.startingSubBuildings) {
/* 1851 */         BuildingPlan plan = this.culture.getBuildingPlanSet(sb).getRandomStartingPlan();
/* 1852 */         BuildingLocation l = location.createLocationForStartingSubBuilding(sb);
/* 1853 */         java.util.List<LocationBuildingPair> vb = plan.build(mw, villageType, l, villageGeneration, false, townHallPos, false, owner, rushBuilding);
/* 1854 */         location.subBuildings.add(sb);
/*      */         
/* 1856 */         for (LocationBuildingPair p : vb) {
/* 1857 */           buildings.add(p);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 1862 */     worldObj.func_147458_c(location.pos.getiX() - this.length / 2 - 5, location.pos.getiY() - this.plan.length - 5, location.pos.getiZ() - this.width / 2 - 5, location.pos.getiX() + this.length / 2 + 5, location.pos.getiY() - 5, location.pos.getiZ() + this.width / 2 + 5);
/*      */     
/*      */ 
/* 1865 */     return buildings;
/*      */   }
/*      */   
/*      */   private void computeCost()
/*      */     throws org.millenaire.common.MLN.MillenaireException
/*      */   {
/* 1871 */     this.resCost = new java.util.HashMap();
/*      */     
/* 1873 */     int plankCost = 0;int plankOakCost = 0;int plankPineCost = 0;int plankBirchCost = 0;int plankJungleCost = 0;int glassPaneCost = 0;int byzBricksHalf = 0;
/*      */     
/* 1875 */     for (int i = 0; i < this.nbfloors; i++) {
/* 1876 */       for (int j = 0; j < this.length; j++) {
/* 1877 */         for (int k = 0; k < this.width; k++) {
/* 1878 */           PointType p = this.plan[i][j][k];
/*      */           
/* 1880 */           if (p == null) {
/* 1881 */             throw new org.millenaire.common.MLN.MillenaireException("PointType null at " + i + "/" + j + "/" + k);
/*      */           }
/*      */           
/* 1884 */           if ((p.block == Blocks.field_150364_r) && ((p.meta & 0x3) == 0)) {
/* 1885 */             plankOakCost += 4;
/* 1886 */           } else if ((p.block == Blocks.field_150364_r) && ((p.meta & 0x3) == 1)) {
/* 1887 */             plankPineCost += 4;
/* 1888 */           } else if ((p.block == Blocks.field_150364_r) && ((p.meta & 0x3) == 2)) {
/* 1889 */             plankBirchCost += 4;
/* 1890 */           } else if ((p.block == Blocks.field_150364_r) && ((p.meta & 0x3) == 3)) {
/* 1891 */             plankJungleCost += 4;
/* 1892 */           } else if ((p.block == Blocks.field_150344_f) && (p.meta == 0)) {
/* 1893 */             plankOakCost++;
/* 1894 */           } else if ((p.block == Blocks.field_150344_f) && (p.meta == 1)) {
/* 1895 */             plankPineCost++;
/* 1896 */           } else if ((p.block == Blocks.field_150344_f) && (p.meta == 2)) {
/* 1897 */             plankBirchCost++;
/* 1898 */           } else if ((p.block == Blocks.field_150344_f) && (p.meta == 3)) {
/* 1899 */             plankJungleCost++;
/*      */           }
/* 1901 */           else if (p.block == org.millenaire.common.forge.Mill.byzantine_tiles) {
/* 1902 */             byzBricksHalf += 2;
/* 1903 */           } else if (p.block == org.millenaire.common.forge.Mill.byzantine_tile_slab) {
/* 1904 */             byzBricksHalf++;
/* 1905 */           } else if (p.block == org.millenaire.common.forge.Mill.byzantine_stone_tiles) {
/* 1906 */             byzBricksHalf++;
/* 1907 */             addToCost(Blocks.field_150348_b, 1);
/*      */           }
/* 1909 */           else if (p.block == Blocks.field_150410_aZ) {
/* 1910 */             glassPaneCost++;
/* 1911 */           } else if (p.block == Blocks.field_150462_ai) {
/* 1912 */             plankCost += 4;
/* 1913 */           } else if (p.block == Blocks.field_150486_ae) {
/* 1914 */             plankCost += 8;
/* 1915 */           } else if (p.block == Blocks.field_150460_al) {
/* 1916 */             addToCost(Blocks.field_150347_e, 8);
/* 1917 */           } else if (p.block == Blocks.field_150478_aa) {
/* 1918 */             plankCost++;
/* 1919 */           } else if (p.block == Blocks.field_150422_aJ) {
/* 1920 */             plankCost++;
/* 1921 */           } else if (p.block == Blocks.field_150396_be) {
/* 1922 */             plankCost += 4;
/* 1923 */           } else if (p.block == Blocks.field_150452_aw) {
/* 1924 */             plankCost += 2;
/* 1925 */           } else if (p.block == Blocks.field_150456_au) {
/* 1926 */             addToCost(Blocks.field_150348_b, 2);
/* 1927 */           } else if ((p.block == Blocks.field_150417_aV) && (p.meta == 0))
/*      */           {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1935 */             addToCost(Blocks.field_150348_b, 1);
/* 1936 */           } else if ((p.block == Blocks.field_150333_U) && ((p.meta & 0x7) == 0)) {
/* 1937 */             addToCost(Blocks.field_150348_b, 1);
/* 1938 */           } else if ((p.block == Blocks.field_150333_U) && ((p.meta & 0x7) == 1)) {
/* 1939 */             addToCost(Blocks.field_150322_A, 1);
/* 1940 */           } else if ((p.block == Blocks.field_150333_U) && ((p.meta & 0x7) == 2)) {
/* 1941 */             plankCost++;
/* 1942 */           } else if ((p.block == Blocks.field_150333_U) && ((p.meta & 0x7) == 3)) {
/* 1943 */             addToCost(Blocks.field_150347_e, 1);
/* 1944 */           } else if ((p.block == Blocks.field_150333_U) && ((p.meta & 0x7) == 4)) {
/* 1945 */             addToCost(Blocks.field_150336_V, 1);
/* 1946 */           } else if ((p.block == Blocks.field_150333_U) && ((p.meta & 0x7) == 5)) {
/* 1947 */             addToCost(Blocks.field_150348_b, 1);
/*      */           }
/* 1949 */           else if ((p.block == Blocks.field_150376_bx) && ((p.meta & 0x7) == 0)) {
/* 1950 */             plankOakCost++;
/* 1951 */           } else if ((p.block == Blocks.field_150376_bx) && ((p.meta & 0x7) == 1)) {
/* 1952 */             plankPineCost++;
/* 1953 */           } else if ((p.block == Blocks.field_150376_bx) && ((p.meta & 0x7) == 2)) {
/* 1954 */             plankBirchCost++;
/* 1955 */           } else if ((p.block == Blocks.field_150376_bx) && ((p.meta & 0x7) == 3)) {
/* 1956 */             plankJungleCost++;
/*      */           }
/* 1958 */           else if (p.block == Blocks.field_150325_L) {
/* 1959 */             addToCost(Blocks.field_150325_L, 1);
/*      */           }
/* 1961 */           else if (p.block == Blocks.field_150388_bm) {
/* 1962 */             addToCost(net.minecraft.init.Items.field_151075_bm, 1);
/*      */           }
/* 1964 */           else if (p.block == Blocks.field_150334_T) {
/* 1965 */             addToCost(Blocks.field_150348_b, 1);
/* 1966 */           } else if (p.block == Blocks.field_150339_S) {
/* 1967 */             addToCost(net.minecraft.init.Items.field_151042_j, 9);
/* 1968 */           } else if (p.block == Blocks.field_150411_aY) {
/* 1969 */             addToCost(net.minecraft.init.Items.field_151042_j, 1);
/* 1970 */           } else if (p.block == Blocks.field_150340_R) {
/* 1971 */             addToCost(net.minecraft.init.Items.field_151043_k, 9);
/* 1972 */           } else if (p.block == Blocks.field_150383_bp) {
/* 1973 */             addToCost(net.minecraft.init.Items.field_151042_j, 7);
/* 1974 */           } else if (p.block == Blocks.field_150463_bK) {
/* 1975 */             addToCost(Blocks.field_150347_e, 1);
/* 1976 */           } else if (p.isType("lockedchest")) {
/* 1977 */             plankCost += 8;
/* 1978 */           } else if (p.isType("furnace")) {
/* 1979 */             addToCost(Blocks.field_150347_e, 8);
/* 1980 */           } else if (p.isType("mainchest")) {
/* 1981 */             plankCost += 8;
/* 1982 */           } else if (p.isSubType("woodstairsOak")) {
/* 1983 */             plankOakCost += 2;
/* 1984 */           } else if (p.isSubType("woodstairsPine")) {
/* 1985 */             plankPineCost += 2;
/* 1986 */           } else if (p.isSubType("woodstairsBirch")) {
/* 1987 */             plankBirchCost += 2;
/* 1988 */           } else if (p.isSubType("woodstairsJungle")) {
/* 1989 */             plankJungleCost += 2;
/* 1990 */           } else if (p.isSubType("stonestair")) {
/* 1991 */             addToCost(Blocks.field_150347_e, 2);
/* 1992 */           } else if (p.isSubType("stonebrickstairs")) {
/* 1993 */             addToCost(Blocks.field_150348_b, 2);
/* 1994 */           } else if (p.isSubType("standstonestairs")) {
/* 1995 */             addToCost(Blocks.field_150322_A, 2);
/* 1996 */           } else if (p.isSubType("brickstairs")) {
/* 1997 */             addToCost(Blocks.field_150336_V, 2);
/* 1998 */           } else if (p.isSubType("ladder")) {
/* 1999 */             plankCost += 2;
/* 2000 */           } else if (p.isSubType("sign")) {
/* 2001 */             plankCost += 7;
/* 2002 */           } else if (p.isSubType("door")) {
/* 2003 */             plankCost += 6;
/* 2004 */           } else if (p.isSubType("trapdoor")) {
/* 2005 */             plankCost += 6;
/* 2006 */           } else if (p.isSubType("bed")) {
/* 2007 */             plankCost += 3;
/* 2008 */             addToCost(Blocks.field_150325_L, 0, 3);
/* 2009 */           } else if (p.isType("tapestry")) {
/* 2010 */             addToCost(org.millenaire.common.forge.Mill.tapestry, 1);
/* 2011 */           } else if (p.isType("indianstatue")) {
/* 2012 */             addToCost(org.millenaire.common.forge.Mill.indianstatue, 1);
/* 2013 */           } else if (p.isType("mayanstatue")) {
/* 2014 */             addToCost(org.millenaire.common.forge.Mill.mayanstatue, 1);
/* 2015 */           } else if (p.isType("byzantineiconsmall")) {
/* 2016 */             addToCost(org.millenaire.common.forge.Mill.byzantineiconsmall, 1);
/* 2017 */           } else if (p.isType("byzantineiconmedium")) {
/* 2018 */             addToCost(org.millenaire.common.forge.Mill.byzantineiconmedium, 1);
/* 2019 */           } else if (p.isType("byzantineiconmedium")) {
/* 2020 */             addToCost(org.millenaire.common.forge.Mill.byzantineiconmedium, 1);
/* 2021 */           } else if (p.isType("silkwormblock")) {
/* 2022 */             plankCost += 4;
/*      */           }
/* 2024 */           else if (p.isType("byzantinetiles_bottomtop")) {
/* 2025 */             byzBricksHalf += 2;
/* 2026 */           } else if (p.isType("byzantinetiles_leftright")) {
/* 2027 */             byzBricksHalf += 2;
/*      */           }
/* 2029 */           else if (p.isType("byzantinestonetiles_bottomtop")) {
/* 2030 */             byzBricksHalf++;
/* 2031 */             addToCost(Blocks.field_150348_b, 1);
/* 2032 */           } else if (p.isType("byzantinestonetiles_leftright")) {
/* 2033 */             byzBricksHalf++;
/* 2034 */             addToCost(Blocks.field_150348_b, 1);
/*      */           }
/* 2036 */           else if (p.isType("byzantinetileslab_bottomtop")) {
/* 2037 */             byzBricksHalf++;
/* 2038 */           } else if (p.isType("byzantinetileslab_leftright")) {
/* 2039 */             byzBricksHalf++;
/* 2040 */           } else if (p.isType("byzantinetileslab_bottomtop_inv")) {
/* 2041 */             byzBricksHalf++;
/* 2042 */           } else if (p.isType("byzantinetileslab_leftright_inv")) {
/* 2043 */             byzBricksHalf++;
/*      */           }
/* 2045 */           else if ((p.block != null) && (p.block != Blocks.field_150350_a) && (!org.millenaire.common.item.Goods.freeGoods.contains(new org.millenaire.common.InvItem(p.block, p.meta))) && (!org.millenaire.common.item.Goods.freeGoods.contains(new org.millenaire.common.InvItem(p.block, -1)))) {
/* 2046 */             addToCost(p.block, p.meta, 1);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 2052 */     if (plankCost > 0) {
/* 2053 */       addToCost(Blocks.field_150364_r, -1, (int)Math.max(Math.ceil(plankCost * 1.0D / 4.0D), 1.0D));
/*      */     }
/*      */     
/* 2056 */     if (plankOakCost > 0) {
/* 2057 */       addToCost(Blocks.field_150364_r, 0, (int)Math.max(Math.ceil(plankOakCost * 1.0D / 4.0D), 1.0D));
/*      */     }
/*      */     
/* 2060 */     if (plankPineCost > 0) {
/* 2061 */       addToCost(Blocks.field_150364_r, 1, (int)Math.max(Math.ceil(plankPineCost * 1.0D / 4.0D), 1.0D));
/*      */     }
/*      */     
/* 2064 */     if (plankBirchCost > 0) {
/* 2065 */       addToCost(Blocks.field_150364_r, 2, (int)Math.max(Math.ceil(plankBirchCost * 1.0D / 4.0D), 1.0D));
/*      */     }
/*      */     
/* 2068 */     if (plankJungleCost > 0) {
/* 2069 */       addToCost(Blocks.field_150364_r, 3, (int)Math.max(Math.ceil(plankJungleCost * 1.0D / 4.0D), 1.0D));
/*      */     }
/*      */     
/* 2072 */     if (glassPaneCost > 0) {
/* 2073 */       addToCost(Blocks.field_150359_w, 0, (int)Math.max(Math.ceil(glassPaneCost * 6.0D / 16.0D), 1.0D));
/*      */     }
/*      */     
/* 2076 */     if (byzBricksHalf > 0) {
/* 2077 */       addToCost(org.millenaire.common.forge.Mill.byzantine_tiles, 0, (int)Math.max(Math.ceil(byzBricksHalf / 2), 1.0D));
/*      */     }
/*      */     
/* 2080 */     if (org.millenaire.common.MLN.LogBuildingPlan >= 1) {
/* 2081 */       org.millenaire.common.MLN.major(this, "Loaded plan for " + this.planName + ".");
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public BuildingLocation findBuildingLocation(org.millenaire.common.MillWorldInfo winfo, org.millenaire.common.pathing.AStarPathing pathing, org.millenaire.common.Point centre, int maxRadius, java.util.Random random, int porientation)
/*      */   {
/* 2088 */     long startTime = System.nanoTime();
/*      */     
/* 2090 */     int ci = centre.getiX() - winfo.mapStartX;
/* 2091 */     int cj = centre.getiZ() - winfo.mapStartZ;
/*      */     
/* 2093 */     int radius = (int)(maxRadius * this.minDistance);
/* 2094 */     maxRadius = (int)(maxRadius * this.maxDistance);
/*      */     
/* 2096 */     if (org.millenaire.common.MLN.LogWorldGeneration >= 1) {
/* 2097 */       org.millenaire.common.MLN.major(this, "testBuildWorldInfo: Called to test for building " + this.planName + " around " + centre + "(" + ci + "/" + cj + "), start radius: " + radius + ", max radius: " + maxRadius);
/*      */     }
/*      */     
/* 2100 */     for (int i = 0; i < winfo.length; i++) {
/* 2101 */       for (int j = 0; j < winfo.width; j++) {
/* 2102 */         winfo.buildTested[i][j] = 0;
/*      */       }
/*      */     }
/*      */     
/* 2106 */     while (radius < maxRadius)
/*      */     {
/* 2108 */       int mini = Math.max(0, ci - radius);
/* 2109 */       int maxi = Math.min(winfo.length - 1, ci + radius);
/* 2110 */       int minj = Math.max(0, cj - radius);
/* 2111 */       int maxj = Math.min(winfo.width - 1, cj + radius);
/*      */       
/* 2113 */       if (org.millenaire.common.MLN.LogWorldGeneration >= 3) {
/* 2114 */         org.millenaire.common.MLN.debug(this, "Testing square: " + mini + "/" + minj + " to " + maxi + "/" + maxj);
/*      */       }
/*      */       
/* 2117 */       for (int i = mini; i < maxi; i++) {
/* 2118 */         if (cj - radius == minj) {
/* 2119 */           LocationReturn lr = testSpot(winfo, pathing, centre, i, minj, random, porientation);
/*      */           
/* 2121 */           if (lr.location != null) {
/* 2122 */             if (org.millenaire.common.MLN.LogBuildingPlan >= 2) {
/* 2123 */               org.millenaire.common.MLN.minor(this, "Time taken for location search: " + (System.nanoTime() - startTime) / 1000000.0D);
/*      */             }
/* 2125 */             return lr.location;
/*      */           }
/*      */         }
/* 2128 */         if (cj + radius == maxj) {
/* 2129 */           LocationReturn lr = testSpot(winfo, pathing, centre, i, maxj, random, porientation);
/* 2130 */           if (lr.location != null) {
/* 2131 */             if (org.millenaire.common.MLN.LogBuildingPlan >= 2) {
/* 2132 */               org.millenaire.common.MLN.minor(this, "Time taken for location search: " + (System.nanoTime() - startTime) / 1000000.0D);
/*      */             }
/* 2134 */             return lr.location;
/*      */           }
/*      */         }
/*      */       }
/*      */       
/* 2139 */       for (int j = minj; j < maxj; j++) {
/* 2140 */         if (ci - radius == mini) {
/* 2141 */           LocationReturn lr = testSpot(winfo, pathing, centre, mini, j, random, porientation);
/* 2142 */           if (lr.location != null) {
/* 2143 */             if (org.millenaire.common.MLN.LogBuildingPlan >= 2) {
/* 2144 */               org.millenaire.common.MLN.minor(this, "Time taken for location search: " + (System.nanoTime() - startTime) / 1000000.0D);
/*      */             }
/* 2146 */             return lr.location;
/*      */           }
/*      */         }
/* 2149 */         if (ci + radius == maxi) {
/* 2150 */           LocationReturn lr = testSpot(winfo, pathing, centre, maxi, j, random, porientation);
/* 2151 */           if (lr.location != null) {
/* 2152 */             if (org.millenaire.common.MLN.LogBuildingPlan >= 2) {
/* 2153 */               org.millenaire.common.MLN.minor(this, "Time taken for location search: " + (System.nanoTime() - startTime) / 1000000.0D);
/*      */             }
/* 2155 */             return lr.location;
/*      */           }
/*      */         }
/*      */       }
/*      */       
/* 2160 */       radius++;
/*      */     }
/*      */     
/* 2163 */     if (org.millenaire.common.MLN.LogWorldGeneration >= 1) {
/* 2164 */       org.millenaire.common.MLN.major(this, "Could not find acceptable location (radius: " + radius + ")");
/*      */     }
/*      */     
/* 2167 */     if (org.millenaire.common.MLN.LogBuildingPlan >= 2) {
/* 2168 */       org.millenaire.common.MLN.minor(this, "Time taken for unsuccessful location search: " + (System.nanoTime() - startTime) / 1000000.0D);
/*      */     }
/*      */     
/* 2171 */     return null;
/*      */   }
/*      */   
/*      */   private int getBedMeta(int direction, int orientation) {
/* 2175 */     int faces = (direction + 4 - orientation) % 4;
/*      */     
/* 2177 */     if (faces == 0)
/* 2178 */       return 1;
/* 2179 */     if (faces == 1)
/* 2180 */       return 2;
/* 2181 */     if (faces == 2) {
/* 2182 */       return 3;
/*      */     }
/* 2184 */     return 0;
/*      */   }
/*      */   
/*      */   public net.minecraft.block.Block getBlock(net.minecraft.world.World worldObj, org.millenaire.common.Point p)
/*      */   {
/* 2189 */     return org.millenaire.common.core.MillCommonUtilities.getBlock(worldObj, p);
/*      */   }
/*      */   
/*      */   public BuildingBlock[] getBuildingPoints(net.minecraft.world.World world, BuildingLocation location, boolean villageGeneration)
/*      */   {
/* 2194 */     int x = location.pos.getiX();
/* 2195 */     int y = location.pos.getiY();
/* 2196 */     int z = location.pos.getiZ();
/* 2197 */     int orientation = location.orientation;
/*      */     
/* 2199 */     this.nbBlocksToPut = 0;
/*      */     
/*      */ 
/* 2202 */     int approximateBlocks = (this.length + this.areaToClear * 2) * (this.width + this.areaToClear * 2) * (this.nbfloors + 50);
/*      */     
/* 2204 */     java.util.List<BuildingBlock> bblocks = new java.util.ArrayList(approximateBlocks + 100);
/*      */     
/* 2206 */     if (org.millenaire.common.MLN.LogWorldGeneration >= 2) {
/* 2207 */       org.millenaire.common.MLN.minor(this, "Getting blocks for " + this.planName + " at " + x + "/" + y + "/" + z + "/" + orientation);
/*      */     }
/*      */     
/* 2210 */     if ((!this.isUpdate) && (!isSubBuilding()) && (!location.bedrocklevel))
/*      */     {
/*      */ 
/* 2213 */       for (int j = -this.areaToClear; j < this.length + this.areaToClear; j++) {
/* 2214 */         for (int k = -this.areaToClear; k < this.width + this.areaToClear; k++) {
/* 2215 */           for (int i = this.nbfloors + 50; i > -1; i--) {
/* 2216 */             int ak = j % 2 == 0 ? k : this.width - k - 1;
/*      */             
/*      */ 
/*      */ 
/* 2220 */             int offset = 0;
/*      */             
/* 2222 */             if (j < 0) {
/* 2223 */               offset = -j;
/* 2224 */             } else if (j >= this.length - 1) {
/* 2225 */               offset = j - this.length + 1;
/*      */             }
/* 2227 */             if ((ak < 0) && (-ak > offset)) {
/* 2228 */               offset = -ak;
/* 2229 */             } else if ((ak >= this.width - 1) && (ak - this.width + 1 > offset)) {
/* 2230 */               offset = ak - this.width + 1;
/*      */             }
/*      */             
/* 2233 */             offset--;
/*      */             
/* 2235 */             if (i >= offset - 1)
/*      */             {
/*      */ 
/* 2238 */               org.millenaire.common.Point p = adjustForOrientation(x, y + i, z, j - this.lengthOffset, ak - this.widthOffset, orientation);
/* 2239 */               bblocks.add(new BuildingBlock(p, 0, 0, BuildingBlock.CLEARGROUND));
/*      */             } else {
/* 2241 */               org.millenaire.common.Point p = adjustForOrientation(x, y + i, z, j - this.lengthOffset, k - this.widthOffset, orientation);
/* 2242 */               bblocks.add(new BuildingBlock(p, 0, 0, BuildingBlock.CLEARTREE));
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 2250 */       for (int j = -this.areaToClear; j < this.length + this.areaToClear; j++) {
/* 2251 */         for (int k = -this.areaToClear; k < this.width + this.areaToClear; k++) {
/* 2252 */           for (int i = -10 + this.startLevel; i < 0; i++) {
/* 2253 */             int ak = j % 2 == 0 ? k : this.width - k - 1;
/*      */             
/*      */ 
/*      */ 
/* 2257 */             int offset = 0;
/*      */             
/* 2259 */             if (j < 0) {
/* 2260 */               offset = -j;
/* 2261 */             } else if (j >= this.length - 1) {
/* 2262 */               offset = j - this.length + 1;
/*      */             }
/* 2264 */             if ((ak < 0) && (-ak > offset)) {
/* 2265 */               offset = -ak;
/* 2266 */             } else if ((ak >= this.width - 1) && (ak - this.width + 1 > offset)) {
/* 2267 */               offset = ak - this.width + 1;
/*      */             }
/*      */             
/* 2270 */             offset--;
/*      */             
/* 2272 */             if (-i > offset) {
/* 2273 */               org.millenaire.common.Point p = adjustForOrientation(x, y + i, z, j - this.lengthOffset, k - this.widthOffset, orientation);
/* 2274 */               bblocks.add(new BuildingBlock(p, 0, 0, BuildingBlock.PRESERVEGROUNDDEPTH));
/* 2275 */               this.nbBlocksToPut += 1;
/* 2276 */             } else if (-i == offset) {
/* 2277 */               org.millenaire.common.Point p = adjustForOrientation(x, y + i, z, j - this.lengthOffset, k - this.widthOffset, orientation);
/* 2278 */               bblocks.add(new BuildingBlock(p, 0, 0, BuildingBlock.PRESERVEGROUNDSURFACE));
/* 2279 */               this.nbBlocksToPut += 1;
/*      */             } else {
/* 2281 */               org.millenaire.common.Point p = adjustForOrientation(x, y + i, z, j - this.lengthOffset, k - this.widthOffset, orientation);
/* 2282 */               bblocks.add(new BuildingBlock(p, 0, 0, BuildingBlock.CLEARTREE));
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 2289 */     for (int i = 0; i < this.nbfloors; i++) {
/* 2290 */       for (int j = 0; j < this.length; j++) {
/* 2291 */         for (int k = 0; k < this.width; k++) {
/* 2292 */           PointType pt = this.plan[i][j][k];
/* 2293 */           if (pt.isType("preserveground")) {
/* 2294 */             org.millenaire.common.Point p = adjustForOrientation(x, y + i + this.startLevel, z, j - this.lengthOffset, k - this.widthOffset, orientation);
/* 2295 */             bblocks.add(new BuildingBlock(p, 0, 0, BuildingBlock.PRESERVEGROUNDSURFACE));
/* 2296 */             this.nbBlocksToPut += 1;
/* 2297 */           } else if (pt.isType("allbuttrees")) {
/* 2298 */             org.millenaire.common.Point p = adjustForOrientation(x, y + i + this.startLevel, z, j - this.lengthOffset, k - this.widthOffset, orientation);
/* 2299 */             bblocks.add(new BuildingBlock(p, 0, 0, BuildingBlock.CLEARTREE));
/* 2300 */             this.nbBlocksToPut += 1;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 2307 */     for (int i = this.nbfloors - 1; i >= 0; i--) {
/* 2308 */       for (int j = 0; j < this.length; j++) {
/* 2309 */         for (int k = 0; k < this.width; k++) {
/* 2310 */           int ak = j % 2 == 0 ? k : this.width - k - 1;
/*      */           
/* 2312 */           PointType pt = this.plan[i][j][ak];
/*      */           
/* 2314 */           org.millenaire.common.Point p = adjustForOrientation(x, y + i + this.startLevel, z, j - this.lengthOffset, ak - this.widthOffset, orientation);
/*      */           
/* 2316 */           if (pt.block == Blocks.field_150350_a) {
/* 2317 */             bblocks.add(new BuildingBlock(p, Blocks.field_150350_a, 0));
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 2324 */     for (int i = 0; i < this.nbfloors; i++) {
/* 2325 */       for (int j = 0; j < this.length; j++) {
/* 2326 */         for (int k = 0; k < this.width; k++)
/*      */         {
/* 2328 */           int ak = j % 2 == 0 ? k : this.width - k - 1;
/* 2329 */           int ai = i + this.startLevel < 0 ? -i - this.startLevel - 1 : i;
/*      */           
/* 2331 */           PointType pt = this.plan[ai][j][ak];
/* 2332 */           int m = 0;
/* 2333 */           net.minecraft.block.Block b = null;
/*      */           
/* 2335 */           org.millenaire.common.Point p = adjustForOrientation(x, y + ai + this.startLevel, z, j - this.lengthOffset, ak - this.widthOffset, orientation);
/*      */           
/* 2337 */           if ((pt.block != null) && (pt.block != Blocks.field_150350_a) && (!pt.secondStep))
/*      */           {
/* 2339 */             b = pt.block;
/* 2340 */             m = pt.meta;
/* 2341 */           } else if ((pt.isType("empty")) && (!this.isUpdate) && (!TYPE_SUBBUILDING.equals(this.type))) {
/* 2342 */             b = Blocks.field_150350_a;
/* 2343 */           } else if ((pt.isType("grass")) && (villageGeneration)) {
/* 2344 */             b = Blocks.field_150349_c;
/* 2345 */           } else if ((pt.isType("grass")) && (!villageGeneration)) {
/* 2346 */             b = Blocks.field_150346_d;
/* 2347 */           } else if ((pt.isType("soil")) && (villageGeneration)) {
/* 2348 */             b = Blocks.field_150349_c;
/* 2349 */           } else if ((pt.isType("ricesoil")) && (villageGeneration)) {
/* 2350 */             b = Blocks.field_150349_c;
/* 2351 */           } else if ((pt.isType("turmericsoil")) && (villageGeneration)) {
/* 2352 */             b = Blocks.field_150349_c;
/* 2353 */           } else if ((pt.isType("maizesoil")) && (villageGeneration)) {
/* 2354 */             b = Blocks.field_150349_c;
/* 2355 */           } else if ((pt.isType("carrotsoil")) && (villageGeneration)) {
/* 2356 */             b = Blocks.field_150349_c;
/* 2357 */           } else if ((pt.isType("potatosoil")) && (villageGeneration)) {
/* 2358 */             b = Blocks.field_150349_c;
/* 2359 */           } else if ((pt.isType("sugarcanesoil")) && (villageGeneration)) {
/* 2360 */             b = Blocks.field_150349_c;
/* 2361 */           } else if ((pt.isType("vinesoil")) && (villageGeneration)) {
/* 2362 */             b = Blocks.field_150349_c;
/* 2363 */           } else if ((pt.isType("cacaospot")) && (villageGeneration)) {
/* 2364 */             b = Blocks.field_150375_by;
/* 2365 */           } else if ((pt.isType("soil")) && (!villageGeneration)) {
/* 2366 */             b = Blocks.field_150346_d;
/* 2367 */           } else if ((pt.isType("ricesoil")) && (!villageGeneration)) {
/* 2368 */             b = Blocks.field_150346_d;
/* 2369 */           } else if ((pt.isType("turmericsoil")) && (!villageGeneration)) {
/* 2370 */             b = Blocks.field_150346_d;
/* 2371 */           } else if ((pt.isType("maizesoil")) && (!villageGeneration)) {
/* 2372 */             b = Blocks.field_150346_d;
/* 2373 */           } else if ((pt.isType("potatosoil")) && (villageGeneration)) {
/* 2374 */             b = Blocks.field_150346_d;
/* 2375 */           } else if ((pt.isType("carrotsoil")) && (!villageGeneration)) {
/* 2376 */             b = Blocks.field_150346_d;
/* 2377 */           } else if ((pt.isType("sugarcanesoil")) && (!villageGeneration)) {
/* 2378 */             b = Blocks.field_150346_d;
/* 2379 */           } else if ((pt.isType("vinesoil")) && (!villageGeneration)) {
/* 2380 */             b = Blocks.field_150346_d;
/* 2381 */           } else if ((pt.isType("cacaospot")) && (!villageGeneration)) {
/* 2382 */             b = null;
/* 2383 */           } else if (pt.isType("netherwartsoil")) {
/* 2384 */             b = Blocks.field_150425_aM;
/* 2385 */           } else if (pt.isType("silkwormblock")) {
/* 2386 */             b = org.millenaire.common.forge.Mill.wood_decoration;
/* 2387 */             m = 3;
/* 2388 */           } else if (pt.isType("lockedchest")) {
/* 2389 */             b = org.millenaire.common.forge.Mill.lockedChest;
/* 2390 */           } else if (pt.isType("furnace")) {
/* 2391 */             b = Blocks.field_150460_al;
/* 2392 */           } else if (pt.isType("brewingstand")) {
/* 2393 */             b = Blocks.field_150382_bo;
/*      */           }
/* 2395 */           else if (pt.isType("logoakhor")) {
/* 2396 */             b = Blocks.field_150364_r;
/* 2397 */             m = getWoodMeta(0, orientation);
/* 2398 */           } else if (pt.isType("logoakvert")) {
/* 2399 */             b = Blocks.field_150364_r;
/* 2400 */             m = getWoodMeta(1, orientation);
/*      */           }
/* 2402 */           else if (pt.isType("logpinehor")) {
/* 2403 */             b = Blocks.field_150364_r;
/* 2404 */             m = 1 + getWoodMeta(0, orientation);
/* 2405 */           } else if (pt.isType("logpinevert")) {
/* 2406 */             b = Blocks.field_150364_r;
/* 2407 */             m = 1 + getWoodMeta(1, orientation);
/*      */           }
/* 2409 */           else if (pt.isType("logbirchhor")) {
/* 2410 */             b = Blocks.field_150364_r;
/* 2411 */             m = 2 + getWoodMeta(0, orientation);
/* 2412 */           } else if (pt.isType("logbirchvert")) {
/* 2413 */             b = Blocks.field_150364_r;
/* 2414 */             m = 2 + getWoodMeta(1, orientation);
/*      */           }
/* 2416 */           else if (pt.isType("logjunglehor")) {
/* 2417 */             b = Blocks.field_150364_r;
/* 2418 */             m = 3 + getWoodMeta(0, orientation);
/* 2419 */           } else if (pt.isType("logjunglevert")) {
/* 2420 */             b = Blocks.field_150364_r;
/* 2421 */             m = 3 + getWoodMeta(1, orientation);
/*      */           }
/* 2423 */           else if (pt.isType("woodstairsOakTop")) {
/* 2424 */             b = Blocks.field_150476_ad;
/* 2425 */             m = getStairMeta(0, orientation);
/* 2426 */           } else if (pt.isType("woodstairsOakRight")) {
/* 2427 */             b = Blocks.field_150476_ad;
/* 2428 */             m = getStairMeta(1, orientation);
/* 2429 */           } else if (pt.isType("woodstairsOakBottom")) {
/* 2430 */             b = Blocks.field_150476_ad;
/* 2431 */             m = getStairMeta(2, orientation);
/* 2432 */           } else if (pt.isType("woodstairsOakLeft")) {
/* 2433 */             b = Blocks.field_150476_ad;
/* 2434 */             m = getStairMeta(3, orientation);
/*      */           }
/* 2436 */           else if (pt.isType("woodstairsPineTop")) {
/* 2437 */             b = Blocks.field_150485_bF;
/* 2438 */             m = getStairMeta(0, orientation);
/* 2439 */           } else if (pt.isType("woodstairsPineRight")) {
/* 2440 */             b = Blocks.field_150485_bF;
/* 2441 */             m = getStairMeta(1, orientation);
/* 2442 */           } else if (pt.isType("woodstairsPineBottom")) {
/* 2443 */             b = Blocks.field_150485_bF;
/* 2444 */             m = getStairMeta(2, orientation);
/* 2445 */           } else if (pt.isType("woodstairsPineLeft")) {
/* 2446 */             b = Blocks.field_150485_bF;
/* 2447 */             m = getStairMeta(3, orientation);
/*      */           }
/* 2449 */           else if (pt.isType("woodstairsBirchTop")) {
/* 2450 */             b = Blocks.field_150487_bG;
/* 2451 */             m = getStairMeta(0, orientation);
/* 2452 */           } else if (pt.isType("woodstairsBirchRight")) {
/* 2453 */             b = Blocks.field_150487_bG;
/* 2454 */             m = getStairMeta(1, orientation);
/* 2455 */           } else if (pt.isType("woodstairsBirchBottom")) {
/* 2456 */             b = Blocks.field_150487_bG;
/* 2457 */             m = getStairMeta(2, orientation);
/* 2458 */           } else if (pt.isType("woodstairsBirchLeft")) {
/* 2459 */             b = Blocks.field_150487_bG;
/* 2460 */             m = getStairMeta(3, orientation);
/*      */           }
/* 2462 */           else if (pt.isType("woodstairsJungleTop")) {
/* 2463 */             b = Blocks.field_150481_bH;
/* 2464 */             m = getStairMeta(0, orientation);
/* 2465 */           } else if (pt.isType("woodstairsJungleRight")) {
/* 2466 */             b = Blocks.field_150481_bH;
/* 2467 */             m = getStairMeta(1, orientation);
/* 2468 */           } else if (pt.isType("woodstairsJungleBottom")) {
/* 2469 */             b = Blocks.field_150481_bH;
/* 2470 */             m = getStairMeta(2, orientation);
/* 2471 */           } else if (pt.isType("woodstairsJungleLeft")) {
/* 2472 */             b = Blocks.field_150481_bH;
/* 2473 */             m = getStairMeta(3, orientation);
/*      */           }
/* 2475 */           else if (pt.isType("stonestairsTop")) {
/* 2476 */             b = Blocks.field_150446_ar;
/* 2477 */             m = getStairMeta(0, orientation);
/* 2478 */           } else if (pt.isType("stonestairsRight")) {
/* 2479 */             b = Blocks.field_150446_ar;
/* 2480 */             m = getStairMeta(1, orientation);
/* 2481 */           } else if (pt.isType("stonestairsBottom")) {
/* 2482 */             b = Blocks.field_150446_ar;
/* 2483 */             m = getStairMeta(2, orientation);
/* 2484 */           } else if (pt.isType("stonestairsLeft")) {
/* 2485 */             b = Blocks.field_150446_ar;
/* 2486 */             m = getStairMeta(3, orientation);
/* 2487 */           } else if (pt.isType("stonebrickstairsTop")) {
/* 2488 */             b = Blocks.field_150390_bg;
/* 2489 */             m = getStairMeta(0, orientation);
/* 2490 */           } else if (pt.isType("stonebrickstairsRight")) {
/* 2491 */             b = Blocks.field_150390_bg;
/* 2492 */             m = getStairMeta(1, orientation);
/* 2493 */           } else if (pt.isType("stonebrickstairsBottom")) {
/* 2494 */             b = Blocks.field_150390_bg;
/* 2495 */             m = getStairMeta(2, orientation);
/* 2496 */           } else if (pt.isType("stonebrickstairsLeft")) {
/* 2497 */             b = Blocks.field_150390_bg;
/* 2498 */             m = getStairMeta(3, orientation);
/* 2499 */           } else if (pt.isType("brickstairsTop")) {
/* 2500 */             b = Blocks.field_150389_bf;
/* 2501 */             m = getStairMeta(0, orientation);
/* 2502 */           } else if (pt.isType("brickstairsRight")) {
/* 2503 */             b = Blocks.field_150389_bf;
/* 2504 */             m = getStairMeta(1, orientation);
/* 2505 */           } else if (pt.isType("brickstairsBottom")) {
/* 2506 */             b = Blocks.field_150389_bf;
/* 2507 */             m = getStairMeta(2, orientation);
/* 2508 */           } else if (pt.isType("brickstairsLeft")) {
/* 2509 */             b = Blocks.field_150389_bf;
/* 2510 */             m = getStairMeta(3, orientation);
/* 2511 */           } else if (pt.isType("sandstonestairsTop")) {
/* 2512 */             b = Blocks.field_150372_bz;
/* 2513 */             m = getStairMeta(0, orientation);
/* 2514 */           } else if (pt.isType("sandstonestairsRight")) {
/* 2515 */             b = Blocks.field_150372_bz;
/* 2516 */             m = getStairMeta(1, orientation);
/* 2517 */           } else if (pt.isType("sandstonestairsBottom")) {
/* 2518 */             b = Blocks.field_150372_bz;
/* 2519 */             m = getStairMeta(2, orientation);
/* 2520 */           } else if (pt.isType("sandstonestairsLeft")) {
/* 2521 */             b = Blocks.field_150372_bz;
/* 2522 */             m = getStairMeta(3, orientation);
/*      */           }
/* 2524 */           else if (pt.isType("woodstairsOakInvTop")) {
/* 2525 */             b = Blocks.field_150476_ad;
/* 2526 */             m = getStairMeta(0, orientation) + 4;
/* 2527 */           } else if (pt.isType("woodstairsOakInvRight")) {
/* 2528 */             b = Blocks.field_150476_ad;
/* 2529 */             m = getStairMeta(1, orientation) + 4;
/* 2530 */           } else if (pt.isType("woodstairsOakInvBottom")) {
/* 2531 */             b = Blocks.field_150476_ad;
/* 2532 */             m = getStairMeta(2, orientation) + 4;
/* 2533 */           } else if (pt.isType("woodstairsOakInvLeft")) {
/* 2534 */             b = Blocks.field_150476_ad;
/* 2535 */             m = getStairMeta(3, orientation) + 4;
/*      */           }
/* 2537 */           else if (pt.isType("woodstairsPineInvTop")) {
/* 2538 */             b = Blocks.field_150485_bF;
/* 2539 */             m = getStairMeta(0, orientation) + 4;
/* 2540 */           } else if (pt.isType("woodstairsPineInvRight")) {
/* 2541 */             b = Blocks.field_150485_bF;
/* 2542 */             m = getStairMeta(1, orientation) + 4;
/* 2543 */           } else if (pt.isType("woodstairsPineInvBottom")) {
/* 2544 */             b = Blocks.field_150485_bF;
/* 2545 */             m = getStairMeta(2, orientation) + 4;
/* 2546 */           } else if (pt.isType("woodstairsPineInvLeft")) {
/* 2547 */             b = Blocks.field_150485_bF;
/* 2548 */             m = getStairMeta(3, orientation) + 4;
/*      */           }
/* 2550 */           else if (pt.isType("woodstairsBirchInvTop")) {
/* 2551 */             b = Blocks.field_150487_bG;
/* 2552 */             m = getStairMeta(0, orientation) + 4;
/* 2553 */           } else if (pt.isType("woodstairsBirchInvRight")) {
/* 2554 */             b = Blocks.field_150487_bG;
/* 2555 */             m = getStairMeta(1, orientation) + 4;
/* 2556 */           } else if (pt.isType("woodstairsBirchInvBottom")) {
/* 2557 */             b = Blocks.field_150487_bG;
/* 2558 */             m = getStairMeta(2, orientation) + 4;
/* 2559 */           } else if (pt.isType("woodstairsBirchInvLeft")) {
/* 2560 */             b = Blocks.field_150487_bG;
/* 2561 */             m = getStairMeta(3, orientation) + 4;
/*      */           }
/* 2563 */           else if (pt.isType("woodstairsJungleInvTop")) {
/* 2564 */             b = Blocks.field_150481_bH;
/* 2565 */             m = getStairMeta(0, orientation) + 4;
/* 2566 */           } else if (pt.isType("woodstairsJungleInvRight")) {
/* 2567 */             b = Blocks.field_150481_bH;
/* 2568 */             m = getStairMeta(1, orientation) + 4;
/* 2569 */           } else if (pt.isType("woodstairsJungleInvBottom")) {
/* 2570 */             b = Blocks.field_150481_bH;
/* 2571 */             m = getStairMeta(2, orientation) + 4;
/* 2572 */           } else if (pt.isType("woodstairsJungleInvLeft")) {
/* 2573 */             b = Blocks.field_150481_bH;
/* 2574 */             m = getStairMeta(3, orientation) + 4;
/*      */           }
/* 2576 */           else if (pt.isType("stonestairsInvTop")) {
/* 2577 */             b = Blocks.field_150446_ar;
/* 2578 */             m = getStairMeta(0, orientation) + 4;
/* 2579 */           } else if (pt.isType("stonestairsInvRight")) {
/* 2580 */             b = Blocks.field_150446_ar;
/* 2581 */             m = getStairMeta(1, orientation) + 4;
/* 2582 */           } else if (pt.isType("stonestairsInvBottom")) {
/* 2583 */             b = Blocks.field_150446_ar;
/* 2584 */             m = getStairMeta(2, orientation) + 4;
/* 2585 */           } else if (pt.isType("stonestairsInvLeft")) {
/* 2586 */             b = Blocks.field_150446_ar;
/* 2587 */             m = getStairMeta(3, orientation) + 4;
/* 2588 */           } else if (pt.isType("stonebrickstairsInvTop")) {
/* 2589 */             b = Blocks.field_150390_bg;
/* 2590 */             m = getStairMeta(0, orientation) + 4;
/* 2591 */           } else if (pt.isType("stonebrickstairsInvRight")) {
/* 2592 */             b = Blocks.field_150390_bg;
/* 2593 */             m = getStairMeta(1, orientation) + 4;
/* 2594 */           } else if (pt.isType("stonebrickstairsInvBottom")) {
/* 2595 */             b = Blocks.field_150390_bg;
/* 2596 */             m = getStairMeta(2, orientation) + 4;
/* 2597 */           } else if (pt.isType("stonebrickstairsInvLeft")) {
/* 2598 */             b = Blocks.field_150390_bg;
/* 2599 */             m = getStairMeta(3, orientation) + 4;
/* 2600 */           } else if (pt.isType("brickstairsInvTop")) {
/* 2601 */             b = Blocks.field_150389_bf;
/* 2602 */             m = getStairMeta(0, orientation) + 4;
/* 2603 */           } else if (pt.isType("brickstairsInvRight")) {
/* 2604 */             b = Blocks.field_150389_bf;
/* 2605 */             m = getStairMeta(1, orientation) + 4;
/* 2606 */           } else if (pt.isType("brickstairsInvBottom")) {
/* 2607 */             b = Blocks.field_150389_bf;
/* 2608 */             m = getStairMeta(2, orientation) + 4;
/* 2609 */           } else if (pt.isType("brickstairsInvLeft")) {
/* 2610 */             b = Blocks.field_150389_bf;
/* 2611 */             m = getStairMeta(3, orientation) + 4;
/* 2612 */           } else if (pt.isType("sandstonestairsInvTop")) {
/* 2613 */             b = Blocks.field_150372_bz;
/* 2614 */             m = getStairMeta(0, orientation) + 4;
/* 2615 */           } else if (pt.isType("sandstonestairsInvRight")) {
/* 2616 */             b = Blocks.field_150372_bz;
/* 2617 */             m = getStairMeta(1, orientation) + 4;
/* 2618 */           } else if (pt.isType("sandstonestairsInvBottom")) {
/* 2619 */             b = Blocks.field_150372_bz;
/* 2620 */             m = getStairMeta(2, orientation) + 4;
/* 2621 */           } else if (pt.isType("sandstonestairsInvLeft")) {
/* 2622 */             b = Blocks.field_150372_bz;
/* 2623 */             m = getStairMeta(3, orientation) + 4;
/*      */           }
/* 2625 */           else if (pt.isType("byzantinetiles_bottomtop")) {
/* 2626 */             b = org.millenaire.common.forge.Mill.byzantine_tiles;
/* 2627 */             m = getOrientedBlockMeta(0, orientation);
/* 2628 */           } else if (pt.isType("byzantinetiles_leftright")) {
/* 2629 */             b = org.millenaire.common.forge.Mill.byzantine_tiles;
/* 2630 */             m = getOrientedBlockMeta(1, orientation);
/* 2631 */           } else if (pt.isType("byzantinestonetiles_bottomtop")) {
/* 2632 */             b = org.millenaire.common.forge.Mill.byzantine_stone_tiles;
/* 2633 */             m = getOrientedBlockMeta(0, orientation);
/* 2634 */           } else if (pt.isType("byzantinestonetiles_leftright")) {
/* 2635 */             b = org.millenaire.common.forge.Mill.byzantine_stone_tiles;
/* 2636 */             m = getOrientedBlockMeta(1, orientation);
/*      */           }
/* 2638 */           else if (pt.isType("byzantinetileslab_bottomtop")) {
/* 2639 */             b = org.millenaire.common.forge.Mill.byzantine_tile_slab;
/* 2640 */             m = getOrientedBlockMeta(0, orientation);
/* 2641 */           } else if (pt.isType("byzantinetileslab_leftright")) {
/* 2642 */             b = org.millenaire.common.forge.Mill.byzantine_tile_slab;
/* 2643 */             m = getOrientedBlockMeta(1, orientation);
/* 2644 */           } else if (pt.isType("byzantinetileslab_bottomtop_inv")) {
/* 2645 */             b = org.millenaire.common.forge.Mill.byzantine_tile_slab;
/* 2646 */             m = getOrientedBlockMeta(0, orientation) + 8;
/* 2647 */           } else if (pt.isType("byzantinetileslab_leftright_inv")) {
/* 2648 */             b = org.millenaire.common.forge.Mill.byzantine_tile_slab;
/* 2649 */             m = getOrientedBlockMeta(1, orientation) + 8;
/*      */           }
/* 2651 */           else if (pt.isType("signpostTop")) {
/* 2652 */             b = Blocks.field_150472_an;
/* 2653 */             m = getSignOrLadderMeta(0, orientation);
/* 2654 */           } else if (pt.isType("signpostRight")) {
/* 2655 */             b = Blocks.field_150472_an;
/* 2656 */             m = getSignOrLadderMeta(1, orientation);
/* 2657 */           } else if (pt.isType("signpostBottom")) {
/* 2658 */             b = Blocks.field_150472_an;
/* 2659 */             m = getSignOrLadderMeta(2, orientation);
/* 2660 */           } else if (pt.isType("signpostLeft")) {
/* 2661 */             b = Blocks.field_150472_an;
/* 2662 */             m = getSignOrLadderMeta(3, orientation);
/* 2663 */           } else if (pt.isType("sleepingPos")) {
/* 2664 */             b = Blocks.field_150350_a;
/* 2665 */             location.sleepingPos = p;
/* 2666 */           } else if (pt.isType("sellingPos")) {
/* 2667 */             b = Blocks.field_150350_a;
/* 2668 */             location.sellingPos = p;
/* 2669 */           } else if (pt.isType("craftingPos")) {
/* 2670 */             b = Blocks.field_150350_a;
/* 2671 */             location.craftingPos = p;
/* 2672 */           } else if (pt.isType("shelterPos")) {
/* 2673 */             b = Blocks.field_150350_a;
/* 2674 */             location.shelterPos = p;
/* 2675 */           } else if (pt.isType("defendingPos")) {
/* 2676 */             b = Blocks.field_150350_a;
/* 2677 */             location.defendingPos = p;
/* 2678 */           } else if (pt.isType("sandsource")) {
/* 2679 */             b = Blocks.field_150354_m;
/* 2680 */           } else if (pt.isType("sandstonesource")) {
/* 2681 */             b = Blocks.field_150322_A;
/* 2682 */           } else if (pt.isType("claysource")) {
/* 2683 */             b = Blocks.field_150435_aG;
/* 2684 */           } else if (pt.isType("gravelsource")) {
/* 2685 */             b = Blocks.field_150351_n;
/* 2686 */           } else if (pt.isType("stonesource")) {
/* 2687 */             b = Blocks.field_150348_b;
/* 2688 */           } else if (pt.isType("freesand")) {
/* 2689 */             b = Blocks.field_150354_m;
/* 2690 */           } else if (pt.isType("freesandstone")) {
/* 2691 */             b = Blocks.field_150322_A;
/* 2692 */           } else if (pt.isType("freegravel")) {
/* 2693 */             b = Blocks.field_150351_n;
/* 2694 */           } else if (pt.isType("freewool")) {
/* 2695 */             b = Blocks.field_150325_L;
/* 2696 */           } else if (pt.isType("freestone")) {
/* 2697 */             b = Blocks.field_150348_b;
/*      */           }
/* 2699 */           else if (pt.isType("squidspawn")) {
/* 2700 */             b = Blocks.field_150355_j;
/*      */           }
/*      */           
/* 2703 */           if (b != null) {
/* 2704 */             bblocks.add(new BuildingBlock(p, b, m));
/* 2705 */             this.nbBlocksToPut += 1;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 2712 */     for (int i = 0; i < this.nbfloors; i++) {
/* 2713 */       for (int j = 0; j < this.length; j++) {
/* 2714 */         for (int k = 0; k < this.width; k++)
/*      */         {
/* 2716 */           int ak = j % 2 == 0 ? k : this.width - k - 1;
/* 2717 */           int ai = i + this.startLevel < 0 ? -i - this.startLevel - 1 : i;
/*      */           
/* 2719 */           PointType pt = this.plan[ai][j][ak];
/* 2720 */           int m = 0;
/* 2721 */           net.minecraft.block.Block b = null;
/* 2722 */           org.millenaire.common.Point p = adjustForOrientation(x, y + ai + this.startLevel, z, j - this.lengthOffset, ak - this.widthOffset, orientation);
/*      */           
/* 2724 */           if ((pt.block != null) && (pt.secondStep)) {
/* 2725 */             b = pt.block;
/* 2726 */             m = pt.meta;
/* 2727 */           } else if (pt.isType("woodstairsOakGuess")) {
/* 2728 */             b = Blocks.field_150476_ad;
/* 2729 */             m = -1;
/* 2730 */           } else if (pt.isType("stonestairGuess")) {
/* 2731 */             b = Blocks.field_150446_ar;
/* 2732 */             m = -1;
/* 2733 */           } else if (pt.isType("ladderGuess")) {
/* 2734 */             b = Blocks.field_150468_ap;
/* 2735 */             m = -1;
/* 2736 */           } else if (pt.isType("signwallGuess")) {
/* 2737 */             b = org.millenaire.common.forge.Mill.panel;
/* 2738 */             m = guessSignMeta(bblocks, p);
/* 2739 */           } else if (pt.isType("plainSignGuess")) {
/* 2740 */             b = Blocks.field_150444_as;
/* 2741 */             m = guessSignMeta(bblocks, p);
/* 2742 */           } else if (pt.isType("signwallTop")) {
/* 2743 */             b = org.millenaire.common.forge.Mill.panel;
/* 2744 */             m = getSignOrLadderMeta(0, orientation);
/* 2745 */           } else if (pt.isType("signwallRight")) {
/* 2746 */             b = org.millenaire.common.forge.Mill.panel;
/* 2747 */             m = getSignOrLadderMeta(3, orientation);
/* 2748 */           } else if (pt.isType("signwallBottom")) {
/* 2749 */             b = org.millenaire.common.forge.Mill.panel;
/* 2750 */             m = getSignOrLadderMeta(2, orientation);
/* 2751 */           } else if (pt.isType("signwallLeft")) {
/* 2752 */             b = org.millenaire.common.forge.Mill.panel;
/* 2753 */             m = getSignOrLadderMeta(1, orientation);
/* 2754 */           } else if (pt.isType("ladderTop")) {
/* 2755 */             b = Blocks.field_150468_ap;
/* 2756 */             m = getSignOrLadderMeta(0, orientation);
/* 2757 */           } else if (pt.isType("ladderRight")) {
/* 2758 */             b = Blocks.field_150468_ap;
/* 2759 */             m = getSignOrLadderMeta(3, orientation);
/* 2760 */           } else if (pt.isType("ladderBottom")) {
/* 2761 */             b = Blocks.field_150468_ap;
/* 2762 */             m = getSignOrLadderMeta(2, orientation);
/* 2763 */           } else if (pt.isType("ladderLeft")) {
/* 2764 */             b = Blocks.field_150468_ap;
/* 2765 */             m = getSignOrLadderMeta(1, orientation);
/* 2766 */           } else if (pt.isType("doorTop")) {
/* 2767 */             b = Blocks.field_150466_ao;
/* 2768 */             m = getDoorMeta(0, orientation);
/* 2769 */           } else if (pt.isType("doorRight")) {
/* 2770 */             b = Blocks.field_150466_ao;
/* 2771 */             m = getDoorMeta(1, orientation);
/* 2772 */           } else if (pt.isType("doorBottom")) {
/* 2773 */             b = Blocks.field_150466_ao;
/* 2774 */             m = getDoorMeta(2, orientation);
/* 2775 */           } else if (pt.isType("doorLeft")) {
/* 2776 */             b = Blocks.field_150466_ao;
/* 2777 */             m = getDoorMeta(3, orientation);
/*      */           }
/* 2779 */           else if (pt.isType("irondoorTop")) {
/* 2780 */             b = Blocks.field_150454_av;
/* 2781 */             m = getDoorMeta(0, orientation);
/* 2782 */           } else if (pt.isType("irondoorRight")) {
/* 2783 */             b = Blocks.field_150454_av;
/* 2784 */             m = getDoorMeta(1, orientation);
/* 2785 */           } else if (pt.isType("irondoorBottom")) {
/* 2786 */             b = Blocks.field_150454_av;
/* 2787 */             m = getDoorMeta(2, orientation);
/* 2788 */           } else if (pt.isType("irondoorLeft")) {
/* 2789 */             b = Blocks.field_150454_av;
/* 2790 */             m = getDoorMeta(3, orientation);
/*      */           }
/* 2792 */           else if (pt.isType("trapdoorTop")) {
/* 2793 */             b = Blocks.field_150415_aT;
/* 2794 */             m = getTrapdoorMeta(0, orientation);
/* 2795 */           } else if (pt.isType("trapdoorRight")) {
/* 2796 */             b = Blocks.field_150415_aT;
/* 2797 */             m = getTrapdoorMeta(1, orientation);
/* 2798 */           } else if (pt.isType("trapdoorBottom")) {
/* 2799 */             b = Blocks.field_150415_aT;
/* 2800 */             m = getTrapdoorMeta(2, orientation);
/* 2801 */           } else if (pt.isType("trapdoorLeft")) {
/* 2802 */             b = Blocks.field_150415_aT;
/* 2803 */             m = getTrapdoorMeta(3, orientation);
/*      */           }
/* 2805 */           else if (pt.isType("fencegateHorizontal")) {
/* 2806 */             b = Blocks.field_150396_be;
/* 2807 */             m = getFenceGateMeta(0, orientation);
/* 2808 */           } else if (pt.isType("fencegateVertical")) {
/* 2809 */             b = Blocks.field_150396_be;
/* 2810 */             m = getFenceGateMeta(1, orientation);
/*      */           }
/* 2812 */           else if (pt.isType("bedTop")) {
/* 2813 */             b = Blocks.field_150324_C;
/* 2814 */             m = getBedMeta(0, orientation) + 8;
/* 2815 */           } else if (pt.isType("bedRight")) {
/* 2816 */             b = Blocks.field_150324_C;
/* 2817 */             m = getBedMeta(1, orientation) + 8;
/* 2818 */           } else if (pt.isType("bedBottom")) {
/* 2819 */             b = Blocks.field_150324_C;
/* 2820 */             m = getBedMeta(2, orientation) + 8;
/* 2821 */           } else if (pt.isType("bedLeft")) {
/* 2822 */             b = Blocks.field_150324_C;
/* 2823 */             m = getBedMeta(3, orientation) + 8;
/*      */           }
/*      */           
/* 2826 */           if (b != null) {
/* 2827 */             bblocks.add(new BuildingBlock(p, b, m));
/* 2828 */             this.nbBlocksToPut += 1;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 2834 */     for (int i = 0; i < this.nbfloors; i++) {
/* 2835 */       for (int j = 0; j < this.length; j++) {
/* 2836 */         for (int k = 0; k < this.width; k++) {
/* 2837 */           PointType pt = this.plan[i][j][k];
/* 2838 */           if (pt.isType("tapestry")) {
/* 2839 */             org.millenaire.common.Point p = adjustForOrientation(x, y + i + this.startLevel, z, j - this.lengthOffset, k - this.widthOffset, orientation);
/* 2840 */             bblocks.add(new BuildingBlock(p, 0, 0, BuildingBlock.TAPESTRY));
/* 2841 */             this.nbBlocksToPut += 1;
/* 2842 */           } else if (pt.isType("indianstatue")) {
/* 2843 */             org.millenaire.common.Point p = adjustForOrientation(x, y + i + this.startLevel, z, j - this.lengthOffset, k - this.widthOffset, orientation);
/* 2844 */             bblocks.add(new BuildingBlock(p, 0, 0, BuildingBlock.INDIANSTATUE));
/* 2845 */             this.nbBlocksToPut += 1;
/* 2846 */           } else if (pt.isType("mayanstatue")) {
/* 2847 */             org.millenaire.common.Point p = adjustForOrientation(x, y + i + this.startLevel, z, j - this.lengthOffset, k - this.widthOffset, orientation);
/* 2848 */             bblocks.add(new BuildingBlock(p, 0, 0, BuildingBlock.MAYANSTATUE));
/* 2849 */             this.nbBlocksToPut += 1;
/* 2850 */           } else if (pt.isType("byzantineiconsmall")) {
/* 2851 */             org.millenaire.common.Point p = adjustForOrientation(x, y + i + this.startLevel, z, j - this.lengthOffset, k - this.widthOffset, orientation);
/* 2852 */             bblocks.add(new BuildingBlock(p, 0, 0, BuildingBlock.BYZANTINEICONSMALL));
/* 2853 */             this.nbBlocksToPut += 1;
/* 2854 */           } else if (pt.isType("byzantineiconmedium")) {
/* 2855 */             org.millenaire.common.Point p = adjustForOrientation(x, y + i + this.startLevel, z, j - this.lengthOffset, k - this.widthOffset, orientation);
/* 2856 */             bblocks.add(new BuildingBlock(p, 0, 0, BuildingBlock.BYZANTINEICONMEDIUM));
/* 2857 */             this.nbBlocksToPut += 1;
/* 2858 */           } else if (pt.isType("byzantineiconlarge")) {
/* 2859 */             org.millenaire.common.Point p = adjustForOrientation(x, y + i + this.startLevel, z, j - this.lengthOffset, k - this.widthOffset, orientation);
/* 2860 */             bblocks.add(new BuildingBlock(p, 0, 0, BuildingBlock.BYZANTINEICONLARGE));
/* 2861 */             this.nbBlocksToPut += 1;
/* 2862 */           } else if (pt.isType("oakspawn")) {
/* 2863 */             org.millenaire.common.Point p = adjustForOrientation(x, y + i + this.startLevel, z, j - this.lengthOffset, k - this.widthOffset, orientation);
/* 2864 */             bblocks.add(new BuildingBlock(p, 0, 0, BuildingBlock.OAKSPAWN));
/* 2865 */             this.nbBlocksToPut += 1;
/* 2866 */           } else if (pt.isType("pinespawn")) {
/* 2867 */             org.millenaire.common.Point p = adjustForOrientation(x, y + i + this.startLevel, z, j - this.lengthOffset, k - this.widthOffset, orientation);
/* 2868 */             bblocks.add(new BuildingBlock(p, 0, 0, BuildingBlock.PINESPAWN));
/* 2869 */             this.nbBlocksToPut += 1;
/* 2870 */           } else if (pt.isType("birchspawn")) {
/* 2871 */             org.millenaire.common.Point p = adjustForOrientation(x, y + i + this.startLevel, z, j - this.lengthOffset, k - this.widthOffset, orientation);
/* 2872 */             bblocks.add(new BuildingBlock(p, 0, 0, BuildingBlock.BIRCHSPAWN));
/* 2873 */             this.nbBlocksToPut += 1;
/* 2874 */           } else if (pt.isType("junglespawn")) {
/* 2875 */             org.millenaire.common.Point p = adjustForOrientation(x, y + i + this.startLevel, z, j - this.lengthOffset, k - this.widthOffset, orientation);
/* 2876 */             bblocks.add(new BuildingBlock(p, 0, 0, BuildingBlock.JUNGLESPAWN));
/* 2877 */             this.nbBlocksToPut += 1;
/* 2878 */           } else if (pt.isType("spawnerskeleton")) {
/* 2879 */             org.millenaire.common.Point p = adjustForOrientation(x, y + i + this.startLevel, z, j - this.lengthOffset, k - this.widthOffset, orientation);
/* 2880 */             bblocks.add(new BuildingBlock(p, 0, 0, BuildingBlock.SPAWNERSKELETON));
/* 2881 */             this.nbBlocksToPut += 1;
/* 2882 */           } else if (pt.isType("spawnerzombie")) {
/* 2883 */             org.millenaire.common.Point p = adjustForOrientation(x, y + i + this.startLevel, z, j - this.lengthOffset, k - this.widthOffset, orientation);
/* 2884 */             bblocks.add(new BuildingBlock(p, 0, 0, BuildingBlock.SPAWNERZOMBIE));
/* 2885 */             this.nbBlocksToPut += 1;
/* 2886 */           } else if (pt.isType("spawnerspider")) {
/* 2887 */             org.millenaire.common.Point p = adjustForOrientation(x, y + i + this.startLevel, z, j - this.lengthOffset, k - this.widthOffset, orientation);
/* 2888 */             bblocks.add(new BuildingBlock(p, 0, 0, BuildingBlock.SPAWNERSPIDER));
/* 2889 */             this.nbBlocksToPut += 1;
/* 2890 */           } else if (pt.isType("spawnercavespider")) {
/* 2891 */             org.millenaire.common.Point p = adjustForOrientation(x, y + i + this.startLevel, z, j - this.lengthOffset, k - this.widthOffset, orientation);
/* 2892 */             bblocks.add(new BuildingBlock(p, 0, 0, BuildingBlock.SPAWNERCAVESPIDER));
/* 2893 */             this.nbBlocksToPut += 1;
/* 2894 */           } else if (pt.isType("spawnercreeper")) {
/* 2895 */             org.millenaire.common.Point p = adjustForOrientation(x, y + i + this.startLevel, z, j - this.lengthOffset, k - this.widthOffset, orientation);
/* 2896 */             bblocks.add(new BuildingBlock(p, 0, 0, BuildingBlock.SPAWNERCREEPER));
/* 2897 */             this.nbBlocksToPut += 1;
/* 2898 */           } else if (pt.isType("spawnerblaze")) {
/* 2899 */             org.millenaire.common.Point p = adjustForOrientation(x, y + i + this.startLevel, z, j - this.lengthOffset, k - this.widthOffset, orientation);
/* 2900 */             bblocks.add(new BuildingBlock(p, 0, 0, BuildingBlock.SPAWNERBLAZE));
/* 2901 */             this.nbBlocksToPut += 1;
/* 2902 */           } else if (pt.isType("dispenserunknownpowder")) {
/* 2903 */             org.millenaire.common.Point p = adjustForOrientation(x, y + i + this.startLevel, z, j - this.lengthOffset, k - this.widthOffset, orientation);
/* 2904 */             bblocks.add(new BuildingBlock(p, 0, 0, BuildingBlock.DISPENDERUNKNOWNPOWDER));
/* 2905 */             this.nbBlocksToPut += 1;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 2911 */     for (int i = 0; i < this.nbfloors; i++) {
/* 2912 */       for (int j = 0; j < this.length; j++) {
/* 2913 */         for (int k = 0; k < this.width; k++) {
/* 2914 */           PointType pt = this.plan[i][j][k];
/* 2915 */           if (pt.isType("mainchest")) {
/* 2916 */             org.millenaire.common.Point p = adjustForOrientation(x, y + i + this.startLevel, z, j - this.lengthOffset, k - this.widthOffset, orientation);
/* 2917 */             location.chestPos = p;
/* 2918 */             bblocks.add(new BuildingBlock(p, org.millenaire.common.forge.Mill.lockedChest, 1));
/* 2919 */             this.nbBlocksToPut += 1;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 2925 */     if (location.chestPos == null) {
/* 2926 */       location.chestPos = ((BuildingBlock)bblocks.get(bblocks.size() - 1)).p;
/*      */     }
/*      */     
/* 2929 */     setMetadata(bblocks);
/*      */     
/* 2931 */     if (location.sleepingPos == null) {
/* 2932 */       location.sleepingPos = location.chestPos;
/*      */     }
/*      */     
/* 2935 */     java.util.HashMap<org.millenaire.common.Point, BuildingBlock> bbmap = new java.util.HashMap();
/*      */     
/* 2937 */     boolean[] toDelete = new boolean[bblocks.size()];
/*      */     
/* 2939 */     for (int i = 0; i < bblocks.size(); i++) {
/* 2940 */       BuildingBlock bb = (BuildingBlock)bblocks.get(i);
/*      */       
/*      */       int special;
/*      */       net.minecraft.block.Block block;
/*      */       int bmeta;
/*      */       int special;
/* 2946 */       if (bbmap.containsKey(bb.p)) {
/* 2947 */         net.minecraft.block.Block block = ((BuildingBlock)bbmap.get(bb.p)).block;
/* 2948 */         int bmeta = ((BuildingBlock)bbmap.get(bb.p)).meta;
/* 2949 */         special = ((BuildingBlock)bbmap.get(bb.p)).special;
/*      */       } else {
/* 2951 */         block = org.millenaire.common.core.MillCommonUtilities.getBlock(world, bb.p);
/* 2952 */         bmeta = org.millenaire.common.core.MillCommonUtilities.getBlockMeta(world, bb.p);
/* 2953 */         special = 0;
/*      */       }
/*      */       
/* 2956 */       if (((block == bb.block) && (bmeta == bb.meta) && (special == 0)) || ((block == Blocks.field_150349_c) && (bb.block == Blocks.field_150346_d) && (bb.special == 0))) {
/* 2957 */         toDelete[i] = true;
/* 2958 */       } else if ((bb.special == BuildingBlock.CLEARTREE) && (block != Blocks.field_150364_r) && (block != Blocks.field_150362_t)) {
/* 2959 */         toDelete[i] = true;
/* 2960 */       } else if ((bb.special == BuildingBlock.CLEARGROUND) && ((block == null) || (block == Blocks.field_150350_a))) {
/* 2961 */         toDelete[i] = true;
/* 2962 */       } else if ((bb.special == BuildingBlock.PRESERVEGROUNDDEPTH) && (org.millenaire.common.core.MillCommonUtilities.getBlockIdValidGround(block, false) == block)) {
/* 2963 */         toDelete[i] = true;
/* 2964 */       } else if ((bb.special == BuildingBlock.PRESERVEGROUNDSURFACE) && (org.millenaire.common.core.MillCommonUtilities.getBlockIdValidGround(block, true) == block)) {
/* 2965 */         toDelete[i] = true;
/*      */       } else {
/* 2967 */         bbmap.put(bb.p, bb);
/* 2968 */         toDelete[i] = false;
/*      */       }
/*      */     }
/*      */     
/* 2972 */     for (int i = toDelete.length - 1; i >= 0; i--) {
/* 2973 */       if (toDelete[i] != 0) {
/* 2974 */         bblocks.remove(i);
/*      */       }
/*      */     }
/*      */     
/* 2978 */     BuildingBlock[] abblocks = new BuildingBlock[bblocks.size()];
/*      */     
/* 2980 */     for (int i = 0; i < bblocks.size(); i++) {
/* 2981 */       abblocks[i] = ((BuildingBlock)bblocks.get(i));
/*      */     }
/*      */     
/* 2984 */     if (abblocks.length == 0) {
/* 2985 */       org.millenaire.common.MLN.error(this, "BBlocks size is zero.");
/*      */     }
/*      */     
/* 2988 */     return abblocks;
/*      */   }
/*      */   
/*      */   public org.millenaire.common.Culture getCulture()
/*      */   {
/* 2993 */     return this.culture;
/*      */   }
/*      */   
/*      */   private int getDoorMeta(int direction, int orientation) {
/* 2997 */     int faces = (direction + 4 - orientation) % 4;
/*      */     
/* 2999 */     if (faces == 0)
/* 3000 */       return 0;
/* 3001 */     if (faces == 1)
/* 3002 */       return 1;
/* 3003 */     if (faces == 2) {
/* 3004 */       return 2;
/*      */     }
/* 3006 */     return 3;
/*      */   }
/*      */   
/*      */ 
/*      */   public java.util.List<String> getFemaleResident()
/*      */   {
/* 3012 */     return this.femaleResident;
/*      */   }
/*      */   
/*      */   private int getFenceGateMeta(int direction, int orientation) {
/* 3016 */     int faces = (direction + 4 - orientation) % 4;
/*      */     
/* 3018 */     if (faces == 0)
/* 3019 */       return 1;
/* 3020 */     if (faces == 1)
/* 3021 */       return 0;
/* 3022 */     if (faces == 2) {
/* 3023 */       return 1;
/*      */     }
/* 3025 */     return 0;
/*      */   }
/*      */   
/*      */   public String getFullDisplayName()
/*      */   {
/* 3030 */     String name = this.nativeName;
/* 3031 */     if (getGameName().length() > 0) {
/* 3032 */       name = name + " (" + getGameName() + ")";
/*      */     }
/* 3034 */     return name;
/*      */   }
/*      */   
/*      */   public String getGameName()
/*      */   {
/* 3039 */     if (this.culture.canReadBuildingNames()) {
/* 3040 */       return this.culture.getBuildingGameName(this);
/*      */     }
/* 3042 */     return "";
/*      */   }
/*      */   
/*      */   public String getGameNameKey() {
/* 3046 */     return "_buildingGame:" + this.culture.key + ":" + this.buildingKey + ":" + this.variation + ":" + this.level;
/*      */   }
/*      */   
/*      */   public java.util.List<String> getMaleResident()
/*      */   {
/* 3051 */     return this.maleResident;
/*      */   }
/*      */   
/*      */   public String getNativeName()
/*      */   {
/* 3056 */     return this.nativeName;
/*      */   }
/*      */   
/*      */   private int getOrientedBlockMeta(int direction, int orientation)
/*      */   {
/* 3061 */     int faces = (direction + 4 - orientation) % 4;
/*      */     
/* 3063 */     if (faces == 0)
/* 3064 */       return 1;
/* 3065 */     if (faces == 1)
/* 3066 */       return 0;
/* 3067 */     if (faces == 2) {
/* 3068 */       return 1;
/*      */     }
/* 3070 */     return 0;
/*      */   }
/*      */   
/*      */ 
/*      */   private int getSignOrLadderMeta(int direction, int orientation)
/*      */   {
/* 3076 */     int faces = (direction + orientation) % 4;
/*      */     
/* 3078 */     if (faces == 0)
/* 3079 */       return 5;
/* 3080 */     if (faces == 1)
/* 3081 */       return 2;
/* 3082 */     if (faces == 2) {
/* 3083 */       return 4;
/*      */     }
/* 3085 */     return 3;
/*      */   }
/*      */   
/*      */ 
/*      */   private int getStairMeta(int direction, int orientation)
/*      */   {
/* 3091 */     int faces = (direction + 4 - orientation) % 4;
/*      */     
/* 3093 */     if (faces == 0)
/* 3094 */       return 1;
/* 3095 */     if (faces == 1)
/* 3096 */       return 3;
/* 3097 */     if (faces == 2) {
/* 3098 */       return 0;
/*      */     }
/* 3100 */     return 2;
/*      */   }
/*      */   
/*      */   private int getTrapdoorMeta(int direction, int orientation)
/*      */   {
/* 3105 */     int faces = (direction + 4 - orientation) % 4;
/*      */     
/* 3107 */     if (faces == 0)
/* 3108 */       return 3;
/* 3109 */     if (faces == 1)
/* 3110 */       return 0;
/* 3111 */     if (faces == 2) {
/* 3112 */       return 2;
/*      */     }
/* 3114 */     return 1;
/*      */   }
/*      */   
/*      */ 
/*      */   private int getWoodMeta(int direction, int orientation)
/*      */   {
/* 3120 */     int faces = (direction + 4 - orientation) % 4;
/*      */     
/* 3122 */     if (faces == 0)
/* 3123 */       return 8;
/* 3124 */     if (faces == 1)
/* 3125 */       return 4;
/* 3126 */     if (faces == 2) {
/* 3127 */       return 8;
/*      */     }
/* 3129 */     return 4;
/*      */   }
/*      */   
/*      */   private int guessSignMeta(java.util.List<BuildingBlock> bblocks, org.millenaire.common.Point p)
/*      */   {
/* 3134 */     boolean westOpen = true;boolean eastOpen = true;boolean northOpen = true;boolean southOpen = true;
/* 3135 */     int m = 0;
/*      */     
/* 3137 */     org.millenaire.common.Point west = p.getRelative(-1.0D, 0.0D, 0.0D);
/* 3138 */     org.millenaire.common.Point east = p.getRelative(1.0D, 0.0D, 0.0D);
/* 3139 */     org.millenaire.common.Point south = p.getRelative(0.0D, 0.0D, -1.0D);
/* 3140 */     org.millenaire.common.Point north = p.getRelative(0.0D, 0.0D, 1.0D);
/*      */     
/* 3142 */     for (BuildingBlock block : bblocks)
/*      */     {
/* 3144 */       if ((block.p.sameBlock(west)) && (isBlockOpaqueCube(block.block))) {
/* 3145 */         westOpen = false;
/* 3146 */       } else if ((block.p.sameBlock(east)) && (isBlockOpaqueCube(block.block))) {
/* 3147 */         eastOpen = false;
/* 3148 */       } else if ((block.p.sameBlock(south)) && (isBlockOpaqueCube(block.block))) {
/* 3149 */         southOpen = false;
/* 3150 */       } else if ((block.p.sameBlock(north)) && (isBlockOpaqueCube(block.block))) {
/* 3151 */         northOpen = false;
/*      */       }
/*      */     }
/*      */     
/* 3155 */     if (!northOpen) {
/* 3156 */       if (org.millenaire.common.MLN.LogBuildingPlan >= 3) {
/* 3157 */         org.millenaire.common.MLN.debug(this, this.planName + ": Putting sign again north wall");
/*      */       }
/* 3159 */       m = 2;
/* 3160 */     } else if (!southOpen) {
/* 3161 */       if (org.millenaire.common.MLN.LogBuildingPlan >= 3) {
/* 3162 */         org.millenaire.common.MLN.debug(this, this.planName + ": Putting sign again south wall");
/*      */       }
/* 3164 */       m = 3;
/* 3165 */     } else if (!eastOpen) {
/* 3166 */       if (org.millenaire.common.MLN.LogBuildingPlan >= 3) {
/* 3167 */         org.millenaire.common.MLN.debug(this, this.planName + ": Putting sign again east wall");
/*      */       }
/* 3169 */       m = 4;
/* 3170 */     } else if (!westOpen) {
/* 3171 */       if (org.millenaire.common.MLN.LogBuildingPlan >= 3) {
/* 3172 */         org.millenaire.common.MLN.debug(this, this.planName + ": Putting sign again west wall");
/*      */       }
/* 3174 */       m = 5;
/*      */     } else {
/* 3176 */       if (org.millenaire.common.MLN.LogBuildingPlan >= 1) {
/* 3177 */         org.millenaire.common.MLN.major(this, this.planName + ": No idea where to put sign.");
/*      */       }
/* 3179 */       m = 2;
/*      */     }
/* 3181 */     return m;
/*      */   }
/*      */   
/*      */   private void initialiseConfig(BuildingPlan parent) {
/* 3185 */     if (parent == null) {
/* 3186 */       this.max = 1;
/* 3187 */       this.priority = 1;
/* 3188 */       this.priorityMoveIn = 10;
/* 3189 */       this.nativeName = null;
/* 3190 */       this.areaToClear = 1;
/* 3191 */       this.startLevel = 0;
/* 3192 */       this.buildingOrientation = 1;
/* 3193 */       this.signOrder = new int[] { 0 };
/* 3194 */       this.tags = new java.util.ArrayList();
/* 3195 */       this.shop = null;
/* 3196 */       this.minDistance = 0.0F;
/* 3197 */       this.maxDistance = 1.0F;
/* 3198 */       this.reputation = 0;
/* 3199 */       this.price = 0;
/* 3200 */       this.subBuildings = new java.util.ArrayList();
/* 3201 */       this.startingSubBuildings = new java.util.ArrayList();
/* 3202 */       this.startingGoods = new java.util.ArrayList();
/* 3203 */       this.showTownHallSigns = true;
/* 3204 */       this.rebuildPath = true;
/*      */     } else {
/* 3206 */       this.max = parent.max;
/* 3207 */       this.priority = parent.priority;
/* 3208 */       this.priorityMoveIn = parent.priorityMoveIn;
/* 3209 */       this.nativeName = parent.nativeName;
/* 3210 */       this.areaToClear = parent.areaToClear;
/* 3211 */       this.startLevel = parent.startLevel;
/* 3212 */       this.buildingOrientation = parent.buildingOrientation;
/* 3213 */       this.signOrder = parent.signOrder;
/* 3214 */       this.tags = new java.util.ArrayList(parent.tags);
/* 3215 */       this.maleResident = parent.maleResident;
/* 3216 */       this.femaleResident = parent.femaleResident;
/* 3217 */       this.shop = parent.shop;
/* 3218 */       this.width = parent.width;
/* 3219 */       this.length = parent.length;
/* 3220 */       this.minDistance = parent.minDistance;
/* 3221 */       this.maxDistance = parent.maxDistance;
/* 3222 */       this.reputation = parent.reputation;
/* 3223 */       this.isgift = parent.isgift;
/* 3224 */       this.price = parent.price;
/* 3225 */       this.pathLevel = parent.pathLevel;
/* 3226 */       this.pathWidth = parent.pathWidth;
/* 3227 */       this.subBuildings = new java.util.ArrayList(parent.subBuildings);
/* 3228 */       this.startingSubBuildings = new java.util.ArrayList();
/* 3229 */       this.startingGoods = new java.util.ArrayList();
/* 3230 */       this.parent = parent;
/* 3231 */       if (org.millenaire.common.MLN.LogBuildingPlan >= 2) {
/* 3232 */         String s = "";
/* 3233 */         for (String s2 : this.subBuildings) {
/* 3234 */           s = s + s2 + " ";
/*      */         }
/* 3236 */         if (s.length() > 0) {
/* 3237 */           org.millenaire.common.MLN.minor(this, "Copied sub-buildings from parent: " + s);
/*      */         }
/*      */       }
/* 3240 */       this.showTownHallSigns = parent.showTownHallSigns;
/* 3241 */       this.exploreTag = parent.exploreTag;
/* 3242 */       this.irrigation = parent.irrigation;
/*      */     }
/*      */   }
/*      */   
/*      */   public boolean isBlockOpaqueCube(net.minecraft.block.Block block) {
/* 3247 */     return org.millenaire.common.core.MillCommonUtilities.isBlockOpaqueCube(block);
/*      */   }
/*      */   
/*      */   public boolean isBuildable(net.minecraft.block.Block block) {
/* 3251 */     return (block == Blocks.field_150350_a) || (block == Blocks.field_150362_t) || (block == Blocks.field_150364_r) || (block == Blocks.field_150338_P) || (block == Blocks.field_150337_Q) || (block == Blocks.field_150328_O) || (block == Blocks.field_150327_N);
/*      */   }
/*      */   
/*      */   public boolean isSubBuilding()
/*      */   {
/* 3256 */     return TYPE_SUBBUILDING.equals(this.type);
/*      */   }
/*      */   
/*      */   public boolean mapIsOpaqueBlock(java.util.Map<org.millenaire.common.Point, BuildingBlock> map, org.millenaire.common.Point p) {
/* 3260 */     return (map.containsKey(p)) && (isBlockOpaqueCube(((BuildingBlock)map.get(p)).block));
/*      */   }
/*      */   
/*      */   public boolean mapIsStairBlock(java.util.Map<org.millenaire.common.Point, BuildingBlock> map, org.millenaire.common.Point p) {
/* 3264 */     if (!map.containsKey(p)) {
/* 3265 */       return false;
/*      */     }
/*      */     
/* 3268 */     net.minecraft.block.Block block = ((BuildingBlock)map.get(p)).block;
/*      */     
/* 3270 */     return (block == Blocks.field_150446_ar) || (block == Blocks.field_150476_ad);
/*      */   }
/*      */   
/*      */   private void readConfigLine(java.io.File file, String line, boolean importPlan)
/*      */   {
/* 3275 */     String[] configs = line.split(";", -1);
/*      */     
/* 3277 */     for (String config : configs) {
/* 3278 */       if (config.split(":").length == 2) {
/* 3279 */         String key = config.split(":")[0].toLowerCase();
/* 3280 */         String value = config.split(":")[1];
/*      */         
/* 3282 */         if (key.equalsIgnoreCase("max")) {
/* 3283 */           this.max = Integer.parseInt(value);
/* 3284 */         } else if (key.equalsIgnoreCase("priority")) {
/* 3285 */           this.priority = Integer.parseInt(value);
/* 3286 */         } else if (key.equalsIgnoreCase("moveinpriority")) {
/* 3287 */           this.priorityMoveIn = Integer.parseInt(value);
/* 3288 */         } else if ((key.equalsIgnoreCase("french")) || (key.equalsIgnoreCase("native"))) {
/* 3289 */           this.nativeName = value;
/* 3290 */         } else if ((key.equalsIgnoreCase("english")) || (key.startsWith("name_"))) {
/* 3291 */           this.names.put(key, value);
/* 3292 */         } else if (key.equalsIgnoreCase("around")) {
/* 3293 */           this.areaToClear = Integer.parseInt(value);
/* 3294 */         } else if (key.equalsIgnoreCase("startLevel")) {
/* 3295 */           this.startLevel = Integer.parseInt(value);
/* 3296 */         } else if (key.equalsIgnoreCase("orientation")) {
/* 3297 */           this.buildingOrientation = Integer.parseInt(value);
/* 3298 */         } else if (key.equalsIgnoreCase("pathlevel")) {
/* 3299 */           this.pathLevel = Integer.parseInt(value);
/* 3300 */           this.rebuildPath = true;
/* 3301 */         } else if (key.equalsIgnoreCase("rebuildpath")) {
/* 3302 */           this.rebuildPath = Boolean.parseBoolean(value);
/* 3303 */         } else if (key.equalsIgnoreCase("isgift")) {
/* 3304 */           this.isgift = Boolean.parseBoolean(value);
/* 3305 */         } else if (key.equalsIgnoreCase("pathwidth")) {
/* 3306 */           this.pathWidth = Integer.parseInt(value);
/* 3307 */         } else if (key.equalsIgnoreCase("reputation")) {
/*      */           try {
/* 3309 */             this.reputation = org.millenaire.common.core.MillCommonUtilities.readInteger(value);
/*      */           } catch (Exception e) {
/* 3311 */             this.reputation = 0;
/* 3312 */             org.millenaire.common.MLN.error(null, "Error when reading reputation line in " + file.getName() + ": " + line + " : " + e.getMessage());
/*      */           }
/* 3314 */         } else if (key.equalsIgnoreCase("price")) {
/*      */           try {
/* 3316 */             this.price = org.millenaire.common.core.MillCommonUtilities.readInteger(value);
/*      */           } catch (Exception e) {
/* 3318 */             this.price = 0;
/* 3319 */             org.millenaire.common.MLN.error(this, "Error when reading reputation line in " + file.getName() + ": " + line + " : " + e.getMessage());
/*      */           }
/* 3321 */         } else if (key.equalsIgnoreCase("length")) {
/* 3322 */           this.length = Integer.parseInt(value);
/* 3323 */         } else if (key.equalsIgnoreCase("width")) {
/* 3324 */           this.width = Integer.parseInt(value);
/* 3325 */         } else if ((!importPlan) && (key.equalsIgnoreCase("male"))) {
/* 3326 */           if (this.culture.villagerTypes.containsKey(value.toLowerCase())) {
/* 3327 */             this.maleResident.add(value.toLowerCase());
/*      */           } else {
/* 3329 */             org.millenaire.common.MLN.error(this, "Attempted to load unknown male villager: " + value);
/*      */           }
/* 3331 */         } else if ((!importPlan) && (key.equalsIgnoreCase("female"))) {
/* 3332 */           if (this.culture.villagerTypes.containsKey(value.toLowerCase())) {
/* 3333 */             this.femaleResident.add(value.toLowerCase());
/*      */           } else {
/* 3335 */             org.millenaire.common.MLN.error(this, "Attempted to load unknown female villager: " + value);
/*      */           }
/* 3337 */         } else if (key.equalsIgnoreCase("exploretag")) {
/* 3338 */           this.exploreTag = value.toLowerCase();
/* 3339 */         } else if (key.equalsIgnoreCase("requiredTag")) {
/* 3340 */           this.requiredTag = value.toLowerCase();
/* 3341 */         } else if (key.equalsIgnoreCase("irrigation")) {
/* 3342 */           this.irrigation = Integer.parseInt(value);
/* 3343 */         } else if ((!importPlan) && (key.equalsIgnoreCase("shop"))) {
/* 3344 */           if (this.culture != null)
/*      */           {
/* 3346 */             if ((this.culture.shopBuys.containsKey(value)) || (this.culture.shopSells.containsKey(value)) || (this.culture.shopBuysOptional.containsKey(value))) {
/* 3347 */               this.shop = value;
/*      */             } else {
/* 3349 */               org.millenaire.common.MLN.error(this, "Undefined shop type: " + value);
/*      */             }
/*      */           }
/* 3352 */         } else if (key.equalsIgnoreCase("minDistance")) {
/* 3353 */           this.minDistance = (Float.parseFloat(value) / 100.0F);
/* 3354 */         } else if (key.equalsIgnoreCase("maxDistance")) {
/* 3355 */           this.maxDistance = (Float.parseFloat(value) / 100.0F);
/* 3356 */         } else if (key.equalsIgnoreCase("signs")) {
/* 3357 */           String[] temp = value.split(",");
/* 3358 */           if (temp[0].length() > 0) {
/* 3359 */             this.signOrder = new int[temp.length];
/* 3360 */             for (int i = 0; i < temp.length; i++) {
/* 3361 */               this.signOrder[i] = Integer.parseInt(temp[i]);
/*      */             }
/*      */           }
/* 3364 */         } else if (key.equalsIgnoreCase("tag")) {
/* 3365 */           this.tags.add(value.toLowerCase());
/* 3366 */         } else if (key.equalsIgnoreCase("subbuilding")) {
/* 3367 */           this.subBuildings.add(value);
/* 3368 */         } else if (key.equalsIgnoreCase("startingsubbuilding")) {
/* 3369 */           this.startingSubBuildings.add(value);
/* 3370 */         } else if ((!importPlan) && (key.equalsIgnoreCase("startinggood"))) {
/* 3371 */           String[] temp = value.split(",");
/* 3372 */           if (temp.length != 4) {
/* 3373 */             org.millenaire.common.MLN.error(this, "Error when reading starting good: expected four fields, found " + temp.length + ": " + value);
/*      */           }
/*      */           else {
/* 3376 */             String s = temp[0];
/* 3377 */             if (!org.millenaire.common.item.Goods.goodsName.containsKey(s)) {
/* 3378 */               org.millenaire.common.MLN.error(this, "Error when reading starting good: unknown good: " + s);
/*      */             } else {
/* 3380 */               StartingGood sg = new StartingGood((org.millenaire.common.InvItem)org.millenaire.common.item.Goods.goodsName.get(s), Double.parseDouble(temp[1]), Integer.parseInt(temp[2]), Integer.parseInt(temp[3]));
/* 3381 */               this.startingGoods.add(sg);
/*      */             }
/*      */             
/*      */           }
/*      */         }
/* 3386 */         else if (key.equalsIgnoreCase("type")) {
/* 3387 */           this.type = value.toLowerCase();
/* 3388 */         } else if (key.equalsIgnoreCase("showtownhallsigns")) {
/* 3389 */           this.showTownHallSigns = Boolean.parseBoolean(value);
/* 3390 */         } else if (!importPlan) {
/* 3391 */           org.millenaire.common.MLN.error(this, "Could not recognise key on line: " + config);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 3396 */     if (TYPE_SUBBUILDING.equals(this.type)) {
/* 3397 */       this.max = 0;
/*      */     }
/*      */     
/* 3400 */     if (this.priority < 1) {
/* 3401 */       org.millenaire.common.MLN.error(this, "Null or negative weight found in config!");
/*      */     }
/*      */     
/* 3404 */     if (org.millenaire.common.MLN.LogBuildingPlan >= 3) {
/* 3405 */       String s = "";
/* 3406 */       for (String s2 : this.subBuildings) {
/* 3407 */         s = s + s2 + " ";
/*      */       }
/* 3409 */       if (s.length() > 0) {
/* 3410 */         org.millenaire.common.MLN.minor(this, "Sub-buildings after read: " + s);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public void referenceBuildingPoints(net.minecraft.world.World worldObj, Building building, BuildingLocation location)
/*      */   {
/* 3417 */     int x = location.pos.getiX();
/* 3418 */     int y = location.pos.getiY();
/* 3419 */     int z = location.pos.getiZ();
/* 3420 */     int orientation = location.orientation;
/*      */     
/* 3422 */     int signNb = 0;
/* 3423 */     for (int i = 0; i < this.signOrder.length; i++) {
/* 3424 */       building.getResManager().signs.add(null);
/*      */     }
/*      */     
/* 3427 */     for (int i = 0; i < this.nbfloors; i++) {
/* 3428 */       for (int j = 0; j < this.length; j++) {
/* 3429 */         for (int k = 0; k < this.width; k++) {
/* 3430 */           PointType pt = this.plan[i][j][k];
/*      */           
/* 3432 */           org.millenaire.common.Point p = adjustForOrientation(x, y + i + this.startLevel, z, j - this.lengthOffset, k - this.widthOffset, orientation);
/*      */           
/* 3434 */           if (pt.isType("soil")) {
/* 3435 */             building.getResManager().addSoilPoint("wheat", p);
/* 3436 */           } else if (pt.isType("ricesoil")) {
/* 3437 */             building.getResManager().addSoilPoint("rice", p);
/* 3438 */           } else if (pt.isType("turmericsoil")) {
/* 3439 */             building.getResManager().addSoilPoint("turmeric", p);
/* 3440 */           } else if (pt.isType("maizesoil")) {
/* 3441 */             building.getResManager().addSoilPoint("maize", p);
/* 3442 */           } else if (pt.isType("carrotsoil")) {
/* 3443 */             building.getResManager().addSoilPoint("carrot", p);
/* 3444 */           } else if (pt.isType("potatosoil")) {
/* 3445 */             building.getResManager().addSoilPoint("potato", p);
/* 3446 */           } else if (pt.isType("sugarcanesoil")) {
/* 3447 */             if (!building.getResManager().sugarcanesoils.contains(p)) {
/* 3448 */               building.getResManager().sugarcanesoils.add(p);
/*      */             }
/* 3450 */           } else if (pt.isType("netherwartsoil")) {
/* 3451 */             if (!building.getResManager().netherwartsoils.contains(p)) {
/* 3452 */               building.getResManager().netherwartsoils.add(p);
/*      */             }
/* 3454 */           } else if (pt.isType("vinesoil")) {
/* 3455 */             building.getResManager().addSoilPoint("vine", p);
/* 3456 */           } else if (pt.isType("cacaospot")) {
/* 3457 */             building.getResManager().addSoilPoint("cacao", p);
/* 3458 */           } else if (pt.isType("silkwormblock")) {
/* 3459 */             if (!building.getResManager().silkwormblock.contains(p)) {
/* 3460 */               building.getResManager().silkwormblock.add(p);
/*      */             }
/* 3462 */           } else if (pt.isType("stall")) {
/* 3463 */             if (!building.getResManager().stalls.contains(p)) {
/* 3464 */               building.getResManager().stalls.add(p);
/*      */             }
/* 3466 */           } else if (pt.isType("oakspawn")) {
/* 3467 */             if (!building.getResManager().woodspawn.contains(p)) {
/* 3468 */               building.getResManager().woodspawn.add(p);
/*      */             }
/* 3470 */           } else if (pt.isType("pinespawn")) {
/* 3471 */             if (!building.getResManager().woodspawn.contains(p)) {
/* 3472 */               building.getResManager().woodspawn.add(p);
/*      */             }
/* 3474 */           } else if (pt.isType("birchspawn")) {
/* 3475 */             if (!building.getResManager().woodspawn.contains(p)) {
/* 3476 */               building.getResManager().woodspawn.add(p);
/*      */             }
/* 3478 */           } else if (pt.isType("junglespawn")) {
/* 3479 */             if (!building.getResManager().woodspawn.contains(p)) {
/* 3480 */               building.getResManager().woodspawn.add(p);
/*      */             }
/* 3482 */           } else if (pt.isType("brickspot")) {
/* 3483 */             if (!building.getResManager().brickspot.contains(p)) {
/* 3484 */               building.getResManager().brickspot.add(p);
/*      */             }
/* 3486 */           } else if (pt.isType("chickenspawn")) {
/* 3487 */             building.getResManager().addSpawnPoint("Chicken", p);
/* 3488 */           } else if (pt.isType("cowspawn")) {
/* 3489 */             building.getResManager().addSpawnPoint("Cow", p);
/* 3490 */           } else if (pt.isType("pigspawn")) {
/* 3491 */             building.getResManager().addSpawnPoint("Pig", p);
/* 3492 */           } else if (pt.isType("squidspawn")) {
/* 3493 */             building.getResManager().addSpawnPoint("Squid", p);
/* 3494 */           } else if (pt.isType("sheepspawn")) {
/* 3495 */             building.getResManager().addSpawnPoint("Sheep", p);
/* 3496 */           } else if (pt.isType("wolfspawn")) {
/* 3497 */             building.getResManager().addSpawnPoint("Wolf", p);
/* 3498 */           } else if (pt.isType("stonesource")) {
/* 3499 */             building.getResManager().addSourcePoint(Blocks.field_150348_b, p);
/* 3500 */           } else if (pt.isType("sandsource")) {
/* 3501 */             building.getResManager().addSourcePoint(Blocks.field_150354_m, p);
/* 3502 */           } else if (pt.isType("sandstonesource")) {
/* 3503 */             building.getResManager().addSourcePoint(Blocks.field_150322_A, p);
/* 3504 */           } else if (pt.isType("claysource")) {
/* 3505 */             building.getResManager().addSourcePoint(Blocks.field_150435_aG, p);
/* 3506 */           } else if (pt.isType("gravelsource")) {
/* 3507 */             building.getResManager().addSourcePoint(Blocks.field_150351_n, p);
/* 3508 */           } else if (pt.isType("spawnerskeleton")) {
/* 3509 */             building.getResManager().addMobSpawnerPoint("Skeleton", p);
/* 3510 */           } else if (pt.isType("spawnerzombie")) {
/* 3511 */             building.getResManager().addMobSpawnerPoint("Zombie", p);
/* 3512 */           } else if (pt.isType("spawnerspider")) {
/* 3513 */             building.getResManager().addMobSpawnerPoint("Spider", p);
/* 3514 */           } else if (pt.isType("spawnercavespider")) {
/* 3515 */             building.getResManager().addMobSpawnerPoint("CaveSpider", p);
/* 3516 */           } else if (pt.isType("spawnercreeper")) {
/* 3517 */             building.getResManager().addMobSpawnerPoint("Creeper", p);
/* 3518 */           } else if (pt.isType("dispenserunknownpowder")) {
/* 3519 */             if (!building.getResManager().dispenderUnknownPowder.contains(p)) {
/* 3520 */               building.getResManager().dispenderUnknownPowder.add(p);
/*      */             }
/* 3522 */           } else if (pt.isType("fishingspot")) {
/* 3523 */             if (!building.getResManager().fishingspots.contains(p)) {
/* 3524 */               building.getResManager().fishingspots.add(p);
/*      */             }
/* 3526 */           } else if (pt.isType("healingspot")) {
/* 3527 */             if (!building.getResManager().healingspots.contains(p)) {
/* 3528 */               building.getResManager().healingspots.add(p);
/*      */             }
/* 3530 */           } else if ((pt.isType("lockedchest")) || (pt.isType("mainchest"))) {
/* 3531 */             if (!building.getResManager().chests.contains(p)) {
/* 3532 */               building.getResManager().chests.add(p);
/*      */             }
/* 3534 */           } else if (pt.isType("furnace")) {
/* 3535 */             if (!building.getResManager().furnaces.contains(p)) {
/* 3536 */               building.getResManager().furnaces.add(p);
/*      */             }
/* 3538 */           } else if (pt.isType("brewingstand")) {
/* 3539 */             if (!building.getResManager().brewingStands.contains(p)) {
/* 3540 */               building.getResManager().brewingStands.add(p);
/*      */             }
/* 3542 */           } else if (pt.isType("sleepingPos")) {
/* 3543 */             building.getResManager().setSleepingPos(p);
/* 3544 */           } else if (pt.isType("sellingPos")) {
/* 3545 */             building.getResManager().setSellingPos(p);
/* 3546 */           } else if (pt.isType("craftingPos")) {
/* 3547 */             building.getResManager().setCraftingPos(p);
/* 3548 */           } else if (pt.isType("defendingPos")) {
/* 3549 */             building.getResManager().setDefendingPos(p);
/* 3550 */           } else if (pt.isType("shelterPos")) {
/* 3551 */             building.getResManager().setShelterPos(p);
/* 3552 */           } else if (pt.isType("pathStartPos")) {
/* 3553 */             building.getResManager().setPathStartPos(p);
/* 3554 */           } else if (pt.isType("leasurePos")) {
/* 3555 */             building.getResManager().setLeasurePos(p);
/* 3556 */           } else if ((pt.isType("signwallGuess")) || (pt.isType("signwallTop")) || (pt.isType("signwallBottom")) || (pt.isType("signwallLeft")) || (pt.isType("signwallRight"))) {
/* 3557 */             building.getResManager().signs.set(this.signOrder[signNb], p);
/* 3558 */             signNb++;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public void setMetadata(java.util.List<BuildingBlock> bblocks) {
/* 3566 */     java.util.List<BuildingBlock> stairs = new java.util.ArrayList();
/* 3567 */     java.util.List<BuildingBlock> ladders = new java.util.ArrayList();
/* 3568 */     java.util.List<BuildingBlock> doors = new java.util.ArrayList();
/*      */     
/* 3570 */     java.util.HashMap<org.millenaire.common.Point, BuildingBlock> map = new java.util.HashMap();
/*      */     
/* 3572 */     for (BuildingBlock block : bblocks) {
/* 3573 */       map.put(block.p, block);
/* 3574 */       if ((block.block == Blocks.field_150468_ap) && (block.meta == -1)) {
/* 3575 */         ladders.add(block);
/* 3576 */       } else if (block.block == Blocks.field_150466_ao) {
/* 3577 */         doors.add(block);
/* 3578 */       } else if (((block.block == Blocks.field_150446_ar) || (block.block == Blocks.field_150476_ad)) && (block.meta == -1)) {
/* 3579 */         block.meta = -1;
/* 3580 */         stairs.add(block);
/*      */       }
/*      */     }
/*      */     
/* 3584 */     boolean[] northValid = new boolean[ladders.size()];
/* 3585 */     boolean[] southValid = new boolean[ladders.size()];
/* 3586 */     boolean[] westValid = new boolean[ladders.size()];
/* 3587 */     boolean[] eastValid = new boolean[ladders.size()];
/*      */     
/* 3589 */     int i = 0;
/* 3590 */     for (BuildingBlock ladder : ladders) {
/* 3591 */       northValid[i] = mapIsOpaqueBlock(map, ladder.p.getNorth());
/* 3592 */       southValid[i] = mapIsOpaqueBlock(map, ladder.p.getSouth());
/* 3593 */       westValid[i] = mapIsOpaqueBlock(map, ladder.p.getWest());
/* 3594 */       eastValid[i] = mapIsOpaqueBlock(map, ladder.p.getEast());
/*      */       
/*      */ 
/*      */ 
/* 3598 */       if ((northValid[i] != 0) && (southValid[i] == 0) && (westValid[i] == 0) && (eastValid[i] == 0)) {
/* 3599 */         ladder.meta = 5;
/*      */       }
/* 3601 */       if ((northValid[i] == 0) && (southValid[i] != 0) && (westValid[i] == 0) && (eastValid[i] == 0)) {
/* 3602 */         ladder.meta = 4;
/*      */       }
/* 3604 */       if ((northValid[i] == 0) && (southValid[i] == 0) && (westValid[i] != 0) && (eastValid[i] == 0)) {
/* 3605 */         ladder.meta = 2;
/*      */       }
/* 3607 */       if ((northValid[i] == 0) && (southValid[i] == 0) && (westValid[i] == 0) && (eastValid[i] != 0)) {
/* 3608 */         ladder.meta = 3;
/*      */       }
/* 3610 */       i++;
/*      */     }
/*      */     
/* 3613 */     boolean goOn = true;
/*      */     
/* 3615 */     while (goOn) {
/* 3616 */       goOn = false;
/* 3617 */       i = 0;
/* 3618 */       for (BuildingBlock ladder : ladders) {
/* 3619 */         if (ladder.meta == 0) {
/* 3620 */           if (org.millenaire.common.MLN.LogBuildingPlan >= 1) {
/* 3621 */             org.millenaire.common.MLN.major(this, this.buildingKey + ": ladder " + ladder + " has no metada, trying to find neighbours.");
/* 3622 */             if (map.containsKey(ladder.p.getAbove())) {
/* 3623 */               org.millenaire.common.MLN.major(this, this.buildingKey + ": Above: " + map.get(ladder.p.getAbove()));
/*      */             }
/*      */             
/* 3626 */             if (map.containsKey(ladder.p.getBelow())) {
/* 3627 */               org.millenaire.common.MLN.major(this, this.buildingKey + ": Below: " + map.get(ladder.p.getBelow()));
/*      */             }
/*      */           }
/*      */           
/* 3631 */           if (map.containsKey(ladder.p.getAbove())) {
/* 3632 */             BuildingBlock b = (BuildingBlock)map.get(ladder.p.getAbove());
/* 3633 */             if ((b.block == Blocks.field_150468_ap) && (b.meta != 0)) {
/* 3634 */               if ((b.meta == 5) && (northValid[i] != 0)) {
/* 3635 */                 ladder.meta = b.meta;
/* 3636 */                 goOn = true;
/* 3637 */               } else if ((b.meta == 4) && (southValid[i] != 0)) {
/* 3638 */                 ladder.meta = b.meta;
/* 3639 */                 goOn = true;
/* 3640 */               } else if ((b.meta == 3) && (westValid[i] != 0)) {
/* 3641 */                 ladder.meta = b.meta;
/* 3642 */                 goOn = true;
/* 3643 */               } else if ((b.meta == 2) && (eastValid[i] != 0)) {
/* 3644 */                 ladder.meta = b.meta;
/* 3645 */                 goOn = true;
/*      */               }
/*      */             }
/*      */           }
/* 3649 */           if ((ladder.meta == 0) && (map.containsKey(ladder.p.getBelow()))) {
/* 3650 */             if (org.millenaire.common.MLN.LogBuildingPlan >= 1) {
/* 3651 */               org.millenaire.common.MLN.major(this, this.buildingKey + ": trying ladder below. " + northValid[i] + "/" + southValid[i] + "/" + westValid[i] + "/" + eastValid[i]);
/*      */             }
/* 3653 */             BuildingBlock b = (BuildingBlock)map.get(ladder.p.getBelow());
/* 3654 */             if ((b.block == Blocks.field_150468_ap) && (b.meta != 0)) {
/* 3655 */               if ((b.meta == 5) && (northValid[i] != 0)) {
/* 3656 */                 if (org.millenaire.common.MLN.LogBuildingPlan >= 1) {
/* 3657 */                   org.millenaire.common.MLN.major(this, this.buildingKey + ": copying blow: north");
/*      */                 }
/* 3659 */                 ladder.meta = b.meta;
/* 3660 */                 goOn = true;
/* 3661 */               } else if ((b.meta == 4) && (southValid[i] != 0)) {
/* 3662 */                 if (org.millenaire.common.MLN.LogBuildingPlan >= 1) {
/* 3663 */                   org.millenaire.common.MLN.major(this, this.buildingKey + ": copying blow: south");
/*      */                 }
/* 3665 */                 ladder.meta = b.meta;
/* 3666 */                 goOn = true;
/* 3667 */               } else if ((b.meta == 3) && (westValid[i] != 0)) {
/* 3668 */                 if (org.millenaire.common.MLN.LogBuildingPlan >= 1) {
/* 3669 */                   org.millenaire.common.MLN.major(this, this.buildingKey + ": copying blow: west");
/*      */                 }
/* 3671 */                 ladder.meta = b.meta;
/* 3672 */                 goOn = true;
/* 3673 */               } else if ((b.meta == 2) && (eastValid[i] != 0)) {
/* 3674 */                 if (org.millenaire.common.MLN.LogBuildingPlan >= 1) {
/* 3675 */                   org.millenaire.common.MLN.major(this, this.buildingKey + ": copying blow: east");
/*      */                 }
/* 3677 */                 ladder.meta = b.meta;
/* 3678 */                 goOn = true;
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/* 3683 */         i++;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 3688 */     northValid = new boolean[stairs.size()];
/* 3689 */     southValid = new boolean[stairs.size()];
/* 3690 */     westValid = new boolean[stairs.size()];
/* 3691 */     eastValid = new boolean[stairs.size()];
/*      */     
/* 3693 */     i = 0;
/* 3694 */     for (BuildingBlock stair : stairs) {
/* 3695 */       northValid[i] = ((!mapIsOpaqueBlock(map, stair.p.getSouth())) && ((!mapIsOpaqueBlock(map, stair.p.getNorth().getAbove())) || (mapIsOpaqueBlock(map, stair.p.getNorth().getAbove()))) ? 1 : false);
/* 3696 */       southValid[i] = ((!mapIsOpaqueBlock(map, stair.p.getNorth())) && ((!mapIsOpaqueBlock(map, stair.p.getSouth().getAbove())) || (mapIsOpaqueBlock(map, stair.p.getSouth().getAbove()))) ? 1 : false);
/* 3697 */       westValid[i] = ((!mapIsOpaqueBlock(map, stair.p.getEast())) && ((!mapIsOpaqueBlock(map, stair.p.getWest().getAbove())) || (mapIsOpaqueBlock(map, stair.p.getWest().getAbove()))) ? 1 : false);
/* 3698 */       eastValid[i] = ((!mapIsOpaqueBlock(map, stair.p.getWest())) && ((!mapIsOpaqueBlock(map, stair.p.getEast().getAbove())) || (mapIsOpaqueBlock(map, stair.p.getEast().getAbove()))) ? 1 : false);
/*      */       
/* 3700 */       if (org.millenaire.common.MLN.LogBuildingPlan >= 1) {
/* 3701 */         if (northValid[i] != 0) {
/* 3702 */           org.millenaire.common.MLN.major(this, this.buildingKey + ": northValid");
/* 3703 */         } else if (southValid[i] != 0) {
/* 3704 */           org.millenaire.common.MLN.major(this, this.buildingKey + ": southValid");
/* 3705 */         } else if (westValid[i] != 0) {
/* 3706 */           org.millenaire.common.MLN.major(this, this.buildingKey + ": westValid");
/* 3707 */         } else if (eastValid[i] != 0) {
/* 3708 */           org.millenaire.common.MLN.major(this, this.buildingKey + ": eastValid");
/*      */         } else {
/* 3710 */           org.millenaire.common.MLN.major(this, this.buildingKey + ": none valid");
/*      */         }
/*      */       }
/*      */       
/* 3714 */       if (northValid[i] != 0) {
/* 3715 */         stair.meta = 1;
/* 3716 */       } else if (southValid[i] != 0) {
/* 3717 */         stair.meta = 0;
/* 3718 */       } else if (westValid[i] != 0) {
/* 3719 */         stair.meta = 3;
/* 3720 */       } else if (eastValid[i] != 0) {
/* 3721 */         stair.meta = 2;
/*      */       } else {
/* 3723 */         stair.meta = 0;
/*      */       }
/* 3725 */       i++;
/*      */     }
/*      */     
/* 3728 */     for (BuildingBlock door : doors) {
/* 3729 */       int orientation = door.meta & 0x3;
/* 3730 */       if (orientation == 2) {
/* 3731 */         if (((!map.containsKey(door.p.getWest())) || (((BuildingBlock)map.get(door.p.getWest())).block == Blocks.field_150350_a) || (((BuildingBlock)map.get(door.p.getWest())).block == Blocks.field_150466_ao)) && (map.containsKey(door.p.getEast()))) {
/* 3732 */           door.special = BuildingBlock.INVERTEDDOOR;
/*      */         }
/* 3734 */       } else if (orientation == 3) {
/* 3735 */         if (((!map.containsKey(door.p.getNorth())) || (((BuildingBlock)map.get(door.p.getNorth())).block == Blocks.field_150350_a) || (((BuildingBlock)map.get(door.p.getNorth())).block == Blocks.field_150466_ao)) && (map.containsKey(door.p.getSouth())))
/*      */         {
/* 3737 */           door.special = BuildingBlock.INVERTEDDOOR;
/*      */         }
/* 3739 */       } else if (orientation == 0) {
/* 3740 */         if (((!map.containsKey(door.p.getEast())) || (((BuildingBlock)map.get(door.p.getEast())).block == Blocks.field_150350_a) || (((BuildingBlock)map.get(door.p.getEast())).block == Blocks.field_150466_ao)) && (map.containsKey(door.p.getWest()))) {
/* 3741 */           door.special = BuildingBlock.INVERTEDDOOR;
/*      */         }
/* 3743 */       } else if ((orientation == 1) && 
/* 3744 */         ((!map.containsKey(door.p.getSouth())) || (((BuildingBlock)map.get(door.p.getSouth())).block == Blocks.field_150350_a) || (((BuildingBlock)map.get(door.p.getSouth())).block == Blocks.field_150466_ao)) && (map.containsKey(door.p.getNorth())))
/*      */       {
/* 3746 */         door.special = BuildingBlock.INVERTEDDOOR;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public LocationReturn testSpot(org.millenaire.common.MillWorldInfo winfo, org.millenaire.common.pathing.AStarPathing pathing, org.millenaire.common.Point centre, int x, int z, java.util.Random random, int porientation)
/*      */   {
/* 3756 */     int relx = x + winfo.mapStartX - centre.getiX();
/* 3757 */     int relz = z + winfo.mapStartZ - centre.getiZ();
/*      */     
/* 3759 */     winfo.buildTested[x][z] = 1;
/*      */     
/* 3761 */     if (org.millenaire.common.MLN.LogWorldGeneration >= 3) {
/* 3762 */       org.millenaire.common.MLN.debug(this, "Testing: " + x + "/" + z);
/*      */     }
/*      */     int orientation;
/* 3765 */     if (porientation == -1) { int orientation;
/* 3766 */       if (relx * relx > relz * relz) { int orientation;
/* 3767 */         if (relx > 0) {
/* 3768 */           orientation = 0;
/*      */         } else
/* 3770 */           orientation = 2;
/*      */       } else {
/*      */         int orientation;
/* 3773 */         if (relz > 0) {
/* 3774 */           orientation = 3;
/*      */         } else {
/* 3776 */           orientation = 1;
/*      */         }
/*      */       }
/*      */     } else {
/* 3780 */       orientation = porientation;
/*      */     }
/*      */     
/* 3783 */     int orientation = (orientation + this.buildingOrientation) % 4;
/*      */     
/*      */     int zwidth;
/*      */     int xwidth;
/*      */     int zwidth;
/* 3788 */     if ((orientation == 0) || (orientation == 2)) {
/* 3789 */       int xwidth = this.length + this.areaToClear * 2 + 2;
/* 3790 */       zwidth = this.width + this.areaToClear * 2 + 2;
/*      */     } else {
/* 3792 */       xwidth = this.width + this.areaToClear * 2 + 2;
/* 3793 */       zwidth = this.length + this.areaToClear * 2 + 2;
/*      */     }
/*      */     
/* 3796 */     int altitudeTotal = 0;
/* 3797 */     int nbPoints = 0;
/* 3798 */     int nbError = 0;
/*      */     
/* 3800 */     int allowedErrors = 10;
/* 3801 */     boolean hugeBuilding = false;
/*      */     
/* 3803 */     if (xwidth * zwidth > 6000) {
/* 3804 */       allowedErrors = 1500;
/* 3805 */       hugeBuilding = true;
/* 3806 */     } else if (xwidth * zwidth > 200) {
/* 3807 */       allowedErrors = xwidth * zwidth / 20;
/*      */     }
/*      */     
/* 3810 */     boolean reachable = false;
/*      */     
/* 3812 */     for (int i = 0; i <= (int)Math.floor(xwidth / 2); i++) {
/* 3813 */       for (int j = 0; j <= (int)Math.floor(zwidth / 2); j++) {
/* 3814 */         for (int k = 0; k < 4; k++) { int cj;
/*      */           int ci;
/* 3816 */           int cj; if (k == 0) {
/* 3817 */             int ci = x + i;
/* 3818 */             cj = z + j; } else { int cj;
/* 3819 */             if (k == 1) {
/* 3820 */               int ci = x - i;
/* 3821 */               cj = z + j; } else { int cj;
/* 3822 */               if (k == 2) {
/* 3823 */                 int ci = x - i;
/* 3824 */                 cj = z - j;
/*      */               } else {
/* 3826 */                 ci = x + i;
/* 3827 */                 cj = z - j;
/*      */               }
/*      */             } }
/* 3830 */           if ((ci < 0) || (cj < 0) || (ci >= winfo.length) || (cj >= winfo.width)) {
/* 3831 */             org.millenaire.common.Point p = new org.millenaire.common.Point(ci + winfo.mapStartX, 64.0D, cj + winfo.mapStartZ);
/*      */             
/* 3833 */             return new LocationReturn(1, p);
/*      */           }
/*      */           
/* 3836 */           if (winfo.buildingLoc[ci][cj] != 0) {
/* 3837 */             if (nbError > allowedErrors) {
/* 3838 */               org.millenaire.common.Point p = new org.millenaire.common.Point(ci + winfo.mapStartX, 64.0D, cj + winfo.mapStartZ);
/*      */               
/* 3840 */               return new LocationReturn(2, p);
/*      */             }
/* 3842 */             nbError += 5;
/*      */           }
/* 3844 */           else if (winfo.buildingForbidden[ci][cj] != 0) {
/* 3845 */             if ((!hugeBuilding) || (nbError > allowedErrors))
/*      */             {
/* 3847 */               org.millenaire.common.Point p = new org.millenaire.common.Point(ci + winfo.mapStartX, 64.0D, cj + winfo.mapStartZ);
/*      */               
/* 3849 */               return new LocationReturn(3, p);
/*      */             }
/* 3851 */             nbError++;
/*      */           }
/* 3853 */           else if (winfo.danger[ci][cj] != 0) {
/* 3854 */             if (nbError > allowedErrors) {
/* 3855 */               org.millenaire.common.Point p = new org.millenaire.common.Point(ci + winfo.mapStartX, 64.0D, cj + winfo.mapStartZ);
/*      */               
/* 3857 */               return new LocationReturn(5, p);
/*      */             }
/* 3859 */             nbError++;
/*      */           }
/* 3861 */           else if (winfo.canBuild[ci][cj] == 0) {
/* 3862 */             if (nbError > allowedErrors) {
/* 3863 */               org.millenaire.common.Point p = new org.millenaire.common.Point(ci + winfo.mapStartX, 64.0D, cj + winfo.mapStartZ);
/*      */               
/* 3865 */               return new LocationReturn(4, p);
/*      */             }
/* 3867 */             nbError++;
/*      */           }
/*      */           
/*      */ 
/* 3871 */           if ((pathing != null) && (pathing.regions[ci][cj] != pathing.thRegion)) {
/* 3872 */             reachable = false;
/*      */           } else {
/* 3874 */             reachable = true;
/*      */           }
/*      */           
/* 3877 */           altitudeTotal += winfo.constructionHeight[ci][cj];
/* 3878 */           nbPoints++;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 3883 */     if ((pathing != null) && (!reachable)) {
/* 3884 */       return new LocationReturn(4, centre);
/*      */     }
/*      */     
/* 3887 */     int altitude = (int)(altitudeTotal * 1.0F / nbPoints);
/*      */     
/* 3889 */     BuildingLocation l = new BuildingLocation(this, new org.millenaire.common.Point(x + winfo.mapStartX, altitude, z + winfo.mapStartZ), orientation);
/*      */     
/* 3891 */     return new LocationReturn(l);
/*      */   }
/*      */   
/*      */   public LocationReturn testSpotBedrock(net.minecraft.world.World world, int cx, int cz) {
/* 3895 */     for (int x = cx - this.width - 2; x < cx + this.width + 2; x++) {
/* 3896 */       for (int z = cz - this.length - 2; z < cz + this.length + 2; z++) {
/* 3897 */         for (int y = 0; y < this.plan.length + 2; y++) {
/* 3898 */           net.minecraft.block.Block block = world.func_147439_a(x, y, z);
/*      */           
/* 3900 */           if ((block != Blocks.field_150357_h) && (block != Blocks.field_150348_b) && (block != Blocks.field_150346_d) && (block != Blocks.field_150351_n) && (block != Blocks.field_150365_q) && (block != Blocks.field_150482_ag) && (block != Blocks.field_150352_o) && (block != Blocks.field_150366_p) && (block != Blocks.field_150369_x) && (block != Blocks.field_150450_ax))
/*      */           {
/* 3902 */             return new LocationReturn(3, null);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 3908 */     BuildingLocation l = new BuildingLocation(this, new org.millenaire.common.Point(cx, 2.0D, cz), 0);
/* 3909 */     l.bedrocklevel = true;
/* 3910 */     return new LocationReturn(l);
/*      */   }
/*      */   
/*      */   public String toString()
/*      */   {
/* 3915 */     if (this.culture != null) {
/* 3916 */       return this.culture.key + ":" + this.planName;
/*      */     }
/*      */     
/* 3919 */     return "null culture:" + this.planName;
/*      */   }
/*      */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\building\BuildingPlan.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */