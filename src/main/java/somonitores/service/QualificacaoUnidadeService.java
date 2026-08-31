package somonitores.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import somonitores.dto.QualificacaoUnidadeDTO;
import somonitores.entity.QualificacaoUnidadeEntity;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@ApplicationScoped
public class QualificacaoUnidadeService {

    private static final String UNIDADE_MAE = "ESTACA BETIM";

    @Inject
    EntityManager entityManager;

    @Transactional
    public List<QualificacaoUnidadeDTO> buscaTodasQualificacoesUnidade() throws Exception {
        try {
            List<QualificacaoUnidadeEntity> qualificacoesEntities = entityManager.createQuery(
                            "SELECT q FROM QualificacaoUnidadeEntity q ORDER BY q.unidade ASC",
                            QualificacaoUnidadeEntity.class)
                    .getResultList();

            if (qualificacoesEntities.isEmpty()) {
                throw new Exception("Nenhuma qualificação de unidade encontrada");
            }

            return qualificacoesEntities.stream()
                    .filter(Objects::nonNull)
                    .map(this::mapToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new Exception("Erro ao buscar qualificações de unidade: " + e.getMessage());
        }
    }

    @Transactional
    public List<QualificacaoUnidadeDTO> buscaQualificacoesUnidadePorUnidade(String unidade) throws Exception {
        String unidadeTratada = unidade.trim();

        // "Estaca Betim" é a unidade mãe (soma de todas as alas/ramos) e não existe como
        // linha própria na tabela — pedir por ela equivale a trazer todos os registros.
        if (UNIDADE_MAE.equalsIgnoreCase(unidadeTratada)) {
            return buscaTodasQualificacoesUnidade();
        }

        String jpql = "SELECT q FROM QualificacaoUnidadeEntity q " +
                "WHERE UPPER(q.unidade) = UPPER(:unidade)";

        List<QualificacaoUnidadeEntity> qualificacoesEntities = entityManager.createQuery(jpql, QualificacaoUnidadeEntity.class)
                .setParameter("unidade", unidadeTratada)
                .getResultList();

        return qualificacoesEntities.stream()
                .filter(Objects::nonNull)
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private QualificacaoUnidadeDTO mapToDTO(QualificacaoUnidadeEntity entity) {
        return QualificacaoUnidadeDTO.builder()
                .id(entity.getId())
                .unidade(entity.getUnidade())
                .totalMembros(entity.getTotalMembros())
                .frequenciaSacramental(entity.getFrequenciaSacramental())
                .dizimistasIntegrais(entity.getDizimistasIntegrais())
                .build();
    }
}
