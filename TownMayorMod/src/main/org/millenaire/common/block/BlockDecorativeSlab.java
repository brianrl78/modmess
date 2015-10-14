/*     */ package org.millenaire.common.block;
/*     */ 
/*     */ import cpw.mods.fml.relauncher.Side;
/*     */ import cpw.mods.fml.relauncher.SideOnly;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import java.util.Set;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.Block.SoundType;
/*     */ import net.minecraft.block.BlockSlab;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.client.renderer.texture.IIconRegister;
/*     */ import net.minecraft.creativetab.CreativeTabs;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemSlab;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.IIcon;
/*     */ import net.minecraft.world.IBlockAccess;
/*     */ import net.minecraft.world.World;
/*     */ import org.millenaire.common.core.MillCommonUtilities;
/*     */ import org.millenaire.common.forge.Mill;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BlockDecorativeSlab
/*     */   extends BlockSlab
/*     */ {
/*     */   public static class ItemDecorativeSlab
/*     */     extends ItemSlab
/*     */   {
/*     */     BlockDecorativeSlab block;
/*     */     private final boolean isFullBlockDec;
/*     */     private final BlockSlab theHalfSlabDec;
/*     */     private final BlockSlab doubleSlabDec;
/*     */     
/*     */     public ItemDecorativeSlab(Block b)
/*     */     {
/*  41 */       super(Mill.pathSlab, Mill.path, b.func_149662_c());
/*  42 */       this.block = ((BlockDecorativeSlab)b);
/*     */       
/*  44 */       func_77656_e(0);
/*  45 */       func_77627_a(true);
/*  46 */       this.theHalfSlabDec = Mill.pathSlab;
/*  47 */       this.doubleSlabDec = Mill.path;
/*  48 */       this.isFullBlockDec = b.func_149662_c();
/*     */     }
/*     */     
/*     */     public ItemDecorativeSlab(BlockDecorativeSlab b, BlockDecorativeSlab halfSlab, BlockDecorativeSlab fullBlock, boolean full) {
/*  52 */       super(halfSlab, fullBlock, full);
/*     */       
/*  54 */       this.block = b;
/*     */       
/*  56 */       func_77656_e(0);
/*  57 */       func_77627_a(true);
/*  58 */       this.theHalfSlabDec = halfSlab;
/*  59 */       this.doubleSlabDec = fullBlock;
/*  60 */       this.isFullBlockDec = full;
/*     */     }
/*     */     
/*     */     private boolean func_77888_a(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7) {
/*  64 */       if (par7 == 0) {
/*  65 */         par5--;
/*     */       }
/*     */       
/*  68 */       if (par7 == 1) {
/*  69 */         par5++;
/*     */       }
/*     */       
/*  72 */       if (par7 == 2) {
/*  73 */         par6--;
/*     */       }
/*     */       
/*  76 */       if (par7 == 3) {
/*  77 */         par6++;
/*     */       }
/*     */       
/*  80 */       if (par7 == 4) {
/*  81 */         par4--;
/*     */       }
/*     */       
/*  84 */       if (par7 == 5) {
/*  85 */         par4++;
/*     */       }
/*     */       
/*  88 */       Block block = par3World.func_147439_a(par4, par5, par6);
/*  89 */       int j1 = par3World.func_72805_g(par4, par5, par6);
/*  90 */       int k1 = j1 & 0x7;
/*     */       
/*  92 */       if ((block == this.theHalfSlabDec) && (k1 == par1ItemStack.func_77960_j())) {
/*  93 */         if ((par3World.func_72829_c(this.doubleSlabDec.func_149668_a(par3World, par4, par5, par6))) && (par3World.func_147465_d(par4, par5, par6, this.doubleSlabDec, k1, 3))) {
/*  94 */           par3World.func_72908_a(par4 + 0.5F, par5 + 0.5F, par6 + 0.5F, this.doubleSlabDec.field_149762_H.field_150501_a, (this.doubleSlabDec.field_149762_H.func_150497_c() + 1.0F) / 2.0F, this.doubleSlabDec.field_149762_H.func_150494_d() * 0.8F);
/*     */           
/*  96 */           par1ItemStack.field_77994_a -= 1;
/*     */         }
/*     */         
/*  99 */         return true;
/*     */       }
/* 101 */       return false;
/*     */     }
/*     */     
/*     */ 
/*     */     public IIcon func_77617_a(int i)
/*     */     {
/* 107 */       return this.block.func_149691_a(2, i);
/*     */     }
/*     */     
/*     */     public int func_77647_b(int i)
/*     */     {
/* 112 */       return i;
/*     */     }
/*     */     
/*     */     public String func_77667_c(ItemStack itemstack)
/*     */     {
/* 117 */       return super.func_77658_a() + "." + itemstack.func_77960_j();
/*     */     }
/*     */     
/*     */ 
/*     */     public boolean func_77648_a(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10)
/*     */     {
/* 123 */       if (this.isFullBlockDec)
/* 124 */         return super.func_77648_a(par1ItemStack, par2EntityPlayer, par3World, par4, par5, par6, par7, par8, par9, par10);
/* 125 */       if (par1ItemStack.field_77994_a == 0)
/* 126 */         return false;
/* 127 */       if (!par2EntityPlayer.func_82247_a(par4, par5, par6, par7, par1ItemStack)) {
/* 128 */         return false;
/*     */       }
/* 130 */       Block block = par3World.func_147439_a(par4, par5, par6);
/* 131 */       int j1 = par3World.func_72805_g(par4, par5, par6);
/*     */       
/* 133 */       if ((par7 == 1) && (block == this.theHalfSlabDec) && (j1 == par1ItemStack.func_77960_j())) {
/* 134 */         if ((par3World.func_72829_c(this.doubleSlabDec.func_149668_a(par3World, par4, par5, par6))) && (par3World.func_147465_d(par4, par5, par6, this.doubleSlabDec, j1, 3)))
/*     */         {
/* 136 */           par3World.func_72908_a(par4 + 0.5F, par5 + 0.5F, par6 + 0.5F, this.doubleSlabDec.field_149762_H.field_150501_a, (this.doubleSlabDec.field_149762_H.func_150497_c() + 1.0F) / 2.0F, this.doubleSlabDec.field_149762_H.func_150494_d() * 0.8F);
/*     */           
/* 138 */           par1ItemStack.field_77994_a -= 1;
/*     */         }
/*     */         
/* 141 */         return true;
/*     */       }
/* 143 */       return func_77888_a(par1ItemStack, par2EntityPlayer, par3World, par4, par5, par6, par7) ? true : super.func_77648_a(par1ItemStack, par2EntityPlayer, par3World, par4, par5, par6, par7, par8, par9, par10);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static int getBlockFromDye(int i)
/*     */   {
/* 151 */     return i;
/*     */   }
/*     */   
/*     */   public static int getDyeFromBlock(int i) {
/* 155 */     return i;
/*     */   }
/*     */   
/* 158 */   HashMap<Integer, String> textureSideNames = new HashMap();
/* 159 */   HashMap<Integer, String> textureTopNames = new HashMap();
/* 160 */   HashMap<Integer, String> textureBottomNames = new HashMap();
/* 161 */   HashMap<Integer, IIcon> texturesSide = new HashMap();
/* 162 */   HashMap<Integer, IIcon> texturesTop = new HashMap();
/* 163 */   HashMap<Integer, IIcon> texturesBottom = new HashMap();
/*     */   
/* 165 */   HashMap<Integer, String> names = new HashMap();
/*     */   
/*     */   public BlockDecorativeSlab(Material material, boolean full) {
/* 168 */     super(full, material);
/* 169 */     func_149675_a(true);
/* 170 */     func_149713_g(0);
/* 171 */     func_149647_a(Mill.tabMillenaire);
/*     */   }
/*     */   
/*     */   public int func_149692_a(int par1)
/*     */   {
/* 176 */     return par1 & 0x7 | 0x8;
/*     */   }
/*     */   
/*     */   public String func_150002_b(int meta)
/*     */   {
/* 181 */     return (String)this.names.get(Integer.valueOf(meta));
/*     */   }
/*     */   
/*     */ 
/*     */   public IIcon func_149691_a(int side, int meta)
/*     */   {
/* 187 */     if (side == 1) {
/* 188 */       if (this.texturesTop.containsKey(Integer.valueOf(meta))) {
/* 189 */         return (IIcon)this.texturesTop.get(Integer.valueOf(meta));
/*     */       }
/* 191 */       return (IIcon)this.texturesTop.get(Integer.valueOf(0));
/*     */     }
/* 193 */     if (side == 0) {
/* 194 */       if (this.texturesBottom.containsKey(Integer.valueOf(meta))) {
/* 195 */         return (IIcon)this.texturesBottom.get(Integer.valueOf(meta));
/*     */       }
/* 197 */       return (IIcon)this.texturesBottom.get(Integer.valueOf(0));
/*     */     }
/*     */     
/* 200 */     if (this.texturesSide.containsKey(Integer.valueOf(meta))) {
/* 201 */       return (IIcon)this.texturesSide.get(Integer.valueOf(meta));
/*     */     }
/* 203 */     return (IIcon)this.texturesTop.get(Integer.valueOf(0));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void func_149666_a(Item item, CreativeTabs par2CreativeTabs, List par3List)
/*     */   {
/* 211 */     for (Iterator i$ = this.texturesSide.keySet().iterator(); i$.hasNext();) { int meta = ((Integer)i$.next()).intValue();
/* 212 */       if (meta >= 8) {
/* 213 */         par3List.add(new ItemStack(item, 1, meta));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public int func_149660_a(World par1World, int par2, int par3, int par4, int par5, float par6, float par7, float par8, int meta)
/*     */   {
/* 220 */     return meta;
/*     */   }
/*     */   
/*     */   public int func_149745_a(Random par1Random)
/*     */   {
/* 225 */     return 1;
/*     */   }
/*     */   
/*     */   @SideOnly(Side.CLIENT)
/*     */   public void func_149651_a(IIconRegister iconRegister)
/*     */   {
/* 231 */     for (Iterator i$ = this.textureTopNames.keySet().iterator(); i$.hasNext();) { int meta = ((Integer)i$.next()).intValue();
/* 232 */       this.texturesTop.put(Integer.valueOf(meta), MillCommonUtilities.getIcon(iconRegister, (String)this.textureTopNames.get(Integer.valueOf(meta))));
/*     */     }
/* 234 */     for (Iterator i$ = this.textureBottomNames.keySet().iterator(); i$.hasNext();) { int meta = ((Integer)i$.next()).intValue();
/* 235 */       this.texturesBottom.put(Integer.valueOf(meta), MillCommonUtilities.getIcon(iconRegister, (String)this.textureBottomNames.get(Integer.valueOf(meta))));
/*     */     }
/* 237 */     for (Iterator i$ = this.textureSideNames.keySet().iterator(); i$.hasNext();) { int meta = ((Integer)i$.next()).intValue();
/* 238 */       this.texturesSide.put(Integer.valueOf(meta), MillCommonUtilities.getIcon(iconRegister, (String)this.textureSideNames.get(Integer.valueOf(meta))));
/*     */     }
/*     */   }
/*     */   
/*     */   public void registerTexture(int meta, String name) {
/* 243 */     this.textureTopNames.put(Integer.valueOf(meta), name);
/* 244 */     this.textureBottomNames.put(Integer.valueOf(meta), name);
/* 245 */     this.textureSideNames.put(Integer.valueOf(meta), name);
/*     */     
/* 247 */     this.textureTopNames.put(Integer.valueOf(meta | 0x8), name);
/* 248 */     this.textureBottomNames.put(Integer.valueOf(meta | 0x8), name);
/* 249 */     this.textureSideNames.put(Integer.valueOf(meta | 0x8), name);
/*     */   }
/*     */   
/*     */   public void registerTexture(int meta, String top, String bottom, String side) {
/* 253 */     this.textureTopNames.put(Integer.valueOf(meta), top);
/* 254 */     this.textureBottomNames.put(Integer.valueOf(meta), bottom);
/* 255 */     this.textureSideNames.put(Integer.valueOf(meta), side);
/*     */     
/* 257 */     this.textureTopNames.put(Integer.valueOf(meta | 0x8), top);
/* 258 */     this.textureBottomNames.put(Integer.valueOf(meta | 0x8), bottom);
/* 259 */     this.textureSideNames.put(Integer.valueOf(meta | 0x8), side);
/*     */   }
/*     */   
/*     */   public void func_149719_a(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
/*     */   {
/* 264 */     if (this.field_149787_q) {
/* 265 */       func_149676_a(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
/*     */     } else {
/* 267 */       func_149676_a(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean func_149646_a(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5)
/*     */   {
/* 273 */     if (this.field_149787_q)
/* 274 */       return super.func_149646_a(par1IBlockAccess, par2, par3, par4, par5);
/* 275 */     if ((par5 != 1) && (par5 != 0) && (!super.func_149646_a(par1IBlockAccess, par2, par3, par4, par5))) {
/* 276 */       return false;
/*     */     }
/* 278 */     return true;
/*     */   }
/*     */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\block\BlockDecorativeSlab.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */