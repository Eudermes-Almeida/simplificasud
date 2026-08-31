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
        "total_membros",
        "frequencia_sacramental",
        "dizimistas_integrais"
})
public class QualificacaoUnidadeDTO {

    @JsonbProperty("id")
    private Long id;

    @JsonbProperty("unidade")
    private String unidade;

    @JsonbProperty("total_membros")
    private String totalMembros;

    @JsonbProperty("frequencia_sacramental")
    private String frequenciaSacramental;

    @JsonbProperty("dizimistas_integrais")
    private String dizimistasIntegrais;

}
