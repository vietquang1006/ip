package perlica.exception;

/**
 * Represents custom exceptions specific to the Perlica application.
 * This exception is thrown when there are errors parsing commands, incorrect
 * formatting, or invalid operations.
 */
public class PerlicaException extends Exception {

    /**
     * Constructs a {@code PerlicaException} with the specified detail message.
     *
     * @param msg The detail message explaining the reason for the exception.
     */
    public PerlicaException(String msg) {
        super(msg);
    }
}
