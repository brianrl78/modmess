/*     */ package org.millenaire.client.gui;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.entity.EntityClientPlayerMP;
/*     */ import net.minecraft.client.gui.FontRenderer;
/*     */ import net.minecraft.client.gui.GuiButton;
/*     */ import net.minecraft.client.gui.inventory.GuiContainer;
/*     */ import net.minecraft.client.renderer.RenderHelper;
/*     */ import net.minecraft.client.renderer.entity.RenderItem;
/*     */ import net.minecraft.client.renderer.texture.TextureManager;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.player.InventoryPlayer;
/*     */ import net.minecraft.inventory.Container;
/*     */ import net.minecraft.inventory.Slot;
/*     */ import net.minecraft.item.EnumRarity;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ import org.millenaire.common.ContainerPuja;
/*     */ import org.millenaire.common.MLN;
/*     */ import org.millenaire.common.PujaSacrifice;
/*     */ import org.millenaire.common.PujaSacrifice.PrayerTarget;
/*     */ import org.millenaire.common.building.Building;
/*     */ import org.millenaire.common.core.MillCommonUtilities;
/*     */ 
/*     */ public class GuiPujas extends GuiContainer
/*     */ {
/*     */   private final Building temple;
/*     */   private final EntityPlayer player;
/*     */   private final Method drawSlotInventory;
/*  34 */   private static final ResourceLocation texturePujas = new ResourceLocation("millenaire", "textures/gui/ML_pujas.png");
/*  35 */   private static final ResourceLocation textureSacrifices = new ResourceLocation("millenaire", "textures/gui/ML_mayansacrifices.png");
/*     */   
/*     */   public GuiPujas(EntityPlayer player, Building temple) {
/*  38 */     super(new ContainerPuja(player, temple));
/*     */     
/*  40 */     this.field_147000_g = 188;
/*     */     
/*  42 */     this.temple = temple;
/*  43 */     this.player = player;
/*     */     
/*  45 */     if (MLN.LogPujas >= 3) {
/*  46 */       MLN.debug(this, "Opening shrine GUI");
/*     */     }
/*     */     
/*  49 */     this.drawSlotInventory = MillCommonUtilities.getDrawSlotInventoryMethod(this);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void displayItemOverlay(ItemStack stack, List<String> list, int posx, int posy)
/*     */   {
/*  56 */     if ((list != null) && (list.size() > 0)) {
/*  57 */       int l1 = 0;
/*     */       
/*  59 */       for (int i2 = 0; i2 < list.size(); i2++) {
/*  60 */         int k2 = this.field_146289_q.func_78256_a((String)list.get(i2));
/*     */         
/*  62 */         if (k2 > l1) {
/*  63 */           l1 = k2;
/*     */         }
/*     */       }
/*     */       
/*  67 */       int j2 = posx + 12;
/*  68 */       int l2 = posy - 12;
/*  69 */       int i3 = l1;
/*  70 */       int j3 = 8;
/*     */       
/*  72 */       if (list.size() > 1) {
/*  73 */         j3 += 2 + (list.size() - 1) * 10;
/*     */       }
/*     */       
/*  76 */       this.field_73735_i = 300.0F;
/*  77 */       field_146296_j.field_77023_b = 300.0F;
/*  78 */       int k3 = -267386864;
/*  79 */       func_73733_a(j2 - 3, l2 - 4, j2 + i3 + 3, l2 - 3, -267386864, -267386864);
/*  80 */       func_73733_a(j2 - 3, l2 + j3 + 3, j2 + i3 + 3, l2 + j3 + 4, -267386864, -267386864);
/*  81 */       func_73733_a(j2 - 3, l2 - 3, j2 + i3 + 3, l2 + j3 + 3, -267386864, -267386864);
/*  82 */       func_73733_a(j2 - 4, l2 - 3, j2 - 3, l2 + j3 + 3, -267386864, -267386864);
/*  83 */       func_73733_a(j2 + i3 + 3, l2 - 3, j2 + i3 + 4, l2 + j3 + 3, -267386864, -267386864);
/*  84 */       int l3 = 1347420415;
/*  85 */       int i4 = 1344798847;
/*  86 */       func_73733_a(j2 - 3, l2 - 3 + 1, j2 - 3 + 1, l2 + j3 + 3 - 1, 1347420415, 1344798847);
/*  87 */       func_73733_a(j2 + i3 + 2, l2 - 3 + 1, j2 + i3 + 3, l2 + j3 + 3 - 1, 1347420415, 1344798847);
/*  88 */       func_73733_a(j2 - 3, l2 - 3, j2 + i3 + 3, l2 - 3 + 1, 1347420415, 1347420415);
/*  89 */       func_73733_a(j2 - 3, l2 + j3 + 2, j2 + i3 + 3, l2 + j3 + 3, 1344798847, 1344798847);
/*     */       
/*  91 */       for (int j4 = 0; j4 < list.size(); j4++) {
/*  92 */         String s = (String)list.get(j4);
/*     */         
/*  94 */         if ((j4 == 0) && (stack != null)) {
/*  95 */           s = "§" + Integer.toHexString(stack.func_77953_t().field_77937_e.func_96298_a()) + s;
/*     */         }
/*     */         else {
/*  98 */           s = "§7" + s;
/*     */         }
/*     */         
/* 101 */         this.field_146289_q.func_78261_a(s, j2, l2, -1);
/*     */         
/* 103 */         if (j4 == 0) {
/* 104 */           l2 += 2;
/*     */         }
/*     */         
/* 107 */         l2 += 10;
/*     */       }
/*     */       
/* 110 */       this.field_73735_i = 0.0F;
/* 111 */       field_146296_j.field_77023_b = 0.0F;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void func_146976_a(float par1, int par2, int par3)
/*     */   {
/*     */     try
/*     */     {
/* 123 */       GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
/* 124 */       if ((this.temple.pujas != null) && (this.temple.pujas.type == 1)) {
/* 125 */         this.field_146297_k.field_71446_o.func_110577_a(textureSacrifices);
/*     */       } else {
/* 127 */         this.field_146297_k.field_71446_o.func_110577_a(texturePujas);
/*     */       }
/* 129 */       int j = (this.field_146294_l - this.field_146999_f) / 2;
/* 130 */       int k = (this.field_146295_m - this.field_147000_g) / 2;
/* 131 */       func_73729_b(j, k, 0, 0, this.field_146999_f, this.field_147000_g);
/*     */       
/* 133 */       if (this.temple.pujas != null)
/*     */       {
/* 135 */         int linePos = 0;
/* 136 */         int colPos = 0;
/*     */         
/* 138 */         for (int cp = 0; cp < this.temple.pujas.getTargets().size(); cp++) {
/* 139 */           if (this.temple.pujas.currentTarget == this.temple.pujas.getTargets().get(cp)) {
/* 140 */             func_73729_b(j + getTargetXStart() + colPos * getButtonWidth(), k + getTargetYStart() + getButtonHeight() * linePos, ((PujaSacrifice.PrayerTarget)this.temple.pujas.getTargets().get(cp)).startXact, ((PujaSacrifice.PrayerTarget)this.temple.pujas.getTargets().get(cp)).startYact, getButtonWidth(), getButtonHeight());
/*     */           }
/*     */           else {
/* 143 */             func_73729_b(j + getTargetXStart() + colPos * getButtonWidth(), k + getTargetYStart() + getButtonHeight() * linePos, ((PujaSacrifice.PrayerTarget)this.temple.pujas.getTargets().get(cp)).startX, ((PujaSacrifice.PrayerTarget)this.temple.pujas.getTargets().get(cp)).startY, getButtonWidth(), getButtonHeight());
/*     */           }
/*     */           
/*     */ 
/* 147 */           colPos++;
/*     */           
/* 149 */           if (colPos >= getNbPerLines()) {
/* 150 */             colPos = 0;
/* 151 */             linePos++;
/*     */           }
/*     */         }
/*     */         
/* 155 */         int progress = this.temple.pujas.getPujaProgressScaled(13);
/* 156 */         func_73729_b(j + 27, k + 39 + 13 - progress, 176, 13 - progress, 15, progress);
/*     */         
/* 158 */         progress = this.temple.pujas.getOfferingProgressScaled(16);
/* 159 */         func_73729_b(j + 84, k + 63 + 16 - progress, 176, 47 - progress, 19, progress);
/*     */       }
/*     */     } catch (Exception e) {
/* 162 */       MLN.printException("Exception in drawScreen: ", e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void func_146979_b(int par1, int par2)
/*     */   {
/* 172 */     if (this.temple.pujas.type == 1) {
/* 173 */       this.field_146289_q.func_78276_b(MLN.string("sacrifices.offering"), 8, 6, 4210752);
/* 174 */       this.field_146289_q.func_78276_b(MLN.string("sacrifices.panditfee"), 8, 75, 4210752);
/*     */     } else {
/* 176 */       this.field_146289_q.func_78276_b(MLN.string("pujas.offering"), 8, 6, 4210752);
/* 177 */       this.field_146289_q.func_78276_b(MLN.string("pujas.panditfee"), 8, 75, 4210752);
/*     */     }
/*     */     
/* 180 */     this.field_146289_q.func_78276_b(net.minecraft.util.StatCollector.func_74838_a("container.inventory"), 8, this.field_147000_g - 104 + 2, 4210752);
/*     */   }
/*     */   
/*     */ 
/*     */   public void func_73863_a(int x, int y, float f)
/*     */   {
/* 186 */     func_146276_q_();
/* 187 */     int k = (this.field_146294_l - this.field_146999_f) / 2;
/* 188 */     int l = (this.field_146295_m - this.field_147000_g) / 2;
/* 189 */     func_146976_a(f, x, y);
/* 190 */     GL11.glPushMatrix();
/* 191 */     GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
/* 192 */     RenderHelper.func_74519_b();
/* 193 */     GL11.glPopMatrix();
/* 194 */     GL11.glPushMatrix();
/* 195 */     GL11.glTranslatef(k, l, 0.0F);
/* 196 */     GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
/* 197 */     GL11.glEnable(32826);
/* 198 */     Slot slot = null;
/*     */     
/* 200 */     for (int i1 = 0; i1 < this.field_147002_h.field_75151_b.size(); i1++) {
/* 201 */       Slot slot1 = (Slot)this.field_147002_h.field_75151_b.get(i1);
/* 202 */       drawSlotInventory(slot1);
/*     */       
/* 204 */       if (getIsMouseOverSlot(slot1, x, y)) {
/* 205 */         slot = slot1;
/*     */         
/* 207 */         GL11.glDisable(2896);
/* 208 */         GL11.glDisable(2929);
/* 209 */         int j1 = slot1.field_75223_e;
/* 210 */         int l1 = slot1.field_75221_f;
/* 211 */         func_73733_a(j1, l1, j1 + 16, l1 + 16, -2130706433, -2130706433);
/* 212 */         GL11.glEnable(2896);
/* 213 */         GL11.glEnable(2929);
/*     */       }
/*     */     }
/*     */     
/* 217 */     InventoryPlayer inventoryplayer = this.field_146297_k.field_71439_g.field_71071_by;
/* 218 */     if (inventoryplayer.func_70445_o() != null) {
/* 219 */       GL11.glTranslatef(0.0F, 0.0F, 32.0F);
/* 220 */       field_146296_j.func_77015_a(this.field_146289_q, this.field_146297_k.field_71446_o, inventoryplayer.func_70445_o(), x - k - 8, y - l - 8);
/* 221 */       field_146296_j.func_77021_b(this.field_146289_q, this.field_146297_k.field_71446_o, inventoryplayer.func_70445_o(), x - k - 8, y - l - 8);
/*     */     }
/* 223 */     GL11.glDisable(32826);
/* 224 */     RenderHelper.func_74518_a();
/* 225 */     GL11.glDisable(2896);
/* 226 */     GL11.glDisable(2929);
/* 227 */     func_146979_b(x, y);
/* 228 */     if ((inventoryplayer.func_70445_o() == null) && (slot != null)) {
/* 229 */       List<String> list = null;
/* 230 */       ItemStack itemstack = null;
/*     */       
/* 232 */       if (slot.func_75216_d()) {
/* 233 */         itemstack = slot.func_75211_c();
/* 234 */         list = itemstack.func_82840_a(this.field_146297_k.field_71439_g, this.field_146297_k.field_71474_y.field_82882_x);
/* 235 */       } else if ((slot instanceof org.millenaire.common.ContainerPuja.OfferingSlot)) {
/* 236 */         list = new ArrayList();
/* 237 */         list.add("§6" + MLN.string("pujas.offeringslot"));
/* 238 */         list.add("§7" + MLN.string("pujas.offeringslot2"));
/* 239 */       } else if ((slot instanceof org.millenaire.common.ContainerPuja.MoneySlot)) {
/* 240 */         list = new ArrayList();
/* 241 */         list.add("§6" + MLN.string("pujas.moneyslot"));
/* 242 */       } else if ((slot instanceof org.millenaire.common.ContainerPuja.ToolSlot)) {
/* 243 */         list = new ArrayList();
/* 244 */         list.add("§6" + MLN.string("pujas.toolslot"));
/*     */       }
/*     */       
/* 247 */       displayItemOverlay(itemstack, list, x - k, y - l);
/*     */     }
/*     */     
/*     */ 
/* 251 */     int startx = (this.field_146294_l - this.field_146999_f) / 2;
/* 252 */     int starty = (this.field_146295_m - this.field_147000_g) / 2;
/*     */     
/* 254 */     if (this.temple.pujas != null)
/*     */     {
/* 256 */       int linePos = 0;
/* 257 */       int colPos = 0;
/*     */       
/* 259 */       for (int cp = 0; cp < this.temple.pujas.getTargets().size(); cp++)
/*     */       {
/* 261 */         if ((x > startx + getTargetXStart() + colPos * getButtonWidth()) && (x < startx + getTargetXStart() + (colPos + 1) * getButtonWidth()) && (y > starty + getTargetYStart() + getButtonHeight() * linePos) && (y < starty + getTargetYStart() + getButtonHeight() * (linePos + 1)))
/*     */         {
/* 263 */           String s = MLN.string(((PujaSacrifice.PrayerTarget)this.temple.pujas.getTargets().get(cp)).mouseOver);
/* 264 */           int stringlength = this.field_146289_q.func_78256_a(s);
/*     */           
/* 266 */           func_73733_a(x - startx + 5, y - starty - 3, x - startx + stringlength + 3, y - starty + 8 + 3, -1073741824, -1073741824);
/* 267 */           this.field_146289_q.func_78276_b(s, x + 8 - startx, y - starty, 10526880);
/*     */         }
/*     */         
/* 270 */         colPos++;
/*     */         
/* 272 */         if (colPos >= getNbPerLines()) {
/* 273 */           colPos = 0;
/* 274 */           linePos++;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 280 */     GL11.glPopMatrix();
/* 281 */     drawScreenGUIScreen(x, y, f);
/* 282 */     GL11.glEnable(2896);
/* 283 */     GL11.glEnable(2929);
/*     */   }
/*     */   
/*     */   public void drawScreenGUIScreen(int i, int j, float f) {
/*     */     try {
/* 288 */       for (int k = 0; k < this.field_146292_n.size(); k++) {
/* 289 */         GuiButton guibutton = (GuiButton)this.field_146292_n.get(k);
/* 290 */         guibutton.func_146112_a(this.field_146297_k, i, j);
/*     */       }
/*     */     } catch (Exception e) {
/* 293 */       MLN.printException("Exception in drawScreenGUIScreen: ", e);
/*     */     }
/*     */   }
/*     */   
/*     */   public void drawSlotInventory(Slot slot)
/*     */   {
/*     */     try
/*     */     {
/* 301 */       this.drawSlotInventory.invoke(this, new Object[] { slot });
/*     */     } catch (Exception e) {
/* 303 */       MLN.printException("Exception when trying to access drawSlotInventory", e);
/*     */     }
/*     */   }
/*     */   
/*     */   private int getButtonHeight() {
/* 308 */     if (this.temple.pujas == null) {
/* 309 */       return 0;
/*     */     }
/*     */     
/* 312 */     if (this.temple.pujas.type == 0) {
/* 313 */       return 17;
/*     */     }
/*     */     
/* 316 */     if (this.temple.pujas.type == 1) {
/* 317 */       return 20;
/*     */     }
/*     */     
/* 320 */     return 0;
/*     */   }
/*     */   
/*     */   private int getButtonWidth() {
/* 324 */     if (this.temple.pujas == null) {
/* 325 */       return 0;
/*     */     }
/*     */     
/* 328 */     if (this.temple.pujas.type == 0) {
/* 329 */       return 46;
/*     */     }
/*     */     
/* 332 */     if (this.temple.pujas.type == 1) {
/* 333 */       return 20;
/*     */     }
/*     */     
/* 336 */     return 0;
/*     */   }
/*     */   
/*     */   private boolean getIsMouseOverSlot(Slot slot, int i, int j) {
/* 340 */     int k = (this.field_146294_l - this.field_146999_f) / 2;
/* 341 */     int l = (this.field_146295_m - this.field_147000_g) / 2;
/* 342 */     i -= k;
/* 343 */     j -= l;
/* 344 */     return (i >= slot.field_75223_e - 1) && (i < slot.field_75223_e + 16 + 1) && (j >= slot.field_75221_f - 1) && (j < slot.field_75221_f + 16 + 1);
/*     */   }
/*     */   
/*     */   private int getNbPerLines() {
/* 348 */     if (this.temple.pujas == null) {
/* 349 */       return 1;
/*     */     }
/*     */     
/* 352 */     if (this.temple.pujas.type == 0) {
/* 353 */       return 1;
/*     */     }
/*     */     
/* 356 */     if (this.temple.pujas.type == 1) {
/* 357 */       return 3;
/*     */     }
/*     */     
/* 360 */     return 1;
/*     */   }
/*     */   
/*     */   private int getTargetXStart() {
/* 364 */     if (this.temple.pujas == null) {
/* 365 */       return 0;
/*     */     }
/*     */     
/* 368 */     if (this.temple.pujas.type == 0) {
/* 369 */       return 118;
/*     */     }
/*     */     
/* 372 */     if (this.temple.pujas.type == 1) {
/* 373 */       return 110;
/*     */     }
/*     */     
/* 376 */     return 0;
/*     */   }
/*     */   
/*     */   private int getTargetYStart() {
/* 380 */     if (this.temple.pujas == null) {
/* 381 */       return 0;
/*     */     }
/*     */     
/* 384 */     if (this.temple.pujas.type == 0) {
/* 385 */       return 22;
/*     */     }
/*     */     
/* 388 */     if (this.temple.pujas.type == 1) {
/* 389 */       return 22;
/*     */     }
/*     */     
/* 392 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */   protected void func_73864_a(int x, int y, int par3)
/*     */   {
/* 398 */     super.func_73864_a(x, y, par3);
/*     */     
/* 400 */     int startx = (this.field_146294_l - this.field_146999_f) / 2;
/* 401 */     int starty = (this.field_146295_m - this.field_147000_g) / 2;
/*     */     
/* 403 */     if (this.temple.pujas != null)
/*     */     {
/* 405 */       int linePos = 0;
/* 406 */       int colPos = 0;
/*     */       
/* 408 */       for (int cp = 0; cp < this.temple.pujas.getTargets().size(); cp++)
/*     */       {
/* 410 */         if ((x > startx + getTargetXStart() + colPos * getButtonWidth()) && (x < startx + getTargetXStart() + (colPos + 1) * getButtonWidth()) && (y > starty + getTargetYStart() + getButtonHeight() * linePos) && (y < starty + getTargetYStart() + getButtonHeight() * (linePos + 1)))
/*     */         {
/* 412 */           org.millenaire.client.network.ClientSender.pujasChangeEnchantment(this.player, this.temple, cp);
/*     */         }
/*     */         
/* 415 */         colPos++;
/*     */         
/* 417 */         if (colPos >= getNbPerLines()) {
/* 418 */           colPos = 0;
/* 419 */           linePos++;
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\client\gui\GuiPujas.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */