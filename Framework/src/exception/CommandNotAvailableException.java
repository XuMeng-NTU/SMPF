/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package exception;

/**
 *
 * @author Meng
 */
public class CommandNotAvailableException extends Exception {

    /**
     * Creates a new instance of
     * <code>OptionNotAvailableException</code> without detail message.
     */
    public CommandNotAvailableException() {
    }

    /**
     * Constructs an instance of
     * <code>OptionNotAvailableException</code> with the specified detail
     * message.
     *
     * @param msg the detail message.
     */
    public CommandNotAvailableException(String msg) {
        super(msg);
    }
}
