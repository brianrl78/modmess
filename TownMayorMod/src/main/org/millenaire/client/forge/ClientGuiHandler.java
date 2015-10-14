/*    */ package org.millenaire.client.forge;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.tileentity.TileEntity;
/*    */ import net.minecraft.world.World;
/*    */ import org.millenaire.client.gui.GuiMillChest;
/*    */ import org.millenaire.client.gui.GuiPujas;
/*    */ import org.millenaire.client.gui.GuiTrade;
/*    */ import org.millenaire.common.MillVillager;
/*    */ import org.millenaire.common.MillWorld;
/*    */ import org.millenaire.common.Point;
/*    */ import org.millenaire.common.TileEntityMillChest;
/*    */ import org.millenaire.common.building.Building;
/*    */ import org.millenaire.common.core.MillCommonUtilities;
/*    */ import org.millenaire.common.forge.CommonGuiHandler;
/*    */ import org.millenaire.common.forge.Mill;
/*    */ 
/*    */ public class ClientGuiHandler extends CommonGuiHandler
/*    */ {
/*    */   public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
/*    */   {
/* 23 */     if (ID == 1) {
/* 24 */       TileEntity te = world.func_147438_o(x, y, z);
/* 25 */       if ((te != null) && ((te instanceof TileEntityMillChest))) {
/* 26 */         return GuiMillChest.createGUI(world, x, y, z, player);
/*    */       }
/* 28 */     } else if (ID == 2) {
/* 29 */       Building building = Mill.clientWorld.getBuilding(new Point(x, y, z));
/*    */       
/* 31 */       if ((building != null) && (building.getTownHall() != null)) {
/* 32 */         return new GuiTrade(player, building);
/*    */       }
/* 34 */     } else if (ID == 8) {
/* 35 */       long id = MillCommonUtilities.unpackLong(x, y);
/* 36 */       if (Mill.clientWorld.villagers.containsKey(Long.valueOf(id))) {
/* 37 */         return new GuiTrade(player, (MillVillager)Mill.clientWorld.villagers.get(Long.valueOf(id)));
/*    */       }
/* 39 */       org.millenaire.common.MLN.error(player, "Failed to find merchant: " + id);
/*    */     }
/* 41 */     else if (ID == 6) {
/* 42 */       Building building = Mill.clientWorld.getBuilding(new Point(x, y, z));
/*    */       
/* 44 */       if ((building != null) && (building.pujas != null)) {
/* 45 */         return new GuiPujas(player, building);
/*    */       }
/*    */     }
/*    */     
/* 49 */     return null;
/*    */   }
/*    */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\client\forge\ClientGuiHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */