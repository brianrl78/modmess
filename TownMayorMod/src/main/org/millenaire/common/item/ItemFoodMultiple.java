/*    */ package org.millenaire.common.item;
/*    */ 
/*    */ import net.minecraft.client.renderer.texture.IIconRegister;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.item.EnumAction;
/*    */ import net.minecraft.item.ItemFood;
/*    */ import net.minecraft.item.ItemStack;
/*    */ import net.minecraft.potion.Potion;
/*    */ import net.minecraft.potion.PotionEffect;
/*    */ import net.minecraft.world.World;
/*    */ import org.millenaire.common.core.MillCommonUtilities;
/*    */ import org.millenaire.common.forge.Mill;
/*    */ import org.millenaire.common.forge.MillAchievements;
/*    */ 
/*    */ public class ItemFoodMultiple extends ItemFood
/*    */ {
/*    */   private final int healthAmount;
/*    */   private final boolean drink;
/*    */   private final int regenerationDuration;
/*    */   private final int drunkDuration;
/*    */   public final String iconName;
/*    */   
/*    */   public ItemFoodMultiple(String iconName, int healthAmount, int regenerationDuration, int foodAmount, float saturation, boolean drink, int drunkDuration)
/*    */   {
/* 25 */     super(foodAmount, saturation, false);
/* 26 */     this.healthAmount = healthAmount;
/* 27 */     this.drink = drink;
/* 28 */     this.regenerationDuration = regenerationDuration;
/* 29 */     this.drunkDuration = drunkDuration;
/*    */     
/* 31 */     if (healthAmount > 0) {
/* 32 */       func_77848_i();
/*    */     }
/*    */     
/* 35 */     func_77637_a(Mill.tabMillenaire);
/*    */     
/* 37 */     this.iconName = iconName;
/*    */   }
/*    */   
/*    */   public EnumAction func_77661_b(ItemStack itemstack)
/*    */   {
/* 42 */     if (this.drink) {
/* 43 */       return EnumAction.drink;
/*    */     }
/*    */     
/* 46 */     return EnumAction.eat;
/*    */   }
/*    */   
/*    */ 
/*    */   public ItemStack func_77654_b(ItemStack itemstack, World world, EntityPlayer entityplayer)
/*    */   {
/* 52 */     itemstack.field_77994_a -= 1;
/* 53 */     world.func_72956_a(entityplayer, "random.burp", 0.5F, world.field_73012_v.nextFloat() * 0.1F + 0.9F);
/*    */     
/* 55 */     entityplayer.func_71024_bL().func_151686_a(this, itemstack);
/* 56 */     entityplayer.func_70691_i(this.healthAmount);
/*    */     
/* 58 */     if (this.drink) {
/* 59 */       entityplayer.func_71064_a(MillAchievements.cheers, 1);
/*    */     }
/*    */     
/* 62 */     if (this.regenerationDuration > 0) {
/* 63 */       entityplayer.func_70690_d(new PotionEffect(Potion.field_76428_l.field_76415_H, this.regenerationDuration * 20, 0));
/*    */     }
/*    */     
/* 66 */     if (this.drunkDuration > 0) {
/* 67 */       entityplayer.func_70690_d(new PotionEffect(Potion.field_76431_k.field_76415_H, this.drunkDuration * 20, 0));
/*    */     }
/*    */     
/* 70 */     func_77849_c(itemstack, world, entityplayer);
/*    */     
/* 72 */     return itemstack;
/*    */   }
/*    */   
/*    */   public ItemStack func_77659_a(ItemStack itemstack, World world, EntityPlayer entityplayer)
/*    */   {
/* 77 */     entityplayer.func_71008_a(itemstack, func_77626_a(itemstack));
/*    */     
/* 79 */     return itemstack;
/*    */   }
/*    */   
/*    */   public void func_94581_a(IIconRegister iconRegister)
/*    */   {
/* 84 */     this.field_77791_bV = MillCommonUtilities.getIcon(iconRegister, this.iconName);
/*    */   }
/*    */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\item\ItemFoodMultiple.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */