/*     */ package org.millenaire.common;
/*     */ 
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.RegistryNamespaced;
/*     */ import org.millenaire.common.forge.CommonProxy;
/*     */ import org.millenaire.common.forge.Mill;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class InvItem
/*     */   implements Comparable<InvItem>
/*     */ {
/*     */   public static final int ANYENCHANTED = 1;
/*     */   public static final int ENCHANTEDSWORD = 2;
/*     */   public final Item item;
/*     */   public final Block block;
/*     */   public final ItemStack staticStack;
/*     */   public final ItemStack[] staticStackArray;
/*     */   public final int meta;
/*     */   public final int special;
/*     */   
/*     */   public InvItem(Block block)
/*     */     throws MLN.MillenaireException
/*     */   {
/*  30 */     this(block, 0);
/*     */   }
/*     */   
/*     */   public InvItem(Block block, int meta) throws MLN.MillenaireException {
/*  34 */     this.block = block;
/*  35 */     this.item = Item.func_150898_a(block);
/*  36 */     this.meta = meta;
/*  37 */     this.staticStack = new ItemStack(this.item, 1, meta);
/*  38 */     this.staticStackArray = new ItemStack[] { this.staticStack };
/*  39 */     this.special = 0;
/*     */     
/*  41 */     checkValidity();
/*     */   }
/*     */   
/*     */   public InvItem(int special) throws MLN.MillenaireException {
/*  45 */     this.special = special;
/*  46 */     this.staticStack = null;
/*  47 */     this.staticStackArray = new ItemStack[] { this.staticStack };
/*  48 */     this.item = null;
/*  49 */     this.block = null;
/*  50 */     this.meta = 0;
/*     */     
/*  52 */     checkValidity();
/*     */   }
/*     */   
/*     */   public InvItem(Item item) throws MLN.MillenaireException {
/*  56 */     this(item, 0);
/*     */   }
/*     */   
/*     */   public InvItem(Item item, int meta) throws MLN.MillenaireException {
/*  60 */     this.item = item;
/*  61 */     if (Block.func_149634_a(item) != Blocks.field_150350_a) {
/*  62 */       this.block = Block.func_149634_a(item);
/*     */     } else {
/*  64 */       this.block = null;
/*     */     }
/*  66 */     this.meta = meta;
/*  67 */     this.staticStack = new ItemStack(item, 1, meta);
/*  68 */     this.staticStackArray = new ItemStack[] { this.staticStack };
/*  69 */     this.special = 0;
/*     */     
/*  71 */     checkValidity();
/*     */   }
/*     */   
/*     */   public InvItem(ItemStack is) throws MLN.MillenaireException {
/*  75 */     this.item = is.func_77973_b();
/*  76 */     if (Block.func_149634_a(this.item) != Blocks.field_150350_a) {
/*  77 */       this.block = Block.func_149634_a(this.item);
/*     */     } else {
/*  79 */       this.block = null;
/*     */     }
/*  81 */     if (is.func_77960_j() > 0) {
/*  82 */       this.meta = is.func_77960_j();
/*     */     } else {
/*  84 */       this.meta = 0;
/*     */     }
/*  86 */     this.staticStack = new ItemStack(this.item, 1, this.meta);
/*  87 */     this.staticStackArray = new ItemStack[] { this.staticStack };
/*  88 */     this.special = 0;
/*     */     
/*  90 */     checkValidity();
/*     */   }
/*     */   
/*     */   public InvItem(String s) throws MLN.MillenaireException {
/*  94 */     this.special = 0;
/*  95 */     if (s.split("/").length > 2) {
/*  96 */       int id = Integer.parseInt(s.split("/")[0]);
/*     */       
/*  98 */       if (Item.func_150899_d(id) == null) {
/*  99 */         MLN.printException("Tried creating InvItem with null id from string: " + s, new Exception());
/* 100 */         this.item = null;
/*     */       } else {
/* 102 */         this.item = Item.func_150899_d(id);
/*     */       }
/*     */       
/* 105 */       if (Block.func_149729_e(id) == null) {
/* 106 */         this.block = null;
/*     */       } else {
/* 108 */         this.block = ((Block)Block.field_149771_c.func_148754_a(id));
/*     */       }
/*     */       
/* 111 */       this.meta = Integer.parseInt(s.split("/")[1]);
/* 112 */       this.staticStack = new ItemStack(this.item, 1, this.meta);
/*     */     } else {
/* 114 */       this.staticStack = null;
/* 115 */       this.item = null;
/* 116 */       this.block = null;
/* 117 */       this.meta = 0;
/*     */     }
/*     */     
/* 120 */     this.staticStackArray = new ItemStack[] { this.staticStack };
/*     */     
/* 122 */     checkValidity();
/*     */   }
/*     */   
/*     */   private void checkValidity() throws MLN.MillenaireException {
/* 126 */     if (this.block == Blocks.field_150350_a) {
/* 127 */       throw new MLN.MillenaireException("Attempted to create an InvItem for air blocks.");
/*     */     }
/* 129 */     if ((this.item == null) && (this.block == null) && (this.special == 0)) {
/* 130 */       throw new MLN.MillenaireException("Attempted to create an empty InvItem.");
/*     */     }
/*     */   }
/*     */   
/*     */   public int compareTo(InvItem ii)
/*     */   {
/* 136 */     if ((this.special > 0) || (ii.special > 0)) {
/* 137 */       return this.special - ii.special;
/*     */     }
/*     */     
/* 140 */     if ((this.item == null) || (ii.item == null)) {
/* 141 */       return this.special - ii.special;
/*     */     }
/*     */     
/* 144 */     return this.item.func_77658_a().compareTo(ii.item.func_77658_a()) + this.meta - ii.meta;
/*     */   }
/*     */   
/*     */   public boolean equals(Object obj)
/*     */   {
/* 149 */     if (this == obj) {
/* 150 */       return true;
/*     */     }
/* 152 */     if (!(obj instanceof InvItem)) {
/* 153 */       return false;
/*     */     }
/* 155 */     InvItem other = (InvItem)obj;
/*     */     
/* 157 */     return (other.item == this.item) && (other.meta == this.meta) && (other.special == this.special);
/*     */   }
/*     */   
/*     */   public Block getBlock() {
/* 161 */     return this.block;
/*     */   }
/*     */   
/*     */   public Item getItem() {
/* 165 */     return this.item;
/*     */   }
/*     */   
/*     */   public ItemStack getItemStack() {
/* 169 */     if (this.item == null) {
/* 170 */       return null;
/*     */     }
/* 172 */     return new ItemStack(this.item, 1, this.meta);
/*     */   }
/*     */   
/*     */   public String getName() {
/* 176 */     if (this.special == 1)
/* 177 */       return MLN.string("ui.anyenchanted");
/* 178 */     if (this.special == 2)
/* 179 */       return MLN.string("ui.enchantedsword");
/* 180 */     if ((this.meta == -1) && (this.block == Blocks.field_150364_r))
/* 181 */       return MLN.string("ui.woodforplanks");
/* 182 */     if ((this.meta == 0) && (this.block == Blocks.field_150364_r))
/* 183 */       return MLN.string("ui.woodoak");
/* 184 */     if ((this.meta == 1) && (this.block == Blocks.field_150364_r))
/* 185 */       return MLN.string("ui.woodpine");
/* 186 */     if ((this.meta == 2) && (this.block == Blocks.field_150364_r))
/* 187 */       return MLN.string("ui.woodbirch");
/* 188 */     if ((this.meta == 3) && (this.block == Blocks.field_150364_r))
/* 189 */       return MLN.string("ui.woodjungle");
/* 190 */     if (this.meta == -1) {
/* 191 */       return Mill.proxy.getItemName(this.item, 0);
/*     */     }
/* 193 */     if (this.item != null) {
/* 194 */       return Mill.proxy.getItemName(this.item, this.meta);
/*     */     }
/* 196 */     MLN.printException(new MLN.MillenaireException("Trying to get the name of an invalid InvItem."));
/* 197 */     return "id:" + this.item + ";meta:" + this.meta;
/*     */   }
/*     */   
/*     */ 
/*     */   public String getTranslationKey()
/*     */   {
/* 203 */     return "_item:" + Item.func_150891_b(this.item) + ":" + this.meta;
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/* 208 */     if (this.item == null) {
/* 209 */       return (this.meta << 8) + (this.special << 12);
/*     */     }
/* 211 */     return this.item.hashCode() + (this.meta << 8) + (this.special << 12);
/*     */   }
/*     */   
/*     */   public boolean matches(InvItem ii)
/*     */   {
/* 216 */     return (ii.item == this.item) && ((ii.meta == this.meta) || (ii.meta == -1) || (this.meta == -1));
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 221 */     return getName() + "/" + this.meta;
/*     */   }
/*     */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\InvItem.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */