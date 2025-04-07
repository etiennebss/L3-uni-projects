package modelling;

import java.util.*;

public class DifferenceConstraint implements Constraint {
    private Variable var1;
    private Variable var2;

    public DifferenceConstraint(Variable var1, Variable var2) {
        this.var1 = var1;
        this.var2 = var2;
    }

    public Variable getV1() {
        return this.var1;
    }

    public Variable getV2() {
        return this.var2;
    }

    @Override
    public Set<Variable> getScope() {
        Set<Variable> scope = new HashSet<>();
        scope.add(var1);
        scope.add(var2);
        return scope;
    }

    @Override
    public boolean isSatisfiedBy(Map<Variable, Object> assignment) {
        for (Variable var : getScope()) {
            if(!assignment.containsKey(var)) {
                throw new IllegalArgumentException("Doit inclure toutes les variables du scope");
            }
        }
        Object value1 = assignment.get(var1);
        Object value2 = assignment.get(var2);
        return !value1.equals(value2);
    }
}
