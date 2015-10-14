/*     */ package org.millenaire.client.gui;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.gui.GuiButton;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import org.millenaire.client.network.ClientSender;
/*     */ import org.millenaire.common.Culture;
/*     */ import org.millenaire.common.MLN;
/*     */ import org.millenaire.common.MillWorld;
/*     */ import org.millenaire.common.Point;
/*     */ import org.millenaire.common.VillageType;
/*     */ import org.millenaire.common.building.Building;
/*     */ 
/*     */ public class GuiControlledMilitary extends GuiText
/*     */ {
/*     */   private final Building townHall;
/*     */   private final EntityPlayer player;
/*     */   
/*     */   public static class GuiButtonDiplomacy extends GuiText.MillGuiButton
/*     */   {
/*     */     public static final int REL_GOOD = 100;
/*     */     public static final int REL_NEUTRAL = 0;
/*     */     public static final int REL_BAD = -100;
/*     */     public static final int REL = 0;
/*     */     public static final int RAID = 1;
/*     */     public static final int RAIDCANCEL = 2;
/*     */     public Point targetVillage;
/*  31 */     public int data = 0;
/*     */     
/*     */     public GuiButtonDiplomacy(Point targetVillage, int id, int data, String s) {
/*  34 */       super(0, 0, 0, 0, s);
/*  35 */       this.targetVillage = targetVillage;
/*  36 */       this.data = data;
/*     */     }
/*     */   }
/*     */   
/*     */   private class VillageRelation implements Comparable<VillageRelation>
/*     */   {
/*     */     int relation;
/*     */     Point pos;
/*     */     String name;
/*     */     
/*     */     VillageRelation(Point p, int r, String name) {
/*  47 */       this.relation = r;
/*  48 */       this.pos = p;
/*  49 */       this.name = name;
/*     */     }
/*     */     
/*     */     public int compareTo(VillageRelation arg0)
/*     */     {
/*  54 */       return this.name.compareTo(arg0.name);
/*     */     }
/*     */     
/*     */     public boolean equals(Object o)
/*     */     {
/*  59 */       if ((o == null) || (!(o instanceof VillageRelation))) {
/*  60 */         return false;
/*     */       }
/*     */       
/*  63 */       return this.pos.equals(((VillageRelation)o).pos);
/*     */     }
/*     */     
/*     */     public int hashCode()
/*     */     {
/*  68 */       return this.pos.hashCode();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  75 */   ResourceLocation background = new ResourceLocation("millenaire", "textures/gui/ML_panel.png");
/*     */   
/*     */   public GuiControlledMilitary(EntityPlayer player, Building th)
/*     */   {
/*  79 */     this.townHall = th;
/*  80 */     this.player = player;
/*     */   }
/*     */   
/*     */   protected void func_146284_a(GuiButton guibutton)
/*     */   {
/*  85 */     if (!guibutton.field_146124_l) {
/*  86 */       return;
/*     */     }
/*     */     
/*  89 */     GuiButtonDiplomacy gbp = (GuiButtonDiplomacy)guibutton;
/*     */     
/*  91 */     if (gbp.field_146127_k == 0) {
/*  92 */       ClientSender.controlledMilitaryDiplomacy(this.player, this.townHall, gbp.targetVillage, gbp.data);
/*  93 */     } else if (gbp.field_146127_k == 1) {
/*  94 */       ClientSender.controlledMilitaryPlanRaid(this.player, this.townHall, gbp.targetVillage);
/*  95 */     } else if (gbp.field_146127_k == 2) {
/*  96 */       ClientSender.controlledMilitaryCancelRaid(this.player, this.townHall);
/*     */     }
/*     */     
/*  99 */     fillData();
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
/*     */   private void fillData()
/*     */   {
/* 114 */     List<GuiText.Line> text = new ArrayList();
/*     */     
/* 116 */     text.add(new GuiText.Line(this.townHall.getVillageQualifiedName(), false));
/* 117 */     text.add(new GuiText.Line(false));
/* 118 */     text.add(new GuiText.Line(MLN.string("ui.controldiplomacy")));
/* 119 */     text.add(new GuiText.Line());
/*     */     
/* 121 */     ArrayList<VillageRelation> relations = new ArrayList();
/*     */     
/* 123 */     for (Point p : this.townHall.getKnownVillages()) {
/* 124 */       Building b = this.townHall.mw.getBuilding(p);
/* 125 */       if (b != null) {
/* 126 */         relations.add(new VillageRelation(p, this.townHall.getRelationWithVillage(p), b.getVillageQualifiedName()));
/*     */       }
/*     */     }
/*     */     
/* 130 */     Collections.sort(relations);
/*     */     
/* 132 */     for (VillageRelation vr : relations) {
/* 133 */       Building b = this.townHall.mw.getBuilding(vr.pos);
/* 134 */       if (b != null) {
/* 135 */         String col = "";
/*     */         
/* 137 */         if (vr.relation > 70) {
/* 138 */           col = "<darkgreen>";
/* 139 */         } else if (vr.relation > 30) {
/* 140 */           col = "<darkblue>";
/* 141 */         } else if (vr.relation <= -90) {
/* 142 */           col = "<darkred>";
/* 143 */         } else if (vr.relation <= -30) {
/* 144 */           col = "<lightred>";
/*     */         }
/*     */         
/* 147 */         text.add(new GuiText.Line(col + MLN.string("ui.villagerelations", new String[] { b.getVillageQualifiedName(), b.villageType.name, b.culture.getCultureGameName(), MLN.string(org.millenaire.common.core.MillCommonUtilities.getRelationName(vr.relation)) + " (" + vr.relation + ")" }), false));
/*     */         
/*     */ 
/*     */ 
/* 151 */         GuiButtonDiplomacy relGood = new GuiButtonDiplomacy(vr.pos, 0, 100, MLN.string("ui.relgood"));
/* 152 */         GuiButtonDiplomacy relNeutral = new GuiButtonDiplomacy(vr.pos, 0, 0, MLN.string("ui.relneutral"));
/* 153 */         GuiButtonDiplomacy relBad = new GuiButtonDiplomacy(vr.pos, 0, -100, MLN.string("ui.relbad"));
/*     */         
/* 155 */         text.add(new GuiText.Line(relGood, relNeutral, relBad));
/* 156 */         text.add(new GuiText.Line(false));
/*     */         
/* 158 */         if (this.townHall.raidTarget == null) {
/* 159 */           GuiButtonDiplomacy raid = new GuiButtonDiplomacy(vr.pos, 1, -100, MLN.string("ui.raid"));
/* 160 */           text.add(new GuiText.Line(raid));
/* 161 */           text.add(new GuiText.Line(false));
/*     */         }
/* 163 */         else if (this.townHall.raidStart > 0L) {
/* 164 */           if (this.townHall.raidTarget.equals(vr.pos)) {
/* 165 */             text.add(new GuiText.Line("<darkred>" + MLN.string("ui.raidinprogress")));
/*     */           } else {
/* 167 */             text.add(new GuiText.Line("<darkred>" + MLN.string("ui.otherraidinprogress")));
/*     */           }
/*     */         }
/* 170 */         else if (this.townHall.raidTarget.equals(vr.pos)) {
/* 171 */           GuiButtonDiplomacy raid = new GuiButtonDiplomacy(vr.pos, 2, 0, MLN.string("ui.raidcancel"));
/* 172 */           text.add(new GuiText.Line(raid));
/* 173 */           text.add(new GuiText.Line(false));
/* 174 */           text.add(new GuiText.Line("<lightred>" + MLN.string("ui.raidplanned")));
/*     */         }
/*     */         else {
/* 177 */           GuiButtonDiplomacy raid = new GuiButtonDiplomacy(vr.pos, 1, -100, MLN.string("ui.raid"));
/* 178 */           text.add(new GuiText.Line(raid));
/* 179 */           text.add(new GuiText.Line(false));
/* 180 */           text.add(new GuiText.Line("<lightred>" + MLN.string("ui.otherraidplanned")));
/*     */         }
/*     */         
/*     */ 
/* 184 */         text.add(new GuiText.Line());
/*     */       }
/*     */     }
/*     */     
/* 188 */     List<List<GuiText.Line>> pages = new ArrayList();
/* 189 */     pages.add(text);
/*     */     
/* 191 */     List<List<String>> milpages = org.millenaire.common.TileEntityPanel.generateMilitary(this.player, this.townHall);
/*     */     
/* 193 */     for (List<String> textPage : milpages) {
/* 194 */       List<GuiText.Line> page = new ArrayList();
/*     */       
/* 196 */       for (String s : textPage) {
/* 197 */         page.add(new GuiText.Line(s));
/*     */       }
/* 199 */       pages.add(page);
/*     */     }
/*     */     
/* 202 */     this.descText = adjustText(pages);
/*     */     
/* 204 */     buttonPagination();
/*     */   }
/*     */   
/*     */   public int getLineSizeInPx()
/*     */   {
/* 209 */     return 195;
/*     */   }
/*     */   
/*     */   public int getPageSize()
/*     */   {
/* 214 */     return 19;
/*     */   }
/*     */   
/*     */   public ResourceLocation getPNGPath()
/*     */   {
/* 219 */     return this.background;
/*     */   }
/*     */   
/*     */   public int getXSize()
/*     */   {
/* 224 */     return 204;
/*     */   }
/*     */   
/*     */   public int getYSize()
/*     */   {
/* 229 */     return 220;
/*     */   }
/*     */   
/*     */   public void initData()
/*     */   {
/* 234 */     fillData();
/*     */   }
/*     */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\client\gui\GuiControlledMilitary.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */