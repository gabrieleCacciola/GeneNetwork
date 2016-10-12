package analytics;

import dao.Gene;

import java.io.*;
import java.util.*;

/**
 * Parses the GeneNetwork.csv and returns a Set<Gene>.
 */
public class DataParser {
    /**
     * Path of the .csv file.
     */
    private String filePath;

    /**
     * List of patients.
     */
    private Set<Gene> genes;

    private int geneIndex = 0;

    /**
     * Parse the CSV file.
     */
    public void parse() {
        genes = new HashSet<Gene>();
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";
        int rowCounter = 1;

        try {
            br = new BufferedReader(new FileReader(filePath));
            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] row = line.split(cvsSplitBy);

                // Skip the header.
                if (rowCounter == 1) {
                    rowCounter++;
                    continue;
                }

                parsePatient(row);

//                System.out.println("Yet another read row: "+rowCounter+", GeneName =  "+row[0]);

                rowCounter++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            System.out.println("#Geni = "+genes.size());
        }
    }

    private void parsePatient(String[] row) {
        Gene gene = new Gene();
        gene.setName(row[0]);
        gene.setIndex(geneIndex);
        int numPatients = (row.length - 2) / 2;
        gene.setControls(new double[numPatients]);
        gene.setPsoriatics(new double[numPatients]);
        double sum = 0;
        for (int i = 2; i < row.length; i++) {
            // pari = controls; dispari = psoriatic.
            double geneValue = Double.parseDouble(row[i]);
            if (i % 2 == 0) {
                gene.getControls()[i/2-1] = Double.parseDouble(row[i]);
            } else {
                gene.getPsoriatics()[i/2-1] = Double.parseDouble(row[i]);
            }
            sum += geneValue;
        }

        // If the row contains just 0, doesn't matter.
        if (sum > 0) {
            genes.add(gene);
            geneIndex++;
        }
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Set<Gene> getGenes() {
        return genes;
    }

    public void setGenes(Set<Gene> genes) {
        this.genes = genes;
    }
}
