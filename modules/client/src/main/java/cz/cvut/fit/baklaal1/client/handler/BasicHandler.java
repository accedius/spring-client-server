package cz.cvut.fit.baklaal1.client.handler;

import cz.cvut.fit.baklaal1.client.handler.helper.ArgumentConstants;
import cz.cvut.fit.baklaal1.client.resource.BasicResource;
import cz.cvut.fit.baklaal1.model.data.entity.dto.Printable;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.boot.ApplicationArguments;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public abstract class BasicHandler<T_DTO extends Printable, T_CREATE_DTO> {
    protected static final String CREATE = ArgumentConstants.CREATE;
    protected static final String READ = ArgumentConstants.READ;
    protected static final String UPDATE = ArgumentConstants.UPDATE;
    protected static final String DELETE = ArgumentConstants.DELETE;
    protected static final String READ_ALL = ArgumentConstants.READ_ALL;
    protected static final String PAGE_ALL = ArgumentConstants.PAGE_ALL;

    private static final String ID = ArgumentConstants.ID;

    private final BasicResource<T_DTO, T_CREATE_DTO> resource;

    public BasicHandler(BasicResource<T_DTO, T_CREATE_DTO> resource) {
        this.resource = resource;
    }

    protected void printModel(T_DTO model) {
        model.print();
    }

    protected void printPagedModel(T_DTO model) {
        model.printAsPaged();
    }

    private void read(ApplicationArguments args) throws Exception {
        String id = getIdFromArguments(args);

        try {
            T_DTO studentDTO = resource.read(id);
            printModel(studentDTO);
        } catch (HttpClientErrorException e) {
            switch (e.getStatusCode()) {
                case NOT_FOUND: {
                    throw new RuntimeException("Record with id =" + id + " not found!", e);
                }
                //TODO other codes
                default: {
                    throw e;
                }
            }
        }
    }

    protected abstract T_CREATE_DTO makeCreateModelFromArguments(ApplicationArguments args) throws Exception;

    protected String getIdFromArguments(ApplicationArguments args) throws Exception {
        if(!args.containsOption(ID)) {
            throwMustContain(ID);
        }

        String id = args.getOptionValues(ID).get(0);
        return id;
    }

    private void update(ApplicationArguments args) throws Exception {
        String id = getIdFromArguments(args);
        T_CREATE_DTO createDto = makeCreateModelFromArguments(args);
        try {
            resource.update(id, createDto);
        } catch (HttpClientErrorException e) {
            switch (e.getStatusCode()) {
                case CONFLICT: {
                    throw new RuntimeException("Record update with dto:\n" + createDto + "\nis in conflict!", e);
                }
                case BAD_REQUEST: {
                    throw new RuntimeException("Bad request for update with dto:\n" + createDto + "\n!", e);
                }
                //TODO other codes
                default: {
                    throw e;
                }
            }
        }
    }

    private void create(ApplicationArguments args) throws Exception {
        T_CREATE_DTO createDto = makeCreateModelFromArguments(args);
        try {
            URI uri = resource.create(createDto);
            System.out.println(uri);
        } catch (HttpClientErrorException e) {
            switch (e.getStatusCode()) {
                case CONFLICT: {
                    throw new RuntimeException("Record equal to given dto:\n" + createDto + "\nalready exists!", e);
                }
                case BAD_REQUEST: {
                    throw new RuntimeException("Bad request for creation with dto:\n" + createDto + "\n!", e);
                }
                //TODO other codes
                default: {
                    throw e;
                }
            }
        }
    }

    private void pageAll() {
        final int size = 2;
        int page = 0;
        PagedModel<T_DTO> pages;
        do {
            pages = resource.pageAll(page, size);
            for (T_DTO item : pages.getContent()) {
                printPagedModel(item);
            }
            page++;
        } while (pages.hasLink("next"));
    }

    protected void printAll(Collection<T_DTO> collection) {
        for(T_DTO item : collection) {
            printModel(item);
            System.out.println("\n");
        }
    }

    private void readAll() {
        List<T_DTO> items = resource.readAll();
        printAll(items);
    }

    private void delete(ApplicationArguments args) throws Exception {
        String id = getIdFromArguments(args);
        resource.delete(id);
    }

    protected void printError(final Exception e, final String actionName, final ApplicationArguments args) {
        System.err.println("Error on action \"" + actionName + "\" with given args: \"" + args.toString() + "\"!");
        System.err.println("Exception Message: " + e.getMessage());
        System.out.println(ExceptionUtils.getStackTrace(e));
    }

    protected void throwMustContain(String... optionNames) throws IllegalArgumentException {
        String message = "Arguments must contain following options:\n";
        for (String optionName : optionNames) {
            message += "\"" + optionName + "\"\n";
        }
        message = message.substring(0, message.length() - 1);
        throw new IllegalArgumentException(message);
    }

    public boolean handle(ApplicationArguments args) throws Exception {
        boolean wasHandled = false;
        String action = args.getOptionValues("action").get(0);
        switch (action) {
            case READ: {
                wasHandled = true;
                try {
                    read(args);
                } catch (Exception e) {
                    printError(e, READ, args);
                }
                break;
            }
            case UPDATE: {
                wasHandled = true;
                try {
                    update(args);
                } catch (Exception e) {
                    printError(e, UPDATE, args);
                }
                break;
            }
            case CREATE: {
                wasHandled = true;
                try {
                    create(args);
                } catch (Exception e) {
                    printError(e, CREATE, args);
                }
                break;
            }
            case PAGE_ALL: {
                wasHandled = true;
                try {
                    pageAll();
                } catch (Exception e) {
                    printError(e, PAGE_ALL, args);
                }
                break;
            }
            case READ_ALL: {
                wasHandled = true;
                try {
                    readAll();
                } catch (Exception e) {
                    printError(e, READ_ALL, args);
                }
                break;
            }
            case DELETE: {
                wasHandled = true;
                try {
                    delete(args);
                } catch (Exception e) {
                    printError(e, DELETE, args);
                }
                break;
            }
        }

        return wasHandled;
    }
}
