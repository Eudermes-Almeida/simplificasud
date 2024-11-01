package somonitores.dto;

import jakarta.json.bind.annotation.JsonbPropertyOrder;
import jakarta.json.bind.annotation.JsonbTypeAdapter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import somonitores.adapter.LocalDateTimeAdapter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonbPropertyOrder({
        "os", "data", "hora", "fantasia", "rzsocial", "codcli", "atendente", "status",
        "prioridade", "equipamento", "marca", "modelo", "topologia", "cor", "serial",
        "patrimonio", "obs1", "obs2", "defeito1", "defeito2", "defeito3",
        "tarefa1", "tarefa2", "tarefa3", "baterias", "material2", "material3",
        "outrositens", "qt1", "qt2", "qt3", "qtoutrositens", "qtdeslocamento",
        "valorunit1", "valorunit2", "valorunit3", "valorunitoutrositens",
        "valorunitariodeslocamento", "valortotal1", "valortotal2", "valortotal3",
        "valortotaloutrositens", "valortotaldeslocamento", "valorassistencia",
        "totalbruto", "valordesconto", "desconto", "totaliq", "fechamento",
        "tecnico", "garantia", "dataorca", "osoriginal", "venda", "modelofake",
        "obs3", "jaimp", "top10", "dataretro", "somatotaliq", "qtOs"})
public class OsDTO {

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

    @JsonbTypeAdapter(LocalDateTimeAdapter.class)  //para aceitar data null ou ""
    private LocalDateTime fechamento;

    private String tecnico;
    private String garantia;

    @JsonbTypeAdapter(LocalDateTimeAdapter.class)
    private LocalDateTime dataorca;


    private String osoriginal;
    private String venda;
    private String modelofake;
    private String obs3;
    private String jaimp;
    private Long top10;
    private String dataretro;
    private SomatoriaDTO somatoriaDTO;

}
