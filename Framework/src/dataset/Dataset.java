/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dataset;

import com.google.common.base.Joiner;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Meng
 */
public class Dataset {
    
    private Table<Integer, String, Object> table;
    
    public Dataset(){
        table = HashBasedTable.create();
    }
    
    public int numAttributes(){
        return table.columnKeySet().size();
    }
    
    public int numInstances(){
        return table.rowKeySet().size();
    }
    
    public Instance getInstance(int row){
        return new Instance(Collections.unmodifiableMap(table.row(row)));
    }
 
    public Attribute getAttribute(String column){
        return new Attribute(Collections.unmodifiableMap(table.column(column)));
    }
    
    protected Object getValue(int row, String column){
        return table.get(row, column);
    }
    
    protected void setValue(int row, String column, Object value){
        table.put(row, column, value);
    }
    
    public void addInstance(int position, Instance instance){
        for(String l : instance.attributeSet()){
            table.put(position, l, instance.get(l));
        }
    }
    
    public void addAttribute(String label, Attribute attribute){
        String columnName = label;
        for(Integer i : attribute.instanceSet()){
            table.put(i, columnName, attribute.get(i));
        }
    }
    
    public Dataset copyInstancesOf(int start, int end){
        Dataset result = new Dataset();
        
        for(int i=start;i<end;i++){
            result.addInstance(result.numInstances(), getInstance(i));
        }
        
        return result;
    }
    
    public Dataset copyAttributesOf(List<String> attributes){
        Dataset result = new Dataset();
        
        for(String attr : attributes){
            result.addAttribute(attr, getAttribute(attr));
        }
        
        return result;
    }
    
    public Set<String> attributeSet(){
        return table.columnKeySet();
    }
    
    public Set<Integer> instanceSet(){
        return table.rowKeySet();
    }
    
    public void print(){
        Iterator<String> iterator = table.columnKeySet().iterator();
        
        List<String> columns = new ArrayList<>();
        List<String> values;
        
        while(iterator.hasNext()){
            columns.add(iterator.next());
        }
        
        System.out.println(Joiner.on(",").join(columns));
        for(int i=0;i<numInstances();i++){
            values = new ArrayList<>();
            for (String col : columns) {
                try {
                    values.add(table.get(i, col).toString());
                } catch (Exception ex) {
                    values.add(null);
                }
            }
            System.out.println(Joiner.on(",").join(values));
        }
        
    }
    
}
