package com.example.polls.payload.request;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class PollRequest {

    @NotBlank
    @Size(max = 140)
    private final String question;

    @NotNull
    @Size(min = 2, max = 6)
    @Valid
    private final List<ChoiceRequest> choices;

    @NotNull
    @Valid
    private final PollLength pollLength;

}
