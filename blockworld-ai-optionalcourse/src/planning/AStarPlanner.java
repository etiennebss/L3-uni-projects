package planning;

import java.util.*;

import modelling.Variable;

public class AStarPlanner extends AbstractPlanner {
    private final Map<Variable, Object> initialState;
    private final Set<Action> actions;
    private final Goal goal;
    private final Heuristic heuristic;

    // Constructeur
    public AStarPlanner(Map<Variable, Object> initialState, Set<Action> actions, Goal goal, Heuristic heuristic) {
        this.initialState = initialState;
        this.actions = actions;
        this.goal = goal;
        this.heuristic = heuristic;
    }

    @Override
    public List<Action> plan() {
        // MAP pour garder les pères des états
        Map<Map<Variable, Object>, Map<Variable, Object>> father = new HashMap<>();
        // MAP pour garder les actions associées aux états
        Map<Map<Variable, Object>, Action> plan = new HashMap<>();
        // MAP pour garder les distances des états
        Map<Map<Variable, Object>, Double> gScore = new HashMap<>();
        // MAP pour garder les coûts estimés (fScore = gScore + heuristique)
        Map<Map<Variable, Object>, Double> fScore = new HashMap<>();
        // PriorityQueue pour explorer les états selon leur score f
        PriorityQueue<Map<Variable, Object>> open = new PriorityQueue<>(
                Comparator.comparingDouble(fScore::get));

        // Initialiser avec l'état initial
        father.put(initialState, null);
        gScore.put(initialState, 0.0);
        fScore.put(initialState, (double) heuristic.estimate(initialState));
        open.add(initialState);

        while (!open.isEmpty()) {
            incrementNodeCount();
            Map<Variable, Object> current = open.poll();

            // Si l'état courant satisfait le but
            if (goal.isSatisfiedBy(current)) {
                return getAStarPlan(father, plan, current);
            }

            // Explorer les actions applicables
            for (Action action : actions) {
                if (action.isApplicable(current)) {
                    Map<Variable, Object> nextState = action.successor(current);
                    double tentativeGScore = gScore.get(current) + action.getCost();

                    // Si un meilleur chemin est trouvé
                    if (tentativeGScore < gScore.getOrDefault(nextState, Double.POSITIVE_INFINITY)) {
                        father.put(nextState, current);
                        plan.put(nextState, action);
                        gScore.put(nextState, tentativeGScore);
                        double estimatedFScore = tentativeGScore + heuristic.estimate(nextState);
                        fScore.put(nextState, estimatedFScore);

                        if (!open.contains(nextState)) {
                            open.add(nextState);
                        }
                    }
                }
            }
        }

        // Si aucun plan n'est trouvé, retourner null
        return null;
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

    // Méthode auxiliaire pour reconstruire le plan à partir des états pères
    private List<Action> getAStarPlan(Map<Map<Variable, Object>, Map<Variable, Object>> father, 
                                      Map<Map<Variable, Object>, Action> plan, 
                                      Map<Variable, Object> goalState) {
        List<Action> actions = new ArrayList<>();
        Map<Variable, Object> currentState = goalState;

        while (father.get(currentState) != null) {
            Action action = plan.get(currentState);
            actions.add(action);
            currentState = father.get(currentState);
        }

        // Inverser la liste des actions pour avoir l'ordre correct
        Collections.reverse(actions);
        return actions;
    }
}
