/*      */ package org.millenaire.common;
/*      */ 
/*      */ import io.netty.buffer.ByteBufInputStream;
/*      */ import io.netty.buffer.ByteBufOutputStream;
/*      */ import java.io.BufferedReader;
/*      */ import java.io.BufferedWriter;
/*      */ import java.io.DataOutput;
/*      */ import java.io.File;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collections;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import org.millenaire.common.building.Building;
/*      */ import org.millenaire.common.building.BuildingCustomPlan;
/*      */ import org.millenaire.common.building.BuildingPlan;
/*      */ import org.millenaire.common.building.BuildingPlanSet;
/*      */ import org.millenaire.common.core.MillCommonUtilities;
/*      */ import org.millenaire.common.forge.CommonProxy;
/*      */ import org.millenaire.common.forge.Mill;
/*      */ import org.millenaire.common.item.Goods;
/*      */ import org.millenaire.common.network.StreamReadWrite;
/*      */ 
/*      */ public class Culture
/*      */ {
/*      */   private static final int LANGUAGE_FLUENT = 500;
/*      */   private static final int LANGUAGE_MODERATE = 200;
/*      */   private static final int LANGUAGE_BEGINNER = 100;
/*      */   
/*      */   public static class CultureLanguage
/*      */   {
/*      */     public Culture culture;
/*      */     public String language;
/*      */     public boolean serverContent;
/*      */     
/*      */     public static class Dialogue implements org.millenaire.common.core.MillCommonUtilities.WeightedChoice
/*      */     {
/*   39 */       private static String adult = "adult"; private static String child = "child"; private static String male = "male"; private static String female = "female"; private static String hasspouse = "hasspouse"; private static String nospouse = "nospouse"; private static String vtype = "vtype"; private static String notvtype = "notvtype";
/*      */       
/*   41 */       private static String rel_spouse = "spouse"; private static String rel_parent = "parent"; private static String rel_child = "child"; private static String rel_sibling = "sibling";
/*      */       
/*   43 */       private static String tag_raining = "raining"; private static String tag_notraining = "notraining";
/*      */       
/*   45 */       public String key = null;
/*   46 */       private int weight = 10;
/*   47 */       private final List<String> villager1 = new ArrayList();
/*   48 */       private final List<String> villager2 = new ArrayList();
/*   49 */       private final List<String> relations = new ArrayList();
/*   50 */       private final List<String> not_relations = new ArrayList();
/*   51 */       private final List<String> buildings = new ArrayList();
/*   52 */       private final List<String> not_buildings = new ArrayList();
/*   53 */       private final List<String> villagers = new ArrayList();
/*   54 */       private final List<String> not_villagers = new ArrayList();
/*   55 */       private final List<String> tags = new ArrayList();
/*      */       
/*   57 */       public final List<Integer> timeDelays = new ArrayList();
/*   58 */       public final List<Integer> speechBy = new ArrayList();
/*      */       
/*      */       Dialogue(String config) {
/*   61 */         this.key = null;
/*   62 */         for (String s : config.split(",")) {
/*   63 */           if (s.split(":").length > 1) {
/*   64 */             String key = s.split(":")[0].trim();
/*   65 */             String val = s.split(":")[1].trim();
/*      */             
/*   67 */             if (s.split(":").length > 2) {
/*   68 */               val = val + ":" + s.split(":")[2];
/*      */             }
/*      */             
/*   71 */             if (key.equals("key")) {
/*   72 */               this.key = val;
/*   73 */             } else if (key.equals("weigth")) {
/*   74 */               this.weight = Integer.parseInt(val);
/*   75 */             } else if (key.equals("v1")) {
/*   76 */               this.villager1.add(val);
/*   77 */             } else if (key.equals("v2")) {
/*   78 */               this.villager2.add(val);
/*   79 */             } else if (key.equals("rel")) {
/*   80 */               this.relations.add(val);
/*   81 */             } else if (key.equals("notrel")) {
/*   82 */               this.not_relations.add(val);
/*   83 */             } else if (key.equals("building")) {
/*   84 */               this.buildings.add(val);
/*   85 */             } else if (key.equals("notbuilding")) {
/*   86 */               this.not_buildings.add(val);
/*   87 */             } else if (key.equals("villager")) {
/*   88 */               this.villagers.add(val);
/*   89 */             } else if (key.equals("notvillager")) {
/*   90 */               this.not_villagers.add(val);
/*   91 */             } else if (key.equals("tag")) {
/*   92 */               this.tags.add(val);
/*      */             } else {
/*   94 */               MLN.error(this, "Could not recognise key " + key + " in dialogue declaration " + config);
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */       public void checkData(Culture culture, String language)
/*      */       {
/*  103 */         for (String s : this.villager1) {
/*  104 */           if ((!s.equals(adult)) && (!s.equals(child)) && (!s.equals(male)) && (!s.equals(female)) && (!s.equals(hasspouse)) && (!s.equals(nospouse)) && (!s.startsWith(vtype + ":")) && (!s.startsWith(notvtype + ":")))
/*      */           {
/*  106 */             MLN.error(culture, language + ": Unknown v1 setting in dialogue " + this.key + ": " + s);
/*      */           }
/*      */           
/*  109 */           if ((s.startsWith(vtype + ":")) || (s.startsWith(notvtype + ":"))) {
/*  110 */             String s2 = s.split(":")[1].trim();
/*      */             
/*  112 */             for (String vtype : s2.split("-")) {
/*  113 */               vtype = vtype.trim();
/*  114 */               if (!culture.villagerTypes.containsKey(vtype)) {
/*  115 */                 MLN.error(culture, language + ": Unknown villager type in dialogue " + this.key + ": " + s);
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*  120 */         for (String s : this.villager2) {
/*  121 */           if ((!s.equals(adult)) && (!s.equals(child)) && (!s.equals(male)) && (!s.equals(female)) && (!s.equals(hasspouse)) && (!s.equals(nospouse)) && (!s.startsWith(vtype + ":")) && (!s.startsWith(notvtype + ":")))
/*      */           {
/*  123 */             MLN.error(culture, language + ": Unknown v2 setting in dialogue " + this.key + ": " + s);
/*      */           }
/*      */         }
/*  126 */         for (String s : this.relations) {
/*  127 */           if ((!s.equals(rel_spouse)) && (!s.equals(rel_parent)) && (!s.equals(rel_child)) && (!s.equals(rel_sibling))) {
/*  128 */             MLN.error(culture, language + ": Unknown rel setting in dialogue " + this.key + ": " + s);
/*      */           }
/*      */         }
/*  131 */         for (String s : this.not_relations) {
/*  132 */           if ((!s.equals(rel_spouse)) && (!s.equals(rel_parent)) && (!s.equals(rel_child)) && (!s.equals(rel_sibling))) {
/*  133 */             MLN.error(culture, language + ": Unknown notrel setting in dialogue " + this.key + ": " + s);
/*      */           }
/*      */         }
/*  136 */         for (String s : this.tags) {
/*  137 */           if ((!s.equals(tag_raining)) && (!s.equals(tag_notraining))) {
/*  138 */             MLN.error(culture, language + ": Unknown tag in dialogue " + this.key + ": " + s);
/*      */           }
/*      */         }
/*  141 */         for (String s : this.buildings) {
/*  142 */           if (!culture.planSet.containsKey(s)) {
/*  143 */             MLN.error(culture, language + ": Unknown building in dialogue " + this.key + ": " + s);
/*      */           }
/*      */         }
/*  146 */         for (String s : this.not_buildings) {
/*  147 */           if (!culture.planSet.containsKey(s)) {
/*  148 */             MLN.error(culture, language + ": Unknown notbuilding in dialogue " + this.key + ": " + s);
/*      */           }
/*      */         }
/*  151 */         for (String s : this.villagers) {
/*  152 */           if (!culture.villagerTypes.containsKey(s)) {
/*  153 */             MLN.error(culture, language + ": Unknown villager in dialogue " + this.key + ": " + s);
/*      */           }
/*      */         }
/*  156 */         for (String s : this.not_villagers) {
/*  157 */           if (!culture.villagerTypes.containsKey(s)) {
/*  158 */             MLN.error(culture, language + ": Unknown notvillager in dialogue " + this.key + ": " + s);
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*      */       public boolean compareWith(Dialogue d, List<String> errors) throws java.io.IOException
/*      */       {
/*  165 */         boolean differentConfig = false;
/*      */         
/*  167 */         if (this.weight != d.weight) {
/*  168 */           differentConfig = true;
/*      */         }
/*      */         
/*  171 */         if (!sameLists(this.villager1, d.villager1)) {
/*  172 */           differentConfig = true;
/*      */         }
/*  174 */         if (!sameLists(this.villager2, d.villager2)) {
/*  175 */           differentConfig = true;
/*      */         }
/*  177 */         if (!sameLists(this.relations, d.relations)) {
/*  178 */           differentConfig = true;
/*      */         }
/*  180 */         if (!sameLists(this.not_relations, d.not_relations)) {
/*  181 */           differentConfig = true;
/*      */         }
/*  183 */         if (!sameLists(this.buildings, d.buildings)) {
/*  184 */           differentConfig = true;
/*      */         }
/*  186 */         if (!sameLists(this.not_buildings, d.not_buildings)) {
/*  187 */           differentConfig = true;
/*      */         }
/*  189 */         if (!sameLists(this.villagers, d.villagers)) {
/*  190 */           differentConfig = true;
/*      */         }
/*  192 */         if (!sameLists(this.not_villagers, d.not_villagers)) {
/*  193 */           differentConfig = true;
/*      */         }
/*  195 */         if (!sameLists(this.tags, d.tags)) {
/*  196 */           differentConfig = true;
/*      */         }
/*      */         
/*  199 */         if (differentConfig) {
/*  200 */           errors.add("Dialogue has different configurations: " + this.key);
/*      */         }
/*      */         
/*      */         boolean differentSentences;
/*      */         
/*  205 */         if (this.timeDelays.size() != d.timeDelays.size()) {
/*  206 */           boolean differentSentences = true;
/*  207 */           errors.add("Dialogue has different sentence numbers: " + this.key);
/*      */         } else {
/*  209 */           differentSentences = (!sameLists(this.timeDelays, d.timeDelays)) || (!sameLists(this.speechBy, d.speechBy));
/*      */           
/*  211 */           if (differentSentences) {
/*  212 */             errors.add("Dialogue has different sentence settings: " + this.key);
/*      */           }
/*      */         }
/*      */         
/*  216 */         return (!differentSentences) && (!differentConfig);
/*      */       }
/*      */       
/*      */ 
/*      */       public int getChoiceWeight(net.minecraft.entity.player.EntityPlayer player)
/*      */       {
/*  222 */         return this.weight;
/*      */       }
/*      */       
/*      */       private boolean isBuildingCompatible(Building townHall)
/*      */       {
/*  227 */         for (String s : this.buildings) {
/*  228 */           boolean found = false;
/*      */           
/*  230 */           for (org.millenaire.common.building.BuildingLocation bl : townHall.getLocations()) {
/*  231 */             if (bl.planKey.equals(s)) {
/*  232 */               found = true;
/*      */             }
/*      */           }
/*      */           
/*  236 */           if (!found) {
/*  237 */             return false;
/*      */           }
/*      */         }
/*      */         
/*  241 */         for (Iterator i$ = this.not_buildings.iterator(); i$.hasNext();) { s = (String)i$.next();
/*  242 */           for (org.millenaire.common.building.BuildingLocation bl : townHall.getLocations()) {
/*  243 */             if (bl.planKey.equals(s)) {
/*  244 */               return false;
/*      */             }
/*      */           }
/*      */         }
/*      */         String s;
/*  249 */         return true;
/*      */       }
/*      */       
/*      */       private boolean isCompatible(List<String> req, MillVillager v)
/*      */       {
/*  254 */         if (v.getRecord() == null) {
/*  255 */           return false;
/*      */         }
/*      */         
/*  258 */         for (String s : req)
/*      */         {
/*  260 */           String key = s.split(":")[0];
/*  261 */           String val = null;
/*      */           
/*  263 */           if (s.split(":").length > 1) {
/*  264 */             val = s.split(":")[1];
/*      */           }
/*      */           
/*  267 */           if (key.equals(adult)) {
/*  268 */             if (v.vtype.isChild) {
/*  269 */               return false;
/*      */             }
/*  271 */           } else if (key.equals(child)) {
/*  272 */             if (!v.vtype.isChild) {
/*  273 */               return false;
/*      */             }
/*  275 */           } else if (key.equals(male)) {
/*  276 */             if (v.vtype.gender != 1) {
/*  277 */               return false;
/*      */             }
/*  279 */           } else if (key.equals(female)) {
/*  280 */             if (v.vtype.gender != 2) {
/*  281 */               return false;
/*      */             }
/*  283 */           } else if (key.equals(vtype)) {
/*  284 */             boolean found = false;
/*  285 */             for (String type : val.split("-")) {
/*  286 */               if (type.equals(v.vtype.key)) {
/*  287 */                 found = true;
/*      */               }
/*      */             }
/*  290 */             if (!found) {
/*  291 */               return false;
/*      */             }
/*  293 */           } else if (key.equals(notvtype)) {
/*  294 */             for (String type : val.split("-")) {
/*  295 */               if (type.equals(v.vtype.key)) {
/*  296 */                 return false;
/*      */               }
/*      */             }
/*  299 */           } else if (key.equals(hasspouse)) {
/*  300 */             if ((v.getRecord().spousesName == null) || (v.getRecord().spousesName.equals(""))) {
/*  301 */               return false;
/*      */             }
/*  303 */           } else if ((v.getRecord().spousesName != null) && (key.equals(nospouse))) {
/*  304 */             if (!v.getRecord().spousesName.equals("")) {
/*  305 */               return false;
/*      */             }
/*  307 */           } else if ((key.equals(female)) && 
/*  308 */             (v.vtype.gender != 2)) {
/*  309 */             return false;
/*      */           }
/*      */         }
/*      */         
/*  313 */         return true;
/*      */       }
/*      */       
/*      */       private boolean isRelCompatible(MillVillager v1, MillVillager v2)
/*      */       {
/*  318 */         for (String s : this.relations)
/*      */         {
/*  320 */           String key = s.split(":")[0];
/*      */           
/*  322 */           if (key.equals(rel_spouse)) {
/*  323 */             if (v1.getSpouse() != v2) {
/*  324 */               return false;
/*      */             }
/*  326 */           } else if (key.equals(rel_parent)) {
/*  327 */             if ((!v1.getRecord().fathersName.equals(v2.getName())) && (!v1.getRecord().mothersName.equals(v2.getName()))) {
/*  328 */               return false;
/*      */             }
/*  330 */           } else if (key.equals(rel_child)) {
/*  331 */             if ((!v2.getRecord().fathersName.equals(v1.getName())) && (!v2.getRecord().mothersName.equals(v1.getName()))) {
/*  332 */               return false;
/*      */             }
/*  334 */           } else if ((key.equals(rel_sibling)) && 
/*  335 */             (!v2.getRecord().mothersName.equals(v1.getRecord().mothersName))) {
/*  336 */             return false;
/*      */           }
/*      */         }
/*      */         
/*      */ 
/*  341 */         for (String s : this.not_relations)
/*      */         {
/*  343 */           String key = s.split(":")[0];
/*      */           
/*  345 */           if (key.equals(rel_spouse)) {
/*  346 */             if (v1.getSpouse() == v2) {
/*  347 */               return false;
/*      */             }
/*  349 */           } else if (key.equals(rel_parent)) {
/*  350 */             if ((v1.getRecord().fathersName.equals(v2.getName())) || (v1.getRecord().mothersName.equals(v2.getName()))) {
/*  351 */               return false;
/*      */             }
/*  353 */           } else if (key.equals(rel_child)) {
/*  354 */             if ((v2.getRecord().fathersName.equals(v1.getName())) || (v2.getRecord().mothersName.equals(v1.getName()))) {
/*  355 */               return false;
/*      */             }
/*  357 */           } else if ((key.equals(rel_sibling)) && 
/*  358 */             (v2.getRecord().mothersName.equals(v1.getRecord().mothersName))) {
/*  359 */             return false;
/*      */           }
/*      */         }
/*      */         
/*      */ 
/*  364 */         return true;
/*      */       }
/*      */       
/*      */       private boolean isTagCompatible(Building townHall)
/*      */       {
/*  369 */         for (String s : this.tags) {
/*  370 */           if (s.equals(tag_raining)) {
/*  371 */             if (!townHall.worldObj.func_72896_J()) {
/*  372 */               return false;
/*      */             }
/*  374 */           } else if ((s.equals(tag_notraining)) && 
/*  375 */             (townHall.worldObj.func_72896_J())) {
/*  376 */             return false;
/*      */           }
/*      */         }
/*      */         
/*      */ 
/*  381 */         return true;
/*      */       }
/*      */       
/*      */       public boolean isValidFor(MillVillager v1, MillVillager v2) {
/*  385 */         return (isCompatible(this.villager1, v1)) && (isCompatible(this.villager2, v2)) && (isRelCompatible(v1, v2)) && (isBuildingCompatible(v1.getTownHall())) && (isVillagersCompatible(v1.getTownHall())) && (isTagCompatible(v1.getTownHall()));
/*      */       }
/*      */       
/*      */ 
/*      */       private boolean isVillagersCompatible(Building townHall)
/*      */       {
/*  391 */         for (String s : this.villagers) {
/*  392 */           boolean found = false;
/*      */           
/*  394 */           for (VillagerRecord vr : townHall.vrecords) {
/*  395 */             if (vr.type.equals(s)) {
/*  396 */               found = true;
/*      */             }
/*      */           }
/*      */           
/*  400 */           if (!found) {
/*  401 */             return false;
/*      */           }
/*      */         }
/*      */         
/*  405 */         for (Iterator i$ = this.not_villagers.iterator(); i$.hasNext();) { s = (String)i$.next();
/*  406 */           for (VillagerRecord vr : townHall.vrecords) {
/*  407 */             if (vr.type.equals(s)) {
/*  408 */               return false;
/*      */             }
/*      */           }
/*      */         }
/*      */         String s;
/*  413 */         return true;
/*      */       }
/*      */       
/*      */       private boolean sameLists(List<?> v, List<?> v2)
/*      */       {
/*  418 */         if (v.size() != v2.size()) {
/*  419 */           return false;
/*      */         }
/*      */         
/*  422 */         for (int i = 0; i < v.size(); i++) {
/*  423 */           if (!v.get(i).equals(v2.get(i))) {
/*  424 */             return false;
/*      */           }
/*      */         }
/*      */         
/*  428 */         return true;
/*      */       }
/*      */       
/*      */       public int validRoleFor(MillVillager v)
/*      */       {
/*  433 */         if (isCompatible(this.villager1, v)) {
/*  434 */           return 1;
/*      */         }
/*  436 */         if (isCompatible(this.villager2, v)) {
/*  437 */           return 2;
/*      */         }
/*      */         
/*  440 */         return 0;
/*      */       }
/*      */     }
/*      */     
/*      */     public static class ReputationLevel implements Comparable<ReputationLevel> {
/*      */       private final String label;
/*      */       private final String desc;
/*      */       public int level;
/*      */       
/*      */       public ReputationLevel(File file, String line) {
/*      */         try {
/*  451 */           this.level = MillCommonUtilities.readInteger(line.split(";")[0]);
/*      */         } catch (Exception e) {
/*  453 */           this.level = 0;
/*  454 */           MLN.error(null, "Error when reading reputation line in file " + file.getAbsolutePath() + ": " + line + " : " + e.getMessage());
/*      */         }
/*  456 */         this.label = line.split(";")[1];
/*  457 */         this.desc = line.split(";")[2];
/*      */       }
/*      */       
/*      */       public int compareTo(ReputationLevel o)
/*      */       {
/*  462 */         return this.level - o.level;
/*      */       }
/*      */       
/*      */       public boolean equals(Object o)
/*      */       {
/*  467 */         return super.equals(o);
/*      */       }
/*      */       
/*      */       public int hashCode()
/*      */       {
/*  472 */         return super.hashCode();
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  480 */     public HashMap<String, List<String>> sentences = new HashMap();
/*  481 */     public HashMap<String, String> buildingNames = new HashMap();
/*  482 */     public HashMap<String, String> strings = new HashMap();
/*  483 */     public HashMap<String, Dialogue> dialogues = new HashMap();
/*      */     
/*  485 */     public List<ReputationLevel> reputationLevels = new ArrayList();
/*      */     
/*      */     public CultureLanguage(Culture c, String l, boolean serverContent) {
/*  488 */       this.culture = c;
/*  489 */       this.language = l;
/*  490 */       this.serverContent = serverContent;
/*      */     }
/*      */     
/*      */     public int[] compareWithLanguage(CultureLanguage ref, BufferedWriter writer) throws Exception
/*      */     {
/*  495 */       int translationsDone = 0;int translationsMissing = 0;
/*      */       
/*  497 */       List<String> errors = new ArrayList();
/*  498 */       List<String> keys = new ArrayList(ref.strings.keySet());
/*  499 */       Collections.sort(keys);
/*      */       
/*  501 */       for (String key : keys) {
/*  502 */         if (!this.strings.containsKey(key)) {
/*  503 */           errors.add("String missing for culture " + this.culture.key + ": " + key);
/*  504 */           translationsMissing++;
/*      */         } else {
/*  506 */           translationsDone++;
/*      */         }
/*      */       }
/*      */       
/*  510 */       if (errors.size() > 0) {
/*  511 */         writer.write("List of gaps found in culture strings for " + this.culture.key + ": " + MLN.EOL + MLN.EOL);
/*      */         
/*  513 */         for (String s : errors) {
/*  514 */           writer.write(s + MLN.EOL);
/*      */         }
/*  516 */         writer.write(MLN.EOL);
/*      */       }
/*      */       
/*  519 */       for (Goods g : this.culture.goodsList) {
/*  520 */         if ((g.desc != null) && (!this.strings.containsKey(g.desc)) && (!ref.strings.containsKey(g.desc))) {
/*  521 */           errors.add("Trading good desc missing in both languages for item: " + g.name + ", desc key: " + g.desc);
/*      */         }
/*      */       }
/*      */       
/*  525 */       errors.clear();
/*  526 */       keys = new ArrayList(ref.sentences.keySet());
/*  527 */       Collections.sort(keys);
/*      */       
/*  529 */       for (String key : keys) {
/*  530 */         if (!key.startsWith("villager.chat_"))
/*      */         {
/*      */ 
/*  533 */           if (!this.sentences.containsKey(key)) {
/*  534 */             errors.add("Sentences missing for culture " + this.culture.key + ": " + key);
/*  535 */             translationsMissing++;
/*  536 */           } else if (((List)this.sentences.get(key)).size() != ((List)ref.sentences.get(key)).size()) {
/*  537 */             errors.add("Different number of sentences for culture " + this.culture.key + ": " + key);
/*  538 */             translationsMissing++;
/*      */           } else {
/*  540 */             translationsDone++;
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*  545 */       if (errors.size() > 0) {
/*  546 */         writer.write("List of gaps found in culture sentences for " + this.culture.key + ": " + MLN.EOL + MLN.EOL);
/*      */         
/*  548 */         for (String s : errors) {
/*  549 */           writer.write(s + MLN.EOL);
/*      */         }
/*  551 */         writer.write(MLN.EOL);
/*      */       }
/*      */       
/*  554 */       keys = new ArrayList(ref.dialogues.keySet());
/*  555 */       Collections.sort(keys);
/*      */       
/*  557 */       errors.clear();
/*      */       
/*  559 */       for (String key : keys)
/*      */       {
/*  561 */         if (!this.dialogues.containsKey(key)) {
/*  562 */           errors.add("Dialogue missing for culture " + this.culture.key + ": " + key);
/*  563 */           translationsMissing++;
/*      */         } else {
/*  565 */           boolean matches = ((Dialogue)this.dialogues.get(key)).compareWith((Dialogue)ref.dialogues.get(key), errors);
/*      */           
/*  567 */           if (matches) {
/*  568 */             translationsDone++;
/*      */           } else {
/*  570 */             translationsMissing++;
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*  575 */       if (errors.size() > 0) {
/*  576 */         writer.write("List of gaps found in culture dialogues for " + this.culture.key + ": " + MLN.EOL + MLN.EOL);
/*      */         
/*  578 */         for (String s : errors) {
/*  579 */           writer.write(s + MLN.EOL);
/*      */         }
/*  581 */         writer.write(MLN.EOL);
/*      */       }
/*      */       
/*  584 */       errors.clear();
/*  585 */       keys = new ArrayList(ref.buildingNames.keySet());
/*  586 */       Collections.sort(keys);
/*      */       
/*  588 */       for (String key : keys) {
/*  589 */         if (!this.buildingNames.containsKey(key)) {
/*  590 */           errors.add("Building name missing for culture " + this.culture.key + ": " + key);
/*  591 */           translationsMissing++;
/*      */         } else {
/*  593 */           translationsDone++;
/*      */         }
/*      */       }
/*      */       
/*  597 */       for (BuildingPlanSet set : this.culture.planSet.values()) {
/*  598 */         for (BuildingPlan[] plans : set.plans) {
/*  599 */           String planNameLC = plans[0].planName.toLowerCase();
/*  600 */           if ((!this.buildingNames.containsKey(planNameLC)) && (!ref.buildingNames.containsKey(planNameLC))) {
/*  601 */             errors.add("Building name missing for culture " + this.culture.key + " in both languages: " + planNameLC);
/*      */           }
/*      */           
/*  604 */           if ((plans[0].shop != null) && (!this.strings.containsKey("shop." + plans[0].shop)) && (!ref.strings.containsKey("shop." + plans[0].shop))) {
/*  605 */             errors.add("Shop name missing for culture " + this.culture.key + " in both languages: " + "shop." + plans[0].shop);
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*  611 */       for (VillagerType vt : this.culture.listVillagerTypes)
/*      */       {
/*  613 */         if ((!this.strings.containsKey("villager." + vt.key)) && (!ref.strings.containsKey("villager." + vt.key))) {
/*  614 */           errors.add("Villager name missing for culture " + this.culture.key + " in both languages: " + "villager." + vt.key);
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*  619 */       if (errors.size() > 0) {
/*  620 */         writer.write("List of gaps found in culture building names for " + this.culture.key + ": " + MLN.EOL + MLN.EOL);
/*      */         
/*  622 */         for (String s : errors) {
/*  623 */           writer.write(s + MLN.EOL);
/*      */         }
/*  625 */         writer.write(MLN.EOL);
/*      */       }
/*      */       
/*  628 */       if (this.reputationLevels.size() != ref.reputationLevels.size()) {
/*  629 */         translationsMissing += ref.reputationLevels.size() - this.reputationLevels.size();
/*  630 */         writer.write("Different number of reputation levels for culture " + this.culture.key + ": " + this.reputationLevels.size() + " in " + this.language + ", " + ref.reputationLevels.size() + " in " + ref.language + "." + MLN.EOL + MLN.EOL);
/*      */       }
/*      */       else {
/*  633 */         translationsDone += ref.reputationLevels.size();
/*      */       }
/*      */       
/*  636 */       return new int[] { translationsDone, translationsMissing };
/*      */     }
/*      */     
/*      */     public Dialogue getDialogue(MillVillager v1, MillVillager v2)
/*      */     {
/*  641 */       List<Dialogue> possibleDialogues = new ArrayList();
/*      */       
/*  643 */       for (Dialogue d : this.dialogues.values()) {
/*  644 */         if (d.isValidFor(v1, v2)) {
/*  645 */           possibleDialogues.add(d);
/*  646 */         } else if (d.isValidFor(v2, v1)) {
/*  647 */           possibleDialogues.add(d);
/*      */         }
/*      */       }
/*      */       
/*  651 */       if (possibleDialogues.isEmpty()) {
/*  652 */         return null;
/*      */       }
/*      */       
/*  655 */       org.millenaire.common.core.MillCommonUtilities.WeightedChoice wc = MillCommonUtilities.getWeightedChoice(possibleDialogues, null);
/*      */       
/*  657 */       return (Dialogue)wc;
/*      */     }
/*      */     
/*      */     public ReputationLevel getReputationLevel(int reputation)
/*      */     {
/*  662 */       if (this.reputationLevels.size() == 0) {
/*  663 */         return null;
/*      */       }
/*      */       
/*  666 */       int i = this.reputationLevels.size() - 1;
/*  667 */       while ((i > 0) && (((ReputationLevel)this.reputationLevels.get(i)).level > reputation)) {
/*  668 */         i--;
/*      */       }
/*  670 */       return (ReputationLevel)this.reputationLevels.get(i);
/*      */     }
/*      */     
/*      */     private void loadBuildingNames(List<File> languageDirs)
/*      */     {
/*  675 */       for (File languageDir : languageDirs)
/*      */       {
/*  677 */         File file = new File(new File(languageDir, this.language), this.culture.key + "_buildings.txt");
/*      */         
/*  679 */         if (!file.exists()) {
/*  680 */           file = new File(new File(languageDir, this.language.split("_")[0]), this.culture.key + "_buildings.txt");
/*      */         }
/*      */         
/*  683 */         if (file.exists()) {
/*  684 */           readBuildingNameFile(file);
/*      */         }
/*      */       }
/*      */       
/*  688 */       for (BuildingPlanSet set : this.culture.ListPlanSets) {
/*  689 */         for (BuildingPlan[] plans : set.plans) {
/*  690 */           for (BuildingPlan plan : plans) {
/*  691 */             loadBuildingPlanName(plan);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */     private void loadBuildingPlanName(BuildingPlan plan)
/*      */     {
/*  699 */       String planNameLC = plan.planName.toLowerCase();
/*      */       
/*  701 */       for (String key : plan.names.keySet()) {
/*  702 */         if (key.equalsIgnoreCase("english")) {
/*  703 */           if (this.language.equals("en")) {
/*  704 */             this.buildingNames.put(planNameLC, plan.names.get(key));
/*      */           }
/*  706 */         } else if ((key.startsWith("name_")) && ((key.endsWith("_" + this.language)) || (key.endsWith("_" + this.language.split("_")[0])))) {
/*  707 */           this.buildingNames.put(planNameLC, plan.names.get(key));
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */     private void loadCultureStrings(List<File> languageDirs)
/*      */     {
/*  714 */       for (File languageDir : languageDirs)
/*      */       {
/*  716 */         File file = new File(new File(languageDir, this.language), this.culture.key + "_strings.txt");
/*      */         
/*  718 */         if (!file.exists()) {
/*  719 */           file = new File(new File(languageDir, this.language.split("_")[0]), this.culture.key + "_strings.txt");
/*      */         }
/*      */         
/*  722 */         if (file.exists()) {
/*  723 */           readCultureStringFile(file);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */     private void loadDialogues(List<File> languageDirs)
/*      */     {
/*  730 */       for (File languageDir : languageDirs)
/*      */       {
/*  732 */         File file = new File(new File(languageDir, this.language), this.culture.key + "_dialogues.txt");
/*      */         
/*  734 */         if (!file.exists()) {
/*  735 */           file = new File(new File(languageDir, this.language.split("_")[0]), this.culture.key + "_dialogues.txt");
/*      */         }
/*      */         
/*  738 */         if (file.exists()) {
/*  739 */           readDialoguesFile(file);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */     public void loadFromDisk(List<File> languageDirs) {
/*  745 */       loadBuildingNames(languageDirs);
/*  746 */       loadCultureStrings(languageDirs);
/*  747 */       loadSentences(languageDirs);
/*  748 */       loadDialogues(languageDirs);
/*  749 */       loadReputations(languageDirs);
/*      */       
/*  751 */       if (!this.culture.loadedLanguages.containsKey(this.language)) {
/*  752 */         this.culture.loadedLanguages.put(this.language, this);
/*      */       }
/*      */     }
/*      */     
/*      */     private void loadReputationFile(File file)
/*      */     {
/*      */       try
/*      */       {
/*  760 */         BufferedReader reader = MillCommonUtilities.getReader(file);
/*      */         String line;
/*  762 */         while ((line = reader.readLine()) != null) {
/*  763 */           if (line.split(";").length > 2) {
/*  764 */             this.reputationLevels.add(new ReputationLevel(file, line));
/*      */           }
/*      */         }
/*      */       } catch (Exception e) {
/*  768 */         MLN.printException(e);
/*      */       }
/*  770 */       Collections.sort(this.reputationLevels);
/*      */     }
/*      */     
/*      */     private void loadReputations(List<File> languageDirs)
/*      */     {
/*  775 */       for (File languageDir : languageDirs)
/*      */       {
/*  777 */         File file = new File(new File(languageDir, this.language), this.culture.key + "_reputation.txt");
/*      */         
/*  779 */         if (!file.exists()) {
/*  780 */           file = new File(new File(languageDir, this.language.split("_")[0]), this.culture.key + "_reputation.txt");
/*      */         }
/*      */         
/*  783 */         if (file.exists()) {
/*  784 */           loadReputationFile(file);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */     private void loadSentences(List<File> languageDirs)
/*      */     {
/*  791 */       for (File languageDir : languageDirs)
/*      */       {
/*  793 */         File file = new File(new File(languageDir, this.language), this.culture.key + "_sentences.txt");
/*      */         
/*  795 */         if (!file.exists()) {
/*  796 */           file = new File(new File(languageDir, this.language.split("_")[0]), this.culture.key + "_sentences.txt");
/*      */         }
/*      */         
/*  799 */         if (file.exists()) {
/*  800 */           readSentenceFile(file);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */     private void readBuildingNameFile(File file) {
/*      */       try {
/*  807 */         BufferedReader reader = MillCommonUtilities.getReader(file);
/*      */         
/*      */         String line;
/*      */         
/*  811 */         while ((line = reader.readLine()) != null) {
/*  812 */           if ((line.trim().length() > 0) && (!line.startsWith("//"))) {
/*  813 */             String[] temp = line.trim().split("=");
/*  814 */             if (temp.length == 2)
/*      */             {
/*  816 */               String key = temp[0].toLowerCase();
/*  817 */               String value = temp[1].trim();
/*      */               
/*  819 */               this.buildingNames.put(key, value);
/*      */               
/*  821 */               if (MLN.LogTranslation >= 2) {
/*  822 */                 MLN.minor(this, "Loading name: " + value + " for " + key);
/*      */               }
/*  824 */             } else if (temp.length == 1) {
/*  825 */               String key = temp[0].toLowerCase();
/*      */               
/*  827 */               this.buildingNames.put(key, "");
/*      */               
/*  829 */               if (MLN.LogTranslation >= 2) {
/*  830 */                 MLN.minor(this, "Loading empty name for " + key);
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*  835 */         reader.close();
/*      */       } catch (Exception e) {
/*  837 */         MLN.printException(e);
/*      */       }
/*      */     }
/*      */     
/*      */     private void readCultureStringFile(File file)
/*      */     {
/*      */       try {
/*  844 */         BufferedReader reader = MillCommonUtilities.getReader(file);
/*      */         
/*      */         String line;
/*      */         
/*  848 */         while ((line = reader.readLine()) != null) {
/*  849 */           if ((line.trim().length() > 0) && (!line.startsWith("//"))) {
/*  850 */             String[] temp = line.trim().split("=");
/*  851 */             if (temp.length == 2)
/*      */             {
/*  853 */               String key = temp[0].toLowerCase();
/*  854 */               String value = temp[1].trim();
/*      */               
/*  856 */               this.strings.put(key, value);
/*  857 */               if (MLN.LogTranslation >= 2) {
/*  858 */                 MLN.minor(this, "Loading name: " + value + " for " + key);
/*      */               }
/*  860 */             } else if (temp.length == 1) {
/*  861 */               String key = temp[0].toLowerCase();
/*      */               
/*  863 */               this.strings.put(key, "");
/*  864 */               if (MLN.LogTranslation >= 2) {
/*  865 */                 MLN.minor(this, "Loading empty name for " + key);
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*  870 */         reader.close();
/*      */       } catch (Exception e) {
/*  872 */         MLN.printException(e);
/*      */       }
/*      */     }
/*      */     
/*      */     private boolean readDialoguesFile(File file)
/*      */     {
/*      */       try {
/*  879 */         BufferedReader reader = MillCommonUtilities.getReader(file);
/*      */         
/*      */ 
/*      */ 
/*  883 */         Dialogue dialogue = null;
/*      */         String line;
/*  885 */         while ((line = reader.readLine()) != null) {
/*  886 */           if ((line.trim().length() > 0) && (!line.startsWith("//")))
/*      */           {
/*  888 */             line = line.trim();
/*      */             
/*  890 */             if ((line.startsWith("newchat;")) && (line.split(";").length == 2))
/*      */             {
/*  892 */               if (dialogue != null) {
/*  893 */                 if (dialogue.speechBy.size() > 0) {
/*  894 */                   if (this.dialogues.containsKey(dialogue.key)) {
/*  895 */                     MLN.error(this.culture, this.language + ": Trying to register two dialogues with the same key: " + dialogue.key);
/*      */                   } else {
/*  897 */                     dialogue.checkData(this.culture, this.language);
/*  898 */                     this.dialogues.put(dialogue.key, dialogue);
/*      */                   }
/*      */                 }
/*      */                 else {
/*  902 */                   MLN.error(this.culture, "In dialogue file " + file.getAbsolutePath() + " dialogue " + dialogue.key + " has no sentences.");
/*      */                 }
/*      */               }
/*      */               
/*  906 */               String s = line.split(";")[1].trim();
/*      */               
/*  908 */               dialogue = new Dialogue(s);
/*      */               
/*  910 */               if (dialogue.key == null) {
/*  911 */                 MLN.error(this.culture, this.language + ": Could not read dialogue line: " + line);
/*  912 */                 dialogue = null;
/*      */               }
/*      */             }
/*  915 */             else if ((dialogue != null) && (line.split(";").length == 3)) {
/*  916 */               String[] temp = line.split(";");
/*      */               
/*  918 */               dialogue.speechBy.add(Integer.valueOf(temp[0].trim().equals("v2") ? 2 : 1));
/*  919 */               dialogue.timeDelays.add(Integer.valueOf(Integer.parseInt(temp[1].trim())));
/*      */               
/*  921 */               List<String> sentence = new ArrayList();
/*  922 */               sentence.add(temp[2]);
/*      */               
/*  924 */               this.sentences.put("villager.chat_" + dialogue.key + "_" + (dialogue.speechBy.size() - 1), sentence);
/*      */             }
/*  926 */             else if (line.trim().length() > 0) {
/*  927 */               MLN.error(this.culture, this.language + ": In dialogue file " + file.getAbsolutePath() + " the following line is invalid: " + line);
/*      */             }
/*      */           }
/*      */         }
/*      */         
/*  932 */         if (dialogue.speechBy.size() > 0) {
/*  933 */           if (this.dialogues.containsKey(dialogue.key)) {
/*  934 */             MLN.error(this.culture, this.language + ": Trying to register two dialogues with the same key: " + dialogue.key);
/*      */           } else {
/*  936 */             dialogue.checkData(this.culture, this.language);
/*  937 */             this.dialogues.put(dialogue.key, dialogue);
/*      */           }
/*      */         }
/*      */         else {
/*  941 */           MLN.error(this.culture, this.language + ": In dialogue file " + file.getAbsolutePath() + " dialogue " + dialogue.key + " has no sentences.");
/*      */         }
/*      */         
/*  944 */         reader.close();
/*      */       } catch (Exception e) {
/*  946 */         MLN.printException(e);
/*  947 */         return false;
/*      */       }
/*      */       
/*  950 */       return true;
/*      */     }
/*      */     
/*      */     private boolean readSentenceFile(File file)
/*      */     {
/*      */       try {
/*  956 */         BufferedReader reader = MillCommonUtilities.getReader(file);
/*      */         
/*      */         String line;
/*      */         
/*  960 */         while ((line = reader.readLine()) != null) {
/*  961 */           if ((line.trim().length() > 0) && (!line.startsWith("//"))) {
/*  962 */             String[] temp = line.split("=");
/*  963 */             if (temp.length == 2)
/*      */             {
/*  965 */               String key = temp[0].toLowerCase();
/*  966 */               String value = temp[1].trim();
/*      */               
/*  968 */               if (this.sentences.containsKey(key)) {
/*  969 */                 ((List)this.sentences.get(key)).add(value);
/*      */               } else {
/*  971 */                 List<String> v = new ArrayList();
/*  972 */                 v.add(value);
/*  973 */                 this.sentences.put(key, v);
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*  978 */         reader.close();
/*      */       } catch (Exception e) {
/*  980 */         MLN.printException(e);
/*  981 */         return false;
/*      */       }
/*      */       
/*  984 */       return true;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  991 */   public static List<Culture> ListCultures = new ArrayList();
/*  992 */   private static HashMap<String, Culture> cultures = new HashMap();
/*      */   
/*  994 */   private static HashMap<String, Culture> serverCultures = new HashMap();
/*      */   
/*  996 */   public static HashMap<String, String> oldShopConversion = new HashMap();
/*      */   private CultureLanguage mainLanguage;
/*      */   
/*  999 */   public static Culture getCultureByName(String name) { if (cultures.containsKey(name)) {
/* 1000 */       return (Culture)cultures.get(name);
/*      */     }
/*      */     
/* 1003 */     if (serverCultures.containsKey(name)) {
/* 1004 */       return (Culture)serverCultures.get(name);
/*      */     }
/*      */     
/* 1007 */     if (Mill.isDistantClient()) {
/* 1008 */       Culture culture = new Culture(name);
/* 1009 */       serverCultures.put(name, culture);
/* 1010 */       return culture;
/*      */     }
/*      */     
/* 1013 */     return null;
/*      */   }
/*      */   
/*      */   public static Culture getRandomCulture() {
/* 1017 */     return (Culture)MillCommonUtilities.getWeightedChoice(ListCultures, null);
/*      */   }
/*      */   
/*      */   public static boolean loadCultures()
/*      */   {
/* 1022 */     ArrayList<File> culturesDirs = new ArrayList();
/*      */     
/* 1024 */     for (File dir : Mill.loadingDirs) {
/* 1025 */       File cultureDir = new File(dir, "cultures");
/*      */       
/* 1027 */       if (cultureDir.exists()) {
/* 1028 */         culturesDirs.add(cultureDir);
/*      */       }
/*      */     }
/*      */     
/* 1032 */     File customcultureDir = new File(Mill.proxy.getCustomDir(), "custom cultures");
/*      */     
/* 1034 */     if (customcultureDir.exists()) {
/* 1035 */       culturesDirs.add(customcultureDir);
/*      */     }
/*      */     
/*      */ 
/* 1039 */     List<File> culturesDirsBis = (ArrayList)culturesDirs.clone();
/*      */     
/* 1041 */     for (File culturesDir : culturesDirsBis) {
/* 1042 */       for (File cultureDir : culturesDir.listFiles()) {
/* 1043 */         if ((cultureDir.exists()) && (cultureDir.isDirectory()) && (!cultureDir.getName().startsWith(".")) && (!cultures.containsKey(cultureDir.getName())))
/*      */         {
/* 1045 */           if (MLN.LogCulture >= 1) {
/* 1046 */             MLN.major(cultureDir, "Loading culture: " + cultureDir.getName());
/*      */           }
/*      */           
/* 1049 */           Culture culture = new Culture(cultureDir.getName());
/* 1050 */           culture.initialise(culturesDirs);
/* 1051 */           cultures.put(culture.key, culture);
/* 1052 */           ListCultures.add(culture);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 1057 */     if (MLN.LogCulture >= 1) {
/* 1058 */       MLN.major(null, "Finished loading cultures.");
/*      */     }
/*      */     
/* 1061 */     return false;
/*      */   }
/*      */   
/*      */   public static void readCultureMissingContentPacket(ByteBufInputStream data)
/*      */   {
/*      */     try {
/* 1067 */       String key = data.readUTF();
/* 1068 */       Culture culture = getCultureByName(key);
/*      */       
/* 1070 */       CultureLanguage main = new CultureLanguage(culture, MLN.effective_language, true);
/* 1071 */       CultureLanguage fallback = new CultureLanguage(culture, MLN.fallback_language, true);
/*      */       
/* 1073 */       culture.mainLanguageServer = main;
/* 1074 */       culture.fallbackLanguageServer = fallback;
/*      */       
/* 1076 */       String playerName = Mill.proxy.getTheSinglePlayer().getDisplayName();
/*      */       
/* 1078 */       CultureLanguage[] langs = { main, fallback };
/*      */       CultureLanguage lang;
/* 1080 */       HashMap<String, List<String>> sentences; for (lang : langs) {
/* 1081 */         HashMap<String, String> strings = StreamReadWrite.readStringStringMap(data);
/* 1082 */         for (String k : strings.keySet()) {
/* 1083 */           if (!lang.strings.containsKey(k)) {
/* 1084 */             lang.strings.put(k, ((String)strings.get(k)).replaceAll("\\$name", playerName));
/*      */           }
/*      */         }
/*      */         
/* 1088 */         strings = StreamReadWrite.readStringStringMap(data);
/* 1089 */         for (String k : strings.keySet()) {
/* 1090 */           if (!lang.buildingNames.containsKey(k)) {
/* 1091 */             lang.buildingNames.put(k, ((String)strings.get(k)).replaceAll("\\$name", playerName));
/*      */           }
/*      */         }
/*      */         
/* 1095 */         sentences = StreamReadWrite.readStringStringListMap(data);
/* 1096 */         for (String k : sentences.keySet()) {
/* 1097 */           if (!lang.sentences.containsKey(k)) {
/* 1098 */             List<String> v = new ArrayList();
/* 1099 */             for (String s : (List)sentences.get(k)) {
/* 1100 */               v.add(s.replaceAll("\\$name", playerName));
/*      */             }
/* 1102 */             lang.sentences.put(k, v);
/*      */           }
/*      */         }
/*      */       }
/*      */       
/* 1107 */       int nb = data.readShort();
/* 1108 */       for (int i = 0; i < nb; i++) {
/* 1109 */         key = data.readUTF();
/* 1110 */         BuildingPlanSet set = culture.getBuildingPlanSet(key);
/* 1111 */         set.readBuildingPlanSetInfoPacket(data);
/*      */       }
/*      */       
/* 1114 */       nb = data.readShort();
/* 1115 */       for (int i = 0; i < nb; i++) {
/* 1116 */         key = data.readUTF();
/* 1117 */         VillagerType vtype = culture.getVillagerType(key);
/* 1118 */         vtype.readVillagerTypeInfoPacket(data);
/*      */       }
/*      */       
/* 1121 */       nb = data.readShort();
/* 1122 */       for (int i = 0; i < nb; i++) {
/* 1123 */         key = data.readUTF();
/* 1124 */         VillageType vtype = culture.getVillageType(key);
/* 1125 */         vtype.readVillageTypeInfoPacket(data);
/*      */       }
/*      */       
/* 1128 */       nb = data.readShort();
/* 1129 */       for (int i = 0; i < nb; i++) {
/* 1130 */         key = data.readUTF();
/* 1131 */         VillageType vtype = culture.getLoneBuildingType(key);
/* 1132 */         vtype.readVillageTypeInfoPacket(data);
/*      */       }
/*      */     }
/*      */     catch (java.io.IOException e) {
/* 1136 */       MLN.printException("Error in readCultureInfoPacket: ", e);
/*      */     }
/*      */   }
/*      */   
/*      */   public static void refreshLists()
/*      */   {
/* 1142 */     ListCultures.clear();
/*      */     
/* 1144 */     for (String k : cultures.keySet()) {
/* 1145 */       Culture c = (Culture)cultures.get(k);
/* 1146 */       ListCultures.add(c);
/*      */     }
/*      */     
/* 1149 */     for (String k : serverCultures.keySet()) {
/* 1150 */       Culture c = (Culture)serverCultures.get(k);
/* 1151 */       ListCultures.add(c);
/*      */     }
/*      */     
/* 1154 */     for (Iterator i$ = ListCultures.iterator(); i$.hasNext();) { c = (Culture)i$.next();
/*      */       
/* 1156 */       c.ListPlanSets.clear();
/* 1157 */       for (String key : c.planSet.keySet()) {
/* 1158 */         c.ListPlanSets.add(c.planSet.get(key));
/*      */       }
/* 1160 */       for (String key : c.serverPlanSet.keySet()) {
/* 1161 */         c.ListPlanSets.add(c.serverPlanSet.get(key));
/*      */       }
/*      */       
/* 1164 */       c.listVillagerTypes.clear();
/* 1165 */       for (String key : c.villagerTypes.keySet()) {
/* 1166 */         c.listVillagerTypes.add(c.villagerTypes.get(key));
/*      */       }
/* 1168 */       for (String key : c.serverVillagerTypes.keySet()) {
/* 1169 */         c.listVillagerTypes.add(c.serverVillagerTypes.get(key));
/*      */       }
/*      */       
/* 1172 */       c.listVillageTypes.clear();
/* 1173 */       for (String key : c.villageTypes.keySet()) {
/* 1174 */         c.listVillageTypes.add(c.villageTypes.get(key));
/*      */       }
/* 1176 */       for (String key : c.serverVillageTypes.keySet()) {
/* 1177 */         c.listVillageTypes.add(c.serverVillageTypes.get(key));
/*      */       }
/*      */       
/* 1180 */       c.listLoneBuildingTypes.clear();
/* 1181 */       for (String key : c.loneBuildingTypes.keySet()) {
/* 1182 */         c.listLoneBuildingTypes.add(c.loneBuildingTypes.get(key));
/*      */       }
/* 1184 */       for (String key : c.serverLoneBuildingTypes.keySet()) {
/* 1185 */         c.listLoneBuildingTypes.add(c.serverLoneBuildingTypes.get(key));
/*      */       }
/*      */     }
/*      */     Culture c;
/*      */   }
/*      */   
/*      */   public static void removeServerContent() {
/* 1192 */     serverCultures.clear();
/*      */     
/* 1194 */     for (String k : cultures.keySet()) {
/* 1195 */       Culture c = (Culture)cultures.get(k);
/* 1196 */       c.serverPlanSet.clear();
/* 1197 */       c.serverVillageTypes.clear();
/* 1198 */       c.serverVillagerTypes.clear();
/* 1199 */       c.serverLoneBuildingTypes.clear();
/*      */       
/* 1201 */       c.mainLanguageServer = null;
/* 1202 */       c.fallbackLanguageServer = null;
/*      */     }
/*      */     
/*      */ 
/* 1206 */     refreshLists();
/*      */   }
/*      */   
/*      */ 
/*      */   private CultureLanguage fallbackLanguage;
/*      */   private CultureLanguage mainLanguageServer;
/*      */   private CultureLanguage fallbackLanguageServer;
/* 1213 */   private final HashMap<String, CultureLanguage> loadedLanguages = new HashMap();
/*      */   public String key;
/*      */   
/* 1216 */   static { oldShopConversion.put("indianarmyforge", "armyforge");
/* 1217 */     oldShopConversion.put("indianforge", "forge");
/* 1218 */     oldShopConversion.put("indiantownhall", "townHall");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/* 1223 */   public String qualifierSeparator = " ";
/*      */   
/* 1225 */   private Map<String, BuildingPlanSet> planSet = new HashMap();
/* 1226 */   private Map<String, BuildingCustomPlan> customBuildings = new HashMap();
/*      */   
/* 1228 */   private final Map<String, BuildingPlanSet> serverPlanSet = new HashMap();
/* 1229 */   private final Map<String, BuildingCustomPlan> serverCustomBuildings = new HashMap();
/*      */   
/* 1231 */   public List<BuildingPlanSet> ListPlanSets = new ArrayList();
/*      */   
/* 1233 */   private final Map<String, VillageType> villageTypes = new HashMap();
/*      */   
/* 1235 */   private final Map<String, VillageType> serverVillageTypes = new HashMap();
/*      */   
/* 1237 */   public List<VillageType> listVillageTypes = new ArrayList();
/*      */   
/* 1239 */   private final Map<String, VillageType> loneBuildingTypes = new HashMap();
/* 1240 */   private final Map<String, VillageType> serverLoneBuildingTypes = new HashMap();
/* 1241 */   public List<VillageType> listLoneBuildingTypes = new ArrayList();
/* 1242 */   public final Map<String, VillagerType> villagerTypes = new HashMap();
/* 1243 */   private final Map<String, VillagerType> serverVillagerTypes = new HashMap();
/* 1244 */   public List<VillagerType> listVillagerTypes = new ArrayList();
/* 1245 */   private final Map<String, List<String>> nameLists = new HashMap();
/*      */   
/* 1247 */   public Map<String, List<Goods>> shopSells = new HashMap();
/* 1248 */   public Map<String, List<Goods>> shopBuys = new HashMap();
/* 1249 */   public Map<String, List<Goods>> shopBuysOptional = new HashMap();
/* 1250 */   public Map<String, List<InvItem>> shopNeeds = new HashMap();
/*      */   
/* 1252 */   public List<Goods> goodsList = new ArrayList();
/* 1253 */   public Map<String, Goods> goods = new HashMap();
/* 1254 */   public Map<InvItem, Goods> goodsByItem = new HashMap();
/*      */   
/* 1256 */   public List<String> knownCrops = new ArrayList();
/*      */   
/*      */   public Culture(String s) {
/* 1259 */     this.key = s;
/*      */   }
/*      */   
/*      */   public boolean canReadBuildingNames()
/*      */   {
/* 1264 */     if (Mill.proxy.getClientProfile() == null) {
/* 1265 */       return true;
/*      */     }
/*      */     
/* 1268 */     return (!MLN.languageLearning) || (Mill.proxy.getClientProfile().getCultureLanguageKnowledge(this.key) >= 100);
/*      */   }
/*      */   
/*      */   public boolean canReadDialogues(String username) {
/* 1272 */     if (Mill.proxy.getClientProfile() == null) {
/* 1273 */       return true;
/*      */     }
/*      */     
/* 1276 */     return (!MLN.languageLearning) || (Mill.proxy.getClientProfile().getCultureLanguageKnowledge(this.key) >= 500);
/*      */   }
/*      */   
/*      */   public boolean canReadVillagerNames(String username) {
/* 1280 */     if (Mill.proxy.getClientProfile() == null) {
/* 1281 */       return true;
/*      */     }
/*      */     
/* 1284 */     return (!MLN.languageLearning) || (Mill.proxy.getClientProfile().getCultureLanguageKnowledge(this.key) >= 200);
/*      */   }
/*      */   
/*      */   public int[] compareCultureLanguages(String main, String ref, BufferedWriter writer) throws Exception
/*      */   {
/* 1289 */     CultureLanguage maincl = null;CultureLanguage refcl = null;
/*      */     
/* 1291 */     if (this.loadedLanguages.containsKey(main)) {
/* 1292 */       maincl = (CultureLanguage)this.loadedLanguages.get(main);
/*      */     }
/*      */     
/* 1295 */     if (this.loadedLanguages.containsKey(ref)) {
/* 1296 */       refcl = (CultureLanguage)this.loadedLanguages.get(ref);
/*      */     }
/*      */     
/* 1299 */     if (refcl == null) {
/* 1300 */       return new int[] { 0, 0 };
/*      */     }
/*      */     
/* 1303 */     if (maincl == null) {
/* 1304 */       writer.write("Data for culture " + this.key + " is missing." + MLN.EOL + MLN.EOL);
/*      */       
/* 1306 */       return new int[] { 0, refcl.buildingNames.size() + refcl.reputationLevels.size() + refcl.sentences.size() + refcl.strings.size() };
/*      */     }
/*      */     
/* 1309 */     return maincl.compareWithLanguage(refcl, writer);
/*      */   }
/*      */   
/*      */   public BuildingCustomPlan getBuildingCustom(String key) {
/* 1313 */     if (this.customBuildings.containsKey(key)) {
/* 1314 */       return (BuildingCustomPlan)this.customBuildings.get(key);
/*      */     }
/*      */     
/* 1317 */     if (this.serverCustomBuildings.containsKey(key)) {
/* 1318 */       return (BuildingCustomPlan)this.serverCustomBuildings.get(key);
/*      */     }
/*      */     
/* 1321 */     if (Mill.isDistantClient()) {
/* 1322 */       BuildingCustomPlan set = new BuildingCustomPlan(this, key);
/* 1323 */       this.serverCustomBuildings.put(key, set);
/* 1324 */       return set;
/*      */     }
/* 1326 */     return null;
/*      */   }
/*      */   
/*      */   public String getBuildingGameName(BuildingPlan plan)
/*      */   {
/* 1331 */     String planNameLC = plan.planName.toLowerCase();
/*      */     
/* 1333 */     if ((this.mainLanguage != null) && (this.mainLanguage.buildingNames.containsKey(planNameLC)))
/* 1334 */       return (String)this.mainLanguage.buildingNames.get(planNameLC);
/* 1335 */     if ((this.mainLanguageServer != null) && (this.mainLanguageServer.buildingNames.containsKey(planNameLC)))
/* 1336 */       return (String)this.mainLanguageServer.buildingNames.get(planNameLC);
/* 1337 */     if ((this.fallbackLanguage != null) && (this.fallbackLanguage.buildingNames.containsKey(planNameLC)))
/* 1338 */       return (String)this.fallbackLanguage.buildingNames.get(planNameLC);
/* 1339 */     if ((this.fallbackLanguageServer != null) && (this.fallbackLanguageServer.buildingNames.containsKey(planNameLC))) {
/* 1340 */       return (String)this.fallbackLanguageServer.buildingNames.get(planNameLC);
/*      */     }
/*      */     
/* 1343 */     if (plan.parent != null) {
/* 1344 */       return getBuildingGameName(plan.parent);
/*      */     }
/*      */     
/* 1347 */     if ((MLN.LogTranslation >= 1) || (MLN.generateTranslationGap)) {
/* 1348 */       MLN.major(this, "Could not find the building name for :" + plan.planName);
/*      */     }
/*      */     
/* 1351 */     return null;
/*      */   }
/*      */   
/*      */   public BuildingPlanSet getBuildingPlanSet(String key) {
/* 1355 */     if (this.planSet.containsKey(key)) {
/* 1356 */       return (BuildingPlanSet)this.planSet.get(key);
/*      */     }
/*      */     
/* 1359 */     if (this.serverPlanSet.containsKey(key)) {
/* 1360 */       return (BuildingPlanSet)this.serverPlanSet.get(key);
/*      */     }
/*      */     
/* 1363 */     if (Mill.isDistantClient()) {
/* 1364 */       BuildingPlanSet set = new BuildingPlanSet(this, key, null);
/* 1365 */       this.serverPlanSet.put(key, set);
/* 1366 */       return set;
/*      */     }
/* 1368 */     return null;
/*      */   }
/*      */   
/*      */   public int getChoiceWeight() {
/* 1372 */     return 10;
/*      */   }
/*      */   
/*      */   public String getCultureGameName() {
/* 1376 */     return getCultureString("culture." + this.key);
/*      */   }
/*      */   
/*      */   public String getCultureString(String key) {
/* 1380 */     key = key.toLowerCase();
/* 1381 */     if ((this.mainLanguage != null) && (this.mainLanguage.strings.containsKey(key)))
/* 1382 */       return (String)this.mainLanguage.strings.get(key);
/* 1383 */     if (MLN.getRawStringMainOnly(key, false) != null)
/* 1384 */       return MLN.getRawStringMainOnly(key, false);
/* 1385 */     if ((this.mainLanguageServer != null) && (this.mainLanguageServer.strings.containsKey(key)))
/* 1386 */       return (String)this.mainLanguageServer.strings.get(key);
/* 1387 */     if ((this.fallbackLanguage != null) && (this.fallbackLanguage.strings.containsKey(key)))
/* 1388 */       return (String)this.fallbackLanguage.strings.get(key);
/* 1389 */     if (MLN.getRawStringFallbackOnly(key, false) != null)
/* 1390 */       return MLN.getRawStringFallbackOnly(key, false);
/* 1391 */     if ((this.fallbackLanguageServer != null) && (this.fallbackLanguageServer.strings.containsKey(key))) {
/* 1392 */       return (String)this.fallbackLanguageServer.strings.get(key);
/*      */     }
/* 1394 */     return key;
/*      */   }
/*      */   
/*      */   public String getCustomBuildingGameName(BuildingCustomPlan customBuilding)
/*      */   {
/*      */     String planNameLC;
/*      */     String planNameLC;
/* 1401 */     if (customBuilding.gameNameKey != null) {
/* 1402 */       planNameLC = customBuilding.gameNameKey.toLowerCase();
/*      */     } else {
/* 1404 */       planNameLC = customBuilding.buildingKey.toLowerCase();
/*      */     }
/*      */     
/* 1407 */     if ((this.mainLanguage != null) && (this.mainLanguage.buildingNames.containsKey(planNameLC)))
/* 1408 */       return (String)this.mainLanguage.buildingNames.get(planNameLC);
/* 1409 */     if ((this.mainLanguageServer != null) && (this.mainLanguageServer.buildingNames.containsKey(planNameLC)))
/* 1410 */       return (String)this.mainLanguageServer.buildingNames.get(planNameLC);
/* 1411 */     if ((this.fallbackLanguage != null) && (this.fallbackLanguage.buildingNames.containsKey(planNameLC)))
/* 1412 */       return (String)this.fallbackLanguage.buildingNames.get(planNameLC);
/* 1413 */     if ((this.fallbackLanguageServer != null) && (this.fallbackLanguageServer.buildingNames.containsKey(planNameLC))) {
/* 1414 */       return (String)this.fallbackLanguageServer.buildingNames.get(planNameLC);
/*      */     }
/*      */     
/* 1417 */     if ((MLN.LogTranslation >= 1) || (MLN.generateTranslationGap)) {
/* 1418 */       MLN.major(this, "Could not find the custom building name for :" + customBuilding.buildingKey);
/*      */     }
/*      */     
/* 1421 */     return null;
/*      */   }
/*      */   
/*      */   public Culture.CultureLanguage.Dialogue getDialog(MillVillager v1, MillVillager v2)
/*      */   {
/* 1426 */     Culture.CultureLanguage.Dialogue d = this.mainLanguage.getDialogue(v1, v2);
/*      */     
/* 1428 */     if (d != null) {
/* 1429 */       return d;
/*      */     }
/*      */     
/* 1432 */     if (this.mainLanguageServer != null) {
/* 1433 */       d = this.mainLanguageServer.getDialogue(v1, v2);
/*      */     }
/*      */     
/* 1436 */     if (d != null) {
/* 1437 */       return d;
/*      */     }
/*      */     
/* 1440 */     if (this.fallbackLanguage != null) {
/* 1441 */       d = this.fallbackLanguage.getDialogue(v1, v2);
/*      */     }
/*      */     
/* 1444 */     if (d != null) {
/* 1445 */       return d;
/*      */     }
/*      */     
/* 1448 */     if (this.fallbackLanguageServer != null) {
/* 1449 */       d = this.fallbackLanguageServer.getDialogue(v1, v2);
/*      */     }
/*      */     
/* 1452 */     if (d != null) {
/* 1453 */       return d;
/*      */     }
/*      */     
/* 1456 */     return null;
/*      */   }
/*      */   
/*      */   public Culture.CultureLanguage.Dialogue getDialogue(String key)
/*      */   {
/* 1461 */     if (this.mainLanguage.dialogues.containsKey(key)) {
/* 1462 */       return (Culture.CultureLanguage.Dialogue)this.mainLanguage.dialogues.get(key);
/*      */     }
/*      */     
/* 1465 */     if ((this.mainLanguageServer != null) && (this.mainLanguageServer.dialogues.containsKey(key))) {
/* 1466 */       return (Culture.CultureLanguage.Dialogue)this.mainLanguageServer.dialogues.get(key);
/*      */     }
/*      */     
/* 1469 */     if ((this.fallbackLanguage != null) && (this.fallbackLanguage.dialogues.containsKey(key))) {
/* 1470 */       return (Culture.CultureLanguage.Dialogue)this.fallbackLanguage.dialogues.get(key);
/*      */     }
/*      */     
/* 1473 */     if ((this.fallbackLanguageServer != null) && (this.fallbackLanguageServer.dialogues.containsKey(key))) {
/* 1474 */       return (Culture.CultureLanguage.Dialogue)this.fallbackLanguageServer.dialogues.get(key);
/*      */     }
/*      */     
/* 1477 */     return null;
/*      */   }
/*      */   
/*      */   public String getLanguageLevelString()
/*      */   {
/* 1482 */     if (Mill.proxy.getClientProfile() == null) {
/* 1483 */       return MLN.string("culturelanguage.minimal");
/*      */     }
/*      */     
/* 1486 */     if (Mill.proxy.getClientProfile().getCultureLanguageKnowledge(this.key) >= 500) {
/* 1487 */       return MLN.string("culturelanguage.fluent");
/*      */     }
/* 1489 */     if (Mill.proxy.getClientProfile().getCultureLanguageKnowledge(this.key) >= 200) {
/* 1490 */       return MLN.string("culturelanguage.moderate");
/*      */     }
/* 1492 */     if (Mill.proxy.getClientProfile().getCultureLanguageKnowledge(this.key) >= 100) {
/* 1493 */       return MLN.string("culturelanguage.beginner");
/*      */     }
/*      */     
/* 1496 */     return MLN.string("culturelanguage.minimal");
/*      */   }
/*      */   
/*      */   public VillageType getLoneBuildingType(String key) {
/* 1500 */     if (this.loneBuildingTypes.containsKey(key)) {
/* 1501 */       return (VillageType)this.loneBuildingTypes.get(key);
/*      */     }
/*      */     
/* 1504 */     if (this.serverLoneBuildingTypes.containsKey(key)) {
/* 1505 */       return (VillageType)this.serverLoneBuildingTypes.get(key);
/*      */     }
/*      */     
/* 1508 */     if (Mill.isDistantClient()) {
/* 1509 */       VillageType vtype = new VillageType(this, key, false);
/* 1510 */       this.serverLoneBuildingTypes.put(key, vtype);
/* 1511 */       return vtype;
/*      */     }
/* 1513 */     return null;
/*      */   }
/*      */   
/*      */   public List<BuildingPlanSet> getPlanSetsWithTag(String tag) {
/* 1517 */     List<BuildingPlanSet> sets = new ArrayList();
/*      */     
/* 1519 */     for (BuildingPlanSet set : this.ListPlanSets) {
/* 1520 */       if (((BuildingPlan[])set.plans.get(0))[0].tags.contains(tag)) {
/* 1521 */         sets.add(set);
/*      */       }
/*      */     }
/* 1524 */     return sets;
/*      */   }
/*      */   
/*      */   public VillagerType getRandomForeignMerchant()
/*      */   {
/* 1529 */     List<VillagerType> foreignMerchants = new ArrayList();
/*      */     
/* 1531 */     for (VillagerType v : this.listVillagerTypes) {
/* 1532 */       if (v.isForeignMerchant) {
/* 1533 */         foreignMerchants.add(v);
/*      */       }
/*      */     }
/*      */     
/* 1537 */     if (foreignMerchants.size() == 0) {
/* 1538 */       return null;
/*      */     }
/*      */     
/* 1541 */     return (VillagerType)MillCommonUtilities.getWeightedChoice(foreignMerchants, null);
/*      */   }
/*      */   
/*      */   public String getRandomNameFromList(String listName) {
/* 1545 */     List<String> list = (List)this.nameLists.get(listName);
/* 1546 */     if (list == null) {
/* 1547 */       MLN.error(this, "Could not find name list: " + listName);
/* 1548 */       return null;
/*      */     }
/* 1550 */     return (String)list.get(MillCommonUtilities.randomInt(list.size()));
/*      */   }
/*      */   
/*      */   public VillageType getRandomVillage() {
/* 1554 */     return (VillageType)MillCommonUtilities.getWeightedChoice(this.listVillageTypes, null);
/*      */   }
/*      */   
/*      */   public Culture.CultureLanguage.ReputationLevel getReputationLevel(int reputation)
/*      */   {
/* 1559 */     Culture.CultureLanguage.ReputationLevel rlevel = null;
/*      */     
/* 1561 */     if (this.mainLanguage != null) {
/* 1562 */       rlevel = this.mainLanguage.getReputationLevel(reputation);
/*      */     }
/*      */     
/* 1565 */     if (rlevel != null) {
/* 1566 */       return rlevel;
/*      */     }
/*      */     
/* 1569 */     if (this.fallbackLanguage != null) {
/* 1570 */       return this.fallbackLanguage.getReputationLevel(reputation);
/*      */     }
/*      */     
/* 1573 */     return null;
/*      */   }
/*      */   
/*      */   public String getReputationLevelDesc(int reputation) {
/* 1577 */     Culture.CultureLanguage.ReputationLevel rlevel = getReputationLevel(reputation);
/*      */     
/* 1579 */     if (rlevel != null) {
/* 1580 */       return rlevel.desc;
/*      */     }
/*      */     
/* 1583 */     return "";
/*      */   }
/*      */   
/*      */   public String getReputationLevelLabel(int reputation) {
/* 1587 */     Culture.CultureLanguage.ReputationLevel rlevel = getReputationLevel(reputation);
/*      */     
/* 1589 */     if (rlevel != null) {
/* 1590 */       return rlevel.label;
/*      */     }
/*      */     
/* 1593 */     return "";
/*      */   }
/*      */   
/*      */   public String getReputationString()
/*      */   {
/* 1598 */     if (Mill.proxy.getClientProfile() == null) {
/* 1599 */       return MLN.string("culturereputation.neutral");
/*      */     }
/*      */     
/* 1602 */     int reputation = Mill.proxy.getClientProfile().getCultureReputation(this.key);
/*      */     
/* 1604 */     if (reputation < 0) {
/* 1605 */       if (reputation <= 64896)
/* 1606 */         return MLN.string("culturereputation.scourgeofgod");
/* 1607 */       if (reputation < -128) {
/* 1608 */         return MLN.string("culturereputation.dreadful");
/*      */       }
/* 1610 */       return MLN.string("culturereputation.bad");
/*      */     }
/*      */     
/* 1613 */     if (reputation > 2048) {
/* 1614 */       return MLN.string("culturereputation.stellar");
/*      */     }
/* 1616 */     if (reputation > 1024) {
/* 1617 */       return MLN.string("culturereputation.excellent");
/*      */     }
/* 1619 */     if (reputation > 512) {
/* 1620 */       return MLN.string("culturereputation.good");
/*      */     }
/* 1622 */     if (reputation > 256) {
/* 1623 */       return MLN.string("culturereputation.decent");
/*      */     }
/*      */     
/*      */ 
/* 1627 */     return MLN.string("culturereputation.neutral");
/*      */   }
/*      */   
/*      */   public List<String> getSentences(String key)
/*      */   {
/* 1632 */     if ((this.mainLanguage != null) && (this.mainLanguage.sentences.containsKey(key))) {
/* 1633 */       return (List)this.mainLanguage.sentences.get(key);
/*      */     }
/*      */     
/* 1636 */     if ((this.mainLanguageServer != null) && (this.mainLanguageServer.sentences.containsKey(key))) {
/* 1637 */       return (List)this.mainLanguageServer.sentences.get(key);
/*      */     }
/*      */     
/* 1640 */     if ((this.fallbackLanguage != null) && (this.fallbackLanguage.sentences.containsKey(key))) {
/* 1641 */       return (List)this.fallbackLanguage.sentences.get(key);
/*      */     }
/*      */     
/* 1644 */     if ((this.fallbackLanguageServer != null) && (this.fallbackLanguageServer.sentences.containsKey(key))) {
/* 1645 */       return (List)this.fallbackLanguageServer.sentences.get(key);
/*      */     }
/*      */     
/* 1648 */     return null;
/*      */   }
/*      */   
/*      */   public VillagerType getVillagerType(String key) {
/* 1652 */     if (this.villagerTypes.containsKey(key)) {
/* 1653 */       return (VillagerType)this.villagerTypes.get(key);
/*      */     }
/*      */     
/* 1656 */     if (this.serverVillagerTypes.containsKey(key)) {
/* 1657 */       return (VillagerType)this.serverVillagerTypes.get(key);
/*      */     }
/*      */     
/* 1660 */     if (Mill.isDistantClient()) {
/* 1661 */       VillagerType vtype = new VillagerType(this, key);
/* 1662 */       this.serverVillagerTypes.put(key, vtype);
/* 1663 */       return vtype;
/*      */     }
/*      */     
/* 1666 */     MLN.error(this, "Could not find villager type: " + key);
/*      */     
/* 1668 */     return null;
/*      */   }
/*      */   
/*      */   public VillageType getVillageType(String key)
/*      */   {
/* 1673 */     if (this.villageTypes.containsKey(key)) {
/* 1674 */       return (VillageType)this.villageTypes.get(key);
/*      */     }
/*      */     
/* 1677 */     if (this.serverVillageTypes.containsKey(key)) {
/* 1678 */       return (VillageType)this.serverVillageTypes.get(key);
/*      */     }
/*      */     
/* 1681 */     if (Mill.isDistantClient()) {
/* 1682 */       VillageType vtype = new VillageType(this, key, false);
/* 1683 */       this.serverVillageTypes.put(key, vtype);
/* 1684 */       return vtype;
/*      */     }
/* 1686 */     return null;
/*      */   }
/*      */   
/*      */   public boolean hasSentences(String key) {
/* 1690 */     return getSentences(key) != null;
/*      */   }
/*      */   
/*      */   public boolean initialise(List<File> culturesDirs)
/*      */   {
/* 1695 */     List<File> thisCultureDirs = new ArrayList();
/*      */     
/* 1697 */     for (File culturesDir : culturesDirs)
/*      */     {
/* 1699 */       File dir = new File(culturesDir, this.key);
/*      */       
/* 1701 */       if (dir.exists()) {
/* 1702 */         thisCultureDirs.add(dir);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */     try
/*      */     {
/* 1709 */       readConfig(thisCultureDirs);
/* 1710 */       loadNameLists(thisCultureDirs);
/* 1711 */       loadGoods(thisCultureDirs);
/* 1712 */       loadShops(thisCultureDirs);
/* 1713 */       loadVillagerTypes(thisCultureDirs);
/*      */       
/* 1715 */       this.planSet = BuildingPlan.loadPlans(thisCultureDirs, this);
/*      */       
/* 1717 */       if (this.planSet == null) {
/* 1718 */         return false;
/*      */       }
/*      */       
/* 1721 */       this.customBuildings = BuildingCustomPlan.loadCustomBuildings(thisCultureDirs, this);
/*      */       
/* 1723 */       if (this.customBuildings == null) {
/* 1724 */         return false;
/*      */       }
/*      */       
/* 1727 */       this.ListPlanSets.addAll(this.planSet.values());
/*      */       
/* 1729 */       if (MLN.LogBuildingPlan >= 1) {
/* 1730 */         for (BuildingPlanSet set : this.ListPlanSets) {
/* 1731 */           MLN.major(set, "Loaded plan set: " + set.key);
/*      */         }
/*      */       }
/*      */       
/* 1735 */       this.listVillageTypes = VillageType.loadVillages(thisCultureDirs, this);
/*      */       
/* 1737 */       if (this.listVillageTypes == null) {
/* 1738 */         return false;
/*      */       }
/*      */       
/* 1741 */       for (VillageType v : this.listVillageTypes) {
/* 1742 */         this.villageTypes.put(v.key, v);
/*      */       }
/*      */       
/* 1745 */       this.listLoneBuildingTypes = VillageType.loadLoneBuildings(thisCultureDirs, this);
/*      */       
/* 1747 */       for (VillageType v : this.listLoneBuildingTypes) {
/* 1748 */         this.loneBuildingTypes.put(v.key, v);
/*      */       }
/*      */       
/* 1751 */       if (MLN.LogCulture >= 1) {
/* 1752 */         MLN.major(this, "Finished loading culture.");
/*      */       }
/* 1754 */       return true;
/*      */     }
/*      */     catch (Exception e)
/*      */     {
/* 1758 */       MLN.printException("Error when loading culture: ", e);
/*      */     }
/* 1760 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */   private void loadGoods(List<File> culturesDirs)
/*      */   {
/* 1766 */     List<File> files = new ArrayList();
/*      */     
/* 1768 */     for (File culturesDir : culturesDirs) {
/* 1769 */       File dir = new File(culturesDir, "traded_goods.txt");
/*      */       
/* 1771 */       if (dir.exists()) {
/* 1772 */         files.add(dir);
/*      */       }
/*      */     }
/*      */     
/* 1776 */     File dir = new File(new File(new File(Mill.proxy.getCustomDir(), "cultures"), this.key), "traded_goods.txt");
/*      */     
/* 1778 */     if (dir.exists()) {
/* 1779 */       files.add(dir);
/*      */     }
/*      */     
/* 1782 */     for (File file : files) {
/*      */       try
/*      */       {
/* 1785 */         if (!file.exists()) {
/* 1786 */           file.createNewFile();
/*      */         }
/*      */         
/* 1789 */         BufferedReader reader = MillCommonUtilities.getReader(file);
/*      */         
/*      */         String line;
/*      */         
/* 1793 */         while ((line = reader.readLine()) != null) {
/* 1794 */           if ((line.trim().length() > 0) && (!line.startsWith("//")))
/*      */           {
/*      */             try
/*      */             {
/* 1798 */               String[] values = line.split(",");
/*      */               
/* 1800 */               String name = values[0].toLowerCase();
/*      */               
/* 1802 */               if (Goods.goodsName.containsKey(name)) {
/* 1803 */                 InvItem item = (InvItem)Goods.goodsName.get(name);
/* 1804 */                 int sellingPrice = (values.length > 1) && (!values[1].isEmpty()) ? MillCommonUtilities.readInteger(values[1]) : 0;
/* 1805 */                 int buyingPrice = (values.length > 2) && (!values[2].isEmpty()) ? MillCommonUtilities.readInteger(values[2]) : 0;
/* 1806 */                 int reservedQuantity = (values.length > 3) && (!values[3].isEmpty()) ? MillCommonUtilities.readInteger(values[3]) : 0;
/* 1807 */                 int targetQuantity = (values.length > 4) && (!values[4].isEmpty()) ? MillCommonUtilities.readInteger(values[4]) : 0;
/* 1808 */                 int foreignMerchantPrice = (values.length > 5) && (!values[5].isEmpty()) ? MillCommonUtilities.readInteger(values[5]) : 0;
/* 1809 */                 boolean autoGenerate = (values.length > 6) && (!values[6].isEmpty()) ? Boolean.parseBoolean(values[6]) : false;
/* 1810 */                 String tag = (values.length > 7) && (!values[7].isEmpty()) ? values[7] : null;
/* 1811 */                 int minReputation = (values.length > 8) && (!values[8].isEmpty()) ? MillCommonUtilities.readInteger(values[8]) : Integer.MIN_VALUE;
/* 1812 */                 String desc = (values.length > 9) && (!values[9].isEmpty()) ? values[9] : null;
/*      */                 
/* 1814 */                 Goods good = new Goods(name, item, sellingPrice, buyingPrice, reservedQuantity, targetQuantity, foreignMerchantPrice, autoGenerate, tag, minReputation, desc);
/*      */                 
/* 1816 */                 if ((this.goods.containsKey(name)) || (this.goodsByItem.containsKey(good.item))) {
/* 1817 */                   MLN.error(this, "Good " + name + " is present twice in the goods list.");
/*      */                 }
/*      */                 
/* 1820 */                 this.goods.put(name, good);
/* 1821 */                 this.goodsByItem.put(good.item, good);
/* 1822 */                 this.goodsList.remove(good);
/* 1823 */                 this.goodsList.add(good);
/*      */                 
/* 1825 */                 if (MLN.LogCulture >= 2) {
/* 1826 */                   MLN.minor(this, "Loaded traded good: " + name + " prices: " + sellingPrice + "/" + buyingPrice);
/*      */                 }
/*      */               }
/*      */               else {
/* 1830 */                 MLN.error(this, "Unknown good on line: " + line);
/*      */               }
/*      */             } catch (Exception e) {
/* 1833 */               MLN.printException("Exception when trying to read trade good on line: " + line, e);
/*      */             }
/*      */           }
/*      */         }
/* 1837 */         reader.close();
/*      */       }
/*      */       catch (Exception e) {
/* 1840 */         MLN.printException(e);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private CultureLanguage loadLanguage(List<File> languageDirs, String key)
/*      */   {
/* 1847 */     if (this.loadedLanguages.containsKey(key)) {
/* 1848 */       return (CultureLanguage)this.loadedLanguages.get(key);
/*      */     }
/*      */     
/* 1851 */     CultureLanguage lang = new CultureLanguage(this, key, false);
/*      */     
/* 1853 */     List<File> languageDirsWithCusto = new ArrayList(languageDirs);
/*      */     
/* 1855 */     File dircusto = new File(new File(new File(Mill.proxy.getCustomDir(), "custom cultures"), key), "languages");
/*      */     
/* 1857 */     if (dircusto.exists()) {
/* 1858 */       languageDirsWithCusto.add(dircusto);
/*      */     }
/*      */     
/* 1861 */     lang.loadFromDisk(languageDirsWithCusto);
/*      */     
/* 1863 */     return lang;
/*      */   }
/*      */   
/*      */   public void loadLanguages(List<File> languageDirs, String effective_language, String fallback_language)
/*      */   {
/* 1868 */     this.mainLanguage = loadLanguage(languageDirs, effective_language);
/* 1869 */     if (!effective_language.equals(fallback_language)) {
/* 1870 */       this.fallbackLanguage = loadLanguage(languageDirs, fallback_language);
/*      */     } else {
/* 1872 */       this.fallbackLanguage = this.mainLanguage;
/*      */     }
/*      */     
/* 1875 */     File mainDir = (File)languageDirs.get(0);
/*      */     
/* 1877 */     for (File lang : mainDir.listFiles()) {
/* 1878 */       if ((lang.isDirectory()) && (!lang.isHidden())) {
/* 1879 */         String key = lang.getName().toLowerCase();
/*      */         
/* 1881 */         if (!this.loadedLanguages.containsKey(key)) {
/* 1882 */           loadLanguage(languageDirs, key);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private void loadNameLists(List<File> culturesDirs)
/*      */   {
/* 1890 */     List<File> listDirs = new ArrayList();
/*      */     
/* 1892 */     for (File culturesDir : culturesDirs) {
/* 1893 */       File dir = new File(culturesDir, "namelists");
/*      */       
/* 1895 */       if (dir.exists()) {
/* 1896 */         listDirs.add(dir);
/*      */       }
/*      */     }
/*      */     
/* 1900 */     File dir = new File(new File(new File(Mill.proxy.getCustomDir(), "cultures"), this.key), "custom namelists");
/*      */     
/* 1902 */     if (dir.exists()) {
/* 1903 */       listDirs.add(dir);
/*      */     }
/*      */     
/* 1906 */     for (File lists : listDirs) {
/*      */       try {
/* 1908 */         for (File file : lists.listFiles(new org.millenaire.common.core.MillCommonUtilities.ExtFileFilter("txt")))
/*      */         {
/* 1910 */           List<String> list = new ArrayList();
/*      */           
/* 1912 */           BufferedReader reader = MillCommonUtilities.getReader(file);
/*      */           String line;
/* 1914 */           while ((line = reader.readLine()) != null) {
/* 1915 */             line = line.trim();
/* 1916 */             if (line.length() > 0) {
/* 1917 */               list.add(line);
/*      */             }
/*      */           }
/*      */           
/* 1921 */           this.nameLists.put(file.getName().split("\\.")[0], list);
/*      */         }
/*      */       } catch (Exception e) {
/* 1924 */         MLN.printException(e);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private void loadShop(File file)
/*      */   {
/*      */     try {
/* 1932 */       BufferedReader reader = MillCommonUtilities.getReader(file);
/*      */       
/*      */       String line;
/*      */       
/* 1936 */       while ((line = reader.readLine()) != null) {
/* 1937 */         if ((line.trim().length() > 0) && (!line.startsWith("//"))) {
/* 1938 */           String[] temp = line.split("=");
/* 1939 */           if (temp.length != 2) {
/* 1940 */             MLN.error(null, "Invalid line when loading shop " + file.getName() + ": " + line);
/*      */           }
/*      */           else {
/* 1943 */             String key = temp[0].toLowerCase();
/* 1944 */             String value = temp[1].toLowerCase();
/*      */             
/* 1946 */             if (key.equals("buys")) {
/* 1947 */               List<Goods> buys = new ArrayList();
/*      */               
/* 1949 */               for (String name : value.split(",")) {
/* 1950 */                 if (this.goods.containsKey(name)) {
/* 1951 */                   buys.add(this.goods.get(name));
/* 1952 */                   if (MLN.LogSelling >= 2) {
/* 1953 */                     MLN.minor(this, "Loaded buying good " + name + " for shop " + file.getName());
/*      */                   }
/*      */                 } else {
/* 1956 */                   MLN.error(this, "Unknown good when loading shop " + file.getName() + ": " + name);
/*      */                 }
/*      */               }
/* 1959 */               this.shopBuys.put(file.getName().split("\\.")[0], buys);
/* 1960 */             } else if (key.equals("buysoptional")) {
/* 1961 */               List<Goods> buys = new ArrayList();
/*      */               
/* 1963 */               for (String name : value.split(",")) {
/* 1964 */                 if (this.goods.containsKey(name)) {
/* 1965 */                   buys.add(this.goods.get(name));
/* 1966 */                   if (MLN.LogSelling >= 2) {
/* 1967 */                     MLN.minor(this, "Loaded optional buying good " + name + " for shop " + file.getName());
/*      */                   }
/*      */                 } else {
/* 1970 */                   MLN.error(this, "Unknown good when loading shop " + file.getName() + ": " + name);
/*      */                 }
/*      */               }
/* 1973 */               this.shopBuysOptional.put(file.getName().split("\\.")[0], buys);
/* 1974 */             } else if (key.equals("sells")) {
/* 1975 */               List<Goods> sells = new ArrayList();
/*      */               
/* 1977 */               for (String name : value.split(",")) {
/* 1978 */                 if (this.goods.containsKey(name)) {
/* 1979 */                   sells.add(this.goods.get(name));
/*      */                 } else {
/* 1981 */                   MLN.error(this, "Unknown good when loading shop " + file.getName() + ": " + name);
/*      */                 }
/*      */               }
/* 1984 */               this.shopSells.put(file.getName().split("\\.")[0], sells);
/* 1985 */             } else if (key.equals("deliverto")) {
/* 1986 */               List<InvItem> needs = new ArrayList();
/*      */               
/* 1988 */               for (String name : value.split(",")) {
/* 1989 */                 if (Goods.goodsName.containsKey(name)) {
/* 1990 */                   needs.add(Goods.goodsName.get(name));
/*      */                 } else {
/* 1992 */                   MLN.error(this, "Unknown good when loading shop " + file.getName() + ": " + name);
/*      */                 }
/*      */               }
/* 1995 */               this.shopNeeds.put(file.getName().split("\\.")[0], needs);
/*      */             } else {
/* 1997 */               MLN.error(this, "Unknown parameter when loading shop " + file.getName() + ": " + line);
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */       
/* 2003 */       reader.close();
/*      */     } catch (Exception e) {
/* 2005 */       MLN.printException(e);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   private void loadShops(List<File> culturesDirs)
/*      */   {
/* 2012 */     List<File> dirs = new ArrayList();
/*      */     
/* 2014 */     for (File culturesDir : culturesDirs) {
/* 2015 */       File dir = new File(culturesDir, "shops");
/*      */       
/* 2017 */       if (dir.exists()) {
/* 2018 */         dirs.add(dir);
/*      */       }
/*      */     }
/*      */     
/* 2022 */     File dircusto = new File(new File(new File(Mill.proxy.getCustomDir(), "cultures"), this.key), "shops");
/*      */     
/* 2024 */     if (dircusto.exists()) {
/* 2025 */       dirs.add(dircusto);
/*      */     }
/*      */     
/* 2028 */     for (File dir : dirs) {
/* 2029 */       if (!dir.exists()) {
/* 2030 */         dir.mkdirs();
/*      */       }
/*      */       try
/*      */       {
/* 2034 */         for (File file : dir.listFiles(new org.millenaire.common.core.MillCommonUtilities.ExtFileFilter("txt"))) {
/* 2035 */           loadShop(file);
/*      */         }
/*      */       } catch (Exception e) {
/* 2038 */         MLN.printException(e);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private void loadVillagerTypes(List<File> culturesDirs)
/*      */   {
/* 2045 */     List<File> dirs = new ArrayList();
/*      */     
/* 2047 */     for (File culturesDir : culturesDirs) {
/* 2048 */       File dir = new File(culturesDir, "villagers");
/*      */       
/* 2050 */       if (dir.exists()) {
/* 2051 */         dirs.add(dir);
/*      */       }
/*      */     }
/*      */     
/* 2055 */     File dircusto = new File(new File(new File(Mill.proxy.getCustomDir(), "cultures"), this.key), "custom villagers");
/*      */     
/* 2057 */     if (dircusto.exists()) {
/* 2058 */       dirs.add(dircusto);
/*      */     }
/*      */     
/* 2061 */     for (File dir : dirs) {
/*      */       try {
/* 2063 */         for (File file : dir.listFiles(new org.millenaire.common.core.MillCommonUtilities.ExtFileFilter("txt")))
/*      */         {
/* 2065 */           VillagerType vtype = VillagerType.loadVillagerType(file, this);
/*      */           
/* 2067 */           if (vtype != null) {
/* 2068 */             this.villagerTypes.put(vtype.key, vtype);
/* 2069 */             this.listVillagerTypes.add(vtype);
/*      */           }
/*      */         }
/*      */       } catch (Exception e) {
/* 2073 */         MLN.printException(e);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private void readConfig(List<File> culturesDirs)
/*      */   {
/*      */     try
/*      */     {
/* 2082 */       for (File cultureDir : culturesDirs)
/*      */       {
/* 2084 */         File file = new File(cultureDir, "culture.txt");
/*      */         
/* 2086 */         if (file.exists())
/*      */         {
/* 2088 */           BufferedReader reader = MillCommonUtilities.getReader(file);
/*      */           
/*      */           String line;
/*      */           
/* 2092 */           while ((line = reader.readLine()) != null) {
/* 2093 */             if ((line.trim().length() > 0) && (!line.startsWith("//"))) {
/* 2094 */               String[] temp = line.split("=");
/* 2095 */               if (temp.length == 2)
/*      */               {
/* 2097 */                 String key = temp[0];
/* 2098 */                 String value = temp[1];
/* 2099 */                 if (key.equalsIgnoreCase("qualifierSeparator")) {
/* 2100 */                   this.qualifierSeparator = value;
/* 2101 */                 } else if (key.equalsIgnoreCase("knownCrop")) {
/* 2102 */                   this.knownCrops.add(value.trim().toLowerCase());
/*      */                 }
/*      */               }
/*      */             }
/*      */           }
/* 2107 */           reader.close();
/*      */         }
/*      */       }
/*      */     }
/*      */     catch (Exception e) {
/* 2112 */       MLN.printException(e);
/*      */     }
/*      */   }
/*      */   
/*      */   public String toString()
/*      */   {
/* 2118 */     return "Culture: " + this.key;
/*      */   }
/*      */   
/*      */   public void writeCultureAvailableContentPacket(ByteBufOutputStream data) throws java.io.IOException
/*      */   {
/* 2123 */     data.writeUTF(this.key);
/*      */     
/* 2125 */     data.writeShort(this.mainLanguage.strings.size());
/* 2126 */     data.writeShort(this.mainLanguage.buildingNames.size());
/* 2127 */     data.writeShort(this.mainLanguage.sentences.size());
/*      */     
/* 2129 */     data.writeShort(this.fallbackLanguage.strings.size());
/* 2130 */     data.writeShort(this.fallbackLanguage.buildingNames.size());
/* 2131 */     data.writeShort(this.fallbackLanguage.sentences.size());
/*      */     
/* 2133 */     data.writeShort(this.ListPlanSets.size());
/* 2134 */     for (BuildingPlanSet set : this.ListPlanSets) {
/* 2135 */       data.writeUTF(set.key);
/*      */     }
/*      */     
/* 2138 */     data.writeShort(this.villagerTypes.size());
/* 2139 */     for (String key : this.villagerTypes.keySet()) {
/* 2140 */       VillagerType vtype = (VillagerType)this.villagerTypes.get(key);
/* 2141 */       data.writeUTF(vtype.key);
/*      */     }
/*      */     
/* 2144 */     data.writeShort(this.villageTypes.size());
/* 2145 */     for (String key : this.villageTypes.keySet()) {
/* 2146 */       VillageType vtype = (VillageType)this.villageTypes.get(key);
/* 2147 */       data.writeUTF(vtype.key);
/*      */     }
/*      */     
/* 2150 */     data.writeShort(this.loneBuildingTypes.size());
/* 2151 */     for (String key : this.loneBuildingTypes.keySet()) {
/* 2152 */       VillageType vtype = (VillageType)this.loneBuildingTypes.get(key);
/* 2153 */       data.writeUTF(vtype.key);
/*      */     }
/*      */   }
/*      */   
/*      */   public void writeCultureMissingContentPackPacket(DataOutput data, String mainLanguage, String fallbackLanguage, int nbStrings, int nbBuildingNames, int nbSentences, int nbFallbackStrings, int nbFallbackBuildingNames, int nbFallbackSentences, List<String> planSetAvailable, List<String> villagerAvailable, List<String> villagesAvailable, List<String> loneBuildingsAvailable)
/*      */     throws java.io.IOException
/*      */   {
/* 2160 */     data.writeUTF(this.key);
/*      */     
/* 2162 */     CultureLanguage clientMain = null;CultureLanguage clientFallback = null;
/*      */     
/* 2164 */     if (this.loadedLanguages.containsKey(mainLanguage)) {
/* 2165 */       clientMain = (CultureLanguage)this.loadedLanguages.get(mainLanguage);
/* 2166 */     } else if (this.loadedLanguages.containsKey(mainLanguage.split("_")[0])) {
/* 2167 */       clientMain = (CultureLanguage)this.loadedLanguages.get(mainLanguage.split("_")[0]);
/*      */     }
/*      */     
/* 2170 */     if (this.loadedLanguages.containsKey(fallbackLanguage)) {
/* 2171 */       clientFallback = (CultureLanguage)this.loadedLanguages.get(fallbackLanguage);
/* 2172 */     } else if (this.loadedLanguages.containsKey(fallbackLanguage.split("_")[0])) {
/* 2173 */       clientFallback = (CultureLanguage)this.loadedLanguages.get(fallbackLanguage.split("_")[0]);
/*      */     }
/*      */     
/* 2176 */     if ((clientMain != null) && (clientMain.strings.size() > nbStrings)) {
/* 2177 */       StreamReadWrite.writeStringStringMap(clientMain.strings, data);
/*      */     } else {
/* 2179 */       StreamReadWrite.writeStringStringMap(null, data);
/*      */     }
/* 2181 */     if ((clientMain != null) && (clientMain.buildingNames.size() > nbBuildingNames)) {
/* 2182 */       StreamReadWrite.writeStringStringMap(clientMain.buildingNames, data);
/*      */     } else {
/* 2184 */       StreamReadWrite.writeStringStringMap(null, data);
/*      */     }
/* 2186 */     if ((clientMain != null) && (clientMain.sentences.size() > nbSentences)) {
/* 2187 */       StreamReadWrite.writeStringStringListMap(clientMain.sentences, data);
/*      */     } else {
/* 2189 */       StreamReadWrite.writeStringStringMap(null, data);
/*      */     }
/*      */     
/* 2192 */     if ((clientFallback != null) && (clientFallback.strings.size() > nbFallbackStrings)) {
/* 2193 */       StreamReadWrite.writeStringStringMap(clientFallback.strings, data);
/*      */     } else {
/* 2195 */       StreamReadWrite.writeStringStringMap(null, data);
/*      */     }
/* 2197 */     if ((clientFallback != null) && (clientFallback.buildingNames.size() > nbFallbackBuildingNames)) {
/* 2198 */       StreamReadWrite.writeStringStringMap(clientFallback.buildingNames, data);
/*      */     } else {
/* 2200 */       StreamReadWrite.writeStringStringMap(null, data);
/*      */     }
/* 2202 */     if ((clientFallback != null) && (clientFallback.sentences.size() > nbFallbackSentences)) {
/* 2203 */       StreamReadWrite.writeStringStringListMap(clientFallback.sentences, data);
/*      */     } else {
/* 2205 */       StreamReadWrite.writeStringStringMap(null, data);
/*      */     }
/*      */     
/* 2208 */     int nbToWrite = 0;
/*      */     
/* 2210 */     for (BuildingPlanSet set : this.ListPlanSets) {
/* 2211 */       if ((planSetAvailable == null) || (!planSetAvailable.contains(set.key))) {
/* 2212 */         nbToWrite++;
/*      */       }
/*      */     }
/*      */     
/* 2216 */     data.writeShort(nbToWrite);
/* 2217 */     for (BuildingPlanSet set : this.ListPlanSets) {
/* 2218 */       if ((planSetAvailable == null) || (!planSetAvailable.contains(set.key))) {
/* 2219 */         set.writeBuildingPlanSetInfo(data);
/*      */       }
/*      */     }
/*      */     
/* 2223 */     nbToWrite = 0;
/* 2224 */     for (String key : this.villagerTypes.keySet()) {
/* 2225 */       if ((villagerAvailable == null) || (!villagerAvailable.contains(key))) {
/* 2226 */         nbToWrite++;
/*      */       }
/*      */     }
/*      */     
/* 2230 */     data.writeShort(nbToWrite);
/* 2231 */     for (String key : this.villagerTypes.keySet()) {
/* 2232 */       if ((villagerAvailable == null) || (!villagerAvailable.contains(key))) {
/* 2233 */         VillagerType vtype = (VillagerType)this.villagerTypes.get(key);
/* 2234 */         vtype.writeVillagerTypeInfo(data);
/*      */       }
/*      */     }
/*      */     
/* 2238 */     nbToWrite = 0;
/* 2239 */     for (String key : this.villageTypes.keySet()) {
/* 2240 */       if ((villagesAvailable == null) || (!villagesAvailable.contains(key))) {
/* 2241 */         nbToWrite++;
/*      */       }
/*      */     }
/*      */     
/* 2245 */     data.writeShort(nbToWrite);
/* 2246 */     for (String key : this.villageTypes.keySet()) {
/* 2247 */       if ((villagesAvailable == null) || (!villagesAvailable.contains(key))) {
/* 2248 */         VillageType vtype = (VillageType)this.villageTypes.get(key);
/* 2249 */         vtype.writeVillageTypeInfo(data);
/*      */       }
/*      */     }
/* 2252 */     nbToWrite = 0;
/* 2253 */     for (String key : this.loneBuildingTypes.keySet()) {
/* 2254 */       if ((loneBuildingsAvailable == null) || (!loneBuildingsAvailable.contains(key))) {
/* 2255 */         nbToWrite++;
/*      */       }
/*      */     }
/*      */     
/* 2259 */     data.writeShort(nbToWrite);
/* 2260 */     for (String key : this.loneBuildingTypes.keySet()) {
/* 2261 */       if ((loneBuildingsAvailable == null) || (!loneBuildingsAvailable.contains(key))) {
/* 2262 */         VillageType vtype = (VillageType)this.loneBuildingTypes.get(key);
/* 2263 */         vtype.writeVillageTypeInfo(data);
/*      */       }
/*      */     }
/*      */   }
/*      */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\Culture.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */