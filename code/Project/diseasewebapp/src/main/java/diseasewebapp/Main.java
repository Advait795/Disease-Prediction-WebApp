package diseasewebapp;

//import java.sql.Connection;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        String[] fileNames = {
                "C:\\Users\\ADWAIT\\Desktop\\Project\\Disease-Prediction-WebApp\\code\\Project\\diseasewebapp\\src\\main\\resources\\hypertension_data.csv",
                "C:\\Users\\ADWAIT\\Desktop\\Project\\Disease-Prediction-WebApp\\code\\Project\\diseasewebapp\\src\\main\\resources\\stroke_data.csv",
                "C:\\Users\\ADWAIT\\Desktop\\Project\\Disease-Prediction-WebApp\\code\\Project\\diseasewebapp\\src\\main\\resources\\diabetes_data.csv"
        };

        String label;
        String[] features;
        String name;

        NB nb = new NB();
        int[] hyperclm = { 0, 3, 4, 7 };
        int[] stokeClm = { 1 };
        int[] diabeClm = { 0, 4, 12, 13 };

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

            if (filename.contains("stroke")) {

                name = "stroke";

                String[] strokeExample = {
                        "1", "63", "0",
                        "1", "", "4",
                        "1", "228.69", "36.6",
                        "1"
                };
                String predictedClass = nb.predict(strokeExample, name);
                System.out.println("Possiblity of having a stroke " + predictedClass + "%");
                System.out.println();
                continue;

            } else if (filename.contains("hypertension")) {
                // System.out.println(filename);

                name = "hypertension";

                String[] newExample = {
                        "57", "1", "3",
                        "145", "233", "1",
                        "1", "123", "0",
                        "2.1", "2", "1",
                        "0"
                };
                String predictedClass = nb.predict(
                        newExample, name);
                System.out.println("Possiblity of having hypertension " + predictedClass + "%");
                System.out.println();
            } else {

                name = "diabetes";

                String[] newExample = {
                        "56", "0", "1",
                        "120", "236", "0",
                        "", "178", "",
                        "0.8", "", "0",
                        "2"
                };
                String predictedClass = nb.predict(
                        newExample, name);
                System.out.println("Possiblity of having diabetes " + predictedClass + "%");
                System.out.println();
            }

        }
    }
}
