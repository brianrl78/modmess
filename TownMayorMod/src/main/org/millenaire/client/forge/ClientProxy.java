/*     */ package org.millenaire.client.forge;
/*     */ 
/*     */ import cpw.mods.fml.client.registry.RenderingRegistry;
/*     */ import cpw.mods.fml.common.FMLCommonHandler;
/*     */ import cpw.mods.fml.common.eventhandler.EventBus;
/*     */ import cpw.mods.fml.common.network.FMLEventChannel;
/*     */ import cpw.mods.fml.common.network.IGuiHandler;
/*     */ import cpw.mods.fml.common.registry.GameRegistry;
/*     */ import java.io.File;
/*     */ import java.util.HashMap;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.GuiIngame;
/*     */ import net.minecraft.client.gui.GuiNewChat;
/*     */ import net.minecraft.client.model.ModelBiped;
/*     */ import net.minecraft.client.renderer.entity.RenderBiped;
/*     */ import net.minecraft.client.renderer.texture.IIconRegister;
/*     */ import net.minecraft.client.renderer.texture.TextureMap;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.ChatComponentText;
/*     */ import net.minecraft.util.ChatStyle;
/*     */ import net.minecraft.util.EnumChatFormatting;
/*     */ import org.lwjgl.input.Keyboard;
/*     */ import org.millenaire.client.MillClientUtilities;
/*     */ import org.millenaire.client.ModelFemaleAsymmetrical;
/*     */ import org.millenaire.client.ModelFemaleSymmetrical;
/*     */ import org.millenaire.client.RenderMillVillager;
/*     */ import org.millenaire.client.network.ClientReceiver;
/*     */ import org.millenaire.client.network.ClientSender;
/*     */ import org.millenaire.client.texture.TextureAmuletVishnu;
/*     */ import org.millenaire.client.texture.TextureAmuletYddrasil;
/*     */ import org.millenaire.common.EntityMillDecoration;
/*     */ import org.millenaire.common.InvItem;
/*     */ import org.millenaire.common.MLN;
/*     */ import org.millenaire.common.MLN.MillenaireException;
/*     */ import org.millenaire.common.MillVillager.MLEntityGenericSymmFemale;
/*     */ import org.millenaire.common.MillVillager.MLEntityGenericZombie;
/*     */ import org.millenaire.common.MillWorld;
/*     */ import org.millenaire.common.TileEntityMillChest;
/*     */ import org.millenaire.common.TileEntityPanel;
/*     */ import org.millenaire.common.UserProfile;
/*     */ import org.millenaire.common.forge.CommonProxy;
/*     */ import org.millenaire.common.forge.Mill;
/*     */ import org.millenaire.common.item.Goods.ItemMillenaireBow;
/*     */ 
/*     */ public class ClientProxy extends CommonProxy
/*     */ {
/*     */   public void checkTextureSize() {}
/*     */   
/*     */   public IGuiHandler createGuiHandler()
/*     */   {
/*  54 */     return new ClientGuiHandler();
/*     */   }
/*     */   
/*     */   public void declareAmuletTextures(IIconRegister iconRegister)
/*     */   {
/*  59 */     TextureMap textureMap = (TextureMap)iconRegister;
/*     */     
/*  61 */     textureMap.setTextureEntry("millenaire:amulet_alchemist" + MLN.getTextSuffix(), new org.millenaire.client.texture.TextureAmuletAlchemist("millenaire:amulet_alchemist" + MLN.getTextSuffix()));
/*  62 */     textureMap.setTextureEntry("millenaire:amulet_vishnu" + MLN.getTextSuffix(), new TextureAmuletVishnu("millenaire:amulet_vishnu" + MLN.getTextSuffix()));
/*  63 */     textureMap.setTextureEntry("millenaire:amulet_yggdrasil" + MLN.getTextSuffix(), new TextureAmuletYddrasil("millenaire:amulet_yggdrasil" + MLN.getTextSuffix()));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public File getBaseDir()
/*     */   {
/*  70 */     if (this.baseDir == null) {
/*  71 */       this.baseDir = new File(new File(Minecraft.func_71410_x().field_71412_D, "mods"), "millenaire");
/*     */     }
/*     */     
/*  74 */     return this.baseDir;
/*     */   }
/*     */   
/*     */   public String getBlockName(Block block, int meta)
/*     */   {
/*  79 */     if (block == null) {
/*  80 */       MLN.printException(new MLN.MillenaireException("Trying to get the name of a null block."));
/*  81 */       return null;
/*     */     }
/*     */     
/*  84 */     if (meta == -1) {
/*  85 */       meta = 0;
/*     */     }
/*     */     
/*  88 */     return new ItemStack(block, 1, meta).func_82833_r();
/*     */   }
/*     */   
/*     */   public UserProfile getClientProfile()
/*     */   {
/*  93 */     if (Mill.clientWorld.profiles.containsKey(Mill.proxy.getTheSinglePlayer().getDisplayName())) {
/*  94 */       return (UserProfile)Mill.clientWorld.profiles.get(Mill.proxy.getTheSinglePlayer().getDisplayName());
/*     */     }
/*     */     
/*  97 */     UserProfile profile = new UserProfile(Mill.clientWorld, Mill.proxy.getTheSinglePlayer().getDisplayName(), Mill.proxy.getTheSinglePlayer().getDisplayName());
/*  98 */     Mill.clientWorld.profiles.put(profile.key, profile);
/*  99 */     return profile;
/*     */   }
/*     */   
/*     */   public File getConfigFile()
/*     */   {
/* 104 */     return new File(getBaseDir(), "config.txt");
/*     */   }
/*     */   
/*     */   public File getCustomConfigFile()
/*     */   {
/* 109 */     return new File(getCustomDir(), "config-custom.txt");
/*     */   }
/*     */   
/*     */ 
/*     */   public File getCustomDir()
/*     */   {
/* 115 */     if (this.customDir == null) {
/* 116 */       this.customDir = new File(new File(Minecraft.func_71410_x().field_71412_D, "mods"), "millenaire-custom");
/*     */     }
/*     */     
/* 119 */     return this.customDir;
/*     */   }
/*     */   
/*     */   public String getInvItemName(InvItem iv)
/*     */   {
/* 124 */     if (iv.block != null) {
/* 125 */       return getBlockName(iv.block, iv.meta);
/*     */     }
/* 127 */     return getItemName(iv.item, iv.meta);
/*     */   }
/*     */   
/*     */ 
/*     */   public String getItemName(Item item, int meta)
/*     */   {
/* 133 */     if (item == null) {
/* 134 */       MLN.printException(new MLN.MillenaireException("Trying to get the name of a null item."));
/* 135 */       return null;
/*     */     }
/*     */     
/* 138 */     if (meta == -1) {
/* 139 */       meta = 0;
/*     */     }
/*     */     
/* 142 */     return new ItemStack(item, 1, meta).func_82833_r();
/*     */   }
/*     */   
/*     */   public String getKeyString(int value)
/*     */   {
/* 147 */     return Keyboard.getKeyName(value);
/*     */   }
/*     */   
/*     */   public File getLogFile()
/*     */   {
/* 152 */     return new File(getBaseDir(), "millenaire.log");
/*     */   }
/*     */   
/*     */   public String getQuestKeyName()
/*     */   {
/* 157 */     return Keyboard.getKeyName(MLN.keyInfoPanelList);
/*     */   }
/*     */   
/*     */ 
/*     */   public String getSinglePlayerName()
/*     */   {
/* 163 */     if (Minecraft.func_71410_x().field_71439_g != null) {
/* 164 */       return Minecraft.func_71410_x().field_71439_g.getDisplayName();
/*     */     }
/* 166 */     return "NULL_PLAYER";
/*     */   }
/*     */   
/*     */   public EntityPlayer getTheSinglePlayer()
/*     */   {
/* 171 */     return Minecraft.func_71410_x().field_71439_g;
/*     */   }
/*     */   
/*     */ 
/*     */   public void handleClientGameUpdate()
/*     */   {
/* 177 */     MillClientUtilities.handleKeyPress(Mill.clientWorld.world);
/*     */     
/* 179 */     if (Mill.clientWorld.world.func_72820_D() % 20L == 0L) {
/* 180 */       Mill.clientWorld.clearPanelQueue();
/*     */     }
/*     */     
/*     */ 
/* 184 */     loadLanguages();
/*     */   }
/*     */   
/*     */   public void handleClientLogin()
/*     */   {
/* 189 */     ClientSender.sendVersionInfo();
/* 190 */     ClientSender.sendAvailableContent();
/*     */   }
/*     */   
/*     */   public void initNetwork()
/*     */   {
/* 195 */     Mill.millChannel.register(new ClientReceiver());
/*     */   }
/*     */   
/*     */   public boolean isTrueServer()
/*     */   {
/* 200 */     return false;
/*     */   }
/*     */   
/*     */   public void loadKeyDefaultSettings()
/*     */   {
/* 205 */     MLN.keyVillageList = 47;
/* 206 */     MLN.keyInfoPanelList = 50;
/* 207 */     MLN.keyAggressiveEscorts = 34;
/*     */   }
/*     */   
/*     */   public int loadKeySetting(String value)
/*     */   {
/* 212 */     return Keyboard.getKeyIndex(value.toUpperCase());
/*     */   }
/*     */   
/*     */   public void loadLanguages()
/*     */   {
/* 217 */     Minecraft minecraft = Minecraft.func_71410_x();
/*     */     
/* 219 */     MLN.loadLanguages(minecraft.field_71474_y.field_74363_ab);
/*     */   }
/*     */   
/*     */ 
/*     */   public void localTranslatedSentence(EntityPlayer player, char colour, String code, String... values)
/*     */   {
/* 225 */     for (int i = 0; i < values.length; i++) {
/* 226 */       values[i] = MLN.unknownString(values[i]);
/*     */     }
/*     */     
/* 229 */     sendLocalChat(player, colour, MLN.string(code, values));
/*     */   }
/*     */   
/*     */   public String logPrefix()
/*     */   {
/* 234 */     return "CLIENT ";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void preloadTextures() {}
/*     */   
/*     */ 
/*     */   public void refreshClientResources()
/*     */   {
/* 244 */     Minecraft.func_71410_x().func_110436_a();
/*     */   }
/*     */   
/*     */   public void registerForgeClientClasses()
/*     */   {
/* 249 */     FMLCommonHandler.instance().bus().register(new ClientTickHandler());
/* 250 */     FMLCommonHandler.instance().bus().register(new ClientReceiver());
/*     */   }
/*     */   
/*     */   public void registerGraphics()
/*     */   {
/* 255 */     RenderingRegistry.registerEntityRenderingHandler(org.millenaire.common.MillVillager.MLEntityGenericMale.class, new RenderMillVillager(new ModelBiped(), 0.5F));
/* 256 */     RenderingRegistry.registerEntityRenderingHandler(org.millenaire.common.MillVillager.MLEntityGenericAsymmFemale.class, new RenderMillVillager(new ModelFemaleAsymmetrical(), 0.5F));
/* 257 */     RenderingRegistry.registerEntityRenderingHandler(MillVillager.MLEntityGenericSymmFemale.class, new RenderMillVillager(new ModelFemaleSymmetrical(), 0.5F));
/* 258 */     RenderingRegistry.registerEntityRenderingHandler(MillVillager.MLEntityGenericZombie.class, new RenderBiped(new net.minecraft.client.model.ModelZombie(), 0.5F));
/* 259 */     RenderingRegistry.registerEntityRenderingHandler(EntityMillDecoration.class, new org.millenaire.client.RenderWallDecoration());
/*     */   }
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
/*     */   public void registerTileEntities()
/*     */   {
/* 274 */     GameRegistry.registerTileEntity(TileEntityMillChest.class, "ml_TileEntityBuilding");
/* 275 */     GameRegistry.registerTileEntity(TileEntityPanel.class, "ml_TileEntityPanel");
/*     */   }
/*     */   
/*     */   public void sendChatAdmin(String s)
/*     */   {
/* 280 */     s = s.trim();
/* 281 */     Minecraft.func_71410_x().field_71456_v.func_146158_b().func_146227_a(new ChatComponentText(s));
/*     */   }
/*     */   
/*     */   public void sendChatAdmin(String s, EnumChatFormatting colour)
/*     */   {
/* 286 */     s = s.trim();
/* 287 */     ChatComponentText cc = new ChatComponentText(s);
/* 288 */     cc.func_150256_b().func_150238_a(colour);
/* 289 */     Minecraft.func_71410_x().field_71456_v.func_146158_b().func_146227_a(cc);
/*     */   }
/*     */   
/*     */   public void sendLocalChat(EntityPlayer player, char colour, String s)
/*     */   {
/* 294 */     s = s.trim();
/* 295 */     Minecraft.func_71410_x().field_71456_v.func_146158_b().func_146227_a(new ChatComponentText("§" + colour + s));
/*     */   }
/*     */   
/*     */ 
/*     */   public void setTextureIds()
/*     */   {
/* 301 */     Mill.normanArmourId = RenderingRegistry.addNewArmourRendererPrefix("ML_norman");
/* 302 */     Mill.japaneseWarriorBlueArmourId = RenderingRegistry.addNewArmourRendererPrefix("ML_japanese_warrior_blue");
/* 303 */     Mill.japaneseWarriorRedArmourId = RenderingRegistry.addNewArmourRendererPrefix("ML_japanese_warrior_red");
/* 304 */     Mill.japaneseGuardArmourId = RenderingRegistry.addNewArmourRendererPrefix("ML_japanese_guard");
/* 305 */     Mill.byzantineArmourId = RenderingRegistry.addNewArmourRendererPrefix("ML_byzantine");
/* 306 */     Mill.mayanQuestArmourId = RenderingRegistry.addNewArmourRendererPrefix("ML_mayan_quest");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void testTextureSize() {}
/*     */   
/*     */ 
/*     */   public void updateBowIcon(Goods.ItemMillenaireBow bow, EntityPlayer entityplayer)
/*     */   {
/* 316 */     MillClientUtilities.updateBowIcon(bow, entityplayer);
/*     */   }
/*     */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\client\forge\ClientProxy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */