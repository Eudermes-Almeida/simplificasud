package somonitores.dto;

import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.annotation.JsonbPropertyOrder;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonbPropertyOrder({
        "id",
        "unidade",
        "nome",
        "sexo",
        "idade",
        "percentual_frequencia",
        "data_ultima_presenca"
})
public class SeminarioDTO {

    @JsonbProperty("id")
    private Long id;

    @JsonbProperty("unidade")
    private String unidade;

    @JsonbProperty("nome")
    private String nome;

    @JsonbProperty("sexo")
    private String sexo;

    @JsonbProperty("idade")
    private String idade;

    @JsonbProperty("percentual_frequencia")
    private String percentualFrequencia;

    @JsonbProperty("data_ultima_presenca")
    private String dataUltimaPresenca;

}
