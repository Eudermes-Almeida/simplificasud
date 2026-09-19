package somonitores.dto;

import jakarta.json.bind.annotation.JsonbProperty;
import lombok.*;

import java.time.LocalDate;

// DTO "administrativo" -- ao contrário de LideresDTO, expõe de propósito registromembro e
// nascimento. Usado só pelas rotas /lideres/admin/**, restritas a escopo=Master pelo
// AutorizacaoFilter (ver memória project-raiox-admin-perfis).
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LiderAdminDTO {

    @JsonbProperty("id")
    private Long id;

    @JsonbProperty("nome")
    private String nome;

    @JsonbProperty("registromembro")
    private String registromembro;

    @JsonbProperty("unidade")
    private String unidade;

    @JsonbProperty("escopo")
    private String escopo;

    @JsonbProperty("nascimento")
    private LocalDate nascimento;

    @JsonbProperty("chamado")
    private String chamado;

}
