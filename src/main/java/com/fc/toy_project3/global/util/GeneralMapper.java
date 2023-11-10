package com.fc.toy_project3.global.util;

import java.util.List;
import org.mapstruct.BeanMapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

public interface GeneralMapper<D, E> {

    D toDto(E e);

    E toEntity(D d);

    List<D> toDtoList(List<E> entityList);

    List<E> toEntityList(List<D> entityList);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(D dto, @MappingTarget E entity);
}
