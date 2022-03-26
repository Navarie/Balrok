package stub;

import constants.GameConstants;
import framework.Player;
import framework.Unit;

import java.util.List;
import java.util.Map;

public class UnitStub implements Unit {

    private Player owner;
    private final String type;
    private final String race;
    private final String name;
    private String orientation;
    private String affinity;
    private String affiliation;
    private String occupation;
    private int currentMovementSpeed;
    private int maxMovementSpeed;
    private int currentHP;
    private int strength;
    private int abilityPower;
    private int maxFerocity;
    private int currentFerocity;
    private int regeneration;
    private boolean hasMoved;
    private boolean hasAttacked;

    public UnitStub(Player owner, String type, String race, String name) {
        this.owner = owner;
        this.type = type;
        this.race = race;
        this.name = name;

        currentMovementSpeed = 42;
    }

    public Player getOwner() { return owner; }
    public String getUnitType() { return type; }
    public String getUnitRace() { return race; }
    public String getUnitGender() { return null; }
    public String getAffinity() { return null; }
    public String getAffiliation() { return null; }
    public String getOccupation() { return null; }
    public String getName() { return name; }
    public int getCurrentMovementSpeed() { return currentMovementSpeed; }
    public int getHealth() { return 0; }
    public int getMaxFerocity() { return 0; }
    public int getStrength() { return 0; }
    public int getAbilityPower() { return 0; }
    public int getRegeneration() { return 0; }
    public boolean hasAttacked() { return false; }
    public boolean hasMoved() { return false; }
    public Map<GameConstants.Debuff, Integer> getDebuffs() { return null; }
    public Map<GameConstants.Buff, List<Integer>> getBuffs() { return null; }
    public void controlUnit(String debuffType, int turns) {}
    public void fatigueUnit() {}
    public void hastenUnit() {}
    public void buffUnit(String stat, int amount) {}
    public void buffUnitTemporarily(String buff, int turns, int amount) {}
    public void debuffUnit(String debuff, int turns) {}
    public void setUnitStat(String stat, int value) {}
    public void takeDamage(int amount) {}
    public void regenerateHealth() {}
    public void secondWind() {}
    public void tickDebuffs() {}
    public void tickBuffs() {}
    public void setOwner(Player owner) {}
    public void setHasAttacked(boolean bool) {}
    public void setHasMoved(boolean bool) {}
    public boolean setGender(String orientation) { return false; }
    public boolean setAffinity(String aff) { return false; }
    public boolean setAffiliation(String affiliation) { return false; }
    public boolean setOccupation(String newOccupation) { return false; }
    public void cleanseUnit() {}
}
