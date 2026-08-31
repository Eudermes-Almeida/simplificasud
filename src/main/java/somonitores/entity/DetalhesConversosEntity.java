package somonitores.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "detalhesconversos")
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DetalhesConversosEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String unidade;

    private String nome;

    private String sexo;

    private String idade;

    private String ativo;

    @Column(name = "tem_chamado")
    private String temChamado;

    private String ministradora;

    private String ministrador;

    private String recomendacao;

    private String sacerdocio;

}
