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
import somonitores.service.ContextoAutenticacao;
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
    // Prefixo, não igualdade exata: desde 2026-09-18 escopo guarda Estaca-A/Estaca-B/
    // Ala-A/Ala-B (ver memória project-raiox-matriz-acesso-ab-design), então "Estaca"
    // sozinho não aparece mais pra líder nenhum. O sufixo A/B não influencia esta
    // checagem de unidade -- só Estaca vs Ala importa aqui.
    private static final String ESCOPO_ESTACA_PREFIXO = "ESTACA";

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

    @Inject
    ContextoAutenticacao contextoAutenticacao;

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
        contextoAutenticacao.setLiderAtual(lider);
        String unidadeSolicitada = uriInfo.getQueryParameters().getFirst("unidade");

        if (unidadeSolicitada != null) {
            boolean escopoTotal = lider.getEscopo() != null
                    && lider.getEscopo().trim().toUpperCase().startsWith(ESCOPO_ESTACA_PREFIXO);
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
