package cz.cvut.fit.baklaal1.client.handler;

import cz.cvut.fit.baklaal1.client.resource.BasicResource;
import org.springframework.boot.ApplicationArguments;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import java.net.URI;
import java.util.List;

public abstract class BasicHandler<T_DTO extends RepresentationModel<T_DTO>, T_CREATE_DTO> {
    private final BasicResource<T_DTO, T_CREATE_DTO> resource;

    public BasicHandler(BasicResource<T_DTO, T_CREATE_DTO> resource) {
        this.resource = resource;
    }

    protected abstract void printModel(T_DTO model);

    private void read(ApplicationArguments args) {
        if (!args.containsOption("id")) {
            System.err.println("missing");
            return;
        }
        String id = args.getOptionValues("id").get(0);
        try {
            T_DTO studentDTO = resource.read(id);
            printModel(studentDTO);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().equals(HttpStatus.NOT_FOUND))
                System.err.println("Not found");
        }
    }

    protected abstract boolean checkArgumentsForCreateModel(ApplicationArguments args);

    protected abstract T_CREATE_DTO makeCreateModelFromArguments(ApplicationArguments args);

    protected abstract String getIdFromArguments(ApplicationArguments args);

    private void update(ApplicationArguments args) {
        if(!checkArgumentsForCreateModel(args)) {
            return;
        }

        String id = getIdFromArguments(args);
        T_CREATE_DTO createDto = makeCreateModelFromArguments(args);
        resource.update(id, createDto);
    }

    private void create(ApplicationArguments args) {
        if(!checkArgumentsForCreateModel(args)) {
            return;
        }

        T_CREATE_DTO createDto = makeCreateModelFromArguments(args);
        try {
            URI uri = resource.create(createDto);
            System.out.println(uri);
        } catch (HttpClientErrorException e) {
            if(e.getStatusCode() == HttpStatus.CONFLICT) {
                System.err.println("Record\n" + createDto + "\nalready exists!");
            }
            //TODO other codes
        }
    }

    protected abstract void printPagedModel(T_DTO model);

    private void pageAll(ApplicationArguments args) {
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

    private void readAll(ApplicationArguments args) {
        List<T_DTO> items = resource.readAll();
        for(T_DTO item : items) {
            printModel(item);
            System.out.println("\n");
        }
    }

    private void delete(ApplicationArguments args) {
        String id = getIdFromArguments(args);
        resource.delete(id);
    }

    public void handle(ApplicationArguments args) {
        switch (args.getOptionValues("action").get(0)) {
            case "read": {
                read(args);
            }
            break;
            case "update": {
                update(args);
            }
            break;
            case "create": {
                create(args);
            }
            break;
            case "pageAll": {
                pageAll(args);
            }
            break;
            case "readAll": {
                readAll(args);
            }
            break;
            case "delete": {
                delete(args);
            }
        }
    }
}
