package cz.cvut.fit.baklaal1.client.handler;

import cz.cvut.fit.baklaal1.client.handler.helper.ArgumentConstants;
import cz.cvut.fit.baklaal1.client.resource.BasicResource;
import cz.cvut.fit.baklaal1.client.resource.PersonResource;
import cz.cvut.fit.baklaal1.model.data.entity.dto.Printable;
import org.springframework.boot.ApplicationArguments;

import java.util.Set;

public abstract class PersonHandler<T_DTO extends Printable, T_CREATE_DTO> extends BasicHandler<T_DTO, T_CREATE_DTO> {
    private static final String READ_BY_USERNAME = ArgumentConstants.READ_BY_USERNAME;
    private static final String USERNAME = ArgumentConstants.USERNAME;

    private static final String READ_ALL_BY_NAME = ArgumentConstants.READ_ALL_BY_NAME;
    private static final String NAME = ArgumentConstants.NAME;

    private final PersonResource<T_DTO, T_CREATE_DTO> personResource;

    public PersonHandler(PersonResource<T_DTO, T_CREATE_DTO> resource) {
        super(resource);
        personResource = resource;
    }

    @Override
    public boolean handle(ApplicationArguments args) throws Exception {
        boolean wasHandled = super.handle(args);
        if(wasHandled) {
            return wasHandled;
        }

        String action = args.getOptionValues("action").get(0);
        switch (action) {
            case READ_BY_USERNAME: {
                wasHandled = true;
                try {
                    readByUsername(args);
                } catch (Exception e) {
                    printError(e, READ_BY_USERNAME, args);
                }
                break;
            }
            case READ_ALL_BY_NAME: {
                wasHandled = true;
                try {
                    readAllByName(args);
                } catch (Exception e) {
                    printError(e, READ_ALL_BY_NAME, args);
                }
            }
        }
        return wasHandled;
    }

    private void readByUsername(ApplicationArguments args) throws Exception {
        if(!args.containsOption(USERNAME)) {
            throwMustContain(USERNAME);
        }

        String username = args.getOptionValues(USERNAME).get(0);
        T_DTO person = personResource.readByUsername(username);
        printModel(person);
    }

    private void readAllByName(ApplicationArguments args) throws IllegalArgumentException {
        if(!args.containsOption(NAME)) {
            throwMustContain(NAME);
        }

        String name = args.getOptionValues(NAME).get(0);
        Set<T_DTO> persons = personResource.readAllByName(name);
        printAll(persons);
    }
}