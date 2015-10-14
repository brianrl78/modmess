/*      */ package org.millenaire.common.item;
/*      */ 
/*      */ import cpw.mods.fml.relauncher.Side;
/*      */ import java.io.BufferedReader;
/*      */ import java.io.BufferedWriter;
/*      */ import java.io.File;
/*      */ import java.io.IOException;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collections;
/*      */ import java.util.HashMap;
/*      */ import java.util.LinkedHashMap;
/*      */ import java.util.List;
/*      */ import java.util.Random;
/*      */ import net.minecraft.block.Block;
/*      */ import net.minecraft.client.renderer.texture.IIconRegister;
/*      */ import net.minecraft.creativetab.CreativeTabs;
/*      */ import net.minecraft.enchantment.Enchantment;
/*      */ import net.minecraft.enchantment.EnchantmentHelper;
/*      */ import net.minecraft.entity.Entity;
/*      */ import net.minecraft.entity.player.EntityPlayer;
/*      */ import net.minecraft.entity.player.InventoryPlayer;
/*      */ import net.minecraft.entity.projectile.EntityArrow;
/*      */ import net.minecraft.init.Blocks;
/*      */ import net.minecraft.init.Items;
/*      */ import net.minecraft.item.Item;
/*      */ import net.minecraft.item.Item.ToolMaterial;
/*      */ import net.minecraft.item.ItemArmor;
/*      */ import net.minecraft.item.ItemArmor.ArmorMaterial;
/*      */ import net.minecraft.item.ItemAxe;
/*      */ import net.minecraft.item.ItemHoe;
/*      */ import net.minecraft.item.ItemPickaxe;
/*      */ import net.minecraft.item.ItemSpade;
/*      */ import net.minecraft.item.ItemStack;
/*      */ import net.minecraft.item.ItemSword;
/*      */ import net.minecraft.util.IIcon;
/*      */ import net.minecraft.util.MathHelper;
/*      */ import net.minecraft.util.RegistryNamespaced;
/*      */ import net.minecraft.util.ResourceLocation;
/*      */ import net.minecraft.world.World;
/*      */ import net.minecraftforge.common.MinecraftForge;
/*      */ import net.minecraftforge.event.entity.player.ArrowLooseEvent;
/*      */ import org.millenaire.client.network.ClientSender;
/*      */ import org.millenaire.common.EntityMillDecoration;
/*      */ import org.millenaire.common.InvItem;
/*      */ import org.millenaire.common.MLN;
/*      */ import org.millenaire.common.MLN.MillenaireException;
/*      */ import org.millenaire.common.MillVillager;
/*      */ import org.millenaire.common.MillWorld;
/*      */ import org.millenaire.common.Point;
/*      */ import org.millenaire.common.VillageType;
/*      */ import org.millenaire.common.building.Building;
/*      */ import org.millenaire.common.core.MillCommonUtilities;
/*      */ import org.millenaire.common.core.MillCommonUtilities.VillageList;
/*      */ import org.millenaire.common.forge.CommonProxy;
/*      */ import org.millenaire.common.forge.Mill;
/*      */ import org.millenaire.common.network.ServerSender;
/*      */ 
/*      */ public class Goods
/*      */ {
/*      */   public static abstract interface IItemInitialEnchantmens
/*      */   {
/*      */     public abstract void applyEnchantments(ItemStack paramItemStack);
/*      */   }
/*      */   
/*      */   public static class ItemAmuletAlchemist extends Item
/*      */   {
/*      */     public final String baseIconName;
/*      */     
/*      */     public ItemAmuletAlchemist(String iconName)
/*      */     {
/*   71 */       func_77637_a(Mill.tabMillenaire);
/*   72 */       this.baseIconName = iconName;
/*      */     }
/*      */     
/*      */     public void func_94581_a(IIconRegister iconRegister)
/*      */     {
/*   77 */       Mill.proxy.declareAmuletTextures(iconRegister);
/*   78 */       this.field_77791_bV = iconRegister.func_94245_a("millenaire:" + this.baseIconName + MLN.getTextSuffix());
/*      */     }
/*      */   }
/*      */   
/*      */   public static class ItemAmuletSkollHati extends Goods.ItemText
/*      */   {
/*      */     public ItemAmuletSkollHati(String iconName) {
/*   85 */       super();
/*      */     }
/*      */     
/*      */ 
/*      */     public ItemStack func_77659_a(ItemStack itemstack, World world, EntityPlayer entityplayer)
/*      */     {
/*   91 */       if (MLN.LogOther >= 3) {
/*   92 */         MLN.debug(this, "Using skoll amulet.");
/*      */       }
/*      */       
/*   95 */       if (world.field_72995_K) {
/*   96 */         return itemstack;
/*      */       }
/*      */       
/*   99 */       long time = world.func_72820_D() + 24000L;
/*      */       
/*  101 */       if ((time % 24000L > 11000L) && (time % 24000L < 23500L)) {
/*  102 */         world.func_72877_b(time - time % 24000L - 500L);
/*      */       } else {
/*  104 */         world.func_72877_b(time - time % 24000L + 13000L);
/*      */       }
/*      */       
/*  107 */       if (!MLN.infiniteAmulet) {
/*  108 */         itemstack.func_77972_a(1, entityplayer);
/*      */       }
/*      */       
/*  111 */       return itemstack;
/*      */     }
/*      */   }
/*      */   
/*      */   public static class ItemAmuletVishnu extends Item
/*      */   {
/*      */     public final String baseIconName;
/*      */     
/*      */     public ItemAmuletVishnu(String iconName)
/*      */     {
/*  121 */       func_77637_a(Mill.tabMillenaire);
/*  122 */       this.baseIconName = iconName;
/*      */     }
/*      */     
/*      */ 
/*      */     public void func_94581_a(IIconRegister iconRegister)
/*      */     {
/*  128 */       Mill.proxy.declareAmuletTextures(iconRegister);
/*  129 */       this.field_77791_bV = iconRegister.func_94245_a("millenaire:" + this.baseIconName + MLN.getTextSuffix());
/*      */     }
/*      */   }
/*      */   
/*      */   public static class ItemAmuletYddrasil
/*      */     extends Item
/*      */   {
/*      */     public final String baseIconName;
/*      */     
/*      */     public ItemAmuletYddrasil(String iconName)
/*      */     {
/*  140 */       func_77637_a(Mill.tabMillenaire);
/*  141 */       this.baseIconName = iconName;
/*      */     }
/*      */     
/*      */     public void func_94581_a(IIconRegister iconRegister)
/*      */     {
/*  146 */       Mill.proxy.declareAmuletTextures(iconRegister);
/*  147 */       this.field_77791_bV = iconRegister.func_94245_a("millenaire:" + this.baseIconName + MLN.getTextSuffix());
/*      */     }
/*      */   }
/*      */   
/*      */   public static class ItemBrickMould extends Goods.ItemText
/*      */   {
/*      */     public ItemBrickMould(String iconName) {
/*  154 */       super();
/*  155 */       func_77637_a(Mill.tabMillenaire);
/*      */     }
/*      */     
/*      */     public boolean onItemUseFirst(ItemStack itemstack, EntityPlayer entityplayer, World world, int i, int j, int k, int l, float hitX, float hitY, float hitZ)
/*      */     {
/*  160 */       if (world.func_147439_a(i, j, k) == Blocks.field_150433_aE) {
/*  161 */         l = 0;
/*      */       } else {
/*  163 */         if (l == 0) {
/*  164 */           j--;
/*      */         }
/*  166 */         if (l == 1) {
/*  167 */           j++;
/*      */         }
/*  169 */         if (l == 2) {
/*  170 */           k--;
/*      */         }
/*  172 */         if (l == 3) {
/*  173 */           k++;
/*      */         }
/*  175 */         if (l == 4) {
/*  176 */           i--;
/*      */         }
/*  178 */         if (l == 5) {
/*  179 */           i++;
/*      */         }
/*      */       }
/*      */       
/*  183 */       if (world.func_147439_a(i, j, k) != Blocks.field_150350_a) {
/*  184 */         return false;
/*      */       }
/*      */       
/*  187 */       if ((MillCommonUtilities.countChestItems(entityplayer.field_71071_by, Blocks.field_150346_d, 0) == 0) || (MillCommonUtilities.countChestItems(entityplayer.field_71071_by, Blocks.field_150354_m, 0) == 0))
/*      */       {
/*  189 */         if (!world.field_72995_K) {
/*  190 */           ServerSender.sendTranslatedSentence(entityplayer, 'f', "ui.brickinstructions", new String[0]);
/*      */         }
/*  192 */         return false;
/*      */       }
/*      */       
/*  195 */       MillCommonUtilities.getItemsFromChest(entityplayer.field_71071_by, Blocks.field_150346_d, 0, 1);
/*  196 */       MillCommonUtilities.getItemsFromChest(entityplayer.field_71071_by, Blocks.field_150354_m, 0, 1);
/*      */       
/*  198 */       MillCommonUtilities.setBlockAndMetadata(world, i, j, k, Mill.earth_decoration, 0, true, false);
/*      */       
/*  200 */       itemstack.func_77972_a(1, entityplayer);
/*      */       
/*  202 */       return false;
/*      */     }
/*      */   }
/*      */   
/*      */   public static class ItemClothes
/*      */     extends Item
/*      */   {
/*      */     public final String[] iconNames;
/*  210 */     public IIcon[] icons = null;
/*      */     
/*      */     public ItemClothes(String... iconNames)
/*      */     {
/*  214 */       func_77627_a(true);
/*  215 */       func_77656_e(0);
/*  216 */       this.iconNames = iconNames;
/*  217 */       func_77637_a(Mill.tabMillenaire);
/*      */     }
/*      */     
/*      */     public String getClothName(int meta) {
/*  221 */       if (meta == 0) {
/*  222 */         return "clothes_byz_wool";
/*      */       }
/*  224 */       return "clothes_byz_silk";
/*      */     }
/*      */     
/*      */     public int getClothPriority(int meta)
/*      */     {
/*  229 */       if (meta == 0) {
/*  230 */         return 1;
/*      */       }
/*  232 */       return 2;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     public IIcon func_77617_a(int meta)
/*      */     {
/*  239 */       if (meta < this.iconNames.length) {
/*  240 */         return this.icons[meta];
/*      */       }
/*      */       
/*  243 */       return this.icons[0];
/*      */     }
/*      */     
/*      */ 
/*      */     @cpw.mods.fml.relauncher.SideOnly(Side.CLIENT)
/*      */     public void func_150895_a(Item item, CreativeTabs par2CreativeTabs, List par3List)
/*      */     {
/*  250 */       for (int var4 = 0; var4 < 2; var4++) {
/*  251 */         par3List.add(new ItemStack(item, 1, var4));
/*      */       }
/*      */     }
/*      */     
/*      */     public String func_77667_c(ItemStack par1ItemStack)
/*      */     {
/*  257 */       int meta = MathHelper.func_76125_a(par1ItemStack.func_77960_j(), 0, 15);
/*      */       
/*  259 */       return "item." + getClothName(meta);
/*      */     }
/*      */     
/*      */ 
/*      */     public void func_94581_a(IIconRegister iconRegister)
/*      */     {
/*  265 */       this.icons = new IIcon[this.iconNames.length];
/*      */       
/*  267 */       for (int i = 0; i < this.iconNames.length; i++) {
/*  268 */         this.icons[i] = MillCommonUtilities.getIcon(iconRegister, this.iconNames[i]);
/*      */       }
/*  270 */       this.field_77791_bV = MillCommonUtilities.getIcon(iconRegister, this.iconNames[0]);
/*      */     }
/*      */   }
/*      */   
/*      */   public static class ItemMayanQuestCrown extends ItemArmor implements Goods.IItemInitialEnchantmens
/*      */   {
/*  276 */     private static final ResourceLocation mayan1 = new ResourceLocation("millenaire", "textures/models/armor/ML_mayan_quest_1.png");
/*      */     public final String iconName;
/*      */     
/*      */     public ItemMayanQuestCrown(String iconName, int armourId, int type)
/*      */     {
/*  281 */       super(armourId, type);
/*  282 */       func_77656_e(0);
/*  283 */       this.iconName = iconName;
/*  284 */       func_77637_a(Mill.tabMillenaire);
/*      */     }
/*      */     
/*      */     public void applyEnchantments(ItemStack stack)
/*      */     {
/*  289 */       if (EnchantmentHelper.func_77506_a(Enchantment.field_77340_h.field_77352_x, stack) == 0) {
/*  290 */         stack.func_77966_a(Enchantment.field_77340_h, 3);
/*      */       }
/*  292 */       if (EnchantmentHelper.func_77506_a(Enchantment.field_77341_i.field_77352_x, stack) == 0) {
/*  293 */         stack.func_77966_a(Enchantment.field_77341_i, 1);
/*      */       }
/*  295 */       if (EnchantmentHelper.func_77506_a(Enchantment.field_77332_c.field_77352_x, stack) == 0) {
/*  296 */         stack.func_77966_a(Enchantment.field_77332_c, 4);
/*      */       }
/*      */     }
/*      */     
/*      */     public String getArmorTexture(ItemStack par1, Entity entity, int slot, String type)
/*      */     {
/*  302 */       return mayan1.toString();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     public boolean func_77648_a(ItemStack stack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10)
/*      */     {
/*  309 */       applyEnchantments(stack);
/*      */       
/*  311 */       return super.func_77648_a(stack, par2EntityPlayer, par3World, par4, par5, par6, par7, par8, par9, par10);
/*      */     }
/*      */     
/*      */ 
/*      */     public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
/*      */     {
/*  317 */       applyEnchantments(stack);
/*  318 */       return super.onItemUseFirst(stack, player, world, x, y, z, side, hitX, hitY, hitZ);
/*      */     }
/*      */     
/*      */     public void func_77663_a(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5)
/*      */     {
/*  323 */       applyEnchantments(par1ItemStack);
/*  324 */       super.func_77663_a(par1ItemStack, par2World, par3Entity, par4, par5);
/*      */     }
/*      */     
/*      */     public void func_94581_a(IIconRegister iconRegister)
/*      */     {
/*  329 */       this.field_77791_bV = MillCommonUtilities.getIcon(iconRegister, this.iconName);
/*      */     }
/*      */   }
/*      */   
/*      */   public static class ItemMillenaireArmour
/*      */     extends ItemArmor
/*      */   {
/*  336 */     private static final ResourceLocation norman1 = new ResourceLocation("millenaire", "textures/models/armor/ML_norman_1.png");
/*  337 */     private static final ResourceLocation norman2 = new ResourceLocation("millenaire", "textures/models/armor/ML_norman_2.png");
/*      */     
/*  339 */     private static final ResourceLocation japaneseGuard1 = new ResourceLocation("millenaire", "textures/models/armor/ML_japanese_guard_1.png");
/*  340 */     private static final ResourceLocation japaneseGuard2 = new ResourceLocation("millenaire", "textures/models/armor/ML_japanese_guard_2.png");
/*      */     
/*  342 */     private static final ResourceLocation japaneseWarriorBlue1 = new ResourceLocation("millenaire", "textures/models/armor/ML_japanese_warrior_blue_1.png");
/*  343 */     private static final ResourceLocation japaneseWarriorBlue2 = new ResourceLocation("millenaire", "textures/models/armor/ML_japanese_warrior_blue_2.png");
/*      */     
/*  345 */     private static final ResourceLocation japaneseWarriorRed1 = new ResourceLocation("millenaire", "textures/models/armor/ML_japanese_warrior_red_1.png");
/*  346 */     private static final ResourceLocation japaneseWarriorRed2 = new ResourceLocation("millenaire", "textures/models/armor/ML_japanese_warrior_red_2.png");
/*      */     
/*  348 */     private static final ResourceLocation byzantine1 = new ResourceLocation("millenaire", "textures/models/armor/ML_byzantine_1.png");
/*  349 */     private static final ResourceLocation byzantine2 = new ResourceLocation("millenaire", "textures/models/armor/ML_byzantine_2.png");
/*      */     public final String iconName;
/*      */     
/*      */     public ItemMillenaireArmour(String iconName, ItemArmor.ArmorMaterial material, int armourId, int type)
/*      */     {
/*  354 */       super(armourId, type);
/*  355 */       this.iconName = iconName;
/*  356 */       func_77637_a(Mill.tabMillenaire);
/*      */     }
/*      */     
/*      */     public String getArmorTexture(ItemStack par1, Entity entity, int slot, String type)
/*      */     {
/*  361 */       if ((par1.func_77973_b() == Mill.normanHelmet) || (par1.func_77973_b() == Mill.normanPlate) || (par1.func_77973_b() == Mill.normanBoots)) {
/*  362 */         return norman1.toString();
/*      */       }
/*  364 */       if (par1.func_77973_b() == Mill.normanLegs) {
/*  365 */         return norman2.toString();
/*      */       }
/*      */       
/*  368 */       if ((par1.func_77973_b() == Mill.japaneseGuardHelmet) || (par1.func_77973_b() == Mill.japaneseGuardPlate) || (par1.func_77973_b() == Mill.japaneseGuardBoots)) {
/*  369 */         return japaneseGuard1.toString();
/*      */       }
/*  371 */       if (par1.func_77973_b() == Mill.japaneseGuardLegs) {
/*  372 */         return japaneseGuard2.toString();
/*      */       }
/*      */       
/*  375 */       if ((par1.func_77973_b() == Mill.japaneseWarriorBlueHelmet) || (par1.func_77973_b() == Mill.japaneseWarriorBluePlate) || (par1.func_77973_b() == Mill.japaneseWarriorBlueBoots)) {
/*  376 */         return japaneseWarriorBlue1.toString();
/*      */       }
/*  378 */       if (par1.func_77973_b() == Mill.japaneseWarriorBlueLegs) {
/*  379 */         return japaneseWarriorBlue2.toString();
/*      */       }
/*      */       
/*  382 */       if ((par1.func_77973_b() == Mill.japaneseWarriorRedHelmet) || (par1.func_77973_b() == Mill.japaneseWarriorRedPlate) || (par1.func_77973_b() == Mill.japaneseWarriorRedBoots)) {
/*  383 */         return japaneseWarriorRed1.toString();
/*      */       }
/*  385 */       if (par1.func_77973_b() == Mill.japaneseWarriorRedLegs) {
/*  386 */         return japaneseWarriorRed2.toString();
/*      */       }
/*      */       
/*  389 */       if ((par1.func_77973_b() == Mill.byzantineHelmet) || (par1.func_77973_b() == Mill.byzantinePlate) || (par1.func_77973_b() == Mill.byzantineBoots)) {
/*  390 */         return byzantine1.toString();
/*      */       }
/*  392 */       if (par1.func_77973_b() == Mill.byzantineLegs) {
/*  393 */         return byzantine2.toString();
/*      */       }
/*      */       
/*  396 */       return norman1.toString();
/*      */     }
/*      */     
/*      */     public void func_94581_a(IIconRegister iconRegister)
/*      */     {
/*  401 */       this.field_77791_bV = MillCommonUtilities.getIcon(iconRegister, this.iconName);
/*      */     }
/*      */   }
/*      */   
/*      */   public static class ItemMillenaireAxe extends ItemAxe
/*      */   {
/*      */     public final String iconName;
/*      */     
/*      */     public ItemMillenaireAxe(String iconName, Item.ToolMaterial material) {
/*  410 */       super();
/*  411 */       this.iconName = iconName;
/*  412 */       func_77637_a(Mill.tabMillenaire);
/*      */     }
/*      */     
/*      */     public void func_94581_a(IIconRegister iconRegister)
/*      */     {
/*  417 */       this.field_77791_bV = MillCommonUtilities.getIcon(iconRegister, this.iconName);
/*      */     }
/*      */   }
/*      */   
/*      */   public static class ItemMillenaireBow extends net.minecraft.item.ItemBow
/*      */   {
/*  423 */     public float speedFactor = 1.0F;
/*  424 */     public float damageBonus = 0.0F;
/*      */     public int iconPos;
/*      */     public final String[] iconNames;
/*      */     public IIcon[] icons;
/*      */     
/*      */     public ItemMillenaireBow(float speedFactor, float damageBonus, String... iconNames)
/*      */     {
/*  431 */       this.speedFactor = speedFactor;
/*  432 */       this.damageBonus = damageBonus;
/*  433 */       this.iconNames = iconNames;
/*  434 */       func_77637_a(Mill.tabMillenaire);
/*      */     }
/*      */     
/*      */     public IIcon func_77617_a(int par1)
/*      */     {
/*  439 */       return this.icons[this.iconPos];
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public void func_77615_a(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer, int par4)
/*      */     {
/*  451 */       int var6 = func_77626_a(par1ItemStack) - par4;
/*      */       
/*  453 */       ArrowLooseEvent event = new ArrowLooseEvent(par3EntityPlayer, par1ItemStack, var6);
/*  454 */       MinecraftForge.EVENT_BUS.post(event);
/*  455 */       if (event.isCanceled()) {
/*  456 */         return;
/*      */       }
/*  458 */       var6 = event.charge;
/*      */       
/*  460 */       boolean var5 = (par3EntityPlayer.field_71075_bZ.field_75098_d) || (EnchantmentHelper.func_77506_a(Enchantment.field_77342_w.field_77352_x, par1ItemStack) > 0);
/*      */       
/*  462 */       if ((var5) || (par3EntityPlayer.field_71071_by.func_146028_b(Items.field_151032_g))) {
/*  463 */         float var7 = var6 / 20.0F;
/*  464 */         var7 = (var7 * var7 + var7 * 2.0F) / 3.0F;
/*      */         
/*  466 */         if (var7 < 0.1D) {
/*  467 */           return;
/*      */         }
/*      */         
/*  470 */         if (var7 > 1.0F) {
/*  471 */           var7 = 1.0F;
/*      */         }
/*      */         
/*  474 */         EntityArrow var8 = new EntityArrow(par2World, par3EntityPlayer, var7 * 2.0F);
/*      */         
/*  476 */         if (var7 == 1.0F) {
/*  477 */           var8.func_70243_d(true);
/*      */         }
/*      */         
/*  480 */         int var9 = EnchantmentHelper.func_77506_a(Enchantment.field_77345_t.field_77352_x, par1ItemStack);
/*      */         
/*  482 */         if (var9 > 0) {
/*  483 */           var8.func_70239_b(var8.func_70242_d() + var9 * 0.5D + 0.5D);
/*      */         }
/*      */         
/*  486 */         int var10 = EnchantmentHelper.func_77506_a(Enchantment.field_77344_u.field_77352_x, par1ItemStack);
/*      */         
/*  488 */         if (var10 > 0) {
/*  489 */           var8.func_70240_a(var10);
/*      */         }
/*      */         
/*  492 */         if (EnchantmentHelper.func_77506_a(Enchantment.field_77343_v.field_77352_x, par1ItemStack) > 0) {
/*  493 */           var8.func_70015_d(100);
/*      */         }
/*      */         
/*  496 */         par1ItemStack.func_77972_a(1, par3EntityPlayer);
/*  497 */         par2World.func_72956_a(par3EntityPlayer, "random.bow", 1.0F, 1.0F / (field_77697_d.nextFloat() * 0.4F + 1.2F) + var7 * 0.5F);
/*      */         
/*  499 */         if (var5) {
/*  500 */           var8.field_70251_a = 2;
/*      */         } else {
/*  502 */           par3EntityPlayer.field_71071_by.func_146026_a(Items.field_151032_g);
/*      */         }
/*      */         
/*      */ 
/*  506 */         var8.field_70159_w *= this.speedFactor;
/*  507 */         var8.field_70181_x *= this.speedFactor;
/*  508 */         var8.field_70179_y *= this.speedFactor;
/*      */         
/*      */ 
/*  511 */         var8.func_70239_b(var8.func_70242_d() + this.damageBonus);
/*      */         
/*  513 */         if (!par2World.field_72995_K) {
/*  514 */           par2World.func_72838_d(var8);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */     public void func_77663_a(ItemStack itemstack, World world, Entity entity, int i, boolean flag)
/*      */     {
/*  521 */       EntityPlayer entityplayer = (EntityPlayer)entity;
/*  522 */       Mill.proxy.updateBowIcon(this, entityplayer);
/*      */     }
/*      */     
/*      */     public void func_94581_a(IIconRegister iconRegister)
/*      */     {
/*  527 */       this.field_77791_bV = MillCommonUtilities.getIcon(iconRegister, this.iconNames[0]);
/*      */       
/*  529 */       this.icons = new IIcon[this.iconNames.length];
/*      */       
/*  531 */       for (int i = 0; i < this.iconNames.length; i++) {
/*  532 */         this.icons[i] = MillCommonUtilities.getIcon(iconRegister, this.iconNames[i]);
/*      */       }
/*      */     }
/*      */     
/*      */     public void setBowIcon(int pos) {
/*  537 */       this.iconPos = pos;
/*      */     }
/*      */   }
/*      */   
/*      */   public static class ItemMillenaireHoe extends ItemHoe
/*      */   {
/*      */     public final String iconName;
/*      */     
/*      */     public ItemMillenaireHoe(String iconName, Item.ToolMaterial material) {
/*  546 */       super();
/*  547 */       func_77637_a(Mill.tabMillenaire);
/*  548 */       this.iconName = iconName;
/*      */     }
/*      */     
/*      */     public void func_94581_a(IIconRegister iconRegister)
/*      */     {
/*  553 */       this.field_77791_bV = MillCommonUtilities.getIcon(iconRegister, this.iconName);
/*      */     }
/*      */   }
/*      */   
/*      */   public static class ItemMillenairePickaxe extends ItemPickaxe
/*      */   {
/*      */     public final String iconName;
/*      */     
/*      */     public ItemMillenairePickaxe(String iconName, Item.ToolMaterial material) {
/*  562 */       super();
/*  563 */       this.iconName = iconName;
/*  564 */       func_77637_a(Mill.tabMillenaire);
/*      */     }
/*      */     
/*      */     public void func_94581_a(IIconRegister iconRegister)
/*      */     {
/*  569 */       this.field_77791_bV = MillCommonUtilities.getIcon(iconRegister, this.iconName);
/*      */     }
/*      */   }
/*      */   
/*      */   public static class ItemMillenaireShovel extends ItemSpade
/*      */   {
/*      */     public final String iconName;
/*      */     
/*      */     public ItemMillenaireShovel(String iconName, Item.ToolMaterial material) {
/*  578 */       super();
/*      */       
/*  580 */       this.iconName = iconName;
/*      */       
/*  582 */       func_77637_a(Mill.tabMillenaire);
/*      */     }
/*      */     
/*      */     public void func_94581_a(IIconRegister iconRegister)
/*      */     {
/*  587 */       this.field_77791_bV = MillCommonUtilities.getIcon(iconRegister, this.iconName);
/*      */     }
/*      */   }
/*      */   
/*      */   public static class ItemMillenaireSword
/*      */     extends ItemSword implements Goods.IItemInitialEnchantmens
/*      */   {
/*      */     float criticalChance;
/*      */     int criticalMultiple;
/*      */     public final String iconName;
/*      */     boolean knockback;
/*      */     
/*      */     public ItemMillenaireSword(String iconName, Item.ToolMaterial material, float criticalChance, int criticalMultiple, boolean knockback)
/*      */     {
/*  601 */       super();
/*  602 */       this.criticalChance = criticalChance;
/*  603 */       this.criticalMultiple = criticalMultiple;
/*  604 */       this.knockback = knockback;
/*  605 */       this.iconName = iconName;
/*  606 */       func_77637_a(Mill.tabMillenaire);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public void applyEnchantments(ItemStack stack)
/*      */     {
/*  625 */       if ((this.knockback) && 
/*  626 */         (EnchantmentHelper.func_77506_a(Enchantment.field_77337_m.field_77352_x, stack) == 0)) {
/*  627 */         stack.func_77966_a(Enchantment.field_77337_m, 2);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     public void func_77622_d(ItemStack stack, World par2World, EntityPlayer par3EntityPlayer)
/*      */     {
/*  635 */       applyEnchantments(stack);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     public boolean func_77648_a(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10)
/*      */     {
/*  642 */       applyEnchantments(par1ItemStack);
/*  643 */       return super.func_77648_a(par1ItemStack, par2EntityPlayer, par3World, par4, par5, par6, par7, par8, par9, par10);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
/*      */     {
/*  650 */       applyEnchantments(stack);
/*  651 */       return super.onItemUseFirst(stack, player, world, x, y, z, side, hitX, hitY, hitZ);
/*      */     }
/*      */     
/*      */     public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity)
/*      */     {
/*  656 */       applyEnchantments(stack);
/*  657 */       return super.onLeftClickEntity(stack, player, entity);
/*      */     }
/*      */     
/*      */     public void func_94581_a(IIconRegister iconRegister)
/*      */     {
/*  662 */       this.field_77791_bV = MillCommonUtilities.getIcon(iconRegister, this.iconName);
/*      */     }
/*      */   }
/*      */   
/*      */   public static class ItemNegationWand extends Goods.ItemText
/*      */   {
/*      */     public ItemNegationWand(String iconName)
/*      */     {
/*  670 */       super();
/*  671 */       func_77637_a(Mill.tabMillenaire);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     public boolean onItemUseFirst(ItemStack itemstack, EntityPlayer entityplayer, World world, int x, int y, int z, int l, float hitX, float hitY, float hitZ)
/*      */     {
/*  678 */       Point pos = new Point(x, y, z);
/*      */       
/*  680 */       Block block = world.func_147439_a(x, y, z);
/*      */       
/*  682 */       if ((block == Blocks.field_150472_an) && (world.field_72995_K)) {
/*  683 */         org.millenaire.common.building.BuildingPlan.exportBuilding(entityplayer, world, pos);
/*  684 */         return true;
/*      */       }
/*      */       
/*  687 */       if (world.field_72995_K) {
/*  688 */         return false;
/*      */       }
/*      */       
/*  691 */       MillWorld mw = Mill.getMillWorld(world);
/*      */       
/*  693 */       for (int i = 0; i < 2; i++)
/*      */       {
/*      */         MillCommonUtilities.VillageList list;
/*      */         MillCommonUtilities.VillageList list;
/*  697 */         if (i == 0) {
/*  698 */           list = mw.loneBuildingsList;
/*      */         } else {
/*  700 */           list = mw.villagesList;
/*      */         }
/*      */         
/*  703 */         for (int j = 0; j < list.names.size(); j++)
/*      */         {
/*  705 */           Point p = (Point)list.pos.get(j);
/*      */           
/*  707 */           int distance = MathHelper.func_76128_c(p.horizontalDistanceTo(pos));
/*      */           
/*  709 */           if (distance <= 30)
/*      */           {
/*  711 */             Building th = mw.getBuilding(p);
/*      */             
/*  713 */             if ((th != null) && (th.isTownhall)) {
/*  714 */               if (th.chestLocked) {
/*  715 */                 ServerSender.sendTranslatedSentence(entityplayer, '6', "negationwand.villagelocked", new String[] { th.villageType.name });
/*  716 */                 return true;
/*      */               }
/*  718 */               ServerSender.displayNegationWandGUI(entityplayer, th);
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*  725 */       return false;
/*      */     }
/*      */   }
/*      */   
/*      */   public static class ItemSummoningWand
/*      */     extends Goods.ItemText
/*      */   {
/*      */     public ItemSummoningWand(String iconName)
/*      */     {
/*  734 */       super();
/*  735 */       func_77637_a(Mill.tabMillenaire);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     public boolean onItemUseFirst(ItemStack itemstack, EntityPlayer entityplayer, World world, int i, int j, int k, int l, float hitX, float hitY, float hitZ)
/*      */     {
/*  742 */       Point pos = new Point(i, j, k);
/*      */       
/*  744 */       Block block = world.func_147439_a(i, j, k);
/*      */       
/*  746 */       if (block == Blocks.field_150472_an) {
/*  747 */         ClientSender.importBuilding(entityplayer, pos);
/*  748 */         return true; }
/*  749 */       if (block == Mill.lockedChest) {
/*  750 */         return false;
/*      */       }
/*      */       
/*  753 */       ClientSender.summoningWandUse(entityplayer, pos);
/*      */       
/*  755 */       return true;
/*      */     }
/*      */   }
/*      */   
/*      */   public static class ItemTapestry extends Goods.ItemText
/*      */   {
/*      */     public int type;
/*      */     
/*      */     public ItemTapestry(String iconName, int type)
/*      */     {
/*  765 */       super();
/*  766 */       this.type = type;
/*  767 */       func_77637_a(Mill.tabMillenaire);
/*      */     }
/*      */     
/*      */ 
/*      */     public boolean func_77648_a(ItemStack itemstack, EntityPlayer entityplayer, World world, int i, int j, int k, int side, float par8, float par9, float par10)
/*      */     {
/*  773 */       if (side == 0) {
/*  774 */         return false;
/*      */       }
/*  776 */       if (side == 1) {
/*  777 */         return false;
/*      */       }
/*      */       
/*  780 */       int orientation = net.minecraft.util.Direction.field_71579_d[side];
/*      */       
/*  782 */       EntityMillDecoration entitypainting = new EntityMillDecoration(world, i, j, k, orientation, this.type, false);
/*  783 */       if (entitypainting.func_70518_d()) {
/*  784 */         if (!world.field_72995_K) {
/*  785 */           world.func_72838_d(entitypainting);
/*      */         }
/*  787 */         itemstack.field_77994_a -= 1;
/*      */       }
/*  789 */       return true;
/*      */     }
/*      */   }
/*      */   
/*      */   public static class ItemText extends Item
/*      */   {
/*      */     public final String iconName;
/*      */     
/*      */     public ItemText(String iconName)
/*      */     {
/*  799 */       func_77637_a(Mill.tabMillenaire);
/*  800 */       this.iconName = iconName;
/*  801 */       func_111206_d("millenaire:" + iconName);
/*      */     }
/*      */   }
/*      */   
/*  805 */   public static final List<InvItem> freeGoods = new ArrayList();
/*      */   
/*  807 */   public static final HashMap<String, InvItem> goodsName = new HashMap();
/*      */   public static final String BOUDIN = "boudin";
/*      */   public static final String TRIPES = "tripes";
/*      */   public static final String CALVA = "calva";
/*      */   public InvItem item;
/*      */   public String name;
/*      */   
/*  814 */   static { try { freeGoods.add(new InvItem(Blocks.field_150346_d, 0));
/*  815 */       freeGoods.add(new InvItem(Mill.earth_decoration, 1));
/*  816 */       freeGoods.add(new InvItem(Blocks.field_150355_j, 0));
/*  817 */       freeGoods.add(new InvItem(Blocks.field_150345_g, 0));
/*  818 */       freeGoods.add(new InvItem(Blocks.field_150327_N, 0));
/*  819 */       freeGoods.add(new InvItem(Blocks.field_150328_O, 0));
/*  820 */       freeGoods.add(new InvItem(Blocks.field_150329_H, 0));
/*  821 */       freeGoods.add(new InvItem(Blocks.field_150435_aG, 0));
/*  822 */       freeGoods.add(new InvItem(Blocks.field_150382_bo, 0));
/*  823 */       freeGoods.add(new InvItem(Blocks.field_150362_t, -1));
/*  824 */       freeGoods.add(new InvItem(Blocks.field_150345_g, -1));
/*  825 */       freeGoods.add(new InvItem(Blocks.field_150414_aQ, 0));
/*  826 */       freeGoods.add(new InvItem(Mill.path, -1));
/*  827 */       freeGoods.add(new InvItem(Mill.pathSlab, -1));
/*      */     } catch (MLN.MillenaireException e) {
/*  829 */       MLN.printException(e);
/*      */     }
/*      */   }
/*      */   
/*      */   public static void generateGoodsList()
/*      */   {
/*  835 */     File file = new File(Mill.proxy.getBaseDir(), "goods.txt");
/*      */     try
/*      */     {
/*  838 */       BufferedWriter writer = MillCommonUtilities.getWriter(file);
/*  839 */       writer.write("//Item key;item id;item meta;label (indicative only)" + MLN.EOL);
/*  840 */       writer.write("//This file is auto-generated and indicative only. Don't edit it." + MLN.EOL + MLN.EOL);
/*      */       
/*  842 */       List<String> names = new ArrayList(goodsName.keySet());
/*  843 */       Collections.sort(names);
/*      */       
/*  845 */       for (String name : names) {
/*  846 */         InvItem iv = (InvItem)goodsName.get(name);
/*  847 */         writer.write(name + ";" + Item.field_150901_e.func_148750_c(iv.item) + ";" + iv.meta + ";" + iv.getName() + MLN.EOL);
/*      */       }
/*  849 */       writer.close();
/*      */     }
/*      */     catch (Exception e) {
/*  852 */       MLN.error(null, "Error when writing goods list: ");
/*  853 */       MLN.printException(e);
/*      */     }
/*      */   }
/*      */   
/*      */   private static void loadGoodList(File file)
/*      */   {
/*      */     try {
/*  860 */       BufferedReader reader = MillCommonUtilities.getReader(file);
/*      */       
/*      */       String line;
/*      */       
/*  864 */       while ((line = reader.readLine()) != null) {
/*      */         try
/*      */         {
/*  867 */           if ((line.trim().length() > 0) && (!line.startsWith("//"))) {
/*  868 */             String[] temp = line.trim().split(";");
/*      */             
/*  870 */             if (temp.length > 2) {
/*  871 */               Item item = (Item)Item.field_150901_e.func_82594_a(temp[1]);
/*      */               
/*  873 */               if (item != null) {
/*  874 */                 goodsName.put(temp[0], new InvItem(item, Integer.parseInt(temp[2])));
/*      */               } else {
/*  876 */                 Block block = (Block)Block.field_149771_c.func_82594_a(temp[1]);
/*      */                 
/*  878 */                 if (block == null) {
/*  879 */                   MLN.error(null, "Could not load good: " + temp[1]);
/*      */                 }
/*  881 */                 else if (Item.func_150898_a(block) == null) {
/*  882 */                   MLN.error(null, "Tried to create good from block with no item: " + line);
/*      */                 } else {
/*  884 */                   goodsName.put(temp[0], new InvItem(block, Integer.parseInt(temp[2])));
/*      */                 }
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */         catch (Exception e) {
/*  891 */           MLN.printException("Exception while reading line: " + line, e);
/*      */         }
/*      */       }
/*      */     } catch (IOException e) {
/*  895 */       MLN.printException(e);
/*  896 */       return;
/*      */     }
/*      */   }
/*      */   
/*      */   public static void loadGoods()
/*      */   {
/*  902 */     for (File loadDir : Mill.loadingDirs)
/*      */     {
/*  904 */       File mainList = new File(loadDir, "itemlist.txt");
/*      */       
/*  906 */       if (mainList.exists()) {
/*  907 */         loadGoodList(mainList);
/*      */       }
/*      */     }
/*      */     
/*  911 */     generateGoodsList();
/*      */     try
/*      */     {
/*  914 */       goodsName.put("anyenchanted", new InvItem(1));
/*  915 */       goodsName.put("enchantedsword", new InvItem(2));
/*      */     } catch (MLN.MillenaireException e) {
/*  917 */       MLN.printException(e);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private final int sellingPrice;
/*      */   
/*      */ 
/*      */   private final int buyingPrice;
/*      */   
/*      */ 
/*      */   public int reservedQuantity;
/*      */   
/*      */ 
/*      */   public int targetQuantity;
/*      */   
/*      */   public int foreignMerchantPrice;
/*      */   
/*      */   public String requiredTag;
/*      */   
/*  938 */   public boolean autoGenerate = false;
/*      */   
/*      */   public int minReputation;
/*      */   
/*  942 */   public String desc = null;
/*      */   
/*      */   public Goods(InvItem iv)
/*      */   {
/*  946 */     this.item = iv;
/*  947 */     this.name = this.item.getName();
/*  948 */     this.sellingPrice = 0;
/*  949 */     this.buyingPrice = 1;
/*  950 */     this.requiredTag = null;
/*      */   }
/*      */   
/*      */   public Goods(String name, InvItem item, int sellingPrice, int buyingPrice, int reservedQuantity, int targetQuantity, int foreignMerchantPrice, boolean autoGenerate, String tag, int minReputation, String desc)
/*      */   {
/*  955 */     this.name = name;
/*  956 */     this.item = item;
/*  957 */     this.sellingPrice = sellingPrice;
/*  958 */     this.buyingPrice = buyingPrice;
/*  959 */     this.requiredTag = tag;
/*  960 */     this.autoGenerate = autoGenerate;
/*  961 */     this.reservedQuantity = reservedQuantity;
/*  962 */     this.targetQuantity = targetQuantity;
/*  963 */     this.foreignMerchantPrice = foreignMerchantPrice;
/*  964 */     this.minReputation = minReputation;
/*  965 */     this.desc = desc;
/*      */   }
/*      */   
/*      */ 
/*      */   public boolean equals(Object obj)
/*      */   {
/*  971 */     if (this == obj) {
/*  972 */       return true;
/*      */     }
/*      */     
/*  975 */     if (!(obj instanceof Goods)) {
/*  976 */       return false;
/*      */     }
/*      */     
/*  979 */     Goods g = (Goods)obj;
/*      */     
/*  981 */     return g.item.equals(obj);
/*      */   }
/*      */   
/*      */   public int getBasicBuyingPrice(Building shop)
/*      */   {
/*  986 */     if (shop == null) {
/*  987 */       return this.buyingPrice;
/*      */     }
/*      */     
/*  990 */     if (shop.getTownHall().villageType.buyingPrices.containsKey(this.item)) {
/*  991 */       return ((Integer)shop.getTownHall().villageType.buyingPrices.get(this.item)).intValue();
/*      */     }
/*      */     
/*  994 */     return this.buyingPrice;
/*      */   }
/*      */   
/*      */   public int getBasicSellingPrice(Building shop)
/*      */   {
/*  999 */     if (shop == null) {
/* 1000 */       return this.sellingPrice;
/*      */     }
/*      */     
/* 1003 */     if (shop.getTownHall().villageType.sellingPrices.containsKey(this.item)) {
/* 1004 */       return ((Integer)shop.getTownHall().villageType.sellingPrices.get(this.item)).intValue();
/*      */     }
/*      */     
/* 1007 */     return this.sellingPrice;
/*      */   }
/*      */   
/*      */   public int getCalculatedBuyingPrice(Building shop, EntityPlayer player)
/*      */   {
/* 1012 */     if (shop == null) {
/* 1013 */       return this.buyingPrice;
/*      */     }
/*      */     
/* 1016 */     return shop.getBuyingPrice(this, player);
/*      */   }
/*      */   
/*      */   public int getCalculatedSellingPrice(Building shop, EntityPlayer player)
/*      */   {
/* 1021 */     if (shop == null) {
/* 1022 */       return this.sellingPrice;
/*      */     }
/*      */     
/* 1025 */     return shop.getSellingPrice(this, player);
/*      */   }
/*      */   
/*      */   public int getCalculatedSellingPrice(MillVillager merchant)
/*      */   {
/* 1030 */     if (merchant == null) {
/* 1031 */       return this.foreignMerchantPrice;
/*      */     }
/*      */     
/* 1034 */     if (merchant.merchantSells.containsKey(this)) {
/* 1035 */       return ((Integer)merchant.merchantSells.get(this)).intValue();
/*      */     }
/*      */     
/* 1038 */     return this.foreignMerchantPrice;
/*      */   }
/*      */   
/*      */   public String getName() {
/* 1042 */     return Mill.proxy.getItemName(this.item.getItem(), this.item.meta);
/*      */   }
/*      */   
/*      */   public int hashCode()
/*      */   {
/* 1047 */     return this.item.hashCode();
/*      */   }
/*      */   
/*      */   public String toString()
/*      */   {
/* 1052 */     return "Goods@" + this.item.getItemStack().func_77977_a();
/*      */   }
/*      */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\item\Goods.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */