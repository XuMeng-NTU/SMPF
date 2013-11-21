/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package components.preprocessing.indicator;

import dataset.Dataset;

/**
 *
 * @author Meng
 */
public class Util {
    
    public static Double calculateAverage(Dataset data, String key, int start, int end){
        double average = 0;
        for(int i=start;i<end;i++){
            average = ((double)data.getInstance(i).get(key)) + average;
        }
        return average / (end-start);
    } 
    
    public static Double calculateAverage(Double[] data, int start, int end){
        double average = 0;
        for(int i=start;i<end;i++){
            average = data[i]+average;
        }
        return average/(end-start);
    }
            

    public static Double calculateEMA(int period, Double current, Double previous){
        
        double m = 2.0/(period+1);
        return m*current+(1-m)*previous;
        
    }
    
    public static Double calculateSMA(int period, Double current, Double previous){
        double m = 1.0/period;
        return m*current+(1-m)*previous;
    }
}
