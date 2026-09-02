package somonitores.dto;

import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.annotation.JsonbPropertyOrder;
import lombok.*;

// Devolvido só quando a identificação encontra a pessoa E ela ainda não tem login/senha
// cadastrados -- usado pela tela de confirmação ("Você é Fulano de tal?"). O "id" aqui só
// é revelado depois que quem chamou já provou conhecer nascimento+registromembro corretos,
// então não é um enumeration risk (ver LideresDTO, que nunca expõe esses 2 campos).
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonbPropertyOrder({"id", "nome"})
public class IdentificacaoResponseDTO {

    @JsonbProperty("id")
    private Long id;

    @JsonbProperty("nome")
    private String nome;

}
