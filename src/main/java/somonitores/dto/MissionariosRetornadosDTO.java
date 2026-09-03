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
        "solteiro",
        "selado",
        "matriculadoinstituto",
        "recomendacaotemplo",
        "chamado",
        "paismissao"
})
public class MissionariosRetornadosDTO {

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

    @JsonbProperty("solteiro")
    private String solteiro;

    @JsonbProperty("selado")
    private String selado;

    @JsonbProperty("matriculadoinstituto")
    private String matriculadoinstituto;

    @JsonbProperty("recomendacaotemplo")
    private String recomendacaotemplo;

    @JsonbProperty("chamado")
    private String chamado;

    @JsonbProperty("paismissao")
    private String paismissao;

}
