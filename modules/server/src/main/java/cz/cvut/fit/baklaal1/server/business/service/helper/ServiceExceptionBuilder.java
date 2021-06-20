package cz.cvut.fit.baklaal1.server.business.service.helper;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ServiceExceptionBuilder<T extends Exception> {
    private static final String DEFAULT = "Undefined";
    private static final String EXCEPTION = "SERVICE EXCEPTION";
    private static final String CREATE = "create";
    private static final String UPDATE = "update";
    private static final String DELIMITER = " ";

    private static final int exceptionSeqId = 0;
    private static final int serviceSeqId = 1;
    private static final int onSeqId = 2;
    private static final int problemSeqId = 3;
    private static final int objectSeqId = 4;
    private static final int fieldCnt = 5;
    private static Set<Integer> optionalFieldSeqIds = new HashSet<>();

    private String exceptionMessage;
    private Map<Integer, String> messageSequenceMap = new HashMap<>();

    public ServiceExceptionBuilder() {
        initialize();
    }

    private void initialize() {
        for (int i = 0; i < fieldCnt; i++) {
            messageSequenceMap.put(i, DEFAULT);
        }

        optionalFieldSeqIds.add(5);
    }

    public ServiceExceptionBuilder exception() {
        messageSequenceMap.put(exceptionSeqId, EXCEPTION);
        return this;
    }

    public ServiceExceptionBuilder inService(String serviceName) {
        String inService = "in " + serviceName;
        messageSequenceMap.put(serviceSeqId, inService);
        return this;
    }

    public ServiceExceptionBuilder onCreate() {
        return onAction(CREATE);
    }

    public ServiceExceptionBuilder onUpdate() {
        return onAction(UPDATE);
    }

    public ServiceExceptionBuilder onAction(String actionName) {
        String onAction = "on " + actionName + " action";
        messageSequenceMap.put(onSeqId, onAction);
        return this;
    }

    public ServiceExceptionBuilder causedBy(String problemDescription) {
        String problem = ", caused by problem: " + problemDescription;
        messageSequenceMap.put(problemSeqId, problem);
        return this;
    }

    public ServiceExceptionBuilder relatedToObject(Object object) {
        String obj = ", related to object: " + object.toString();
        messageSequenceMap.put(objectSeqId, obj);
        return this;
    }

    public ServiceExceptionInBusinessLogic build() {
        String message = buildMessage();
        ServiceExceptionInBusinessLogic exception = new ServiceExceptionInBusinessLogic(message);
        return exception;
    }

    private String buildMessage() {
        String message = "";
        for (int i = 0; i < fieldCnt; i++) {
            if(optionalFieldSeqIds.contains(i) && messageSequenceMap.get(i).equals(DEFAULT)) {
                continue;
            }
            if(i > 0) {
                message += DELIMITER;
            }
            message += messageSequenceMap.get(i);
        }
        return message;
    }
}
