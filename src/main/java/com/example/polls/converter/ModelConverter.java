package com.example.polls.converter;

import org.springframework.core.convert.converter.Converter;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public interface ModelConverter<S, T> extends Converter<S, T> {

    default List<T> convertAll(Collection<S> sourceModels) {
        return sourceModels.stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }
}
