package HelloWorld;

import java.util.Map;

/**
 * Created by marika on 08/10/16.
 */
public class Patient {
    /**
     * E.g. psoriatic1, ... , psoriatic14
     *      control1, ... , control14.
     */
    private String name;

    /**
     * Maps the gene name to his expression value in this patient.
     */
    private Map<String, Double> geneToExpression;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Double> getGeneToExpression() {
        return geneToExpression;
    }

    public void setGeneToExpression(Map<String, Double> geneToExpression) {
        this.geneToExpression = geneToExpression;
    }

    @Override
    public String toString() {
        return "Patient{" +
                "name='" + name + '\'' +
                ", geneToExpression=" + geneToExpression +
                '}';
    }
}
