package diseasewebapp;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.URLEncoder;
import java.sql.Connection;
import java.util.*;

import java.util.stream.*;

import com.google.gson.Gson;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class servlet extends HttpServlet {
    private NB nb;
    // private Connection connect;
    private Map<Integer, Double> featurePred = new HashMap<>();

    @Override
    public void init() {

        String[] fileNames = {
                "C:/Users/ADWAIT/Desktop/Project/Disease-Prediction-WebApp/code/Project/diseasewebapp/src/main/resources/hypertension_data.csv",
                "C:/Users/ADWAIT/Desktop/Project/Disease-Prediction-WebApp/code/Project/diseasewebapp/src/main/resources/stroke_data.csv",
                "C:/Users/ADWAIT/Desktop/Project/Disease-Prediction-WebApp/code/Project/diseasewebapp/src/main/resources/diabetes_prediction_dataset.csv"

        };

        String label;
        String[] features;
        String name;

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

    }

    public void processrequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html; charset-UTF-8");
        PrintWriter out = response.getWriter();
        try {

            String[] diseases = { "hypertension", "stroke", "diabetes" };

            // Reatrieve from input
            String[] newExample = {
                    request.getParameter("age"),
                    request.getParameter("MF"),
                    request.getParameter("cp"),
                    request.getParameter("trestbps"),
                    request.getParameter("cholesterol"),
                    request.getParameter("fbs"),
                    request.getParameter("restecg"),
                    request.getParameter("thalach"),
                    request.getParameter("exang"),
                    request.getParameter("oldpeak"),
                    request.getParameter("slope"),
                    request.getParameter("ca"),
                    request.getParameter("thal"),
                    request.getParameter("hyper"), // stroke
                    request.getParameter("heart"),
                    request.getParameter("married"),
                    request.getParameter("work_type"),
                    request.getParameter("residence"),
                    request.getParameter("glucose"),
                    request.getParameter("bmi"),
                    request.getParameter("smoking"),
                    request.getParameter("smoking_history"), // diabetes
                    request.getParameter("HbA1c_level")
            };

            Map<String, String> predictions = new HashMap<>();
            Map<String, Map<String, Double>> featuresPred = new HashMap<>();

            String[] input = null;

            for (String d : diseases) {
                if ("hypertension".equals(d)) {
                    input = Arrays.copyOfRange(newExample, 0, 13);
                } else if ("stroke".equals(d)) {
                    input = new String[] { newExample[1], newExample[0], newExample[13], newExample[14], newExample[15],
                            newExample[16], newExample[17], newExample[18], newExample[19], newExample[20] };
                } else if ("diabetes".equals(d)) {
                    input = new String[] { newExample[1], newExample[0], newExample[13], newExample[14], newExample[21],
                            newExample[19], newExample[22], newExample[18] };
                }

                // Predict Class
                String predictedClass = nb.predict(input, d);
                Map<String, Double> featurePred = nb.featuresPredictions();

                // Create a new map and copy values from featurePred
                Map<String, Double> featurePredCopy = new HashMap<>(featurePred);

                predictions.put(d, predictedClass);
                featuresPred.put(d, featurePredCopy);

            }

            String predictedClassesJson = new Gson().toJson(predictions);
            String featuresPredJson = new Gson().toJson(featuresPred);

            String redirectUrl = "result.html?predictedClasses=" + URLEncoder.encode(predictedClassesJson, "UTF-8") +
                    "&featuresPred=" + URLEncoder.encode(featuresPredJson, "UTF-8");
            response.sendRedirect(redirectUrl);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.close();
        }

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
