package app.core.exceptions;

public class CouponSystemServiceException extends CouponSystemException{
    public CouponSystemServiceException() {
    }

    public CouponSystemServiceException(String message) {
        super(message);
    }

    public CouponSystemServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public CouponSystemServiceException(Throwable cause) {
        super(cause);
    }

    public CouponSystemServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
