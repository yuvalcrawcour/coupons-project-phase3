package app.core.exceptions;

public class CouponSystemServiceExceptionUnauthorized extends CouponSystemServiceException{
    public CouponSystemServiceExceptionUnauthorized() {
    }

    public CouponSystemServiceExceptionUnauthorized(String message) {
        super(message);
    }

    public CouponSystemServiceExceptionUnauthorized(String message, Throwable cause) {
        super(message, cause);
    }

    public CouponSystemServiceExceptionUnauthorized(Throwable cause) {
        super(cause);
    }

    public CouponSystemServiceExceptionUnauthorized(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
