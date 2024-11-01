package somonitores.dto;

import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.annotation.JsonbPropertyOrder;
import lombok.*;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonbPropertyOrder({
        "id",
        "nomefantasia",
        "razaosocial",
        "tipo",
        "cpfcnpj",
        "contato",
        "ruanum",
        "num",
        "bairro",
        "cidade",
        "cep",
        "fonefixo",
        "fone",
        "celular2",
        "email",
        "segmento",
        "inscestadual"
})


public class ClienteDTO {

    @JsonbProperty("id")
    private Long id;

   // @JsonbProperty("datacriacao")
   // private LocalDate datacriacao;

    @JsonbProperty("nomefantasia")
    private String nomefantasia;

    @JsonbProperty("razaosocial")
    private String razaosocial;

    @JsonbProperty("tipo")
    private String tipo;

    @JsonbProperty("cpfcnpj")
    private String cpfcnpj;

    @JsonbProperty("contato")
    private String contato;

    @JsonbProperty("ruanum")
    private String ruanum;

    @JsonbProperty("num")
    private String num;

    @JsonbProperty("bairro")
    private String bairro;

    @JsonbProperty("cidade")
    private String cidade;

    @JsonbProperty("cep")
    private String cep;

    @JsonbProperty("fonefixo")
    private String fonefixo;

    @JsonbProperty("fone")
    private String fone;

    @JsonbProperty("celular2")
    private String celular2;

    @JsonbProperty("email")
    private String email;

    @JsonbProperty("segmento")
    private String segmento;

    @JsonbProperty("inscestadual")
    private String inscestadual;

}
