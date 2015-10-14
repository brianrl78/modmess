/*     */ package org.millenaire.client;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.gui.FontRenderer;
/*     */ import net.minecraft.client.model.ModelBase;
/*     */ import net.minecraft.client.model.ModelBiped;
/*     */ import net.minecraft.client.renderer.Tessellator;
/*     */ import net.minecraft.client.renderer.entity.RenderBiped;
/*     */ import net.minecraft.client.renderer.entity.RenderManager;
/*     */ import net.minecraft.entity.EntityLiving;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ import org.millenaire.common.MLN;
/*     */ import org.millenaire.common.MillVillager;
/*     */ import org.millenaire.common.MillWorld;
/*     */ import org.millenaire.common.Point;
/*     */ import org.millenaire.common.Quest.QuestInstance;
/*     */ import org.millenaire.common.UserProfile;
/*     */ import org.millenaire.common.forge.CommonProxy;
/*     */ import org.millenaire.common.forge.Mill;
/*     */ import org.millenaire.common.goal.Goal;
/*     */ import org.millenaire.common.pathing.atomicstryker.AS_PathEntity;
/*     */ import org.millenaire.common.pathing.atomicstryker.AStarPathPlanner;
/*     */ 
/*     */ public class RenderMillVillager extends RenderBiped
/*     */ {
/*     */   private static final float SCALE = 0.01666667F;
/*     */   private static final float LINE_HEIGHT = 0.2F;
/*     */   private static final int LINE_SIZE = 60;
/*     */   private final ModelBiped modelCloth;
/*     */   
/*     */   public RenderMillVillager(ModelBiped modelbiped, float f)
/*     */   {
/*  36 */     super(modelbiped, f);
/*  37 */     this.field_77071_a = ((ModelBiped)this.field_77045_g);
/*  38 */     if ((modelbiped instanceof ModelFemaleAsymmetrical)) {
/*  39 */       this.modelCloth = new ModelFemaleAsymmetrical(0.1F);
/*  40 */     } else if ((modelbiped instanceof ModelFemaleSymmetrical)) {
/*  41 */       this.modelCloth = new ModelFemaleSymmetrical(0.1F);
/*     */     } else {
/*  43 */       this.modelCloth = new ModelBiped(0.1F);
/*     */     }
/*     */   }
/*     */   
/*     */   private void displayText(String text, float scale, int colour, float x, float y, float z) {
/*  48 */     FontRenderer fontrenderer = func_76983_a();
/*  49 */     GL11.glPushMatrix();
/*  50 */     GL11.glTranslatef(x, y, z);
/*  51 */     GL11.glNormal3f(0.0F, 1.0F, 0.0F);
/*  52 */     GL11.glRotatef(-this.field_76990_c.field_78735_i, 0.0F, 1.0F, 0.0F);
/*  53 */     GL11.glRotatef(this.field_76990_c.field_78732_j, 1.0F, 0.0F, 0.0F);
/*  54 */     GL11.glScalef(-scale, -scale, scale);
/*  55 */     GL11.glDisable(2896);
/*  56 */     GL11.glDepthMask(false);
/*  57 */     GL11.glDisable(2929);
/*  58 */     GL11.glEnable(3042);
/*  59 */     GL11.glBlendFunc(770, 771);
/*  60 */     Tessellator tessellator = Tessellator.field_78398_a;
/*  61 */     GL11.glDisable(3553);
/*  62 */     tessellator.func_78382_b();
/*  63 */     int textWidth = fontrenderer.func_78256_a(text) / 2;
/*  64 */     tessellator.func_78369_a(0.0F, 0.0F, 0.0F, 0.8F);
/*  65 */     tessellator.func_78377_a(-textWidth - 1, -1.0D, 0.0D);
/*  66 */     tessellator.func_78377_a(-textWidth - 1, 8.0D, 0.0D);
/*  67 */     tessellator.func_78377_a(textWidth + 1, 8.0D, 0.0D);
/*  68 */     tessellator.func_78377_a(textWidth + 1, -1.0D, 0.0D);
/*  69 */     tessellator.func_78381_a();
/*  70 */     GL11.glEnable(3553);
/*  71 */     fontrenderer.func_78276_b(text, -fontrenderer.func_78256_a(text) / 2, 0, colour);
/*  72 */     GL11.glEnable(2929);
/*  73 */     GL11.glDepthMask(true);
/*     */     
/*     */ 
/*  76 */     GL11.glEnable(2896);
/*  77 */     GL11.glDisable(3042);
/*  78 */     GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
/*  79 */     GL11.glPopMatrix();
/*     */   }
/*     */   
/*     */ 
/*     */   public void func_76986_a(net.minecraft.entity.Entity entity, double d, double d1, double d2, float f, float f1)
/*     */   {
/*  85 */     MillVillager villager = (MillVillager)entity;
/*     */     
/*  87 */     if (villager.isUsingBow) {
/*  88 */       this.modelCloth.field_78118_o = (this.field_77071_a.field_78118_o = 1);
/*     */     }
/*     */     
/*  91 */     super.func_76986_a(entity, d, d1, d2, f, f1);
/*     */     
/*  93 */     this.modelCloth.field_78118_o = (this.field_77071_a.field_78118_o = 0);
/*     */     
/*  95 */     doRenderVillagerName(villager, d, d1, d2);
/*     */   }
/*     */   
/*     */   public void doRenderVillagerName(MillVillager villager, double x, double y, double z)
/*     */   {
/* 100 */     float villagerSize = villager.scale * 2.0F;
/*     */     
/* 102 */     if (villager.shouldLieDown)
/*     */     {
/* 104 */       double height = villager.field_70121_D.field_72337_e - villager.field_70121_D.field_72338_b;
/*     */       
/* 106 */       float angle = villager.getBedOrientationInDegrees();
/*     */       
/* 108 */       double dx = 0.0D;double dz = 0.0D;
/*     */       
/* 110 */       if (angle == 0.0F) {
/* 111 */         dx = -height * 0.9D;
/* 112 */       } else if (angle == 90.0F) {
/* 113 */         dz = -height * 0.9D;
/* 114 */       } else if (angle == 180.0F) {
/* 115 */         dx = height * 0.9D;
/* 116 */       } else if (angle == 270.0F) {
/* 117 */         dz = height * 0.9D;
/*     */       }
/*     */       
/* 120 */       x = villager.field_70142_S - RenderManager.field_78725_b + dx;
/* 121 */       z = villager.field_70136_U - RenderManager.field_78723_d + dz;
/*     */       
/* 123 */       villagerSize = 0.5F;
/*     */     }
/*     */     
/* 126 */     UserProfile profile = Mill.clientWorld.getProfile(Mill.proxy.getTheSinglePlayer().getDisplayName());
/*     */     
/* 128 */     float f4 = villager.func_70032_d(this.field_76990_c.field_78734_h);
/*     */     
/* 130 */     if (f4 < MLN.VillagersNamesDistance) {
/* 131 */       String gameSpeech = villager.getGameSpeech(Mill.proxy.getTheSinglePlayer().getDisplayName());
/* 132 */       String nativeSpeech = villager.getNativeSpeech(Mill.proxy.getTheSinglePlayer().getDisplayName());
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 138 */       float height = 0.2F;
/*     */       
/* 140 */       if ((MLN.DEV) && (Mill.serverWorlds.size() > 0) && (((MillWorld)Mill.serverWorlds.get(0)).villagers.containsKey(Long.valueOf(villager.villager_id))) && (!MLN.DEV))
/*     */       {
/* 142 */         MillVillager dv = (MillVillager)((MillWorld)Mill.serverWorlds.get(0)).villagers.get(Long.valueOf(villager.villager_id));
/*     */         
/* 144 */         AS_PathEntity pe = dv.pathEntity;
/*     */         
/* 146 */         if ((pe != null) && (pe.pointsCopy != null)) {
/* 147 */           net.minecraft.pathfinding.PathPoint[] pp = pe.pointsCopy;
/*     */           
/* 149 */           if ((pp != null) && 
/* 150 */             (pp.length > 0))
/*     */           {
/* 152 */             String s = "";
/* 153 */             for (int i = pe.func_75873_e(); (i < pp.length) && (i < pe.func_75873_e() + 5); i++) {
/* 154 */               s = s + "(" + pp[i] + ") ";
/*     */             }
/* 156 */             displayText(s, 0.01666667F, -1593835521, (float)x, (float)y + villagerSize + height, (float)z);
/* 157 */             height += 0.2F;
/*     */           }
/*     */           
/*     */ 
/* 161 */           if (pe != null) {
/* 162 */             if (pe.func_75874_d() > 0)
/*     */             {
/* 164 */               displayText("Path: " + pe.func_75874_d() + " end: " + pe.getCurrentTargetPathPoint() + " dist: " + Math.round(villager.getPos().horizontalDistanceTo(pe.func_75870_c()) * 10.0D) / 10L + " index: " + pe.func_75873_e() + " " + dv.func_70781_l() + " PF: " + dv.pathfailure + ", stuck: " + dv.longDistanceStuck, 0.01666667F, -1593835521, (float)x, (float)y + villagerSize + height, (float)z);
/*     */ 
/*     */             }
/*     */             else
/*     */             {
/* 169 */               displayText("Empty path PF: " + dv.pathfailure + ", stuck: " + dv.longDistanceStuck, 0.01666667F, -1593835521, (float)x, (float)y + villagerSize + height, (float)z);
/*     */             }
/* 171 */             height += 0.2F;
/*     */           }
/*     */         }
/*     */         else {
/* 175 */           displayText("Null path entity, PF: " + dv.pathfailure + ", stuck: " + dv.longDistanceStuck, 0.01666667F, -1593835521, (float)x, (float)y + villagerSize + height, (float)z);
/* 176 */           height += 0.2F;
/*     */         }
/* 178 */         if (dv.func_70777_m() == null) {
/* 179 */           displayText("Pos: " + dv.getPos() + " Path dest: " + dv.getPathDestPoint() + " Goal dest: " + dv.getGoalDestPoint() + " dist: " + Math.round(dv.getPos().horizontalDistanceTo(dv.getPathDestPoint()) * 10.0D) / 10L + " sm: " + dv.stopMoving + " jps busy: " + dv.jpsPathPlanner.isBusy(), 0.01666667F, -1593835521, (float)x, (float)y + villagerSize + height, (float)z);
/*     */ 
/*     */         }
/*     */         else
/*     */         {
/* 184 */           displayText("Pos: " + dv.getPos() + " Entity: " + dv.func_70777_m() + " dest: " + new Point(dv.func_70777_m()) + " dist: " + Math.round(dv.getPos().horizontalDistanceTo(new Point(dv.func_70777_m())) * 10.0D) / 10L + " sm: " + dv.stopMoving + " jps busy: " + dv.jpsPathPlanner.isBusy(), 0.01666667F, -1593835521, (float)x, (float)y + villagerSize + height, (float)z);
/*     */         }
/*     */         
/*     */ 
/*     */ 
/*     */ 
/* 190 */         height += 0.2F;
/*     */       }
/*     */       
/* 193 */       if (villager.hiredBy == null) {
/* 194 */         if (gameSpeech != null) {
/* 195 */           List<String> lines = new java.util.ArrayList();
/* 196 */           String line = gameSpeech;
/* 197 */           while (line.length() > 60) {
/* 198 */             String subLine = line.substring(0, line.lastIndexOf(' ', 60));
/* 199 */             line = line.substring(subLine.length()).trim();
/* 200 */             lines.add(subLine);
/*     */           }
/* 202 */           lines.add(line);
/*     */           
/* 204 */           for (int i = lines.size() - 1; i >= 0; i--) {
/* 205 */             displayText((String)lines.get(i), 0.01666667F, -1596166533, (float)x, (float)y + villagerSize + height, (float)z);
/* 206 */             height += 0.2F;
/*     */           }
/*     */         }
/* 209 */         if (nativeSpeech != null) {
/* 210 */           List<String> lines = new java.util.ArrayList();
/* 211 */           String line = nativeSpeech;
/* 212 */           while (line.length() > 60) {
/* 213 */             String subLine = line.substring(0, line.lastIndexOf(' ', 60));
/* 214 */             line = line.substring(subLine.length()).trim();
/* 215 */             lines.add(subLine);
/*     */           }
/* 217 */           lines.add(line);
/*     */           
/* 219 */           for (int i = lines.size() - 1; i >= 0; i--) {
/* 220 */             displayText((String)lines.get(i), 0.01666667F, -1603244324, (float)x, (float)y + villagerSize + height, (float)z);
/* 221 */             height += 0.2F;
/*     */           }
/*     */         }
/*     */         
/* 225 */         if ((MLN.displayNames) && (Goal.goals.containsKey(villager.goalKey))) {
/* 226 */           displayText(((Goal)Goal.goals.get(villager.goalKey)).gameName(villager), 0.01666667F, -1596142994, (float)x, (float)y + villagerSize + height, (float)z);
/* 227 */           height += 0.2F;
/*     */         }
/*     */         
/* 230 */         if (profile.villagersInQuests.containsKey(Long.valueOf(villager.villager_id))) {
/* 231 */           Quest.QuestInstance qi = (Quest.QuestInstance)profile.villagersInQuests.get(Long.valueOf(villager.villager_id));
/* 232 */           if (qi.getCurrentVillager().id == villager.villager_id) {
/* 233 */             displayText("[" + qi.getLabel(profile) + "]", 0.01666667F, -1596072483, (float)x, (float)y + villagerSize + height, (float)z);
/* 234 */             height += 0.2F;
/*     */           }
/*     */         }
/*     */         
/* 238 */         if (villager.isRaider) {
/* 239 */           displayText(MLN.string("ui.raider"), 0.01666667F, -1593872773, (float)x, (float)y + villagerSize + height, (float)z);
/* 240 */           height += 0.2F;
/*     */         }
/*     */         
/* 243 */         if (villager.vtype.showHealth) {
/* 244 */           displayText(MLN.string("hire.health") + ": " + villager.func_110143_aJ() * 0.5D + "/" + villager.func_110138_aP() * 0.5D, 0.01666667F, -1596072483, (float)x, (float)y + villagerSize + height, (float)z);
/*     */           
/* 246 */           height += 0.2F;
/*     */         }
/*     */       }
/* 249 */       else if (villager.hiredBy.equals(profile.playerName))
/*     */       {
/* 251 */         String s = MLN.string("hire.health") + ": " + villager.func_110143_aJ() * 0.5D + "/" + villager.func_110138_aP() * 0.5D;
/*     */         
/* 253 */         if (villager.aggressiveStance) {
/* 254 */           s = s + " - " + MLN.string("hire.aggressive");
/*     */         } else {
/* 256 */           s = s + " - " + MLN.string("hire.passive");
/*     */         }
/*     */         
/* 259 */         displayText(s, 0.01666667F, -1596142994, (float)x, (float)y + villagerSize + height, (float)z);
/* 260 */         height += 0.2F;
/*     */         
/* 262 */         s = MLN.string("hire.timeleft", new String[] { "" + Math.round((float)((villager.hiredUntil - villager.field_70170_p.func_72820_D()) / 1000L)) });
/*     */         
/* 264 */         displayText(s, 0.01666667F, -1596072483, (float)x, (float)y + villagerSize + height, (float)z);
/* 265 */         height += 0.2F;
/*     */       } else {
/* 267 */         String s = MLN.string("hire.hiredby", new String[] { villager.hiredBy });
/*     */         
/* 269 */         displayText(s, 0.01666667F, -1596072483, (float)x, (float)y + villagerSize + height, (float)z);
/* 270 */         height += 0.2F;
/*     */       }
/*     */       
/* 273 */       if ((MLN.displayNames) && (!villager.vtype.hideName)) {
/* 274 */         displayText(villager.getName() + ", " + villager.getNativeOccupationName(), 0.01666667F, -1593835521, (float)x, (float)y + villagerSize + height, (float)z);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   protected net.minecraft.util.ResourceLocation func_110775_a(EntityLiving par1EntityLiving)
/*     */   {
/* 282 */     MillVillager villager = (MillVillager)par1EntityLiving;
/*     */     
/* 284 */     return villager.texture;
/*     */   }
/*     */   
/*     */   protected void func_77041_b(EntityLivingBase entityliving, float f)
/*     */   {
/* 289 */     preRenderScale((MillVillager)entityliving, f);
/*     */   }
/*     */   
/*     */   protected void preRenderScale(MillVillager villager, float f) {
/* 293 */     GL11.glScalef(villager.scale, villager.scale, villager.scale);
/*     */   }
/*     */   
/*     */ 
/*     */   protected void func_77043_a(EntityLivingBase par1EntityLiving, float par2, float par3, float par4)
/*     */   {
/* 299 */     MillVillager v = (MillVillager)par1EntityLiving;
/*     */     
/* 301 */     if ((v.func_70089_S()) && (v.isVillagerSleeping())) {
/* 302 */       float orientation = -v.getBedOrientationInDegrees();
/* 303 */       GL11.glRotatef(orientation, 0.0F, 1.0F, 0.0F);
/* 304 */       GL11.glRotatef(func_77037_a(v), 0.0F, 0.0F, 1.0F);
/* 305 */       GL11.glRotatef(270.0F, 0.0F, 1.0F, 0.0F);
/*     */     } else {
/* 307 */       super.func_77043_a(v, par2, par3, par4);
/*     */     }
/*     */   }
/*     */   
/*     */   protected int setClothModel(MillVillager villager, int clothPartID, float f)
/*     */   {
/*     */     try {
/* 314 */       net.minecraft.util.ResourceLocation clothTexture = villager.getClothTexturePath();
/*     */       
/* 316 */       if (clothTexture == null) {
/* 317 */         this.modelCloth.field_78116_c.field_78806_j = false;
/* 318 */         this.modelCloth.field_78114_d.field_78806_j = false;
/* 319 */         this.modelCloth.field_78115_e.field_78806_j = false;
/* 320 */         this.modelCloth.field_78112_f.field_78806_j = false;
/* 321 */         this.modelCloth.field_78113_g.field_78806_j = false;
/* 322 */         this.modelCloth.field_78123_h.field_78806_j = false;
/* 323 */         this.modelCloth.field_78124_i.field_78806_j = false;
/* 324 */         return -1;
/*     */       }
/*     */       
/* 327 */       func_110776_a(clothTexture);
/* 328 */       this.modelCloth.field_78116_c.field_78806_j = true;
/* 329 */       this.modelCloth.field_78114_d.field_78806_j = true;
/* 330 */       this.modelCloth.field_78115_e.field_78806_j = true;
/* 331 */       this.modelCloth.field_78112_f.field_78806_j = true;
/* 332 */       this.modelCloth.field_78113_g.field_78806_j = true;
/* 333 */       this.modelCloth.field_78123_h.field_78806_j = true;
/* 334 */       this.modelCloth.field_78124_i.field_78806_j = true;
/* 335 */       func_77042_a(this.modelCloth);
/*     */       
/* 337 */       this.modelCloth.field_78095_p = this.field_77045_g.field_78095_p;
/* 338 */       this.modelCloth.field_78093_q = this.field_77045_g.field_78093_q;
/* 339 */       this.modelCloth.field_78091_s = this.field_77045_g.field_78091_s;
/* 340 */       float f1 = 1.0F;
/*     */       
/* 342 */       GL11.glColor3f(1.0F, 1.0F, 1.0F);
/*     */       
/* 344 */       return 1;
/*     */     }
/*     */     catch (Exception e) {
/* 347 */       MLN.printException("Error when loading cloth: ", e);
/*     */     }
/*     */     
/* 350 */     return -1;
/*     */   }
/*     */   
/*     */ 
/*     */   protected int func_77032_a(EntityLivingBase entityliving, int i, float f)
/*     */   {
/* 356 */     int armourRes = func_77032_a((EntityLiving)entityliving, i, f);
/* 357 */     int clothRes = -1;
/*     */     
/* 359 */     if (i == 0) {
/* 360 */       clothRes = setClothModel((MillVillager)entityliving, i, f);
/*     */     }
/*     */     
/* 363 */     if (armourRes > 0) {
/* 364 */       return armourRes;
/*     */     }
/*     */     
/* 367 */     return clothRes;
/*     */   }
/*     */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\client\RenderMillVillager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */