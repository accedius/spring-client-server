package cz.cvut.fit.baklaal1.data.entity.dto;

import java.util.List;
import java.util.Optional;

public class WorkCreateDTO {
    private final String title;
    private final String text;
    private final List<Integer> authorIds;
    private final Integer assessmentId;

    public WorkCreateDTO(String title, String text, List<Integer> authorIds, Integer assessmentId) {
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

    public List<Integer> getAuthorIds() {
        return authorIds;
    }

    public Integer getAssessmentId() {
        return assessmentId;
    }
}
