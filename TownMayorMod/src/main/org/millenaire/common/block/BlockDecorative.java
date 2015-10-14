/*     */ package org.millenaire.common.block;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.creativetab.CreativeTabs;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemBlock;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.IIcon;
/*     */ import net.minecraft.world.IBlockAccess;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraftforge.common.util.ForgeDirection;
/*     */ import org.millenaire.common.core.MillCommonUtilities;
/*     */ import org.millenaire.common.forge.Mill;
/*     */ 
/*     */ public class BlockDecorative extends Block
/*     */ {
/*     */   private static final int EXPLOSION_RADIUS = 32;
/*     */   
/*     */   public static class ItemDecorative extends ItemBlock
/*     */   {
/*     */     BlockDecorative block;
/*     */     
/*     */     public ItemDecorative(Block b)
/*     */     {
/*  31 */       super();
/*  32 */       func_77656_e(0);
/*  33 */       func_77627_a(true);
/*  34 */       this.block = ((BlockDecorative)b);
/*     */     }
/*     */     
/*     */     public IIcon func_77617_a(int i)
/*     */     {
/*  39 */       return this.block.func_149691_a(2, i);
/*     */     }
/*     */     
/*     */     public int func_77647_b(int i)
/*     */     {
/*  44 */       return i;
/*     */     }
/*     */     
/*     */     public String func_77667_c(ItemStack itemstack)
/*     */     {
/*  49 */       return super.func_77658_a() + "." + itemstack.func_77960_j();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public static int getBlockFromDye(int i)
/*     */   {
/*  56 */     return i;
/*     */   }
/*     */   
/*     */   public static int getDyeFromBlock(int i) {
/*  60 */     return i;
/*     */   }
/*     */   
/*  63 */   HashMap<Integer, String> textureSideNames = new HashMap();
/*  64 */   HashMap<Integer, String> textureTopNames = new HashMap();
/*  65 */   HashMap<Integer, String> textureBottomNames = new HashMap();
/*  66 */   HashMap<Integer, IIcon> texturesSide = new HashMap();
/*  67 */   HashMap<Integer, IIcon> texturesTop = new HashMap();
/*  68 */   HashMap<Integer, IIcon> texturesBottom = new HashMap();
/*     */   
/*  70 */   HashMap<Integer, String> names = new HashMap();
/*     */   
/*     */   public BlockDecorative(Material material) {
/*  73 */     super(material);
/*  74 */     func_149675_a(true);
/*  75 */     func_149647_a(Mill.tabMillenaire);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void alchemistExplosion(World world, int i, int j, int k)
/*     */   {
/*  92 */     MillCommonUtilities.setBlockAndMetadata(world, i, j, k, Blocks.field_150350_a, 0, true, false);
/*     */     
/*  94 */     for (int y = 32; y >= -32; y--) {
/*  95 */       if ((y + j >= 0) && (y + j < 128)) {
/*  96 */         for (int x = -32; x <= 32; x++) {
/*  97 */           for (int z = -32; z <= 32; z++) {
/*  98 */             if (x * x + y * y + z * z <= 1024) {
/*  99 */               Block block = world.func_147439_a(i + x, j + y, k + z);
/* 100 */               if (block != Blocks.field_150350_a) {
/* 101 */                 MillCommonUtilities.setBlockAndMetadata(world, i + x, j + y, k + z, Blocks.field_150350_a, 0, true, false);
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public int func_149692_a(int i)
/*     */   {
/* 112 */     return i;
/*     */   }
/*     */   
/*     */ 
/*     */   public void func_149690_a(World par1World, int i, int j, int k, int meta, float par6, int par7)
/*     */   {
/* 118 */     if ((this == Mill.stone_decoration) && (meta == 3)) {
/* 119 */       boolean isExplosion = false;
/*     */       
/*     */ 
/* 122 */       StackTraceElement[] trace = Thread.currentThread().getStackTrace();
/*     */       
/* 124 */       for (int id = 0; id < trace.length; id++) {
/* 125 */         String className = trace[id].getClassName();
/* 126 */         if (className.equals(net.minecraft.world.Explosion.class.getName())) {
/* 127 */           isExplosion = true;
/*     */         }
/*     */       }
/*     */       
/* 131 */       if (isExplosion) {
/* 132 */         alchemistExplosion(par1World, i, j, k);
/* 133 */         return;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 138 */     super.func_149690_a(par1World, i, j, k, meta, par6, par7);
/*     */   }
/*     */   
/*     */   public int getFireSpreadSpeed(IBlockAccess world, int x, int y, int z, ForgeDirection face)
/*     */   {
/* 143 */     if (func_149688_o() == Material.field_151575_d) {
/* 144 */       return 5;
/*     */     }
/*     */     
/* 147 */     return 0;
/*     */   }
/*     */   
/*     */   public int getFlammability(IBlockAccess world, int x, int y, int z, ForgeDirection face)
/*     */   {
/* 152 */     if (func_149688_o() == Material.field_151575_d) {
/* 153 */       return 150;
/*     */     }
/*     */     
/* 156 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */   public IIcon func_149691_a(int side, int meta)
/*     */   {
/* 162 */     if (side == 1) {
/* 163 */       if (this.texturesTop.containsKey(Integer.valueOf(meta))) {
/* 164 */         return (IIcon)this.texturesTop.get(Integer.valueOf(meta));
/*     */       }
/* 166 */       return (IIcon)this.texturesTop.get(Integer.valueOf(0));
/*     */     }
/* 168 */     if (side == 0) {
/* 169 */       if (this.texturesBottom.containsKey(Integer.valueOf(meta))) {
/* 170 */         return (IIcon)this.texturesBottom.get(Integer.valueOf(meta));
/*     */       }
/* 172 */       return (IIcon)this.texturesBottom.get(Integer.valueOf(0));
/*     */     }
/*     */     
/* 175 */     if (this.texturesSide.containsKey(Integer.valueOf(meta))) {
/* 176 */       return (IIcon)this.texturesSide.get(Integer.valueOf(meta));
/*     */     }
/* 178 */     return (IIcon)this.texturesSide.get(Integer.valueOf(0));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void func_149666_a(Item item, CreativeTabs par2CreativeTabs, List par3List)
/*     */   {
/* 187 */     for (Iterator i$ = this.texturesSide.keySet().iterator(); i$.hasNext();) { int meta = ((Integer)i$.next()).intValue();
/* 188 */       par3List.add(new ItemStack(item, 1, meta));
/*     */     }
/*     */   }
/*     */   
/*     */   public void func_149651_a(net.minecraft.client.renderer.texture.IIconRegister iconRegister)
/*     */   {
/* 194 */     for (Iterator i$ = this.textureTopNames.keySet().iterator(); i$.hasNext();) { int meta = ((Integer)i$.next()).intValue();
/* 195 */       this.texturesTop.put(Integer.valueOf(meta), MillCommonUtilities.getIcon(iconRegister, (String)this.textureTopNames.get(Integer.valueOf(meta))));
/*     */     }
/* 197 */     for (Iterator i$ = this.textureBottomNames.keySet().iterator(); i$.hasNext();) { int meta = ((Integer)i$.next()).intValue();
/* 198 */       this.texturesBottom.put(Integer.valueOf(meta), MillCommonUtilities.getIcon(iconRegister, (String)this.textureBottomNames.get(Integer.valueOf(meta))));
/*     */     }
/* 200 */     for (Iterator i$ = this.textureSideNames.keySet().iterator(); i$.hasNext();) { int meta = ((Integer)i$.next()).intValue();
/* 201 */       this.texturesSide.put(Integer.valueOf(meta), MillCommonUtilities.getIcon(iconRegister, (String)this.textureSideNames.get(Integer.valueOf(meta))));
/*     */     }
/*     */   }
/*     */   
/*     */   public void registerName(int meta, String name) {
/* 206 */     this.names.put(Integer.valueOf(meta), name);
/*     */   }
/*     */   
/*     */   public void registerTexture(int meta, String name) {
/* 210 */     this.textureTopNames.put(Integer.valueOf(meta), name);
/* 211 */     this.textureBottomNames.put(Integer.valueOf(meta), name);
/* 212 */     this.textureSideNames.put(Integer.valueOf(meta), name);
/*     */   }
/*     */   
/*     */   public void registerTexture(int meta, String top, String bottom, String side) {
/* 216 */     this.textureTopNames.put(Integer.valueOf(meta), top);
/* 217 */     this.textureBottomNames.put(Integer.valueOf(meta), bottom);
/* 218 */     this.textureSideNames.put(Integer.valueOf(meta), side);
/*     */   }
/*     */   
/*     */ 
/*     */   public void func_149674_a(World world, int i, int j, int k, java.util.Random random)
/*     */   {
/* 224 */     int meta = world.func_72805_g(i, j, k);
/*     */     
/* 226 */     if ((this == Mill.earth_decoration) && (meta == 0)) {
/* 227 */       if ((world.func_72957_l(i, j + 1, k) >= 15) && 
/* 228 */         (MillCommonUtilities.chanceOn(5))) {
/* 229 */         MillCommonUtilities.setBlockAndMetadata(world, i, j, k, Mill.stone_decoration, 1, true, false);
/*     */       }
/*     */     }
/* 232 */     else if ((this == Mill.wood_decoration) && (meta == 3) && 
/* 233 */       (world.func_72957_l(i, j + 1, k) < 7) && 
/* 234 */       (MillCommonUtilities.chanceOn(5))) {
/* 235 */       MillCommonUtilities.setBlockAndMetadata(world, i, j, k, Mill.wood_decoration, 4, true, false);
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\block\BlockDecorative.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */