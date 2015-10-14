/*     */ package org.millenaire.common.block;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.BlockCrops;
/*     */ import net.minecraft.block.IGrowable;
/*     */ import net.minecraft.client.renderer.texture.IIconRegister;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.IIcon;
/*     */ import net.minecraft.world.IBlockAccess;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraftforge.common.IPlantable;
/*     */ import net.minecraftforge.common.util.ForgeDirection;
/*     */ import org.millenaire.common.core.MillCommonUtilities;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BlockMillCrops
/*     */   extends BlockCrops
/*     */   implements IGrowable
/*     */ {
/*     */   private final String[] textureNames;
/*     */   private final IIcon[] textures;
/*     */   private final boolean requireIrrigation;
/*     */   private final boolean slowGrowth;
/*     */   private IPlantable seed;
/*     */   
/*     */   public BlockMillCrops(String[] textureNames, boolean requireIrrigation, boolean slowGrowth)
/*     */   {
/*  32 */     this.textures = new IIcon[8];
/*  33 */     this.textureNames = textureNames;
/*  34 */     this.requireIrrigation = requireIrrigation;
/*  35 */     this.slowGrowth = slowGrowth;
/*  36 */     func_149675_a(true);
/*  37 */     float f = 0.5F;
/*  38 */     func_149676_a(0.0F, 0.0F, 0.0F, 1.0F, 0.25F, 1.0F);
/*     */   }
/*     */   
/*     */   protected Item func_149865_P()
/*     */   {
/*  43 */     return (Item)this.seed;
/*     */   }
/*     */   
/*     */   protected Item func_149866_i()
/*     */   {
/*  48 */     return (Item)this.seed;
/*     */   }
/*     */   
/*     */   public ArrayList getDrops(World world, int x, int y, int z, int metadata, int fortune)
/*     */   {
/*  53 */     ArrayList ret = new ArrayList();
/*  54 */     ret.add(new ItemStack(func_149865_P(), 1, 0));
/*  55 */     if (metadata == 7) {
/*  56 */       for (int n = 0; n < 3 + fortune; n++) {
/*  57 */         if (world.field_73012_v.nextInt(15) <= metadata) {
/*  58 */           ret.add(new ItemStack(func_149865_P(), 1, 0));
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*  63 */     return ret;
/*     */   }
/*     */   
/*     */   public int getFireSpreadSpeed(IBlockAccess world, int x, int y, int z, ForgeDirection face)
/*     */   {
/*  68 */     return 60;
/*     */   }
/*     */   
/*     */   public int getFlammability(IBlockAccess world, int x, int y, int z, ForgeDirection face)
/*     */   {
/*  73 */     return 150;
/*     */   }
/*     */   
/*     */   protected float getGrowthRate(World world, int i, int j, int k) {
/*  77 */     int irrigation = world.func_72805_g(i, j - 1, k);
/*  78 */     if ((this.requireIrrigation) && (irrigation == 0)) {
/*  79 */       return 0.0F;
/*     */     }
/*  81 */     return !this.slowGrowth ? 8.0F : 4.0F;
/*     */   }
/*     */   
/*     */   public IIcon func_149691_a(int p_149691_1_, int meta)
/*     */   {
/*  86 */     if ((meta < 0) || (meta > 7)) {
/*  87 */       meta = 7;
/*     */     }
/*  89 */     return this.textures[meta];
/*     */   }
/*     */   
/*     */   public Item func_149650_a(int par1, Random par2Random, int par3)
/*     */   {
/*  94 */     return (Item)this.seed;
/*     */   }
/*     */   
/*     */   public int func_149645_b()
/*     */   {
/*  99 */     return 6;
/*     */   }
/*     */   
/*     */   public IPlantable getSeed() {
/* 103 */     return this.seed;
/*     */   }
/*     */   
/*     */   public int func_149745_a(Random par1Random)
/*     */   {
/* 108 */     return 1;
/*     */   }
/*     */   
/*     */   public void func_149651_a(IIconRegister iconRegister)
/*     */   {
/* 113 */     for (int i = 0; i < 8; i++) {
/* 114 */       this.textures[i] = MillCommonUtilities.getIcon(iconRegister, this.textureNames[i]);
/*     */     }
/*     */   }
/*     */   
/*     */   public void setSeed(IPlantable seed)
/*     */   {
/* 120 */     this.seed = seed;
/*     */   }
/*     */   
/*     */   public void func_149674_a(World world, int i, int j, int k, Random random)
/*     */   {
/* 125 */     func_149855_e(world, i, j, k);
/* 126 */     if (world.func_72957_l(i, j + 1, k) >= 9) {
/* 127 */       int l = world.func_72805_g(i, j, k);
/* 128 */       if (l < 7) {
/* 129 */         float f = getGrowthRate(world, i, j, k);
/* 130 */         if ((f > 0.0F) && (random.nextInt((int)(100.0F / f)) == 0)) {
/* 131 */           MillCommonUtilities.setBlockMetadata(world, i, j, k, l + 1, true);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\block\BlockMillCrops.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */