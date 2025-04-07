package blocksworld;

import java.util.*;

import planning.Heuristic;
import modelling.Variable;

public class BWHeuristicBlocksAtWrongPlace implements Heuristic {

    private Map<Variable, Object> goal;

    public BWHeuristicBlocksAtWrongPlace(Map<Variable, Object> goal) {
        this.goal = goal;
    }

    @Override
    public float estimate(Map<Variable, Object> state) {
        int estimate = 0;
        for (Variable block : goal.keySet()) {
            if (block != null && block.getName().contains("on") && state.get(block) != goal.get(block)) {
                estimate++;
            }
        }
        return estimate;
    }
}