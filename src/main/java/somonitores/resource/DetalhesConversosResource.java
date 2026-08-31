package somonitores.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import somonitores.dto.DetalhesConversosDTO;
import somonitores.service.DetalhesConversosService;

import java.util.List;

@Path("/detalhesconversos")
public class DetalhesConversosResource {

    @Inject
    DetalhesConversosService detalhesConversosService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Tag(name = "Busca Detalhes de Conversos", description = "Busca Detalhes de Conversos por unidade")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Detalhes de conversos encontrados (pode ser lista vazia se a unidade não tiver registros)", content = @Content(schema = @Schema(implementation = DetalhesConversosDTO.class))),
            @APIResponse(responseCode = "400", description = "Parâmetro 'unidade' não informado", content = @Content(schema = @Schema(implementation = DetalhesConversosDTO.class))),
            @APIResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content(schema = @Schema(implementation = DetalhesConversosDTO.class))),
    })
    @Operation(summary = "Busca Detalhes de Conversos por unidade", description = "Busca os detalhes de conversos de uma unidade específica. Informar 'Estaca Betim' retorna os dados de todas as unidades, já que a estaca é a soma de todas as alas/ramos.")
    public Response buscaDetalhesConversosPorUnidade(@QueryParam("unidade") String unidade) {
        try {
            if (unidade == null || unidade.trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("O parâmetro 'unidade' é obrigatório")
                        .build();
            }

            List<DetalhesConversosDTO> detalhes = detalhesConversosService.buscaDetalhesConversosPorUnidade(unidade);

            return Response.ok(detalhes).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao buscar detalhes de conversos: " + e.getMessage())
                    .build();
        }
    }

}
