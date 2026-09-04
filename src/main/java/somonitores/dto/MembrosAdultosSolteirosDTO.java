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
        "estadocivil",
        "recomendacaotemplo",
        "paismissao",
        "chamados"
})
public class MembrosAdultosSolteirosDTO {

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

    @JsonbProperty("estadocivil")
    private String estadocivil;

    @JsonbProperty("recomendacaotemplo")
    private String recomendacaotemplo;

    @JsonbProperty("paismissao")
    private String paismissao;

    @JsonbProperty("chamados")
    private String chamados;

}
