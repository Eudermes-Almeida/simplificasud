package somonitores.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import somonitores.dto.LiderAdminDTO;
import somonitores.service.LideresService;

import java.util.Optional;

// CRUD de perfis para a tela de administração (template secreto, acessível só pelo perfil
// de escopo "Master" -- ver memória project-raiox-admin-perfis). O AutorizacaoFilter já
// garante que só uma sessão com escopo Master chega até aqui, então este Resource não
// repete essa checagem.
@Path("/lideres/admin")
public class LideresAdminResource {

    @Inject
    LideresService lideresService;

    @GET
    @Path("/{registromembro}")
    @Produces(MediaType.APPLICATION_JSON)
    @Tag(name = "Administração de Perfis", description = "CRUD de líderes para o perfil Master")
    @Operation(summary = "Busca um líder pelo registro de membro completo", description = "Retorna todos os campos (inclusive registromembro e nascimento), diferente do endpoint público de leitura.")
    public Response buscarPorRegistroMembro(@PathParam("registromembro") String registromembro) {
        Optional<LiderAdminDTO> encontrado = lideresService.buscaAdminPorRegistroMembro(registromembro);
        return encontrado.<Response>map(dto -> Response.ok(dto).build())
                .orElseGet(() -> Response.status(Response.Status.NOT_FOUND).build());
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Tag(name = "Administração de Perfis")
    @Operation(summary = "Cria um novo perfil de líder")
    public Response criar(LiderAdminDTO dto) {
        Response erroValidacao = validar(dto);
        if (erroValidacao != null) {
            return erroValidacao;
        }

        if (lideresService.registroMembroEmUsoPorOutro(dto.getRegistromembro(), null)) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("Já existe um perfil com este registro de membro.")
                    .build();
        }

        LiderAdminDTO criado = lideresService.criarLider(dto);
        return Response.status(Response.Status.CREATED).entity(criado).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Tag(name = "Administração de Perfis")
    @Operation(summary = "Atualiza os dados de um perfil de líder existente")
    public Response atualizar(@PathParam("id") Long id, LiderAdminDTO dto) {
        Response erroValidacao = validar(dto);
        if (erroValidacao != null) {
            return erroValidacao;
        }

        if (lideresService.registroMembroEmUsoPorOutro(dto.getRegistromembro(), id)) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("Já existe outro perfil com este registro de membro.")
                    .build();
        }

        return lideresService.atualizarLider(id, dto)
                .<Response>map(atualizado -> Response.ok(atualizado).build())
                .orElseGet(() -> Response.status(Response.Status.NOT_FOUND).build());
    }

    @PATCH
    @Path("/{id}/revogar-acesso")
    @Tag(name = "Administração de Perfis")
    @Operation(summary = "Revoga o acesso de um líder", description = "Não apaga o perfil -- só esvazia o escopo, o que passa a bloquear o login dessa pessoa.")
    public Response revogarAcesso(@PathParam("id") Long id) {
        boolean encontrado = lideresService.revogarAcesso(id);
        return encontrado ? Response.ok().build() : Response.status(Response.Status.NOT_FOUND).build();
    }

    private Response validar(LiderAdminDTO dto) {
        if (dto == null
                || isVazio(dto.getNome())
                || isVazio(dto.getRegistromembro())
                || isVazio(dto.getUnidade())
                || isVazio(dto.getEscopo())
                || dto.getNascimento() == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Informe nome, registro de membro, unidade, escopo e data de nascimento.")
                    .build();
        }
        return null;
    }

    private boolean isVazio(String valor) {
        return valor == null || valor.trim().isEmpty();
    }

}
