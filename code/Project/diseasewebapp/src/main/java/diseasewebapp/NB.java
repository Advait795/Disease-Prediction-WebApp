package diseasewebapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NB {
    private Map<String, Integer> classCounts = new HashMap<>();
    // private Map<String, Map<String, Integer>> featureCounts = new HashMap<>();
    private Map<Map<Integer, String>, Map<String, Integer>> featureCounts = new HashMap<>();
    private int totalExamples = 0;

    // categorising bins
    private int[] categories = { 1, 2, 3, 4, 5 };
    private int[] age = { 19, 36, 66, 100 };
    private int[] trestbps = { 121, 130, 140, 500 };
    private int[] chol = { 200, 240, 500 };
    private int[] thalach = { 100, 151, 500 };
    private double[] oldpeak = { 0.6, 1.6, 5.0 };
    private double[] avgGlucose = { 99, 125, 300 };
    private double[] bmi = { 18.5, 25, 30, 50 };
    private int[] mentHelth = { 10, 11, 21 };
    private int fCount = 0;
    private HashMap<Integer, Double> featurePred = new HashMap<>();
    private List<Integer> featureKey = new ArrayList<>();

    public void train(String[] features, String label, String name) {

        classCounts.put(label, classCounts.getOrDefault(label, 0) + 1);
        // System.out.println("Updated classCounts: " + classCounts);

        // System.out.println(features[0] + features[1] + features[2] + features[3] +
        // features[4] + features[5] +
        // features[6] + features[7] + features[8] + features[9] + features[10] +
        // features[11]
        // + features[12]);

        if (name == "stroke") {
            for (int i = 0; i < features.length; i++) {
                if (i == 1 || i == 7 || i == 8) {
                    features[i] = strokeCategory(features[i], i);
                }

            }
        } else if (name == "hypertension") {
            for (int i = 0; i < features.length; i++) {
                if (i == 0 || i == 3 || i == 4 || i == 7 || i == 9) {
                    features[i] = category(features[i], i);
                }
            }
        } else if (name == "diabetes") {
            for (int i = 0; i < features.length; i++) {
                if (i == 0 || i == 3 || i == 4 || i == 7 || i == 9) {
                    features[i] = diabetesCategory(features[i], i);
                }
            }
        }

        // System.out.println(features[0] + features[1] + features[2] + features[3] +
        // features[4] + features[5] +
        // features[6] + features[7] + features[8] + features[9] + features[10] +
        // features[11]
        // + features[12]);

        featureKey.clear();
        featureKey(features.length);

        for (int i = 0; i < features.length; i++) {
            String feature = features[i];
            int key = featureKey.get(i);

            Map<Integer, String> keyMap = new HashMap<>();
            keyMap.put(key, feature);

            featureCounts.putIfAbsent(keyMap, new HashMap<>());
            Map<String, Integer> nestedMap = featureCounts.get(keyMap);
            nestedMap.put(label, nestedMap.getOrDefault(label, 0) + 1);

        }

        totalExamples++;

    }

    public String predict(String[] features, String name) {
        String bestClass = null;
        double bestProb = Double.NEGATIVE_INFINITY;
        double one_label = 0.0;
        double zero_label = 0.0;
        this.fCount = 0;
        this.featurePred.clear();

        // System.out.println();
        // System.out.println(
        // "in pred " + features[0] + features[1] + features[2] + features[3] +
        // features[4] + features[5] +
        // features[6] + features[7] + features[8] + features[9] + features[10] +
        // features[11]
        // + features[12]);

        // categorising test data`
        if (name == "stroke") {
            for (int i = 0; i < features.length; i++) {
                if (features[i] == "") {
                    continue;
                } else if (i == 1 || i == 7 || i == 8) {
                    features[i] = strokeCategory(features[i], i);
                }

            }
        } else if (name == "hypertension") {
            for (int i = 0; i < features.length; i++) {

                if (features[i] == "") {
                    continue;
                } else if (i == 0 || i == 3 || i == 4 || i == 7 || i == 9) {
                    features[i] = category(features[i], i);

                }
            }
        } else if (name == "diabetes") {
            for (int i = 0; i < features.length; i++) {

                if (features[i] == "") {
                    continue;
                } else if (i == 0 || i == 4 || i == 12 || i == 13) {
                    features[i] = diabetesCategory(features[i], i);

                }
            }
        }

        // System.out.println(features[0] + features[1] + features[2] + features[3] +
        // features[4] + features[5] +
        // features[6] + features[7] + features[8] + features[9] + features[10] +
        // features[11]
        // + features[12]);

        featureKey.clear();
        featureKey(features.length);

        // System.out.println(features.length);
        // System.out.println(featureKey.size());

        // Iterate through each calss label in classCounts
        for (String label : classCounts.keySet()) {

            // calculate the prior probablitites
            double classPriorProb = (double) classCounts.get(label) / totalExamples;

            // calculate the product of conditional probablitites
            double featureProductProb = 1.0;

            for (int i = 0; i < features.length; i++) {
                String feature = features[i];
                int key = featureKey.get(i);
                int count = 0;

                if (feature.equals("")) {
                    if (label.trim().equals("1")) {
                        fCount++;
                    }
                    continue;
                }

                // Create the key map for the outer map
                Map<Integer, String> keyMap = new HashMap<>();
                keyMap.put(key, feature);

                // Check if featureCounts contains the keyMap and label
                if (featureCounts.containsKey(keyMap) && featureCounts.get(keyMap).containsKey(label)) {
                    count = featureCounts.get(keyMap).get(label);
                }

                double featureProb = (count + 1.0) / classCounts.get(label);
                System.out.println("Probability of feature " + feature + " where class " +
                        label + ": " + featureProb);

                // Multiply all probabilities
                featureProductProb *= featureProb;

                if (label.trim().equals("1")) {
                    fCount++;
                    featurePred.put(fCount, featureProb);
                }
            }

            // final probablity for class
            double classProb = classPriorProb * featureProductProb;
            // System.out.println();
            // System.out.println("Probablity of class " + label + " for given equation is:"
            // + classProb);
            // System.out.println();

            if (Double.valueOf(label) == 0) {
                zero_label = classProb;
            } else {
                one_label = classProb;
            }

            if (classProb > bestProb) {
                bestProb = classProb;
                bestClass = label;
            }

        }

        // System.out.println(featurePred);

        double total = zero_label + one_label;
        double zero_final = (double) zero_label / total;
        double one_final = (double) one_label / total;

        // System.out.println("zero label: " + zero_label);
        // System.out.println();
        // System.out.println("one lable: " + one_label);

        // System.out.println();
        // System.out.println("0 label final value: " + zero_final);
        // System.out.println("1 label final value " + one_final);
        // System.out.println();

        return String.valueOf(Math.round(one_final * 100));
    }

    public String diabetesCategory(String value, int y) {
        if (y == 0) {
            for (int i = 0; i < age.length; i++) {

                if (Integer.valueOf(value) < age[i]) {
                    return String.valueOf(categories[i]);
                }
            }
        } else if (y == 4) {
            for (int i = 0; i < bmi.length; i++) {

                if (Double.valueOf(value) < bmi[i]) {
                    return String.valueOf(categories[i]);
                }
            }
        } else if (y == 12) {
            for (int i = 0; i < mentHelth.length; i++) {

                if (Double.valueOf(value) < mentHelth[i]) {
                    return String.valueOf(categories[i]);
                }
            }
        } else {
            for (int i = 0; i < mentHelth.length; i++) {

                if (Double.valueOf(value) < mentHelth[i]) {
                    return String.valueOf(categories[i]);
                }
            }
        }
        return value;
    }

    public String strokeCategory(String value, int x) {
        if (x == 1) {
            for (int i = 0; i < age.length; i++) {

                if (Integer.valueOf(value) < age[i]) {
                    return String.valueOf(categories[i]);
                }
            }
        } else if (x == 7) {
            for (int i = 0; i < avgGlucose.length; i++) {

                if (Double.valueOf(value) < avgGlucose[i]) {
                    return String.valueOf(categories[i]);
                }
            }
        } else if (x == 8) {
            for (int i = 0; i < bmi.length; i++) {

                if (Double.valueOf(value) < bmi[i]) {
                    return String.valueOf(categories[i]);
                }
            }
        }
        return value;
    }

    public String category(String value, int x) {

        if (x == 0) {
            for (int i = 0; i < age.length; i++) {

                if (Integer.valueOf(value) < age[i]) {
                    return String.valueOf(categories[i]);
                }
            }
        } else if (x == 3) {
            for (int i = 0; i < trestbps.length; i++) {

                if (Integer.valueOf(value) < trestbps[i]) {
                    return String.valueOf(categories[i]);
                }
            }
        } else if (x == 4) {
            for (int i = 0; i < chol.length; i++) {

                if (Integer.valueOf(value) < chol[i]) {
                    return String.valueOf(categories[i]);
                }
            }
        } else if (x == 7) {
            for (int i = 0; i < thalach.length; i++) {

                if (Integer.valueOf(value) < thalach[i]) {
                    return String.valueOf(categories[i]);
                }
            }
        } else if (x == 9) {
            for (int i = 0; i < oldpeak.length; i++) {

                if (Double.parseDouble(value) < oldpeak[i]) {
                    return String.valueOf(categories[i]);
                }
            }
        }
        return String.valueOf(2);
    }

    public Map featuresPredictions() {
        return featurePred;
    }

    public Map finalFeaturePred(HashMap value) {

        return null;
    }

    public List<Integer> featureKey(int value) {

        for (int i = 0; i < value; i++) {
            featureKey.add(i);
        }

        return featureKey;
    }

}
