/*    */ package org.millenaire.client.texture;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*    */ import org.millenaire.common.Point;
/*    */ 
/*    */ public class TextureAmuletYddrasil extends TextureAtlasSprite
/*    */ {
/*    */   public TextureAmuletYddrasil(String iconName)
/*    */   {
/* 12 */     super(iconName);
/*    */   }
/*    */   
/*    */   private int getScore(Minecraft mc) {
/* 16 */     int level = 0;
/*    */     
/* 18 */     if ((mc.field_71441_e != null) && (mc.field_71439_g != null)) {
/* 19 */       Point p = new Point(mc.field_71439_g);
/*    */       
/* 21 */       level = (int)Math.floor(p.getiY());
/*    */     }
/*    */     
/* 24 */     if (level > 127) {
/* 25 */       level = 127;
/*    */     }
/*    */     
/* 28 */     level /= 8;
/*    */     
/* 30 */     return level;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void func_94219_l()
/*    */   {
/* 37 */     int iconPos = getScore(Minecraft.func_71410_x());
/*    */     
/* 39 */     if (iconPos > 15) {
/* 40 */       iconPos = 15;
/*    */     }
/* 42 */     if (iconPos < 0) {
/* 43 */       iconPos = 0;
/*    */     }
/*    */     
/* 46 */     if (iconPos != this.field_110973_g) {
/* 47 */       this.field_110973_g = iconPos;
/* 48 */       net.minecraft.client.renderer.texture.TextureUtil.func_147955_a((int[][])this.field_110976_a.get(this.field_110973_g), this.field_130223_c, this.field_130224_d, this.field_110975_c, this.field_110974_d, false, false);
/*    */     }
/*    */   }
/*    */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\client\texture\TextureAmuletYddrasil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */