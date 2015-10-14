/*     */ package org.millenaire.client.gui;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.GuiButton;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import org.lwjgl.input.Keyboard;
/*     */ import org.millenaire.common.MLN;
/*     */ import org.millenaire.common.MillConfig;
/*     */ 
/*     */ public class GuiConfig extends GuiText
/*     */ {
/*     */   public static class ConfigButton extends GuiText.MillGuiButton
/*     */   {
/*     */     public MillConfig config;
/*     */     
/*     */     public ConfigButton(MillConfig config)
/*     */     {
/*  21 */       super(0, 0, 0, 0, config.getLabel());
/*  22 */       this.config = config;
/*     */       
/*  24 */       refreshLabel();
/*     */     }
/*     */     
/*     */     public void refreshLabel() {
/*  28 */       this.field_146126_j = (this.config.getLabel() + ": " + this.config.getStringValue());
/*     */     }
/*     */   }
/*     */   
/*     */   public static class ConfigPageButton extends GuiText.MillGuiButton {
/*     */     public int pageId;
/*     */     
/*     */     public ConfigPageButton(int pageId) {
/*  36 */       super(0, 0, 0, 0, MLN.string((String)MLN.configPageTitles.get(pageId)));
/*  37 */       this.pageId = pageId;
/*     */     }
/*     */   }
/*     */   
/*  41 */   int pageId = -1;
/*     */   
/*  43 */   ResourceLocation background = new ResourceLocation("millenaire", "textures/gui/ML_config.png");
/*     */   
/*     */   protected void func_146284_a(GuiButton guibutton)
/*     */   {
/*  47 */     if ((guibutton instanceof ConfigButton)) {
/*  48 */       ConfigButton configButton = (ConfigButton)guibutton;
/*     */       
/*  50 */       int valPos = -1;
/*     */       
/*  52 */       for (int i = 0; i < configButton.config.getPossibleVals().length; i++) {
/*  53 */         Object o = configButton.config.getPossibleVals()[i];
/*  54 */         if (o.equals(configButton.config.getValue())) {
/*  55 */           valPos = i;
/*     */         }
/*     */       }
/*     */       
/*  59 */       valPos++;
/*     */       
/*  61 */       if (valPos >= configButton.config.getPossibleVals().length) {
/*  62 */         valPos = 0;
/*     */       }
/*     */       
/*  65 */       configButton.config.setValue(configButton.config.getPossibleVals()[valPos]);
/*     */       
/*  67 */       configButton.refreshLabel();
/*     */       
/*  69 */       MLN.writeConfigFile();
/*  70 */     } else if ((guibutton instanceof ConfigPageButton)) {
/*  71 */       ConfigPageButton configPageButton = (ConfigPageButton)guibutton;
/*     */       
/*  73 */       this.pageId = configPageButton.pageId;
/*     */       
/*  75 */       this.pageNum = 0;
/*     */       
/*  77 */       this.descText = getData();
/*  78 */       buttonPagination();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void customDrawBackground(int i, int j, float f) {}
/*     */   
/*     */ 
/*     */ 
/*     */   protected void customDrawScreen(int i, int j, float f) {}
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean func_73868_f()
/*     */   {
/*  94 */     return true;
/*     */   }
/*     */   
/*     */   private List<List<GuiText.Line>> getData() {
/*  98 */     if (this.pageId == -1) {
/*  99 */       return getHomepageData();
/*     */     }
/* 101 */     return getPageData();
/*     */   }
/*     */   
/*     */ 
/*     */   private List<List<GuiText.Line>> getHomepageData()
/*     */   {
/* 107 */     List<List<GuiText.Line>> pages = new ArrayList();
/*     */     
/* 109 */     List<GuiText.Line> text = new ArrayList();
/*     */     
/* 111 */     text.add(new GuiText.Line("<darkblue>" + MLN.string("config.pagetitle"), false));
/* 112 */     text.add(new GuiText.Line("", false));
/*     */     
/* 114 */     for (int i = 0; i < MLN.configPages.size(); i++) {
/* 115 */       text.add(new GuiText.Line(new ConfigPageButton(i)));
/* 116 */       text.add(new GuiText.Line(false));
/* 117 */       text.add(new GuiText.Line());
/*     */     }
/*     */     
/* 120 */     pages.add(text);
/*     */     
/* 122 */     return adjustText(pages);
/*     */   }
/*     */   
/*     */   public int getLineSizeInPx()
/*     */   {
/* 127 */     return 247;
/*     */   }
/*     */   
/*     */   private List<List<GuiText.Line>> getPageData() {
/* 131 */     List<List<GuiText.Line>> pages = new ArrayList();
/*     */     
/* 133 */     List<GuiText.Line> text = new ArrayList();
/*     */     
/* 135 */     text.add(new GuiText.Line("<darkblue>" + MLN.string((String)MLN.configPageTitles.get(this.pageId)), false));
/* 136 */     text.add(new GuiText.Line());
/*     */     
/* 138 */     if (MLN.configPageDesc.get(this.pageId) != null) {
/* 139 */       text.add(new GuiText.Line(MLN.string((String)MLN.configPageDesc.get(this.pageId)), false));
/* 140 */       text.add(new GuiText.Line());
/*     */     }
/*     */     
/* 143 */     for (int j = 0; j < ((List)MLN.configPages.get(this.pageId)).size(); j++)
/*     */     {
/* 145 */       MillConfig config = (MillConfig)((List)MLN.configPages.get(this.pageId)).get(j);
/*     */       
/* 147 */       if ((config.displayConfig) || ((config.displayConfigDev) && (MLN.DEV)))
/*     */       {
/* 149 */         if (config.getDesc().length() > 0) {
/* 150 */           text.add(new GuiText.Line(config.getDesc(), false));
/*     */         }
/*     */         
/* 153 */         if (config.hasTextField()) {
/* 154 */           GuiText.MillGuiTextField textField = new GuiText.MillGuiTextField(this.field_146289_q, 0, 0, 0, 0, config.key);
/* 155 */           textField.func_146180_a(config.getStringValue());
/* 156 */           textField.func_146203_f(config.strLimit);
/* 157 */           textField.func_146193_g(-1);
/* 158 */           text.add(new GuiText.Line(config.getLabel() + ":", textField));
/* 159 */           text.add(new GuiText.Line(false));
/* 160 */           text.add(new GuiText.Line());
/*     */         } else {
/* 162 */           text.add(new GuiText.Line(new ConfigButton(config)));
/* 163 */           text.add(new GuiText.Line(false));
/* 164 */           text.add(new GuiText.Line());
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 169 */     pages.add(text);
/*     */     
/* 171 */     return adjustText(pages);
/*     */   }
/*     */   
/*     */   public int getPageSize()
/*     */   {
/* 176 */     return 19;
/*     */   }
/*     */   
/*     */   public ResourceLocation getPNGPath()
/*     */   {
/* 181 */     return this.background;
/*     */   }
/*     */   
/*     */   public int getXSize()
/*     */   {
/* 186 */     return 256;
/*     */   }
/*     */   
/*     */   public int getYSize()
/*     */   {
/* 191 */     return 220;
/*     */   }
/*     */   
/*     */   protected void handleTextFieldPress(GuiText.MillGuiTextField textField)
/*     */   {
/* 196 */     if (MLN.configs.containsKey(textField.fieldKey)) {
/* 197 */       MillConfig config = (MillConfig)MLN.configs.get(textField.fieldKey);
/* 198 */       config.setValueFromString(textField.func_146179_b(), false);
/* 199 */       MLN.writeConfigFile();
/*     */     }
/*     */   }
/*     */   
/*     */   public void initData()
/*     */   {
/* 205 */     this.descText = getData();
/*     */   }
/*     */   
/*     */ 
/*     */   protected void func_73869_a(char c, int i)
/*     */   {
/* 211 */     if (i == 1) {
/* 212 */       if (this.pageId == -1) {
/* 213 */         this.field_146297_k.func_147108_a(null);
/* 214 */         this.field_146297_k.func_71381_h();
/*     */       } else {
/* 216 */         this.pageId = -1;
/* 217 */         this.pageNum = 0;
/*     */         
/* 219 */         this.descText = getData();
/* 220 */         buttonPagination();
/*     */       }
/*     */     } else {
/* 223 */       super.func_73869_a(c, i);
/*     */     }
/*     */   }
/*     */   
/*     */   public void func_146281_b()
/*     */   {
/* 229 */     Keyboard.enableRepeatEvents(false);
/*     */   }
/*     */   
/*     */   public void func_73876_c() {}
/*     */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\client\gui\GuiConfig.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */