package somonitores.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;

// Sem @ManyToOne para LideresEntity de propósito -- mesmo estilo "flat" das outras
// entidades do projeto (nenhuma delas usa relacionamento JPA entre si). lider_id é uma
// FK só a nível de banco (ver 005_create_table_sessoes.sql), lida/gravada como Long puro.
@Entity
@Table(name = "sessoes")
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SessaoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "lider_id")
    private Long liderId;

    private String token;

    @Column(name = "criado_em")
    private LocalDateTime criadoEm;

}
