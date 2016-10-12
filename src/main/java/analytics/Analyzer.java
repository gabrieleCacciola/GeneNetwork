package analytics;

import dao.Gene;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;

import java.util.Arrays;
import java.util.Set;

/**
 * Given the Set<Gene>, calculates the Correlation and P-Values matrices.
 */
public class Analyzer {

    private double[][] correlationMatrix;

    private double[][] pValuesMatrix;

    private boolean[][] adjacencyMatrix;

    /**
     * Calculates the Correlation Matrix.
     *
     * @param genes Set of genes.
     * @return The correlation matrix.
     */
    public void buildMatrices(Set<Gene> genes) {
//        correlationMatrix = new double[genes.size()][genes.size()];
//        pValuesMatrix = new double[genes.size()][genes.size()];
        adjacencyMatrix = new boolean[genes.size()][genes.size()];
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
//                correlationMatrix[g1.getIndex()][g2.getIndex()] = pearson.getCorrelationMatrix().getEntry(0, 1);
//                pValuesMatrix[g1.getIndex()][g2.getIndex()] = pearson.getCorrelationPValues().getEntry(0, 1);
                correlation = pearson.getCorrelationMatrix().getEntry(0, 1);
                pValue = pearson.getCorrelationPValues().getEntry(0, 1);

                if (correlation > 0.7 && pValue < 0.05) {
                    adjacencyMatrix[g1.getIndex()][g2.getIndex()] = true;
                    connessi++;
                } else {
                    adjacencyMatrix[g1.getIndex()][g2.getIndex()] = false;
                }
            }

            processedGenes++;
        }

        System.out.println("Connessi = "+connessi);
    }

    private double[][] createInputMatrix(double[] controls, double[] psoriatics) {
        double[][] input = new double[controls.length][2];

        for (int i = 0; i < controls.length; i++) {
            input[i][0] = controls[i];
            input[i][1] = psoriatics[i];
        }

        return input;
    }

    public double[][] getCorrelationMatrix() {
        return correlationMatrix;
    }

    public void setCorrelationMatrix(double[][] correlationMatrix) {
        this.correlationMatrix = correlationMatrix;
    }

    public double[][] getpValuesMatrix() {
        return pValuesMatrix;
    }

    public void setpValuesMatrix(double[][] pValuesMatrix) {
        this.pValuesMatrix = pValuesMatrix;
    }

    public boolean[][] getAdjacencyMatrix() {
        return adjacencyMatrix;
    }

    public void setAdjacencyMatrix(boolean[][] adjacencyMatrix) {
        this.adjacencyMatrix = adjacencyMatrix;
    }

    @Override
    public String toString() {
        return "Analyzer{" +
                "correlationMatrix=" + Arrays.toString(correlationMatrix) +
                ", pValuesMatrix=" + Arrays.toString(pValuesMatrix) +
//                ", adjacencyMatrix=" + Arrays.toString(adjacencyMatrix) +
                '}';
    }
}
