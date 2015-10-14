/*     */ package org.millenaire.common.building;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.BlockButton;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.tileentity.MobSpawnerBaseLogic;
/*     */ import net.minecraft.tileentity.TileEntityDispenser;
/*     */ import net.minecraft.tileentity.TileEntityMobSpawner;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.gen.feature.WorldGenTrees;
/*     */ import net.minecraft.world.gen.feature.WorldGenerator;
/*     */ import org.millenaire.common.EntityMillDecoration;
/*     */ import org.millenaire.common.InvItem;
/*     */ import org.millenaire.common.Point;
/*     */ import org.millenaire.common.VillageType;
/*     */ import org.millenaire.common.core.MillCommonUtilities;
/*     */ import org.millenaire.common.forge.Mill;
/*     */ 
/*     */ public class BuildingBlock
/*     */ {
/*  23 */   public static byte TAPESTRY = 1;
/*  24 */   public static byte OAKSPAWN = 2;
/*  25 */   public static byte PINESPAWN = 3;
/*  26 */   public static byte BIRCHSPAWN = 4;
/*  27 */   public static byte INDIANSTATUE = 5;
/*  28 */   public static byte PRESERVEGROUNDDEPTH = 6;
/*  29 */   public static byte CLEARTREE = 7;
/*  30 */   public static byte MAYANSTATUE = 8;
/*  31 */   public static byte SPAWNERSKELETON = 9;
/*  32 */   public static byte SPAWNERZOMBIE = 10;
/*  33 */   public static byte SPAWNERSPIDER = 11;
/*  34 */   public static byte SPAWNERCAVESPIDER = 12;
/*  35 */   public static byte SPAWNERCREEPER = 13;
/*  36 */   public static byte DISPENDERUNKNOWNPOWDER = 14;
/*  37 */   public static byte JUNGLESPAWN = 15;
/*  38 */   public static byte INVERTEDDOOR = 16;
/*  39 */   public static byte CLEARGROUND = 17;
/*  40 */   public static byte BYZANTINEICONSMALL = 18;
/*  41 */   public static byte BYZANTINEICONMEDIUM = 19;
/*  42 */   public static byte BYZANTINEICONLARGE = 20;
/*  43 */   public static byte PRESERVEGROUNDSURFACE = 21;
/*  44 */   public static byte SPAWNERBLAZE = 22;
/*     */   public Block block;
/*     */   
/*  47 */   public static BuildingBlock read(NBTTagCompound nbttagcompound, String label) { Point p = Point.read(nbttagcompound, label + "pos");
/*  48 */     return new BuildingBlock(p, nbttagcompound.func_74762_e(label + "bid"), nbttagcompound.func_74762_e(label + "meta"), nbttagcompound.func_74762_e(label + "special"));
/*     */   }
/*     */   
/*     */ 
/*     */   public byte meta;
/*     */   
/*     */   public Point p;
/*     */   public byte special;
/*     */   BuildingBlock(Point p, Block block)
/*     */   {
/*  58 */     this(p, block, 0);
/*     */   }
/*     */   
/*     */   public BuildingBlock(Point p, Block block, int meta) {
/*  62 */     this.p = p;
/*  63 */     this.block = block;
/*  64 */     this.meta = ((byte)meta);
/*  65 */     this.special = 0;
/*     */   }
/*     */   
/*     */   public BuildingBlock(Point p, Block block, int meta, int special) {
/*  69 */     this.p = p;
/*  70 */     this.block = block;
/*  71 */     this.meta = ((byte)meta);
/*  72 */     this.special = ((byte)special);
/*     */   }
/*     */   
/*     */   public BuildingBlock(Point p, int blockID, int meta, int special) {
/*  76 */     this.p = p;
/*  77 */     this.block = ((Block)Block.field_149771_c.func_148754_a(blockID));
/*  78 */     this.meta = ((byte)meta);
/*  79 */     this.special = ((byte)special);
/*     */   }
/*     */   
/*     */   public boolean alreadyDone(World world)
/*     */   {
/*  84 */     if (this.special != 0) {
/*  85 */       return false;
/*     */     }
/*     */     
/*  88 */     Block block = MillCommonUtilities.getBlock(world, this.p);
/*     */     
/*  90 */     if (this.block != block) {
/*  91 */       return false;
/*     */     }
/*     */     
/*  94 */     int meta = MillCommonUtilities.getBlockMeta(world, this.p);
/*     */     
/*  96 */     if (this.meta != meta) {
/*  97 */       return false;
/*     */     }
/*     */     
/* 100 */     return true;
/*     */   }
/*     */   
/*     */   public void build(World world, boolean worldGeneration, boolean wandimport)
/*     */   {
/*     */     try
/*     */     {
/* 107 */       boolean notifyBlocks = true;
/*     */       
/* 109 */       boolean playSound = (!worldGeneration) && (!wandimport);
/*     */       
/* 111 */       if ((this.special != PRESERVEGROUNDDEPTH) && (this.special != PRESERVEGROUNDSURFACE) && (this.special != CLEARTREE))
/*     */       {
/* 113 */         if ((!wandimport) || (this.block != Blocks.field_150350_a) || (MillCommonUtilities.getBlock(world, this.p) != Blocks.field_150472_an))
/*     */         {
/* 115 */           MillCommonUtilities.setBlockAndMetadata(world, this.p, this.block, this.meta, true, playSound);
/*     */         }
/*     */       }
/*     */       
/* 119 */       if ((this.special == PRESERVEGROUNDDEPTH) || (this.special == PRESERVEGROUNDSURFACE)) {
/* 120 */         Block block = MillCommonUtilities.getBlock(world, this.p);
/*     */         
/* 122 */         boolean surface = this.special == PRESERVEGROUNDSURFACE;
/*     */         
/* 124 */         Block validGroundBlock = MillCommonUtilities.getBlockIdValidGround(block, surface);
/*     */         
/* 126 */         if (validGroundBlock == null) {
/* 127 */           Point below = this.p.getBelow();
/* 128 */           Block targetblock = null;
/* 129 */           while ((targetblock == null) && (below.getiY() > 0)) {
/* 130 */             block = MillCommonUtilities.getBlock(world, below);
/* 131 */             if (MillCommonUtilities.getBlockIdValidGround(block, surface) != null) {
/* 132 */               targetblock = MillCommonUtilities.getBlockIdValidGround(block, surface);
/*     */             }
/* 134 */             below = below.getBelow();
/*     */           }
/*     */           
/* 137 */           if ((targetblock == Blocks.field_150346_d) && (worldGeneration)) {
/* 138 */             targetblock = Blocks.field_150349_c;
/* 139 */           } else if ((targetblock == Blocks.field_150349_c) && (!worldGeneration)) {
/* 140 */             targetblock = Blocks.field_150346_d;
/*     */           }
/*     */           
/* 143 */           if (targetblock == Blocks.field_150350_a) {
/* 144 */             if (worldGeneration) {
/* 145 */               targetblock = Blocks.field_150349_c;
/*     */             } else {
/* 147 */               targetblock = Blocks.field_150346_d;
/*     */             }
/*     */           }
/*     */           
/* 151 */           MillCommonUtilities.setBlockAndMetadata(world, this.p, targetblock, 0, true, playSound);
/* 152 */         } else if ((worldGeneration) && (validGroundBlock == Blocks.field_150346_d) && (MillCommonUtilities.getBlock(world, this.p.getAbove()) == null)) {
/* 153 */           MillCommonUtilities.setBlockAndMetadata(world, this.p, Blocks.field_150349_c, 0, true, playSound);
/* 154 */         } else if ((validGroundBlock != block) && ((validGroundBlock != Blocks.field_150346_d) || (block != Blocks.field_150349_c))) {
/* 155 */           MillCommonUtilities.setBlockAndMetadata(world, this.p, validGroundBlock, 0, true, playSound);
/*     */         }
/*     */         
/*     */       }
/* 159 */       else if (this.special == CLEARTREE) {
/* 160 */         Block block = MillCommonUtilities.getBlock(world, this.p);
/*     */         
/* 162 */         if ((block == Blocks.field_150364_r) || (block == Blocks.field_150362_t)) {
/* 163 */           MillCommonUtilities.setBlockAndMetadata(world, this.p, Blocks.field_150350_a, 0, true, playSound);
/*     */           
/* 165 */           Block blockBelow = MillCommonUtilities.getBlock(world, this.p.getBelow());
/*     */           
/* 167 */           Block targetBlock = MillCommonUtilities.getBlockIdValidGround(blockBelow, true);
/*     */           
/* 169 */           if ((worldGeneration) && (targetBlock == Blocks.field_150346_d)) {
/* 170 */             MillCommonUtilities.setBlock(world, this.p.getBelow(), Blocks.field_150349_c, true, playSound);
/* 171 */           } else if (targetBlock != null) {
/* 172 */             MillCommonUtilities.setBlock(world, this.p.getBelow(), targetBlock, true, playSound);
/*     */           }
/*     */         }
/*     */       }
/* 176 */       else if (this.special == CLEARGROUND)
/*     */       {
/* 178 */         if ((!wandimport) || (MillCommonUtilities.getBlock(world, this.p) != Blocks.field_150472_an)) {
/* 179 */           MillCommonUtilities.setBlockAndMetadata(world, this.p, Blocks.field_150350_a, 0, true, playSound);
/*     */         }
/*     */         
/* 182 */         Block blockBelow = MillCommonUtilities.getBlock(world, this.p.getBelow());
/*     */         
/* 184 */         Block targetBlock = MillCommonUtilities.getBlockIdValidGround(blockBelow, true);
/*     */         
/* 186 */         if ((worldGeneration) && (targetBlock == Blocks.field_150346_d)) {
/* 187 */           MillCommonUtilities.setBlock(world, this.p.getBelow(), Blocks.field_150349_c, true, playSound);
/* 188 */         } else if (targetBlock != null) {
/* 189 */           MillCommonUtilities.setBlock(world, this.p.getBelow(), targetBlock, true, playSound);
/*     */         }
/*     */       }
/* 192 */       else if (this.special == TAPESTRY) {
/* 193 */         EntityMillDecoration tapestry = EntityMillDecoration.createTapestry(world, this.p, 1);
/* 194 */         if ((tapestry.func_70518_d()) && 
/* 195 */           (!world.field_72995_K)) {
/* 196 */           world.func_72838_d(tapestry);
/*     */         }
/*     */       }
/* 199 */       else if (this.special == INDIANSTATUE) {
/* 200 */         EntityMillDecoration statue = EntityMillDecoration.createTapestry(world, this.p, 2);
/* 201 */         if ((statue.func_70518_d()) && 
/* 202 */           (!world.field_72995_K)) {
/* 203 */           world.func_72838_d(statue);
/*     */         }
/*     */       }
/* 206 */       else if (this.special == MAYANSTATUE) {
/* 207 */         EntityMillDecoration statue = EntityMillDecoration.createTapestry(world, this.p, 3);
/* 208 */         if ((statue.func_70518_d()) && 
/* 209 */           (!world.field_72995_K)) {
/* 210 */           world.func_72838_d(statue);
/*     */         }
/*     */       }
/* 213 */       else if (this.special == BYZANTINEICONSMALL) {
/* 214 */         EntityMillDecoration statue = EntityMillDecoration.createTapestry(world, this.p, 4);
/* 215 */         if ((statue.func_70518_d()) && 
/* 216 */           (!world.field_72995_K)) {
/* 217 */           world.func_72838_d(statue);
/*     */         }
/*     */       }
/* 220 */       else if (this.special == BYZANTINEICONMEDIUM) {
/* 221 */         EntityMillDecoration statue = EntityMillDecoration.createTapestry(world, this.p, 5);
/* 222 */         if ((statue.func_70518_d()) && 
/* 223 */           (!world.field_72995_K)) {
/* 224 */           world.func_72838_d(statue);
/*     */         }
/*     */       }
/* 227 */       else if (this.special == BYZANTINEICONLARGE) {
/* 228 */         EntityMillDecoration statue = EntityMillDecoration.createTapestry(world, this.p, 6);
/* 229 */         if ((statue.func_70518_d()) && 
/* 230 */           (!world.field_72995_K)) {
/* 231 */           world.func_72838_d(statue);
/*     */         }
/*     */       }
/* 234 */       else if (this.special == OAKSPAWN) {
/* 235 */         if (worldGeneration) {
/* 236 */           WorldGenerator wg = new WorldGenTrees(false);
/* 237 */           wg.func_76484_a(world, MillCommonUtilities.random, this.p.getiX(), this.p.getiY(), this.p.getiZ());
/*     */         }
/* 239 */       } else if (this.special == PINESPAWN) {
/* 240 */         if (worldGeneration) {
/* 241 */           WorldGenerator wg = new net.minecraft.world.gen.feature.WorldGenTaiga2(false);
/* 242 */           wg.func_76484_a(world, MillCommonUtilities.random, this.p.getiX(), this.p.getiY(), this.p.getiZ());
/*     */         }
/* 244 */       } else if (this.special == BIRCHSPAWN) {
/* 245 */         if (worldGeneration) {
/* 246 */           WorldGenerator wg = new net.minecraft.world.gen.feature.WorldGenForest(false, true);
/* 247 */           wg.func_76484_a(world, MillCommonUtilities.random, this.p.getiX(), this.p.getiY(), this.p.getiZ());
/*     */         }
/* 249 */       } else if (this.special == JUNGLESPAWN) {
/* 250 */         if (worldGeneration) {
/* 251 */           WorldGenerator wg = new WorldGenTrees(true, 4 + MillCommonUtilities.random.nextInt(7), 3, 3, false);
/* 252 */           wg.func_76484_a(world, MillCommonUtilities.random, this.p.getiX(), this.p.getiY(), this.p.getiZ());
/*     */         }
/* 254 */       } else if (this.special == SPAWNERSKELETON) {
/* 255 */         MillCommonUtilities.setBlockAndMetadata(world, this.p, Blocks.field_150474_ac, 0);
/* 256 */         TileEntityMobSpawner tileentitymobspawner = (TileEntityMobSpawner)this.p.getTileEntity(world);
/* 257 */         tileentitymobspawner.func_145881_a().func_98272_a("Skeleton");
/* 258 */       } else if (this.special == SPAWNERZOMBIE) {
/* 259 */         MillCommonUtilities.setBlockAndMetadata(world, this.p, Blocks.field_150474_ac, 0);
/* 260 */         TileEntityMobSpawner tileentitymobspawner = (TileEntityMobSpawner)this.p.getTileEntity(world);
/* 261 */         tileentitymobspawner.func_145881_a().func_98272_a("Zombie");
/* 262 */       } else if (this.special == SPAWNERSPIDER) {
/* 263 */         MillCommonUtilities.setBlockAndMetadata(world, this.p, Blocks.field_150474_ac, 0);
/* 264 */         TileEntityMobSpawner tileentitymobspawner = (TileEntityMobSpawner)this.p.getTileEntity(world);
/* 265 */         tileentitymobspawner.func_145881_a().func_98272_a("Spider");
/* 266 */       } else if (this.special == SPAWNERCAVESPIDER) {
/* 267 */         MillCommonUtilities.setBlockAndMetadata(world, this.p, Blocks.field_150474_ac, 0);
/* 268 */         TileEntityMobSpawner tileentitymobspawner = (TileEntityMobSpawner)this.p.getTileEntity(world);
/* 269 */         tileentitymobspawner.func_145881_a().func_98272_a("CaveSpider");
/* 270 */       } else if (this.special == SPAWNERCREEPER) {
/* 271 */         MillCommonUtilities.setBlockAndMetadata(world, this.p, Blocks.field_150474_ac, 0);
/* 272 */         TileEntityMobSpawner tileentitymobspawner = (TileEntityMobSpawner)this.p.getTileEntity(world);
/* 273 */         tileentitymobspawner.func_145881_a().func_98272_a("Creeper");
/* 274 */       } else if (this.special == SPAWNERBLAZE) {
/* 275 */         MillCommonUtilities.setBlockAndMetadata(world, this.p, Blocks.field_150474_ac, 0);
/* 276 */         TileEntityMobSpawner tileentitymobspawner = (TileEntityMobSpawner)this.p.getTileEntity(world);
/* 277 */         tileentitymobspawner.func_145881_a().func_98272_a("Blaze");
/*     */       }
/* 279 */       else if (this.special == DISPENDERUNKNOWNPOWDER) {
/* 280 */         MillCommonUtilities.setBlockAndMetadata(world, this.p, Blocks.field_150367_z, 0);
/* 281 */         TileEntityDispenser dispenser = this.p.getDispenser(world);
/* 282 */         MillCommonUtilities.putItemsInChest(dispenser, Mill.unknownPowder, 2);
/* 283 */       } else if (this.block == Blocks.field_150466_ao) {
/* 284 */         if (this.special == INVERTEDDOOR) {
/* 285 */           MillCommonUtilities.setBlockAndMetadata(world, this.p.getAbove(), this.block, 9, true, playSound);
/*     */         } else {
/* 287 */           MillCommonUtilities.setBlockAndMetadata(world, this.p.getAbove(), this.block, 8, true, playSound);
/*     */         }
/* 289 */       } else if (this.block == Blocks.field_150454_av) {
/* 290 */         if (this.special == INVERTEDDOOR) {
/* 291 */           MillCommonUtilities.setBlockAndMetadata(world, this.p.getAbove(), this.block, 9, true, playSound);
/*     */         } else {
/* 293 */           MillCommonUtilities.setBlockAndMetadata(world, this.p.getAbove(), this.block, 8, true, playSound);
/*     */         }
/* 295 */       } else if (this.block == Blocks.field_150324_C) {
/* 296 */         if ((this.meta & 0x3) == 0) {
/* 297 */           MillCommonUtilities.setBlockAndMetadata(world, this.p.getEast(), this.block, this.meta - 8, true, playSound);
/*     */         }
/* 299 */         if ((this.meta & 0x3) == 1) {
/* 300 */           MillCommonUtilities.setBlockAndMetadata(world, this.p.getSouth(), this.block, this.meta - 8, true, playSound);
/*     */         }
/* 302 */         if ((this.meta & 0x3) == 2) {
/* 303 */           MillCommonUtilities.setBlockAndMetadata(world, this.p.getWest(), this.block, this.meta - 8, true, playSound);
/*     */         }
/* 305 */         if ((this.meta & 0x3) == 3) {
/* 306 */           MillCommonUtilities.setBlockAndMetadata(world, this.p.getNorth(), this.block, this.meta - 8, true, playSound);
/*     */         }
/* 308 */       } else if (this.block == Blocks.field_150430_aB) {
/* 309 */         int newmeta = ((BlockButton)Blocks.field_150430_aB).func_149660_a(world, this.p.getiX(), this.p.getiY(), this.p.getiZ(), 0, 0.0F, 0.0F, 0.0F, 0);
/*     */         
/* 311 */         MillCommonUtilities.setBlockMetadata(world, this.p, newmeta, true);
/* 312 */       } else if (this.block == Blocks.field_150355_j) {
/* 313 */         MillCommonUtilities.notifyBlock(world, this.p);
/* 314 */       } else if (this.block == Blocks.field_150460_al) {
/* 315 */         setFurnaceMeta(world, this.p);
/* 316 */       } else if (this.block == Blocks.field_150427_aO) {
/* 317 */         Blocks.field_150427_aO.func_150000_e(world, this.p.getiX(), this.p.getiY(), this.p.getiZ());
/*     */       }
/*     */     } catch (Exception e) {
/* 320 */       org.millenaire.common.MLN.printException("Exception in BuildingBlock.build():", e);
/*     */     }
/*     */   }
/*     */   
/*     */   public void pathBuild(Building th)
/*     */   {
/* 326 */     int targetPathLevel = 0;
/*     */     
/* 328 */     for (int i = 0; i < th.villageType.pathMaterial.size(); i++) {
/* 329 */       if (((((InvItem)th.villageType.pathMaterial.get(i)).getBlock() == this.block) || ((((InvItem)th.villageType.pathMaterial.get(i)).getBlock() == Mill.path) && (this.block == Mill.pathSlab))) && (((InvItem)th.villageType.pathMaterial.get(i)).meta == this.meta))
/*     */       {
/* 331 */         targetPathLevel = i;
/*     */       }
/*     */     }
/*     */     
/* 335 */     Block currentBlock = this.p.getBlock(th.worldObj);
/* 336 */     int meta = this.p.getMeta(th.worldObj);
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
/*     */ 
/*     */ 
/*     */ 
/* 353 */     if ((currentBlock != Mill.path) && (currentBlock != Mill.pathSlab) && (MillCommonUtilities.canPathBeBuiltHere(currentBlock, meta))) {
/* 354 */       build(th.worldObj, false, false);
/* 355 */     } else if ((currentBlock == Mill.path) || (currentBlock == Mill.pathSlab)) {
/* 356 */       int currentPathLevel = Integer.MAX_VALUE;
/*     */       
/* 358 */       for (int i = 0; i < th.villageType.pathMaterial.size(); i++) {
/* 359 */         if (((InvItem)th.villageType.pathMaterial.get(i)).meta == meta) {
/* 360 */           currentPathLevel = i;
/*     */         }
/*     */       }
/*     */       
/* 364 */       if (currentPathLevel < targetPathLevel) {
/* 365 */         build(th.worldObj, false, false);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private void setFurnaceMeta(World world, Point p)
/*     */   {
/* 373 */     Block var5 = p.getRelative(0.0D, 0.0D, -1.0D).getBlock(world);
/* 374 */     Block var6 = p.getRelative(0.0D, 0.0D, 1.0D).getBlock(world);
/* 375 */     Block var7 = p.getRelative(-1.0D, 0.0D, 0.0D).getBlock(world);
/* 376 */     Block var8 = p.getRelative(1.0D, 0.0D, 0.0D).getBlock(world);
/* 377 */     byte var9 = 3;
/*     */     
/* 379 */     if ((var5.func_149662_c()) && (var5 != Blocks.field_150460_al) && (var5 != Blocks.field_150470_am) && (!var6.func_149662_c())) {
/* 380 */       var9 = 3;
/*     */     }
/*     */     
/* 383 */     if ((var6.func_149662_c()) && (var6 != Blocks.field_150460_al) && (var6 != Blocks.field_150470_am) && (!var5.func_149662_c())) {
/* 384 */       var9 = 2;
/*     */     }
/*     */     
/* 387 */     if ((var7.func_149662_c()) && (var7 != Blocks.field_150460_al) && (var7 != Blocks.field_150470_am) && (!var8.func_149662_c())) {
/* 388 */       var9 = 5;
/*     */     }
/*     */     
/* 391 */     if ((var8.func_149662_c()) && (var8 != Blocks.field_150460_al) && (var8 != Blocks.field_150470_am) && (!var7.func_149662_c())) {
/* 392 */       var9 = 4;
/*     */     }
/*     */     
/* 395 */     MillCommonUtilities.setBlockMetadata(world, p, var9);
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 400 */     return "(block: " + this.block + " meta: " + this.meta + " pos:" + this.p + ")";
/*     */   }
/*     */   
/*     */   public void write(NBTTagCompound nbttagcompound, String label) {
/* 404 */     nbttagcompound.func_74768_a(label + "bid", Block.func_149682_b(this.block));
/* 405 */     nbttagcompound.func_74768_a(label + "meta", this.meta);
/* 406 */     nbttagcompound.func_74768_a(label + "special", this.special);
/* 407 */     this.p.write(nbttagcompound, label + "pos");
/*     */   }
/*     */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\building\BuildingBlock.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */