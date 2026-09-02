package somonitores.filter;

import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.Provider;
import somonitores.entity.LideresEntity;
import somonitores.service.SessaoService;

import java.util.Optional;
import java.util.Set;

// Filtro central de autorização -- roda na frente de TODOS os endpoints da API, sem
// precisar alterar nenhum dos 10 Resources originais (frequenciasacramental, rapazes,
// etc). @Provider sem @NameBinding aplica globalmente por padrão no JAX-RS/RESTEasy
// Reactive. Ver memória project-raiox-auth-design pelo desenho completo (sessão simples
// em tabela, não JWT).
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AutorizacaoFilter implements ContainerRequestFilter {

    private static final String HEADER_TOKEN = "X-Auth-Token";
    private static final String ESCOPO_ESTACA = "ESTACA";

    // Rotas que não exigem sessão: login em si, e o fluxo de primeiro acesso completo
    // (roda ANTES de existir qualquer token), além da listagem segura de líderes
    // (LideresDTO já não expõe nada sensível, ver memória).
    private static final Set<String> CAMINHOS_LIVRES = Set.of(
            "/auth/login",
            "/auth/logout",
            "/lideres",
            "/lideres/identificar",
            "/lideres/cadastrar-credenciais"
    );

    @Inject
    SessaoService sessaoService;

    @Override
    public void filter(ContainerRequestContext requestContext) {
        // Preflight de CORS não deve exigir sessão -- o navegador nem manda o header
        // customizado nesse pedido.
        if ("OPTIONS".equalsIgnoreCase(requestContext.getMethod())) {
            return;
        }

        UriInfo uriInfo = requestContext.getUriInfo();
        String caminho = uriInfo.getPath();

        if (CAMINHOS_LIVRES.contains(caminho)) {
            return;
        }

        String token = requestContext.getHeaderString(HEADER_TOKEN);
        Optional<LideresEntity> liderOpt = sessaoService.buscaLiderPorToken(token);

        if (liderOpt.isEmpty()) {
            abortar(requestContext, Response.Status.UNAUTHORIZED, "Sessão inválida ou expirada. Faça login novamente.");
            return;
        }

        LideresEntity lider = liderOpt.get();
        String unidadeSolicitada = uriInfo.getQueryParameters().getFirst("unidade");

        if (unidadeSolicitada != null) {
            boolean escopoTotal = ESCOPO_ESTACA.equalsIgnoreCase(lider.getEscopo());
            boolean mesmaUnidade = lider.getUnidade() != null
                    && lider.getUnidade().equalsIgnoreCase(unidadeSolicitada.trim());

            if (!escopoTotal && !mesmaUnidade) {
                abortar(requestContext, Response.Status.FORBIDDEN, "Você não tem permissão para ver os dados desta unidade.");
            }
        }
    }

    private void abortar(ContainerRequestContext requestContext, Response.Status status, String mensagem) {
        requestContext.abortWith(
                Response.status(status)
                        .entity(mensagem)
                        .type(MediaType.TEXT_PLAIN)
                        .build());
    }
}
