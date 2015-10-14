/*     */ package org.millenaire.common;
/*     */ 
/*     */ import io.netty.buffer.ByteBufInputStream;
/*     */ import io.netty.buffer.ByteBufOutputStream;
/*     */ import java.util.ArrayList;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.world.World;
/*     */ import org.millenaire.common.forge.Mill;
/*     */ 
/*     */ public class EntityMillDecoration extends net.minecraft.entity.EntityHanging implements cpw.mods.fml.common.registry.IEntityAdditionalSpawnData
/*     */ {
/*     */   public static final int NORMAN_TAPESTRY = 1;
/*     */   public static final int INDIAN_STATUE = 2;
/*     */   public static final int MAYAN_STATUE = 3;
/*     */   public static final int BYZANTINE_ICON_SMALL = 4;
/*     */   public static final int BYZANTINE_ICON_MEDIUM = 5;
/*     */   public static final int BYZANTINE_ICON_LARGE = 6;
/*     */   public EnumWallDecoration millArt;
/*     */   public double clientX;
/*     */   public double clientY;
/*     */   public double clientZ;
/*     */   public int type;
/*     */   
/*     */   public static enum EnumWallDecoration
/*     */   {
/*  27 */     Griffon("Griffon", 16, 16, 0, 0, 1),  Oiseau("Oiseau", 16, 16, 16, 0, 1),  CorbeauRenard("CorbeauRenard", 32, 16, 32, 0, 1),  Serment("Serment", 80, 48, 0, 16, 1), 
/*  28 */     MortHarold("MortHarold", 64, 48, 80, 16, 1),  Drakar("Drakar", 96, 48, 144, 16, 1),  MontStMichel("MontStMichel", 48, 32, 0, 64, 1), 
/*  29 */     Bucherons("Bucherons", 48, 32, 48, 64, 1),  Cuisine("Cuisine", 48, 32, 96, 64, 1), 
/*  30 */     Flotte("Flotte", 240, 48, 0, 96, 1),  Chasse("Chasse", 96, 48, 0, 144, 1),  Siege("Siege", 256, 48, 0, 192, 1), 
/*     */     
/*     */ 
/*  33 */     Ganesh("Ganesh", 32, 48, 0, 0, 2),  Kali("Kali", 32, 48, 32, 0, 2),  Shiva("Shiva", 32, 48, 64, 0, 2),  Osiyan("Osiyan", 32, 48, 96, 0, 2), 
/*  34 */     Durga("Durga", 32, 48, 128, 0, 2), 
/*     */     
/*  36 */     MayanTeal("MayanTeal", 32, 32, 0, 48, 3),  MayanGold("MayanGold", 32, 32, 32, 48, 3), 
/*     */     
/*  38 */     LargeJesus("LargeJesus", 32, 48, 0, 80, 6),  LargeVirgin("LargeVirgin", 32, 48, 32, 80, 6),  MediumVirgin1("MediumVirgin1", 32, 32, 0, 128, 5), 
/*  39 */     MediumVirgin2("MediumVirgin2", 32, 32, 32, 128, 5),  SmallJesus("SmallJesus", 16, 16, 0, 160, 4), 
/*  40 */     SmallVirgin1("SmallVirgin1", 16, 16, 16, 160, 4),  SmallVirgin2("SmallVirgin2", 16, 16, 32, 160, 4),  SmallVirgin3("SmallVirgin3", 16, 16, 48, 160, 4);
/*     */     
/*     */ 
/*  43 */     public static final int maxArtTitleLength = "SkullAndRoses".length();
/*     */     public final String title;
/*     */     public final int sizeX;
/*     */     public final int sizeY;
/*     */     public final int offsetX;
/*     */     public final int offsetY;
/*     */     public final int type;
/*     */     
/*     */     private EnumWallDecoration(String s1, int j, int k, int l, int i1, int type)
/*     */     {
/*  53 */       this.title = s1;
/*  54 */       this.sizeX = j;
/*  55 */       this.sizeY = k;
/*  56 */       this.offsetX = l;
/*  57 */       this.offsetY = i1;
/*  58 */       this.type = type;
/*     */     }
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
/*     */   public static EntityMillDecoration createTapestry(World world, Point p, int type)
/*     */   {
/*  72 */     int orientation = guessOrientation(world, p);
/*     */     
/*  74 */     if (orientation == 0) {
/*  75 */       p = p.getWest();
/*  76 */     } else if (orientation == 1) {
/*  77 */       p = p.getSouth();
/*  78 */     } else if (orientation == 2) {
/*  79 */       p = p.getEast();
/*  80 */     } else if (orientation == 3) {
/*  81 */       p = p.getNorth();
/*     */     }
/*     */     
/*  84 */     return new EntityMillDecoration(world, p.getiX(), p.getiY(), p.getiZ(), orientation, type, true);
/*     */   }
/*     */   
/*     */   public static int guessOrientation(World world, Point p) {
/*  88 */     int i = p.getiX();
/*  89 */     int j = p.getiY();
/*  90 */     int k = p.getiZ();
/*  91 */     if (org.millenaire.common.core.MillCommonUtilities.isBlockIdSolid(world.func_147439_a(i - 1, j, k)))
/*  92 */       return 3;
/*  93 */     if (org.millenaire.common.core.MillCommonUtilities.isBlockIdSolid(world.func_147439_a(i + 1, j, k)))
/*  94 */       return 1;
/*  95 */     if (org.millenaire.common.core.MillCommonUtilities.isBlockIdSolid(world.func_147439_a(i, j, k - 1)))
/*  96 */       return 2;
/*  97 */     if (org.millenaire.common.core.MillCommonUtilities.isBlockIdSolid(world.func_147439_a(i, j, k + 1))) {
/*  98 */       return 0;
/*     */     }
/* 100 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public EntityMillDecoration(World par1World)
/*     */   {
/* 110 */     super(par1World);
/*     */   }
/*     */   
/*     */   public EntityMillDecoration(World world, int type) {
/* 114 */     this(world);
/* 115 */     this.type = type;
/*     */   }
/*     */   
/*     */   public EntityMillDecoration(World par1World, int par2, int par3, int par4, int par5) {
/* 119 */     super(par1World, par2, par3, par4, par5);
/*     */   }
/*     */   
/*     */   public EntityMillDecoration(World world, int x, int y, int z, int orientation, int type, boolean largestPossible) {
/* 123 */     this(world, x, y, z, orientation);
/*     */     
/* 125 */     this.type = type;
/*     */     
/* 127 */     ArrayList<EnumWallDecoration> arraylist = new ArrayList();
/* 128 */     EnumWallDecoration[] aenumart = EnumWallDecoration.values();
/* 129 */     int maxSize = 0;
/* 130 */     for (EnumWallDecoration enumart : aenumart) {
/* 131 */       if ((enumart.type == type) && (
/* 132 */         (!largestPossible) || (enumart.sizeX * enumart.sizeY >= maxSize))) {
/* 133 */         this.millArt = enumart;
/* 134 */         func_82328_a(orientation);
/* 135 */         if (func_70518_d()) {
/* 136 */           if ((largestPossible) && (enumart.sizeX * enumart.sizeY > maxSize)) {
/* 137 */             arraylist.clear();
/*     */           }
/* 139 */           arraylist.add(enumart);
/* 140 */           maxSize = enumart.sizeX * enumart.sizeY;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 146 */     if (arraylist.size() > 0) {
/* 147 */       this.millArt = ((EnumWallDecoration)arraylist.get(this.field_70146_Z.nextInt(arraylist.size())));
/*     */     }
/*     */     
/* 150 */     if (MLN.LogBuildingPlan >= 1) {
/* 151 */       MLN.major(this, "Creating wall decoration: " + x + "/" + y + "/" + z + "/" + orientation + "/" + type + "/" + largestPossible + ". Result: " + this.millArt.title + " picked amoung " + arraylist.size());
/*     */     }
/*     */     
/*     */ 
/* 155 */     func_82328_a(orientation);
/*     */   }
/*     */   
/*     */   public EntityMillDecoration(World world, int i, int j, int k, int l, int type, String s) {
/* 159 */     super(world, i, j, k, l);
/* 160 */     this.type = type;
/*     */     
/* 162 */     EnumWallDecoration[] aenumart = EnumWallDecoration.values();
/* 163 */     int i1 = aenumart.length;
/* 164 */     int j1 = 0;
/*     */     
/* 166 */     while (j1 < i1)
/*     */     {
/*     */ 
/* 169 */       EnumWallDecoration enumart = aenumart[j1];
/* 170 */       if (enumart.title.equals(s)) {
/* 171 */         this.millArt = enumart;
/* 172 */         break;
/*     */       }
/* 174 */       j1++;
/*     */     }
/* 176 */     func_82328_a(l);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void dropItemStack()
/*     */   {
/* 183 */     if (this.type == 1) {
/* 184 */       this.field_70170_p.func_72838_d(new net.minecraft.entity.item.EntityItem(this.field_70170_p, this.field_70165_t, this.field_70163_u, this.field_70161_v, new ItemStack(Mill.tapestry)));
/* 185 */     } else if (this.type == 2) {
/* 186 */       this.field_70170_p.func_72838_d(new net.minecraft.entity.item.EntityItem(this.field_70170_p, this.field_70165_t, this.field_70163_u, this.field_70161_v, new ItemStack(Mill.indianstatue)));
/* 187 */     } else if (this.type == 3) {
/* 188 */       this.field_70170_p.func_72838_d(new net.minecraft.entity.item.EntityItem(this.field_70170_p, this.field_70165_t, this.field_70163_u, this.field_70161_v, new ItemStack(Mill.mayanstatue)));
/* 189 */     } else if (this.type == 4) {
/* 190 */       this.field_70170_p.func_72838_d(new net.minecraft.entity.item.EntityItem(this.field_70170_p, this.field_70165_t, this.field_70163_u, this.field_70161_v, new ItemStack(Mill.byzantineiconsmall)));
/* 191 */     } else if (this.type == 5) {
/* 192 */       this.field_70170_p.func_72838_d(new net.minecraft.entity.item.EntityItem(this.field_70170_p, this.field_70165_t, this.field_70163_u, this.field_70161_v, new ItemStack(Mill.byzantineiconmedium)));
/* 193 */     } else if (this.type == 6) {
/* 194 */       this.field_70170_p.func_72838_d(new net.minecraft.entity.item.EntityItem(this.field_70170_p, this.field_70165_t, this.field_70163_u, this.field_70161_v, new ItemStack(Mill.byzantineiconlarge)));
/*     */     }
/*     */   }
/*     */   
/*     */   public int func_82330_g()
/*     */   {
/* 200 */     return this.millArt.sizeY;
/*     */   }
/*     */   
/*     */   public int func_82329_d()
/*     */   {
/* 205 */     return this.millArt.sizeX;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void func_110128_b(net.minecraft.entity.Entity p_110128_1_)
/*     */   {
/* 213 */     if ((p_110128_1_ instanceof net.minecraft.entity.player.EntityPlayer)) {
/* 214 */       net.minecraft.entity.player.EntityPlayer entityplayer = (net.minecraft.entity.player.EntityPlayer)p_110128_1_;
/*     */       
/* 216 */       if (entityplayer.field_71075_bZ.field_75098_d) {
/* 217 */         return;
/*     */       }
/*     */     }
/*     */     
/* 221 */     func_70099_a(new ItemStack(net.minecraft.init.Items.field_151159_an), 0.0F);
/*     */   }
/*     */   
/*     */ 
/*     */   public void func_70071_h_()
/*     */   {
/* 227 */     if (this.field_70170_p.field_72995_K) {
/* 228 */       this.field_70165_t = this.clientX;
/* 229 */       this.field_70163_u = this.clientY;
/* 230 */       this.field_70161_v = this.clientZ;
/* 231 */       this.field_70181_x = 0.0D;
/*     */     }
/*     */     
/* 234 */     super.func_70071_h_();
/*     */   }
/*     */   
/*     */ 
/*     */   public void func_70037_a(NBTTagCompound nbttagcompound)
/*     */   {
/* 240 */     this.type = nbttagcompound.func_74762_e("Type");
/* 241 */     String s = nbttagcompound.func_74779_i("Motive");
/*     */     
/* 243 */     for (EnumWallDecoration enumart : EnumWallDecoration.values()) {
/* 244 */       if (enumart.title.equals(s)) {
/* 245 */         this.millArt = enumart;
/*     */       }
/*     */     }
/*     */     
/* 249 */     if (this.millArt == null) {
/* 250 */       this.millArt = EnumWallDecoration.Griffon;
/*     */     }
/*     */     
/* 253 */     if (this.type == 0) {
/* 254 */       this.type = 1;
/*     */     }
/*     */     
/* 257 */     if (nbttagcompound.func_74764_b("Direction")) {
/* 258 */       this.field_82332_a = nbttagcompound.func_74771_c("Direction");
/*     */     } else {
/* 260 */       switch (nbttagcompound.func_74771_c("Dir")) {
/*     */       case 0: 
/* 262 */         this.field_82332_a = 2;
/* 263 */         break;
/*     */       case 1: 
/* 265 */         this.field_82332_a = 1;
/* 266 */         break;
/*     */       case 2: 
/* 268 */         this.field_82332_a = 0;
/* 269 */         break;
/*     */       case 3: 
/* 271 */         this.field_82332_a = 3;
/*     */       }
/*     */       
/*     */     }
/* 275 */     this.field_70165_t = nbttagcompound.func_74762_e("TileX");
/* 276 */     this.field_70163_u = nbttagcompound.func_74762_e("TileY");
/* 277 */     this.field_70161_v = nbttagcompound.func_74762_e("TileZ");
/* 278 */     func_82328_a(this.field_82332_a);
/*     */   }
/*     */   
/*     */   public void readSpawnData(io.netty.buffer.ByteBuf ds)
/*     */   {
/* 283 */     ByteBufInputStream data = new ByteBufInputStream(ds);
/*     */     try
/*     */     {
/* 286 */       this.type = data.readByte();
/* 287 */       this.field_82332_a = data.readByte();
/* 288 */       this.field_70165_t = data.readInt();
/* 289 */       this.field_70163_u = data.readInt();
/* 290 */       this.field_70161_v = data.readInt();
/*     */       
/* 292 */       String title = data.readUTF();
/*     */       
/* 294 */       for (EnumWallDecoration enumart : EnumWallDecoration.values()) {
/* 295 */         if (enumart.title.equals(title)) {
/* 296 */           this.millArt = enumart;
/*     */         }
/*     */       }
/*     */       
/* 300 */       this.clientX = data.readDouble();
/* 301 */       this.clientY = data.readDouble();
/* 302 */       this.clientZ = data.readDouble();
/* 303 */       data.close();
/*     */     } catch (java.io.IOException e) {
/* 305 */       MLN.printException("Exception for villager " + this, e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public String toString()
/*     */   {
/* 312 */     return "Tapestry (" + this.millArt.title + ")";
/*     */   }
/*     */   
/*     */   public void func_70014_b(NBTTagCompound nbttagcompound)
/*     */   {
/* 317 */     nbttagcompound.func_74768_a("Type", this.type);
/* 318 */     nbttagcompound.func_74778_a("Motive", this.millArt.title);
/* 319 */     nbttagcompound.func_74774_a("Direction", (byte)this.field_82332_a);
/* 320 */     nbttagcompound.func_74780_a("TileX", this.field_70165_t);
/* 321 */     nbttagcompound.func_74780_a("TileY", this.field_70163_u);
/* 322 */     nbttagcompound.func_74780_a("TileZ", this.field_70161_v);
/*     */     
/* 324 */     switch (this.field_82332_a) {
/*     */     case 0: 
/* 326 */       nbttagcompound.func_74774_a("Dir", (byte)2);
/* 327 */       break;
/*     */     case 1: 
/* 329 */       nbttagcompound.func_74774_a("Dir", (byte)1);
/* 330 */       break;
/*     */     case 2: 
/* 332 */       nbttagcompound.func_74774_a("Dir", (byte)0);
/* 333 */       break;
/*     */     case 3: 
/* 335 */       nbttagcompound.func_74774_a("Dir", (byte)3);
/*     */     }
/*     */   }
/*     */   
/*     */   public void writeSpawnData(io.netty.buffer.ByteBuf ds)
/*     */   {
/* 341 */     ByteBufOutputStream data = new ByteBufOutputStream(ds);
/*     */     try {
/* 343 */       data.write(this.type);
/* 344 */       data.write(this.field_82332_a);
/* 345 */       data.writeDouble(this.field_70165_t);
/* 346 */       data.writeDouble(this.field_70163_u);
/* 347 */       data.writeDouble(this.field_70161_v);
/* 348 */       data.writeUTF(this.millArt.title);
/* 349 */       data.writeDouble(this.field_70165_t);
/* 350 */       data.writeDouble(this.field_70163_u);
/* 351 */       data.writeDouble(this.field_70161_v);
/* 352 */       data.close();
/*     */     } catch (java.io.IOException e) {
/* 354 */       MLN.printException("Exception for painting " + this, e);
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\EntityMillDecoration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */