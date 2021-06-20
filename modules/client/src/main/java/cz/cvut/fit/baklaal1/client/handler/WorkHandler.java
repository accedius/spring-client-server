package cz.cvut.fit.baklaal1.client.handler;

import cz.cvut.fit.baklaal1.client.handler.helper.ArgumentConstants;
import cz.cvut.fit.baklaal1.client.resource.WorkResource;
import cz.cvut.fit.baklaal1.model.data.entity.dto.WorkCreateDTO;
import cz.cvut.fit.baklaal1.model.data.entity.dto.WorkDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Component
public class WorkHandler extends BasicHandler<WorkDTO, WorkCreateDTO> {
    private static final String TITLE = ArgumentConstants.TITLE;
    private static final String READ_ALL_BY_TITLE = ArgumentConstants.READ_ALL_BY_TITLE;
    private static final String AUTHOR_IDS = ArgumentConstants.AUTHOR_IDS;
    private static final String TEXT = ArgumentConstants.TEXT;
    private static final String ASSESSMENT_ID = ArgumentConstants.ASSESSMENT_ID;

    @Autowired
    private final WorkResource workResource;

    public WorkHandler(WorkResource workResource) {
        super(workResource);
        this.workResource = workResource;
    }

    @Override
    public boolean handle(ApplicationArguments args) throws Exception {
        boolean wasHandled = super.handle(args);
        if(wasHandled) {
            return wasHandled;
        }

        String action = args.getOptionValues("action").get(0);
        switch (action) {
            case READ_ALL_BY_TITLE: {
                wasHandled = true;
                try {
                    readAllByTitle(args);
                } catch (Exception e) {
                    printError(e, READ_ALL_BY_TITLE, args);
                }
                break;
            }
            default: {
                throw new IllegalArgumentException("No such action for Work: \"" + action + "\"!");
            }
        }

        return wasHandled;
    }

    private void readAllByTitle(ApplicationArguments args) {
        if(!args.containsOption(TITLE)) {
            throwMustContain(TITLE);
        }

        String title = args.getOptionValues(TITLE).get(0);
        Set<WorkDTO> works = workResource.readAllByTitle(title);
        printAll(works);
    }

    @Override
    protected WorkCreateDTO makeCreateModelFromArguments(ApplicationArguments args) throws Exception {
        if(!args.containsOption(TITLE) || !args.containsOption(AUTHOR_IDS)) {
            throwMustContain(TITLE, AUTHOR_IDS);
        }

        String title = args.getOptionValues(TITLE).get(0);
        String text = args.getOptionValues(TEXT) != null ? args.getOptionValues(TEXT).get(0) : null;
        Set<Integer> authorIds = args.getOptionValues(AUTHOR_IDS).stream().map(Integer::parseInt).distinct().collect(Collectors.toCollection(TreeSet::new));
        Integer assessmentId = args.getOptionValues(ASSESSMENT_ID) != null ? Integer.parseInt(args.getOptionValues(ASSESSMENT_ID).get(0)) : null;
        return new WorkCreateDTO(title, text, authorIds, assessmentId);
    }
}
