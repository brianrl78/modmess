/*    */ package org.millenaire.common.building;
/*    */ 
/*    */ import net.minecraft.block.Block;
/*    */ import org.millenaire.common.MLN;
/*    */ import org.millenaire.common.MLN.MillenaireException;
/*    */ import org.millenaire.common.core.MillCommonUtilities;
/*    */ 
/*    */ public class PointType
/*    */ {
/*    */   static PointType readColourPoint(String s)
/*    */     throws MLN.MillenaireException
/*    */   {
/* 13 */     String[] params = s.split(";", -1);
/*    */     
/* 15 */     if (params.length != 5) {
/* 16 */       throw new MLN.MillenaireException("Line " + s + " in blocklist.txt does not have five fields.");
/*    */     }
/*    */     
/* 19 */     String[] rgb = params[4].split("/", -1);
/*    */     
/* 21 */     if (rgb.length != 3) {
/* 22 */       throw new MLN.MillenaireException("Colour in line " + s + " does not have three values.");
/*    */     }
/*    */     
/* 25 */     int colour = (Integer.parseInt(rgb[0]) << 16) + (Integer.parseInt(rgb[1]) << 8) + (Integer.parseInt(rgb[2]) << 0);
/*    */     
/* 27 */     if (MLN.LogBuildingPlan >= 1) {
/* 28 */       MLN.major(null, "Loading colour point: " + BuildingPlan.getColourString(colour) + ", " + params[0]);
/*    */     }
/*    */     
/* 31 */     if (params[1].length() == 0) {
/* 32 */       return new PointType(colour, params[0]);
/*    */     }
/* 34 */     return new PointType(colour, params[1], Integer.parseInt(params[2]), Boolean.parseBoolean(params[3]));
/*    */   }
/*    */   
/*    */ 
/*    */ 
/* 39 */   int colour = -1; int meta = -1;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/* 44 */   boolean secondStep = false;
/*    */   char letter;
/*    */   
/* 47 */   public PointType(char letter, Block block, int meta, boolean secondStep) { this.letter = letter;
/* 48 */     this.block = block;
/* 49 */     this.meta = meta;
/* 50 */     this.secondStep = secondStep;
/* 51 */     this.name = null; }
/*    */   
/*    */   final String name;
/*    */   Block block;
/* 55 */   public PointType(char letter, String name) { this.name = name;
/* 56 */     this.letter = letter;
/* 57 */     this.block = null;
/*    */   }
/*    */   
/*    */   public PointType(char letter, String minecraftBlockName, int meta, boolean secondStep) {
/* 61 */     this.letter = letter;
/* 62 */     this.block = Block.func_149684_b(minecraftBlockName);
/* 63 */     this.meta = meta;
/* 64 */     this.secondStep = secondStep;
/* 65 */     this.name = null;
/*    */   }
/*    */   
/*    */   public PointType(int colour, String name) {
/* 69 */     this.name = name;
/* 70 */     this.colour = colour;
/* 71 */     this.block = null;
/*    */   }
/*    */   
/*    */   public PointType(int colour, String minecraftBlockName, int meta, boolean secondStep) {
/* 75 */     this.colour = colour;
/* 76 */     this.block = Block.func_149684_b(minecraftBlockName);
/* 77 */     this.meta = meta;
/* 78 */     this.secondStep = secondStep;
/* 79 */     this.name = null;
/*    */   }
/*    */   
/*    */   public Block getBlock() {
/* 83 */     return this.block;
/*    */   }
/*    */   
/*    */   public boolean isSubType(String type) {
/* 87 */     if (this.name == null) {
/* 88 */       return false;
/*    */     }
/* 90 */     return this.name.startsWith(type);
/*    */   }
/*    */   
/*    */   public boolean isType(String type) {
/* 94 */     return type.equalsIgnoreCase(this.name);
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 99 */     return this.name + "/" + this.colour + "/" + this.block + "/" + this.meta + "/" + MillCommonUtilities.getPointHash(this.block, this.meta);
/*    */   }
/*    */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\building\PointType.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */