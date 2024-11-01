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
import somonitores.dto.ParametroDTO;
import somonitores.entity.ParametroEntity;
import somonitores.service.ParametroService;

import java.util.List;

@Path("/parametro")
public class ParametroResource {
    @Inject
    ParametroService parametroService;

    @GET
    @Path("/parametro")
    @Produces(MediaType.APPLICATION_JSON)
    @Tag(name = "Busca parametros pelo nome do parametro", description = "Busca Clientes por parte do nome Fantasia")
    public Response buscaParametroPorNome(@QueryParam("parametro") String parametro) {
        try {

            System.out.println(parametro + "**************************************");
            List<ParametroDTO> parametros = parametroService.buscaParametroPorNome(parametro);

            if (parametros.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Nenhum valor encontrado para este parametro: " + parametro)
                        .build();
            }

            return Response.ok(parametros).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao buscar parametro " + e.getMessage())
                    .build();
        }
    }

//    @Path("/parametro")
//    @GET
//    @Tag(name = "Busca parametro", description = "Busca todos os parametros")
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Produces(MediaType.APPLICATION_JSON)
//    @APIResponses(value = {
//            @APIResponse(responseCode = "204", description = "Nenhum parametro encontrado", content = @Content(schema = @Schema(implementation = ParametroDTO.class))),
//            @APIResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content(schema = @Schema(implementation = ParametroDTO.class))),
//    })
//    @Operation(summary = "Busca todos os parametros", description = "Buscar todos os parametro")
//    public List<ParametroDTO> buscaTodosParametros() throws Exception {
//
//        System.out.println(" buscando todos os parametros *******************************");
//        List<ParametroDTO> parametroDTOS = parametroService.buscaTodosParametros();
//
//        return parametroDTOS;
//    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Tag(name = "Persiste um novo parametro no banco", description = "Persiste um novo parâmentro ")
    public Response criarNovoParametro(ParametroDTO parametroDTO) {
        try {

            // DTO para Entity:
            ParametroEntity parametroEntity = mapToEntity(parametroDTO);

            // Salva o cliente usando o serviço
            ParametroEntity parametroPersistido = parametroService.criarParametro(parametroEntity);

            return Response.status(Response.Status.CREATED)
                    .entity(parametroPersistido) // Retorna o DTO criado com status 201
                    .build();
        } catch (Exception e) {

            return Response.status(Response.Status.BAD_REQUEST)
                    //Se chegar um id repetido no banco de dados, ele não aceita a persistência e dá esta excessão
                    .entity("Erro ao criar nova ordem de servico: já existe uma ordem com o mesmo ID " + e.getMessage())
                    .build();
        }
    }

    public static ParametroEntity mapToEntity(ParametroDTO parametroDTO) {
        if (parametroDTO == null) {
            return null; // Retorna null se o DTO for nulo
        }
        ParametroEntity parametroEntity = new ParametroEntity();
        parametroEntity.setParametro(parametroDTO.getParametro());
        parametroEntity.setValor(parametroDTO.getValor());

        return parametroEntity;

    }

    @DELETE
    @Path("/parametro/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Tag(name = "Remove um parametro existente", description = "Remove um parametro existente do banco de dados")
    public Response deletarCliente(@PathParam("id") Long id) {
        try {

            parametroService.deletarParametro(id);

            return Response.noContent().build();
        } catch (Exception e) {
            // Verifica se a mensagem de erro contém informações sobre não encontrado
            if (e.getMessage() != null && e.getMessage().contains("Parametro não encontrado")) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Parametro não encontrado com o código: " + id)
                        .build();
            }
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Erro ao deletar parametro: " + e.getMessage())
                    .build();
        }
    }

    @PUT
    @Path("/parametro/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Tag(name = "Atualiza um parametro existente", description = "Atualiza os dados de um parametro existente no banco de dados")
    public Response atualizarParametro(@PathParam("id") Long id, ParametroDTO parametroDTO) {
        try {

            // transforma o DTO em Entity:
            ParametroEntity parametroEntity = mapToEntity(parametroDTO);

            // Atualiza o cliente usando o serviço
            ParametroEntity parametro = parametroService.atualizarParametro(id, parametroEntity);

            return Response.ok(parametro) // Retorna o cliente atualizado com status 200
                    .build();
        } catch (Exception e) {
            // Verifica se a mensagem de erro contém informações sobre não encontrado
            if (e.getMessage() != null && e.getMessage().contains("Parametro não encontrado")) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Parametro não encontrado com o código: " + id)
                        .build();
            }
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Erro ao atualizar parametro: " + e.getMessage())
                    .build();
        }
    }


}
