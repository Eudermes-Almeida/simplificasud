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
        "ativo",
        "tem_chamado",
        "ministradora",
        "ministrador",
        "recomendacao",
        "sacerdocio"
})
public class DetalhesConversosDTO {

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

    @JsonbProperty("ativo")
    private String ativo;

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
