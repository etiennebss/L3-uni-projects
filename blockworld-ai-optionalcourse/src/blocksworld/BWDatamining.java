package blocksworld;

import java.util.*;
import bwgeneratordemo.*;
import datamining.BooleanDatabase;
import modelling.*;

public class BWDatamining {
    private Set<BooleanVariable> items = new HashSet<>();
    private Map<String, BooleanVariable> str = new HashMap<>();
    private BWVariables variables;

    public BWDatamining(int nbBlocks, int nbPiles) {
        variables = new BWVariables(nbBlocks, nbPiles);
        generateOnVariables();
        generateOnTablesVariables();
    }

    //Méthode qui génère les variables on_i_j qui montre un bloc sur un autre
    public void generateOnVariables() {
        for (Variable on_i : variables.getOnbVariables()) {
            for (Variable fixed_j : variables.getFixedbVariables()) {
                int i = BWVariables.getIndex(on_i);
                int j = BWVariables.getIndex(fixed_j);
                if (i != j) {
                    String onStr = "on_" + i +"_" + j;
                    String fixedStr = "fixed_" + j;
                    items.add(getOrCreateVariable(onStr));
                    items.add(getOrCreateVariable(fixedStr));
                }
            }
        }
    }

    public void generateOnTablesVariables() {
        for (Variable on_i : variables.getOnbVariables()) {
            for (Variable free_j : variables.getFreepVariables()) {
                int i = BWVariables.getIndex(on_i);
                int j = BWVariables.getIndex(free_j);
                if (i != j) {
                    String onTableStr = "onTable_" + i +"_" + j;
                    String freeStr = "free_" + j;
                    items.add(getOrCreateVariable(onTableStr));
                    items.add(getOrCreateVariable(freeStr));
                }
            }
        }
    }

    // Renvoie les variables booléennes qui sont vraies pour un état de BlocksWorld
    public Set<BooleanVariable> getTransaction(List<List<Integer>> transaction) {
        Set<BooleanVariable> variables = new HashSet<>();
        for (int p = 0; p < transaction.size(); p++){
            if(transaction.get(p).isEmpty()) {
                variables.add(getOrCreateVariable("free_" + p));
            }
            for (int b = 0; b < transaction.get(p).size(); b++) {
                int j = transaction.get(p).get(b);
                if (b==0) {
                    variables.add(getOrCreateVariable("onTable_" + j + "_" + p));
                }
                if (b < transaction.get(p).size() -1) {
                    int i = transaction.get(p).get(b+1);
                    variables.add(getOrCreateVariable("on_" + i + "_" + j));
                    variables.add(getOrCreateVariable("fixed_" + j));
                }
            }
        }
        return variables;
    }

    public BooleanDatabase generateDatabase(int nbTransactions) {
        BooleanDatabase db = new BooleanDatabase(items);
        for (int i = 0; i < nbTransactions; i++) {
            List<List<Integer>> transaction = Demo.getState(new Random());
            db.add(getTransaction(transaction));
        }
        return db;
    }

    // Méthode qui permet de faire une variable booléenne si elle n'existe pas
    public BooleanVariable getOrCreateVariable(String name) {
        return str.computeIfAbsent(name, BooleanVariable::new);
    }

    public Set<BooleanVariable> getItems() { return items; }
    public String toString() { return "{" + "items : " + getItems() + "}"; }
}
