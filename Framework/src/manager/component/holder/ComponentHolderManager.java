/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package manager.component.holder;

import abstraction.component.result.ResultHolder;
import application.Background;
import dataset.Dataset;
import exception.NoSuchInstanceException;
import java.util.List;
import javax.persistence.EntityManager;
import interfaces.manager.Manager;
import java.util.ArrayList;
import javax.persistence.Query;
import persistence.component.Component;
import persistence.component.holder.ComponentHolder;
import persistence.component.method.Method;
import persistence.parameter.ParameterDefinition;
import persistence.parameter.ParameterValue;
import support.memory.MemoryManager;
import workbench.wrapper.WorkerWrapper;

/**
 *
 * @author Meng
 */
public class ComponentHolderManager implements Manager{

    private EntityManager entityManager;
    private MemoryManager memoryManager;

    public ComponentHolderManager(){
        entityManager = Background.getInstance().getEntityManager();
        memoryManager = Background.getInstance().getMemoryManager();
    }
    
    public void assignComponent(ComponentHolder holder, String componentName) throws NoSuchInstanceException{
        Query query = entityManager.createQuery("SELECT item FROM "+Component.class.getSimpleName()+" item WHERE item.name = :name");
        query.setParameter("name", componentName);

        if(query.getResultList().isEmpty()){
            throw new NoSuchInstanceException(componentName + " does not exist");
        } else{
            Component component = (Component) query.getSingleResult();
            holder.setComponent(component);
            for(ParameterDefinition definition : component.getParameters()){
                ParameterValue value = new ParameterValue();
                value.setDefinition(definition);
                value.setValue(definition.getDefaultValue());
                holder.addParameter(value);
            }
            
            for(Method method : component.getMethods()){
                if(method.isDefault()){
                    holder.setMethod(method);
                    break;
                }
            }

            entityManager.getTransaction().begin();
            entityManager.persist(holder);
            entityManager.getTransaction().commit();

        }
    }
    
    public void deassignComponent(ComponentHolder holder){
        holder.removeComponent();
        
        entityManager.getTransaction().begin();
        entityManager.persist(holder);
        entityManager.getTransaction().commit();           
    }
    
    public List<Component> scanForComponents(){
        List<Component> result = new ArrayList();
        
        Query query = entityManager.createQuery("SELECT item FROM "+Component.class.getSimpleName()+" item");
        result.addAll(query.getResultList());
        
        return result;
    }

    public void printComponentHolder(ComponentHolder holder){
        System.out.println("Component: "+holder.getComponent());
        System.out.println("Parameters: ");
        for(ParameterValue value : holder.getParameters()){
            System.out.println(value.toString());
        }

        System.out.println("Method: "+holder.getMethod());
    }
    
    public void changeParameter(ComponentHolder holder, String name, String value) throws NoSuchInstanceException{
        ParameterValue param = getParameterByName(holder, name);
        param.setValue(value);
        entityManager.getTransaction().begin();
        entityManager.persist(param);
        entityManager.getTransaction().commit();    
    }
    
    private ParameterValue getParameterByName(ComponentHolder holder, String name) throws NoSuchInstanceException{
        for(ParameterValue param : holder.getParameters()){
            if(param.getDefinition().getName().equalsIgnoreCase(name)){
                return param;
            }
        }
        throw new NoSuchInstanceException("Parameter "+name+" does not exist");
    }
    
    public void changeMethod(ComponentHolder holder, String name) throws NoSuchInstanceException{
        Method method = getMethodByName(holder, name);
        holder.setMethod(method);
        
        entityManager.getTransaction().begin();
        entityManager.persist(holder);
        entityManager.getTransaction().commit();    
    }
    
    private Method getMethodByName(ComponentHolder holder, String name) throws NoSuchInstanceException{
        for(Method method : holder.getComponent().getMethods()){
            if(method.getName().equalsIgnoreCase(name)){
                return method;
            }
        }
        throw new NoSuchInstanceException("Method "+name+" does not exist");
    }    
    
    private ResultHolder process(ComponentHolder holder, Dataset input){
        WorkerWrapper wrapper = new WorkerWrapper(holder);
        wrapper.process(input);
        return wrapper.getOutput();
    }
    
    public ResultHolder process(ComponentHolder holder, String label){
        Dataset input = (Dataset) memoryManager.get(label);
        return process(holder, input);
    }

    public void save(String label, Object obj){
        memoryManager.put(label, obj);
    }    
}
