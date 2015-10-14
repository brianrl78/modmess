/*     */ package org.millenaire.client.gui;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.gui.FontRenderer;
/*     */ import net.minecraft.client.gui.GuiButton;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import net.minecraft.world.ChunkCoordIntPair;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.chunk.Chunk;
/*     */ import net.minecraft.world.chunk.IChunkProvider;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ import org.millenaire.common.MLN;
/*     */ import org.millenaire.common.MillMapInfo;
/*     */ import org.millenaire.common.MillVillager;
/*     */ import org.millenaire.common.MillWorld;
/*     */ import org.millenaire.common.MillWorldInfo;
/*     */ import org.millenaire.common.Point;
/*     */ import org.millenaire.common.VillageType;
/*     */ import org.millenaire.common.VillagerRecord;
/*     */ import org.millenaire.common.building.Building;
/*     */ import org.millenaire.common.building.BuildingLocation;
/*     */ import org.millenaire.common.forge.Mill;
/*     */ import org.millenaire.common.pathing.AStarPathing;
/*     */ 
/*     */ public class GuiPanelParchment extends GuiText
/*     */ {
/*     */   public static final int VILLAGE_MAP = 1;
/*     */   public static final int CHUNK_MAP = 2;
/*  31 */   private boolean isParchment = false;
/*  32 */   private int mapType = 0;
/*  33 */   private Building townHall = null;
/*     */   
/*     */   private final EntityPlayer player;
/*     */   
/*     */   private final List<List<String>> fullText;
/*     */   private final List<List<GuiText.Line>> fullTextLines;
/*  39 */   ResourceLocation backgroundParchment = new ResourceLocation("millenaire", "textures/gui/ML_parchment.png");
/*     */   
/*  41 */   ResourceLocation backgroundPanel = new ResourceLocation("millenaire", "textures/gui/ML_panel.png");
/*     */   
/*     */   public static final int chunkMapSizeInBlocks = 1280;
/*     */   
/*     */ 
/*     */   public GuiPanelParchment(EntityPlayer player, Building townHall, List<List<GuiText.Line>> fullTextLines, int mapType, boolean isParchment)
/*     */   {
/*  48 */     this.mapType = mapType;
/*     */     
/*  50 */     this.townHall = townHall;
/*  51 */     this.isParchment = isParchment;
/*  52 */     this.player = player;
/*  53 */     this.fullTextLines = fullTextLines;
/*  54 */     this.fullText = null;
/*     */   }
/*     */   
/*     */ 
/*     */   public GuiPanelParchment(EntityPlayer player, List<List<String>> fullText, Building townHall, int mapType, boolean isParchment)
/*     */   {
/*  60 */     this.mapType = mapType;
/*     */     
/*  62 */     this.townHall = townHall;
/*  63 */     this.isParchment = isParchment;
/*  64 */     this.player = player;
/*  65 */     this.fullText = fullText;
/*  66 */     this.fullTextLines = null;
/*     */   }
/*     */   
/*     */ 
/*     */   protected void func_146284_a(GuiButton guibutton)
/*     */   {
/*  72 */     if ((guibutton instanceof GuiText.MillGuiButton))
/*     */     {
/*  74 */       GuiText.MillGuiButton gb = (GuiText.MillGuiButton)guibutton;
/*     */       
/*  76 */       if (gb.field_146127_k == 2000) {
/*  77 */         DisplayActions.displayHelpGUI();
/*  78 */       } else if (gb.field_146127_k == 3000) {
/*  79 */         DisplayActions.displayChunkGUI(this.player, this.player.field_70170_p);
/*  80 */       } else if (gb.field_146127_k == 4000) {
/*  81 */         DisplayActions.displayConfigGUI();
/*     */       }
/*     */     }
/*     */     
/*  85 */     super.func_146284_a(guibutton);
/*     */   }
/*     */   
/*     */ 
/*     */   protected void customDrawBackground(int i, int j, float f) {}
/*     */   
/*     */ 
/*     */   public void customDrawScreen(int i, int j, float f)
/*     */   {
/*     */     try
/*     */     {
/*  96 */       if ((this.mapType == 1) && (this.pageNum == 0) && (this.townHall != null) && (this.townHall.mapInfo != null)) {
/*  97 */         drawVillageMap(i, j);
/*  98 */       } else if ((this.mapType == 2) && (this.pageNum == 0)) {
/*  99 */         drawChunkMap(i, j);
/*     */       }
/*     */     } catch (Exception e) {
/* 102 */       MLN.printException("Exception while rendering map: ", e);
/*     */     }
/*     */   }
/*     */   
/*     */   private void drawChunkMap(int i, int j)
/*     */   {
/* 108 */     if (Mill.serverWorlds.isEmpty()) {
/* 109 */       return;
/*     */     }
/*     */     
/* 112 */     int windowXstart = (this.field_146294_l - getXSize()) / 2;
/* 113 */     int windowYstart = (this.field_146295_m - getYSize()) / 2;
/*     */     
/* 115 */     World world = ((MillWorld)Mill.serverWorlds.get(0)).world;
/* 116 */     MillWorld mw = (MillWorld)Mill.serverWorlds.get(0);
/*     */     
/* 118 */     GL11.glDisable(2896);
/* 119 */     GL11.glDisable(2929);
/*     */     
/* 121 */     int startX = (getXSize() - 160) / 2;
/* 122 */     int startY = (getYSize() - 160) / 2;
/*     */     
/* 124 */     int posXstart = this.player.field_70176_ah * 16 - 640;
/* 125 */     int posZstart = this.player.field_70164_aj * 16 - 640;
/*     */     
/* 127 */     int mouseX = (i - startX - windowXstart) / 2 * 16 + posXstart;
/* 128 */     int mouseZ = (j - startY - windowYstart) / 2 * 16 + posZstart;
/*     */     
/* 130 */     func_73733_a(startX - 2, startY - 2, startX + 160 + 2, startY + 160 + 2, 536870912, 536870912);
/*     */     
/* 132 */     ArrayList<String> labels = new ArrayList();
/*     */     
/* 134 */     for (int x = posXstart; x < posXstart + 1280; x += 16) {
/* 135 */       for (int z = posZstart; z < posZstart + 1280; z += 16)
/*     */       {
/* 137 */         int colour = 0;
/* 138 */         if (!world.func_72863_F().func_73149_a(x / 16, z / 16)) {
/* 139 */           colour = 1074860305;
/*     */         } else {
/* 141 */           Chunk chunk = world.func_72863_F().func_73154_d(x / 16, z / 16);
/* 142 */           if (chunk.field_76636_d) {
/* 143 */             colour = -1073676544;
/*     */           } else {
/* 145 */             colour = -1057030144;
/*     */           }
/* 147 */           drawPixel(startX + (x - posXstart) / 8, startY + (z - posZstart) / 8, colour);
/*     */           
/* 149 */           if ((mouseX == x) && (mouseZ == z)) {
/* 150 */             labels.add(MLN.string("chunk.chunkcoords", new String[] { "" + x / 16 + "/" + z / 16 }));
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 158 */     ArrayList<Building> buildings = new ArrayList(mw.allBuildings());
/*     */     
/* 160 */     for (Building b : buildings) {
/* 161 */       if ((b.isTownhall) && (b.winfo != null) && (b.villageType != null)) {
/* 162 */         for (int x = b.winfo.mapStartX; x < b.winfo.mapStartX + b.winfo.length; x += 16) {
/* 163 */           for (int z = b.winfo.mapStartZ; z < b.winfo.mapStartZ + b.winfo.width; z += 16) {
/* 164 */             if ((x >= posXstart) && (x <= posXstart + 1280) && (z >= posZstart) && (z <= posZstart + 1280)) {
/*     */               int colour;
/*     */               int colour;
/* 167 */               if (b.villageType.lonebuilding) {
/* 168 */                 colour = -258408295;
/*     */               } else {
/* 170 */                 colour = -268435201;
/*     */               }
/*     */               
/* 173 */               drawPixel(startX + (x - posXstart) / 8 + 1, startY + (z - posZstart) / 8 + 1, colour);
/*     */               
/* 175 */               if ((mouseX == x) && (mouseZ == z)) {
/* 176 */                 labels.add(MLN.string("chunk.village", new String[] { b.getVillageQualifiedName() }));
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 184 */     boolean labelForced = false;
/*     */     
/* 186 */     for (ChunkCoordIntPair cc : net.minecraftforge.common.ForgeChunkManager.getPersistentChunksFor(world).keys()) {
/* 187 */       if ((cc.field_77276_a * 16 >= posXstart) && (cc.field_77276_a * 16 <= posXstart + 1280) && (cc.field_77275_b * 16 >= posZstart) && (cc.field_77275_b * 16 <= posZstart + 1280)) {
/* 188 */         drawPixel(startX + (cc.field_77276_a * 16 - posXstart) / 8, startY + (cc.field_77275_b * 16 - posZstart) / 8 + 1, -251658241);
/*     */         
/* 190 */         if ((mouseX == cc.field_77276_a * 16) && (mouseZ == cc.field_77275_b * 16) && (!labelForced)) {
/* 191 */           labels.add(MLN.string("chunk.chunkforced"));
/* 192 */           labelForced = true;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 197 */     if (!labels.isEmpty()) {
/* 198 */       int stringlength = 0;
/*     */       
/* 200 */       for (String s : labels) {
/* 201 */         int w = this.field_146289_q.func_78256_a(s);
/* 202 */         if (w > stringlength) {
/* 203 */           stringlength = w;
/*     */         }
/*     */       }
/*     */       
/* 207 */       func_73733_a(i - 3 - windowXstart + 10, j - 3 - windowYstart, i + stringlength + 3 - windowXstart + 10, j + 11 * labels.size() - windowYstart, -1073741824, -1073741824);
/*     */       
/* 209 */       for (int si = 0; si < labels.size(); si++) {
/* 210 */         this.field_146289_q.func_78276_b((String)labels.get(si), i - windowXstart + 10, j - windowYstart + 11 * si, 9474192);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 215 */     GL11.glEnable(2896);
/* 216 */     GL11.glEnable(2929);
/*     */   }
/*     */   
/*     */   private void drawPixel(int x, int y, int colour)
/*     */   {
/* 221 */     func_73733_a(x, y, x + 1, y + 1, colour, colour);
/*     */   }
/*     */   
/*     */   private void drawVillageMap(int i, int j)
/*     */   {
/* 226 */     int xStart = (this.field_146294_l - getXSize()) / 2;
/* 227 */     int yStart = (this.field_146295_m - getYSize()) / 2;
/*     */     
/* 229 */     MillMapInfo minfo = this.townHall.mapInfo;
/*     */     
/* 231 */     GL11.glDisable(2896);
/* 232 */     GL11.glDisable(2929);
/*     */     
/* 234 */     int startX = (getXSize() - minfo.length) / 2;
/* 235 */     int startY = (getYSize() - minfo.width) / 2;
/*     */     
/* 237 */     func_73733_a(startX - 2, startY - 2, startX + minfo.length + 2, startY + minfo.width + 2, 536870912, 536870912);
/*     */     
/* 239 */     BuildingLocation locHover = null;
/* 240 */     MillVillager villagerHover = null;
/*     */     
/* 242 */     byte thRegionId = -1;
/*     */     
/* 244 */     if (this.townHall.pathing != null) {
/* 245 */       thRegionId = this.townHall.pathing.thRegion;
/*     */     }
/*     */     
/* 248 */     for (int x = 0; x < minfo.length; x++) {
/* 249 */       for (int y = 0; y < minfo.width; y++)
/*     */       {
/* 251 */         int colour = 0;
/*     */         
/* 253 */         BuildingLocation bl = this.townHall.getLocationAtCoordPlanar(new Point(x + minfo.mapStartX, 0.0D, y + minfo.mapStartZ));
/*     */         
/* 255 */         byte groundType = minfo.data[x][y];
/*     */         
/* 257 */         if (bl != null) {
/* 258 */           if (bl == this.townHall.buildingLocationIP) {
/* 259 */             colour = 1090453759;
/* 260 */           } else if (bl.level < 0) {
/* 261 */             colour = 1073741920;
/*     */           } else {
/* 263 */             Building b = bl.getBuilding(this.townHall.worldObj);
/* 264 */             if ((b == null) || (thRegionId == -1) || (b.isReachableFromRegion(thRegionId))) {
/* 265 */               colour = 1073742079;
/*     */             } else {
/* 267 */               colour = 1090453504;
/*     */             }
/*     */           }
/* 270 */         } else if (groundType == 1) {
/* 271 */           colour = -1439682305;
/* 272 */         } else if (groundType == 2) {
/* 273 */           colour = 1090453504;
/* 274 */         } else if (groundType == 3) {
/* 275 */           colour = 1090518784;
/* 276 */         } else if (groundType == 4) {
/* 277 */           colour = 1090486336;
/* 278 */         } else if (groundType == 5) {
/* 279 */           colour = 268500736;
/* 280 */         } else if (groundType == 10) {
/* 281 */           colour = 536936192;
/* 282 */         } else if (groundType == 6) {
/* 283 */           colour = 1090474064;
/* 284 */         } else if (groundType == 7) {
/* 285 */           colour = Integer.MIN_VALUE;
/* 286 */         } else if (groundType == 8) {
/* 287 */           colour = 1083834265;
/*     */         } else {
/* 289 */           colour = 1073807104;
/*     */         }
/*     */         
/* 292 */         drawPixel(startX + x, startY + y, colour);
/*     */         
/* 294 */         if ((xStart + startX + x == i) && (yStart + startY + y == j) && (bl != null)) {
/* 295 */           locHover = bl;
/*     */         }
/*     */       }
/*     */     }
/* 299 */     for (MillVillager villager : this.townHall.villagers)
/*     */     {
/* 301 */       int mapPosX = (int)(villager.field_70165_t - minfo.mapStartX);
/* 302 */       int mapPosZ = (int)(villager.field_70161_v - minfo.mapStartZ);
/*     */       
/* 304 */       if ((mapPosX > 0) && (mapPosZ > 0) && (mapPosX < minfo.length) && (mapPosZ < minfo.width))
/*     */       {
/* 306 */         if (villager.func_70631_g_()) {
/* 307 */           func_73733_a(startX + mapPosX - 1, startY + mapPosZ - 1, startX + mapPosX + 1, startY + mapPosZ + 1, -1593835776, -1593835776);
/* 308 */         } else if ((villager.getRecord() != null) && (villager.getRecord().raidingVillage)) {
/* 309 */           func_73733_a(startX + mapPosX - 1, startY + mapPosZ - 1, startX + mapPosX + 1, startY + mapPosZ + 1, -1610612736, -1610612736);
/* 310 */         } else if (villager.gender == 1) {
/* 311 */           func_73733_a(startX + mapPosX - 1, startY + mapPosZ - 1, startX + mapPosX + 1, startY + mapPosZ + 1, -1610547201, -1610547201);
/*     */         } else {
/* 313 */           func_73733_a(startX + mapPosX - 1, startY + mapPosZ - 1, startX + mapPosX + 1, startY + mapPosZ + 1, -1593901056, -1593901056);
/*     */         }
/*     */         
/* 316 */         int screenPosX = xStart + startX + mapPosX;
/* 317 */         int screenPosY = yStart + startY + mapPosZ;
/*     */         
/* 319 */         if ((screenPosX > i - 2) && (screenPosX < i + 2) && (screenPosY > j - 2) && (screenPosY < j + 2)) {
/* 320 */           villagerHover = villager;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     int pos;
/* 326 */     if (villagerHover != null) {
/* 327 */       int stringlength = this.field_146289_q.func_78256_a(villagerHover.getName());
/* 328 */       stringlength = Math.max(stringlength, this.field_146289_q.func_78256_a(villagerHover.getNativeOccupationName()));
/*     */       
/* 330 */       boolean gameString = (villagerHover.getGameOccupationName(this.player.getDisplayName()) != null) && (villagerHover.getGameOccupationName(this.player.getDisplayName()).length() > 0);
/*     */       
/* 332 */       if (gameString) {
/* 333 */         stringlength = Math.max(stringlength, this.field_146289_q.func_78256_a(villagerHover.getGameOccupationName(this.player.getDisplayName())));
/*     */         
/* 335 */         func_73733_a(i + 10 - 3 - xStart, j + 10 - 3 - yStart, i + 10 + stringlength + 3 - xStart, j + 10 + 33 - yStart, -1073741824, -1073741824);
/* 336 */         this.field_146289_q.func_78276_b(villagerHover.getName(), i + 10 - xStart, j + 10 - yStart, 9474192);
/* 337 */         this.field_146289_q.func_78276_b(villagerHover.getNativeOccupationName(), i + 10 - xStart, j + 10 - yStart + 11, 9474192);
/* 338 */         this.field_146289_q.func_78276_b(villagerHover.getGameOccupationName(this.player.getDisplayName()), i + 10 - xStart, j + 10 - yStart + 22, 9474192);
/*     */       } else {
/* 340 */         func_73733_a(i + 10 - 3 - xStart, j + 10 - 3 - yStart, i + 10 + stringlength + 3 - xStart, j + 10 + 22 - yStart, -1073741824, -1073741824);
/* 341 */         this.field_146289_q.func_78276_b(villagerHover.getName(), i + 10 - xStart, j + 10 - yStart, 9474192);
/* 342 */         this.field_146289_q.func_78276_b(villagerHover.getNativeOccupationName(), i + 10 - xStart, j + 10 - yStart + 11, 9474192);
/*     */       }
/* 344 */     } else if (locHover != null)
/*     */     {
/* 346 */       Building b = locHover.getBuilding(this.townHall.worldObj);
/*     */       
/* 348 */       boolean unreachable = (b != null) && (this.townHall.pathing != null) && (!b.isReachableFromRegion(this.townHall.pathing.thRegion));
/*     */       
/*     */       String nativeString;
/*     */       
/*     */       int stringlength;
/*     */       String nativeString;
/* 354 */       if (unreachable) {
/* 355 */         int stringlength = this.field_146289_q.func_78256_a(locHover.getNativeName() + " - " + MLN.string("panels.unreachablebuilding"));
/* 356 */         nativeString = locHover.getNativeName() + " - " + MLN.string("panels.unreachablebuilding");
/*     */       } else {
/* 358 */         stringlength = this.field_146289_q.func_78256_a(locHover.getNativeName());
/* 359 */         nativeString = locHover.getNativeName();
/*     */       }
/*     */       
/* 362 */       int nblines = 1;
/*     */       
/* 364 */       boolean gameString = (locHover.getGameName() != null) && (locHover.getGameName().length() > 0);
/*     */       
/* 366 */       if (gameString) {
/* 367 */         stringlength = Math.max(stringlength, this.field_146289_q.func_78256_a(locHover.getGameName()));
/* 368 */         nblines++;
/*     */       }
/*     */       
/* 371 */       List<String> effects = locHover.getBuildingEffects(this.townHall.worldObj);
/*     */       
/* 373 */       nblines += effects.size();
/*     */       
/* 375 */       for (String s : effects) {
/* 376 */         stringlength = Math.max(stringlength, this.field_146289_q.func_78256_a(s));
/*     */       }
/*     */       
/* 379 */       func_73733_a(i - 3 - xStart, j - 3 - yStart, i + stringlength + 3 - xStart, j + 11 * nblines - yStart, -1073741824, -1073741824);
/* 380 */       this.field_146289_q.func_78276_b(nativeString, i - xStart, j - yStart, 9474192);
/*     */       
/* 382 */       pos = 1;
/*     */       
/* 384 */       if (gameString) {
/* 385 */         this.field_146289_q.func_78276_b(locHover.getGameName(), i - xStart, j - yStart + 11, 9474192);
/* 386 */         pos++;
/*     */       }
/*     */       
/* 389 */       for (String s : effects) {
/* 390 */         this.field_146289_q.func_78276_b(s, i - xStart, j - yStart + 11 * pos, 9474192);
/* 391 */         pos++;
/*     */       }
/*     */     }
/*     */     
/* 395 */     GL11.glEnable(2896);
/* 396 */     GL11.glEnable(2929);
/*     */   }
/*     */   
/*     */ 
/*     */   public int getLineSizeInPx()
/*     */   {
/* 402 */     return 195;
/*     */   }
/*     */   
/*     */   public int getPageSize()
/*     */   {
/* 407 */     return 19;
/*     */   }
/*     */   
/*     */   public ResourceLocation getPNGPath()
/*     */   {
/* 412 */     if (this.isParchment) {
/* 413 */       return this.backgroundParchment;
/*     */     }
/* 415 */     return this.backgroundPanel;
/*     */   }
/*     */   
/*     */ 
/*     */   public int getXSize()
/*     */   {
/* 421 */     return 204;
/*     */   }
/*     */   
/*     */   public int getYSize()
/*     */   {
/* 426 */     return 220;
/*     */   }
/*     */   
/*     */   public void initData()
/*     */   {
/* 431 */     if (this.fullText != null) {
/* 432 */       this.descText = convertAdjustText(this.fullText);
/*     */     }
/*     */     
/* 435 */     if (this.fullTextLines != null) {
/* 436 */       this.descText = adjustText(this.fullTextLines);
/*     */     }
/*     */     
/* 439 */     if ((this.mapType == 1) && (this.townHall != null)) {
/* 440 */       org.millenaire.client.network.ClientSender.requestMapInfo(this.townHall);
/*     */     }
/*     */   }
/*     */   
/*     */   public void func_146281_b()
/*     */   {
/* 446 */     org.lwjgl.input.Keyboard.enableRepeatEvents(false);
/*     */   }
/*     */   
/*     */   public void func_73876_c() {}
/*     */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\client\gui\GuiPanelParchment.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */