/*    */ package org.millenaire.common.forge;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import net.minecraft.world.ChunkCoordIntPair;
/*    */ import net.minecraft.world.World;
/*    */ import net.minecraftforge.common.ForgeChunkManager;
/*    */ import net.minecraftforge.common.ForgeChunkManager.LoadingCallback;
/*    */ import net.minecraftforge.common.ForgeChunkManager.Ticket;
/*    */ import net.minecraftforge.common.ForgeChunkManager.Type;
/*    */ import org.millenaire.common.MillWorldInfo;
/*    */ import org.millenaire.common.building.Building;
/*    */ 
/*    */ public class BuildingChunkLoader
/*    */ {
/*    */   Building townHall;
/*    */   
/*    */   public static class ChunkLoaderCallback implements ForgeChunkManager.LoadingCallback
/*    */   {
/*    */     public void ticketsLoaded(List<ForgeChunkManager.Ticket> tickets, World world)
/*    */     {
/* 22 */       for (ForgeChunkManager.Ticket ticket : tickets) {
/* 23 */         ForgeChunkManager.releaseTicket(ticket);
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/* 30 */   List<ForgeChunkManager.Ticket> tickets = new ArrayList();
/*    */   
/* 32 */   public boolean chunksLoaded = false;
/*    */   
/*    */   public BuildingChunkLoader(Building th) {
/* 35 */     this.townHall = th;
/*    */   }
/*    */   
/*    */   private ForgeChunkManager.Ticket getTicket()
/*    */   {
/* 40 */     for (ForgeChunkManager.Ticket ticket : this.tickets) {
/* 41 */       if (ticket.getChunkList().size() < ticket.getChunkListDepth() - 1) {
/* 42 */         return ticket;
/*    */       }
/*    */     }
/*    */     
/* 46 */     ForgeChunkManager.Ticket ticket = ForgeChunkManager.requestTicket(Mill.instance, this.townHall.worldObj, ForgeChunkManager.Type.NORMAL);
/*    */     
/* 48 */     if (ticket == null) {
/* 49 */       org.millenaire.common.MLN.warning(this.townHall, "Couldn't get ticket in BuildingChunkLoader. Your Forge chunk loading settings must be interfearing.");
/* 50 */       return null;
/*    */     }
/* 52 */     this.tickets.add(ticket);
/* 53 */     return ticket;
/*    */   }
/*    */   
/*    */ 
/*    */   public void loadChunks()
/*    */   {
/* 59 */     if (this.townHall.winfo != null) {
/* 60 */       for (int cx = this.townHall.winfo.chunkStartX - 1; cx < this.townHall.winfo.chunkStartX + this.townHall.winfo.length / 16 + 1; cx++) {
/* 61 */         for (int cz = this.townHall.winfo.chunkStartZ - 1; cz < this.townHall.winfo.chunkStartZ + this.townHall.winfo.width / 16 + 1; cz++) {
/* 62 */           ForgeChunkManager.Ticket ticket = getTicket();
/*    */           
/* 64 */           if (ticket != null) {
/* 65 */             ChunkCoordIntPair chunkCoords = new ChunkCoordIntPair(cx, cz);
/* 66 */             ForgeChunkManager.forceChunk(ticket, chunkCoords);
/*    */           }
/*    */         }
/*    */       }
/* 70 */       this.chunksLoaded = true;
/*    */     }
/*    */   }
/*    */   
/*    */   public void unloadChunks() {
/* 75 */     for (ForgeChunkManager.Ticket ticket : this.tickets) {
/* 76 */       ForgeChunkManager.releaseTicket(ticket);
/*    */     }
/* 78 */     this.tickets.clear();
/* 79 */     this.chunksLoaded = false;
/*    */   }
/*    */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\forge\BuildingChunkLoader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */