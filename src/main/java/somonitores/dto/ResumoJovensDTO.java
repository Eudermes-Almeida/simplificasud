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
        "rapazes_total",
        "rapazes_ativos",
        "mocas_total",
        "mocas_ativas",
        "criancas_0_a_2",
        "criancas_3_a_11_potencial",
        "criancas_total_ativas",
        "total_criancas",
        "total_matriculados_seminario",
        "frequencia_acima_75",
        "rapazes_recomendacao_batisterio",
        "mocas_recomendacao_batisterio"
})
public class ResumoJovensDTO {

    @JsonbProperty("id")
    private Long id;

    @JsonbProperty("unidade")
    private String unidade;

    @JsonbProperty("rapazes_total")
    private String rapazesTotal;

    @JsonbProperty("rapazes_ativos")
    private String rapazesAtivos;

    @JsonbProperty("mocas_total")
    private String mocasTotal;

    @JsonbProperty("mocas_ativas")
    private String mocasAtivas;

    @JsonbProperty("criancas_0_a_2")
    private String criancas0A2;

    @JsonbProperty("criancas_3_a_11_potencial")
    private String criancas3A11Potencial;

    @JsonbProperty("criancas_total_ativas")
    private String criancasTotalAtivas;

    @JsonbProperty("total_criancas")
    private String totalCriancas;

    @JsonbProperty("total_matriculados_seminario")
    private String totalMatriculadosSeminario;

    @JsonbProperty("frequencia_acima_75")
    private String frequenciaAcima75;

    @JsonbProperty("rapazes_recomendacao_batisterio")
    private String rapazesRecomendacaoBatisterio;

    @JsonbProperty("mocas_recomendacao_batisterio")
    private String mocasRecomendacaoBatisterio;

}
