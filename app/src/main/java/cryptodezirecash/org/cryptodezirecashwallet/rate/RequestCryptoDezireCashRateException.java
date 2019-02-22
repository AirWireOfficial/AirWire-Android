package cryptodezirecash.org.cryptodezirecashwallet.rate;

/**
 * Created by akshaynexus on 7/5/17.
 */
public class RequestCryptoDezireCashRateException extends Exception {
    public RequestCryptoDezireCashRateException(String message) {
        super(message);
    }

    public RequestCryptoDezireCashRateException(String message, Throwable cause) {
        super(message, cause);
    }

    public RequestCryptoDezireCashRateException(Exception e) {
        super(e);
    }
}
