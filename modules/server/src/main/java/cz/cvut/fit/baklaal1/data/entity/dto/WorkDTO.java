package cz.cvut.fit.baklaal1.data.entity.dto;

import cz.cvut.fit.baklaal1.data.entity.Student;
import cz.cvut.fit.baklaal1.data.entity.Work;

import java.util.List;
import java.util.stream.Collectors;

public class WorkDTO {
    private final int id;
    private final String title;
    private final String text;
    private final List<Integer> authorIds;
    private final Integer assessmentId;

    public WorkDTO(int id, String title, String text, List<Integer> authorIds, Integer assessmentId) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.authorIds = authorIds;
        this.assessmentId = assessmentId;
    }

    public WorkDTO(Work work) {
        this.id = work.getId();
        this.title = work.getTitle();
        this.text = work.getText();
        List<Student> authors = work.getAuthors();
        this.authorIds = authors.stream().map(Student::getId).collect(Collectors.toList());
        this.assessmentId = work.getAssessment() != null ? work.getAssessment().getId() : null;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public List<Integer> getAuthorIds() {
        return authorIds;
    }

    public Integer getAssessmentId() {
        return assessmentId;
    }
}
