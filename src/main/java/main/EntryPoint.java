package main;

import analytics.Analyzer;
import analytics.DataParser;

/**
 * Runner.
 */
public class EntryPoint {
    public static void main (String [] args) {
        System.out.println("Welcome to the GeneNetwork :)");

        long timeBefore = System.currentTimeMillis();

        DataParser dataParser = new DataParser();
        dataParser.setFilePath("/home/marika/Scaricati/GeneNetwork/GeneNetwork.csv");
        dataParser.parse();
        Analyzer analyzer = new Analyzer();
        analyzer.buildMatrices(dataParser.getGenes());
        System.out.println(analyzer);
        System.out.println("Exec time = "+ (System.currentTimeMillis() - timeBefore));
    }
}