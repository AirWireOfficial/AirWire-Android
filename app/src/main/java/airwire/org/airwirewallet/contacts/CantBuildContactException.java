package airwire.org.airwirewallet.contacts;

/**
 * Created by akshaynexus on 7/1/17.
 */
public class CantBuildContactException extends RuntimeException {
    public CantBuildContactException(Exception e) {
        super(e);
    }
}
