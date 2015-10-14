/*     */ package org.millenaire.client.network;
/*     */ 
/*     */ import cpw.mods.fml.common.FMLCommonHandler;
/*     */ import cpw.mods.fml.common.eventhandler.SubscribeEvent;
/*     */ import cpw.mods.fml.common.network.FMLNetworkEvent.ClientCustomPacketEvent;
/*     */ import cpw.mods.fml.common.network.internal.FMLProxyPacket;
/*     */ import cpw.mods.fml.relauncher.ReflectionHelper;
/*     */ import cpw.mods.fml.relauncher.Side;
/*     */ import io.netty.buffer.ByteBufInputStream;
/*     */ import java.io.IOException;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.passive.EntityAnimal;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import org.millenaire.client.MillClientUtilities;
/*     */ import org.millenaire.client.gui.DisplayActions;
/*     */ import org.millenaire.common.Culture;
/*     */ import org.millenaire.common.MLN;
/*     */ import org.millenaire.common.MillMapInfo;
/*     */ import org.millenaire.common.MillVillager;
/*     */ import org.millenaire.common.MillWorld;
/*     */ import org.millenaire.common.Point;
/*     */ import org.millenaire.common.TileEntityMillChest;
/*     */ import org.millenaire.common.TileEntityPanel;
/*     */ import org.millenaire.common.UserProfile;
/*     */ import org.millenaire.common.building.Building;
/*     */ import org.millenaire.common.building.BuildingLocation;
/*     */ import org.millenaire.common.core.MillCommonUtilities;
/*     */ import org.millenaire.common.forge.CommonProxy;
/*     */ import org.millenaire.common.forge.Mill;
/*     */ import org.millenaire.common.network.StreamReadWrite;
/*     */ 
/*     */ 
/*     */ public class ClientReceiver
/*     */ {
/*     */   @SubscribeEvent
/*     */   public void onPacketData(FMLNetworkEvent.ClientCustomPacketEvent event)
/*     */   {
/*  40 */     if ((FMLCommonHandler.instance().getSide().isServer()) && (MLN.LogNetwork >= 1)) {
/*  41 */       MLN.major(this, "Received a packet despite being server.");
/*  42 */       return;
/*     */     }
/*     */     
/*  45 */     if (event.packet == null) {
/*  46 */       MLN.error(this, "Received a null packet!");
/*  47 */       return;
/*     */     }
/*     */     
/*  50 */     if (event.packet.payload() == null) {
/*  51 */       MLN.error(this, "Received a packet with null data on channel: " + event.packet.channel());
/*  52 */       return;
/*     */     }
/*     */     
/*  55 */     if (Mill.clientWorld == null) {
/*  56 */       MLN.error(this, "Received a packet despite null clientWorld.");
/*  57 */       return;
/*     */     }
/*     */     
/*  60 */     ByteBufInputStream data = new ByteBufInputStream(event.packet.payload());
/*     */     try
/*     */     {
/*  63 */       int packettype = data.read();
/*     */       
/*  65 */       Mill.clientWorld.millenaireEnabled = true;
/*     */       
/*  67 */       if (MLN.LogNetwork >= 3) {
/*  68 */         MLN.debug(this, "Received client packet type: " + packettype);
/*     */       }
/*     */       
/*  71 */       UserProfile profile = Mill.proxy.getClientProfile();
/*     */       
/*  73 */       if (packettype == 2) {
/*  74 */         Building.readBuildingPacket(Mill.clientWorld, data);
/*  75 */       } else if (packettype == 11) {
/*  76 */         Building.readShopPacket(Mill.clientWorld, data);
/*  77 */       } else if (packettype == 3) {
/*  78 */         MillVillager.readVillagerPacket(data);
/*  79 */       } else if (packettype == 100) {
/*  80 */         readTranslatedChatPackage(data);
/*  81 */       } else if (packettype == 108) {
/*  82 */         readVillagerSentencePackage(data);
/*  83 */       } else if (packettype == 5) {
/*  84 */         TileEntityMillChest.readUpdatePacket(data, Mill.clientWorld.world);
/*  85 */       } else if (packettype == 101) {
/*  86 */         profile.receiveProfilePacket(data);
/*  87 */       } else if (packettype == 102) {
/*  88 */         profile.receiveQuestInstancePacket(data);
/*  89 */       } else if (packettype == 103) {
/*  90 */         profile.receiveQuestInstanceDeletePacket(data);
/*  91 */       } else if (packettype == 104) {
/*  92 */         readGUIPacket(data);
/*  93 */       } else if (packettype == 7) {
/*  94 */         MillMapInfo.readPacket(data);
/*  95 */       } else if (packettype == 106) {
/*  96 */         TileEntityPanel.readPacket(data);
/*  97 */       } else if (packettype == 9) {
/*  98 */         Mill.clientWorld.receiveVillageListPacket(data);
/*  99 */       } else if (packettype == 10) {
/* 100 */         readServerContentPacket(data);
/* 101 */       } else if (packettype == 107) {
/* 102 */         readAnimalBreedPacket(data);
/*     */       } else {
/* 104 */         MLN.error(null, "Received packet with unknown type: " + packettype);
/*     */       }
/*     */     }
/*     */     catch (Exception e) {
/* 108 */       MLN.printException("Error in ClientReceiver.onPacketData:", e);
/*     */     }
/*     */   }
/*     */   
/*     */   private void readAnimalBreedPacket(ByteBufInputStream data)
/*     */   {
/*     */     try {
/* 115 */       Point p = StreamReadWrite.readNullablePoint(data);
/* 116 */       endId = data.readInt();
/*     */       
/* 118 */       List<Entity> animals = MillCommonUtilities.getEntitiesWithinAABB(Mill.clientWorld.world, EntityAnimal.class, p, 5, 5);
/*     */       
/* 120 */       for (Entity ent : animals)
/*     */       {
/* 122 */         EntityAnimal animal = (EntityAnimal)ent;
/*     */         
/* 124 */         if (animal.func_145782_y() == endId) {
/* 125 */           ReflectionHelper.setPrivateValue(EntityAnimal.class, animal, Integer.valueOf(600), 0);
/* 126 */           MillCommonUtilities.generateHearts(animal);
/*     */         }
/*     */       }
/*     */     } catch (IOException e) {
/*     */       int endId;
/* 131 */       MLN.printException(e);
/*     */     }
/*     */   }
/*     */   
/*     */   private void readGUIPacket(ByteBufInputStream data)
/*     */   {
/*     */     try {
/* 138 */       int guiId = data.read();
/*     */       
/* 140 */       if (guiId == 3) {
/* 141 */         MillVillager v = (MillVillager)Mill.clientWorld.villagers.get(Long.valueOf(data.readLong()));
/*     */         
/* 143 */         if (v != null) {
/* 144 */           DisplayActions.displayQuestGUI(Mill.proxy.getTheSinglePlayer(), v);
/*     */         } else {
/* 146 */           MLN.error(this, "Unknown villager id in readGUIPacket: " + guiId);
/*     */         }
/* 148 */       } else if (guiId == 12) {
/* 149 */         MillVillager v = (MillVillager)Mill.clientWorld.villagers.get(Long.valueOf(data.readLong()));
/*     */         
/* 151 */         if (v != null) {
/* 152 */           DisplayActions.displayHireGUI(Mill.proxy.getTheSinglePlayer(), v);
/*     */         } else {
/* 154 */           MLN.error(this, "Unknown villager id in readGUIPacket: " + guiId);
/*     */         }
/* 156 */       } else if (guiId == 4) {
/* 157 */         MillVillager v = (MillVillager)Mill.clientWorld.villagers.get(Long.valueOf(data.readLong()));
/*     */         
/* 159 */         if (v != null) {
/* 160 */           DisplayActions.displayVillageChiefGUI(Mill.proxy.getTheSinglePlayer(), v);
/*     */         } else {
/* 162 */           MLN.error(this, "Unknown villager id in readGUIPacket: " + guiId);
/*     */         }
/* 164 */       } else if (guiId == 5) {
/* 165 */         Point p = StreamReadWrite.readNullablePoint(data);
/*     */         
/* 167 */         if (p != null) {
/* 168 */           DisplayActions.displayVillageBookGUI(Mill.proxy.getTheSinglePlayer(), p);
/*     */         } else {
/* 170 */           MLN.error(this, "Unknown point in readGUIPacket: " + guiId);
/*     */         }
/* 172 */       } else if (guiId == 9) {
/* 173 */         Point p = StreamReadWrite.readNullablePoint(data);
/*     */         
/* 175 */         if (p != null) {
/* 176 */           Building building = Mill.clientWorld.getBuilding(p);
/* 177 */           if (building != null) {
/* 178 */             DisplayActions.displayNegationWandGUI(Mill.proxy.getTheSinglePlayer(), building);
/*     */           }
/*     */         } else {
/* 181 */           MLN.error(this, "Unknown point in readGUIPacket: " + guiId);
/*     */         }
/* 183 */       } else if (guiId == 10) {
/* 184 */         Point thPos = StreamReadWrite.readNullablePoint(data);
/* 185 */         Point pos = StreamReadWrite.readNullablePoint(data);
/*     */         
/* 187 */         if ((thPos != null) && (pos != null)) {
/* 188 */           Building townHall = Mill.clientWorld.getBuilding(thPos);
/* 189 */           if (townHall != null) {
/* 190 */             Building building = townHall.getBuildingAtCoordPlanar(pos);
/* 191 */             if ((building == null) || (!building.location.isCustomBuilding)) {
/* 192 */               DisplayActions.displayNewBuildingProjectGUI(Mill.proxy.getTheSinglePlayer(), townHall, pos);
/*     */             } else {
/* 194 */               DisplayActions.displayEditCustomBuildingGUI(Mill.proxy.getTheSinglePlayer(), building);
/*     */             }
/*     */           }
/*     */         } else {
/* 198 */           MLN.error(this, "Unknown point in readGUIPacket: " + guiId);
/*     */         }
/* 200 */       } else if (guiId == 13) {
/* 201 */         Point pos = StreamReadWrite.readNullablePoint(data);
/*     */         
/* 203 */         if (pos != null) {
/* 204 */           DisplayActions.displayNewVillageGUI(Mill.proxy.getTheSinglePlayer(), pos);
/*     */         } else {
/* 206 */           MLN.error(this, "Unknown point in readGUIPacket: " + guiId);
/*     */         }
/* 208 */       } else if (guiId == 11) {
/* 209 */         Point thPos = StreamReadWrite.readNullablePoint(data);
/*     */         
/* 211 */         if (thPos != null) {
/* 212 */           Building building = Mill.clientWorld.getBuilding(thPos);
/* 213 */           if (building != null) {
/* 214 */             DisplayActions.displayControlledProjectGUI(Mill.proxy.getTheSinglePlayer(), building);
/*     */           }
/*     */         } else {
/* 217 */           MLN.error(this, "Unknown point in readGUIPacket: " + guiId);
/*     */         }
/* 219 */       } else if (guiId == 14) {
/* 220 */         Point thPos = StreamReadWrite.readNullablePoint(data);
/*     */         
/* 222 */         if (thPos != null) {
/* 223 */           Building building = Mill.clientWorld.getBuilding(thPos);
/* 224 */           if (building != null) {
/* 225 */             DisplayActions.displayControlledMilitaryGUI(Mill.proxy.getTheSinglePlayer(), building);
/*     */           }
/*     */         } else {
/* 228 */           MLN.error(this, "Unknown point in readGUIPacket: " + guiId);
/*     */         }
/* 230 */       } else if (guiId == 7) {
/* 231 */         Point p = StreamReadWrite.readNullablePoint(data);
/*     */         
/* 233 */         if (p != null) {
/* 234 */           MillClientUtilities.displayPanel(Mill.clientWorld.world, Mill.proxy.getTheSinglePlayer(), p);
/*     */         } else {
/* 236 */           MLN.error(this, "Unknown point in readGUIPacket: " + guiId);
/*     */         }
/* 238 */       } else if (guiId == 2) {
/* 239 */         Point p = StreamReadWrite.readNullablePoint(data);
/*     */         
/* 241 */         if (p != null) {
/* 242 */           Mill.proxy.getTheSinglePlayer().openGui(Mill.instance, 2, Mill.clientWorld.world, p.getiX(), p.getiY(), p.getiZ());
/*     */         } else {
/* 244 */           MLN.error(this, "Unknown point in readGUIPacket: " + guiId);
/*     */         }
/* 246 */       } else if (guiId == 8) {
/* 247 */         int id1 = data.readInt();
/* 248 */         int id2 = data.readInt();
/* 249 */         Mill.proxy.getTheSinglePlayer().openGui(Mill.instance, 8, Mill.clientWorld.world, id1, id2, 0);
/* 250 */       } else if (guiId == 1) {
/* 251 */         Point p = StreamReadWrite.readNullablePoint(data);
/*     */         
/* 253 */         if (p != null) {
/* 254 */           TileEntityMillChest chest = p.getMillChest(Mill.clientWorld.world);
/* 255 */           if ((chest != null) && (chest.loaded)) {
/* 256 */             Mill.proxy.getTheSinglePlayer().openGui(Mill.instance, 1, Mill.clientWorld.world, p.getiX(), p.getiY(), p.getiZ());
/*     */           }
/*     */         } else {
/* 259 */           MLN.error(this, "Unknown point in readGUIPacket: " + guiId);
/*     */         }
/*     */       } else {
/* 262 */         MLN.error(null, "Unknown GUI: " + guiId);
/*     */       }
/*     */     }
/*     */     catch (IOException e) {
/* 266 */       MLN.printException(e);
/*     */     }
/*     */   }
/*     */   
/*     */   private void readServerContentPacket(ByteBufInputStream data)
/*     */   {
/*     */     try {
/* 273 */       int nbCultures = data.readShort();
/*     */       
/* 275 */       for (int i = 0; i < nbCultures; i++) {
/* 276 */         Culture.readCultureMissingContentPacket(data);
/*     */       }
/*     */       
/* 279 */       Culture.refreshLists();
/*     */     }
/*     */     catch (IOException e) {
/* 282 */       MLN.printException("Error in readServerContentPacket:", e);
/*     */     }
/*     */   }
/*     */   
/*     */   private void readTranslatedChatPackage(ByteBufInputStream data)
/*     */   {
/*     */     try {
/* 289 */       char colour = data.readChar();
/*     */       
/* 291 */       String s = data.readUTF();
/*     */       
/* 293 */       String[] values = new String[data.read()];
/*     */       
/* 295 */       for (int i = 0; i < values.length; i++) {
/* 296 */         values[i] = MLN.unknownString(StreamReadWrite.readNullableString(data));
/*     */       }
/*     */       
/* 299 */       s = MLN.string(s, values);
/*     */       
/* 301 */       Mill.proxy.sendLocalChat(Mill.proxy.getTheSinglePlayer(), colour, s);
/*     */     } catch (IOException e) {
/* 303 */       MLN.printException(e);
/*     */     }
/*     */   }
/*     */   
/*     */   private void readVillagerSentencePackage(ByteBufInputStream data)
/*     */   {
/*     */     try {
/* 310 */       MillVillager v = (MillVillager)Mill.clientWorld.villagers.get(Long.valueOf(data.readLong()));
/*     */       
/* 312 */       if (v != null) {
/* 313 */         MillClientUtilities.putVillagerSentenceInChat(v);
/*     */       }
/*     */     } catch (IOException e) {
/* 316 */       MLN.printException(e);
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\client\network\ClientReceiver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */