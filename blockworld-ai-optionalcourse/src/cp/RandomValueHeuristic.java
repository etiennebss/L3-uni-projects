package cp;

import java.util.*;

import modelling.Variable;

public class RandomValueHeuristic implements ValueHeuristic {
    private Random random;

    public RandomValueHeuristic(Random random) {
        this.random = random;
    }

    @Override
    public List<Object> ordering(Variable variable, Set<Object> domain) {
        List<Object> values = new ArrayList<>(domain);
        Collections.shuffle(values, random);
        return values;
    }
}
