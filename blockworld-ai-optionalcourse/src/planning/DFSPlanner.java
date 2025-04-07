package planning;

import java.util.*;

import modelling.Variable;

public class DFSPlanner extends AbstractPlanner {
    private final Map<Variable, Object> initialState;
    private final Set<Action> actions;
    private final Goal goal;

    public DFSPlanner(Map<Variable, Object> initialState, Set<Action> actions, Goal goal) {
        this.initialState = initialState;
        this.actions = actions;
        this.goal = goal;
    }

    @Override
    public List<Action> plan() {
        return dfs(initialState, new Stack<>(), new HashSet<>());
    }
    @Override
    public Map<Variable, Object> getInitialState() {
        return initialState;
    }

    @Override
    public Set<Action> getActions() {
        return actions;
    }

    @Override
    public Goal getGoal() {
        return goal;
    }





    private List<Action> dfs(Map<Variable, Object> initialState, Stack<Action> plan, Set<Map<Variable, Object>> closed) {
        incrementNodeCount();
        if(goal.isSatisfiedBy(initialState)) {
            return new ArrayList<>(plan);
        }
        closed.add(initialState);
        for (Action action : actions) {
            //incrementNodeCount();
            if(action.isApplicable(initialState)) {
                Map<Variable, Object> newState = action.successor(initialState);
                if(!closed.contains(newState)) {
                    plan.push(action);
                    List<Action> result = dfs(newState,plan, closed); 
                    if(result != null) {
                        return result;
                    }
                    plan.pop();
                }
            }
        }
        return null;
    }
}
