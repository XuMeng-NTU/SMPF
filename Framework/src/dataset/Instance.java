/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dataset;

import java.util.Map;
import java.util.Set;

/**
 *
 * @author Meng
 */
public class Instance {

    private Map<String, Object> map;
    
    public Instance(Map<String, Object> map) {
        this.map = map;
    }

    public Object get(String value) {
        try {
            return map.get(value);
        } catch (Exception ex) {
            return null;
        }
    }
    
    public void add(String attribute, Object value){
        map.put(attribute, value);
    }
    
    public Set<String> attributeSet(){
        return map.keySet();
    }
    
}
