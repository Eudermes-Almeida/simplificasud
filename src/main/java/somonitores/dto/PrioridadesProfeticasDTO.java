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
        "frequencia",
        "meta_frequencia",
        "membros_participantes",
        "meta_membros_participantes",
        "membros_retornando",
        "meta_membros_retornando",
        "membros_jejuando",
        "meta_membros_jejuando",
        "batismos_conversos",
        "meta_batismos_conversos",
        "missionarios",
        "meta_missionarios",
        "recomendacao_templo",
        "meta_recomendacao_templo",
        "recomendacao_batisterio",
        "meta_recomendacao_batisterio"
})
public class PrioridadesProfeticasDTO {

    @JsonbProperty("id")
    private Long id;

    @JsonbProperty("unidade")
    private String unidade;

    @JsonbProperty("frequencia")
    private String frequencia;

    @JsonbProperty("meta_frequencia")
    private String metaFrequencia;

    @JsonbProperty("membros_participantes")
    private String membrosParticipantes;

    @JsonbProperty("meta_membros_participantes")
    private String metaMembrosParticipantes;

    @JsonbProperty("membros_retornando")
    private String membrosRetornando;

    @JsonbProperty("meta_membros_retornando")
    private String metaMembrosRetornando;

    @JsonbProperty("membros_jejuando")
    private String membrosJejuando;

    @JsonbProperty("meta_membros_jejuando")
    private String metaMembrosJejuando;

    @JsonbProperty("batismos_conversos")
    private String batismosConversos;

    @JsonbProperty("meta_batismos_conversos")
    private String metaBatismosConversos;

    @JsonbProperty("missionarios")
    private String missionarios;

    @JsonbProperty("meta_missionarios")
    private String metaMissionarios;

    @JsonbProperty("recomendacao_templo")
    private String recomendacaoTemplo;

    @JsonbProperty("meta_recomendacao_templo")
    private String metaRecomendacaoTemplo;

    @JsonbProperty("recomendacao_batisterio")
    private String recomendacaoBatisterio;

    @JsonbProperty("meta_recomendacao_batisterio")
    private String metaRecomendacaoBatisterio;

}
