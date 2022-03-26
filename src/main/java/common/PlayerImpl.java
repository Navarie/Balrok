package common;

import framework.Player;

public class PlayerImpl implements Player {

    public static final int MAXIMUM_HEALTH = 150;
    public static final int STARTING_HEALTH = 100;
    public static final int STARTING_MANA = 200;
    public static final int DEFAULT_REGENERATION = 5;
    public static final int DEFAULT_BLOODLINE = 8;

    private final String playerColor;
    private int currentHealth;
    private int currentMana;
    private int regeneration;

    public PlayerImpl(String color) {
        regeneration = DEFAULT_REGENERATION;
        playerColor = color;
    }

    public int getCurrentHealth() {
        return currentHealth;
    }
    public int getCurrentMana() { return currentMana; }
    public int getRegeneration() {
        return regeneration;
    }

    public void setHealth(int newValue) {
        currentHealth = newValue;
    }

    @Override
    public void setMana(int newValue) {
        currentMana = newValue;
    }

    public void spendMana(int amount) {
        currentMana = currentMana - amount;
    }
    public void takeDamage(int amount) {
        currentHealth = currentHealth - amount;
    }

    @Override
    public void restoreHealth() {
        currentHealth = Math.min( (currentHealth + regeneration), MAXIMUM_HEALTH );
    }

    @Override
    public String toString() {
        return playerColor;
    }
}
