/*    */ package org.millenaire.common.item;
/*    */ 
/*    */ import net.minecraft.block.Block;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.init.Blocks;
/*    */ import net.minecraft.item.ItemStack;
/*    */ import net.minecraft.world.IBlockAccess;
/*    */ import net.minecraft.world.World;
/*    */ import net.minecraftforge.common.EnumPlantType;
/*    */ import net.minecraftforge.common.IPlantable;
/*    */ import org.millenaire.common.MLN;
/*    */ import org.millenaire.common.MillWorld;
/*    */ import org.millenaire.common.UserProfile;
/*    */ import org.millenaire.common.core.MillCommonUtilities;
/*    */ import org.millenaire.common.forge.Mill;
/*    */ import org.millenaire.common.forge.MillAchievements;
/*    */ import org.millenaire.common.network.ServerSender;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ItemMillSeeds
/*    */   extends Goods.ItemText
/*    */   implements IPlantable
/*    */ {
/*    */   public final Block crop;
/*    */   public final String cropKey;
/*    */   
/*    */   public ItemMillSeeds(String iconName, Block j, String cropKey)
/*    */   {
/* 34 */     super(iconName);
/* 35 */     this.crop = j;
/* 36 */     this.cropKey = cropKey;
/* 37 */     func_77637_a(Mill.tabMillenaire);
/*    */   }
/*    */   
/*    */   public Block getPlant(IBlockAccess world, int x, int y, int z)
/*    */   {
/* 42 */     return this.crop;
/*    */   }
/*    */   
/*    */   public int getPlantMetadata(IBlockAccess world, int x, int y, int i)
/*    */   {
/* 47 */     return 0;
/*    */   }
/*    */   
/*    */   public EnumPlantType getPlantType(IBlockAccess world, int x, int y, int z)
/*    */   {
/* 52 */     return EnumPlantType.Crop;
/*    */   }
/*    */   
/*    */ 
/*    */   public boolean func_77648_a(ItemStack itemstack, EntityPlayer entityplayer, World world, int i, int j, int k, int l, float hitX, float hitY, float hitZ)
/*    */   {
/* 58 */     if (l != 1) {
/* 59 */       return false;
/*    */     }
/* 61 */     if ((!entityplayer.func_82247_a(i, j, k, l, itemstack)) || (!entityplayer.func_82247_a(i, j + 1, k, l, itemstack))) {
/* 62 */       return false;
/*    */     }
/* 64 */     UserProfile profile = Mill.getMillWorld(world).getProfile(entityplayer.getDisplayName());
/* 65 */     if ((!profile.isTagSet("cropplanting_" + this.cropKey)) && (!MLN.DEV)) {
/* 66 */       if (!world.field_72995_K) {
/* 67 */         ServerSender.sendTranslatedSentence(entityplayer, 'f', "ui.cropplantingknowledge", new String[] { "item." + this.cropKey });
/*    */       }
/* 69 */       return false;
/*    */     }
/* 71 */     Block block = world.func_147439_a(i, j, k);
/* 72 */     if ((block == Blocks.field_150458_ak) && (world.func_147437_c(i, j + 1, k))) {
/* 73 */       MillCommonUtilities.setBlockAndMetadata(world, i, j + 1, k, this.crop, 0, true, false);
/* 74 */       itemstack.field_77994_a -= 1;
/* 75 */       if (!world.field_72995_K) {
/* 76 */         entityplayer.func_71064_a(MillAchievements.masterfarmer, 1);
/*    */       }
/* 78 */       return true;
/*    */     }
/* 80 */     return false;
/*    */   }
/*    */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\item\ItemMillSeeds.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */