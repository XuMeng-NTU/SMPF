/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package exception;

/**
 *
 * @author Meng
 */
public class NoSuchInstanceException extends Exception {

    /**
     * Creates a new instance of
     * <code>NoSuchInstanceException</code> without detail message.
     */
    public NoSuchInstanceException() {
    }

    /**
     * Constructs an instance of
     * <code>NoSuchInstanceException</code> with the specified detail message.
     *
     * @param msg the detail message.
     */
    public NoSuchInstanceException(String msg) {
        super(msg);
    }
}
