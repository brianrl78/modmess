/*     */ package org.millenaire.common;
/*     */ 
/*     */ import io.netty.buffer.ByteBufInputStream;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.DataOutput;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.util.EnumChatFormatting;
/*     */ import net.minecraft.world.World;
/*     */ import org.millenaire.common.building.Building;
/*     */ import org.millenaire.common.core.MillCommonUtilities;
/*     */ import org.millenaire.common.forge.CommonProxy;
/*     */ import org.millenaire.common.forge.Mill;
/*     */ import org.millenaire.common.forge.MillAchievements;
/*     */ import org.millenaire.common.network.ServerSender;
/*     */ import org.millenaire.common.network.StreamReadWrite;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class UserProfile
/*     */ {
/*     */   public static final String OLD_PROFILE_SINGLE_PLAYER = "SinglePlayer";
/*     */   private static final int CULTURE_MAX_REPUTATION = 4096;
/*     */   private static final int CULTURE_MIN_REPUTATION = -640;
/*     */   public static final int UPDATE_ALL = 1;
/*     */   public static final int UPDATE_REPUTATION = 2;
/*     */   public static final int UPDATE_DIPLOMACY = 3;
/*     */   public static final int UPDATE_ACTIONDATA = 4;
/*     */   public static final int UPDATE_TAGS = 5;
/*     */   public static final int UPDATE_LANGUAGE = 6;
/*     */   public static final int UPDATE_GLOBAL_TAGS = 7;
/*     */   
/*     */   public static UserProfile readProfile(MillWorld world, File dir)
/*     */   {
/*  45 */     UserProfile profile = new UserProfile(world, dir.getName(), dir.getName());
/*     */     
/*  47 */     profile.loadProfileConfig(new File(profile.getDir(), "config.txt"));
/*  48 */     profile.loadProfileTags();
/*  49 */     profile.loadActionData(new File(profile.getDir(), "actiondata.txt"));
/*  50 */     profile.loadQuestInstances(new File(profile.getDir(), "quests.txt"));
/*     */     
/*  52 */     profile.loadLegacyFiles();
/*     */     
/*  54 */     return profile;
/*     */   }
/*     */   
/*  57 */   private final HashMap<Point, Integer> villageReputations = new HashMap();
/*  58 */   private final HashMap<Point, Byte> villageDiplomacy = new HashMap();
/*  59 */   private final HashMap<String, Integer> cultureReputations = new HashMap();
/*  60 */   private final HashMap<String, Integer> cultureLanguages = new HashMap();
/*     */   
/*  62 */   private final List<String> profileTags = new ArrayList();
/*     */   public String key;
/*     */   public String playerName;
/*  65 */   public List<Quest.QuestInstance> questInstances = new ArrayList();
/*  66 */   public HashMap<Long, Quest.QuestInstance> villagersInQuests = new HashMap();
/*     */   
/*  68 */   private final HashMap<String, String> actionData = new HashMap();
/*     */   
/*     */   private final MillWorld mw;
/*  71 */   boolean connectionActionDone = false;
/*  72 */   public boolean connected = false;
/*     */   
/*  74 */   private boolean showNewWorldMessageDone = false;
/*     */   
/*  76 */   public String releaseNumber = null;
/*     */   
/*  78 */   public HashMap<Point, Integer> panelsSent = new HashMap();
/*  79 */   public HashMap<Point, Long> buildingsSent = new HashMap();
/*     */   
/*     */   public UserProfile(MillWorld world, String key, String name) {
/*  82 */     this.key = key;
/*  83 */     this.playerName = name;
/*  84 */     this.mw = world;
/*     */   }
/*     */   
/*     */   public void adjustDiplomacyPoint(Building b, int change)
/*     */   {
/*  89 */     int dp = 0;
/*     */     
/*  91 */     if (this.villageDiplomacy.containsKey(b.getPos())) {
/*  92 */       dp = ((Byte)this.villageDiplomacy.get(b.getPos())).byteValue();
/*     */     }
/*     */     
/*  95 */     dp += change;
/*     */     
/*  97 */     if (dp > 5) {
/*  98 */       dp = 5;
/*     */     }
/* 100 */     if (dp < 0) {
/* 101 */       dp = 0;
/*     */     }
/*     */     
/* 104 */     this.villageDiplomacy.put(b.getPos(), Byte.valueOf((byte)dp));
/*     */     
/* 106 */     saveProfileConfig();
/* 107 */     sendProfilePacket(3);
/*     */   }
/*     */   
/*     */   public void adjustLanguage(String culture, int change)
/*     */   {
/* 112 */     if (this.cultureLanguages.containsKey(culture)) {
/* 113 */       this.cultureLanguages.put(culture, Integer.valueOf(((Integer)this.cultureLanguages.get(culture)).intValue() + change));
/*     */     } else {
/* 115 */       this.cultureLanguages.put(culture, Integer.valueOf(change));
/*     */     }
/*     */     
/* 118 */     saveProfileConfig();
/* 119 */     sendProfilePacket(6);
/*     */   }
/*     */   
/*     */   public void adjustReputation(Building b, int change)
/*     */   {
/* 124 */     if (b == null) {
/* 125 */       return;
/*     */     }
/*     */     
/* 128 */     if (this.villageReputations.containsKey(b.getPos())) {
/* 129 */       this.villageReputations.put(b.getPos(), Integer.valueOf(((Integer)this.villageReputations.get(b.getPos())).intValue() + change));
/*     */     } else {
/* 131 */       this.villageReputations.put(b.getPos(), Integer.valueOf(change));
/*     */     }
/*     */     
/* 134 */     int rep = 0;
/*     */     
/* 136 */     if (this.cultureReputations.containsKey(b.culture.key)) {
/* 137 */       rep = ((Integer)this.cultureReputations.get(b.culture.key)).intValue();
/*     */     }
/*     */     
/* 140 */     rep += change / 10;
/*     */     
/* 142 */     rep = Math.max(64896, rep);
/* 143 */     rep = Math.min(4096, rep);
/*     */     
/* 145 */     this.cultureReputations.put(b.culture.key, Integer.valueOf(rep));
/*     */     
/* 147 */     if (rep <= 64896)
/*     */     {
/* 149 */       int nbAwfulRep = 0;
/*     */       
/* 151 */       for (Iterator i$ = this.cultureReputations.values().iterator(); i$.hasNext();) { int cultureRep = ((Integer)i$.next()).intValue();
/* 152 */         if (cultureRep <= 64896) {
/* 153 */           nbAwfulRep++;
/*     */         }
/*     */       }
/*     */       
/* 157 */       if (nbAwfulRep >= 3) {
/* 158 */         getPlayer().func_71064_a(MillAchievements.attila, 1);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 163 */     saveProfileConfig();
/*     */     
/* 165 */     sendProfilePacket(2);
/*     */   }
/*     */   
/*     */   public void changeProfileKey(String newKey) {
/* 169 */     MillCommonUtilities.deleteDir(getDir());
/* 170 */     this.key = newKey;
/* 171 */     saveProfile();
/*     */   }
/*     */   
/*     */   public void clearActionData(String key) {
/* 175 */     if (this.actionData.containsKey(key)) {
/* 176 */       this.actionData.remove(key);
/* 177 */       saveActionData();
/* 178 */       sendProfilePacket(4);
/*     */     }
/*     */   }
/*     */   
/*     */   private void clearFarAwayPanels() {
/* 183 */     List<Point> farAway = new ArrayList();
/*     */     
/* 185 */     EntityPlayer player = getPlayer();
/*     */     
/* 187 */     for (Point p : this.panelsSent.keySet()) {
/* 188 */       if (p.distanceToSquared(player) > 900.0D) {
/* 189 */         farAway.add(p);
/*     */       }
/*     */     }
/*     */     
/* 193 */     for (Point p : farAway) {
/* 194 */       this.panelsSent.remove(p);
/*     */     }
/*     */   }
/*     */   
/*     */   public void clearTag(String tag) {
/* 199 */     if (this.profileTags.contains(tag)) {
/* 200 */       this.profileTags.remove(tag);
/* 201 */       saveProfileTags();
/* 202 */       sendProfilePacket(5);
/*     */     }
/*     */   }
/*     */   
/*     */   public void connectUser() {
/* 207 */     this.connected = true;
/* 208 */     this.connectionActionDone = false;
/*     */   }
/*     */   
/*     */   private void deleteQuestInstances(long id) {
/* 212 */     List<Long> toDelete = new ArrayList();
/* 213 */     for (Iterator i$ = this.villagersInQuests.keySet().iterator(); i$.hasNext();) { long vid = ((Long)i$.next()).longValue();
/* 214 */       if (((Quest.QuestInstance)this.villagersInQuests.get(Long.valueOf(vid))).uniqueid == id) {
/* 215 */         toDelete.add(Long.valueOf(vid));
/*     */       }
/*     */     }
/*     */     
/* 219 */     for (Iterator i$ = toDelete.iterator(); i$.hasNext();) { long vid = ((Long)i$.next()).longValue();
/* 220 */       this.villagersInQuests.remove(Long.valueOf(vid));
/*     */     }
/*     */     
/* 223 */     for (int i = this.questInstances.size() - 1; i >= 0; i--) {
/* 224 */       if (((Quest.QuestInstance)this.questInstances.get(i)).uniqueid == id) {
/* 225 */         this.questInstances.remove(i);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void disconnectUser() {
/* 231 */     this.connected = false;
/* 232 */     this.panelsSent.clear();
/* 233 */     this.buildingsSent.clear();
/*     */     
/* 235 */     if (MLN.LogNetwork >= 1) {
/* 236 */       MLN.major(this, "Disconnected user.");
/*     */     }
/*     */   }
/*     */   
/*     */   public String getActionData(String key) {
/* 241 */     return (String)this.actionData.get(key);
/*     */   }
/*     */   
/*     */   public int getCultureLanguageKnowledge(String key) {
/* 245 */     if (this.cultureLanguages.containsKey(key)) {
/* 246 */       return ((Integer)this.cultureLanguages.get(key)).intValue();
/*     */     }
/*     */     
/* 249 */     return 0;
/*     */   }
/*     */   
/*     */   public int getCultureReputation(String key) {
/* 253 */     if (this.cultureReputations.containsKey(key)) {
/* 254 */       return ((Integer)this.cultureReputations.get(key)).intValue();
/*     */     }
/*     */     
/* 257 */     return 0;
/*     */   }
/*     */   
/*     */   public int getDiplomacyPoints(Building b)
/*     */   {
/* 262 */     int dp = 0;
/* 263 */     if (this.villageDiplomacy.containsKey(b.getPos())) {
/* 264 */       dp = ((Byte)this.villageDiplomacy.get(b.getPos())).byteValue();
/*     */     }
/*     */     
/* 267 */     return dp;
/*     */   }
/*     */   
/*     */   private File getDir() {
/* 271 */     File dir = new File(new File(this.mw.millenaireDir, "profiles"), this.key);
/*     */     
/* 273 */     if (!dir.exists()) {
/* 274 */       dir.mkdirs();
/*     */     }
/* 276 */     return dir;
/*     */   }
/*     */   
/*     */   public EntityPlayer getPlayer() {
/* 280 */     return this.mw.world.func_72924_a(this.key);
/*     */   }
/*     */   
/*     */   public int getReputation(Building b)
/*     */   {
/* 285 */     int rep = 0;
/* 286 */     if (this.villageReputations.containsKey(b.getPos())) {
/* 287 */       rep = ((Integer)this.villageReputations.get(b.getPos())).intValue();
/*     */     }
/*     */     
/* 290 */     if ((b.culture != null) && (this.cultureReputations.containsKey(b.culture.key))) {
/* 291 */       rep += ((Integer)this.cultureReputations.get(b.culture.key)).intValue();
/*     */     }
/*     */     
/* 294 */     return rep;
/*     */   }
/*     */   
/*     */   public List<String> getWorldQuestStatus()
/*     */   {
/* 299 */     List<String> res = new ArrayList();
/*     */     
/* 301 */     boolean remaining = false;
/*     */     
/* 303 */     for (int i = 0; i < Quest.WORLD_MISSION_NB.length; i++) {
/* 304 */       String status = getActionData(Quest.WORLD_MISSION_KEYS[i] + "queststatus");
/* 305 */       String chapterName = MLN.string("quest.cqchapter" + Quest.WORLD_MISSION_KEYS[i]);
/*     */       
/* 307 */       if (status == null) {
/* 308 */         res.add(MLN.string("quest.cqchapternotstarted", new String[] { chapterName }));
/* 309 */         res.add("");
/* 310 */         res.add(MLN.string("quest.cq" + Quest.WORLD_MISSION_KEYS[i] + "startexplanation"));
/* 311 */         remaining = true;
/*     */       } else {
/* 313 */         int mission = Integer.parseInt(status);
/*     */         
/* 315 */         int nbMission = Quest.WORLD_MISSION_NB[i];
/*     */         
/* 317 */         if (mission >= nbMission) {
/* 318 */           res.add(MLN.string("quest.cqchaptercompleted", new String[] { chapterName }));
/*     */         }
/*     */         else {
/* 321 */           res.add(MLN.string("quest.cqchapterinprogress", new String[] { chapterName, "" + mission, "" + nbMission }));
/* 322 */           remaining = true;
/*     */         }
/*     */       }
/* 325 */       res.add("");
/*     */     }
/*     */     
/* 328 */     if (!remaining) {
/* 329 */       res.add(MLN.string("quest.cqallcompleted"));
/* 330 */       res.add("");
/* 331 */       res.add(MLN.string("quest.cqcheckforupdates"));
/*     */     }
/*     */     
/* 334 */     return res;
/*     */   }
/*     */   
/*     */   private String getWorldQuestStatusShort() {
/* 338 */     String res = MLN.string("quest.creationqueststatusshort") + " ";
/* 339 */     for (int i = 0; i < Quest.WORLD_MISSION_NB.length; i++) {
/* 340 */       String status = getActionData(Quest.WORLD_MISSION_KEYS[i] + "queststatus");
/* 341 */       String chapterName = MLN.string("quest.cqchapter" + Quest.WORLD_MISSION_KEYS[i]);
/*     */       
/* 343 */       if (status == null) {
/* 344 */         res = res + MLN.string("quest.cqchapternotstartedshort", new String[] { chapterName }) + " ";
/*     */       } else {
/* 346 */         int mission = Integer.parseInt(status);
/*     */         
/* 348 */         int nbMission = Quest.WORLD_MISSION_NB[i];
/*     */         
/* 350 */         if (mission >= nbMission) {
/* 351 */           res = res + MLN.string("quest.cqchaptercompletedshort", new String[] { chapterName }) + " ";
/*     */         } else {
/* 353 */           res = res + MLN.string("quest.cqchapterinprogressshort", new String[] { chapterName, "" + mission, "" + nbMission }) + " ";
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 358 */     return res + " " + MLN.string("quest.cqcheckquestlistandhelp", new String[] { Mill.proxy.getQuestKeyName() });
/*     */   }
/*     */   
/*     */   public boolean isTagSet(String tag) {
/* 362 */     return this.profileTags.contains(tag);
/*     */   }
/*     */   
/*     */   public boolean isWorldQuestFinished() {
/* 366 */     boolean remaining = false;
/*     */     
/* 368 */     for (int i = 0; i < Quest.WORLD_MISSION_NB.length; i++) {
/* 369 */       String status = getActionData(Quest.WORLD_MISSION_KEYS[i] + "queststatus");
/* 370 */       if (status == null) {
/* 371 */         remaining = true;
/*     */       } else {
/* 373 */         int mission = Integer.parseInt(status);
/*     */         
/* 375 */         int nbMission = Quest.WORLD_MISSION_NB[i];
/*     */         
/* 377 */         if (mission < nbMission)
/*     */         {
/* 379 */           remaining = true;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 384 */     return !remaining;
/*     */   }
/*     */   
/*     */   private void loadActionData(File dataFile) {
/* 388 */     this.actionData.clear();
/*     */     
/* 390 */     if (dataFile.exists()) {
/*     */       try
/*     */       {
/* 393 */         BufferedReader reader = MillCommonUtilities.getReader(dataFile);
/* 394 */         String line = reader.readLine();
/*     */         
/* 396 */         while (line != null) {
/* 397 */           if ((line.trim().length() > 0) && (line.split(":").length == 2)) {
/* 398 */             this.actionData.put(line.split(":")[0], line.split(":")[1]);
/*     */           }
/* 400 */           line = reader.readLine();
/*     */         }
/*     */         
/* 403 */         if (MLN.LogWorldGeneration >= 1) {
/* 404 */           MLN.major(null, "Loaded " + this.actionData.size() + " action data.");
/*     */         }
/*     */       }
/*     */       catch (Exception e) {
/* 408 */         MLN.printException(e);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void loadLegacyFiles()
/*     */   {
/* 421 */     File millenaireDir = this.mw.millenaireDir;
/*     */     
/* 423 */     File questDataFile = new File(millenaireDir, "quests.txt");
/*     */     
/* 425 */     if (questDataFile.exists()) {
/* 426 */       loadQuestInstances(questDataFile);
/*     */       
/* 428 */       for (String tag : this.mw.globalTags) {
/* 429 */         setTag(tag);
/*     */       }
/*     */       
/* 432 */       File dataFile = new File(millenaireDir, "actiondata.txt");
/*     */       
/* 434 */       if (dataFile != null) {
/* 435 */         loadActionData(dataFile);
/*     */       }
/*     */       
/* 438 */       File configFile = new File(millenaireDir, "config.txt");
/*     */       
/* 440 */       if (configFile != null) {
/* 441 */         loadProfileConfig(configFile);
/*     */       }
/*     */       
/* 444 */       saveProfile();
/*     */       
/* 446 */       questDataFile.delete();
/* 447 */       if (dataFile != null) {
/* 448 */         dataFile.delete();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void loadProfileConfig(File configFile) {
/* 454 */     if ((configFile != null) && (configFile.exists())) {
/*     */       try
/*     */       {
/* 457 */         BufferedReader reader = MillCommonUtilities.getReader(configFile);
/*     */         
/*     */         String line;
/*     */         
/* 461 */         while ((line = reader.readLine()) != null) {
/* 462 */           if ((line.trim().length() > 0) && (!line.startsWith("//"))) {
/* 463 */             String[] temp = line.split("=");
/* 464 */             if (temp.length == 2)
/*     */             {
/* 466 */               String key = temp[0];
/* 467 */               String value = temp[1];
/*     */               
/* 469 */               if (key.equalsIgnoreCase("culture_reputation")) {
/* 470 */                 String c = value.split(",")[0];
/* 471 */                 int level = Integer.parseInt(value.split(",")[1]);
/* 472 */                 this.cultureReputations.put(c, Integer.valueOf(level));
/* 473 */               } else if (key.equalsIgnoreCase("culture_language")) {
/* 474 */                 String c = value.split(",")[0];
/* 475 */                 int level = Integer.parseInt(value.split(",")[1]);
/* 476 */                 this.cultureLanguages.put(c, Integer.valueOf(level));
/* 477 */               } else if (key.equalsIgnoreCase("village_reputations")) {
/* 478 */                 Point p = new Point(value.split(",")[0]);
/* 479 */                 int level = Integer.parseInt(value.split(",")[1]);
/* 480 */                 this.villageReputations.put(p, Integer.valueOf(level));
/* 481 */               } else if (key.equalsIgnoreCase("village_diplomacy")) {
/* 482 */                 Point p = new Point(value.split(",")[0]);
/* 483 */                 int level = Integer.parseInt(value.split(",")[1]);
/* 484 */                 this.villageDiplomacy.put(p, Byte.valueOf((byte)level));
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */         
/* 490 */         reader.close();
/*     */       }
/*     */       catch (IOException e) {
/* 493 */         MLN.printException(e);
/*     */       }
/*     */     }
/*     */     
/* 497 */     if (MLN.LogWorldGeneration >= 1) {
/* 498 */       MLN.major(null, "Config loaded. generateVillages: " + MLN.generateVillages);
/*     */     }
/*     */   }
/*     */   
/*     */   private void loadProfileTags() {
/* 503 */     File tagsFile = new File(getDir(), "tags.txt");
/*     */     
/* 505 */     this.profileTags.clear();
/*     */     
/* 507 */     if (tagsFile.exists()) {
/*     */       try
/*     */       {
/* 510 */         BufferedReader reader = MillCommonUtilities.getReader(tagsFile);
/* 511 */         String line = reader.readLine();
/*     */         
/* 513 */         while (line != null)
/*     */         {
/* 515 */           if (line.trim().length() > 0) {
/* 516 */             this.profileTags.add(line.trim());
/*     */           }
/* 518 */           line = reader.readLine();
/*     */         }
/*     */         
/* 521 */         if (MLN.LogWorldGeneration >= 1) {
/* 522 */           MLN.major(this, "Loaded " + this.profileTags.size() + " tags.");
/*     */         }
/*     */       }
/*     */       catch (Exception e) {
/* 526 */         MLN.printException(e);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void loadQuestInstances(File questDataFile) {
/* 532 */     this.questInstances.clear();
/* 533 */     this.villagersInQuests.clear();
/*     */     try
/*     */     {
/* 536 */       if ((questDataFile != null) && (questDataFile.exists())) {
/* 537 */         BufferedReader reader = MillCommonUtilities.getReader(questDataFile);
/*     */         
/*     */         String line;
/*     */         Quest.QuestInstance qi;
/* 541 */         while ((line = reader.readLine()) != null) {
/* 542 */           if ((line.trim().length() > 0) && (!line.startsWith("//"))) {
/* 543 */             qi = Quest.QuestInstance.loadFromString(this.mw, line, this);
/* 544 */             if (qi != null) {
/* 545 */               this.questInstances.add(qi);
/* 546 */               for (Quest.QuestInstanceVillager qiv : qi.villagers.values()) {
/* 547 */                 this.villagersInQuests.put(Long.valueOf(qiv.id), qi);
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/* 552 */         reader.close();
/*     */       }
/*     */     }
/*     */     catch (IOException e) {
/* 556 */       MLN.printException(e);
/*     */     }
/*     */   }
/*     */   
/*     */   public void receiveDeclareReleaseNumberPacket(ByteBufInputStream ds)
/*     */   {
/*     */     try {
/* 563 */       this.releaseNumber = ds.readUTF();
/*     */       
/* 565 */       if (MLN.LogNetwork >= 1) {
/* 566 */         MLN.major(this, "Declared release number: " + this.releaseNumber);
/*     */       }
/*     */     }
/*     */     catch (IOException e) {
/* 570 */       MLN.printException("Error in receiveDeclareReleaseNumberPacket", e);
/*     */     }
/*     */   }
/*     */   
/*     */   public void receiveProfilePacket(ByteBufInputStream ds)
/*     */   {
/* 576 */     if (MLN.LogNetwork >= 2) {
/* 577 */       MLN.minor(null, "Receiving profile packet");
/*     */     }
/*     */     
/*     */     try
/*     */     {
/* 582 */       int updateType = ds.read();
/*     */       
/*     */ 
/*     */ 
/* 586 */       if ((updateType == 1) || (updateType == 2)) {
/* 587 */         int nb = ds.readInt();
/* 588 */         this.villageReputations.clear();
/* 589 */         for (int i = 0; i < nb; i++) {
/* 590 */           this.villageReputations.put(StreamReadWrite.readNullablePoint(ds), Integer.valueOf(ds.readInt()));
/*     */         }
/*     */         
/* 593 */         nb = ds.readInt();
/* 594 */         this.cultureReputations.clear();
/* 595 */         for (int i = 0; i < nb; i++) {
/* 596 */           String culture = ds.readUTF();
/* 597 */           int rep = ds.readInt();
/* 598 */           this.cultureReputations.put(culture, Integer.valueOf(rep));
/*     */         }
/*     */       }
/*     */       
/* 602 */       if ((updateType == 1) || (updateType == 6)) {
/* 603 */         int nb = ds.readInt();
/* 604 */         this.cultureLanguages.clear();
/* 605 */         for (int i = 0; i < nb; i++) {
/* 606 */           this.cultureLanguages.put(ds.readUTF(), Integer.valueOf(ds.readInt()));
/*     */         }
/*     */       }
/*     */       
/* 610 */       if ((updateType == 1) || (updateType == 3)) {
/* 611 */         int nb = ds.readInt();
/* 612 */         this.villageDiplomacy.clear();
/* 613 */         for (int i = 0; i < nb; i++) {
/* 614 */           this.villageDiplomacy.put(StreamReadWrite.readNullablePoint(ds), Byte.valueOf(ds.readByte()));
/*     */         }
/*     */       }
/*     */       
/* 618 */       if ((updateType == 1) || (updateType == 4)) {
/* 619 */         int nb = ds.readInt();
/* 620 */         this.actionData.clear();
/* 621 */         for (int i = 0; i < nb; i++) {
/* 622 */           this.actionData.put(ds.readUTF(), StreamReadWrite.readNullableString(ds));
/*     */         }
/*     */       }
/*     */       
/* 626 */       if ((updateType == 1) || (updateType == 5)) {
/* 627 */         int nb = ds.readInt();
/* 628 */         this.profileTags.clear();
/* 629 */         for (int i = 0; i < nb; i++) {
/* 630 */           this.profileTags.add(ds.readUTF());
/*     */         }
/*     */       }
/*     */       
/* 634 */       if ((updateType == 1) || (updateType == 7)) {
/* 635 */         int nb = ds.readInt();
/* 636 */         this.mw.globalTags.clear();
/* 637 */         for (int i = 0; i < nb; i++) {
/* 638 */           this.mw.globalTags.add(ds.readUTF());
/*     */         }
/*     */       }
/*     */       
/* 642 */       showNewWorldMessage();
/*     */     }
/*     */     catch (IOException e) {
/* 645 */       MLN.printException("Error in receiveProfilePacket", e);
/*     */     }
/*     */   }
/*     */   
/*     */   public void receiveQuestInstanceDeletePacket(ByteBufInputStream ds) {
/*     */     try {
/* 651 */       deleteQuestInstances(ds.readLong());
/*     */     } catch (IOException e) {
/* 653 */       MLN.printException("Error in receiveQuestInstanceDeletePacket", e);
/*     */     }
/*     */   }
/*     */   
/*     */   public void receiveQuestInstancePacket(ByteBufInputStream ds) {
/*     */     try {
/* 659 */       qi = StreamReadWrite.readNullableQuestInstance(this.mw, ds);
/*     */       
/* 661 */       deleteQuestInstances(qi.uniqueid);
/*     */       
/* 663 */       this.questInstances.add(qi);
/*     */       
/* 665 */       for (String key : qi.villagers.keySet())
/* 666 */         this.villagersInQuests.put(Long.valueOf(((Quest.QuestInstanceVillager)qi.villagers.get(key)).id), qi);
/*     */     } catch (IOException e) {
/*     */       Quest.QuestInstance qi;
/* 669 */       MLN.printException("Error in receiveQuestInstancePacket", e);
/*     */     }
/*     */   }
/*     */   
/*     */   private void saveActionData()
/*     */   {
/* 675 */     if (this.mw.world.field_72995_K) {
/* 676 */       return;
/*     */     }
/*     */     
/* 679 */     File configFile = new File(getDir(), "actiondata.txt");
/*     */     
/*     */     try
/*     */     {
/* 683 */       BufferedWriter writer = MillCommonUtilities.getWriter(configFile);
/*     */       
/* 685 */       for (String key : this.actionData.keySet()) {
/* 686 */         writer.write(key + ":" + (String)this.actionData.get(key) + MLN.EOL);
/*     */       }
/* 688 */       writer.flush();
/*     */     }
/*     */     catch (IOException e) {
/* 691 */       MLN.printException(e);
/*     */     }
/*     */   }
/*     */   
/*     */   public void saveProfile()
/*     */   {
/* 697 */     if (this.mw.world.field_72995_K) {
/* 698 */       return;
/*     */     }
/*     */     
/* 701 */     saveProfileConfig();
/* 702 */     saveProfileTags();
/* 703 */     saveQuestInstances();
/* 704 */     saveActionData();
/*     */   }
/*     */   
/*     */   private void saveProfileConfig()
/*     */   {
/* 709 */     if (this.mw.world.field_72995_K) {
/* 710 */       return;
/*     */     }
/*     */     
/* 713 */     File configFile = new File(getDir(), "config.txt");
/*     */     
/*     */     try
/*     */     {
/* 717 */       BufferedWriter writer = MillCommonUtilities.getWriter(configFile);
/*     */       
/* 719 */       for (String c : this.cultureReputations.keySet()) {
/* 720 */         writer.write("culture_reputation=" + c + "," + this.cultureReputations.get(c) + MLN.EOL);
/*     */       }
/*     */       
/* 723 */       for (String c : this.cultureLanguages.keySet()) {
/* 724 */         writer.write("culture_language=" + c + "," + this.cultureLanguages.get(c) + MLN.EOL);
/*     */       }
/*     */       
/* 727 */       for (Point p : this.villageReputations.keySet()) {
/* 728 */         writer.write("village_reputations=" + p + "," + this.villageReputations.get(p) + MLN.EOL);
/*     */       }
/*     */       
/* 731 */       for (Point p : this.villageDiplomacy.keySet()) {
/* 732 */         writer.write("village_diplomacy=" + p + "," + this.villageDiplomacy.get(p) + MLN.EOL);
/*     */       }
/*     */       
/* 735 */       writer.flush();
/*     */     }
/*     */     catch (IOException e) {
/* 738 */       MLN.printException(e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private void saveProfileTags()
/*     */   {
/* 745 */     if (this.mw.world.field_72995_K) {
/* 746 */       return;
/*     */     }
/*     */     
/* 749 */     File configFile = new File(getDir(), "tags.txt");
/*     */     
/*     */     try
/*     */     {
/* 753 */       BufferedWriter writer = MillCommonUtilities.getWriter(configFile);
/*     */       
/* 755 */       for (String tag : this.profileTags) {
/* 756 */         writer.write(tag + MLN.EOL);
/*     */       }
/* 758 */       writer.flush();
/*     */     }
/*     */     catch (IOException e) {
/* 761 */       MLN.printException(e);
/*     */     }
/*     */   }
/*     */   
/*     */   public void saveQuestInstances()
/*     */   {
/* 767 */     if (this.mw.world.field_72995_K) {
/* 768 */       return;
/*     */     }
/*     */     
/* 771 */     File questDataFile = new File(getDir(), "quests.txt");
/*     */     try
/*     */     {
/* 774 */       BufferedWriter writer = MillCommonUtilities.getWriter(questDataFile);
/* 775 */       for (Quest.QuestInstance qi : this.questInstances) {
/* 776 */         writer.write(qi.writeToString() + MLN.EOL);
/*     */       }
/* 778 */       writer.flush();
/*     */     } catch (IOException e) {
/* 780 */       MLN.printException(e);
/*     */     }
/*     */   }
/*     */   
/*     */   public void sendInitialPackets()
/*     */   {
/* 786 */     if (MLN.LogNetwork >= 1) {
/* 787 */       MLN.major(this, "Sending initial packets.");
/*     */     }
/*     */     
/* 790 */     sendProfilePacket(1);
/*     */     
/* 792 */     for (Quest.QuestInstance qi : this.questInstances) {
/* 793 */       sendQuestInstancePacket(qi);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void sendProfilePacket(int updateType)
/*     */   {
/* 801 */     if (this.mw.world.field_72995_K) {
/* 802 */       return;
/*     */     }
/*     */     
/* 805 */     if (getPlayer() == null) {
/* 806 */       MLN.printException(new MLN.MillenaireException("Null player while trying to send packet:"));
/* 807 */       return;
/*     */     }
/*     */     
/* 810 */     DataOutput data = ServerSender.getNewByteBufOutputStream();
/*     */     try
/*     */     {
/* 813 */       data.write(101);
/* 814 */       data.write(updateType);
/*     */       
/* 816 */       if ((updateType == 1) || (updateType == 2)) {
/* 817 */         data.writeInt(this.villageReputations.size());
/* 818 */         for (Point p : this.villageReputations.keySet()) {
/* 819 */           StreamReadWrite.writeNullablePoint(p, data);
/* 820 */           data.writeInt(((Integer)this.villageReputations.get(p)).intValue());
/*     */         }
/*     */         
/* 823 */         data.writeInt(this.cultureReputations.size());
/* 824 */         for (String culture : this.cultureReputations.keySet()) {
/* 825 */           data.writeUTF(culture);
/* 826 */           data.writeInt(((Integer)this.cultureReputations.get(culture)).intValue());
/*     */         }
/*     */       }
/*     */       
/* 830 */       if ((updateType == 1) || (updateType == 6)) {
/* 831 */         data.writeInt(this.cultureLanguages.size());
/* 832 */         for (String culture : this.cultureLanguages.keySet()) {
/* 833 */           data.writeUTF(culture);
/* 834 */           data.writeInt(((Integer)this.cultureLanguages.get(culture)).intValue());
/*     */         }
/*     */       }
/*     */       
/* 838 */       if ((updateType == 1) || (updateType == 3)) {
/* 839 */         data.writeInt(this.villageDiplomacy.size());
/* 840 */         for (Point p : this.villageDiplomacy.keySet()) {
/* 841 */           StreamReadWrite.writeNullablePoint(p, data);
/* 842 */           data.write(((Byte)this.villageDiplomacy.get(p)).byteValue());
/*     */         }
/*     */       }
/*     */       
/* 846 */       if ((updateType == 1) || (updateType == 4)) {
/* 847 */         data.writeInt(this.actionData.size());
/* 848 */         for (String key : this.actionData.keySet()) {
/* 849 */           data.writeUTF(key);
/* 850 */           StreamReadWrite.writeNullableString((String)this.actionData.get(key), data);
/*     */         }
/*     */       }
/*     */       
/* 854 */       if ((updateType == 1) || (updateType == 5)) {
/* 855 */         data.writeInt(this.profileTags.size());
/* 856 */         for (String tag : this.profileTags) {
/* 857 */           data.writeUTF(tag);
/*     */         }
/*     */       }
/*     */       
/* 861 */       if ((updateType == 1) || (updateType == 7)) {
/* 862 */         data.writeInt(this.mw.globalTags.size());
/* 863 */         for (String tag : this.mw.globalTags) {
/* 864 */           data.writeUTF(tag);
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (IOException e) {
/* 869 */       MLN.printException(this + ": Error in sendProfilePacket", e);
/*     */     }
/*     */     
/* 872 */     ServerSender.createAndSendPacketToPlayer(data, getPlayer());
/*     */   }
/*     */   
/*     */   public void sendQuestInstanceDeletePacket(long id)
/*     */   {
/* 877 */     if (this.mw.world.field_72995_K) {
/* 878 */       return;
/*     */     }
/*     */     
/* 881 */     DataOutput data = ServerSender.getNewByteBufOutputStream();
/*     */     try
/*     */     {
/* 884 */       data.write(103);
/* 885 */       data.writeLong(id);
/*     */     } catch (IOException e) {
/* 887 */       MLN.printException(this + ": Error in sendQuestInstanceDeletePacket", e);
/*     */     }
/*     */     
/* 890 */     ServerSender.createAndSendPacketToPlayer(data, getPlayer());
/*     */   }
/*     */   
/*     */   public void sendQuestInstancePacket(Quest.QuestInstance qi)
/*     */   {
/* 895 */     if (this.mw.world.field_72995_K) {
/* 896 */       return;
/*     */     }
/*     */     
/*     */ 
/* 900 */     for (Quest.QuestInstanceVillager qiv : qi.villagers.values()) {
/* 901 */       Building th = qiv.getTownHall(this.mw.world);
/* 902 */       if ((th != null) && 
/* 903 */         (!this.buildingsSent.containsKey(th.getPos()))) {
/* 904 */         th.sendBuildingPacket(getPlayer(), false);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 909 */     DataOutput data = ServerSender.getNewByteBufOutputStream();
/*     */     try
/*     */     {
/* 912 */       data.write(102);
/* 913 */       StreamReadWrite.writeNullableQuestInstance(qi, data);
/*     */     }
/*     */     catch (IOException e) {
/* 916 */       MLN.printException(this + ": Error in sendQuestInstancePacket", e);
/*     */     }
/*     */     
/* 919 */     ServerSender.createAndSendPacketToPlayer(data, getPlayer());
/*     */   }
/*     */   
/*     */   public void setActionData(String key, String value) {
/* 923 */     if ((!this.actionData.containsKey(key)) || (!((String)this.actionData.get(key)).equals(value))) {
/* 924 */       this.actionData.put(key, value);
/* 925 */       saveActionData();
/* 926 */       sendProfilePacket(4);
/*     */     }
/*     */   }
/*     */   
/*     */   public void setTag(String tag) {
/* 931 */     if (!this.profileTags.contains(tag)) {
/* 932 */       this.profileTags.add(tag);
/* 933 */       saveProfileTags();
/* 934 */       sendProfilePacket(5);
/*     */     }
/*     */   }
/*     */   
/*     */   public void showNewWorldMessage() {
/* 939 */     if (!this.showNewWorldMessageDone) {
/* 940 */       ServerSender.sendChat(getPlayer(), EnumChatFormatting.YELLOW, getWorldQuestStatusShort());
/* 941 */       this.showNewWorldMessageDone = true;
/*     */     }
/*     */   }
/*     */   
/*     */   public void testQuests()
/*     */   {
/* 947 */     if (!this.mw.world.field_72995_K) {
/* 948 */       boolean change = false;
/*     */       
/* 950 */       for (int i = this.questInstances.size() - 1; i >= 0; i--) {
/* 951 */         Quest.QuestInstance qi = (Quest.QuestInstance)this.questInstances.get(i);
/* 952 */         change |= qi.checkStatus(this.mw.world);
/*     */       }
/*     */       
/* 955 */       for (Quest q : Quest.quests.values()) {
/* 956 */         Quest.QuestInstance qi = q.testQuest(this.mw, this);
/* 957 */         change |= qi != null;
/*     */         
/* 959 */         if (qi != null) {
/* 960 */           sendQuestInstancePacket(qi);
/*     */         }
/*     */       }
/* 963 */       if (change) {
/* 964 */         saveQuestInstances();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 971 */     return "Profile: " + this.key + "/" + this.playerName;
/*     */   }
/*     */   
/*     */   public void updateProfile() {
/* 975 */     EntityPlayer player = getPlayer();
/*     */     
/* 977 */     if (this.connected) {
/* 978 */       if (player != null) {
/* 979 */         clearFarAwayPanels();
/*     */         
/* 981 */         if ((player.field_71093_bK == 0) && 
/* 982 */           (!this.connectionActionDone) && (!this.mw.world.field_72995_K)) {
/* 983 */           sendInitialPackets();
/* 984 */           this.connectionActionDone = true;
/*     */         }
/*     */         
/*     */ 
/* 988 */         if ((player != null) && (this.mw.world.func_72820_D() % 1000L == 0L) && (this.mw.world.func_72935_r())) {
/* 989 */           testQuests();
/*     */         }
/*     */         
/* 992 */         if ((MLN.DEV) && (player != null) && (this.mw.world.func_72820_D() % 20L == 0L) && (this.mw.world.func_72935_r())) {
/* 993 */           testQuests();
/*     */         }
/*     */       }
/*     */       else {
/* 997 */         this.connectionActionDone = false;
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\UserProfile.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */