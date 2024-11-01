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
    @Tag(name = "Busca Ordens de Serviço por status", description = "Busca Ordens de Serviço entre duas datas fornecidas.")
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
            if (comissaoDTO == null || comissaoDTO.getComissao().compareTo(BigDecimal.ZERO) == 0) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Nenhuma comissão calculada para o intervalo de datas fornecido.")
                        .build();
            }

            return Response.ok(comissaoDTO).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao calcular comissão: " + e.getMessage())
                    .build();
        }
    }

}
