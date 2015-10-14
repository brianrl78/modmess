/*     */ package org.millenaire.common;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.inventory.Slot;
/*     */ import net.minecraft.item.ItemAxe;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.item.ItemSword;
/*     */ import net.minecraft.world.World;
/*     */ import org.millenaire.common.building.Building;
/*     */ import org.millenaire.common.forge.Mill;
/*     */ 
/*     */ public class ContainerPuja extends net.minecraft.inventory.Container
/*     */ {
/*     */   EntityPlayer player;
/*     */   PujaSacrifice shrine;
/*     */   ToolSlot slotTool;
/*     */   
/*     */   public static class MoneySlot extends Slot
/*     */   {
/*     */     PujaSacrifice shrine;
/*     */     
/*     */     public MoneySlot(PujaSacrifice shrine, int par2, int par3, int par4)
/*     */     {
/*  25 */       super(par2, par3, par4);
/*  26 */       this.shrine = shrine;
/*     */     }
/*     */     
/*     */     public boolean func_75214_a(ItemStack is)
/*     */     {
/*  31 */       return (is.func_77973_b() == Mill.denier) || (is.func_77973_b() == Mill.denier_or) || (is.func_77973_b() == Mill.denier_argent);
/*     */     }
/*     */     
/*     */     public void func_75218_e()
/*     */     {
/*  36 */       if (!this.shrine.temple.worldObj.field_72995_K) {
/*  37 */         this.shrine.temple.getTownHall().requestSave("Puja money slot changed");
/*     */       }
/*  39 */       super.func_75218_e();
/*     */     }
/*     */   }
/*     */   
/*     */   public static class OfferingSlot extends Slot
/*     */   {
/*     */     PujaSacrifice shrine;
/*     */     
/*     */     public OfferingSlot(PujaSacrifice shrine, int par2, int par3, int par4)
/*     */     {
/*  49 */       super(par2, par3, par4);
/*  50 */       this.shrine = shrine;
/*     */     }
/*     */     
/*     */     public boolean func_75214_a(ItemStack par1ItemStack)
/*     */     {
/*  55 */       return this.shrine.getOfferingValue(par1ItemStack) > 0;
/*     */     }
/*     */     
/*     */     public void func_75218_e()
/*     */     {
/*  60 */       if (!this.shrine.temple.worldObj.field_72995_K) {
/*  61 */         this.shrine.temple.getTownHall().requestSave("Puja offering slot changed");
/*     */       }
/*  63 */       super.func_75218_e();
/*     */     }
/*     */   }
/*     */   
/*     */   public static class ToolSlot extends Slot
/*     */   {
/*     */     PujaSacrifice shrine;
/*     */     
/*     */     public ToolSlot(PujaSacrifice shrine, int par2, int par3, int par4) {
/*  72 */       super(par2, par3, par4);
/*  73 */       this.shrine = shrine;
/*     */     }
/*     */     
/*     */     public boolean func_75214_a(ItemStack is)
/*     */     {
/*  78 */       net.minecraft.item.Item item = is.func_77973_b();
/*     */       
/*  80 */       if (this.shrine.type == 1) {
/*  81 */         return ((item instanceof ItemSword)) || ((item instanceof net.minecraft.item.ItemArmor)) || ((item instanceof net.minecraft.item.ItemBow)) || ((item instanceof ItemAxe));
/*     */       }
/*     */       
/*  84 */       return ((item instanceof net.minecraft.item.ItemSpade)) || ((item instanceof ItemAxe)) || ((item instanceof net.minecraft.item.ItemPickaxe));
/*     */     }
/*     */     
/*     */ 
/*     */     public void func_75218_e()
/*     */     {
/*  90 */       this.shrine.calculateOfferingsNeeded();
/*     */       
/*  92 */       if (!this.shrine.temple.worldObj.field_72995_K) {
/*  93 */         this.shrine.temple.getTownHall().requestSave("Puja tool slot changed");
/*     */       }
/*     */       
/*  96 */       super.func_75218_e();
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
/*     */   public ContainerPuja(EntityPlayer player, Building temple)
/*     */   {
/*     */     try
/*     */     {
/* 111 */       this.player = player;
/*     */       
/* 113 */       this.shrine = temple.pujas;
/*     */       
/* 115 */       this.slotTool = new ToolSlot(temple.pujas, 4, 86, 37);
/*     */       
/* 117 */       func_75146_a(new OfferingSlot(temple.pujas, 0, 26, 19));
/* 118 */       func_75146_a(new MoneySlot(temple.pujas, 1, 8, 55));
/* 119 */       func_75146_a(new MoneySlot(temple.pujas, 2, 26, 55));
/* 120 */       func_75146_a(new MoneySlot(temple.pujas, 3, 44, 55));
/* 121 */       func_75146_a(this.slotTool);
/*     */       
/* 123 */       for (int i = 0; i < 3; i++) {
/* 124 */         for (int k = 0; k < 9; k++) {
/* 125 */           func_75146_a(new Slot(player.field_71071_by, k + i * 9 + 9, 8 + k * 18, 106 + i * 18));
/*     */         }
/*     */       }
/*     */       
/* 129 */       for (int j = 0; j < 9; j++) {
/* 130 */         func_75146_a(new Slot(player.field_71071_by, j, 8 + j * 18, 164));
/*     */       }
/*     */     } catch (Exception e) {
/* 133 */       MLN.printException("Exception in ContainerPuja(): ", e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean func_75145_c(EntityPlayer entityplayer)
/*     */   {
/* 140 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ItemStack func_82846_b(EntityPlayer par1EntityPlayer, int stackID)
/*     */   {
/* 149 */     ItemStack itemstack = null;
/* 150 */     Slot slot = (Slot)this.field_75151_b.get(stackID);
/*     */     
/* 152 */     if ((slot != null) && (slot.func_75216_d())) {
/* 153 */       ItemStack itemstack1 = slot.func_75211_c();
/* 154 */       itemstack = itemstack1.func_77946_l();
/*     */       
/* 156 */       if (stackID == 4)
/*     */       {
/* 158 */         if (!func_75135_a(itemstack1, 5, 41, true)) {
/* 159 */           return null;
/*     */         }
/*     */         
/* 162 */         slot.func_75220_a(itemstack1, itemstack);
/* 163 */       } else if (stackID > 4)
/*     */       {
/* 165 */         if ((itemstack1.func_77973_b() == Mill.denier) || (itemstack1.func_77973_b() == Mill.denier_argent) || (itemstack1.func_77973_b() == Mill.denier_or)) {
/* 166 */           if (!func_75135_a(itemstack1, 1, 4, false)) {
/* 167 */             return null;
/*     */           }
/* 169 */         } else if (this.shrine.getOfferingValue(itemstack1) > 0) {
/* 170 */           if (!func_75135_a(itemstack1, 0, 1, false)) {
/* 171 */             return null;
/*     */           }
/* 173 */         } else if (this.slotTool.func_75214_a(itemstack1)) {
/* 174 */           if (!func_75135_a(itemstack1, 4, 5, false)) {
/* 175 */             return null;
/*     */           }
/* 177 */         } else if ((stackID >= 5) && (stackID < 32)) {
/* 178 */           if (!func_75135_a(itemstack1, 30, 39, false)) {
/* 179 */             return null;
/*     */           }
/* 181 */         } else if ((stackID >= 32) && (stackID < 41) && (!func_75135_a(itemstack1, 5, 32, false))) {
/* 182 */           return null;
/*     */         }
/* 184 */       } else if (!func_75135_a(itemstack1, 5, 41, false)) {
/* 185 */         return null;
/*     */       }
/*     */       
/* 188 */       if (itemstack1.field_77994_a == 0) {
/* 189 */         slot.func_75215_d((ItemStack)null);
/*     */       } else {
/* 191 */         slot.func_75218_e();
/*     */       }
/*     */       
/* 194 */       if (itemstack1.field_77994_a == itemstack.field_77994_a) {
/* 195 */         return null;
/*     */       }
/*     */       
/* 198 */       slot.func_82870_a(par1EntityPlayer, itemstack1);
/*     */     }
/*     */     
/* 201 */     return itemstack;
/*     */   }
/*     */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\ContainerPuja.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */