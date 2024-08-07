package diseasewebapp;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.URLEncoder;
import java.sql.Connection;
import java.util.*;

import java.util.stream.*;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.google.gson.Gson;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class servlet extends HttpServlet {
    private NB nb;
    // private Connection connect;
    private Map<Integer, Double> featurePred = new HashMap<>();
    Map<String, Integer> totalCount = new HashMap<>();
    Map<String, Double> ClassCountOne = new HashMap<>();
    MongoClient mongoClient;
    String redirectUrl;

    @Override
    public void init() {

        // Connecting to the database
        String connectionString = "mongodb://localhost:27017";
        mongoClient = MongoClients.create(connectionString);
        MongoDatabase database = mongoClient.getDatabase("AllDisease");

        String[] fileNames = {
                "C:/Users/ADWAIT/Desktop/Project/Disease-Prediction-WebApp/code/Project/diseasewebapp/src/main/resources/hypertension_data.csv",
                "C:/Users/ADWAIT/Desktop/Project/Disease-Prediction-WebApp/code/Project/diseasewebapp/src/main/resources/stroke_data.csv",
                "C:/Users/ADWAIT/Desktop/Project/Disease-Prediction-WebApp/code/Project/diseasewebapp/src/main/resources/diabetes_prediction_dataset.csv"

        };

        String label;
        String[] features;
        String name = "";
        int totalExamples = 0;
        double classCountZero;
        double classCountOne;

        nb = new NB();
        int[] hyperclm = { 0, 3, 4, 7 };
        int[] stokeClm = { 1 };
        int[] diabeClm = { 1, 7 };

        for (String filename : fileNames) {

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
                totalCount.put(name, totalExamples);
                classCountZero = nb.classCountZero(name, "0");
                classCountOne = nb.classCountOne(name, "1");
                ClassCountOne.put(name, classCountOne);

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
                totalCount.put(name, totalExamples);
                classCountZero = nb.classCountZero(name, "0");
                classCountOne = nb.classCountOne(name, "1");
                ClassCountOne.put(name, classCountOne);

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
                totalCount.put(name, totalExamples);
                classCountZero = nb.classCountZero(name, "0");
                classCountOne = nb.classCountOne(name, "1");
                ClassCountOne.put(name, classCountOne);

                Bson filter = Filters.eq("name", "training");

                Bson updateTotalExamples = Updates.set("TotalExamples", totalExamples);
                Bson updateZeroClassCount = Updates.set("0.classCount", classCountZero);
                Bson updateOneClassCount = Updates.set("1.classCount", classCountOne);

                Bson updateValue = Updates.combine(updateOneClassCount, updateZeroClassCount, updateTotalExamples);

                MongoCollection<Document> collection = database.getCollection("Diabetes");

                collection.updateOne(filter, updateValue);
            }
        }

    }

    public void processrequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html; charset-UTF-8");
        PrintWriter out = response.getWriter();
        try {

            String[] diseases = { "Hypertension", "Stroke", "Diabetes" };

            // Reatrieve from input
            String[] newExample = {
                    request.getParameter("age"), // 0
                    request.getParameter("MF"), // 1
                    request.getParameter("cp"), // 2
                    request.getParameter("trestbps"), // 3
                    request.getParameter("cholesterol"), // 4
                    request.getParameter("fbs"), // 5
                    request.getParameter("restecg"), // 6
                    request.getParameter("thalach"), // 7
                    request.getParameter("exang"), // 8
                    request.getParameter("oldpeak"), // 9
                    request.getParameter("slope"), // 10
                    request.getParameter("ca"), // 11
                    request.getParameter("thal"), // 12
                    request.getParameter("hyper"), // stroke //13
                    request.getParameter("heart"), // 14
                    request.getParameter("married"), // 15
                    request.getParameter("work_type"), // 16
                    request.getParameter("residence"), // 17
                    request.getParameter("glucose"), // 18
                    request.getParameter("bmi"), // 19
                    request.getParameter("smoking"), // 20
                    request.getParameter("smoking_history"), // diabetes //21
                    request.getParameter("HbA1c_level")// 22
            };

            Map<String, String> predictions = new HashMap<>();
            Map<String, Map<String, Double>> featuresPred = new HashMap<>();
            Map<String, Map<String, Integer>> featuresInput = new HashMap<>();
            Map<String, Map<Integer, Integer>> featureCounts = new HashMap<>();

            String[] input = null;
            int inputLen = 0;
            int[] inputVal = null;

            // List<String> HypertensionIP = new ArrayList<>();

            for (String d : diseases) {
                if ("Hypertension".equals(d)) {
                    // input = Arrays.copyOfRange(newExample, 0, 13);

                    input = new String[] { newExample[0].split(",")[0], newExample[1].split(",")[0],
                            newExample[2].split(",")[0], newExample[3].split(",")[0],
                            newExample[4].split(",")[0],
                            newExample[5].split(",")[0], newExample[6].split(",")[0], newExample[7].split(",")[0],
                            newExample[8].split(",")[0], newExample[9].split(",")[0], newExample[10].split(",")[0],
                            newExample[11].split(",")[0], newExample[12].split(",")[0] };
                    inputLen = 13;

                } else if ("Stroke".equals(d)) {
                    input = new String[] { newExample[1].split(",")[0], newExample[0].split(",")[0],
                            newExample[13].split(",")[0],
                            newExample[14].split(",")[0], newExample[15].split(",")[0],
                            newExample[16].split(",")[0], newExample[17].split(",")[0], newExample[18].split(",")[0],
                            newExample[19].split(",")[0],
                            newExample[20].split(",")[0] };
                    inputVal = new int[] { 1, 0, 13, 14, 16, 15, 19, 17, 18, 20 };

                } else if ("Diabetes".equals(d)) {
                    input = new String[] { newExample[1].split(",")[0], newExample[0].split(",")[0],
                            newExample[13].split(",")[0],
                            newExample[14].split(",")[0], newExample[21].split(",")[0],
                            newExample[19].split(",")[0], newExample[22].split(",")[0], newExample[18].split(",")[0] };
                    inputVal = new int[] { 1, 0, 13, 14, 21, 19, 22, 18 };
                }

                if ("Hypertension".equals(d)) {
                    for (int i = 0; i < inputLen; i++) {
                        if (!input[i].split(",")[0].isEmpty()) {
                            int x = i + 1;

                            featuresInput.putIfAbsent(d, new HashMap<>());
                            Map<String, Integer> nestMap = featuresInput.get(d);
                            nestMap.put(newExample[i].split(",")[1], x);
                        }
                    }
                } else if ("Stroke".equals(d)) {
                    int x = 0;
                    for (int i = 0; i < inputVal.length; i++) {
                        x++;

                        String value = newExample[inputVal[i]].split(",")[0];
                        if (!value.isEmpty()) {

                            System.out.println();
                            System.out.println(
                                    newExample[inputVal[i]].split(",")[0] + newExample[inputVal[i]].split(",")[0]);
                            System.out.println(inputVal[i] + " inputVal[i]");
                            System.out.println(x + " x");
                            System.out.println();
                            featuresInput.putIfAbsent(d, new HashMap<>());
                            Map<String, Integer> nestMap = featuresInput.get(d);
                            nestMap.put(newExample[inputVal[i]].split(",")[1], x);
                        }
                    }
                } else if ("Diabetes".equals(d)) {
                    int x = 0;
                    for (int i = 0; i < inputVal.length; i++) {
                        x++;
                        String value = newExample[inputVal[i]].split(",")[0];
                        if (!value.isEmpty()) {
                            System.out.println(d);
                            featuresInput.putIfAbsent(d, new HashMap<>());
                            Map<String, Integer> nestMap = featuresInput.get(d);
                            nestMap.put(newExample[inputVal[i]].split(",")[1], x);
                        }
                    }
                }

                // Predict Class
                String predictedClass = nb.predict(input, d);
                Map<String, Double> featurePred = nb.featuresPredictions();

                // Create a new map and copy values from featurePred
                Map<String, Double> featurePredCopy = new HashMap<>(featurePred);

                predictions.put(d, predictedClass);
                featuresPred.put(d, featurePredCopy);

                featureCounts = nb.featureCounts();

                // System.out.println(featureCounts);
                System.out.println(featuresInput);

                String predictedClassesJson = new Gson().toJson(predictions);
                String featuresPredJson = new Gson().toJson(featuresPred);
                String totalCountJson = new Gson().toJson(totalCount);
                String classCountOne = new Gson().toJson(ClassCountOne);
                String FeatureCounts = new Gson().toJson(featureCounts);
                String FeaturesInputs = new Gson().toJson(featuresInput);

                redirectUrl = "result.html?predictedClasses=" + URLEncoder.encode(predictedClassesJson, "UTF-8")
                        +
                        "&featuresPred=" + URLEncoder.encode(featuresPredJson, "UTF-8") + "&totalCount="
                        + URLEncoder.encode(totalCountJson, "UTF-8") + "&ClassCountOne="
                        + URLEncoder.encode(classCountOne, "UTF-8") + "&featureCounts="
                        + URLEncoder.encode(FeatureCounts, "UTF-8") + "&featureInput="
                        + URLEncoder.encode(FeaturesInputs, "UTF-8");

                // response.sendRedirect(redirectUrl);

                // mongoClient.close();

            }

            response.sendRedirect(redirectUrl);

        } catch (

        Exception e) {
            e.printStackTrace();
        } finally {
            out.close();
        }

    }

    @Override
    public void destroy() {
        mongoClient.close();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        processrequest(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        processrequest(request, response);
    }

}
