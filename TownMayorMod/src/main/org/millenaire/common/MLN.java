/*      */ package org.millenaire.common;
/*      */ 
/*      */ import cpw.mods.fml.common.registry.LanguageRegistry;
/*      */ import java.io.BufferedReader;
/*      */ import java.io.BufferedWriter;
/*      */ import java.io.File;
/*      */ import java.io.FileWriter;
/*      */ import java.io.FilenameFilter;
/*      */ import java.io.IOException;
/*      */ import java.io.PrintStream;
/*      */ import java.io.PrintWriter;
/*      */ import java.io.StringWriter;
/*      */ import java.math.BigInteger;
/*      */ import java.security.MessageDigest;
/*      */ import java.security.NoSuchAlgorithmException;
/*      */ import java.text.SimpleDateFormat;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Calendar;
/*      */ import java.util.Collections;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import net.minecraft.block.Block;
/*      */ import net.minecraft.entity.player.EntityPlayer;
/*      */ import net.minecraft.item.ItemStack;
/*      */ import net.minecraft.util.RegistryNamespaced;
/*      */ import net.minecraft.util.ResourceLocation;
/*      */ import org.millenaire.common.building.BuildingPlan;
/*      */ import org.millenaire.common.building.BuildingPlanSet;
/*      */ import org.millenaire.common.core.MillCommonUtilities;
/*      */ import org.millenaire.common.core.MillCommonUtilities.BonusThread;
/*      */ import org.millenaire.common.forge.CommonProxy;
/*      */ import org.millenaire.common.forge.Mill;
/*      */ import org.millenaire.common.goal.Goal;
/*      */ 
/*      */ public class MLN
/*      */ {
/*      */   public static class FileFiler implements FilenameFilter
/*      */   {
/*      */     String end;
/*      */     
/*      */     public FileFiler(String ending)
/*      */     {
/*   47 */       this.end = ending;
/*      */     }
/*      */     
/*      */ 
/*      */     public boolean accept(File file, String name)
/*      */     {
/*   53 */       if (!name.endsWith(this.end)) {
/*   54 */         return false;
/*      */       }
/*      */       
/*   57 */       if (name.startsWith(".")) {
/*   58 */         return false;
/*      */       }
/*      */       
/*   61 */       return true;
/*      */     }
/*      */   }
/*      */   
/*      */   public static class Language {
/*      */     private static final int PARCHMENT = 0;
/*      */     private static final int HELP = 1;
/*      */     public String language;
/*      */     
/*      */     private static class ParchmentFileFilter implements FilenameFilter { private final String filePrefix;
/*      */       
/*   72 */       public ParchmentFileFilter(String filePrefix) { this.filePrefix = filePrefix; }
/*      */       
/*      */ 
/*      */ 
/*      */       public boolean accept(File file, String name)
/*      */       {
/*   78 */         if (!name.startsWith(this.filePrefix)) {
/*   79 */           return false;
/*      */         }
/*      */         
/*   82 */         if (!name.endsWith(".txt")) {
/*   83 */           return false;
/*      */         }
/*      */         
/*   86 */         String id = name.substring(this.filePrefix.length() + 1, name.length() - 4);
/*      */         
/*   88 */         if ((id.length() == 0) || (Integer.parseInt(id) < 1)) {
/*   89 */           return false;
/*      */         }
/*      */         
/*   92 */         return true;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*   99 */     public String topLevelLanguage = null;
/*      */     
/*      */     public boolean serverContent;
/*  102 */     public HashMap<String, String> strings = new HashMap();
/*  103 */     public HashMap<String, String> questStrings = new HashMap();
/*      */     
/*  105 */     public HashMap<Integer, List<List<String>>> texts = new HashMap();
/*  106 */     public HashMap<Integer, String> textsVersion = new HashMap();
/*      */     
/*  108 */     public HashMap<Integer, List<List<String>>> help = new HashMap();
/*  109 */     public HashMap<Integer, String> helpVersion = new HashMap();
/*      */     
/*      */     public Language(String key, boolean serverContent) {
/*  112 */       this.language = key;
/*      */       
/*  114 */       if (this.language.split("_").length > 1) {
/*  115 */         this.topLevelLanguage = this.language.split("_")[0];
/*      */       }
/*      */       
/*  118 */       this.serverContent = serverContent;
/*      */     }
/*      */     
/*      */     public void compareWithLanguage(HashMap<String, Integer> percentages, Language ref)
/*      */     {
/*  123 */       MLN.major(null, "Generating translation gap file between " + this.language + " and " + ref.language);
/*      */       
/*  125 */       File translationGapDir = new File(Mill.proxy.getBaseDir(), "Translation gaps");
/*      */       
/*  127 */       if (!translationGapDir.exists()) {
/*  128 */         translationGapDir.mkdirs();
/*      */       }
/*      */       
/*  131 */       File file = new File(translationGapDir, this.language + "-" + ref.language + ".txt");
/*      */       
/*  133 */       if (file.exists()) {
/*  134 */         file.delete();
/*      */       }
/*      */       
/*      */ 
/*      */       try
/*      */       {
/*  140 */         int translationsMissing = 0;int translationsDone = 0;
/*      */         
/*  142 */         BufferedWriter writer = MillCommonUtilities.getWriter(file);
/*      */         
/*  144 */         writer.write("Translation comparison between " + this.language + " and " + ref.language + MLN.EOL + MLN.EOL);
/*      */         
/*  146 */         List<String> errors = new ArrayList();
/*      */         
/*  148 */         List<String> keys = new ArrayList(ref.strings.keySet());
/*  149 */         Collections.sort(keys);
/*      */         
/*  151 */         for (String key : keys) {
/*  152 */           if (!this.strings.containsKey(key)) {
/*  153 */             errors.add("Key missing in the strings.txt file: " + key);
/*  154 */             translationsMissing++;
/*      */           } else {
/*  156 */             int nbValues = ((String)ref.strings.get(key)).split("<").length - 1;
/*  157 */             int nbValues2 = ((String)this.strings.get(key)).split("<").length - 1;
/*  158 */             if (nbValues != nbValues2) {
/*  159 */               errors.add("Mismatched number of parameters for " + key + ": " + nbValues + " in " + ref.language + " and " + nbValues2 + " in " + this.language);
/*  160 */               translationsMissing++;
/*      */             } else {
/*  162 */               translationsDone++;
/*      */             }
/*      */           }
/*      */         }
/*      */         
/*  167 */         if (errors.size() > 0) {
/*  168 */           writer.write("List of gaps found in strings.txt: " + MLN.EOL + MLN.EOL);
/*      */           
/*  170 */           for (String s : errors) {
/*  171 */             writer.write(s + MLN.EOL);
/*      */           }
/*  173 */           writer.write(MLN.EOL);
/*      */         }
/*      */         
/*  176 */         errors = new ArrayList();
/*  177 */         keys = new ArrayList(ref.questStrings.keySet());
/*  178 */         Collections.sort(keys);
/*      */         
/*  180 */         for (String key : keys) {
/*  181 */           if (!this.questStrings.containsKey(key)) {
/*  182 */             errors.add("Key missing in the quests.txt file: " + key);
/*  183 */             translationsMissing++;
/*      */           } else {
/*  185 */             translationsDone++;
/*      */           }
/*      */         }
/*      */         
/*  189 */         if (errors.size() > 0) {
/*  190 */           writer.write("List of gaps found in quest files: " + MLN.EOL + MLN.EOL);
/*      */           
/*  192 */           for (String s : errors) {
/*  193 */             writer.write(s + MLN.EOL);
/*      */           }
/*  195 */           writer.write(MLN.EOL);
/*      */         }
/*      */         
/*  198 */         errors = new ArrayList();
/*      */         
/*  200 */         for (Goal goal : Goal.goals.values()) {
/*  201 */           if ((!this.strings.containsKey("goal." + goal.labelKey(null))) && (!ref.strings.containsKey("goal." + goal.labelKey(null)))) {
/*  202 */             errors.add("Could not find label for goal." + goal.labelKey(null) + " (class: " + goal.getClass().getSimpleName() + ") in either language.");
/*      */           }
/*      */         }
/*      */         
/*  206 */         if (errors.size() > 0) {
/*  207 */           writer.write("List of goals without labels: " + MLN.EOL + MLN.EOL);
/*      */           
/*  209 */           for (String s : errors) {
/*  210 */             writer.write(s + MLN.EOL);
/*      */           }
/*  212 */           writer.write(MLN.EOL);
/*      */         }
/*      */         
/*  215 */         errors = new ArrayList();
/*      */         
/*  217 */         for (Iterator i$ = ref.texts.keySet().iterator(); i$.hasNext();) { int id = ((Integer)i$.next()).intValue();
/*  218 */           if (!this.texts.containsKey(Integer.valueOf(id))) {
/*  219 */             errors.add("Parchment " + id + " is missing.");
/*  220 */             translationsMissing += 10;
/*      */           }
/*  222 */           else if (!((String)this.textsVersion.get(Integer.valueOf(id))).equals(ref.textsVersion.get(Integer.valueOf(id)))) {
/*  223 */             errors.add("Parchment " + id + " has a different version: it is at version " + (String)this.textsVersion.get(Integer.valueOf(id)) + " while " + ref.language + " parchment is at " + (String)ref.textsVersion.get(Integer.valueOf(id)));
/*      */             
/*  225 */             translationsMissing += 5;
/*      */           } else {
/*  227 */             translationsDone += 10;
/*      */           }
/*      */         }
/*      */         
/*      */ 
/*  232 */         for (Iterator i$ = ref.help.keySet().iterator(); i$.hasNext();) { int id = ((Integer)i$.next()).intValue();
/*  233 */           if (!this.help.containsKey(Integer.valueOf(id))) {
/*  234 */             errors.add("Help " + id + " is missing.");
/*  235 */             translationsMissing += 10;
/*      */           }
/*  237 */           else if (!((String)this.helpVersion.get(Integer.valueOf(id))).equals(ref.helpVersion.get(Integer.valueOf(id)))) {
/*  238 */             errors.add("Help " + id + " has a different version: it is at version " + (String)this.helpVersion.get(Integer.valueOf(id)) + " while " + ref.language + " parchment is at " + (String)ref.helpVersion.get(Integer.valueOf(id)));
/*  239 */             translationsMissing += 5;
/*      */           } else {
/*  241 */             translationsDone += 10;
/*      */           }
/*      */         }
/*      */         
/*      */ 
/*  246 */         if (errors.size() > 0) {
/*  247 */           writer.write("List of gaps found between parchments: " + MLN.EOL + MLN.EOL);
/*      */           
/*  249 */           for (String s : errors) {
/*  250 */             writer.write(s + MLN.EOL);
/*      */           }
/*  252 */           writer.write(MLN.EOL);
/*      */         }
/*      */         
/*  255 */         for (Culture c : Culture.ListCultures) {
/*  256 */           int[] res = c.compareCultureLanguages(this.language, ref.language, writer);
/*  257 */           translationsDone += res[0];
/*  258 */           translationsMissing += res[1];
/*      */         }
/*      */         
/*      */         int percentDone;
/*      */         int percentDone;
/*  263 */         if (translationsDone + translationsMissing > 0) {
/*  264 */           percentDone = translationsDone * 100 / (translationsDone + translationsMissing);
/*      */         } else {
/*  266 */           percentDone = 0;
/*      */         }
/*      */         
/*  269 */         percentages.put(this.language, Integer.valueOf(percentDone));
/*      */         
/*  271 */         writer.write("Traduction completness: " + percentDone + "%" + MLN.EOL);
/*      */         
/*  273 */         writer.flush();
/*  274 */         writer.close();
/*      */       } catch (Exception e) {
/*  276 */         MLN.printException(e);
/*      */       }
/*      */     }
/*      */     
/*      */     public void loadFromDisk(List<File> languageDirs) {
/*  281 */       for (File languageDir : languageDirs)
/*      */       {
/*  283 */         File effectiveLanguageDir = new File(languageDir, this.language);
/*      */         
/*  285 */         if (!effectiveLanguageDir.exists()) {
/*  286 */           effectiveLanguageDir = new File(languageDir, this.language.split("_")[0]);
/*      */         }
/*      */         
/*  289 */         File stringFile = new File(effectiveLanguageDir, "strings.txt");
/*  290 */         if (stringFile.exists()) {
/*  291 */           loadStrings(this.strings, stringFile);
/*      */         }
/*      */         
/*  294 */         if (effectiveLanguageDir.exists()) {
/*  295 */           for (File file : effectiveLanguageDir.listFiles(new org.millenaire.common.core.MillCommonUtilities.PrefixExtFileFilter("quests", "txt"))) {
/*  296 */             loadStrings(this.questStrings, file);
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*  301 */       for (Quest q : Quest.quests.values()) {
/*  302 */         for (Quest.QuestStep step : q.steps) {
/*  303 */           if (step.labels.containsKey(this.language)) {
/*  304 */             this.questStrings.put(step.getStringKey() + "label", step.labels.get(this.language));
/*  305 */           } else if ((this.topLevelLanguage != null) && (step.labels.containsKey(this.topLevelLanguage))) {
/*  306 */             this.questStrings.put(step.getStringKey() + "label", step.labels.get(this.topLevelLanguage));
/*      */           }
/*      */           
/*  309 */           if (step.descriptions.containsKey(this.language)) {
/*  310 */             this.questStrings.put(step.getStringKey() + "description", step.descriptions.get(this.language));
/*  311 */           } else if ((this.topLevelLanguage != null) && (step.descriptions.containsKey(this.topLevelLanguage))) {
/*  312 */             this.questStrings.put(step.getStringKey() + "description", step.descriptions.get(this.topLevelLanguage));
/*      */           }
/*      */           
/*  315 */           if (step.descriptionsSuccess.containsKey(this.language)) {
/*  316 */             this.questStrings.put(step.getStringKey() + "description_success", step.descriptionsSuccess.get(this.language));
/*  317 */           } else if ((this.topLevelLanguage != null) && (step.descriptionsSuccess.containsKey(this.topLevelLanguage))) {
/*  318 */             this.questStrings.put(step.getStringKey() + "description_success", step.descriptionsSuccess.get(this.topLevelLanguage));
/*      */           }
/*      */           
/*  321 */           if (step.descriptionsRefuse.containsKey(this.language)) {
/*  322 */             this.questStrings.put(step.getStringKey() + "description_refuse", step.descriptionsRefuse.get(this.language));
/*  323 */           } else if ((this.topLevelLanguage != null) && (step.descriptionsRefuse.containsKey(this.topLevelLanguage))) {
/*  324 */             this.questStrings.put(step.getStringKey() + "description_refuse", step.descriptionsRefuse.get(this.topLevelLanguage));
/*      */           }
/*      */           
/*  327 */           if (step.descriptionsTimeUp.containsKey(this.language)) {
/*  328 */             this.questStrings.put(step.getStringKey() + "description_timeup", step.descriptionsTimeUp.get(this.language));
/*  329 */           } else if ((this.topLevelLanguage != null) && (step.descriptionsTimeUp.containsKey(this.topLevelLanguage))) {
/*  330 */             this.questStrings.put(step.getStringKey() + "description_timeup", step.descriptionsTimeUp.get(this.topLevelLanguage));
/*      */           }
/*      */           
/*  333 */           if (step.listings.containsKey(this.language)) {
/*  334 */             this.questStrings.put(step.getStringKey() + "listing", step.listings.get(this.language));
/*  335 */           } else if ((this.topLevelLanguage != null) && (step.listings.containsKey(this.topLevelLanguage))) {
/*  336 */             this.questStrings.put(step.getStringKey() + "listing", step.listings.get(this.topLevelLanguage));
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*  341 */       loadTextFiles(languageDirs, 0);
/*  342 */       loadTextFiles(languageDirs, 1);
/*      */       
/*  344 */       if (!MLN.loadedLanguages.containsKey(this.language)) {
/*  345 */         MLN.loadedLanguages.put(this.language, this);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */     private void loadStrings(HashMap<String, String> strings, File file)
/*      */     {
/*      */       try
/*      */       {
/*  354 */         BufferedReader reader = MillCommonUtilities.getReader(file);
/*      */         
/*      */         String line;
/*      */         
/*  358 */         while ((line = reader.readLine()) != null) {
/*  359 */           line = line.trim();
/*  360 */           if ((line.length() > 0) && (!line.startsWith("//"))) {
/*  361 */             String[] temp = line.split("=");
/*  362 */             if (temp.length == 2)
/*      */             {
/*  364 */               String key = temp[0].trim().toLowerCase();
/*  365 */               String value = temp[1].trim();
/*      */               
/*  367 */               if (strings.containsKey(key)) {
/*  368 */                 MLN.error(null, "Key " + key + " is present more than once in " + file.getAbsolutePath());
/*      */               } else {
/*  370 */                 strings.put(key, value);
/*      */               }
/*  372 */             } else if (line.endsWith("=")) {
/*  373 */               String key = temp[0].toLowerCase();
/*      */               
/*  375 */               if (strings.containsKey(key)) {
/*  376 */                 MLN.error(null, "Key " + key + " is present more than once in " + file.getAbsolutePath());
/*      */               } else {
/*  378 */                 strings.put(key, "");
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*  383 */         reader.close();
/*      */       } catch (Exception e) {
/*  385 */         MLN.printException(e);
/*  386 */         return;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */     public void loadTextFiles(List<File> languageDirs, int type)
/*      */     {
/*      */       String dirName;
/*      */       
/*      */       String dirName;
/*  396 */       if (type == 0) {
/*  397 */         dirName = "parchments";
/*      */       } else
/*  399 */         dirName = "help";
/*      */       String filePrefix;
/*      */       String filePrefix;
/*  402 */       if (type == 0) {
/*  403 */         filePrefix = "parchment";
/*      */       } else {
/*  405 */         filePrefix = "help";
/*      */       }
/*      */       
/*  408 */       for (File languageDir : languageDirs)
/*      */       {
/*  410 */         File parchmentsDir = new File(new File(languageDir, this.language), dirName);
/*      */         
/*  412 */         if (!parchmentsDir.exists()) {
/*  413 */           parchmentsDir = new File(new File(languageDir, this.language.split("_")[0]), dirName);
/*      */         }
/*      */         
/*  416 */         if (!parchmentsDir.exists()) {
/*  417 */           return;
/*      */         }
/*      */         
/*  420 */         ParchmentFileFilter filter = new ParchmentFileFilter(filePrefix);
/*      */         
/*  422 */         for (File file : parchmentsDir.listFiles(filter))
/*      */         {
/*  424 */           String sId = file.getName().substring(filePrefix.length() + 1, file.getName().length() - 4);
/*      */           
/*  426 */           int id = 0;
/*      */           
/*  428 */           if (sId.length() > 0) {
/*      */             try {
/*  430 */               id = Integer.parseInt(sId);
/*      */             } catch (Exception e) {
/*  432 */               MLN.printException("Error when trying to read pachment id: ", e);
/*      */             }
/*      */           } else {
/*  435 */             MLN.error(null, "Couldn't read the ID of " + file.getAbsolutePath() + ". sId: " + sId);
/*      */           }
/*      */           
/*  438 */           if (MLN.LogBuildingPlan >= 1) {
/*  439 */             MLN.minor(file, "Loading " + dirName + ": " + file.getAbsolutePath());
/*      */           }
/*      */           
/*  442 */           List<List<String>> text = new ArrayList();
/*      */           
/*  444 */           String version = "unknown";
/*      */           try
/*      */           {
/*  447 */             BufferedReader reader = MillCommonUtilities.getReader(file);
/*      */             
/*      */ 
/*      */ 
/*  451 */             List<String> page = new ArrayList();
/*      */             String line;
/*  453 */             while ((line = reader.readLine()) != null)
/*      */             {
/*  455 */               if (line.equals("NEW_PAGE")) {
/*  456 */                 text.add(page);
/*  457 */                 page = new ArrayList();
/*  458 */               } else if (line.startsWith("version:")) {
/*  459 */                 version = line.split(":")[1];
/*      */               } else {
/*  461 */                 page.add(line);
/*      */               }
/*      */             }
/*  464 */             text.add(page);
/*      */             
/*  466 */             if (type == 0) {
/*  467 */               this.texts.put(Integer.valueOf(id), text);
/*  468 */               this.textsVersion.put(Integer.valueOf(id), version);
/*      */             } else {
/*  470 */               this.help.put(Integer.valueOf(id), text);
/*  471 */               this.helpVersion.put(Integer.valueOf(id), version);
/*      */             }
/*      */           }
/*      */           catch (Exception e) {
/*  475 */             MLN.printException(e);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */     public String toString()
/*      */     {
/*  483 */       return this.language;
/*      */     }
/*      */   }
/*      */   
/*      */   public static class MillenaireException extends Exception
/*      */   {
/*      */     private static final long serialVersionUID = 1L;
/*      */     
/*      */     public MillenaireException(String string) {
/*  492 */       super();
/*      */     }
/*      */   }
/*      */   
/*  496 */   public static boolean logPerformed = false;
/*      */   
/*  498 */   private static final Map<Integer, Integer> exceptionCount = new HashMap();
/*      */   
/*      */   public static final char BLACK = '0';
/*      */   
/*      */   public static final char DARKBLUE = '1';
/*      */   
/*      */   public static final char DARKGREEN = '2';
/*      */   
/*      */   public static final char LIGHTBLUE = '3';
/*      */   public static final char DARKRED = '4';
/*      */   public static final char PURPLE = '5';
/*      */   public static final char ORANGE = '6';
/*      */   public static final char LIGHTGREY = '7';
/*      */   public static final char DARKGREY = '8';
/*      */   public static final char BLUE = '9';
/*      */   public static final char LIGHTGREEN = 'a';
/*      */   public static final char CYAN = 'b';
/*      */   public static final char LIGHTRED = 'c';
/*      */   public static final char PINK = 'd';
/*      */   public static final char YELLOW = 'e';
/*      */   public static final char WHITE = 'f';
/*  519 */   public static int KeepActiveRadius = 200;
/*  520 */   public static int BackgroundRadius = 2000;
/*  521 */   public static int BanditRaidRadius = 1500;
/*      */   
/*  523 */   public static int LogBuildingPlan = 0;
/*  524 */   public static int LogCattleFarmer = 0;
/*  525 */   public static int LogChildren = 0;
/*  526 */   public static int LogTranslation = 0;
/*  527 */   public static int LogConnections = 0;
/*  528 */   public static int LogCulture = 0;
/*  529 */   public static int LogDiplomacy = 0;
/*  530 */   public static int LogFarmerAI = 0;
/*  531 */   public static int LogGeneralAI = 0;
/*  532 */   public static int LogGetPath = 0;
/*  533 */   public static int LogHybernation = 0;
/*  534 */   public static int LogLumberman = 0;
/*  535 */   public static int LogMerchant = 0;
/*  536 */   public static int LogMiner = 0;
/*  537 */   public static int LogOther = 0;
/*  538 */   public static int LogPathing = 0;
/*  539 */   public static int LogPerformance = 0;
/*  540 */   public static int LogSelling = 0;
/*  541 */   public static int LogTileEntityBuilding = 0;
/*  542 */   public static int LogVillage = 0;
/*  543 */   public static int LogVillager = 0;
/*  544 */   public static int LogQuest = 0;
/*  545 */   public static int LogWifeAI = 0;
/*  546 */   public static int LogWorldGeneration = 0;
/*  547 */   public static int LogWorldInfo = 0;
/*  548 */   public static int LogPujas = 0;
/*  549 */   public static int LogVillagerSpawn = 0;
/*  550 */   public static int LogVillagePaths = 0;
/*      */   
/*  552 */   public static String questBiomeForest = "forest";
/*  553 */   public static String questBiomeDesert = "desert";
/*  554 */   public static String questBiomeMountain = "mountain";
/*      */   
/*  556 */   public static int LogNetwork = 0;
/*      */   
/*      */   public static final int MAJOR = 1;
/*      */   public static final int MINOR = 2;
/*      */   public static final int DEBUG = 3;
/*  561 */   private static boolean console = true;
/*      */   
/*      */   public static final String DATE_FORMAT_NOW = "dd-MM-yyyy HH:mm:ss";
/*  564 */   public static boolean DEV = false;
/*  565 */   public static boolean displayNames = true;
/*  566 */   public static boolean displayStart = true;
/*  567 */   public static final String EOL = System.getProperty("line.separator");
/*  568 */   public static List<Block> forbiddenBlocks = new ArrayList();
/*  569 */   public static boolean generateBuildingRes = false;
/*  570 */   public static boolean generateColourSheet = false;
/*  571 */   public static boolean generateVillages = true;
/*  572 */   public static boolean generateVillagesDefault = true;
/*  573 */   public static boolean generateLoneBuildings = true;
/*  574 */   public static boolean generateTranslationGap = false;
/*  575 */   public static boolean generateGoodsList = false;
/*  576 */   public static boolean infiniteAmulet = false;
/*  577 */   public static boolean languageLearning = true;
/*  578 */   public static boolean stopDefaultVillages = false;
/*  579 */   public static boolean seIndicators = false;
/*  580 */   public static boolean loadAllLanguages = true;
/*      */   
/*  582 */   public static boolean jpsPathing = true;
/*      */   
/*  584 */   public static String main_language = "";
/*  585 */   public static String effective_language = "";
/*  586 */   public static String fallback_language = "en";
/*      */   
/*  588 */   private static boolean logfile = true;
/*  589 */   public static int maxChildrenNumber = 10;
/*  590 */   public static int minDistanceBetweenBuildings = 5;
/*  591 */   public static int minDistanceBetweenVillages = 500;
/*  592 */   public static int minDistanceBetweenVillagesAndLoneBuildings = 250;
/*  593 */   public static int minDistanceBetweenLoneBuildings = 500;
/*  594 */   public static int forcePreload = 0;
/*  595 */   public static int spawnProtectionRadius = 250;
/*  596 */   public static int VillageRadius = 60;
/*  597 */   public static int VillagersNamesDistance = 6;
/*  598 */   public static boolean BuildVillagePaths = true;
/*  599 */   public static int VillagersSentenceInChatDistanceClient = 0;
/*  600 */   public static int VillagersSentenceInChatDistanceSP = 6;
/*      */   
/*  602 */   public static int RaidingRate = 20;
/*      */   
/*      */   public static int keyVillageList;
/*      */   
/*      */   public static int keyInfoPanelList;
/*      */   public static int keyAggressiveEscorts;
/*      */   private static FileWriter writer;
/*  609 */   private static String loadedLanguage = null;
/*  610 */   public static int textureSize = -1;
/*  611 */   public static boolean dynamictextures = true;
/*      */   
/*  613 */   public static String customTexture = null;
/*      */   
/*  615 */   public static Language mainLanguage = null;
/*  616 */   public static Language fallbackLanguage = null;
/*  617 */   public static Language serverMainLanguage = null;
/*  618 */   public static Language serverFallbackLanguage = null;
/*  619 */   public static HashMap<String, Language> loadedLanguages = new HashMap();
/*      */   
/*  621 */   public static String bonusCode = null;
/*  622 */   public static boolean bonusEnabled = false;
/*      */   
/*  624 */   public static HashMap<String, MillConfig> configs = new HashMap();
/*      */   
/*  626 */   public static List<String> configPageTitles = new ArrayList();
/*  627 */   public static List<String> configPageDesc = new ArrayList();
/*  628 */   public static List<List<MillConfig>> configPages = new ArrayList();
/*      */   
/*  630 */   public static final ResourceLocation textureLargeChest64 = new ResourceLocation("millenaire", "textures/entity/chest/ML_lockedlargechest_64.png");
/*      */   
/*  632 */   public static final ResourceLocation textureLargeChest = new ResourceLocation("millenaire", "textures/entity/chest/ML_lockedlargechest.png");
/*      */   
/*  634 */   public static final ResourceLocation textureChest64 = new ResourceLocation("millenaire", "textures/entity/chest/ML_lockedchest_64.png");
/*      */   
/*  636 */   public static final ResourceLocation textureChest = new ResourceLocation("millenaire", "textures/entity/chest/ML_lockedchest.png");
/*      */   
/*      */   private static void applyLanguage()
/*      */   {
/*  640 */     nameItems();
/*      */     
/*  642 */     LanguageRegistry.instance().addStringLocalization("entity.ml_GenericVillager.name", string("other.malevillager"));
/*  643 */     LanguageRegistry.instance().addStringLocalization("entity.ml_GenericAsimmFemale.name", string("other.femalevillager"));
/*  644 */     LanguageRegistry.instance().addStringLocalization("entity.ml_GenericSimmFemale.name", string("other.femalevillager"));
/*      */     
/*  646 */     if (!Mill.proxy.isTrueServer())
/*      */     {
/*      */       try
/*      */       {
/*  650 */         InvItem iv = new InvItem(Mill.summoningWand, 1);
/*  651 */         major(null, "Language loaded: " + effective_language + ". Wand name: " + string("item.villagewand") + " Wand invitem name: " + iv.getName());
/*      */       } catch (MillenaireException e) {
/*  653 */         printException(e);
/*      */       }
/*      */       
/*  656 */       if (generateBuildingRes)
/*      */       {
/*  658 */         major(null, "Generating building res file.");
/*  659 */         BuildingPlan.generateBuildingRes();
/*      */         try {
/*  661 */           BuildingPlan.generateWikiTable();
/*      */         } catch (MillenaireException e) {
/*  663 */           printException(e);
/*      */         }
/*  665 */         major(null, "Generated building res file.");
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public static String calculateLoginMD5(String login) {
/*  671 */     return md5(login + login.substring(1)).substring(0, 4);
/*      */   }
/*      */   
/*      */   public static void checkBonusCode(boolean manual)
/*      */   {
/*  676 */     if (Mill.proxy.getSinglePlayerName() == null) {
/*  677 */       bonusEnabled = false;
/*  678 */       return;
/*      */     }
/*      */     
/*  681 */     String login = Mill.proxy.getSinglePlayerName();
/*      */     
/*  683 */     if (bonusCode != null) {
/*  684 */       String calculatedCode = calculateLoginMD5(login);
/*      */       
/*  686 */       bonusEnabled = calculatedCode.equals(bonusCode);
/*      */     }
/*      */     
/*  689 */     if ((!bonusEnabled) && (!manual)) {
/*  690 */       new MillCommonUtilities.BonusThread(login).start();
/*      */     }
/*      */     
/*  693 */     if ((manual) && (bonusCode != null) && (bonusCode.length() == 4)) {
/*  694 */       if (bonusEnabled) {
/*  695 */         Mill.proxy.sendLocalChat(Mill.proxy.getTheSinglePlayer(), '2', string("config.validbonuscode"));
/*      */       } else {
/*  697 */         Mill.proxy.sendLocalChat(Mill.proxy.getTheSinglePlayer(), '4', string("config.invalidbonuscode"));
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public static void debug(Object obj, String s)
/*      */   {
/*  704 */     writeText("DEBUG: " + obj + ": " + s);
/*      */   }
/*      */   
/*      */   public static void error(Object obj, String s) {
/*  708 */     if (DEV) {
/*  709 */       writeText("    !====================================!");
/*      */     }
/*  711 */     writeText("ERROR: " + obj + ": " + s);
/*  712 */     if (DEV) {
/*  713 */       writeText("     ==================================== ");
/*      */     }
/*      */   }
/*      */   
/*      */   private static String fillInName(String s)
/*      */   {
/*  719 */     if (s == null) {
/*  720 */       return "";
/*      */     }
/*      */     
/*  723 */     EntityPlayer player = Mill.proxy.getTheSinglePlayer();
/*      */     
/*  725 */     if (player != null) {
/*  726 */       return s.replaceAll("\\$name", player.getDisplayName());
/*      */     }
/*  728 */     return s;
/*      */   }
/*      */   
/*      */   public static List<List<String>> getHelp(int id)
/*      */   {
/*  733 */     if (mainLanguage.help.containsKey(Integer.valueOf(id))) {
/*  734 */       return (List)mainLanguage.help.get(Integer.valueOf(id));
/*      */     }
/*  736 */     if (fallbackLanguage.help.containsKey(Integer.valueOf(id))) {
/*  737 */       return (List)fallbackLanguage.help.get(Integer.valueOf(id));
/*      */     }
/*  739 */     return null;
/*      */   }
/*      */   
/*      */   public static List<String> getHoFData()
/*      */   {
/*  744 */     List<String> hofData = new ArrayList();
/*      */     try
/*      */     {
/*  747 */       BufferedReader reader = MillCommonUtilities.getReader(new File(Mill.proxy.getBaseDir(), "hof.txt"));
/*      */       
/*      */       String line;
/*      */       
/*  751 */       while ((line = reader.readLine()) != null) {
/*  752 */         line = line.trim();
/*  753 */         if ((line.length() > 0) && (!line.startsWith("//"))) {
/*  754 */           hofData.add(line);
/*      */         }
/*      */       }
/*      */     }
/*      */     catch (Exception e) {
/*  759 */       printException("Error when loading HoF: ", e);
/*      */     }
/*      */     
/*  762 */     return hofData;
/*      */   }
/*      */   
/*      */   public static List<File> getLanguageDirs() {
/*  766 */     List<File> languageDirs = new ArrayList();
/*      */     
/*  768 */     for (File dir : Mill.loadingDirs) {
/*  769 */       File languageDir = new File(dir, "languages");
/*      */       
/*  771 */       if (languageDir.exists()) {
/*  772 */         languageDirs.add(languageDir);
/*      */       }
/*      */     }
/*      */     
/*  776 */     return languageDirs;
/*      */   }
/*      */   
/*      */   public static ResourceLocation getLargeLockedChestTexture()
/*      */   {
/*  781 */     if ((dynamictextures) && (textureSize >= 64)) {
/*  782 */       return textureLargeChest64;
/*      */     }
/*      */     
/*  785 */     return textureLargeChest;
/*      */   }
/*      */   
/*      */   public static ResourceLocation getLockedChestTexture()
/*      */   {
/*  790 */     if ((dynamictextures) && (textureSize >= 64)) {
/*  791 */       return textureChest64;
/*      */     }
/*      */     
/*  794 */     return textureChest;
/*      */   }
/*      */   
/*      */   public static String getLogLevel(int level) {
/*  798 */     if (level == 1) {
/*  799 */       return "major";
/*      */     }
/*  801 */     if (level == 2) {
/*  802 */       return "minor";
/*      */     }
/*  804 */     if (level == 3) {
/*  805 */       return "debug";
/*      */     }
/*  807 */     return "";
/*      */   }
/*      */   
/*      */   public static List<List<String>> getParchment(int id) {
/*  811 */     if (mainLanguage.texts.containsKey(Integer.valueOf(id))) {
/*  812 */       return (List)mainLanguage.texts.get(Integer.valueOf(id));
/*      */     }
/*  814 */     if (fallbackLanguage.texts.containsKey(Integer.valueOf(id))) {
/*  815 */       return (List)fallbackLanguage.texts.get(Integer.valueOf(id));
/*      */     }
/*  817 */     return null;
/*      */   }
/*      */   
/*      */   public static String getRawString(String key, boolean mustFind) {
/*  821 */     return getRawString(key, mustFind, true, true);
/*      */   }
/*      */   
/*      */   public static String getRawString(String key, boolean mustFind, boolean main, boolean fallback)
/*      */   {
/*  826 */     if ((main) && (mainLanguage != null) && (mainLanguage.strings.containsKey(key))) {
/*  827 */       return (String)mainLanguage.strings.get(key);
/*      */     }
/*      */     
/*  830 */     if ((main) && (serverMainLanguage != null) && (serverMainLanguage.strings.containsKey(key))) {
/*  831 */       return (String)serverMainLanguage.strings.get(key);
/*      */     }
/*      */     
/*  834 */     if ((fallback) && (fallbackLanguage != null) && (fallbackLanguage.strings.containsKey(key))) {
/*  835 */       return (String)fallbackLanguage.strings.get(key);
/*      */     }
/*      */     
/*  838 */     if ((fallback) && (serverFallbackLanguage != null) && (serverFallbackLanguage.strings.containsKey(key))) {
/*  839 */       return (String)serverFallbackLanguage.strings.get(key);
/*      */     }
/*      */     
/*  842 */     if ((mustFind) && (LogTranslation >= 1)) {
/*  843 */       error(null, "String not found: " + key);
/*      */     }
/*      */     
/*  846 */     if (mustFind) {
/*  847 */       return key;
/*      */     }
/*  849 */     return null;
/*      */   }
/*      */   
/*      */   public static String getRawStringFallbackOnly(String key, boolean mustFind)
/*      */   {
/*  854 */     return getRawString(key, mustFind, false, true);
/*      */   }
/*      */   
/*      */   public static String getRawStringMainOnly(String key, boolean mustFind) {
/*  858 */     return getRawString(key, mustFind, true, false);
/*      */   }
/*      */   
/*      */   public static String getTextSuffix()
/*      */   {
/*  863 */     if (textureSize == -1) {
/*  864 */       Mill.proxy.testTextureSize();
/*      */     }
/*      */     
/*  867 */     if (customTexture != null) {
/*  868 */       return "";
/*      */     }
/*      */     
/*  871 */     if ((dynamictextures) && (textureSize >= 64)) {
/*  872 */       return "_64";
/*      */     }
/*      */     
/*  875 */     return "";
/*      */   }
/*      */   
/*      */   private static void initConfigItems()
/*      */   {
/*      */     try {
/*  881 */       List<MillConfig> configPage = new ArrayList();
/*  882 */       configPage.add(new MillConfig(MLN.class.getField("keyVillageList"), "village_list_key", 3).setMaxStringLength(1));
/*  883 */       configPage.add(new MillConfig(MLN.class.getField("keyInfoPanelList"), "quest_list_key", 3).setMaxStringLength(1));
/*  884 */       configPage.add(new MillConfig(MLN.class.getField("keyAggressiveEscorts"), "escort_key", 3).setMaxStringLength(1));
/*      */       
/*  886 */       configPage.add(new MillConfig(MLN.class.getField("fallback_language"), "fallback_language", new Object[] { "en", "fr" }));
/*  887 */       configPage.add(new MillConfig(MLN.class.getField("dynamictextures"), "dynamic_textures", new Object[0]));
/*  888 */       configPage.add(new MillConfig(MLN.class.getField("languageLearning"), "language_learning", new Object[0]));
/*  889 */       configPage.add(new MillConfig(MLN.class.getField("loadAllLanguages"), "load_all_languages", new Object[0]));
/*  890 */       configPage.add(new MillConfig(MLN.class.getField("displayStart"), "display_start", new Object[0]));
/*  891 */       configPage.add(new MillConfig(MLN.class.getField("displayNames"), "display_names", new Object[0]));
/*  892 */       configPage.add(new MillConfig(MLN.class.getField("VillagersNamesDistance"), "villagers_names_distance", new Object[] { Integer.valueOf(5), Integer.valueOf(10), Integer.valueOf(20), Integer.valueOf(30), Integer.valueOf(50) }));
/*  893 */       configPage.add(new MillConfig(MLN.class.getField("VillagersSentenceInChatDistanceSP"), "villagers_sentence_in_chat_distance_sp", new Object[] { Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4), Integer.valueOf(6), Integer.valueOf(10) }));
/*  894 */       configPage.add(new MillConfig(MLN.class.getField("VillagersSentenceInChatDistanceClient"), "villagers_sentence_in_chat_distance_client", new Object[] { Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4), Integer.valueOf(6), Integer.valueOf(10) }));
/*      */       
/*  896 */       configPages.add(configPage);
/*  897 */       configPageTitles.add("config.page.uisettings");
/*  898 */       configPageDesc.add(null);
/*      */       
/*  900 */       configPage = new ArrayList();
/*  901 */       configPage.add(new MillConfig(MLN.class.getField("generateVillagesDefault"), "generate_villages", new Object[0]));
/*  902 */       configPage.add(new MillConfig(MLN.class.getField("generateLoneBuildings"), "generate_lone_buildings", new Object[0]));
/*  903 */       configPage.add(new MillConfig(MLN.class.getField("minDistanceBetweenVillages"), "min_village_distance", new Object[] { Integer.valueOf(300), Integer.valueOf(450), Integer.valueOf(600), Integer.valueOf(800), Integer.valueOf(1000) }));
/*  904 */       configPage.add(new MillConfig(MLN.class.getField("minDistanceBetweenVillagesAndLoneBuildings"), "min_village_lonebuilding_distance", new Object[] { Integer.valueOf(100), Integer.valueOf(200), Integer.valueOf(300), Integer.valueOf(500), Integer.valueOf(800) }));
/*  905 */       configPage.add(new MillConfig(MLN.class.getField("minDistanceBetweenLoneBuildings"), "min_lonebuilding_distance", new Object[] { Integer.valueOf(300), Integer.valueOf(450), Integer.valueOf(600), Integer.valueOf(800), Integer.valueOf(1000) }));
/*  906 */       configPage.add(new MillConfig(MLN.class.getField("spawnProtectionRadius"), "spawn_protection_radius", new Object[] { Integer.valueOf(0), Integer.valueOf(50), Integer.valueOf(100), Integer.valueOf(150), Integer.valueOf(250), Integer.valueOf(500) }));
/*      */       
/*  908 */       configPages.add(configPage);
/*  909 */       configPageTitles.add("config.page.worldgeneration");
/*  910 */       configPageDesc.add("config.page.worldgeneration.desc");
/*      */       
/*  912 */       configPage = new ArrayList();
/*      */       
/*  914 */       configPage.add(new MillConfig(MLN.class.getField("KeepActiveRadius"), "keep_active_radius", new Object[] { Integer.valueOf(0), Integer.valueOf(100), Integer.valueOf(150), Integer.valueOf(200), Integer.valueOf(250), Integer.valueOf(300), Integer.valueOf(400), Integer.valueOf(500) }));
/*  915 */       configPage.add(new MillConfig(MLN.class.getField("VillageRadius"), "village_radius", new Object[] { Integer.valueOf(40), Integer.valueOf(50), Integer.valueOf(60), Integer.valueOf(70), Integer.valueOf(80) }));
/*  916 */       configPage.add(new MillConfig(MLN.class.getField("minDistanceBetweenBuildings"), "min_distance_between_buildings", new Object[] { Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4) }));
/*  917 */       configPage.add(new MillConfig(MLN.class.getField("BuildVillagePaths"), "village_paths", new Object[0]));
/*  918 */       configPage.add(new MillConfig(MLN.class.getField("maxChildrenNumber"), "max_children_number", new Object[] { Integer.valueOf(2), Integer.valueOf(5), Integer.valueOf(10), Integer.valueOf(15), Integer.valueOf(20) }));
/*  919 */       configPage.add(new MillConfig(MLN.class.getField("BackgroundRadius"), "background_radius", new Object[] { Integer.valueOf(0), Integer.valueOf(200), Integer.valueOf(500), Integer.valueOf(1000), Integer.valueOf(1500), Integer.valueOf(2000), Integer.valueOf(2500), Integer.valueOf(3000) }));
/*  920 */       configPage.add(new MillConfig(MLN.class.getField("BanditRaidRadius"), "bandit_raid_radius", new Object[] { Integer.valueOf(0), Integer.valueOf(200), Integer.valueOf(500), Integer.valueOf(1000), Integer.valueOf(1500), Integer.valueOf(2000) }));
/*  921 */       configPage.add(new MillConfig(MLN.class.getField("RaidingRate"), "raiding_rate", new Object[] { Integer.valueOf(0), Integer.valueOf(10), Integer.valueOf(20), Integer.valueOf(50), Integer.valueOf(100) }));
/*      */       
/*  923 */       configPages.add(configPage);
/*  924 */       configPageTitles.add("config.page.villagebehaviour");
/*  925 */       configPageDesc.add("config.page.villagebehaviour.desc");
/*      */       
/*  927 */       configPage = new ArrayList();
/*      */       
/*  929 */       configPage.add(new MillConfig(MLN.class.getField("generateTranslationGap"), "generate_translation_gap", new Object[0]));
/*  930 */       configPage.add(new MillConfig(MLN.class.getField("generateColourSheet"), "generate_colour_chart", new Object[0]));
/*  931 */       configPage.add(new MillConfig(MLN.class.getField("generateBuildingRes"), "generate_building_res", new Object[0]));
/*  932 */       configPage.add(new MillConfig(MLN.class.getField("generateGoodsList"), "generate_goods_list", new Object[0]));
/*      */       
/*  934 */       configPage.add(new MillConfig(MLN.class.getField("LogTileEntityBuilding"), "LogTileEntityBuilding", 5).setDisplayDev(true));
/*  935 */       configPage.add(new MillConfig(MLN.class.getField("LogWorldGeneration"), "LogWorldGeneration", 5).setDisplayDev(true));
/*  936 */       configPage.add(new MillConfig(MLN.class.getField("LogFarmerAI"), "LogFarmerAI", 5).setDisplayDev(true));
/*  937 */       configPage.add(new MillConfig(MLN.class.getField("LogDiplomacy"), "LogDiplomacy", 5).setDisplayDev(true));
/*  938 */       configPage.add(new MillConfig(MLN.class.getField("LogWifeAI"), "LogWifeAI", 5).setDisplayDev(true));
/*  939 */       configPage.add(new MillConfig(MLN.class.getField("LogVillager"), "LogVillager", 5).setDisplayDev(true));
/*  940 */       configPage.add(new MillConfig(MLN.class.getField("LogQuest"), "LogQuest", 5).setDisplayDev(true));
/*  941 */       configPage.add(new MillConfig(MLN.class.getField("LogPathing"), "LogPathing", 5).setDisplayDev(true));
/*  942 */       configPage.add(new MillConfig(MLN.class.getField("LogConnections"), "LogConnections", 5).setDisplayDev(true));
/*  943 */       configPage.add(new MillConfig(MLN.class.getField("LogGetPath"), "LogGetPath", 5).setDisplayDev(true));
/*  944 */       configPage.add(new MillConfig(MLN.class.getField("LogLumberman"), "LogLumberman", 5).setDisplayDev(true));
/*  945 */       configPage.add(new MillConfig(MLN.class.getField("LogBuildingPlan"), "LogBuildingPlan", 5).setDisplayDev(true));
/*  946 */       configPage.add(new MillConfig(MLN.class.getField("LogGeneralAI"), "LogGeneralAI", 5).setDisplayDev(true));
/*  947 */       configPage.add(new MillConfig(MLN.class.getField("LogSelling"), "LogSelling", 5).setDisplayDev(true));
/*  948 */       configPage.add(new MillConfig(MLN.class.getField("LogHybernation"), "LogHybernation", 5).setDisplayDev(true));
/*  949 */       configPage.add(new MillConfig(MLN.class.getField("LogOther"), "LogOther", 5).setDisplayDev(true));
/*  950 */       configPage.add(new MillConfig(MLN.class.getField("LogChildren"), "LogChildren", 5).setDisplayDev(true));
/*  951 */       configPage.add(new MillConfig(MLN.class.getField("LogPerformance"), "LogPerformance", 5).setDisplayDev(true));
/*  952 */       configPage.add(new MillConfig(MLN.class.getField("LogCattleFarmer"), "LogCattleFarmer", 5).setDisplayDev(true));
/*  953 */       configPage.add(new MillConfig(MLN.class.getField("LogMiner"), "LogMiner", 5).setDisplayDev(true));
/*  954 */       configPage.add(new MillConfig(MLN.class.getField("LogVillage"), "LogVillage", 5).setDisplayDev(true));
/*  955 */       configPage.add(new MillConfig(MLN.class.getField("LogWorldInfo"), "LogWorldInfo", 5).setDisplayDev(true));
/*  956 */       configPage.add(new MillConfig(MLN.class.getField("LogPujas"), "LogPujas", 5).setDisplayDev(true));
/*  957 */       configPage.add(new MillConfig(MLN.class.getField("LogVillagerSpawn"), "LogVillagerSpawn", 5).setDisplayDev(true));
/*  958 */       configPage.add(new MillConfig(MLN.class.getField("LogVillagePaths"), "LogVillagePaths", 5).setDisplayDev(true));
/*  959 */       configPage.add(new MillConfig(MLN.class.getField("LogNetwork"), "LogNetwork", 5).setDisplayDev(true));
/*  960 */       configPage.add(new MillConfig(MLN.class.getField("LogMerchant"), "LogMerchant", 5).setDisplayDev(true));
/*  961 */       configPage.add(new MillConfig(MLN.class.getField("LogCulture"), "LogCulture", 5).setDisplayDev(true));
/*  962 */       configPage.add(new MillConfig(MLN.class.getField("LogTranslation"), "LogTranslation", 5).setDisplayDev(true));
/*      */       
/*  964 */       configPages.add(configPage);
/*  965 */       configPageTitles.add("config.page.devtools");
/*  966 */       configPageDesc.add(null);
/*      */       
/*  968 */       configPage = new ArrayList();
/*      */       
/*  970 */       configPage.add(new MillConfig(MLN.class.getField("bonusCode"), "bonus_code", 6).setMaxStringLength(4));
/*      */       
/*  972 */       configPages.add(configPage);
/*  973 */       configPageTitles.add("config.page.bonus");
/*  974 */       configPageDesc.add("config.page.bonus.desc");
/*      */       
/*  976 */       for (List<MillConfig> aConfigPage : configPages) {
/*  977 */         for (MillConfig config : aConfigPage) {
/*  978 */           configs.put(config.key, config);
/*      */         }
/*      */       }
/*      */     }
/*      */     catch (Exception e) {
/*  983 */       error(null, "Exception when initialising config items: " + e);
/*      */     }
/*      */   }
/*      */   
/*      */   public static boolean isTranslationLoaded() {
/*  988 */     return mainLanguage != null;
/*      */   }
/*      */   
/*      */   public static void loadConfig()
/*      */   {
/*  993 */     Mill.proxy.loadKeyDefaultSettings();
/*      */     
/*  995 */     initConfigItems();
/*      */     
/*  997 */     boolean mainConfig = readConfigFile(Mill.proxy.getConfigFile(), true);
/*      */     
/*  999 */     if (!mainConfig) {
/* 1000 */       System.err.println("ERREUR: Impossible de trouver le fichier de configuration " + Mill.proxy.getConfigFile().getAbsolutePath() + ". Vérifiez que le dossier millenaire est bien dans minecraft/mods/");
/*      */       
/* 1002 */       System.err.println("ERROR: Could not find the config file at " + Mill.proxy.getConfigFile().getAbsolutePath() + ". Check that the millenaire directory is in minecraft/mods/");
/*      */       
/* 1004 */       if (!Mill.proxy.isTrueServer()) {
/* 1005 */         Mill.displayMillenaireLocationError = true;
/*      */       }
/*      */       
/* 1008 */       Mill.startupError = true;
/* 1009 */       return;
/*      */     }
/*      */     
/* 1012 */     readConfigFile(Mill.proxy.getCustomConfigFile(), false);
/*      */     
/* 1014 */     if (logfile) {
/*      */       try {
/* 1016 */         writer = new FileWriter(Mill.proxy.getLogFile(), true);
/*      */       } catch (IOException e) {
/* 1018 */         writer = null;
/*      */       }
/*      */     } else {
/* 1021 */       writer = null;
/*      */     }
/*      */     
/* 1024 */     Mill.loadingDirs.add(Mill.proxy.getBaseDir());
/*      */     
/* 1026 */     File modDirs = new File(Mill.proxy.getCustomDir(), "mods");
/*      */     
/* 1028 */     modDirs.mkdirs();
/*      */     
/* 1030 */     String mods = "";
/*      */     
/* 1032 */     for (File mod : modDirs.listFiles())
/*      */     {
/* 1034 */       if ((mod.isDirectory()) && (!mod.isHidden())) {
/* 1035 */         Mill.loadingDirs.add(mod);
/* 1036 */         mods = mods + mod.getName() + " ";
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1041 */     if (mods.length() == 0) {
/* 1042 */       writeText("Starting new session.");
/*      */     } else {
/* 1044 */       writeText("Starting new session. Mods: " + mods);
/*      */     }
/*      */   }
/*      */   
/*      */   public static void loadLanguages(String minecraftLanguage)
/*      */   {
/* 1050 */     if (!main_language.equals("")) {
/* 1051 */       effective_language = main_language;
/*      */ 
/*      */     }
/* 1054 */     else if (minecraftLanguage != null) {
/* 1055 */       effective_language = minecraftLanguage;
/*      */     } else {
/* 1057 */       effective_language = "fr";
/*      */     }
/*      */     
/* 1060 */     if ((loadedLanguage != null) && (loadedLanguage.equals(effective_language))) {
/* 1061 */       return;
/*      */     }
/*      */     
/* 1064 */     major(null, "Loading language: " + effective_language);
/*      */     
/* 1066 */     loadedLanguage = effective_language;
/*      */     
/* 1068 */     List<File> languageDirs = getLanguageDirs();
/*      */     
/* 1070 */     mainLanguage = new Language(effective_language, false);
/* 1071 */     mainLanguage.loadFromDisk(languageDirs);
/*      */     
/* 1073 */     if (main_language.equals(fallback_language)) {
/* 1074 */       fallbackLanguage = mainLanguage;
/*      */     } else {
/* 1076 */       fallbackLanguage = new Language(fallback_language, false);
/* 1077 */       fallbackLanguage.loadFromDisk(languageDirs);
/*      */     }
/*      */     
/* 1080 */     if (loadAllLanguages) {
/* 1081 */       File mainDir = (File)languageDirs.get(0);
/*      */       
/* 1083 */       for (File lang : mainDir.listFiles()) {
/* 1084 */         if ((lang.isDirectory()) && (!lang.isHidden())) {
/* 1085 */           String key = lang.getName().toLowerCase();
/* 1086 */           if (!loadedLanguages.containsKey(key)) {
/* 1087 */             Language l = new Language(key, false);
/* 1088 */             l.loadFromDisk(languageDirs);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 1094 */     if (!loadedLanguages.containsKey("fr")) {
/* 1095 */       Language l = new Language("fr", false);
/* 1096 */       l.loadFromDisk(languageDirs);
/*      */     }
/* 1098 */     if (!loadedLanguages.containsKey("en")) {
/* 1099 */       Language l = new Language("en", false);
/* 1100 */       l.loadFromDisk(languageDirs);
/*      */     }
/*      */     
/* 1103 */     for (Culture c : Culture.ListCultures) {
/* 1104 */       c.loadLanguages(languageDirs, effective_language, fallback_language);
/*      */     }
/*      */     
/* 1107 */     applyLanguage();
/*      */     
/* 1109 */     if (generateTranslationGap)
/*      */     {
/* 1111 */       HashMap<String, Integer> percentageComplete = new HashMap();
/*      */       
/* 1113 */       ArrayList<Language> list = new ArrayList(loadedLanguages.values());
/*      */       
/* 1115 */       for (Language l : list)
/*      */       {
/*      */         String refLanguage;
/*      */         String refLanguage;
/* 1119 */         if (l.language.startsWith("fr")) {
/* 1120 */           refLanguage = "en";
/*      */         } else {
/* 1122 */           refLanguage = "fr";
/*      */         }
/*      */         
/* 1125 */         Language ref = null;
/*      */         
/* 1127 */         if (loadedLanguages.containsKey(refLanguage)) {
/* 1128 */           ref = (Language)loadedLanguages.get(refLanguage);
/*      */         } else {
/* 1130 */           ref = new Language(refLanguage, false);
/* 1131 */           ref.loadFromDisk(languageDirs);
/*      */         }
/*      */         
/* 1134 */         l.compareWithLanguage(percentageComplete, ref);
/*      */       }
/*      */       
/* 1137 */       File translationGapDir = new File(Mill.proxy.getBaseDir(), "Translation gaps");
/*      */       
/* 1139 */       if (!translationGapDir.exists()) {
/* 1140 */         translationGapDir.mkdirs();
/*      */       }
/*      */       
/* 1143 */       File file = new File(translationGapDir, "Results.txt");
/*      */       
/* 1145 */       if (file.exists()) {
/* 1146 */         file.delete();
/*      */       }
/*      */       
/*      */       try
/*      */       {
/* 1151 */         BufferedWriter writer = MillCommonUtilities.getWriter(file);
/*      */         
/* 1153 */         for (String key : percentageComplete.keySet()) {
/* 1154 */           writer.write(key + ": " + percentageComplete.get(key) + "%" + EOL);
/*      */         }
/* 1156 */         writer.close();
/*      */       } catch (Exception e) {
/* 1158 */         printException(e);
/*      */       }
/*      */     }
/*      */     
/* 1162 */     if (DEV) {
/* 1163 */       writeBaseConfigFile();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public static void major(Object obj, String s)
/*      */   {
/* 1170 */     writeText("MAJOR: " + obj + ": " + s);
/*      */   }
/*      */   
/*      */   private static String md5(String input) {
/* 1174 */     String result = input;
/* 1175 */     if (input != null) {
/*      */       try
/*      */       {
/* 1178 */         MessageDigest md = MessageDigest.getInstance("MD5");
/* 1179 */         md.update(input.getBytes());
/* 1180 */         BigInteger hash = new BigInteger(1, md.digest());
/* 1181 */         result = hash.toString(16);
/* 1182 */         while (result.length() < 32) {
/* 1183 */           result = "0" + result;
/*      */         }
/*      */       } catch (NoSuchAlgorithmException e) {
/* 1186 */         printException("Exception in md5():", e);
/*      */       }
/*      */     }
/* 1189 */     return result;
/*      */   }
/*      */   
/*      */   public static void minor(Object obj, String s) {
/* 1193 */     writeText("MINOR: " + obj + ": " + s);
/*      */   }
/*      */   
/*      */   private static void nameItems()
/*      */   {
/* 1198 */     LanguageRegistry.addName(Mill.lockedChest, string("item.building"));
/* 1199 */     LanguageRegistry.addName(Mill.denier, string("item.denier"));
/* 1200 */     LanguageRegistry.addName(Mill.denier_or, string("item.denieror"));
/* 1201 */     LanguageRegistry.addName(Mill.denier_argent, string("item.denierargent"));
/*      */     
/* 1203 */     LanguageRegistry.addName(Mill.calva, string("item.calva"));
/* 1204 */     LanguageRegistry.addName(Mill.tripes, string("item.tripes"));
/* 1205 */     LanguageRegistry.addName(Mill.boudin, string("item.boudin"));
/*      */     
/* 1207 */     LanguageRegistry.addName(Mill.ciderapple, string("item.ciderapple"));
/* 1208 */     LanguageRegistry.addName(Mill.cider, string("item.cider"));
/* 1209 */     LanguageRegistry.addName(Mill.summoningWand, string("item.villagewand"));
/* 1210 */     LanguageRegistry.addName(Mill.negationWand, string("item.negationwand"));
/* 1211 */     LanguageRegistry.addName(Mill.normanPickaxe, string("item.normanPickaxe"));
/* 1212 */     LanguageRegistry.addName(Mill.normanAxe, string("item.normanAxe"));
/* 1213 */     LanguageRegistry.addName(Mill.normanShovel, string("item.normanShovel"));
/* 1214 */     LanguageRegistry.addName(Mill.normanHoe, string("item.normanHoe"));
/* 1215 */     LanguageRegistry.addName(Mill.normanBroadsword, string("item.normanBroadsword"));
/* 1216 */     LanguageRegistry.addName(Mill.normanHelmet, string("item.normanHelmet"));
/* 1217 */     LanguageRegistry.addName(Mill.normanPlate, string("item.normanPlate"));
/* 1218 */     LanguageRegistry.addName(Mill.normanLegs, string("item.normanLegs"));
/* 1219 */     LanguageRegistry.addName(Mill.normanBoots, string("item.normanBoots"));
/* 1220 */     LanguageRegistry.addName(Mill.parchmentVillagers, string("item.normanvillagers"));
/* 1221 */     LanguageRegistry.addName(Mill.parchmentBuildings, string("item.normanbuildings"));
/* 1222 */     LanguageRegistry.addName(Mill.parchmentItems, string("item.normanitems"));
/* 1223 */     LanguageRegistry.addName(Mill.parchmentComplete, string("item.normanfull"));
/* 1224 */     LanguageRegistry.addName(Mill.tapestry, string("item.tapestry"));
/* 1225 */     LanguageRegistry.addName(Mill.vishnu_amulet, string("item.vishnu_amulet"));
/* 1226 */     LanguageRegistry.addName(Mill.alchemist_amulet, string("item.alchemist_amulet"));
/* 1227 */     LanguageRegistry.addName(Mill.yddrasil_amulet, string("item.yddrasil_amulet"));
/* 1228 */     LanguageRegistry.addName(Mill.skoll_hati_amulet, string("item.skoll_hati_amulet"));
/* 1229 */     LanguageRegistry.addName(Mill.parchmentVillageScroll, string("item.villagescroll"));
/* 1230 */     LanguageRegistry.addName(Mill.rice, string("item.rice"));
/* 1231 */     LanguageRegistry.addName(Mill.turmeric, string("item.turmeric"));
/* 1232 */     LanguageRegistry.addName(Mill.vegcurry, string("item.vegcurry"));
/* 1233 */     LanguageRegistry.addName(Mill.chickencurry, string("item.chickencurry"));
/* 1234 */     LanguageRegistry.addName(Mill.brickmould, string("item.brickmould"));
/* 1235 */     LanguageRegistry.addName(Mill.rasgulla, string("item.rasgulla"));
/* 1236 */     LanguageRegistry.addName(Mill.indianstatue, string("item.indianstatue"));
/*      */     
/* 1238 */     LanguageRegistry.addName(Mill.parchmentIndianVillagers, string("item.indianvillagers"));
/* 1239 */     LanguageRegistry.addName(Mill.parchmentIndianBuildings, string("item.indianbuildings"));
/* 1240 */     LanguageRegistry.addName(Mill.parchmentIndianItems, string("item.indianitems"));
/* 1241 */     LanguageRegistry.addName(Mill.parchmentIndianComplete, string("item.indianfull"));
/*      */     
/* 1243 */     LanguageRegistry.addName(new ItemStack(Mill.wood_decoration, 1, 0), string("item.plaintimber"));
/* 1244 */     LanguageRegistry.addName(new ItemStack(Mill.wood_decoration, 1, 1), string("item.crosstimber"));
/* 1245 */     LanguageRegistry.addName(new ItemStack(Mill.wood_decoration, 1, 2), string("item.thatched"));
/* 1246 */     LanguageRegistry.addName(new ItemStack(Mill.wood_decoration, 1, 3), string("item.emptysilkwormblock"));
/* 1247 */     LanguageRegistry.addName(new ItemStack(Mill.wood_decoration, 1, 4), string("item.fullsilkwormblock"));
/* 1248 */     LanguageRegistry.addName(new ItemStack(Mill.earth_decoration, 1, 0), string("item.wetbrick"));
/* 1249 */     LanguageRegistry.addName(new ItemStack(Mill.earth_decoration, 1, 1), string("item.dirtwall"));
/* 1250 */     LanguageRegistry.addName(new ItemStack(Mill.stone_decoration, 1, 0), string("item.cookedbrick"));
/* 1251 */     LanguageRegistry.addName(new ItemStack(Mill.stone_decoration, 1, 1), string("item.mudbrick"));
/* 1252 */     LanguageRegistry.addName(new ItemStack(Mill.stone_decoration, 1, 2), string("item.mayangold"));
/* 1253 */     LanguageRegistry.addName(new ItemStack(Mill.stone_decoration, 1, 3), string("item.alchimistexplosive"));
/*      */     
/* 1255 */     LanguageRegistry.addName(new ItemStack(Mill.path, 1, 0), string("item.pathdirt"));
/* 1256 */     LanguageRegistry.addName(new ItemStack(Mill.path, 1, 1), string("item.pathgravel"));
/* 1257 */     LanguageRegistry.addName(new ItemStack(Mill.path, 1, 2), string("item.pathslabs"));
/* 1258 */     LanguageRegistry.addName(new ItemStack(Mill.path, 1, 3), string("item.pathsandstone"));
/* 1259 */     LanguageRegistry.addName(new ItemStack(Mill.path, 1, 4), string("item.pathochretiles"));
/* 1260 */     LanguageRegistry.addName(new ItemStack(Mill.path, 1, 5), string("item.pathgravelslabs"));
/*      */     
/* 1262 */     LanguageRegistry.addName(new ItemStack(Mill.path, 1, 8), string("item.pathdirt"));
/* 1263 */     LanguageRegistry.addName(new ItemStack(Mill.path, 1, 9), string("item.pathgravel"));
/* 1264 */     LanguageRegistry.addName(new ItemStack(Mill.path, 1, 10), string("item.pathslabs"));
/* 1265 */     LanguageRegistry.addName(new ItemStack(Mill.path, 1, 11), string("item.pathsandstone"));
/* 1266 */     LanguageRegistry.addName(new ItemStack(Mill.path, 1, 12), string("item.pathochretiles"));
/* 1267 */     LanguageRegistry.addName(new ItemStack(Mill.path, 1, 13), string("item.pathgravelslabs"));
/*      */     
/* 1269 */     LanguageRegistry.addName(new ItemStack(Mill.pathSlab, 1, 0), string("item.pathdirt"));
/* 1270 */     LanguageRegistry.addName(new ItemStack(Mill.pathSlab, 1, 1), string("item.pathgravel"));
/* 1271 */     LanguageRegistry.addName(new ItemStack(Mill.pathSlab, 1, 2), string("item.pathslabs"));
/* 1272 */     LanguageRegistry.addName(new ItemStack(Mill.pathSlab, 1, 3), string("item.pathsandstone"));
/* 1273 */     LanguageRegistry.addName(new ItemStack(Mill.pathSlab, 1, 4), string("item.pathochretiles"));
/* 1274 */     LanguageRegistry.addName(new ItemStack(Mill.pathSlab, 1, 5), string("item.pathgravelslabs"));
/*      */     
/* 1276 */     LanguageRegistry.addName(new ItemStack(Mill.pathSlab, 1, 8), string("item.pathdirt"));
/* 1277 */     LanguageRegistry.addName(new ItemStack(Mill.pathSlab, 1, 9), string("item.pathgravel"));
/* 1278 */     LanguageRegistry.addName(new ItemStack(Mill.pathSlab, 1, 10), string("item.pathslabs"));
/* 1279 */     LanguageRegistry.addName(new ItemStack(Mill.pathSlab, 1, 11), string("item.pathsandstone"));
/* 1280 */     LanguageRegistry.addName(new ItemStack(Mill.pathSlab, 1, 12), string("item.pathochretiles"));
/* 1281 */     LanguageRegistry.addName(new ItemStack(Mill.pathSlab, 1, 13), string("item.pathgravelslabs"));
/*      */     
/* 1283 */     LanguageRegistry.addName(Mill.mayanstatue, string("item.mayanstatue"));
/* 1284 */     LanguageRegistry.addName(Mill.maize, string("item.maize"));
/* 1285 */     LanguageRegistry.addName(Mill.wah, string("item.wah"));
/* 1286 */     LanguageRegistry.addName(Mill.masa, string("item.masa"));
/* 1287 */     LanguageRegistry.addName(Mill.unknownPowder, string("item.unknownpowder"));
/*      */     
/* 1289 */     LanguageRegistry.addName(Mill.parchmentMayanVillagers, string("item.mayanvillagers"));
/* 1290 */     LanguageRegistry.addName(Mill.parchmentMayanBuildings, string("item.mayanbuildings"));
/* 1291 */     LanguageRegistry.addName(Mill.parchmentMayanItems, string("item.mayanitems"));
/* 1292 */     LanguageRegistry.addName(Mill.parchmentMayanComplete, string("item.mayanfull"));
/* 1293 */     LanguageRegistry.addName(Mill.parchmentSadhu, string("item.parchmentsadhu"));
/*      */     
/* 1295 */     LanguageRegistry.addName(new ItemStack(Mill.paperWall, 1, 0), string("item.paperwall"));
/* 1296 */     LanguageRegistry.addName(Mill.udon, string("item.udon"));
/*      */     
/* 1298 */     LanguageRegistry.addName(Mill.tachiSword, string("item.tachisword"));
/*      */     
/* 1300 */     LanguageRegistry.addName(Mill.obsidianFlake, string("item.obsidianFlake"));
/* 1301 */     LanguageRegistry.addName(Mill.mayanPickaxe, string("item.mayanPickaxe"));
/* 1302 */     LanguageRegistry.addName(Mill.mayanAxe, string("item.mayanAxe"));
/* 1303 */     LanguageRegistry.addName(Mill.mayanShovel, string("item.mayanShovel"));
/* 1304 */     LanguageRegistry.addName(Mill.mayanHoe, string("item.mayanHoe"));
/* 1305 */     LanguageRegistry.addName(Mill.mayanMace, string("item.mayanMace"));
/*      */     
/* 1307 */     LanguageRegistry.addName(Mill.yumiBow, string("item.yumibow"));
/*      */     
/* 1309 */     LanguageRegistry.addName(Mill.japaneseWarriorBlueLegs, string("item.japaneseWarriorBlueLegs"));
/* 1310 */     LanguageRegistry.addName(Mill.japaneseWarriorBlueHelmet, string("item.japaneseWarriorBlueHelmet"));
/* 1311 */     LanguageRegistry.addName(Mill.japaneseWarriorBluePlate, string("item.japaneseWarriorBluePlate"));
/* 1312 */     LanguageRegistry.addName(Mill.japaneseWarriorBlueBoots, string("item.japaneseWarriorBlueBoots"));
/*      */     
/* 1314 */     LanguageRegistry.addName(Mill.japaneseWarriorRedLegs, string("item.japaneseWarriorRedLegs"));
/* 1315 */     LanguageRegistry.addName(Mill.japaneseWarriorRedHelmet, string("item.japaneseWarriorRedHelmet"));
/* 1316 */     LanguageRegistry.addName(Mill.japaneseWarriorRedPlate, string("item.japaneseWarriorRedPlate"));
/* 1317 */     LanguageRegistry.addName(Mill.japaneseWarriorRedBoots, string("item.japaneseWarriorRedBoots"));
/*      */     
/* 1319 */     LanguageRegistry.addName(Mill.japaneseGuardLegs, string("item.japaneseGuardLegs"));
/* 1320 */     LanguageRegistry.addName(Mill.japaneseGuardHelmet, string("item.japaneseGuardHelmet"));
/* 1321 */     LanguageRegistry.addName(Mill.japaneseGuardPlate, string("item.japaneseGuardPlate"));
/* 1322 */     LanguageRegistry.addName(Mill.japaneseGuardBoots, string("item.japaneseGuardBoots"));
/*      */     
/* 1324 */     LanguageRegistry.addName(Mill.parchmentJapaneseVillagers, string("item.japanesevillagers"));
/* 1325 */     LanguageRegistry.addName(Mill.parchmentJapaneseBuildings, string("item.japanesebuildings"));
/* 1326 */     LanguageRegistry.addName(Mill.parchmentJapaneseItems, string("item.japaneseitems"));
/* 1327 */     LanguageRegistry.addName(Mill.parchmentJapaneseComplete, string("item.japanesefull"));
/*      */     
/* 1329 */     LanguageRegistry.addName(Mill.grapes, string("item.grapes"));
/* 1330 */     LanguageRegistry.addName(Mill.wineFancy, string("item.wine"));
/* 1331 */     LanguageRegistry.addName(Mill.silk, string("item.silk"));
/* 1332 */     LanguageRegistry.addName(Mill.byzantineiconsmall, string("item.byzantineiconsmall"));
/* 1333 */     LanguageRegistry.addName(Mill.byzantineiconmedium, string("item.byzantineiconmedium"));
/* 1334 */     LanguageRegistry.addName(Mill.byzantineiconlarge, string("item.byzantineiconlarge"));
/* 1335 */     LanguageRegistry.addName(Mill.byzantine_tiles, string("item.byzantinebrick"));
/* 1336 */     LanguageRegistry.addName(Mill.byzantine_tile_slab, string("item.byzantineslab"));
/* 1337 */     LanguageRegistry.addName(Mill.byzantine_stone_tiles, string("item.byzantinemixedbrick"));
/*      */     
/* 1339 */     LanguageRegistry.addName(Mill.byzantineLegs, string("item.byzantineLegs"));
/* 1340 */     LanguageRegistry.addName(Mill.byzantineHelmet, string("item.byzantineHelmet"));
/* 1341 */     LanguageRegistry.addName(Mill.byzantinePlate, string("item.byzantinePlate"));
/* 1342 */     LanguageRegistry.addName(Mill.byzantineBoots, string("item.byzantineBoots"));
/*      */     
/* 1344 */     LanguageRegistry.addName(Mill.byzantineMace, string("item.byzantineMace"));
/*      */     
/* 1346 */     LanguageRegistry.addName(new ItemStack(Mill.clothes, 1, 0), string("item.clothes_byz_wool"));
/* 1347 */     LanguageRegistry.addName(new ItemStack(Mill.clothes, 1, 1), string("item.clothes_byz_silk"));
/* 1348 */     LanguageRegistry.addName(Mill.wineBasic, string("item.wineBasic"));
/* 1349 */     LanguageRegistry.addName(Mill.lambRaw, string("item.lambRaw"));
/* 1350 */     LanguageRegistry.addName(Mill.lambCooked, string("item.lambCooked"));
/* 1351 */     LanguageRegistry.addName(Mill.feta, string("item.feta"));
/* 1352 */     LanguageRegistry.addName(Mill.souvlaki, string("item.souvlaki"));
/* 1353 */     LanguageRegistry.addName(Mill.purse, string("item.purse"));
/* 1354 */     LanguageRegistry.addName(Mill.sake, string("item.sake"));
/* 1355 */     LanguageRegistry.addName(Mill.cacauhaa, string("item.cacauhaa"));
/* 1356 */     LanguageRegistry.addName(Mill.mayanQuestCrown, string("item.mayanQuestCrown"));
/* 1357 */     LanguageRegistry.addName(Mill.ikayaki, string("item.ikayaki"));
/*      */   }
/*      */   
/*      */   private static String now() {
/* 1361 */     Calendar cal = Calendar.getInstance();
/* 1362 */     SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
/* 1363 */     return sdf.format(cal.getTime());
/*      */   }
/*      */   
/*      */   public static void printException(Exception e)
/*      */   {
/* 1368 */     printException(null, e);
/*      */   }
/*      */   
/*      */   public static void printException(String s, Exception e)
/*      */   {
/* 1373 */     int cpt = 1;
/*      */     
/*      */     String hashString;
/*      */     String hashString;
/* 1377 */     if (s != null) {
/* 1378 */       hashString = s;
/*      */     } else {
/* 1380 */       hashString = "";
/*      */     }
/*      */     
/* 1383 */     if (e.getMessage() != null) {
/* 1384 */       hashString = hashString + e.getMessage();
/*      */     }
/*      */     
/* 1387 */     if ((e.getStackTrace() != null) && (e.getStackTrace().length > 0)) {
/* 1388 */       hashString = hashString + e.getStackTrace()[0].toString();
/*      */     }
/*      */     
/* 1391 */     int hash = hashString.hashCode();
/* 1392 */     if (exceptionCount.containsKey(Integer.valueOf(hash))) {
/* 1393 */       cpt = ((Integer)exceptionCount.get(Integer.valueOf(hash))).intValue();
/* 1394 */       cpt++;
/*      */     }
/* 1396 */     exceptionCount.put(Integer.valueOf(hash), Integer.valueOf(cpt));
/*      */     
/* 1398 */     if ((DEV) && (cpt == 1)) {
/* 1399 */       writeText("    !====================================!");
/*      */     }
/*      */     
/* 1402 */     if (cpt == 1) {
/* 1403 */       if (s == null) {
/* 1404 */         writeText("Exception, printing stack:");
/*      */       } else {
/* 1406 */         writeText(s);
/*      */       }
/*      */       
/* 1409 */       StringWriter sw = new StringWriter();
/* 1410 */       PrintWriter pw = new PrintWriter(sw, true);
/* 1411 */       e.printStackTrace(pw);
/* 1412 */       pw.flush();
/* 1413 */       sw.flush();
/* 1414 */       writeText(sw.toString());
/*      */     } else {
/* 1416 */       writeText("Repeat exception x" + cpt + ": " + e.getMessage());
/*      */     }
/*      */     
/* 1419 */     if ((DEV) && (cpt == 1)) {
/* 1420 */       writeText("     ==================================== ");
/*      */     }
/*      */   }
/*      */   
/*      */   public static String questString(String key, boolean required) {
/* 1425 */     return questString(key, true, true, required);
/*      */   }
/*      */   
/*      */   public static String questString(String key, boolean main, boolean fallback, boolean required) {
/* 1429 */     key = key.toLowerCase();
/* 1430 */     if ((main) && (mainLanguage != null) && (mainLanguage.questStrings.containsKey(key))) {
/* 1431 */       return (String)mainLanguage.questStrings.get(key);
/*      */     }
/*      */     
/* 1434 */     if ((main) && (serverMainLanguage != null) && (serverMainLanguage.questStrings.containsKey(key))) {
/* 1435 */       return (String)serverMainLanguage.questStrings.get(key);
/*      */     }
/*      */     
/* 1438 */     if ((fallback) && (fallbackLanguage != null) && (fallbackLanguage.questStrings.containsKey(key))) {
/* 1439 */       return (String)fallbackLanguage.questStrings.get(key);
/*      */     }
/*      */     
/* 1442 */     if ((fallback) && (serverFallbackLanguage != null) && (serverFallbackLanguage.questStrings.containsKey(key))) {
/* 1443 */       return (String)serverFallbackLanguage.questStrings.get(key);
/*      */     }
/*      */     
/* 1446 */     if (required) {
/* 1447 */       return key;
/*      */     }
/*      */     
/* 1450 */     return null;
/*      */   }
/*      */   
/*      */   public static String questStringFallbackOnly(String key, boolean required) {
/* 1454 */     return questString(key, false, true, required);
/*      */   }
/*      */   
/*      */   public static String questStringMainOnly(String key, boolean required) {
/* 1458 */     return questString(key, true, false, required);
/*      */   }
/*      */   
/*      */   private static boolean readConfigFile(File file, boolean defaultFile)
/*      */   {
/* 1463 */     if (!file.exists()) {
/* 1464 */       return false;
/*      */     }
/*      */     try
/*      */     {
/* 1468 */       BufferedReader reader = MillCommonUtilities.getReader(file);
/*      */       
/*      */       String line;
/*      */       
/* 1472 */       while ((line = reader.readLine()) != null) {
/* 1473 */         if ((line.trim().length() > 0) && (!line.startsWith("//"))) {
/* 1474 */           String[] temp = line.split("=");
/* 1475 */           if (temp.length == 2)
/*      */           {
/* 1477 */             String key = temp[0].trim().toLowerCase();
/* 1478 */             String value = temp[1];
/*      */             
/* 1480 */             boolean configHandled = false;
/*      */             
/* 1482 */             if (configs.containsKey(key)) {
/* 1483 */               ((MillConfig)configs.get(key)).setValueFromString(value, defaultFile);
/* 1484 */               configHandled = true;
/*      */             }
/*      */             
/* 1487 */             if (!configHandled) {
/* 1488 */               if (key.equalsIgnoreCase("devmode")) {
/* 1489 */                 DEV = Boolean.parseBoolean(value);
/* 1490 */               } else if (key.equalsIgnoreCase("console")) {
/* 1491 */                 console = Boolean.parseBoolean(value);
/* 1492 */               } else if (key.equalsIgnoreCase("logfile")) {
/* 1493 */                 logfile = Boolean.parseBoolean(value);
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
/*      */               }
/* 1512 */               else if (key.equalsIgnoreCase("logfile")) {
/* 1513 */                 logfile = Boolean.parseBoolean(value);
/* 1514 */               } else if (key.equalsIgnoreCase("infinite_amulet")) {
/* 1515 */                 infiniteAmulet = Boolean.parseBoolean(value);
/*      */ 
/*      */ 
/*      */               }
/* 1519 */               else if (key.equalsIgnoreCase("stop_default_villages")) {
/* 1520 */                 stopDefaultVillages = Boolean.parseBoolean(value);
/*      */ 
/*      */ 
/*      */ 
/*      */               }
/* 1525 */               else if (key.equalsIgnoreCase("se_indicators")) {
/* 1526 */                 seIndicators = Boolean.parseBoolean(value);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */               }
/* 1557 */               else if (key.equalsIgnoreCase("language")) {
/* 1558 */                 main_language = value.toLowerCase();
/*      */ 
/*      */ 
/*      */               }
/* 1562 */               else if (key.equalsIgnoreCase("forbidden_blocks")) {
/* 1563 */                 for (String name : value.split(",")) {
/* 1564 */                   if (Block.field_149771_c.func_148741_d(name)) {
/* 1565 */                     forbiddenBlocks.add((Block)Block.field_149771_c.func_82594_a(name));
/*      */                   } else {
/* 1567 */                     System.out.println("Could not read forbidden name: " + name);
/*      */                   }
/*      */                 }
/* 1570 */               } else if (key.equalsIgnoreCase("log.TileEntityBuilding")) {
/* 1571 */                 LogTileEntityBuilding = readLogLevel(value);
/* 1572 */               } else if (key.equalsIgnoreCase("log.WorldGeneration")) {
/* 1573 */                 LogWorldGeneration = readLogLevel(value);
/* 1574 */               } else if (key.equalsIgnoreCase("log.FarmerAI")) {
/* 1575 */                 LogFarmerAI = readLogLevel(value);
/* 1576 */               } else if (key.equalsIgnoreCase("log.Diplomacy")) {
/* 1577 */                 LogDiplomacy = readLogLevel(value);
/* 1578 */               } else if (key.equalsIgnoreCase("log.WifeAI")) {
/* 1579 */                 LogWifeAI = readLogLevel(value);
/* 1580 */               } else if (key.equalsIgnoreCase("log.Villager")) {
/* 1581 */                 LogVillager = readLogLevel(value);
/* 1582 */               } else if (key.equalsIgnoreCase("log.Quest")) {
/* 1583 */                 LogQuest = readLogLevel(value);
/* 1584 */               } else if (key.equalsIgnoreCase("log.Pathing")) {
/* 1585 */                 LogPathing = readLogLevel(value);
/* 1586 */               } else if (key.equalsIgnoreCase("log.Connections")) {
/* 1587 */                 LogConnections = readLogLevel(value);
/* 1588 */               } else if (key.equalsIgnoreCase("log.getPath")) {
/* 1589 */                 LogGetPath = readLogLevel(value);
/* 1590 */               } else if (key.equalsIgnoreCase("log.Lumberman")) {
/* 1591 */                 LogLumberman = readLogLevel(value);
/* 1592 */               } else if (key.equalsIgnoreCase("log.BuildingPlan")) {
/* 1593 */                 LogBuildingPlan = readLogLevel(value);
/* 1594 */               } else if (key.equalsIgnoreCase("log.GeneralAI")) {
/* 1595 */                 LogGeneralAI = readLogLevel(value);
/* 1596 */               } else if (key.equalsIgnoreCase("log.Selling")) {
/* 1597 */                 LogSelling = readLogLevel(value);
/* 1598 */               } else if (key.equalsIgnoreCase("log.Hybernation")) {
/* 1599 */                 LogHybernation = readLogLevel(value);
/* 1600 */               } else if (key.equalsIgnoreCase("log.Other")) {
/* 1601 */                 LogOther = readLogLevel(value);
/* 1602 */               } else if (key.equalsIgnoreCase("log.Children")) {
/* 1603 */                 LogChildren = readLogLevel(value);
/* 1604 */               } else if (key.equalsIgnoreCase("log.Performance")) {
/* 1605 */                 LogPerformance = readLogLevel(value);
/* 1606 */               } else if (key.equalsIgnoreCase("log.CattleFarmer")) {
/* 1607 */                 LogCattleFarmer = readLogLevel(value);
/* 1608 */               } else if (key.equalsIgnoreCase("log.Miner")) {
/* 1609 */                 LogMiner = readLogLevel(value);
/* 1610 */               } else if (key.equalsIgnoreCase("log.Village")) {
/* 1611 */                 LogVillage = readLogLevel(value);
/* 1612 */               } else if (key.equalsIgnoreCase("log.WorldInfo")) {
/* 1613 */                 LogWorldInfo = readLogLevel(value);
/* 1614 */               } else if (key.equalsIgnoreCase("log.Pujas")) {
/* 1615 */                 LogPujas = readLogLevel(value);
/* 1616 */               } else if (key.equalsIgnoreCase("log.villagerspawn")) {
/* 1617 */                 LogVillagerSpawn = readLogLevel(value);
/* 1618 */               } else if (key.equalsIgnoreCase("log.villagepaths")) {
/* 1619 */                 LogVillagePaths = readLogLevel(value);
/* 1620 */               } else if (key.equalsIgnoreCase("log.Network")) {
/* 1621 */                 LogNetwork = readLogLevel(value);
/* 1622 */               } else if (key.equalsIgnoreCase("log.Merchant")) {
/* 1623 */                 LogMerchant = readLogLevel(value);
/* 1624 */               } else if (key.equalsIgnoreCase("log.Culture")) {
/* 1625 */                 LogCulture = readLogLevel(value);
/* 1626 */               } else if (key.equalsIgnoreCase("log.Translation")) {
/* 1627 */                 LogTranslation = readLogLevel(value);
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
/*      */               }
/* 1640 */               else if (key.equalsIgnoreCase("force_preload_radius")) {
/* 1641 */                 forcePreload = Integer.parseInt(value) / 16;
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
/*      */ 
/*      */ 
/*      */               }
/* 1725 */               else if (key.equalsIgnoreCase("sprites_path")) {
/* 1726 */                 customTexture = value.trim();
/* 1727 */               } else if (key.equalsIgnoreCase("dynamic_textures")) {
/* 1728 */                 dynamictextures = Boolean.parseBoolean(value);
/* 1729 */               } else if (key.equalsIgnoreCase("quest_biome_forest")) {
/* 1730 */                 questBiomeForest = value.trim().toLowerCase();
/* 1731 */               } else if (key.equalsIgnoreCase("quest_biome_desert")) {
/* 1732 */                 questBiomeDesert = value.trim().toLowerCase();
/* 1733 */               } else if (key.equalsIgnoreCase("quest_biome_mountain")) {
/* 1734 */                 questBiomeMountain = value.trim().toLowerCase();
/*      */               } else {
/* 1736 */                 error(null, "Unknown config on line: " + line);
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/* 1742 */       reader.close();
/*      */       
/* 1744 */       System.out.println("Read config in " + file.getName() + ". Logging: " + console + "/" + logfile);
/*      */       
/* 1746 */       return true;
/*      */     }
/*      */     catch (Exception e) {
/* 1749 */       printException(e); }
/* 1750 */     return false;
/*      */   }
/*      */   
/*      */   public static int readLogLevel(String s)
/*      */   {
/* 1755 */     if (s.equalsIgnoreCase("major")) {
/* 1756 */       return 1;
/*      */     }
/* 1758 */     if (s.equalsIgnoreCase("minor")) {
/* 1759 */       return 2;
/*      */     }
/* 1761 */     if (s.equalsIgnoreCase("debug")) {
/* 1762 */       return 3;
/*      */     }
/* 1764 */     return 0;
/*      */   }
/*      */   
/*      */   public static String removeAccent(String source) {
/* 1768 */     return java.text.Normalizer.normalize(source, java.text.Normalizer.Form.NFD).replaceAll("[̀-ͯ]", "");
/*      */   }
/*      */   
/*      */   public static String string(String key) {
/* 1772 */     if (!isTranslationLoaded()) {
/* 1773 */       return "";
/*      */     }
/*      */     
/* 1776 */     key = key.toLowerCase();
/*      */     
/* 1778 */     return fillInName(getRawString(key, true));
/*      */   }
/*      */   
/*      */   public static String string(String key, String... values)
/*      */   {
/* 1783 */     String s = string(key);
/*      */     
/* 1785 */     int pos = 0;
/* 1786 */     for (String value : values) {
/* 1787 */       if (value != null) {
/* 1788 */         s = s.replaceAll("<" + pos + ">", value);
/*      */       } else {
/* 1790 */         s = s.replaceAll("<" + pos + ">", "");
/*      */       }
/* 1792 */       pos++;
/*      */     }
/*      */     
/* 1795 */     return s;
/*      */   }
/*      */   
/*      */ 
/*      */   public static String string(String[] values)
/*      */   {
/* 1801 */     if (values.length == 0) {
/* 1802 */       return "";
/*      */     }
/*      */     
/* 1805 */     String s = unknownString(values[0]);
/*      */     
/* 1807 */     int pos = -1;
/* 1808 */     for (String value : values) {
/* 1809 */       if (pos > -1) {
/* 1810 */         if (value != null) {
/* 1811 */           s = s.replaceAll("<" + pos + ">", unknownString(value));
/*      */         } else {
/* 1813 */           s = s.replaceAll("<" + pos + ">", "");
/*      */         }
/*      */       }
/* 1816 */       pos++;
/*      */     }
/*      */     
/* 1819 */     return fillInName(s);
/*      */   }
/*      */   
/*      */   public static void temp(Object obj, String s) {
/* 1823 */     if (DEV) {
/* 1824 */       writeText("TEMP: " + obj + ": " + s);
/*      */     }
/*      */   }
/*      */   
/*      */   public static String unknownString(String key)
/*      */   {
/* 1830 */     if (key == null) {
/* 1831 */       return "";
/*      */     }
/*      */     
/* 1834 */     if (!isTranslationLoaded()) {
/* 1835 */       return key;
/*      */     }
/*      */     
/* 1838 */     if (key.startsWith("_item:")) {
/* 1839 */       int id = Integer.parseInt(key.split(":")[1]);
/* 1840 */       int meta = Integer.parseInt(key.split(":")[2]);
/*      */       try
/*      */       {
/* 1843 */         InvItem item = new InvItem(MillCommonUtilities.getItemById(id), meta);
/* 1844 */         return item.getName();
/*      */       } catch (MillenaireException e) {
/* 1846 */         printException(e);
/* 1847 */         return "";
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1852 */     if (key.startsWith("_buildingGame:")) {
/* 1853 */       String cultureKey = key.split(":")[1];
/* 1854 */       Culture culture = Culture.getCultureByName(cultureKey);
/* 1855 */       if (culture != null) {
/* 1856 */         String buildingKey = key.split(":")[2];
/* 1857 */         BuildingPlanSet set = culture.getBuildingPlanSet(buildingKey);
/* 1858 */         if (set != null) {
/* 1859 */           int variation = Integer.parseInt(key.split(":")[3]);
/* 1860 */           if (variation < set.plans.size()) {
/* 1861 */             int level = Integer.parseInt(key.split(":")[4]);
/* 1862 */             if (level < ((BuildingPlan[])set.plans.get(variation)).length) {
/* 1863 */               BuildingPlan plan = ((BuildingPlan[])set.plans.get(variation))[level];
/* 1864 */               return plan.getGameName();
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 1871 */     String rawKey = getRawString(key, false);
/*      */     
/* 1873 */     if (rawKey != null) {
/* 1874 */       return fillInName(rawKey);
/*      */     }
/*      */     
/* 1877 */     return key;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void validateResourceMap(Map<InvItem, Integer> map)
/*      */   {
/* 1885 */     int errors = 0;
/* 1886 */     for (InvItem item : map.keySet()) {
/* 1887 */       if (item == null) {
/* 1888 */         printException(new MillenaireException("Found a null InvItem in map!"));
/* 1889 */         errors++;
/* 1890 */       } else if (!map.containsKey(item)) {
/* 1891 */         printException(new MillenaireException("Key: " + item + " not present in map???"));
/* 1892 */         errors++;
/* 1893 */       } else if (map.get(item) == null) {
/* 1894 */         printException(new MillenaireException("Key: " + item + " has null value in map."));
/* 1895 */         errors++;
/*      */       }
/*      */     }
/* 1898 */     if (map.size() > 0) {
/* 1899 */       error(null, "Validated map. Found " + errors + " amoung " + map.size() + " keys.");
/*      */     }
/*      */   }
/*      */   
/*      */   public static void warning(Object obj, String s) {
/* 1904 */     if (DEV) {
/* 1905 */       writeText("    !=============!");
/*      */     }
/* 1907 */     writeText("WARNING: " + obj + ": " + s);
/* 1908 */     if (DEV) {
/* 1909 */       writeText("     =============");
/*      */     }
/*      */   }
/*      */   
/*      */   public static void writeBaseConfigFile()
/*      */   {
/* 1915 */     File file = new File(Mill.proxy.getBaseDir(), "config-base.txt");
/*      */     
/*      */     try
/*      */     {
/* 1919 */       BufferedWriter writer = MillCommonUtilities.getWriter(file);
/*      */       
/* 1921 */       Language main = mainLanguage;
/*      */       
/* 1923 */       Language fr = (Language)loadedLanguages.get("fr");
/* 1924 */       Language en = (Language)loadedLanguages.get("en");
/*      */       
/* 1926 */       for (int i = 0; i < configPages.size(); i++)
/*      */       {
/* 1928 */         mainLanguage = fr;
/* 1929 */         String frTitle = string((String)configPageTitles.get(i));
/* 1930 */         mainLanguage = en;
/* 1931 */         String enTitle = string((String)configPageTitles.get(i));
/*      */         
/* 1933 */         writer.write("//--------------------------------------------------------------------------------------------" + EOL);
/* 1934 */         writer.write("//       " + frTitle + "    -    " + enTitle + EOL);
/* 1935 */         writer.write("//--------------------------------------------------------------------------------------------" + EOL + EOL);
/*      */         
/* 1937 */         for (int j = 0; j < ((List)configPages.get(i)).size(); j++)
/*      */         {
/* 1939 */           MillConfig config = (MillConfig)((List)configPages.get(i)).get(j);
/*      */           
/* 1941 */           mainLanguage = fr;
/* 1942 */           writer.write("//" + config.getLabel() + "; " + config.getDesc() + EOL);
/* 1943 */           mainLanguage = en;
/* 1944 */           writer.write("//" + config.getLabel() + "; " + config.getDesc() + EOL);
/* 1945 */           writer.write(config.key + "=" + config.getDefaultValue() + EOL + EOL);
/*      */         }
/*      */       }
/*      */       
/*      */ 
/* 1950 */       mainLanguage = main;
/*      */       
/* 1952 */       writer.close();
/*      */     } catch (Exception e) {
/* 1954 */       printException("Exception in writeBaseConfigFile:", e);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public static void writeConfigFile()
/*      */   {
/* 1961 */     File file = Mill.proxy.getCustomConfigFile();
/*      */     
/*      */     try
/*      */     {
/* 1965 */       BufferedReader reader = MillCommonUtilities.getReader(file);
/*      */       
/*      */ 
/*      */ 
/* 1969 */       List<String> toWrite = new ArrayList();
/*      */       
/* 1971 */       HashSet<MillConfig> configsWritten = new HashSet();
/*      */       String line;
/* 1973 */       while ((line = reader.readLine()) != null)
/*      */       {
/* 1975 */         boolean handled = false;
/*      */         
/* 1977 */         if ((line.trim().length() > 0) && (!line.startsWith("//"))) {
/* 1978 */           String[] temp = line.split("=");
/* 1979 */           String key = temp[0].trim().toLowerCase();
/* 1980 */           String value = "";
/*      */           
/* 1982 */           if (temp.length > 1) {
/* 1983 */             value = temp[1];
/*      */           }
/*      */           
/* 1986 */           if (configs.containsKey(key))
/*      */           {
/* 1988 */             if (((MillConfig)configs.get(key)).compareValuesFromString(value))
/*      */             {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1995 */               configsWritten.add(configs.get(key));
/*      */             } else {
/* 1997 */               toWrite.add(key + "=" + ((MillConfig)configs.get(key)).getSaveValue());
/* 1998 */               configsWritten.add(configs.get(key));
/* 1999 */               handled = true;
/*      */             }
/*      */           }
/*      */         }
/*      */         
/* 2004 */         if (!handled) {
/* 2005 */           toWrite.add(line);
/*      */         }
/*      */       }
/*      */       
/* 2009 */       reader.close();
/*      */       
/* 2011 */       BufferedWriter writer = MillCommonUtilities.getWriter(file);
/*      */       
/* 2013 */       for (String s : toWrite) {
/* 2014 */         writer.write(s + EOL);
/*      */       }
/*      */       
/* 2017 */       for (MillConfig config : configs.values())
/*      */       {
/* 2019 */         if (!configsWritten.contains(config))
/*      */         {
/* 2021 */           if (!config.hasDefaultValue()) {
/* 2022 */             writer.write("//" + config.getLabel() + "; " + config.getDesc() + EOL);
/* 2023 */             writer.write(config.key + "=" + config.getSaveValue() + EOL + EOL);
/*      */           }
/*      */         }
/*      */       }
/*      */       
/* 2028 */       writer.close();
/*      */     }
/*      */     catch (Exception e) {
/* 2031 */       printException("Exception in writeConfigFile:", e);
/*      */     }
/*      */   }
/*      */   
/*      */   private static void writeText(String s) {
/* 2036 */     if (console) {
/* 2037 */       cpw.mods.fml.common.FMLLog.info(Mill.proxy.logPrefix() + removeAccent(s), new Object[0]);
/*      */     }
/*      */     
/* 2040 */     if (writer != null) {
/*      */       try {
/* 2042 */         writer.write("6.0.0 " + now() + " " + s + EOL);
/* 2043 */         writer.flush();
/*      */       } catch (IOException e) {
/* 2045 */         System.out.println("Failed to write line to log file.");
/*      */       }
/*      */     }
/*      */   }
/*      */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\MLN.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */