package cz.cvut.fit.baklaal1.business.service.helper;

public class ServiceExceptionEntityAlreadyExists extends IllegalAccessException {
    public ServiceExceptionEntityAlreadyExists() {}

    public ServiceExceptionEntityAlreadyExists(String s) {
        super(s);
    }
}
