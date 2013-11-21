/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package manager.senario;

import application.Background;
import exception.NoSuchInstanceException;
import interfaces.manager.Manager;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import persistence.dataset.DatasetDefinition;
import persistence.flow.Flow;
import persistence.flow.node.Node;
import persistence.senario.Senario;
import persistence.senario.destination.Destination;
import persistence.senario.source.Source;

/**
 *
 * @author Meng
 */
public class SenarioManager implements Manager{
    private EntityManager entityManager;

    public SenarioManager(){
        entityManager = Background.getInstance().getEntityManager();
    }    
    
    public List<Flow> scanForFlows(){
        List<Flow> result = new ArrayList();
        
        Query query = entityManager.createQuery("SELECT item FROM "+Flow.class.getSimpleName()+" item");
        result.addAll(query.getResultList());
        
        return result;
    }
    
    public List<DatasetDefinition> scanForDatasets(){
        List<DatasetDefinition> result = new ArrayList();
        
        Query query = entityManager.createQuery("SELECT item FROM "+DatasetDefinition.class.getSimpleName()+" item");
        result.addAll(query.getResultList());
        
        return result;
    }
    
    public void assignFlow(Senario senario, String flowName) throws NoSuchInstanceException {
        Query query = entityManager.createQuery("SELECT item FROM " + Flow.class.getSimpleName() + " item WHERE item.name = :name");
        query.setParameter("name", flowName);

        if (query.getResultList().isEmpty()) {
            throw new NoSuchInstanceException(flowName + " does not exist");
        } else {
            Flow flow = (Flow) query.getSingleResult();
            senario.setFlow(flow);
            List<Node> rootNodes = flow.getRootNodes();
            for (Node node : rootNodes) {
                Source source = new Source();
                source.setNode(node);
                senario.addSource(source);
            }

            entityManager.getTransaction().begin();
            entityManager.persist(senario);
            entityManager.getTransaction().commit();
        }

    }        

    public void deassignFlow(Senario senario){
        senario.removeFlow();
        
        entityManager.getTransaction().begin();
        entityManager.persist(senario);
        entityManager.getTransaction().commit();        
        
    }
    
    public void addDestination(Senario senario, String nodeName, String filename) throws NoSuchInstanceException{
        Node node = getNodeByName(senario, nodeName);
        
        Destination destination = new Destination();
        destination.setNode(node);
        destination.setFilename(filename);
        
        entityManager.getTransaction().begin();
        entityManager.persist(destination);
        senario.addDestination(destination);
        entityManager.getTransaction().commit();   
        
    }
    
    public void removeDestination(Senario senario, String destinationName) throws NoSuchInstanceException{
        
        Destination destination = getDestinationByName(senario, destinationName);
        
        entityManager.getTransaction().begin();
        entityManager.remove(destination);
        senario.removeDestination(destination);
        entityManager.getTransaction().commit();   
        
    }
    public void setSource(Senario senario, String sourceName, String datasetName, String filename) throws NoSuchInstanceException{
        Source source = getSourceByName(senario, sourceName);
        source.setFilename(filename);
        source.setDataset(getDatasetByName(datasetName));
        
        entityManager.getTransaction().begin();
        entityManager.persist(source);
        entityManager.getTransaction().commit();      
    }
    
    private Node getNodeByName(Senario senario, String name) throws NoSuchInstanceException{
        for(Node node : senario.getFlow().getNodes()){
            if(node.getName().equalsIgnoreCase(name)){
                return node;
            }
        }
        throw new NoSuchInstanceException("Source "+name+" does not exist");
    }    
    
    private Source getSourceByName(Senario senario, String name) throws NoSuchInstanceException{
        for(Source source : senario.getSources()){
            if(source.getNode().getName().equalsIgnoreCase(name)){
                return source;
            }
        }
        throw new NoSuchInstanceException("Source "+name+" does not exist");
    }    
    
    private Destination getDestinationByName(Senario senario, String name) throws NoSuchInstanceException{
        for(Destination destination : senario.getDestination()){
            if(destination.getNode().getName().equalsIgnoreCase(name)){
                return destination;
            }
        }
        throw new NoSuchInstanceException("Source "+name+" does not exist");
    }    
    private DatasetDefinition getDatasetByName(String datasetName) throws NoSuchInstanceException{
        Query query = entityManager.createQuery("SELECT item FROM " + DatasetDefinition.class.getSimpleName() + " item WHERE item.name = :name");
        query.setParameter("name", datasetName);

        if (query.getResultList().isEmpty()) {
            throw new NoSuchInstanceException(datasetName + " does not exist");
        } else {
            return (DatasetDefinition) query.getSingleResult();
        }
    }    
    public void printSenario(Senario current) {
        System.out.println("Flow: "+current.getFlow().getName());
        for(Source source : current.getSources()){
            System.out.println(source.toString());
        }
    }
}
