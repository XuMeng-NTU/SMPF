/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package manager.component;

import application.Background;
import support.implementation.ImplementationManager;
import java.util.List;
import javax.persistence.EntityManager;
import interfaces.manager.Manager;
import persistence.component.Component;
import persistence.component.method.Method;
import persistence.component.attribute.AttributeSpecification;
import persistence.parameter.ParameterDefinition;

/**
 *
 * @author Meng
 */
public class ComponentManager implements Manager{

    private EntityManager entityManager;
    private ImplementationManager implManager;

    public ComponentManager(){
        entityManager = Background.getInstance().getEntityManager();
        implManager = Background.getInstance().getImplementationManager();
    }
    
    public void assignProvider(Component component, String provider) throws ClassNotFoundException{
        implManager.assignProvider(component, provider);
        
        entityManager.getTransaction().begin();
        entityManager.persist(component);
        entityManager.getTransaction().commit();        
        
    }
    
    public void deassignProvider(Component component){
        component.removeProvider();
        
        entityManager.getTransaction().begin();
        entityManager.persist(component);
        entityManager.getTransaction().commit();           
    }
    
    public List<Class> scanForProviders(){
        return implManager.scanForProviders();
    }

    public void printComponent(Component component){
        System.out.println("Provider: "+component.getProvider());
        System.out.println("Parameters: ");
        for(ParameterDefinition definition : component.getParameters()){
            System.out.println(definition.toString());
        }
        System.out.println("Required Inputs: ");
        for(AttributeSpecification attribute : component.getRequiredInputs()){
            System.out.println(attribute.toString());
        }        
        System.out.println("Generated Outputs: ");
        for(AttributeSpecification attribute : component.getGeneratedOutputs()){
            System.out.println(attribute.toString());
        }         
        System.out.println("Methods: ");
        for(Method method : component.getMethods()){
            System.out.println(method.toString());
        }             
    }
    
//    public List<Component> getAllComponents(){
//        Query query = entityManager.createQuery("SELECT component FROM Component component");
//        
//        List<Component> result = new ArrayList();
//        result.addAll(query.getResultList());
//        
//        return result;
//    }
//    
//    public List<Component> getComponentsByType(ComponentType type){
//        Query query = entityManager.createQuery("SELECT component FROM Component component WHERE component.type = :type");
//        query.setParameter("type", type);
//        
//        List<Component> result = new ArrayList();
//        result.addAll(query.getResultList());
//        
//        return result;        
//    }



    
}
