package somonitores.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "resumojovens")
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResumoJovensEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String unidade;

    @Column(name = "rapazes_total")
    private String rapazesTotal;

    @Column(name = "rapazes_ativos")
    private String rapazesAtivos;

    @Column(name = "mocas_total")
    private String mocasTotal;

    @Column(name = "mocas_ativas")
    private String mocasAtivas;

    @Column(name = "criancas_0_a_2")
    private String criancas0A2;

    @Column(name = "criancas_3_a_11_potencial")
    private String criancas3A11Potencial;

    @Column(name = "criancas_total_ativas")
    private String criancasTotalAtivas;

    @Column(name = "total_criancas")
    private String totalCriancas;

    @Column(name = "total_matriculados_seminario")
    private String totalMatriculadosSeminario;

    @Column(name = "frequencia_acima_75")
    private String frequenciaAcima75;

    @Column(name = "rapazes_recomendacao_batisterio")
    private String rapazesRecomendacaoBatisterio;

    @Column(name = "mocas_recomendacao_batisterio")
    private String mocasRecomendacaoBatisterio;

}
