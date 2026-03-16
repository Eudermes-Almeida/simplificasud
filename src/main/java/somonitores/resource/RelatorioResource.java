package somonitores.resource;


import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import somonitores.dto.ComissaoDTO;
import somonitores.service.RelatorioService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import somonitores.dto.OsDTO;

import java.math.BigDecimal;
import java.util.List;

@Path("/relatorio")
public class RelatorioResource {

    @Inject
    RelatorioService relatorioService;

    @GET
    @Path("/osFechadasEntreDatas")
    @Produces(MediaType.APPLICATION_JSON)
    @Tag(name = "Busca Ordens de Serviço por intervalo de datas", description = "Busca Ordens de Serviço entre duas datas fornecidas.")
    public Response buscarOsPelaDataFechamentoIntervalo(@QueryParam("dataInicio") String dataInicioString,
                                                        @QueryParam("dataFim") String dataFimString,
                                                        @QueryParam("busca") String busca) {
        try {
            // Verifica se as datas têm o comprimento correto
            if (dataInicioString.length() != 8 || dataFimString.length() != 8) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Formato de data inválido. Use o formato: ddMMyyyy.")
                        .build();
            }

            // Formato esperado: 01012024
            String dataInicioFormatada = dataInicioString.substring(4, 8) + "-" +
                    dataInicioString.substring(2, 4) + "-" +
                    dataInicioString.substring(0, 2) + " 00:00:00";
            String dataFimFormatada = dataFimString.substring(4, 8) + "-" +
                    dataFimString.substring(2, 4) + "-" +
                    dataFimString.substring(0, 2) + " 23:59:59";

            List<OsDTO> osList = null;

            // if (busca.equals("faturamentoTotal")) {
            osList = relatorioService.faturamentoEmIntervalo(dataInicioFormatada, dataFimFormatada, busca);
            //}

            if (osList.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Nenhuma ordem de serviço encontrada para o intervalo de datas fornecido.")
                        .build();
            }

            return Response.ok(osList).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao buscar ordens de serviço: a busca " + busca + " está com problema" + e.getMessage())
                    .build();
        }

    }

    @GET
    @Path("/osPorStatus")
    @Produces(MediaType.APPLICATION_JSON)
    @Tag(name = "Busca Ordens de Serviço por status", description = "Busca Ordens de Serviço com um determinado status.")
    public Response buscarOsPorStatus(@QueryParam("status") String status) {
        try {

            List<OsDTO> osList = null;

            // if (busca.equals("faturamentoTotal")) {
            osList = relatorioService.buscaOsPorStatus(status);
            //}

            if (osList.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Nenhuma ordem de serviço encontrada para o intervalo de datas fornecido.")
                        .build();
            }

            return Response.ok(osList).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao buscar ordens de serviço: a busca " + status + " está com problema" + e.getMessage())
                    .build();
        }

    }


    @GET
    @Path("/osPorStatusRemoto")
    @Produces(MediaType.APPLICATION_JSON)
    @Tag(name = "Busca Ordens de Serviço Remotas", description = "Busca Ordens de Serviço com status 'APROVADA REMOTA' ou 'REPROVADA REMOTA'.")
    public Response buscarOsPorStatusRemoto() {
        try {
            List<OsDTO> osList = relatorioService.buscaOsPorStatusRemoto();

            if (osList.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Nenhuma ordem de serviço encontrada para os status 'APROVADA REMOTA' ou 'REPROVADA REMOTA'.")
                        .build();
            }

            return Response.ok(osList).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao buscar ordens de serviço com status remoto: " + e.getMessage())
                    .build();
        }
    }


    @GET
    @Path("/aprovacaoOnline")
    @Produces(MediaType.APPLICATION_JSON)
    @Tag(name = "Busca Ordens de Serviço por status", description = "Busca Ordens de Serviço com um determinado status.")
    public Response aprovacaoOnline(
            @QueryParam("status") String status,
            @QueryParam("codcli") String codcli) {
        try {
            // Verifica se os parâmetros 'status' e 'codcli' são fornecidos
            if (status == null || codcli == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Os parâmetros 'status' e 'codcli' são obrigatórios.")
                        .build();
            }

            // Valida se 'codcli' é um número válido
            Long codcliLong;
            try {
                codcliLong = Long.parseLong(codcli);
            } catch (NumberFormatException e) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("O parâmetro 'codcli' deve ser um número válido.")
                        .build();
            }

            // Busca as ordens de serviço pelo status e codcli
            List<OsDTO> osList = relatorioService.buscaOsPorStatusECodcli(status, codcliLong);

            // Verifica se a lista de ordens de serviço está vazia
            if (osList.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Nenhuma ordem de serviço encontrada para o status e código cliente fornecidos.")
                        .build();
            }

            // Retorna a lista de ordens de serviço encontradas
            return Response.ok(osList).build();
        } catch (Exception e) {
            // Trata exceções inesperadas
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao buscar ordens de serviço: " + e.getMessage())
                    .build();
        }
    }


    @GET
    @Path("/calculoComissaoTecnicos")
    @Produces(MediaType.APPLICATION_JSON)
    @Tag(name = "Busca Ordens de Serviço por intervalo de datas", description = "Busca Ordens de Serviço entre duas datas fornecidas.")
    public Response calculaComissaoTecnicos(@QueryParam("dataInicio") String dataInicioString,
                                            @QueryParam("dataFim") String dataFimString,
                                            @QueryParam("busca") String busca) {
        try {
            // Verifica se as datas têm o comprimento correto
            if (dataInicioString.length() != 8 || dataFimString.length() != 8) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Formato de data inválido. Use o formato: ddMMyyyy.")
                        .build();
            }

            // Formato esperado: 01012024
            String dataInicioFormatada = dataInicioString.substring(4, 8) + "-" +
                    dataInicioString.substring(2, 4) + "-" +
                    dataInicioString.substring(0, 2) + " 00:00:00";
            String dataFimFormatada = dataFimString.substring(4, 8) + "-" +
                    dataFimString.substring(2, 4) + "-" +
                    dataFimString.substring(0, 2) + " 23:59:59";

            // Chama o método service que agora retorna um ComissaoDTO
            ComissaoDTO comissaoDTO = relatorioService.calculaComissaoTecnicos(dataInicioFormatada, dataFimFormatada, busca);

            // Verifica se o ComissaoDTO é nulo ou possui valores que indicam ausência de dados
            if (comissaoDTO == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Erro ao calcular comissão: Dados não encontrados para o intervalo de datas fornecido.")
                        .build();
            }

            // Retorna a resposta com o DTO calculado
            return Response.ok(comissaoDTO).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao calcular comissão: " + e.getMessage())
                    .build();
        }
    }


}
