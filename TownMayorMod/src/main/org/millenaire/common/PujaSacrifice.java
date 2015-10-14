/*     */ package org.millenaire.common;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import net.minecraft.enchantment.Enchantment;
/*     */ import net.minecraft.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemArmor;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.nbt.NBTTagList;
/*     */ import org.millenaire.common.building.Building;
/*     */ import org.millenaire.common.forge.Mill;
/*     */ 
/*     */ public class PujaSacrifice implements net.minecraft.inventory.IInventory
/*     */ {
/*     */   public static final int TOOL = 1;
/*     */   public static final int ARMOUR = 2;
/*     */   public static final int HELMET = 3;
/*     */   public static final int BOOTS = 4;
/*     */   public static final int SWORD_AXE = 5;
/*     */   public static final int SWORD = 6;
/*     */   public static final int BOW = 7;
/*     */   public static final int UNBREAKABLE = 8;
/*     */   
/*     */   public static class PrayerTarget
/*     */   {
/*     */     public final Enchantment enchantment;
/*     */     public final String mouseOver;
/*     */     public final int startX;
/*     */     public final int startY;
/*     */     public final int startXact;
/*     */     public final int startYact;
/*     */     public final int toolType;
/*     */     
/*     */     public PrayerTarget(Enchantment enchantment, String mouseOver, int startX, int startY, int startXact, int startYact, int toolType)
/*     */     {
/*  39 */       this.enchantment = enchantment;
/*  40 */       this.mouseOver = mouseOver;
/*  41 */       this.startX = startX;
/*  42 */       this.startY = startY;
/*  43 */       this.startXact = startXact;
/*  44 */       this.startYact = startYact;
/*  45 */       this.toolType = toolType;
/*     */     }
/*     */     
/*     */     public boolean validForItem(Item item) {
/*  49 */       return PujaSacrifice.validForItem(this.toolType, item);
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
/*     */ 
/*     */ 
/*  63 */   public static PrayerTarget[] PUJA_TARGETS = { new PrayerTarget(Enchantment.field_77349_p, "pujas.god0", 0, 188, 46, 188, 1), new PrayerTarget(Enchantment.field_77347_r, "pujas.god1", 0, 205, 46, 205, 1), new PrayerTarget(Enchantment.field_77346_s, "pujas.god2", 0, 222, 46, 222, 1), new PrayerTarget(Enchantment.field_77348_q, "pujas.god3", 0, 239, 46, 239, 1) };
/*     */   
/*     */ 
/*     */ 
/*  67 */   public static PrayerTarget[] MAYAN_TARGETS = { new PrayerTarget(Enchantment.field_77332_c, "mayan.god0", 0, 188, 120, 188, 2), new PrayerTarget(Enchantment.field_77329_d, "mayan.god1", 20, 188, 140, 188, 2), new PrayerTarget(Enchantment.field_77327_f, "mayan.god2", 40, 188, 160, 188, 2), new PrayerTarget(Enchantment.field_77328_g, "mayan.god3", 60, 188, 180, 188, 2), new PrayerTarget(Enchantment.field_92091_k, "mayan.god4", 80, 188, 200, 188, 2), new PrayerTarget(Enchantment.field_77340_h, "mayan.god5", 100, 188, 120, 188, 3), new PrayerTarget(Enchantment.field_77341_i, "mayan.god6", 0, 208, 120, 208, 3), new PrayerTarget(Enchantment.field_77330_e, "mayan.god7", 20, 208, 140, 208, 4), new PrayerTarget(Enchantment.field_77338_j, "mayan.god8", 40, 208, 160, 208, 5), new PrayerTarget(Enchantment.field_77339_k, "mayan.god9", 0, 188, 120, 188, 5), new PrayerTarget(Enchantment.field_77336_l, "mayan.god10", 80, 188, 200, 188, 5), new PrayerTarget(Enchantment.field_77337_m, "mayan.god11", 60, 208, 180, 208, 6), new PrayerTarget(Enchantment.field_77334_n, "mayan.god12", 20, 188, 140, 188, 6), new PrayerTarget(Enchantment.field_77335_o, "mayan.god13", 80, 208, 200, 208, 6), new PrayerTarget(Enchantment.field_77345_t, "mayan.god14", 40, 208, 160, 208, 7), new PrayerTarget(Enchantment.field_77344_u, "mayan.god15", 60, 208, 180, 208, 7), new PrayerTarget(Enchantment.field_77343_v, "mayan.god16", 20, 188, 140, 188, 7), new PrayerTarget(Enchantment.field_77342_w, "mayan.god17", 80, 208, 200, 208, 7), new PrayerTarget(Enchantment.field_77347_r, "mayan.god18", 100, 208, 220, 208, 8) };
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
/*     */ 
/*     */ 
/*  86 */   public static int PUJA_DURATION = 30;
/*     */   private ItemStack[] items;
/*     */   
/*  89 */   public static boolean validForItem(int type, Item item) { if (type == 1)
/*  90 */       return ((item instanceof net.minecraft.item.ItemSpade)) || ((item instanceof net.minecraft.item.ItemAxe)) || ((item instanceof net.minecraft.item.ItemPickaxe));
/*  91 */     if (type == 2)
/*  92 */       return item instanceof ItemArmor;
/*  93 */     if (type == 3)
/*  94 */       return ((item instanceof ItemArmor)) && (((ItemArmor)item).field_77881_a == 0);
/*  95 */     if (type == 4)
/*  96 */       return ((item instanceof ItemArmor)) && (((ItemArmor)item).field_77881_a == 3);
/*  97 */     if (type == 5)
/*  98 */       return ((item instanceof net.minecraft.item.ItemSword)) || ((item instanceof net.minecraft.item.ItemAxe));
/*  99 */     if (type == 6)
/* 100 */       return item instanceof net.minecraft.item.ItemSword;
/* 101 */     if (type == 7)
/* 102 */       return item instanceof net.minecraft.item.ItemBow;
/* 103 */     if (type == 8) {
/* 104 */       return ((item instanceof net.minecraft.item.ItemSword)) || ((item instanceof ItemArmor)) || ((item instanceof net.minecraft.item.ItemBow));
/*     */     }
/* 106 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/* 111 */   public PrayerTarget currentTarget = null;
/*     */   
/* 113 */   public int offeringProgress = 0;
/*     */   
/* 115 */   public int offeringNeeded = 1;
/*     */   
/* 117 */   public short pujaProgress = 0;
/* 118 */   public Building temple = null;
/* 119 */   public MillVillager priest = null;
/* 120 */   public short type = 0;
/*     */   public static final short PUJA = 0;
/*     */   public static final short MAYAN = 1;
/*     */   
/*     */   public PujaSacrifice(Building temple, NBTTagCompound tag) {
/* 125 */     this.temple = temple;
/*     */     
/* 127 */     if (temple.location.tags.contains("sacrifices")) {
/* 128 */       this.type = 1;
/*     */     }
/*     */     
/* 131 */     readFromNBT(tag);
/*     */   }
/*     */   
/*     */   public PujaSacrifice(Building temple, short type) {
/* 135 */     this.temple = temple;
/* 136 */     this.items = new ItemStack[func_70302_i_()];
/* 137 */     this.type = type;
/*     */   }
/*     */   
/*     */   public void calculateOfferingsNeeded()
/*     */   {
/* 142 */     this.offeringNeeded = 0;
/*     */     
/* 144 */     if ((this.items[4] == null) || (this.currentTarget == null)) {
/* 145 */       return;
/*     */     }
/*     */     
/* 148 */     ItemStack tool = this.items[4];
/*     */     
/* 150 */     if (EnchantmentHelper.func_77506_a(this.currentTarget.enchantment.field_77352_x, tool) >= this.currentTarget.enchantment.func_77325_b()) {
/* 151 */       return;
/*     */     }
/*     */     
/* 154 */     if (!this.currentTarget.enchantment.func_92089_a(tool)) {
/* 155 */       return;
/*     */     }
/*     */     
/* 158 */     int nbother = 0;
/* 159 */     java.util.Iterator i$; if (tool.func_77948_v()) {
/* 160 */       NBTTagList nbttaglist = tool.func_77986_q();
/* 161 */       nbother = nbttaglist.func_74745_c();
/*     */       
/*     */ 
/* 164 */       java.util.Map<Integer, Integer> existingEnchantments = EnchantmentHelper.func_82781_a(tool);
/*     */       
/* 166 */       for (i$ = existingEnchantments.keySet().iterator(); i$.hasNext();) { int enchId = ((Integer)i$.next()).intValue();
/* 167 */         if ((enchId != this.currentTarget.enchantment.field_77352_x) && (!Enchantment.field_77331_b[enchId].func_77326_a(this.currentTarget.enchantment))) {
/* 168 */           return;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 173 */     int currentLevel = EnchantmentHelper.func_77506_a(this.currentTarget.enchantment.field_77352_x, tool);
/*     */     
/* 175 */     if (currentLevel > 0) {
/* 176 */       nbother--;
/*     */     }
/*     */     
/* 179 */     int cost = 50 + this.currentTarget.enchantment.func_77321_a(currentLevel + 1) * 10;
/*     */     
/* 181 */     cost *= (nbother / 2 + 1);
/*     */     
/* 183 */     if (MLN.LogPujas >= 2) {
/* 184 */       MLN.minor(this, "Offering needed: " + cost);
/*     */     }
/*     */     
/* 187 */     this.offeringNeeded = cost;
/*     */   }
/*     */   
/*     */   public boolean canPray()
/*     */   {
/* 192 */     if (this.offeringNeeded <= this.offeringProgress) {
/* 193 */       return false;
/*     */     }
/*     */     
/* 196 */     if (this.items[0] == null) {
/* 197 */       return false;
/*     */     }
/*     */     
/* 200 */     return true;
/*     */   }
/*     */   
/*     */   public void changeEnchantment(int i)
/*     */   {
/* 205 */     if (this.currentTarget == getTargets().get(i)) {
/* 206 */       return;
/*     */     }
/*     */     
/* 209 */     this.currentTarget = ((PrayerTarget)getTargets().get(i));
/* 210 */     this.offeringProgress = 0;
/* 211 */     calculateOfferingsNeeded();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void func_70305_f() {}
/*     */   
/*     */ 
/*     */   private void completeOffering()
/*     */   {
/* 221 */     int currentlevel = EnchantmentHelper.func_77506_a(this.currentTarget.enchantment.field_77352_x, this.items[4]);
/*     */     
/* 223 */     if (currentlevel == 0) {
/* 224 */       this.items[4].func_77966_a(this.currentTarget.enchantment, 1);
/*     */     } else {
/* 226 */       NBTTagList enchList = this.items[4].func_77986_q();
/*     */       
/* 228 */       for (int i = 0; i < enchList.func_74745_c(); i++) {
/* 229 */         short id = enchList.func_150305_b(i).func_74765_d("id");
/*     */         
/* 231 */         if (id == this.currentTarget.enchantment.field_77352_x) {
/* 232 */           enchList.func_150305_b(i).func_74777_a("lvl", (short)(currentlevel + 1));
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 238 */     this.offeringProgress = 0;
/* 239 */     calculateOfferingsNeeded();
/*     */     
/* 241 */     this.temple.getTownHall().requestSave("Puja/sacrifice offering complete");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ItemStack func_70298_a(int slot, int nb)
/*     */   {
/* 251 */     if (this.items[slot] != null) {
/* 252 */       if (this.items[slot].field_77994_a <= nb) {
/* 253 */         ItemStack itemstack = this.items[slot];
/* 254 */         this.items[slot] = null;
/* 255 */         return itemstack;
/*     */       }
/*     */       
/* 258 */       ItemStack itemstack1 = this.items[slot].func_77979_a(nb);
/*     */       
/* 260 */       if (this.items[slot].field_77994_a == 0) {
/* 261 */         this.items[slot] = null;
/*     */       }
/*     */       
/* 264 */       return itemstack1;
/*     */     }
/* 266 */     return null;
/*     */   }
/*     */   
/*     */   private void endPuja()
/*     */   {
/* 271 */     ItemStack offer = this.items[0];
/*     */     
/* 273 */     if (offer == null) {
/* 274 */       return;
/*     */     }
/*     */     
/* 277 */     int offerValue = getOfferingValue(offer);
/*     */     
/* 279 */     this.offeringProgress += offerValue;
/*     */     
/* 281 */     offer.field_77994_a -= 1;
/*     */     
/* 283 */     if (offer.field_77994_a == 0) {
/* 284 */       this.items[0] = null;
/*     */     }
/*     */     
/* 287 */     if (this.offeringProgress >= this.offeringNeeded) {
/* 288 */       completeOffering();
/*     */     }
/*     */   }
/*     */   
/*     */   public String func_145825_b()
/*     */   {
/* 294 */     return MLN.string("pujas.invanme");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int func_70297_j_()
/*     */   {
/* 303 */     return 64;
/*     */   }
/*     */   
/*     */   public int getOfferingProgressScaled(int scale) {
/* 307 */     if (this.offeringNeeded == 0) {
/* 308 */       return 0;
/*     */     }
/*     */     
/* 311 */     return this.offeringProgress * scale / this.offeringNeeded;
/*     */   }
/*     */   
/*     */   public int getOfferingValue(ItemStack is)
/*     */   {
/* 316 */     if (this.type == 0) {
/* 317 */       return getOfferingValuePuja(is);
/*     */     }
/*     */     
/* 320 */     if (this.type == 1) {
/* 321 */       return getOfferingValueMayan(is);
/*     */     }
/*     */     
/* 324 */     return 0;
/*     */   }
/*     */   
/*     */   public int getOfferingValueMayan(ItemStack is)
/*     */   {
/* 329 */     if (is.func_77973_b() == Items.field_151144_bL) {
/* 330 */       return 4096;
/*     */     }
/*     */     
/* 333 */     if (is.func_77973_b() == Items.field_151073_bk) {
/* 334 */       return 384;
/*     */     }
/*     */     
/* 337 */     if (is.func_77973_b() == Items.field_151072_bj) {
/* 338 */       return 64;
/*     */     }
/*     */     
/* 341 */     if (is.func_77973_b() == Mill.cacauhaa) {
/* 342 */       return 64;
/*     */     }
/*     */     
/* 345 */     if (is.func_77973_b() == Items.field_151076_bf) {
/* 346 */       return 1;
/*     */     }
/*     */     
/* 349 */     if (is.func_77973_b() == Items.field_151082_bd) {
/* 350 */       return 1;
/*     */     }
/*     */     
/* 353 */     if (is.func_77973_b() == Items.field_151147_al) {
/* 354 */       return 1;
/*     */     }
/*     */     
/* 357 */     if (is.func_77973_b() == Items.field_151115_aP) {
/* 358 */       return 1;
/*     */     }
/*     */     
/* 361 */     if (is.func_77973_b() == Items.field_151116_aA) {
/* 362 */       return 1;
/*     */     }
/*     */     
/* 365 */     if ((is.func_77973_b() == Items.field_151100_aR) && (is.func_77960_j() == 0)) {
/* 366 */       return 1;
/*     */     }
/*     */     
/* 369 */     if (is.func_77973_b() == Items.field_151123_aH) {
/* 370 */       return 1;
/*     */     }
/*     */     
/* 373 */     if (is.func_77973_b() == Items.field_151078_bh) {
/* 374 */       return 2;
/*     */     }
/*     */     
/* 377 */     if (is.func_77973_b() == Items.field_151103_aS) {
/* 378 */       return 2;
/*     */     }
/*     */     
/* 381 */     if (is.func_77973_b() == Items.field_151064_bs) {
/* 382 */       return 4;
/*     */     }
/*     */     
/* 385 */     if (is.func_77973_b() == Items.field_151016_H) {
/* 386 */       return 4;
/*     */     }
/*     */     
/* 389 */     if (is.func_77973_b() == Items.field_151070_bp) {
/* 390 */       return 4;
/*     */     }
/*     */     
/* 393 */     if (is.func_77973_b() == Items.field_151079_bi) {
/* 394 */       return 6;
/*     */     }
/*     */     
/* 397 */     return 0;
/*     */   }
/*     */   
/*     */   public int getOfferingValuePuja(ItemStack is) {
/* 401 */     if (is.func_77973_b() == Items.field_151045_i) {
/* 402 */       return 384;
/*     */     }
/*     */     
/* 405 */     if (is.func_77973_b() == Items.field_151117_aB) {
/* 406 */       return 128;
/*     */     }
/*     */     
/* 409 */     if (is.func_77973_b() == Items.field_151153_ao) {
/* 410 */       return 96;
/*     */     }
/*     */     
/* 413 */     if (is.func_77973_b() == Items.field_151043_k) {
/* 414 */       return 64;
/*     */     }
/*     */     
/* 417 */     if (is.func_77973_b() == Mill.rice) {
/* 418 */       return 8;
/*     */     }
/*     */     
/* 421 */     if (is.func_77973_b() == Mill.rasgulla) {
/* 422 */       return 64;
/*     */     }
/*     */     
/* 425 */     if ((is.func_77973_b() == Item.func_150898_a(net.minecraft.init.Blocks.field_150328_O)) || (is.func_77973_b() == Item.func_150898_a(net.minecraft.init.Blocks.field_150327_N))) {
/* 426 */       return 16;
/*     */     }
/*     */     
/* 429 */     if ((is.func_77973_b() == Item.func_150898_a(net.minecraft.init.Blocks.field_150329_H)) || (is.func_77973_b() == Items.field_151034_e)) {
/* 430 */       return 8;
/*     */     }
/*     */     
/* 433 */     if ((is.func_77973_b() == Item.func_150898_a(net.minecraft.init.Blocks.field_150325_L)) && (is.func_77960_j() == 0)) {
/* 434 */       return 8;
/*     */     }
/*     */     
/* 437 */     if (is.func_77973_b() == Items.field_151127_ba) {
/* 438 */       return 4;
/*     */     }
/*     */     
/* 441 */     return 0;
/*     */   }
/*     */   
/*     */   public int getPujaProgressScaled(int scale) {
/* 445 */     return this.pujaProgress * scale / PUJA_DURATION;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int func_70302_i_()
/*     */   {
/* 453 */     return 5;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ItemStack func_70301_a(int par1)
/*     */   {
/* 461 */     return this.items[par1];
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ItemStack func_70304_b(int par1)
/*     */   {
/* 471 */     if (this.items[par1] != null) {
/* 472 */       ItemStack itemstack = this.items[par1];
/* 473 */       this.items[par1] = null;
/* 474 */       return itemstack;
/*     */     }
/* 476 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public List<PrayerTarget> getTargets()
/*     */   {
/* 482 */     if (this.items[4] == null) {
/* 483 */       return new ArrayList();
/*     */     }
/*     */     
/* 486 */     if (this.type == 0)
/*     */     {
/* 488 */       List<PrayerTarget> targets = new ArrayList();
/*     */       
/* 490 */       for (PrayerTarget t : PUJA_TARGETS) {
/* 491 */         if (t.validForItem(this.items[4].func_77973_b())) {
/* 492 */           targets.add(t);
/*     */         }
/*     */       }
/*     */       
/* 496 */       return targets; }
/* 497 */     if (this.type == 1) {
/* 498 */       List<PrayerTarget> targets = new ArrayList();
/*     */       
/* 500 */       for (PrayerTarget t : MAYAN_TARGETS) {
/* 501 */         if (t.validForItem(this.items[4].func_77973_b())) {
/* 502 */           targets.add(t);
/*     */         }
/*     */       }
/*     */       
/* 506 */       return targets;
/*     */     }
/*     */     
/* 509 */     return new ArrayList();
/*     */   }
/*     */   
/*     */   public boolean func_145818_k_()
/*     */   {
/* 514 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isActive() {
/* 518 */     return false;
/*     */   }
/*     */   
/*     */   public boolean func_94041_b(int i, ItemStack itemstack)
/*     */   {
/* 523 */     return true;
/*     */   }
/*     */   
/*     */   public boolean func_70300_a(net.minecraft.entity.player.EntityPlayer entityplayer)
/*     */   {
/* 528 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void func_70296_d() {}
/*     */   
/*     */ 
/*     */ 
/*     */   public void func_70295_k_() {}
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean performPuja(MillVillager priest)
/*     */   {
/* 543 */     this.priest = priest;
/*     */     
/* 545 */     if (this.pujaProgress == 0)
/*     */     {
/* 547 */       boolean success = startPuja();
/*     */       
/* 549 */       if (success) {
/* 550 */         this.pujaProgress = 1;
/*     */       }
/*     */       
/* 553 */       return success;
/*     */     }
/* 555 */     if (this.pujaProgress >= PUJA_DURATION)
/*     */     {
/* 557 */       endPuja();
/*     */       
/* 559 */       this.pujaProgress = 0;
/* 560 */       return canPray();
/*     */     }
/* 562 */     this.pujaProgress = ((short)(this.pujaProgress + 1));
/* 563 */     return canPray();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void readFromNBT(NBTTagCompound par1NBTTagCompound)
/*     */   {
/* 570 */     NBTTagList nbttaglist = par1NBTTagCompound.func_150295_c("Items", 10);
/* 571 */     this.items = new ItemStack[func_70302_i_()];
/*     */     
/* 573 */     for (int i = 0; i < nbttaglist.func_74745_c(); i++) {
/* 574 */       NBTTagCompound nbttagcompound = nbttaglist.func_150305_b(i);
/* 575 */       byte byte0 = nbttagcompound.func_74771_c("Slot");
/*     */       
/* 577 */       if ((byte0 >= 0) && (byte0 < this.items.length)) {
/* 578 */         this.items[byte0] = ItemStack.func_77949_a(nbttagcompound);
/*     */       }
/*     */     }
/*     */     
/* 582 */     int enchId = par1NBTTagCompound.func_74765_d("enchantmentTarget");
/*     */     
/* 584 */     if (enchId > 0)
/*     */     {
/* 586 */       for (PrayerTarget t : getTargets()) {
/* 587 */         if (t.enchantment.field_77352_x == enchId) {
/* 588 */           this.currentTarget = t;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 593 */     if (MLN.LogPujas >= 2) {
/* 594 */       MLN.minor(this, "Reading enchantmentTarget: " + enchId + ", " + this.currentTarget);
/*     */     }
/*     */     
/* 597 */     this.offeringProgress = par1NBTTagCompound.func_74765_d("offeringProgress");
/* 598 */     this.pujaProgress = par1NBTTagCompound.func_74765_d("pujaProgress");
/*     */     
/* 600 */     calculateOfferingsNeeded();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void func_70299_a(int par1, ItemStack par2ItemStack)
/*     */   {
/* 609 */     this.items[par1] = par2ItemStack;
/*     */     
/* 611 */     if ((par2ItemStack != null) && (par2ItemStack.field_77994_a > func_70297_j_())) {
/* 612 */       par2ItemStack.field_77994_a = func_70297_j_();
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean startPuja() {
/* 617 */     int money = org.millenaire.common.core.MillCommonUtilities.countMoney(this);
/*     */     
/* 619 */     if (money == 0) {
/* 620 */       return false;
/*     */     }
/*     */     
/* 623 */     if ((this.offeringNeeded == 0) || (this.offeringProgress >= this.offeringNeeded)) {
/* 624 */       return false;
/*     */     }
/*     */     
/* 627 */     if (this.items[0] == null) {
/* 628 */       return false;
/*     */     }
/*     */     
/* 631 */     money -= 8;
/*     */     
/* 633 */     int denier = money % 64;
/* 634 */     int denier_argent = (money - denier) / 64 % 64;
/* 635 */     int denier_or = (money - denier - denier_argent * 64) / 4096;
/*     */     
/* 637 */     if (denier == 0) {
/* 638 */       this.items[1] = null;
/*     */     } else {
/* 640 */       this.items[1] = new ItemStack(Mill.denier, denier);
/*     */     }
/*     */     
/* 643 */     if (denier_argent == 0) {
/* 644 */       this.items[2] = null;
/*     */     } else {
/* 646 */       this.items[2] = new ItemStack(Mill.denier_argent, denier_argent);
/*     */     }
/*     */     
/* 649 */     if (denier_or == 0) {
/* 650 */       this.items[3] = null;
/*     */     } else {
/* 652 */       this.items[3] = new ItemStack(Mill.denier_or, denier_or);
/*     */     }
/*     */     
/* 655 */     return true;
/*     */   }
/*     */   
/*     */   public void writeToNBT(NBTTagCompound par1NBTTagCompound) {
/* 659 */     if (this.currentTarget != null) {
/* 660 */       par1NBTTagCompound.func_74777_a("enchantmentTarget", (short)this.currentTarget.enchantment.field_77352_x);
/*     */       
/* 662 */       if (MLN.LogPujas >= 2) {
/* 663 */         MLN.minor(this, "Writing enchantmentTarget: " + this.currentTarget.enchantment.field_77352_x + ", " + this.currentTarget);
/*     */       }
/*     */     }
/*     */     
/* 667 */     par1NBTTagCompound.func_74777_a("offeringProgress", (short)this.offeringProgress);
/* 668 */     par1NBTTagCompound.func_74777_a("pujaProgress", this.pujaProgress);
/* 669 */     NBTTagList nbttaglist = new NBTTagList();
/*     */     
/* 671 */     for (int i = 0; i < this.items.length; i++) {
/* 672 */       if (this.items[i] != null) {
/* 673 */         NBTTagCompound nbttagcompound = new NBTTagCompound();
/* 674 */         nbttagcompound.func_74774_a("Slot", (byte)i);
/* 675 */         this.items[i].func_77955_b(nbttagcompound);
/* 676 */         nbttaglist.func_74742_a(nbttagcompound);
/*     */       }
/*     */     }
/*     */     
/* 680 */     par1NBTTagCompound.func_74782_a("Items", nbttaglist);
/*     */   }
/*     */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\PujaSacrifice.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */