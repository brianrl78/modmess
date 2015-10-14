/*     */ package org.millenaire.common.block;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.BlockChest;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.passive.EntityOcelot;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.player.InventoryPlayer;
/*     */ import net.minecraft.inventory.ContainerChest;
/*     */ import net.minecraft.inventory.IInventory;
/*     */ import net.minecraft.inventory.InventoryLargeChest;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.tileentity.TileEntity;
/*     */ import net.minecraft.tileentity.TileEntityChest;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraftforge.common.util.ForgeDirection;
/*     */ import org.millenaire.client.network.ClientSender;
/*     */ import org.millenaire.common.TileEntityMillChest;
/*     */ import org.millenaire.common.TileEntityMillChest.InventoryMillLargeChest;
/*     */ import org.millenaire.common.forge.Mill;
/*     */ 
/*     */ public class BlockMillChest extends BlockChest
/*     */ {
/*     */   public static ContainerChest createContainer(World world, int i, int j, int k, EntityPlayer entityplayer)
/*     */   {
/*  30 */     TileEntityMillChest lockedchest = (TileEntityMillChest)world.func_147438_o(i, j, k);
/*     */     
/*  32 */     IInventory chest = getInventory(lockedchest, world, i, j, k);
/*     */     
/*  34 */     return new ContainerChest(entityplayer.field_71071_by, chest);
/*     */   }
/*     */   
/*     */   private static boolean func_149953_o(World p_149953_0_, int p_149953_1_, int p_149953_2_, int p_149953_3_)
/*     */   {
/*  39 */     Iterator iterator = p_149953_0_.func_72872_a(EntityOcelot.class, AxisAlignedBB.func_72330_a(p_149953_1_, p_149953_2_ + 1, p_149953_3_, p_149953_1_ + 1, p_149953_2_ + 2, p_149953_3_ + 1)).iterator();
/*     */     
/*     */     EntityOcelot entityocelot;
/*     */     do
/*     */     {
/*  44 */       if (!iterator.hasNext()) {
/*  45 */         return false;
/*     */       }
/*     */       
/*  48 */       Entity entity = (Entity)iterator.next();
/*  49 */       entityocelot = (EntityOcelot)entity;
/*  50 */     } while (!entityocelot.func_70906_o());
/*     */     
/*  52 */     return true;
/*     */   }
/*     */   
/*     */   public static IInventory getInventory(TileEntityMillChest lockedchest, World world, int i, int j, int k)
/*     */   {
/*  57 */     String largename = lockedchest.getInvLargeName();
/*     */     
/*  59 */     IInventory chest = lockedchest;
/*     */     
/*  61 */     Block block = world.func_147439_a(i, j, k);
/*     */     
/*  63 */     if (world.func_147439_a(i - 1, j, k) == block) {
/*  64 */       chest = new InventoryLargeChest(largename, (TileEntityChest)world.func_147438_o(i - 1, j, k), chest);
/*     */     }
/*  66 */     if (world.func_147439_a(i + 1, j, k) == block) {
/*  67 */       chest = new InventoryLargeChest(largename, chest, (TileEntityChest)world.func_147438_o(i + 1, j, k));
/*     */     }
/*  69 */     if (world.func_147439_a(i, j, k - 1) == block) {
/*  70 */       chest = new InventoryLargeChest(largename, (TileEntityChest)world.func_147438_o(i, j, k - 1), chest);
/*     */     }
/*  72 */     if (world.func_147439_a(i, j, k + 1) == block) {
/*  73 */       chest = new InventoryLargeChest(largename, chest, (TileEntityChest)world.func_147438_o(i, j, k + 1));
/*     */     }
/*     */     
/*  76 */     return chest;
/*     */   }
/*     */   
/*     */   public BlockMillChest() {
/*  80 */     super(2);
/*     */   }
/*     */   
/*     */   public BlockMillChest(int blockID) {
/*  84 */     super(0);
/*  85 */     func_149647_a(Mill.tabMillenaire);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void func_149749_a(World p_149749_1_, int p_149749_2_, int p_149749_3_, int p_149749_4_, Block p_149749_5_, int p_149749_6_) {}
/*     */   
/*     */ 
/*     */   public TileEntity func_149915_a(World world, int p_149915_2_)
/*     */   {
/*  95 */     return new TileEntityMillChest();
/*     */   }
/*     */   
/*     */   public TileEntity createTileEntity(World world, int metadata)
/*     */   {
/* 100 */     return new TileEntityMillChest();
/*     */   }
/*     */   
/*     */   public IInventory func_149951_m(World p_149951_1_, int p_149951_2_, int p_149951_3_, int p_149951_4_)
/*     */   {
/* 105 */     TileEntityMillChest lockedchest = (TileEntityMillChest)p_149951_1_.func_147438_o(p_149951_2_, p_149951_3_, p_149951_4_);
/*     */     
/* 107 */     IInventory chest = lockedchest;
/*     */     
/* 109 */     if (lockedchest == null)
/* 110 */       return null;
/* 111 */     if (p_149951_1_.isSideSolid(p_149951_2_, p_149951_3_ + 1, p_149951_4_, ForgeDirection.DOWN))
/* 112 */       return null;
/* 113 */     if (func_149953_o(p_149951_1_, p_149951_2_, p_149951_3_, p_149951_4_))
/* 114 */       return null;
/* 115 */     if ((p_149951_1_.func_147439_a(p_149951_2_ - 1, p_149951_3_, p_149951_4_) == this) && ((p_149951_1_.isSideSolid(p_149951_2_ - 1, p_149951_3_ + 1, p_149951_4_, ForgeDirection.DOWN)) || (func_149953_o(p_149951_1_, p_149951_2_ - 1, p_149951_3_, p_149951_4_))))
/*     */     {
/* 117 */       return null; }
/* 118 */     if ((p_149951_1_.func_147439_a(p_149951_2_ + 1, p_149951_3_, p_149951_4_) == this) && ((p_149951_1_.isSideSolid(p_149951_2_ + 1, p_149951_3_ + 1, p_149951_4_, ForgeDirection.DOWN)) || (func_149953_o(p_149951_1_, p_149951_2_ + 1, p_149951_3_, p_149951_4_))))
/*     */     {
/* 120 */       return null; }
/* 121 */     if ((p_149951_1_.func_147439_a(p_149951_2_, p_149951_3_, p_149951_4_ - 1) == this) && ((p_149951_1_.isSideSolid(p_149951_2_, p_149951_3_ + 1, p_149951_4_ - 1, ForgeDirection.DOWN)) || (func_149953_o(p_149951_1_, p_149951_2_, p_149951_3_, p_149951_4_ - 1))))
/*     */     {
/* 123 */       return null; }
/* 124 */     if ((p_149951_1_.func_147439_a(p_149951_2_, p_149951_3_, p_149951_4_ + 1) == this) && ((p_149951_1_.isSideSolid(p_149951_2_, p_149951_3_ + 1, p_149951_4_ + 1, ForgeDirection.DOWN)) || (func_149953_o(p_149951_1_, p_149951_2_, p_149951_3_, p_149951_4_ + 1))))
/*     */     {
/* 126 */       return null;
/*     */     }
/* 128 */     String largename = lockedchest.getInvLargeName();
/*     */     
/* 130 */     if (p_149951_1_.func_147439_a(p_149951_2_ - 1, p_149951_3_, p_149951_4_) == this) {
/* 131 */       chest = new TileEntityMillChest.InventoryMillLargeChest(largename, (TileEntityChest)p_149951_1_.func_147438_o(p_149951_2_ - 1, p_149951_3_, p_149951_4_), lockedchest);
/*     */     }
/*     */     
/* 134 */     if (p_149951_1_.func_147439_a(p_149951_2_ + 1, p_149951_3_, p_149951_4_) == this) {
/* 135 */       chest = new TileEntityMillChest.InventoryMillLargeChest(largename, lockedchest, (TileEntityChest)p_149951_1_.func_147438_o(p_149951_2_ + 1, p_149951_3_, p_149951_4_));
/*     */     }
/*     */     
/* 138 */     if (p_149951_1_.func_147439_a(p_149951_2_, p_149951_3_, p_149951_4_ - 1) == this) {
/* 139 */       chest = new TileEntityMillChest.InventoryMillLargeChest(largename, (TileEntityChest)p_149951_1_.func_147438_o(p_149951_2_, p_149951_3_, p_149951_4_ - 1), lockedchest);
/*     */     }
/*     */     
/* 142 */     if (p_149951_1_.func_147439_a(p_149951_2_, p_149951_3_, p_149951_4_ + 1) == this) {
/* 143 */       chest = new TileEntityMillChest.InventoryMillLargeChest(largename, lockedchest, (TileEntityChest)p_149951_1_.func_147438_o(p_149951_2_, p_149951_3_, p_149951_4_ + 1));
/*     */     }
/*     */     
/* 146 */     return chest;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean func_149727_a(World world, int i, int j, int k, EntityPlayer entityplayer, int par6, float par7, float par8, float par9)
/*     */   {
/* 152 */     if (world.field_72995_K) {
/* 153 */       ClientSender.activateMillChest(entityplayer, new org.millenaire.common.Point(i, j, k));
/*     */     }
/* 155 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   public void func_149699_a(World world, int i, int j, int k, EntityPlayer entityplayer)
/*     */   {
/* 161 */     if ((entityplayer.field_71071_by.func_70448_g() != null) && (entityplayer.field_71071_by.func_70448_g().func_77973_b() != Mill.summoningWand)) {
/* 162 */       super.func_149699_a(world, i, j, k, entityplayer);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public int func_149745_a(Random random)
/*     */   {
/* 169 */     return 0;
/*     */   }
/*     */   
/*     */   public boolean unifyMillChests(World p_149952_1_, int p_149952_2_, int p_149952_3_, int p_149952_4_) {
/* 173 */     return p_149952_1_.func_147439_a(p_149952_2_, p_149952_3_, p_149952_4_) == this;
/*     */   }
/*     */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\block\BlockMillChest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */