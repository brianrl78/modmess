/*     */ package org.millenaire.common.item;
/*     */ 
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.world.World;
/*     */ import org.millenaire.common.MLN;
/*     */ import org.millenaire.common.core.MillCommonUtilities;
/*     */ import org.millenaire.common.forge.Mill;
/*     */ 
/*     */ public class ItemPurse
/*     */   extends Goods.ItemText
/*     */ {
/*     */   private static final String ML_PURSE_DENIER = "ml_Purse_denier";
/*     */   private static final String ML_PURSE_DENIERARGENT = "ml_Purse_denierargent";
/*     */   private static final String ML_PURSE_DENIEROR = "ml_Purse_denieror";
/*     */   private static final String ML_PURSE_RAND = "ml_Purse_rand";
/*     */   
/*     */   public ItemPurse(String iconName)
/*     */   {
/*  21 */     super(iconName);
/*     */   }
/*     */   
/*     */ 
/*     */   public ItemStack func_77659_a(ItemStack purse, World world, EntityPlayer player)
/*     */   {
/*  27 */     if (totalDeniers(purse) > 0) {
/*  28 */       removeDeniersFromPurse(purse, player);
/*     */     } else {
/*  30 */       storeDeniersInPurse(purse, player);
/*     */     }
/*     */     
/*  33 */     return super.func_77659_a(purse, world, player);
/*     */   }
/*     */   
/*     */   private void removeDeniersFromPurse(ItemStack purse, EntityPlayer player) {
/*  37 */     if (purse.field_77990_d != null) {
/*  38 */       int deniers = purse.field_77990_d.func_74762_e("ml_Purse_denier");
/*  39 */       int denierargent = purse.field_77990_d.func_74762_e("ml_Purse_denierargent");
/*  40 */       int denieror = purse.field_77990_d.func_74762_e("ml_Purse_denieror");
/*     */       
/*  42 */       int result = MillCommonUtilities.putItemsInChest(player.field_71071_by, Mill.denier, deniers);
/*  43 */       purse.field_77990_d.func_74768_a("ml_Purse_denier", deniers - result);
/*     */       
/*  45 */       result = MillCommonUtilities.putItemsInChest(player.field_71071_by, Mill.denier_argent, denierargent);
/*  46 */       purse.field_77990_d.func_74768_a("ml_Purse_denierargent", denierargent - result);
/*     */       
/*  48 */       result = MillCommonUtilities.putItemsInChest(player.field_71071_by, Mill.denier_or, denieror);
/*  49 */       purse.field_77990_d.func_74768_a("ml_Purse_denieror", denieror - result);
/*     */       
/*  51 */       purse.field_77990_d.func_74768_a("ml_Purse_rand", player.field_70170_p.field_72995_K ? 0 : 1);
/*     */       
/*  53 */       setItemName(purse);
/*     */     }
/*     */   }
/*     */   
/*     */   public void setDeniers(ItemStack purse, EntityPlayer player, int amount)
/*     */   {
/*  59 */     int denier = amount % 64;
/*  60 */     int denier_argent = (amount - denier) / 64 % 64;
/*  61 */     int denier_or = (amount - denier - denier_argent * 64) / 4096;
/*     */     
/*  63 */     setDeniers(purse, player, denier, denier_argent, denier_or);
/*     */   }
/*     */   
/*     */   public void setDeniers(ItemStack purse, EntityPlayer player, int denier, int denierargent, int denieror)
/*     */   {
/*  68 */     if (purse.field_77990_d == null) {
/*  69 */       purse.func_77982_d(new NBTTagCompound());
/*     */     }
/*     */     
/*  72 */     purse.field_77990_d.func_74768_a("ml_Purse_denier", denier);
/*  73 */     purse.field_77990_d.func_74768_a("ml_Purse_denierargent", denierargent);
/*  74 */     purse.field_77990_d.func_74768_a("ml_Purse_denieror", denieror);
/*     */     
/*  76 */     purse.field_77990_d.func_74768_a("ml_Purse_rand", player.field_70170_p.field_72995_K ? 0 : 1);
/*     */     
/*  78 */     setItemName(purse);
/*     */   }
/*     */   
/*     */   private void setItemName(ItemStack purse) {
/*  82 */     if (purse.field_77990_d == null) {
/*  83 */       purse.func_151001_c(MLN.string("item.purse"));
/*     */     } else {
/*  85 */       int deniers = purse.field_77990_d.func_74762_e("ml_Purse_denier");
/*  86 */       int denierargent = purse.field_77990_d.func_74762_e("ml_Purse_denierargent");
/*  87 */       int denieror = purse.field_77990_d.func_74762_e("ml_Purse_denieror");
/*     */       
/*  89 */       String label = "";
/*     */       
/*  91 */       if (denieror != 0) {
/*  92 */         label = "§e" + denieror + "o ";
/*     */       }
/*  94 */       if (denierargent != 0) {
/*  95 */         label = label + "§f" + denierargent + "a ";
/*     */       }
/*  97 */       if ((deniers != 0) || (label.length() == 0)) {
/*  98 */         label = label + "§6" + deniers + "d";
/*     */       }
/*     */       
/* 101 */       label.trim();
/*     */       
/* 103 */       purse.func_151001_c("§f" + MLN.string("item.purse") + ": " + label);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private void storeDeniersInPurse(ItemStack purse, EntityPlayer player)
/*     */   {
/* 110 */     int deniers = MillCommonUtilities.getItemsFromChest(player.field_71071_by, Mill.denier, 0, Integer.MAX_VALUE);
/* 111 */     int denierargent = MillCommonUtilities.getItemsFromChest(player.field_71071_by, Mill.denier_argent, 0, Integer.MAX_VALUE);
/* 112 */     int denieror = MillCommonUtilities.getItemsFromChest(player.field_71071_by, Mill.denier_or, 0, Integer.MAX_VALUE);
/*     */     
/* 114 */     int total = totalDeniers(purse) + deniers + denierargent * 64 + denieror * 64 * 64;
/*     */     
/* 116 */     int new_denier = total % 64;
/* 117 */     int new_denier_argent = (total - new_denier) / 64 % 64;
/* 118 */     int new_denier_or = (total - new_denier - new_denier_argent * 64) / 4096;
/*     */     
/* 120 */     setDeniers(purse, player, new_denier, new_denier_argent, new_denier_or);
/*     */   }
/*     */   
/*     */   public int totalDeniers(ItemStack purse) {
/* 124 */     if (purse.field_77990_d == null) {
/* 125 */       return 0;
/*     */     }
/*     */     
/* 128 */     int deniers = purse.field_77990_d.func_74762_e("ml_Purse_denier");
/* 129 */     int denierargent = purse.field_77990_d.func_74762_e("ml_Purse_denierargent");
/* 130 */     int denieror = purse.field_77990_d.func_74762_e("ml_Purse_denieror");
/*     */     
/* 132 */     return deniers + denierargent * 64 + denieror * 64 * 64;
/*     */   }
/*     */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\item\ItemPurse.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */