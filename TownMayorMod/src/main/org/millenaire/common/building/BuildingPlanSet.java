/*     */ package org.millenaire.common.building;
/*     */ 
/*     */ import io.netty.buffer.ByteBufInputStream;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.DataOutput;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import org.millenaire.common.Culture;
/*     */ import org.millenaire.common.MLN;
/*     */ import org.millenaire.common.MLN.MillenaireException;
/*     */ import org.millenaire.common.MillWorld;
/*     */ import org.millenaire.common.Point;
/*     */ import org.millenaire.common.VillageType;
/*     */ import org.millenaire.common.core.MillCommonUtilities;
/*     */ import org.millenaire.common.network.StreamReadWrite;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BuildingPlanSet
/*     */ {
/*     */   public File dir;
/*     */   public String key;
/*     */   public int max;
/*  29 */   public List<BuildingPlan[]> plans = new ArrayList();
/*     */   public Culture culture;
/*     */   
/*     */   public BuildingPlanSet(Culture c, String key, File dir) {
/*  33 */     this.culture = c;
/*  34 */     this.key = key;
/*  35 */     this.dir = dir;
/*     */   }
/*     */   
/*     */   public List<BuildingPlan.LocationBuildingPair> buildLocation(MillWorld mw, VillageType villageType, BuildingLocation location, boolean worldGeneration, boolean townHall, Point townHallPos, boolean wandimport, EntityPlayer owner)
/*     */   {
/*  40 */     return ((BuildingPlan[])this.plans.get(location.getVariation()))[location.level].build(mw, villageType, location, worldGeneration, townHall, townHallPos, wandimport, owner, false);
/*     */   }
/*     */   
/*     */   public BuildingProject getBuildingProject() {
/*  44 */     return new BuildingProject(this);
/*     */   }
/*     */   
/*     */   public PointType[][][] getConsolidatedPlan(int variation, int level)
/*     */   {
/*  49 */     int minLevel = getMinLevel(variation, level);
/*  50 */     int maxLevel = getMaxLevel(variation, level);
/*     */     
/*  52 */     int length = ((BuildingPlan[])this.plans.get(variation))[0].plan[0].length;
/*  53 */     int width = ((BuildingPlan[])this.plans.get(variation))[0].plan[0][0].length;
/*     */     
/*  55 */     PointType[][][] consolidatedPlan = new PointType[maxLevel - minLevel][length][width];
/*     */     
/*  57 */     for (int lid = 0; lid <= level; lid++) {
/*  58 */       BuildingPlan plan = ((BuildingPlan[])this.plans.get(variation))[lid];
/*     */       
/*  60 */       if (MLN.LogBuildingPlan >= 1) {
/*  61 */         MLN.major(this, "Consolidating plan: adding level " + lid);
/*     */       }
/*     */       
/*  64 */       int ioffset = plan.startLevel - minLevel;
/*     */       
/*  66 */       for (int i = 0; i < plan.plan.length; i++) {
/*  67 */         for (int j = 0; j < length; j++) {
/*  68 */           for (int k = 0; k < width; k++) {
/*  69 */             PointType pt = plan.plan[i][j][k];
/*     */             
/*  71 */             if ((!pt.isType("empty")) || (lid == 0))
/*     */             {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  77 */               consolidatedPlan[(i + ioffset)][j][k] = pt;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*  83 */     return consolidatedPlan;
/*     */   }
/*     */   
/*     */   public BuildingPlan getFirstStartingPlan() {
/*  87 */     if (this.plans.size() == 0) {
/*  88 */       return null;
/*     */     }
/*  90 */     return ((BuildingPlan[])this.plans.get(0))[0];
/*     */   }
/*     */   
/*     */   public String getFullName(EntityPlayer player) {
/*  94 */     BuildingPlan plan = getRandomStartingPlan();
/*     */     
/*  96 */     return plan.getFullDisplayName();
/*     */   }
/*     */   
/*     */   public String getGameName() {
/* 100 */     BuildingPlan plan = getRandomStartingPlan();
/*     */     
/* 102 */     return plan.getGameName();
/*     */   }
/*     */   
/*     */   public int getMaxLevel(int variation, int level)
/*     */   {
/* 107 */     int maxLevel = Integer.MIN_VALUE;
/*     */     
/* 109 */     for (int i = 0; i <= level; i++) {
/* 110 */       BuildingPlan plan = ((BuildingPlan[])this.plans.get(variation))[i];
/* 111 */       if (plan.plan.length + plan.startLevel > maxLevel) {
/* 112 */         maxLevel = plan.plan.length + plan.startLevel;
/*     */       }
/*     */     }
/*     */     
/* 116 */     return maxLevel;
/*     */   }
/*     */   
/*     */   public int getMinLevel(int variation, int level)
/*     */   {
/* 121 */     int minLevel = Integer.MAX_VALUE;
/*     */     
/* 123 */     for (int i = 0; i <= level; i++) {
/* 124 */       BuildingPlan plan = ((BuildingPlan[])this.plans.get(variation))[i];
/* 125 */       if (plan.startLevel < minLevel) {
/* 126 */         minLevel = plan.startLevel;
/*     */       }
/*     */     }
/*     */     
/* 130 */     return minLevel;
/*     */   }
/*     */   
/*     */   public String getNativeName() {
/* 134 */     if (this.plans.size() == 0) {
/* 135 */       return this.key;
/*     */     }
/* 137 */     BuildingPlan plan = getRandomStartingPlan();
/* 138 */     return plan.nativeName;
/*     */   }
/*     */   
/*     */   public BuildingPlan getRandomStartingPlan() {
/* 142 */     if (this.plans.size() == 0) {
/* 143 */       return null;
/*     */     }
/* 145 */     return ((BuildingPlan[])this.plans.get(MillCommonUtilities.randomInt(this.plans.size())))[0];
/*     */   }
/*     */   
/*     */   public void loadPictPlans(boolean importPlan) throws Exception
/*     */   {
/* 150 */     List<List<BuildingPlan>> vplans = new ArrayList();
/*     */     
/* 152 */     BuildingPlan prevPlan = null;
/*     */     
/* 154 */     char varChar = 'A';
/* 155 */     int variation = 0;
/*     */     
/* 157 */     while (new File(this.dir, this.key + "_" + varChar + ".txt").exists()) {
/* 158 */       vplans.add(new ArrayList());
/* 159 */       int level = 0;
/* 160 */       prevPlan = null;
/*     */       
/* 162 */       BufferedReader reader = MillCommonUtilities.getReader(new File(this.dir, this.key + "_" + varChar + ".txt"));
/*     */       
/* 164 */       while (new File(this.dir, this.key + "_" + varChar + level + ".png").exists()) {
/* 165 */         String line = reader.readLine();
/* 166 */         prevPlan = new BuildingPlan(this.dir, this.key, level, variation, prevPlan, line, this.culture, importPlan);
/* 167 */         ((List)vplans.get(variation)).add(prevPlan);
/* 168 */         level++;
/*     */       }
/* 170 */       if (((List)vplans.get(variation)).size() == 0) {
/* 171 */         throw new MLN.MillenaireException("No file found for building " + this.key + varChar);
/*     */       }
/* 173 */       varChar = (char)(varChar + '\001');
/* 174 */       variation++;
/*     */     }
/*     */     
/* 177 */     this.max = ((BuildingPlan)((List)vplans.get(0)).get(0)).max;
/*     */     
/* 179 */     for (List<BuildingPlan> varPlans : vplans) {
/* 180 */       int length = ((BuildingPlan)varPlans.get(0)).length;
/* 181 */       int width = ((BuildingPlan)varPlans.get(0)).width;
/*     */       
/* 183 */       for (BuildingPlan plan : varPlans) {
/* 184 */         if (plan.width != width) {
/* 185 */           throw new MLN.MillenaireException("Width of upgrade " + plan.level + " of building " + plan.buildingKey + " is " + plan.width + " instead of " + width);
/*     */         }
/*     */         
/* 188 */         if (plan.length != length) {
/* 189 */           throw new MLN.MillenaireException("Length of upgrade " + plan.level + " of building " + plan.buildingKey + " is " + plan.length + " instead of " + length);
/*     */         }
/*     */       }
/*     */       
/* 193 */       BuildingPlan[] varplansarray = new BuildingPlan[varPlans.size()];
/*     */       
/* 195 */       for (int i = 0; i < varPlans.size(); i++) {
/* 196 */         varplansarray[i] = ((BuildingPlan)varPlans.get(i));
/*     */       }
/*     */       
/* 199 */       this.plans.add(varplansarray);
/*     */     }
/*     */   }
/*     */   
/*     */   public void loadPlans(Culture culture, boolean importPlan) throws Exception
/*     */   {
/* 205 */     List<List<BuildingPlan>> vplans = new ArrayList();
/*     */     
/* 207 */     BuildingPlan prevPlan = null;
/*     */     
/* 209 */     char varChar = 'A';
/* 210 */     int variation = 0;
/*     */     
/* 212 */     while (new File(this.dir, this.key + "_" + varChar + "0.txt").exists()) {
/* 213 */       vplans.add(new ArrayList());
/* 214 */       int level = 0;
/* 215 */       prevPlan = null;
/* 216 */       while (new File(this.dir, this.key + "_" + varChar + level + ".txt").exists()) {
/* 217 */         prevPlan = new BuildingPlan(this.dir, this.key, level, variation, prevPlan, culture, importPlan);
/* 218 */         ((List)vplans.get(variation)).add(prevPlan);
/* 219 */         level++;
/*     */       }
/*     */       
/* 222 */       if (((List)vplans.get(variation)).size() == 0) {
/* 223 */         throw new MLN.MillenaireException("No file found for building " + this.key + varChar);
/*     */       }
/* 225 */       varChar = (char)(varChar + '\001');
/* 226 */       variation++;
/*     */     }
/*     */     
/* 229 */     this.max = ((BuildingPlan)((List)vplans.get(0)).get(0)).max;
/*     */     
/* 231 */     for (List<BuildingPlan> varPlans : vplans) {
/* 232 */       int length = ((BuildingPlan)varPlans.get(0)).length;
/* 233 */       int width = ((BuildingPlan)varPlans.get(0)).width;
/*     */       
/* 235 */       for (BuildingPlan plan : varPlans) {
/* 236 */         if (plan.width != width) {
/* 237 */           throw new MLN.MillenaireException("Width of upgrade " + plan.level + " of building " + plan.buildingKey + " is " + plan.width + " instead of " + width);
/*     */         }
/*     */         
/* 240 */         if (plan.length != length) {
/* 241 */           throw new MLN.MillenaireException("Length of upgrade " + plan.level + " of building " + plan.buildingKey + " is " + plan.length + " instead of " + length);
/*     */         }
/*     */       }
/*     */       
/* 245 */       BuildingPlan[] varplansarray = new BuildingPlan[varPlans.size()];
/*     */       
/* 247 */       for (int i = 0; i < varPlans.size(); i++) {
/* 248 */         varplansarray[i] = ((BuildingPlan)varPlans.get(i));
/*     */       }
/*     */       
/* 251 */       this.plans.add(varplansarray);
/*     */     }
/*     */   }
/*     */   
/*     */   public void readBuildingPlanSetInfoPacket(ByteBufInputStream ds) throws IOException
/*     */   {
/* 257 */     int nb = ds.readInt();
/*     */     
/* 259 */     this.plans.clear();
/*     */     
/* 261 */     for (int i = 0; i < nb; i++) {
/* 262 */       int nb2 = ds.readInt();
/*     */       
/* 264 */       BuildingPlan[] plans = new BuildingPlan[nb2];
/*     */       
/* 266 */       for (int j = 0; j < nb2; j++) {
/* 267 */         plans[j] = StreamReadWrite.readBuildingPlanInfo(ds, this.culture);
/*     */       }
/*     */       
/* 270 */       this.plans.add(plans);
/*     */     }
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 276 */     return this.key + " (" + this.plans.size() + " / " + this.max + "/" + ((BuildingPlan[])this.plans.get(0))[0].nativeName + ")";
/*     */   }
/*     */   
/*     */   public void writeBuildingPlanSetInfo(DataOutput data) throws IOException {
/* 280 */     data.writeUTF(this.key);
/* 281 */     data.writeInt(this.plans.size());
/*     */     
/* 283 */     for (BuildingPlan[] plans : this.plans) {
/* 284 */       data.writeInt(plans.length);
/* 285 */       for (BuildingPlan plan : plans) {
/* 286 */         StreamReadWrite.writeBuildingPlanInfo(plan, data);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\building\BuildingPlanSet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */