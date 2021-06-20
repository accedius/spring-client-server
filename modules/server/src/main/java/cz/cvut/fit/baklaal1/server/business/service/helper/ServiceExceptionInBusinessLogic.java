package cz.cvut.fit.baklaal1.server.business.service.helper;

public class ServiceExceptionInBusinessLogic extends RuntimeException {
    public ServiceExceptionInBusinessLogic() {}

    public ServiceExceptionInBusinessLogic(String message) {
        super(message);
    }
}
