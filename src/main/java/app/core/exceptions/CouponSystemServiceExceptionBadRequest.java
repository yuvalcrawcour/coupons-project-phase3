package app.core.exceptions;

public class CouponSystemServiceExceptionBadRequest extends CouponSystemServiceException{
    public CouponSystemServiceExceptionBadRequest() {
    }

    public CouponSystemServiceExceptionBadRequest(String message) {
        super(message);
    }

    public CouponSystemServiceExceptionBadRequest(String message, Throwable cause) {
        super(message, cause);
    }

    public CouponSystemServiceExceptionBadRequest(Throwable cause) {
        super(cause);
    }

    public CouponSystemServiceExceptionBadRequest(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
