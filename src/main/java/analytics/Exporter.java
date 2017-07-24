package analytics;

import dao.Edge;
import dao.Gene;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

/**
 * Generates the Neo4J CSVs to be uploaded.
 */
public class Exporter {
    private Set<Gene> genes = new HashSet<>();
    private Set<Edge> edges = new HashSet<>();

    public void addGene(Gene g) {
        genes.add(g);
    }

    public void addEdge(Edge e) {
        edges.add(e);
    }

    public void export() throws FileNotFoundException {
        PrintWriter pw = new PrintWriter("/usr/local/Cellar/neo4j/3.2.0/libexec/import/genes.csv");
        genes.stream().forEach(g -> pw.println(g.getIndex() + "," + g.getName()));
        pw.close();

        PrintWriter pw2 = new PrintWriter("/usr/local/Cellar/neo4j/3.2.0/libexec/import/edges.csv");
        edges.stream().forEach(e -> pw2.println(e.getOne().getIndex() + "," + e.getOther().getIndex() + ","
                + e.getCorrelation() + "," + e.getpValue()));
        pw2.close();
    }

    public int getGenesSize() {
        return genes.size();
    }

    public int getEdgesSize() {
        return edges.size();
    }
}
