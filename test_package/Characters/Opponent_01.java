package Characters;
import java.util.ArrayList;
import java.util.List;

public class Opponent_01 extends CharacterAttributes {
    public Opponent_01(String name ,int HP ,List<Skill> skills) {
        super(name,HP,skills);
    }

    public static List<Skill> initializeSkills() {
        List<Skill> skills = new ArrayList<>();
        skills.add(new Skill("Normal Attack", "Element One", 0));
        skills.add(new Skill("One with the Shadow", "Element Two", 0));
        skills.add(new Skill("Debt Collector", "Pyro", 270));
        return skills;
    }
}
