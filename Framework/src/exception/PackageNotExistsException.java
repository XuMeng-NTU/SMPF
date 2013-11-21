/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package exception;

/**
 *
 * @author Meng
 */
public class PackageNotExistsException extends Exception {

    /**
     * Creates a new instance of
     * <code>PackageNotExistException</code> without detail message.
     */
    public PackageNotExistsException() {
    }

    /**
     * Constructs an instance of
     * <code>PackageNotExistException</code> with the specified detail message.
     *
     * @param msg the detail message.
     */
    public PackageNotExistsException(String msg) {
        super(msg);
    }
}
