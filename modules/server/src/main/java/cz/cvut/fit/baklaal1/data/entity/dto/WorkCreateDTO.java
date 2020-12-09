package cz.cvut.fit.baklaal1.data.entity.dto;

import cz.cvut.fit.baklaal1.data.entity.Student;
import cz.cvut.fit.baklaal1.data.entity.Work;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class WorkCreateDTO {
    private final String title;
    private final String text;
    private final Set<Integer> authorIds;
    private final Integer assessmentId;

    public WorkCreateDTO(String title, String text, Set<Integer> authorIds, Integer assessmentId) {
        this.title = title;
        this.text = text;
        this.authorIds = authorIds;
        this.assessmentId = assessmentId;
    }

    public WorkCreateDTO(final Work work) {
        this.title = work.getTitle();
        this.text = work.getText();
        Set<Student> authors = work.getAuthors();
        this.authorIds = authors.stream().map(Student::getId).collect(Collectors.toSet());
        this.assessmentId = work.getAssessment() != null ? work.getAssessment().getId() : null;
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
        if (!(o instanceof WorkCreateDTO)) return false;
        WorkCreateDTO that = (WorkCreateDTO) o;
        return title.equals(that.title) &&
                Objects.equals(text, that.text) &&
                authorIds.equals(that.authorIds) &&
                Objects.equals(assessmentId, that.assessmentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, text, authorIds, assessmentId);
    }
}
