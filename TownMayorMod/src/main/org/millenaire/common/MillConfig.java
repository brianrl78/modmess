/*     */ package org.millenaire.common;
/*     */ 
/*     */ import java.lang.reflect.Field;
/*     */ import org.millenaire.common.forge.CommonProxy;
/*     */ import org.millenaire.common.forge.Mill;
/*     */ 
/*     */ public class MillConfig
/*     */ {
/*   9 */   private static final Object[] BOOLEAN_VALS = { Boolean.TRUE, Boolean.FALSE };
/*     */   public static final int LANGUAGE = 1;
/*     */   public static final int EDITABLE_STRING = 2;
/*     */   public static final int KEY = 3;
/*     */   public static final int EDITABLE_INTEGER = 4; public static final int LOG = 5; public static final int BONUS_KEY = 6;
/*  14 */   private static String getBooleanString(boolean b) { if (b) {
/*  15 */       return MLN.string("config.valuetrue");
/*     */     }
/*  17 */     return MLN.string("config.valuefalse");
/*     */   }
/*     */   
/*     */ 
/*     */   final Field field;
/*     */   public final String key;
/*     */   public Object defaultVal;
/*     */   private Object[] possibleVals;
/*  25 */   public int special = 0; public int strLimit = 20;
/*  26 */   public boolean displayConfig = true;
/*     */   
/*  28 */   public boolean displayConfigDev = false;
/*     */   
/*     */   public MillConfig(Field field, String key, int special) {
/*  31 */     this.field = field;
/*  32 */     this.special = special;
/*  33 */     this.key = key.toLowerCase();
/*     */     
/*  35 */     if (special == 5) {
/*  36 */       this.defaultVal = "";
/*     */     }
/*     */   }
/*     */   
/*     */   public MillConfig(Field field, String key, Object... possibleVals)
/*     */   {
/*  42 */     this.field = field;
/*  43 */     this.possibleVals = possibleVals;
/*  44 */     this.key = key.toLowerCase();
/*     */     
/*  46 */     if (isBoolean()) {
/*  47 */       this.possibleVals = BOOLEAN_VALS;
/*  48 */     } else if (possibleVals.length == 0) {
/*  49 */       MLN.error(null, "No possible values specified for non-boolean config: " + field.getName());
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean compareValuesFromString(String newValStr) {
/*  54 */     Object newVal = getValueFromString(newValStr);
/*     */     
/*  56 */     if (newVal == null) {
/*  57 */       return false;
/*     */     }
/*     */     
/*  60 */     return newVal.equals(getValue());
/*     */   }
/*     */   
/*     */   public Object getDefaultValue()
/*     */   {
/*  65 */     if ((this.special == 3) && 
/*  66 */       (this.defaultVal != null)) {
/*  67 */       return Mill.proxy.getKeyString(((Integer)this.defaultVal).intValue());
/*     */     }
/*     */     
/*     */ 
/*  71 */     return this.defaultVal;
/*     */   }
/*     */   
/*     */   public String getDesc()
/*     */   {
/*  76 */     if (this.special == 5) {
/*  77 */       return "";
/*     */     }
/*     */     
/*  80 */     return MLN.string("config." + this.key + ".desc", new String[] { getStringFromValue(this.defaultVal) });
/*     */   }
/*     */   
/*     */   public String getLabel()
/*     */   {
/*  85 */     if (this.special == 5) {
/*  86 */       return this.key;
/*     */     }
/*     */     
/*  89 */     return MLN.string("config." + this.key + ".label");
/*     */   }
/*     */   
/*     */   public Object[] getPossibleVals()
/*     */   {
/*  94 */     if (this.special == 1)
/*  95 */       return new Object[] { MLN.loadedLanguages.get("fr"), MLN.loadedLanguages.get("en") };
/*  96 */     if (this.special == 5) {
/*  97 */       return new Object[] { Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3) };
/*     */     }
/*     */     
/* 100 */     return this.possibleVals;
/*     */   }
/*     */   
/*     */   public String getSaveValue() {
/*     */     try {
/* 105 */       return getSaveValue(this.field.get(null));
/*     */     } catch (Exception e) {
/* 107 */       MLN.printException(this + ": Exception when getting the field.", e);
/*     */     }
/* 109 */     return null;
/*     */   }
/*     */   
/*     */   public String getSaveValue(Object o)
/*     */   {
/* 114 */     if ((this.special == 3) && (o != null))
/* 115 */       return Mill.proxy.getKeyString(((Integer)o).intValue());
/* 116 */     if (this.special == 5) {
/* 117 */       return MLN.getLogLevel(((Integer)o).intValue());
/*     */     }
/*     */     
/* 120 */     if (o == null) {
/* 121 */       return "";
/*     */     }
/* 123 */     return o.toString();
/*     */   }
/*     */   
/*     */ 
/*     */   public String getStringFromValue(Object o)
/*     */   {
/* 129 */     if ((this.special == 3) && (o != null))
/* 130 */       return Mill.proxy.getKeyString(((Integer)o).intValue());
/* 131 */     if (this.special == 5)
/* 132 */       return MLN.getLogLevel(((Integer)o).intValue());
/* 133 */     if ((isBoolean()) && (o != null)) {
/* 134 */       return getBooleanString(((Boolean)o).booleanValue());
/*     */     }
/*     */     
/* 137 */     if (o == null) {
/* 138 */       return "";
/*     */     }
/* 140 */     return o.toString();
/*     */   }
/*     */   
/*     */   public String getStringValue()
/*     */   {
/*     */     try {
/* 146 */       return getStringFromValue(this.field.get(null));
/*     */     } catch (Exception e) {
/* 148 */       MLN.printException(this + ": Exception when getting the field.", e);
/*     */     }
/* 150 */     return null;
/*     */   }
/*     */   
/*     */   public Object getValue() {
/*     */     try {
/* 155 */       return this.field.get(null);
/*     */     } catch (Exception e) {
/* 157 */       MLN.printException(this + ": Exception when getting the field.", e);
/*     */     }
/* 159 */     return null;
/*     */   }
/*     */   
/*     */   public Object getValueFromString(String val)
/*     */   {
/* 164 */     if (this.special == 1)
/* 165 */       return MLN.loadedLanguages.get(val);
/* 166 */     if (this.special == 3)
/* 167 */       return Integer.valueOf(Mill.proxy.loadKeySetting(val));
/* 168 */     if (this.special == 5) {
/* 169 */       return Integer.valueOf(MLN.readLogLevel(val));
/*     */     }
/*     */     
/* 172 */     if (isString())
/* 173 */       return val;
/* 174 */     if (isInteger())
/* 175 */       return Integer.valueOf(Integer.parseInt(val));
/* 176 */     if (isBoolean()) {
/* 177 */       return Boolean.valueOf(Boolean.parseBoolean(val));
/*     */     }
/* 179 */     return null;
/*     */   }
/*     */   
/*     */   public boolean hasDefaultValue()
/*     */   {
/* 184 */     if (this.defaultVal == null) {
/* 185 */       return false;
/*     */     }
/*     */     
/* 188 */     if ((this.special == 5) && 
/* 189 */       (((Integer)getValue()).intValue() == 0)) {
/* 190 */       return true;
/*     */     }
/*     */     
/*     */ 
/* 194 */     return this.defaultVal.equals(getValue());
/*     */   }
/*     */   
/*     */   public boolean hasTextField()
/*     */   {
/* 199 */     return (this.special == 2) || (this.special == 3) || (this.special == 4) || (this.special == 6);
/*     */   }
/*     */   
/*     */   public boolean isBoolean() {
/* 203 */     return (this.field.getType().equals(Boolean.class)) || (this.field.getType().equals(Boolean.TYPE));
/*     */   }
/*     */   
/*     */   public boolean isInteger() {
/* 207 */     return (this.field.getType().equals(Integer.class)) || (this.field.getType().equals(Integer.TYPE));
/*     */   }
/*     */   
/*     */   public boolean isString() {
/* 211 */     return this.field.getType().equals(String.class);
/*     */   }
/*     */   
/*     */   public void setDefaultValue(Object defaultVal) {
/* 215 */     this.defaultVal = defaultVal;
/*     */   }
/*     */   
/*     */   public MillConfig setDisplay(boolean display) {
/* 219 */     this.displayConfig = display;
/* 220 */     return this;
/*     */   }
/*     */   
/*     */   public MillConfig setDisplayDev(boolean display) {
/* 224 */     this.displayConfigDev = display;
/* 225 */     return this;
/*     */   }
/*     */   
/*     */   public MillConfig setMaxStringLength(int len) {
/* 229 */     this.strLimit = len;
/* 230 */     return this;
/*     */   }
/*     */   
/*     */   public void setValue(Object val)
/*     */   {
/* 235 */     if ((this.special == 5) && ((val instanceof String))) {
/* 236 */       val = Integer.valueOf(MLN.readLogLevel((String)val));
/*     */     }
/*     */     
/* 239 */     if ((this.special == 3) && (val.equals(Integer.valueOf(0)))) {
/* 240 */       return;
/*     */     }
/*     */     try
/*     */     {
/* 244 */       this.field.set(null, val);
/*     */     } catch (Exception e) {
/* 246 */       MLN.printException(this + ": Exception when setting the field.", e);
/*     */     }
/*     */     
/* 249 */     if ((this.special == 6) && (Mill.proxy.getTheSinglePlayer() != null))
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 256 */       MLN.checkBonusCode(true);
/*     */     }
/*     */   }
/*     */   
/*     */   public void setValueFromString(String val, boolean setDefault)
/*     */   {
/* 262 */     setValue(getValueFromString(val));
/* 263 */     if (setDefault) {
/* 264 */       setDefaultValue(getValueFromString(val));
/*     */     }
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 270 */     return "MillConfig:" + this.key;
/*     */   }
/*     */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\MillConfig.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */