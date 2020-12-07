package cz.cvut.fit.baklaal1.data.entity;

public interface ConvertibleToDTO<T_DTO> {
    public T_DTO toDTO();
}
