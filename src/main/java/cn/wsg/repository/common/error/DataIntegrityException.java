package cn.wsg.repository.common.error;

/**
 * Exceptions thrown when data are lacking.
 *
 * @author Kingen
 */
public class DataIntegrityException extends Exception {

    private static final long serialVersionUID = -357783039394404365L;

    public DataIntegrityException(String message) {
        super(message);
    }
}
