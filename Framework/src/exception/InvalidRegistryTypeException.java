/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package exception;

/**
 *
 * @author Meng
 */
public class InvalidRegistryTypeException extends Exception {

    /**
     * Creates a new instance of
     * <code>UnsupportedRegistryTypeException</code> without detail message.
     */
    public InvalidRegistryTypeException() {
    }

    /**
     * Constructs an instance of
     * <code>UnsupportedRegistryTypeException</code> with the specified detail
     * message.
     *
     * @param msg the detail message.
     */
    public InvalidRegistryTypeException(String msg) {
        super(msg);
    }
}
