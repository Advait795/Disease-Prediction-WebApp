package NaiveBayesProject.src;

import java.util.HashMap;
import java.util.Map;

public class NaiveBayes {
    private Map<String, Integer> classCounts = new HashMap<>();
    private Map<String, Map<String, Integer>> featureCounts = new HashMap<>();
    private int totalExamples = 0;

    public void train(String[] features, String label) {
        classCounts.put(label, classCounts.getOrDefault(label, 0) + 1);
        for (String feature : features) {
            featureCounts.putIfAbsent(feature, new HashMap<>());
            featureCounts.get(feature).put(label, featureCounts.get(feature).getOrDefault(label, 0) + 1);
        }
        totalExamples++;
       
    }

    

    public String classify(String[] features) {
    
        String bestClass = null;
        double bestProb = Double.NEGATIVE_INFINITY;
    
        // Iterate through each class label in classCounts
        for (String label : classCounts.keySet()) {
            if (label.equals("target")) {
                continue;
            }

            // Calculate the prior probability of the class
            double classPriorProb = (double) classCounts.get(label) / totalExamples;
            System.out.println("Prior probability of class " + label + ": " + classPriorProb);
    
            // Calculate the product of conditional probabilities of features given the class
            double featureProductProb = 1.0;
            for (String feature : features) {
                // Calculate the count of the feature for the current class
                int count = featureCounts.containsKey(feature) && featureCounts.get(feature).containsKey(label)
                             ? featureCounts.get(feature).get(label) : 0;
    
                // Calculate the conditional probability of the feature given the class using Laplace smoothing
                double featureProb = (count + 1.0) / (classCounts.get(label) + featureCounts.size());
                System.out.println("Probability of feature '" + feature + "' given class " + label + ": " + featureProb);
    
                // Multiply the probabilities of all features together
                featureProductProb *= featureProb;
            }
    
            // Calculate the final probability for the class
            double classProb = classPriorProb * featureProductProb;
            System.out.println();
            System.out.println("Probability of class " + label + ": " + classProb);
            System.out.println();
    
            // Check if the current class has a higher probability than the current best class
            if (classProb > bestProb) {
                bestProb = classProb;
                bestClass = label;
            }
           
        }
        
    
        // Return the class label with the highest probability
        return bestClass;
    }
   
    
       
    
}
