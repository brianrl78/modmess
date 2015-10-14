/*     */ package org.millenaire.common.goal;
/*     */ 
/*     */ import cpw.mods.fml.relauncher.ReflectionHelper;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityList;
/*     */ import net.minecraft.entity.passive.EntityAnimal;
/*     */ import net.minecraft.entity.passive.EntityChicken;
/*     */ import net.minecraft.entity.passive.EntityCow;
/*     */ import net.minecraft.entity.passive.EntityPig;
/*     */ import net.minecraft.entity.passive.EntitySheep;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import org.millenaire.common.MillVillager;
/*     */ import org.millenaire.common.building.Building;
/*     */ import org.millenaire.common.building.BuildingLocation;
/*     */ import org.millenaire.common.building.BuildingResManager;
/*     */ import org.millenaire.common.core.MillCommonUtilities;
/*     */ import org.millenaire.common.forge.Mill;
/*     */ import org.millenaire.common.network.ServerSender;
/*     */ import org.millenaire.common.pathing.atomicstryker.AStarConfig;
/*     */ 
/*     */ public class GoalBreedAnimals extends Goal
/*     */ {
/*  28 */   private static final Item[] CEREALS = { Items.field_151015_O, Mill.rice, Mill.maize };
/*     */   
/*  30 */   private static final Item[] SEEDS = { Items.field_151014_N, Mill.rice, Mill.maize };
/*     */   
/*  32 */   private static final Item[] CARROTS = { Items.field_151172_bF };
/*     */   
/*     */   private Item[] getBreedingItems(Class animalClass)
/*     */   {
/*  36 */     if ((animalClass == EntityCow.class) || (animalClass == EntitySheep.class)) {
/*  37 */       return CEREALS;
/*     */     }
/*  39 */     if (animalClass == EntityPig.class) {
/*  40 */       return CARROTS;
/*     */     }
/*  42 */     if (animalClass == EntityChicken.class) {
/*  43 */       return SEEDS;
/*     */     }
/*     */     
/*  46 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public Goal.GoalInformation getDestination(MillVillager villager)
/*     */     throws Exception
/*     */   {
/*  53 */     List<Class> validAnimals = getValidAnimalClasses(villager);
/*     */     
/*  55 */     for (Class animalClass : validAnimals)
/*     */     {
/*  57 */       Item[] breedingItems = getBreedingItems(animalClass);
/*     */       
/*  59 */       boolean available = false;
/*     */       
/*  61 */       if (breedingItems == null) {
/*  62 */         available = true;
/*     */       } else {
/*  64 */         for (Item breedingItem : breedingItems) {
/*  65 */           if ((!available) && (villager.getHouse().countGoods(breedingItem) > 0)) {
/*  66 */             available = true;
/*     */           }
/*     */         }
/*     */       }
/*     */       
/*  71 */       if (available)
/*     */       {
/*  73 */         int targetAnimals = 0;
/*     */         
/*  75 */         for (int i = 0; i < villager.getHouse().getResManager().spawns.size(); i++) {
/*  76 */           if (((String)villager.getHouse().getResManager().spawnTypes.get(i)).equals(EntityList.field_75626_c.get(animalClass))) {
/*  77 */             targetAnimals = ((List)villager.getHouse().getResManager().spawns.get(i)).size();
/*     */           }
/*     */         }
/*     */         
/*  81 */         List<Entity> animals = MillCommonUtilities.getEntitiesWithinAABB(villager.field_70170_p, animalClass, villager.getHouse().getPos(), 30, 10);
/*     */         
/*  83 */         int nbAdultAnimal = 0;int nbAnimal = 0;
/*     */         
/*  85 */         for (Entity ent : animals) {
/*  86 */           EntityAnimal animal = (EntityAnimal)ent;
/*     */           
/*  88 */           if (animal.func_70874_b() == 0) {
/*  89 */             nbAdultAnimal++;
/*     */           }
/*     */           
/*  92 */           nbAnimal++;
/*     */         }
/*     */         
/*  95 */         if ((nbAdultAnimal >= 2) && (nbAnimal < targetAnimals * 2)) {
/*  96 */           for (Entity ent : animals) {
/*  97 */             EntityAnimal animal = (EntityAnimal)ent;
/*     */             
/*  99 */             if ((animal.func_70874_b() == 0) && (!animal.func_70880_s())) {
/* 100 */               return packDest(null, villager.getHouse(), animal);
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 107 */     return null;
/*     */   }
/*     */   
/*     */   public ItemStack[] getHeldItemsTravelling(MillVillager villager)
/*     */     throws Exception
/*     */   {
/* 113 */     if ((villager.getGoalDestEntity() == null) || (!(villager.getGoalDestEntity() instanceof EntityAnimal))) {
/* 114 */       return null;
/*     */     }
/*     */     
/* 117 */     EntityAnimal animal = (EntityAnimal)villager.getGoalDestEntity();
/*     */     
/* 119 */     Item[] breedingItems = getBreedingItems(animal.getClass());
/*     */     
/* 121 */     if (breedingItems != null) {
/* 122 */       for (Item breedingItem : breedingItems) {
/* 123 */         if (villager.getHouse().countGoods(breedingItem) > 0) {
/* 124 */           return new ItemStack[] { new ItemStack(breedingItem, 1) };
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 129 */     return null;
/*     */   }
/*     */   
/*     */   public AStarConfig getPathingConfig()
/*     */   {
/* 134 */     return JPS_CONFIG_WIDE;
/*     */   }
/*     */   
/*     */   private List<Class> getValidAnimalClasses(MillVillager villager)
/*     */   {
/* 139 */     List<Class> validAnimals = new ArrayList();
/*     */     
/* 141 */     if (villager.getHouse().location.tags.contains("sheeps")) {
/* 142 */       validAnimals.add(EntitySheep.class);
/* 143 */       validAnimals.add(EntityChicken.class);
/*     */     }
/* 145 */     if (villager.getHouse().location.tags.contains("cattle")) {
/* 146 */       validAnimals.add(EntityCow.class);
/*     */     }
/* 148 */     if (villager.getHouse().location.tags.contains("pigs")) {
/* 149 */       validAnimals.add(EntityPig.class);
/*     */     }
/* 151 */     if (villager.getHouse().location.tags.contains("chicken")) {
/* 152 */       validAnimals.add(EntityChicken.class);
/*     */     }
/*     */     
/* 155 */     return validAnimals;
/*     */   }
/*     */   
/*     */   public boolean isFightingGoal()
/*     */   {
/* 160 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isPossibleSpecific(MillVillager villager) throws Exception
/*     */   {
/* 165 */     return getDestination(villager) != null;
/*     */   }
/*     */   
/*     */   public boolean lookAtGoal()
/*     */   {
/* 170 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean performAction(MillVillager villager)
/*     */     throws Exception
/*     */   {
/* 177 */     List<Class> validAnimals = getValidAnimalClasses(villager);
/*     */     
/* 179 */     for (Class animalClass : validAnimals)
/*     */     {
/* 181 */       List<Entity> animals = MillCommonUtilities.getEntitiesWithinAABB(villager.field_70170_p, animalClass, villager.getPos(), 4, 2);
/*     */       
/* 183 */       for (Entity ent : animals)
/*     */       {
/* 185 */         if (!ent.field_70128_L)
/*     */         {
/* 187 */           EntityAnimal animal = (EntityAnimal)ent;
/*     */           
/* 189 */           Item[] breedingItems = getBreedingItems(animal.getClass());
/*     */           
/* 191 */           boolean available = false;
/* 192 */           Item foundBreedingItem = null;
/*     */           
/* 194 */           if (breedingItems == null) {
/* 195 */             available = true;
/*     */           } else {
/* 197 */             for (Item breedingItem : breedingItems) {
/* 198 */               if ((!available) && (villager.getHouse().countGoods(breedingItem) > 0)) {
/* 199 */                 available = true;
/* 200 */                 foundBreedingItem = breedingItem;
/*     */               }
/*     */             }
/*     */           }
/*     */           
/* 205 */           if (available)
/*     */           {
/* 207 */             if ((!animal.func_70631_g_()) && (!animal.func_70880_s()) && (animal.func_70874_b() == 0)) {
/* 208 */               ReflectionHelper.setPrivateValue(EntityAnimal.class, animal, Integer.valueOf(600), 0);
/* 209 */               animal.func_70784_b(null);
/*     */               
/* 211 */               if (foundBreedingItem != null) {
/* 212 */                 villager.getHouse().takeGoods(foundBreedingItem, 1);
/*     */               }
/*     */               
/* 215 */               villager.func_71038_i();
/*     */               
/* 217 */               ServerSender.sendAnimalBreeding(animal);
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 224 */     return true;
/*     */   }
/*     */   
/*     */   public int priority(MillVillager villager) throws Exception
/*     */   {
/* 229 */     return 100;
/*     */   }
/*     */   
/*     */   public int range(MillVillager villager)
/*     */   {
/* 234 */     return 5;
/*     */   }
/*     */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\goal\GoalBreedAnimals.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */