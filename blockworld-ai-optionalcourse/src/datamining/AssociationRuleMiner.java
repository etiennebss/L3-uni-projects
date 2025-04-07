package datamining;

import java.util.*;

public interface AssociationRuleMiner {
    // Renvoie la base de données
    BooleanDatabase getDatabase();
    // Extrait les règles d'association selon les seuils
    Set<AssociationRule> extract(float minFrequency, float minConfidence);
}
