/*     */ package org.millenaire.client;
/*     */ 
/*     */ import net.minecraft.client.model.ModelBiped;
/*     */ import net.minecraft.client.model.ModelRenderer;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.util.MathHelper;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ModelFemaleSymmetrical
/*     */   extends ModelBiped
/*     */ {
/*     */   public ModelRenderer bipedUpperBody;
/*     */   public ModelRenderer bipedBreast;
/*     */   public boolean field_78119_l;
/*     */   public boolean field_78120_m;
/*     */   
/*     */   public ModelFemaleSymmetrical()
/*     */   {
/*  24 */     this(0.0F);
/*     */   }
/*     */   
/*     */   public ModelFemaleSymmetrical(float f) {
/*  28 */     this(f, 0.0F);
/*     */   }
/*     */   
/*     */   public ModelFemaleSymmetrical(float f, float f1) {
/*  32 */     this.field_78119_l = false;
/*  33 */     this.field_78120_m = false;
/*  34 */     this.field_78122_k = new ModelRenderer(this, 0, 0);
/*  35 */     this.field_78122_k.func_78790_a(-5.0F, 0.0F, -1.0F, 10, 16, 1, f);
/*  36 */     this.field_78121_j = new ModelRenderer(this, 24, 0);
/*  37 */     this.field_78121_j.func_78790_a(-3.0F, -6.0F, -1.0F, 6, 6, 1, f);
/*  38 */     this.field_78116_c = new ModelRenderer(this, 0, 0);
/*  39 */     this.field_78116_c.func_78790_a(-4.0F, -8.0F, -4.0F, 8, 8, 8, f);
/*  40 */     this.field_78116_c.func_78793_a(0.0F, 0.0F + f1, 0.0F);
/*  41 */     this.field_78114_d = new ModelRenderer(this, 32, 0);
/*  42 */     this.field_78114_d.func_78790_a(-4.0F, -8.0F, -4.0F, 8, 8, 8, f + 0.5F);
/*  43 */     this.field_78114_d.func_78793_a(0.0F, 0.0F + f1, 0.0F);
/*  44 */     this.bipedUpperBody = new ModelRenderer(this, 16, 17);
/*  45 */     this.bipedUpperBody.func_78790_a(-3.5F, 0.0F, -1.5F, 7, 12, 3, f);
/*  46 */     this.bipedUpperBody.func_78793_a(0.0F, 0.0F + f1, 0.0F);
/*  47 */     this.bipedBreast = new ModelRenderer(this, 17, 18);
/*  48 */     this.bipedBreast.func_78790_a(-3.5F, 0.75F, -3.0F, 7, 4, 2, f);
/*  49 */     this.bipedBreast.func_78793_a(0.0F, 0.0F + f1, 0.0F);
/*  50 */     this.field_78112_f = new ModelRenderer(this, 36, 17);
/*  51 */     this.field_78112_f.func_78790_a(-1.5F, -2.0F, -1.5F, 3, 12, 3, f);
/*  52 */     this.field_78112_f.func_78793_a(-5.0F, 2.0F + f1, 0.0F);
/*  53 */     this.field_78113_g = new ModelRenderer(this, 36, 17);
/*  54 */     this.field_78113_g.field_78809_i = true;
/*  55 */     this.field_78113_g.func_78790_a(-1.5F, -2.0F, -1.5F, 3, 12, 3, f);
/*  56 */     this.field_78113_g.func_78793_a(5.0F, 2.0F + f1, 0.0F);
/*  57 */     this.field_78123_h = new ModelRenderer(this, 0, 16);
/*  58 */     this.field_78123_h.func_78790_a(-2.0F, 0.0F, -2.0F, 4, 12, 4, f);
/*  59 */     this.field_78123_h.func_78793_a(-2.0F, 12.0F + f1, 0.0F);
/*  60 */     this.field_78124_i = new ModelRenderer(this, 0, 16);
/*  61 */     this.field_78124_i.field_78809_i = true;
/*  62 */     this.field_78124_i.func_78790_a(-2.0F, 0.0F, -2.0F, 4, 12, 4, f);
/*  63 */     this.field_78124_i.func_78793_a(2.0F, 12.0F + f1, 0.0F);
/*     */   }
/*     */   
/*     */   public void func_78088_a(Entity par1Entity, float f, float f1, float f2, float f3, float f4, float f5)
/*     */   {
/*  68 */     func_78087_a(f, f1, f2, f3, f4, f5, par1Entity);
/*  69 */     this.field_78116_c.func_78785_a(f5);
/*  70 */     this.bipedUpperBody.func_78785_a(f5);
/*  71 */     this.field_78112_f.func_78785_a(f5);
/*  72 */     this.field_78113_g.func_78785_a(f5);
/*  73 */     this.field_78123_h.func_78785_a(f5);
/*  74 */     this.field_78124_i.func_78785_a(f5);
/*  75 */     this.field_78114_d.func_78785_a(f5);
/*  76 */     this.bipedBreast.func_78785_a(f5);
/*     */   }
/*     */   
/*     */   public void func_78111_c(float f)
/*     */   {
/*  81 */     this.field_78122_k.func_78785_a(f);
/*     */   }
/*     */   
/*     */   public void func_78110_b(float f)
/*     */   {
/*  86 */     this.field_78121_j.field_78796_g = this.field_78116_c.field_78796_g;
/*  87 */     this.field_78121_j.field_78795_f = this.field_78116_c.field_78795_f;
/*  88 */     this.field_78121_j.field_78800_c = 0.0F;
/*  89 */     this.field_78121_j.field_78797_d = 0.0F;
/*  90 */     this.field_78121_j.func_78785_a(f);
/*     */   }
/*     */   
/*     */   public void func_78087_a(float f, float f1, float f2, float f3, float f4, float f5, Entity par7Entity)
/*     */   {
/*  95 */     this.field_78116_c.field_78796_g = (f3 / 57.29578F);
/*  96 */     this.field_78116_c.field_78795_f = (f4 / 57.29578F);
/*  97 */     this.field_78114_d.field_78796_g = this.field_78116_c.field_78796_g;
/*  98 */     this.field_78114_d.field_78795_f = this.field_78116_c.field_78795_f;
/*  99 */     this.field_78112_f.field_78795_f = (MathHelper.func_76134_b(f * 0.6662F + 3.141593F) * 2.0F * f1 * 0.5F);
/* 100 */     this.field_78113_g.field_78795_f = (MathHelper.func_76134_b(f * 0.6662F) * 2.0F * f1 * 0.5F);
/* 101 */     this.field_78112_f.field_78808_h = 0.0F;
/* 102 */     this.field_78113_g.field_78808_h = 0.0F;
/* 103 */     this.field_78123_h.field_78795_f = (MathHelper.func_76134_b(f * 0.6662F) * 1.4F * f1);
/* 104 */     this.field_78124_i.field_78795_f = (MathHelper.func_76134_b(f * 0.6662F + 3.141593F) * 1.4F * f1);
/* 105 */     this.field_78123_h.field_78796_g = 0.0F;
/* 106 */     this.field_78124_i.field_78796_g = 0.0F;
/* 107 */     if (this.field_78093_q) {
/* 108 */       this.field_78112_f.field_78795_f += -0.6283185F;
/* 109 */       this.field_78113_g.field_78795_f += -0.6283185F;
/* 110 */       this.field_78123_h.field_78795_f = -1.256637F;
/* 111 */       this.field_78124_i.field_78795_f = -1.256637F;
/* 112 */       this.field_78123_h.field_78796_g = 0.3141593F;
/* 113 */       this.field_78124_i.field_78796_g = -0.3141593F;
/*     */     }
/* 115 */     if (this.field_78119_l) {
/* 116 */       this.field_78113_g.field_78795_f = (this.field_78113_g.field_78795_f * 0.5F - 0.3141593F);
/*     */     }
/* 118 */     if (this.field_78120_m) {
/* 119 */       this.field_78112_f.field_78795_f = (this.field_78112_f.field_78795_f * 0.5F - 0.3141593F);
/*     */     }
/* 121 */     this.field_78112_f.field_78796_g = 0.0F;
/* 122 */     this.field_78113_g.field_78796_g = 0.0F;
/* 123 */     if (this.field_78095_p > -9990.0F) {
/* 124 */       float f6 = this.field_78095_p;
/* 125 */       this.bipedUpperBody.field_78796_g = (MathHelper.func_76126_a(MathHelper.func_76129_c(f6) * 3.141593F * 2.0F) * 0.2F);
/* 126 */       this.field_78112_f.field_78798_e = (MathHelper.func_76126_a(this.bipedUpperBody.field_78796_g) * 5.0F);
/* 127 */       this.field_78112_f.field_78800_c = (-MathHelper.func_76134_b(this.bipedUpperBody.field_78796_g) * 5.0F);
/* 128 */       this.field_78113_g.field_78798_e = (-MathHelper.func_76126_a(this.bipedUpperBody.field_78796_g) * 5.0F);
/* 129 */       this.field_78113_g.field_78800_c = (MathHelper.func_76134_b(this.bipedUpperBody.field_78796_g) * 5.0F);
/* 130 */       this.field_78112_f.field_78796_g += this.bipedUpperBody.field_78796_g;
/* 131 */       this.field_78113_g.field_78796_g += this.bipedUpperBody.field_78796_g;
/* 132 */       this.field_78113_g.field_78795_f += this.bipedUpperBody.field_78796_g;
/* 133 */       f6 = 1.0F - this.field_78095_p;
/* 134 */       f6 *= f6;
/* 135 */       f6 *= f6;
/* 136 */       f6 = 1.0F - f6;
/* 137 */       float f7 = MathHelper.func_76126_a(f6 * 3.141593F);
/* 138 */       float f8 = MathHelper.func_76126_a(this.field_78095_p * 3.141593F) * -(this.field_78116_c.field_78795_f - 0.7F) * 0.75F; ModelRenderer 
/* 139 */         tmp558_555 = this.field_78112_f;tmp558_555.field_78795_f = ((float)(tmp558_555.field_78795_f - (f7 * 1.2D + f8)));
/* 140 */       this.field_78112_f.field_78796_g += this.bipedUpperBody.field_78796_g * 2.0F;
/* 141 */       this.field_78112_f.field_78808_h = (MathHelper.func_76126_a(this.field_78095_p * 3.141593F) * -0.4F);
/*     */     }
/* 143 */     if (this.field_78117_n) {
/* 144 */       this.bipedUpperBody.field_78795_f = 0.5F;
/* 145 */       this.field_78123_h.field_78795_f -= 0.0F;
/* 146 */       this.field_78124_i.field_78795_f -= 0.0F;
/* 147 */       this.field_78112_f.field_78795_f += 0.4F;
/* 148 */       this.field_78113_g.field_78795_f += 0.4F;
/* 149 */       this.field_78123_h.field_78798_e = 4.0F;
/* 150 */       this.field_78124_i.field_78798_e = 4.0F;
/* 151 */       this.field_78123_h.field_78797_d = 9.0F;
/* 152 */       this.field_78124_i.field_78797_d = 9.0F;
/* 153 */       this.field_78116_c.field_78797_d = 1.0F;
/*     */     } else {
/* 155 */       this.bipedUpperBody.field_78795_f = 0.0F;
/* 156 */       this.field_78123_h.field_78798_e = 0.0F;
/* 157 */       this.field_78124_i.field_78798_e = 0.0F;
/* 158 */       this.field_78123_h.field_78797_d = 12.0F;
/* 159 */       this.field_78124_i.field_78797_d = 12.0F;
/* 160 */       this.field_78116_c.field_78797_d = 0.0F;
/*     */     }
/* 162 */     this.field_78112_f.field_78808_h += MathHelper.func_76134_b(f2 * 0.09F) * 0.05F + 0.05F;
/* 163 */     this.field_78113_g.field_78808_h -= MathHelper.func_76134_b(f2 * 0.09F) * 0.05F + 0.05F;
/* 164 */     this.field_78112_f.field_78795_f += MathHelper.func_76126_a(f2 * 0.067F) * 0.05F;
/* 165 */     this.field_78113_g.field_78795_f -= MathHelper.func_76126_a(f2 * 0.067F) * 0.05F;
/*     */   }
/*     */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\client\ModelFemaleSymmetrical.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */