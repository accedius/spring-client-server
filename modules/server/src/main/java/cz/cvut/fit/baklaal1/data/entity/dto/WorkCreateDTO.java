package cz.cvut.fit.baklaal1.data.entity.dto;

import java.util.Set;

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
}
