/*    */ package org.millenaire.client.texture;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*    */ import net.minecraft.client.renderer.texture.TextureUtil;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.monster.EntityMob;
/*    */ import net.minecraft.world.World;
/*    */ import org.millenaire.common.Point;
/*    */ import org.millenaire.common.core.MillCommonUtilities;
/*    */ 
/*    */ public class TextureAmuletVishnu
/*    */   extends TextureAtlasSprite
/*    */ {
/*    */   private static final int radius = 20;
/*    */   
/*    */   public TextureAmuletVishnu(String iconName)
/*    */   {
/* 20 */     super(iconName);
/*    */   }
/*    */   
/*    */   private int getScore(Minecraft mc) {
/* 24 */     double level = 0.0D;
/*    */     
/* 26 */     double closestDistance = Double.MAX_VALUE;
/*    */     Point p;
/* 28 */     if ((mc.field_71441_e != null) && (mc.field_71439_g != null))
/*    */     {
/* 30 */       World world = mc.field_71441_e;
/*    */       
/* 32 */       p = new Point(mc.field_71439_g);
/*    */       
/* 34 */       List<Entity> entities = MillCommonUtilities.getEntitiesWithinAABB(world, EntityMob.class, p, 20, 20);
/*    */       
/* 36 */       for (Entity ent : entities) {
/* 37 */         if (p.distanceTo(ent) < closestDistance) {
/* 38 */           closestDistance = p.distanceTo(ent);
/*    */         }
/*    */       }
/*    */     }
/*    */     
/* 43 */     if (closestDistance > 20.0D) {
/* 44 */       level = 0.0D;
/*    */     } else {
/* 46 */       level = (20.0D - closestDistance) / 20.0D;
/*    */     }
/*    */     
/* 49 */     return (int)(level * 15.0D);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void func_94219_l()
/*    */   {
/* 56 */     int iconPos = getScore(Minecraft.func_71410_x());
/*    */     
/* 58 */     if (iconPos > 15) {
/* 59 */       iconPos = 15;
/*    */     }
/* 61 */     if (iconPos < 0) {
/* 62 */       iconPos = 0;
/*    */     }
/*    */     
/* 65 */     if (iconPos != this.field_110973_g) {
/* 66 */       this.field_110973_g = iconPos;
/* 67 */       TextureUtil.func_147955_a((int[][])this.field_110976_a.get(this.field_110973_g), this.field_130223_c, this.field_130224_d, this.field_110975_c, this.field_110974_d, false, false);
/*    */     }
/*    */   }
/*    */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\client\texture\TextureAmuletVishnu.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */