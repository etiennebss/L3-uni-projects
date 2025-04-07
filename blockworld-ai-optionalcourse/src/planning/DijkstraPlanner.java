package planning;

import java.util.*;
import modelling.Variable;

public class DijkstraPlanner extends AbstractPlanner {
    private final Map<Variable, Object> initialState;
    private final Set<Action> actions;
    private final Goal goal;

    // Constructeur
    public DijkstraPlanner(Map<Variable, Object> initialState, Set<Action> actions, Goal goal) {
        this.initialState = initialState;
        this.actions = actions;
        this.goal = goal;
    }

    @Override
    public List<Action> plan() {
        // MAP pour garder les pères des états
        Map<Map<Variable, Object>, Map<Variable, Object>> father = new HashMap<>();
        // MAP pour garder les actions associées aux états
        Map<Map<Variable, Object>, Action> plan = new HashMap<>();
        // MAP pour garder les distances des états
        Map<Map<Variable, Object>, Double> distance = new HashMap<>();
        // SET pour les états à explorer (ouverts)
        Set<Map<Variable, Object>> open = new HashSet<>();
        // Liste des états atteignant les buts
        List<Map<Variable, Object>> goals = new ArrayList<>();

        // Initialiser avec l'état initial
        father.put(initialState, null);
        distance.put(initialState, 0.0);
        open.add(initialState);

        // Boucle principale
        while (!open.isEmpty()) {
            // Trouver l'état avec la plus petite distance
            Map<Variable, Object> current = getMinDistanceState(open, distance);
            incrementNodeCount();
            open.remove(current);

            // Si l'état courant satisfait le but
            if (goal.isSatisfiedBy(current)) {
                goals.add(current);
            }

            // Explorer les actions applicables
            for (Action action : actions) {
                if (action.isApplicable(current)) {
                    Map<Variable, Object> nextState = action.successor(current);

                    // Initialiser la distance de nextState s'il n'a pas été rencontré
                    // putIfAbsent écrit une entrée dans la liste ou map
                    // Si l'entrée existe déjà et que ça valeur n'est pas null, on ne change rien
                    distance.putIfAbsent(nextState, Double.POSITIVE_INFINITY);

                    // Si une meilleure distance est trouvée
                    double newDistance = distance.get(current) + action.getCost();
                    if (newDistance < distance.get(nextState)) {
                        distance.put(nextState, newDistance);
                        father.put(nextState, current);
                        plan.put(nextState, action);
                        open.add(nextState);
                    }
                }
            }
        }

        // Si aucun état but n'est trouvé, retourner null
        if (goals.isEmpty()) {
            return null;
        }

        // Retourner le plan optimal en appelant la méthode auxiliaire
        return getDijkstraPlan(father, plan, goals, distance);
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

    // Méthode auxiliaire pour trouver l'état avec la plus petite distance
    private Map<Variable, Object> getMinDistanceState(Set<Map<Variable, Object>> open, 
                                                      Map<Map<Variable, Object>, Double> distance) {
        Map<Variable, Object> minState = null;
        double minDistance = Double.POSITIVE_INFINITY;

        for (Map<Variable, Object> state : open) {
            double dist = distance.getOrDefault(state, Double.POSITIVE_INFINITY);
            if (dist < minDistance) {
                minDistance = dist;
                minState = state;
            }
        }

        return minState;
    }

    // Méthode auxiliaire pour reconstruire le plan à partir des états buts
    private List<Action> getDijkstraPlan(Map<Map<Variable, Object>, Map<Variable, Object>> father, 
                                         Map<Map<Variable, Object>, Action> plan, 
                                         List<Map<Variable, Object>> goals, 
                                         Map<Map<Variable, Object>, Double> distance) {
        // Trouver le but avec la distance la plus courte
        Map<Variable, Object> goalState = null;
        double minDistance = Double.POSITIVE_INFINITY;

        for (Map<Variable, Object> goal : goals) {
            double dist = distance.get(goal);
            if (dist < minDistance) {
                minDistance = dist;
                goalState = goal;
            }
        }

        // Reconstituer le plan en remontant depuis le but
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
