/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package workbench;

import abstraction.component.result.ResultHolderStatus;
import dataset.Dataset;
import io.reader.DatasetReader;
import io.writer.DatasetWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import persistence.component.holder.ComponentHolder;
import persistence.flow.edge.Edge;
import persistence.flow.node.Node;
import persistence.senario.Senario;
import persistence.senario.destination.Destination;
import persistence.senario.source.Source;
import workbench.wrapper.WorkerWrapper;

/**
 *
 * @author Meng
 */
public class Workbench {

    private Senario senario;
    
    private Map<ComponentHolder, WorkerWrapper> mapping;

    
    public Workbench(Senario senario){
        
        this.senario = senario;
        mapping = new HashMap<>();

    }
    
    public void run(){
        List<Source> sources = senario.getSources();
        DatasetReader reader = new DatasetReader();
        
        for(Source source : sources){
            Dataset dataset = reader.read(source.getDataset(), source.getFilename());
            
            startChain(source.getNode(), dataset);
            
        }
    }
    
    private void startChain(Node node, Dataset data){
        WorkerWrapper nodeWrapper = getOrCreateWrapper(node);
        nodeWrapper.process(data);
        
        if (nodeWrapper.getOutput().getStatus() == ResultHolderStatus.COMPLETE) {
            
            Destination dest = isDestination(node);
            if(dest!=null){
                new DatasetWriter().write(nodeWrapper.getOutput().getDataset(), dest.getFilename());
            }
            
            List<Edge> edges = senario.getFlow().getDownstreamEdges(node);

            for (Edge edge : edges) {
                WorkerWrapper edgeWrapper = getOrCreateWrapper(edge);
                edgeWrapper.process(nodeWrapper.getOutput().getDataset());
                if (edgeWrapper.getOutput().getStatus() == ResultHolderStatus.PASS) {
                    startChain(edge.getDownstream(), nodeWrapper.getOutput().getDataset());
                }
            }
        }
        
    }
    
    private WorkerWrapper getOrCreateWrapper(ComponentHolder holder){
        if(mapping.containsKey(holder)){
            return mapping.get(holder);
        } else{
            WorkerWrapper wrapper = new WorkerWrapper(holder);
            mapping.put(holder, wrapper);
            return wrapper;
        }
    }
    
    private Destination isDestination(Node node){
        for(Destination dest : senario.getDestination()){
            if(dest.getNode().equals(node)){
                return dest;
            }
        }
        return null;
    }
    
}
