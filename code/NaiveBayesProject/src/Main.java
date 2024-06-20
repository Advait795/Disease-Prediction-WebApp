package NaiveBayesProject.src;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        String fileName = "C:\\Users\\ADWAIT\\Desktop\\Final Year Project\\Data\\disease Prediction\\Hypertension Stroke Diabetes\\hypertension_data.csv";
        NB nb = new NB();

        // Read data
        List<String[]> data = CSVReaderExample.readCSV(fileName);

        // Train model
        for (String[] row : data) {
            String label = row[row.length - 1];  // Assuming the label is the last column
            String[] features = new String[row.length - 1];
            System.arraycopy(row, 0, features, 0, row.length - 1);
            nb.train(features, label);
        }

        // Classify new example
        String[] newExample = {
            "57", "1", "3", 
            "145", "233", "1",
            "0", "150", "0", 
            "2.3", "0", "0", 
            "1"
        }; // replace with actual feature values
        String predictedClass = nb.predict(newExample);
        System.out.println("Predicted class: " + predictedClass);
    }
}
