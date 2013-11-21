/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package persistence.flow;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import persistence.flow.edge.Edge;
import persistence.flow.node.Node;
import persistence.registry.RegistryItem;

/**
 *
 * @author Meng
 */
@Entity
public class Flow extends RegistryItem implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL, mappedBy="flow")
    private List<Node> nodes = new ArrayList<>();
    
    @OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL, mappedBy="flow")
    private List<Edge> edges = new ArrayList<>();
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void addNode(Node node){
        nodes.add(node);
    }

    public void removeNode(Node node) {
        nodes.remove(node);
    }    
    
    public List<Node> getNodes(){
        return nodes;
    }
    
    public void registerNode(Node node){
        addNode(node);
        node.setFlow(this);
    }
    
    public void deregisterNode(Node node){
        
        for(Edge edge : edges){
            if(edge.getDownstream().equals(node)||edge.getUpstream().equals(node)){
                deregisterEdge(edge);
            }
        }
        
        removeNode(node);
        node.setFlow(null);
    }
    
    public void addEdge(Edge edge){
        edges.add(edge);
    }
    
    public void removeEdge(Edge edge){
        edges.remove(edge);
    }
    
    public List<Edge> getEdges(){
        return edges;
    }
    
    public void registerEdge(Edge edge){
        addEdge(edge);
        edge.setFlow(this);
    }
    
    public void deregisterEdge(Edge edge){
        removeEdge(edge);
        edge.setFlow(null);
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Flow)) {
            return false;
        }
        Flow other = (Flow) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.getName();
    }

    public List<Node> getRootNodes(){
        List<Node> result = new ArrayList<>();
        
        boolean hasUpstream;
        for(Node node : nodes){
            hasUpstream = false;
            for(Edge edge : edges){
                if(edge.getDownstream().equals(node)){
                    hasUpstream = true;
                    break;
                }
            }
            if(!hasUpstream){
                result.add(node);
            }
        } 
        return result;
    }
    
    public List<Edge> getDownstreamEdges(Node node){
        List<Edge> result = new ArrayList<>();
        for(Edge edge : edges){
            if(edge.getUpstream().equals(node)){
                result.add(edge);
            }
        }
        return result;
    }
    
    public List<Edge> getUpstreamEdges(Node node){
        List<Edge> result = new ArrayList<>();
        for(Edge edge : edges){
            if(edge.getDownstream().equals(node)){
                result.add(edge);
            }
        }
        return result;
    }    
}
