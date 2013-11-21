/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package application.cli;

import application.Background;
import application.cli.command.ComponentCommand;
import application.cli.command.ComponentHolderCommand;
import application.cli.command.DatasetCommand;
import application.cli.command.FlowCommand;
import application.cli.command.GeneralCommand;
import interfaces.command.Command;
import application.cli.command.RegistryCommand;
import application.cli.command.SenarioCommand;
import exception.CommandNotAvailableException;
import exception.ExecutionException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import manager.component.ComponentManager;
import manager.component.holder.ComponentHolderManager;
import manager.dataset.DatasetManager;
import manager.flow.FlowManager;
import manager.general.GeneralManager;
import manager.registry.RegistryManager;
import manager.senario.SenarioManager;
import org.apache.commons.cli.ParseException;
import persistence.component.Component;
import persistence.component.holder.ComponentHolder;
import persistence.dataset.DatasetDefinition;
import persistence.flow.Flow;
import persistence.registry.RegistryCategory;
import persistence.senario.Senario;

/**
 *
 * @author Meng
 */
public class CommandLineInterpreter {

    public static final String PATH_SEPARATOR = ">";
    public static final String SCRIPT_END = "END";
    public static final String SCRIPT_ABORT = "ABORT";
    
    private Deque<Object> stack;
    private GeneralManager generalManager;
    private RegistryManager registryManager;
    private ComponentManager componentManager;
    private FlowManager flowManager;
    private ComponentHolderManager componentHolderManager;
    private DatasetManager datasetManager;
    private SenarioManager senarioManager;
    
    public CommandLineInterpreter(){
        Background.getInstance();
        stack = new LinkedList<>();
        
        generalManager = new GeneralManager();
        registryManager = new RegistryManager();
        componentManager = new ComponentManager();
        flowManager = new FlowManager();
        componentHolderManager = new ComponentHolderManager();
        datasetManager = new DatasetManager();
        senarioManager = new SenarioManager();
        
        stack.add(registryManager.getRoot());
    }
    
    public void run(){
        Scanner keyboard = new Scanner(System.in);

        boolean exit = false;

        while (!exit) {
            try {
                System.out.print(getPath(stack));
                String input = keyboard.nextLine();

                if (input.equalsIgnoreCase("Q")) {
                    exit = true;
                } else if(input.equalsIgnoreCase("script")){
                    System.out.println("SCRIPTING");
                    List<String> script = new ArrayList<>();
                    
                    String line = keyboard.nextLine();
                    
                    while(!(line.equalsIgnoreCase(SCRIPT_END)||line.equalsIgnoreCase(SCRIPT_ABORT))){
                        script.add(line);
                        line = keyboard.nextLine();
                    }

                    switch(line){
                        case SCRIPT_END:
                            for(String inst : script){
                                execute(inst);
                            }
                            break;
                        case SCRIPT_ABORT:
                            break;
                    }
                    

                } else if(input.equalsIgnoreCase("load")){
                    System.out.print("name of script? ");
                    String file = keyboard.nextLine();
                    Scanner reader = new Scanner(Paths.get("assets/scripts/"+file+".kddl"));
                    while(reader.hasNextLine()){                      
                        execute(reader.nextLine());
                    }
                }else {
                    execute(input);
                }
            } catch (ParseException ex) {
                System.out.println(ex.getMessage());
            } catch (CommandNotAvailableException ex) {
                System.out.println(ex.getMessage());
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
        
        Background.getInstance().close();
    }

    private void execute(String cmdLine) throws ParseException, CommandNotAvailableException {
        String[] cmdTokens = cmdLine.trim().split("\\s+");

        String cmd = cmdTokens[0].toUpperCase();
        Command command;

        try {
            command = GeneralCommand.valueOf(cmd);
            command.execute(cmdTokens, generalManager, stack);
        } catch (IllegalArgumentException exception) {
            if (stack.peekLast() instanceof RegistryCategory) {
                try {
                    command = RegistryCommand.valueOf(cmd);
                    command.execute(cmdTokens, registryManager, stack);
                } catch (ExecutionException ex) {
                    System.out.println(ex.getMessage());
                } catch (IllegalArgumentException ex) {
                    throw new CommandNotAvailableException("Command " + cmd + " is not available");
                }
            } else if (stack.peekLast() instanceof Component) {
                try {
                    command = ComponentCommand.valueOf(cmd);
                    command.execute(cmdTokens, componentManager, stack);
                } catch (ExecutionException ex) {
                    System.out.println(ex.getMessage());
                } catch (IllegalArgumentException ex) {
                    throw new CommandNotAvailableException("Command " + cmd + " is not available");
                }
            } else if(stack.peekLast() instanceof Flow){
                try {
                    command = FlowCommand.valueOf(cmd);
                    command.execute(cmdTokens, flowManager, stack);
                } catch (ExecutionException ex) {
                    System.out.println(ex.getMessage());
                } catch (IllegalArgumentException ex) {
                    throw new CommandNotAvailableException("Command " + cmd + " is not available");
                }
            } else if(stack.peekLast() instanceof ComponentHolder){
                try {
                    command = ComponentHolderCommand.valueOf(cmd);
                    command.execute(cmdTokens, componentHolderManager, stack);
                } catch (ExecutionException ex) {
                    System.out.println(ex.getMessage());
                } catch (IllegalArgumentException ex) {
                    throw new CommandNotAvailableException("Command " + cmd + " is not available");
                }
            } else if(stack.peekLast() instanceof DatasetDefinition){
                try {
                    command = DatasetCommand.valueOf(cmd);
                    command.execute(cmdTokens, datasetManager, stack);
                } catch (IllegalArgumentException ex) {
                    throw new CommandNotAvailableException("Command " + cmd + " is not available");
                } catch (ExecutionException ex) {
                    Logger.getLogger(CommandLineInterpreter.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else if(stack.peekLast() instanceof Senario){
                try {
                    command = SenarioCommand.valueOf(cmd);
                    command.execute(cmdTokens, senarioManager, stack);
                } catch (IllegalArgumentException ex) {
                    throw new CommandNotAvailableException("Command " + cmd + " is not available");
                } catch (ExecutionException ex) {
                    Logger.getLogger(CommandLineInterpreter.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (ExecutionException exception) {
            System.out.println(exception.getMessage());
        }
    }
    
    private String getPath(Deque<Object> stack){
        String result = "";
        
        for(Object element : stack){
            result = result+element.toString()+PATH_SEPARATOR;
        }
        
        return result+" ";
    }
    
    public static void main(String[] args){
        CommandLineInterpreter cli = new CommandLineInterpreter();
        cli.run();
    }    
}
