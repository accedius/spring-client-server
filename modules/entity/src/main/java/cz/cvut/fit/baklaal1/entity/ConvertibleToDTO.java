package cz.cvut.fit.baklaal1.entity;

public interface ConvertibleToDTO<T_DTO> {
    public T_DTO toDTO();
}
