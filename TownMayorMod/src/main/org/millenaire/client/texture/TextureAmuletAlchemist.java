/*    */ package org.millenaire.client.texture;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*    */ import net.minecraft.client.renderer.texture.TextureUtil;
/*    */ import net.minecraft.init.Blocks;
/*    */ import net.minecraft.world.World;
/*    */ import org.millenaire.common.Point;
/*    */ 
/*    */ public class TextureAmuletAlchemist extends TextureAtlasSprite
/*    */ {
/*    */   private static final int radius = 5;
/*    */   
/*    */   public TextureAmuletAlchemist(String iconName)
/*    */   {
/* 17 */     super(iconName);
/*    */   }
/*    */   
/*    */   private int getScore(Minecraft mc) {
/* 21 */     int score = 0;
/*    */     
/* 23 */     if ((mc.field_71441_e != null) && (mc.field_71439_g != null))
/*    */     {
/* 25 */       World world = mc.field_71441_e;
/*    */       
/* 27 */       Point p = new Point(mc.field_71439_g);
/*    */       
/* 29 */       int startY = Math.max(p.getiY() - 5, 0);
/* 30 */       int endY = Math.min(p.getiY() + 5, 127);
/*    */       
/* 32 */       for (int i = p.getiX() - 5; i < p.getiX() + 5; i++) {
/* 33 */         for (int j = p.getiZ() - 5; j < p.getiZ() + 5; j++) {
/* 34 */           for (int k = startY; k < endY; k++) {
/* 35 */             net.minecraft.block.Block block = world.func_147439_a(i, k, j);
/* 36 */             if (block == Blocks.field_150365_q) {
/* 37 */               score++;
/* 38 */             } else if (block == Blocks.field_150482_ag) {
/* 39 */               score += 30;
/* 40 */             } else if (block == Blocks.field_150412_bA) {
/* 41 */               score += 30;
/* 42 */             } else if (block == Blocks.field_150352_o) {
/* 43 */               score += 10;
/* 44 */             } else if (block == Blocks.field_150366_p) {
/* 45 */               score += 5;
/* 46 */             } else if (block == Blocks.field_150369_x) {
/* 47 */               score += 10;
/* 48 */             } else if (block == Blocks.field_150450_ax) {
/* 49 */               score += 5;
/* 50 */             } else if (block == Blocks.field_150439_ay) {
/* 51 */               score += 5;
/*    */             }
/*    */           }
/*    */         }
/*    */       }
/*    */     }
/*    */     
/* 58 */     if (score > 100) {
/* 59 */       score = 100;
/*    */     }
/*    */     
/* 62 */     return score * 15 / 100;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void func_94219_l()
/*    */   {
/* 69 */     int iconPos = getScore(Minecraft.func_71410_x());
/*    */     
/*    */ 
/* 72 */     if (iconPos > 15) {
/* 73 */       iconPos = 15;
/*    */     }
/* 75 */     if (iconPos < 0) {
/* 76 */       iconPos = 0;
/*    */     }
/*    */     
/* 79 */     if (iconPos != this.field_110973_g) {
/* 80 */       this.field_110973_g = iconPos;
/* 81 */       TextureUtil.func_147955_a((int[][])this.field_110976_a.get(this.field_110973_g), this.field_130223_c, this.field_130224_d, this.field_110975_c, this.field_110974_d, false, false);
/*    */     }
/*    */   }
/*    */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\client\texture\TextureAmuletAlchemist.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */