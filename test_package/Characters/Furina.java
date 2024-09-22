package Characters;
import java.util.ArrayList;
import java.util.List;

public class Furina extends CharacterAttributes {
    
    public Furina(String name, int HP, List<Skill> skills) {
        super(name, HP, skills);
    }

    public static List<Skill> initializeSkills() {
        List<Skill> skills = new ArrayList<>();
        skills.add(new Skill("Fireball", "Pyro", 200));
        skills.add(new Skill("Ice Blast", "Cryo", 150));
        skills.add(new Skill("Thunder Strike", "Electro", 350));
        return skills;
    }
    
}


