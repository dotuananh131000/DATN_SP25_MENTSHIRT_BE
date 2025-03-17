package com.java.project.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class APIRequestOrResponse <T> {
    @Builder.Default
    Integer code = 1000;
    String message;
    T data;

}
