/*    */ package org.millenaire.common;
/*    */ 
/*    */ import net.minecraft.block.Block;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.world.World;
/*    */ import net.minecraft.world.chunk.Chunk;
/*    */ import org.millenaire.common.core.MillCommonUtilities;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class WorldWrapper
/*    */ {
/*    */   World world;
/*    */   
/*    */   public WorldWrapper() {}
/*    */   
/*    */   public WorldWrapper(World pworld)
/*    */   {
/* 19 */     this.world = pworld;
/*    */   }
/*    */   
/*    */   public int findTopGroundBlock(int x, int z)
/*    */   {
/* 24 */     int y = findTopSoilBlock(x, z);
/*    */     
/* 26 */     while ((y < 127) && (MillCommonUtilities.isBlockIdSolid(this.world.func_147439_a(x, y, z)))) {
/* 27 */       y++;
/*    */     }
/*    */     
/* 30 */     return y;
/*    */   }
/*    */   
/*    */   public int findTopSoilBlock(int x, int z) {
/* 34 */     return MillCommonUtilities.findTopSoilBlock(this.world, x, z);
/*    */   }
/*    */   
/*    */   public Block getBlockId(int x, int y, int z) {
/* 38 */     return this.world.func_147439_a(x, y, z);
/*    */   }
/*    */   
/*    */   public EntityPlayer getClosestPlayer(int i, int j, int k, int l) {
/* 42 */     return this.world.func_72977_a(i, j, k, l);
/*    */   }
/*    */   
/*    */   public boolean isChunkLoaded(int i, int j) {
/* 46 */     Chunk chunk = this.world.func_72964_e(i, j);
/* 47 */     return chunk.field_76636_d;
/*    */   }
/*    */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\WorldWrapper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */