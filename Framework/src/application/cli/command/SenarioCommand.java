/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package application.cli.command;

import interfaces.command.Command;
import exception.ExecutionException;
import exception.NoSuchInstanceException;
import java.util.Arrays;
import java.util.Deque;
import interfaces.manager.Manager;
import java.util.List;
import manager.senario.SenarioManager;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import persistence.dataset.DatasetDefinition;
import persistence.flow.Flow;
import persistence.senario.Senario;

/**
 *
 * @author Meng
 */
public enum SenarioCommand implements Command {

    SCAN(new Options()
            .addOption("f","flow",false,"scan for flow")
            .addOption("d","dataset",false,"scan for dataset definition")
         ),
    DEASSIGN(new Options()),
    ASSIGN(new Options()
            .addOption("f","flow",true,"name of flow")
            ),
    DISPLAY(new Options()),
    SET(new Options()
            .addOption("n","name",true,"name of source")
            .addOption("f","file",true,"filename")
            .addOption("d","dataset",true,"dataset definition")
            ),
    PROCESS(new Options()),
    ADD(new Options()
            .addOption("n","name",true,"name of destination")
            .addOption("f","file",true,"filename")
         ),
    REMOVE(new Options()
            .addOption("n","name",true,"name of destination")
         ),    
    VIEW(new Options());
    
    private Options options;
    
    SenarioCommand(Options options){
        this.options = options;
    }
    
    private CommandLine parseOptions(String[] args) throws ParseException{
        CommandLineParser parser = new BasicParser();
        CommandLine cmdLine = parser.parse(options, Arrays.copyOfRange(args, 1, args.length));

        if (cmdLine.getArgs().length > 0) {
            throw new ParseException("Option " + cmdLine.getArgList() + " is not recognized for command " + this.name());
        }

        return cmdLine;
    }
    
    @Override
    public void execute(String[] args, Manager manager, Deque<Object> stack) throws ParseException, ExecutionException {
        
        CommandLine commandLine = parseOptions(args);
        SenarioManager senarioManager = (SenarioManager) manager;
        
        Senario current = (Senario) stack.getLast();
        
        switch(this){
            case SCAN:
                if (commandLine.hasOption("f")) {
                    List<Flow> result = senarioManager.scanForFlows();
                    for (Flow flow : result) {
                        System.out.println(flow);
                    }
                } 
                
                if (commandLine.hasOption("d")) {
                    List<DatasetDefinition> result = senarioManager.scanForDatasets();
                    for (DatasetDefinition dataset : result) {
                        System.out.println(dataset);
                    }
                }
                break;
            case DEASSIGN:
                senarioManager.deassignFlow(current);
                break;                
            case ASSIGN:
                try {
                    String flowName = commandLine.getOptionValue("f");
                    senarioManager.assignFlow(current, flowName);
                } catch (NullPointerException ex) {
                    ex.printStackTrace(System.out);
                    throw new ParseException("One or more option value missing");
                } catch (NoSuchInstanceException ex) {
                    throw new ExecutionException(ex.getMessage());
                }
                break;
            case SET:
                try {
                    String sourceName = commandLine.getOptionValue("n");
                    String filename = commandLine.getOptionValue("f");
                    String datasetName = commandLine.getOptionValue("d");
                    senarioManager.setSource(current, sourceName, datasetName, filename);
                } catch (NullPointerException ex) {
                    ex.printStackTrace(System.out);
                    throw new ParseException("One or more option value missing");
                } catch (NoSuchInstanceException ex) {
                    throw new ExecutionException(ex.getMessage());
                }
                break;    
            case ADD:
                try {
                    String destinationName = commandLine.getOptionValue("n");
                    String filename = commandLine.getOptionValue("f");
                    senarioManager.addDestination(current, destinationName, filename);
                } catch (NullPointerException ex) {
                    ex.printStackTrace(System.out);
                    throw new ParseException("One or more option value missing");
                } catch (NoSuchInstanceException ex) {
                    throw new ExecutionException(ex.getMessage());
                }
                break;      
            case REMOVE:
                try {
                    String destinationName = commandLine.getOptionValue("n");
                    senarioManager.removeDestination(current, destinationName);
                } catch (NullPointerException ex) {
                    ex.printStackTrace(System.out);
                    throw new ParseException("One or more option value missing");
                } catch (NoSuchInstanceException ex) {
                    throw new ExecutionException(ex.getMessage());
                }
                break;                 
            case DISPLAY:
                senarioManager.printSenario(current);
                break;         
            case VIEW:
                if(current.getFlow()!=null){
                    stack.addLast(current.getFlow());
                } else{
                    throw new ExecutionException("Flow not set for this ComponentHolder");
                }
                break;            
        }

    }
    
}
