package somonitores.resource;


import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import somonitores.dto.ClienteDTO;
import somonitores.dto.OsDTO;
import somonitores.entity.ClienteEntity;
import somonitores.entity.OsEntity;
import somonitores.service.ClienteService;
import somonitores.service.OsService;

import java.util.List;

@Path("/os")
public class OsResource {

    @Inject
    OsService osService;

    @Path("/{os}")
    @GET
//@Authenticated
    @Tag(name = "Busca ordem de servico por id", description = "Busca ordem de servico por id")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @APIResponses(value = {
            @APIResponse(responseCode = "400", description = "Requisição recebida não é válida", content = @Content(schema = @Schema(implementation = OsDTO.class))),
            @APIResponse(responseCode = "401", description = "Não autenticado", content = @Content(schema = @Schema(implementation = OsDTO.class))),
            @APIResponse(responseCode = "403", description = "Token inválido", content = @Content(schema = @Schema(implementation = OsDTO.class))),
            @APIResponse(responseCode = "404", description = "Requisição não retornou dados", content = @Content(schema = @Schema(implementation = OsDTO.class))),
            //@APIResponse(responseCode = "500", description = Constants.INTERNAL_SERVER_ERROR_MESSAGE),
    })

    @Operation(summary = "Busca Ordem de servico por id", description = "Busca Ordem de servico por id")
    public Response buscaOsPorId(@PathParam("os") Long os) {
        try {
            OsDTO osDTO = osService.buscarOsPeloId(os);

            if (osDTO == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Ordem de Servico não encontrada para o ID: " + os)
                        .build();
            }

            return Response.ok(osDTO).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao buscar ordem de Servico: " + e.getMessage())
                    .build();
        }
    }


    @GET
    @Path("/codcli")
    @Produces(MediaType.APPLICATION_JSON)
    @Tag(name = "Busca Ordens de Servico pelo codigo do cliente", description = "Busca Ordens de Servico pelo codigo do cliente")
    public Response buscarOsPorCodcli(@QueryParam("codcli") Long codcli) {
        try {
            List<OsDTO> ordens = osService.buscarOsPeloCodcli(codcli);

            if (ordens.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Nenhuma ordem de servico para o cliente com o codcli  " + codcli)
                        .build();
            }

            return Response.ok(ordens).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao buscar ordens de servico: " + e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/razaosocial")
    @Produces(MediaType.APPLICATION_JSON)
    @Tag(name = "Busca Ordens de Servico por parte da Razao Social", description = "Busca Ordens de SErvico por parte da Razao Social")
    public Response buscarOsPorRazaoSocial(@QueryParam("razaosocial") String rzsocial) {
        try {
            List<OsDTO> ordens = osService.buscarOsPorRazaoSocial(rzsocial);

            if (ordens.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Nenhuma ordem de servico para a razao social  : " + rzsocial)
                        .build();
            }

            return Response.ok(ordens).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao buscar ordens de servico " + e.getMessage())
                    .build();
        }

    }
    @GET
    @Path("/fantasia")
    @Produces(MediaType.APPLICATION_JSON)
    @Tag(name = "Busca Ordens de SErvico pelo nome fantasia", description = "Busca Ordens de SErvico pelo nome fantasia")
    public Response buscarOsPorNomeFantasia(@QueryParam("fantasia") String fantasia) {

        try {
            List<OsDTO> ordens = osService.buscarOsPorNomeFantasia(fantasia);

            if (ordens.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Nenhuma ordem de servico para este nome fantasia  " + fantasia)
                        .build();
            }

            return Response.ok(ordens).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao buscar clientes: " + e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/buscarPorDataEntrada/{dataString}")
    @Produces(MediaType.APPLICATION_JSON)
    @Tag(name = "Busca Ordens de SErvico pela data de ENTRADA", description = "Busca Ordens de SErvico pela data de ENTRADA")
    public Response buscarPorDataEntrada(@PathParam("dataString") String dataString) {
        try {
            // Verifica se a dataString tem o comprimento correto
            if (dataString.length() != 8) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Formato de data inválido. Use o formato: ddMMyyyy.")
                        .build();
            }

            // Formato esperado: 29072024
            String dataFormatada = dataString.substring(0, 2) + "/" +
                    dataString.substring(2, 4) + "/" +
                    dataString.substring(4, 8);

            List<OsDTO> osList = osService.buscarOsPelaDataEntrada(dataFormatada);

            if (osList.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Nenhuma ordem de serviço encontrada para a data " + dataFormatada)
                        .build();
            }

            return Response.ok(osList).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao buscar ordens de serviço: " + e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/buscarPorDataFechamento/{dataString}")
    @Produces(MediaType.APPLICATION_JSON)
    @Tag(name = "Busca Ordens de SErvico pela data de FECHAMENTO", description = "Busca Ordens de SErvico pela data de FECHAMENTO")
    public Response buscarPorDataFechamento(@PathParam("dataString") String fechamentoString) {
        try {
            // Verifica se a dataString tem o comprimento correto
            if (fechamentoString.length() != 8) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Formato de data inválido. Use o formato: ddMMyyyy.")
                        .build();
            }

            // Formato esperado: 29072024
            String dataFormatada = fechamentoString.substring(0, 2) + "/" +
                    fechamentoString.substring(2, 4) + "/" +
                    fechamentoString.substring(4, 8);

            List<OsDTO> osList = osService.buscarOsPelaDataFechamento(dataFormatada);

            if (osList.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Nenhuma ordem de serviço encontrada para a data " + dataFormatada)
                        .build();
            }

            return Response.ok(osList).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao buscar ordens de serviço: " + e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/ultimasos")
    @Produces(MediaType.APPLICATION_JSON)
    @Tag(name = "Busca Ultimas ordens de serviço - o parametro limite -define quantas serão trazidas", description = "Busca Ordens de Serviço com um limite definido")
    public Response buscaUltimasOs(@QueryParam("limite") int limite) {
        try {
            List<OsDTO> ordens = osService.buscarUltimasOs(limite);

            if (ordens.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("O limite de ultimas OS deve ser maior que zero e menor que 1.000 " + limite)
                        .build();
            }

            return Response.ok(ordens).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao buscar ordens de serviço: " + e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/ultimaos")
    @Produces(MediaType.APPLICATION_JSON)
    @Tag(name = "Busca o id da ultima OS cadastrada", description = "Busca o id da ultima OS cadastrada")
    public Response buscarUltimaOs() {
        try {
            Long maiorId = osService.buscarUltimaOsCadastrada();

            if (maiorId == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Nenhuma ordem de servico cadastrada.")
                        .build();
            }

            return Response.ok(maiorId).build(); // Retorna apenas o maior ID
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao buscar ultima Os cadastrada " + e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/serial")
    @Produces(MediaType.APPLICATION_JSON)
    @Tag(name = "Busca Ordens de SErvico pelo nome fantasia", description = "Busca Ordens de SErvico pelo nome fantasia")
    public Response buscarOsPorSerial(@QueryParam("serial") String serial) {

        try {
            List<OsDTO> ordens = osService.buscarOsPorSerial(serial);

            if (ordens.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Nenhuma ordem de servico para esta serial  " + serial)
                        .build();
            }

            return Response.ok(ordens).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao buscar clientes: " + e.getMessage())
                    .build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Tag(name = "Persiste uma nova ordem de servico", description = "Persiste uma nova ordem de servico")
    public Response criarNovaOs(OsDTO osDTO) {
        try {

            // DTO para Entity:
            OsEntity osEntity = mapToEntity(osDTO);

            // Salva o cliente usando o serviço
            OsEntity osPersistido = osService.criarOs(osEntity);

            return Response.status(Response.Status.CREATED)
                    .entity(osPersistido) // Retorna o DTO criado com status 201
                    .build();
        } catch (Exception e) {

            return Response.status(Response.Status.BAD_REQUEST)
                    //Se chegar um id repetido no banco de dados, ele não aceita a persistência e dá esta excessão
                    .entity("Erro ao criar nova ordem de servico: já existe uma ordem com o mesmo ID " + e.getMessage())
                    .build();
        }
    }

    @PUT
    @Path("/os/{os}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Tag(name = "Atualiza uma ordem de servico existente", description = "Atualiza uma ordem de servico existente")
    public Response atualizarOs(@PathParam("os") Long os, OsDTO osDTO) {
        try {

            // transforma o DTO em Entity:
            OsEntity osEntity = mapToEntity(osDTO);

            // Atualiza o cliente usando o serviço
            OsEntity osAtualizada = osService.atualizarOs(os, osEntity);

            return Response.ok(osAtualizada) // Retorna a ordem servico atualizado com status 200
                    .build();
        } catch (Exception e) {
            // Verifica se a mensagem de erro contém informações sobre não encontrado
            if (e.getMessage() != null && e.getMessage().contains("Cliente não encontrado")) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Ordem de servico não encontrada com o código: " + os)
                        .build();
            }
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Erro ao atualizar ordem de servico: " + e.getMessage())
                    .build();
        }
    }

    @DELETE
    @Path("/os/{os}")
    @Produces(MediaType.APPLICATION_JSON)
    @Tag(name = "Remove uma ordem de servico existente", description = "Remove uma ordem de servico existente")
    public Response deletarOs(@PathParam("os") Long os) {
        try {
            // Deleta o cliente usando o serviço
            osService.deletarOs(os);

            return Response.noContent().build(); // Retorna 204 No Content
        } catch (Exception e) {
            // Verifica se a mensagem de erro contém informações sobre não encontrado
            if (e.getMessage() != null && e.getMessage().contains("Ordem de Servico não encontrada")) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Ordem de servico não encontrada com o código: " + os)
                        .build();
            }
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Erro ao deletar ordem de servico: " + e.getMessage())
                    .build();
        }
    }



    public static OsEntity mapToEntity(OsDTO osDTO) {
        if (osDTO == null) {
            return null; // Retorna null se o DTO for nulo
        }

        OsEntity osEntity = new OsEntity();

        osEntity.setOs(osDTO.getOs());
        osEntity.setData(osDTO.getData());
        osEntity.setHora(osDTO.getHora());
        osEntity.setFantasia(osDTO.getFantasia().toUpperCase());
        osEntity.setRzsocial(osDTO.getRzsocial().toUpperCase());
        osEntity.setCodcli(osDTO.getCodcli());
        osEntity.setAtendente(osDTO.getAtendente().toUpperCase());
        osEntity.setStatus(osDTO.getStatus().toUpperCase());
        osEntity.setPrioridade(osDTO.getPrioridade());
        osEntity.setEquipamento(osDTO.getEquipamento().toUpperCase());
        osEntity.setMarca(osDTO.getMarca().toUpperCase());
        osEntity.setModelo(osDTO.getModelo().toUpperCase());
        osEntity.setTopologia(osDTO.getTopologia().toUpperCase());
        osEntity.setCor(osDTO.getCor().toUpperCase());
        osEntity.setSerial(osDTO.getSerial());
        osEntity.setPatrimonio(osDTO.getPatrimonio().toUpperCase());
        osEntity.setObs1(osDTO.getObs1().toUpperCase());
        osEntity.setObs2(osDTO.getObs2().toUpperCase());
        osEntity.setDefeito1(osDTO.getDefeito1().toUpperCase());
        osEntity.setDefeito2(osDTO.getDefeito2().toUpperCase());
        osEntity.setDefeito3(osDTO.getDefeito3().toUpperCase());
        osEntity.setTarefa1(osDTO.getTarefa1().toUpperCase());
        osEntity.setTarefa2(osDTO.getTarefa2().toUpperCase());
        osEntity.setTarefa3(osDTO.getTarefa3().toUpperCase());
        osEntity.setBaterias(osDTO.getBaterias().toUpperCase());
        osEntity.setMaterial2(osDTO.getMaterial2().toUpperCase());
        osEntity.setMaterial3(osDTO.getMaterial3().toUpperCase());
        osEntity.setOutrositens(osDTO.getOutrositens().toUpperCase());
        osEntity.setQt1(osDTO.getQt1());
        osEntity.setQt2(osDTO.getQt2());
        osEntity.setQt3(osDTO.getQt3());
        osEntity.setQtoutrositens(osDTO.getQtoutrositens());
        osEntity.setQtdeslocamento(osDTO.getQtdeslocamento());
        osEntity.setValorunit1(osDTO.getValorunit1());
        osEntity.setValorunit2(osDTO.getValorunit2());
        osEntity.setValorunit3(osDTO.getValorunit3());
        osEntity.setValorunitoutrositens(osDTO.getValorunitoutrositens());
        osEntity.setValorunitariodeslocamento(osDTO.getValorunitariodeslocamento());
        osEntity.setValortotal1(osDTO.getValortotal1());
        osEntity.setValortotal2(osDTO.getValortotal2());
        osEntity.setValortotal3(osDTO.getValortotal3());
        osEntity.setValortotaloutrositens(osDTO.getValortotaloutrositens());
        osEntity.setValortotaldeslocamento(osDTO.getValortotaldeslocamento());
        osEntity.setValorassistencia(osDTO.getValorassistencia());
        osEntity.setTotalbruto(osDTO.getTotalbruto());
        osEntity.setValordesconto(osDTO.getValordesconto());
        osEntity.setDesconto(osDTO.getDesconto());
        osEntity.setTotaliq(osDTO.getTotaliq());
        osEntity.setFechamento(osDTO.getFechamento());
        osEntity.setTecnico(osDTO.getTecnico().toUpperCase());
        osEntity.setGarantia(osDTO.getGarantia().toUpperCase());
        osEntity.setDataorca(osDTO.getDataorca());
        osEntity.setOsoriginal(osDTO.getOsoriginal());
        osEntity.setVenda(osDTO.getVenda().toUpperCase());
        osEntity.setModelofake(osDTO.getModelofake().toUpperCase());
        osEntity.setObs3(osDTO.getObs3().toUpperCase());
        osEntity.setJaimp(osDTO.getJaimp().toUpperCase());
        osEntity.setTop10(osDTO.getTop10());
        osEntity.setDataretro(osDTO.getDataretro());

        return osEntity;
    }



}
