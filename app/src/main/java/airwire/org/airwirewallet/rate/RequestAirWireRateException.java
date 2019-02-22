package airwire.org.airwirewallet.rate;

/**
 * Created by akshaynexus on 7/5/17.
 */
public class RequestAirWireRateException extends Exception {
    public RequestAirWireRateException(String message) {
        super(message);
    }

    public RequestAirWireRateException(String message, Throwable cause) {
        super(message, cause);
    }

    public RequestAirWireRateException(Exception e) {
        super(e);
    }
}
