package NaiveBayesProject.src;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        String fileName = "C:\\Users\\ADWAIT\\Desktop\\Final Year Project\\main\\Disease-Prediction-WebApp\\Data\\disease Prediction\\Hypertension Stroke Diabetes\\hypertension_data - Copy.csv";
        NB nb = new NB();

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

           
            if(features[0].endsWith(".0")){
                features[0]= features[0].substring(0, features[0].length() - 2);
            }
        
            //System.out.println(features[0]);
            
            features[0]= nb.categories_age(features[0]);
            System.out.println(features[0]);
            nb.train(features, label);
        }

        // Classify new example
        String[] newExample = {
            "2", "0", "1", 
            "1", "2", "2",
            "0", "3", "1", 
            "2", "2", "1", 
            "2"
        }; // replace with actual feature values
        // String predictedClass = nb.predict(newExample);
        // System.out.println("Predicted class: " + predictedClass);
        // System.out.println();
    }
}
