/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package application.cli.command;

import abstraction.component.result.ResultHolder;
import interfaces.command.Command;
import exception.ExecutionException;
import exception.NoSuchInstanceException;
import java.util.Arrays;
import java.util.Deque;
import interfaces.manager.Manager;
import java.util.List;
import manager.component.holder.ComponentHolderManager;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import persistence.component.Component;
import persistence.component.holder.ComponentHolder;

/**
 *
 * @author Meng
 */
public enum ComponentHolderCommand implements Command {

    SCAN(new Options()),
    DEASSIGN(new Options()),
    ASSIGN(new Options()
            .addOption("c","component",true,"name of component")
            ),
    DISPLAY(new Options()),
    PARAM(new Options()
            .addOption("n","name",true,"name of parameter")
            .addOption("v","value",true,"value of parameter")
            ),
    METHOD(new Options()
            .addOption("n","name",true,"name of method")
            ),
    PROCESS(new Options()
            .addOption("r","recall",true,"label for object to recall")
            .addOption("s","save",true,"label for object to save")
            .addOption("p","print",false,"print result")
            ),
    VIEW(new Options());
    
    private Options options;
    
    ComponentHolderCommand(Options options){
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
        ComponentHolderManager componentHolderManager = (ComponentHolderManager) manager;
        
        ComponentHolder current = (ComponentHolder) stack.getLast();
        
        switch(this){
            case SCAN:
                List<Component> result = componentHolderManager.scanForComponents();
                for(Component component : result){
                    System.out.println(component);
                }
                break;
            case DEASSIGN:
                componentHolderManager.deassignComponent(current);
                break;                
            case ASSIGN:
                try {
                    String componentName = commandLine.getOptionValue("c");
                    componentHolderManager.assignComponent(current, componentName);
                } catch (NullPointerException ex) {
                    ex.printStackTrace(System.out);
                    throw new ParseException("One or more option value missing");
                } catch (NoSuchInstanceException ex) {
                    throw new ExecutionException(ex.getMessage());
                }
                break;
            case DISPLAY:
                componentHolderManager.printComponentHolder(current);
                break;        
            case PARAM:
                try {
                    String paramName = commandLine.getOptionValue("n");
                    String paramValue = commandLine.getOptionValue("v");
                    componentHolderManager.changeParameter(current, paramName, paramValue);
                } catch (NullPointerException ex) {
                    ex.printStackTrace(System.out);
                    throw new ParseException("One or more option value missing");
                } catch (NoSuchInstanceException ex) {
                    throw new ExecutionException(ex.getMessage());
                }
                break;
            case METHOD:
                try {
                    String methodName = commandLine.getOptionValue("n");
                    componentHolderManager.changeMethod(current, methodName);
                } catch (NullPointerException ex) {
                    ex.printStackTrace(System.out);
                    throw new ParseException("One or more option value missing");
                } catch (NoSuchInstanceException ex) {
                    throw new ExecutionException(ex.getMessage());
                }
                break;       
            case VIEW:
                if(current.getComponent()!=null){
                    stack.addLast(current.getComponent());
                } else{
                    throw new ExecutionException("Component not set for this ComponentHolder");
                }
                break;    
            case PROCESS:
                try {
                    String label = commandLine.getOptionValue("r");
                    ResultHolder output = componentHolderManager.process(current, label);
                    if (commandLine.hasOption("p")) {
                        output.getDataset().print();
                    }       
                    if(commandLine.hasOption("s")){
                        String label2 = commandLine.getOptionValue("s");
                        componentHolderManager.save(label2, output);
                    }
                } catch (NullPointerException ex) {
                    ex.printStackTrace(System.out);
                    throw new ParseException("One or more option value missing");
                }
                break;                 
        }

    }
    
}
