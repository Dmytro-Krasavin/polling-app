package com.example.polls.payload.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class PagedResponse<T> {

    @Getter(AccessLevel.NONE)
    private final List<T> content;

    private final int pageNumber;

    private final int size;

    private final long totalElements;

    private final int totalPages;

    private final boolean last;

    public List<T> getContent() {
        return Collections.unmodifiableList(content);
    }
}
