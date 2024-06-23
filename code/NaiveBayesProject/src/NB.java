package NaiveBayesProject.src;

import java.util.HashMap;
import java.util.Map;

public class NB {
    private Map<String, Integer> classCounts = new HashMap<>();
    private Map<String, Map<String, Integer>> featureCounts = new HashMap<>();
    private int totalExamples= 0;
    

    //bins
    // // private Map<Integer, Integer> age_bin = new HashMap<>();
    // //private Map<Integer, Integer> trestbps_bin = new HashMap<>();
    // private int[] age_bin ={1, 2, 3, 4};
    // private int[] chol_bin = {1, 2, 3};
    // private int[] thal_bin = {1, 2, 3};
    // private int[] oldpeak_bin = {1,2,3};



    public void train(String[] features, String label){

        classCounts.put(label, classCounts.getOrDefault(label, 0)+1);

        for(String feature: features){
            featureCounts.putIfAbsent(feature, new HashMap<>());
            featureCounts.get(feature).put(label, featureCounts.get(feature).getOrDefault(label, 0) + 1);     
        }
        

        totalExamples++;

    }

    public String predict(String[] features){
        String bestClass = null;       
        double bestProb = Double.NEGATIVE_INFINITY;
        double one_label = 0.0;
        double zero_label = 0.0;
       

        // Iterate through each calss label in classCounts 
        for (String label : classCounts.keySet()){
            
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
                System.out.println("Probablity of feature " + feature + " where class " +label+ ": " + featureProb );
                
                //multipling all probablities
                featureProductProb *= featureProb;          
            }

            //final probablity for class
            double classProb = classPriorProb * featureProductProb;
            System.out.println();
            System.out.println("Probablity of class " +label+ " for given equation is : " + classProb);
            System.out.println();

            if(Integer.valueOf(label) == 0){
                zero_label = classProb; 
            }else{
                one_label = classProb;     
            }

            if(classProb > bestProb){
                bestProb = classProb;
                bestClass = label;
            }
            
        }

        double total = zero_label + one_label;
        double zero_final = (double) zero_label/total;
        double one_final = (double) one_label/total;
        System.out.println("0 label final value: "+ zero_final);
        System.out.println("1 label final value " + one_final);
        System.out.println();
        

        return bestClass;
    }

    public String categories_age(String value){
       
        int ip =  Integer.valueOf(value);
        if( ip < 19){
            return String.valueOf(1);
        }else if(ip < 36){
            return String.valueOf(2);

        }else if(ip < 66){
            return String.valueOf(3);
        }

        return String.valueOf(4);

    }

    public String categories_trestbps(String value){
        
        int ip = Integer.valueOf(value);
        if(ip < 121){
            return String.valueOf(1);
        }else if(ip < 130){
            return String.valueOf(2);
        }else if(ip < 140){
            return String.valueOf(3);
        }
        return String.valueOf(4);

    }

    public String categories_chol(String value){
        int ip = Integer.valueOf(value);
        if(ip < 200){
            return String.valueOf(1);
        }else if (ip < 240){
            return String.valueOf(2);
        }
        return String.valueOf(3);
        
    }

    public String catergories_thalach(String value){
        int ip = Integer.valueOf(value);
        if(ip < 100){
            return String.valueOf(1);
        }else if(ip < 151){
            return String.valueOf(2);
        }
        return String.valueOf(3);
    }

    public String categories_oldpeak(String value){
        double ip = Double.valueOf(value);
        if(ip < 0.6){
            return String.valueOf(1);
        }else if(ip < 1.6){
            return String.valueOf(2);
        }
        return String.valueOf(3);
    }

}
