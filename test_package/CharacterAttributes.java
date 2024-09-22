import java.util.List;

public class CharacterAttributes {
    public int HP;
    public String name;
    public List<Skill> skills;

    public CharacterAttributes(String name, int HP, List<Skill> skills) {
        this.name = name;
        this.HP = HP;
        this.skills = skills;
    }

    public void castSkill(CharacterAttributes target, Skill skill) {
        target.HP -= skill.damage; // Reduce target's HP by skill damage
    }
    
    public boolean isDefeated() {
        return HP <= 0;
    }
}
