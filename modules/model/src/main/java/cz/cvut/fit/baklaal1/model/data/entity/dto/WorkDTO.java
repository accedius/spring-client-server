package cz.cvut.fit.baklaal1.model.data.entity.dto;


import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class WorkDTO extends BasicDTO<WorkDTO> implements Comparable<WorkDTO> {
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
    public void print() {
        System.out.println("Work: {");
        printFormatted("id", id);
        printFormatted("title", title);
        printFormatted("text", text);

        printCollectionFormatted("Author (Student) Ids", authorIds);

        printFormatted("assessmentId", assessmentId);

        printLinksFormatted(super.toString());
        System.out.println("}");
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

    @Override
    public int compareTo(WorkDTO o) {
        if(this.equals(o)) return 0;
        int res = this.title.compareTo(o.title);
        if(res == 0) return Integer.compare(this.id, o.id);
        return res;
    }
}
