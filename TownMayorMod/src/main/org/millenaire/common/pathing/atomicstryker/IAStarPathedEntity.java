package org.millenaire.common.pathing.atomicstryker;

import java.util.List;

public abstract interface IAStarPathedEntity
{
  public abstract void onFoundPath(List<AStarNode> paramList);
  
  public abstract void onNoPathAvailable();
}


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\pathing\atomicstryker\IAStarPathedEntity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */