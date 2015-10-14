/*      */ package org.millenaire.common;
/*      */ 
/*      */ import java.io.BufferedReader;
/*      */ import java.io.File;
/*      */ import java.util.ArrayList;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import net.minecraft.entity.item.EntityItem;
/*      */ import net.minecraft.entity.player.EntityPlayer;
/*      */ import net.minecraft.entity.player.InventoryPlayer;
/*      */ import net.minecraft.item.ItemStack;
/*      */ import net.minecraft.item.ItemSword;
/*      */ import net.minecraft.util.EnumChatFormatting;
/*      */ import net.minecraft.world.World;
/*      */ import org.millenaire.common.building.Building;
/*      */ import org.millenaire.common.core.MillCommonUtilities;
/*      */ import org.millenaire.common.core.MillCommonUtilities.ExtFileFilter;
/*      */ import org.millenaire.common.forge.CommonProxy;
/*      */ import org.millenaire.common.forge.Mill;
/*      */ import org.millenaire.common.forge.MillAchievements;
/*      */ import org.millenaire.common.item.Goods;
/*      */ import org.millenaire.common.item.Goods.IItemInitialEnchantmens;
/*      */ 
/*      */ public class Quest
/*      */ {
/*      */   public static class QuestInstance
/*      */   {
/*      */     private static final int QUEST_LANGUAGE_BONUS = 50;
/*      */     
/*      */     public static QuestInstance loadFromString(MillWorld mw, String line, UserProfile profile)
/*      */     {
/*   33 */       Quest q = null;
/*   34 */       int step = 0;
/*   35 */       long startTime = 0L;long stepStartTime = 0L;
/*      */       
/*   37 */       HashMap<String, Quest.QuestInstanceVillager> villagers = new HashMap();
/*      */       
/*   39 */       for (String s : line.split(";")) {
/*   40 */         if (s.split(":").length == 2) {
/*   41 */           String key = s.split(":")[0];
/*   42 */           String value = s.split(":")[1];
/*      */           
/*   44 */           if (key.equals("quest")) {
/*   45 */             if (Quest.quests.containsKey(value)) {
/*   46 */               q = (Quest)Quest.quests.get(value);
/*      */             } else {
/*   48 */               MLN.error(null, "Could not find quest '" + value + "'.");
/*      */             }
/*   50 */           } else if (key.equals("startTime")) {
/*   51 */             startTime = Long.parseLong(value);
/*   52 */           } else if (key.equals("currentStepStartTime")) {
/*   53 */             stepStartTime = Long.parseLong(value);
/*   54 */           } else if (key.equals("step")) {
/*   55 */             step = Integer.parseInt(value);
/*   56 */           } else if (key.equals("villager")) {
/*   57 */             String[] vals = value.split(",");
/*   58 */             Quest.QuestInstanceVillager qiv = new Quest.QuestInstanceVillager(mw, new Point(vals[2]), Long.parseLong(vals[1]));
/*   59 */             villagers.put(vals[0], qiv);
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*   64 */       if ((q != null) && (villagers.size() > 0)) {
/*   65 */         return new QuestInstance(mw, q, profile, villagers, startTime, step, stepStartTime);
/*      */       }
/*   67 */       return null;
/*      */     }
/*      */     
/*   70 */     public int currentStep = 0;
/*      */     public long currentStepStart;
/*      */     public Quest quest;
/*      */     public long startTime;
/*      */     public HashMap<String, Quest.QuestInstanceVillager> villagers;
/*   75 */     public UserProfile profile = null;
/*      */     public MillWorld mw;
/*      */     public World world;
/*      */     public long uniqueid;
/*      */     
/*      */     public QuestInstance(MillWorld mw, Quest quest, UserProfile profile, HashMap<String, Quest.QuestInstanceVillager> villagers, long startTime)
/*      */     {
/*   82 */       this(mw, quest, profile, villagers, startTime, 0, startTime);
/*      */     }
/*      */     
/*      */     public QuestInstance(MillWorld mw, Quest quest, UserProfile profile, HashMap<String, Quest.QuestInstanceVillager> villagers, long startTime, int step, long stepStartTime)
/*      */     {
/*   87 */       this.mw = mw;
/*   88 */       this.world = mw.world;
/*   89 */       this.villagers = villagers;
/*   90 */       this.quest = quest;
/*   91 */       this.currentStep = step;
/*   92 */       this.startTime = startTime;
/*   93 */       this.profile = profile;
/*   94 */       this.currentStepStart = stepStartTime;
/*   95 */       this.uniqueid = ((Math.random() * 9.223372036854776E18D));
/*      */     }
/*      */     
/*      */     private void applyActionData(List<String[]> data) {
/*   99 */       for (String[] val : data) {
/*  100 */         this.profile.setActionData(val[0], val[1]);
/*      */       }
/*      */     }
/*      */     
/*      */     private void applyGlobalTags(List<String> set, List<String> clear) {
/*  105 */       if (MLN.LogQuest >= 3) {
/*  106 */         MLN.debug(this, "Applying " + set.size() + " global tags, clearing " + clear.size() + " global tags.");
/*      */       }
/*  108 */       for (String val : set) {
/*  109 */         this.mw.setGlobalTag(val);
/*      */       }
/*  111 */       for (String val : clear) {
/*  112 */         this.mw.clearGlobalTag(val);
/*      */       }
/*      */     }
/*      */     
/*      */     private void applyPlayerTags(List<String> set, List<String> clear) {
/*  117 */       if (MLN.LogQuest >= 3) {
/*  118 */         MLN.debug(this, "Applying " + set.size() + " player tags, clearing " + clear.size() + " player tags.");
/*      */       }
/*  120 */       for (String val : set) {
/*  121 */         this.profile.setTag(val);
/*      */       }
/*  123 */       for (String val : clear) {
/*  124 */         this.profile.clearTag(val);
/*      */       }
/*      */     }
/*      */     
/*      */     private void applyTags(List<String[]> set, List<String[]> clear) {
/*  129 */       if (MLN.LogQuest >= 3) {
/*  130 */         MLN.debug(this, "Applying " + set.size() + " tags, clearing " + clear.size() + " tags.");
/*      */       }
/*  132 */       for (String[] val : set)
/*      */       {
/*  134 */         String tag = this.profile.key + "_" + val[1];
/*      */         
/*  136 */         if (MLN.LogQuest >= 3) {
/*  137 */           MLN.debug(this, "Applying tag: " + val[0] + "/" + tag);
/*      */         }
/*  139 */         if (!((Quest.QuestInstanceVillager)this.villagers.get(val[0])).getVillagerRecord(this.world).questTags.contains(tag)) {
/*  140 */           ((Quest.QuestInstanceVillager)this.villagers.get(val[0])).getVillagerRecord(this.world).questTags.add(tag);
/*  141 */           ((Quest.QuestInstanceVillager)this.villagers.get(val[0])).getVillagerRecord(this.world).getTownHall().requestSave("quest tag");
/*  142 */           if (MLN.LogQuest >= 2) {
/*  143 */             MLN.minor(this, "Setting tag: " + tag + " on villager: " + val[0] + " (" + ((Quest.QuestInstanceVillager)this.villagers.get(val[0])).getVillagerRecord(this.world).getName() + ") Now present: " + ((Quest.QuestInstanceVillager)this.villagers.get(val[0])).getVillagerRecord(this.world).questTags.size());
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*  149 */       for (String[] val : clear) {
/*  150 */         String tag = this.profile.key + "_" + val[1];
/*      */         
/*  152 */         if (MLN.LogQuest >= 3) {
/*  153 */           MLN.debug(this, "Clearing tag: " + val[0] + "/" + tag);
/*      */         }
/*  155 */         ((Quest.QuestInstanceVillager)this.villagers.get(val[0])).getVillagerRecord(this.world).questTags.remove(tag);
/*  156 */         ((Quest.QuestInstanceVillager)this.villagers.get(val[0])).getVillagerRecord(this.world).getTownHall().requestSave("quest tag");
/*  157 */         if (MLN.LogQuest >= 2) {
/*  158 */           MLN.minor(this, "Clearing tag: " + tag + " on villager: " + val[0] + " (" + ((Quest.QuestInstanceVillager)this.villagers.get(val[0])).getVillagerRecord(this.world).getName() + ")");
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */     public boolean checkStatus(World world)
/*      */     {
/*  165 */       if (this.currentStepStart + getCurrentStep().duration * 1000 <= world.func_72820_D()) {
/*  166 */         MillVillager cv = getCurrentVillager().getVillager(world);
/*  167 */         if ((cv != null) && (getCurrentStep().penaltyReputation > 0)) {
/*  168 */           this.profile.adjustReputation(cv.getTownHall(), -getCurrentStep().penaltyReputation);
/*      */         }
/*      */         
/*  171 */         applyTags(getCurrentStep().setVillagerTagsFailure, getCurrentStep().clearTagsFailure);
/*  172 */         applyGlobalTags(getCurrentStep().setGlobalTagsFailure, getCurrentStep().clearGlobalTagsFailure);
/*  173 */         applyPlayerTags(getCurrentStep().setPlayerTagsFailure, getCurrentStep().clearPlayerTagsFailure);
/*      */         
/*  175 */         if (getCurrentStep().getDescriptionTimeUp() != null) {
/*  176 */           org.millenaire.common.network.ServerSender.sendChat(this.profile.getPlayer(), EnumChatFormatting.RED, getDescriptionTimeUp(this.profile) + " (" + MLN.string("quest.reputationlost") + ": " + getCurrentStep().penaltyReputation + ")");
/*      */         }
/*      */         
/*      */ 
/*  180 */         destroySelf();
/*      */         
/*  182 */         return true;
/*      */       }
/*  184 */       return false;
/*      */     }
/*      */     
/*      */     public String completeStep(EntityPlayer player, MillVillager villager)
/*      */     {
/*  189 */       String reward = "";
/*      */       
/*  191 */       for (InvItem item : getCurrentStep().requiredGood.keySet()) {
/*  192 */         if (item.special == 0) {
/*  193 */           villager.addToInv(item.getItem(), item.meta, ((Integer)getCurrentStep().requiredGood.get(item)).intValue());
/*  194 */           MillCommonUtilities.getItemsFromChest(player.field_71071_by, item.getItem(), item.meta, ((Integer)getCurrentStep().requiredGood.get(item)).intValue());
/*      */         }
/*      */       }
/*      */       
/*  198 */       for (InvItem item : getCurrentStep().rewardGoods.keySet()) {
/*  199 */         int nbLeft = ((Integer)getCurrentStep().rewardGoods.get(item)).intValue() - MillCommonUtilities.putItemsInChest(player.field_71071_by, item.getItem(), item.meta, ((Integer)getCurrentStep().rewardGoods.get(item)).intValue());
/*      */         
/*  201 */         if (nbLeft > 0) {
/*  202 */           EntityItem entItem = MillCommonUtilities.spawnItem(this.world, villager.getPos(), new ItemStack(item.getItem(), nbLeft, item.meta), 0.0F);
/*      */           
/*  204 */           if ((entItem.func_92059_d().func_77973_b() instanceof Goods.IItemInitialEnchantmens)) {
/*  205 */             ((Goods.IItemInitialEnchantmens)entItem.func_92059_d().func_77973_b()).applyEnchantments(entItem.func_92059_d());
/*      */           }
/*      */         }
/*      */         
/*  209 */         reward = reward + " " + getCurrentStep().rewardGoods.get(item) + " " + item.getName();
/*      */       }
/*      */       
/*  212 */       if (getCurrentStep().rewardMoney > 0) {
/*  213 */         MillCommonUtilities.changeMoney(player.field_71071_by, getCurrentStep().rewardMoney, player);
/*  214 */         reward = reward + " " + getCurrentStep().rewardMoney + " deniers";
/*      */       }
/*      */       
/*  217 */       if (getCurrentStep().rewardReputation > 0) {
/*  218 */         this.mw.getProfile(player.getDisplayName()).adjustReputation(villager.getTownHall(), getCurrentStep().rewardReputation);
/*      */         
/*  220 */         reward = reward + " " + getCurrentStep().rewardReputation + " reputation";
/*      */         
/*  222 */         int experience = getCurrentStep().rewardReputation / 32;
/*      */         
/*  224 */         if (experience > 16) {
/*  225 */           experience = 16;
/*      */         }
/*      */         
/*  228 */         if (experience > 0) {
/*  229 */           reward = reward + " " + experience + " experience";
/*  230 */           MillCommonUtilities.spawnExp(this.world, villager.getPos().getRelative(0.0D, 2.0D, 0.0D), experience);
/*      */         }
/*      */       }
/*      */       
/*  234 */       this.mw.getProfile(player.getDisplayName()).adjustLanguage(villager.getCulture().key, 50);
/*      */       
/*  236 */       if (!this.world.field_72995_K)
/*      */       {
/*  238 */         applyTags(getCurrentStep().setVillagerTagsSuccess, getCurrentStep().clearTagsSuccess);
/*  239 */         applyGlobalTags(getCurrentStep().setGlobalTagsSuccess, getCurrentStep().clearGlobalTagsSuccess);
/*  240 */         applyPlayerTags(getCurrentStep().setPlayerTagsSuccess, getCurrentStep().clearPlayerTagsSuccess);
/*  241 */         applyActionData(getCurrentStep().setActionDataSuccess);
/*      */         
/*  243 */         for (String s : getCurrentStep().bedrockbuildings) {
/*  244 */           String culture = s.split(",")[0];
/*  245 */           String village = s.split(",")[1];
/*      */           
/*  247 */           VillageType vt = Culture.getCultureByName(culture).getLoneBuildingType(village);
/*      */           try
/*      */           {
/*  250 */             WorldGenVillage.generateBedrockLoneBuilding(new Point(player), this.world, vt, MillCommonUtilities.random, 50, 120, player);
/*      */           } catch (MLN.MillenaireException e) {
/*  252 */             MLN.printException(e);
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*  257 */       String res = getDescriptionSuccess(this.mw.getProfile(player.getDisplayName()));
/*      */       
/*  259 */       if (reward.length() > 0) {
/*  260 */         res = res + "<ret><ret>" + MLN.string("quest.obtained") + ":" + reward;
/*      */       }
/*      */       
/*  263 */       this.currentStep += 1;
/*  264 */       if (this.currentStep >= this.quest.steps.size()) {
/*  265 */         player.func_71064_a(MillAchievements.thequest, 1);
/*      */         
/*  267 */         if (this.mw.getProfile(player.getDisplayName()).isWorldQuestFinished()) {
/*  268 */           player.func_71064_a(MillAchievements.forbiddenknwoledge, 1);
/*      */         }
/*      */         
/*  271 */         destroySelf();
/*      */       } else {
/*  273 */         this.currentStepStart = villager.field_70170_p.func_72820_D();
/*  274 */         this.profile.sendQuestInstancePacket(this);
/*  275 */         this.profile.saveQuestInstances();
/*      */       }
/*      */       
/*  278 */       return res;
/*      */     }
/*      */     
/*      */     private void destroySelf() {
/*  282 */       this.profile.questInstances.remove(this);
/*  283 */       for (Quest.QuestInstanceVillager qiv : this.villagers.values()) {
/*  284 */         this.profile.villagersInQuests.remove(Long.valueOf(qiv.id));
/*      */       }
/*  286 */       this.profile.saveQuestInstances();
/*      */     }
/*      */     
/*      */     public Quest.QuestStep getCurrentStep() {
/*  290 */       return (Quest.QuestStep)this.quest.steps.get(this.currentStep);
/*      */     }
/*      */     
/*      */     public Quest.QuestInstanceVillager getCurrentVillager() {
/*  294 */       return (Quest.QuestInstanceVillager)this.villagers.get(getCurrentStep().villager);
/*      */     }
/*      */     
/*      */     public String getDescription(UserProfile profile) {
/*  298 */       return handleString(profile, getCurrentStep().getDescription());
/*      */     }
/*      */     
/*      */     public String getDescriptionRefuse(UserProfile profile) {
/*  302 */       return handleString(profile, getCurrentStep().getDescriptionRefuse());
/*      */     }
/*      */     
/*      */     public String getDescriptionSuccess(UserProfile profile) {
/*  306 */       return handleString(profile, getCurrentStep().getDescriptionSuccess());
/*      */     }
/*      */     
/*      */     public String getDescriptionTimeUp(UserProfile profile) {
/*  310 */       return handleString(profile, getCurrentStep().getDescriptionTimeUp());
/*      */     }
/*      */     
/*      */     public String getLabel(UserProfile profile) {
/*  314 */       return handleString(profile, getCurrentStep().getLabel());
/*      */     }
/*      */     
/*      */     public String getListing(UserProfile profile) {
/*  318 */       return handleString(profile, getCurrentStep().getListing());
/*      */     }
/*      */     
/*      */     public Quest.QuestStep getNextStep() {
/*  322 */       if (this.currentStep + 1 < this.quest.steps.size()) {
/*  323 */         return (Quest.QuestStep)this.quest.steps.get(this.currentStep + 1);
/*      */       }
/*  325 */       return null;
/*      */     }
/*      */     
/*      */     public Quest.QuestStep getPreviousStep() {
/*  329 */       if (this.currentStep > 0) {
/*  330 */         return (Quest.QuestStep)this.quest.steps.get(this.currentStep - 1);
/*      */       }
/*  332 */       return null;
/*      */     }
/*      */     
/*      */     private String handleString(UserProfile profile, String s)
/*      */     {
/*  337 */       if (s == null) {
/*  338 */         return null;
/*      */       }
/*      */       
/*  341 */       Building giverTH = ((Quest.QuestInstanceVillager)this.villagers.get(getCurrentStep().villager)).getTownHall(this.world);
/*      */       
/*  343 */       if (giverTH == null) {
/*  344 */         return s;
/*      */       }
/*      */       
/*  347 */       for (Iterator i$ = this.villagers.keySet().iterator(); i$.hasNext();) { key = (String)i$.next();
/*  348 */         Quest.QuestInstanceVillager qiv = (Quest.QuestInstanceVillager)this.villagers.get(key);
/*  349 */         th = qiv.getTownHall(this.world);
/*      */         
/*  351 */         if (th != null) {
/*  352 */           s = s.replaceAll("\\$" + key + "_villagename\\$", th.getVillageQualifiedName());
/*  353 */           s = s.replaceAll("\\$" + key + "_direction\\$", MLN.string(giverTH.getPos().directionTo(th.getPos())));
/*  354 */           s = s.replaceAll("\\$" + key + "_tothedirection\\$", MLN.string(giverTH.getPos().directionTo(th.getPos(), true)));
/*  355 */           s = s.replaceAll("\\$" + key + "_directionshort\\$", MLN.string(giverTH.getPos().directionToShort(th.getPos())));
/*  356 */           s = s.replaceAll("\\$" + key + "_distance\\$", MLN.string(giverTH.getPos().approximateDistanceLongString(th.getPos())));
/*  357 */           s = s.replaceAll("\\$" + key + "_distanceshort\\$", MLN.string(giverTH.getPos().approximateDistanceShortString(th.getPos())));
/*      */           
/*  359 */           VillagerRecord villager = qiv.getVillagerRecord(this.world);
/*      */           
/*  361 */           if (villager != null) {
/*  362 */             s = s.replaceAll("\\$" + key + "_villagername\\$", villager.getName());
/*  363 */             s = s.replaceAll("\\$" + key + "_villagerrole\\$", villager.getGameOccupation(profile.key));
/*      */           }
/*      */           
/*  366 */           for (String key2 : this.villagers.keySet()) {
/*  367 */             Quest.QuestInstanceVillager qiv2 = (Quest.QuestInstanceVillager)this.villagers.get(key2);
/*  368 */             Building th2 = qiv2.getTownHall(this.world);
/*      */             
/*  370 */             if (th2 != null) {
/*  371 */               s = s.replaceAll("\\$" + key + "_" + key2 + "_direction\\$", MLN.string(th.getPos().directionTo(th2.getPos())));
/*  372 */               s = s.replaceAll("\\$" + key + "_" + key2 + "_directionshort\\$", MLN.string(th.getPos().directionToShort(th2.getPos())));
/*  373 */               s = s.replaceAll("\\$" + key + "_" + key2 + "_distance\\$", MLN.string(th.getPos().approximateDistanceLongString(th2.getPos())));
/*  374 */               s = s.replaceAll("\\$" + key + "_" + key2 + "_distanceshort\\$", MLN.string(th.getPos().approximateDistanceShortString(th2.getPos())));
/*      */             } else {
/*  376 */               s = s.replaceAll("\\$" + key + "_" + key2 + "_direction\\$", "");
/*  377 */               s = s.replaceAll("\\$" + key + "_" + key2 + "_directionshort\\$", "");
/*  378 */               s = s.replaceAll("\\$" + key + "_" + key2 + "_distance\\$", "");
/*  379 */               s = s.replaceAll("\\$" + key + "_" + key2 + "_distanceshort\\$", "");
/*      */             }
/*      */           }
/*      */         } }
/*      */       String key;
/*      */       Building th;
/*  385 */       s = s.replaceAll("\\$name", profile.playerName);
/*      */       
/*  387 */       return s;
/*      */     }
/*      */     
/*      */     public String refuseQuest(EntityPlayer player, MillVillager villager)
/*      */     {
/*  392 */       String replost = "";
/*      */       
/*  394 */       MillVillager cv = getCurrentVillager().getVillager(this.world);
/*  395 */       if ((cv != null) && (getCurrentStep().penaltyReputation > 0)) {
/*  396 */         this.mw.getProfile(player.getDisplayName()).adjustReputation(cv.getTownHall(), -getCurrentStep().penaltyReputation);
/*  397 */         replost = " (Reputation lost: " + getCurrentStep().penaltyReputation + ")";
/*      */       }
/*      */       
/*  400 */       applyTags(getCurrentStep().setVillagerTagsFailure, getCurrentStep().clearTagsFailure);
/*  401 */       applyPlayerTags(getCurrentStep().setPlayerTagsFailure, getCurrentStep().clearPlayerTagsFailure);
/*  402 */       applyGlobalTags(getCurrentStep().setGlobalTagsFailure, getCurrentStep().clearGlobalTagsFailure);
/*      */       
/*  404 */       String s = getDescriptionRefuse(this.mw.getProfile(player.getDisplayName())) + replost;
/*      */       
/*  406 */       destroySelf();
/*      */       
/*  408 */       return s;
/*      */     }
/*      */     
/*      */     public String toString()
/*      */     {
/*  413 */       return "QI:" + this.quest.key;
/*      */     }
/*      */     
/*      */     public String writeToString() {
/*  417 */       String s = "quest:" + this.quest.key + ";step:" + this.currentStep + ";startTime:" + this.startTime + ";currentStepStartTime:" + this.currentStepStart;
/*      */       
/*  419 */       for (String key : this.villagers.keySet()) {
/*  420 */         Quest.QuestInstanceVillager qiv = (Quest.QuestInstanceVillager)this.villagers.get(key);
/*  421 */         s = s + ";villager:" + key + "," + qiv.id + "," + qiv.townHall;
/*      */       }
/*  423 */       return s;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public static class QuestInstanceVillager
/*      */   {
/*      */     public long id;
/*      */     public Point townHall;
/*  432 */     private MillVillager villager = null;
/*  433 */     private VillagerRecord vr = null;
/*      */     public MillWorld mw;
/*      */     
/*      */     public QuestInstanceVillager(MillWorld mw, Point p, long vid) {
/*  437 */       this.townHall = p;
/*  438 */       this.id = vid;
/*  439 */       this.mw = mw;
/*      */     }
/*      */     
/*      */     public QuestInstanceVillager(MillWorld mw, Point p, long vid, MillVillager v) {
/*  443 */       this.townHall = p;
/*  444 */       this.id = vid;
/*  445 */       this.villager = v;
/*  446 */       this.mw = mw;
/*      */     }
/*      */     
/*      */     public QuestInstanceVillager(MillWorld mw, Point p, long vid, VillagerRecord v) {
/*  450 */       this.townHall = p;
/*  451 */       this.id = vid;
/*  452 */       this.vr = v;
/*  453 */       this.mw = mw;
/*      */     }
/*      */     
/*      */     public Building getTownHall(World world) {
/*  457 */       return this.mw.getBuilding(this.townHall);
/*      */     }
/*      */     
/*      */     public MillVillager getVillager(World world) {
/*  461 */       if (this.villager == null) {
/*  462 */         Building th = this.mw.getBuilding(this.townHall);
/*  463 */         if (th != null) {
/*  464 */           this.villager = th.getVillagerById(this.id);
/*      */         }
/*      */       }
/*  467 */       return this.villager;
/*      */     }
/*      */     
/*      */     public VillagerRecord getVillagerRecord(World world) {
/*  471 */       if (this.vr == null) {
/*  472 */         Building th = this.mw.getBuilding(this.townHall);
/*  473 */         if (th != null) {
/*  474 */           this.vr = th.getVillagerRecordById(this.id);
/*      */         }
/*      */       }
/*  477 */       return this.vr;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public class QuestStep
/*      */   {
/*      */     int pos;
/*  485 */     public List<String> clearGlobalTagsFailure = new ArrayList();
/*  486 */     public List<String> clearGlobalTagsSuccess = new ArrayList();
/*  487 */     public List<String> clearPlayerTagsFailure = new ArrayList();
/*  488 */     public List<String> clearPlayerTagsSuccess = new ArrayList();
/*  489 */     public List<String[]> clearTagsFailure = new ArrayList();
/*  490 */     public List<String[]> clearTagsSuccess = new ArrayList();
/*      */     
/*  492 */     public final HashMap<String, String> descriptions = new HashMap();
/*  493 */     public final HashMap<String, String> descriptionsRefuse = new HashMap();
/*  494 */     public final HashMap<String, String> descriptionsSuccess = new HashMap();
/*  495 */     public final HashMap<String, String> descriptionsTimeUp = new HashMap();
/*  496 */     public final HashMap<String, String> labels = new HashMap();
/*  497 */     public final HashMap<String, String> listings = new HashMap();
/*      */     
/*  499 */     public int duration = 1;
/*  500 */     public List<String> forbiddenGlobalTag = new ArrayList();
/*  501 */     public List<String> forbiddenPlayerTag = new ArrayList();
/*      */     
/*  503 */     public int penaltyReputation = 0;
/*  504 */     public HashMap<InvItem, Integer> requiredGood = new HashMap();
/*  505 */     public List<String> stepRequiredGlobalTag = new ArrayList();
/*  506 */     public List<String> stepRequiredPlayerTag = new ArrayList();
/*  507 */     public HashMap<InvItem, Integer> rewardGoods = new HashMap();
/*  508 */     public int rewardMoney = 0;
/*  509 */     public int rewardReputation = 0;
/*      */     
/*  511 */     public List<String> bedrockbuildings = new ArrayList();
/*      */     
/*  513 */     public List<String> setGlobalTagsFailure = new ArrayList();
/*  514 */     public List<String> setGlobalTagsSuccess = new ArrayList();
/*      */     
/*  516 */     public List<String> setPlayerTagsFailure = new ArrayList();
/*  517 */     public List<String> setPlayerTagsSuccess = new ArrayList();
/*      */     
/*  519 */     public List<String[]> setVillagerTagsFailure = new ArrayList();
/*  520 */     public List<String[]> setVillagerTagsSuccess = new ArrayList();
/*      */     
/*  522 */     public List<String[]> setActionDataSuccess = new ArrayList();
/*      */     
/*  524 */     public boolean showRequiredGoods = true;
/*      */     public String villager;
/*      */     
/*      */     public QuestStep(int pos) {
/*  528 */       this.pos = pos;
/*      */     }
/*      */     
/*      */     public String getDescription() {
/*  532 */       return MLN.questString(getStringKey() + "description", true);
/*      */     }
/*      */     
/*      */     public String getDescriptionRefuse() {
/*  536 */       return MLN.questString(getStringKey() + "description_refuse", true);
/*      */     }
/*      */     
/*      */     public String getDescriptionSuccess() {
/*  540 */       return MLN.questString(getStringKey() + "description_success", true);
/*      */     }
/*      */     
/*      */     public String getDescriptionTimeUp() {
/*  544 */       return MLN.questString(getStringKey() + "description_timeup", false);
/*      */     }
/*      */     
/*      */     public String getLabel() {
/*  548 */       return MLN.questString(getStringKey() + "label", true);
/*      */     }
/*      */     
/*      */     public String getListing() {
/*  552 */       return MLN.questString(getStringKey() + "listing", false);
/*      */     }
/*      */     
/*      */     public String getStringKey() {
/*  556 */       return Quest.this.key + "_" + this.pos + "_";
/*      */     }
/*      */     
/*      */     public String lackingConditions(EntityPlayer player)
/*      */     {
/*  561 */       MillWorld mw = Mill.getMillWorld(player.field_70170_p);
/*      */       
/*  563 */       UserProfile profile = mw.getProfile(player.getDisplayName());
/*      */       
/*  565 */       String lackingGoods = null;
/*  566 */       for (InvItem item : this.requiredGood.keySet())
/*      */       {
/*      */         int diff;
/*      */         int diff;
/*  570 */         if (item.special == 1) {
/*  571 */           int nbenchanted = 0;
/*      */           
/*  573 */           for (int i = 0; i < player.field_71071_by.func_70302_i_(); i++) {
/*  574 */             ItemStack stack = player.field_71071_by.func_70301_a(i);
/*      */             
/*  576 */             if ((stack != null) && (stack.func_77948_v())) {
/*  577 */               nbenchanted++;
/*      */             }
/*      */           }
/*  580 */           diff = ((Integer)this.requiredGood.get(item)).intValue() - nbenchanted; } else { int diff;
/*  581 */           if (item.special == 2) {
/*  582 */             int nbenchanted = 0;
/*      */             
/*  584 */             for (int i = 0; i < player.field_71071_by.func_70302_i_(); i++) {
/*  585 */               ItemStack stack = player.field_71071_by.func_70301_a(i);
/*      */               
/*  587 */               if ((stack != null) && (stack.func_77948_v()) && ((stack.func_77973_b() instanceof ItemSword))) {
/*  588 */                 nbenchanted++;
/*      */               }
/*      */             }
/*  591 */             diff = ((Integer)this.requiredGood.get(item)).intValue() - nbenchanted;
/*      */           } else {
/*  593 */             diff = ((Integer)this.requiredGood.get(item)).intValue() - MillCommonUtilities.countChestItems(player.field_71071_by, item.getItem(), item.meta);
/*      */           }
/*      */         }
/*  596 */         if (diff > 0) {
/*  597 */           if (lackingGoods != null) {
/*  598 */             lackingGoods = lackingGoods + ", ";
/*      */           } else {
/*  600 */             lackingGoods = "";
/*      */           }
/*  602 */           lackingGoods = lackingGoods + diff + " " + item.getName();
/*      */         }
/*      */       }
/*  605 */       if (lackingGoods != null) {
/*  606 */         if (this.showRequiredGoods) {
/*  607 */           lackingGoods = MLN.string("quest.lackingcondition") + " " + lackingGoods;
/*      */         } else {
/*  609 */           lackingGoods = MLN.string("quest.lackinghiddengoods");
/*      */         }
/*      */       }
/*      */       
/*  613 */       boolean tagsOk = true;
/*  614 */       for (String tag : this.stepRequiredGlobalTag) {
/*  615 */         if (!mw.isGlobalTagSet(tag)) {
/*  616 */           tagsOk = false;
/*      */         }
/*      */       }
/*  619 */       for (String tag : this.forbiddenGlobalTag) {
/*  620 */         if (mw.isGlobalTagSet(tag)) {
/*  621 */           tagsOk = false;
/*      */         }
/*      */       }
/*  624 */       for (String tag : this.stepRequiredPlayerTag) {
/*  625 */         if (!profile.isTagSet(tag)) {
/*  626 */           tagsOk = false;
/*      */         }
/*      */       }
/*  629 */       for (String tag : this.forbiddenPlayerTag) {
/*  630 */         if (profile.isTagSet(tag)) {
/*  631 */           tagsOk = false;
/*      */         }
/*      */       }
/*      */       
/*  635 */       if (!tagsOk) {
/*  636 */         if (lackingGoods != null) {
/*  637 */           lackingGoods = lackingGoods + ". ";
/*      */         } else {
/*  639 */           lackingGoods = "";
/*      */         }
/*  641 */         lackingGoods = lackingGoods + MLN.string("quest.conditionsnotmet");
/*      */       }
/*      */       
/*  644 */       return lackingGoods;
/*      */     }
/*      */   }
/*      */   
/*      */   public class QuestVillager {
/*  649 */     List<String> forbiddenTags = new ArrayList();
/*  650 */     String key = null; String relatedto = null; String relation = null;
/*  651 */     List<String> requiredTags = new ArrayList();
/*  652 */     List<String> types = new ArrayList();
/*      */     
/*      */     public QuestVillager() {}
/*      */     
/*  656 */     public boolean testVillager(UserProfile profile, VillagerRecord vr) { if (profile.villagersInQuests.containsKey(Long.valueOf(vr.id))) {
/*  657 */         return false;
/*      */       }
/*      */       
/*  660 */       if ((!this.types.isEmpty()) && (!this.types.contains(vr.type))) {
/*  661 */         return false;
/*      */       }
/*      */       
/*  664 */       for (String tag : this.requiredTags) {
/*  665 */         String tagPlayer = profile.key + "_" + tag;
/*      */         
/*  667 */         if (!vr.questTags.contains(tagPlayer)) {
/*  668 */           return false;
/*      */         }
/*      */       }
/*      */       
/*  672 */       for (String tag : this.forbiddenTags) {
/*  673 */         String tagPlayer = profile.key + "_" + tag;
/*      */         
/*  675 */         if (vr.questTags.contains(tagPlayer)) {
/*  676 */           return false;
/*      */         }
/*      */       }
/*      */       
/*  680 */       for (String tag : vr.questTags)
/*      */       {
/*  682 */         String tagPlayer = profile.key + "_" + tag;
/*      */         
/*  684 */         if (this.forbiddenTags.contains(tagPlayer)) {
/*  685 */           return false;
/*      */         }
/*      */       }
/*      */       
/*  689 */       return true;
/*      */     }
/*      */   }
/*      */   
/*  693 */   public static HashMap<String, Quest> quests = new HashMap();
/*      */   
/*      */   private static final String REL_NEARBYVILLAGE = "nearbyvillage";
/*      */   
/*      */   private static final String REL_ANYVILLAGE = "anyvillage";
/*      */   
/*      */   private static final String REL_SAMEHOUSE = "samehouse";
/*      */   private static final String REL_SAMEVILLAGE = "samevillage";
/*  701 */   public static final int[] WORLD_MISSION_NB = { 15, 13, 10 };
/*  702 */   public static final String[] WORLD_MISSION_KEYS = { "sadhu", "alchemist", "fallenking" };
/*      */   
/*      */   private static Quest loadQuest(File file)
/*      */   {
/*  706 */     Quest q = new Quest();
/*      */     
/*  708 */     q.key = file.getName().split("\\.")[0];
/*      */     try
/*      */     {
/*  711 */       BufferedReader reader = MillCommonUtilities.getReader(file);
/*      */       
/*      */ 
/*  714 */       QuestStep step = null;
/*      */       String line;
/*  716 */       while ((line = reader.readLine()) != null) {
/*  717 */         if ((line.trim().length() > 0) && (!line.startsWith("//"))) {
/*  718 */           String[] temp = line.split(":");
/*  719 */           if (temp.length != 2) {
/*  720 */             MLN.error(null, "Invalid line when loading quest " + file.getName() + ": " + line);
/*      */           } else {
/*  722 */             String key = temp[0].toLowerCase();
/*  723 */             String value = temp[1];
/*  724 */             if (key.equals("step")) {
/*  725 */               Quest tmp142_141 = q;tmp142_141.getClass();step = new QuestStep(tmp142_141, q.steps.size());
/*  726 */               q.steps.add(step);
/*  727 */             } else if (key.equals("minreputation")) {
/*  728 */               q.minreputation = MillCommonUtilities.readInteger(value);
/*  729 */             } else if (key.equals("chanceperhour")) {
/*  730 */               q.chanceperhour = Double.parseDouble(value);
/*  731 */             } else if (key.equals("maxsimultaneous")) {
/*  732 */               q.maxsimultaneous = MillCommonUtilities.readInteger(value);
/*  733 */             } else if (key.equals("definevillager")) {
/*  734 */               QuestVillager v = q.loadQVillager(value);
/*  735 */               if (v != null) {
/*  736 */                 q.villagers.put(v.key, v);
/*  737 */                 q.villagersOrdered.add(v);
/*      */               }
/*  739 */             } else if (key.equals("requiredglobaltag")) {
/*  740 */               q.globalTagsRequired.add(value.trim().toLowerCase());
/*  741 */             } else if (key.equals("forbiddenglobaltag")) {
/*  742 */               q.globalTagsForbidden.add(value.trim().toLowerCase());
/*  743 */             } else if (key.equals("requiredplayertag")) {
/*  744 */               q.profileTagsRequired.add(value.trim().toLowerCase());
/*  745 */             } else if (key.equals("forbiddenplayertag")) {
/*  746 */               q.profileTagsForbidden.add(value.trim().toLowerCase());
/*  747 */             } else if (step == null) {
/*  748 */               MLN.error(q, "Reached line while not in a step: " + line);
/*  749 */             } else if (key.equals("villager")) {
/*  750 */               step.villager = value;
/*  751 */             } else if (key.equals("duration")) {
/*  752 */               step.duration = MillCommonUtilities.readInteger(value);
/*  753 */             } else if (key.equals("showrequiredgoods")) {
/*  754 */               step.showRequiredGoods = Boolean.parseBoolean(value);
/*  755 */             } else if (key.startsWith("label_")) {
/*  756 */               step.labels.put(key, value);
/*  757 */             } else if (key.startsWith("description_success_")) {
/*  758 */               step.descriptionsSuccess.put(key, value);
/*  759 */             } else if (key.startsWith("description_refuse_")) {
/*  760 */               step.descriptionsRefuse.put(key, value);
/*  761 */             } else if (key.startsWith("description_timeup_")) {
/*  762 */               step.descriptionsTimeUp.put(key, value);
/*  763 */             } else if (key.startsWith("description_")) {
/*  764 */               step.descriptions.put(key, value);
/*  765 */             } else if (key.startsWith("listing_")) {
/*  766 */               step.listings.put(key, value);
/*  767 */             } else if (key.equals("requiredgood")) {
/*  768 */               if (Goods.goodsName.containsKey(value.split(",")[0].toLowerCase())) {
/*  769 */                 InvItem iv = (InvItem)Goods.goodsName.get(value.split(",")[0].toLowerCase());
/*  770 */                 step.requiredGood.put(iv, Integer.valueOf(MillCommonUtilities.readInteger(value.split(",")[1])));
/*      */               } else {
/*  772 */                 MLN.error(null, "Unknown requiredgood found when loading quest " + file.getName() + ": " + value);
/*      */               }
/*  774 */             } else if (key.equals("rewardgood")) {
/*  775 */               if (Goods.goodsName.containsKey(value.split(",")[0].toLowerCase())) {
/*  776 */                 InvItem iv = (InvItem)Goods.goodsName.get(value.split(",")[0].toLowerCase());
/*  777 */                 step.rewardGoods.put(iv, Integer.valueOf(MillCommonUtilities.readInteger(value.split(",")[1])));
/*      */               } else {
/*  779 */                 MLN.error(null, "Unknown rewardGood found when loading quest " + file.getName() + ": " + value);
/*      */               }
/*  781 */             } else if (key.equals("rewardmoney")) {
/*  782 */               step.rewardMoney = MillCommonUtilities.readInteger(value);
/*  783 */             } else if (key.equals("rewardreputation")) {
/*  784 */               step.rewardReputation = MillCommonUtilities.readInteger(value);
/*  785 */             } else if (key.equals("penaltyreputation")) {
/*  786 */               step.penaltyReputation = MillCommonUtilities.readInteger(value);
/*  787 */             } else if (key.equals("setactiondatasuccess")) {
/*  788 */               step.setActionDataSuccess.add(value.split(","));
/*      */             }
/*  790 */             else if (key.equals("settagsuccess")) {
/*  791 */               step.setVillagerTagsSuccess.add(value.split(","));
/*  792 */             } else if (key.equals("cleartagsuccess")) {
/*  793 */               step.clearTagsSuccess.add(value.split(","));
/*  794 */             } else if (key.equals("settagfailure")) {
/*  795 */               step.setVillagerTagsFailure.add(value.split(","));
/*  796 */             } else if (key.equals("cleartagfailure")) {
/*  797 */               step.clearTagsFailure.add(value.split(","));
/*      */             }
/*  799 */             else if (key.equals("setglobaltagsuccess")) {
/*  800 */               step.setGlobalTagsSuccess.add(value);
/*  801 */             } else if (key.equals("clearglobaltagsuccess")) {
/*  802 */               step.clearGlobalTagsSuccess.add(value);
/*  803 */             } else if (key.equals("setglobaltagfailure")) {
/*  804 */               step.setGlobalTagsFailure.add(value);
/*  805 */             } else if (key.equals("clearglobaltagfailure")) {
/*  806 */               step.clearGlobalTagsFailure.add(value);
/*      */             }
/*  808 */             else if (key.equals("setplayertagsuccess")) {
/*  809 */               step.setPlayerTagsSuccess.add(value);
/*  810 */             } else if (key.equals("clearplayertagsuccess")) {
/*  811 */               step.clearPlayerTagsSuccess.add(value);
/*  812 */             } else if (key.equals("setplayertagfailure")) {
/*  813 */               step.setPlayerTagsFailure.add(value);
/*  814 */             } else if (key.equals("clearplayertagfailure")) {
/*  815 */               step.clearPlayerTagsFailure.add(value);
/*      */             }
/*  817 */             else if (key.equals("steprequiredglobaltag")) {
/*  818 */               step.stepRequiredGlobalTag.add(value);
/*  819 */             } else if (key.equals("stepforbiddenglobaltag")) {
/*  820 */               step.forbiddenGlobalTag.add(value);
/*  821 */             } else if (key.equals("steprequiredplayertag")) {
/*  822 */               step.stepRequiredPlayerTag.add(value);
/*  823 */             } else if (key.equals("stepforbiddenplayertag")) {
/*  824 */               step.forbiddenPlayerTag.add(value);
/*      */             }
/*  826 */             else if (key.equals("bedrockbuilding")) {
/*  827 */               step.bedrockbuildings.add(value.trim().toLowerCase());
/*      */             } else {
/*  829 */               MLN.error(null, "Unknow parameter when loading quest " + file.getName() + ": " + line);
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*  834 */       reader.close();
/*      */       
/*  836 */       if (q.steps.size() == 0) {
/*  837 */         MLN.error(q, "No steps found in " + file.getName() + ".");
/*  838 */         return null;
/*      */       }
/*      */       
/*  841 */       if (q.villagersOrdered.size() == 0) {
/*  842 */         MLN.error(q, "No villagers defined in " + file.getName() + ".");
/*  843 */         return null;
/*      */       }
/*      */       
/*  846 */       if (MLN.LogQuest >= 1) {
/*  847 */         MLN.major(q, "Loaded quest type: " + q.key);
/*      */       }
/*      */       
/*  850 */       return q;
/*      */     }
/*      */     catch (Exception e) {
/*  853 */       MLN.printException(e);
/*      */     }
/*  855 */     return null;
/*      */   }
/*      */   
/*      */   public static void loadQuests()
/*      */   {
/*  860 */     List<File> questDirs = new ArrayList();
/*      */     
/*  862 */     for (File dir : Mill.loadingDirs) {
/*  863 */       File questDir = new File(dir, "quests");
/*      */       
/*  865 */       if (questDir.exists()) {
/*  866 */         questDirs.add(questDir);
/*      */       }
/*      */     }
/*      */     
/*  870 */     File customQuestDir = new File(Mill.proxy.getCustomDir(), "quests");
/*      */     
/*  872 */     if (customQuestDir.exists()) {
/*  873 */       questDirs.add(customQuestDir);
/*      */     }
/*      */     
/*  876 */     for (File questdir : questDirs)
/*      */     {
/*  878 */       for (File dir : questdir.listFiles()) {
/*  879 */         if (dir.isDirectory()) {
/*  880 */           for (File file : dir.listFiles(new MillCommonUtilities.ExtFileFilter("txt")))
/*      */           {
/*  882 */             Quest quest = loadQuest(file);
/*      */             
/*  884 */             if (quest != null) {
/*  885 */               quests.put(quest.key, quest);
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  892 */     if (MLN.LogQuest >= 1) {
/*  893 */       MLN.major(null, "Loaded " + quests.size() + " quests.");
/*      */     }
/*      */   }
/*      */   
/*  897 */   public double chanceperhour = 0.0D;
/*      */   
/*      */   public String key;
/*      */   
/*  901 */   public int maxsimultaneous = 5;
/*      */   
/*  903 */   public int minreputation = 0;
/*      */   
/*  905 */   public List<QuestStep> steps = new ArrayList();
/*      */   
/*  907 */   public List<String> globalTagsForbidden = new ArrayList();
/*  908 */   public List<String> globalTagsRequired = new ArrayList();
/*      */   
/*  910 */   public List<String> profileTagsForbidden = new ArrayList();
/*  911 */   public List<String> profileTagsRequired = new ArrayList();
/*      */   
/*  913 */   public HashMap<String, QuestVillager> villagers = new HashMap();
/*      */   
/*  915 */   public List<QuestVillager> villagersOrdered = new ArrayList();
/*      */   
/*      */   private QuestVillager loadQVillager(String line) {
/*  918 */     QuestVillager v = new QuestVillager();
/*  919 */     for (String s : line.split(",")) {
/*  920 */       String key = s.split("=")[0].toLowerCase();
/*  921 */       String val = s.split("=")[1];
/*      */       
/*  923 */       if (key.equals("key")) {
/*  924 */         v.key = val;
/*  925 */       } else if (key.equals("type")) {
/*  926 */         Culture c = Culture.getCultureByName(val.split("/")[0]);
/*  927 */         if (c == null) {
/*  928 */           MLN.error(this, "Unknow culture when loading definevillager: " + line);
/*  929 */           return null;
/*      */         }
/*  931 */         VillagerType vtype = c.getVillagerType(val.split("/")[1]);
/*  932 */         if (vtype == null) {
/*  933 */           MLN.error(this, "Unknow vilager type when loading definevillager: " + line);
/*  934 */           return null;
/*      */         }
/*  936 */         v.types.add(vtype.key);
/*      */ 
/*      */       }
/*  939 */       else if (key.equals("relatedto")) {
/*  940 */         v.relatedto = val;
/*  941 */       } else if (key.equals("relation")) {
/*  942 */         v.relation = val;
/*  943 */       } else if (key.equals("forbiddentag")) {
/*  944 */         v.forbiddenTags.add(val);
/*  945 */       } else if (key.equals("requiredtag")) {
/*  946 */         v.requiredTags.add(val);
/*      */       }
/*      */     }
/*      */     
/*  950 */     if (v.key == null) {
/*  951 */       MLN.error(this, "No key found when loading definevillager: " + line);
/*  952 */       return null;
/*      */     }
/*      */     
/*  955 */     return v;
/*      */   }
/*      */   
/*      */   public QuestInstance testQuest(MillWorld mw, UserProfile profile)
/*      */   {
/*  960 */     if (!MillCommonUtilities.probability(this.chanceperhour)) {
/*  961 */       return null;
/*      */     }
/*      */     
/*  964 */     int nb = 0;
/*      */     
/*  966 */     for (QuestInstance qi : profile.questInstances) {
/*  967 */       if (qi.quest == this) {
/*  968 */         nb++;
/*      */       }
/*      */     }
/*      */     
/*  972 */     if (nb >= this.maxsimultaneous) {
/*  973 */       return null;
/*      */     }
/*      */     
/*  976 */     for (String tag : this.globalTagsRequired) {
/*  977 */       if (!mw.isGlobalTagSet(tag)) {
/*  978 */         return null;
/*      */       }
/*      */     }
/*      */     
/*  982 */     for (String tag : this.profileTagsRequired) {
/*  983 */       if (!profile.isTagSet(tag)) {
/*  984 */         return null;
/*      */       }
/*      */     }
/*      */     
/*  988 */     for (String tag : this.globalTagsForbidden) {
/*  989 */       if (mw.isGlobalTagSet(tag)) {
/*  990 */         return null;
/*      */       }
/*      */     }
/*      */     
/*  994 */     for (String tag : this.profileTagsForbidden) {
/*  995 */       if (profile.isTagSet(tag)) {
/*  996 */         return null;
/*      */       }
/*      */     }
/*      */     
/* 1000 */     if (MLN.LogQuest >= 3) {
/* 1001 */       MLN.debug(this, "Testing quest " + this.key);
/*      */     }
/*      */     
/* 1004 */     QuestVillager startingVillager = (QuestVillager)this.villagersOrdered.get(0);
/*      */     
/* 1006 */     List<HashMap<String, QuestInstanceVillager>> possibleVillagers = new ArrayList();
/*      */     
/* 1008 */     for (Iterator i$ = mw.getCombinedVillagesLoneBuildings().iterator(); i$.hasNext();) { p = (Point)i$.next();
/* 1009 */       th = mw.getBuilding(p);
/* 1010 */       if ((th != null) && (th.isActive) && (th.getReputation(profile.key) >= this.minreputation))
/*      */       {
/* 1012 */         if (MLN.LogQuest >= 3) {
/* 1013 */           MLN.debug(this, "Loooking for starting villager in: " + th.getVillageQualifiedName());
/*      */         }
/*      */         
/* 1016 */         for (VillagerRecord vr : th.vrecords) {
/* 1017 */           if (startingVillager.testVillager(profile, vr)) {
/* 1018 */             HashMap<String, QuestInstanceVillager> villagers = new HashMap();
/* 1019 */             villagers.put(startingVillager.key, new QuestInstanceVillager(mw, p, vr.id, vr));
/*      */             
/* 1021 */             boolean error = false;
/*      */             
/* 1023 */             if (MLN.LogQuest >= 3) {
/* 1024 */               MLN.debug(this, "Found possible starting villager: " + vr);
/*      */             }
/*      */             
/* 1027 */             for (QuestVillager qv : this.villagersOrdered) {
/* 1028 */               if ((!error) && (qv != startingVillager))
/*      */               {
/* 1030 */                 if (MLN.LogQuest >= 3) {
/* 1031 */                   MLN.debug(this, "Trying to find villager type: " + qv.relation + "/" + qv.relatedto);
/*      */                 }
/*      */                 
/* 1034 */                 VillagerRecord relatedVillager = ((QuestInstanceVillager)villagers.get(qv.relatedto)).getVillagerRecord(mw.world);
/*      */                 
/* 1036 */                 if (relatedVillager == null) {
/* 1037 */                   error = true;
/* 1038 */                   break;
/*      */                 }
/*      */                 
/* 1041 */                 if ("samevillage".equals(qv.relation)) {
/* 1042 */                   List<VillagerRecord> newVillagers = new ArrayList();
/* 1043 */                   for (VillagerRecord vr2 : mw.getBuilding(relatedVillager.townHallPos).vrecords) {
/* 1044 */                     if ((!vr2.housePos.equals(relatedVillager.housePos)) && (qv.testVillager(profile, vr2))) {
/* 1045 */                       newVillagers.add(vr2);
/*      */                     }
/*      */                   }
/*      */                   
/* 1049 */                   if (newVillagers.size() > 0) {
/* 1050 */                     VillagerRecord chosen = (VillagerRecord)newVillagers.get(MillCommonUtilities.randomInt(newVillagers.size()));
/* 1051 */                     villagers.put(qv.key, new QuestInstanceVillager(mw, p, chosen.id, chosen));
/*      */                   } else {
/* 1053 */                     error = true;
/*      */                   }
/* 1055 */                 } else if (("nearbyvillage".equals(qv.relation)) || ("anyvillage".equals(qv.relation))) {
/* 1056 */                   List<QuestInstanceVillager> newVillagers = new ArrayList();
/*      */                   
/* 1058 */                   for (Iterator i$ = mw.getCombinedVillagesLoneBuildings().iterator(); i$.hasNext();) { p2 = (Point)i$.next();
/* 1059 */                     Building th2 = mw.getBuilding(p2);
/* 1060 */                     if ((th2 != null) && (th2 != th) && (("anyvillage".equals(qv.relation)) || (th.getPos().distanceTo(th2.getPos()) < 2000.0D)))
/*      */                     {
/* 1062 */                       if (MLN.LogQuest >= 3) {
/* 1063 */                         MLN.debug(this, "Trying to find villager type: " + qv.relation + "/" + qv.relatedto + " in " + th2.getVillageQualifiedName());
/*      */                       }
/*      */                       
/* 1066 */                       for (VillagerRecord vr2 : th2.vrecords)
/*      */                       {
/* 1068 */                         if (MLN.LogQuest >= 3) {
/* 1069 */                           MLN.debug(this, "Testing: " + vr2);
/*      */                         }
/*      */                         
/* 1072 */                         if (qv.testVillager(profile, vr2)) {
/* 1073 */                           newVillagers.add(new QuestInstanceVillager(mw, p2, vr2.id, vr2));
/*      */                         }
/*      */                       }
/*      */                     }
/*      */                   }
/*      */                   Point p2;
/* 1079 */                   if (newVillagers.size() > 0) {
/* 1080 */                     villagers.put(qv.key, newVillagers.get(MillCommonUtilities.randomInt(newVillagers.size())));
/*      */                   } else {
/* 1082 */                     error = true;
/*      */                   }
/* 1084 */                 } else if ("samehouse".equals(qv.relation)) {
/* 1085 */                   List<VillagerRecord> newVillagers = new ArrayList();
/* 1086 */                   for (VillagerRecord vr2 : mw.getBuilding(relatedVillager.townHallPos).vrecords) {
/* 1087 */                     if ((vr2.housePos.equals(relatedVillager.housePos)) && (qv.testVillager(profile, vr2))) {
/* 1088 */                       newVillagers.add(vr2);
/*      */                     }
/*      */                   }
/*      */                   
/* 1092 */                   if (newVillagers.size() > 0) {
/* 1093 */                     VillagerRecord chosen = (VillagerRecord)newVillagers.get(MillCommonUtilities.randomInt(newVillagers.size()));
/* 1094 */                     villagers.put(qv.key, new QuestInstanceVillager(mw, p, chosen.id, chosen));
/*      */                   } else {
/* 1096 */                     error = true;
/*      */                   }
/*      */                 } else {
/* 1099 */                   MLN.error(this, "Unknown relation: " + qv.relation);
/*      */                 }
/*      */               }
/*      */             }
/*      */             
/* 1104 */             if (!error) {
/* 1105 */               possibleVillagers.add(villagers);
/* 1106 */               if (MLN.LogQuest >= 3)
/* 1107 */                 MLN.debug(this, "Found all the villagers needed: " + villagers.size());
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     Point p;
/*      */     Building th;
/* 1115 */     if (possibleVillagers.isEmpty()) {
/* 1116 */       return null;
/*      */     }
/*      */     
/* 1119 */     HashMap<String, QuestInstanceVillager> selectedOption = (HashMap)possibleVillagers.get(MillCommonUtilities.randomInt(possibleVillagers.size()));
/*      */     
/* 1121 */     QuestInstance qi = new QuestInstance(mw, this, profile, selectedOption, mw.world.func_72820_D());
/*      */     
/* 1123 */     profile.questInstances.add(qi);
/*      */     
/* 1125 */     for (QuestInstanceVillager qiv : selectedOption.values()) {
/* 1126 */       profile.villagersInQuests.put(Long.valueOf(qiv.id), qi);
/*      */     }
/*      */     
/* 1129 */     return qi;
/*      */   }
/*      */   
/*      */ 
/*      */   public String toString()
/*      */   {
/* 1135 */     return "QT: " + this.key;
/*      */   }
/*      */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\Quest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */