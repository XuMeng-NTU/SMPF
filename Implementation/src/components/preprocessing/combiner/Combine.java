/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package components.preprocessing.combiner;

import abstraction.component.result.ResultHolder;
import abstraction.component.result.ResultHolderStatus;
import annotation.component.Component;
import annotation.component.method.Method;
import annotation.parameter.Parameter;
import dataset.Dataset;

/**
 *
 * @author Meng
 */
@Component
public class Combine {
    
    private static Dataset combined = new Dataset();
    private static int counter = 0;
    
    @Parameter(name="NUM_COMBINED", format="java.lang.Integer")
    private int NUM_COMBINED = 1;
    
    @Method(name="COMBINE", isDefault=true)
    public ResultHolder combine(Dataset data){
        
        for(int i=0;i<data.numInstances();i++){
            combined.addInstance(combined.numInstances(), data.getInstance(i));
        }
        
        counter++;
        
        if(counter<NUM_COMBINED){
            return new ResultHolder(combined, ResultHolderStatus.INCOMPLETE);
        } else{
            return new ResultHolder(combined, ResultHolderStatus.COMPLETE);
        }
    }
}
