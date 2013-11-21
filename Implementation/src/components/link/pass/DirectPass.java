/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package components.link.pass;

import abstraction.component.ComponentTemplate;
import abstraction.component.result.ResultHolder;
import abstraction.component.result.ResultHolderStatus;
import annotation.component.Component;
import annotation.component.method.Method;
import dataset.Dataset;

/**
 *
 * @author Meng
 */
@Component
public class DirectPass extends ComponentTemplate{

    @Method(name="TEST", isDefault=true)
    public ResultHolder test(Dataset data){
        return new ResultHolder(data, ResultHolderStatus.PASS);
    }
}
