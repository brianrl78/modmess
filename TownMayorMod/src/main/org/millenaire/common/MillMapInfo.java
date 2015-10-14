/*     */ package org.millenaire.common;
/*     */ 
/*     */ import io.netty.buffer.ByteBufInputStream;
/*     */ import java.io.DataOutput;
/*     */ import java.io.IOException;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import org.millenaire.common.building.Building;
/*     */ import org.millenaire.common.building.BuildingLocation;
/*     */ import org.millenaire.common.forge.Mill;
/*     */ import org.millenaire.common.network.ServerSender;
/*     */ import org.millenaire.common.network.StreamReadWrite;
/*     */ import org.millenaire.common.pathing.AStarPathing;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MillMapInfo
/*     */ {
/*     */   public Building townHall;
/*     */   
/*     */   public static void readPacket(ByteBufInputStream ds)
/*     */   {
/*  38 */     Point pos = null;
/*     */     try {
/*  40 */       pos = StreamReadWrite.readNullablePoint(ds);
/*     */     } catch (IOException e) {
/*  42 */       MLN.printException(e);
/*  43 */       return;
/*     */     }
/*     */     
/*  46 */     Building building = Mill.clientWorld.getBuilding(pos);
/*     */     
/*  48 */     if (building == null) {
/*  49 */       return;
/*     */     }
/*     */     
/*  52 */     MillMapInfo minfo = new MillMapInfo(building);
/*     */     
/*     */     try
/*     */     {
/*  56 */       minfo.length = ds.readInt();
/*  57 */       minfo.width = ds.readInt();
/*  58 */       minfo.mapStartX = ds.readInt();
/*  59 */       minfo.mapStartZ = ds.readInt();
/*     */       
/*  61 */       minfo.data = new byte[minfo.length][];
/*     */       
/*  63 */       for (int x = 0; x < minfo.length; x++) {
/*  64 */         minfo.data[x] = new byte[minfo.width];
/*  65 */         for (int z = 0; z < minfo.width; z++) {
/*  66 */           minfo.data[x][z] = ds.readByte();
/*     */         }
/*     */       }
/*     */       
/*  70 */       building.mapInfo = minfo;
/*     */       
/*  72 */       if (MLN.LogNetwork >= 3) {
/*  73 */         MLN.debug(null, "Receiving map info packet.");
/*     */       }
/*     */     }
/*     */     catch (IOException e) {
/*  77 */       MLN.printException(e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  86 */   public int mapStartZ = 0; public int mapStartX = 0;
/*     */   public int length;
/*     */   public int width;
/*     */   public byte[][] data;
/*     */   
/*  91 */   private MillMapInfo(Building townHall) { this.townHall = townHall; }
/*     */   
/*     */   public static final byte PATH = 10;
/*     */   
/*     */   public MillMapInfo(Building townHall, MillWorldInfo winfo) {
/*  96 */     this.townHall = townHall;
/*     */     
/*  98 */     byte thRegionId = 0;
/*     */     
/* 100 */     if (townHall.pathing != null) {
/* 101 */       thRegionId = townHall.pathing.thRegion;
/*     */     }
/*     */     
/* 104 */     Point centre = townHall.location.pos;
/* 105 */     int relcentreX = centre.getiX() - winfo.mapStartX;
/* 106 */     int relcentreZ = centre.getiZ() - winfo.mapStartZ;
/*     */     
/* 108 */     this.width = winfo.width;
/* 109 */     this.length = winfo.length;
/* 110 */     this.mapStartX = winfo.mapStartX;
/* 111 */     this.mapStartZ = winfo.mapStartZ;
/*     */     
/* 113 */     this.data = new byte[winfo.length][];
/*     */     
/* 115 */     for (int x = 0; x < winfo.length; x++) {
/* 116 */       this.data[x] = new byte[winfo.width];
/* 117 */       for (int y = 0; y < winfo.width; y++)
/*     */       {
/* 119 */         if (winfo.water[x][y] != 0) {
/* 120 */           this.data[x][y] = 1;
/* 121 */         } else if (winfo.danger[x][y] != 0) {
/* 122 */           this.data[x][y] = 2;
/* 123 */         } else if (winfo.buildingForbidden[x][y] != 0) {
/* 124 */           this.data[x][y] = 3;
/* 125 */         } else if (winfo.buildingLoc[x][y] != 0) {
/* 126 */           this.data[x][y] = 4;
/* 127 */         } else if (winfo.tree[x][y] != 0) {
/* 128 */           this.data[x][y] = 5;
/* 129 */         } else if (winfo.path[x][y] != 0) {
/* 130 */           this.data[x][y] = 10;
/* 131 */         } else if ((townHall.pathing != null) && (townHall.pathing.regions[x][y] != thRegionId)) {
/* 132 */           this.data[x][y] = 6;
/* 133 */         } else if (winfo.canBuild[x][y] == 0) {
/* 134 */           this.data[x][y] = 7;
/* 135 */         } else if ((Math.abs(relcentreX - x) > townHall.villageType.radius) || (Math.abs(relcentreZ - y) > townHall.villageType.radius)) {
/* 136 */           this.data[x][y] = 8;
/*     */         } else
/* 138 */           this.data[x][y] = 9; }
/*     */     }
/*     */   }
/*     */   
/*     */   public static final byte OTHER = 9;
/*     */   public static final byte OUTOFRANGE = 8;
/*     */   
/* 145 */   public void sendMapInfoPacket(EntityPlayer player) { DataOutput ds = ServerSender.getNewByteBufOutputStream();
/*     */     
/*     */     try
/*     */     {
/* 149 */       ds.write(7);
/* 150 */       StreamReadWrite.writeNullablePoint(this.townHall.getPos(), ds);
/*     */       
/* 152 */       ds.writeInt(this.length);
/* 153 */       ds.writeInt(this.width);
/* 154 */       ds.writeInt(this.mapStartX);
/* 155 */       ds.writeInt(this.mapStartZ);
/*     */       
/* 157 */       for (int x = 0; x < this.length; x++) {
/* 158 */         for (int z = 0; z < this.width; z++) {
/* 159 */           ds.writeByte(this.data[x][z]);
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (IOException e) {
/* 164 */       MLN.printException(this + ": Error in sendUpdatePacket", e);
/*     */     }
/*     */     
/* 167 */     ServerSender.createAndSendPacketToPlayer(ds, player);
/*     */   }
/*     */   
/*     */   public static final byte UNBUILDABLE = 7;
/*     */   public static final byte UNREACHABLE = 6;
/*     */   public static final byte TREE = 5;
/*     */   public static final byte BUILDING_LOC = 4;
/*     */   public static final byte BUILDING_FORBIDDEN = 3;
/*     */   public static final byte DANGER = 2;
/*     */   public static final byte WATER = 1;
/*     */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\MillMapInfo.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */