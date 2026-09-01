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
        "idade",
        "recomendacao_batisterio"
})
public class MocasDTO {

    @JsonbProperty("id")
    private Long id;

    @JsonbProperty("unidade")
    private String unidade;

    @JsonbProperty("nome")
    private String nome;

    @JsonbProperty("idade")
    private String idade;

    @JsonbProperty("recomendacao_batisterio")
    private String recomendacaoBatisterio;

}
