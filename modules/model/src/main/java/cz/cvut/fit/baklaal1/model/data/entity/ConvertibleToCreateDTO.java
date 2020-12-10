package cz.cvut.fit.baklaal1.model.data.entity;

public interface ConvertibleToCreateDTO<T_CREATE_DTO> {
    public T_CREATE_DTO toCreateDTO();
}
