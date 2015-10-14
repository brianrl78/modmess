/*     */ package org.millenaire.common.network;
/*     */ 
/*     */ import io.netty.buffer.ByteBufOutputStream;
/*     */ import io.netty.buffer.Unpooled;
/*     */ import java.io.DataOutput;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import net.minecraft.entity.passive.EntityAnimal;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.player.EntityPlayerMP;
/*     */ import net.minecraft.network.NetHandlerPlayServer;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.play.server.S3FPacketCustomPayload;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.server.management.ServerConfigurationManager;
/*     */ import net.minecraft.util.ChatComponentText;
/*     */ import net.minecraft.util.ChatStyle;
/*     */ import net.minecraft.util.EnumChatFormatting;
/*     */ import net.minecraft.world.World;
/*     */ import org.millenaire.common.MLN;
/*     */ import org.millenaire.common.MLN.MillenaireException;
/*     */ import org.millenaire.common.MillVillager;
/*     */ import org.millenaire.common.MillWorld;
/*     */ import org.millenaire.common.Point;
/*     */ import org.millenaire.common.TileEntityMillChest;
/*     */ import org.millenaire.common.TileEntityPanel;
/*     */ import org.millenaire.common.UserProfile;
/*     */ import org.millenaire.common.building.Building;
/*     */ import org.millenaire.common.core.MillCommonUtilities;
/*     */ import org.millenaire.common.forge.Mill;
/*     */ 
/*     */ 
/*     */ public class ServerSender
/*     */ {
/*     */   public static void createAndSendPacketToPlayer(DataOutput data, EntityPlayer player)
/*     */   {
/*  39 */     if (((EntityPlayerMP)player).field_71135_a == null) {
/*  40 */       MLN.printException(new MLN.MillenaireException("Trying to send packet to a player with null playerNetServerHandler"));
/*  41 */       return;
/*     */     }
/*     */     
/*  44 */     Packet packet = createServerPacket(data);
/*  45 */     ((EntityPlayerMP)player).field_71135_a.func_147359_a(packet);
/*     */   }
/*     */   
/*     */   public static S3FPacketCustomPayload createServerPacket(DataOutput data) {
/*  49 */     return new S3FPacketCustomPayload("millenaire", ((ByteBufOutputStream)data).buffer());
/*     */   }
/*     */   
/*     */   public static void displayControlledMilitaryGUI(EntityPlayer player, Building townHall) {
/*  53 */     townHall.sendBuildingPacket(player, false);
/*     */     
/*  55 */     townHall.sendBuildingPacket(player, false);
/*     */     
/*  57 */     MillWorld mw = Mill.getMillWorld(player.field_70170_p);
/*     */     
/*  59 */     for (Point p : townHall.getKnownVillages()) {
/*  60 */       Building b = mw.getBuilding(p);
/*  61 */       if (b != null) {
/*  62 */         b.sendBuildingPacket(player, false);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*  67 */     DataOutput data = getNewByteBufOutputStream();
/*     */     try
/*     */     {
/*  70 */       data.write(104);
/*  71 */       data.write(14);
/*  72 */       StreamReadWrite.writeNullablePoint(townHall.getPos(), data);
/*     */     } catch (IOException e) {
/*  74 */       MLN.printException(ServerSender.class + ": Error in displayControlledMilitaryGUI", e);
/*     */     }
/*     */     
/*  77 */     sendPacketToPlayer(createServerPacket(data), player);
/*     */   }
/*     */   
/*     */   public static void displayControlledProjectGUI(EntityPlayer player, Building townHall) {
/*  81 */     townHall.sendBuildingPacket(player, false);
/*     */     
/*  83 */     DataOutput data = getNewByteBufOutputStream();
/*     */     try
/*     */     {
/*  86 */       data.write(104);
/*  87 */       data.write(11);
/*  88 */       StreamReadWrite.writeNullablePoint(townHall.getPos(), data);
/*     */     } catch (IOException e) {
/*  90 */       MLN.printException(ServerSender.class + ": Error in displayControlledProjectGUI", e);
/*     */     }
/*     */     
/*  93 */     sendPacketToPlayer(createServerPacket(data), player);
/*     */   }
/*     */   
/*     */   public static void displayHireGUI(EntityPlayer player, MillVillager villager)
/*     */   {
/*  98 */     villager.getTownHall().sendBuildingPacket(player, false);
/*     */     
/* 100 */     DataOutput data = getNewByteBufOutputStream();
/*     */     try
/*     */     {
/* 103 */       data.write(104);
/* 104 */       data.write(12);
/* 105 */       data.writeLong(villager.villager_id);
/*     */     } catch (IOException e) {
/* 107 */       MLN.printException(ServerSender.class + ": Error in displayHireGUI", e);
/*     */     }
/*     */     
/* 110 */     sendPacketToPlayer(createServerPacket(data), player);
/*     */   }
/*     */   
/*     */   public static void displayMerchantTradeGUI(EntityPlayer player, MillVillager villager) {
/* 114 */     DataOutput data = getNewByteBufOutputStream();
/*     */     
/* 116 */     int[] ids = MillCommonUtilities.packLong(villager.villager_id);
/*     */     try
/*     */     {
/* 119 */       data.write(104);
/* 120 */       data.write(8);
/* 121 */       data.writeInt(ids[0]);
/* 122 */       data.writeInt(ids[1]);
/*     */     } catch (IOException e) {
/* 124 */       MLN.printException(ServerSender.class + ": Error in displayMerchantTradeGUI", e);
/*     */     }
/*     */     
/* 127 */     villager.getHouse().sendBuildingPacket(player, true);
/* 128 */     villager.getTownHall().sendBuildingPacket(player, true);
/* 129 */     sendPacketToPlayer(createServerPacket(data), player);
/*     */     
/* 131 */     player.openGui(Mill.instance, 8, player.field_70170_p, ids[0], ids[1], 0);
/*     */   }
/*     */   
/*     */ 
/*     */   public static void displayMillChest(EntityPlayer player, Point chestPos)
/*     */   {
/* 137 */     TileEntityMillChest chest = chestPos.getMillChest(player.field_70170_p);
/*     */     
/* 139 */     if (chest == null) {
/* 140 */       return;
/*     */     }
/*     */     
/* 143 */     MillWorld mw = Mill.getMillWorld(player.field_70170_p);
/*     */     
/* 145 */     if (chest.buildingPos != null) {
/* 146 */       Building building = mw.getBuilding(chest.buildingPos);
/* 147 */       if (building != null) {
/* 148 */         building.sendBuildingPacket(player, true);
/*     */       } else {
/* 150 */         chest.buildingPos = null;
/* 151 */         chest.sendUpdatePacket(player);
/*     */       }
/*     */     } else {
/* 154 */       chest.sendUpdatePacket(player);
/*     */     }
/*     */     
/* 157 */     DataOutput data = getNewByteBufOutputStream();
/*     */     try
/*     */     {
/* 160 */       data.write(104);
/* 161 */       data.write(1);
/* 162 */       StreamReadWrite.writeNullablePoint(chestPos, data);
/* 163 */       data.writeBoolean(chest.isLockedFor(player));
/*     */     } catch (IOException e) {
/* 165 */       MLN.printException(ServerSender.class + ": Error in displayMillChest", e);
/*     */     }
/*     */     
/* 168 */     sendPacketToPlayer(createServerPacket(data), player);
/*     */     
/* 170 */     player.openGui(Mill.instance, 1, player.field_70170_p, chestPos.getiX(), chestPos.getiY(), chestPos.getiZ());
/*     */   }
/*     */   
/*     */   public static void displayNegationWandGUI(EntityPlayer player, Building townHall)
/*     */   {
/* 175 */     townHall.sendBuildingPacket(player, false);
/*     */     
/* 177 */     DataOutput data = getNewByteBufOutputStream();
/*     */     try
/*     */     {
/* 180 */       data.write(104);
/* 181 */       data.write(9);
/* 182 */       StreamReadWrite.writeNullablePoint(townHall.getPos(), data);
/*     */     } catch (IOException e) {
/* 184 */       MLN.printException(ServerSender.class + ": Error in displayNegationWandGUI", e);
/*     */     }
/*     */     
/* 187 */     sendPacketToPlayer(createServerPacket(data), player);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void displayNewBuildingProjectGUI(EntityPlayer player, Building townHall, Point pos)
/*     */   {
/* 195 */     townHall.sendBuildingPacket(player, false);
/*     */     
/* 197 */     DataOutput data = getNewByteBufOutputStream();
/*     */     try
/*     */     {
/* 200 */       data.write(104);
/* 201 */       data.write(10);
/* 202 */       StreamReadWrite.writeNullablePoint(townHall.getPos(), data);
/* 203 */       StreamReadWrite.writeNullablePoint(pos, data);
/*     */     } catch (IOException e) {
/* 205 */       MLN.printException(ServerSender.class + ": Error in displayNewBuildingProjectGUI", e);
/*     */     }
/*     */     
/* 208 */     sendPacketToPlayer(createServerPacket(data), player);
/*     */   }
/*     */   
/*     */   public static void displayNewVillageGUI(EntityPlayer player, Point pos) {
/* 212 */     DataOutput data = getNewByteBufOutputStream();
/*     */     try
/*     */     {
/* 215 */       data.write(104);
/* 216 */       data.write(13);
/* 217 */       StreamReadWrite.writeNullablePoint(pos, data);
/*     */     }
/*     */     catch (IOException e) {
/* 220 */       MLN.printException(ServerSender.class + ": Error in displayNewVillageGUI", e);
/*     */     }
/*     */     
/* 223 */     sendPacketToPlayer(createServerPacket(data), player);
/*     */   }
/*     */   
/*     */   public static void displayPanel(EntityPlayer player, Point signPos)
/*     */   {
/* 228 */     TileEntityPanel panel = signPos.getPanel(player.field_70170_p);
/*     */     
/* 230 */     if (panel == null) {
/* 231 */       return;
/*     */     }
/*     */     
/* 234 */     MillWorld mw = Mill.getMillWorld(player.field_70170_p);
/*     */     
/* 236 */     if (panel.buildingPos != null) {
/* 237 */       Building building = mw.getBuilding(panel.buildingPos);
/* 238 */       if (building != null) {
/* 239 */         building.sendBuildingPacket(player, true);
/*     */       }
/*     */     }
/*     */     
/* 243 */     DataOutput data = getNewByteBufOutputStream();
/*     */     try
/*     */     {
/* 246 */       data.write(104);
/* 247 */       data.write(7);
/* 248 */       StreamReadWrite.writeNullablePoint(signPos, data);
/*     */     } catch (IOException e) {
/* 250 */       MLN.printException(ServerSender.class + ": Error in displayPanel", e);
/*     */     }
/*     */     
/* 253 */     sendPacketToPlayer(createServerPacket(data), player);
/*     */   }
/*     */   
/*     */   public static void displayQuestGUI(EntityPlayer player, MillVillager villager) {
/* 257 */     DataOutput data = getNewByteBufOutputStream();
/*     */     try
/*     */     {
/* 260 */       data.write(104);
/* 261 */       data.write(3);
/* 262 */       data.writeLong(villager.villager_id);
/*     */     } catch (IOException e) {
/* 264 */       MLN.printException(ServerSender.class + ": Error in displayQuestGUI", e);
/*     */     }
/* 266 */     sendPacketToPlayer(createServerPacket(data), player);
/*     */   }
/*     */   
/*     */   public static void displayVillageBookGUI(EntityPlayer player, Point p)
/*     */   {
/* 271 */     MillWorld mw = Mill.getMillWorld(player.field_70170_p);
/*     */     
/* 273 */     Building th = mw.getBuilding(p);
/*     */     
/* 275 */     if (th == null) {
/* 276 */       return;
/*     */     }
/*     */     
/* 279 */     th.sendBuildingPacket(player, true);
/*     */     
/* 281 */     DataOutput data = getNewByteBufOutputStream();
/*     */     try
/*     */     {
/* 284 */       data.write(104);
/* 285 */       data.write(5);
/* 286 */       StreamReadWrite.writeNullablePoint(p, data);
/*     */     } catch (IOException e) {
/* 288 */       MLN.printException(ServerSender.class + ": Error in displayQuestGUI", e);
/*     */     }
/*     */     
/* 291 */     sendPacketToPlayer(createServerPacket(data), player);
/*     */   }
/*     */   
/*     */ 
/*     */   public static void displayVillageChiefGUI(EntityPlayer player, MillVillager chief)
/*     */   {
/* 297 */     if (chief.getTownHall() == null) {
/* 298 */       MLN.error(chief, "Needed to send chief's TH but TH is null.");
/* 299 */       return;
/*     */     }
/*     */     
/* 302 */     chief.getTownHall().sendBuildingPacket(player, false);
/*     */     
/* 304 */     MillWorld mw = Mill.getMillWorld(player.field_70170_p);
/*     */     
/* 306 */     for (Point p : chief.getTownHall().getKnownVillages()) {
/* 307 */       Building b = mw.getBuilding(p);
/* 308 */       if (b != null) {
/* 309 */         b.sendBuildingPacket(player, false);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 314 */     DataOutput data = getNewByteBufOutputStream();
/*     */     try
/*     */     {
/* 317 */       data.write(104);
/* 318 */       data.write(4);
/* 319 */       data.writeLong(chief.villager_id);
/*     */     } catch (IOException e) {
/* 321 */       MLN.printException(ServerSender.class + ": Error in displayVillageChiefGUI", e);
/*     */     }
/*     */     
/* 324 */     sendPacketToPlayer(createServerPacket(data), player);
/*     */   }
/*     */   
/*     */   public static void displayVillageTradeGUI(EntityPlayer player, Building building)
/*     */   {
/* 329 */     building.computeShopGoods(player);
/*     */     
/* 331 */     building.sendShopPacket(player);
/*     */     
/* 333 */     building.sendBuildingPacket(player, true);
/*     */     
/* 335 */     if (!building.isTownhall) {
/* 336 */       building.getTownHall().sendBuildingPacket(player, false);
/*     */     }
/*     */     
/* 339 */     DataOutput data = getNewByteBufOutputStream();
/*     */     try
/*     */     {
/* 342 */       data.write(104);
/* 343 */       data.write(2);
/* 344 */       StreamReadWrite.writeNullablePoint(building.getPos(), data);
/*     */     } catch (IOException e) {
/* 346 */       MLN.printException(ServerSender.class + ": Error in displayVillageTradeGUI", e);
/*     */     }
/*     */     
/* 349 */     sendPacketToPlayer(createServerPacket(data), player);
/*     */     
/* 351 */     player.openGui(Mill.instance, 2, player.field_70170_p, building.getPos().getiX(), building.getPos().getiY(), building.getPos().getiZ());
/*     */   }
/*     */   
/*     */   public static DataOutput getNewByteBufOutputStream() {
/* 355 */     DataOutput data = new ByteBufOutputStream(Unpooled.buffer());
/* 356 */     return data;
/*     */   }
/*     */   
/*     */   public static void sendAnimalBreeding(EntityAnimal animal) {
/* 360 */     DataOutput data = getNewByteBufOutputStream();
/*     */     
/* 362 */     Point pos = new Point(animal);
/*     */     try
/*     */     {
/* 365 */       data.write(107);
/*     */       
/* 367 */       StreamReadWrite.writeNullablePoint(pos, data);
/* 368 */       data.writeInt(animal.func_145782_y());
/*     */     }
/*     */     catch (IOException e) {
/* 371 */       MLN.printException(animal + ": Error in sendAnimalBreeding", e);
/*     */     }
/*     */     
/* 374 */     S3FPacketCustomPayload packet = createServerPacket(data);
/*     */     
/* 376 */     sendPacketToPlayersInRange(packet, pos, 50);
/*     */   }
/*     */   
/*     */   public static void sendChat(EntityPlayer player, EnumChatFormatting colour, String s) {
/* 380 */     ChatComponentText chat = new ChatComponentText(s);
/* 381 */     chat.func_150256_b().func_150238_a(colour);
/* 382 */     player.func_145747_a(chat);
/*     */   }
/*     */   
/*     */   public static void sendLockedChestUpdatePacket(TileEntityMillChest chest, EntityPlayer player) {
/* 386 */     DataOutput data = getNewByteBufOutputStream();
/*     */     
/* 388 */     Point pos = new Point(chest.field_145851_c, chest.field_145848_d, chest.field_145849_e);
/*     */     
/*     */     try
/*     */     {
/* 392 */       data.write(5);
/*     */       
/* 394 */       StreamReadWrite.writeNullablePoint(pos, data);
/* 395 */       StreamReadWrite.writeNullablePoint(chest.buildingPos, data);
/* 396 */       data.writeBoolean(MLN.DEV);
/*     */       
/* 398 */       data.writeByte(chest.func_70302_i_());
/* 399 */       for (int i = 0; i < chest.func_70302_i_(); i++) {
/* 400 */         StreamReadWrite.writeNullableItemStack(chest.func_70301_a(i), data);
/*     */       }
/*     */     }
/*     */     catch (IOException e) {
/* 404 */       MLN.printException(chest + ": Error in sendUpdatePacket", e);
/*     */     }
/*     */     
/* 407 */     sendPacketToPlayer(createServerPacket(data), player);
/*     */   }
/*     */   
/*     */   public static void sendPacketToPlayer(Packet packet, EntityPlayer player) {
/* 411 */     ((EntityPlayerMP)player).field_71135_a.func_147359_a(packet);
/*     */   }
/*     */   
/*     */   public static void sendPacketToPlayersInRange(DataOutput data, Point p, int range) {
/* 415 */     sendPacketToPlayersInRange(createServerPacket(data), p, range);
/*     */   }
/*     */   
/*     */   public static void sendPacketToPlayersInRange(Packet packet, Point p, int range) {
/* 419 */     MinecraftServer server = MinecraftServer.func_71276_C();
/*     */     
/* 421 */     if (server == null) {
/* 422 */       MLN.error(null, "Wanted to send a packet in sendPacketToPlayersInRange but server is null.");
/* 423 */       return;
/*     */     }
/*     */     
/* 426 */     ServerConfigurationManager config = server.func_71203_ab();
/*     */     
/* 428 */     config.func_148541_a(p.x, p.y, p.z, range, 0, packet);
/*     */   }
/*     */   
/*     */   public static void sendTranslatedSentence(EntityPlayer player, char colour, String code, String... values)
/*     */   {
/* 433 */     if (player == null) {
/* 434 */       return;
/*     */     }
/*     */     
/* 437 */     if (!(player instanceof EntityPlayerMP)) {
/* 438 */       return;
/*     */     }
/*     */     
/* 441 */     DataOutput data = getNewByteBufOutputStream();
/*     */     try
/*     */     {
/* 444 */       data.write(100);
/* 445 */       data.writeChar(colour);
/* 446 */       data.writeUTF(code);
/*     */       
/* 448 */       data.write(values.length);
/* 449 */       for (String value : values) {
/* 450 */         StreamReadWrite.writeNullableString(value, data);
/*     */       }
/*     */     }
/*     */     catch (IOException e) {
/* 454 */       MLN.printException(ServerSender.class + ": Error in sendTranslatedSentence", e);
/*     */     }
/*     */     
/* 457 */     sendPacketToPlayer(createServerPacket(data), player);
/*     */   }
/*     */   
/*     */   public static void sendTranslatedSentenceInRange(World world, Point p, int range, char colour, String key, String... values) {
/* 461 */     for (Object oplayer : world.field_73010_i) {
/* 462 */       EntityPlayer player = (EntityPlayer)oplayer;
/* 463 */       if (p.distanceTo(player) < range) {
/* 464 */         sendTranslatedSentence(player, colour, key, values);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static void sendVillagerSentence(EntityPlayer player, MillVillager v)
/*     */   {
/* 471 */     if (player == null) {
/* 472 */       return;
/*     */     }
/*     */     
/* 475 */     if (!(player instanceof EntityPlayerMP)) {
/* 476 */       return;
/*     */     }
/*     */     
/* 479 */     DataOutput data = getNewByteBufOutputStream();
/*     */     try
/*     */     {
/* 482 */       data.write(108);
/* 483 */       data.writeLong(v.villager_id);
/*     */     }
/*     */     catch (IOException e) {
/* 486 */       MLN.printException(ServerSender.class + ": Error in sendVillagerSentence", e);
/*     */     }
/*     */     
/* 489 */     sendPacketToPlayer(createServerPacket(data), player);
/*     */   }
/*     */   
/*     */   public static void sendVillageSentenceInRange(World world, Point p, int range, MillVillager v) {
/* 493 */     for (Object oplayer : world.field_73010_i) {
/* 494 */       EntityPlayer player = (EntityPlayer)oplayer;
/* 495 */       if (p.distanceTo(player) < range) {
/* 496 */         sendVillagerSentence(player, v);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static void updatePanel(MillWorld mw, Point p, String[][] lines, int type, Point buildingPos, long villager_id)
/*     */   {
/* 503 */     if (lines == null) {
/* 504 */       return;
/*     */     }
/*     */     
/* 507 */     TileEntityPanel panel = p.getPanel(mw.world);
/*     */     
/* 509 */     if (panel != null) {
/* 510 */       panel.panelType = type;
/* 511 */       panel.buildingPos = buildingPos;
/* 512 */       panel.villager_id = villager_id;
/*     */     }
/*     */     
/* 515 */     String key = "" + type + ";" + buildingPos + ";" + villager_id + ";";
/*     */     
/* 517 */     List<EntityPlayer> receivers = new ArrayList();
/*     */     
/* 519 */     for (int i = 0; i < lines.length; i++) {
/* 520 */       for (int j = 0; j < lines[i].length; j++) {
/* 521 */         key = key + ";" + lines[i][j];
/*     */       }
/*     */     }
/*     */     
/* 525 */     int keyHash = key.hashCode();
/*     */     
/* 527 */     for (EntityPlayer player : MillCommonUtilities.getServerPlayers(mw.world)) {
/* 528 */       if (p.distanceToSquared(player) < 256.0D) {
/* 529 */         UserProfile profile = MillCommonUtilities.getServerProfile(mw.world, player.getDisplayName());
/*     */         
/* 531 */         if ((!profile.panelsSent.containsKey(p)) || (!((Integer)profile.panelsSent.get(p)).equals(Integer.valueOf(keyHash)))) {
/* 532 */           receivers.add(player);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 537 */     if (receivers.size() == 0) {
/* 538 */       return;
/*     */     }
/*     */     
/* 541 */     DataOutput data = getNewByteBufOutputStream();
/*     */     try
/*     */     {
/* 544 */       data.write(106);
/* 545 */       StreamReadWrite.writeNullablePoint(p, data);
/* 546 */       data.write(type);
/* 547 */       StreamReadWrite.writeNullablePoint(buildingPos, data);
/* 548 */       data.writeLong(villager_id);
/* 549 */       StreamReadWrite.writeStringStringArray(lines, data);
/*     */     } catch (IOException e) {
/* 551 */       MLN.printException(ServerSender.class + ": Error in updatePanel", e);
/*     */     }
/*     */     
/* 554 */     S3FPacketCustomPayload packet = createServerPacket(data);
/*     */     
/* 556 */     for (EntityPlayer player : receivers) {
/* 557 */       sendPacketToPlayer(packet, player);
/* 558 */       UserProfile profile = MillCommonUtilities.getServerProfile(player.field_70170_p, player.getDisplayName());
/* 559 */       profile.panelsSent.put(p, Integer.valueOf(keyHash));
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\network\ServerSender.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */