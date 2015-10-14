/*     */ package org.millenaire.client.network;
/*     */ 
/*     */ import io.netty.buffer.ByteBufOutputStream;
/*     */ import io.netty.buffer.Unpooled;
/*     */ import java.io.IOException;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.entity.EntityClientPlayerMP;
/*     */ import net.minecraft.client.network.NetHandlerPlayClient;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.play.client.C17PacketCustomPayload;
/*     */ import org.millenaire.common.Culture;
/*     */ import org.millenaire.common.GuiActions;
/*     */ import org.millenaire.common.MLN;
/*     */ import org.millenaire.common.MillVillager;
/*     */ import org.millenaire.common.MillWorld;
/*     */ import org.millenaire.common.Point;
/*     */ import org.millenaire.common.building.Building;
/*     */ import org.millenaire.common.building.BuildingLocation;
/*     */ import org.millenaire.common.building.BuildingProject;
/*     */ import org.millenaire.common.network.StreamReadWrite;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ClientSender
/*     */ {
/*     */   public static void activateMillChest(EntityPlayer player, Point pos)
/*     */   {
/*  34 */     ByteBufOutputStream data = getNewByteBufOutputStream();
/*     */     try
/*     */     {
/*  37 */       data.write(200);
/*  38 */       data.write(81);
/*  39 */       StreamReadWrite.writeNullablePoint(pos, data);
/*     */     } catch (IOException e) {
/*  41 */       MLN.printException("Error in activateMillChest", e);
/*     */     }
/*     */     
/*  44 */     createAndSendServerPacket(data);
/*     */   }
/*     */   
/*     */   public static void controlledBuildingsForgetBuilding(EntityPlayer player, Building townHall, BuildingProject project)
/*     */   {
/*  49 */     ByteBufOutputStream data = getNewByteBufOutputStream();
/*     */     try
/*     */     {
/*  52 */       data.write(200);
/*  53 */       data.write(71);
/*  54 */       StreamReadWrite.writeNullablePoint(townHall.getPos(), data);
/*  55 */       data.writeUTF(project.key);
/*  56 */       StreamReadWrite.writeNullablePoint(project.location.pos, data);
/*     */     } catch (IOException e) {
/*  58 */       MLN.printException("Error in controlledBuildingsToggleUpgrades", e);
/*     */     }
/*     */     
/*  61 */     createAndSendServerPacket(data);
/*     */     
/*  63 */     GuiActions.controlledBuildingsForgetBuilding(player, townHall, project);
/*     */   }
/*     */   
/*     */   public static void controlledBuildingsToggleUpgrades(EntityPlayer player, Building townHall, BuildingProject project, boolean allow)
/*     */   {
/*  68 */     ByteBufOutputStream data = getNewByteBufOutputStream();
/*     */     try
/*     */     {
/*  71 */       data.write(200);
/*  72 */       data.write(70);
/*  73 */       StreamReadWrite.writeNullablePoint(townHall.getPos(), data);
/*  74 */       data.writeUTF(project.key);
/*  75 */       StreamReadWrite.writeNullablePoint(project.location.pos, data);
/*  76 */       data.writeBoolean(allow);
/*     */     } catch (IOException e) {
/*  78 */       MLN.printException("Error in controlledBuildingsToggleUpgrades", e);
/*     */     }
/*     */     
/*  81 */     createAndSendServerPacket(data);
/*     */     
/*  83 */     GuiActions.controlledBuildingsToggleUpgrades(player, townHall, project, allow);
/*     */   }
/*     */   
/*     */   public static void controlledMilitaryCancelRaid(EntityPlayer player, Building th) {
/*  87 */     ByteBufOutputStream data = getNewByteBufOutputStream();
/*     */     try
/*     */     {
/*  90 */       data.write(200);
/*  91 */       data.write(92);
/*  92 */       StreamReadWrite.writeNullablePoint(th.getPos(), data);
/*     */     } catch (IOException e) {
/*  94 */       MLN.printException("Error in controlledMilitaryCancelRaid", e);
/*     */     }
/*     */     
/*  97 */     createAndSendServerPacket(data);
/*     */     
/*     */ 
/* 100 */     GuiActions.controlledMilitaryCancelRaid(player, th);
/*     */   }
/*     */   
/*     */   public static void controlledMilitaryDiplomacy(EntityPlayer player, Building th, Point target, int amount) {
/* 104 */     ByteBufOutputStream data = getNewByteBufOutputStream();
/*     */     try
/*     */     {
/* 107 */       data.write(200);
/* 108 */       data.write(90);
/* 109 */       StreamReadWrite.writeNullablePoint(th.getPos(), data);
/* 110 */       StreamReadWrite.writeNullablePoint(target, data);
/* 111 */       data.writeInt(amount);
/*     */     }
/*     */     catch (IOException e) {
/* 114 */       MLN.printException("Error in controlledMilitaryDiplomacy", e);
/*     */     }
/*     */     
/* 117 */     createAndSendServerPacket(data);
/*     */     
/*     */ 
/* 120 */     GuiActions.controlledMilitaryDiplomacy(player, th, target, amount);
/*     */   }
/*     */   
/*     */   public static void controlledMilitaryPlanRaid(EntityPlayer player, Building th, Point target) {
/* 124 */     ByteBufOutputStream data = getNewByteBufOutputStream();
/*     */     try
/*     */     {
/* 127 */       data.write(200);
/* 128 */       data.write(91);
/* 129 */       StreamReadWrite.writeNullablePoint(th.getPos(), data);
/* 130 */       StreamReadWrite.writeNullablePoint(target, data);
/*     */     } catch (IOException e) {
/* 132 */       MLN.printException("Error in controlledMilitaryStartRaid", e);
/*     */     }
/*     */     
/* 135 */     createAndSendServerPacket(data);
/*     */     
/*     */ 
/* 138 */     GuiActions.controlledMilitaryPlanRaid(player, th, th.mw.getBuilding(target));
/*     */   }
/*     */   
/*     */   private static void createAndSendServerPacket(ByteBufOutputStream bytes) {
/* 142 */     sendPacketToServer(createServerPacket(bytes));
/*     */   }
/*     */   
/*     */   public static C17PacketCustomPayload createServerPacket(ByteBufOutputStream data) {
/* 146 */     return new C17PacketCustomPayload("millenaire", data.buffer());
/*     */   }
/*     */   
/*     */   public static void devCommand(int devcommand) {
/* 150 */     ByteBufOutputStream data = getNewByteBufOutputStream();
/*     */     try
/*     */     {
/* 153 */       data.write(206);
/* 154 */       data.write(devcommand);
/*     */     } catch (IOException e) {
/* 156 */       MLN.printException("Error in devCommand", e);
/*     */     }
/*     */     
/* 159 */     createAndSendServerPacket(data);
/*     */   }
/*     */   
/*     */   public static void displayVillageList(boolean loneBuildings) {
/* 163 */     ByteBufOutputStream data = getNewByteBufOutputStream();
/*     */     try
/*     */     {
/* 166 */       data.write(201);
/* 167 */       data.writeBoolean(loneBuildings);
/*     */     } catch (IOException e) {
/* 169 */       MLN.printException("Error in displayVillageList", e);
/*     */     }
/*     */     
/* 172 */     createAndSendServerPacket(data);
/*     */   }
/*     */   
/*     */   public static ByteBufOutputStream getNewByteBufOutputStream() {
/* 176 */     return new ByteBufOutputStream(Unpooled.buffer());
/*     */   }
/*     */   
/*     */   public static void hireExtend(EntityPlayer player, MillVillager villager) {
/* 180 */     ByteBufOutputStream data = getNewByteBufOutputStream();
/*     */     try
/*     */     {
/* 183 */       data.write(200);
/* 184 */       data.write(31);
/* 185 */       data.writeLong(villager.villager_id);
/*     */     } catch (IOException e) {
/* 187 */       MLN.printException("Error in hireExtend", e);
/*     */     }
/*     */     
/* 190 */     createAndSendServerPacket(data);
/*     */     
/* 192 */     GuiActions.hireExtend(player, villager);
/*     */   }
/*     */   
/*     */   public static void hireHire(EntityPlayer player, MillVillager villager) {
/* 196 */     ByteBufOutputStream data = getNewByteBufOutputStream();
/*     */     try
/*     */     {
/* 199 */       data.write(200);
/* 200 */       data.write(30);
/* 201 */       data.writeLong(villager.villager_id);
/*     */     } catch (IOException e) {
/* 203 */       MLN.printException("Error in hireHire", e);
/*     */     }
/*     */     
/* 206 */     createAndSendServerPacket(data);
/*     */     
/* 208 */     GuiActions.hireHire(player, villager);
/*     */   }
/*     */   
/*     */   public static void hireRelease(EntityPlayer player, MillVillager villager) {
/* 212 */     ByteBufOutputStream data = getNewByteBufOutputStream();
/*     */     try
/*     */     {
/* 215 */       data.write(200);
/* 216 */       data.write(32);
/* 217 */       data.writeLong(villager.villager_id);
/*     */     } catch (IOException e) {
/* 219 */       MLN.printException("Error in hireRelease", e);
/*     */     }
/*     */     
/* 222 */     createAndSendServerPacket(data);
/*     */     
/* 224 */     GuiActions.hireRelease(player, villager);
/*     */   }
/*     */   
/*     */   public static void importBuilding(EntityPlayer player, Point pos) {
/* 228 */     ByteBufOutputStream data = getNewByteBufOutputStream();
/*     */     try
/*     */     {
/* 231 */       data.write(200);
/* 232 */       data.write(82);
/* 233 */       StreamReadWrite.writeNullablePoint(pos, data);
/*     */     } catch (IOException e) {
/* 235 */       MLN.printException("Error in importBuilding", e);
/*     */     }
/*     */     
/* 238 */     createAndSendServerPacket(data);
/*     */   }
/*     */   
/*     */   public static void negationWand(EntityPlayer player, Building townHall) {
/* 242 */     ByteBufOutputStream data = getNewByteBufOutputStream();
/*     */     try
/*     */     {
/* 245 */       data.write(200);
/* 246 */       data.write(40);
/* 247 */       StreamReadWrite.writeNullablePoint(townHall.getPos(), data);
/*     */     } catch (IOException e) {
/* 249 */       MLN.printException("Error in negationWand", e);
/*     */     }
/*     */     
/* 252 */     createAndSendServerPacket(data);
/*     */   }
/*     */   
/*     */   public static void newBuilding(EntityPlayer player, Building townHall, Point pos, String planKey) {
/* 256 */     ByteBufOutputStream data = getNewByteBufOutputStream();
/*     */     try
/*     */     {
/* 259 */       data.write(200);
/* 260 */       data.write(50);
/* 261 */       StreamReadWrite.writeNullablePoint(townHall.getPos(), data);
/* 262 */       StreamReadWrite.writeNullablePoint(pos, data);
/* 263 */       data.writeUTF(planKey);
/*     */     } catch (IOException e) {
/* 265 */       MLN.printException("Error in newBuilding", e);
/*     */     }
/*     */     
/* 268 */     createAndSendServerPacket(data);
/*     */   }
/*     */   
/*     */   public static void newCustomBuilding(EntityPlayer player, Building townHall, Point pos, String planKey) {
/* 272 */     ByteBufOutputStream data = getNewByteBufOutputStream();
/*     */     try
/*     */     {
/* 275 */       data.write(200);
/* 276 */       data.write(51);
/* 277 */       StreamReadWrite.writeNullablePoint(townHall.getPos(), data);
/* 278 */       StreamReadWrite.writeNullablePoint(pos, data);
/* 279 */       data.writeUTF(planKey);
/*     */     } catch (IOException e) {
/* 281 */       MLN.printException("Error in newCustomBuilding", e);
/*     */     }
/*     */     
/* 284 */     createAndSendServerPacket(data);
/*     */   }
/*     */   
/*     */   public static void newVillageCreation(EntityPlayer player, Point pos, String cultureKey, String villageTypeKey) {
/* 288 */     ByteBufOutputStream data = getNewByteBufOutputStream();
/*     */     try
/*     */     {
/* 291 */       data.write(200);
/* 292 */       data.write(20);
/* 293 */       data.writeUTF(cultureKey);
/* 294 */       data.writeUTF(villageTypeKey);
/* 295 */       StreamReadWrite.writeNullablePoint(pos, data);
/*     */     } catch (IOException e) {
/* 297 */       MLN.printException("Error in newVillageCreation", e);
/*     */     }
/*     */     
/* 300 */     createAndSendServerPacket(data);
/*     */   }
/*     */   
/*     */   public static void pujasChangeEnchantment(EntityPlayer player, Building temple, int enchantmentId) {
/* 304 */     ByteBufOutputStream data = getNewByteBufOutputStream();
/*     */     try
/*     */     {
/* 307 */       data.write(200);
/* 308 */       data.write(60);
/* 309 */       StreamReadWrite.writeNullablePoint(temple.getPos(), data);
/* 310 */       data.writeShort(enchantmentId);
/*     */     } catch (IOException e) {
/* 312 */       MLN.printException("Error in pujasChangeEnchantment", e);
/*     */     }
/*     */     
/* 315 */     createAndSendServerPacket(data);
/*     */   }
/*     */   
/*     */   public static void questCompleteStep(EntityPlayer player, MillVillager villager) {
/* 319 */     ByteBufOutputStream data = getNewByteBufOutputStream();
/*     */     try
/*     */     {
/* 322 */       data.write(200);
/* 323 */       data.write(10);
/* 324 */       data.writeLong(villager.villager_id);
/*     */     } catch (IOException e) {
/* 326 */       MLN.printException("Error in questCompleteStep", e);
/*     */     }
/*     */     
/* 329 */     createAndSendServerPacket(data);
/*     */   }
/*     */   
/*     */   public static void questRefuse(EntityPlayer player, MillVillager villager) {
/* 333 */     ByteBufOutputStream data = getNewByteBufOutputStream();
/*     */     try
/*     */     {
/* 336 */       data.write(200);
/* 337 */       data.write(11);
/* 338 */       data.writeLong(villager.villager_id);
/*     */     } catch (IOException e) {
/* 340 */       MLN.printException("Error in questRefuse", e);
/*     */     }
/*     */     
/* 343 */     createAndSendServerPacket(data);
/*     */   }
/*     */   
/*     */   public static void requestMapInfo(Building townHall) {
/* 347 */     ByteBufOutputStream data = getNewByteBufOutputStream();
/*     */     try
/*     */     {
/* 350 */       data.write(203);
/* 351 */       StreamReadWrite.writeNullablePoint(townHall.getPos(), data);
/*     */     } catch (IOException e) {
/* 353 */       MLN.printException(townHall + ": Error in sendUpdatePacket", e);
/*     */     }
/*     */     
/* 356 */     createAndSendServerPacket(data);
/*     */   }
/*     */   
/*     */   public static void sendAvailableContent()
/*     */   {
/* 361 */     ByteBufOutputStream data = getNewByteBufOutputStream();
/*     */     try
/*     */     {
/* 364 */       data.write(205);
/*     */       
/* 366 */       data.writeUTF(MLN.effective_language);
/* 367 */       data.writeUTF(MLN.fallback_language);
/*     */       
/* 369 */       data.writeShort(Culture.ListCultures.size());
/*     */       
/* 371 */       for (Culture culture : Culture.ListCultures) {
/* 372 */         culture.writeCultureAvailableContentPacket(data);
/*     */       }
/*     */     }
/*     */     catch (IOException e) {
/* 376 */       MLN.printException("Error in displayVillageList", e);
/*     */     }
/*     */     
/* 379 */     createAndSendServerPacket(data);
/*     */   }
/*     */   
/*     */   public static void sendPacketToServer(Packet packet) {
/* 383 */     Minecraft.func_71410_x().field_71439_g.field_71174_a.func_147297_a(packet);
/*     */   }
/*     */   
/*     */   public static void sendVersionInfo() {
/* 387 */     ByteBufOutputStream data = getNewByteBufOutputStream();
/*     */     try
/*     */     {
/* 390 */       data.write(202);
/* 391 */       data.writeUTF("6.0.0");
/*     */     } catch (IOException e) {
/* 393 */       MLN.printException("Error in sendVersionInfo", e);
/*     */     }
/*     */     
/* 396 */     createAndSendServerPacket(data);
/*     */   }
/*     */   
/*     */   public static void summoningWandUse(EntityPlayer player, Point pos)
/*     */   {
/* 401 */     ByteBufOutputStream data = getNewByteBufOutputStream();
/*     */     try
/*     */     {
/* 404 */       data.write(200);
/* 405 */       data.write(80);
/* 406 */       StreamReadWrite.writeNullablePoint(pos, data);
/*     */     } catch (IOException e) {
/* 408 */       MLN.printException("Error in summoningWandUse", e);
/*     */     }
/*     */     
/* 411 */     createAndSendServerPacket(data);
/*     */   }
/*     */   
/*     */   public static void updateCustomBuilding(EntityPlayer player, Building building) {
/* 415 */     ByteBufOutputStream data = getNewByteBufOutputStream();
/*     */     try
/*     */     {
/* 418 */       data.write(200);
/* 419 */       data.write(52);
/* 420 */       StreamReadWrite.writeNullablePoint(building.getPos(), data);
/*     */     } catch (IOException e) {
/* 422 */       MLN.printException("Error in updateCustomBuilding", e);
/*     */     }
/*     */     
/* 425 */     createAndSendServerPacket(data);
/*     */   }
/*     */   
/*     */   public static void villageChiefPerformBuilding(EntityPlayer player, MillVillager chief, String planKey)
/*     */   {
/* 430 */     ByteBufOutputStream data = getNewByteBufOutputStream();
/*     */     try
/*     */     {
/* 433 */       data.write(200);
/* 434 */       data.write(1);
/* 435 */       data.writeLong(chief.villager_id);
/* 436 */       data.writeUTF(planKey);
/*     */     } catch (IOException e) {
/* 438 */       MLN.printException("Error in villageChiefPerformBuilding", e);
/*     */     }
/*     */     
/* 441 */     createAndSendServerPacket(data);
/*     */   }
/*     */   
/*     */   public static void villageChiefPerformCrop(EntityPlayer player, MillVillager chief, String value) {
/* 445 */     ByteBufOutputStream data = getNewByteBufOutputStream();
/*     */     try
/*     */     {
/* 448 */       data.write(200);
/* 449 */       data.write(2);
/* 450 */       data.writeLong(chief.villager_id);
/* 451 */       data.writeUTF(value);
/*     */     } catch (IOException e) {
/* 453 */       MLN.printException("Error in villageChiefPerformCrop", e);
/*     */     }
/*     */     
/* 456 */     createAndSendServerPacket(data);
/*     */   }
/*     */   
/*     */   public static void villageChiefPerformCultureControl(EntityPlayer player, MillVillager chief) {
/* 460 */     ByteBufOutputStream data = getNewByteBufOutputStream();
/*     */     try
/*     */     {
/* 463 */       data.write(200);
/* 464 */       data.write(3);
/* 465 */       data.writeLong(chief.villager_id);
/*     */     } catch (IOException e) {
/* 467 */       MLN.printException("Error in villageChiefPerformCultureControl", e);
/*     */     }
/*     */     
/* 470 */     createAndSendServerPacket(data);
/*     */   }
/*     */   
/*     */   public static void villageChiefPerformDiplomacy(EntityPlayer player, MillVillager chief, Point village, boolean praise) {
/* 474 */     ByteBufOutputStream data = getNewByteBufOutputStream();
/*     */     try
/*     */     {
/* 477 */       data.write(200);
/* 478 */       data.write(4);
/* 479 */       data.writeLong(chief.villager_id);
/* 480 */       StreamReadWrite.writeNullablePoint(village, data);
/* 481 */       data.writeBoolean(praise);
/*     */     } catch (IOException e) {
/* 483 */       MLN.printException("Error in villageChiefPerformDiplomacy", e);
/*     */     }
/*     */     
/* 486 */     createAndSendServerPacket(data);
/*     */     
/*     */ 
/* 489 */     GuiActions.villageChiefPerformDiplomacy(player, chief, village, praise);
/*     */   }
/*     */   
/*     */   public static void villageChiefPerformVillageScroll(EntityPlayer player, MillVillager chief) {
/* 493 */     ByteBufOutputStream data = getNewByteBufOutputStream();
/*     */     try
/*     */     {
/* 496 */       data.write(200);
/* 497 */       data.write(5);
/* 498 */       data.writeLong(chief.villager_id);
/*     */     } catch (IOException e) {
/* 500 */       MLN.printException("Error in villageChiefPerformVillageScroll", e);
/*     */     }
/*     */     
/* 503 */     createAndSendServerPacket(data);
/*     */   }
/*     */   
/*     */   public static void villagerInteractSpecial(EntityPlayer player, MillVillager villager) {
/* 507 */     ByteBufOutputStream data = getNewByteBufOutputStream();
/*     */     try
/*     */     {
/* 510 */       data.write(204);
/* 511 */       data.writeLong(villager.villager_id);
/*     */     } catch (IOException e) {
/* 513 */       MLN.printException("Error in villagerInteractSpecial", e);
/*     */     }
/*     */     
/* 516 */     createAndSendServerPacket(data);
/*     */   }
/*     */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\client\network\ClientSender.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */