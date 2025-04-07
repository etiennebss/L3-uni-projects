package datamining;

import java.util.*;
import modelling.*;

public class AssociationRule {
    Set<BooleanVariable> premise;
    Set<BooleanVariable> conclusion;
    float frequency;
    float confidence;

    public AssociationRule(Set<BooleanVariable> premise, Set<BooleanVariable> conclusion, float frequency, float confidence ) {
        this.premise = premise;
        this.conclusion = conclusion;
        this.frequency = frequency;
        this.confidence = confidence;
    }

    public Set<BooleanVariable> getPremise() {
        return premise;
    }

    public Set<BooleanVariable> getConclusion() {
        return conclusion;
    }

    public float getFrequency() {
        return frequency;
    }

    public float getConfidence() {
        return confidence;
    }

    @Override
    public String toString() {
        return "règle : " + premise + "->" + conclusion + "\n" + "fréquence : " + frequency + "confiance : " + confidence;
    }

    

}
