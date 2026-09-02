package somonitores.dto;

import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.annotation.JsonbPropertyOrder;
import lombok.*;

// Campos sensíveis (registromembro, nascimento, senha_hash) NÃO entram aqui de propósito
// -- este DTO é o que qualquer chamada de leitura devolve, e a API hoje não tem
// autenticação nenhuma (ver memória project-raiox-auth-design). registromembro+nascimento
// são justamente os 2 fatores de identificação do primeiro acesso; expô-los por essa rota
// permitiria a qualquer um "adivinhar" a identidade de um líder. senha_hash nunca deve
// trafegar pela API, mesmo hasheada.
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonbPropertyOrder({
        "id",
        "nome",
        "unidade",
        "escopo",
        "chamado",
        "login"
})
public class LideresDTO {

    @JsonbProperty("id")
    private Long id;

    @JsonbProperty("nome")
    private String nome;

    @JsonbProperty("unidade")
    private String unidade;

    @JsonbProperty("escopo")
    private String escopo;

    @JsonbProperty("chamado")
    private String chamado;

    @JsonbProperty("login")
    private String login;

}
