package somonitores.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "missionariosretornados")
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MissionariosRetornadosEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String unidade;

    private String nome;

    private String sexo;

    private String idade;

    private String solteiro;

    private String selado;

    private String matriculadoinstituto;

    private String recomendacaotemplo;

    private String chamado;

    private String paismissao;

}
