/*     */ package org.millenaire.common.forge;
/*     */ 
/*     */ import cpw.mods.fml.common.network.FMLEventChannel;
/*     */ import cpw.mods.fml.common.network.IGuiHandler;
/*     */ import cpw.mods.fml.common.registry.GameRegistry;
/*     */ import java.io.File;
/*     */ import java.util.List;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.client.renderer.texture.IIconRegister;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.util.EnumChatFormatting;
/*     */ import org.millenaire.common.InvItem;
/*     */ import org.millenaire.common.MLN;
/*     */ import org.millenaire.common.TileEntityMillChest;
/*     */ import org.millenaire.common.TileEntityPanel;
/*     */ import org.millenaire.common.UserProfile;
/*     */ import org.millenaire.common.item.Goods.ItemMillenaireBow;
/*     */ import org.millenaire.common.network.ServerReceiver;
/*     */ 
/*     */ 
/*     */ public class CommonProxy
/*     */ {
/*  24 */   protected File baseDir = null;
/*     */   
/*  26 */   protected File customDir = null;
/*     */   
/*     */ 
/*     */   public void checkTextureSize() {}
/*     */   
/*     */   public IGuiHandler createGuiHandler()
/*     */   {
/*  33 */     return new CommonGuiHandler();
/*     */   }
/*     */   
/*     */ 
/*     */   public void declareAmuletTextures(IIconRegister iconRegister) {}
/*     */   
/*     */   public File getBaseDir()
/*     */   {
/*  41 */     if (this.baseDir == null) {
/*  42 */       this.baseDir = new File(new File(new File("."), "mods"), "millenaire");
/*     */     }
/*     */     
/*  45 */     return this.baseDir;
/*     */   }
/*     */   
/*     */   public String getBlockName(Block block, int meta)
/*     */   {
/*  50 */     return null;
/*     */   }
/*     */   
/*     */   public UserProfile getClientProfile() {
/*  54 */     return null;
/*     */   }
/*     */   
/*     */   public File getConfigFile() {
/*  58 */     return new File(getBaseDir(), "config-server.txt");
/*     */   }
/*     */   
/*     */   public File getCustomConfigFile() {
/*  62 */     return new File(getCustomDir(), "config-server-custom.txt");
/*     */   }
/*     */   
/*     */   public File getCustomDir() {
/*  66 */     if (this.customDir == null) {
/*  67 */       this.customDir = new File(new File(new File("."), "mods"), "millenaire-custom");
/*     */     }
/*     */     
/*  70 */     return this.customDir;
/*     */   }
/*     */   
/*     */   public String getInvItemName(InvItem iv) {
/*  74 */     return "";
/*     */   }
/*     */   
/*     */   public String getItemName(Item item, int meta) {
/*  78 */     return "";
/*     */   }
/*     */   
/*     */   public String getKeyString(int value) {
/*  82 */     return "";
/*     */   }
/*     */   
/*     */   public File getLogFile() {
/*  86 */     return new File(getBaseDir(), "millenaire-server.log");
/*     */   }
/*     */   
/*     */   public String getQuestKeyName() {
/*  90 */     return "";
/*     */   }
/*     */   
/*     */   public String getSinglePlayerName() {
/*  94 */     return null;
/*     */   }
/*     */   
/*     */   public EntityPlayer getTheSinglePlayer() {
/*  98 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public void handleClientGameUpdate() {}
/*     */   
/*     */ 
/*     */   public void handleClientLogin() {}
/*     */   
/*     */ 
/*     */   public void initNetwork()
/*     */   {
/* 110 */     Mill.millChannel.register(new ServerReceiver());
/*     */   }
/*     */   
/*     */   public boolean isTrueClient() {
/* 114 */     return Mill.serverWorlds.isEmpty();
/*     */   }
/*     */   
/*     */   public boolean isTrueServer() {
/* 118 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   public void loadKeyDefaultSettings() {}
/*     */   
/*     */   public int loadKeySetting(String value)
/*     */   {
/* 126 */     return 0;
/*     */   }
/*     */   
/*     */   public void loadLanguages() {
/* 130 */     MLN.loadLanguages(null);
/*     */   }
/*     */   
/*     */ 
/*     */   public void localTranslatedSentence(EntityPlayer player, char colour, String code, String... values) {}
/*     */   
/*     */   public String logPrefix()
/*     */   {
/* 138 */     return "SRV ";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void preloadTextures() {}
/*     */   
/*     */ 
/*     */ 
/*     */   public void refreshClientResources() {}
/*     */   
/*     */ 
/*     */ 
/*     */   public void registerForgeClientClasses() {}
/*     */   
/*     */ 
/*     */   public void registerGraphics() {}
/*     */   
/*     */ 
/*     */   public void registerTileEntities()
/*     */   {
/* 159 */     GameRegistry.registerTileEntity(TileEntityMillChest.class, "ml_TileEntityBuilding");
/* 160 */     GameRegistry.registerTileEntity(TileEntityPanel.class, "ml_TileEntityPanel");
/*     */   }
/*     */   
/*     */   public void sendChatAdmin(String s) {}
/*     */   
/*     */   public void sendChatAdmin(String s, EnumChatFormatting colour) {}
/*     */   
/*     */   public void sendLocalChat(EntityPlayer player, char colour, String s) {}
/*     */   
/*     */   public void setTextureIds() {}
/*     */   
/*     */   public void testTextureSize() {}
/*     */   
/*     */   public void updateBowIcon(Goods.ItemMillenaireBow bow, EntityPlayer entityplayer) {}
/*     */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\forge\CommonProxy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */