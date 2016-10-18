package analytics;

import dao.Gene;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import relationship.GeneLabel;
import relationship.GenesRelationship;

import java.util.Iterator;
import java.util.Set;

/**
 * Given the Set<Gene>, calculates the Correlation and P-Values matrices.
 */
public class Analyzer {
    /**
     * Neo4j graph.
     */
    private GraphDatabaseService graphDB;

    public Analyzer(){
        graphDB = new GraphDatabaseFactory().newEmbeddedDatabase("data");
    }

    /**
     * Calculates the Correlation Matrix.
     *
     * @param genes Set of genes.
     * @return The correlation matrix.
     */
    public void buildMatrices(Set<Gene> genes) {
        double correlation;
        double pValue;
        int connessi = 0;

        double[][] input;
        int processedGenes = 1;
        PearsonsCorrelation pearson;

        for (Gene g1 : genes) {
            if ((processedGenes % 100) == 0) {
                System.out.println("Processing gene #" + processedGenes);
            }

            for (Gene g2 : genes) {
                input = createInputMatrix(g1.getControls(), g2.getPsoriatics());
                pearson = new PearsonsCorrelation(input);
                correlation = pearson.getCorrelationMatrix().getEntry(0, 1);
                pValue = pearson.getCorrelationPValues().getEntry(0, 1);

                if (Math.abs(correlation) > 0.7 && pValue < 0.05) {
                    createNodes (g1, g2, correlation);
                    connessi++;
                }
            }

            processedGenes++;
        }

        System.out.println("Connessi = "+connessi);
    }

    private void createNodes(Gene g1, Gene g2, double correlation) {
        Transaction transaction = graphDB.beginTx();
        
        Node node1 = graphDB.createNode();
        node1.setProperty("name", g1.getName());

        Node node2 = graphDB.createNode();
        node2.setProperty("name", g2.getName());

        Relationship edge;
        if (correlation > 0) {
            edge = node1.createRelationshipTo(node2, GenesRelationship.ACTIVATION);
        } else {
            edge = node1.createRelationshipTo(node2, GenesRelationship.INHIBITION);
        }

        edge.setProperty("weight", correlation);

        transaction.success();
        transaction.terminate();
    }

    private double[][] createInputMatrix(double[] controls, double[] psoriatics) {
        double[][] input = new double[controls.length][2];

        for (int i = 0; i < controls.length; i++) {
            input[i][0] = controls[i];
            input[i][1] = psoriatics[i];
        }

        return input;
    }

    public GraphDatabaseService getGraphDB() {
        return graphDB;
    }

    public void setGraphDB(GraphDatabaseService graphDB) {
        this.graphDB = graphDB;
    }

    public void print() {
        Transaction transaction = graphDB.beginTx();
        ResourceIterator<Node> nodes = graphDB.findNodes(GeneLabel.GENE);

        while(nodes.hasNext()) {
            Node gene = nodes.next();
            Iterator<Relationship> relationships = gene.getRelationships().iterator();
            while (relationships.hasNext()){
                Relationship edge = relationships.next();
                System.out.println(gene.getProperty("name") + " --" + edge.getProperty("weight") + "--" + edge.getEndNode());
            }
        }

        transaction.success();
    }

    public void close() {
        graphDB.shutdown();
    }
}
