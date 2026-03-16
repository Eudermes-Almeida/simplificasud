package somonitores.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDateTime;

@Entity
@Table(name = "OS$")
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
//@Jacksonized
@AllArgsConstructor
@NoArgsConstructor
public class OsEntity {

    @Id
    private Long os;
    private LocalDateTime data;
    private String hora;
    private String fantasia;
    private String rzsocial;
    private Long  codcli;
    private String atendente;
    private String status;
    private String prioridade;
    private String equipamento;
    private String marca;
    private String modelo;
    private String topologia;
    private String cor;
    private String serial;
    private String patrimonio;
    private String obs1;
    private String obs2;
    private String defeito1;
    private String defeito2;
    private String defeito3;
    private String tarefa1;
    private String tarefa2;
    private String tarefa3;
    private String baterias;
    private String material2;
    private String material3;
    private String outrositens;
    private String qt1;
    private String qt2;
    private String qt3;
    private String qtoutrositens;
    private String qtdeslocamento;
    private String valorunit1;
    private String valorunit2;
    private String valorunit3;
    private String valorunitoutrositens;
    private String valorunitariodeslocamento;
    private String valortotal1;
    private String valortotal2;
    private String valortotal3;
    private String valortotaloutrositens;
    private String valortotaldeslocamento;
    private String valorassistencia;
    private String totalbruto;
    private String valordesconto;
    private String desconto;
    private String totaliq;
    private LocalDateTime fechamento;
    private String tecnico;
    private String garantia;
    private LocalDateTime dataorca;
    private String osoriginal;
    private String venda;
    private String modelofake;
    private String obs3;
    private String jaimp;
    private Long top10;
    private String dataretro;
}
