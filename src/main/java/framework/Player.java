package framework;

public interface Player {

    int getCurrentHealth();
    int getCurrentMana();
    int getRegeneration();

    void setHealth(int newValue);
    void setMana(int newValue);
    void spendMana(int amount);
    void takeDamage(int amount);
    void restoreHealth();
}
