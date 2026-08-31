package somonitores.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "qualificacaounidade")
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QualificacaoUnidadeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String unidade;

    @Column(name = "total_membros")
    private String totalMembros;

    @Column(name = "frequencia_sacramental")
    private String frequenciaSacramental;

    @Column(name = "dizimistas_integrais")
    private String dizimistasIntegrais;

}
