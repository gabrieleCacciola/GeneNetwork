package analytics;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import dao.Edge;
import dao.Gene;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;

import java.io.FileNotFoundException;
import java.util.Set;

/**
 * Given the Set<Gene>, calculates the Correlation and P-Values matrices.
 */
public class Analyzer {
    private Exporter exporter = new Exporter();
    private Multimap<Gene, Gene> pairDone = ArrayListMultimap.create();
    private int processedGenes = 0;
    /**
     * Calculates the Correlation Matrix.
     *
     * @param genes Set of genes.
     * @return The correlation matrix.
     */
    public void buildMatrices(Set<Gene> genes) throws FileNotFoundException {
        genes.parallelStream().forEach(g1 -> {
            if(processedGenes % 100 == 0) {
                System.out.println("Processing gene #" + processedGenes + " - " +
                "Genes related = " + exporter.getGenesSize() + " - " +
                "Edges calculated = " + exporter.getEdgesSize());
            }

            genes.parallelStream().forEach(g2 -> {
                if(pairDone.containsEntry(g1, g2) || pairDone.containsEntry(g2, g1)) {
                    return;
                }
                double[][] input = createInputMatrix(g1.getControls(), g2.getPsoriatics());
                PearsonsCorrelation pearson = new PearsonsCorrelation(input);
                double correlation = pearson.getCorrelationMatrix().getEntry(0, 1);
                double pValue = (pearson.getCorrelationPValues().getEntry(0, 1)) * Math.pow(genes.size(), 2);

                if (Math.abs(correlation) > 0.7 && pValue < 0.05) {
                    createNodes (g1, g2, correlation, pValue);
                }
            });

            processedGenes++;
        });

        exporter.export();
    }

    private void createNodes(Gene g1, Gene g2, double correlation, double pValue) {
        exporter.addGene(g1);
        exporter.addGene(g2);
        exporter.addEdge(new Edge(g1, g2, correlation, pValue));
        pairDone.put(g1, g2);
    }

    private double[][] createInputMatrix(double[] controls, double[] psoriatics) {
        double[][] input = new double[controls.length][2];

        for (int i = 0; i < controls.length; i++) {
            input[i][0] = controls[i];
            input[i][1] = psoriatics[i];
        }

        return input;
    }
}
