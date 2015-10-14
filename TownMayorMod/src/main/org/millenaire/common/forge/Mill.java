/*     */ package org.millenaire.common.forge;
/*     */ 
/*     */ import cpw.mods.fml.common.FMLCommonHandler;
/*     */ import cpw.mods.fml.common.Mod;
/*     */ import cpw.mods.fml.common.Mod.EventHandler;
/*     */ import cpw.mods.fml.common.Mod.Instance;
/*     */ import cpw.mods.fml.common.event.FMLInitializationEvent;
/*     */ import cpw.mods.fml.common.event.FMLPreInitializationEvent;
/*     */ import cpw.mods.fml.common.event.FMLServerStartingEvent;
/*     */ import cpw.mods.fml.common.eventhandler.EventBus;
/*     */ import cpw.mods.fml.common.network.FMLEventChannel;
/*     */ import cpw.mods.fml.common.network.NetworkRegistry;
/*     */ import cpw.mods.fml.common.registry.EntityRegistry;
/*     */ import cpw.mods.fml.common.registry.GameRegistry;
/*     */ import cpw.mods.fml.common.registry.LanguageRegistry;
/*     */ import cpw.mods.fml.relauncher.Side;
/*     */ import cpw.mods.fml.relauncher.SideOnly;
/*     */ import java.lang.reflect.Field;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.creativetab.CreativeTabs;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.Item.ToolMaterial;
/*     */ import net.minecraft.item.ItemArmor.ArmorMaterial;
/*     */ import net.minecraft.item.ItemFood;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.item.crafting.FurnaceRecipes;
/*     */ import net.minecraft.potion.Potion;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraftforge.common.AchievementPage;
/*     */ import net.minecraftforge.common.ForgeChunkManager;
/*     */ import net.minecraftforge.common.IPlantable;
/*     */ import net.minecraftforge.common.MinecraftForge;
/*     */ import net.minecraftforge.common.util.EnumHelper;
/*     */ import org.millenaire.common.Culture;
/*     */ import org.millenaire.common.EntityMillDecoration;
/*     */ import org.millenaire.common.MLN;
/*     */ import org.millenaire.common.MillVillager.MLEntityGenericAsymmFemale;
/*     */ import org.millenaire.common.MillVillager.MLEntityGenericMale;
/*     */ import org.millenaire.common.MillVillager.MLEntityGenericSymmFemale;
/*     */ import org.millenaire.common.MillVillager.MLEntityGenericZombie;
/*     */ import org.millenaire.common.MillWorld;
/*     */ import org.millenaire.common.Quest;
/*     */ import org.millenaire.common.TileEntityPanel;
/*     */ import org.millenaire.common.WorldGenVillage;
/*     */ import org.millenaire.common.block.BlockDecorative;
/*     */ import org.millenaire.common.block.BlockDecorative.ItemDecorative;
/*     */ import org.millenaire.common.block.BlockDecorativeSlab;
/*     */ import org.millenaire.common.block.BlockDecorativeSlab.ItemDecorativeSlab;
/*     */ import org.millenaire.common.block.BlockMLNPane;
/*     */ import org.millenaire.common.block.BlockMillChest;
/*     */ import org.millenaire.common.block.BlockMillCrops;
/*     */ import org.millenaire.common.block.BlockOrientedBrick;
/*     */ import org.millenaire.common.block.BlockOrientedSlab;
/*     */ import org.millenaire.common.block.BlockPanel;
/*     */ import org.millenaire.common.building.BuildingPlan;
/*     */ import org.millenaire.common.entity.EntityTargetedBlaze;
/*     */ import org.millenaire.common.entity.EntityTargetedGhast;
/*     */ import org.millenaire.common.entity.EntityTargetedWitherSkeleton;
/*     */ import org.millenaire.common.item.Goods;
/*     */ import org.millenaire.common.item.Goods.ItemAmuletAlchemist;
/*     */ import org.millenaire.common.item.Goods.ItemAmuletSkollHati;
/*     */ import org.millenaire.common.item.Goods.ItemAmuletVishnu;
/*     */ import org.millenaire.common.item.Goods.ItemAmuletYddrasil;
/*     */ import org.millenaire.common.item.Goods.ItemBrickMould;
/*     */ import org.millenaire.common.item.Goods.ItemClothes;
/*     */ import org.millenaire.common.item.Goods.ItemMayanQuestCrown;
/*     */ import org.millenaire.common.item.Goods.ItemMillenaireArmour;
/*     */ import org.millenaire.common.item.Goods.ItemMillenaireAxe;
/*     */ import org.millenaire.common.item.Goods.ItemMillenaireBow;
/*     */ import org.millenaire.common.item.Goods.ItemMillenaireHoe;
/*     */ import org.millenaire.common.item.Goods.ItemMillenairePickaxe;
/*     */ import org.millenaire.common.item.Goods.ItemMillenaireShovel;
/*     */ import org.millenaire.common.item.Goods.ItemMillenaireSword;
/*     */ import org.millenaire.common.item.Goods.ItemNegationWand;
/*     */ import org.millenaire.common.item.Goods.ItemSummoningWand;
/*     */ import org.millenaire.common.item.Goods.ItemTapestry;
/*     */ import org.millenaire.common.item.Goods.ItemText;
/*     */ import org.millenaire.common.item.ItemFoodMultiple;
/*     */ import org.millenaire.common.item.ItemMillSeeds;
/*     */ import org.millenaire.common.item.ItemParchment;
/*     */ import org.millenaire.common.item.ItemPurse;
/*     */ 
/*     */ @Mod(modid="millenaire", name="Millénaire", version="6.0.0")
/*     */ public class Mill
/*     */ {
/*     */   public static final String versionNumber = "6.0.0";
/*     */   public static final String versionBound = "[6.0.0,7.0)";
/*     */   public static final String modId = "millenaire";
/*     */   public static final String name = "Millénaire";
/*     */   public static final String version = "Millénaire 6.0.0";
/*     */   
/*     */   static class CreativeTabMill extends CreativeTabs
/*     */   {
/*     */     public CreativeTabMill(String par2Str)
/*     */     {
/* 102 */       super();
/*     */     }
/*     */     
/*     */     @SideOnly(Side.CLIENT)
/*     */     public Item func_78016_d()
/*     */     {
/* 108 */       return Mill.denier_or;
/*     */     }
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
/* 120 */   public static final CreativeTabs tabMillenaire = new CreativeTabMill("Millenaire");
/*     */   
/*     */   @cpw.mods.fml.common.SidedProxy(clientSide="org.millenaire.client.forge.ClientProxy", serverSide="org.millenaire.common.forge.CommonProxy")
/*     */   public static CommonProxy proxy;
/*     */   
/*     */   @Mod.Instance
/*     */   public static Mill instance;
/*     */   
/*     */   public static final int VILLAGER_ENT_ID = 1;
/*     */   
/* 130 */   public static List<MillWorld> serverWorlds = new ArrayList();
/* 131 */   public static MillWorld clientWorld = null;
/*     */   
/* 133 */   public static List<java.io.File> loadingDirs = new ArrayList();
/*     */   
/* 135 */   static Item.ToolMaterial TOOLS_norman = EnumHelper.addToolMaterial("normanTools", 2, 1561, 10.0F, 4.0F, 10);
/* 136 */   static Item.ToolMaterial TOOLS_obsidian = EnumHelper.addToolMaterial("obsidianTools", 3, 1561, 6.0F, 2.0F, 25);
/*     */   
/* 138 */   static ItemArmor.ArmorMaterial ARMOUR_norman = EnumHelper.addArmorMaterial("normanArmour", 66, new int[] { 3, 8, 6, 3 }, 10);
/* 139 */   static ItemArmor.ArmorMaterial ARMOUR_japaneseWarrior = EnumHelper.addArmorMaterial("japaneseWarrior", 33, new int[] { 2, 6, 5, 2 }, 25);
/* 140 */   static ItemArmor.ArmorMaterial ARMOUR_japaneseGuard = EnumHelper.addArmorMaterial("japaneseGuard", 25, new int[] { 2, 5, 4, 1 }, 25);
/* 141 */   static ItemArmor.ArmorMaterial ARMOUR_byzantine = EnumHelper.addArmorMaterial("byzantineArmour", 33, new int[] { 3, 8, 6, 3 }, 20);
/*     */   
/*     */   public static FMLEventChannel millChannel;
/*     */   
/*     */   public static Block lockedChest;
/*     */   
/*     */   public static Block panel;
/*     */   
/*     */   public static BlockDecorative wood_decoration;
/*     */   
/*     */   public static BlockDecorative earth_decoration;
/*     */   public static BlockDecorative stone_decoration;
/*     */   public static BlockDecorativeSlab path;
/*     */   public static BlockDecorativeSlab pathSlab;
/*     */   public static BlockOrientedBrick byzantine_tiles;
/*     */   public static BlockOrientedSlab byzantine_tile_slab;
/*     */   public static BlockOrientedBrick byzantine_stone_tiles;
/*     */   public static BlockMillCrops cropRice;
/*     */   public static BlockMillCrops cropTurmeric;
/*     */   public static BlockMillCrops cropMaize;
/*     */   public static BlockMillCrops cropVine;
/*     */   public static Block paperWall;
/* 163 */   public static int normanArmourId = 0;
/* 164 */   public static int japaneseWarriorBlueArmourId = 0;
/* 165 */   public static int japaneseWarriorRedArmourId = 0;
/* 166 */   public static int japaneseGuardArmourId = 0;
/* 167 */   public static int byzantineArmourId = 0;
/* 168 */   public static int mayanQuestArmourId = 0;
/*     */   
/*     */   public static Item denier;
/*     */   
/*     */   public static Item denier_or;
/*     */   
/*     */   public static Item denier_argent;
/*     */   
/*     */   public static Item ciderapple;
/*     */   
/*     */   public static Item cider;
/*     */   
/*     */   public static Item calva;
/*     */   
/*     */   public static Item tripes;
/*     */   
/*     */   public static Item normanPickaxe;
/*     */   
/*     */   public static Item normanAxe;
/*     */   
/*     */   public static Item normanShovel;
/*     */   
/*     */   public static Item normanHoe;
/*     */   
/*     */   public static Item summoningWand;
/*     */   
/*     */   public static Item normanBroadsword;
/*     */   
/*     */   public static Item normanHelmet;
/*     */   
/*     */   public static Item normanPlate;
/*     */   
/*     */   public static Item normanLegs;
/*     */   
/*     */   public static Item normanBoots;
/*     */   
/*     */   public static Item parchmentVillagers;
/*     */   
/*     */   public static Item parchmentBuildings;
/*     */   
/*     */   public static Item parchmentItems;
/*     */   
/*     */   public static Item parchmentComplete;
/*     */   
/*     */   public static Item boudin;
/*     */   
/*     */   public static Item tapestry;
/*     */   
/*     */   public static Item vishnu_amulet;
/*     */   
/*     */   public static Item alchemist_amulet;
/*     */   public static Item yddrasil_amulet;
/*     */   public static Item skoll_hati_amulet;
/*     */   public static Item parchmentVillageScroll;
/*     */   public static Item rice;
/*     */   public static Item turmeric;
/*     */   public static Item vegcurry;
/*     */   public static Item chickencurry;
/*     */   public static Item brickmould;
/*     */   public static Item rasgulla;
/*     */   public static Item indianstatue;
/*     */   public static Item parchmentIndianVillagers;
/*     */   public static Item parchmentIndianBuildings;
/*     */   public static Item parchmentIndianItems;
/*     */   public static Item parchmentIndianComplete;
/*     */   public static Item mayanstatue;
/*     */   public static Item maize;
/*     */   public static Item wah;
/*     */   public static Item masa;
/*     */   public static Item parchmentMayanVillagers;
/*     */   public static Item parchmentMayanBuildings;
/*     */   public static Item parchmentMayanItems;
/*     */   public static Item parchmentMayanComplete;
/*     */   public static Item parchmentSadhu;
/*     */   public static Item unknownPowder;
/*     */   public static Item udon;
/*     */   public static Item tachiSword;
/*     */   public static Item negationWand;
/*     */   public static Item obsidianFlake;
/*     */   public static Item mayanMace;
/*     */   public static Item mayanPickaxe;
/*     */   public static Item mayanAxe;
/*     */   public static Item mayanShovel;
/*     */   public static Item mayanHoe;
/*     */   public static Item yumiBow;
/*     */   public static Item japaneseWarriorBlueLegs;
/*     */   public static Item japaneseWarriorBlueHelmet;
/*     */   public static Item japaneseWarriorBluePlate;
/*     */   public static Item japaneseWarriorBlueBoots;
/*     */   public static Item japaneseWarriorRedLegs;
/*     */   public static Item japaneseWarriorRedHelmet;
/*     */   public static Item japaneseWarriorRedPlate;
/*     */   public static Item japaneseWarriorRedBoots;
/*     */   public static Item japaneseGuardLegs;
/*     */   public static Item japaneseGuardHelmet;
/*     */   public static Item japaneseGuardPlate;
/*     */   public static Item japaneseGuardBoots;
/*     */   public static Item parchmentJapaneseVillagers;
/*     */   public static Item parchmentJapaneseBuildings;
/*     */   public static Item parchmentJapaneseItems;
/*     */   public static Item parchmentJapaneseComplete;
/*     */   public static Item grapes;
/*     */   public static Item wineFancy;
/*     */   public static Item silk;
/*     */   public static Item byzantineiconsmall;
/*     */   public static Item byzantineiconmedium;
/*     */   public static Item byzantineiconlarge;
/*     */   public static Item byzantineMace;
/*     */   public static Item byzantineLegs;
/*     */   public static Item byzantineHelmet;
/*     */   public static Item byzantinePlate;
/*     */   public static Item byzantineBoots;
/*     */   public static Goods.ItemClothes clothes;
/*     */   public static Item wineBasic;
/*     */   public static Item lambRaw;
/*     */   public static Item lambCooked;
/*     */   public static Item feta;
/*     */   public static Item souvlaki;
/*     */   public static ItemPurse purse;
/*     */   public static Item sake;
/*     */   public static Item cacauhaa;
/*     */   public static Item mayanQuestCrown;
/*     */   public static Item ikayaki;
/* 291 */   public static boolean loadingComplete = false;
/*     */   
/* 293 */   public static boolean startMessageDisplayed = false;
/*     */   public static HashMap<Class, String> entityNames;
/*     */   public static final String ENTITY_PIG = "Pig";
/*     */   public static final String ENTITY_COW = "Cow";
/*     */   public static final String ENTITY_CHICKEN = "Chicken";
/*     */   public static final String ENTITY_SHEEP = "Sheep";
/*     */   public static final String ENTITY_SQUID = "Squid";
/*     */   public static final String ENTITY_WOLF = "Wolf";
/*     */   public static final String ENTITY_SKELETON = "Skeleton";
/*     */   public static final String ENTITY_CREEPER = "Creeper";
/*     */   public static final String ENTITY_SPIDER = "Spider";
/*     */   public static final String ENTITY_CAVESPIDER = "CaveSpider";
/*     */   public static final String ENTITY_ZOMBIE = "Zombie";
/* 306 */   public static final String ENTITY_TARGETED_GHAST = "MillGhast"; public static final String ENTITY_TARGETED_BLAZE = "MillBlaze"; public static final String ENTITY_TARGETED_WITHERSKELETON = "MillWitherSkeleton"; public static final String CROP_WHEAT = "wheat"; public static final String CROP_CARROT = "carrot"; public static final String CROP_POTATO = "potato"; public static final String CROP_RICE = "rice"; public static final String CROP_TURMERIC = "turmeric"; public static final String CROP_MAIZE = "maize"; public static final String CROP_VINE = "vine"; public static final String CROP_CACAO = "cacao"; public static boolean startupError = false;
/*     */   
/* 308 */   public static boolean checkedMillenaireDir = false;
/*     */   
/* 310 */   public static boolean displayMillenaireLocationError = false;
/*     */   
/*     */   public static MillWorld getMillWorld(World world)
/*     */   {
/* 314 */     if ((clientWorld != null) && (clientWorld.world == world)) {
/* 315 */       return clientWorld;
/*     */     }
/*     */     
/* 318 */     for (MillWorld mw : serverWorlds) {
/* 319 */       if (mw.world == world) {
/* 320 */         return mw;
/*     */       }
/*     */     }
/*     */     
/* 324 */     if ((serverWorlds != null) && (serverWorlds.size() > 0)) {
/* 325 */       return (MillWorld)serverWorlds.get(0);
/*     */     }
/*     */     
/* 328 */     return null;
/*     */   }
/*     */   
/*     */   public static boolean isDistantClient() {
/* 332 */     if ((clientWorld != null) && (serverWorlds.isEmpty())) {
/* 333 */       return true;
/*     */     }
/* 335 */     return false;
/*     */   }
/*     */   
/*     */   public static boolean isRunningDeobf() {
/* 339 */     return net.minecraft.entity.EntityCreature.class.getSimpleName().equals("EntityCreature");
/*     */   }
/*     */   
/*     */ 
/*     */   protected void initBlockItems()
/*     */   {
/* 345 */     lockedChest = new BlockMillChest().func_149663_c("ml_building").func_149711_c(10.0F).func_149752_b(2000.0F).func_149672_a(Block.field_149766_f);
/*     */     
/* 347 */     panel = new BlockPanel(TileEntityPanel.class, false).func_149663_c("ml_panel").func_149711_c(10.0F).func_149752_b(2000.0F).func_149672_a(Block.field_149766_f);
/* 348 */     wood_decoration = new BlockDecorative(Material.field_151575_d);
/* 349 */     earth_decoration = new BlockDecorative(Material.field_151578_c);
/* 350 */     stone_decoration = new BlockDecorative(Material.field_151576_e);
/* 351 */     path = new BlockDecorativeSlab(Material.field_151578_c, true);
/* 352 */     pathSlab = new BlockDecorativeSlab(Material.field_151578_c, false);
/*     */     
/* 354 */     cropRice = new BlockMillCrops(new String[] { "rice0", "rice0", "rice0", "rice0", "rice0", "rice0", "rice0", "rice1" }, true, false);
/* 355 */     cropTurmeric = new BlockMillCrops(new String[] { "turmeric0", "turmeric0", "turmeric0", "turmeric0", "turmeric0", "turmeric0", "turmeric0", "turmeric1" }, false, false);
/* 356 */     cropMaize = new BlockMillCrops(new String[] { "maize0", "maize0", "maize0", "maize0", "maize0", "maize0", "maize0", "maize1" }, false, true);
/* 357 */     cropVine = new BlockMillCrops(new String[] { "vine0", "vine0", "vine0", "vine0", "vine0", "vine0", "vine0", "vine1" }, false, false);
/* 358 */     rice = new ItemMillSeeds("rice", cropRice, "rice").func_77655_b("ml_rice");
/* 359 */     turmeric = new ItemMillSeeds("turmeric", cropTurmeric, "turmeric").func_77655_b("ml_turmeric");
/* 360 */     maize = new ItemMillSeeds("maize", cropMaize, "maize").func_77655_b("ml_maize");
/* 361 */     grapes = new ItemMillSeeds("grapes", cropVine, "vine").func_77655_b("ml_vine");
/* 362 */     cropRice.setSeed((IPlantable)rice);
/* 363 */     cropTurmeric.setSeed((IPlantable)turmeric);
/* 364 */     cropMaize.setSeed((IPlantable)maize);
/* 365 */     cropVine.setSeed((IPlantable)grapes);
/* 366 */     cropRice.func_149663_c("ml_cropRice");
/* 367 */     cropTurmeric.func_149663_c("ml_cropTurmeric");
/* 368 */     cropMaize.func_149663_c("ml_cropMaize");
/* 369 */     cropVine.func_149663_c("ml_cropVine");
/*     */     
/* 371 */     paperWall = new BlockMLNPane("paperwall", "paperwall", Material.field_151580_n, true).func_149711_c(0.3F).func_149672_a(Block.field_149775_l).func_149663_c("ml_panes");
/*     */     
/* 373 */     byzantine_tiles = (BlockOrientedBrick)new BlockOrientedBrick("tilestopvert", "tilestophor", "tilestopvert", "tilestophor", "tilesfront", "tilestophor").func_149711_c(2.0F).func_149752_b(10.0F).func_149672_a(Block.field_149769_e).func_149663_c("byzantine_brick");
/*     */     
/* 375 */     byzantine_tile_slab = (BlockOrientedSlab)new BlockOrientedSlab("tilestophor", "tilestopvert", "tilesfront").func_149711_c(2.0F).func_149752_b(10.0F).func_149672_a(Block.field_149769_e).func_149663_c("byzantine_brick_slab");
/*     */     
/* 377 */     byzantine_stone_tiles = (BlockOrientedBrick)new BlockOrientedBrick("tilestopvert", "tilestophor", "stone", "stone", "tileshalffront", "tileshalfside").func_149711_c(2.0F).func_149752_b(10.0F).func_149672_a(Block.field_149769_e).func_149663_c("byzantine_mixedbrick");
/*     */     
/*     */ 
/* 380 */     lockedChest.setHarvestLevel("axe", 0);
/* 381 */     wood_decoration.setHarvestLevel("axe", 0);
/* 382 */     paperWall.setHarvestLevel("axe", 0);
/* 383 */     panel.setHarvestLevel("axe", 0);
/* 384 */     stone_decoration.setHarvestLevel("pickaxe", 0);
/* 385 */     byzantine_tiles.setHarvestLevel("pickaxe", 0);
/* 386 */     byzantine_tile_slab.setHarvestLevel("pickaxe", 0);
/* 387 */     byzantine_stone_tiles.setHarvestLevel("pickaxe", 0);
/* 388 */     earth_decoration.setHarvestLevel("shovel", 0);
/* 389 */     path.setHarvestLevel("shovel", 0);
/* 390 */     pathSlab.setHarvestLevel("shovel", 0);
/*     */     
/* 392 */     proxy.setTextureIds();
/*     */     
/* 394 */     denier = new Goods.ItemText("denier").func_77655_b("ml_denier");
/* 395 */     denier_or = new Goods.ItemText("denier_or").func_77655_b("ml_denier_or");
/* 396 */     denier_argent = new Goods.ItemText("denier_argent").func_77655_b("ml_denier_argent");
/* 397 */     ciderapple = new ItemFoodMultiple("ciderapple", 0, 0, 1, 0.05F, false, 0).func_77655_b("ml_ciderapple").func_77625_d(64);
/*     */     
/* 399 */     cider = new ItemFoodMultiple("cider", 4, 15, 0, 0.0F, true, 5).func_77848_i().func_77655_b("ml_cider");
/* 400 */     calva = new ItemFoodMultiple("calva", 8, 30, 0, 0.0F, true, 10).func_77844_a(Potion.field_76420_g.field_76415_H, 180, 0, 1.0F).func_77848_i().func_77655_b("ml_calva");
/* 401 */     tripes = new ItemFoodMultiple("tripes", 0, 0, 10, 1.0F, false, 0).func_77844_a(Potion.field_76428_l.field_76415_H, 90, 0, 1.0F).func_77848_i().func_77655_b("ml_tripes");
/*     */     
/* 403 */     normanPickaxe = new Goods.ItemMillenairePickaxe("normanpickaxe", TOOLS_norman).func_77655_b("ml_normanPickaxe");
/* 404 */     normanAxe = new Goods.ItemMillenaireAxe("normanaxe", TOOLS_norman).func_77655_b("ml_normanAxe");
/* 405 */     normanShovel = new Goods.ItemMillenaireShovel("normanshovel", TOOLS_norman).func_77655_b("ml_normanShovel");
/* 406 */     normanHoe = new Goods.ItemMillenaireHoe("normanhoe", TOOLS_norman).func_77655_b("ml_normanHoe");
/*     */     
/* 408 */     summoningWand = new Goods.ItemSummoningWand("summoningwand").func_77664_n().func_77655_b("ml_villageWand");
/*     */     
/* 410 */     normanBroadsword = new Goods.ItemMillenaireSword("normansword", TOOLS_norman, 0.0F, 0, false).func_77655_b("ml_normanBroadsword");
/* 411 */     normanHelmet = new Goods.ItemMillenaireArmour("normanhelmet", ARMOUR_norman, normanArmourId, 0).func_77655_b("ml_normanHelmet");
/* 412 */     normanPlate = new Goods.ItemMillenaireArmour("normanplate", ARMOUR_norman, normanArmourId, 1).func_77655_b("ml_normanPlate");
/* 413 */     normanLegs = new Goods.ItemMillenaireArmour("normanlegs", ARMOUR_norman, normanArmourId, 2).func_77655_b("ml_normanLegs");
/* 414 */     normanBoots = new Goods.ItemMillenaireArmour("normanboots", ARMOUR_norman, normanArmourId, 3).func_77655_b("ml_normanBoots");
/*     */     
/* 416 */     parchmentVillagers = new ItemParchment("parchmentvillagers", 1).func_77655_b("ml_parchmentVillagers");
/*     */     
/* 418 */     parchmentBuildings = new ItemParchment("parchmentbuildings", 2).func_77655_b("ml_parchmentBuildings");
/*     */     
/* 420 */     parchmentItems = new ItemParchment("parchmentitems", 3).func_77655_b("ml_parchmentItems");
/*     */     
/* 422 */     parchmentComplete = new ItemParchment("parchmentall", new int[] { 1, 2, 3 }).func_77655_b("ml_marchmentComplete");
/*     */     
/* 424 */     boudin = new ItemFoodMultiple("boudin", 0, 0, 10, 1.0F, false, 0).func_77655_b("ml_boudin");
/*     */     
/* 426 */     tapestry = new Goods.ItemTapestry("normantapestry", 1).func_77655_b("ml_tapestry");
/*     */     
/* 428 */     vishnu_amulet = new Goods.ItemAmuletVishnu("amulet_vishnu").func_77637_a(tabMillenaire).func_77655_b("ml_raven_amulet").func_77625_d(1);
/* 429 */     alchemist_amulet = new Goods.ItemAmuletAlchemist("amulet_alchemist").func_77637_a(tabMillenaire).func_77655_b("ml_dwarves_amulet").func_77625_d(1);
/* 430 */     yddrasil_amulet = new Goods.ItemAmuletYddrasil("amulet_yggdrasil").func_77637_a(tabMillenaire).func_77655_b("ml_yddrasil_amulet").func_77625_d(1);
/*     */     
/* 432 */     skoll_hati_amulet = new Goods.ItemAmuletSkollHati("amulet_skollhati").func_77637_a(tabMillenaire).func_77655_b("ml_skoll_hati_amulet").func_77625_d(1).func_77656_e(10);
/* 433 */     parchmentVillageScroll = new ItemParchment("parchmentvillage", new int[] { 4 }).func_77655_b("ml_parchmentVillageScroll");
/*     */     
/* 435 */     vegcurry = new ItemFoodMultiple("curry", 0, 0, 6, 0.6F, false, 0).func_77655_b("ml_vegcurry");
/* 436 */     chickencurry = new ItemFoodMultiple("currychicken", 0, 0, 8, 0.8F, false, 0).func_77844_a(Potion.field_76426_n.field_76415_H, 480, 0, 1.0F).func_77848_i().func_77655_b("ml_chickencurry");
/* 437 */     brickmould = new Goods.ItemBrickMould("brickmould").func_77655_b("ml_brickmould").func_77625_d(1).func_77656_e(128);
/* 438 */     rasgulla = new ItemFoodMultiple("rasgulla", 2, 30, 0, 0.0F, false, 0).func_77844_a(Potion.field_76424_c.field_76415_H, 480, 1, 1.0F).func_77848_i().func_77655_b("ml_rasgullaId");
/* 439 */     indianstatue = new Goods.ItemTapestry("indianstatue", 2).func_77655_b("ml_indianstatue");
/*     */     
/* 441 */     parchmentIndianVillagers = new ItemParchment("parchmentvillagers", 5).func_77655_b("ml_parchmentIndianVillagers");
/* 442 */     parchmentIndianBuildings = new ItemParchment("parchmentbuildings", 6).func_77655_b("ml_parchmentIndianBuildings");
/* 443 */     parchmentIndianItems = new ItemParchment("parchmentitems", 7).func_77655_b("ml_parchmentIndianItems");
/* 444 */     parchmentIndianComplete = new ItemParchment("parchmentall", new int[] { 5, 6, 7 }).func_77655_b("ml_marchmentIndianComplete");
/*     */     
/*     */ 
/* 447 */     mayanstatue = new Goods.ItemTapestry("mayanstatue", 3).func_77655_b("ml_mayanstatue");
/* 448 */     masa = new ItemFoodMultiple("masa", 0, 0, 6, 0.6F, false, 0).func_77655_b("ml_masa");
/* 449 */     wah = new ItemFoodMultiple("wah", 0, 0, 10, 1.0F, false, 0).func_77844_a(Potion.field_76422_e.field_76415_H, 480, 0, 1.0F).func_77848_i().func_77655_b("ml_wah");
/*     */     
/* 451 */     parchmentMayanVillagers = new ItemParchment("parchmentvillagers", 9).func_77655_b("ml_parchmentMayanVillagers");
/* 452 */     parchmentMayanBuildings = new ItemParchment("parchmentbuildings", 10).func_77655_b("ml_parchmentMayanBuildings");
/* 453 */     parchmentMayanItems = new ItemParchment("parchmentitems", 11).func_77655_b("ml_parchmentMayanItems");
/* 454 */     parchmentMayanComplete = new ItemParchment("parchmentall", new int[] { 9, 10, 11 }).func_77655_b("ml_parchmentMayanComplete");
/*     */     
/*     */ 
/* 457 */     parchmentSadhu = new ItemParchment("parchmentall", new int[] { 15 }).func_77655_b("ml_parchmentSadhu");
/*     */     
/* 459 */     unknownPowder = new Goods.ItemText("unknownpowder").func_77655_b("ml_unknownPowder").func_77637_a(tabMillenaire);
/*     */     
/* 461 */     udon = new ItemFoodMultiple("udon", 0, 0, 8, 0.8F, false, 0).func_77848_i().func_77655_b("ml_udon");
/*     */     
/* 463 */     tachiSword = new Goods.ItemMillenaireSword("tachisword", Item.ToolMaterial.IRON, 0.2F, 5, false).func_77655_b("ml_taichiSword");
/*     */     
/* 465 */     negationWand = new Goods.ItemNegationWand("negationwand").func_77664_n().func_77655_b("ml_negationWand");
/*     */     
/* 467 */     obsidianFlake = new Goods.ItemText("obsidianflake").func_77655_b("ml_obsidianFlake");
/* 468 */     mayanMace = new Goods.ItemMillenaireSword("mayanmace", TOOLS_obsidian, 0.0F, 0, false).func_77655_b("ml_mayanMace");
/* 469 */     mayanPickaxe = new Goods.ItemMillenairePickaxe("mayanpickaxe", TOOLS_obsidian).func_77655_b("ml_mayanPickaxe");
/* 470 */     mayanAxe = new Goods.ItemMillenaireAxe("mayanaxe", TOOLS_obsidian).func_77655_b("ml_mayanAxe");
/* 471 */     mayanShovel = new Goods.ItemMillenaireShovel("mayanshovel", TOOLS_obsidian).func_77655_b("ml_mayanShovel");
/* 472 */     mayanHoe = new Goods.ItemMillenaireHoe("mayanhoe", TOOLS_obsidian).func_77655_b("ml_mayanHoe");
/*     */     
/* 474 */     yumiBow = new Goods.ItemMillenaireBow(2.0F, 0.5F, new String[] { "yumibow0", "yumibow1", "yumibow2", "yumibow3" }).func_77655_b("ml_yumiBow").func_77664_n();
/*     */     
/* 476 */     japaneseWarriorBlueLegs = new Goods.ItemMillenaireArmour("japanesebluelegs", ARMOUR_japaneseWarrior, japaneseWarriorBlueArmourId, 2).func_77655_b("ml_japaneseWarriorBlueLegs");
/* 477 */     japaneseWarriorBlueHelmet = new Goods.ItemMillenaireArmour("japanesebluehelmet", ARMOUR_japaneseWarrior, japaneseWarriorBlueArmourId, 0).func_77655_b("ml_japaneseWarriorBlueHelmet");
/* 478 */     japaneseWarriorBluePlate = new Goods.ItemMillenaireArmour("japaneseblueplate", ARMOUR_japaneseWarrior, japaneseWarriorBlueArmourId, 1).func_77655_b("ml_japaneseWarriorBluePlate");
/* 479 */     japaneseWarriorBlueBoots = new Goods.ItemMillenaireArmour("japaneseblueboots", ARMOUR_japaneseWarrior, japaneseWarriorBlueArmourId, 3).func_77655_b("ml_japaneseWarriorBlueBoots");
/*     */     
/* 481 */     japaneseWarriorRedLegs = new Goods.ItemMillenaireArmour("japaneseredlegs", ARMOUR_japaneseWarrior, japaneseWarriorRedArmourId, 2).func_77655_b("ml_japaneseWarriorRedLegs");
/* 482 */     japaneseWarriorRedHelmet = new Goods.ItemMillenaireArmour("japaneseredhelmet", ARMOUR_japaneseWarrior, japaneseWarriorRedArmourId, 0).func_77655_b("ml_japaneseWarriorRedHelmet");
/* 483 */     japaneseWarriorRedPlate = new Goods.ItemMillenaireArmour("japaneseredplate", ARMOUR_japaneseWarrior, japaneseWarriorRedArmourId, 1).func_77655_b("ml_japaneseWarriorRedPlate");
/* 484 */     japaneseWarriorRedBoots = new Goods.ItemMillenaireArmour("japaneseredboots", ARMOUR_japaneseWarrior, japaneseWarriorRedArmourId, 3).func_77655_b("ml_japaneseWarriorRedBoots");
/*     */     
/* 486 */     japaneseGuardLegs = new Goods.ItemMillenaireArmour("japaneseguardlegs", ARMOUR_japaneseGuard, japaneseGuardArmourId, 2).func_77655_b("ml_japaneseGuardLegs");
/* 487 */     japaneseGuardHelmet = new Goods.ItemMillenaireArmour("japaneseguardhelmet", ARMOUR_japaneseGuard, japaneseGuardArmourId, 0).func_77655_b("ml_japaneseGuardHelmet");
/* 488 */     japaneseGuardPlate = new Goods.ItemMillenaireArmour("japaneseguardplate", ARMOUR_japaneseGuard, japaneseGuardArmourId, 1).func_77655_b("ml_japaneseGuardPlate");
/* 489 */     japaneseGuardBoots = new Goods.ItemMillenaireArmour("japaneseguardboots", ARMOUR_japaneseGuard, japaneseGuardArmourId, 3).func_77655_b("ml_japaneseGuardBoots");
/*     */     
/* 491 */     parchmentJapaneseVillagers = new ItemParchment("parchmentvillagers", 16).func_77655_b("ml_parchmentJapaneseVillagers");
/* 492 */     parchmentJapaneseBuildings = new ItemParchment("parchmentbuildings", 17).func_77655_b("ml_parchmentJapaneseBuildings");
/* 493 */     parchmentJapaneseItems = new ItemParchment("parchmentitems", 18).func_77655_b("ml_parchmentJapaneseItems");
/* 494 */     parchmentJapaneseComplete = new ItemParchment("parchmentall", new int[] { 16, 17, 18 }).func_77655_b("ml_parchmentJapaneseComplete");
/*     */     
/*     */ 
/* 497 */     wineFancy = new ItemFoodMultiple("winefancy", 8, 30, 0, 0.0F, true, 5).func_77844_a(Potion.field_76429_m.field_76415_H, 480, 0, 1.0F).func_77848_i().func_77655_b("ml_wine");
/* 498 */     silk = new Goods.ItemText("silk").func_77655_b("ml_silk");
/* 499 */     byzantineiconsmall = new Goods.ItemTapestry("byzantineicon", 4).func_77655_b("ml_byzantineicon");
/* 500 */     byzantineiconmedium = new Goods.ItemTapestry("byzantineicon", 5).func_77655_b("ml_byzantineiconmedium");
/* 501 */     byzantineiconlarge = new Goods.ItemTapestry("byzantineicon", 6).func_77655_b("ml_byzantineiconlarge");
/*     */     
/* 503 */     byzantineLegs = new Goods.ItemMillenaireArmour("byzantinelegs", ARMOUR_byzantine, byzantineArmourId, 2).func_77655_b("ml_byzantineLegs");
/* 504 */     byzantineHelmet = new Goods.ItemMillenaireArmour("byzantinehelmet", ARMOUR_byzantine, byzantineArmourId, 0).func_77655_b("ml_byzantineHelmet");
/* 505 */     byzantinePlate = new Goods.ItemMillenaireArmour("byzantineplate", ARMOUR_byzantine, byzantineArmourId, 1).func_77655_b("ml_byzantinePlate");
/* 506 */     byzantineBoots = new Goods.ItemMillenaireArmour("byzantineboots", ARMOUR_byzantine, byzantineArmourId, 3).func_77655_b("ml_byzantineBoots");
/*     */     
/* 508 */     byzantineMace = new Goods.ItemMillenaireSword("byzantinemace", Item.ToolMaterial.IRON, 0.0F, 0, true).func_77655_b("ml_byzantineMace");
/*     */     
/* 510 */     clothes = (Goods.ItemClothes)new Goods.ItemClothes(new String[] { "byzantineclothwool", "byzantineclothsilk" }).func_77655_b("ml_clothes");
/* 511 */     wineBasic = new ItemFoodMultiple("winebasic", 3, 15, 0, 0.0F, true, 5).func_77848_i().func_77655_b("ml_wine_basic");
/* 512 */     lambRaw = new ItemFoodMultiple("lambraw", 0, 0, 2, 0.2F, false, 0).func_77655_b("ml_lamb_raw");
/* 513 */     lambCooked = new ItemFoodMultiple("lambcooked", 0, 0, 6, 0.6F, false, 0).func_77655_b("ml_lamb_cooked");
/* 514 */     feta = new ItemFoodMultiple("feta", 2, 15, 0, 0.0F, false, 0).func_77655_b("ml_feta");
/* 515 */     souvlaki = new ItemFoodMultiple("souvlaki", 0, 0, 10, 1.0F, false, 0).func_77844_a(Potion.field_76432_h.field_76415_H, 1, 0, 1.0F).func_77848_i().func_77655_b("ml_souvlaki");
/*     */     
/* 517 */     purse = (ItemPurse)new ItemPurse("purse").func_77625_d(1).func_77655_b("ml_purse");
/*     */     
/* 519 */     sake = new ItemFoodMultiple("sake", 8, 30, 0, 0.0F, true, 10).func_77844_a(Potion.field_76430_j.field_76415_H, 480, 1, 1.0F).func_77848_i().func_77655_b("ml_sake");
/*     */     
/* 521 */     cacauhaa = new ItemFoodMultiple("cacauhaa", 6, 30, 0, 0.0F, true, 0).func_77844_a(Potion.field_76439_r.field_76415_H, 480, 0, 1.0F).func_77848_i().func_77655_b("ml_cacauhaa");
/*     */     
/* 523 */     mayanQuestCrown = new Goods.ItemMayanQuestCrown("mayanquestcrown", mayanQuestArmourId, 0).func_77655_b("ml_mayanQuestCrown");
/*     */     
/* 525 */     ikayaki = new ItemFoodMultiple("ikayaki", 0, 0, 10, 1.0F, false, 0).func_77844_a(Potion.field_76427_o.field_76415_H, 480, 2, 1.0F).func_77848_i().func_77655_b("ml_ikayaki");
/*     */     
/* 527 */     wood_decoration.func_149663_c("ml_wood_deco").func_149711_c(2.0F).func_149752_b(5.0F).func_149672_a(Block.field_149766_f);
/* 528 */     wood_decoration.registerTexture(0, "timberframeplain");
/* 529 */     wood_decoration.registerTexture(1, "timberframecross");
/* 530 */     wood_decoration.registerTexture(2, "thatch");
/* 531 */     wood_decoration.registerTexture(3, "silkwormempty");
/* 532 */     wood_decoration.registerTexture(4, "silkwormfull");
/*     */     
/* 534 */     earth_decoration.func_149663_c("ml_earth_deco").func_149711_c(1.0F).func_149752_b(2.0F).func_149672_a(Block.field_149767_g);
/* 535 */     earth_decoration.registerTexture(0, "mudbrick");
/* 536 */     earth_decoration.registerTexture(1, "dirtwall");
/*     */     
/* 538 */     stone_decoration.func_149663_c("ml_stone_deco").func_149711_c(1.0F).func_149752_b(8.0F).func_149672_a(Block.field_149769_e);
/* 539 */     stone_decoration.registerTexture(0, "cookedbrick");
/* 540 */     stone_decoration.registerTexture(1, "mudbrickdried");
/* 541 */     stone_decoration.registerTexture(2, "mayangoldblock");
/* 542 */     stone_decoration.registerTexture(3, "alchemistexplosive");
/*     */     
/* 544 */     path.func_149663_c("ml_path").func_149711_c(1.0F).func_149752_b(2.0F).func_149672_a(Block.field_149767_g);
/* 545 */     path.registerTexture(0, "pathdirt", "pathbottom", "pathdirt_side");
/* 546 */     path.registerTexture(1, "pathgravel", "pathbottom", "pathgravel_side");
/* 547 */     path.registerTexture(2, "pathslabs", "pathbottom", "pathslabs_side");
/* 548 */     path.registerTexture(3, "pathsandstone", "pathbottom", "pathsandstone_side");
/* 549 */     path.registerTexture(4, "pathochretiles", "pathbottom", "pathochretiles_side");
/* 550 */     path.registerTexture(5, "pathgravelslabs", "pathbottom", "pathgravel_side");
/*     */     
/* 552 */     pathSlab.func_149663_c("ml_path_slab").func_149711_c(1.0F).func_149752_b(2.0F).func_149672_a(Block.field_149767_g);
/* 553 */     pathSlab.registerTexture(0, "pathdirt", "pathbottom", "pathdirt_halfside");
/* 554 */     pathSlab.registerTexture(1, "pathgravel", "pathbottom", "pathgravel_halfside");
/* 555 */     pathSlab.registerTexture(2, "pathslabs", "pathbottom", "pathslabs_halfside");
/* 556 */     pathSlab.registerTexture(3, "pathsandstone", "pathbottom", "pathsandstone_halfside");
/* 557 */     pathSlab.registerTexture(4, "pathochretiles", "pathbottom", "pathochretiles_halfside");
/* 558 */     pathSlab.registerTexture(5, "pathgravelslabs", "pathbottom", "pathgravel_halfside");
/*     */     
/* 560 */     entityNames = new HashMap();
/*     */     
/* 562 */     entityNames.put(MillVillager.MLEntityGenericMale.class, "ml_GenericVillager");
/* 563 */     entityNames.put(MillVillager.MLEntityGenericAsymmFemale.class, "ml_GenericAsimmFemale");
/* 564 */     entityNames.put(MillVillager.MLEntityGenericSymmFemale.class, "ml_GenericSimmFemale");
/* 565 */     entityNames.put(MillVillager.MLEntityGenericZombie.class, "ml_GenericZombie");
/*     */     
/* 567 */     entityNames.put(EntityTargetedGhast.class, "MillGhast");
/* 568 */     entityNames.put(EntityTargetedBlaze.class, "MillBlaze");
/* 569 */     entityNames.put(EntityTargetedWitherSkeleton.class, "MillWitherSkeleton");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   @Mod.EventHandler
/*     */   public void load(FMLInitializationEvent evt)
/*     */   {
/* 577 */     if (startupError) {
/* 578 */       return;
/*     */     }
/*     */     
/* 581 */     LanguageRegistry.instance().addStringLocalization("itemGroup.Millenaire", "en_US", "Millénaire");
/*     */     
/* 583 */     if (MLN.stopDefaultVillages) {
/* 584 */       net.minecraft.world.gen.structure.MapGenVillage.field_75055_e = Arrays.asList(new net.minecraft.world.biome.BiomeGenBase[0]);
/*     */     }
/*     */     
/* 587 */     boolean error = false;
/*     */     
/* 589 */     if (!error) {
/* 590 */       error = BuildingPlan.loadBuildingPoints();
/*     */     }
/*     */     
/* 593 */     if (!error) {
/* 594 */       Goods.loadGoods();
/*     */     }
/*     */     
/* 597 */     if (!error) {
/* 598 */       org.millenaire.common.goal.Goal.initGoals();
/*     */     }
/*     */     
/* 601 */     if (!error) {
/* 602 */       error = Culture.loadCultures();
/*     */     }
/*     */     
/* 605 */     if (!error) {
/* 606 */       Quest.loadQuests();
/*     */     }
/* 608 */     if (MLN.generateGoodsList) {
/* 609 */       Goods.generateGoodsList();
/*     */     }
/*     */     
/* 612 */     startupError = error;
/*     */     
/* 614 */     loadingComplete = true;
/*     */     
/* 616 */     if (MLN.LogOther >= 1) {
/* 617 */       if (startupError) {
/* 618 */         MLN.major(this, "Millénaire 6.0.0 loaded unsuccessfully.");
/*     */       } else {
/* 620 */         MLN.major(this, "Millénaire 6.0.0 loaded successfully.");
/*     */       }
/*     */     }
/*     */     
/* 624 */     FMLCommonHandler.instance().bus().register(new ServerTickHandler());
/* 625 */     FMLCommonHandler.instance().bus().register(this);
/*     */     
/* 627 */     millChannel = NetworkRegistry.INSTANCE.newEventDrivenChannel("millenaire");
/* 628 */     millChannel.register(new org.millenaire.common.network.ServerReceiver());
/* 629 */     millChannel.register(new org.millenaire.client.network.ClientReceiver());
/*     */     
/* 631 */     proxy.registerForgeClientClasses();
/*     */     
/* 633 */     NetworkRegistry.INSTANCE.registerGuiHandler(instance, proxy.createGuiHandler());
/* 634 */     GameRegistry.registerWorldGenerator(new WorldGenVillage(), 1000);
/*     */     
/* 636 */     MinecraftForge.EVENT_BUS.register(new MillEventController());
/*     */     
/* 638 */     ForgeChunkManager.setForcedChunkLoadingCallback(this, new BuildingChunkLoader.ChunkLoaderCallback());
/*     */     
/* 640 */     MLN.checkBonusCode(false);
/*     */     
/* 642 */     proxy.loadLanguages();
/*     */   }
/*     */   
/*     */   @Mod.EventHandler
/*     */   public void preInit(FMLPreInitializationEvent event) {
/* 647 */     MLN.loadConfig();
/*     */     
/* 649 */     initBlockItems();
/*     */     
/* 651 */     AchievementPage.registerAchievementPage(MillAchievements.millAchievements);
/*     */     
/* 653 */     registerBlocksItemsEntities();
/*     */     
/* 655 */     proxy.refreshClientResources();
/*     */   }
/*     */   
/*     */   private void registerBlocksItemsEntities() {
/*     */     try {
/* 660 */       EntityRegistry.registerGlobalEntityID(MillVillager.MLEntityGenericAsymmFemale.class, "ml_GenericAsimmFemale", EntityRegistry.findGlobalUniqueEntityId());
/* 661 */       EntityRegistry.registerModEntity(MillVillager.MLEntityGenericAsymmFemale.class, "ml_GenericAsimmFemale", 2, instance, 64, 3, true);
/*     */       
/* 663 */       EntityRegistry.registerGlobalEntityID(MillVillager.MLEntityGenericSymmFemale.class, "ml_GenericSimmFemale", EntityRegistry.findGlobalUniqueEntityId());
/* 664 */       EntityRegistry.registerModEntity(MillVillager.MLEntityGenericSymmFemale.class, "ml_GenericSimmFemale", 3, instance, 64, 3, true);
/*     */       
/* 666 */       EntityRegistry.registerGlobalEntityID(MillVillager.MLEntityGenericMale.class, "ml_GenericVillager", EntityRegistry.findGlobalUniqueEntityId());
/* 667 */       EntityRegistry.registerModEntity(MillVillager.MLEntityGenericMale.class, "ml_GenericVillager", 1, instance, 64, 3, true);
/*     */       
/* 669 */       EntityRegistry.registerGlobalEntityID(EntityMillDecoration.class, "ml_Tapestry", EntityRegistry.findGlobalUniqueEntityId());
/* 670 */       EntityRegistry.registerModEntity(EntityMillDecoration.class, "ml_Tapestry", 8, instance, 80, 100000, false);
/*     */       
/* 672 */       EntityRegistry.registerGlobalEntityID(EntityTargetedGhast.class, "MillGhast", EntityRegistry.findGlobalUniqueEntityId());
/* 673 */       EntityRegistry.registerModEntity(EntityTargetedGhast.class, "MillGhast", 9, instance, 64, 3, true);
/*     */       
/* 675 */       EntityRegistry.registerGlobalEntityID(EntityTargetedBlaze.class, "MillBlaze", EntityRegistry.findGlobalUniqueEntityId());
/* 676 */       EntityRegistry.registerModEntity(EntityTargetedBlaze.class, "MillBlaze", 10, instance, 64, 3, true);
/*     */       
/* 678 */       EntityRegistry.registerGlobalEntityID(EntityTargetedWitherSkeleton.class, "MillWitherSkeleton", EntityRegistry.findGlobalUniqueEntityId());
/* 679 */       EntityRegistry.registerModEntity(EntityTargetedWitherSkeleton.class, "MillWitherSkeleton", 11, instance, 64, 3, true);
/*     */       
/* 681 */       GameRegistry.registerBlock(lockedChest, lockedChest.func_149739_a());
/* 682 */       GameRegistry.registerBlock(panel, panel.func_149739_a());
/* 683 */       GameRegistry.registerBlock(wood_decoration, BlockDecorative.ItemDecorative.class, wood_decoration.func_149739_a());
/* 684 */       GameRegistry.registerBlock(earth_decoration, BlockDecorative.ItemDecorative.class, earth_decoration.func_149739_a());
/* 685 */       GameRegistry.registerBlock(stone_decoration, BlockDecorative.ItemDecorative.class, stone_decoration.func_149739_a());
/* 686 */       GameRegistry.registerBlock(path, BlockDecorativeSlab.ItemDecorativeSlab.class, path.func_149739_a());
/* 687 */       GameRegistry.registerBlock(pathSlab, BlockDecorativeSlab.ItemDecorativeSlab.class, pathSlab.func_149739_a());
/*     */       
/* 689 */       GameRegistry.registerBlock(cropRice, cropRice.func_149739_a());
/* 690 */       GameRegistry.registerBlock(cropTurmeric, cropTurmeric.func_149739_a());
/* 691 */       GameRegistry.registerBlock(cropMaize, cropMaize.func_149739_a());
/* 692 */       GameRegistry.registerBlock(cropVine, cropVine.func_149739_a());
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
/* 704 */       GameRegistry.registerBlock(byzantine_tiles, byzantine_tiles.func_149739_a());
/* 705 */       GameRegistry.registerBlock(byzantine_tile_slab, byzantine_tile_slab.func_149739_a());
/* 706 */       GameRegistry.registerBlock(byzantine_stone_tiles, byzantine_stone_tiles.func_149739_a());
/*     */       
/* 708 */       GameRegistry.addSmelting(lambRaw, new ItemStack(lambCooked, 1), 0.3F);
/*     */       
/* 710 */       FurnaceRecipes.func_77602_a().func_151394_a(new ItemStack(stone_decoration, 1, 1), new ItemStack(stone_decoration, 1, 0), 0.3F);
/*     */       
/* 712 */       GameRegistry.addShapelessRecipe(new ItemStack(vegcurry, 1), new Object[] { rice, turmeric });
/* 713 */       GameRegistry.addShapelessRecipe(new ItemStack(chickencurry, 1), new Object[] { rice, turmeric, Items.field_151076_bf });
/*     */       
/* 715 */       GameRegistry.addShapelessRecipe(new ItemStack(wineBasic, 1), new Object[] { grapes, grapes, grapes, grapes, grapes, grapes });
/*     */       
/* 717 */       GameRegistry.addRecipe(new ItemStack(masa, 1), new Object[] { "###", Character.valueOf('#'), maize });
/* 718 */       GameRegistry.addRecipe(new ItemStack(wah, 1), new Object[] { "#X#", Character.valueOf('#'), maize, Character.valueOf('X'), Items.field_151076_bf });
/*     */       
/* 720 */       GameRegistry.addRecipe(new ItemStack(byzantine_tile_slab, 6), new Object[] { "###", Character.valueOf('#'), byzantine_tiles });
/* 721 */       GameRegistry.addRecipe(new ItemStack(byzantine_stone_tiles, 6), new Object[] { "###", "SSS", Character.valueOf('#'), byzantine_tiles, Character.valueOf('S'), net.minecraft.init.Blocks.field_150348_b });
/*     */       
/* 723 */       for (int meta = 0; meta < 16; meta++) {
/* 724 */         GameRegistry.addRecipe(new ItemStack(pathSlab, 3, meta), new Object[] { "xxx", Character.valueOf('x'), new ItemStack(path, 1, meta) });
/*     */       }
/*     */       
/* 727 */       GameRegistry.registerBlock(paperWall, paperWall.func_149739_a());
/*     */       
/* 729 */       Field[] fields = Mill.class.getFields();
/*     */       
/* 731 */       for (Field f : fields) {
/* 732 */         if (f.getType().isAssignableFrom(Item.class)) {
/* 733 */           Item item = (Item)f.get(this);
/* 734 */           GameRegistry.registerItem(item, item.func_77658_a());
/*     */         }
/*     */       }
/* 737 */       GameRegistry.registerItem(clothes, clothes.func_77658_a());
/* 738 */       GameRegistry.registerItem(purse, purse.func_77658_a());
/*     */       
/* 740 */       proxy.registerTileEntities();
/* 741 */       proxy.registerGraphics();
/* 742 */       proxy.preloadTextures();
/*     */     }
/*     */     catch (Exception e) {
/* 745 */       MLN.error(this, "Exception in mod initialisation: ");
/* 746 */       MLN.printException(e);
/*     */     }
/*     */   }
/*     */   
/*     */   @Mod.EventHandler
/*     */   public void serverLoad(FMLServerStartingEvent event) {}
/*     */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\forge\Mill.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */