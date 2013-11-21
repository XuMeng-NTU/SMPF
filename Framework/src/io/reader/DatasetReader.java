/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package io.reader;

import dataset.Dataset;
import dataset.Instance;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.supercsv.cellprocessor.ParseDate;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvMapReader;
import org.supercsv.prefs.CsvPreference;
import persistence.dataset.DatasetDefinition;

/**
 *
 * @author Meng
 */
public class DatasetReader {

    public Dataset read(DatasetDefinition format, String filename){
        try {
            CsvMapReader reader = new CsvMapReader(new FileReader(filename), CsvPreference.STANDARD_PREFERENCE);
            String[] header = reader.getHeader(true);
            
            CellProcessor[] processors = new CellProcessor[format.getAttributes().size()];
            String[] nameMapping = new String[format.getAttributes().size()];
            
            int i;
            
            for(i=0;i<format.getAttributes().size();i++){
                Class repr = Class.forName(format.getAttributes().get(i).getFormat());
                
                nameMapping[i] = format.getAttributes().get(i).getLabel();
                
                if(repr.equals(ParseDate.class)){
                    processors[i] = (CellProcessor) repr.getConstructor(String.class).newInstance("yyyyMMdd");
                } else{
                    processors[i] = (CellProcessor) repr.newInstance();
                }
            }

            Dataset data = new Dataset();
            
            Map<String, Object> temp = reader.read(nameMapping, processors);
            
            while(temp!=null){
                data.addInstance(data.numInstances(), new Instance(temp));
                temp = reader.read(header, processors);
            }
         
            return data;            
            
        } catch (FileNotFoundException ex) {
            System.out.println(ex.getMessage());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        } catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(DatasetReader.class.getName()).log(Level.SEVERE, null, ex);
        } 
        
        return null;
    }
    
}

