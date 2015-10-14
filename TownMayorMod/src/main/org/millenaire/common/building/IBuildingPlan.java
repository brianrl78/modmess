package org.millenaire.common.building;

import java.util.List;
import org.millenaire.common.Culture;

public abstract interface IBuildingPlan
{
  public abstract Culture getCulture();
  
  public abstract List<String> getFemaleResident();
  
  public abstract String getGameName();
  
  public abstract List<String> getMaleResident();
  
  public abstract String getNativeName();
}


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\building\IBuildingPlan.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */