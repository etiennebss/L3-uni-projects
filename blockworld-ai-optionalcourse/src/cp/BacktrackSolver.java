package cp;

import java.util.*;
import modelling.Constraint;
import modelling.Variable;

public class BacktrackSolver extends AbstractSolver {

    // Constructeur prenant les variables et les contraintes
    public BacktrackSolver(Set<Variable> variables, Set<Constraint> constraints) {
        super(variables, constraints);
    }

    @Override
    public Map<Variable, Object> solve() {
        // Appel initial à backtrack avec une affectation vide et toutes les variables non assignées
        return backtrack(new HashMap<>(), new LinkedList<>(this.variables));
    }

    public Map<Variable, Object> backtrack(Map<Variable, Object> i, LinkedList<Variable> v) {
        if (v.isEmpty()) {
            return i;
        }
        Variable xi = v.poll();

        for (Object vi : xi.getDomain()) {
            Map<Variable, Object> ni = new HashMap<>(i);
            ni.put(xi,vi);
            if (this.isConsistent2(ni)) {
                Map<Variable, Object> res = this.backtrack(ni, v);
                if (res != null) {
                    return res;
                }
            }
        }
        v.add(xi);
        return null;
    }

    public boolean isConsistent2(Map<Variable, Object> affectionPartielle) {
        for (Constraint constraint : this.constraints) {
            if (affectionPartielle.keySet().containsAll(constraint.getScope())) {
                if (!constraint.isSatisfiedBy(affectionPartielle)) {
                    return false;
                }
            }
        }
        return true;
    }

}
