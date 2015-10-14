/*    */ package org.millenaire.common.block;
/*    */ 
/*    */ import net.minecraft.block.BlockPane;
/*    */ import net.minecraft.block.material.Material;
/*    */ import net.minecraft.client.renderer.texture.IIconRegister;
/*    */ import net.minecraft.util.IIcon;
/*    */ import org.millenaire.common.core.MillCommonUtilities;
/*    */ 
/*    */ public class BlockMLNPane extends BlockPane
/*    */ {
/*    */   private IIcon sideTexture;
/*    */   private final String textureName;
/*    */   private final String sideTextureName;
/*    */   
/*    */   public BlockMLNPane(String textureName, String sideTexture, Material material, boolean flag)
/*    */   {
/* 17 */     super(textureName, sideTexture, material, flag);
/* 18 */     this.textureName = textureName;
/* 19 */     this.sideTextureName = sideTexture;
/* 20 */     func_149647_a(org.millenaire.common.forge.Mill.tabMillenaire);
/*    */   }
/*    */   
/*    */ 
/*    */   public IIcon func_150097_e()
/*    */   {
/* 26 */     return this.sideTexture;
/*    */   }
/*    */   
/*    */   public void func_149651_a(IIconRegister iconRegister)
/*    */   {
/* 31 */     this.field_149761_L = MillCommonUtilities.getIcon(iconRegister, this.textureName);
/* 32 */     this.sideTexture = MillCommonUtilities.getIcon(iconRegister, this.sideTextureName);
/*    */   }
/*    */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\block\BlockMLNPane.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */