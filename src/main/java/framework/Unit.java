package framework;

import constants.GameConstants;

import java.util.List;
import java.util.Map;

public interface Unit {

    Player getOwner();
    String getUnitType();
    String getUnitRace();
    String getUnitGender();
    String getAffinity();
    String getAffiliation();
    String getOccupation();
    String getName();

    int getCurrentMovementSpeed();
    int getHealth();
    int getMaxFerocity();
    int getStrength();
    int getAbilityPower();
    int getRegeneration();
    boolean hasAttacked();
    boolean hasMoved();

    Map<GameConstants.Debuff, Integer> getDebuffs();
    Map<GameConstants.Buff, List<Integer>> getBuffs();

    void controlUnit(String debuffType, int turns);

    void fatigueUnit();
    void hastenUnit();
    void buffUnit(String stat, int amount);
    void buffUnitTemporarily(String buff, int turns, int amount);
    void debuffUnit(String debuff, int turns);
    void setUnitStat(String stat, int value);

    void takeDamage(int amount);
    void regenerateHealth();
    void secondWind();

    void tickDebuffs();
    void tickBuffs();

    void setOwner(Player owner);
    void setHasAttacked(boolean bool);
    void setHasMoved(boolean bool);
    boolean setGender(String orientation);
    boolean setAffinity(String aff);
    boolean setAffiliation(String affiliation);
    boolean setOccupation(String newOccupation);

    void cleanseUnit();
}
