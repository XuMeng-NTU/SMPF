/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package support.memory;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Meng
 */
public class MemoryManager {
    private Map<String, Object> memory;
    
    public MemoryManager(){
        memory = new HashMap<>();
    }
    
    public void put(String label, Object obj){
        memory.put(label, obj);
    }
    
    public Object get(String label){
        return memory.get(label);
    }
}
