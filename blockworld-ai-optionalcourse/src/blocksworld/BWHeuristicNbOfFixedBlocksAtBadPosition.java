package blocksworld;

import java.util.*;
import planning.Heuristic;
import modelling.Variable;

public class BWHeuristicNbOfFixedBlocksAtBadPosition implements Heuristic {
    
    Map<Variable, Object> goal;

    public BWHeuristicNbOfFixedBlocksAtBadPosition(Map<Variable, Object> goal) {
        this.goal = goal;
    }

    @Override
    public float estimate(Map<Variable, Object> state) {
        int estimate = 0;
        for (Variable block : state.keySet()) {
            if(block != null && block.getName().contains("on") && state.get(block) != goal.get(block)){

                int index = Integer.parseInt(block.getName().substring(3));
                Variable fixed = new Variable("fixed_" + index, Set.of(true, false));

                if(state.get(fixed).equals(true)){
                    estimate++;
                }
            }


        }
        return estimate;
    }
}



