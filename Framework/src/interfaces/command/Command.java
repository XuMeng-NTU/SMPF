/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces.command;

import exception.ExecutionException;
import java.util.Deque;
import interfaces.manager.Manager;
import org.apache.commons.cli.ParseException;

/**
 *
 * @author Meng
 */
public interface Command {
    public void execute(String[] args, Manager manager, Deque<Object> stack) throws ParseException, ExecutionException;
}
