/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package manager.general;

import application.Background;
import exception.NoSuchInstanceException;
import interfaces.manager.Manager;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import persistence.senario.Senario;
import persistence.senario.source.Source;
import support.memory.MemoryManager;
import workbench.Workbench;

/**
 *
 * @author Meng
 */
public class GeneralManager implements Manager{
    private EntityManager entityManager;
    private MemoryManager memoryManager;

    public GeneralManager(){
        entityManager = Background.getInstance().getEntityManager();
        memoryManager = Background.getInstance().getMemoryManager();
    }    

    public void save(String label, Object obj){
        memoryManager.put(label, obj);
    }
    
    public Object recall(String label){
        return memoryManager.get(label);
    }    
    
    
    public List<Senario> scanForCompleteSenarios(){
        List<Senario> senarioList = new ArrayList();
        List<Senario> result = new ArrayList();
        
        Query query = entityManager.createQuery("SELECT item FROM "+Senario.class.getSimpleName()+" item");
        senarioList.addAll(query.getResultList());
        
        for(Senario senario : senarioList){
            if(senario.getFlow()!=null){
                for(Source source : senario.getSources()){
                    if(source.getDataset()!=null && source.getFilename()!=null){
                        result.add(senario);
                        break;
                    }
                }
            }
        }
        
        return result;
    }    
    
    public void runSenario(String name) throws NoSuchInstanceException{
        Senario senario = getCompleteSenarioByName(name);
        Workbench workbench = new Workbench(senario);
        workbench.run();
    }
    
    private Senario getCompleteSenarioByName(String name) throws NoSuchInstanceException{
        List<Senario> senarioList = new ArrayList();
        List<Senario> result = new ArrayList();
        
        Query query = entityManager.createQuery("SELECT item FROM "+Senario.class.getSimpleName()+" item WHERE item.name=:name");
        query.setParameter("name", name);
        

        if (query.getResultList().isEmpty()) {
            throw new NoSuchInstanceException(name + " does not exist");
        } else{
            Senario senario = (Senario) query.getSingleResult();
            
            boolean complete = true;
            if(senario.getFlow()==null){
                complete = false;
            } else{
                for(Source source : senario.getSources()){
                    if(source.getDataset()==null || source.getFilename()==null){
                        complete = false;
                        break;
                    }
                }
            }
            if(complete){
                return senario;
            } else{
                throw new NoSuchInstanceException(name + " is not complete");
            }
        }
    }    
    
}
