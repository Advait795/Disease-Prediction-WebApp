package diseasewebapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

public class NB {

    private int fCount = 0;
    private double smthClassCount;
    private List<Integer> featureKey = new ArrayList<>();
    private HashMap<Integer, Double> featurePred = new HashMap<>();
    private Map<Integer, Integer> featureTotals = new HashMap<>();
    private Map<String, Integer> totalExamples = new HashMap<>();
    private Map<String, Map<String, Integer>> classCounts = new HashMap<>();
    private Map<Map<Integer, String>, Map<String, Integer>> featureCounts = new HashMap<>();

    // categorising bins
    private int[] categories = { 1, 2, 3, 4 };
    private int[] age = { 19, 36, 66, 200 };
    private int[] trestbps = { 121, 130, 140, 500 };
    private int[] chol = { 200, 240, 500 };
    private int[] thalach = { 100, 151, 500 };
    private double[] oldpeak = { 0.6, 1.6, 5.0 };
    private double[] avgGlucose = { 99, 125, 500 };
    private double[] bmi = { 18.5, 25, 30, 200 };
    private double[] HbA1c = { 5.7, 6.5, 10.0 };

    // connect MongoDb
    private MongoClient mongoClient;
    private MongoDatabase database;

    public NB() {
        String connexctionsString = "mongodb://localhost:27017";
        mongoClient = MongoClients.create(connexctionsString);
        database = mongoClient.getDatabase("AllDisease");

    }

    public void close() {
        mongoClient.close();
    }

    public void train(String[] features, String label, String name) {

        classCounts.putIfAbsent(name, new HashMap<>());
        Map<String, Integer> labelCounts = classCounts.get(name);
        labelCounts.put(label, labelCounts.getOrDefault(label, 0) + 1);

        // db.name.updateOne({name:"training"},{$inc:{"label.featureCount.key.feature":1}})
        MongoCollection<Document> collection = database.getCollection(name);

        Bson filter = Filters.eq("name", "training");

        if (name == "Stroke") {
            for (int i = 0; i < features.length; i++) {
                if (i == 1 || i == 7 || i == 8) {
                    features[i] = strokeCategory(features[i], i);
                }

            }
        } else if (name == "Hypertension") {
            for (int i = 0; i < features.length; i++) {
                if (i == 0 || i == 3 || i == 4 || i == 7 || i == 9) {
                    features[i] = category(features[i], i);
                }
            }
        } else if (name == "Diabetes") {
            for (int i = 0; i < features.length; i++) {
                if (i == 1 || i == 5 || i == 6 || i == 7) {
                    features[i] = diabetesCategory(features[i], i);
                }
            }
        }

        featureKey.clear();
        featureKey(features.length);

        for (int i = 0; i < features.length; i++) {
            String feature = features[i];
            int key = featureKey.get(i);

            Document update = new Document("$inc", new Document(label + ".featureCount." + key + "." + feature, 1));

            // System.out.println(label + ".featureCount." + key + "." + feature);

            // collection.updateOne(filter, update);

            // Map<Integer, String> keyMap = new HashMap<>();
            // keyMap.put(key, feature);

            // featureCounts.putIfAbsent(keyMap, new HashMap<>());
            // Map<String, Integer> nestedMap = featureCounts.get(keyMap);
            // nestedMap.put(label, nestedMap.getOrDefault(label, 0) + 1);

        }

        totalExamples.put(name, totalExamples.getOrDefault(name, 0) + 1);

        // System.out.println(classCounts.get(name).get(label));

    }

    public String predict(String[] features, String name) {
        double one_label = 0;
        double zero_label = 0;
        this.fCount = 0;
        this.featurePred.clear();
        this.featureTotals.clear();
        double featureProductProb = 1;

        // categorising test data`
        if (name == "Stroke") {
            for (int i = 0; i < features.length; i++) {
                if (features[i] == "") {
                    continue;
                } else if (i == 1 || i == 7 || i == 8) {
                    features[i] = strokeCategory(features[i], i);
                }

            }
        } else if (name == "Hypertension") {
            for (int i = 0; i < features.length; i++) {

                if (features[i] == "") {
                    continue;
                } else if (i == 0 || i == 3 || i == 4 || i == 7 || i == 9) {
                    features[i] = category(features[i], i);

                }
            }
        } else if (name == "Diabetes") {
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

        Map<String, Integer> labels = classCounts.get(name);

        // Iterate through each calss label in classCounts
        for (String label : labels.keySet()) {

            // db.Hypertension.findOne({name:"training"}, {'1.classCount':true, _id:false});
            MongoCollection<Document> collection = database.getCollection(name);

            Bson filter = Filters.eq("name", "training");

            Bson projection = new Document(label + ".classCount", true).append("TotalExamples", true).append("_id",
                    false);

            // db.Hypertension.findOne({name:"training"}, {TotalExamples:true,_id:false})
            Document result = collection.find(filter).projection(projection).first();

            Document classCountDocument = (result).get(label, Document.class);
            Double ClassCount = classCountDocument.getDouble("classCount");

            Integer TotalExamples = result.getInteger("TotalExamples");

            // calculate the prior probablitites
            // double classPriorProb = (double) classCounts.get(name).get(label) /
            // totalExamples.get(name);

            double classPriorProb = ClassCount / TotalExamples;

            System.out.println(
                    "Class Prior Prob: " + label + ": " + ClassCount + "/" + TotalExamples + "= "
                            + classPriorProb);

            // System.out.println(
            // "ClassPriorProb: " + classCounts.get(name).get(label) + " / " +
            // totalExamples.get(name) + "="
            // + classPriorProb);
            System.out.println();

            // calculate the product of conditional probablitites

            double logFeatureProductProb = Math.log(classPriorProb);

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

                String projectionField = String.format("%s.featureCount.%s.%s", label, key, feature);

                Bson projectionFeat = new Document(projectionField, true).append("_id", false);

                Document resultFeat = collection.find(filter).projection(projectionFeat).first();

                Document labelDoc = resultFeat.get(label, Document.class);
                Document featureCountDoc = labelDoc.get("featureCount", Document.class);
                Document keyDoc = featureCountDoc.get(String.valueOf(key), Document.class);
                Integer Count = keyDoc.getInteger(feature);

                // Create the key map for the outer map
                // Map<Integer, String> keyMap = new HashMap<>();
                // keyMap.put(key, feature);

                // // Check if featureCounts contains the keyMap and label
                // if (featureCounts.containsKey(keyMap) &&
                // featureCounts.get(keyMap).containsKey(label)) {
                // count = featureCounts.get(keyMap).get(label);
                // }

                // count += 1.0;
                // double smthClassCount = classCounts.get(name).get(label) + featureKey.size();

                // double featureProb = (double) count / smthClassCount;

                Count += 1;
                double featureProb = (Count) / ClassCount;

                // System.out.println((Count + 1) + "/" + ClassCount);
                System.out.println("Probability of feature " + feature + " where class " +
                        label + ": " + featureProb);

                // Multiply all probabilities
                // featureProductProb *= featureProb;
                logFeatureProductProb += Math.log(featureProb);

                // System.out.println(featureProductProb + " featureProdctProb");

                if (label.trim().equals("1")) {
                    fCount++;
                    featureTotals.put(fCount, count);
                    featurePred.put(fCount, (featureProb * 100));
                }

                // db.Hypertension.updateOne({name:"testing"}, {$set:{"0.featurePred.1": 0}})

                Bson filterTest = Filters.eq("name", "testing");

                Document update = new Document("$set", new Document(label + ".featurePred." + key, featureProb));

                collection.updateOne(filterTest, update);

            }

            // System.out.println("Feature Totals: " + featureTotals);

            // final probablity for class
            double classProb = Math.exp(logFeatureProductProb);
            // double classProb = (featureProductProb) * (classPriorProb);

            // System.out.println(featureProductProb + " * " + classPriorProb + "=" +
            // classProb);

            if (Double.valueOf(label) == 0) {
                zero_label = classProb;
            } else {
                one_label = classProb;
            }

        }

        double total = zero_label + one_label;
        double zero_final = zero_label / total;
        double one_final = one_label / total;

        System.out.println();
        System.out.println("Total: " + zero_label + " + " + one_label + " = " +
                total);
        System.out.println();

        return String.valueOf(Math.round((one_final * 100)));

    }

    public void destroy() {
        mongoClient.close();
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

    @SuppressWarnings("rawtypes")
    public Map featuresPredictions() {
        return featurePred;
    }

    public Map totalFeatureValues() {
        return featureTotals;
    }

    public double classCountZero(String name, String label) {

        return classCounts.get(name).get(label);
    }

    public double classCountOne(String name, String label) {

        return classCounts.get(name).get(label);
    }

    public List<Integer> featureKey(int value) {

        for (int i = 1; i <= value; i++) {
            featureKey.add(i);
        }

        return featureKey;
    }

    public Integer totalInstance(String name) {
        return totalExamples.get(name);
    }
}
