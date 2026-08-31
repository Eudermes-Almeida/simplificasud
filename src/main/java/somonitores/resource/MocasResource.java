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
import somonitores.dto.MocasDTO;
import somonitores.service.MocasService;

import java.util.List;

@Path("/mocas")
public class MocasResource {

    @Inject
    MocasService mocasService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Tag(name = "Busca Moças", description = "Busca Moças por unidade")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Moças encontradas (pode ser lista vazia se a unidade não tiver registros)", content = @Content(schema = @Schema(implementation = MocasDTO.class))),
            @APIResponse(responseCode = "400", description = "Parâmetro 'unidade' não informado", content = @Content(schema = @Schema(implementation = MocasDTO.class))),
            @APIResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content(schema = @Schema(implementation = MocasDTO.class))),
    })
    @Operation(summary = "Busca Moças por unidade", description = "Busca as moças de uma unidade específica. Informar 'Estaca Betim' retorna os dados de todas as unidades, já que a estaca é a soma de todas as alas/ramos.")
    public Response buscaMocasPorUnidade(@QueryParam("unidade") String unidade) {
        try {
            if (unidade == null || unidade.trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("O parâmetro 'unidade' é obrigatório")
                        .build();
            }

            List<MocasDTO> mocas = mocasService.buscaMocasPorUnidade(unidade);

            return Response.ok(mocas).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao buscar moças: " + e.getMessage())
                    .build();
        }
    }

}
