/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package application.cli.command;

import interfaces.command.Command;
import exception.ExecutionException;
import exception.InvalidRegistryTypeException;
import exception.NoSuchInstanceException;
import java.util.Arrays;
import java.util.Deque;
import interfaces.manager.Manager;
import manager.registry.RegistryManager;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import persistence.registry.RegistryCategory;
import persistence.registry.RegistryItem;

/**
 *
 * @author Meng
 */
public enum RegistryCommand implements Command{

    LIST(new Options()
            .addOption("d","detail",false,"all information")
            ), 
    ADD(new Options()
            .addOption("t","type",true,"type of the registry item")
            .addOption("n","name",true,"name of the registry item")
            ),
    RENAME(new Options()
            .addOption("n","new-name",true,"new name of the registry item")
            .addOption("o","old-name",true,"old name of the registry item")
            ),
    REMOVE(new Options()
            .addOption("n","name",true,"name of the registry item")
            ),
    VIEW(new Options()
            .addOption("n","name",true,"name of the registry item")
            );       
    
    private Options options;
    
    RegistryCommand(Options options){
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
        
        RegistryManager registryManager = (RegistryManager) manager;
        RegistryCategory current = (RegistryCategory) stack.getLast();
        
        String type;
        String name;
        String oldName;
        String newName;
        boolean detail;
        
        switch(this){
            case LIST:
                detail = false;
                if(commandLine.hasOption("d")){
                    detail = true;
                }
                registryManager.printChildren(current,detail);

                break;
            case ADD:
                try {
                    type = commandLine.getOptionValue("t").toUpperCase();
                    name = commandLine.getOptionValue("n");
                    try {
                        registryManager.add(current, name, type);
                    } catch (InvalidRegistryTypeException ex) {
                        throw new ExecutionException(ex.getMessage());
                    }
                } catch (NullPointerException ex) {
                    throw new ParseException("One or more option value missing");
                }        
                break;
            case REMOVE:
                try {
                    name = commandLine.getOptionValue("n");
                    try {
                        registryManager.remove(registryManager.getChildByName(name, current));
                    } catch (NoSuchInstanceException ex) {
                        throw new ExecutionException(ex.getMessage());
                    }
                } catch (NullPointerException ex) {
                    throw new ParseException("One or more option value missing");
                }
                break;
            case RENAME:
                try {
                    oldName = commandLine.getOptionValue("o");
                    newName = commandLine.getOptionValue("n");
                    try {
                        registryManager.rename(registryManager.getChildByName(oldName, current), newName);
                    } catch (NoSuchInstanceException ex) {
                        throw new ExecutionException(ex.getMessage());
                    }
                } catch (NullPointerException ex) {
                    throw new ParseException("One or more option value missing");
                }
                break;    
            case VIEW:
                try {
                    name = commandLine.getOptionValue("n");
                    try {
                        RegistryItem item = registryManager.getChildByName(name, current);
                        stack.addLast(item);
                    } catch (NoSuchInstanceException ex) {
                        throw new ExecutionException(ex.getMessage());
                    }
                } catch (NullPointerException ex) {
                    throw new ParseException("One or more option value missing");
                }
                break;             
        }
    }
    
}
