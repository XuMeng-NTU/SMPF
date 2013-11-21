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
import java.util.logging.Level;
import java.util.logging.Logger;
import manager.flow.FlowManager;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import persistence.flow.Flow;

/**
 *
 * @author Meng
 */
public enum FlowCommand implements Command {
    
    LIST(new Options()
            .addOption("t","type",true,"type of instance")),
    ADD(new Options()
            .addOption("n","name",true,"name of node")),
    REMOVE(new Options()
            .addOption("n","name",true,"name of node")),
    LINK(new Options()
            .addOption("u","upstream",true,"upstream node")
            .addOption("d","downstream",true,"downstream node")),
    UNLINK(new Options()
            .addOption("u","upstream",true,"upstream node")
            .addOption("d","downstream",true,"downstream node")),
    VIEW(new Options()
            .addOption("t","type",true,"type of instance")
            .addOption("n","name",true,"name of node")
            .addOption("u","upstream",true,"upstream node")
            .addOption("d","downstream",true,"downstream node"));        
    
    private Options options;
    
    FlowCommand(Options options){
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
        FlowManager flowManager = (FlowManager) manager;
        
        Flow current = (Flow) stack.getLast();
        
        switch(this){
            case ADD:
              try {
                    String nodeName = commandLine.getOptionValue("n");
                    flowManager.addNode(current, nodeName);
                } catch (NullPointerException ex) {
                    throw new ParseException("One or more option value missing");
                }                
                break;                
            case REMOVE:
              try {
                    String nodeName = commandLine.getOptionValue("n");
                    flowManager.removeNode(current, nodeName);
                } catch (NullPointerException ex) {
                    throw new ParseException("One or more option value missing");
                } catch (NoSuchInstanceException ex) {
                    throw new ExecutionException(ex.getMessage());
                }
                break;       
            case LINK:
              try {
                    String unodeName = commandLine.getOptionValue("u");
                    String dnodeName = commandLine.getOptionValue("d");
                    flowManager.addEdge(current, unodeName, dnodeName);
                } catch (NullPointerException ex) {
                    throw new ParseException("One or more option value missing");
                } catch (NoSuchInstanceException ex) {
                    throw new ExecutionException(ex.getMessage());
                }
                break;                  
            case UNLINK:
              try {
                    String unodeName = commandLine.getOptionValue("u");
                    String dnodeName = commandLine.getOptionValue("d");
                    flowManager.removeEdge(current, unodeName, dnodeName);
                } catch (NullPointerException ex) {
                    throw new ParseException("One or more option value missing");
                } catch (NoSuchInstanceException ex) {
                    throw new ExecutionException(ex.getMessage());
                }
                break;
            case VIEW:
                try {
                    if (commandLine.hasOption("n")) {
                        String nodeName = commandLine.getOptionValue("n");
                        stack.addLast(flowManager.getNodeByName(current, nodeName));
                    } else if (commandLine.hasOption("u") && commandLine.hasOption("d")) {
                        String unodeName = commandLine.getOptionValue("u");
                        String dnodeName = commandLine.getOptionValue("d");
                        stack.addLast(flowManager.getEdgeByConnection(current, unodeName, dnodeName));
                    } else{
                        throw new ParseException("One or more option value missing");
                    }
                } catch (NoSuchInstanceException ex) {
                    Logger.getLogger(FlowCommand.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;          
            case LIST:
                flowManager.printFlow(current);
                break;
        }

    }
    
}
