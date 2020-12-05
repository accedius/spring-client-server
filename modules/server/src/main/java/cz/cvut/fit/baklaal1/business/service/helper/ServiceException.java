package cz.cvut.fit.baklaal1.business.service.helper;

public class ServiceException extends RuntimeException {
    public ServiceException() {
    }

    public ServiceException(String message) {
        super(message);
    }
}
