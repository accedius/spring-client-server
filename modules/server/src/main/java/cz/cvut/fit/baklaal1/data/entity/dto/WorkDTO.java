package cz.cvut.fit.baklaal1.data.entity.dto;

import cz.cvut.fit.baklaal1.data.entity.Student;
import cz.cvut.fit.baklaal1.data.entity.Work;
import org.springframework.hateoas.RepresentationModel;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class WorkDTO extends RepresentationModel<WorkDTO> implements ReadableId {
    private final int id;
    private final String title;
    private final String text;
    private final Set<Integer> authorIds;
    private final Integer assessmentId;

    public WorkDTO(int id, String title, String text, Set<Integer> authorIds, Integer assessmentId) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.authorIds = authorIds;
        this.assessmentId = assessmentId;
    }

    public WorkDTO(final Work work) {
        this.id = work.getId() == null ? -1 : work.getId();
        this.title = work.getTitle();
        this.text = work.getText();
        Set<Student> authors = work.getAuthors();
        this.authorIds = authors.stream().map(Student::getId).collect(Collectors.toSet());
        this.assessmentId = work.getAssessment() != null ? work.getAssessment().getId() : null;
    }

    public int getId() {
        return id;
    }

    @Override
    public int readId() {
        return getId();
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public Set<Integer> getAuthorIds() {
        return authorIds;
    }

    public Integer getAssessmentId() {
        return assessmentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorkDTO workDTO = (WorkDTO) o;
        if(id != -1 && workDTO.id != -1 && id != workDTO.id) return false;
        return title.equals(workDTO.title) &&
                text.equals(workDTO.text) &&
                authorIds.equals(workDTO.authorIds) &&
                Objects.equals(assessmentId, workDTO.assessmentId);
    }
}
