package modelling;

import java.util.*;

public class BooleanVariable extends Variable {
    public static final Set<Object> BOOLEAN_DOMAIN = new HashSet<>();

    static {
        BOOLEAN_DOMAIN.add(true);
        BOOLEAN_DOMAIN.add(false);
    }

    public BooleanVariable(String name) {
        super(name, BOOLEAN_DOMAIN);
    }

    @Override
    public boolean equals(Object obj) {

        Variable variable = (Variable) obj;
        return variable.getName() == this.name;
    }

    @Override
    public int hashCode(){
        return super.name.hashCode();
    }

    @Override
    public String toString() {
        return name;
    }
    
}
