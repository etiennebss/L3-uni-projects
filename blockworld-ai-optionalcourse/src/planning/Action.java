package planning;

import java.util.*;
import modelling.Variable;

public interface Action {

    //Verification si l'action est applicable
    boolean isApplicable(Map<Variable, Object> state);

    //Application de l'action à l'état donné et retourne le nouvel état
    Map<Variable, Object> successor(Map<Variable, Object> state);
    
    //Coût de l'action
    int getCost();
}