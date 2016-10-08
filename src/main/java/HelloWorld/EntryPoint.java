package HelloWorld;

/**
 * Created by marika on 08/10/16.
 */
public class EntryPoint {
    public static void main (String [] args) {
        System.out.println("Welcome to the GeneNetwork :)");

        DataParser dataParser = new DataParser();
        dataParser.setFilePath("/home/marika/Scaricati/GeneNetwork/GeneNetwork.csv");
        dataParser.parse();
    }
}
