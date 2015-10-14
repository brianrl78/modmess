/*     */ package org.millenaire.common;
/*     */ 
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.pathfinding.PathPoint;
/*     */ import net.minecraft.tileentity.TileEntity;
/*     */ import net.minecraft.tileentity.TileEntityBrewingStand;
/*     */ import net.minecraft.tileentity.TileEntityChest;
/*     */ import net.minecraft.tileentity.TileEntityDispenser;
/*     */ import net.minecraft.tileentity.TileEntityFurnace;
/*     */ import net.minecraft.tileentity.TileEntitySign;
/*     */ import net.minecraft.util.ChunkCoordinates;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.world.World;
/*     */ import org.millenaire.common.pathing.atomicstryker.AStarNode;
/*     */ 
/*     */ public class Point
/*     */ {
/*     */   public final double x;
/*     */   public final double y;
/*     */   public final double z;
/*     */   
/*     */   public static final Point read(NBTTagCompound nbttagcompound, String label)
/*     */   {
/*  26 */     double x = nbttagcompound.func_74769_h(label + "_xCoord");
/*  27 */     double y = nbttagcompound.func_74769_h(label + "_yCoord");
/*  28 */     double z = nbttagcompound.func_74769_h(label + "_zCoord");
/*     */     
/*  30 */     if ((x == 0.0D) && (y == 0.0D) && (z == 0.0D)) {
/*  31 */       return null;
/*     */     }
/*     */     
/*  34 */     return new Point(x, y, z);
/*     */   }
/*     */   
/*     */ 
/*     */   public Point(AStarNode node)
/*     */   {
/*  40 */     this.x = node.x;
/*  41 */     this.y = node.y;
/*  42 */     this.z = node.z;
/*     */   }
/*     */   
/*     */   public Point(double i, double j, double k) {
/*  46 */     this.x = i;
/*  47 */     this.y = j;
/*  48 */     this.z = k;
/*     */   }
/*     */   
/*     */   public Point(Entity ent) {
/*  52 */     this.x = ent.field_70165_t;
/*  53 */     this.y = ent.field_70163_u;
/*  54 */     this.z = ent.field_70161_v;
/*     */   }
/*     */   
/*     */   public Point(PathPoint pp) {
/*  58 */     this.x = pp.field_75839_a;
/*  59 */     this.y = pp.field_75837_b;
/*  60 */     this.z = pp.field_75838_c;
/*     */   }
/*     */   
/*     */   public Point(String s) {
/*  64 */     String[] scoord = s.split("/");
/*  65 */     this.x = Double.parseDouble(scoord[0]);
/*  66 */     this.y = Double.parseDouble(scoord[1]);
/*  67 */     this.z = Double.parseDouble(scoord[2]);
/*     */   }
/*     */   
/*     */   public String approximateDistanceLongString(Point p) {
/*  71 */     int dist = (int)distanceTo(p);
/*     */     
/*  73 */     if (dist < 950) {
/*  74 */       return dist / 100 * 100 + " " + MLN.string("other.metre");
/*     */     }
/*  76 */     dist = Math.round(dist / 500);
/*  77 */     if (dist % 2 == 0) {
/*  78 */       return dist / 2 + " " + MLN.string("other.kilometre");
/*     */     }
/*  80 */     return (dist - 1) / 2 + MLN.string("other.andhalf") + " " + MLN.string("other.kilometre");
/*     */   }
/*     */   
/*     */ 
/*     */   public String approximateDistanceShortString(Point p)
/*     */   {
/*  86 */     int dist = (int)distanceTo(p);
/*     */     
/*  88 */     if (dist < 950) {
/*  89 */       return dist / 100 * 100 + "m";
/*     */     }
/*  91 */     dist /= 500;
/*  92 */     if (dist % 2 == 0) {
/*  93 */       return dist / 2 + "km";
/*     */     }
/*  95 */     return (dist - 1) / 2 + ".5 km";
/*     */   }
/*     */   
/*     */ 
/*     */   public String directionTo(Point p)
/*     */   {
/* 101 */     return directionTo(p, false);
/*     */   }
/*     */   
/*     */   public String directionTo(Point p, boolean prefixed) {
/*     */     String prefix;
/*     */     String prefix;
/* 107 */     if (prefixed) {
/* 108 */       prefix = "other.tothe";
/*     */     } else {
/* 110 */       prefix = "other.";
/*     */     }
/*     */     
/* 113 */     int xdist = MathHelper.func_76128_c(p.x - this.x);
/* 114 */     int zdist = MathHelper.func_76128_c(p.z - this.z);
/*     */     String direction;
/* 116 */     if (((Math.abs(xdist) > Math.abs(zdist) * 0.6D) && (Math.abs(xdist) < Math.abs(zdist) * 1.4D)) || ((Math.abs(zdist) > Math.abs(xdist) * 0.6D) && (Math.abs(zdist) < Math.abs(xdist) * 1.4D))) { String direction;
/*     */       String direction;
/* 118 */       if (zdist > 0) {
/* 119 */         direction = prefix + "south" + "-";
/*     */       } else {
/* 121 */         direction = prefix + "north" + "-";
/*     */       }
/*     */       
/* 124 */       if (xdist > 0) {
/* 125 */         direction = direction + "east";
/*     */       } else
/* 127 */         direction = direction + "west";
/*     */     } else {
/*     */       String direction;
/* 130 */       if (Math.abs(xdist) > Math.abs(zdist)) { String direction;
/* 131 */         if (xdist > 0) {
/* 132 */           direction = prefix + "east";
/*     */         } else
/* 134 */           direction = prefix + "west";
/*     */       } else {
/*     */         String direction;
/* 137 */         if (zdist > 0) {
/* 138 */           direction = prefix + "south";
/*     */         } else {
/* 140 */           direction = prefix + "north";
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 145 */     return direction;
/*     */   }
/*     */   
/*     */ 
/*     */   public String directionToShort(Point p)
/*     */   {
/* 151 */     int xdist = MathHelper.func_76128_c(p.x - this.x);
/* 152 */     int zdist = MathHelper.func_76128_c(p.z - this.z);
/*     */     String direction;
/* 154 */     if (((Math.abs(xdist) > Math.abs(zdist) * 0.6D) && (Math.abs(xdist) < Math.abs(zdist) * 1.4D)) || ((Math.abs(zdist) > Math.abs(xdist) * 0.6D) && (Math.abs(zdist) < Math.abs(xdist) * 1.4D))) { String direction;
/*     */       String direction;
/* 156 */       if (zdist > 0) {
/* 157 */         direction = MLN.string("other.south_short");
/*     */       } else {
/* 159 */         direction = MLN.string("other.north_short");
/*     */       }
/*     */       
/* 162 */       if (xdist > 0) {
/* 163 */         direction = direction + MLN.string("other.east_short");
/*     */       } else
/* 165 */         direction = direction + MLN.string("other.west_short");
/*     */     } else {
/*     */       String direction;
/* 168 */       if (Math.abs(xdist) > Math.abs(zdist)) { String direction;
/* 169 */         if (xdist > 0) {
/* 170 */           direction = MLN.string("other.east_short");
/*     */         } else
/* 172 */           direction = MLN.string("other.west_short");
/*     */       } else {
/*     */         String direction;
/* 175 */         if (zdist > 0) {
/* 176 */           direction = MLN.string("other.south_short");
/*     */         } else {
/* 178 */           direction = MLN.string("other.north_short");
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 183 */     return direction;
/*     */   }
/*     */   
/*     */   public String distanceDirectionShort(Point p) {
/* 187 */     return MLN.string("other.directionshort", new String[] { directionToShort(p), "" + (int)horizontalDistanceTo(p) + "m" });
/*     */   }
/*     */   
/*     */   public double distanceTo(double px, double py, double pz) {
/* 191 */     double d = px - this.x;
/* 192 */     double d1 = py - this.y;
/* 193 */     double d2 = pz - this.z;
/* 194 */     return MathHelper.func_76133_a(d * d + d1 * d1 + d2 * d2);
/*     */   }
/*     */   
/*     */   public double distanceTo(Entity e) {
/* 198 */     return distanceTo(e.field_70165_t, e.field_70163_u, e.field_70161_v);
/*     */   }
/*     */   
/*     */   public double distanceTo(Point p) {
/* 202 */     if (p == null) {
/* 203 */       return -1.0D;
/*     */     }
/*     */     
/* 206 */     return distanceTo(p.x, p.y, p.z);
/*     */   }
/*     */   
/*     */   public double distanceToSquared(double px, double py, double pz) {
/* 210 */     double d = px - this.x;
/* 211 */     double d1 = py - this.y;
/* 212 */     double d2 = pz - this.z;
/* 213 */     return d * d + d1 * d1 + d2 * d2;
/*     */   }
/*     */   
/*     */   public double distanceToSquared(Entity e) {
/* 217 */     return distanceToSquared(e.field_70165_t, e.field_70163_u, e.field_70161_v);
/*     */   }
/*     */   
/*     */   public double distanceToSquared(PathPoint pp) {
/* 221 */     return distanceToSquared(pp.field_75839_a, pp.field_75837_b, pp.field_75838_c);
/*     */   }
/*     */   
/*     */   public double distanceToSquared(Point p) {
/* 225 */     return distanceToSquared(p.x, p.y, p.z);
/*     */   }
/*     */   
/*     */   public boolean equals(Object o)
/*     */   {
/* 230 */     if (this == o) {
/* 231 */       return true;
/*     */     }
/*     */     
/* 234 */     if (!(o instanceof Point)) {
/* 235 */       return false;
/*     */     }
/*     */     
/* 238 */     Point p = (Point)o;
/*     */     
/* 240 */     return (p.x == this.x) && (p.y == this.y) && (p.z == this.z);
/*     */   }
/*     */   
/*     */   public Point getAbove() {
/* 244 */     return new Point(this.x, this.y + 1.0D, this.z);
/*     */   }
/*     */   
/*     */   public Point getBelow() {
/* 248 */     return new Point(this.x, this.y - 1.0D, this.z);
/*     */   }
/*     */   
/*     */   public Block getBlock(World world) {
/* 252 */     return world.func_147439_a(getiX(), getiY(), getiZ());
/*     */   }
/*     */   
/*     */   public TileEntityBrewingStand getBrewingStand(World world) {
/* 256 */     TileEntity ent = world.func_147438_o(getiX(), getiY(), getiZ());
/*     */     
/* 258 */     if ((ent != null) && ((ent instanceof TileEntityBrewingStand))) {
/* 259 */       return (TileEntityBrewingStand)ent;
/*     */     }
/*     */     
/* 262 */     return null;
/*     */   }
/*     */   
/*     */   public TileEntityChest getChest(World world) {
/* 266 */     TileEntity ent = world.func_147438_o(getiX(), getiY(), getiZ());
/*     */     
/* 268 */     if ((ent != null) && ((ent instanceof TileEntityChest))) {
/* 269 */       return (TileEntityChest)ent;
/*     */     }
/*     */     
/* 272 */     return null;
/*     */   }
/*     */   
/*     */   public String getChunkString() {
/* 276 */     return getChunkX() + "/" + getChunkZ();
/*     */   }
/*     */   
/*     */   public int getChunkX() {
/* 280 */     return getiX() >> 4;
/*     */   }
/*     */   
/*     */   public int getChunkZ() {
/* 284 */     return getiZ() >> 4;
/*     */   }
/*     */   
/*     */   public TileEntityDispenser getDispenser(World world) {
/* 288 */     TileEntity ent = world.func_147438_o(getiX(), getiY(), getiZ());
/*     */     
/* 290 */     if ((ent != null) && ((ent instanceof TileEntityDispenser))) {
/* 291 */       return (TileEntityDispenser)ent;
/*     */     }
/*     */     
/* 294 */     return null;
/*     */   }
/*     */   
/*     */   public Point getEast() {
/* 298 */     return new Point(this.x, this.y, this.z - 1.0D);
/*     */   }
/*     */   
/*     */   public TileEntityFurnace getFurnace(World world) {
/* 302 */     TileEntity ent = world.func_147438_o(getiX(), getiY(), getiZ());
/*     */     
/* 304 */     if ((ent != null) && ((ent instanceof TileEntityFurnace))) {
/* 305 */       return (TileEntityFurnace)ent;
/*     */     }
/*     */     
/* 308 */     return null;
/*     */   }
/*     */   
/*     */   public String getIntString() {
/* 312 */     return getiX() + "/" + getiY() + "/" + getiZ();
/*     */   }
/*     */   
/*     */   public int getiX() {
/* 316 */     return MathHelper.func_76128_c(this.x);
/*     */   }
/*     */   
/*     */   public int getiY() {
/* 320 */     return MathHelper.func_76128_c(this.y);
/*     */   }
/*     */   
/*     */   public int getiZ() {
/* 324 */     return MathHelper.func_76128_c(this.z);
/*     */   }
/*     */   
/*     */   public int getMeta(World world) {
/* 328 */     return world.func_72805_g(getiX(), getiY(), getiZ());
/*     */   }
/*     */   
/*     */   public TileEntityMillChest getMillChest(World world) {
/* 332 */     TileEntity ent = world.func_147438_o(getiX(), getiY(), getiZ());
/*     */     
/* 334 */     if ((ent != null) && ((ent instanceof TileEntityMillChest))) {
/* 335 */       return (TileEntityMillChest)ent;
/*     */     }
/*     */     
/* 338 */     return null;
/*     */   }
/*     */   
/*     */   public Point getNorth() {
/* 342 */     return new Point(this.x - 1.0D, this.y, this.z);
/*     */   }
/*     */   
/*     */   public org.millenaire.common.pathing.AStarPathing.Point2D getP2D() {
/* 346 */     return new org.millenaire.common.pathing.AStarPathing.Point2D(getiX(), getiZ());
/*     */   }
/*     */   
/*     */   public TileEntityPanel getPanel(World world) {
/* 350 */     TileEntity ent = world.func_147438_o(getiX(), getiY(), getiZ());
/*     */     
/* 352 */     if ((ent != null) && ((ent instanceof TileEntityPanel))) {
/* 353 */       return (TileEntityPanel)ent;
/*     */     }
/*     */     
/* 356 */     return null;
/*     */   }
/*     */   
/*     */   public short[] getPathingCoord(int xoffset, int yoffset, int zoffset) {
/* 360 */     return new short[] { (short)(int)(this.x - xoffset), (short)(int)(this.y - yoffset), (short)(int)(this.z - zoffset) };
/*     */   }
/*     */   
/*     */   public PathPoint getPathPoint() {
/* 364 */     return new PathPoint((int)this.x, (int)this.y, (int)this.z);
/*     */   }
/*     */   
/*     */   public String getPathString() {
/* 368 */     return getiX() + "_" + getiY() + "_" + getiZ();
/*     */   }
/*     */   
/*     */   public Point getRelative(double dx, double dy, double dz) {
/* 372 */     return new Point(this.x + dx, this.y + dy, this.z + dz);
/*     */   }
/*     */   
/*     */   public TileEntitySign getSign(World world) {
/* 376 */     TileEntity ent = world.func_147438_o(getiX(), getiY(), getiZ());
/*     */     
/* 378 */     if ((ent != null) && ((ent instanceof TileEntitySign))) {
/* 379 */       return (TileEntitySign)ent;
/*     */     }
/*     */     
/* 382 */     return null;
/*     */   }
/*     */   
/*     */   public Point getSouth() {
/* 386 */     return new Point(this.x + 1.0D, this.y, this.z);
/*     */   }
/*     */   
/*     */   public TileEntity getTileEntity(World world) {
/* 390 */     return world.func_147438_o(getiX(), getiY(), getiZ());
/*     */   }
/*     */   
/*     */   public Point getWest() {
/* 394 */     return new Point(this.x, this.y, this.z + 1.0D);
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/* 399 */     return (int)(this.x + ((int)this.y << 8) + ((int)this.z << 16));
/*     */   }
/*     */   
/*     */   public double horizontalDistanceTo(ChunkCoordinates cc) {
/* 403 */     return horizontalDistanceTo(cc.field_71574_a, cc.field_71573_c);
/*     */   }
/*     */   
/*     */   public double horizontalDistanceTo(double px, double pz) {
/* 407 */     double d = px - this.x;
/* 408 */     double d2 = pz - this.z;
/* 409 */     return MathHelper.func_76133_a(d * d + d2 * d2);
/*     */   }
/*     */   
/*     */   public double horizontalDistanceTo(Entity e) {
/* 413 */     return horizontalDistanceTo(e.field_70165_t, e.field_70161_v);
/*     */   }
/*     */   
/*     */   public double horizontalDistanceTo(PathPoint p) {
/* 417 */     if (p == null) {
/* 418 */       return 0.0D;
/*     */     }
/*     */     
/* 421 */     return horizontalDistanceTo(p.field_75839_a, p.field_75838_c);
/*     */   }
/*     */   
/*     */   public double horizontalDistanceTo(Point p) {
/* 425 */     if (p == null) {
/* 426 */       return 0.0D;
/*     */     }
/*     */     
/* 429 */     return horizontalDistanceTo(p.x, p.z);
/*     */   }
/*     */   
/*     */   public double horizontalDistanceToSquared(double px, double pz) {
/* 433 */     double d = px - this.x;
/* 434 */     double d2 = pz - this.z;
/* 435 */     return d * d + d2 * d2;
/*     */   }
/*     */   
/*     */   public double horizontalDistanceToSquared(Entity e) {
/* 439 */     return horizontalDistanceToSquared(e.field_70165_t, e.field_70161_v);
/*     */   }
/*     */   
/*     */   public double horizontalDistanceToSquared(Point p) {
/* 443 */     return horizontalDistanceTo(p.x, p.z);
/*     */   }
/*     */   
/*     */   public boolean isBlockPassable(World world) {
/* 447 */     return org.millenaire.common.pathing.atomicstryker.AStarStatic.isPassableBlock(world, getiX(), getiY(), getiZ(), MillVillager.DEFAULT_JPS_CONFIG);
/*     */   }
/*     */   
/*     */   public int manhattanDistance(Point p) {
/* 451 */     return (int)(Math.abs(this.x - p.x) + Math.abs(this.z - p.z));
/*     */   }
/*     */   
/*     */   public boolean sameBlock(PathPoint p) {
/* 455 */     if (p == null) {
/* 456 */       return false;
/*     */     }
/*     */     
/* 459 */     return (getiX() == p.field_75839_a) && (getiY() == p.field_75837_b) && (getiZ() == p.field_75838_c);
/*     */   }
/*     */   
/*     */   public boolean sameBlock(Point p) {
/* 463 */     if (p == null) {
/* 464 */       return false;
/*     */     }
/*     */     
/* 467 */     return (getiX() == p.getiX()) && (getiY() == p.getiY()) && (getiZ() == p.getiZ());
/*     */   }
/*     */   
/*     */   public boolean sameHorizontalBlock(PathPoint p) {
/* 471 */     if (p == null) {
/* 472 */       return false;
/*     */     }
/*     */     
/* 475 */     return (getiX() == p.field_75839_a) && (getiZ() == p.field_75838_c);
/*     */   }
/*     */   
/*     */   public boolean sameHorizontalBlock(Point p) {
/* 479 */     if (p == null) {
/* 480 */       return false;
/*     */     }
/*     */     
/* 483 */     return (getiX() == p.getiX()) && (getiZ() == p.getiZ());
/*     */   }
/*     */   
/*     */   public void setBlock(World world, Block block, int meta, boolean notify, boolean sound) {
/* 487 */     org.millenaire.common.core.MillCommonUtilities.setBlockAndMetadata(world, this, block, meta, notify, sound);
/*     */   }
/*     */   
/*     */   public int squareRadiusDistance(Point p) {
/* 491 */     return (int)Math.max(Math.abs(this.x - p.x), Math.abs(this.z - p.z));
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 496 */     return Math.round(this.x * 100.0D) / 100L + "/" + Math.round(this.y * 100.0D) / 100L + "/" + Math.round(this.z * 100.0D) / 100L;
/*     */   }
/*     */   
/*     */   public void write(NBTTagCompound nbttagcompound, String label) {
/* 500 */     nbttagcompound.func_74780_a(label + "_xCoord", this.x);
/* 501 */     nbttagcompound.func_74780_a(label + "_yCoord", this.y);
/* 502 */     nbttagcompound.func_74780_a(label + "_zCoord", this.z);
/*     */   }
/*     */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\Point.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */