/*    */ package org.millenaire.common.block;
/*    */ 
/*    */ import java.util.Random;
/*    */ import net.minecraft.block.BlockSign;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.world.IBlockAccess;
/*    */ import net.minecraft.world.World;
/*    */ import net.minecraftforge.common.util.ForgeDirection;
/*    */ import org.millenaire.common.MillWorld;
/*    */ import org.millenaire.common.Point;
/*    */ import org.millenaire.common.TileEntityPanel;
/*    */ import org.millenaire.common.building.Building;
/*    */ import org.millenaire.common.forge.Mill;
/*    */ import org.millenaire.common.network.ServerSender;
/*    */ 
/*    */ public class BlockPanel
/*    */   extends BlockSign
/*    */ {
/*    */   public BlockPanel(Class class1, boolean flag)
/*    */   {
/* 21 */     super(class1, flag);
/*    */   }
/*    */   
/*    */   public int getFireSpreadSpeed(IBlockAccess world, int x, int y, int z, ForgeDirection face)
/*    */   {
/* 26 */     return 60;
/*    */   }
/*    */   
/*    */   public int getFlammability(IBlockAccess world, int x, int y, int z, ForgeDirection face)
/*    */   {
/* 31 */     return 200;
/*    */   }
/*    */   
/*    */ 
/*    */   public boolean func_149727_a(World world, int i, int j, int k, EntityPlayer entityplayer, int par6, float par7, float par8, float par9)
/*    */   {
/* 37 */     if (world.field_72995_K) {
/* 38 */       return true;
/*    */     }
/*    */     
/* 41 */     TileEntityPanel panel = (TileEntityPanel)world.func_147438_o(i, j, k);
/*    */     
/* 43 */     if ((panel == null) || (panel.panelType == 0)) {
/* 44 */       return false;
/*    */     }
/*    */     
/* 47 */     Building building = Mill.getMillWorld(world).getBuilding(panel.buildingPos);
/*    */     
/* 49 */     if (building == null) {
/* 50 */       return false;
/*    */     }
/*    */     
/* 53 */     if ((panel.panelType == 4) && (building.controlledBy(entityplayer.getDisplayName()))) {
/* 54 */       ServerSender.displayControlledProjectGUI(entityplayer, building);
/* 55 */       return true;
/*    */     }
/*    */     
/* 58 */     if ((panel.panelType == 13) && (building.controlledBy(entityplayer.getDisplayName()))) {
/* 59 */       ServerSender.displayControlledMilitaryGUI(entityplayer, building);
/* 60 */       return true;
/*    */     }
/*    */     
/* 63 */     ServerSender.displayPanel(entityplayer, new Point(i, j, k));
/*    */     
/* 65 */     return true;
/*    */   }
/*    */   
/*    */   public int func_149745_a(Random random)
/*    */   {
/* 70 */     return 0;
/*    */   }
/*    */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\block\BlockPanel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */