package cz.cvut.fit.baklaal1.model.data.entity.dto;

import org.springframework.hateoas.RepresentationModel;

//TODO printable methods should = toString() result, connect them for code reuse and readability of toString() method
public abstract class BasicDTO<T_DTO extends BasicDTO<T_DTO>> extends RepresentationModel<T_DTO> implements ReadableId, Printable {
    protected void printFormatted(String valueName, Object value) {
        System.out.println(valueName + ": \"" + value + "\"");
    }

    @Override
    public void printAsPaged() {
        print();
    }
}
