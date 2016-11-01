package analytics;

import dao.Gene;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Session;

import java.util.Set;

/**
 * Given the Set<Gene>, calculates the Correlation and P-Values matrices.
 */
public class Analyzer {
    /**
     * Neo4j drivers.
     */
    private Driver driver;

    private Session session;

    public Analyzer(){
        driver = GraphDatabase.driver( "bolt://localhost", AuthTokens.basic( "neo4j", "loop4aiC" ) );
        session = driver.session();
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
        int connected = 0;

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
                    connected++;
                }
            }

            processedGenes++;
        }

        System.out.println("Connected = "+connected);
    }

    private void createNodes(Gene g1, Gene g2, double correlation) {
//        System.out.println(g1.getName()+" --"+correlation+"--> "+g2.getName());
        try {
//            session.run("CREATE (a:Gene {name:'" + g1.getName() + "'})");
//            session.run("CREATE (b:Gene {name:'" + g2.getName() + "'})");
            session.run("CREATE(a:Gene{name:'" + g1.getName() + "'}) -[r:CORRELATED]-> (b:Gene{name:'" + g2.getName() + "'})");
        } catch (Exception e) {
            session.run("MATCH (n) DETACH DELETE n");
            System.exit(-1);
        }
    }

    private double[][] createInputMatrix(double[] controls, double[] psoriatics) {
        double[][] input = new double[controls.length][2];

        for (int i = 0; i < controls.length; i++) {
            input[i][0] = controls[i];
            input[i][1] = psoriatics[i];
        }

        return input;
    }

    public void close() {
        session.close();
        driver.close();
    }
}
