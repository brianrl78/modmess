/*    */ package org.millenaire.client.gui;
/*    */ 
/*    */ import net.minecraft.client.gui.inventory.GuiChest;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.world.World;
/*    */ import org.millenaire.common.TileEntityMillChest;
/*    */ 
/*    */ public class GuiMillChest extends GuiChest
/*    */ {
/*    */   EntityPlayer player;
/*    */   
/*    */   public static GuiMillChest createGUI(World world, int i, int j, int k, EntityPlayer entityplayer)
/*    */   {
/* 14 */     TileEntityMillChest lockedchest = (TileEntityMillChest)world.func_147438_o(i, j, k);
/*    */     
/* 16 */     if ((lockedchest == null) || ((world.field_72995_K) && (!lockedchest.loaded))) {
/* 17 */       return null;
/*    */     }
/*    */     
/* 20 */     net.minecraft.inventory.IInventory chest = org.millenaire.common.block.BlockMillChest.getInventory(lockedchest, world, i, j, k);
/*    */     
/* 22 */     return new GuiMillChest(entityplayer, chest, lockedchest);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/* 27 */   boolean locked = true;
/*    */   
/*    */   private GuiMillChest(EntityPlayer player, net.minecraft.inventory.IInventory iinventory1, TileEntityMillChest lockedchest) {
/* 30 */     super(player.field_71071_by, iinventory1);
/* 31 */     this.player = player;
/*    */     
/* 33 */     this.locked = lockedchest.isLockedFor(player);
/*    */   }
/*    */   
/*    */   protected void func_73869_a(char par1, int par2)
/*    */   {
/* 38 */     if (!this.locked) {
/* 39 */       super.func_73869_a(par1, par2);
/*    */     }
/* 41 */     else if ((par2 == 1) || (par2 == this.field_146297_k.field_71474_y.field_151445_Q.func_151463_i())) {
/* 42 */       this.field_146297_k.field_71439_g.func_71053_j();
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */   protected void func_73864_a(int i, int j, int k)
/*    */   {
/* 49 */     if (!this.locked) {
/* 50 */       super.func_73864_a(i, j, k);
/*    */     }
/*    */   }
/*    */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\client\gui\GuiMillChest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */