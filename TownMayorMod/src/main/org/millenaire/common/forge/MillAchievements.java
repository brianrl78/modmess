/*    */ package org.millenaire.common.forge;
/*    */ 
/*    */ import net.minecraft.block.Block;
/*    */ import net.minecraft.init.Items;
/*    */ import net.minecraft.item.Item;
/*    */ import net.minecraft.item.ItemStack;
/*    */ import net.minecraft.stats.Achievement;
/*    */ import net.minecraft.util.ChatComponentText;
/*    */ import net.minecraftforge.common.AchievementPage;
/*    */ import org.millenaire.common.MLN;
/*    */ 
/*    */ public class MillAchievements extends Achievement
/*    */ {
/*    */   final String key;
/*    */   
/*    */   public static class MillAchievementPage extends AchievementPage
/*    */   {
/*    */     public MillAchievementPage(String name, Achievement... achievements)
/*    */     {
/* 20 */       super(achievements);
/*    */     }
/*    */     
/*    */     public String getName()
/*    */     {
/* 25 */       return MLN.string("achievementpage.name");
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/* 32 */   public static final Achievement firstContact = new MillAchievements("firstcontact", 0, 0, Mill.parchmentVillagers, null).func_75971_g();
/*    */   
/* 34 */   public static final Achievement cresus = new MillAchievements("cresus", 2, -1, Mill.denier_or, firstContact).func_75971_g();
/*    */   
/* 36 */   public static final Achievement summoningwand = new MillAchievements("summoningwand", 5, 0, Mill.summoningWand, cresus).func_75971_g();
/*    */   
/* 38 */   public static final Achievement villageleader = new MillAchievements("villageleader", 4, 2, Mill.normanHelmet, summoningwand).func_75987_b().func_75971_g();
/*    */   
/* 40 */   public static final Achievement thequest = new MillAchievements("thequest", 0, -4, net.minecraft.init.Blocks.field_150478_aa, firstContact).func_75971_g();
/*    */   
/* 42 */   public static final Achievement maitreapenser = new MillAchievements("maitreapenser", 2, -5, Items.field_151099_bA, thequest).func_75971_g();
/*    */   
/* 44 */   public static final Achievement forbiddenknwoledge = new MillAchievements("forbiddenknwoledge", 2, -7, Mill.parchmentSadhu, maitreapenser).func_75987_b().func_75971_g();
/*    */   
/* 46 */   public static final Achievement puja = new MillAchievements("puja", -1, -6, Mill.indianstatue, maitreapenser).func_75971_g();
/*    */   
/* 48 */   public static final Achievement explorer = new MillAchievements("explorer", -3, 1, Items.field_151021_T, firstContact).func_75971_g();
/*    */   
/* 50 */   public static final Achievement marcopolo = new MillAchievements("marcopolo", -4, 3, Items.field_151148_bJ, explorer).func_75971_g();
/*    */   
/* 52 */   public static final Achievement magellan = new MillAchievements("magellan", -6, 5, Items.field_151111_aL, marcopolo).func_75987_b().func_75971_g();
/*    */   
/* 54 */   public static final Achievement selfdefense = new MillAchievements("selfdefense", -5, 2, Mill.byzantinePlate, explorer).func_75971_g();
/*    */   
/* 56 */   public static final Achievement pantheon = new MillAchievements("pantheon", -4, -5, Items.field_151155_ap, explorer).func_75971_g();
/*    */   
/* 58 */   public static final Achievement darkside = new MillAchievements("darkside", 0, 3, Items.field_151052_q, firstContact).func_75971_g();
/*    */   
/* 60 */   public static final Achievement scipio = new MillAchievements("scipio", -1, 6, Items.field_151040_l, darkside).func_75971_g();
/*    */   
/* 62 */   public static final Achievement attila = new MillAchievements("attila", 2, 9, Mill.normanBroadsword, scipio).func_75987_b().func_75971_g();
/*    */   
/* 64 */   public static final Achievement cheers = new MillAchievements("cheers", 2, 2, Mill.calva, firstContact).func_75971_g();
/*    */   
/* 66 */   public static final Achievement hired = new MillAchievements("hired", -1, -2, Mill.normanAxe, firstContact).func_75971_g();
/*    */   
/* 68 */   public static final Achievement masterfarmer = new MillAchievements("masterfarmer", 3, -3, Mill.grapes, firstContact).func_75971_g();
/*    */   
/* 70 */   public static MillAchievementPage millAchievements = new MillAchievementPage("", new Achievement[] { firstContact, cresus, summoningwand, villageleader, thequest, maitreapenser, forbiddenknwoledge, puja, explorer, marcopolo, magellan, selfdefense, pantheon, darkside, scipio, attila, cheers, hired, masterfarmer });
/*    */   
/*    */   public MillAchievements(String par2Str, int par3, int par4, Block par5Block, Achievement par6Achievement)
/*    */   {
/* 74 */     super("achievement.millenaire" + par2Str, par2Str, par3, par4, par5Block, par6Achievement);
/* 75 */     this.key = par2Str;
/*    */   }
/*    */   
/*    */   public MillAchievements(String par2Str, int par3, int par4, Item par5Item, Achievement par6Achievement) {
/* 79 */     super("achievement.millenaire" + par2Str, par2Str, par3, par4, par5Item, par6Achievement);
/* 80 */     this.key = par2Str;
/*    */   }
/*    */   
/*    */   public MillAchievements(String par2Str, int par3, int par4, ItemStack par5ItemStack, Achievement par6Achievement) {
/* 84 */     super("achievement.millenaire" + par2Str, par2Str, par3, par4, par5ItemStack, par6Achievement);
/* 85 */     this.key = par2Str;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public net.minecraft.util.IChatComponent func_150951_e()
/*    */   {
/* 92 */     return new ChatComponentText(MLN.string("achievement." + this.key + ".name"));
/*    */   }
/*    */   
/*    */   public String func_75989_e()
/*    */   {
/* 97 */     return MLN.string("achievement." + this.key + ".desc");
/*    */   }
/*    */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\forge\MillAchievements.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */