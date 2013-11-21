/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package components.preprocessing.indicator;

import abstraction.component.ComponentTemplate;
import abstraction.component.result.ResultHolder;
import abstraction.component.result.ResultHolderStatus;
import annotation.component.Component;
import annotation.component.method.Method;
import annotation.component.input.RequiredInput;
import annotation.component.output.GeneratedOutput;
import annotation.parameter.Parameter;
import dataset.Attribute;
import dataset.Dataset;

/**
 *
 * @author Meng
 */
@Component
public class MACD extends ComponentTemplate{
    @Parameter(name="FAST_DAYS", format="java.lang.Integer")
    private int FAST_DAYS = 12;
    @Parameter(name="SLOW_DAYS", format="java.lang.Integer")
    private int SLOW_DAYS = 26;
    @Parameter(name="SIGNAL_DAYS", format="java.lang.Integer")
    private int SIGNAL_DAYS = 9;
    
    @RequiredInput(name="CLOSE", format="java.lang.Double")
    private String CLOSE = "CLOSE";
    
    @GeneratedOutput(name="MACD_LINE", format="java.lang.Double")
    private String MACD_LINE = "MACD_LINE";
    @GeneratedOutput(name="MACD_SIGNAL", format="java.lang.Double")
    private String MACD_SIGNAL = "MACD_SIGNAL";
    @GeneratedOutput(name="MACD_DIFF", format="java.lang.Double")
    private String MACD_DIFF = "MACD_DIFF";    
    
    @Method(name="INDICATE", isDefault=true)
    public ResultHolder indicate(Dataset data){
        Double[] fastEMA = new Double[data.numInstances()];
        Double[] slowEMA = new Double[data.numInstances()];
        Double[] diff = new Double[data.numInstances()];
        Double[] signal = new Double[data.numInstances()];        
        
        int position=0;
        
        while(position<data.numInstances()){
            
            if(position<FAST_DAYS){
                fastEMA[position] = null;
            } else{
                if(fastEMA[position-1]==null){
                    fastEMA[position-1] = Util.calculateAverage(data, CLOSE, position - FAST_DAYS, position);
                }
                fastEMA[position] = Util.calculateEMA(FAST_DAYS, (Double)data.getInstance(position).get(CLOSE), fastEMA[position-1]);              
            }
            
            if(position<SLOW_DAYS){
                slowEMA[position] = null;
            } else{
                if(slowEMA[position-1]==null){
                    slowEMA[position-1] = Util.calculateAverage(data, CLOSE, position - SLOW_DAYS, position);
                }
                slowEMA[position] = Util.calculateEMA(SLOW_DAYS, (Double)data.getInstance(position).get(CLOSE), slowEMA[position-1]);             
            }
            
            if(fastEMA[position]==null || slowEMA[position]==null){
                diff[position] = null;
                signal[position] = null;
            } else{
                diff[position] = fastEMA[position] - slowEMA[position];
                if(position<SLOW_DAYS+SIGNAL_DAYS){
                    signal[position]=  null;
                } else{
                    if(signal[position-1]==null){
                        signal[position] = Util.calculateEMA(SIGNAL_DAYS, diff[position], Util.calculateAverage(diff, position-SIGNAL_DAYS, position));
                    } else{
                        signal[position] = Util.calculateEMA(SIGNAL_DAYS, diff[position], signal[position-1]);
                    }
                }
            }
            position++;
        }        
  
        Attribute macdLine = new Attribute();
        Attribute macdSignal = new Attribute();
        Attribute macdDiff = new Attribute();
        
        for(int i=0;i<data.numInstances();i++){
            if(diff[i]!=null){
                macdLine.add(i, diff[i]);
            }
            if(signal[i]!=null){
                macdSignal.add(i, signal[i]);
            }
            if(diff[i]!=null && signal[i]!=null){
                macdDiff.add(i, diff[i] - signal[i]);
            }
        }        
        
        data.addAttribute(MACD_LINE, macdLine);
        data.addAttribute(MACD_SIGNAL, macdSignal);
        data.addAttribute(MACD_DIFF, macdDiff);
        
        return new ResultHolder(data, ResultHolderStatus.COMPLETE);
    }

}
