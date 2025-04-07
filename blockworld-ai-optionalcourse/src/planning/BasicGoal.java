package planning;

import java.util.*;
import modelling.Variable;

public class BasicGoal implements Goal {
    private final Map<Variable, Object> goalState;

    public BasicGoal(Map<Variable, Object> goalVariables) {
        this.goalState = goalVariables;
    }


    public boolean isSatisfiedBy(Map<Variable, Object> state) {
        for(Map.Entry<Variable, Object> entry : goalState.entrySet()) {
            if (!state.containsKey(entry.getKey()) || !state.get(entry.getKey()).equals(entry.getValue())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return "Goal : " + goalState.toString();
    }
}
