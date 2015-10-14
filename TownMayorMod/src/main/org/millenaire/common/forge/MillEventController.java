/*     */ package org.millenaire.common.forge;
/*     */ 
/*     */ import cpw.mods.fml.common.eventhandler.SubscribeEvent;
/*     */ import java.io.File;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import net.minecraft.entity.ai.EntityAIAttackOnCollide;
/*     */ import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
/*     */ import net.minecraft.entity.ai.EntityAITasks;
/*     */ import net.minecraft.entity.monster.EntityMob;
/*     */ import net.minecraft.entity.monster.EntitySkeleton;
/*     */ import net.minecraft.entity.monster.EntityZombie;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.WorldServer;
/*     */ import net.minecraft.world.WorldServerMulti;
/*     */ import net.minecraft.world.storage.WorldInfo;
/*     */ import net.minecraftforge.event.entity.EntityJoinWorldEvent;
/*     */ import net.minecraftforge.event.world.WorldEvent.Load;
/*     */ import net.minecraftforge.event.world.WorldEvent.Save;
/*     */ import net.minecraftforge.event.world.WorldEvent.Unload;
/*     */ import org.millenaire.common.MLN;
/*     */ import org.millenaire.common.MillVillager;
/*     */ import org.millenaire.common.MillWorld;
/*     */ 
/*     */ public class MillEventController
/*     */ {
/*     */   @SubscribeEvent
/*     */   public void entityCreated(EntityJoinWorldEvent event)
/*     */   {
/*  30 */     if (Mill.startupError) {
/*  31 */       return;
/*     */     }
/*     */     
/*  34 */     if (((event.entity instanceof EntityZombie)) || ((event.entity instanceof EntitySkeleton))) {
/*  35 */       EntityMob mob = (EntityMob)event.entity;
/*  36 */       mob.field_70714_bg.func_75776_a(3, new EntityAIAttackOnCollide(mob, MillVillager.class, mob.func_70689_ay(), true));
/*     */       
/*     */       try
/*     */       {
/*  40 */         EntityAITasks targetTasks = mob.field_70715_bh;
/*  41 */         targetTasks.func_75776_a(3, new EntityAINearestAttackableTarget(mob, MillVillager.class, 10, false));
/*     */       }
/*     */       catch (Exception e) {
/*  44 */         MLN.printException("Error when trying to make new mob " + mob + " target villagers:", e);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void worldLoaded(WorldEvent.Load event)
/*     */   {
/*  52 */     Mill.proxy.loadLanguages();
/*     */     
/*  54 */     if ((Mill.displayMillenaireLocationError) && (!Mill.proxy.isTrueServer())) {
/*  55 */       Mill.proxy.sendLocalChat(Mill.proxy.getTheSinglePlayer(), '4', "ERREUR: Impossible de trouver le fichier de configuration " + Mill.proxy.getConfigFile().getAbsolutePath() + ". Vérifiez que le dossier millenaire est bien dans minecraft/mods/");
/*     */       
/*  57 */       Mill.proxy.sendLocalChat(Mill.proxy.getTheSinglePlayer(), '4', "ERROR: Could not find the config file at " + Mill.proxy.getConfigFile().getAbsolutePath() + ". Check that the millenaire directory is in minecraft/mods/");
/*     */       
/*  59 */       return;
/*     */     }
/*     */     
/*  62 */     if (!(event.world instanceof WorldServer)) {
/*  63 */       MLN.temp(event.world, "Loading new client world");
/*  64 */       Mill.clientWorld = new MillWorld(event.world);
/*  65 */       Mill.proxy.testTextureSize();
/*     */     }
/*  67 */     else if (!(event.world instanceof WorldServerMulti)) {
/*  68 */       MLN.temp(event.world, "Loading new world");
/*  69 */       MillWorld newWorld = new MillWorld(event.world);
/*  70 */       Mill.serverWorlds.add(newWorld);
/*  71 */       newWorld.loadData();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   @SubscribeEvent
/*     */   public void worldSaved(WorldEvent.Save event)
/*     */   {
/*  79 */     if (Mill.startupError) {
/*  80 */       return;
/*     */     }
/*     */     
/*  83 */     if (event.world.func_72912_H().func_76076_i() != 0) {
/*  84 */       return;
/*     */     }
/*     */     
/*  87 */     if (!(event.world instanceof WorldServer)) {
/*  88 */       Mill.clientWorld.saveEverything();
/*     */     }
/*     */     else {
/*  91 */       for (MillWorld mw : Mill.serverWorlds) {
/*  92 */         if (mw.world == event.world) {
/*  93 */           mw.saveEverything();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void worldUnloaded(WorldEvent.Unload event)
/*     */   {
/* 102 */     if (Mill.startupError) {
/* 103 */       return;
/*     */     }
/*     */     
/* 106 */     if (event.world.func_72912_H().func_76076_i() != 0) {
/* 107 */       return;
/*     */     }
/*     */     
/* 110 */     if (!(event.world instanceof WorldServer)) {
/* 111 */       if (Mill.clientWorld.world == event.world) {
/* 112 */         Mill.clientWorld = null;
/* 113 */         MLN.temp(null, "Unloaded client world.");
/*     */       } else {
/* 115 */         MLN.temp(null, "Skipped unloading client world as it's not current world.");
/*     */       }
/*     */     }
/*     */     else {
/* 119 */       List<MillWorld> toDelete = new ArrayList();
/*     */       
/* 121 */       for (MillWorld mw : Mill.serverWorlds) {
/* 122 */         if (mw.world == event.world) {
/* 123 */           toDelete.add(mw);
/*     */         }
/*     */       }
/*     */       
/* 127 */       for (MillWorld mw : toDelete) {
/* 128 */         Mill.serverWorlds.remove(mw);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\forge\MillEventController.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */