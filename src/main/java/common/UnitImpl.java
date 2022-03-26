package common;

import framework.Player;
import framework.Unit;

import java.util.*;

import static constants.GameConstants.*;

public class UnitImpl implements Unit {

    public static final int STARTING_HEALTH = 100;
    public static final int DEFAULT_STRENGTH = 40;
    public static final int DEFAULT_ABILITY_POWER = 25;
    public static final int DEFAULT_FEROCITY = 1;
    public static final int DEFAULT_REGENERATION = 0;
    public static final int DEFAULT_MOVEMENT_SPEED = 1;

    private static final int FATIGUED_MOVEMENT = 0;
    private static final String DEFAULT_ORIENTATION = "male";

    private final String type;
    private final String race;
    private final String name;

    private Player owner;
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

    private Map<Debuff, Integer> debuffs;
    private int debuffSpecifics;
    private List<Integer> buffSpecifics;
    private Map<Buff, List<Integer>> buffs;

    public UnitImpl(Player owner, String type, String race, String name) {
        this.owner = owner;
        this.type = type;
        this.race = race;
        this.name = name;

        maxMovementSpeed = DEFAULT_MOVEMENT_SPEED;
        currentMovementSpeed = maxMovementSpeed;
        currentHP = STARTING_HEALTH;
        strength = DEFAULT_STRENGTH;
        affinity = NO_AFFINITY;
        affiliation = NO_AFFILIATION;
        maxFerocity = DEFAULT_FEROCITY;
        currentFerocity = maxFerocity;
        orientation = DEFAULT_ORIENTATION;
        regeneration = DEFAULT_REGENERATION;

        if (isCaster(race)) {
            abilityPower = DEFAULT_ABILITY_POWER;
        }

        buffSpecifics = new ArrayList<>();
        buffs = new HashMap<>();

        debuffs = new HashMap<>();
    }

    private boolean isCaster(String race) {
        boolean isCaster = (race.equals( LESHY )
                            || race.equals( CHERNOBOG ));
        return isCaster;
    }

    public String getUnitType() {
        return type;
    }
    public Player getOwner() { return owner; }
    public String getUnitRace() { return race; }
    public String getUnitGender() {
        return orientation;
    }
    public String getAffinity() {
        return affinity;
    }
    public String getAffiliation() { return affiliation; }
    public String getOccupation() { return occupation; }
    public String getName() {return name;}
    public int getCurrentMovementSpeed() {
        return currentMovementSpeed;
    }
    public int getHealth() {
        return currentHP;
    }
    public int getStrength() {
        return strength;
    }
    public int getAbilityPower() {
        return abilityPower;
    }
    public int getRegeneration() { return regeneration; }
    public int getMaxFerocity() {
        return currentFerocity;
    }
    public boolean hasAttacked() { return hasAttacked; }
    public boolean hasMoved() { return hasMoved; }
    public Map<Debuff, Integer> getDebuffs() {
        return debuffs;
    }
    public Map<Buff, List<Integer>> getBuffs() { return buffs; }

    public void fatigueUnit() {
        if (currentMovementSpeed == 1) {
            currentMovementSpeed = FATIGUED_MOVEMENT;
        } else {
            currentMovementSpeed--; }
    }

    public void hastenUnit() {
        currentMovementSpeed++;
        maxMovementSpeed++; }

    public void controlUnit(String debuffType, int turns) {
        switch (debuffType) {
            case ("BAN") -> debuffs.put(Debuff.BAN, turns);
            case ("ROOT") -> debuffs.put(Debuff.ROOT, turns);
            case ("SUPP") -> debuffs.put(Debuff.SUPPRESS, turns);
            default -> System.out.println("Debuff type not found. What are you trying to do to that unit?");
        }
    }

    public void takeDamage(int amount) {
        currentHP = currentHP - amount;

        if (currentHP < 0) {
            currentHP = 0;
        }
    }

    public void regenerateHealth() {
        currentHP = currentHP + regeneration;
    }

    public void secondWind() {
        int extraRegeneration = getBuffs().get(Buff.SECOND_WIND).get(1);
        currentHP = currentHP + extraRegeneration;
    }

    public void tickDebuffs() {
        Collection<Integer> oldValues = debuffs.values();
        List<Integer> newValues = new ArrayList<>();

        for (Integer i : oldValues) {
            i--;
            newValues.add(i);
        }
        for (int i=0; i < debuffs.values().size(); i++) {
            for ( Debuff d : debuffs.keySet() ) {
                debuffs.put(d, newValues.get(i));
            }
        }
    }

    public void tickBuffs() {

        for (Buff buff : Buff.values()) {
            if (getBuffs().get(buff) != null) {
                int oldTurnValue = getBuffs().get(buff).remove(0);
                oldTurnValue--;
                getBuffs().get(buff).add(0, oldTurnValue); }
        }
    }

    public boolean setAffinity(String newAffinity) {
        boolean isMatchingRace = isMatchingRaceForAffinity(newAffinity, getUnitRace());
        if (!isMatchingRace) return false;
        if (hasMoved || hasAttacked) return false;

        switch (newAffinity) {
            case (SORCERER) -> {
                buffUnit("STR", - 25);
                buffUnit("AP", 25); }
            case (BERSERKER) -> buffUnit("FER", 1);
            case (PALADIN) -> {
                buffUnit("STR", 15);
                buffUnit("AP", 15); }
            case (KNIGHT) -> buffUnit("STR", 40);
            case (DRUID) -> buffUnit("MS", 1);
            case (HUNTER) -> buffUnit("STR", 10);
        }

        affinity = newAffinity;
        return true;
    }

    public boolean setAffiliation(String affiliation) {
        this.affiliation = affiliation;
        return true;
    }

    public boolean setOccupation(String newOccupation) {
        occupation = newOccupation;
        return true;
    }

    public void cleanseUnit() {
        for (int i=0; i < debuffs.values().size(); i++) {
            debuffs.replaceAll((d, v) -> 0);
        }
    }

    private boolean isMatchingRaceForAffinity(String newAffinity, String unitRace) {
        switch (newAffinity) {
            case (PALADIN) -> {
                switch (unitRace) {
                    case (DWARF),       // Dwarves can't be paladins!
                         (CHERNOBOG),
                         (DEMON) -> { return false; }
                    default -> { return true; }
                }   }
            default -> { return true; }
        }
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }
    public void setHasAttacked(boolean bool) { hasAttacked = bool; }
    public void setHasMoved(boolean bool) { hasMoved = bool; }
    public void refreshMovementSpeed() {
        currentMovementSpeed = maxMovementSpeed;
    }
    public void refreshFerocity() {
        currentFerocity = maxFerocity;
    }
    public void setMaxFerocity(int maxFerocity) {
        this.currentFerocity = maxFerocity;
    }
    public boolean setGender(String orientation) {
        boolean isMatchingGender = (Arrays.asList(GENDER_LIST).contains( orientation ));
        if (isMatchingGender) {
            this.orientation = orientation;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void buffUnit(String stat, int amount) {
        switch (stat) {
            case ("AP") -> abilityPower = abilityPower + amount;
            case ("FER") -> {
                currentFerocity = maxFerocity + amount;
                maxFerocity = currentFerocity; }
            case ("HP") -> currentHP = currentHP + amount;
            case ("MS") -> {
                currentMovementSpeed = currentMovementSpeed + amount;
                maxMovementSpeed = currentMovementSpeed;
            }
            case ("STR") -> strength = strength + amount;
            case ("RGN") -> regeneration = regeneration + amount;

            default -> System.out.println("Unit stat type not found. Read the manual.");
        }
    }

    @Override
    public void buffUnitTemporarily(String buff, int turns, int amount) {
        buffSpecifics.add(0, turns);
        buffSpecifics.add(1, amount);

        switch (buff) {
            case ("RGN") -> {
                buffs.put(Buff.SECOND_WIND, buffSpecifics); }
            case ("FER") -> {
                buffs.put(Buff.WINDFURY, buffSpecifics);
                buffUnit("FER", amount); }
            case ("RUSH") -> {
                buffs.put(Buff.RUSH, buffSpecifics); }
            default -> {
                System.out.println("Buff type not found. Send an angry letter."); }
        }
    }

    @Override
    public void debuffUnit(String debuff, int turns) {

        switch (debuff) {
            case ("BAN") -> {
                debuffs.put(Debuff.BAN, turns); }
            case ("DRUNK") -> {
                debuffs.put(Debuff.DRUNK, turns);
                buffUnit("MS", -1);
                buffUnit("STR", -20);
                buffUnit("AP", -20);
                setUnitStat("FER", 1);
            }
            case ("FEAR") -> {
                debuffs.put(Debuff.ROOT, turns);
                setUnitStat("STR", getStrength() / 2);
                setUnitStat("AP", getAbilityPower() / 2);
                setUnitStat("FER", DEFAULT_FEROCITY);
                setUnitStat("MS", DEFAULT_MOVEMENT_SPEED);
            }
            case ("LETH") -> {
                debuffs.put(Debuff.LETHARGY, turns);
                setUnitStat("STR", getStrength() / 2);
                setUnitStat("FER", DEFAULT_FEROCITY);
                setUnitStat("MS", FATIGUED_MOVEMENT); }
            case ("ROOT") -> {
                debuffs.put(Debuff.ROOT, turns);
            }
            case ("SUPP") -> {
                debuffs.put(Debuff.SUPPRESS, turns);
            }
            default -> {
                System.out.println("Debuff type not found. Write an anonymous thread on Reddit."); }
        }
    }

    @Override
    public void setUnitStat(String stat, int value) {
        switch (stat) {
            case ("AP") -> abilityPower = value;
            case ("FER") -> {
                maxFerocity = value;
                currentFerocity = value;
            }
            case ("HP") -> currentHP = value;
            case ("MS") -> currentMovementSpeed = value;
            case ("STR") -> strength = value;
            case ("RGN") -> regeneration = value;

            default -> System.out.println("Unit stat type not found. Have you tried restarting?");
        }
    }
}
