/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package manager.dataset;

import application.Background;
import dataset.Dataset;
import exception.NoSuchInstanceException;
import interfaces.manager.Manager;
import io.reader.DatasetReader;
import javax.persistence.EntityManager;
import persistence.dataset.DatasetDefinition;
import persistence.dataset.attribute.AttributeDefinition;
import support.memory.MemoryManager;

/**
 *
 * @author Meng
 */
public class DatasetManager implements Manager{
    private EntityManager entityManager;
    private MemoryManager memoryManager;

    public DatasetManager(){
        entityManager = Background.getInstance().getEntityManager();
        memoryManager = Background.getInstance().getMemoryManager();
    }    

    public void addAttribute(DatasetDefinition dataset, String name, String format, String label){
        
        AttributeDefinition attribute = new AttributeDefinition();
        attribute.setName(name);
        attribute.setLabel(label);
        attribute.setFormat(format);
        
        entityManager.getTransaction().begin();
        dataset.addAttribute(attribute);
        entityManager.getTransaction().commit();        
    }

    public void removeAttribute(DatasetDefinition dataset, String name) throws NoSuchInstanceException{
        AttributeDefinition attribute = getAttributeByName(dataset, name);

        entityManager.getTransaction().begin();
        dataset.removeAttribute(attribute);
        entityManager.remove(attribute);
        entityManager.getTransaction().commit();        
    }    
    
    private AttributeDefinition getAttributeByName(DatasetDefinition dataset, String name) throws NoSuchInstanceException{
        for(AttributeDefinition attr : dataset.getAttributes()){
            if(attr.getName().equalsIgnoreCase(name)){
                return attr;
            }
        }
        throw new NoSuchInstanceException("Attribute "+name+" does not exist");
    }    

    public void printDataset(DatasetDefinition dataset) {
        for(AttributeDefinition attr : dataset.getAttributes()){
            System.out.println("Attribute: "+attr);
        }
    }
    
    public Dataset readDataset(DatasetDefinition definition, String filepath){
        DatasetReader reader = new DatasetReader();
        return reader.read(definition, filepath);
    }

    public void save(String label, Object obj){
        memoryManager.put(label, obj);
    }    
    
}
