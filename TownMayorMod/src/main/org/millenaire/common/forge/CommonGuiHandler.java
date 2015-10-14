/*    */ package org.millenaire.common.forge;
/*    */ 
/*    */ import cpw.mods.fml.common.network.IGuiHandler;
/*    */ import java.util.HashMap;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.tileentity.TileEntity;
/*    */ import net.minecraft.world.World;
/*    */ import org.millenaire.common.ContainerPuja;
/*    */ import org.millenaire.common.ContainerTrade;
/*    */ import org.millenaire.common.MLN;
/*    */ import org.millenaire.common.MillVillager;
/*    */ import org.millenaire.common.MillWorld;
/*    */ import org.millenaire.common.Point;
/*    */ import org.millenaire.common.TileEntityMillChest;
/*    */ import org.millenaire.common.block.BlockMillChest;
/*    */ import org.millenaire.common.building.Building;
/*    */ import org.millenaire.common.core.MillCommonUtilities;
/*    */ 
/*    */ public class CommonGuiHandler
/*    */   implements IGuiHandler
/*    */ {
/*    */   public static final int GUI_MILL_CHEST = 1;
/*    */   public static final int GUI_TRADE = 2;
/*    */   public static final int GUI_QUEST = 3;
/*    */   public static final int GUI_VILLAGECHIEF = 4;
/*    */   public static final int GUI_VILLAGEBOOK = 5;
/*    */   public static final int GUI_PUJAS = 6;
/*    */   public static final int GUI_PANEL = 7;
/*    */   public static final int GUI_MERCHANT = 8;
/*    */   public static final int GUI_NEGATIONWAND = 9;
/*    */   public static final int GUI_NEWBUILDING = 10;
/*    */   public static final int GUI_CONTROLLEDPROJECTPANEL = 11;
/*    */   public static final int GUI_HIRE = 12;
/*    */   public static final int GUI_NEWVILLAGE = 13;
/*    */   public static final int GUI_CONTROLLEDMILITARYPANEL = 14;
/*    */   
/*    */   public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
/*    */   {
/* 39 */     return null;
/*    */   }
/*    */   
/*    */ 
/*    */   public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
/*    */   {
/* 45 */     MillWorld mw = Mill.getMillWorld(world);
/*    */     
/* 47 */     if (ID == 1) {
/* 48 */       TileEntity te = world.func_147438_o(x, y, z);
/* 49 */       if ((te != null) && ((te instanceof TileEntityMillChest))) {
/* 50 */         return BlockMillChest.createContainer(world, x, y, z, player);
/*    */       }
/* 52 */     } else if (ID == 2) {
/* 53 */       Building building = mw.getBuilding(new Point(x, y, z));
/*    */       
/* 55 */       if (building != null) {
/* 56 */         return new ContainerTrade(player, building);
/*    */       }
/* 58 */       MLN.error(this, "Server-side traiding for unknow building at " + new Point(x, y, z) + " in world: " + world);
/*    */     }
/* 60 */     else if (ID == 8) {
/* 61 */       long id = MillCommonUtilities.unpackLong(x, y);
/* 62 */       if (mw.villagers.containsKey(Long.valueOf(id))) {
/* 63 */         return new ContainerTrade(player, (MillVillager)mw.villagers.get(Long.valueOf(id)));
/*    */       }
/* 65 */       MLN.error(player, "Failed to find merchant: " + id);
/*    */     }
/* 67 */     else if (ID == 6) {
/* 68 */       Building building = mw.getBuilding(new Point(x, y, z));
/*    */       
/* 70 */       if ((building != null) && (building.pujas != null)) {
/* 71 */         return new ContainerPuja(player, building);
/*    */       }
/*    */     }
/*    */     
/* 75 */     return null;
/*    */   }
/*    */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\forge\CommonGuiHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */