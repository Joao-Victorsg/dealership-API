package integrated.util;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(builder = TokenDto.TokenDtoBuilder.class)
@Builder
public record TokenDto(

        @JsonProperty("data")
        String token
){}