package somonitores.dto;

import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.annotation.JsonbPropertyOrder;
import lombok.*;

// token vai num header customizado (X-Auth-Token) em toda chamada seguinte -- ver
// AuthResource. escopo/unidade vêm junto pra o frontend já povoar a variável "unidade"
// existente sem precisar de uma segunda chamada logo após o login.
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonbPropertyOrder({"token", "nome", "unidade", "escopo"})
public class LoginResponseDTO {

    @JsonbProperty("token")
    private String token;

    @JsonbProperty("nome")
    private String nome;

    @JsonbProperty("unidade")
    private String unidade;

    @JsonbProperty("escopo")
    private String escopo;

}
