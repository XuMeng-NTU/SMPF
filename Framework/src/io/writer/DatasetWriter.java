/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package io.writer;

import dataset.Dataset;
import dataset.Instance;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.supercsv.io.CsvMapWriter;
import org.supercsv.prefs.CsvPreference;

/**
 *
 * @author Meng
 */
public class DatasetWriter {
    public void write(Dataset dataset, String filename){
        try {
            Set<String> labels = dataset.attributeSet();
            CsvMapWriter writer = new CsvMapWriter(new FileWriter(filename), CsvPreference.STANDARD_PREFERENCE);
            
            String[] nameMapping = new String[labels.size()];
            nameMapping = labels.toArray(nameMapping);
            
            writer.writeHeader(nameMapping);
            Map<String, Object> temp;
            
            for(int i=0;i<dataset.numInstances();i++){
                temp = new HashMap<>();
                Instance instance = dataset.getInstance(i);
                for(String label : nameMapping){
                    temp.put(label, instance.get(label));
                }
                writer.write(temp, nameMapping);
            }
            
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
