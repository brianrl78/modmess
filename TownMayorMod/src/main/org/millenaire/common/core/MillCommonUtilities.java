/*      */ package org.millenaire.common.core;
/*      */ 
/*      */ import com.google.common.collect.Multimap;
/*      */ import cpw.mods.fml.common.registry.GameRegistry;
/*      */ import cpw.mods.fml.common.registry.GameRegistry.UniqueIdentifier;
/*      */ import cpw.mods.fml.relauncher.ReflectionHelper;
/*      */ import java.io.BufferedReader;
/*      */ import java.io.BufferedWriter;
/*      */ import java.io.File;
/*      */ import java.io.FileNotFoundException;
/*      */ import java.io.FileOutputStream;
/*      */ import java.io.FilenameFilter;
/*      */ import java.io.InputStream;
/*      */ import java.io.InputStreamReader;
/*      */ import java.io.UnsupportedEncodingException;
/*      */ import java.net.URL;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Random;
/*      */ import net.minecraft.block.Block;
/*      */ import net.minecraft.block.Block.SoundType;
/*      */ import net.minecraft.block.BlockSlab;
/*      */ import net.minecraft.block.BlockStairs;
/*      */ import net.minecraft.client.gui.inventory.GuiContainer;
/*      */ import net.minecraft.client.renderer.texture.IIconRegister;
/*      */ import net.minecraft.entity.Entity;
/*      */ import net.minecraft.entity.EntityCreature;
/*      */ import net.minecraft.entity.EntityList;
/*      */ import net.minecraft.entity.EntityLiving;
/*      */ import net.minecraft.entity.SharedMonsterAttributes;
/*      */ import net.minecraft.entity.ai.attributes.AttributeModifier;
/*      */ import net.minecraft.entity.ai.attributes.IAttribute;
/*      */ import net.minecraft.entity.item.EntityItem;
/*      */ import net.minecraft.entity.item.EntityXPOrb;
/*      */ import net.minecraft.entity.player.EntityPlayer;
/*      */ import net.minecraft.entity.player.InventoryPlayer;
/*      */ import net.minecraft.init.Blocks;
/*      */ import net.minecraft.inventory.IInventory;
/*      */ import net.minecraft.item.Item;
/*      */ import net.minecraft.item.ItemStack;
/*      */ import net.minecraft.nbt.NBTTagCompound;
/*      */ import net.minecraft.nbt.NBTTagList;
/*      */ import net.minecraft.util.AxisAlignedBB;
/*      */ import net.minecraft.util.RegistryNamespaced;
/*      */ import net.minecraft.world.World;
/*      */ import net.minecraft.world.storage.SaveHandler;
/*      */ import org.millenaire.common.Culture;
/*      */ import org.millenaire.common.InvItem;
/*      */ import org.millenaire.common.MLN;
/*      */ import org.millenaire.common.MLN.MillenaireException;
/*      */ import org.millenaire.common.MillVillager;
/*      */ import org.millenaire.common.MillWorld;
/*      */ import org.millenaire.common.Point;
/*      */ import org.millenaire.common.UserProfile;
/*      */ import org.millenaire.common.building.Building;
/*      */ import org.millenaire.common.building.BuildingBlock;
/*      */ import org.millenaire.common.building.BuildingLocation;
/*      */ import org.millenaire.common.forge.CommonProxy;
/*      */ import org.millenaire.common.forge.Mill;
/*      */ import org.millenaire.common.forge.MillAchievements;
/*      */ import org.millenaire.common.item.Goods.IItemInitialEnchantmens;
/*      */ import org.millenaire.common.item.ItemPurse;
/*      */ import org.millenaire.common.pathing.atomicstryker.AStarNode;
/*      */ import org.millenaire.common.pathing.atomicstryker.AStarStatic;
/*      */ 
/*      */ public class MillCommonUtilities
/*      */ {
/*      */   public static class BonusThread extends Thread
/*      */   {
/*      */     String login;
/*      */     
/*      */     public BonusThread(String login)
/*      */     {
/*   78 */       this.login = login;
/*      */     }
/*      */     
/*      */     public void run()
/*      */     {
/*      */       try {
/*   84 */         InputStream stream = new URL("http://millenaire.org/php/bonuscheck.php?login=" + this.login).openStream();
/*      */         
/*   86 */         BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
/*      */         
/*   88 */         String result = reader.readLine();
/*      */         
/*   90 */         if (result.trim().equals("thik hai")) {
/*   91 */           MLN.bonusEnabled = true;
/*   92 */           MLN.bonusCode = MLN.calculateLoginMD5(this.login);
/*      */           
/*   94 */           MLN.writeConfigFile();
/*      */         }
/*      */       }
/*      */       catch (Exception e) {}
/*      */     }
/*      */   }
/*      */   
/*      */   public static class ExtFileFilter
/*      */     implements FilenameFilter
/*      */   {
/*  104 */     String ext = null;
/*      */     
/*      */     public ExtFileFilter(String ext) {
/*  107 */       this.ext = ext;
/*      */     }
/*      */     
/*      */ 
/*      */     public boolean accept(File file, String name)
/*      */     {
/*  113 */       if (!name.toLowerCase().endsWith("." + this.ext)) {
/*  114 */         return false;
/*      */       }
/*      */       
/*  117 */       if (name.startsWith(".")) {
/*  118 */         return false;
/*      */       }
/*      */       
/*  121 */       return true;
/*      */     }
/*      */   }
/*      */   
/*      */   private static class LogThread extends Thread {
/*      */     String url;
/*      */     
/*      */     public LogThread(String url) {
/*  129 */       this.url = url;
/*      */     }
/*      */     
/*      */     public void run()
/*      */     {
/*      */       try {
/*  135 */         InputStream stream = new URL(this.url).openStream();
/*  136 */         stream.close();
/*      */       }
/*      */       catch (Exception e) {}
/*      */     }
/*      */   }
/*      */   
/*      */   public static class PrefixExtFileFilter implements FilenameFilter
/*      */   {
/*  144 */     String ext = null;
/*  145 */     String prefix = null;
/*      */     
/*      */     public PrefixExtFileFilter(String pref, String ext) {
/*  148 */       this.ext = ext;
/*  149 */       this.prefix = pref;
/*      */     }
/*      */     
/*      */ 
/*      */     public boolean accept(File file, String name)
/*      */     {
/*  155 */       if (!name.toLowerCase().endsWith("." + this.ext)) {
/*  156 */         return false;
/*      */       }
/*      */       
/*  159 */       if (!name.toLowerCase().startsWith(this.prefix)) {
/*  160 */         return false;
/*      */       }
/*      */       
/*  163 */       if (name.startsWith(".")) {
/*  164 */         return false;
/*      */       }
/*      */       
/*  167 */       return true;
/*      */     }
/*      */   }
/*      */   
/*      */   public static class VillageInfo implements Comparable<VillageInfo>
/*      */   {
/*      */     public String textKey;
/*      */     public String[] values;
/*      */     public int distance;
/*      */     
/*      */     public int compareTo(VillageInfo arg0) {
/*  178 */       return arg0.distance - this.distance;
/*      */     }
/*      */     
/*      */     public boolean equals(Object o)
/*      */     {
/*  183 */       if ((o == null) || (!(o instanceof VillageInfo))) {
/*  184 */         return false;
/*      */       }
/*      */       
/*  187 */       return this.distance == ((VillageInfo)o).distance;
/*      */     }
/*      */     
/*      */     public int hashCode()
/*      */     {
/*  192 */       return super.hashCode();
/*      */     }
/*      */   }
/*      */   
/*      */   public static class VillageList
/*      */   {
/*  198 */     public List<Point> pos = new ArrayList();
/*  199 */     public List<String> names = new ArrayList();
/*  200 */     public List<String> types = new ArrayList();
/*  201 */     public List<String> cultures = new ArrayList();
/*  202 */     public List<String> generatedFor = new ArrayList();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public void addVillage(Point p, String name, String type, String culture, String generatedFor)
/*      */     {
/*  209 */       this.pos.add(p);
/*  210 */       this.names.add(name);
/*  211 */       this.types.add(type);
/*  212 */       this.cultures.add(culture);
/*  213 */       this.generatedFor.add(generatedFor);
/*      */     }
/*      */     
/*      */     public void removeVillage(Point p)
/*      */     {
/*  218 */       int id = -1;
/*      */       
/*  220 */       for (int i = 0; (i < this.pos.size()) && (id == -1); i++) {
/*  221 */         if (p.sameBlock((Point)this.pos.get(i))) {
/*  222 */           id = i;
/*      */         }
/*      */       }
/*      */       
/*  226 */       if (id != -1) {
/*  227 */         this.pos.remove(id);
/*  228 */         this.names.remove(id);
/*  229 */         this.types.remove(id);
/*  230 */         this.cultures.remove(id);
/*  231 */         this.generatedFor.remove(id);
/*      */       }
/*      */     }
/*      */     
/*      */     public void reset() {
/*  236 */       this.pos.clear();
/*  237 */       this.names.clear();
/*  238 */       this.types.clear();
/*  239 */       this.cultures.clear();
/*  240 */       this.generatedFor.clear();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  251 */   public static Random random = new Random();
/*      */   private static final boolean PATH_RAISE = false;
/*      */   
/*      */   private static boolean attemptPathBuild(Building th, World world, List<BuildingBlock> pathPoints, Point p, Block pathBlock, int pathMeta)
/*      */   {
/*  256 */     Block block = p.getBlock(world);
/*  257 */     int meta = p.getMeta(world);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  263 */     if (th.isPointProtectedFromPathBuilding(p)) {
/*  264 */       return false;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  270 */     if ((p.getRelative(0.0D, 2.0D, 0.0D).isBlockPassable(world)) && (p.getAbove().isBlockPassable(world)) && (canPathBeBuiltHere(block, meta)))
/*      */     {
/*  272 */       pathPoints.add(new BuildingBlock(p, pathBlock, pathMeta));
/*  273 */       return true;
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
/*  290 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */   public static List<BuildingBlock> buildPath(Building th, List<AStarNode> path, Block pathBlock, int pathMeta, int pathWidth)
/*      */   {
/*  296 */     List<BuildingBlock> pathPoints = new ArrayList();
/*      */     
/*  298 */     boolean lastNodeHalfSlab = false;
/*      */     
/*  300 */     boolean[] pathShouldBuild = new boolean[path.size()];
/*  301 */     for (int ip = 0; ip < path.size(); ip++) {
/*  302 */       pathShouldBuild[ip] = true;
/*      */     }
/*      */     
/*  305 */     for (int ip = 0; ip < path.size(); ip++)
/*      */     {
/*  307 */       AStarNode node = (AStarNode)path.get(ip);
/*  308 */       Point p = new Point(node);
/*  309 */       BuildingLocation l = th.getLocationAtCoordPlanar(p);
/*      */       
/*  311 */       if (l != null)
/*      */       {
/*  313 */         if (ip == 0) {
/*  314 */           pathShouldBuild[ip] = true;
/*  315 */           clearPathForward(path, pathShouldBuild, th, l, ip);
/*  316 */         } else if (ip == path.size() - 1) {
/*  317 */           pathShouldBuild[ip] = true;
/*  318 */           clearPathBackward(path, pathShouldBuild, th, l, ip);
/*      */         } else {
/*  320 */           boolean stablePath = isPointOnStablePath(p, th.worldObj);
/*  321 */           if (stablePath) {
/*  322 */             pathShouldBuild[ip] = true;
/*  323 */             clearPathBackward(path, pathShouldBuild, th, l, ip);
/*  324 */             clearPathForward(path, pathShouldBuild, th, l, ip);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  330 */     for (int ip = 0; ip < path.size(); ip++)
/*      */     {
/*  332 */       if (pathShouldBuild[ip] != 0)
/*      */       {
/*  334 */         AStarNode node = (AStarNode)path.get(ip);
/*  335 */         AStarNode lastNode = null;AStarNode nextNode = null;
/*      */         
/*  337 */         if (ip > 0) {
/*  338 */           lastNode = (AStarNode)path.get(ip - 1);
/*      */         }
/*      */         
/*  341 */         if (ip + 1 < path.size()) {
/*  342 */           nextNode = (AStarNode)path.get(ip + 1);
/*      */         }
/*      */         
/*  345 */         boolean halfSlab = false;
/*      */         
/*  347 */         if ((lastNode != null) && (nextNode != null))
/*      */         {
/*  349 */           Point p = new Point(node);
/*  350 */           Point nextp = new Point(nextNode);
/*  351 */           Point lastp = new Point(lastNode);
/*      */           
/*      */ 
/*  354 */           if ((!isStairsOrSlabOrChest(th.worldObj, nextp.getBelow())) && (!isStairsOrSlabOrChest(th.worldObj, lastp.getBelow()))) { if (((p.x == lastp.x) && (p.x == nextp.x)) || ((p.z != lastp.z) || (p.z == nextp.z)) || (
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
/*  365 */               (lastNode.y == nextNode.y) && (node.y < lastNode.y) && (p.getRelative(0.0D, lastNode.y - node.y, 0.0D).isBlockPassable(th.worldObj)) && (p.getRelative(0.0D, lastNode.y - node.y + 1, 0.0D).isBlockPassable(th.worldObj))))
/*      */             {
/*  367 */               halfSlab = true;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             }
/*  377 */             else if ((!lastNodeHalfSlab) && (node.y == lastNode.y) && (node.y > nextNode.y)) {
/*  378 */               halfSlab = true;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             }
/*  388 */             else if ((!lastNodeHalfSlab) && (node.y == nextNode.y) && (node.y > lastNode.y)) {
/*  389 */               halfSlab = true;
/*      */             }
/*      */           }
/*      */           else {
/*  393 */             Block block = p.getBelow().getBlock(th.worldObj);
/*      */             
/*  395 */             if (block == Mill.pathSlab) {
/*  396 */               halfSlab = true;
/*      */             }
/*      */           }
/*      */         }
/*      */         
/*  401 */         Point p = new Point(node).getBelow();
/*      */         
/*  403 */         Block nodePathBlock = pathBlock;
/*      */         
/*  405 */         if ((nodePathBlock == Mill.path) && (halfSlab)) {
/*  406 */           nodePathBlock = Mill.pathSlab;
/*      */         }
/*      */         
/*  409 */         attemptPathBuild(th, th.worldObj, pathPoints, p, nodePathBlock, pathMeta);
/*      */         
/*  411 */         if (lastNode != null) {
/*  412 */           int dx = p.getiX() - lastNode.x;
/*  413 */           int dz = p.getiZ() - lastNode.z;
/*      */           
/*  415 */           int nbPass = 1;
/*      */           
/*  417 */           if ((dx != 0) && (dz != 0)) {
/*  418 */             nbPass = 2;
/*      */           }
/*      */           
/*  421 */           for (int i = 0; i < nbPass; i++)
/*      */           {
/*      */ 
/*  424 */             int direction = i == 0 ? 1 : -1;
/*      */             
/*  426 */             Point secondPoint = null;Point secondPointAlternate = null;Point thirdPoint = null;
/*      */             
/*      */ 
/*      */ 
/*  430 */             if (pathWidth > 1) {
/*  431 */               if ((dx == 0) && (direction == 1)) {
/*  432 */                 secondPoint = p.getRelative(direction, 0.0D, 0.0D);
/*  433 */                 secondPointAlternate = p.getRelative(-direction, 0.0D, 0.0D);
/*  434 */               } else if ((dz == 0) && (direction == 1)) {
/*  435 */                 secondPoint = p.getRelative(0.0D, 0.0D, direction);
/*  436 */                 secondPointAlternate = p.getRelative(0.0D, 0.0D, -direction);
/*      */               } else {
/*  438 */                 secondPoint = p.getRelative(dx * direction, 0.0D, 0.0D);
/*  439 */                 thirdPoint = p.getRelative(0.0D, 0.0D, dz * direction);
/*      */               }
/*      */               
/*      */             }
/*  443 */             else if ((dx != 0) && (dz != 0)) {
/*  444 */               secondPoint = p.getRelative(dx * direction, 0.0D, 0.0D);
/*  445 */               secondPointAlternate = p.getRelative(0.0D, 0.0D, dz * direction);
/*      */             }
/*      */             
/*      */ 
/*  449 */             if (secondPoint != null) {
/*  450 */               boolean success = attemptPathBuild(th, th.worldObj, pathPoints, secondPoint, nodePathBlock, pathMeta);
/*      */               
/*  452 */               if ((!success) && (secondPointAlternate != null)) {
/*  453 */                 attemptPathBuild(th, th.worldObj, pathPoints, secondPointAlternate, nodePathBlock, pathMeta);
/*      */               }
/*      */             }
/*      */             
/*  457 */             if (thirdPoint != null) {
/*  458 */               attemptPathBuild(th, th.worldObj, pathPoints, thirdPoint, nodePathBlock, pathMeta);
/*      */             }
/*      */           }
/*      */         }
/*      */         
/*  463 */         lastNodeHalfSlab = halfSlab;
/*      */       } else {
/*  465 */         lastNodeHalfSlab = false;
/*      */       }
/*      */     }
/*      */     
/*  469 */     return pathPoints;
/*      */   }
/*      */   
/*      */   public static boolean canPathBeBuiltHere(Block block, int meta)
/*      */   {
/*  474 */     return (block == Blocks.field_150346_d) || (block == Blocks.field_150349_c) || (block == Blocks.field_150354_m) || (block == Blocks.field_150351_n) || (((block != Mill.path) && (block != Mill.pathSlab)) || ((meta < 8) || (block == Blocks.field_150327_N) || (block == Blocks.field_150328_O) || (block == Blocks.field_150338_P) || (block == Blocks.field_150337_Q) || (block == Blocks.field_150329_H) || (block == Blocks.field_150330_I)));
/*      */   }
/*      */   
/*      */ 
/*      */   public static boolean canPathBeBuiltOnTopOfThis(Block block, int meta)
/*      */   {
/*  480 */     return (block == Blocks.field_150346_d) || (block == Blocks.field_150349_c) || (block == Blocks.field_150354_m) || (block == Blocks.field_150351_n) || (((block != Mill.path) && (block != Mill.pathSlab)) || ((meta < 8) || (block == Blocks.field_150348_b) || (block == Blocks.field_150322_A)));
/*      */   }
/*      */   
/*      */ 
/*      */   public static boolean canStandInBlock(World world, Point p)
/*      */   {
/*  486 */     if (!AStarStatic.isPassableBlock(world, p.getiX(), p.getiY(), p.getiZ(), MillVillager.DEFAULT_JPS_CONFIG)) {
/*  487 */       return false;
/*      */     }
/*      */     
/*  490 */     p = p.getAbove();
/*      */     
/*  492 */     if (!AStarStatic.isPassableBlock(world, p.getiX(), p.getiY(), p.getiZ(), MillVillager.DEFAULT_JPS_CONFIG)) {
/*  493 */       return false;
/*      */     }
/*      */     
/*  496 */     p = p.getRelative(0.0D, -2.0D, 0.0D);
/*      */     
/*  498 */     if (AStarStatic.isPassableBlock(world, p.getiX(), p.getiY(), p.getiZ(), MillVillager.DEFAULT_JPS_CONFIG)) {
/*  499 */       return false;
/*      */     }
/*      */     
/*  502 */     return true;
/*      */   }
/*      */   
/*      */   public static boolean chanceOn(int i) {
/*  506 */     return getRandom().nextInt(i) == 0;
/*      */   }
/*      */   
/*      */   public static void changeMoney(IInventory chest, int toChange, EntityPlayer player)
/*      */   {
/*  511 */     boolean hasPurse = false;
/*      */     
/*  513 */     for (int i = 0; (i < chest.func_70302_i_()) && (!hasPurse); i++) {
/*  514 */       ItemStack stack = chest.func_70301_a(i);
/*  515 */       if ((stack != null) && 
/*  516 */         (stack.func_77973_b() == Mill.purse)) {
/*  517 */         hasPurse = true;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  522 */     if (hasPurse)
/*      */     {
/*  524 */       int current_denier = getItemsFromChest(chest, Mill.denier, 0, Integer.MAX_VALUE);
/*  525 */       int current_denier_argent = getItemsFromChest(chest, Mill.denier_argent, 0, Integer.MAX_VALUE);
/*  526 */       int current_denier_or = getItemsFromChest(chest, Mill.denier_or, 0, Integer.MAX_VALUE);
/*      */       
/*  528 */       int finalChange = current_denier_or * 64 * 64 + current_denier_argent * 64 + current_denier + toChange;
/*      */       
/*  530 */       for (int i = 0; (i < chest.func_70302_i_()) && (finalChange != 0); i++) {
/*  531 */         ItemStack stack = chest.func_70301_a(i);
/*  532 */         if ((stack != null) && 
/*  533 */           (stack.func_77973_b() == Mill.purse)) {
/*  534 */           int content = Mill.purse.totalDeniers(stack) + finalChange;
/*      */           
/*  536 */           if (content >= 0) {
/*  537 */             Mill.purse.setDeniers(stack, player, content);
/*  538 */             finalChange = 0;
/*      */           } else {
/*  540 */             Mill.purse.setDeniers(stack, player, 0);
/*  541 */             finalChange = content;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/*  548 */       int total = toChange + countMoney(chest);
/*      */       
/*  550 */       int denier = total % 64;
/*  551 */       int denier_argent = (total - denier) / 64 % 64;
/*  552 */       int denier_or = (total - denier - denier_argent * 64) / 4096;
/*      */       
/*  554 */       if ((player != null) && (denier_or > 0)) {
/*  555 */         player.func_71064_a(MillAchievements.cresus, 1);
/*      */       }
/*      */       
/*  558 */       int current_denier = countChestItems(chest, Mill.denier, 0);
/*  559 */       int current_denier_argent = countChestItems(chest, Mill.denier_argent, 0);
/*  560 */       int current_denier_or = countChestItems(chest, Mill.denier_or, 0);
/*      */       
/*  562 */       if (MLN.LogWifeAI >= 1) {
/*  563 */         MLN.major(null, "Putting: " + denier + "/" + denier_argent + "/" + denier_or + " replacing " + current_denier + "/" + current_denier_argent + "/" + current_denier_or);
/*      */       }
/*      */       
/*  566 */       if (denier < current_denier) {
/*  567 */         getItemsFromChest(chest, Mill.denier, 0, current_denier - denier);
/*  568 */       } else if (denier > current_denier) {
/*  569 */         putItemsInChest(chest, Mill.denier, 0, denier - current_denier);
/*      */       }
/*      */       
/*  572 */       if (denier_argent < current_denier_argent) {
/*  573 */         getItemsFromChest(chest, Mill.denier_argent, 0, current_denier_argent - denier_argent);
/*  574 */       } else if (denier_argent > current_denier_argent) {
/*  575 */         putItemsInChest(chest, Mill.denier_argent, 0, denier_argent - current_denier_argent);
/*      */       }
/*      */       
/*  578 */       if (denier_or < current_denier_or) {
/*  579 */         getItemsFromChest(chest, Mill.denier_or, 0, current_denier_or - denier_or);
/*  580 */       } else if (denier_or > current_denier_or) {
/*  581 */         putItemsInChest(chest, Mill.denier_or, 0, denier_or - current_denier_or);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public static void changeMoneyObsolete(IInventory chest, int toChange, EntityPlayer player)
/*      */   {
/*  589 */     int total = toChange + countMoney(chest);
/*      */     
/*  591 */     int denier = total % 64;
/*  592 */     int denier_argent = (total - denier) / 64 % 64;
/*  593 */     int denier_or = (total - denier - denier_argent * 64) / 4096;
/*      */     
/*  595 */     if ((player != null) && (denier_or > 0)) {
/*  596 */       player.func_71064_a(MillAchievements.cresus, 1);
/*      */     }
/*      */     
/*  599 */     boolean placedInPurse = false;
/*      */     
/*  601 */     for (int i = 0; i < chest.func_70302_i_(); i++) {
/*  602 */       ItemStack stack = chest.func_70301_a(i);
/*  603 */       if ((stack != null) && 
/*  604 */         (stack.func_77973_b() == Mill.purse)) {
/*  605 */         if (!placedInPurse) {
/*  606 */           Mill.purse.setDeniers(stack, player, denier, denier_argent, denier_or);
/*  607 */           placedInPurse = true;
/*      */         } else {
/*  609 */           Mill.purse.setDeniers(stack, player, 0, 0, 0);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  615 */     if (placedInPurse) {
/*  616 */       getItemsFromChest(chest, Mill.denier, 0, Integer.MAX_VALUE);
/*  617 */       getItemsFromChest(chest, Mill.denier_argent, 0, Integer.MAX_VALUE);
/*  618 */       getItemsFromChest(chest, Mill.denier_or, 0, Integer.MAX_VALUE);
/*      */     }
/*      */     else {
/*  621 */       int current_denier = countChestItems(chest, Mill.denier, 0);
/*  622 */       int current_denier_argent = countChestItems(chest, Mill.denier_argent, 0);
/*  623 */       int current_denier_or = countChestItems(chest, Mill.denier_or, 0);
/*      */       
/*  625 */       if (MLN.LogWifeAI >= 1) {
/*  626 */         MLN.major(null, "Putting: " + denier + "/" + denier_argent + "/" + denier_or + " replacing " + current_denier + "/" + current_denier_argent + "/" + current_denier_or);
/*      */       }
/*      */       
/*  629 */       if (denier < current_denier) {
/*  630 */         getItemsFromChest(chest, Mill.denier, 0, current_denier - denier);
/*  631 */       } else if (denier > current_denier) {
/*  632 */         putItemsInChest(chest, Mill.denier, 0, denier - current_denier);
/*      */       }
/*      */       
/*  635 */       if (denier_argent < current_denier_argent) {
/*  636 */         getItemsFromChest(chest, Mill.denier_argent, 0, current_denier_argent - denier_argent);
/*  637 */       } else if (denier_argent > current_denier_argent) {
/*  638 */         putItemsInChest(chest, Mill.denier_argent, 0, denier_argent - current_denier_argent);
/*      */       }
/*      */       
/*  641 */       if (denier_or < current_denier_or) {
/*  642 */         getItemsFromChest(chest, Mill.denier_or, 0, current_denier_or - denier_or);
/*  643 */       } else if (denier_or > current_denier_or) {
/*  644 */         putItemsInChest(chest, Mill.denier_or, 0, denier_or - current_denier_or);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private static void clearPathBackward(List<AStarNode> path, boolean[] pathShouldBuild, Building th, BuildingLocation l, int index) {
/*  650 */     boolean exit = false;
/*  651 */     boolean leadsToBorder = false;
/*  652 */     for (int i = index - 1; (i >= 0) && (!exit); i--) {
/*  653 */       Point np = new Point((AStarNode)path.get(i));
/*  654 */       BuildingLocation l2 = th.getLocationAtCoordPlanar(np);
/*      */       
/*  656 */       if (l2 != l) {
/*  657 */         leadsToBorder = true;
/*  658 */         exit = true;
/*  659 */       } else if (isPointOnStablePath(np, th.worldObj)) {
/*  660 */         exit = true;
/*      */       }
/*      */     }
/*      */     
/*  664 */     if (!leadsToBorder) {
/*  665 */       exit = false;
/*  666 */       for (int i = index - 1; (i >= 0) && (!exit); i--) {
/*  667 */         Point np = new Point((AStarNode)path.get(i));
/*  668 */         BuildingLocation l2 = th.getLocationAtCoordPlanar(np);
/*      */         
/*  670 */         if (l2 != l) {
/*  671 */           exit = true;
/*  672 */         } else if (isPointOnStablePath(np, th.worldObj)) {
/*  673 */           exit = true;
/*      */         } else {
/*  675 */           pathShouldBuild[i] = false;
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private static void clearPathForward(List<AStarNode> path, boolean[] pathShouldBuild, Building th, BuildingLocation l, int index) {
/*  682 */     boolean exit = false;
/*  683 */     boolean leadsToBorder = false;
/*  684 */     for (int i = index + 1; (i < path.size()) && (!exit); i++) {
/*  685 */       Point np = new Point((AStarNode)path.get(i));
/*  686 */       BuildingLocation l2 = th.getLocationAtCoordPlanar(np);
/*      */       
/*  688 */       if (l2 != l) {
/*  689 */         leadsToBorder = true;
/*  690 */         exit = true;
/*  691 */       } else if (isPointOnStablePath(np, th.worldObj)) {
/*  692 */         exit = true;
/*      */       }
/*      */     }
/*      */     
/*  696 */     if (!leadsToBorder) {
/*  697 */       exit = false;
/*  698 */       for (int i = index + 1; (i < path.size()) && (!exit); i++) {
/*  699 */         Point np = new Point((AStarNode)path.get(i));
/*  700 */         BuildingLocation l2 = th.getLocationAtCoordPlanar(np);
/*      */         
/*  702 */         if (l2 != l) {
/*  703 */           exit = true;
/*  704 */         } else if (isPointOnStablePath(np, th.worldObj)) {
/*  705 */           exit = true;
/*      */         } else {
/*  707 */           pathShouldBuild[i] = false;
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public static int countBlocksAround(World world, int x, int y, int z, int rx, int ry, int rz) {
/*  714 */     int counter = 0;
/*      */     
/*  716 */     for (int i = x - rx; i <= x + rx; i++) {
/*  717 */       for (int j = y - ry; j <= y + ry; j++) {
/*  718 */         for (int k = z - rz; k <= z + rz; k++)
/*      */         {
/*      */ 
/*  721 */           if ((world.func_147439_a(i, j, k) != null) && (world.func_147439_a(i, j, k).func_149688_o().func_76230_c())) {
/*  722 */             counter++;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  728 */     return counter;
/*      */   }
/*      */   
/*      */   public static int countChestItems(IInventory chest, Block block, int meta)
/*      */   {
/*  733 */     return countChestItems(chest, Item.func_150898_a(block), meta);
/*      */   }
/*      */   
/*      */   public static int countChestItems(IInventory chest, Item item, int meta) {
/*  737 */     if (chest == null) {
/*  738 */       return 0;
/*      */     }
/*      */     
/*  741 */     int maxSlot = chest.func_70302_i_();
/*      */     
/*  743 */     if ((chest instanceof InventoryPlayer)) {
/*  744 */       maxSlot -= 4;
/*      */     }
/*      */     
/*  747 */     int nb = 0;
/*      */     
/*  749 */     for (int i = 0; i < maxSlot; i++) {
/*  750 */       ItemStack stack = chest.func_70301_a(i);
/*  751 */       if ((stack != null) && (stack.func_77973_b() == item) && ((meta == -1) || (stack.func_77960_j() < 0) || (stack.func_77960_j() == meta))) {
/*  752 */         nb += stack.field_77994_a;
/*      */       }
/*      */     }
/*      */     
/*  756 */     return nb;
/*      */   }
/*      */   
/*      */   public static int countFurnaceItems(IInventory furnace, Item item, int meta) {
/*  760 */     if (furnace == null) {
/*  761 */       return 0;
/*      */     }
/*      */     
/*  764 */     int nb = 0;
/*      */     
/*  766 */     ItemStack stack = furnace.func_70301_a(2);
/*  767 */     if ((stack != null) && (stack.func_77973_b() == item) && ((meta == -1) || (stack.func_77960_j() < 0) || (stack.func_77960_j() == meta))) {
/*  768 */       nb += stack.field_77994_a;
/*      */     }
/*      */     
/*  771 */     return nb;
/*      */   }
/*      */   
/*      */   public static int countMoney(IInventory chest)
/*      */   {
/*  776 */     int deniers = 0;
/*      */     
/*  778 */     for (int i = 0; i < chest.func_70302_i_(); i++) {
/*  779 */       ItemStack stack = chest.func_70301_a(i);
/*  780 */       if (stack != null) {
/*  781 */         if (stack.func_77973_b() == Mill.purse) {
/*  782 */           deniers += Mill.purse.totalDeniers(stack);
/*  783 */         } else if (stack.func_77973_b() == Mill.denier) {
/*  784 */           deniers += stack.field_77994_a;
/*  785 */         } else if (stack.func_77973_b() == Mill.denier_argent) {
/*  786 */           deniers += stack.field_77994_a * 64;
/*  787 */         } else if (stack.func_77973_b() == Mill.denier_or) {
/*  788 */           deniers += stack.field_77994_a * 64 * 64;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  794 */     return deniers;
/*      */   }
/*      */   
/*      */   public static boolean deleteDir(File dir) {
/*  798 */     if (dir.isDirectory()) {
/*  799 */       String[] children = dir.list();
/*  800 */       for (int i = 0; i < children.length; i++) {
/*  801 */         boolean success = deleteDir(new File(dir, children[i]));
/*  802 */         if (!success) {
/*  803 */           return false;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  809 */     return dir.delete();
/*      */   }
/*      */   
/*      */   public static Point findRandomStandingPosAround(World world, Point dest)
/*      */   {
/*  814 */     if (dest == null) {
/*  815 */       return null;
/*      */     }
/*      */     
/*  818 */     for (int i = 0; i < 50; i++) {
/*  819 */       dest = dest.getRelative(5 - randomInt(10), 5 - randomInt(20), 5 - randomInt(10));
/*      */       
/*  821 */       if ((isBlockIdSolid(world.func_147439_a(dest.getiX(), dest.getiY() - 1, dest.getiZ()))) && (!isBlockIdSolid(world.func_147439_a(dest.getiX(), dest.getiY(), dest.getiZ()))) && (!isBlockIdSolid(world.func_147439_a(dest.getiX(), dest.getiY() + 1, dest.getiZ()))))
/*      */       {
/*  823 */         return dest;
/*      */       }
/*      */     }
/*      */     
/*  827 */     return null;
/*      */   }
/*      */   
/*      */   public static Point findTopNonPassableBlock(World world, int x, int z) {
/*  831 */     for (int y = 255; y > 0; y--) {
/*  832 */       if (!AStarStatic.isPassableBlock(world, x, y, z, MillVillager.DEFAULT_JPS_CONFIG)) {
/*  833 */         return new Point(x, y, z);
/*      */       }
/*      */     }
/*      */     
/*  837 */     return null;
/*      */   }
/*      */   
/*      */   public static int findTopSoilBlock(World world, int x, int z)
/*      */   {
/*  842 */     int y = world.func_72825_h(x, z);
/*      */     
/*  844 */     while ((y > -1) && (!isBlockIdGround(world.func_147439_a(x, y, z)))) {
/*  845 */       y--;
/*      */     }
/*      */     
/*  848 */     if (y > 254) {
/*  849 */       y = 254;
/*      */     }
/*      */     
/*  852 */     return y + 1;
/*      */   }
/*      */   
/*      */   public static Point findVerticalStandingPos(World world, Point dest)
/*      */   {
/*  857 */     if (dest == null) {
/*  858 */       return null;
/*      */     }
/*      */     
/*  861 */     int y = dest.getiY();
/*  862 */     while ((y < 250) && ((isBlockIdSolid(world.func_147439_a(dest.getiX(), y, dest.getiZ()))) || (isBlockIdSolid(world.func_147439_a(dest.getiX(), y + 1, dest.getiZ()))))) {
/*  863 */       y++;
/*      */     }
/*      */     
/*  866 */     if (y == 250) {
/*  867 */       return null;
/*      */     }
/*      */     
/*  870 */     return new Point(dest.getiX(), y, dest.getiZ());
/*      */   }
/*      */   
/*      */   public static void generateHearts(Entity ent) {
/*  874 */     for (int var3 = 0; var3 < 7; var3++) {
/*  875 */       double var4 = random.nextGaussian() * 0.02D;
/*  876 */       double var6 = random.nextGaussian() * 0.02D;
/*  877 */       double var8 = random.nextGaussian() * 0.02D;
/*      */       
/*  879 */       ent.field_70170_p.func_72869_a("heart", ent.field_70165_t + random.nextFloat() * ent.field_70130_N * 2.0F - ent.field_70130_N, ent.field_70163_u + 0.5D + random.nextFloat() * ent.field_70131_O, ent.field_70161_v + random.nextFloat() * ent.field_70130_N * 2.0F - ent.field_70130_N, var4, var6, var8);
/*      */     }
/*      */   }
/*      */   
/*      */   public static BufferedWriter getAppendWriter(File file) throws UnsupportedEncodingException, FileNotFoundException
/*      */   {
/*  885 */     return new BufferedWriter(new java.io.OutputStreamWriter(new FileOutputStream(file, true), "UTF8"));
/*      */   }
/*      */   
/*      */   public static Block getBlock(World world, Point p) {
/*  889 */     if ((p.x < -3.2E7D) || (p.z < -3.2E7D) || (p.x >= 3.2E7D) || (p.z > 3.2E7D)) {
/*  890 */       return null;
/*      */     }
/*  892 */     if (p.y < 0.0D) {
/*  893 */       return null;
/*      */     }
/*  895 */     if (p.y >= 256.0D) {
/*  896 */       return null;
/*      */     }
/*      */     
/*  899 */     return world.func_147439_a(p.getiX(), p.getiY(), p.getiZ());
/*      */   }
/*      */   
/*      */   public static int getBlockId(Block b) {
/*  903 */     return Block.field_149771_c.func_148757_b(b);
/*      */   }
/*      */   
/*      */   public static Block getBlockIdValidGround(Block b, boolean surface)
/*      */   {
/*  908 */     if (b == Blocks.field_150357_h)
/*  909 */       return Blocks.field_150346_d;
/*  910 */     if ((b == Blocks.field_150348_b) && (surface))
/*  911 */       return Blocks.field_150346_d;
/*  912 */     if ((b == Blocks.field_150348_b) && (!surface))
/*  913 */       return Blocks.field_150348_b;
/*  914 */     if (b == Blocks.field_150346_d)
/*  915 */       return Blocks.field_150346_d;
/*  916 */     if (b == Blocks.field_150349_c)
/*  917 */       return Blocks.field_150346_d;
/*  918 */     if (b == Blocks.field_150351_n)
/*  919 */       return Blocks.field_150351_n;
/*  920 */     if (b == Blocks.field_150354_m)
/*  921 */       return Blocks.field_150354_m;
/*  922 */     if ((b == Blocks.field_150322_A) && (surface))
/*  923 */       return Blocks.field_150354_m;
/*  924 */     if ((b == Blocks.field_150322_A) && (!surface)) {
/*  925 */       return Blocks.field_150322_A;
/*      */     }
/*      */     
/*  928 */     return null;
/*      */   }
/*      */   
/*      */   public static int getBlockMeta(World world, Point p) {
/*  932 */     if ((p.x < -3.2E7D) || (p.z < -3.2E7D) || (p.x >= 3.2E7D) || (p.z > 3.2E7D)) {
/*  933 */       return -1;
/*      */     }
/*  935 */     if (p.y < 0.0D) {
/*  936 */       return -1;
/*      */     }
/*  938 */     if (p.y >= 256.0D) {
/*  939 */       return -1;
/*      */     }
/*      */     
/*  942 */     return world.func_72805_g(p.getiX(), p.getiY(), p.getiZ());
/*      */   }
/*      */   
/*      */   public static List<Point> getBlocksAround(World world, Block[] targetBlocks, Point pos, int rx, int ry, int rz)
/*      */   {
/*  947 */     List<Point> matches = new ArrayList();
/*      */     
/*  949 */     for (int i = pos.getiX() - rx; i <= pos.getiX() + rx; i++) {
/*  950 */       for (int j = pos.getiY() - ry; j <= pos.getiY() + ry; j++) {
/*  951 */         for (int k = pos.getiZ() - rz; k <= pos.getiZ() + rz; k++) {
/*  952 */           for (int l = 0; l < targetBlocks.length; l++) {
/*  953 */             if (world.func_147439_a(i, j, k) == targetBlocks[l]) {
/*  954 */               matches.add(new Point(i, j, k));
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  961 */     return matches;
/*      */   }
/*      */   
/*      */   public static File getBuildingsDir(World worldObj)
/*      */   {
/*  966 */     File saveDir = getWorldSaveDir(worldObj);
/*      */     
/*  968 */     File millenaireDir = new File(saveDir, "millenaire");
/*      */     
/*  970 */     if (!millenaireDir.exists()) {
/*  971 */       millenaireDir.mkdir();
/*      */     }
/*      */     
/*  974 */     File buildingsDir = new File(millenaireDir, "buildings");
/*      */     
/*  976 */     if (!buildingsDir.exists()) {
/*  977 */       buildingsDir.mkdir();
/*      */     }
/*      */     
/*  980 */     return buildingsDir;
/*      */   }
/*      */   
/*      */   public static Point getClosestBlock(World world, Block[] blocks, Point pos, int rx, int ry, int rz) {
/*  984 */     return getClosestBlockMeta(world, blocks, -1, pos, rx, ry, rz);
/*      */   }
/*      */   
/*      */ 
/*      */   public static Point getClosestBlockMeta(World world, Block[] blocks, int meta, Point pos, int rx, int ry, int rz)
/*      */   {
/*  990 */     Point closest = null;
/*  991 */     double minDistance = 9.99999999E8D;
/*      */     
/*  993 */     for (int i = pos.getiX() - rx; i <= pos.getiX() + rx; i++) {
/*  994 */       for (int j = pos.getiY() - ry; j <= pos.getiY() + ry; j++) {
/*  995 */         for (int k = pos.getiZ() - rz; k <= pos.getiZ() + rz; k++) {
/*  996 */           for (int l = 0; l < blocks.length; l++) {
/*  997 */             if ((world.func_147439_a(i, j, k) == blocks[l]) && (
/*  998 */               (meta == -1) || (world.func_72805_g(i, j, k) == meta)))
/*      */             {
/* 1000 */               Point temp = new Point(i, j, k);
/*      */               
/* 1002 */               if ((closest == null) || (temp.distanceTo(pos) < minDistance)) {
/* 1003 */                 closest = temp;
/* 1004 */                 minDistance = closest.distanceTo(pos);
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1013 */     if (minDistance < 9.99999999E8D) {
/* 1014 */       return closest;
/*      */     }
/* 1016 */     return null;
/*      */   }
/*      */   
/*      */   public static EntityItem getClosestItemVertical(World world, Point p, InvItem[] items, int radius, int vertical)
/*      */   {
/* 1021 */     List<Entity> list = getEntitiesWithinAABB(world, Entity.class, p, radius, vertical);
/*      */     
/* 1023 */     double bestdist = Double.MAX_VALUE;
/* 1024 */     EntityItem citem = null;
/*      */     
/* 1026 */     for (Entity ent : list) {
/* 1027 */       if (ent.getClass() == EntityItem.class) {
/* 1028 */         EntityItem item = (EntityItem)ent;
/*      */         
/* 1030 */         if (!item.field_70128_L) {
/* 1031 */           for (InvItem key : items) {
/* 1032 */             if ((item.func_92059_d().func_77973_b() == key.getItem()) && (item.func_92059_d().func_77960_j() == key.meta)) {
/* 1033 */               double dist = item.func_70092_e(p.x, p.y, p.z);
/* 1034 */               if (dist < bestdist) {
/* 1035 */                 bestdist = dist;
/* 1036 */                 citem = item;
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 1044 */     if (citem == null) {
/* 1045 */       return null;
/*      */     }
/* 1047 */     return citem;
/*      */   }
/*      */   
/*      */   public static java.lang.reflect.Method getDrawSlotInventoryMethod(GuiContainer gui)
/*      */   {
/* 1052 */     return ReflectionHelper.findMethod(GuiContainer.class, gui, new String[] { "func_146977_a", "func_146977_a" }, new Class[] { net.minecraft.inventory.Slot.class });
/*      */   }
/*      */   
/*      */ 
/*      */   public static List<Entity> getEntitiesWithinAABB(World world, Class type, Point p, int hradius, int vradius)
/*      */   {
/* 1058 */     AxisAlignedBB area = AxisAlignedBB.func_72330_a(p.x, p.y, p.z, p.x + 1.0D, p.y + 1.0D, p.z + 1.0D).func_72314_b(hradius, vradius, hradius);
/* 1059 */     return world.func_72872_a(type, area);
/*      */   }
/*      */   
/*      */ 
/*      */   public static List<Entity> getEntitiesWithinAABB(World world, Class type, Point pstart, Point pend)
/*      */   {
/* 1065 */     AxisAlignedBB area = AxisAlignedBB.func_72330_a(pstart.x, pstart.y, pstart.z, pend.x, pend.y, pend.z);
/* 1066 */     return world.func_72872_a(type, area);
/*      */   }
/*      */   
/*      */   public static net.minecraft.util.IIcon getIcon(IIconRegister register, String iconName) {
/* 1070 */     return register.func_94245_a("millenaire:" + iconName + MLN.getTextSuffix());
/*      */   }
/*      */   
/*      */   public static int getInvItemHashTotal(HashMap<InvItem, Integer> map) {
/* 1074 */     int total = 0;
/*      */     
/* 1076 */     for (InvItem key : map.keySet()) {
/* 1077 */       total += ((Integer)map.get(key)).intValue();
/*      */     }
/* 1079 */     return total;
/*      */   }
/*      */   
/*      */   public static Item getItemById(int id) {
/* 1083 */     return (Item)Item.field_150901_e.func_148754_a(id);
/*      */   }
/*      */   
/*      */   public static int getItemId(Item it) {
/* 1087 */     return Item.field_150901_e.func_148757_b(it);
/*      */   }
/*      */   
/*      */   public static int getItemsFromChest(IInventory chest, Block block, int meta, int toTake) {
/* 1091 */     return getItemsFromChest(chest, Item.func_150898_a(block), meta, toTake);
/*      */   }
/*      */   
/*      */   public static int getItemsFromChest(IInventory chest, Item item, int meta, int toTake) {
/* 1095 */     if (chest == null) {
/* 1096 */       return 0;
/*      */     }
/*      */     
/* 1099 */     int nb = 0;
/*      */     
/* 1101 */     int maxSlot = chest.func_70302_i_() - 1;
/*      */     
/* 1103 */     if ((chest instanceof InventoryPlayer)) {
/* 1104 */       maxSlot -= 4;
/*      */     }
/*      */     
/* 1107 */     for (int i = maxSlot; (i >= 0) && (nb < toTake); i--) {
/* 1108 */       ItemStack stack = chest.func_70301_a(i);
/*      */       
/* 1110 */       if ((stack != null) && (stack.func_77973_b() == item) && ((stack.func_77960_j() == meta) || (meta == -1))) {
/* 1111 */         if (stack.field_77994_a <= toTake - nb) {
/* 1112 */           nb += stack.field_77994_a;
/* 1113 */           chest.func_70299_a(i, null);
/*      */         } else {
/* 1115 */           chest.func_70298_a(i, toTake - nb);
/* 1116 */           nb = toTake;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 1121 */     return nb;
/*      */   }
/*      */   
/*      */   public static int getItemsFromFurnace(IInventory furnace, Item item, int toTake) {
/* 1125 */     if (furnace == null) {
/* 1126 */       return 0;
/*      */     }
/*      */     
/* 1129 */     int nb = 0;
/*      */     
/* 1131 */     ItemStack stack = furnace.func_70301_a(2);
/* 1132 */     if ((stack != null) && (stack.func_77973_b() == item)) {
/* 1133 */       if (stack.field_77994_a <= toTake - nb) {
/* 1134 */         nb += stack.field_77994_a;
/* 1135 */         furnace.func_70299_a(2, null);
/*      */       } else {
/* 1137 */         furnace.func_70298_a(2, toTake - nb);
/* 1138 */         nb = toTake;
/*      */       }
/*      */     }
/*      */     
/* 1142 */     return nb;
/*      */   }
/*      */   
/*      */   public static double getItemWeaponDamage(Item item)
/*      */   {
/* 1147 */     Multimap multimap = item.func_111205_h();
/*      */     
/* 1149 */     if ((multimap.containsKey(SharedMonsterAttributes.field_111264_e.func_111108_a())) && 
/* 1150 */       (multimap.get(SharedMonsterAttributes.field_111264_e.func_111108_a()).toArray().length > 0) && 
/* 1151 */       ((multimap.get(SharedMonsterAttributes.field_111264_e.func_111108_a()).toArray()[0] instanceof AttributeModifier))) {
/* 1152 */       AttributeModifier weaponModifier = (AttributeModifier)multimap.get(SharedMonsterAttributes.field_111264_e.func_111108_a()).toArray()[0];
/* 1153 */       return weaponModifier.func_111164_d();
/*      */     }
/*      */     
/*      */ 
/* 1157 */     return 0.0D;
/*      */   }
/*      */   
/*      */   public static int[] getJumpDestination(World world, int x, int y, int z)
/*      */   {
/* 1162 */     if ((!isBlockOpaqueCube(world, x, y, z)) && (!isBlockOpaqueCube(world, x, y + 1, z))) {
/* 1163 */       return new int[] { x, y, z };
/*      */     }
/*      */     
/* 1166 */     if ((!isBlockOpaqueCube(world, x + 1, y, z)) && (!isBlockOpaqueCube(world, x + 1, y + 1, z))) {
/* 1167 */       return new int[] { x + 1, y, z };
/*      */     }
/*      */     
/* 1170 */     if ((!isBlockOpaqueCube(world, x - 1, y, z)) && (!isBlockOpaqueCube(world, x - 1, y + 1, z))) {
/* 1171 */       return new int[] { x - 1, y, z };
/*      */     }
/*      */     
/* 1174 */     if ((!isBlockOpaqueCube(world, x, y, z + 1)) && (!isBlockOpaqueCube(world, x, y + 1, z + 1))) {
/* 1175 */       return new int[] { x, y, z + 1 };
/*      */     }
/*      */     
/* 1178 */     if ((!isBlockOpaqueCube(world, x, y, z - 1)) && (!isBlockOpaqueCube(world, x, y + 1, z - 1))) {
/* 1179 */       return new int[] { x, y, z - 1 };
/*      */     }
/*      */     
/* 1182 */     return null;
/*      */   }
/*      */   
/*      */   public static int getPointHash(Block b, int meta) {
/* 1186 */     return (Block.field_149771_c.func_148750_c(b) + "_" + meta).hashCode();
/*      */   }
/*      */   
/*      */   public static int getPointHash(String special) {
/* 1190 */     return ("sp_" + special).hashCode();
/*      */   }
/*      */   
/*      */   public static int getPriceColour(int price) {
/* 1194 */     if (price >= 4096) {
/* 1195 */       return 16770304;
/*      */     }
/*      */     
/* 1198 */     if (price >= 64) {
/* 1199 */       return 15790320;
/*      */     }
/*      */     
/* 1202 */     return 10373418;
/*      */   }
/*      */   
/*      */   public static int getPriceColourMC(int price) {
/* 1206 */     if (price >= 4096) {
/* 1207 */       return 14;
/*      */     }
/*      */     
/* 1210 */     if (price >= 64) {
/* 1211 */       return 15;
/*      */     }
/*      */     
/* 1214 */     return 6;
/*      */   }
/*      */   
/*      */   public static Object getPrivateValue(Class pclass, Object obj, int pos)
/*      */     throws IllegalArgumentException, SecurityException, NoSuchFieldException
/*      */   {
/* 1220 */     return ReflectionHelper.getPrivateValue(pclass, obj, pos);
/*      */   }
/*      */   
/*      */   public static Object getPrivateValue(Class pclass, Object obj, String nameMCP, String name)
/*      */     throws IllegalArgumentException, SecurityException, NoSuchFieldException
/*      */   {
/* 1226 */     if (EntityCreature.class.getSimpleName().equals("EntityCreature")) {
/* 1227 */       return ReflectionHelper.getPrivateValue(pclass, obj, new String[] { nameMCP });
/*      */     }
/* 1229 */     return ReflectionHelper.getPrivateValue(pclass, obj, new String[] { name });
/*      */   }
/*      */   
/*      */   public static Random getRandom()
/*      */   {
/* 1234 */     if (random == null) {
/* 1235 */       random = new Random();
/*      */     }
/*      */     
/* 1238 */     return random;
/*      */   }
/*      */   
/*      */   public static BufferedReader getReader(File file) throws UnsupportedEncodingException, FileNotFoundException {
/* 1242 */     return new BufferedReader(new InputStreamReader(new java.io.FileInputStream(file), "UTF8"));
/*      */   }
/*      */   
/*      */   public static String getRelationName(int relation)
/*      */   {
/* 1247 */     if (relation >= 90) {
/* 1248 */       return "relation.excellent";
/*      */     }
/* 1250 */     if (relation >= 70) {
/* 1251 */       return "relation.verygood";
/*      */     }
/* 1253 */     if (relation >= 50) {
/* 1254 */       return "relation.good";
/*      */     }
/* 1256 */     if (relation >= 30) {
/* 1257 */       return "relation.decent";
/*      */     }
/* 1259 */     if (relation >= 10) {
/* 1260 */       return "relation.fair";
/*      */     }
/*      */     
/* 1263 */     if (relation <= -90) {
/* 1264 */       return "relation.openconflict";
/*      */     }
/* 1266 */     if (relation <= -70) {
/* 1267 */       return "relation.atrocious";
/*      */     }
/* 1269 */     if (relation <= -50) {
/* 1270 */       return "relation.verybad";
/*      */     }
/* 1272 */     if (relation <= -30) {
/* 1273 */       return "relation.bad";
/*      */     }
/* 1275 */     if (relation <= -10) {
/* 1276 */       return "relation.chilly";
/*      */     }
/*      */     
/* 1279 */     return "relation.neutral";
/*      */   }
/*      */   
/*      */   public static EntityPlayer getServerPlayer(World world, String playerName)
/*      */   {
/* 1284 */     for (Object o : world.field_73010_i) {
/* 1285 */       if ((o instanceof EntityPlayer)) {
/* 1286 */         EntityPlayer player = (EntityPlayer)o;
/*      */         
/* 1288 */         if (player.getDisplayName().equals(playerName)) {
/* 1289 */           return player;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 1294 */     return null;
/*      */   }
/*      */   
/*      */   public static List<EntityPlayer> getServerPlayers(World world)
/*      */   {
/* 1299 */     List<EntityPlayer> players = new ArrayList(world.field_73010_i);
/* 1300 */     return players;
/*      */   }
/*      */   
/*      */   public static UserProfile getServerProfile(World world, String name)
/*      */   {
/* 1305 */     MillWorld mw = Mill.getMillWorld(world);
/*      */     
/* 1307 */     if (mw == null) {
/* 1308 */       return null;
/*      */     }
/*      */     
/* 1311 */     return mw.getProfile(name);
/*      */   }
/*      */   
/*      */   public static String getShortPrice(int price)
/*      */   {
/* 1316 */     String res = "";
/*      */     
/* 1318 */     if (price >= 4096) {
/* 1319 */       res = (int)Math.floor(price / 4096) + "o ";
/* 1320 */       price %= 4096;
/*      */     }
/* 1322 */     if (price >= 64) {
/* 1323 */       res = res + (int)Math.floor(price / 64) + "a ";
/* 1324 */       price %= 64;
/*      */     }
/* 1326 */     if (price > 0) {
/* 1327 */       res = res + price + "d";
/*      */     }
/* 1329 */     return res.trim();
/*      */   }
/*      */   
/*      */   public static String getVillagerSentence(MillVillager v, String playerName, boolean nativeSpeech)
/*      */   {
/* 1334 */     if (v.speech_key == null) {
/* 1335 */       return null;
/*      */     }
/*      */     
/* 1338 */     if ((!nativeSpeech) && (!v.getCulture().canReadDialogues(playerName))) {
/* 1339 */       return null;
/*      */     }
/*      */     
/* 1342 */     List<String> variants = v.getCulture().getSentences(v.speech_key);
/*      */     
/* 1344 */     if ((variants != null) && (variants.size() > v.speech_variant)) {
/* 1345 */       String s = ((String)variants.get(v.speech_variant)).replaceAll("\\$name", playerName);
/*      */       
/* 1347 */       if ((v.getGoalDestEntity() != null) && ((v.getGoalDestEntity() instanceof MillVillager))) {
/* 1348 */         s = s.replaceAll("\\$targetfirstname", v.dialogueTargetFirstName);
/* 1349 */         s = s.replaceAll("\\$targetlastname", v.dialogueTargetLastName);
/*      */       } else {
/* 1351 */         s = s.replaceAll("\\$targetfirstname", "");
/* 1352 */         s = s.replaceAll("\\$targetlastname", "");
/*      */       }
/*      */       
/* 1355 */       if (!nativeSpeech) {
/* 1356 */         if (s.split("/").length > 1) {
/* 1357 */           s = s.split("/")[1].trim();
/*      */           
/* 1359 */           if (s.length() == 0) {
/* 1360 */             s = null;
/*      */           }
/*      */           
/* 1363 */           return s;
/*      */         }
/* 1365 */         return null;
/*      */       }
/*      */       
/* 1368 */       if (s.split("/").length > 1) {
/* 1369 */         s = s.split("/")[0].trim();
/*      */       }
/*      */       
/* 1372 */       if (s.length() == 0) {
/* 1373 */         s = null;
/*      */       }
/*      */       
/* 1376 */       return s;
/*      */     }
/*      */     
/*      */ 
/* 1380 */     return v.speech_key;
/*      */   }
/*      */   
/*      */ 
/*      */   public static WeightedChoice getWeightedChoice(List oChoices, EntityPlayer player)
/*      */   {
/* 1386 */     List<WeightedChoice> choices = oChoices;
/*      */     
/* 1388 */     int weightTotal = 0;
/* 1389 */     List<Integer> weights = new ArrayList();
/*      */     
/* 1391 */     for (WeightedChoice choice : choices) {
/* 1392 */       weightTotal += choice.getChoiceWeight(player);
/* 1393 */       weights.add(Integer.valueOf(choice.getChoiceWeight(player)));
/*      */     }
/*      */     
/* 1396 */     if (weightTotal < 1) {
/* 1397 */       return null;
/*      */     }
/*      */     
/* 1400 */     int random = randomInt(weightTotal);
/* 1401 */     int count = 0;
/*      */     
/* 1403 */     for (int i = 0; i < choices.size(); i++) {
/* 1404 */       count += ((Integer)weights.get(i)).intValue();
/* 1405 */       if (random < count) {
/* 1406 */         return (WeightedChoice)choices.get(i);
/*      */       }
/*      */     }
/* 1409 */     return null;
/*      */   }
/*      */   
/*      */   public static File getWorldSaveDir(World world) {
/* 1413 */     net.minecraft.world.storage.ISaveHandler isavehandler = world.func_72860_G();
/*      */     
/* 1415 */     if ((isavehandler instanceof SaveHandler)) {
/* 1416 */       return ((SaveHandler)isavehandler).func_75765_b();
/*      */     }
/* 1418 */     return null;
/*      */   }
/*      */   
/*      */   public static BufferedWriter getWriter(File file) throws UnsupportedEncodingException, FileNotFoundException
/*      */   {
/* 1423 */     return new BufferedWriter(new java.io.OutputStreamWriter(new FileOutputStream(file), "UTF8"));
/*      */   }
/*      */   
/*      */   public static int guessSignMetaData(World world, Point p)
/*      */   {
/* 1428 */     boolean northOpen = true;boolean southOpen = true;boolean eastOpen = true;boolean westOpen = true;
/*      */     
/* 1430 */     if (isBlockOpaqueCube(getBlock(world, p.getNorth()))) {
/* 1431 */       northOpen = false;
/*      */     }
/* 1433 */     if (isBlockOpaqueCube(getBlock(world, p.getEast()))) {
/* 1434 */       eastOpen = false;
/*      */     }
/* 1436 */     if (isBlockOpaqueCube(getBlock(world, p.getSouth()))) {
/* 1437 */       southOpen = false;
/*      */     }
/* 1439 */     if (isBlockOpaqueCube(getBlock(world, p.getWest()))) {
/* 1440 */       westOpen = false;
/*      */     }
/*      */     
/* 1443 */     if (!eastOpen)
/* 1444 */       return 3;
/* 1445 */     if (!westOpen)
/* 1446 */       return 2;
/* 1447 */     if (!southOpen)
/* 1448 */       return 4;
/* 1449 */     if (!northOpen) {
/* 1450 */       return 5;
/*      */     }
/* 1452 */     return 0;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static boolean isBlockIdGround(Block b)
/*      */   {
/* 1459 */     if (b == Blocks.field_150357_h)
/* 1460 */       return true;
/* 1461 */     if (b == Blocks.field_150435_aG)
/* 1462 */       return true;
/* 1463 */     if (b == Blocks.field_150346_d)
/* 1464 */       return true;
/* 1465 */     if (b == Blocks.field_150349_c)
/* 1466 */       return true;
/* 1467 */     if (b == Blocks.field_150351_n)
/* 1468 */       return true;
/* 1469 */     if (b == Blocks.field_150343_Z)
/* 1470 */       return true;
/* 1471 */     if (b == Blocks.field_150354_m)
/* 1472 */       return true;
/* 1473 */     if (b == Blocks.field_150458_ak) {
/* 1474 */       return true;
/*      */     }
/*      */     
/* 1477 */     return false;
/*      */   }
/*      */   
/*      */   public static boolean isBlockIdGroundOrCeiling(Block b)
/*      */   {
/* 1482 */     if (b == Blocks.field_150348_b)
/* 1483 */       return true;
/* 1484 */     if (b == Blocks.field_150322_A) {
/* 1485 */       return true;
/*      */     }
/*      */     
/* 1488 */     return false;
/*      */   }
/*      */   
/*      */   public static boolean isBlockIdLiquid(Block b) {
/* 1492 */     if (b == null) {
/* 1493 */       return false;
/*      */     }
/*      */     
/* 1496 */     if ((b == Blocks.field_150355_j) || (b == Blocks.field_150358_i) || (b == Blocks.field_150353_l) || (b == Blocks.field_150356_k)) {
/* 1497 */       return true;
/*      */     }
/*      */     
/* 1500 */     return false;
/*      */   }
/*      */   
/*      */   public static boolean isBlockIdSolid(Block b) {
/* 1504 */     if (b == null) {
/* 1505 */       return false;
/*      */     }
/*      */     
/* 1508 */     if (b.func_149662_c()) {
/* 1509 */       return true;
/*      */     }
/* 1511 */     if ((b == Blocks.field_150359_w) || (b == Blocks.field_150410_aZ) || (b == Blocks.field_150333_U) || ((b instanceof BlockSlab)) || ((b instanceof BlockStairs)) || (b == Blocks.field_150422_aJ) || (b == Mill.paperWall)) {
/* 1512 */       return true;
/*      */     }
/*      */     
/* 1515 */     return false;
/*      */   }
/*      */   
/*      */   public static boolean isBlockOpaqueCube(Block b) {
/* 1519 */     if (b == null) {
/* 1520 */       return false;
/*      */     }
/* 1522 */     return b.func_149688_o().func_76230_c();
/*      */   }
/*      */   
/*      */   public static boolean isBlockOpaqueCube(World world, int i, int j, int k) {
/* 1526 */     Block b = world.func_147439_a(i, j, k);
/* 1527 */     if (b == null) {
/* 1528 */       return false;
/*      */     }
/* 1530 */     return isBlockOpaqueCube(b);
/*      */   }
/*      */   
/*      */   public static boolean isPointOnStablePath(Point p, World world) {
/* 1534 */     Block block = p.getBlock(world);
/* 1535 */     int meta = p.getMeta(world);
/*      */     
/* 1537 */     if (((block == Mill.path) || (block == Mill.pathSlab)) && (meta > 7)) {
/* 1538 */       return true;
/*      */     }
/*      */     
/* 1541 */     block = p.getBelow().getBlock(world);
/* 1542 */     meta = p.getBelow().getMeta(world);
/*      */     
/* 1544 */     if (((block == Mill.path) || (block == Mill.pathSlab)) && (meta > 7)) {
/* 1545 */       return true;
/*      */     }
/*      */     
/* 1548 */     return false;
/*      */   }
/*      */   
/*      */   private static boolean isStairsOrSlabOrChest(World world, Point p) {
/* 1552 */     Block block = p.getBlock(world);
/*      */     
/* 1554 */     if ((block == Blocks.field_150486_ae) || (block == Mill.lockedChest) || (block == Blocks.field_150462_ai) || (block == Blocks.field_150460_al) || (block == Blocks.field_150470_am)) {
/* 1555 */       return true;
/*      */     }
/*      */     
/* 1558 */     if ((block instanceof BlockStairs)) {
/* 1559 */       return true;
/*      */     }
/*      */     
/* 1562 */     if (((block instanceof BlockSlab)) && 
/* 1563 */       (!block.func_149662_c())) {
/* 1564 */       return true;
/*      */     }
/*      */     
/*      */ 
/* 1568 */     return false;
/*      */   }
/*      */   
/*      */   public static String[] limitSignText(String[] lines) {
/* 1572 */     for (int i = 0; i < lines.length; i++) {
/* 1573 */       String s = lines[i];
/* 1574 */       if (s != null) {
/* 1575 */         if (s.length() > 15) {
/* 1576 */           s = s.substring(0, 15);
/*      */         }
/*      */       } else {
/* 1579 */         s = "";
/*      */       }
/* 1581 */       lines[i] = s;
/*      */     }
/* 1583 */     return lines;
/*      */   }
/*      */   
/*      */   public static void logInstance(World world)
/*      */   {
/* 1588 */     long uid = 0L;
/*      */     
/* 1590 */     if (!Mill.proxy.isTrueServer()) {
/* 1591 */       String login = Mill.proxy.getClientProfile().playerName;
/*      */       
/* 1593 */       if (login.startsWith("Player")) {
/* 1594 */         uid = -1L;
/*      */       } else {
/* 1596 */         uid = login.hashCode();
/*      */       }
/*      */     } else {
/* 1599 */       uid = world.func_72912_H().func_76063_b();
/*      */     }
/*      */     
/* 1602 */     String os = System.getProperty("os.name");
/*      */     
/*      */     String mode;
/*      */     String mode;
/* 1606 */     if (Mill.proxy.isTrueServer()) {
/* 1607 */       mode = "s";
/*      */     } else { String mode;
/* 1609 */       if (Mill.isDistantClient()) {
/* 1610 */         mode = "c";
/*      */       } else {
/* 1612 */         mode = "l";
/*      */       }
/*      */     }
/*      */     
/* 1616 */     int totalexp = 0;
/*      */     Iterator i$;
/* 1618 */     UserProfile p; UserProfile p; if (Mill.proxy.isTrueServer()) {
/* 1619 */       if (!Mill.serverWorlds.isEmpty()) {
/* 1620 */         for (i$ = ((MillWorld)Mill.serverWorlds.get(0)).profiles.values().iterator(); i$.hasNext();) { p = (UserProfile)i$.next();
/* 1621 */           for (Culture c : Culture.ListCultures) {
/* 1622 */             totalexp += Math.abs(p.getCultureReputation(c.key));
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/* 1629 */       p = Mill.proxy.getClientProfile();
/* 1630 */       if (p != null) {
/* 1631 */         for (Culture c : Culture.ListCultures) {
/* 1632 */           totalexp += Math.abs(p.getCultureReputation(c.key));
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1638 */     String lang = "";
/*      */     
/* 1640 */     if (MLN.mainLanguage != null) {
/* 1641 */       lang = MLN.mainLanguage.language;
/*      */     }
/*      */     
/* 1644 */     int nbplayers = 1;
/*      */     
/* 1646 */     if ((Mill.proxy.isTrueServer()) && (!Mill.serverWorlds.isEmpty())) {
/* 1647 */       nbplayers = ((MillWorld)Mill.serverWorlds.get(0)).profiles.size();
/*      */     }
/*      */     
/* 1650 */     String url = "http://millenaire.org/php/mlnuse.php?uid=" + uid + "&mlnversion=" + "6.0.0" + "&mode=" + mode + "&lang=" + lang + "&backuplang=" + MLN.fallback_language + "&nbplayers=" + nbplayers + "&os=" + os + "&totalexp=" + totalexp;
/*      */     
/*      */ 
/* 1653 */     url = url.replaceAll(" ", "%20");
/*      */     
/* 1655 */     MLN.logPerformed = true;
/*      */     
/* 1657 */     new LogThread(url).start();
/*      */   }
/*      */   
/*      */   public static void notifyBlock(World world, Point p) {
/* 1661 */     world.func_147444_c(p.getiX() + 1, p.getiY(), p.getiZ(), null);
/*      */   }
/*      */   
/*      */   public static int[] packLong(long nb) {
/* 1665 */     return new int[] { (int)(nb >> 32), (int)nb };
/*      */   }
/*      */   
/*      */   public static void playSound(World world, Point p, String sound, float volume, float pitch) {
/* 1669 */     world.func_72908_a((float)p.x + 0.5F, (float)p.y + 0.5F, (float)p.z + 0.5F, sound, (volume + 1.0F) / 2.0F, pitch * 0.8F);
/*      */   }
/*      */   
/*      */   public static void playSoundBlockBreaking(World world, Point p, Block b, float volume) {
/* 1673 */     if ((b != null) && (b.field_149762_H != null)) {
/* 1674 */       playSound(world, p, b.field_149762_H.func_150495_a(), b.field_149762_H.func_150497_c() * volume, b.field_149762_H.func_150494_d());
/*      */     }
/*      */   }
/*      */   
/*      */   public static void playSoundBlockPlaced(World world, Point p, Block b, float volume) {
/* 1679 */     if ((b != null) && (b.field_149762_H != null)) {
/* 1680 */       playSound(world, p, b.field_149762_H.field_150501_a, b.field_149762_H.func_150497_c() * volume, b.field_149762_H.func_150494_d());
/*      */     }
/*      */   }
/*      */   
/*      */   public static void playSoundByMillName(World world, Point p, String soundMill, float volume) {
/* 1685 */     if (soundMill.equals("metal")) {
/* 1686 */       playSoundBlockPlaced(world, p, Blocks.field_150339_S, volume);
/* 1687 */     } else if (soundMill.equals("wood")) {
/* 1688 */       playSoundBlockPlaced(world, p, Blocks.field_150364_r, volume);
/* 1689 */     } else if (soundMill.equals("wool")) {
/* 1690 */       playSoundBlockPlaced(world, p, Blocks.field_150325_L, volume);
/* 1691 */     } else if (soundMill.equals("glass")) {
/* 1692 */       playSoundBlockPlaced(world, p, Blocks.field_150359_w, volume);
/* 1693 */     } else if (soundMill.equals("stone")) {
/* 1694 */       playSoundBlockPlaced(world, p, Blocks.field_150348_b, volume);
/* 1695 */     } else if (soundMill.equals("earth")) {
/* 1696 */       playSoundBlockPlaced(world, p, Blocks.field_150346_d, volume);
/*      */     } else {
/* 1698 */       MLN.printException("Tried to play unknown sound: " + soundMill, new Exception());
/*      */     }
/*      */   }
/*      */   
/*      */   public static boolean probability(double probability)
/*      */   {
/* 1704 */     return getRandom().nextDouble() < probability;
/*      */   }
/*      */   
/*      */   public static int putItemsInChest(IInventory chest, Block block, int toPut) {
/* 1708 */     return putItemsInChest(chest, Item.func_150898_a(block), 0, toPut);
/*      */   }
/*      */   
/*      */   public static int putItemsInChest(IInventory chest, Block block, int meta, int toPut) {
/* 1712 */     return putItemsInChest(chest, Item.func_150898_a(block), meta, toPut);
/*      */   }
/*      */   
/*      */   public static int putItemsInChest(IInventory chest, Item item, int toPut) {
/* 1716 */     return putItemsInChest(chest, item, 0, toPut);
/*      */   }
/*      */   
/*      */   public static int putItemsInChest(IInventory chest, Item item, int meta, int toPut)
/*      */   {
/* 1721 */     if (chest == null) {
/* 1722 */       return 0;
/*      */     }
/*      */     
/* 1725 */     int nb = 0;
/*      */     
/* 1727 */     int maxSlot = chest.func_70302_i_();
/*      */     
/* 1729 */     if ((chest instanceof InventoryPlayer)) {
/* 1730 */       maxSlot -= 4;
/*      */     }
/*      */     
/* 1733 */     for (int i = 0; (i < maxSlot) && (nb < toPut); i++) {
/* 1734 */       ItemStack stack = chest.func_70301_a(i);
/* 1735 */       if ((stack != null) && (stack.func_77973_b() == item) && (stack.func_77960_j() == meta))
/*      */       {
/*      */ 
/* 1738 */         if (stack.func_77976_d() - stack.field_77994_a >= toPut - nb) {
/* 1739 */           stack.field_77994_a += toPut - nb;
/* 1740 */           nb = toPut;
/*      */         } else {
/* 1742 */           nb += stack.func_77976_d() - stack.field_77994_a;
/* 1743 */           stack.field_77994_a = stack.func_77976_d();
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 1748 */     for (int i = 0; (i < maxSlot) && (nb < toPut); i++) {
/* 1749 */       ItemStack stack = chest.func_70301_a(i);
/* 1750 */       if (stack == null) {
/* 1751 */         stack = new ItemStack(item, 1, meta);
/*      */         
/* 1753 */         if ((stack.func_77973_b() instanceof Goods.IItemInitialEnchantmens)) {
/* 1754 */           ((Goods.IItemInitialEnchantmens)stack.func_77973_b()).applyEnchantments(stack);
/*      */         }
/*      */         
/* 1757 */         if (toPut - nb <= stack.func_77976_d()) {
/* 1758 */           stack.field_77994_a = (toPut - nb);
/* 1759 */           nb = toPut;
/*      */         } else {
/* 1761 */           stack.field_77994_a = stack.func_77976_d();
/* 1762 */           nb += stack.field_77994_a;
/*      */         }
/* 1764 */         chest.func_70299_a(i, stack);
/*      */       }
/*      */     }
/* 1767 */     return nb;
/*      */   }
/*      */   
/*      */   public static int randomInt(int i) {
/* 1771 */     return getRandom().nextInt(i);
/*      */   }
/*      */   
/*      */   public static long randomLong() {
/* 1775 */     return getRandom().nextLong();
/*      */   }
/*      */   
/*      */   public static int readInteger(String line) throws Exception {
/* 1779 */     int res = 1;
/* 1780 */     for (String s : line.trim().split("\\*")) {
/* 1781 */       res *= Integer.parseInt(s);
/*      */     }
/* 1783 */     return res;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void readInventory(NBTTagList nbttaglist, Map<InvItem, Integer> inventory)
/*      */   {
/* 1793 */     for (int i = 0; i < nbttaglist.func_74745_c(); i++) {
/* 1794 */       NBTTagCompound nbttagcompound1 = nbttaglist.func_150305_b(i);
/*      */       
/* 1796 */       String itemName = nbttagcompound1.func_74779_i("item");
/* 1797 */       String itemMod = nbttagcompound1.func_74779_i("itemmod");
/* 1798 */       int itemMeta = nbttagcompound1.func_74762_e("meta");
/*      */       try
/*      */       {
/* 1801 */         inventory.put(new InvItem(GameRegistry.findItem(itemMod, itemName), itemMeta), Integer.valueOf(nbttagcompound1.func_74762_e("amount")));
/*      */       } catch (MLN.MillenaireException e) {
/* 1803 */         MLN.printException(e);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public static boolean setBlock(World world, Point p, Block block) {
/* 1809 */     return setBlock(world, p, block, true, false);
/*      */   }
/*      */   
/*      */   public static boolean setBlock(World world, Point p, Block block, boolean notify, boolean playSound) {
/* 1813 */     if ((p.x < -3.2E7D) || (p.z < -3.2E7D) || (p.x >= 3.2E7D) || (p.z > 3.2E7D)) {
/* 1814 */       return false;
/*      */     }
/* 1816 */     if (p.y < 0.0D) {
/* 1817 */       return false;
/*      */     }
/* 1819 */     if (p.y >= 256.0D) {
/* 1820 */       return false;
/*      */     }
/*      */     
/* 1823 */     if ((playSound) && (block == Blocks.field_150350_a)) {
/* 1824 */       Block oldBlock = world.func_147439_a(p.getiX(), p.getiY(), p.getiZ());
/*      */       
/* 1826 */       if ((oldBlock != null) && 
/* 1827 */         (oldBlock.field_149762_H != null)) {
/* 1828 */         playSoundBlockBreaking(world, p, oldBlock, 2.0F);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1833 */     if (notify) {
/* 1834 */       world.func_147465_d(p.getiX(), p.getiY(), p.getiZ(), block, 0, 3);
/*      */     } else {
/* 1836 */       world.func_147465_d(p.getiX(), p.getiY(), p.getiZ(), block, 0, 2);
/*      */     }
/*      */     
/* 1839 */     if ((playSound) && (block != Blocks.field_150350_a) && 
/* 1840 */       (block.field_149762_H != null)) {
/* 1841 */       playSoundBlockPlaced(world, p, block, 2.0F);
/*      */     }
/*      */     
/*      */ 
/* 1845 */     return true;
/*      */   }
/*      */   
/*      */   public static boolean setBlockAndMetadata(World world, int x, int y, int z, Block block, int metadata, boolean notify, boolean playSound) {
/* 1849 */     if ((x < -32000000) || (z < -32000000) || (x >= 32000000) || (z > 32000000)) {
/* 1850 */       return false;
/*      */     }
/* 1852 */     if (y < 0) {
/* 1853 */       return false;
/*      */     }
/* 1855 */     if (y >= 256) {
/* 1856 */       return false;
/*      */     }
/*      */     
/* 1859 */     if ((playSound) && (block != Blocks.field_150350_a)) {
/* 1860 */       Block oldBlock = world.func_147439_a(x, y, z);
/*      */       
/* 1862 */       if ((oldBlock != null) && 
/* 1863 */         (oldBlock.field_149762_H != null)) {
/* 1864 */         playSoundBlockBreaking(world, new Point(x, y, z), oldBlock, 2.0F);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1870 */     if (block == null) {
/* 1871 */       MLN.printException("Trying to set null block", new Exception());
/*      */     }
/*      */     
/* 1874 */     if (notify) {
/* 1875 */       world.func_147465_d(x, y, z, block, metadata, 3);
/*      */     } else {
/* 1877 */       world.func_147465_d(x, y, z, block, metadata, 2);
/*      */     }
/*      */     
/* 1880 */     if ((playSound) && (block != Blocks.field_150350_a) && 
/* 1881 */       (block.field_149762_H != null)) {
/* 1882 */       playSoundBlockPlaced(world, new Point(x, y, z), block, 2.0F);
/*      */     }
/*      */     
/*      */ 
/* 1886 */     return true;
/*      */   }
/*      */   
/*      */   public static boolean setBlockAndMetadata(World world, Point p, Block block, int metadata) {
/* 1890 */     return setBlockAndMetadata(world, p, block, metadata, true, false);
/*      */   }
/*      */   
/*      */   public static boolean setBlockAndMetadata(World world, Point p, Block block, int metadata, boolean notify, boolean playSound) {
/* 1894 */     return setBlockAndMetadata(world, p.getiX(), p.getiY(), p.getiZ(), block, metadata, notify, playSound);
/*      */   }
/*      */   
/*      */   public static boolean setBlockMetadata(World world, int x, int y, int z, int metadata, boolean notify) {
/* 1898 */     if ((x < -32000000) || (z < -32000000) || (x >= 32000000) || (z > 32000000)) {
/* 1899 */       return false;
/*      */     }
/* 1901 */     if (y < 0) {
/* 1902 */       return false;
/*      */     }
/* 1904 */     if (y >= 256) {
/* 1905 */       return false;
/*      */     }
/*      */     
/* 1908 */     if (notify) {
/* 1909 */       world.func_72921_c(x, y, z, metadata, 3);
/*      */     } else {
/* 1911 */       world.func_72921_c(x, y, z, metadata, 2);
/*      */     }
/*      */     
/* 1914 */     return true;
/*      */   }
/*      */   
/*      */   public static boolean setBlockMetadata(World world, Point p, int metadata) {
/* 1918 */     return setBlockMetadata(world, p, metadata, true);
/*      */   }
/*      */   
/*      */   public static boolean setBlockMetadata(World world, Point p, int metadata, boolean notify)
/*      */   {
/* 1923 */     return setBlockMetadata(world, p.getiX(), p.getiY(), p.getiZ(), metadata, notify);
/*      */   }
/*      */   
/*      */ 
/*      */   public static void setPrivateValue(Class pclass, Object obj, String nameMCP, String name, Object value)
/*      */     throws IllegalArgumentException, SecurityException, NoSuchFieldException
/*      */   {
/* 1930 */     if (EntityCreature.class.getSimpleName().equals("EntityCreature")) {
/* 1931 */       ReflectionHelper.setPrivateValue(pclass, obj, value, new String[] { nameMCP });
/*      */     } else {
/* 1933 */       ReflectionHelper.setPrivateValue(pclass, obj, value, new String[] { name });
/*      */     }
/*      */   }
/*      */   
/*      */   public static void spawnExp(World world, Point p, int nb)
/*      */   {
/* 1939 */     for (int j = nb; j > 0;) {
/* 1940 */       int l = EntityXPOrb.func_70527_a(j);
/* 1941 */       j -= l;
/* 1942 */       world.func_72838_d(new EntityXPOrb(world, p.x + 0.5D, p.y + 5.0D, p.z + 0.5D, l));
/*      */     }
/*      */   }
/*      */   
/*      */   public static EntityItem spawnItem(World world, Point p, ItemStack itemstack, float f)
/*      */   {
/* 1948 */     EntityItem entityitem = new EntityItem(world, p.x, p.y + f, p.z, itemstack);
/* 1949 */     entityitem.field_145804_b = 10;
/* 1950 */     world.func_72838_d(entityitem);
/* 1951 */     return entityitem;
/*      */   }
/*      */   
/*      */   public static void spawnMobsAround(World world, Point p, int radius, String mobType, int minNb, int extraNb)
/*      */   {
/* 1956 */     int nb = minNb;
/* 1957 */     if (extraNb > 0) {
/* 1958 */       nb += randomInt(extraNb);
/*      */     }
/*      */     
/* 1961 */     for (int i = 0; i < nb; i++) {
/* 1962 */       EntityLiving entityliving = (EntityLiving)EntityList.func_75620_a(mobType, world);
/* 1963 */       if (entityliving != null)
/*      */       {
/*      */ 
/*      */ 
/* 1967 */         boolean spawned = false;
/*      */         
/* 1969 */         for (int j = 0; (j < 20) && (!spawned); j++) {
/* 1970 */           double ex = p.x + (world.field_73012_v.nextDouble() * 2.0D - 1.0D) * radius;
/* 1971 */           double ey = p.y + world.field_73012_v.nextInt(3) - 1.0D;
/* 1972 */           double ez = p.z + (world.field_73012_v.nextDouble() * 2.0D - 1.0D) * radius;
/* 1973 */           entityliving.func_70012_b(ex, ey, ez, world.field_73012_v.nextFloat() * 360.0F, 0.0F);
/*      */           
/* 1975 */           if (entityliving.func_70601_bi()) {
/* 1976 */             world.func_72838_d(entityliving);
/* 1977 */             MLN.major(null, "Entering world: " + entityliving.getClass().getName());
/* 1978 */             spawned = true;
/*      */           }
/*      */         }
/*      */         
/* 1982 */         if (!spawned) {
/* 1983 */           MLN.major(null, "No valid space found.");
/*      */         }
/*      */         
/* 1986 */         entityliving.func_70656_aK();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public static Entity spawnMobsSpawner(World world, Point p, String mobType) {
/* 1992 */     EntityLiving entityliving = (EntityLiving)EntityList.func_75620_a(mobType, world);
/* 1993 */     if (entityliving == null) {
/* 1994 */       return null;
/*      */     }
/*      */     
/* 1997 */     int x = randomInt(2) - 1;
/* 1998 */     int z = randomInt(2) - 1;
/*      */     
/* 2000 */     int ex = (int)(p.x + x);
/* 2001 */     int ey = (int)p.y;
/* 2002 */     int ez = (int)(p.z + z);
/*      */     
/* 2004 */     if ((world.func_147439_a(ex, ey, ez) != Blocks.field_150350_a) && (world.func_147439_a(ex, ey + 1, ez) != Blocks.field_150350_a)) {
/* 2005 */       return null;
/*      */     }
/*      */     
/* 2008 */     entityliving.func_70012_b(ex, ey, ez, world.field_73012_v.nextFloat() * 360.0F, 0.0F);
/*      */     
/* 2010 */     world.func_72838_d(entityliving);
/*      */     
/* 2012 */     entityliving.func_70656_aK();
/*      */     
/* 2014 */     return entityliving;
/*      */   }
/*      */   
/*      */   public static long unpackLong(int nb1, int nb2)
/*      */   {
/* 2019 */     return nb1 << 32 | nb2 & 0xFFFFFFFF;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static NBTTagList writeInventory(Map<InvItem, Integer> inventory)
/*      */   {
/* 2029 */     NBTTagList nbttaglist = new NBTTagList();
/* 2030 */     for (InvItem key : inventory.keySet())
/*      */     {
/* 2032 */       NBTTagCompound nbttagcompound1 = new NBTTagCompound();
/*      */       
/* 2034 */       if (key.getItem() != null) {
/* 2035 */         nbttagcompound1.func_74778_a("item", GameRegistry.findUniqueIdentifierFor(key.getItem()).name);
/* 2036 */         nbttagcompound1.func_74778_a("itemmod", GameRegistry.findUniqueIdentifierFor(key.getItem()).modId);
/*      */       }
/* 2038 */       nbttagcompound1.func_74768_a("meta", key.meta);
/* 2039 */       nbttagcompound1.func_74768_a("amount", ((Integer)inventory.get(key)).intValue());
/* 2040 */       nbttaglist.func_74742_a(nbttagcompound1);
/*      */     }
/*      */     
/* 2043 */     return nbttaglist;
/*      */   }
/*      */   
/*      */   public static abstract interface WeightedChoice
/*      */   {
/*      */     public abstract int getChoiceWeight(EntityPlayer paramEntityPlayer);
/*      */   }
/*      */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\core\MillCommonUtilities.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */