package cp;

import java.util.Map;
import modelling.Variable;

public interface Solver {
    // Méthode pour résoudre le problème et retourner une solution
    Map<Variable, Object> solve();
}