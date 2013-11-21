/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package exception;

/**
 *
 * @author Meng
 */
public class ExecutionException extends Exception {

    /**
     * Creates a new instance of
     * <code>ExecutionException</code> without detail message.
     */
    public ExecutionException() {
    }

    /**
     * Constructs an instance of
     * <code>ExecutionException</code> with the specified detail message.
     *
     * @param msg the detail message.
     */
    public ExecutionException(String msg) {
        super(msg);
    }
}
