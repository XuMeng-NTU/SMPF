/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package components.preprocessing.cleaner;

import abstraction.component.ComponentTemplate;
import abstraction.component.result.ResultHolder;
import abstraction.component.result.ResultHolderStatus;
import annotation.component.Component;
import annotation.component.method.Method;
import annotation.component.input.RequiredInput;
import annotation.parameter.Parameter;
import dataset.Dataset;
import dataset.Instance;

/**
 *
 * @author Meng
 */
@Component
public class Date extends ComponentTemplate{
    
    @Parameter(name="MAX_DAY_SPAN", format="java.lang.Integer")
    private int MAX_DAY_SPAN = 15;
    
    @RequiredInput(name="DATE", format="java.util.Date")
    private String DATE = "DATE";
    
    @Method(name="CLEAN", isDefault=true)
    public ResultHolder clean(Dataset data){
        int breakPoint = 0;
        int i;
        for (i = 1; i < data.numInstances(); i++) {
            Instance current = data.getInstance(i);
            Instance previous = data.getInstance(i-1);

            if (((java.util.Date) current.get(DATE)).getTime() - ((java.util.Date) previous.get(DATE)).getTime() > (MAX_DAY_SPAN * (1000 * 60 * 60 * 24))) {
                breakPoint = i;
            }    
        }
        return new ResultHolder(data.copyInstancesOf(breakPoint, data.numInstances()), ResultHolderStatus.COMPLETE);
    }  
}
