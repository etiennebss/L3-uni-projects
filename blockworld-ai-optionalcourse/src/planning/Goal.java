package planning;

import java.util.*;
import modelling.Variable;

public interface Goal {
    boolean isSatisfiedBy(Map<Variable, Object> goal);
}
