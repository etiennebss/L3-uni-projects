package modelling;

import java.util.*;

public class Implication implements Constraint {
    private final Variable var1;
    private final Set<Object> set1;
    private final Variable var2;
    private final Set<Object> set2;

    // Constructeur v1, S1, v2 et S2
    public Implication(Variable var1, Set<Object> set1, Variable var2, Set<Object> set2) {
        this.var1 = var1;
        this.set1 = set1;
        this.var2 = var2;
        this.set2 = set2;
    }

    // Retourne l'ensemble des variables sur lesquelles porte la contrainte
    @Override
    public Set<Variable> getScope() {
        Set<Variable> scope = new HashSet<>();
        scope.add(var1);
        scope.add(var2);
        return scope;
    }

    // Vérifie si la contrainte est satisfaite par l'affectation donnée
    @Override
    public boolean isSatisfiedBy(Map<Variable, Object> assignment) throws IllegalArgumentException {

        for (Variable var : getScope()) {
            if (!assignment.containsKey(var)) {
                throw new IllegalArgumentException("L'affectation doit inclure toutes les variables du scope.");
            }
        }
        // Récupère les valeurs affectées aux variables
        Object value1 = assignment.get(var1);
        Object value2 = assignment.get(var2);

        // Si value1 est dans S1, alors value2 doit être dans S2 pour que la contrainte soit satisfaite
        if (set1.contains(value1)) {
            return set2.contains(value2);
        }

        // Si value1 n'est pas dans S1, la contrainte est satisfaite indépendamment de value2
        return true;
    }
}
