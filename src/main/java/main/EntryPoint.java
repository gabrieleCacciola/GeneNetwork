package main;

import analytics.Analyzer;
import analytics.DataParser;

import java.io.FileNotFoundException;

/**
 * Runner.
 */
public class EntryPoint {
    public static void main (String [] args) throws FileNotFoundException {
        System.out.println("Welcome to the GeneNetwork :)");
        long timeBefore = System.currentTimeMillis();

        DataParser dataParser = new DataParser();
        dataParser.setFilePath("src/main/resource/GeneNetwork.csv");
        dataParser.parse();
        Analyzer analyzer = new Analyzer();
        analyzer.buildMatrices(dataParser.getGenes());

        System.out.println("Exec time = "+ (System.currentTimeMillis() - timeBefore));
    }
}