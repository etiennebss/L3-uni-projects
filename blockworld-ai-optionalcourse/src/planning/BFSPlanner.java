package planning;

import java.util.*;
import modelling.Variable;

public class BFSPlanner extends AbstractPlanner {
    private final Map<Variable, Object> initialState;
    private final Set<Action> actions;
    private final Goal goal;

    public BFSPlanner(Map<Variable, Object> initialState, Set<Action> actions, Goal goal) {
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
        // SET pour les états visités (fermés)
        Set<Map<Variable, Object>> closed = new HashSet<>();
        // QUEUE pour les états à explorer (ouverts)
        Queue<Map<Variable, Object>> open = new LinkedList<>();

        // On initialise la recherche avec l'état initial
        closed.add(initialState);
        open.add(initialState);
        father.put(initialState, null);

        // Si l'état initial satisfait le but
        if (goal.isSatisfiedBy(initialState)) {
            return new ArrayList<>();
        }

        // Boucle principale de la recherche en largeur
        while (!open.isEmpty()) {
            
            Map<Variable, Object> current = open.poll();  // Déqueue l'état courant
            incrementNodeCount();
            closed.add(current);

            // Pour chaque action disponible
            for (Action action : actions) {
                // On vérifie si l'action est applicable dans l'état courant
                if (action.isApplicable(current)) {
                    // On applique l'action pour obtenir le nouvel état
                    Map<Variable, Object> nextState = action.successor(current);

                    // On vérifie que le nouvel état n'est pas déjà dans fermé ou ouvert
                    if (!closed.contains(nextState) && !open.contains(nextState)) {
                        // On enregistre le père de nextState
                        father.put(nextState, current);
                        // On enregistre l'action associée
                        plan.put(nextState, action);

                        // Si le nouvel état satisfait le but
                        if (goal.isSatisfiedBy(nextState)) {
                            // On calcule et retourner le plan en utilisant la méthode auxiliaire
                            return getBFSPlan(father, plan, nextState);
                        } else {
                            // On ajoute le nouvel état à la queue (open)
                            open.add(nextState);
                        }
                    }
                }
            }
        }

        // Si aucun plan n'a été trouvé, retourner null
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

    // Méthode auxiliaire pour reconstruire le plan à partir de l'état final
    private List<Action> getBFSPlan(Map<Map<Variable, Object>, Map<Variable, Object>> father, 
                                    Map<Map<Variable, Object>, Action> plan, 
                                    Map<Variable, Object> goalState) {
        List<Action> actions = new ArrayList<>();
        Map<Variable, Object> currentState = goalState;

        // On remonte depuis l'état final jusqu'à l'état initial en suivant les pères
        while (father.get(currentState) != null) {
            Action action = plan.get(currentState);
            actions.add(action);
            currentState = father.get(currentState);
        }

        // On inverse la liste des actions pour obtenir le plan dans l'ordre
        Collections.reverse(actions);
        return actions;
    }
}
