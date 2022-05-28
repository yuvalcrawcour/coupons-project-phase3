package app.core.exceptions;

public class CouponSystemServiceExceptionNotFound extends CouponSystemServiceException{
    public CouponSystemServiceExceptionNotFound() {
    }

    public CouponSystemServiceExceptionNotFound(String message) {
        super(message);
    }

    public CouponSystemServiceExceptionNotFound(String message, Throwable cause) {
        super(message, cause);
    }

    public CouponSystemServiceExceptionNotFound(Throwable cause) {
        super(cause);
    }

    public CouponSystemServiceExceptionNotFound(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
