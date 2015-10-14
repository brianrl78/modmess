/*     */ package org.millenaire.client;
/*     */ 
/*     */ import cpw.mods.fml.relauncher.Side;
/*     */ import cpw.mods.fml.relauncher.SideOnly;
/*     */ import net.minecraft.client.renderer.OpenGlHelper;
/*     */ import net.minecraft.client.renderer.Tessellator;
/*     */ import net.minecraft.client.renderer.entity.Render;
/*     */ import net.minecraft.client.renderer.entity.RenderManager;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import net.minecraft.world.World;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ import org.millenaire.common.EntityMillDecoration;
/*     */ import org.millenaire.common.EntityMillDecoration.EnumWallDecoration;
/*     */ 
/*     */ @SideOnly(Side.CLIENT)
/*     */ public class RenderWallDecoration
/*     */   extends Render
/*     */ {
/*  21 */   public static final ResourceLocation textureTapestries = new ResourceLocation("millenaire", "textures/painting/ML_Tapestry.png");
/*  22 */   public static final ResourceLocation textureSculptures = new ResourceLocation("millenaire", "textures/painting/ML_Scultures.png");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void func_76986_a(Entity par1Entity, double par2, double par4, double par6, float par8, float par9)
/*     */   {
/*  34 */     renderThePainting((EntityMillDecoration)par1Entity, par2, par4, par6, par8, par9);
/*     */   }
/*     */   
/*     */   private void func_77008_a(EntityMillDecoration par1EntityPainting, float par2, float par3) {
/*  38 */     int i = MathHelper.func_76128_c(par1EntityPainting.field_70165_t);
/*  39 */     int j = MathHelper.func_76128_c(par1EntityPainting.field_70163_u + par3 / 16.0F);
/*  40 */     int k = MathHelper.func_76128_c(par1EntityPainting.field_70161_v);
/*     */     
/*  42 */     if (par1EntityPainting.field_82332_a == 2) {
/*  43 */       i = MathHelper.func_76128_c(par1EntityPainting.field_70165_t + par2 / 16.0F);
/*     */     }
/*     */     
/*  46 */     if (par1EntityPainting.field_82332_a == 1) {
/*  47 */       k = MathHelper.func_76128_c(par1EntityPainting.field_70161_v - par2 / 16.0F);
/*     */     }
/*     */     
/*  50 */     if (par1EntityPainting.field_82332_a == 0) {
/*  51 */       i = MathHelper.func_76128_c(par1EntityPainting.field_70165_t - par2 / 16.0F);
/*     */     }
/*     */     
/*  54 */     if (par1EntityPainting.field_82332_a == 3) {
/*  55 */       k = MathHelper.func_76128_c(par1EntityPainting.field_70161_v + par2 / 16.0F);
/*     */     }
/*     */     
/*  58 */     int l = this.field_76990_c.field_78722_g.func_72802_i(i, j, k, 0);
/*  59 */     int i1 = l % 65536;
/*  60 */     int j1 = l / 65536;
/*  61 */     OpenGlHelper.func_77475_a(OpenGlHelper.field_77476_b, i1, j1);
/*  62 */     GL11.glColor3f(1.0F, 1.0F, 1.0F);
/*     */   }
/*     */   
/*     */   private void func_77010_a(EntityMillDecoration par1EntityPainting, int sizeX, int sizeY, int offsetX, int offsetY) {
/*  66 */     float f = -sizeX / 2.0F;
/*  67 */     float f1 = -sizeY / 2.0F;
/*  68 */     float f2 = 0.5F;
/*  69 */     float f3 = 0.75F;
/*  70 */     float f4 = 0.8125F;
/*  71 */     float f5 = 0.0F;
/*  72 */     float f6 = 0.0625F;
/*  73 */     float f7 = 0.75F;
/*  74 */     float f8 = 0.8125F;
/*  75 */     float f9 = 0.001953125F;
/*  76 */     float f10 = 0.001953125F;
/*  77 */     float f11 = 0.7519531F;
/*  78 */     float f12 = 0.7519531F;
/*  79 */     float f13 = 0.0F;
/*  80 */     float f14 = 0.0625F;
/*     */     
/*  82 */     for (int i1 = 0; i1 < sizeX / 16; i1++) {
/*  83 */       for (int j1 = 0; j1 < sizeY / 16; j1++) {
/*  84 */         float f15 = f + (i1 + 1) * 16;
/*  85 */         float f16 = f + i1 * 16;
/*  86 */         float f17 = f1 + (j1 + 1) * 16;
/*  87 */         float f18 = f1 + j1 * 16;
/*  88 */         func_77008_a(par1EntityPainting, (f15 + f16) / 2.0F, (f17 + f18) / 2.0F);
/*  89 */         float f19 = (offsetX + sizeX - i1 * 16) / 256.0F;
/*  90 */         float f20 = (offsetX + sizeX - (i1 + 1) * 16) / 256.0F;
/*  91 */         float f21 = (offsetY + sizeY - j1 * 16) / 256.0F;
/*  92 */         float f22 = (offsetY + sizeY - (j1 + 1) * 16) / 256.0F;
/*  93 */         Tessellator tessellator = Tessellator.field_78398_a;
/*  94 */         tessellator.func_78382_b();
/*  95 */         tessellator.func_78375_b(0.0F, 0.0F, -1.0F);
/*  96 */         tessellator.func_78374_a(f15, f18, -0.5D, f20, f21);
/*  97 */         tessellator.func_78374_a(f16, f18, -0.5D, f19, f21);
/*  98 */         tessellator.func_78374_a(f16, f17, -0.5D, f19, f22);
/*  99 */         tessellator.func_78374_a(f15, f17, -0.5D, f20, f22);
/* 100 */         tessellator.func_78375_b(0.0F, 0.0F, 1.0F);
/* 101 */         tessellator.func_78374_a(f15, f17, 0.5D, 0.75D, 0.0D);
/* 102 */         tessellator.func_78374_a(f16, f17, 0.5D, 0.8125D, 0.0D);
/* 103 */         tessellator.func_78374_a(f16, f18, 0.5D, 0.8125D, 0.0625D);
/* 104 */         tessellator.func_78374_a(f15, f18, 0.5D, 0.75D, 0.0625D);
/* 105 */         tessellator.func_78375_b(0.0F, 1.0F, 0.0F);
/* 106 */         tessellator.func_78374_a(f15, f17, -0.5D, 0.75D, 0.001953125D);
/* 107 */         tessellator.func_78374_a(f16, f17, -0.5D, 0.8125D, 0.001953125D);
/* 108 */         tessellator.func_78374_a(f16, f17, 0.5D, 0.8125D, 0.001953125D);
/* 109 */         tessellator.func_78374_a(f15, f17, 0.5D, 0.75D, 0.001953125D);
/* 110 */         tessellator.func_78375_b(0.0F, -1.0F, 0.0F);
/* 111 */         tessellator.func_78374_a(f15, f18, 0.5D, 0.75D, 0.001953125D);
/* 112 */         tessellator.func_78374_a(f16, f18, 0.5D, 0.8125D, 0.001953125D);
/* 113 */         tessellator.func_78374_a(f16, f18, -0.5D, 0.8125D, 0.001953125D);
/* 114 */         tessellator.func_78374_a(f15, f18, -0.5D, 0.75D, 0.001953125D);
/* 115 */         tessellator.func_78375_b(-1.0F, 0.0F, 0.0F);
/* 116 */         tessellator.func_78374_a(f15, f17, 0.5D, 0.751953125D, 0.0D);
/* 117 */         tessellator.func_78374_a(f15, f18, 0.5D, 0.751953125D, 0.0625D);
/* 118 */         tessellator.func_78374_a(f15, f18, -0.5D, 0.751953125D, 0.0625D);
/* 119 */         tessellator.func_78374_a(f15, f17, -0.5D, 0.751953125D, 0.0D);
/* 120 */         tessellator.func_78375_b(1.0F, 0.0F, 0.0F);
/* 121 */         tessellator.func_78374_a(f16, f17, -0.5D, 0.751953125D, 0.0D);
/* 122 */         tessellator.func_78374_a(f16, f18, -0.5D, 0.751953125D, 0.0625D);
/* 123 */         tessellator.func_78374_a(f16, f18, 0.5D, 0.751953125D, 0.0625D);
/* 124 */         tessellator.func_78374_a(f16, f17, 0.5D, 0.751953125D, 0.0D);
/* 125 */         tessellator.func_78381_a();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected ResourceLocation func_110775_a(Entity entity)
/*     */   {
/* 132 */     EntityMillDecoration ent = (EntityMillDecoration)entity;
/* 133 */     if (ent.type == 1) {
/* 134 */       return textureTapestries;
/*     */     }
/* 136 */     return textureSculptures;
/*     */   }
/*     */   
/*     */   public void renderThePainting(EntityMillDecoration ent, double par2, double par4, double par6, float par8, float par9)
/*     */   {
/* 141 */     GL11.glPushMatrix();
/* 142 */     GL11.glTranslatef((float)par2, (float)par4, (float)par6);
/* 143 */     GL11.glRotatef(par8, 0.0F, 1.0F, 0.0F);
/* 144 */     GL11.glEnable(32826);
/* 145 */     if (ent.type == 1) {
/* 146 */       func_110776_a(textureTapestries);
/*     */     } else {
/* 148 */       func_110776_a(textureSculptures);
/*     */     }
/* 150 */     EntityMillDecoration.EnumWallDecoration enumart = ent.millArt;
/* 151 */     float f2 = 0.0625F;
/* 152 */     GL11.glScalef(0.0625F, 0.0625F, 0.0625F);
/* 153 */     func_77010_a(ent, enumart.sizeX, enumart.sizeY, enumart.offsetX, enumart.offsetY);
/* 154 */     GL11.glDisable(32826);
/* 155 */     GL11.glPopMatrix();
/*     */   }
/*     */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\client\RenderWallDecoration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */