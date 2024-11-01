package somonitores.entity;


import jakarta.persistence.*;
import lombok.*;
import lombok.extern.jackson.Jacksonized;

@Entity
@Table(name = "PARAMETROS")
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@Jacksonized
@AllArgsConstructor
@NoArgsConstructor
public class ParametroEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String parametro;
    private String valor;
}
