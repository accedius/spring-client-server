package cz.cvut.fit.baklaal1.client.handler;

import cz.cvut.fit.baklaal1.client.resource.TeacherResource;
import cz.cvut.fit.baklaal1.model.data.entity.dto.TeacherCreateDTO;
import cz.cvut.fit.baklaal1.model.data.entity.dto.TeacherDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

@Component
public class TeacherHandler extends PersonHandler<TeacherDTO, TeacherCreateDTO> {
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
    protected TeacherCreateDTO makeCreateModelFromArguments(ApplicationArguments args) {
        return null;
    }
}
