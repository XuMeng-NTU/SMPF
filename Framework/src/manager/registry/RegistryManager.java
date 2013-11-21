/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package manager.registry;

import interfaces.manager.Manager;
import application.Background;
import exception.InvalidRegistryTypeException;
import exception.NoSuchInstanceException;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import persistence.component.Component;
import persistence.dataset.DatasetDefinition;
import persistence.flow.Flow;
import persistence.registry.RegistryCategory;
import persistence.registry.RegistryItem;
import persistence.senario.Senario;

/**
 *
 * @author Meng
 */
public class RegistryManager implements Manager{

    private EntityManager entityManager;
    
    public RegistryManager(){
        entityManager = Background.getInstance().getEntityManager();
    }
    
    private void clearRegistry(){
        entityManager.getTransaction().begin();
        entityManager.remove(getRoot());
        entityManager.getTransaction().commit();        
    }
    
    public void initRegistry(){
        clearRegistry();
        establishRoot();
    }
    
    private void establishRoot(){
        
        RegistryItem root = new RegistryCategory();
        root.setName("ROOT");
        
        entityManager.getTransaction().begin();
        entityManager.persist(root);
        entityManager.getTransaction().commit();        
    }

    public RegistryItem getRoot(){
        Query query = entityManager.createQuery("SELECT item FROM "+RegistryItem.class.getSimpleName()+" item WHERE item.parent=null");
        if(query.getResultList().isEmpty()){
            establishRoot();
            return getRoot();
        } else{
            return (RegistryItem) query.getSingleResult();
        }
    }
    
    public void rename(RegistryItem target, String name){
        entityManager.getTransaction().begin();
        target.setName(name);
        entityManager.getTransaction().commit();
    }

    public RegistryItem add(RegistryCategory parent, String name, String type) throws InvalidRegistryTypeException{
        RegistryItem item = null;
        
        switch(type){
            case "CATEGORY":
                item = new RegistryCategory();
                break;
            case "COMPONENT":
                item = new Component();
                break;
            case "FLOW":
                item = new Flow();
                break;
            case "DATASET":
                item = new DatasetDefinition();
                break;
            case "SENARIO":
                item = new Senario();
                break;                
            default:
                throw new InvalidRegistryTypeException(type + " is not a valid registry type");
        }
        
        item.setName(name);
        item.setParent(parent);
        parent.addChild(item);
        
        entityManager.getTransaction().begin();
        entityManager.persist(item);
        entityManager.getTransaction().commit();       
        
        return item;
    }

    public void move(RegistryItem target, RegistryCategory parent){
        target.getParent().removeChild(target);
        target.setParent(parent);
        parent.addChild(target);
    }
    
    public void remove(RegistryItem item){
        entityManager.getTransaction().begin();
        item.getParent().removeChild(item);
        entityManager.remove(item);
        entityManager.getTransaction().commit();          
    }
    
    
    
    public RegistryItem getChildByName(String name, RegistryItem parent) throws NoSuchInstanceException{
        if(parent instanceof RegistryCategory){
            for(RegistryItem child : ((RegistryCategory)parent).getChildren()){
                if(child.getName().equalsIgnoreCase(name)){
                    return child;
                }
            }
        }
        
        throw new NoSuchInstanceException("Instance with name "+name+" does not exist");
    }

    public void printChildren(RegistryCategory current, boolean detail) {
        for (RegistryItem item : current.getChildren()) {
            System.out.print(item);
            if (detail) {
                System.out.print("\t" + item.getClass().getSimpleName());
            }
            System.out.println("");
        }
    }

}
