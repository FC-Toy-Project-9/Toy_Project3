package com.fc.toy_project3.domain.trip.dto.request;

import com.fc.toy_project3.domain.trip.exception.InvalidPagingRequestException;
import lombok.Builder;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

public class TripPageRequestDTO {

    private final int page;
    private final int size;
    private final String criteria;
    private final String sort;

    @Builder
    public TripPageRequestDTO(int page, int size, String criteria, String sort) {
        this.page = Math.max(page, 0);
        int DEFAULT_SIZE = 10;
        int MAX_SIZE = 50;
        this.size = size > MAX_SIZE ? DEFAULT_SIZE : size;
        if(!criteria.equals("likeCount")&&!criteria.equals("createdAt")){
            throw new InvalidPagingRequestException("orderBy는 `likeCount`와 `createdAt` 중 선택해주세요.");
        }
        this.criteria = criteria;
        if (!sort.equals("ASC") && !sort.equals("DESC")) {
            throw new InvalidPagingRequestException("sort는 `ASC`(오름차순)과 `DESC`(내림차순) 중 선택해주세요.");
        }
        this.sort = sort;
    }

    public org.springframework.data.domain.PageRequest of() {
        if (sort.equals("ASC")) {
            return org.springframework.data.domain.PageRequest.of(page, size,
                Sort.by(Direction.ASC, criteria));
        } else if (sort.equals("DESC")) {
            return org.springframework.data.domain.PageRequest.of(page, size,
                Sort.by(Direction.DESC, criteria));
        } else {
            return org.springframework.data.domain.PageRequest.of(page, size,
                Sort.by(Direction.DESC, "createdAt"));
        }
    }
}
