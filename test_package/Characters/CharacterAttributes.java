package Characters;
import java.util.List;

public class CharacterAttributes {
    String name;
    public int HP;
    public List<Skill> skills;
    public int ElementId;
    public String status;
    public int passiveID;
    public static boolean showStats = false;
    public CharacterAttributes(String name, int HP, List<Skill> skills) {
        this.name = name;
        this.HP = HP;
        this.skills = skills;
    }

    public boolean isDefeated() {
        return HP <= 0;
    }

    public void castSkill(CharacterAttributes target, Skill skill) {
        // Logic for casting a skill
        target.HP -= skill.getDamage();; // Simplified damage calculation
    }

    // Change this method to return the list of skills
    public List<Skill> getSkills() {
        return skills;
    }
    public int getElement() {
        return this.ElementId; // Adjust based on your actual attribute
    }
    
    public String getStatus() {
        return this.status; // Adjust based on your actual attribute
    }
    
    public int getPassive() {
        return this.passiveID; // Adjust based on your actual attribute
    }
    
    // Add similar getters for strength, dexterity, intelligence, luck, and agility
    
}
