package somonitores.dto;


import jakarta.json.bind.annotation.JsonbProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ParametroDTO {

    @JsonbProperty("id")
    private Long id;
    private String parametro;
    private String valor;
}
