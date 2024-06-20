package NaiveBayesProject.src;

import java.util.HashMap;
import java.util.Map;

public class NB {
    private Map<String, Integer> classCounts = new HashMap<>();
    private Map<String, Map<String, Integer>> featureCounts = new HashMap<>();
    private int totalExamples= 0;

    
    public void train(String[] features, String label){
        classCounts.put(label, classCounts.getOrDefault(label, 0)+1);

        for(String feature: features){
            
            featureCounts.putIfAbsent(feature, new HashMap<>());
            featureCounts.get(feature).put(label, featureCounts.get(feature).getOrDefault(label, 0) + 1);
        }
        
        totalExamples++;

        System.out.println(featureCounts);
        System.out.println(classCounts);
    }

    public String predict(String[] features){
        String bestClass = null;       
        double bestProb = Double.NEGATIVE_INFINITY;

        // Iterate through each calss label in classCounts 
        for (String label : classCounts.keySet()){
            if (label.equals("target")){
                continue;
            }
            
            //calculate the prior probablitites
            double classPriorProb = (double) classCounts.get(label) / totalExamples;
            
            //calculate the product of conditional probablitites
            double featureProductProb = 1.0;
            for(String feature : features){
                int count = 0;
                if (featureCounts.containsKey(feature) && featureCounts.get(feature).containsKey(label)) {
                    count = featureCounts.get(feature).get(label);
                }
                
                //for smoothing we can add + featureCounts.size()
                double featureProb = (count + 1.0)/(classCounts.get(label));
                // System.out.println("Probablity of feature " + feature + " where class " +label+ ": " + featureProb );
                
                //multipling all probablities
                featureProductProb *= featureProb;          
            }

            //final probablity for class
            double classProb = classPriorProb * featureProductProb;

            System.out.println();
            System.out.println("Probablity of class " +label+ ": " + classProb);

            if(classProb > bestProb){
                bestProb = classProb;
                bestClass = label;
            }
            
        }

        return bestClass;
    }



}
