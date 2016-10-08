package HelloWorld;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by marika on 08/10/16.
 */
public class DataParser {
    /**
     * Path of the .csv file.
     */
    private String filePath;

    private List<Patient> patients;

    /**
     * Parse the CSV file.
     */
    public void parse() {
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";
        int rowCounter = 1;

        prefillPatients();

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

                System.out.println("Yet another read row: "+rowCounter+", GeneName =  "+row[0]);

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
        }
    }

    private void parsePatient(String[] row) {
        String geneName = row[0];
        double sum = 0;
        for (int i = 2; i < row.length; i++) {
            double geneValue = Double.parseDouble(row[i]);
            patients.get(i-2).getGeneToExpression().put(geneName, geneValue);
            sum += geneValue;
        }

        // If the row contains just 0, doesn't matter.
        if (sum == 0) {
            removeFromPatients(geneName);
        }
    }

    private void removeFromPatients(String geneName) {
        for (Patient patient : patients) {
            patient.getGeneToExpression().remove(geneName);
        }
    }


    /**
     * Create the list of patients to be filled by parse().
     */
    private void prefillPatients() {
        patients = new ArrayList<Patient>();

        for (int i = 1; i<15; i++) {
            Patient control = new Patient();
            control.setName("control"+i);
            control.setGeneToExpression(new HashMap<String, Double>());
            patients.add(control);
            Patient psoriatic = new Patient();
            psoriatic.setName("psoriatic"+i);
            psoriatic.setGeneToExpression(new HashMap<String, Double>());
            patients.add(psoriatic);
        }
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public List<Patient> getPatients() {
        return patients;
    }

    public void setPatients(List<Patient> patients) {
        this.patients = patients;
    }
}
