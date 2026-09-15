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
        "data_batismo",
        "tem_chamado",
        "ministradora",
        "ministrador",
        "recomendacao",
        "sacerdocio"
})
public class HomensPreparadosDTO {

    @JsonbProperty("id")
    private Long id;

    @JsonbProperty("unidade")
    private String unidade;

    @JsonbProperty("nome")
    private String nome;

    @JsonbProperty("idade")
    private String idade;

    @JsonbProperty("data_batismo")
    private String dataBatismo;

    @JsonbProperty("tem_chamado")
    private String temChamado;

    @JsonbProperty("ministradora")
    private String ministradora;

    @JsonbProperty("ministrador")
    private String ministrador;

    @JsonbProperty("recomendacao")
    private String recomendacao;

    @JsonbProperty("sacerdocio")
    private String sacerdocio;

}
