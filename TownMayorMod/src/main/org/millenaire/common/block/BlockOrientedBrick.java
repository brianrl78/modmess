/*     */ package org.millenaire.common.block;
/*     */ 
/*     */ import org.millenaire.common.core.MillCommonUtilities;
/*     */ 
/*     */ public class BlockOrientedBrick extends net.minecraft.block.Block
/*     */ {
/*     */   String topTextureId;
/*     */   String topTextureRotateId;
/*     */   String bottomTextureId;
/*     */   String bottomTextureRotateId;
/*     */   String frontbackTextureId;
/*     */   String sideTextureId;
/*     */   net.minecraft.util.IIcon topTexture;
/*     */   net.minecraft.util.IIcon topTextureRotate;
/*     */   net.minecraft.util.IIcon bottomTexture;
/*     */   net.minecraft.util.IIcon bottomTextureRotate;
/*     */   net.minecraft.util.IIcon frontbackTexture;
/*     */   net.minecraft.util.IIcon sideTexture;
/*     */   
/*     */   public static int limitToValidMetadata(int par0) {
/*  21 */     return par0 & 0x1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public BlockOrientedBrick(String topTextureId, String topTextureRotateId, String bottomTextureId, String bottomTextureRotateId, String frontbackTextureId, String sideTextureId)
/*     */   {
/*  29 */     super(net.minecraft.block.material.Material.field_151576_e);
/*  30 */     func_149647_a(org.millenaire.common.forge.Mill.tabMillenaire);
/*     */     
/*  32 */     this.topTextureId = topTextureId;
/*  33 */     this.topTextureRotateId = topTextureRotateId;
/*  34 */     this.bottomTextureId = bottomTextureId;
/*  35 */     this.bottomTextureRotateId = bottomTextureRotateId;
/*  36 */     this.frontbackTextureId = frontbackTextureId;
/*  37 */     this.sideTextureId = sideTextureId;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected net.minecraft.item.ItemStack func_149644_j(int par1)
/*     */   {
/*  48 */     return new net.minecraft.item.ItemStack(this, 1, 0);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int func_149692_a(int par1)
/*     */   {
/*  57 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public net.minecraft.util.IIcon func_149691_a(int side, int meta)
/*     */   {
/*  67 */     if (side == 0) {
/*  68 */       if (meta == 0) {
/*  69 */         return this.bottomTexture;
/*     */       }
/*  71 */       return this.bottomTextureRotate;
/*     */     }
/*     */     
/*     */ 
/*  75 */     if (side == 1) {
/*  76 */       if (meta == 0) {
/*  77 */         return this.topTexture;
/*     */       }
/*  79 */       return this.topTextureRotate;
/*     */     }
/*     */     
/*     */ 
/*  83 */     int turn = 0;
/*     */     
/*  85 */     if ((side == 4) || (side == 5)) {
/*  86 */       turn = 1;
/*     */     }
/*     */     
/*  89 */     if (meta == 1) {
/*  90 */       turn = 1 - turn;
/*     */     }
/*     */     
/*  93 */     if (turn == 0) {
/*  94 */       return this.frontbackTexture;
/*     */     }
/*  96 */     if (turn == 1) {
/*  97 */       return this.sideTexture;
/*     */     }
/*  99 */     return this.sideTexture;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void func_149689_a(net.minecraft.world.World world, int par2, int par3, int par4, net.minecraft.entity.EntityLivingBase par5EntityLiving, net.minecraft.item.ItemStack par6ItemStack)
/*     */   {
/* 108 */     int var6 = net.minecraft.util.MathHelper.func_76128_c(par5EntityLiving.field_70177_z * 4.0F / 360.0F + 0.5D) & 0x3;
/*     */     
/* 110 */     if (var6 == 0) {
/* 111 */       MillCommonUtilities.setBlockMetadata(world, par2, par3, par4, 0, true);
/*     */     }
/*     */     
/* 114 */     if (var6 == 1) {
/* 115 */       MillCommonUtilities.setBlockMetadata(world, par2, par3, par4, 1, true);
/*     */     }
/*     */     
/* 118 */     if (var6 == 2) {
/* 119 */       MillCommonUtilities.setBlockMetadata(world, par2, par3, par4, 0, true);
/*     */     }
/*     */     
/* 122 */     if (var6 == 3) {
/* 123 */       MillCommonUtilities.setBlockMetadata(world, par2, par3, par4, 1, true);
/*     */     }
/*     */   }
/*     */   
/*     */   public void func_149651_a(net.minecraft.client.renderer.texture.IIconRegister iconRegister)
/*     */   {
/* 129 */     this.topTexture = MillCommonUtilities.getIcon(iconRegister, this.topTextureId);
/* 130 */     this.topTextureRotate = MillCommonUtilities.getIcon(iconRegister, this.topTextureRotateId);
/* 131 */     this.bottomTexture = MillCommonUtilities.getIcon(iconRegister, this.bottomTextureId);
/* 132 */     this.bottomTextureRotate = MillCommonUtilities.getIcon(iconRegister, this.bottomTextureRotateId);
/* 133 */     this.frontbackTexture = MillCommonUtilities.getIcon(iconRegister, this.frontbackTextureId);
/* 134 */     this.sideTexture = MillCommonUtilities.getIcon(iconRegister, this.sideTextureId);
/*     */   }
/*     */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\block\BlockOrientedBrick.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */