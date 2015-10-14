/*     */ package org.millenaire.client.gui;
/*     */ 
/*     */ import java.util.List;
/*     */ 
/*     */ public abstract class GuiText extends net.minecraft.client.gui.GuiScreen {
/*     */   public static final String WHITE = "<white>";
/*     */   public static final String YELLOW = "<yellow>";
/*     */   public static final String PINK = "<pink>";
/*     */   public static final String LIGHTRED = "<lightred>";
/*     */   public static final String CYAN = "<cyan>";
/*     */   public static final String LIGHTGREEN = "<lightgreen>";
/*     */   public static final String BLUE = "<blue>";
/*     */   public static final String DARKGREY = "<darkgrey>";
/*     */   public static final String LIGHTGREY = "<lightgrey>";
/*     */   public static final String ORANGE = "<orange>";
/*     */   public static final String PURPLE = "<purple>";
/*     */   public static final String DARKRED = "<darkred>";
/*     */   public static final String LIGHTBLUE = "<lightblue>";
/*     */   public static final String DARKGREEN = "<darkgreen>";
/*     */   public static final String DARKBLUE = "<darkblue>";
/*     */   public static final String BLACK = "<black>";
/*     */   public static final String LINE_HELP_GUI_BUTTON = "<help_gui_button>";
/*     */   public static final String LINE_CHUNK_GUI_BUTTON = "<chunk_gui_button>";
/*     */   public static final String LINE_CONFIG_GUI_BUTTON = "<config_gui_button>";
/*     */   
/*  26 */   public static class Line { String text = "";
/*  27 */     GuiText.MillGuiButton[] buttons = null;
/*  28 */     GuiText.MillGuiTextField textField = null;
/*  29 */     List<net.minecraft.item.ItemStack> icons = null;
/*  30 */     List<String> iconExtraLegends = null;
/*  31 */     boolean canCutAfter = true;
/*  32 */     boolean shadow = false;
/*  33 */     int margin = 0;
/*     */     
/*     */     public Line() {}
/*     */     
/*     */     public Line(boolean canCutAfter)
/*     */     {
/*  39 */       this.canCutAfter = canCutAfter;
/*     */     }
/*     */     
/*     */     public Line(List<net.minecraft.item.ItemStack> icons, List<String> iconExtraLegends, String s, int margin) {
/*  43 */       this.icons = icons;
/*  44 */       this.iconExtraLegends = iconExtraLegends;
/*     */       
/*  46 */       if ((icons != null) && (iconExtraLegends == null)) {
/*  47 */         org.millenaire.common.MLN.printException("iconExtraLegends is null but icons isn't.", new Exception());
/*  48 */       } else if ((icons != null) && (iconExtraLegends != null) && (icons.size() != iconExtraLegends.size())) {
/*  49 */         org.millenaire.common.MLN.printException("iconExtraLegends has a size of " + iconExtraLegends.size() + " but icons has a size of " + icons.size(), new Exception());
/*     */       }
/*     */       
/*  52 */       this.text = s;
/*  53 */       this.canCutAfter = false;
/*     */       
/*  55 */       this.margin = margin;
/*     */     }
/*     */     
/*     */     public Line(GuiText.MillGuiButton b) {
/*  59 */       this.buttons = new GuiText.MillGuiButton[] { b };
/*  60 */       this.canCutAfter = false;
/*     */     }
/*     */     
/*     */     public Line(GuiText.MillGuiButton b, GuiText.MillGuiButton b2) {
/*  64 */       this.buttons = new GuiText.MillGuiButton[] { b, b2 };
/*  65 */       this.canCutAfter = false;
/*     */     }
/*     */     
/*     */     public Line(GuiText.MillGuiButton b, GuiText.MillGuiButton b2, GuiText.MillGuiButton b3) {
/*  69 */       this.buttons = new GuiText.MillGuiButton[] { b, b2, b3 };
/*  70 */       this.canCutAfter = false;
/*     */     }
/*     */     
/*     */     public Line(GuiText.MillGuiTextField tf) {
/*  74 */       this.textField = tf;
/*     */     }
/*     */     
/*     */     public Line(String s) {
/*  78 */       if (s == null) {
/*  79 */         this.text = "";
/*     */       } else {
/*  81 */         this.text = s;
/*  82 */         interpretTags();
/*     */       }
/*     */     }
/*     */     
/*     */     public Line(String s, boolean canCutAfter) {
/*  87 */       if (s == null) {
/*  88 */         this.text = "";
/*     */       } else {
/*  90 */         this.text = s;
/*  91 */         interpretTags();
/*     */       }
/*  93 */       this.canCutAfter = canCutAfter;
/*     */     }
/*     */     
/*     */     public Line(String s, Line model, int lnpos)
/*     */     {
/*  98 */       if ((model.icons != null) && (lnpos % 2 == 0))
/*     */       {
/* 100 */         int lnicon = lnpos / 2;
/*     */         
/* 102 */         this.icons = new java.util.ArrayList();
/* 103 */         this.iconExtraLegends = new java.util.ArrayList();
/*     */         
/* 105 */         for (int i = lnicon * 4; (i < model.icons.size()) && (i < (lnicon + 1) * 4); i++) {
/* 106 */           this.icons.add(model.icons.get(i));
/* 107 */           this.iconExtraLegends.add(model.iconExtraLegends.get(i));
/*     */         }
/*     */       }
/*     */       
/* 111 */       if (s == null) {
/* 112 */         this.text = "";
/*     */       } else {
/* 114 */         this.text = s;
/* 115 */         interpretTags();
/*     */       }
/* 117 */       this.canCutAfter = model.canCutAfter;
/* 118 */       this.shadow = model.shadow;
/* 119 */       this.margin = model.margin;
/*     */     }
/*     */     
/*     */     public Line(String s, GuiText.MillGuiTextField tf) {
/* 123 */       this.textField = tf;
/* 124 */       if (s == null) {
/* 125 */         this.text = "";
/*     */       } else {
/* 127 */         this.text = s;
/* 128 */         interpretTags();
/*     */       }
/*     */     }
/*     */     
/*     */     public boolean empty() {
/* 133 */       return ((this.text == null) || (this.text.length() == 0)) && (this.buttons == null) && (this.textField == null);
/*     */     }
/*     */     
/*     */     private void interpretTags() {
/* 137 */       if (this.text.startsWith("<shadow>")) {
/* 138 */         this.shadow = true;
/* 139 */         this.text = this.text.replaceAll("<shadow>", "");
/*     */       }
/* 141 */       this.text = this.text.replaceAll("<black>", "§0");
/* 142 */       this.text = this.text.replaceAll("<darkblue>", "§1");
/* 143 */       this.text = this.text.replaceAll("<darkgreen>", "§2");
/* 144 */       this.text = this.text.replaceAll("<lightblue>", "§3");
/* 145 */       this.text = this.text.replaceAll("<darkred>", "§4");
/* 146 */       this.text = this.text.replaceAll("<purple>", "§5");
/* 147 */       this.text = this.text.replaceAll("<orange>", "§6");
/* 148 */       this.text = this.text.replaceAll("<lightgrey>", "§7");
/* 149 */       this.text = this.text.replaceAll("<darkgrey>", "§8");
/* 150 */       this.text = this.text.replaceAll("<blue>", "§9");
/* 151 */       this.text = this.text.replaceAll("<lightgreen>", "§a");
/* 152 */       this.text = this.text.replaceAll("<cyan>", "§b");
/* 153 */       this.text = this.text.replaceAll("<lightred>", "§c");
/* 154 */       this.text = this.text.replaceAll("<pink>", "§d");
/* 155 */       this.text = this.text.replaceAll("<yellow>", "§e");
/* 156 */       this.text = this.text.replaceAll("<white>", "§f");
/*     */     }
/*     */   }
/*     */   
/*     */   public static class MillGuiButton extends net.minecraft.client.gui.GuiButton
/*     */   {
/*     */     public static final int HELPBUTTON = 2000;
/*     */     public static final int CHUNKBUTTON = 3000;
/*     */     public static final int CONFIGBUTTON = 4000;
/*     */     
/*     */     public MillGuiButton(int par1, int par2, int par3, int par4, int par5, String par6Str)
/*     */     {
/* 168 */       super(par2, par3, par4, par5, par6Str);
/*     */     }
/*     */     
/*     */     public MillGuiButton(String label, int id) {
/* 172 */       super(0, 0, 0, 0, label);
/*     */     }
/*     */     
/*     */     public int getHeight() {
/* 176 */       return this.field_146121_g;
/*     */     }
/*     */     
/*     */     public int getWidth() {
/* 180 */       return this.field_146120_f;
/*     */     }
/*     */     
/*     */     public void setHeight(int h) {
/* 184 */       this.field_146121_g = h;
/*     */     }
/*     */     
/*     */     public void setWidth(int w) {
/* 188 */       this.field_146120_f = w;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class MillGuiTextField extends net.minecraft.client.gui.GuiTextField
/*     */   {
/*     */     public final String fieldKey;
/*     */     
/*     */     public MillGuiTextField(net.minecraft.client.gui.FontRenderer par1FontRenderer, int par2, int par3, int par4, int par5, String fieldKey)
/*     */     {
/* 198 */       super(par2, par3, par4, par5);
/* 199 */       this.fieldKey = fieldKey;
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
/*     */ 
/*     */ 
/*     */ 
/* 225 */   protected int pageNum = 0;
/*     */   
/* 227 */   protected List<List<Line>> descText = null;
/*     */   
/* 229 */   List<MillGuiTextField> textFields = new java.util.ArrayList();
/*     */   
/*     */ 
/* 232 */   protected static net.minecraft.client.renderer.entity.RenderItem itemRenderer = new net.minecraft.client.renderer.entity.RenderItem();
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
/*     */   public List<List<Line>> adjustText(List<List<Line>> baseText)
/*     */   {
/* 246 */     List<List<Line>> text = new java.util.ArrayList();
/*     */     
/* 248 */     for (List<Line> page : baseText)
/*     */     {
/* 250 */       List<Line> newPage = new java.util.ArrayList();
/*     */       
/* 252 */       for (Line line : page) {
/* 253 */         if ((line.buttons != null) || (line.textField != null)) {
/* 254 */           newPage.add(line);
/*     */         } else {
/* 256 */           for (String l : line.text.split("<ret>"))
/*     */           {
/* 258 */             int lineSize = getLineSizeInPx() - line.margin;
/* 259 */             int lineSizeInChar = getLineWidthInChars(lineSize);
/*     */             
/* 261 */             int lnpos = 0;
/*     */             
/* 263 */             while (this.field_146289_q.func_78256_a(l) > lineSize) {
/* 264 */               int end = l.lastIndexOf(' ', lineSizeInChar);
/* 265 */               if (end < 1) {
/* 266 */                 end = lineSizeInChar;
/*     */               }
/* 268 */               if (end >= l.length()) {
/* 269 */                 end = l.length() / 2;
/*     */               }
/* 271 */               String subLine = l.substring(0, end);
/* 272 */               l = l.substring(subLine.length()).trim();
/*     */               
/* 274 */               int colPos = subLine.lastIndexOf('§');
/*     */               
/* 276 */               if (colPos > -1)
/*     */               {
/* 278 */                 l = subLine.substring(colPos, colPos + 2) + l;
/*     */               }
/*     */               
/* 281 */               newPage.add(new Line(subLine, line, lnpos));
/*     */               
/* 283 */               lnpos++;
/*     */             }
/* 285 */             newPage.add(new Line(l, line, lnpos));
/*     */             
/* 287 */             lnpos++;
/*     */             
/* 289 */             if (line.icons != null) {
/* 290 */               for (int i = lnpos; i < line.icons.size() / 2; i++) {
/* 291 */                 newPage.add(new Line("", line, i));
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 299 */       while (newPage.size() > getPageSize()) {
/* 300 */         List<Line> newPage2 = new java.util.ArrayList();
/*     */         
/* 302 */         int nblinetaken = 0;
/*     */         
/* 304 */         for (int i = 0; i < getPageSize(); i++)
/*     */         {
/* 306 */           int blockSize = -1;
/*     */           
/* 308 */           for (int j = i; (j < newPage.size()) && (blockSize == -1); j++) {
/* 309 */             if (((Line)newPage.get(j)).canCutAfter) {
/* 310 */               blockSize = j - i;
/*     */             }
/*     */           }
/*     */           
/* 314 */           if (blockSize == -1) {
/* 315 */             blockSize = newPage.size() - i;
/*     */           }
/*     */           
/* 318 */           if (i + blockSize > getPageSize()) {
/*     */             break;
/*     */           }
/*     */           
/* 322 */           newPage2.add(newPage.get(i));
/* 323 */           nblinetaken++;
/*     */         }
/* 325 */         for (int i = 0; i < nblinetaken; i++) {
/* 326 */           newPage.remove(0);
/*     */         }
/*     */         
/* 329 */         newPage2 = clearEmptyLines(newPage2);
/*     */         
/* 331 */         if (newPage2 != null) {
/* 332 */           text.add(newPage2);
/*     */         }
/*     */       }
/*     */       
/* 336 */       List<Line> adjustedPage = clearEmptyLines(newPage);
/*     */       
/* 338 */       if (adjustedPage != null) {
/* 339 */         text.add(adjustedPage);
/*     */       }
/*     */     }
/*     */     
/* 343 */     return text;
/*     */   }
/*     */   
/*     */ 
/*     */   public void buttonPagination()
/*     */   {
/*     */     try
/*     */     {
/* 351 */       if (this.descText == null) {
/* 352 */         return;
/*     */       }
/*     */       
/* 355 */       int xStart = (this.field_146294_l - getXSize()) / 2;
/* 356 */       int yStart = (this.field_146295_m - getYSize()) / 2;
/*     */       
/* 358 */       this.field_146292_n.clear();
/* 359 */       this.textFields.clear();
/*     */       
/* 361 */       int vpos = 6;
/*     */       
/* 363 */       if (this.pageNum < this.descText.size()) {
/* 364 */         for (int cp = 0; (cp < getPageSize()) && (cp < ((List)this.descText.get(this.pageNum)).size()); cp++)
/*     */         {
/* 366 */           Line line = (Line)((List)this.descText.get(this.pageNum)).get(cp);
/* 367 */           if (line.buttons != null)
/*     */           {
/* 369 */             if (line.buttons.length == 1) {
/* 370 */               if (line.buttons[0] != null) {
/* 371 */                 line.buttons[0].field_146128_h = (xStart + getXSize() / 2 - 100);
/* 372 */                 line.buttons[0].setWidth(200);
/*     */               }
/* 374 */             } else if (line.buttons.length == 2) {
/* 375 */               if (line.buttons[0] != null) {
/* 376 */                 line.buttons[0].field_146128_h = (xStart + getXSize() / 2 - 100);
/* 377 */                 line.buttons[0].setWidth(95);
/*     */               }
/* 379 */               if (line.buttons[1] != null) {
/* 380 */                 line.buttons[1].field_146128_h = (xStart + getXSize() / 2 + 5);
/* 381 */                 line.buttons[1].setWidth(95);
/*     */               }
/* 383 */             } else if (line.buttons.length == 3) {
/* 384 */               if (line.buttons[0] != null) {
/* 385 */                 line.buttons[0].field_146128_h = (xStart + getXSize() / 2 - 100);
/* 386 */                 line.buttons[0].setWidth(60);
/*     */               }
/* 388 */               if (line.buttons[1] != null) {
/* 389 */                 line.buttons[1].field_146128_h = (xStart + getXSize() / 2 - 30);
/* 390 */                 line.buttons[1].setWidth(60);
/*     */               }
/* 392 */               if (line.buttons[2] != null) {
/* 393 */                 line.buttons[2].field_146128_h = (xStart + getXSize() / 2 + 40);
/* 394 */                 line.buttons[2].setWidth(60);
/*     */               }
/*     */             }
/*     */             
/* 398 */             for (int i = 0; i < line.buttons.length; i++) {
/* 399 */               if (line.buttons[i] != null) {
/* 400 */                 line.buttons[i].field_146129_i = (yStart + vpos);
/* 401 */                 line.buttons[i].setHeight(20);
/* 402 */                 this.field_146292_n.add(line.buttons[i]);
/*     */               }
/*     */             }
/* 405 */           } else if (line.textField != null) {
/* 406 */             MillGuiTextField textField = new MillGuiTextField(this.field_146289_q, xStart + getXSize() / 2 + 40, yStart + vpos, 95, 20, line.textField.fieldKey);
/* 407 */             textField.func_146180_a(line.textField.func_146179_b());
/* 408 */             textField.func_146203_f(line.textField.func_146208_g());
/* 409 */             textField.func_146193_g(-1);
/* 410 */             line.textField = textField;
/* 411 */             line.textField.func_146193_g(-1);
/* 412 */             line.textField.func_146185_a(false);
/*     */             
/* 414 */             this.textFields.add(textField);
/*     */           }
/* 416 */           vpos += 10;
/*     */         }
/*     */       }
/*     */     } catch (Exception e) {
/* 420 */       org.millenaire.common.MLN.printException("Exception while doing button pagination in GUI " + this, e);
/*     */     }
/*     */   }
/*     */   
/*     */   private List<Line> clearEmptyLines(List<Line> page) {
/* 425 */     List<Line> clearedPage = new java.util.ArrayList();
/*     */     
/* 427 */     boolean nonEmptyLine = false;
/*     */     
/* 429 */     for (Line line : page) {
/* 430 */       if (!line.empty()) {
/* 431 */         clearedPage.add(line);
/* 432 */         nonEmptyLine = true;
/*     */       }
/* 434 */       else if (nonEmptyLine) {
/* 435 */         clearedPage.add(line);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 440 */     if (clearedPage.size() > 0) {
/* 441 */       return clearedPage;
/*     */     }
/* 443 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   protected void closeWindow()
/*     */   {
/* 449 */     this.field_146297_k.func_147108_a(null);
/* 450 */     this.field_146297_k.func_71381_h();
/*     */   }
/*     */   
/*     */   public List<List<Line>> convertAdjustText(List<List<String>> baseText)
/*     */   {
/* 455 */     List<List<Line>> text = new java.util.ArrayList();
/*     */     
/* 457 */     for (List<String> page : baseText)
/*     */     {
/* 459 */       List<Line> newPage = new java.util.ArrayList();
/*     */       
/* 461 */       for (String s : page) {
/* 462 */         if (s.equals("<help_gui_button>")) {
/* 463 */           newPage.add(new Line(new MillGuiButton(2000, 0, 0, 0, 0, org.millenaire.common.MLN.string("ui.helpbutton"))));
/* 464 */         } else if (s.equals("<chunk_gui_button>")) {
/* 465 */           newPage.add(new Line(new MillGuiButton(3000, 0, 0, 0, 0, org.millenaire.common.MLN.string("ui.chunkbutton"))));
/* 466 */         } else if (s.equals("<config_gui_button>")) {
/* 467 */           newPage.add(new Line(new MillGuiButton(4000, 0, 0, 0, 0, org.millenaire.common.MLN.string("ui.configbutton"))));
/*     */         } else {
/* 469 */           newPage.add(new Line(s, true));
/*     */         }
/*     */       }
/*     */       
/* 473 */       text.add(newPage);
/*     */     }
/*     */     
/* 476 */     return adjustText(text);
/*     */   }
/*     */   
/*     */   protected abstract void customDrawBackground(int paramInt1, int paramInt2, float paramFloat);
/*     */   
/*     */   protected abstract void customDrawScreen(int paramInt1, int paramInt2, float paramFloat);
/*     */   
/*     */   public void decrementPage()
/*     */   {
/* 485 */     if (this.descText == null) {
/* 486 */       return;
/*     */     }
/*     */     
/* 489 */     if (this.pageNum > 0) {
/* 490 */       this.pageNum -= 1;
/*     */     }
/* 492 */     buttonPagination();
/*     */   }
/*     */   
/*     */   public boolean func_73868_f()
/*     */   {
/* 497 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   protected void drawHoveringText(List par1List, int par2, int par3, net.minecraft.client.gui.FontRenderer font)
/*     */   {
/* 503 */     if (!par1List.isEmpty()) {
/* 504 */       org.lwjgl.opengl.GL11.glDisable(32826);
/* 505 */       net.minecraft.client.renderer.RenderHelper.func_74518_a();
/* 506 */       org.lwjgl.opengl.GL11.glDisable(2896);
/* 507 */       org.lwjgl.opengl.GL11.glDisable(2929);
/* 508 */       int k = 0;
/* 509 */       java.util.Iterator iterator = par1List.iterator();
/*     */       
/* 511 */       while (iterator.hasNext()) {
/* 512 */         String s = (String)iterator.next();
/* 513 */         int l = font.func_78256_a(s);
/*     */         
/* 515 */         if (l > k) {
/* 516 */           k = l;
/*     */         }
/*     */       }
/*     */       
/* 520 */       int i1 = par2 + 12;
/* 521 */       int j1 = par3 - 12;
/* 522 */       int k1 = 8;
/*     */       
/* 524 */       if (par1List.size() > 1) {
/* 525 */         k1 += 2 + (par1List.size() - 1) * 10;
/*     */       }
/*     */       
/* 528 */       if (i1 + k > this.field_146294_l) {
/* 529 */         i1 -= 28 + k;
/*     */       }
/*     */       
/* 532 */       if (j1 + k1 + 6 > this.field_146295_m) {
/* 533 */         j1 = this.field_146295_m - k1 - 6;
/*     */       }
/*     */       
/* 536 */       this.field_73735_i = 300.0F;
/* 537 */       itemRenderer.field_77023_b = 300.0F;
/* 538 */       int l1 = -267386864;
/* 539 */       func_73733_a(i1 - 3, j1 - 4, i1 + k + 3, j1 - 3, -267386864, -267386864);
/* 540 */       func_73733_a(i1 - 3, j1 + k1 + 3, i1 + k + 3, j1 + k1 + 4, -267386864, -267386864);
/* 541 */       func_73733_a(i1 - 3, j1 - 3, i1 + k + 3, j1 + k1 + 3, -267386864, -267386864);
/* 542 */       func_73733_a(i1 - 4, j1 - 3, i1 - 3, j1 + k1 + 3, -267386864, -267386864);
/* 543 */       func_73733_a(i1 + k + 3, j1 - 3, i1 + k + 4, j1 + k1 + 3, -267386864, -267386864);
/* 544 */       int i2 = 1347420415;
/* 545 */       int j2 = 1344798847;
/* 546 */       func_73733_a(i1 - 3, j1 - 3 + 1, i1 - 3 + 1, j1 + k1 + 3 - 1, 1347420415, 1344798847);
/* 547 */       func_73733_a(i1 + k + 2, j1 - 3 + 1, i1 + k + 3, j1 + k1 + 3 - 1, 1347420415, 1344798847);
/* 548 */       func_73733_a(i1 - 3, j1 - 3, i1 + k + 3, j1 - 3 + 1, 1347420415, 1347420415);
/* 549 */       func_73733_a(i1 - 3, j1 + k1 + 2, i1 + k + 3, j1 + k1 + 3, 1344798847, 1344798847);
/*     */       
/* 551 */       for (int k2 = 0; k2 < par1List.size(); k2++) {
/* 552 */         String s1 = (String)par1List.get(k2);
/* 553 */         font.func_78261_a(s1, i1, j1, -1);
/*     */         
/* 555 */         if (k2 == 0) {
/* 556 */           j1 += 2;
/*     */         }
/*     */         
/* 559 */         j1 += 10;
/*     */       }
/*     */       
/* 562 */       this.field_73735_i = 0.0F;
/* 563 */       itemRenderer.field_77023_b = 0.0F;
/* 564 */       org.lwjgl.opengl.GL11.glEnable(2896);
/* 565 */       org.lwjgl.opengl.GL11.glEnable(2929);
/* 566 */       net.minecraft.client.renderer.RenderHelper.func_74519_b();
/* 567 */       org.lwjgl.opengl.GL11.glEnable(32826);
/*     */     }
/*     */   }
/*     */   
/*     */   protected void drawItemStackTooltip(net.minecraft.item.ItemStack par1ItemStack, int par2, int par3, String extraLegend)
/*     */   {
/* 573 */     List list = par1ItemStack.func_82840_a(this.field_146297_k.field_71439_g, this.field_146297_k.field_71474_y.field_82882_x);
/*     */     
/* 575 */     if (extraLegend != null) {
/* 576 */       list.add(extraLegend);
/*     */     }
/*     */     
/* 579 */     for (int k = 0; k < list.size(); k++) {
/* 580 */       if (k == 0) {
/* 581 */         list.set(k, par1ItemStack.func_77953_t().field_77937_e + (String)list.get(k));
/*     */       } else {
/* 583 */         list.set(k, net.minecraft.util.EnumChatFormatting.GRAY + (String)list.get(k));
/*     */       }
/*     */     }
/*     */     
/* 587 */     net.minecraft.client.gui.FontRenderer font = par1ItemStack.func_77973_b().getFontRenderer(par1ItemStack);
/* 588 */     drawHoveringText(list, par2, par3, font == null ? this.field_146289_q : font);
/*     */   }
/*     */   
/*     */ 
/*     */   public void func_73863_a(int i, int j, float f)
/*     */   {
/*     */     try
/*     */     {
/* 596 */       func_146276_q_();
/* 597 */       org.lwjgl.opengl.GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
/* 598 */       this.field_146297_k.field_71446_o.func_110577_a(getPNGPath());
/* 599 */       int xStart = (this.field_146294_l - getXSize()) / 2;
/* 600 */       int yStart = (this.field_146295_m - getYSize()) / 2;
/* 601 */       func_73729_b(xStart, yStart, 0, 0, getXSize(), getYSize());
/*     */       
/* 603 */       org.lwjgl.opengl.GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
/*     */       
/* 605 */       customDrawBackground(i, j, f);
/*     */       
/* 607 */       org.lwjgl.opengl.GL11.glPushMatrix();
/* 608 */       org.lwjgl.opengl.GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
/* 609 */       net.minecraft.client.renderer.RenderHelper.func_74519_b();
/* 610 */       org.lwjgl.opengl.GL11.glPopMatrix();
/* 611 */       org.lwjgl.opengl.GL11.glPushMatrix();
/* 612 */       org.lwjgl.opengl.GL11.glTranslatef(xStart, yStart, 0.0F);
/* 613 */       net.minecraft.client.renderer.RenderHelper.func_74518_a();
/* 614 */       org.lwjgl.opengl.GL11.glDisable(2896);
/* 615 */       org.lwjgl.opengl.GL11.glDisable(2929);
/*     */       
/* 617 */       if (this.descText != null) {
/* 618 */         int vpos = 6;
/*     */         
/* 620 */         if (this.pageNum < this.descText.size())
/*     */         {
/* 622 */           if (this.descText.get(this.pageNum) == null) {
/* 623 */             org.millenaire.common.MLN.printException(new org.millenaire.common.MLN.MillenaireException("descText.get(pageNum)==null for pageNum: " + this.pageNum + " in GUI: " + this));
/*     */           }
/*     */           
/* 626 */           for (int cp = 0; (cp < getPageSize()) && (cp < ((List)this.descText.get(this.pageNum)).size()); cp++)
/*     */           {
/* 628 */             if (((Line)((List)this.descText.get(this.pageNum)).get(cp)).shadow) {
/* 629 */               this.field_146289_q.func_78261_a(((Line)((List)this.descText.get(this.pageNum)).get(cp)).text, getTextXStart() + ((Line)((List)this.descText.get(this.pageNum)).get(cp)).margin, vpos, 1052688);
/*     */             } else {
/* 631 */               this.field_146289_q.func_78276_b(((Line)((List)this.descText.get(this.pageNum)).get(cp)).text, getTextXStart() + ((Line)((List)this.descText.get(this.pageNum)).get(cp)).margin, vpos, 1052688);
/*     */             }
/*     */             
/* 634 */             vpos += 10;
/*     */           }
/*     */         }
/*     */         
/* 638 */         this.field_146289_q.func_78276_b(this.pageNum + 1 + "/" + getNbPage(), getXSize() / 2 - 10, getYSize() - 10, 1052688);
/*     */         
/* 640 */         vpos = 6;
/*     */         
/* 642 */         this.field_73735_i = 100.0F;
/* 643 */         itemRenderer.field_77023_b = 100.0F;
/*     */         
/* 645 */         net.minecraft.item.ItemStack hoverIcon = null;
/* 646 */         String extraLegend = null;
/*     */         
/* 648 */         if (this.pageNum < this.descText.size()) {
/* 649 */           for (int cp = 0; (cp < getPageSize()) && (cp < ((List)this.descText.get(this.pageNum)).size()); cp++) {
/* 650 */             if (((Line)((List)this.descText.get(this.pageNum)).get(cp)).icons != null) {
/* 651 */               for (int ic = 0; ic < ((Line)((List)this.descText.get(this.pageNum)).get(cp)).icons.size(); ic++) {
/* 652 */                 net.minecraft.item.ItemStack icon = (net.minecraft.item.ItemStack)((Line)((List)this.descText.get(this.pageNum)).get(cp)).icons.get(ic);
/*     */                 
/* 654 */                 if (((Line)((List)this.descText.get(this.pageNum)).get(cp)).iconExtraLegends == null) {
/* 655 */                   org.millenaire.common.MLN.error(null, "Null legends!");
/*     */                 }
/*     */                 
/* 658 */                 String legend = (String)((Line)((List)this.descText.get(this.pageNum)).get(cp)).iconExtraLegends.get(ic);
/*     */                 
/* 660 */                 org.lwjgl.opengl.GL11.glEnable(2929);
/* 661 */                 itemRenderer.func_82406_b(this.field_146289_q, this.field_146297_k.field_71446_o, icon, getTextXStart() + 18 * ic, vpos);
/* 662 */                 itemRenderer.func_94148_a(this.field_146289_q, this.field_146297_k.field_71446_o, icon, getTextXStart() + 18 * ic, vpos, null);
/*     */                 
/* 664 */                 if ((xStart + getTextXStart() + 18 * ic < i) && (yStart + vpos < j) && (xStart + getTextXStart() + 18 * ic + 16 > i) && (yStart + vpos + 16 > j)) {
/* 665 */                   hoverIcon = icon;
/* 666 */                   extraLegend = legend;
/*     */                 }
/*     */               }
/*     */             }
/* 670 */             vpos += 10;
/*     */           }
/*     */         }
/*     */         
/* 674 */         if (hoverIcon != null) {
/* 675 */           drawItemStackTooltip(hoverIcon, i - xStart, j - yStart, extraLegend);
/*     */         }
/*     */         
/* 678 */         itemRenderer.field_77023_b = 0.0F;
/* 679 */         this.field_73735_i = 0.0F;
/*     */         
/* 681 */         customDrawScreen(i, j, f);
/*     */       }
/*     */       
/* 684 */       org.lwjgl.opengl.GL11.glPopMatrix();
/* 685 */       super.func_73863_a(i, j, f);
/* 686 */       org.lwjgl.opengl.GL11.glEnable(2896);
/* 687 */       org.lwjgl.opengl.GL11.glEnable(2929);
/*     */       
/* 689 */       org.lwjgl.opengl.GL11.glDisable(2896);
/*     */       
/* 691 */       for (MillGuiTextField textField : this.textFields) {
/* 692 */         textField.func_146194_f();
/*     */       }
/*     */     } catch (Exception e) {
/* 695 */       org.millenaire.common.MLN.printException("Exception in drawScreen of GUI: " + this, e);
/*     */     }
/*     */   }
/*     */   
/*     */   public abstract int getLineSizeInPx();
/*     */   
/*     */   private int getLineWidthInChars(int lineWidthInPx) {
/* 702 */     String testLine = "a";
/*     */     
/* 704 */     while (this.field_146289_q.func_78256_a(testLine) < lineWidthInPx) {
/* 705 */       testLine = testLine + "a";
/*     */     }
/*     */     
/* 708 */     return testLine.length() - 1;
/*     */   }
/*     */   
/*     */   protected int getNbPage() {
/* 712 */     return this.descText.size();
/*     */   }
/*     */   
/*     */   public abstract int getPageSize();
/*     */   
/*     */   public abstract net.minecraft.util.ResourceLocation getPNGPath();
/*     */   
/*     */   public int getTextXStart() {
/* 720 */     return 8;
/*     */   }
/*     */   
/*     */ 
/*     */   public abstract int getXSize();
/*     */   
/*     */ 
/*     */   public abstract int getYSize();
/*     */   
/*     */   protected void handleTextFieldPress(MillGuiTextField textField) {}
/*     */   
/*     */   public void incrementPage()
/*     */   {
/* 733 */     if (this.descText == null) {
/* 734 */       return;
/*     */     }
/*     */     
/* 737 */     if (this.pageNum < getNbPage() - 1) {
/* 738 */       this.pageNum += 1;
/*     */     }
/* 740 */     buttonPagination();
/*     */   }
/*     */   
/*     */   public abstract void initData();
/*     */   
/*     */   public void func_73866_w_()
/*     */   {
/* 747 */     super.func_73866_w_();
/*     */     
/* 749 */     initData();
/*     */     
/* 751 */     buttonPagination();
/*     */   }
/*     */   
/*     */ 
/*     */   protected void func_73869_a(char c, int i)
/*     */   {
/* 757 */     boolean keyTyped = false;
/* 758 */     for (MillGuiTextField textField : this.textFields) {
/* 759 */       if (textField.func_146201_a(c, i)) {
/* 760 */         keyTyped = true;
/* 761 */         handleTextFieldPress(textField);
/*     */       }
/*     */     }
/*     */     
/* 765 */     if ((!keyTyped) && (i == 1)) {
/* 766 */       this.field_146297_k.func_147108_a(null);
/* 767 */       this.field_146297_k.func_71381_h();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   protected void func_73864_a(int i, int j, int k)
/*     */   {
/* 774 */     int xStart = (this.field_146294_l - getXSize()) / 2;
/* 775 */     int yStart = (this.field_146295_m - getYSize()) / 2;
/*     */     
/* 777 */     int ai = i - xStart;
/* 778 */     int aj = j - yStart;
/*     */     
/* 780 */     if ((aj > getYSize() - 14) && (aj < getYSize())) {
/* 781 */       if ((ai > 0) && (ai < 33)) {
/* 782 */         decrementPage();
/* 783 */       } else if ((ai > getXSize() - 33) && (ai < getXSize())) {
/* 784 */         incrementPage();
/*     */       }
/*     */     }
/*     */     
/* 788 */     for (MillGuiTextField textField : this.textFields) {
/* 789 */       textField.func_146192_a(i, j, k);
/*     */     }
/*     */     
/* 792 */     super.func_73864_a(i, j, k);
/*     */   }
/*     */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\client\gui\GuiText.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */