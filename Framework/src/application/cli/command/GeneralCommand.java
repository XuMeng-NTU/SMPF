/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package application.cli.command;

import dataset.Dataset;
import interfaces.command.Command;
import exception.ExecutionException;
import java.util.Arrays;
import java.util.Deque;
import interfaces.manager.Manager;
import java.util.List;
import manager.general.GeneralManager;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import persistence.senario.Senario;

/**
 *
 * @author Meng
 */
public enum GeneralCommand implements Command {
    BACK(new Options()),
    SCAN(new Options()),
    RUN(new Options()
            .addOption("n","name",true,"name of senario")
            ),
    RECALL(new Options()
        .addOption("m","memory",true,"label for object in memory")
        );
    
    private Options options;
    
    GeneralCommand(Options options){
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
        
        GeneralManager generalManager = (GeneralManager) manager;
        CommandLine commandLine = parseOptions(args);
        
        String name;
        String label;
        
        switch(this){
            case BACK:
                stack.removeLast();
                break;
            case SCAN:
                List<Senario> senarioList = generalManager.scanForCompleteSenarios();
                for(Senario senario : senarioList){
                    System.out.println(senario.getName());
                }
                break;
            case RECALL:
                try {
                    label = commandLine.getOptionValue("m");
                    Object object = generalManager.recall(label);
                    if(object instanceof Dataset){
                        ((Dataset)object).print();
                    }
                } catch (NullPointerException ex) {
                    throw new ParseException("One or more option value missing");
                }
        }
    }
    
}
