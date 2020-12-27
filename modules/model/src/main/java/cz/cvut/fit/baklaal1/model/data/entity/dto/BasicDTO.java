package cz.cvut.fit.baklaal1.model.data.entity.dto;

import org.springframework.hateoas.RepresentationModel;

import java.util.Collection;

//TODO printable methods should = toString() result, connect them for code reuse and readability of toString() method
public abstract class BasicDTO<T_DTO extends BasicDTO<T_DTO>> extends RepresentationModel<T_DTO> implements Printable {
    protected final int id;
    //TODO if needed variable for version etc.

    BasicDTO(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    protected void printFormatted(String valueName, Object value) {
        System.out.println("\t" + valueName + ": \"" + value + "\"");
    }

    @Override
    public void printAsPaged() {
        print();
    }

    //TODO do better
    protected <T> void printCollectionFormatted(String collectionName, Collection<T> collection) {
        String collectionFormatted = "";
        for(T item : collection) {
            collectionFormatted += " " + item + ", ";
        }
        if(collectionFormatted.length() >= 2) {
            collectionFormatted = collectionFormatted.substring(0, collectionFormatted.length() - 2);
        }
        System.out.println("\t" + collectionName + ": {" + collectionFormatted + "}");
    }

    protected  void printLinksFormatted(String links) {
        System.out.println("\t" + links);
    }
}
