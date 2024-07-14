package diseasewebapp;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;
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
    private int[] categories = { 1, 2, 3, 4 };
    private int[] age = { 19, 36, 66, 100 };
    private int[] trestbps = { 121, 130, 140, 500 };
    private int[] chol = { 200, 240, 500 };
    private int[] thalach = { 100, 151, 500 };
    private double[] oldpeak = { 0.6, 1.6, 5.0 };
    private double[] avgGlucose = { 99, 125, 300 };
    private double[] bmi = { 18.5, 25, 30, 50 };
    private double[] HbA1c = { 5.7, 6.5, 10.0 };

    private int fCount = 0;
    private HashMap<Integer, Double> featurePred = new HashMap<>();
    private List<Integer> featureKey = new ArrayList<>();

    public void train(String[] features, String label, String name) {

        classCounts.put(label, classCounts.getOrDefault(label, 0) + 1);

        // System.out.println("Updated classCounts: " + classCounts);

        // System.out.println();
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
                if (i == 1 || i == 5 || i == 6 || i == 7) {
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
        double one_label = 0;
        double zero_label = 0;

        this.fCount = 0;
        this.featurePred.clear();

        System.out.println(features[0]);

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
                } else if (i == 1 || i == 5 || i == 6 || i == 7) {
                    features[i] = diabetesCategory(features[i], i);

                }
            }
        }

        featureKey.clear();
        featureKey(features.length);

        System.out.println(features[0]);

        // Iterate through each calss label in classCounts
        for (String label : classCounts.keySet()) {

            // calculate the prior probablitites
            double classPriorProb = (double) classCounts.get(label) / totalExamples;

            System.out.println(
                    "ClassPriorProb: " + classCounts.get(label) + " / " + totalExamples + "=" + classPriorProb);
            System.out.println();

            // calculate the product of conditional probablitites
            double featureProductProb = 1.0;

            // System.out.println("Featuers length: " + features.length);
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
                // System.out.println(featureProductProb + "*=" + featureProb);

                if (label.trim().equals("1")) {
                    fCount++;
                    featurePred.put(fCount, featureProb);
                }
            }

            // final probablity for class
            double classProb = (featureProductProb) * (classPriorProb);
            System.out.println();
            System.out.println(
                    "classProb: " + featureProductProb + " * " + classPriorProb + " = " + classProb);
            System.out.println();

            if (Double.valueOf(label) == 0) {
                zero_label = classProb;
            } else {
                one_label = classProb;
            }

        }
        BigDecimal zero_bigdeci = new BigDecimal(zero_label);
        BigDecimal one_bigdeci = new BigDecimal(one_label);

        BigDecimal total = zero_bigdeci.add(one_bigdeci);
        // BigDecimal zero_final =
        BigDecimal one_final = one_bigdeci.divide(total, 10, RoundingMode.HALF_UP);

        System.out.println();
        System.out.println("Total: " + zero_label + " + " + one_label + " = " + total);

        return String.valueOf((one_final.multiply(new BigDecimal(100)).setScale(0, RoundingMode.HALF_UP)));
    }

    public String diabetesCategory(String value, int y) {
        if (y == 1) {
            for (int i = 0; i < age.length; i++) {

                if (Integer.valueOf(value) < age[i]) {
                    return String.valueOf(categories[i]);
                }
            }
        } else if (y == 5) {
            for (int i = 0; i < bmi.length; i++) {

                if (Double.valueOf(value) < bmi[i]) {
                    return String.valueOf(categories[i]);
                }
            }
        } else if (y == 6) {
            for (int i = 0; i < HbA1c.length; i++) {

                if (Double.valueOf(value) < HbA1c[i]) {
                    return String.valueOf(categories[i]);
                }
            }
        } else {
            for (int i = 0; i < avgGlucose.length; i++) {

                if (Double.valueOf(value) < avgGlucose[i]) {
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
