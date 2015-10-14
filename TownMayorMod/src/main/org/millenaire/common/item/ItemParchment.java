/*     */ package org.millenaire.common.item;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.chunk.Chunk;
/*     */ import org.millenaire.client.gui.DisplayActions;
/*     */ import org.millenaire.common.MLN;
/*     */ import org.millenaire.common.MillWorld;
/*     */ import org.millenaire.common.Point;
/*     */ import org.millenaire.common.building.Building;
/*     */ import org.millenaire.common.core.MillCommonUtilities.VillageList;
/*     */ import org.millenaire.common.forge.CommonProxy;
/*     */ import org.millenaire.common.forge.Mill;
/*     */ import org.millenaire.common.network.ServerSender;
/*     */ 
/*     */ public class ItemParchment
/*     */   extends Goods.ItemText
/*     */ {
/*     */   public static final int villagers = 1;
/*     */   public static final int buildings = 2;
/*     */   public static final int items = 3;
/*     */   public static final int villageBook = 4;
/*     */   public static final int indianVillagers = 5;
/*     */   public static final int indianBuildings = 6;
/*     */   public static final int indianItems = 7;
/*     */   public static final int mayanVillagers = 9;
/*     */   public static final int mayanBuildings = 10;
/*     */   public static final int mayanItems = 11;
/*     */   public static final int japaneseVillagers = 16;
/*     */   public static final int japaneseBuildings = 17;
/*     */   public static final int japaneseItems = 18;
/*     */   public static final int sadhu = 15;
/*     */   private final int[] textsId;
/*     */   
/*     */   public ItemParchment(String iconName, int t)
/*     */   {
/*  40 */     this(iconName, new int[] { t });
/*  41 */     func_77637_a(Mill.tabMillenaire);
/*     */   }
/*     */   
/*     */   public ItemParchment(String iconName, int[] tIds) {
/*  45 */     super(iconName);
/*  46 */     this.textsId = tIds;
/*  47 */     this.field_77777_bU = 1;
/*     */   }
/*     */   
/*     */   private void displayVillageBook(EntityPlayer player, ItemStack is)
/*     */   {
/*  52 */     if (player.field_70170_p.field_72995_K) {
/*  53 */       return;
/*     */     }
/*     */     
/*  56 */     if (is.func_77960_j() >= Mill.getMillWorld(player.field_70170_p).villagesList.pos.size()) {
/*  57 */       ServerSender.sendTranslatedSentence(player, '6', "panels.invalidid", new String[0]);
/*  58 */       return;
/*     */     }
/*     */     
/*  61 */     Point p = (Point)Mill.getMillWorld(player.field_70170_p).villagesList.pos.get(is.func_77960_j());
/*     */     
/*  63 */     Chunk chunk = player.field_70170_p.func_72964_e(p.getChunkX(), p.getChunkZ());
/*     */     
/*  65 */     if (!chunk.field_76636_d) {
/*  66 */       ServerSender.sendTranslatedSentence(player, '6', "panels.toofar", new String[0]);
/*  67 */       return;
/*     */     }
/*     */     
/*  70 */     Building townHall = Mill.getMillWorld(player.field_70170_p).getBuilding(p);
/*     */     
/*  72 */     if (townHall == null) {
/*  73 */       ServerSender.sendTranslatedSentence(player, '6', "panels.recordsnotloaded", new String[0]);
/*  74 */       return;
/*     */     }
/*     */     
/*  77 */     if (!townHall.isActive) {
/*  78 */       ServerSender.sendTranslatedSentence(player, '6', "panels.toofar", new String[0]);
/*  79 */       return;
/*     */     }
/*     */     
/*  82 */     ServerSender.displayVillageBookGUI(player, p);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public ItemStack func_77659_a(ItemStack itemstack, World world, EntityPlayer entityplayer)
/*     */   {
/*  89 */     if (this.textsId[0] == 4) {
/*  90 */       if ((!world.field_72995_K) && (this.textsId[0] == 4))
/*     */       {
/*  92 */         displayVillageBook(entityplayer, itemstack);
/*  93 */         return itemstack;
/*     */       }
/*  95 */       return itemstack;
/*     */     }
/*     */     
/*     */ 
/*  99 */     if (world.field_72995_K) {
/* 100 */       if (this.textsId.length == 1) {
/* 101 */         List<List<String>> parchment = MLN.getParchment(this.textsId[0]);
/* 102 */         if (parchment != null) {
/* 103 */           DisplayActions.displayParchmentPanelGUI(entityplayer, parchment, null, 0, true);
/*     */         } else {
/* 105 */           Mill.proxy.localTranslatedSentence(entityplayer, '6', "panels.notextfound", new String[] { "" + this.textsId[0] });
/*     */         }
/*     */       }
/*     */       else {
/* 109 */         List<List<String>> combinedText = new ArrayList();
/*     */         
/* 111 */         for (int i = 0; i < this.textsId.length; i++) {
/* 112 */           List<List<String>> parchment = MLN.getParchment(this.textsId[i]);
/* 113 */           if (parchment != null) {
/* 114 */             for (List<String> page : parchment) {
/* 115 */               combinedText.add(page);
/*     */             }
/*     */           }
/*     */         }
/* 119 */         DisplayActions.displayParchmentPanelGUI(entityplayer, combinedText, null, 0, true);
/*     */       }
/*     */     }
/*     */     
/* 123 */     return itemstack;
/*     */   }
/*     */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\item\ItemParchment.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */