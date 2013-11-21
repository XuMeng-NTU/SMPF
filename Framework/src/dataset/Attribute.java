/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dataset;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Meng
 */
public class Attribute {
    private Map<Integer, Object> map;
    
    public Attribute(){
        this.map = new LinkedHashMap<>();
    }
    
    public Attribute(Map<Integer, Object> map) {
        this.map = map;
    }

    public Object get(Integer value) {
        try {
            return map.get(value);
        } catch (Exception ex) {
            return null;
        }
    }
    
    public void add(int position, Object value){
        map.put(position, value);
    }
    
    public Set<Integer> instanceSet(){
        return map.keySet();
    }
    
}
