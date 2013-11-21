/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package abstraction.component.result;

import dataset.Dataset;

/**
 *
 * @author Meng
 */
public class ResultHolder {
    private Dataset dataset;
    private ResultHolderStatus status;
    
    public ResultHolder(Dataset dataset, ResultHolderStatus status){
        this.dataset = dataset;
        this.status = status;
    }
    
    public Dataset getDataset(){
        return dataset;
    }
    
    public ResultHolderStatus getStatus(){
        return status;
    }
    
}
