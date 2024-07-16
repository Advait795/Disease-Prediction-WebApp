package diseasewebapp;

import java.util.ArrayList;
//import java.sql.Connection;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {

        String[] fileNames = {
                "C:/Users/ADWAIT/Desktop/Project/Disease-Prediction-WebApp/code/Project/diseasewebapp/src/main/resources/hypertension_data.csv",
                "C:/Users/ADWAIT/Desktop/Project/Disease-Prediction-WebApp/code/Project/diseasewebapp/src/main/resources/stroke_data.csv",
                "C:/Users/ADWAIT/Desktop/Project/Disease-Prediction-WebApp/code/Project/diseasewebapp/src/main/resources/diabetes_prediction_dataset.csv"
        };

        String label;
        String[] features;
        String name;

        NB nb = new NB();
        int[] hyperclm = { 0, 3, 4, 7 };
        int[] stokeClm = { 1 };
        int[] diabeClm = { 1, 7 };

        // Connection connect;
        // not to add all the disease

        for (String filename : fileNames) {

            // Read data
            List<String[]> data = CSVReaderExample.readCSV(filename);

            boolean isFirstRow = true;

            if (filename.contains("stroke")) {
                for (String[] row : data) {
                    if (isFirstRow) {

                        isFirstRow = false;
                        continue;
                    }

                    label = row[row.length - 1]; // Assuming the label is the last column
                    features = new String[row.length - 1];
                    System.arraycopy(row, 0, features, 0, row.length - 1);

                    for (int x : stokeClm) {
                        if (features[x].endsWith(".0")) {
                            features[x] = features[x].substring(0, features[x].length() - 2);
                        }
                    }
                    name = "stroke";

                    nb.train(features, label, name);

                }

            } else if (filename.contains("hypertension")) {
                for (String[] row : data) {
                    if (isFirstRow) {

                        isFirstRow = false;
                        continue;
                    }

                    label = row[row.length - 1]; // Assuming the label is the last column
                    features = new String[row.length - 1];
                    System.arraycopy(row, 0, features, 0, row.length - 1);

                    for (int x : hyperclm) {
                        if (features[x].endsWith(".0")) {
                            features[x] = features[x].substring(0, features[x].length() - 2);
                        }
                    }
                    name = "hypertension";

                    nb.train(features, label, name);
                }
            } else {
                for (String[] row : data) {
                    if (isFirstRow) {

                        isFirstRow = false;
                        continue;
                    }

                    label = row[row.length - 1]; // Assuming the label is the last column
                    features = new String[row.length - 1];
                    System.arraycopy(row, 0, features, 0, row.length - 1);

                    for (int x : diabeClm) {
                        if (features[x].endsWith(".0")) {
                            features[x] = features[x].substring(0, features[x].length() - 2);
                        }
                    }
                    name = "diabetes";

                    nb.train(features, label, name);
                }
            }
        }

        // Prediction
        String[] examples = {
                "29", "1", "1",
                "", "", "0",
                "0", "", "0",
                "", "2", "0",
                "2", // hypertension
                "0",
                "0", "1", "4",
                "1", "104.51", "27.3",
                "1", // stroke
                "1", "7" // diabetes

        };

        for (String filename : fileNames) {

            if (filename.contains("hypertension"))

            {
                name = "hypertension";

                String[] input = new String[13];

                input[0] = (examples[0]); // age
                input[1] = (examples[1]); // sex
                input[2] = (examples[2]); // cp
                input[3] = (examples[3]); // trestbps
                input[4] = (examples[4]); // chol
                input[5] = (examples[5]); // fbs
                input[6] = (examples[6]); // restecg
                input[7] = (examples[7]); // thalach
                input[8] = (examples[8]); // exang
                input[9] = (examples[9]); // oldpeak
                input[10] = (examples[10]); // slope
                input[11] = (examples[11]); // ca
                input[12] = (examples[12]); // thal

                String predictedClass = nb.predict(input, name);
                System.out.println("Possiblity of having hypertension " + predictedClass + "%");
                System.out.println();

                Map<String, Double> featurePred = nb.featuresPredictions();

            } else if (filename.contains("stroke")) {

                name = "stroke";

                String[] input = new String[10];

                input[0] = (examples[1]); // sex
                input[1] = (examples[0]); // age
                input[2] = (examples[13]); // hypertension
                input[3] = (examples[14]); // heart_disease
                input[4] = (examples[15]); // ever_married
                input[5] = (examples[16]); // work_type
                input[6] = (examples[17]); // Residence_type
                input[7] = (examples[18]); // avg_glucose_level
                input[8] = (examples[19]); // bmi
                input[9] = (examples[20]); // smoking_status

                String predictedClass = nb.predict(input, name);
                System.out.println("Possiblity of having a stroke " + predictedClass + "%");
                System.out.println();

                Map<String, Double> featurePred = nb.featuresPredictions();

            } else {
                name = "diabetes";

                String[] input = new String[8];

                input[0] = (examples[1]); // sex
                input[1] = (examples[0]); // age
                input[2] = (examples[13]); // hypertension
                input[3] = (examples[14]); // heart_disease
                input[4] = (examples[21]); // smoking_history
                input[5] = (examples[19]); // bmi
                input[6] = (examples[22]); // HbA1c_level
                input[7] = (examples[18]); // avg_glucose_level

                String predictedClass = nb.predict(input, name);
                System.out.println("Possiblity of having a diabetes " + predictedClass + "%");
                System.out.println();

                Map<String, Double> featurePred = nb.featuresPredictions();

            }

        }

    }
}
