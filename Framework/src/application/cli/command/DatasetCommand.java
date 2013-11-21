/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package application.cli.command;

import dataset.Dataset;
import interfaces.command.Command;
import exception.ExecutionException;
import exception.NoSuchInstanceException;
import java.util.Arrays;
import java.util.Deque;
import interfaces.manager.Manager;
import manager.dataset.DatasetManager;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import persistence.dataset.DatasetDefinition;

/**
 *
 * @author Meng
 */
public enum DatasetCommand implements Command{

    DISPLAY(new Options()), 
    ADD(new Options()
            .addOption("n","name",true,"name of the attribute")
            .addOption("f","format",true,"format of the attribute")
            .addOption("l","label",true,"label of the attribute")
            ),
    REMOVE(new Options()
            .addOption("n","name",true,"name of the attribute")
            ),
    READ(new Options()
            .addOption("f","file",true,"path of the file")
            .addOption("s","save",true,"label for object to save")
            .addOption("p","print",false,"print result")
            );
    private Options options;
    
    DatasetCommand(Options options){
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
    public void execute(String[] args, Manager manager, Deque<Object> stack) throws ParseException, ExecutionException{
        
        CommandLine commandLine = parseOptions(args);
        
        DatasetManager datasetManager = (DatasetManager) manager;
        DatasetDefinition current = (DatasetDefinition) stack.getLast();
        
        String format;
        String name;
        String label;
        
        switch(this){
            case DISPLAY:
                datasetManager.printDataset(current);
                break;
            case ADD:
                try {
                    name = commandLine.getOptionValue("n");
                    format = commandLine.getOptionValue("f");
                    label = commandLine.getOptionValue("l");
                    datasetManager.addAttribute(current, name, format, label);
                } catch (NullPointerException ex) {
                    throw new ParseException("One or more option value missing");
                }
                break;
            case REMOVE:
                try {
                    name = commandLine.getOptionValue("n");
                    try {
                        datasetManager.removeAttribute(current, name);
                    } catch (NoSuchInstanceException ex) {
                        throw new ExecutionException(ex.getMessage());
                    }
                } catch (NullPointerException ex) {
                    throw new ParseException("One or more option value missing");
                }
                break;
            case READ:
                if (stack.getLast() instanceof DatasetDefinition) {
                    try {
                        name = commandLine.getOptionValue("f");
                        Dataset dataset = datasetManager.readDataset(current, name);
                        if(commandLine.hasOption("p")){
                            dataset.print();
                        }
                        if (commandLine.hasOption("s")) {
                            label = commandLine.getOptionValue("s");
                            datasetManager.save(label, dataset);
                        }
                    } catch (NullPointerException ex) {
                        throw new ParseException("One or more option value missing");
                    }
                } else{
                    throw new ExecutionException(READ.name()+" is not available under "+stack.getLast().getClass());
                }
                break; 
        }
    }
    
}