package modelling;

import java.util.*;

public class UnaryConstraint implements Constraint {
    private Variable var;
    private Set<Object> domain;

    public UnaryConstraint(Variable var, Set<Object> domain) {
        this.var = var;
        this.domain = domain;
    }
    //Retourne l'ensemble des variables avec contrainte
    @Override
    public Set<Variable> getScope() {
        Set<Variable> scope = new HashSet<>();
        scope.add(this.var);
        return scope;
    }
    
    @Override
    public boolean isSatisfiedBy(Map<Variable, Object> assignment) {
        // On regarde si toutes les variables du scope sont affectées
        for (Variable var : getScope()) {
            if (!assignment.containsKey(var)) {
                throw new IllegalArgumentException("Doit inclure toutes les variables");
            }
        }
        // On récupe la valeur affectée à la variable
        Object value = assignment.get(var);
        // Retourne vrai si le valeur est dans le sous-ensemble
        return domain.contains(value);
    } 
}
