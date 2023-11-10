package com.fc.toy_project3.global.util;

import java.util.List;

public interface GeneralMapper<D, E> {

    D toDto(E e);

    E toEntity(D d);

    List<D> toDtoList(List<E> entityList);

    List<E> toEntityList(List<D> entityList);

    void updateFromDto(D dto, E entity);
}
