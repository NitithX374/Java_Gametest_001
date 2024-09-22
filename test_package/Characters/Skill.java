package Characters;
public class Skill {
    protected String name;
    protected String type;
    protected int damage;

    public Skill(String name, String type, int damage) {
        this.name = name;
        this.type = type;
        this.damage = damage;
    }

    // Getter methods for the fields
    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public int getDamage() {
        return damage;
    }
}
