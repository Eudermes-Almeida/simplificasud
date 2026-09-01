package somonitores.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import somonitores.dto.ResumoJovensDTO;
import somonitores.entity.ResumoJovensEntity;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@ApplicationScoped
public class ResumoJovensService {

    private static final String UNIDADE_MAE = "ESTACA BETIM";

    @Inject
    EntityManager entityManager;

    @Transactional
    public List<ResumoJovensDTO> buscaTodosResumoJovens() throws Exception {
        try {
            List<ResumoJovensEntity> resumoJovensEntities = entityManager.createQuery(
                            "SELECT r FROM ResumoJovensEntity r ORDER BY r.unidade ASC, r.id ASC",
                            ResumoJovensEntity.class)
                    .getResultList();

            if (resumoJovensEntities.isEmpty()) {
                throw new Exception("Nenhum resumo de jovens encontrado");
            }

            return resumoJovensEntities.stream()
                    .filter(Objects::nonNull)
                    .map(this::mapToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new Exception("Erro ao buscar resumo de jovens: " + e.getMessage());
        }
    }

    @Transactional
    public List<ResumoJovensDTO> buscaResumoJovensPorUnidade(String unidade) throws Exception {
        String unidadeTratada = unidade.trim();

        // "Estaca Betim" é a unidade mãe (soma de todas as alas/ramos) e não existe como
        // linha própria na tabela — pedir por ela equivale a trazer todos os registros.
        if (UNIDADE_MAE.equalsIgnoreCase(unidadeTratada)) {
            return buscaTodosResumoJovens();
        }

        String jpql = "SELECT r FROM ResumoJovensEntity r " +
                "WHERE UPPER(r.unidade) = UPPER(:unidade) " +
                "ORDER BY r.id ASC";

        List<ResumoJovensEntity> resumoJovensEntities = entityManager.createQuery(jpql, ResumoJovensEntity.class)
                .setParameter("unidade", unidadeTratada)
                .getResultList();

        return resumoJovensEntities.stream()
                .filter(Objects::nonNull)
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private ResumoJovensDTO mapToDTO(ResumoJovensEntity entity) {
        return ResumoJovensDTO.builder()
                .id(entity.getId())
                .unidade(entity.getUnidade())
                .rapazesTotal(entity.getRapazesTotal())
                .rapazesAtivos(entity.getRapazesAtivos())
                .mocasTotal(entity.getMocasTotal())
                .mocasAtivas(entity.getMocasAtivas())
                .criancas0A2(entity.getCriancas0A2())
                .criancas3A11Potencial(entity.getCriancas3A11Potencial())
                .criancasTotalAtivas(entity.getCriancasTotalAtivas())
                .totalCriancas(entity.getTotalCriancas())
                .totalMatriculadosSeminario(entity.getTotalMatriculadosSeminario())
                .frequenciaAcima75(entity.getFrequenciaAcima75())
                .rapazesRecomendacaoBatisterio(entity.getRapazesRecomendacaoBatisterio())
                .mocasRecomendacaoBatisterio(entity.getMocasRecomendacaoBatisterio())
                .build();
    }
}
