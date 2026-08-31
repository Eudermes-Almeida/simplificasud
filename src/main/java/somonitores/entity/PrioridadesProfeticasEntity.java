package somonitores.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "prioridadesprofeticas")
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PrioridadesProfeticasEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String unidade;

    private String frequencia;

    @Column(name = "meta_frequencia")
    private String metaFrequencia;

    @Column(name = "membros_participantes")
    private String membrosParticipantes;

    @Column(name = "meta_membros_participantes")
    private String metaMembrosParticipantes;

    @Column(name = "membros_retornando")
    private String membrosRetornando;

    @Column(name = "meta_membros_retornando")
    private String metaMembrosRetornando;

    @Column(name = "membros_jejuando")
    private String membrosJejuando;

    @Column(name = "meta_membros_jejuando")
    private String metaMembrosJejuando;

    @Column(name = "batismos_conversos")
    private String batismosConversos;

    @Column(name = "meta_batismos_conversos")
    private String metaBatismosConversos;

    private String missionarios;

    @Column(name = "meta_missionarios")
    private String metaMissionarios;

    @Column(name = "recomendacao_templo")
    private String recomendacaoTemplo;

    @Column(name = "meta_recomendacao_templo")
    private String metaRecomendacaoTemplo;

    @Column(name = "recomendacao_batisterio")
    private String recomendacaoBatisterio;

    @Column(name = "meta_recomendacao_batisterio")
    private String metaRecomendacaoBatisterio;

}
