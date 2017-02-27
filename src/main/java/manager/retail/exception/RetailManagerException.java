package manager.retail.exception;

import org.springframework.http.HttpStatus;

import javax.validation.constraints.NotNull;

/**
 * @CreatedBy Hasan on 25-Feb-2017
 */
public class RetailManagerException extends RuntimeException {

    @NotNull
    private HttpStatus httpStatusCode;

    public RetailManagerException(Exception e, String message, HttpStatus httpStatusCode) {
        super(message, e);
        this.httpStatusCode = httpStatusCode;
    }

    public RetailManagerException(String message, HttpStatus httpStatusCode) {
        super(message);
        this.httpStatusCode = httpStatusCode;
    }

    public HttpStatus getHttpStatusCode() {
        return httpStatusCode;
    }
}
