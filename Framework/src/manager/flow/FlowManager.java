/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package manager.flow;

import application.Background;
import exception.NoSuchInstanceException;
import interfaces.manager.Manager;
import javax.persistence.EntityManager;
import persistence.flow.edge.Edge;
import persistence.flow.Flow;
import persistence.flow.node.Node;

/**
 *
 * @author Meng
 */
public class FlowManager implements Manager {
    private EntityManager entityManager;

    public FlowManager(){
        entityManager = Background.getInstance().getEntityManager();
    }
     
    public void addNode(Flow flow, String nodeName) {
        Node node = new Node();
        node.setName(nodeName);
        node.setFlow(flow);
        
        flow.addNode(node);      
        
        entityManager.getTransaction().begin();
        entityManager.persist(node);
        entityManager.persist(flow);
        entityManager.getTransaction().commit();
    }
    
    public void removeNode(Flow flow, String name) throws NoSuchInstanceException{
        
        Node node = getNodeByName(flow, name);
        
        entityManager.getTransaction().begin();
        flow.removeNode(node);
        entityManager.remove(node);
        entityManager.getTransaction().commit();
    }

    public void addEdge(Flow flow, String uname, String dname) throws NoSuchInstanceException {
        entityManager.getTransaction().begin();
        Edge edge = new Edge();
        edge.setName(uname+"_TO_"+dname);
        edge.setFlow(flow);
        
        edge.setUpstream(getNodeByName(flow, uname));
        edge.setDownstream(getNodeByName(flow, dname));
        
        flow.addEdge(edge);
        entityManager.getTransaction().commit();
    }
    
    public void removeEdge(Flow flow, String uname, String dname) throws NoSuchInstanceException{
        
        Edge edge = getEdgeByConnection(flow, uname, dname);
        
        entityManager.getTransaction().begin();
        flow.removeEdge(edge);
        entityManager.remove(edge);
        entityManager.getTransaction().commit();
    }    
    
    public Node getNodeByName(Flow flow, String name) throws NoSuchInstanceException{
        for (Node node : flow.getNodes()) {
            if (node.getName().equalsIgnoreCase(name)) {
                return node;
            }
        }

        throw new NoSuchInstanceException("Instance with name "+name+" does not exist");
    }
    
    public Edge getEdgeByConnection(Flow flow, String uname, String dname) throws NoSuchInstanceException{
        for (Edge edge : flow.getEdges()) {
            if (edge.getUpstream().getName().equalsIgnoreCase(uname) && edge.getDownstream().getName().equalsIgnoreCase(dname)){
                return edge;
            }
        }

        throw new NoSuchInstanceException("Instance with upstream node "+uname+" and downstream node "+dname+" does not exist");
    }    
    
    public void printFlow(Flow flow){
        System.out.println("Nodes: ");
        for(Node node : flow.getNodes()){
            System.out.println(node.getName());
        }
        
        System.out.println("Edges: ");
        for(Edge edge : flow.getEdges()){
            System.out.println(edge.getName());
        }
    }
}
