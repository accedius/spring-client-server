package cz.cvut.fit.baklaal1.model.data.entity.dto;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class WorkCreateDTO {
    private String title;
    private String text;
    private Set<Integer> authorIds;
    private Integer assessmentId;

    public WorkCreateDTO(String title, String text, Set<Integer> authorIds, Integer assessmentId) {
        this.title = title;
        this.text = text;
        this.authorIds = authorIds;
        this.assessmentId = assessmentId;
    }

    public WorkCreateDTO() {
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
