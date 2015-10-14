/*     */ package org.millenaire.client;
/*     */ 
/*     */ import net.minecraft.client.model.ModelRenderer;
/*     */ import net.minecraft.tileentity.TileEntityChest;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ 
/*     */ public class TileEntityMillChestRenderer extends net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer
/*     */ {
/*     */   private final ModelLockedChest field_35377_b;
/*     */   private final ModelLockedChest field_35378_c;
/*     */   
/*     */   public class ModelLargeLockedChest extends TileEntityMillChestRenderer.ModelLockedChest
/*     */   {
/*     */     public ModelLargeLockedChest()
/*     */     {
/*  16 */       super();
/*  17 */       this.chestLid = new ModelRenderer(this, 0, 0).func_78787_b(128, 64);
/*  18 */       this.chestLid.func_78790_a(0.0F, -5.0F, -14.0F, 30, 5, 14, 0.0F);
/*  19 */       this.chestLid.field_78800_c = 1.0F;
/*  20 */       this.chestLid.field_78797_d = 7.0F;
/*  21 */       this.chestLid.field_78798_e = 15.0F;
/*  22 */       this.chestKnob = new ModelRenderer(this, 0, 0).func_78787_b(128, 64);
/*  23 */       this.chestKnob.func_78790_a(-2.0F, -2.0F, -15.0F, 4, 4, 1, 0.0F);
/*  24 */       this.chestKnob.field_78800_c = 16.0F;
/*  25 */       this.chestKnob.field_78797_d = 7.0F;
/*  26 */       this.chestKnob.field_78798_e = 15.0F;
/*  27 */       this.chestBelow = new ModelRenderer(this, 0, 19).func_78787_b(128, 64);
/*  28 */       this.chestBelow.func_78790_a(0.0F, 0.0F, 0.0F, 30, 10, 14, 0.0F);
/*  29 */       this.chestBelow.field_78800_c = 1.0F;
/*  30 */       this.chestBelow.field_78797_d = 6.0F;
/*  31 */       this.chestBelow.field_78798_e = 1.0F;
/*     */     }
/*     */   }
/*     */   
/*     */   private class ModelLockedChest extends net.minecraft.client.model.ModelBase {
/*     */     public ModelRenderer chestLid;
/*     */     public ModelRenderer chestBelow;
/*     */     public ModelRenderer chestKnob;
/*     */     
/*     */     public ModelLockedChest() {
/*  41 */       this.chestLid = new ModelRenderer(this, 0, 0).func_78787_b(64, 64);
/*  42 */       this.chestLid.func_78790_a(0.0F, -5.0F, -14.0F, 14, 5, 14, 0.0F);
/*  43 */       this.chestLid.field_78800_c = 1.0F;
/*  44 */       this.chestLid.field_78797_d = 7.0F;
/*  45 */       this.chestLid.field_78798_e = 15.0F;
/*  46 */       this.chestKnob = new ModelRenderer(this, 0, 0).func_78787_b(64, 64);
/*  47 */       this.chestKnob.func_78790_a(-2.0F, -2.0F, -15.0F, 4, 4, 1, 0.0F);
/*  48 */       this.chestKnob.field_78800_c = 8.0F;
/*  49 */       this.chestKnob.field_78797_d = 7.0F;
/*  50 */       this.chestKnob.field_78798_e = 15.0F;
/*  51 */       this.chestBelow = new ModelRenderer(this, 0, 19).func_78787_b(64, 64);
/*  52 */       this.chestBelow.func_78790_a(0.0F, 0.0F, 0.0F, 14, 10, 14, 0.0F);
/*  53 */       this.chestBelow.field_78800_c = 1.0F;
/*  54 */       this.chestBelow.field_78797_d = 6.0F;
/*  55 */       this.chestBelow.field_78798_e = 1.0F;
/*     */     }
/*     */     
/*     */     public void func_35402_a() {
/*  59 */       this.chestKnob.field_78795_f = this.chestLid.field_78795_f;
/*  60 */       this.chestLid.func_78785_a(0.0625F);
/*  61 */       this.chestKnob.func_78785_a(0.0625F);
/*  62 */       this.chestBelow.func_78785_a(0.0625F);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public TileEntityMillChestRenderer()
/*     */   {
/*  71 */     this.field_35377_b = new ModelLockedChest();
/*  72 */     this.field_35378_c = new ModelLargeLockedChest();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*  77 */   public void func_147500_a(net.minecraft.tileentity.TileEntity tileentity, double d, double d1, double d2, float f) { renderTileEntityChestAt((TileEntityChest)tileentity, d, d1, d2, f); }
/*     */   
/*     */   public void renderTileEntityChestAt(TileEntityChest tileentitychest, double d, double d1, double d2, float f) {
/*     */     int i;
/*     */     int i;
/*  82 */     if (tileentitychest.func_145831_w() == null) {
/*  83 */       i = 0;
/*     */     } else {
/*  85 */       net.minecraft.block.Block block = tileentitychest.func_145838_q();
/*  86 */       i = tileentitychest.func_145832_p();
/*  87 */       if ((block != null) && (i == 0) && ((block instanceof org.millenaire.common.block.BlockMillChest))) {
/*  88 */         ((org.millenaire.common.block.BlockMillChest)block).unifyMillChests(tileentitychest.func_145831_w(), tileentitychest.field_145851_c, tileentitychest.field_145848_d, tileentitychest.field_145849_e);
/*  89 */         i = tileentitychest.func_145832_p();
/*     */       }
/*  91 */       tileentitychest.func_145979_i();
/*     */     }
/*  93 */     if ((tileentitychest.field_145992_i != null) || (tileentitychest.field_145991_k != null)) {
/*     */       return;
/*     */     }
/*     */     ModelLockedChest modelchest;
/*  97 */     if ((tileentitychest.field_145990_j != null) || (tileentitychest.field_145988_l != null)) {
/*  98 */       ModelLockedChest modelchest = this.field_35378_c;
/*  99 */       func_147499_a(org.millenaire.common.MLN.getLargeLockedChestTexture());
/*     */     } else {
/* 101 */       modelchest = this.field_35377_b;
/* 102 */       func_147499_a(org.millenaire.common.MLN.getLockedChestTexture());
/*     */     }
/* 104 */     GL11.glPushMatrix();
/* 105 */     GL11.glEnable(32826);
/* 106 */     GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
/* 107 */     GL11.glTranslatef((float)d, (float)d1 + 1.0F, (float)d2 + 1.0F);
/* 108 */     GL11.glScalef(1.0F, -1.0F, -1.0F);
/* 109 */     GL11.glTranslatef(0.5F, 0.5F, 0.5F);
/* 110 */     int j = 0;
/* 111 */     if (i == 2) {
/* 112 */       j = 180;
/*     */     }
/* 114 */     if (i == 3) {
/* 115 */       j = 0;
/*     */     }
/* 117 */     if (i == 4) {
/* 118 */       j = 90;
/*     */     }
/* 120 */     if (i == 5) {
/* 121 */       j = -90;
/*     */     }
/* 123 */     if ((i == 2) && (tileentitychest.field_145990_j != null)) {
/* 124 */       GL11.glTranslatef(1.0F, 0.0F, 0.0F);
/*     */     }
/* 126 */     if ((i == 5) && (tileentitychest.field_145988_l != null)) {
/* 127 */       GL11.glTranslatef(0.0F, 0.0F, -1.0F);
/*     */     }
/* 129 */     GL11.glRotatef(j, 0.0F, 1.0F, 0.0F);
/* 130 */     GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
/* 131 */     float f1 = tileentitychest.field_145986_n + (tileentitychest.field_145989_m - tileentitychest.field_145986_n) * f;
/* 132 */     if (tileentitychest.field_145992_i != null) {
/* 133 */       float f2 = tileentitychest.field_145992_i.field_145986_n + (tileentitychest.field_145992_i.field_145989_m - tileentitychest.field_145992_i.field_145986_n) * f;
/* 134 */       if (f2 > f1) {
/* 135 */         f1 = f2;
/*     */       }
/*     */     }
/* 138 */     if (tileentitychest.field_145991_k != null) {
/* 139 */       float f3 = tileentitychest.field_145991_k.field_145986_n + (tileentitychest.field_145991_k.field_145989_m - tileentitychest.field_145991_k.field_145986_n) * f;
/* 140 */       if (f3 > f1) {
/* 141 */         f1 = f3;
/*     */       }
/*     */     }
/* 144 */     f1 = 1.0F - f1;
/* 145 */     f1 = 1.0F - f1 * f1 * f1;
/* 146 */     modelchest.chestLid.field_78795_f = (-(f1 * 3.141593F / 2.0F));
/* 147 */     modelchest.func_35402_a();
/* 148 */     GL11.glDisable(32826);
/* 149 */     GL11.glPopMatrix();
/* 150 */     GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
/*     */   }
/*     */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\client\TileEntityMillChestRenderer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */