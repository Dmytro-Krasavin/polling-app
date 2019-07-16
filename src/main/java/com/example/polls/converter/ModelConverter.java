package com.example.polls.converter;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public interface ModelConverter<S, T> {

    T convert(S sourceModel);

    default List<T> convertAll(Collection<S> sourceModels) {
        return sourceModels.stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }
}
