package diseasewebapp;

import java.sql.Connection;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String fileName = "C:\\Users\\ADWAIT\\Desktop\\Project\\Disease-Prediction-WebApp\\Data\\disease Prediction\\Hypertension Stroke Diabetes\\hypertension_data.csv";
        NB nb = new NB();
        int[] numclm = { 0, 3, 4, 7 };
        Connection connect;

        // Read data
        List<String[]> data = CSVReaderExample.readCSV(fileName);

        boolean isFirstRow = true;

        // Train model
        for (String[] row : data) {
            if (isFirstRow) {

                isFirstRow = false;
                continue;
            }

            String label = row[row.length - 1]; // Assuming the label is the last column
            String[] features = new String[row.length - 1];
            System.arraycopy(row, 0, features, 0, row.length - 1);

            for (int x : numclm) {
                if (features[x].endsWith(".0")) {
                    features[x] = features[x].substring(0, features[x].length() - 2);
                }
            }

            nb.train(features, label);
        }

        // connect = connectDB.getConnection();

        String[] newExample = {
                "56", "0", "1",
                "120", "236", "0",
                "", "178", "",
                "0.8", "", "0",
                "2"
        };
        String predictedClass = nb.predict(newExample);
        System.out.println("Possiblity of having a disease " + predictedClass + "%");
        System.out.println();
    }
}
