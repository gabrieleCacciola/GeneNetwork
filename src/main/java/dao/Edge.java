package dao;

/**
 * Edge on the Neo4J graph.
 */
public class Edge {
    private Gene one;
    private Gene other;
    private double correlation;
    private double pValue;

    public Edge(Gene one, Gene other, double weight, double pValue) {
        this.one = one;
        this.other = other;
        this.correlation = weight;
    }

    public Gene getOne() {
        return one;
    }

    public void setOne(Gene one) {
        this.one = one;
    }

    public Gene getOther() {
        return other;
    }

    public void setOther(Gene other) {
        this.other = other;
    }

    public double getCorrelation() {
        return correlation;
    }

    public void setCorrelation(double correlation) {
        this.correlation = correlation;
    }

    public double getpValue() {
        return pValue;
    }

    public void setpValue(double pValue) {
        this.pValue = pValue;
    }

    @Override
    public String toString() {
        return one.getName() + "," + other.getName() + "," + correlation + "," + pValue;
    }
}
