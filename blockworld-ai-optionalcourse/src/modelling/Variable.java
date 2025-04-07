package modelling;

import java.util.*;

public class Variable {
    protected String name;
    protected Set<Object> domain;

    // Constructeur
    public Variable(String name, Set<Object> domain) {
        this.name = name;
        this.domain = domain;
    }
    //Accesseur name
    public String getName() {
        return this.name;
    }
    //Accesseur domain
    public Set<Object> getDomain() {
        return this.domain;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Variable) {
            Variable other = (Variable) obj;
            return name.equals(other.name);
        }
        return false;
    }

    @Override
    public int hashCode(){
        return this.name.hashCode();
    }

    @Override
        public String toString() {
            return "Variable : " + this.name;
        }
}