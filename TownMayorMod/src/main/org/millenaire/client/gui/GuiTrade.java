/*     */ package org.millenaire.client.gui;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.entity.EntityClientPlayerMP;
/*     */ import net.minecraft.client.gui.FontRenderer;
/*     */ import net.minecraft.client.gui.GuiButton;
/*     */ import net.minecraft.client.gui.inventory.GuiContainer;
/*     */ import net.minecraft.client.renderer.RenderHelper;
/*     */ import net.minecraft.client.renderer.entity.RenderItem;
/*     */ import net.minecraft.client.renderer.texture.TextureManager;
/*     */ import net.minecraft.client.settings.GameSettings;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.player.InventoryPlayer;
/*     */ import net.minecraft.inventory.Container;
/*     */ import net.minecraft.inventory.Slot;
/*     */ import net.minecraft.item.EnumRarity;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ import org.millenaire.client.MillClientUtilities;
/*     */ import org.millenaire.common.ContainerTrade;
/*     */ import org.millenaire.common.ContainerTrade.MerchantSlot;
/*     */ import org.millenaire.common.ContainerTrade.TradeSlot;
/*     */ import org.millenaire.common.MLN;
/*     */ import org.millenaire.common.MillVillager;
/*     */ import org.millenaire.common.building.Building;
/*     */ import org.millenaire.common.core.MillCommonUtilities;
/*     */ import org.millenaire.common.item.Goods;
/*     */ 
/*     */ public class GuiTrade extends GuiContainer
/*     */ {
/*  35 */   private int sellingRow = 0; private int buyingRow = 0;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  40 */   private static RenderItem itemRenderer = new RenderItem();
/*     */   
/*  42 */   ResourceLocation background = new ResourceLocation("millenaire", "textures/gui/ML_trade.png");
/*     */   private Building building;
/*     */   private MillVillager merchant;
/*     */   
/*  46 */   public GuiTrade(EntityPlayer player, Building building) { super(new ContainerTrade(player, building));
/*     */     
/*  48 */     this.drawSlotInventory = MillCommonUtilities.getDrawSlotInventoryMethod(this);
/*     */     
/*  50 */     this.container = ((ContainerTrade)this.field_147002_h);
/*     */     
/*  52 */     this.building = building;
/*  53 */     this.player = player;
/*  54 */     this.field_147000_g = 222;
/*  55 */     this.field_146999_f = 248;
/*     */     
/*  57 */     updateRows(false, 0, 0);
/*  58 */     updateRows(true, 0, 0);
/*     */   }
/*     */   
/*     */   public GuiTrade(EntityPlayer player, MillVillager merchant) {
/*  62 */     super(new ContainerTrade(player, merchant));
/*     */     
/*  64 */     this.drawSlotInventory = MillCommonUtilities.getDrawSlotInventoryMethod(this);
/*     */     
/*  66 */     this.container = ((ContainerTrade)this.field_147002_h);
/*     */     
/*  68 */     this.merchant = merchant;
/*  69 */     this.player = player;
/*  70 */     this.field_147000_g = 222;
/*  71 */     this.field_146999_f = 248;
/*     */     
/*  73 */     updateRows(false, 0, 0);
/*  74 */     updateRows(true, 0, 0);
/*     */   }
/*     */   
/*     */ 
/*     */   protected void func_146284_a(GuiButton button)
/*     */   {
/*  80 */     if ((button instanceof GuiText.MillGuiButton)) {
/*  81 */       MillClientUtilities.displayTradeHelp(this.building, this.player);
/*  82 */       return;
/*     */     }
/*     */     
/*  85 */     super.func_146284_a(button);
/*     */   }
/*     */   
/*     */   private final EntityPlayer player;
/*     */   private final ContainerTrade container;
/*     */   private final Method drawSlotInventory;
/*     */   public void displayItemOverlay(ItemStack stack, List<String> list, int posx, int posy) {
/*  92 */     if (list.size() > 0) {
/*  93 */       int l1 = 0;
/*     */       
/*  95 */       for (int i2 = 0; i2 < list.size(); i2++) {
/*  96 */         int k2 = this.field_146289_q.func_78256_a((String)list.get(i2));
/*     */         
/*  98 */         if (k2 > l1) {
/*  99 */           l1 = k2;
/*     */         }
/*     */       }
/*     */       
/* 103 */       int j2 = posx + 12;
/* 104 */       int l2 = posy - 12;
/* 105 */       int i3 = l1;
/* 106 */       int j3 = 8;
/*     */       
/* 108 */       if (list.size() > 1) {
/* 109 */         j3 += 2 + (list.size() - 1) * 10;
/*     */       }
/*     */       
/* 112 */       this.field_73735_i = 300.0F;
/* 113 */       itemRenderer.field_77023_b = 300.0F;
/* 114 */       int k3 = -267386864;
/* 115 */       func_73733_a(j2 - 3, l2 - 4, j2 + i3 + 3, l2 - 3, -267386864, -267386864);
/* 116 */       func_73733_a(j2 - 3, l2 + j3 + 3, j2 + i3 + 3, l2 + j3 + 4, -267386864, -267386864);
/* 117 */       func_73733_a(j2 - 3, l2 - 3, j2 + i3 + 3, l2 + j3 + 3, -267386864, -267386864);
/* 118 */       func_73733_a(j2 - 4, l2 - 3, j2 - 3, l2 + j3 + 3, -267386864, -267386864);
/* 119 */       func_73733_a(j2 + i3 + 3, l2 - 3, j2 + i3 + 4, l2 + j3 + 3, -267386864, -267386864);
/* 120 */       int l3 = 1347420415;
/* 121 */       int i4 = 1344798847;
/* 122 */       func_73733_a(j2 - 3, l2 - 3 + 1, j2 - 3 + 1, l2 + j3 + 3 - 1, 1347420415, 1344798847);
/* 123 */       func_73733_a(j2 + i3 + 2, l2 - 3 + 1, j2 + i3 + 3, l2 + j3 + 3 - 1, 1347420415, 1344798847);
/* 124 */       func_73733_a(j2 - 3, l2 - 3, j2 + i3 + 3, l2 - 3 + 1, 1347420415, 1347420415);
/* 125 */       func_73733_a(j2 - 3, l2 + j3 + 2, j2 + i3 + 3, l2 + j3 + 3, 1344798847, 1344798847);
/*     */       
/* 127 */       for (int j4 = 0; j4 < list.size(); j4++) {
/* 128 */         String s = (String)list.get(j4);
/*     */         
/* 130 */         if (j4 == 0) {
/* 131 */           s = stack.func_77953_t().field_77937_e + s;
/*     */         } else {
/* 133 */           s = "§7" + s;
/*     */         }
/*     */         
/* 136 */         this.field_146289_q.func_78261_a(s, j2, l2, -1);
/*     */         
/* 138 */         if (j4 == 0) {
/* 139 */           l2 += 2;
/*     */         }
/*     */         
/* 142 */         l2 += 10;
/*     */       }
/*     */       
/* 145 */       this.field_73735_i = 0.0F;
/* 146 */       itemRenderer.field_77023_b = 0.0F;
/*     */     }
/*     */   }
/*     */   
/*     */   protected void func_146976_a(float f, int i, int j)
/*     */   {
/* 152 */     GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
/* 153 */     this.field_146297_k.field_71446_o.func_110577_a(this.background);
/* 154 */     int x = (this.field_146294_l - this.field_146999_f) / 2;
/* 155 */     int y = (this.field_146295_m - this.field_147000_g) / 2;
/* 156 */     func_73729_b(x, y, 0, 0, this.field_146999_f, this.field_147000_g);
/*     */     
/* 158 */     if (this.sellingRow == 0) {
/* 159 */       func_73729_b(x + 216, y + 68, 5, 5, 11, 7);
/*     */     }
/* 161 */     if (this.buyingRow == 0) {
/* 162 */       func_73729_b(x + 216, y + 122, 5, 5, 11, 7);
/*     */     }
/*     */     
/* 165 */     if (this.sellingRow >= this.container.nbRowSelling - 2) {
/* 166 */       func_73729_b(x + 230, y + 68, 5, 5, 11, 7);
/*     */     }
/*     */     
/* 169 */     if (this.buyingRow >= this.container.nbRowBuying - 2) {
/* 170 */       func_73729_b(x + 230, y + 122, 5, 5, 11, 7);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   protected void func_146979_b(int x, int y)
/*     */   {
/* 177 */     if (this.building != null) {
/* 178 */       this.field_146289_q.func_78276_b(this.building.getNativeBuildingName(), 8, 6, 4210752);
/* 179 */       this.field_146289_q.func_78276_b(MLN.string("ui.wesell") + ":", 8, 22, 4210752);
/* 180 */       this.field_146289_q.func_78276_b(MLN.string("ui.webuy") + ":", 8, 76, 4210752);
/*     */     } else {
/* 182 */       this.field_146289_q.func_78276_b(this.merchant.getName() + ": " + this.merchant.getNativeOccupationName(), 8, 6, 4210752);
/* 183 */       this.field_146289_q.func_78276_b(MLN.string("ui.isell") + ":", 8, 22, 4210752);
/*     */     }
/*     */     
/* 186 */     this.field_146289_q.func_78276_b(MLN.string("ui.inventory"), 44, this.field_147000_g - 96 + 2, 4210752);
/*     */     
/*     */ 
/* 189 */     Iterator iterator = this.field_146292_n.iterator();
/*     */     
/* 191 */     while (iterator.hasNext()) {
/* 192 */       GuiButton guibutton = (GuiButton)iterator.next();
/*     */       
/* 194 */       if (guibutton.func_146115_a()) {
/* 195 */         guibutton.func_146111_b(x - this.field_147003_i, y - this.field_147009_r);
/* 196 */         break;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void func_73863_a(int i, int j, float f)
/*     */   {
/* 204 */     func_146276_q_();
/* 205 */     int k = (this.field_146294_l - this.field_146999_f) / 2;
/* 206 */     int l = (this.field_146295_m - this.field_147000_g) / 2;
/* 207 */     func_146976_a(f, i, j);
/* 208 */     GL11.glPushMatrix();
/* 209 */     GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
/* 210 */     RenderHelper.func_74519_b();
/* 211 */     GL11.glPopMatrix();
/* 212 */     GL11.glPushMatrix();
/* 213 */     GL11.glTranslatef(k, l, 0.0F);
/* 214 */     GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
/* 215 */     GL11.glEnable(32826);
/* 216 */     Slot slot = null;
/*     */     
/* 218 */     String currentProblemString = null;
/*     */     
/* 220 */     for (int i1 = 0; i1 < this.field_147002_h.field_75151_b.size(); i1++) {
/* 221 */       Slot slot1 = (Slot)this.field_147002_h.field_75151_b.get(i1);
/*     */       try {
/* 223 */         this.drawSlotInventory.invoke(this, new Object[] { slot1 });
/*     */       } catch (Exception e) {
/* 225 */         MLN.printException("Exception when trying to access drawSlotInventory", e);
/*     */       }
/*     */       
/* 228 */       String problem = null;
/*     */       
/* 230 */       if ((slot1 instanceof ContainerTrade.TradeSlot)) {
/* 231 */         ContainerTrade.TradeSlot tslot = (ContainerTrade.TradeSlot)slot1;
/*     */         
/* 233 */         problem = tslot.isProblem();
/*     */         
/* 235 */         if (problem != null) {
/* 236 */           GL11.glDisable(2896);
/* 237 */           GL11.glDisable(2929);
/* 238 */           int j1 = slot1.field_75223_e;
/* 239 */           int l1 = slot1.field_75221_f;
/* 240 */           func_73733_a(j1, l1, j1 + 16, l1 + 16, Integer.MIN_VALUE, Integer.MIN_VALUE);
/* 241 */           GL11.glEnable(2896);
/* 242 */           GL11.glEnable(2929);
/*     */         }
/* 244 */       } else if ((slot1 instanceof ContainerTrade.MerchantSlot)) {
/* 245 */         ContainerTrade.MerchantSlot tslot = (ContainerTrade.MerchantSlot)slot1;
/*     */         
/* 247 */         problem = tslot.isProblem();
/*     */         
/* 249 */         if (problem != null) {
/* 250 */           GL11.glDisable(2896);
/* 251 */           GL11.glDisable(2929);
/* 252 */           int j1 = slot1.field_75223_e;
/* 253 */           int l1 = slot1.field_75221_f;
/* 254 */           func_73733_a(j1, l1, j1 + 16, l1 + 16, Integer.MIN_VALUE, Integer.MIN_VALUE);
/* 255 */           GL11.glEnable(2896);
/* 256 */           GL11.glEnable(2929);
/*     */         }
/*     */       }
/*     */       
/* 260 */       if (getIsMouseOverSlot(slot1, i, j)) {
/* 261 */         slot = slot1;
/*     */         
/* 263 */         currentProblemString = problem;
/*     */         
/* 265 */         GL11.glDisable(2896);
/* 266 */         GL11.glDisable(2929);
/* 267 */         int j1 = slot1.field_75223_e;
/* 268 */         int l1 = slot1.field_75221_f;
/* 269 */         if (problem == null) {
/* 270 */           func_73733_a(j1, l1, j1 + 16, l1 + 16, -2130706433, -2130706433);
/*     */         }
/* 272 */         GL11.glEnable(2896);
/* 273 */         GL11.glEnable(2929);
/*     */       }
/*     */     }
/*     */     
/* 277 */     InventoryPlayer inventoryplayer = this.field_146297_k.field_71439_g.field_71071_by;
/* 278 */     if (inventoryplayer.func_70445_o() != null) {
/* 279 */       GL11.glTranslatef(0.0F, 0.0F, 32.0F);
/* 280 */       itemRenderer.func_77015_a(this.field_146289_q, this.field_146297_k.field_71446_o, inventoryplayer.func_70445_o(), i - k - 8, j - l - 8);
/* 281 */       itemRenderer.func_77021_b(this.field_146289_q, this.field_146297_k.field_71446_o, inventoryplayer.func_70445_o(), i - k - 8, j - l - 8);
/*     */     }
/* 283 */     GL11.glDisable(32826);
/* 284 */     RenderHelper.func_74518_a();
/* 285 */     GL11.glDisable(2896);
/* 286 */     GL11.glDisable(2929);
/* 287 */     func_146979_b(i, j);
/* 288 */     if ((inventoryplayer.func_70445_o() == null) && (slot != null) && (slot.func_75216_d())) {
/* 289 */       if ((slot instanceof ContainerTrade.TradeSlot))
/*     */       {
/* 291 */         ContainerTrade.TradeSlot tslot = (ContainerTrade.TradeSlot)slot;
/*     */         
/*     */         int priceColour;
/*     */         String price;
/*     */         int priceColour;
/* 296 */         if (tslot.sellingSlot) {
/* 297 */           String price = MillCommonUtilities.getShortPrice(tslot.good.getCalculatedSellingPrice(this.building, this.player));
/* 298 */           priceColour = MillCommonUtilities.getPriceColourMC(tslot.good.getCalculatedSellingPrice(this.building, this.player));
/*     */         } else {
/* 300 */           price = MillCommonUtilities.getShortPrice(tslot.good.getCalculatedBuyingPrice(this.building, this.player));
/* 301 */           priceColour = MillCommonUtilities.getPriceColourMC(tslot.good.getCalculatedBuyingPrice(this.building, this.player));
/*     */         }
/*     */         
/* 304 */         ItemStack itemstack = slot.func_75211_c();
/*     */         try
/*     */         {
/* 307 */           List<String> list = itemstack.func_82840_a(this.field_146297_k.field_71439_g, this.field_146297_k.field_71474_y.field_82882_x);
/*     */           
/* 309 */           list.add("§" + Integer.toHexString(priceColour) + price);
/* 310 */           if (currentProblemString != null) {
/* 311 */             list.add("§4" + currentProblemString);
/*     */           }
/*     */           
/* 314 */           displayItemOverlay(itemstack, list, i - k, j - l);
/*     */         }
/*     */         catch (Exception e) {
/* 317 */           MLN.printException("Exception when rendering tooltip for stack: " + itemstack, e);
/*     */         }
/*     */       }
/* 320 */       else if ((slot instanceof ContainerTrade.MerchantSlot))
/*     */       {
/* 322 */         ContainerTrade.MerchantSlot tslot = (ContainerTrade.MerchantSlot)slot;
/*     */         
/*     */ 
/*     */ 
/*     */ 
/* 327 */         String price = MillCommonUtilities.getShortPrice(tslot.good.getCalculatedSellingPrice(this.merchant));
/* 328 */         int priceColour = MillCommonUtilities.getPriceColourMC(tslot.good.getCalculatedSellingPrice(this.merchant));
/*     */         
/* 330 */         ItemStack itemstack = slot.func_75211_c();
/* 331 */         List<String> list = itemstack.func_82840_a(this.field_146297_k.field_71439_g, this.field_146297_k.field_71474_y.field_82882_x);
/*     */         
/* 333 */         list.add("§" + Integer.toHexString(priceColour) + price);
/* 334 */         if (currentProblemString != null) {
/* 335 */           list.add("§4" + currentProblemString);
/*     */         }
/*     */         
/* 338 */         displayItemOverlay(itemstack, list, i - k, j - l);
/*     */       }
/*     */       else {
/* 341 */         ItemStack itemstack = slot.func_75211_c();
/* 342 */         displayItemOverlay(itemstack, itemstack.func_82840_a(this.field_146297_k.field_71439_g, this.field_146297_k.field_71474_y.field_82882_x), i - k, j - l);
/*     */       }
/*     */     }
/* 345 */     GL11.glPopMatrix();
/* 346 */     drawScreenGUIScreen(i, j, f);
/* 347 */     GL11.glEnable(2896);
/* 348 */     GL11.glEnable(2929);
/*     */   }
/*     */   
/*     */   public void drawScreenGUIScreen(int i, int j, float f) {
/* 352 */     for (int k = 0; k < this.field_146292_n.size(); k++) {
/* 353 */       GuiButton guibutton = (GuiButton)this.field_146292_n.get(k);
/* 354 */       guibutton.func_146112_a(this.field_146297_k, i, j);
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean getIsMouseOverSlot(Slot slot, int i, int j)
/*     */   {
/* 360 */     int k = (this.field_146294_l - this.field_146999_f) / 2;
/* 361 */     int l = (this.field_146295_m - this.field_147000_g) / 2;
/* 362 */     i -= k;
/* 363 */     j -= l;
/* 364 */     return (i >= slot.field_75223_e - 1) && (i < slot.field_75223_e + 16 + 1) && (j >= slot.field_75221_f - 1) && (j < slot.field_75221_f + 16 + 1);
/*     */   }
/*     */   
/*     */ 
/*     */   public void func_73866_w_()
/*     */   {
/* 370 */     super.func_73866_w_();
/*     */     
/* 372 */     if (this.building != null) {
/* 373 */       int xStart = (this.field_146294_l - this.field_146999_f) / 2;
/* 374 */       int yStart = (this.field_146295_m - this.field_147000_g) / 2;
/*     */       
/* 376 */       this.field_146292_n.add(new GuiText.MillGuiButton(0, xStart + this.field_146999_f - 21, yStart + 5, 15, 20, "?"));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   protected void func_73864_a(int x, int y, int clickType)
/*     */   {
/* 383 */     if (clickType == 0) {
/* 384 */       int startx = (this.field_146294_l - this.field_146999_f) / 2;
/* 385 */       int starty = (this.field_146295_m - this.field_147000_g) / 2;
/*     */       
/* 387 */       int dx = x - startx;
/* 388 */       int dy = y - starty;
/*     */       
/* 390 */       if ((dy >= 68) && (dy <= 74)) {
/* 391 */         if ((dx >= 216) && (dx <= 226)) {
/* 392 */           if (this.sellingRow > 0) {
/* 393 */             this.sellingRow -= 1;
/* 394 */             updateRows(true, 1, this.sellingRow);
/*     */           }
/* 396 */         } else if ((dx >= 230) && (dx <= 240) && 
/* 397 */           (this.sellingRow < this.container.nbRowSelling - 2)) {
/* 398 */           this.sellingRow += 1;
/* 399 */           updateRows(true, -1, this.sellingRow);
/*     */         }
/*     */       }
/* 402 */       else if ((dy >= 122) && (dy <= 127)) {
/* 403 */         if ((dx >= 216) && (dx <= 226)) {
/* 404 */           if (this.buyingRow > 0) {
/* 405 */             this.buyingRow -= 1;
/* 406 */             updateRows(false, 1, this.buyingRow);
/*     */           }
/*     */         }
/* 409 */         else if ((dx >= 230) && (dx <= 240) && 
/* 410 */           (this.buyingRow < this.container.nbRowBuying - 2)) {
/* 411 */           this.buyingRow += 1;
/* 412 */           updateRows(false, -1, this.buyingRow);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 420 */     super.func_73864_a(x, y, clickType);
/*     */   }
/*     */   
/*     */   private void updateRows(boolean selling, int change, int row)
/*     */   {
/* 425 */     int pos = 0;
/*     */     
/* 427 */     for (Object o : this.container.field_75151_b) {
/* 428 */       Slot slot = (Slot)o;
/* 429 */       if ((slot instanceof ContainerTrade.TradeSlot)) {
/* 430 */         ContainerTrade.TradeSlot tradeSlot = (ContainerTrade.TradeSlot)slot;
/* 431 */         if (tradeSlot.sellingSlot == selling) {
/* 432 */           tradeSlot.field_75221_f += 18 * change;
/*     */           
/* 434 */           if ((pos / 13 < row) || (pos / 13 > row + 1)) {
/* 435 */             if (tradeSlot.field_75223_e > 0) {
/* 436 */               tradeSlot.field_75223_e -= 1000;
/*     */             }
/*     */           }
/* 439 */           else if (tradeSlot.field_75223_e < 0) {
/* 440 */             tradeSlot.field_75223_e += 1000;
/*     */           }
/*     */           
/*     */ 
/* 444 */           pos++;
/*     */         }
/* 446 */       } else if (((slot instanceof ContainerTrade.MerchantSlot)) && (selling)) {
/* 447 */         ContainerTrade.MerchantSlot merchantSlot = (ContainerTrade.MerchantSlot)slot;
/* 448 */         merchantSlot.field_75221_f += 18 * change;
/*     */         
/* 450 */         if ((pos / 13 < row) || (pos / 13 > row + 1)) {
/* 451 */           if (merchantSlot.field_75223_e > 0) {
/* 452 */             merchantSlot.field_75223_e -= 1000;
/*     */           }
/*     */         }
/* 455 */         else if (merchantSlot.field_75223_e < 0) {
/* 456 */           merchantSlot.field_75223_e += 1000;
/*     */         }
/*     */         
/*     */ 
/* 460 */         pos++;
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\client\gui\GuiTrade.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */