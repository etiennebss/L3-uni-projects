package planning;

import java.util.*;
import modelling.Variable;

public interface Planner {
    List<Action> plan();
    Map<Variable, Object> getInitialState();
    Set<Action> getActions();
    Goal getGoal();

    void activateNodeCount(boolean activate);
    
    // Méthode pour récupérer le nombre de nœuds explorés
    int getNodeCount();
}
