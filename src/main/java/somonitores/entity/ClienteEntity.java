package somonitores.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.extern.jackson.Jacksonized;
//import org.hibernate.annotations.CreationTimestamp;
//import java.time.LocalDate;

@Entity
@Table(name = "CLIENTE$")
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
//@Jacksonized
@AllArgsConstructor
@NoArgsConstructor
public class ClienteEntity {

    @Id
    private Long codcli;

//    @CreationTimestamp
//    @Column(name = "datacriacao", updatable = false)
//    private LocalDate datacriacao;

    private String nomefantasia;
    private String razaosocial;
    private String tipo;
    private String cpfcnpj;
    private String contato;
    private String ruanum;
    private String num;
    private String bairro;
    private String cidade;
    private String cep;
    private String fonefixo;
    private String fone;
    private String celular2;
    private String email;
    private String segmento;
    private String inscestadual;

}
