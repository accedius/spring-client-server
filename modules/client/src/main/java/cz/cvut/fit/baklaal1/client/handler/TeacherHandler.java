package cz.cvut.fit.baklaal1.client.handler;

import cz.cvut.fit.baklaal1.client.handler.helper.ArgumentConstants;
import cz.cvut.fit.baklaal1.client.resource.TeacherResource;
import cz.cvut.fit.baklaal1.model.data.entity.dto.TeacherCreateDTO;
import cz.cvut.fit.baklaal1.model.data.entity.dto.TeacherDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Component
public class TeacherHandler extends PersonHandler<TeacherDTO, TeacherCreateDTO> {
    private static final String BIRTHDATE = ArgumentConstants.BIRTHDATE;
    private static final String WAGE = ArgumentConstants.WAGE;
    private static final String ASSESSMENT_IDS = ArgumentConstants.ASSESSMENT_IDS;

    @Autowired
    private final TeacherResource teacherResource;

    public TeacherHandler(TeacherResource teacherResource) {
        super(teacherResource);
        this.teacherResource = teacherResource;
    }

    @Override
    public boolean handle(ApplicationArguments args) throws Exception {
        boolean wasHandled = super.handle(args);
        if(wasHandled) {
            return wasHandled;
        }

        String action = args.getOptionValues("action").get(0);
        switch (action) {
            default: {
                throw new IllegalArgumentException("No such action for Teacher: \"" + action + "\"!");
            }
        }
    }

    @Override
    protected TeacherCreateDTO makeCreateModelFromArguments(ApplicationArguments args) throws Exception {
        if(!args.containsOption(USERNAME) || !args.containsOption(NAME)) {
            throwMustContain(USERNAME, NAME);
        }

        String username = args.getOptionValues(USERNAME).get(0);
        String name = args.getOptionValues(NAME).get(0);
        Timestamp birthdate = args.getOptionValues(BIRTHDATE) != null ? new Timestamp(Long.parseLong(args.getOptionValues(BIRTHDATE).get(0))) : null;

        String wageString = args.getOptionValues(WAGE) != null ? args.getOptionValues(WAGE).get(0) : "0";
        //TODO Number locales are hell
        wageString = wageString.replaceAll(",", ".");
        double wage = Double.parseDouble(wageString);

        Set<Integer> assessmentIds = args.getOptionValues(ASSESSMENT_IDS) != null ? args.getOptionValues(ASSESSMENT_IDS).stream().map(Integer::parseInt).distinct().collect(Collectors.toCollection(TreeSet::new)) : new TreeSet<>();
        return new TeacherCreateDTO(username, name, birthdate, wage, assessmentIds);
    }
}
