/*     */ package org.millenaire.common.block;
/*     */ 
/*     */ import net.minecraft.block.BlockSlab;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.IIcon;
/*     */ import net.minecraft.world.World;
/*     */ import org.millenaire.common.core.MillCommonUtilities;
/*     */ import org.millenaire.common.forge.Mill;
/*     */ 
/*     */ public class BlockOrientedSlab extends BlockSlab
/*     */ {
/*     */   public final String textureTopVertName;
/*     */   public final String textureTopHorName;
/*     */   public final String textureSideName;
/*     */   public IIcon textureTopVert;
/*     */   public IIcon textureTopHor;
/*     */   public IIcon textureSide;
/*     */   
/*     */   public BlockOrientedSlab(String textureTopVertName, String textureTopHorName, String textureSideName)
/*     */   {
/*  22 */     super(false, net.minecraft.block.material.Material.field_151576_e);
/*  23 */     func_149647_a(Mill.tabMillenaire);
/*  24 */     func_149713_g(0);
/*  25 */     this.textureTopVertName = textureTopVertName;
/*  26 */     this.textureTopHorName = textureTopHorName;
/*  27 */     this.textureSideName = textureSideName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected ItemStack func_149644_j(int par1)
/*     */   {
/*  38 */     return new ItemStack(this, 2, 0);
/*     */   }
/*     */   
/*     */   public String func_150002_b(int var1)
/*     */   {
/*  43 */     return "byzantinebrick";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public IIcon func_149691_a(int side, int meta)
/*     */   {
/*  52 */     meta &= 0x1;
/*     */     
/*  54 */     if ((side == 1) || (side == 0)) {
/*  55 */       if (meta == 0) {
/*  56 */         return this.textureSide;
/*     */       }
/*  58 */       return this.textureTopVert;
/*     */     }
/*     */     
/*     */ 
/*  62 */     int turn = 0;
/*     */     
/*  64 */     if ((side == 4) || (side == 5)) {
/*  65 */       turn = 1;
/*     */     }
/*     */     
/*  68 */     if (meta == 1) {
/*  69 */       turn = 1 - turn;
/*     */     }
/*     */     
/*  72 */     if (turn == 0) {
/*  73 */       return this.textureTopHor;
/*     */     }
/*  75 */     if (turn == 1) {
/*  76 */       return this.textureTopVert;
/*     */     }
/*  78 */     return this.textureTopVert;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void func_149689_a(World world, int x, int y, int z, EntityLivingBase par5EntityLiving, ItemStack par6ItemStack)
/*     */   {
/*  87 */     if (world.func_147439_a(x, y - 1, z) == this)
/*     */     {
/*  89 */       int meta = world.func_72805_g(x, y - 1, z);
/*     */       
/*  91 */       MillCommonUtilities.setBlockAndMetadata(world, x, y - 1, z, Mill.byzantine_tiles, meta & 0x1, true, false);
/*  92 */       MillCommonUtilities.setBlockAndMetadata(world, x, y, z, net.minecraft.init.Blocks.field_150350_a, 0, true, false);
/*     */     }
/*     */     else
/*     */     {
/*  96 */       int var6 = net.minecraft.util.MathHelper.func_76128_c(par5EntityLiving.field_70177_z * 4.0F / 360.0F + 0.5D) & 0x3;
/*     */       
/*  98 */       int meta = world.func_72805_g(x, y, z);
/*     */       
/* 100 */       if (var6 == 0) {
/* 101 */         MillCommonUtilities.setBlockMetadata(world, x, y, z, 0x0 | meta, true);
/*     */       }
/*     */       
/* 104 */       if (var6 == 1) {
/* 105 */         MillCommonUtilities.setBlockMetadata(world, x, y, z, 0x1 | meta, true);
/*     */       }
/*     */       
/* 108 */       if (var6 == 2) {
/* 109 */         MillCommonUtilities.setBlockMetadata(world, x, y, z, 0x0 | meta, true);
/*     */       }
/*     */       
/* 112 */       if (var6 == 3) {
/* 113 */         MillCommonUtilities.setBlockMetadata(world, x, y, z, 0x1 | meta, true);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void func_149651_a(net.minecraft.client.renderer.texture.IIconRegister iconRegister)
/*     */   {
/* 120 */     this.textureTopVert = MillCommonUtilities.getIcon(iconRegister, this.textureTopVertName);
/* 121 */     this.textureTopHor = MillCommonUtilities.getIcon(iconRegister, this.textureTopHorName);
/* 122 */     this.textureSide = MillCommonUtilities.getIcon(iconRegister, this.textureSideName);
/*     */   }
/*     */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\block\BlockOrientedSlab.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */