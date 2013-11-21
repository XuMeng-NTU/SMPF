/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package abstraction.component;

import abstraction.component.result.ResultHolder;
import dataset.Dataset;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Meng
 */
public class ComponentTemplate {
    public ResultHolder process(String methodName, Dataset data){
        try {

            Method method = this.getClass().getDeclaredMethod(methodName, Dataset.class);
            return (ResultHolder) method.invoke(this, data);
            
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(ComponentTemplate.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
}
