package relationship;

import org.neo4j.graphdb.RelationshipType;

/**
 * Inhibition or Activation.
 */
public enum GenesRelationship implements RelationshipType{
    INHIBITION,
    ACTIVATION;
}
