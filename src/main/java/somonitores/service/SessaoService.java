package somonitores.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import somonitores.entity.LideresEntity;
import somonitores.entity.SessaoEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class SessaoService {

    // Decisão explícita do usuário (2026-09-02): expira 60 min após o login, contado a
    // partir de "criado_em" (janela fixa, não renovada a cada requisição).
    private static final long DURACAO_SESSAO_MINUTOS = 60;

    @Inject
    EntityManager entityManager;

    // UUID aleatório (122 bits de entropia) como token opaco -- não é JWT, não carrega
    // claim nenhum, só identifica a linha em "sessoes". Ver memória project-raiox-auth-design
    // pela decisão de usar sessão em banco em vez de token autocontido.
    @Transactional
    public String criarSessao(Long liderId) {
        String token = UUID.randomUUID().toString();

        SessaoEntity sessao = SessaoEntity.builder()
                .liderId(liderId)
                .token(token)
                .criadoEm(LocalDateTime.now())
                .build();

        entityManager.persist(sessao);

        return token;
    }

    // Usado pelo filtro central de autorização e por qualquer endpoint que precise
    // resolver "quem é o dono deste token". Sessão expirada é tratada exatamente como
    // token inexistente (Optional.empty()) -- e a linha é apagada aqui mesmo, não fica
    // como lixo esperando um logout que nunca vai vir.
    @Transactional
    public Optional<LideresEntity> buscaLiderPorToken(String token) {
        if (token == null || token.isBlank()) {
            return Optional.empty();
        }

        List<SessaoEntity> sessoes = entityManager.createQuery(
                        "SELECT s FROM SessaoEntity s WHERE s.token = :token",
                        SessaoEntity.class)
                .setParameter("token", token.trim())
                .getResultList();

        if (sessoes.isEmpty()) {
            return Optional.empty();
        }

        SessaoEntity sessao = sessoes.get(0);
        LocalDateTime expiraEm = sessao.getCriadoEm().plusMinutes(DURACAO_SESSAO_MINUTOS);

        if (LocalDateTime.now().isAfter(expiraEm)) {
            entityManager.remove(sessao);
            return Optional.empty();
        }

        return Optional.ofNullable(entityManager.find(LideresEntity.class, sessao.getLiderId()));
    }

    // Logout é idempotente -- apagar um token que já não existe (ou nunca existiu) não é erro.
    @Transactional
    public void encerrarSessao(String token) {
        if (token == null || token.isBlank()) {
            return;
        }
        entityManager.createQuery("DELETE FROM SessaoEntity s WHERE s.token = :token")
                .setParameter("token", token.trim())
                .executeUpdate();
    }

}
