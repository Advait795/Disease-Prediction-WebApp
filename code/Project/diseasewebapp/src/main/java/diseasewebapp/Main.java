package diseasewebapp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bson.*;
import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;

public class Main {
    public static void main(String[] args) {

        // Connecting to the database
        String connectionString = "mongodb://localhost:27017";
        MongoClient mongoClient = MongoClients.create(connectionString);
        MongoDatabase database = mongoClient.getDatabase("AllDisease");

        String[] fileNames = {
                "C:/Users/ADWAIT/Desktop/Project/Disease-Prediction-WebApp/code/Project/diseasewebapp/src/main/resources/hypertension_data.csv",
                // "C:/Users/ADWAIT/Desktop/Project/Disease-Prediction-WebApp/code/Project/diseasewebapp/src/main/resources/stroke_data.csv",
                // "C:/Users/ADWAIT/Desktop/Project/Disease-Prediction-WebApp/code/Project/diseasewebapp/src/main/resources/diabetes_prediction_dataset.csv"
        };

        String label;
        String[] features;
        String name = "";
        int totalExamples = 0;
        double classCountZero;
        double classCountOne;

        NB nb = new NB();
        int[] hyperclm = { 0, 3, 4, 7 };
        int[] stokeClm = { 1 };
        int[] diabeClm = { 1, 7 };

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
                    name = "Stroke";

                    nb.train(features, label, name);

                }

                totalExamples = nb.totalInstance(name);
                classCountZero = nb.classCountZero(name, "0");
                classCountOne = nb.classCountOne(name, "1");

                Bson filter = Filters.eq("name", "training");

                Bson updateTotalExamples = Updates.set("TotalExamples", totalExamples);
                Bson updateZeroClassCount = Updates.set("0.classCount", classCountZero);
                Bson updateOneClassCount = Updates.set("1.classCount", classCountOne);

                Bson updateValue = Updates.combine(updateOneClassCount, updateZeroClassCount, updateTotalExamples);

                MongoCollection<Document> collection = database.getCollection("Stroke");

                collection.updateOne(filter, updateValue);

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
                    name = "Hypertension";

                    nb.train(features, label, name);
                }
                totalExamples = nb.totalInstance(name);
                classCountZero = nb.classCountZero(name, "0");
                classCountOne = nb.classCountOne(name, "1");

                Bson filter = Filters.eq("name", "training");

                Bson updateTotalExamples = Updates.set("TotalExamples", totalExamples);
                Bson updateZeroClassCount = Updates.set("0.classCount", classCountZero);
                Bson updateOneClassCount = Updates.set("1.classCount", classCountOne);

                Bson updateValue = Updates.combine(updateOneClassCount, updateZeroClassCount, updateTotalExamples);

                MongoCollection<Document> collection = database.getCollection("Hypertension");
                collection.updateOne(filter, updateValue);

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
                    name = "Diabetes";

                    nb.train(features, label, name);
                }

                totalExamples = nb.totalInstance(name);
                classCountZero = nb.classCountZero(name, "0");
                classCountOne = nb.classCountOne(name, "1");

                Bson filter = Filters.eq("name", "training");

                Bson updateTotalExamples = Updates.set("TotalExamples", totalExamples);
                Bson updateZeroClassCount = Updates.set("0.classCount", classCountZero);
                Bson updateOneClassCount = Updates.set("1.classCount", classCountOne);

                Bson updateValue = Updates.combine(updateOneClassCount, updateZeroClassCount, updateTotalExamples);

                MongoCollection<Document> collection = database.getCollection("Diabetes");
                collection.updateOne(filter, updateValue);
            }

        }

        // Prediction
        String[] examples = {
                "26", "0", "0",
                "110", "206", "0",
                "0", "", "1",
                "1.2", "", "1",
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
                name = "Hypertension";

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

                // Map<String, Double> featurePred = nb.featuresPredictions();
                // Map<Integer, Map<Integer, Double>> fetureTotals = nb.totalFeatureValues();

            } else if (filename.contains("stroke")) {

                name = "Stroke";

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

                // String predictedClass = nb.predict(input, name);
                // System.out.println("Possiblity of having a stroke " + predictedClass + "%");
                // System.out.println();

                // Map<String, Double> featurePred = nb.featuresPredictions();
                // Map<Integer, Map<Integer, Double>> fetureTotals = nb.totalFeatureValues();

            } else {
                name = "Diabetes";

                String[] input = new String[8];

                input[0] = (examples[1]); // sex
                input[1] = (examples[0]); // age
                input[2] = (examples[13]); // hypertension
                input[3] = (examples[14]); // heart_disease
                input[4] = (examples[21]); // smoking_history
                input[5] = (examples[19]); // bmi
                input[6] = (examples[22]); // HbA1c_level
                input[7] = (examples[18]); // avg_glucose_level

                // String predictedClass = nb.predict(input, name);
                // System.out.println("Possiblity of having a diabetes " + predictedClass +
                // "%");
                // System.out.println();

                // Map<String, Double> featurePred = nb.featuresPredictions();
                // Map<Integer, Integer> fetureTotals = nb.totalFeatureValues();
                // double classCount = nb.classCount();

            }

        }

    }
}
