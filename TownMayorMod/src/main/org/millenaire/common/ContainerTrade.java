/*     */ package org.millenaire.common;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.inventory.Container;
/*     */ import net.minecraft.inventory.Slot;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.IIcon;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import org.millenaire.common.building.Building;
/*     */ import org.millenaire.common.core.MillCommonUtilities;
/*     */ import org.millenaire.common.forge.Mill;
/*     */ import org.millenaire.common.item.Goods;
/*     */ 
/*     */ public class ContainerTrade extends Container
/*     */ {
/*     */   public static class MerchantSlot extends Slot
/*     */   {
/*     */     public MillVillager merchant;
/*     */     public EntityPlayer player;
/*     */     public final Goods good;
/*     */     
/*     */     public MerchantSlot(MillVillager merchant, EntityPlayer player, Goods good, int xpos, int ypos)
/*     */     {
/*  26 */       super(-1, xpos, ypos);
/*  27 */       this.merchant = merchant;
/*  28 */       this.good = good;
/*  29 */       this.player = player;
/*     */     }
/*     */     
/*     */     public ItemStack func_75209_a(int i)
/*     */     {
/*  34 */       return null;
/*     */     }
/*     */     
/*     */     public boolean func_25014_f() {
/*  38 */       return false;
/*     */     }
/*     */     
/*     */     public IIcon func_75212_b()
/*     */     {
/*  43 */       return null;
/*     */     }
/*     */     
/*     */     public boolean func_75216_d()
/*     */     {
/*  48 */       return func_75211_c() != null;
/*     */     }
/*     */     
/*     */     public int func_75219_a()
/*     */     {
/*  53 */       return 0;
/*     */     }
/*     */     
/*     */     public ItemStack func_75211_c()
/*     */     {
/*  58 */       return new ItemStack(this.good.item.getItem(), Math.min(this.merchant.getHouse().countGoods(this.good.item), 99), this.good.item.meta);
/*     */     }
/*     */     
/*     */     public boolean func_75214_a(ItemStack itemstack)
/*     */     {
/*  63 */       return true;
/*     */     }
/*     */     
/*     */     public String isProblem() {
/*  67 */       if (this.merchant.getHouse().countGoods(this.good.item) < 1) {
/*  68 */         return MLN.string("ui.outofstock");
/*     */       }
/*  70 */       int playerMoney = MillCommonUtilities.countMoney(this.player.field_71071_by);
/*  71 */       if (this.merchant.getCulture().goodsByItem.containsKey(this.good.item)) {
/*  72 */         if (playerMoney < this.good.getCalculatedSellingPrice(this.merchant)) {
/*  73 */           return MLN.string("ui.missingdeniers").replace("<0>", "" + (this.good.getCalculatedSellingPrice(this.merchant) - playerMoney));
/*     */         }
/*     */       } else {
/*  76 */         MLN.error(null, "Unknown trade good: " + this.good);
/*     */       }
/*  78 */       return null;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public void func_75218_e() {}
/*     */     
/*     */ 
/*     */ 
/*     */     public void func_75215_d(ItemStack itemstack) {}
/*     */     
/*     */ 
/*     */ 
/*     */     public String toString()
/*     */     {
/*  93 */       return this.good.getName();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public static class TradeSlot
/*     */     extends Slot
/*     */   {
/*     */     public final Building building;
/*     */     public final EntityPlayer player;
/*     */     public final Goods good;
/*     */     public final boolean sellingSlot;
/*     */     
/*     */     public TradeSlot(Building building, EntityPlayer player, boolean sellingSlot, Goods good, int xpos, int ypos)
/*     */     {
/* 108 */       super(-1, xpos, ypos);
/* 109 */       this.building = building;
/* 110 */       this.good = good;
/* 111 */       this.player = player;
/* 112 */       this.sellingSlot = sellingSlot;
/*     */     }
/*     */     
/*     */     public ItemStack func_75209_a(int i)
/*     */     {
/* 117 */       return null;
/*     */     }
/*     */     
/*     */     public boolean func_25014_f() {
/* 121 */       return false;
/*     */     }
/*     */     
/*     */     public IIcon func_75212_b()
/*     */     {
/* 126 */       return null;
/*     */     }
/*     */     
/*     */     public boolean func_75216_d()
/*     */     {
/* 131 */       return func_75211_c() != null;
/*     */     }
/*     */     
/*     */     public int func_75219_a()
/*     */     {
/* 136 */       return 0;
/*     */     }
/*     */     
/*     */     public ItemStack func_75211_c()
/*     */     {
/* 141 */       if (this.sellingSlot) {
/* 142 */         return new ItemStack(this.good.item.getItem(), Math.min(this.building.countGoods(this.good.item.getItem(), this.good.item.meta), 99), this.good.item.meta);
/*     */       }
/* 144 */       return new ItemStack(this.good.item.getItem(), Math.min(MillCommonUtilities.countChestItems(this.player.field_71071_by, this.good.item.getItem(), this.good.item.meta), 99), this.good.item.meta);
/*     */     }
/*     */     
/*     */ 
/*     */     public boolean func_75214_a(ItemStack itemstack)
/*     */     {
/* 150 */       return true;
/*     */     }
/*     */     
/*     */     public String isProblem()
/*     */     {
/* 155 */       if (this.sellingSlot) {
/* 156 */         if ((this.building.countGoods(this.good.item.getItem(), this.good.item.meta) < 1) && (this.good.requiredTag != null) && (!this.building.location.tags.contains(this.good.requiredTag))) {
/* 157 */           return MLN.string("ui.missingequipment") + ": " + this.good.requiredTag;
/*     */         }
/* 159 */         if ((this.building.countGoods(this.good.item.getItem(), this.good.item.meta) < 1) && (!this.good.autoGenerate)) {
/* 160 */           return MLN.string("ui.outofstock");
/*     */         }
/* 162 */         if (this.building.getTownHall().getReputation(this.player.getDisplayName()) < this.good.minReputation) {
/* 163 */           return MLN.string("ui.reputationneeded", new String[] { this.building.culture.getReputationLevelLabel(this.good.minReputation) });
/*     */         }
/* 165 */         int playerMoney = MillCommonUtilities.countMoney(this.player.field_71071_by);
/* 166 */         if (playerMoney < this.good.getCalculatedSellingPrice(this.building, this.player)) {
/* 167 */           return MLN.string("ui.missingdeniers").replace("<0>", "" + (this.good.getCalculatedSellingPrice(this.building, this.player) - playerMoney));
/*     */         }
/*     */       }
/* 170 */       else if (MillCommonUtilities.countChestItems(this.player.field_71071_by, this.good.item.getItem(), this.good.item.meta) == 0) {
/* 171 */         return MLN.string("ui.noneininventory");
/*     */       }
/*     */       
/* 174 */       return null;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public void func_75218_e() {}
/*     */     
/*     */ 
/*     */ 
/*     */     public void func_75215_d(ItemStack itemstack) {}
/*     */     
/*     */ 
/*     */ 
/*     */     public String toString()
/*     */     {
/* 189 */       return this.good.name + (this.sellingSlot ? MLN.string("ui.selling") : MLN.string("ui.buying"));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 196 */   public int nbRowBuying = 0; public int nbRowSelling = 0;
/*     */   private MillVillager merchant;
/*     */   private Building building;
/*     */   
/* 200 */   public ContainerTrade(EntityPlayer player, Building building) { this.building = building;
/*     */     
/* 202 */     Set<Goods> sellingGoods = building.getSellingGoods(player);
/*     */     
/* 204 */     int slotnb = 0;
/*     */     
/* 206 */     if (sellingGoods != null) {
/* 207 */       for (Goods g : sellingGoods) {
/* 208 */         int slotrow = slotnb / 13;
/* 209 */         func_75146_a(new TradeSlot(building, player, true, g, 8 + 18 * (slotnb - 13 * slotrow), 32 + slotrow * 18));
/*     */         
/* 211 */         slotnb++;
/*     */       }
/*     */     }
/*     */     
/* 215 */     this.nbRowSelling = (slotnb / 13 + 1);
/*     */     
/* 217 */     Set<Goods> buyingGoods = building.getBuyingGoods(player);
/*     */     
/* 219 */     slotnb = 0;
/*     */     
/* 221 */     if (buyingGoods != null) {
/* 222 */       for (Goods g : buyingGoods) {
/* 223 */         int slotrow = slotnb / 13;
/* 224 */         func_75146_a(new TradeSlot(building, player, false, g, 8 + 18 * (slotnb - 13 * slotrow), 86 + slotrow * 18));
/*     */         
/* 226 */         slotnb++;
/*     */       }
/*     */     }
/*     */     
/* 230 */     this.nbRowBuying = (slotnb / 13 + 1);
/*     */     
/* 232 */     for (int l = 0; l < 3; l++) {
/* 233 */       for (int k1 = 0; k1 < 9; k1++) {
/* 234 */         func_75146_a(new Slot(player.field_71071_by, k1 + l * 9 + 9, 8 + k1 * 18 + 36, 103 + l * 18 + 37));
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 239 */     for (int i1 = 0; i1 < 9; i1++) {
/* 240 */       func_75146_a(new Slot(player.field_71071_by, i1, 8 + i1 * 18 + 36, 198));
/*     */     }
/*     */   }
/*     */   
/*     */   public ContainerTrade(EntityPlayer player, MillVillager merchant)
/*     */   {
/* 246 */     this.merchant = merchant;
/*     */     
/* 248 */     int slotnb = 0;
/*     */     
/* 250 */     Set<Goods> sellingGoods = merchant.merchantSells.keySet();
/*     */     
/* 252 */     if (sellingGoods != null) {
/* 253 */       for (Goods g : sellingGoods) {
/* 254 */         int slotrow = slotnb / 13;
/* 255 */         func_75146_a(new MerchantSlot(merchant, player, g, 8 + 18 * (slotnb - 13 * slotrow), 32 + slotrow * 18));
/*     */         
/* 257 */         slotnb++;
/*     */       }
/*     */     }
/*     */     
/* 261 */     this.nbRowSelling = (slotnb / 13 + 1);
/*     */     
/* 263 */     for (int l = 0; l < 3; l++) {
/* 264 */       for (int k1 = 0; k1 < 9; k1++) {
/* 265 */         func_75146_a(new Slot(player.field_71071_by, k1 + l * 9 + 9, 8 + k1 * 18 + 36, 103 + l * 18 + 37));
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 270 */     for (int i1 = 0; i1 < 9; i1++) {
/* 271 */       func_75146_a(new Slot(player.field_71071_by, i1, 8 + i1 * 18 + 36, 198));
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean func_75145_c(EntityPlayer entityplayer)
/*     */   {
/* 277 */     return true;
/*     */   }
/*     */   
/*     */   public boolean isUsableByPlayer(EntityPlayer entityplayer) {
/* 281 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   public ItemStack func_75144_a(int slotNb, int buttonNumber, int shiftPressed, EntityPlayer player)
/*     */   {
/* 287 */     if ((slotNb >= 0) && (slotNb < this.field_75151_b.size()))
/*     */     {
/* 289 */       Slot slot = (Slot)this.field_75151_b.get(slotNb);
/*     */       
/* 291 */       if ((slot != null) && ((slot instanceof TradeSlot))) {
/* 292 */         TradeSlot tslot = (TradeSlot)slot;
/* 293 */         Goods good = tslot.good;
/*     */         
/* 295 */         int nbItems = 1;
/*     */         
/* 297 */         if (shiftPressed > 0) {
/* 298 */           nbItems = 64;
/* 299 */         } else if (buttonNumber == 1) {
/* 300 */           nbItems = 8;
/*     */         }
/*     */         
/* 303 */         if (tslot.isProblem() == null)
/*     */         {
/* 305 */           int playerMoney = MillCommonUtilities.countMoney(player.field_71071_by);
/*     */           
/* 307 */           if (tslot.sellingSlot) {
/* 308 */             if (playerMoney < good.getCalculatedSellingPrice(this.building, player) * nbItems) {
/* 309 */               nbItems = MathHelper.func_76128_c(playerMoney / good.getCalculatedSellingPrice(this.building, player));
/*     */             }
/* 311 */             if ((!good.autoGenerate) && (this.building.countGoods(good.item.getItem(), good.item.meta) < nbItems)) {
/* 312 */               nbItems = this.building.countGoods(good.item.getItem(), good.item.meta);
/*     */             }
/*     */             
/* 315 */             nbItems = MillCommonUtilities.putItemsInChest(player.field_71071_by, good.item.getItem(), good.item.meta, nbItems);
/* 316 */             MillCommonUtilities.changeMoney(player.field_71071_by, -good.getCalculatedSellingPrice(this.building, player) * nbItems, player);
/* 317 */             if (!good.autoGenerate) {
/* 318 */               this.building.takeGoods(good.item.getItem(), good.item.meta, nbItems);
/*     */             }
/*     */             
/* 321 */             this.building.adjustReputation(player, good.getCalculatedSellingPrice(this.building, player) * nbItems);
/*     */             
/* 323 */             this.building.getTownHall().adjustLanguage(player, nbItems);
/*     */           }
/*     */           else {
/* 326 */             if (MillCommonUtilities.countChestItems(player.field_71071_by, good.item.getItem(), good.item.meta) < nbItems) {
/* 327 */               nbItems = MillCommonUtilities.countChestItems(player.field_71071_by, good.item.getItem(), good.item.meta);
/*     */             }
/*     */             
/* 330 */             nbItems = this.building.storeGoods(good.item.getItem(), good.item.meta, nbItems);
/* 331 */             MillCommonUtilities.getItemsFromChest(player.field_71071_by, good.item.getItem(), good.item.meta, nbItems);
/* 332 */             MillCommonUtilities.changeMoney(player.field_71071_by, good.getCalculatedBuyingPrice(this.building, player) * nbItems, player);
/* 333 */             this.building.adjustReputation(player, good.getCalculatedBuyingPrice(this.building, player) * nbItems);
/* 334 */             this.building.getTownHall().adjustLanguage(player, nbItems);
/*     */           }
/*     */         }
/*     */         
/*     */ 
/* 339 */         return slot.func_75211_c(); }
/* 340 */       if ((slot != null) && ((slot instanceof MerchantSlot))) {
/* 341 */         MerchantSlot tslot = (MerchantSlot)slot;
/* 342 */         int nbItems = 1;
/*     */         
/* 344 */         if (shiftPressed > 0) {
/* 345 */           nbItems = 64;
/* 346 */         } else if (buttonNumber == 1) {
/* 347 */           nbItems = 8;
/*     */         }
/*     */         
/* 350 */         if (tslot.isProblem() == null)
/*     */         {
/* 352 */           int playerMoney = MillCommonUtilities.countMoney(player.field_71071_by);
/* 353 */           if (playerMoney < tslot.good.getCalculatedSellingPrice(this.merchant) * nbItems) {
/* 354 */             nbItems = MathHelper.func_76128_c(playerMoney / tslot.good.getCalculatedSellingPrice(this.merchant));
/*     */           }
/* 356 */           if (this.merchant.getHouse().countGoods(tslot.good.item) < nbItems) {
/* 357 */             nbItems = this.merchant.getHouse().countGoods(tslot.good.item);
/*     */           }
/*     */           
/* 360 */           nbItems = MillCommonUtilities.putItemsInChest(player.field_71071_by, tslot.good.item.getItem(), tslot.good.item.meta, nbItems);
/* 361 */           MillCommonUtilities.changeMoney(player.field_71071_by, -tslot.good.getCalculatedSellingPrice(this.merchant) * nbItems, player);
/* 362 */           this.merchant.getHouse().takeGoods(tslot.good.item, nbItems);
/* 363 */           Mill.getMillWorld(player.field_70170_p).getProfile(player.getDisplayName()).adjustLanguage(this.merchant.getCulture().key, nbItems);
/*     */         }
/* 365 */         return slot.func_75211_c();
/*     */       }
/*     */     }
/*     */     
/* 369 */     if (shiftPressed > 0) {
/* 370 */       return null;
/*     */     }
/*     */     
/* 373 */     return super.func_75144_a(slotNb, buttonNumber, shiftPressed, player);
/*     */   }
/*     */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\ContainerTrade.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */