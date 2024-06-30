package diseasewebapp;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        String fileName = "code\\Test\\diseasewebapp\\src\\main\\resources\\hypertension_data.csv";
        NB nb = new NB();
        int[] numclm = {0, 3, 4, 7};
        
        // Read data
        List<String[]> data = CSVReaderExample.readCSV(fileName);

        boolean isFirstRow = true;

        // Train model
        for (String[] row : data) {
            if(isFirstRow){
                isFirstRow = false;
                continue;
            }

            String label = row[row.length - 1];  // Assuming the label is the last column
            String[] features = new String[row.length - 1];
            System.arraycopy(row, 0, features, 0, row.length - 1);

            for(int x : numclm) {
                if(features[x].endsWith(".0")){
                    features[x]= features[x].substring(0, features[x].length() - 2);
                }
            }

            nb.train(features, label);
        }

        
        String[] newExample = {
            "57", "1", "3", 
            "145", "233", "1",
            "0", "150", "0", 
            "2", "0", "0", 
            "1"
        }; 
        String predictedClass = nb.predict(newExample);
        System.out.println("Predicted class: " + predictedClass);
        System.out.println();
    }
}
