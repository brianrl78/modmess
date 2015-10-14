/*     */ package org.millenaire.common.network;
/*     */ 
/*     */ import cpw.mods.fml.common.eventhandler.SubscribeEvent;
/*     */ import cpw.mods.fml.common.network.FMLNetworkEvent.ServerCustomPacketEvent;
/*     */ import cpw.mods.fml.common.network.internal.FMLProxyPacket;
/*     */ import io.netty.buffer.ByteBufInputStream;
/*     */ import java.io.DataOutput;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.player.EntityPlayerMP;
/*     */ import net.minecraft.network.NetHandlerPlayServer;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.server.management.ServerConfigurationManager;
/*     */ import org.millenaire.common.Culture;
/*     */ import org.millenaire.common.GuiActions;
/*     */ import org.millenaire.common.MLN;
/*     */ import org.millenaire.common.MillVillager;
/*     */ import org.millenaire.common.MillWorld;
/*     */ import org.millenaire.common.Point;
/*     */ import org.millenaire.common.UserProfile;
/*     */ import org.millenaire.common.building.Building;
/*     */ import org.millenaire.common.building.BuildingLocation;
/*     */ import org.millenaire.common.building.BuildingPlan;
/*     */ import org.millenaire.common.building.BuildingProject;
/*     */ import org.millenaire.common.core.DevModUtilities;
/*     */ import org.millenaire.common.forge.CommonProxy;
/*     */ import org.millenaire.common.forge.Mill;
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
/*     */ public class ServerReceiver
/*     */ {
/*     */   public static final String PACKET_CHANNEL = "millenaire";
/*     */   public static final int PACKET_BUILDING = 2;
/*     */   public static final int PACKET_VILLAGER = 3;
/*     */   public static final int PACKET_MILLCHEST = 5;
/*     */   public static final int PACKET_MAPINFO = 7;
/*     */   public static final int PACKET_VILLAGELIST = 9;
/*     */   public static final int PACKET_SERVER_CONTENT = 10;
/*     */   public static final int PACKET_SHOP = 11;
/*     */   public static final int PACKET_TRANSLATED_CHAT = 100;
/*     */   public static final int PACKET_PROFILE = 101;
/*     */   public static final int PACKET_QUESTINSTANCE = 102;
/*     */   public static final int PACKET_QUESTINSTANCEDELETE = 103;
/*     */   public static final int PACKET_OPENGUI = 104;
/*     */   public static final int PACKET_PANELUPDATE = 106;
/*     */   public static final int PACKET_ANIMALBREED = 107;
/*     */   public static final int PACKET_VILLAGER_SENTENCE = 108;
/*     */   public static final int PACKET_GUIACTION = 200;
/*     */   public static final int PACKET_VILLAGELIST_REQUEST = 201;
/*     */   public static final int PACKET_DECLARERELEASENUMBER = 202;
/*     */   public static final int PACKET_MAPINFO_REQUEST = 203;
/*     */   public static final int PACKET_VILLAGERINTERACT_REQUEST = 204;
/*     */   public static final int PACKET_AVAILABLECONTENT = 205;
/*     */   public static final int PACKET_DEVCOMMAND = 206;
/*     */   public static final int GUIACTION_CHIEF_BUILDING = 1;
/*     */   public static final int GUIACTION_CHIEF_CROP = 2;
/*     */   public static final int GUIACTION_CHIEF_CONTROL = 3;
/*     */   public static final int GUIACTION_CHIEF_DIPLOMACY = 4;
/*     */   public static final int GUIACTION_CHIEF_SCROLL = 5;
/*     */   public static final int GUIACTION_QUEST_COMPLETESTEP = 10;
/*     */   public static final int GUIACTION_QUEST_REFUSE = 11;
/*     */   public static final int GUIACTION_NEWVILLAGE = 20;
/*     */   public static final int GUIACTION_HIRE_HIRE = 30;
/*     */   public static final int GUIACTION_HIRE_EXTEND = 31;
/*     */   public static final int GUIACTION_HIRE_RELEASE = 32;
/*     */   public static final int GUIACTION_NEGATION_WAND = 40;
/*     */   public static final int GUIACTION_NEW_BUILDING_PROJECT = 50;
/*     */   public static final int GUIACTION_NEW_CUSTOM_BUILDING_PROJECT = 51;
/*     */   public static final int GUIACTION_UPDATE_CUSTOM_BUILDING_PROJECT = 52;
/*     */   public static final int GUIACTION_PUJAS_CHANGE_ENCHANTMENT = 60;
/*     */   public static final int GUIACTION_CONTROLLEDBUILDING_TOGGLEALLOWED = 70;
/*     */   public static final int GUIACTION_CONTROLLEDBUILDING_FORGET = 71;
/*     */   public static final int GUIACTION_SUMMONINGWANDUSE = 80;
/*     */   public static final int GUIACTION_MILLCHESTACTIVATE = 81;
/*     */   public static final int GUIACTION_IMPORTBUILDING = 82;
/*     */   public static final int GUIACTION_MILITARY_RELATIONS = 90;
/*     */   public static final int GUIACTION_MILITARY_RAID = 91;
/*     */   public static final int GUIACTION_MILITARY_CANCEL_RAID = 92;
/*     */   public static final int DEV_COMMAND_TOGGLE_AUTO_MOVE = 1;
/*     */   public static final int DEV_COMMAND_TEST_PATH = 2;
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onPacketData(FMLNetworkEvent.ServerCustomPacketEvent event)
/*     */   {
/*  99 */     if (Mill.serverWorlds.size() == 0) {
/* 100 */       return;
/*     */     }
/*     */     
/* 103 */     ByteBufInputStream dataStream = new ByteBufInputStream(event.packet.payload());
/* 104 */     EntityPlayerMP sender = ((NetHandlerPlayServer)event.handler).field_147369_b;
/* 105 */     MillWorld mw = Mill.getMillWorld(sender.field_70170_p);
/*     */     
/* 107 */     if (mw == null)
/*     */     {
/* 109 */       mw = (MillWorld)Mill.serverWorlds.get(0);
/*     */     }
/*     */     
/* 112 */     if (mw == null) {
/* 113 */       MLN.error(this, "ServerReceiver.onPacketData: could not find MillWorld.");
/*     */     }
/*     */     try
/*     */     {
/* 117 */       int packettype = dataStream.read();
/*     */       
/* 119 */       if (MLN.LogNetwork >= 3) {
/* 120 */         MLN.debug(this, "Receiving packet type " + packettype);
/*     */       }
/*     */       
/* 123 */       if (packettype == 200) {
/* 124 */         readGuiActionPacket(sender, dataStream);
/* 125 */       } else if (packettype == 203) {
/* 126 */         readMapInfoRequestPacket(sender, dataStream);
/* 127 */       } else if (packettype == 201) {
/* 128 */         mw.displayVillageList(sender, dataStream.readBoolean());
/* 129 */       } else if (packettype == 202) {
/* 130 */         mw.getProfile(sender.getDisplayName()).receiveDeclareReleaseNumberPacket(dataStream);
/* 131 */       } else if (packettype == 204) {
/* 132 */         readVillagerInteractRequestPacket(sender, dataStream);
/* 133 */       } else if (packettype == 205) {
/* 134 */         readAvailableContentPacket(sender, dataStream);
/* 135 */       } else if (packettype == 206) {
/* 136 */         readDevCommandPacket(sender, dataStream);
/*     */       }
/*     */     }
/*     */     catch (IOException e) {
/* 140 */       MLN.printException(e);
/*     */     }
/*     */   }
/*     */   
/*     */   private void readAvailableContentPacket(EntityPlayer player, ByteBufInputStream ds)
/*     */   {
/* 146 */     HashMap<String, Integer> nbStrings = new HashMap();
/* 147 */     HashMap<String, Integer> nbBuildingNames = new HashMap();
/* 148 */     HashMap<String, Integer> nbSentences = new HashMap();
/*     */     
/* 150 */     HashMap<String, Integer> nbFallbackStrings = new HashMap();
/* 151 */     HashMap<String, Integer> nbFallbackBuildingNames = new HashMap();
/* 152 */     HashMap<String, Integer> nbFallbackSentences = new HashMap();
/*     */     
/* 154 */     HashMap<String, List<String>> planSets = new HashMap();
/* 155 */     HashMap<String, List<String>> villagers = new HashMap();
/* 156 */     HashMap<String, List<String>> villages = new HashMap();
/* 157 */     HashMap<String, List<String>> lonebuildings = new HashMap();
/*     */     
/*     */     try
/*     */     {
/* 161 */       String clientMainLanguage = ds.readUTF();
/* 162 */       String clientFallbackLanguage = ds.readUTF();
/*     */       
/* 164 */       int nbCultures = ds.readShort();
/*     */       
/* 166 */       for (int i = 0; i < nbCultures; i++) {
/* 167 */         String key = ds.readUTF();
/*     */         
/* 169 */         nbStrings.put(key, Integer.valueOf(ds.readShort()));
/* 170 */         nbBuildingNames.put(key, Integer.valueOf(ds.readShort()));
/* 171 */         nbSentences.put(key, Integer.valueOf(ds.readShort()));
/*     */         
/* 173 */         nbFallbackStrings.put(key, Integer.valueOf(ds.readShort()));
/* 174 */         nbFallbackBuildingNames.put(key, Integer.valueOf(ds.readShort()));
/* 175 */         nbFallbackSentences.put(key, Integer.valueOf(ds.readShort()));
/*     */         
/* 177 */         List<String> v = new ArrayList();
/* 178 */         int nb = ds.readShort();
/* 179 */         for (int j = 0; j < nb; j++) {
/* 180 */           v.add(ds.readUTF());
/*     */         }
/* 182 */         planSets.put(key, v);
/*     */         
/* 184 */         v = new ArrayList();
/* 185 */         nb = ds.readShort();
/* 186 */         for (int j = 0; j < nb; j++) {
/* 187 */           v.add(ds.readUTF());
/*     */         }
/* 189 */         villagers.put(key, v);
/*     */         
/* 191 */         v = new ArrayList();
/* 192 */         nb = ds.readShort();
/* 193 */         for (int j = 0; j < nb; j++) {
/* 194 */           v.add(ds.readUTF());
/*     */         }
/* 196 */         villages.put(key, v);
/*     */         
/* 198 */         v = new ArrayList();
/* 199 */         nb = ds.readShort();
/* 200 */         for (int j = 0; j < nb; j++) {
/* 201 */           v.add(ds.readUTF());
/*     */         }
/* 203 */         lonebuildings.put(key, v);
/*     */       }
/*     */       
/* 206 */       DataOutput data = ServerSender.getNewByteBufOutputStream();
/*     */       
/* 208 */       data.write(10);
/*     */       
/* 210 */       data.writeShort(Culture.ListCultures.size());
/*     */       
/* 212 */       for (Culture culture : Culture.ListCultures) {
/* 213 */         if (!nbStrings.containsKey(culture.key)) {
/* 214 */           culture.writeCultureMissingContentPackPacket(data, clientMainLanguage, clientFallbackLanguage, 0, 0, 0, 0, 0, 0, null, null, null, null);
/*     */         } else {
/* 216 */           culture.writeCultureMissingContentPackPacket(data, clientMainLanguage, clientFallbackLanguage, ((Integer)nbStrings.get(culture.key)).intValue(), ((Integer)nbBuildingNames.get(culture.key)).intValue(), ((Integer)nbSentences.get(culture.key)).intValue(), ((Integer)nbFallbackStrings.get(culture.key)).intValue(), ((Integer)nbFallbackBuildingNames.get(culture.key)).intValue(), ((Integer)nbFallbackSentences.get(culture.key)).intValue(), (List)planSets.get(culture.key), (List)villagers.get(culture.key), (List)villages.get(culture.key), (List)lonebuildings.get(culture.key));
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 222 */       ServerSender.createAndSendPacketToPlayer(data, player);
/*     */     }
/*     */     catch (IOException e) {
/* 225 */       MLN.printException("Error in readAvailableContentPacket: ", e);
/*     */     }
/*     */   }
/*     */   
/*     */   private void readDevCommandPacket(EntityPlayer player, ByteBufInputStream data)
/*     */   {
/*     */     try {
/* 232 */       int commandId = data.read();
/*     */       
/* 234 */       if (commandId == 1) {
/* 235 */         DevModUtilities.toggleAutoMove(player);
/* 236 */       } else if (commandId == 2) {
/* 237 */         DevModUtilities.testPaths(player);
/*     */       }
/*     */     }
/*     */     catch (IOException e) {
/* 241 */       MLN.printException(e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private void readGuiActionPacket(EntityPlayer player, ByteBufInputStream data)
/*     */   {
/* 248 */     MillWorld mw = Mill.getMillWorld(player.field_70170_p);
/*     */     try
/*     */     {
/* 251 */       int guiActionId = data.read();
/*     */       
/* 253 */       if (guiActionId == 1) {
/* 254 */         MillVillager v = (MillVillager)mw.villagers.get(Long.valueOf(data.readLong()));
/* 255 */         if (v != null) {
/* 256 */           GuiActions.villageChiefPerformBuilding(player, v, data.readUTF());
/*     */         } else {
/* 258 */           MLN.error(this, "Unknown villager id in readGUIPacket: " + guiActionId);
/*     */         }
/* 260 */       } else if (guiActionId == 2) {
/* 261 */         MillVillager v = (MillVillager)mw.villagers.get(Long.valueOf(data.readLong()));
/* 262 */         if (v != null) {
/* 263 */           GuiActions.villageChiefPerformCrop(player, v, data.readUTF());
/*     */         } else {
/* 265 */           MLN.error(this, "Unknown villager id in readGUIPacket: " + guiActionId);
/*     */         }
/* 267 */       } else if (guiActionId == 3) {
/* 268 */         MillVillager v = (MillVillager)mw.villagers.get(Long.valueOf(data.readLong()));
/* 269 */         if (v != null) {
/* 270 */           GuiActions.villageChiefPerformCultureControl(player, v);
/*     */         } else {
/* 272 */           MLN.error(this, "Unknown villager id in readGUIPacket: " + guiActionId);
/*     */         }
/* 274 */       } else if (guiActionId == 4) {
/* 275 */         MillVillager v = (MillVillager)mw.villagers.get(Long.valueOf(data.readLong()));
/* 276 */         if (v != null) {
/* 277 */           GuiActions.villageChiefPerformDiplomacy(player, v, StreamReadWrite.readNullablePoint(data), data.readBoolean());
/*     */         } else {
/* 279 */           MLN.error(this, "Unknown villager id in readGUIPacket: " + guiActionId);
/*     */         }
/* 281 */       } else if (guiActionId == 5) {
/* 282 */         long vid = data.readLong();
/* 283 */         MillVillager v = (MillVillager)mw.villagers.get(Long.valueOf(vid));
/* 284 */         if (v != null) {
/* 285 */           GuiActions.villageChiefPerformVillageScroll(player, v);
/*     */         } else {
/* 287 */           MLN.error(this, "Unknown villager id in readGUIPacket: " + vid);
/*     */         }
/* 289 */       } else if (guiActionId == 10) {
/* 290 */         long vid = data.readLong();
/* 291 */         MillVillager v = (MillVillager)mw.villagers.get(Long.valueOf(vid));
/* 292 */         if (v != null) {
/* 293 */           GuiActions.questCompleteStep(player, v);
/*     */         } else {
/* 295 */           MLN.error(this, "Unknown villager id in readGUIPacket: " + vid);
/*     */         }
/* 297 */       } else if (guiActionId == 11) {
/* 298 */         long vid = data.readLong();
/* 299 */         MillVillager v = (MillVillager)mw.villagers.get(Long.valueOf(vid));
/* 300 */         if (v != null) {
/* 301 */           GuiActions.questRefuse(player, v);
/*     */         } else {
/* 303 */           MLN.error(this, "Unknown villager id in readGUIPacket: " + vid);
/*     */         }
/* 305 */       } else if (guiActionId == 20) {
/* 306 */         String cultureKey = data.readUTF();
/* 307 */         String villageType = data.readUTF();
/* 308 */         Point pos = StreamReadWrite.readNullablePoint(data);
/* 309 */         GuiActions.newVillageCreation(player, pos, cultureKey, villageType);
/*     */       }
/* 311 */       else if (guiActionId == 40)
/*     */       {
/* 313 */         Point pos = StreamReadWrite.readNullablePoint(data);
/* 314 */         Building th = mw.getBuilding(pos);
/* 315 */         if (th != null) {
/* 316 */           GuiActions.useNegationWand(player, th);
/*     */         }
/*     */       }
/* 319 */       else if (guiActionId == 80) {
/* 320 */         Point pos = StreamReadWrite.readNullablePoint(data);
/* 321 */         GuiActions.useSummoningWand(player, pos);
/*     */       }
/* 323 */       else if (guiActionId == 82) {
/* 324 */         Point pos = StreamReadWrite.readNullablePoint(data);
/*     */         
/* 326 */         EntityPlayerMP playerMP = (EntityPlayerMP)player;
/*     */         
/* 328 */         if ((!Mill.proxy.isTrueServer()) || (playerMP.field_71133_b.func_71203_ab().func_152596_g(playerMP.func_146103_bH()))) {
/* 329 */           BuildingPlan.importBuilding(player, ((MillWorld)Mill.serverWorlds.get(0)).world, pos);
/*     */         } else {
/* 331 */           ServerSender.sendTranslatedSentence(player, '4', "ui.serverimportforbidden", new String[0]);
/*     */         }
/*     */       }
/* 334 */       else if (guiActionId == 81) {
/* 335 */         Point pos = StreamReadWrite.readNullablePoint(data);
/* 336 */         GuiActions.activateMillChest(player, pos);
/*     */       }
/* 338 */       else if (guiActionId == 60) {
/* 339 */         Point pos = StreamReadWrite.readNullablePoint(data);
/* 340 */         Building temple = mw.getBuilding(pos);
/* 341 */         if ((temple != null) && (temple.pujas != null)) {
/* 342 */           GuiActions.pujasChangeEnchantment(player, temple, data.readShort());
/*     */         }
/*     */       }
/* 345 */       else if (guiActionId == 50) {
/* 346 */         Point thPos = StreamReadWrite.readNullablePoint(data);
/* 347 */         Point pos = StreamReadWrite.readNullablePoint(data);
/* 348 */         String planKey = data.readUTF();
/* 349 */         Building th = mw.getBuilding(thPos);
/* 350 */         if (th != null) {
/* 351 */           GuiActions.newBuilding(player, th, pos, planKey);
/*     */         }
/*     */       }
/* 354 */       else if (guiActionId == 51) {
/* 355 */         Point thPos = StreamReadWrite.readNullablePoint(data);
/* 356 */         Point pos = StreamReadWrite.readNullablePoint(data);
/* 357 */         String planKey = data.readUTF();
/* 358 */         Building th = mw.getBuilding(thPos);
/*     */         
/* 360 */         if (th != null) {
/* 361 */           GuiActions.newCustomBuilding(player, th, pos, planKey);
/*     */         }
/*     */       }
/* 364 */       else if (guiActionId == 52) {
/* 365 */         Point pos = StreamReadWrite.readNullablePoint(data);
/* 366 */         Building building = mw.getBuilding(pos);
/*     */         
/* 368 */         if (building != null) {
/* 369 */           GuiActions.updateCustomBuilding(player, building);
/*     */         }
/*     */       }
/* 372 */       else if (guiActionId == 70) {
/* 373 */         Point thPos = StreamReadWrite.readNullablePoint(data);
/* 374 */         String projectKey = data.readUTF();
/* 375 */         Point projectPos = StreamReadWrite.readNullablePoint(data);
/* 376 */         boolean allow = data.readBoolean();
/* 377 */         Building th = mw.getBuilding(thPos);
/* 378 */         if (th != null) {
/* 379 */           BuildingProject project = null;
/*     */           
/* 381 */           for (BuildingProject p : th.getFlatProjectList()) {
/* 382 */             if ((p.key.equals(projectKey)) && (p.location != null) && (p.location.pos.equals(projectPos))) {
/* 383 */               project = p;
/*     */             }
/*     */           }
/*     */           
/* 387 */           if (project != null) {
/* 388 */             GuiActions.controlledBuildingsToggleUpgrades(player, th, project, allow);
/*     */           }
/*     */         }
/*     */       }
/* 392 */       else if (guiActionId == 71) {
/* 393 */         Point thPos = StreamReadWrite.readNullablePoint(data);
/* 394 */         String projectKey = data.readUTF();
/* 395 */         Point projectPos = StreamReadWrite.readNullablePoint(data);
/* 396 */         Building th = mw.getBuilding(thPos);
/* 397 */         if (th != null) {
/* 398 */           BuildingProject project = null;
/*     */           
/* 400 */           for (BuildingProject p : th.getFlatProjectList()) {
/* 401 */             if ((p.key.equals(projectKey)) && (p.location != null) && (p.location.pos.equals(projectPos))) {
/* 402 */               project = p;
/*     */             }
/*     */           }
/*     */           
/* 406 */           if (project != null) {
/* 407 */             GuiActions.controlledBuildingsForgetBuilding(player, th, project);
/*     */           }
/*     */         }
/*     */       }
/* 411 */       else if (guiActionId == 30) {
/* 412 */         long vid = data.readLong();
/* 413 */         MillVillager v = (MillVillager)mw.villagers.get(Long.valueOf(vid));
/* 414 */         if (v != null) {
/* 415 */           GuiActions.hireHire(player, v);
/*     */         } else {
/* 417 */           MLN.error(this, "Unknown villager id in readGUIPacket: " + vid);
/*     */         }
/* 419 */       } else if (guiActionId == 31) {
/* 420 */         long vid = data.readLong();
/* 421 */         MillVillager v = (MillVillager)mw.villagers.get(Long.valueOf(vid));
/* 422 */         if (v != null) {
/* 423 */           GuiActions.hireExtend(player, v);
/*     */         } else {
/* 425 */           MLN.error(this, "Unknown villager id in readGUIPacket: " + vid);
/*     */         }
/* 427 */       } else if (guiActionId == 32) {
/* 428 */         long vid = data.readLong();
/* 429 */         MillVillager v = (MillVillager)mw.villagers.get(Long.valueOf(vid));
/* 430 */         if (v != null) {
/* 431 */           GuiActions.hireRelease(player, v);
/*     */         } else {
/* 433 */           MLN.error(this, "Unknown villager id in readGUIPacket: " + vid);
/*     */         }
/* 435 */       } else if (guiActionId == 90) {
/* 436 */         Point thPos = StreamReadWrite.readNullablePoint(data);
/* 437 */         Point targetpos = StreamReadWrite.readNullablePoint(data);
/* 438 */         int amount = data.readInt();
/* 439 */         Building th = mw.getBuilding(thPos);
/* 440 */         if (th != null) {
/* 441 */           GuiActions.controlledMilitaryDiplomacy(player, th, targetpos, amount);
/*     */         }
/* 443 */       } else if (guiActionId == 91) {
/* 444 */         Point thPos = StreamReadWrite.readNullablePoint(data);
/* 445 */         Point targetpos = StreamReadWrite.readNullablePoint(data);
/* 446 */         Building th = mw.getBuilding(thPos);
/* 447 */         Building target = mw.getBuilding(targetpos);
/* 448 */         if (th != null) {
/* 449 */           GuiActions.controlledMilitaryPlanRaid(player, th, target);
/*     */         }
/* 451 */       } else if (guiActionId == 92) {
/* 452 */         Point thPos = StreamReadWrite.readNullablePoint(data);
/* 453 */         Building th = mw.getBuilding(thPos);
/* 454 */         if (th != null) {
/* 455 */           GuiActions.controlledMilitaryCancelRaid(player, th);
/*     */         }
/*     */       } else {
/* 458 */         MLN.error(null, "Unknown Gui action: " + guiActionId);
/*     */       }
/*     */     }
/*     */     catch (IOException e) {
/* 462 */       MLN.printException(e);
/*     */     }
/*     */   }
/*     */   
/*     */   private void readMapInfoRequestPacket(EntityPlayer player, ByteBufInputStream data)
/*     */   {
/* 468 */     MillWorld mw = Mill.getMillWorld(player.field_70170_p);
/*     */     try
/*     */     {
/* 471 */       Point p = StreamReadWrite.readNullablePoint(data);
/*     */       
/* 473 */       Building townHall = mw.getBuilding(p);
/*     */       
/* 475 */       if (townHall != null) {
/* 476 */         townHall.sendMapInfo(player);
/*     */       }
/*     */     }
/*     */     catch (IOException e) {
/* 480 */       MLN.printException(e);
/*     */     }
/*     */   }
/*     */   
/*     */   private void readVillagerInteractRequestPacket(EntityPlayer player, ByteBufInputStream data)
/*     */   {
/* 486 */     MillWorld mw = Mill.getMillWorld(player.field_70170_p);
/*     */     try
/*     */     {
/* 489 */       long vid = data.readLong();
/*     */       
/* 491 */       if (mw.villagers.containsKey(Long.valueOf(vid))) {
/* 492 */         ((MillVillager)mw.villagers.get(Long.valueOf(vid))).interactSpecial(player);
/*     */       }
/*     */     }
/*     */     catch (IOException e) {
/* 496 */       MLN.printException(e);
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\network\ServerReceiver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */