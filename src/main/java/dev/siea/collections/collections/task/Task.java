package dev.siea.collections.collections.task;
import java.util.ArrayList;
import java.util.List;

public class Task {
    private final Object target;
    private final List<Integer> level;

    public Task(Object type, int levelAmount, int startingIndex) {
        target = type;
        double levelMultiplier = 2;
        level = generateLevels(startingIndex, levelAmount, levelMultiplier);
    }

    public Task(Object type, int levelAmount, int startingIndex, double levelMultiplier) {
        target = type;
        level = generateLevels(startingIndex, levelAmount, levelMultiplier);
    }

    public Task(Object type, List<Integer> level) {
        target = type;
        this.level = level;
    }

    public Object getTarget() {
        return target;
    }

    public List<Integer> getLevel() {
        return level;
    }

    private List<Integer> generateLevels(int startingIndex, int levelAmount, double levelMultiplier) {
        List<Integer> levels = new ArrayList<>();
        double currentValue = startingIndex;
        for (int i = 0; i < levelAmount; i++) {
            levels.add((int) currentValue);
            currentValue *= levelMultiplier;
        }
        return levels;
    }
}

