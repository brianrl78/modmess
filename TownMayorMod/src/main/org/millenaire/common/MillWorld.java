/*      */ package org.millenaire.common;
/*      */ 
/*      */ import io.netty.buffer.ByteBufInputStream;
/*      */ import java.io.BufferedReader;
/*      */ import java.io.BufferedWriter;
/*      */ import java.io.DataOutput;
/*      */ import java.io.File;
/*      */ import java.io.FileInputStream;
/*      */ import java.io.IOException;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.HashMap;
/*      */ import java.util.List;
/*      */ import net.minecraft.block.Block;
/*      */ import net.minecraft.entity.player.EntityPlayer;
/*      */ import net.minecraft.nbt.CompressedStreamTools;
/*      */ import net.minecraft.nbt.NBTTagCompound;
/*      */ import net.minecraft.nbt.NBTTagList;
/*      */ import net.minecraft.util.ChunkCoordinates;
/*      */ import net.minecraft.util.EnumChatFormatting;
/*      */ import net.minecraft.util.MathHelper;
/*      */ import net.minecraft.world.World;
/*      */ import net.minecraft.world.WorldServer;
/*      */ import net.minecraft.world.biome.BiomeGenBase;
/*      */ import net.minecraft.world.biome.WorldChunkManager;
/*      */ import net.minecraft.world.chunk.IChunkProvider;
/*      */ import net.minecraft.world.storage.WorldInfo;
/*      */ import org.millenaire.common.building.Building;
/*      */ import org.millenaire.common.building.BuildingLocation;
/*      */ import org.millenaire.common.core.DevModUtilities;
/*      */ import org.millenaire.common.core.MillCommonUtilities;
/*      */ import org.millenaire.common.core.MillCommonUtilities.ExtFileFilter;
/*      */ import org.millenaire.common.core.MillCommonUtilities.VillageInfo;
/*      */ import org.millenaire.common.core.MillCommonUtilities.VillageList;
/*      */ import org.millenaire.common.forge.CommonProxy;
/*      */ import org.millenaire.common.forge.Mill;
/*      */ import org.millenaire.common.network.ServerSender;
/*      */ import org.millenaire.common.network.StreamReadWrite;
/*      */ 
/*      */ public class MillWorld
/*      */ {
/*      */   public static final String CULTURE_CONTROL = "culturecontrol_";
/*      */   public static final String CROP_PLANTING = "cropplanting_";
/*      */   public static final String PUJAS = "pujas";
/*      */   public static final String MAYANSACRIFICES = "mayansacrifices";
/*   47 */   private final HashMap<Point, Building> buildings = new HashMap();
/*   48 */   private final HashMap<Point, String> renameNames = new HashMap();
/*   49 */   private final HashMap<Point, String> renameQualifiers = new HashMap();
/*   50 */   public final HashMap<Long, MillVillager> villagers = new HashMap();
/*   51 */   public final List<String> globalTags = new ArrayList();
/*   52 */   public MillCommonUtilities.VillageList loneBuildingsList = new MillCommonUtilities.VillageList();
/*      */   public File millenaireDir;
/*   54 */   public File saveDir = null;
/*   55 */   public MillCommonUtilities.VillageList villagesList = new MillCommonUtilities.VillageList();
/*   56 */   public long lastWorldUpdate = 0L;
/*      */   
/*   58 */   public HashMap<String, UserProfile> profiles = new HashMap();
/*      */   
/*   60 */   public List<TileEntityPanel.PanelPacketInfo> panelPacketInfos = new ArrayList();
/*      */   
/*      */   public World world;
/*      */   
/*   64 */   public boolean millenaireEnabled = false;
/*      */   
/*      */ 
/*      */   private int lastupdate;
/*      */   
/*      */ 
/*   70 */   private static HashMap<Point, String> buildingsTags = new HashMap();
/*   71 */   private static HashMap<Point, Integer> buildingsVariation = new HashMap();
/*   72 */   private static HashMap<Point, String> buildingsLocation = new HashMap();
/*      */   
/*      */   public MillWorld(World worldObj) {
/*   75 */     this.world = worldObj;
/*      */     
/*   77 */     if (!this.world.field_72995_K) {
/*   78 */       this.saveDir = MillCommonUtilities.getWorldSaveDir(this.world);
/*   79 */       this.millenaireEnabled = true;
/*      */     }
/*      */     
/*   82 */     if (!this.world.field_72995_K) {
/*   83 */       this.millenaireDir = new File(this.saveDir, "millenaire");
/*      */       
/*   85 */       if (!this.millenaireDir.exists()) {
/*   86 */         this.millenaireDir.mkdir();
/*      */       }
/*      */     }
/*      */     
/*   90 */     Culture.removeServerContent();
/*      */   }
/*      */   
/*      */   public void addBuilding(Building b, Point p) {
/*   94 */     this.buildings.put(p, b);
/*      */   }
/*      */   
/*      */   public Collection<Building> allBuildings() {
/*   98 */     return this.buildings.values();
/*      */   }
/*      */   
/*      */   public boolean buildingExists(Point p) {
/*  102 */     return this.buildings.containsKey(p);
/*      */   }
/*      */   
/*      */   public void checkConnections()
/*      */   {
/*  107 */     for (UserProfile profile : this.profiles.values())
/*      */     {
/*  109 */       if ((profile.connected) && 
/*  110 */         (profile.getPlayer() == null)) {
/*  111 */         profile.disconnectUser();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public void clearGlobalTag(String tag)
/*      */   {
/*  119 */     if (this.globalTags.contains(tag)) {
/*  120 */       this.globalTags.remove(tag);
/*  121 */       saveGlobalTags();
/*      */       
/*  123 */       if (!this.world.field_72995_K) {
/*  124 */         for (UserProfile up : this.profiles.values()) {
/*  125 */           if (up.connected) {
/*  126 */             up.sendProfilePacket(7);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public void clearPanelQueue() {
/*  134 */     List<TileEntityPanel.PanelPacketInfo> toDelete = new ArrayList();
/*      */     
/*  136 */     for (TileEntityPanel.PanelPacketInfo pinfo : this.panelPacketInfos) {
/*  137 */       TileEntityPanel panel = pinfo.pos.getPanel(this.world);
/*      */       
/*  139 */       if (panel != null) {
/*  140 */         panel.panelType = pinfo.panelType;
/*  141 */         panel.buildingPos = pinfo.buildingPos;
/*  142 */         panel.villager_id = pinfo.villager_id;
/*      */         
/*  144 */         for (int i = 0; (i < pinfo.lines.length) && (i < panel.field_145915_a.length); i++) {
/*  145 */           panel.field_145915_a[i] = MLN.string(pinfo.lines[i]);
/*      */         }
/*      */         
/*  148 */         toDelete.add(pinfo);
/*      */       }
/*      */     }
/*      */     
/*  152 */     for (TileEntityPanel.PanelPacketInfo pinfo : toDelete) {
/*  153 */       this.panelPacketInfos.remove(pinfo);
/*      */     }
/*      */   }
/*      */   
/*      */   public void displayTagActionData(EntityPlayer player) {
/*  158 */     String s = "";
/*  159 */     for (String tag : this.globalTags) {
/*  160 */       s = s + tag + " ";
/*      */     }
/*  162 */     ServerSender.sendChat(player, EnumChatFormatting.GREEN, "Tags: " + s);
/*      */     
/*  164 */     ServerSender.sendChat(player, EnumChatFormatting.GREEN, "ActionData: " + s);
/*  165 */     String biomeName = this.world.func_72959_q().func_76935_a((int)player.field_70165_t, (int)player.field_70161_v).field_76791_y.toLowerCase();
/*  166 */     ServerSender.sendChat(player, EnumChatFormatting.GREEN, "Biome: " + biomeName + ", time: " + this.world.func_72820_D() % 24000L + " / " + this.world.func_72820_D());
/*      */   }
/*      */   
/*      */   public void displayVillageList(EntityPlayer player, boolean loneBuildings)
/*      */   {
/*      */     MillCommonUtilities.VillageList list;
/*      */     MillCommonUtilities.VillageList list;
/*  173 */     if (loneBuildings) {
/*  174 */       list = this.loneBuildingsList;
/*      */     } else {
/*  176 */       list = this.villagesList;
/*      */     }
/*      */     
/*  179 */     List<MillCommonUtilities.VillageInfo> villageList = new ArrayList();
/*      */     
/*  181 */     for (int i = 0; i < list.names.size(); i++)
/*      */     {
/*  183 */       Point p = (Point)list.pos.get(i);
/*      */       
/*  185 */       int distance = MathHelper.func_76128_c(p.horizontalDistanceTo(player));
/*      */       
/*  187 */       if (distance <= MLN.BackgroundRadius) {
/*  188 */         String direction = new Point(player).directionTo(p, true);
/*      */         
/*      */ 
/*      */ 
/*  192 */         Building townHall = getBuilding(p);
/*      */         String loaded;
/*  194 */         String loaded; if (townHall == null) {
/*  195 */           loaded = "command.inactive"; } else { String loaded;
/*  196 */           if (townHall.isActive) {
/*  197 */             loaded = "command.active";
/*      */           } else { String loaded;
/*  199 */             if (!townHall.isAreaLoaded) {
/*  200 */               loaded = "command.inactive";
/*      */             } else {
/*  202 */               loaded = "command.frozen";
/*      */             }
/*      */           }
/*      */         }
/*      */         VillageType villageType;
/*      */         VillageType villageType;
/*  208 */         if (loneBuildings) {
/*  209 */           villageType = Culture.getCultureByName((String)list.cultures.get(i)).getLoneBuildingType((String)list.types.get(i));
/*      */         } else {
/*  211 */           villageType = Culture.getCultureByName((String)list.cultures.get(i)).getVillageType((String)list.types.get(i));
/*      */         }
/*      */         
/*  214 */         MillCommonUtilities.VillageInfo vi = new MillCommonUtilities.VillageInfo();
/*  215 */         vi.distance = distance;
/*      */         
/*  217 */         if (villageType != null) {
/*  218 */           vi.textKey = "command.villagelist";
/*  219 */           vi.values = new String[] { (String)list.names.get(i), loaded, "" + distance, direction, villageType.name };
/*      */         }
/*  221 */         villageList.add(vi);
/*      */       }
/*      */     }
/*      */     
/*  225 */     if (!loneBuildings) {
/*  226 */       for (int i = 0; i < this.loneBuildingsList.names.size(); i++)
/*      */       {
/*  228 */         VillageType village = Culture.getCultureByName((String)this.loneBuildingsList.cultures.get(i)).getLoneBuildingType((String)this.loneBuildingsList.types.get(i));
/*      */         
/*  230 */         if ((village.keyLonebuilding) || (village.keyLoneBuildingGenerateTag != null))
/*      */         {
/*  232 */           if ((!village.generatedForPlayer) || (player.getDisplayName().equalsIgnoreCase((String)this.loneBuildingsList.generatedFor.get(i))))
/*      */           {
/*  234 */             Point p = (Point)this.loneBuildingsList.pos.get(i);
/*      */             
/*  236 */             int distance = MathHelper.func_76128_c(p.horizontalDistanceTo(player));
/*      */             
/*  238 */             if (distance <= 2000) {
/*  239 */               String direction = new Point(player).directionTo(p, true);
/*      */               
/*  241 */               MillCommonUtilities.VillageInfo vi = new MillCommonUtilities.VillageInfo();
/*  242 */               vi.distance = distance;
/*      */               
/*  244 */               if (village != null) {
/*  245 */                 vi.textKey = "command.villagelistkeylonebuilding";
/*  246 */                 vi.values = new String[] { village.name, "" + distance, direction };
/*      */               }
/*  248 */               villageList.add(vi);
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  255 */     if (villageList.size() == 0) {
/*  256 */       ServerSender.sendTranslatedSentence(player, '7', "command.noknowvillage", new String[0]);
/*      */     }
/*      */     else {
/*  259 */       Collections.sort(villageList);
/*      */       
/*  261 */       for (MillCommonUtilities.VillageInfo vi : villageList) {
/*  262 */         ServerSender.sendTranslatedSentence(player, '7', vi.textKey, vi.values);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public void forcePreload()
/*      */   {
/*  269 */     if ((this.world.field_72995_K) || (MLN.forcePreload <= 0)) {
/*  270 */       return;
/*      */     }
/*      */     
/*  273 */     this.lastupdate += 1;
/*      */     
/*  275 */     if (this.lastupdate < 50) {
/*  276 */       return;
/*      */     }
/*      */     
/*  279 */     this.lastupdate = 0;
/*      */     int centreZ;
/*      */     int centreX;
/*      */     int centreZ;
/*  283 */     if (this.world.field_73010_i.size() > 0)
/*      */     {
/*  285 */       Object o = this.world.field_73010_i.get(0);
/*  286 */       EntityPlayer player = (EntityPlayer)o;
/*      */       
/*  288 */       int centreX = (int)(player.field_70165_t / 16.0D);
/*  289 */       centreZ = (int)(player.field_70161_v / 16.0D);
/*      */     } else {
/*  291 */       centreX = this.world.func_72861_E().field_71574_a / 16;
/*  292 */       centreZ = this.world.func_72861_E().field_71573_c / 16;
/*      */     }
/*      */     
/*  295 */     int nbGenerated = 0;
/*      */     
/*  297 */     for (int radius = 1; radius < MLN.forcePreload; radius++) {
/*  298 */       for (int i = -MLN.forcePreload; (i < MLN.forcePreload) && (nbGenerated < 100); i++) {
/*  299 */         for (int j = -MLN.forcePreload; (j < MLN.forcePreload) && (nbGenerated < 100); j++) {
/*  300 */           if ((i * i + j * j < radius * radius) && 
/*  301 */             (!this.world.func_72863_F().func_73149_a(i + centreX, j + centreZ))) {
/*  302 */             this.world.func_72863_F().func_73158_c(i + centreX, j + centreZ);
/*  303 */             Block block = this.world.func_147439_a((i + centreX) * 16, 60, (j + centreZ) * 16);
/*  304 */             this.world.func_72863_F().func_73151_a(false, null);
/*  305 */             MLN.minor(this, "Forcing population of chunk " + (i + centreX) + "/" + (j + centreZ) + ", block: " + block);
/*  306 */             nbGenerated++;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public Building getBuilding(Point p)
/*      */   {
/*  316 */     if (this.buildings.containsKey(p))
/*      */     {
/*  318 */       if (this.buildings.get(p) == null) {
/*  319 */         MLN.error(this, "Building record for " + p + " is null.");
/*  320 */       } else if (((Building)this.buildings.get(p)).location == null) {
/*  321 */         MLN.printException("Building location for " + p + " is null.", new Exception());
/*      */       }
/*      */       
/*  324 */       return (Building)this.buildings.get(p);
/*      */     }
/*      */     
/*  327 */     if (MLN.LogWorldInfo >= 2) {
/*  328 */       MLN.minor(this, "Could not find a building at location " + p + " amoung " + this.buildings.size() + " records.");
/*      */     }
/*      */     
/*  331 */     return null;
/*      */   }
/*      */   
/*      */   public Building getClosestVillage(Point p)
/*      */   {
/*  336 */     int bestDistance = Integer.MAX_VALUE;
/*  337 */     Building bestVillage = null;
/*      */     
/*  339 */     for (Point villageCoord : this.villagesList.pos) {
/*  340 */       int dist = (int)p.distanceToSquared(villageCoord);
/*  341 */       if ((bestVillage == null) || (dist < bestDistance)) {
/*  342 */         Building village = getBuilding(villageCoord);
/*  343 */         if (village != null) {
/*  344 */           bestVillage = village;
/*  345 */           bestDistance = dist;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  350 */     return bestVillage;
/*      */   }
/*      */   
/*      */   public List<Point> getCombinedVillagesLoneBuildings() {
/*  354 */     List<Point> thPosLists = new ArrayList(this.villagesList.pos);
/*  355 */     thPosLists.addAll(this.loneBuildingsList.pos);
/*  356 */     return thPosLists;
/*      */   }
/*      */   
/*      */   public UserProfile getProfile(String name) {
/*  360 */     if (this.profiles.containsKey(name)) {
/*  361 */       return (UserProfile)this.profiles.get(name);
/*      */     }
/*      */     
/*      */ 
/*  365 */     if (this.profiles.containsKey("SinglePlayer"))
/*      */     {
/*  367 */       UserProfile profile = (UserProfile)this.profiles.get("SinglePlayer");
/*  368 */       profile.changeProfileKey(name);
/*      */       
/*  370 */       return profile;
/*      */     }
/*      */     
/*  373 */     UserProfile profile = new UserProfile(this, name, name);
/*  374 */     this.profiles.put(profile.key, profile);
/*  375 */     return profile;
/*      */   }
/*      */   
/*      */   public boolean isGlobalTagSet(String tag) {
/*  379 */     return this.globalTags.contains(tag);
/*      */   }
/*      */   
/*      */ 
/*      */   private void loadBuildings()
/*      */   {
/*  385 */     long startTime = System.currentTimeMillis();
/*      */     
/*  387 */     File buildingsDir = new File(this.millenaireDir, "buildings");
/*      */     
/*  389 */     if (!buildingsDir.exists()) {
/*  390 */       buildingsDir.mkdir();
/*      */     }
/*      */     
/*  393 */     for (File file : buildingsDir.listFiles(new MillCommonUtilities.ExtFileFilter("gz"))) {
/*      */       try {
/*  395 */         FileInputStream fileinputstream = new FileInputStream(file);
/*  396 */         NBTTagCompound nbttagcompound = CompressedStreamTools.func_74796_a(fileinputstream);
/*      */         
/*  398 */         NBTTagList nbttaglist = nbttagcompound.func_150295_c("buildings", 10);
/*  399 */         for (int i = 0; i < nbttaglist.func_74745_c(); i++) {
/*  400 */           NBTTagCompound nbttagcompound1 = nbttaglist.func_150305_b(i);
/*  401 */           new Building(this, nbttagcompound1);
/*      */         }
/*      */       }
/*      */       catch (Exception e) {
/*  405 */         MLN.printException("Error when attempting to load building file " + file.getAbsolutePath() + ":", e);
/*      */       }
/*      */     }
/*      */     
/*  409 */     if (MLN.LogHybernation >= 1) {
/*  410 */       for (Building b : this.buildings.values()) {
/*  411 */         MLN.major(null, b + " - " + b.culture);
/*      */       }
/*      */       
/*  414 */       MLN.major(this, "Loaded " + this.buildings.size() + " in " + (System.currentTimeMillis() - startTime) + " ms.");
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public void loadData()
/*      */   {
/*  421 */     if (this.world.field_72995_K) {
/*  422 */       return;
/*      */     }
/*      */     
/*  425 */     loadWorldConfig();
/*  426 */     loadVillageList();
/*  427 */     loadGlobalTags();
/*  428 */     loadBuildings();
/*  429 */     loadProfiles();
/*      */     
/*  431 */     Mill.startMessageDisplayed = false;
/*      */   }
/*      */   
/*      */   private void loadGlobalTags() {
/*  435 */     File tagsFile = new File(this.millenaireDir, "tags.txt");
/*      */     
/*  437 */     this.globalTags.clear();
/*      */     
/*  439 */     if (tagsFile.exists()) {
/*      */       try
/*      */       {
/*  442 */         BufferedReader reader = MillCommonUtilities.getReader(tagsFile);
/*  443 */         String line = reader.readLine();
/*      */         
/*  445 */         while (line != null)
/*      */         {
/*  447 */           if (line.trim().length() > 0) {
/*  448 */             this.globalTags.add(line.trim());
/*      */           }
/*  450 */           line = reader.readLine();
/*      */         }
/*      */         
/*  453 */         if (MLN.LogWorldGeneration >= 1) {
/*  454 */           MLN.major(null, "Loaded " + this.globalTags.size() + " tags.");
/*      */         }
/*      */       }
/*      */       catch (Exception e) {
/*  458 */         MLN.printException(e);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private void loadProfiles()
/*      */   {
/*  465 */     File profilesDir = new File(this.millenaireDir, "profiles");
/*      */     
/*  467 */     if (!profilesDir.exists()) {
/*  468 */       profilesDir.mkdirs();
/*      */     }
/*      */     
/*  471 */     for (File profileDir : profilesDir.listFiles()) {
/*  472 */       if ((profileDir.isDirectory()) && (!profileDir.isHidden())) {
/*  473 */         UserProfile profile = UserProfile.readProfile(this, profileDir);
/*      */         
/*  475 */         if (profile != null) {
/*  476 */           this.profiles.put(profile.key, profile);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private void loadVillageList() {
/*  483 */     File villageLog = new File(this.millenaireDir, "villages.txt");
/*      */     
/*  485 */     if (villageLog.exists()) {
/*      */       try
/*      */       {
/*  488 */         BufferedReader reader = MillCommonUtilities.getReader(villageLog);
/*  489 */         String line = reader.readLine();
/*      */         
/*  491 */         while (line != null)
/*      */         {
/*  493 */           if (line.trim().length() > 0)
/*      */           {
/*  495 */             String[] p = line.split(";")[1].split("/");
/*      */             
/*  497 */             String type = "";
/*  498 */             if (line.split(";").length > 2) {
/*  499 */               type = line.split(";")[2];
/*      */             }
/*      */             
/*  502 */             String culture = "";
/*  503 */             if (line.split(";").length > 3) {
/*  504 */               culture = line.split(";")[3];
/*      */             }
/*      */             
/*  507 */             Culture c = Culture.getCultureByName(culture);
/*      */             
/*  509 */             String generatedFor = null;
/*  510 */             if (line.split(";").length > 4) {
/*  511 */               generatedFor = line.split(";")[4];
/*      */             }
/*      */             
/*  514 */             registerVillageLocation(this.world, new Point(Integer.parseInt(p[0]), Integer.parseInt(p[1]), Integer.parseInt(p[2])), line.split(";")[0], c.getVillageType(type), c, false, generatedFor);
/*      */           }
/*      */           
/*      */ 
/*  518 */           line = reader.readLine();
/*      */         }
/*      */         
/*  521 */         if (MLN.LogWorldGeneration >= 1) {
/*  522 */           MLN.major(null, "Loaded " + this.villagesList.names.size() + " village positions.");
/*      */         }
/*      */       }
/*      */       catch (Exception e) {
/*  526 */         MLN.printException(e);
/*      */       }
/*      */     }
/*      */     
/*  530 */     villageLog = new File(this.millenaireDir, "lonebuildings.txt");
/*      */     
/*  532 */     if (villageLog.exists()) {
/*      */       try
/*      */       {
/*  535 */         BufferedReader reader = MillCommonUtilities.getReader(villageLog);
/*  536 */         String line = reader.readLine();
/*      */         
/*  538 */         while (line != null)
/*      */         {
/*  540 */           if (line.trim().length() > 0)
/*      */           {
/*  542 */             String[] p = line.split(";")[1].split("/");
/*      */             
/*  544 */             String type = "";
/*  545 */             if (line.split(";").length > 2) {
/*  546 */               type = line.split(";")[2];
/*      */             }
/*      */             
/*  549 */             String culture = "";
/*  550 */             if (line.split(";").length > 3) {
/*  551 */               culture = line.split(";")[3];
/*      */             }
/*      */             
/*  554 */             Culture c = Culture.getCultureByName(culture);
/*      */             
/*  556 */             String generatedFor = null;
/*  557 */             if (line.split(";").length > 4) {
/*  558 */               generatedFor = line.split(";")[4];
/*      */             }
/*      */             
/*  561 */             registerLoneBuildingsLocation(this.world, new Point(Integer.parseInt(p[0]), Integer.parseInt(p[1]), Integer.parseInt(p[2])), line.split(";")[0], c.getLoneBuildingType(type), c, false, generatedFor);
/*      */           }
/*      */           
/*      */ 
/*  565 */           line = reader.readLine();
/*      */         }
/*      */         
/*  568 */         if (MLN.LogWorldGeneration >= 1) {
/*  569 */           MLN.major(null, "Loaded " + this.loneBuildingsList.names.size() + " lone buildings positions.");
/*      */         }
/*      */       }
/*      */       catch (Exception e) {
/*  573 */         MLN.printException(e);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private void loadWorldConfig()
/*      */   {
/*  580 */     MLN.generateVillages = MLN.generateVillagesDefault;
/*      */     
/*  582 */     this.renameNames.clear();
/*  583 */     this.renameQualifiers.clear();
/*      */     
/*  585 */     File configFile = new File(this.millenaireDir, "config.txt");
/*      */     
/*  587 */     if ((configFile != null) && (configFile.exists())) {
/*      */       try
/*      */       {
/*  590 */         BufferedReader reader = MillCommonUtilities.getReader(configFile);
/*      */         
/*      */         String line;
/*      */         
/*  594 */         while ((line = reader.readLine()) != null) {
/*  595 */           if ((line.trim().length() > 0) && (!line.startsWith("//"))) {
/*  596 */             String[] temp = line.split("=");
/*  597 */             if (temp.length == 2)
/*      */             {
/*  599 */               String key = temp[0];
/*  600 */               String value = temp[1];
/*      */               
/*  602 */               if (key.equalsIgnoreCase("generate_villages")) {
/*  603 */                 MLN.generateVillages = Boolean.parseBoolean(value);
/*  604 */               } else if (key.equalsIgnoreCase("rename_name")) {
/*  605 */                 Point p = new Point(value.split(",")[0]);
/*  606 */                 this.renameNames.put(p, value.split(",")[1]);
/*  607 */               } else if (key.equalsIgnoreCase("rename_qualifier")) {
/*  608 */                 Point p = new Point(value.split(",")[0]);
/*  609 */                 if (value.split(",").length > 1) {
/*  610 */                   this.renameQualifiers.put(p, value.split(",")[1]);
/*      */                 } else {
/*  612 */                   this.renameQualifiers.put(p, "");
/*      */                 }
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*  618 */         reader.close();
/*      */       }
/*      */       catch (IOException e) {
/*  621 */         MLN.printException(e);
/*      */       }
/*      */     }
/*      */     
/*  625 */     if (MLN.LogWorldGeneration >= 1) {
/*  626 */       MLN.major(null, "Config loaded. generateVillages: " + MLN.generateVillages);
/*      */     }
/*      */   }
/*      */   
/*      */   public int nbCultureInGeneratedVillages()
/*      */   {
/*  632 */     List<String> cultures = new ArrayList();
/*      */     
/*  634 */     for (int i = 0; i < this.villagesList.names.size(); i++) {
/*  635 */       if (!cultures.contains(this.villagesList.cultures.get(i))) {
/*  636 */         cultures.add(this.villagesList.cultures.get(i));
/*      */       }
/*      */     }
/*      */     
/*  640 */     return cultures.size();
/*      */   }
/*      */   
/*      */   public void receiveVillageListPacket(ByteBufInputStream ds)
/*      */   {
/*  645 */     if (MLN.LogNetwork >= 2) {
/*  646 */       MLN.minor(this, "Received village list packet.");
/*      */     }
/*      */     
/*  649 */     this.villagesList = new MillCommonUtilities.VillageList();
/*  650 */     this.loneBuildingsList = new MillCommonUtilities.VillageList();
/*      */     try
/*      */     {
/*  653 */       int nb = ds.readInt();
/*  654 */       for (int i = 0; i < nb; i++) {
/*  655 */         this.villagesList.pos.add(StreamReadWrite.readNullablePoint(ds));
/*  656 */         this.villagesList.names.add(StreamReadWrite.readNullableString(ds));
/*  657 */         this.villagesList.cultures.add(StreamReadWrite.readNullableString(ds));
/*  658 */         this.villagesList.types.add(StreamReadWrite.readNullableString(ds));
/*      */       }
/*      */       
/*  661 */       nb = ds.readInt();
/*  662 */       for (int i = 0; i < nb; i++) {
/*  663 */         this.loneBuildingsList.pos.add(StreamReadWrite.readNullablePoint(ds));
/*  664 */         this.loneBuildingsList.names.add(StreamReadWrite.readNullableString(ds));
/*  665 */         this.loneBuildingsList.cultures.add(StreamReadWrite.readNullableString(ds));
/*  666 */         this.loneBuildingsList.types.add(StreamReadWrite.readNullableString(ds));
/*      */       }
/*      */     }
/*      */     catch (IOException e) {
/*  670 */       MLN.printException(this + ": Error in receiveVillageListPacket", e);
/*      */     }
/*      */   }
/*      */   
/*      */   public void registerLoneBuildingsLocation(World world, Point pos, String name, VillageType type, Culture culture, boolean newVillage, String playerName)
/*      */   {
/*  676 */     boolean found = false;
/*      */     
/*  678 */     for (Point p : this.loneBuildingsList.pos) {
/*  679 */       if (p.equals(pos)) {
/*  680 */         found = true;
/*      */       }
/*      */     }
/*      */     
/*  684 */     if (found) {
/*  685 */       return;
/*      */     }
/*      */     
/*  688 */     if (!type.generatedForPlayer) {
/*  689 */       playerName = null;
/*      */     }
/*      */     
/*  692 */     this.loneBuildingsList.addVillage(pos, name, type.key, culture.key, playerName);
/*      */     
/*  694 */     if (MLN.LogWorldGeneration >= 1) {
/*  695 */       MLN.major(null, "Registering lone buildings: " + name + " / " + type + " / " + culture + " / " + pos);
/*      */     }
/*  697 */     for (Object o : world.field_73010_i) {
/*  698 */       EntityPlayer player = (EntityPlayer)o;
/*  699 */       if ((newVillage) && ((type.keyLonebuilding) || (type.keyLoneBuildingGenerateTag != null))) {
/*  700 */         int distance = MathHelper.func_76128_c(pos.horizontalDistanceTo(player));
/*      */         
/*  702 */         if (distance <= 2000) {
/*  703 */           String direction = new Point(player).directionTo(pos, true);
/*  704 */           ServerSender.sendTranslatedSentence(player, 'e', "command.newlonebuildingfound", new String[] { type.name, "" + distance, direction });
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  710 */     saveLoneBuildingsList();
/*      */   }
/*      */   
/*      */ 
/*      */   public void registerVillageLocation(World world, Point pos, String name, VillageType type, Culture culture, boolean newVillage, String playerName)
/*      */   {
/*  716 */     boolean found = false;
/*      */     
/*  718 */     if (type == null) {
/*  719 */       MLN.error(null, "Attempting to register village with null type: " + pos + "/" + culture + "/" + name + "/" + newVillage);
/*  720 */       return;
/*      */     }
/*      */     
/*  723 */     if (culture == null) {
/*  724 */       MLN.error(null, "Attempting to register village with null culture: " + pos + "/" + type + "/" + name + "/" + newVillage);
/*  725 */       return;
/*      */     }
/*      */     
/*  728 */     for (Point p : this.villagesList.pos) {
/*  729 */       if (p.equals(pos)) {
/*  730 */         found = true;
/*      */       }
/*      */     }
/*      */     
/*  734 */     if (found) {
/*  735 */       return;
/*      */     }
/*      */     
/*  738 */     if (!type.generatedForPlayer) {
/*  739 */       playerName = null;
/*      */     }
/*      */     
/*  742 */     this.villagesList.addVillage(pos, name, type.key, culture.key, playerName);
/*      */     
/*  744 */     if (MLN.LogWorldGeneration >= 1) {
/*  745 */       MLN.major(null, "Registering village: " + name + " / " + type + " / " + culture + " / " + pos);
/*      */     }
/*      */     
/*  748 */     if (newVillage) {
/*  749 */       for (Object o : world.field_73010_i) {
/*  750 */         EntityPlayer player = (EntityPlayer)o;
/*      */         
/*  752 */         int distance = MathHelper.func_76128_c(pos.horizontalDistanceTo(player));
/*      */         
/*  754 */         if ((distance <= 2000) && (!world.field_72995_K)) {
/*  755 */           String direction = new Point(player).directionTo(pos, true);
/*  756 */           ServerSender.sendTranslatedSentence(player, 'e', "command.newvillagefound", new String[] { name, type.name, "culture." + culture.key, "" + distance, direction });
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  763 */     saveVillageList();
/*      */   }
/*      */   
/*      */   public void removeBuilding(Point p) {
/*  767 */     this.buildings.remove(p);
/*      */   }
/*      */   
/*      */   public void removeVillageOrLoneBuilding(Point p)
/*      */   {
/*  772 */     this.loneBuildingsList.removeVillage(p);
/*  773 */     this.villagesList.removeVillage(p);
/*  774 */     saveLoneBuildingsList();
/*  775 */     saveVillageList();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void saveEverything()
/*      */   {
/*  785 */     if (this.world.field_72995_K) {
/*  786 */       return;
/*      */     }
/*      */     
/*  789 */     saveGlobalTags();
/*  790 */     saveLoneBuildingsList();
/*  791 */     saveVillageList();
/*  792 */     saveWorldConfig();
/*      */     
/*  794 */     for (Building b : this.buildings.values())
/*      */     {
/*  796 */       if ((b.isTownhall) && (b.isActive)) {
/*  797 */         b.saveTownHall("world save");
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private void saveGlobalTags()
/*      */   {
/*  806 */     if (this.world.field_72995_K) {
/*  807 */       return;
/*      */     }
/*      */     
/*  810 */     File configFile = new File(this.millenaireDir, "tags.txt");
/*      */     try
/*      */     {
/*  813 */       BufferedWriter writer = MillCommonUtilities.getWriter(configFile);
/*      */       
/*  815 */       for (String tag : this.globalTags) {
/*  816 */         writer.write(tag + MLN.EOL);
/*      */       }
/*  818 */       writer.flush();
/*      */     }
/*      */     catch (IOException e) {
/*  821 */       MLN.printException(e);
/*      */     }
/*      */   }
/*      */   
/*      */   public void saveLoneBuildingsList()
/*      */   {
/*  827 */     if (this.world.field_72995_K) {
/*  828 */       return;
/*      */     }
/*      */     
/*  831 */     File millenaireDir = new File(this.saveDir, "millenaire");
/*      */     
/*  833 */     if (!millenaireDir.exists()) {
/*  834 */       millenaireDir.mkdir();
/*      */     }
/*      */     
/*  837 */     File villageLog = new File(millenaireDir, "lonebuildings.txt");
/*      */     
/*      */     try
/*      */     {
/*  841 */       BufferedWriter writer = MillCommonUtilities.getWriter(villageLog);
/*      */       
/*  843 */       for (int i = 0; i < this.loneBuildingsList.pos.size(); i++) {
/*  844 */         Point p = (Point)this.loneBuildingsList.pos.get(i);
/*      */         
/*  846 */         String generatedFor = (String)this.loneBuildingsList.generatedFor.get(i);
/*      */         
/*  848 */         if (generatedFor == null) {
/*  849 */           generatedFor = "";
/*      */         }
/*      */         
/*  852 */         writer.write((String)this.loneBuildingsList.names.get(i) + ";" + p.getiX() + "/" + p.getiY() + "/" + p.getiZ() + ";" + (String)this.loneBuildingsList.types.get(i) + ";" + (String)this.loneBuildingsList.cultures.get(i) + ";" + generatedFor + System.getProperty("line.separator"));
/*      */       }
/*      */       
/*  855 */       writer.flush();
/*  856 */       if (MLN.LogWorldGeneration >= 1) {
/*  857 */         MLN.major(null, "Saved " + this.loneBuildingsList.names.size() + " lone buildings.txt positions.");
/*      */       }
/*      */     }
/*      */     catch (IOException e) {
/*  861 */       MLN.printException(e);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public void saveVillageList()
/*      */   {
/*  868 */     if (this.world.field_72995_K) {
/*  869 */       return;
/*      */     }
/*      */     
/*  872 */     File millenaireDir = new File(this.saveDir, "millenaire");
/*      */     
/*  874 */     if (!millenaireDir.exists()) {
/*  875 */       millenaireDir.mkdir();
/*      */     }
/*      */     
/*  878 */     File villageLog = new File(millenaireDir, "villages.txt");
/*      */     
/*      */     try
/*      */     {
/*  882 */       BufferedWriter writer = MillCommonUtilities.getWriter(villageLog);
/*      */       
/*  884 */       for (int i = 0; i < this.villagesList.pos.size(); i++) {
/*  885 */         Point p = (Point)this.villagesList.pos.get(i);
/*      */         
/*  887 */         String generatedFor = (String)this.villagesList.generatedFor.get(i);
/*      */         
/*  889 */         if (generatedFor == null) {
/*  890 */           generatedFor = "";
/*      */         }
/*      */         
/*  893 */         writer.write((String)this.villagesList.names.get(i) + ";" + p.getiX() + "/" + p.getiY() + "/" + p.getiZ() + ";" + (String)this.villagesList.types.get(i) + ";" + (String)this.villagesList.cultures.get(i) + ";" + generatedFor + System.getProperty("line.separator"));
/*      */       }
/*      */       
/*  896 */       writer.flush();
/*  897 */       if (MLN.LogWorldGeneration >= 1) {
/*  898 */         MLN.major(null, "Saved " + this.villagesList.names.size() + " village positions.");
/*      */       }
/*      */     }
/*      */     catch (IOException e) {
/*  902 */       MLN.printException(e);
/*      */     }
/*      */   }
/*      */   
/*      */   public void saveWorldConfig()
/*      */   {
/*  908 */     if (this.world.field_72995_K) {
/*  909 */       return;
/*      */     }
/*      */     
/*  912 */     File configFile = new File(this.millenaireDir, "config.txt");
/*      */     
/*      */     try
/*      */     {
/*  916 */       BufferedWriter writer = MillCommonUtilities.getWriter(configFile);
/*      */       
/*  918 */       writer.write("generate_villages=" + MLN.generateVillages + MLN.EOL);
/*      */       
/*  920 */       writer.flush();
/*      */     }
/*      */     catch (IOException e) {
/*  923 */       MLN.printException(e);
/*      */     }
/*      */   }
/*      */   
/*      */   public void sendVillageListPacket(EntityPlayer player) {
/*  928 */     DataOutput data = ServerSender.getNewByteBufOutputStream();
/*      */     try
/*      */     {
/*  931 */       data.write(9);
/*      */       
/*  933 */       data.writeInt(this.villagesList.pos.size());
/*  934 */       for (int i = 0; i < this.villagesList.pos.size(); i++) {
/*  935 */         StreamReadWrite.writeNullablePoint((Point)this.villagesList.pos.get(i), data);
/*  936 */         StreamReadWrite.writeNullableString((String)this.villagesList.names.get(i), data);
/*  937 */         StreamReadWrite.writeNullableString((String)this.villagesList.cultures.get(i), data);
/*  938 */         StreamReadWrite.writeNullableString((String)this.villagesList.types.get(i), data);
/*      */       }
/*      */       
/*  941 */       data.writeInt(this.loneBuildingsList.pos.size());
/*  942 */       for (int i = 0; i < this.loneBuildingsList.pos.size(); i++) {
/*  943 */         StreamReadWrite.writeNullablePoint((Point)this.loneBuildingsList.pos.get(i), data);
/*  944 */         StreamReadWrite.writeNullableString((String)this.loneBuildingsList.names.get(i), data);
/*  945 */         StreamReadWrite.writeNullableString((String)this.loneBuildingsList.cultures.get(i), data);
/*  946 */         StreamReadWrite.writeNullableString((String)this.loneBuildingsList.types.get(i), data);
/*      */       }
/*      */     }
/*      */     catch (IOException e) {
/*  950 */       MLN.printException(this + ": Error in sendVillageListPacket", e);
/*      */     }
/*      */     
/*  953 */     ServerSender.sendPacketToPlayer(ServerSender.createServerPacket(data), player);
/*      */   }
/*      */   
/*      */   public void setGlobalTag(String tag) {
/*  957 */     if (!this.globalTags.contains(tag)) {
/*  958 */       this.globalTags.add(tag);
/*  959 */       saveGlobalTags();
/*      */       
/*  961 */       if (!this.world.field_72995_K) {
/*  962 */         for (UserProfile up : this.profiles.values()) {
/*  963 */           if (up.connected) {
/*  964 */             up.sendProfilePacket(7);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public void testLocations(String label)
/*      */   {
/*  973 */     if (!MLN.DEV) {
/*  974 */       return;
/*      */     }
/*      */     
/*  977 */     for (Building b : allBuildings())
/*      */     {
/*      */       try
/*      */       {
/*  981 */         if (b.location != null)
/*      */         {
/*  983 */           String tags = "";
/*      */           
/*  985 */           for (String s : b.location.tags) {
/*  986 */             tags = tags + s + ";";
/*      */           }
/*      */           
/*  989 */           if (!buildingsTags.containsKey(b.getPos())) {
/*  990 */             MLN.minor(null, "Detected new building: " + b + " with tags: " + tags);
/*  991 */             buildingsTags.put(b.getPos(), tags);
/*      */           }
/*  993 */           else if (!tags.equals(buildingsTags.get(b.getPos()))) {
/*  994 */             MLN.warning(null, "Testing locations due to: " + label);
/*  995 */             MLN.warning(null, "Tags changed for building: " + b + ". Was: " + (String)buildingsTags.get(b.getPos()) + " now: " + tags);
/*  996 */             buildingsTags.put(b.getPos(), tags);
/*      */           }
/*      */           
/*      */ 
/* 1000 */           if (!buildingsVariation.containsKey(b.getPos())) {
/* 1001 */             MLN.minor(null, "Detected new building: " + b + " with variation: " + b.location.getVariation());
/* 1002 */             buildingsVariation.put(b.getPos(), Integer.valueOf(b.location.getVariation()));
/*      */           }
/* 1004 */           else if (!((Integer)buildingsVariation.get(b.getPos())).equals(Integer.valueOf(b.location.getVariation()))) {
/* 1005 */             MLN.warning(null, "Testing locations due to: " + label);
/* 1006 */             MLN.warning(null, "Variation changed for building: " + b + ". Was: " + buildingsVariation.get(b.getPos()) + " now: " + b.location.getVariation());
/* 1007 */             buildingsVariation.put(b.getPos(), Integer.valueOf(b.location.getVariation()));
/*      */           }
/*      */           
/*      */ 
/* 1011 */           if (!buildingsLocation.containsKey(b.getPos())) {
/* 1012 */             MLN.minor(null, "Detected new building: " + b + " with location key: " + b.location.planKey);
/* 1013 */             buildingsLocation.put(b.getPos(), b.location.planKey);
/*      */           }
/* 1015 */           else if (!b.location.planKey.equals(buildingsLocation.get(b.getPos()))) {
/* 1016 */             MLN.warning(null, "Testing locations due to: " + label);
/* 1017 */             MLN.warning(null, "Location key changed for building: " + b + ". Was: " + (String)buildingsLocation.get(b.getPos()) + " now: " + b.location.planKey);
/* 1018 */             buildingsLocation.put(b.getPos(), b.location.planKey);
/*      */           }
/*      */         }
/*      */       }
/*      */       catch (Exception e) {
/* 1023 */         MLN.printException("Error in dev monitoring of a building building: ", e);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private void testLog()
/*      */   {
/* 1030 */     if (!MLN.logPerformed) {
/* 1031 */       if (Mill.proxy.isTrueServer()) {
/* 1032 */         MillCommonUtilities.logInstance(this.world);
/* 1033 */       } else if (!(this.world instanceof WorldServer)) {
/* 1034 */         MillCommonUtilities.logInstance(this.world);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public String toString()
/*      */   {
/* 1041 */     return "World(" + this.world.func_72912_H().func_76063_b() + ")";
/*      */   }
/*      */   
/*      */   public void updateWorldClient(boolean surfaceLoaded)
/*      */   {
/* 1046 */     if ((!Mill.checkedMillenaireDir) && ((!Mill.proxy.getBaseDir().exists()) || (!new File(Mill.proxy.getBaseDir(), "config.txt").exists()))) {
/* 1047 */       Mill.proxy.sendChatAdmin("The millenaire directory could not be found. It should be inside the minecraft directory, alongside \"bin\".");
/* 1048 */       Mill.proxy.sendChatAdmin("Le dossier millenaire est introuvable. Il devrait être dans le dossier minecraft, à côté de \"bin\".");
/*      */     }
/*      */     
/* 1051 */     Mill.checkedMillenaireDir = true;
/*      */     
/* 1053 */     if (surfaceLoaded) {
/* 1054 */       for (Building b : allBuildings()) {
/* 1055 */         b.updateBuildingClient();
/*      */       }
/*      */     }
/*      */     
/* 1059 */     Mill.proxy.checkTextureSize();
/*      */     
/* 1061 */     testLog();
/*      */   }
/*      */   
/*      */ 
/*      */   public void updateWorldServer()
/*      */   {
/* 1067 */     for (Building b : allBuildings()) {
/* 1068 */       b.updateBuildingServer();
/* 1069 */       b.updateBackgroundVillage();
/*      */     }
/*      */     
/* 1072 */     checkConnections();
/*      */     
/* 1074 */     for (UserProfile profile : this.profiles.values()) {
/* 1075 */       if ((!profile.connected) && (profile.getPlayer() != null)) {
/* 1076 */         profile.connectUser();
/*      */       }
/* 1078 */       if (profile.connected) {
/* 1079 */         profile.updateProfile();
/*      */       }
/*      */     }
/*      */     
/* 1083 */     for (Object o : this.world.field_73010_i) {
/* 1084 */       EntityPlayer player = (EntityPlayer)o;
/*      */       
/*      */ 
/*      */ 
/* 1088 */       SpecialQuestActions.onTick(this, player);
/*      */     }
/*      */     
/* 1091 */     if (MLN.DEV)
/*      */     {
/*      */ 
/*      */ 
/* 1095 */       DevModUtilities.runAutoMove(this.world);
/*      */     }
/*      */     
/* 1098 */     for (Point p : this.renameNames.keySet()) {
/* 1099 */       if (this.buildings.containsKey(p)) {
/* 1100 */         Building b = (Building)this.buildings.get(p);
/* 1101 */         b.changeVillageName((String)this.renameNames.get(p));
/* 1102 */         for (int i = 0; i < this.villagesList.pos.size(); i++) {
/* 1103 */           if (((Point)this.villagesList.pos.get(i)).equals(p)) {
/* 1104 */             this.villagesList.names.add(i, b.getVillageQualifiedName());
/*      */           }
/*      */         }
/* 1107 */         for (int i = 0; i < this.loneBuildingsList.pos.size(); i++) {
/* 1108 */           if (((Point)this.loneBuildingsList.pos.get(i)).equals(p)) {
/* 1109 */             this.loneBuildingsList.names.add(i, b.getVillageQualifiedName());
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 1114 */     for (Point p : this.renameQualifiers.keySet()) {
/* 1115 */       if (this.buildings.containsKey(p)) {
/* 1116 */         Building b = (Building)this.buildings.get(p);
/* 1117 */         b.changeVillageQualifier((String)this.renameQualifiers.get(p));
/* 1118 */         for (int i = 0; i < this.villagesList.pos.size(); i++) {
/* 1119 */           if (((Point)this.villagesList.pos.get(i)).equals(p)) {
/* 1120 */             this.villagesList.names.add(i, b.getVillageQualifiedName());
/*      */           }
/*      */         }
/* 1123 */         for (int i = 0; i < this.loneBuildingsList.pos.size(); i++) {
/* 1124 */           if (((Point)this.loneBuildingsList.pos.get(i)).equals(p)) {
/* 1125 */             this.loneBuildingsList.names.add(i, b.getVillageQualifiedName());
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 1130 */     this.renameNames.clear();
/* 1131 */     this.renameQualifiers.clear();
/*      */     
/* 1133 */     forcePreload();
/*      */     
/* 1135 */     testLog();
/*      */   }
/*      */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\MillWorld.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */