/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package workbench.wrapper;

import abstraction.component.ComponentTemplate;
import abstraction.component.result.ResultHolder;
import application.Background;
import dataset.Dataset;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;
import persistence.component.holder.ComponentHolder;
import persistence.parameter.ParameterValue;

/**
 *
 * @author Meng
 */
public class WorkerWrapper {
    private ComponentHolder holder;
    private Dataset input;
    private ResultHolder output;
    private ComponentTemplate worker;
    
    public WorkerWrapper(ComponentHolder holder){
        this.holder = holder;
        worker = setupWorker(holder);
    }
    
    private ComponentTemplate setupWorker(ComponentHolder holder){
        try {          
            Class provider = Background.getInstance().getImplementationManager().loadClass(holder.getComponent().getProvider());           
            ComponentTemplate worker = (ComponentTemplate) provider.newInstance();
            
            for(ParameterValue parameter : holder.getParameters()){
                Object value = Class.forName(parameter.getDefinition().getFormat()).getConstructor(String.class).newInstance(parameter.getValue());
                Field target = provider.getDeclaredField(parameter.getDefinition().getFieldName());
                target.setAccessible(true);
                target.set(worker, value);
                
            }
            return worker;
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | SecurityException | ClassNotFoundException | IllegalArgumentException | InvocationTargetException | NoSuchFieldException ex) {
            Logger.getLogger(WorkerWrapper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }    
    
    public void process(Dataset data){
        input = data;
        output = worker.process(holder.getMethod().getMethodName(), data);
    }
    
    public ResultHolder getOutput(){
        return output;
    }
    
    public Dataset getInput(){
        return input;
    }
    
}
