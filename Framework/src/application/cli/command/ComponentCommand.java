/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package application.cli.command;

import interfaces.command.Command;
import exception.ExecutionException;
import java.util.Arrays;
import java.util.Deque;
import interfaces.manager.Manager;
import java.util.List;
import manager.component.ComponentManager;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import persistence.component.Component;

/**
 *
 * @author Meng
 */
public enum ComponentCommand implements Command {

    SCAN(new Options()),
    DEASSIGN(new Options()),
    ASSIGN(new Options()
            .addOption("p","provider",true,"class name of provider")
            ),
    DISPLAY(new Options());
    
    private Options options;
    
    ComponentCommand(Options options){
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
        ComponentManager componentManager = (ComponentManager) manager;
        
        Component current = (Component) stack.getLast();
        
        switch(this){
            case SCAN:
                List<Class> result = componentManager.scanForProviders();
                for(Class provider : result){
                    System.out.println(provider);
                }
                break;
            case DEASSIGN:
                componentManager.deassignProvider(current);
                break;                
            case ASSIGN:
                try {
                    String className = commandLine.getOptionValue("p");
                    componentManager.assignProvider(current, className);
                } catch (NullPointerException ex) {
                    ex.printStackTrace(System.out);
                    throw new ParseException("One or more option value missing");
                } catch (ClassNotFoundException ex) {
                    throw new ExecutionException(ex.getMessage());
                }
                break;
            case DISPLAY:
                componentManager.printComponent(current);
                break;                  
        }

    }
    
}
