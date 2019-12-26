package com.example.polls.payload.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
public class ChoiceRequest {

    @NotBlank
    @Size(max = 40)
    private final String text;

    @JsonCreator
    public ChoiceRequest(@NotBlank @Size(max = 40) String text) {
        this.text = text;
    }
}
