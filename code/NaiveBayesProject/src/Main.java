package NaiveBayesProject.src;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        String fileName = "C:\\Users\\ADWAIT\\Desktop\\Final Year Project\\main\\Disease-Prediction-WebApp\\Data\\disease Prediction\\Hypertension Stroke Diabetes\\hypertension_data.csv";
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

            features[0]= nb.categories_age(features[0]);    
            features[3]= nb.categories_trestbps(features[3]);
            features[4]= nb.categories_chol(features[4]);
            features[7]= nb.catergories_thalach(features[7]);
            features[9]= nb.categories_oldpeak(features[9]);
           
            
            nb.train(features, label);
        }

        // Classify new example
        String[] newExample = {
            "3", "0", "0", 
            "4", "3", "0",
            "0", "2", "1", 
            "2", "1", "0", 
            "3"
        }; // replace with actual feature values
        String predictedClass = nb.predict(newExample);
        System.out.println("Predicted class: " + predictedClass);
        System.out.println();
    }
}
