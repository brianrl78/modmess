/*     */ package org.millenaire.client;
/*     */ 
/*     */ import cpw.mods.fml.client.FMLClientHandler;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.renderer.texture.ITextureObject;
/*     */ import net.minecraft.client.renderer.texture.TextureManager;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.EnumChatFormatting;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraftforge.common.ForgeChunkManager;
/*     */ import org.lwjgl.input.Keyboard;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ import org.millenaire.client.gui.DisplayActions;
/*     */ import org.millenaire.client.gui.GuiPanelParchment;
/*     */ import org.millenaire.client.gui.GuiText.Line;
/*     */ import org.millenaire.client.network.ClientSender;
/*     */ import org.millenaire.common.Culture;
/*     */ import org.millenaire.common.InvItem;
/*     */ import org.millenaire.common.MLN;
/*     */ import org.millenaire.common.MillVillager;
/*     */ import org.millenaire.common.MillWorld;
/*     */ import org.millenaire.common.Point;
/*     */ import org.millenaire.common.Quest.QuestInstance;
/*     */ import org.millenaire.common.TileEntityPanel;
/*     */ import org.millenaire.common.TileEntityPanel.PanelPacketInfo;
/*     */ import org.millenaire.common.UserProfile;
/*     */ import org.millenaire.common.building.Building;
/*     */ import org.millenaire.common.core.DevModUtilities;
/*     */ import org.millenaire.common.core.MillCommonUtilities;
/*     */ import org.millenaire.common.forge.CommonProxy;
/*     */ import org.millenaire.common.forge.Mill;
/*     */ import org.millenaire.common.item.Goods;
/*     */ import org.millenaire.common.item.Goods.ItemMillenaireBow;
/*     */ 
/*     */ public class MillClientUtilities
/*     */ {
/*  43 */   private static long lastPing = 0L;
/*  44 */   private static long lastFreeRes = 0L;
/*     */   
/*  46 */   private static final ResourceLocation textureTest = new ResourceLocation("millenaire", "textures/blocks/blockGold.png");
/*     */   
/*     */   public static void checkTextSize() {
/*  49 */     ITextureObject texture = Minecraft.func_71410_x().field_71446_o.func_110581_b(textureTest);
/*     */     
/*  51 */     if (texture == null) {
/*  52 */       return;
/*     */     }
/*     */     
/*  55 */     GL11.glBindTexture(3553, texture.func_110552_b());
/*  56 */     int textSize = GL11.glGetTexLevelParameteri(3553, 0, 4096);
/*     */     
/*  58 */     if (textSize != MLN.textureSize) {
/*  59 */       MLN.textureSize = textSize;
/*  60 */       Minecraft.func_71410_x().field_71446_o.func_110550_d();
/*     */     }
/*     */   }
/*     */   
/*     */   public static void displayChunkPanel(World world, EntityPlayer player)
/*     */   {
/*  66 */     List<List<String>> pages = new ArrayList();
/*     */     
/*  68 */     List<String> page = new ArrayList();
/*     */     
/*  70 */     page.add(MLN.string("chunk.chunkmap"));
/*     */     
/*  72 */     pages.add(page);
/*     */     
/*  74 */     page = new ArrayList();
/*     */     
/*  76 */     page.add(MLN.string("chunk.caption"));
/*     */     
/*  78 */     page.add(MLN.string(""));
/*  79 */     page.add(MLN.string("chunk.captiongeneral"));
/*  80 */     page.add(MLN.string("chunk.captiongreen"));
/*  81 */     page.add(MLN.string("chunk.captionblue"));
/*  82 */     page.add(MLN.string("chunk.captionpurple"));
/*  83 */     page.add(MLN.string("chunk.captionwhite"));
/*  84 */     page.add(MLN.string(""));
/*  85 */     page.add(MLN.string("chunk.playerposition", new String[] { (int)player.field_70165_t + "/" + (int)player.field_70161_v }));
/*  86 */     page.add(MLN.string(""));
/*  87 */     page.add(MLN.string("chunk.settings", new String[] { "" + MLN.KeepActiveRadius, "" + ForgeChunkManager.getMaxTicketLengthFor("millenaire") }));
/*  88 */     page.add(MLN.string(""));
/*  89 */     page.add(MLN.string("chunk.explanations"));
/*     */     
/*  91 */     pages.add(page);
/*     */     
/*  93 */     Minecraft.func_71410_x().func_147108_a(new GuiPanelParchment(player, pages, null, 2, true));
/*     */   }
/*     */   
/*     */   public static void displayInfoPanel(World world, EntityPlayer player)
/*     */   {
/*  98 */     List<List<String>> pages = new ArrayList();
/*  99 */     List<String> page = new ArrayList();
/*     */     
/* 101 */     page.add("<help_gui_button>");
/* 102 */     page.add("");
/* 103 */     page.add("");
/*     */     
/* 105 */     page.add("<config_gui_button>");
/* 106 */     page.add("");
/* 107 */     page.add("");
/*     */     
/* 109 */     if (!Mill.serverWorlds.isEmpty()) {
/* 110 */       page.add("<chunk_gui_button>");
/* 111 */       page.add("");
/* 112 */       page.add("");
/*     */     }
/*     */     
/* 115 */     page.add(MLN.string("info.culturetitle"));
/* 116 */     page.add("");
/*     */     
/* 118 */     for (Culture culture : Culture.ListCultures)
/*     */     {
/* 120 */       page.add(MLN.string("info.culture", new String[] { culture.getCultureGameName() }));
/* 121 */       page.add(MLN.string("info.culturereputation", new String[] { culture.getReputationString() }));
/* 122 */       if (MLN.languageLearning) {
/* 123 */         page.add(MLN.string("info.culturelanguage", new String[] { culture.getLanguageLevelString() }));
/*     */       }
/* 125 */       page.add("");
/*     */     }
/*     */     
/* 128 */     pages.add(page);
/* 129 */     page = new ArrayList();
/*     */     
/* 131 */     page.add(MLN.string("quest.creationqueststatus"));
/* 132 */     page.add("");
/*     */     
/* 134 */     for (String s : Mill.proxy.getClientProfile().getWorldQuestStatus()) {
/* 135 */       page.add(s);
/*     */     }
/*     */     
/* 138 */     page.add("");
/*     */     
/* 140 */     page.add(MLN.string("quest.questlist"));
/* 141 */     page.add("");
/*     */     
/* 143 */     boolean questShown = false;
/*     */     
/* 145 */     UserProfile profile = Mill.proxy.getClientProfile();
/*     */     
/* 147 */     for (Quest.QuestInstance qi : profile.questInstances) {
/* 148 */       String s = qi.getListing(profile);
/* 149 */       if (s != null) {
/* 150 */         questShown = true;
/*     */         
/* 152 */         page.add(s);
/*     */         
/* 154 */         long timeLeft = qi.currentStepStart + qi.getCurrentStep().duration * 1000 - world.func_72820_D();
/* 155 */         timeLeft = Math.round((float)(timeLeft / 1000L));
/*     */         
/* 157 */         if (timeLeft == 0L) {
/* 158 */           page.add(MLN.string("quest.lessthananhourleft"));
/*     */         } else {
/* 160 */           page.add(MLN.string("quest.timeremaining") + ": " + timeLeft + " " + MLN.string("quest.hours"));
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 165 */     if (!questShown) {
/* 166 */       page.add(MLN.string("quest.noquestsvisible"));
/*     */     }
/*     */     
/* 169 */     pages.add(page);
/*     */     
/* 171 */     Minecraft.func_71410_x().func_147108_a(new GuiPanelParchment(player, pages, null, 0, true));
/*     */   }
/*     */   
/*     */   public static void displayPanel(World world, EntityPlayer player, Point p)
/*     */   {
/* 176 */     TileEntityPanel panel = p.getPanel(world);
/*     */     
/* 178 */     if ((panel == null) || (panel.buildingPos == null)) {
/* 179 */       return;
/*     */     }
/*     */     
/* 182 */     Building building = Mill.clientWorld.getBuilding(panel.buildingPos);
/*     */     
/* 184 */     if (building == null) {
/* 185 */       return;
/*     */     }
/*     */     
/* 188 */     List<List<String>> fullText = panel.getFullText(player);
/*     */     
/* 190 */     if (fullText != null) {
/* 191 */       DisplayActions.displayParchmentPanelGUI(player, fullText, building, panel.getMapType(), false);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void displayStartupText(boolean error) {
/* 196 */     if (error) {
/* 197 */       Mill.proxy.sendChatAdmin(MLN.string("startup.loadproblem", new String[] { "Millénaire 6.0.0" }));
/* 198 */       Mill.proxy.sendChatAdmin(MLN.string("startup.checkload"));
/* 199 */       MLN.error(null, "There was an error when trying to load Millénaire 6.0.0.");
/*     */     } else {
/* 201 */       if (MLN.displayStart)
/*     */       {
/* 203 */         String bonus = "";
/*     */         
/* 205 */         if (MLN.bonusEnabled) {
/* 206 */           bonus = " " + MLN.string("startup.bonus");
/*     */         }
/*     */         
/* 209 */         Mill.proxy.sendChatAdmin(MLN.string("startup.millenaireloaded", new String[] { "Millénaire 6.0.0", Keyboard.getKeyName(MLN.keyVillageList) }));
/* 210 */         Mill.proxy.sendChatAdmin(MLN.string("startup.bonus", new String[] { "Millénaire 6.0.0", bonus }), EnumChatFormatting.BLUE);
/*     */       }
/* 212 */       if (MLN.VillageRadius > 70) {
/* 213 */         Mill.proxy.sendChatAdmin(MLN.string("startup.radiuswarning"));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static void displayTradeHelp(Building shop, EntityPlayer player)
/*     */   {
/* 220 */     List<List<GuiText.Line>> pages = new ArrayList();
/* 221 */     List<GuiText.Line> page = new ArrayList();
/*     */     
/* 223 */     page.add(new GuiText.Line("<darkblue>" + MLN.string("tradehelp.title", new String[] { shop.getNativeBuildingName() })));
/* 224 */     page.add(new GuiText.Line(""));
/*     */     
/* 226 */     page.add(new GuiText.Line("<darkblue>" + MLN.string("tradehelp.goodssold")));
/* 227 */     page.add(new GuiText.Line(""));
/*     */     
/* 229 */     List<Goods> goods = shop.calculateSellingGoods(null);
/*     */     
/* 231 */     if (goods != null) {
/* 232 */       String lastDesc = null;
/* 233 */       List<ItemStack> stacks = new ArrayList();
/* 234 */       List<Integer> prices = new ArrayList();
/* 235 */       for (Goods g : goods) {
/* 236 */         if ((lastDesc != null) && (!lastDesc.equals(g.desc))) {
/* 237 */           List<String> vprices = new ArrayList();
/*     */           
/* 239 */           for (Iterator i$ = prices.iterator(); i$.hasNext();) { int p = ((Integer)i$.next()).intValue();
/* 240 */             vprices.add(MLN.string("tradehelp.sellingprice") + " " + MillCommonUtilities.getShortPrice(p));
/*     */           }
/*     */           
/* 243 */           page.add(new GuiText.Line(stacks, vprices, MLN.string(lastDesc), 72));
/* 244 */           page.add(new GuiText.Line());
/* 245 */           stacks = new ArrayList();
/* 246 */           prices = new ArrayList();
/*     */         }
/*     */         
/* 249 */         stacks.add(new ItemStack(g.item.item, 1, g.item.meta));
/* 250 */         prices.add(Integer.valueOf(g.getBasicSellingPrice(shop)));
/* 251 */         if (g.desc != null) {
/* 252 */           lastDesc = g.desc;
/*     */         } else {
/* 254 */           lastDesc = "";
/*     */         }
/*     */       }
/* 257 */       if (lastDesc != null) {
/* 258 */         List<String> vprices = new ArrayList();
/*     */         
/* 260 */         for (Iterator i$ = prices.iterator(); i$.hasNext();) { int p = ((Integer)i$.next()).intValue();
/* 261 */           vprices.add(MLN.string("tradehelp.sellingprice") + " " + MillCommonUtilities.getShortPrice(p));
/*     */         }
/*     */         
/* 264 */         page.add(new GuiText.Line(stacks, vprices, MLN.string(lastDesc), 72));
/* 265 */         page.add(new GuiText.Line());
/*     */       }
/*     */     }
/*     */     
/* 269 */     page.add(new GuiText.Line("<darkblue>" + MLN.string("tradehelp.goodsbought")));
/* 270 */     page.add(new GuiText.Line(""));
/*     */     
/* 272 */     goods = shop.calculateBuyingGoods(null);
/*     */     
/* 274 */     if (goods != null) {
/* 275 */       String lastDesc = null;
/* 276 */       List<ItemStack> stacks = new ArrayList();
/* 277 */       List<Integer> prices = new ArrayList();
/* 278 */       for (Goods g : goods) {
/* 279 */         if ((lastDesc != null) && (!lastDesc.equals(g.desc))) {
/* 280 */           List<String> vprices = new ArrayList();
/*     */           
/* 282 */           for (Iterator i$ = prices.iterator(); i$.hasNext();) { int p = ((Integer)i$.next()).intValue();
/* 283 */             vprices.add(MLN.string("tradehelp.buyingprice") + " " + MillCommonUtilities.getShortPrice(p));
/*     */           }
/*     */           
/* 286 */           page.add(new GuiText.Line(stacks, vprices, MLN.string(lastDesc), 72));
/* 287 */           page.add(new GuiText.Line());
/* 288 */           stacks = new ArrayList();
/* 289 */           prices = new ArrayList();
/*     */         }
/* 291 */         stacks.add(new ItemStack(g.item.item, 1, g.item.meta));
/* 292 */         prices.add(Integer.valueOf(g.getBasicBuyingPrice(shop)));
/* 293 */         if (g.desc != null) {
/* 294 */           lastDesc = g.desc;
/*     */         } else {
/* 296 */           lastDesc = "";
/*     */         }
/*     */       }
/* 299 */       if (lastDesc != null) {
/* 300 */         List<String> vprices = new ArrayList();
/*     */         
/* 302 */         for (Iterator i$ = prices.iterator(); i$.hasNext();) { int p = ((Integer)i$.next()).intValue();
/* 303 */           vprices.add(MLN.string("tradehelp.buyingprice") + " " + MillCommonUtilities.getShortPrice(p));
/*     */         }
/*     */         
/* 306 */         page.add(new GuiText.Line(stacks, vprices, MLN.string(lastDesc), 72));
/* 307 */         page.add(new GuiText.Line());
/*     */       }
/*     */     }
/*     */     
/* 311 */     pages.add(page);
/*     */     
/* 313 */     page = new ArrayList();
/*     */     
/* 315 */     page.add(new GuiText.Line("<darkblue>" + MLN.string("tradehelp.helptitle")));
/* 316 */     page.add(new GuiText.Line());
/* 317 */     page.add(new GuiText.Line(MLN.string("tradehelp.helptext")));
/*     */     
/* 319 */     pages.add(page);
/*     */     
/* 321 */     Minecraft.func_71410_x().func_147108_a(new GuiPanelParchment(player, null, pages, 0, true));
/*     */   }
/*     */   
/*     */   public static void displayVillageBook(World world, EntityPlayer player, Point p)
/*     */   {
/* 326 */     Building townHall = Mill.clientWorld.getBuilding(p);
/*     */     
/* 328 */     if (townHall == null) {
/* 329 */       return;
/*     */     }
/*     */     
/* 332 */     List<List<String>> pages = new ArrayList();
/* 333 */     List<String> page = new ArrayList();
/*     */     
/* 335 */     page.add(MLN.string("panels.villagescroll") + ": " + townHall.getVillageQualifiedName());
/* 336 */     page.add("");
/* 337 */     pages.add(page);
/*     */     
/* 339 */     List<List<String>> newPages = TileEntityPanel.generateSummary(player, townHall);
/*     */     
/* 341 */     for (List<String> newPage : newPages) {
/* 342 */       pages.add(newPage);
/*     */     }
/*     */     
/* 345 */     newPages = TileEntityPanel.generateEtatCivil(player, townHall);
/*     */     
/* 347 */     for (List<String> newPage : newPages) {
/* 348 */       pages.add(newPage);
/*     */     }
/*     */     
/* 351 */     newPages = TileEntityPanel.generateConstructions(player, townHall);
/* 352 */     for (List<String> newPage : newPages) {
/* 353 */       pages.add(newPage);
/*     */     }
/*     */     
/* 356 */     newPages = TileEntityPanel.generateProjects(player, townHall);
/* 357 */     for (List<String> newPage : newPages) {
/* 358 */       pages.add(newPage);
/*     */     }
/*     */     
/* 361 */     newPages = TileEntityPanel.generateResources(player, townHall);
/* 362 */     for (List<String> newPage : newPages) {
/* 363 */       pages.add(newPage);
/*     */     }
/*     */     
/* 366 */     newPages = TileEntityPanel.generateMarketGoods(townHall);
/* 367 */     for (List<String> newPage : newPages) {
/* 368 */       pages.add(newPage);
/*     */     }
/*     */     
/* 371 */     DisplayActions.displayParchmentPanelGUI(player, pages, townHall, 1, true);
/*     */   }
/*     */   
/*     */   public static void handleKeyPress(World world)
/*     */   {
/* 376 */     Minecraft minecraft = FMLClientHandler.instance().getClient();
/*     */     
/* 378 */     if (minecraft.field_71462_r != null) {
/* 379 */       return;
/*     */     }
/*     */     
/* 382 */     EntityPlayer player = minecraft.field_71439_g;
/*     */     
/* 384 */     if (System.currentTimeMillis() - lastPing > 2000L)
/*     */     {
/* 386 */       if (player.field_71093_bK == 0)
/*     */       {
/* 388 */         if (Keyboard.isKeyDown(MLN.keyVillageList)) {
/* 389 */           ClientSender.displayVillageList(false);
/* 390 */           lastPing = System.currentTimeMillis();
/*     */         }
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
/* 411 */         if ((!world.field_72995_K) && (Keyboard.isKeyDown(42)) && (Keyboard.isKeyDown(25))) {
/* 412 */           if (!MillVillager.usingCustomPathing) {
/* 413 */             MillVillager.usingCustomPathing = true;
/* 414 */             Mill.proxy.sendChatAdmin(MLN.string("command.astarpathing"));
/*     */           } else {
/* 416 */             MillVillager.usingCustomPathing = false;
/* 417 */             Mill.proxy.sendChatAdmin(MLN.string("command.minecraftpathing"));
/*     */           }
/* 419 */           lastPing = System.currentTimeMillis();
/*     */         }
/*     */         
/* 422 */         if (Keyboard.isKeyDown(MLN.keyAggressiveEscorts)) {
/* 423 */           boolean stance = !Keyboard.isKeyDown(42);
/*     */           
/*     */ 
/* 426 */           List list = world.func_72872_a(MillVillager.class, AxisAlignedBB.func_72330_a(player.field_70165_t, player.field_70163_u, player.field_70161_v, player.field_70165_t + 1.0D, player.field_70163_u + 1.0D, player.field_70161_v + 1.0D).func_72314_b(16.0D, 8.0D, 16.0D));
/*     */           
/*     */ 
/* 429 */           for (Object o : list) {
/* 430 */             MillVillager villager = (MillVillager)o;
/*     */             
/* 432 */             if (player.getDisplayName().equals(villager.hiredBy)) {
/* 433 */               villager.aggressiveStance = stance;
/*     */             }
/*     */           }
/*     */           
/* 437 */           lastPing = System.currentTimeMillis();
/*     */         }
/*     */         
/* 440 */         if (Keyboard.isKeyDown(MLN.keyInfoPanelList)) {
/* 441 */           displayInfoPanel(world, player);
/* 442 */           lastPing = System.currentTimeMillis();
/*     */         }
/*     */         
/* 445 */         if (Keyboard.isKeyDown(25)) {
/* 446 */           MLN.jpsPathing = !MLN.jpsPathing;
/* 447 */           if (MLN.jpsPathing) {
/* 448 */             Mill.proxy.sendChatAdmin("Chemins JPS / JPS pathing");
/*     */           } else {
/* 450 */             Mill.proxy.sendChatAdmin("Chemins classiques / classic pathing");
/*     */           }
/* 452 */           lastPing = System.currentTimeMillis();
/*     */         }
/*     */         
/* 455 */         if (MLN.DEV)
/*     */         {
/* 457 */           if ((Keyboard.isKeyDown(42)) && (Keyboard.isKeyDown(19)) && (System.currentTimeMillis() - lastFreeRes > 5000L)) {
/* 458 */             DevModUtilities.fillInFreeGoods(player);
/* 459 */             lastFreeRes = System.currentTimeMillis();
/*     */           }
/*     */           
/* 462 */           if ((Keyboard.isKeyDown(42)) && (Keyboard.isKeyDown(203))) {
/* 463 */             player.func_70107_b(player.field_70165_t + 10000.0D, player.field_70163_u + 10.0D, player.field_70161_v);
/*     */             
/* 465 */             lastPing = System.currentTimeMillis();
/*     */           }
/*     */           
/* 468 */           if ((Keyboard.isKeyDown(42)) && (Keyboard.isKeyDown(205))) {
/* 469 */             player.func_70107_b(player.field_70165_t - 10000.0D, player.field_70163_u + 10.0D, player.field_70161_v);
/*     */             
/* 471 */             lastPing = System.currentTimeMillis();
/*     */           }
/*     */           
/* 474 */           if (Keyboard.isKeyDown(38)) {
/* 475 */             ClientSender.displayVillageList(true);
/* 476 */             lastPing = System.currentTimeMillis();
/*     */           }
/*     */           
/* 479 */           if ((Keyboard.isKeyDown(50)) && (Keyboard.isKeyDown(42))) {
/* 480 */             ClientSender.devCommand(1);
/* 481 */             lastPing = System.currentTimeMillis();
/*     */           }
/*     */           
/* 484 */           if ((Keyboard.isKeyDown(21)) && (Keyboard.isKeyDown(29))) {
/* 485 */             Mill.proxy.sendChatAdmin("Sending test path request.");
/* 486 */             ClientSender.devCommand(2);
/* 487 */             lastPing = System.currentTimeMillis();
/*     */           }
/*     */           
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 498 */           if (Keyboard.isKeyDown(20)) {
/* 499 */             Mill.clientWorld.displayTagActionData(player);
/* 500 */             lastPing = System.currentTimeMillis();
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static void putVillagerSentenceInChat(MillVillager v)
/*     */   {
/* 509 */     if ((v.dialogueTargetFirstName != null) && (!v.dialogueChat)) {
/* 510 */       return;
/*     */     }
/*     */     
/* 513 */     int radius = 0;
/*     */     
/* 515 */     if (Mill.proxy.isTrueClient()) {
/* 516 */       radius = MLN.VillagersSentenceInChatDistanceClient;
/*     */     } else {
/* 518 */       radius = MLN.VillagersSentenceInChatDistanceSP;
/*     */     }
/*     */     
/* 521 */     EntityPlayer player = Mill.proxy.getTheSinglePlayer();
/*     */     
/* 523 */     if (v.getPos().distanceTo(player) > radius) {
/* 524 */       return;
/*     */     }
/*     */     
/* 527 */     String gameSpeech = MillCommonUtilities.getVillagerSentence(v, player.getDisplayName(), false);
/* 528 */     String nativeSpeech = MillCommonUtilities.getVillagerSentence(v, player.getDisplayName(), true);
/*     */     
/* 530 */     if ((nativeSpeech != null) || (gameSpeech != null)) {
/*     */       String s;
/*     */       String s;
/* 533 */       if (v.dialogueTargetFirstName != null) {
/* 534 */         s = MLN.string("other.chattosomeone", new String[] { v.getName(), v.dialogueTargetFirstName + " " + v.dialogueTargetLastName }) + ": ";
/*     */       } else {
/* 536 */         s = v.getName() + ": ";
/*     */       }
/*     */       
/* 539 */       if (nativeSpeech != null) {
/* 540 */         s = s + "§9" + nativeSpeech;
/*     */       }
/*     */       
/* 543 */       if ((nativeSpeech != null) && (gameSpeech != null)) {
/* 544 */         s = s + " ";
/*     */       }
/*     */       
/* 547 */       if (gameSpeech != null) {
/* 548 */         s = s + "§4" + gameSpeech;
/*     */       }
/*     */       
/* 551 */       Mill.proxy.sendLocalChat(Mill.proxy.getTheSinglePlayer(), v.dialogueColour, s);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void updateBowIcon(Goods.ItemMillenaireBow bow, EntityPlayer entityplayer)
/*     */   {
/* 557 */     ItemStack itemstack1 = entityplayer.field_71071_by.func_70448_g();
/* 558 */     if ((entityplayer.func_71039_bw()) && (itemstack1.func_77973_b() == bow)) {
/* 559 */       int k = itemstack1.func_77988_m() - entityplayer.func_71052_bv();
/* 560 */       if (k >= 18) {
/* 561 */         bow.setBowIcon(3);
/* 562 */       } else if (k > 13) {
/* 563 */         bow.setBowIcon(2);
/* 564 */       } else if (k > 0) {
/* 565 */         bow.setBowIcon(1);
/*     */       } else {
/* 567 */         bow.setBowIcon(0);
/*     */       }
/*     */     } else {
/* 570 */       bow.setBowIcon(0);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void updatePanel(Point p, String[][] lines, int type, Point buildingPos, long villager_id)
/*     */   {
/* 576 */     if (lines == null) {
/* 577 */       return;
/*     */     }
/*     */     
/* 580 */     TileEntityPanel panel = p.getPanel(Mill.clientWorld.world);
/*     */     
/* 582 */     if (panel == null) {
/* 583 */       TileEntityPanel.PanelPacketInfo pinfo = new TileEntityPanel.PanelPacketInfo(p, lines, buildingPos, type, villager_id);
/* 584 */       Mill.clientWorld.panelPacketInfos.add(pinfo);
/* 585 */       return;
/*     */     }
/*     */     
/* 588 */     panel.panelType = type;
/* 589 */     panel.buildingPos = buildingPos;
/* 590 */     panel.villager_id = villager_id;
/*     */     
/* 592 */     for (int i = 0; (i < lines.length) && (i < panel.field_145915_a.length); i++) {
/* 593 */       panel.field_145915_a[i] = MLN.string(lines[i]);
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\client\MillClientUtilities.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */