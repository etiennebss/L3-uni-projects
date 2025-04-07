package planning;

import java.util.*;
import modelling.Variable;

public class BasicAction implements Action {
    private final Map<Variable, Object> precondition;
    private final Map<Variable, Object> effect;
    private final int cost;

    public BasicAction(Map<Variable, Object> precondition, Map<Variable, Object> effect, int cost) {
        this.precondition = precondition;
        this.effect = effect;
        this.cost = cost;
    }

    @Override
    public boolean isApplicable(Map<Variable, Object> state) {
        // Vérifie si l'action peut être appliquée dans l'état
        // L'action est applicable si toutes les variables de la précondition
        // sont présentes dans l'état avec les valeurs correspondantes.
        for (Map.Entry<Variable, Object> entry : precondition.entrySet()) {
            if (!state.containsKey(entry.getKey()) || !(state.get(entry.getKey()) == entry.getValue())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Map<Variable, Object> successor(Map<Variable,Object> state) {
        //Création d'un état basé sur l'état actuel
        Map<Variable, Object> newState = new HashMap<>(state);

        //Application des effets de l'action en faisant une MàJ des valeurs dans le nouvel état.
        for (Map.Entry<Variable, Object> entry : effect.entrySet()) {
            newState.put(entry.getKey(), entry.getValue());
        }
        return newState;
    }

    @Override
    public int getCost() {
        return cost;
    }

    @Override
    public String toString() {
        return "precondition : " + precondition.toString() + "effet : " + effect.toString();
    }
}
