package somonitores.dto;

import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.annotation.JsonbPropertyOrder;
import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonbPropertyOrder({
        "id",
        "unidade",
        "anomes",
        "frequencia"
})
public class FrequenciaSacramentalDTO {

    @JsonbProperty("id")
    private Long id;

    @JsonbProperty("unidade")
    private String unidade;

    @JsonbProperty("anomes")
    private LocalDate anomes;

    @JsonbProperty("frequencia")
    private String frequencia;

}
