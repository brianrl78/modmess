/*     */ package org.millenaire.common;
/*     */ 
/*     */ import io.netty.buffer.ByteBufInputStream;
/*     */ import java.io.IOException;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.inventory.IInventory;
/*     */ import net.minecraft.inventory.ISidedInventory;
/*     */ import net.minecraft.inventory.InventoryLargeChest;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.tileentity.TileEntityChest;
/*     */ import net.minecraft.world.World;
/*     */ import org.millenaire.common.building.Building;
/*     */ import org.millenaire.common.forge.Mill;
/*     */ import org.millenaire.common.network.ServerSender;
/*     */ import org.millenaire.common.network.StreamReadWrite;
/*     */ 
/*     */ public class TileEntityMillChest
/*     */   extends TileEntityChest implements ISidedInventory
/*     */ {
/*     */   public static class InventoryMillLargeChest
/*     */     extends InventoryLargeChest implements ISidedInventory
/*     */   {
/*     */     public InventoryMillLargeChest(String par1Str, IInventory par2iInventory, IInventory par3iInventory)
/*     */     {
/*  26 */       super(par2iInventory, par3iInventory);
/*     */     }
/*     */     
/*     */     public boolean func_102008_b(int i, ItemStack itemstack, int j)
/*     */     {
/*  31 */       return false;
/*     */     }
/*     */     
/*     */     public boolean func_102007_a(int i, ItemStack itemstack, int j)
/*     */     {
/*  36 */       return false;
/*     */     }
/*     */     
/*     */     public int[] func_94128_d(int var1)
/*     */     {
/*  41 */       return new int[0];
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public static void readUpdatePacket(ByteBufInputStream ds, World world)
/*     */   {
/*  48 */     Point pos = null;
/*     */     try {
/*  50 */       pos = StreamReadWrite.readNullablePoint(ds);
/*     */     } catch (IOException e) {
/*  52 */       MLN.printException(e);
/*  53 */       return;
/*     */     }
/*     */     
/*  56 */     TileEntityMillChest te = pos.getMillChest(world);
/*     */     
/*  58 */     if (te != null) {
/*     */       try {
/*  60 */         te.buildingPos = StreamReadWrite.readNullablePoint(ds);
/*  61 */         te.serverDevMode = ds.readBoolean();
/*     */         
/*  63 */         byte nb = ds.readByte();
/*  64 */         for (int i = 0; i < nb; i++) {
/*  65 */           te.func_70299_a(i, StreamReadWrite.readNullableItemStack(ds));
/*     */         }
/*     */         
/*  68 */         te.loaded = true;
/*     */       }
/*     */       catch (IOException e) {
/*  71 */         MLN.printException(te + ": Error in readUpdatePacket", e);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*  77 */   public Point buildingPos = null;
/*  78 */   public boolean loaded = false;
/*     */   
/*  80 */   public boolean serverDevMode = false;
/*     */   
/*     */   public boolean func_102008_b(int i, ItemStack itemstack, int j)
/*     */   {
/*  84 */     return false;
/*     */   }
/*     */   
/*     */   public boolean func_102007_a(int i, ItemStack itemstack, int j)
/*     */   {
/*  89 */     return false;
/*     */   }
/*     */   
/*     */   public int[] func_94128_d(int var1)
/*     */   {
/*  94 */     return new int[0];
/*     */   }
/*     */   
/*     */ 
/*     */   public String func_145825_b()
/*     */   {
/* 100 */     if (this.buildingPos == null) {
/* 101 */       return MLN.string("ui.unlockedchest");
/*     */     }
/*     */     
/* 104 */     Building building = null;
/*     */     
/* 106 */     if (Mill.clientWorld != null) {
/* 107 */       building = Mill.clientWorld.getBuilding(this.buildingPos);
/*     */     }
/*     */     
/* 110 */     if (building == null) {
/* 111 */       return MLN.string("ui.unlockedchest");
/*     */     }
/*     */     
/* 114 */     String s = building.getNativeBuildingName();
/*     */     
/* 116 */     if (building.chestLocked) {
/* 117 */       return s + ": " + MLN.string("ui.lockedchest");
/*     */     }
/* 119 */     return s + ": " + MLN.string("ui.unlockedchest");
/*     */   }
/*     */   
/*     */ 
/*     */   public String getInvLargeName()
/*     */   {
/* 125 */     if (this.buildingPos == null) {
/* 126 */       return MLN.string("ui.largeunlockedchest");
/*     */     }
/*     */     
/* 129 */     Building building = null;
/*     */     
/* 131 */     if (Mill.clientWorld != null) {
/* 132 */       building = Mill.clientWorld.getBuilding(this.buildingPos);
/*     */     }
/*     */     
/* 135 */     if (building == null) {
/* 136 */       return MLN.string("ui.largeunlockedchest");
/*     */     }
/*     */     
/* 139 */     String s = building.getNativeBuildingName();
/*     */     
/* 141 */     if (building.chestLocked) {
/* 142 */       return s + ": " + MLN.string("ui.largelockedchest");
/*     */     }
/* 144 */     return s + ": " + MLN.string("ui.largeunlockedchest");
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isLockedFor(EntityPlayer player)
/*     */   {
/* 150 */     if (player == null) {
/* 151 */       MLN.printException("Null player", new Exception());
/* 152 */       return true;
/*     */     }
/*     */     
/* 155 */     if ((!this.loaded) && (this.field_145850_b.field_72995_K)) {
/* 156 */       return true;
/*     */     }
/*     */     
/* 159 */     if (this.buildingPos == null) {
/* 160 */       return false;
/*     */     }
/*     */     
/* 163 */     if ((!this.field_145850_b.field_72995_K) && (MLN.DEV)) {
/* 164 */       return false;
/*     */     }
/*     */     
/* 167 */     if (this.serverDevMode) {
/* 168 */       return false;
/*     */     }
/*     */     
/* 171 */     MillWorld mw = Mill.getMillWorld(this.field_145850_b);
/*     */     
/* 173 */     if (mw == null) {
/* 174 */       MLN.printException("Null MillWorld", new Exception());
/* 175 */       return true;
/*     */     }
/*     */     
/* 178 */     Building building = mw.getBuilding(this.buildingPos);
/*     */     
/* 180 */     if (building == null) {
/* 181 */       return true;
/*     */     }
/*     */     
/* 184 */     if (building.lockedForPlayer(player.getDisplayName())) {
/* 185 */       return true;
/*     */     }
/* 187 */     return false;
/*     */   }
/*     */   
/*     */   public void func_145839_a(NBTTagCompound nbttagcompound)
/*     */   {
/* 192 */     super.func_145839_a(nbttagcompound);
/*     */     
/* 194 */     this.buildingPos = Point.read(nbttagcompound, "buildingPos");
/*     */   }
/*     */   
/*     */   public void sendUpdatePacket(EntityPlayer player) {
/* 198 */     ServerSender.sendLockedChestUpdatePacket(this, player);
/*     */   }
/*     */   
/*     */   public void func_145841_b(NBTTagCompound nbttagcompound)
/*     */   {
/* 203 */     super.func_145841_b(nbttagcompound);
/*     */     
/* 205 */     if (this.buildingPos != null) {
/* 206 */       this.buildingPos.write(nbttagcompound, "buildingPos");
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\TileEntityMillChest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */