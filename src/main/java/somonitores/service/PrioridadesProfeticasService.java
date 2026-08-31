package somonitores.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import somonitores.dto.PrioridadesProfeticasDTO;
import somonitores.entity.PrioridadesProfeticasEntity;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@ApplicationScoped
public class PrioridadesProfeticasService {

    private static final String UNIDADE_MAE = "ESTACA BETIM";

    @Inject
    EntityManager entityManager;

    @Transactional
    public List<PrioridadesProfeticasDTO> buscaTodasPrioridadesProfeticas() throws Exception {
        try {
            List<PrioridadesProfeticasEntity> prioridadesEntities = entityManager.createQuery(
                            "SELECT p FROM PrioridadesProfeticasEntity p ORDER BY p.unidade ASC",
                            PrioridadesProfeticasEntity.class)
                    .getResultList();

            if (prioridadesEntities.isEmpty()) {
                throw new Exception("Nenhuma prioridade profética encontrada");
            }

            return prioridadesEntities.stream()
                    .filter(Objects::nonNull)
                    .map(this::mapToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new Exception("Erro ao buscar prioridades proféticas: " + e.getMessage());
        }
    }

    @Transactional
    public List<PrioridadesProfeticasDTO> buscaPrioridadesProfeticasPorUnidade(String unidade) throws Exception {
        String unidadeTratada = unidade.trim();

        // "Estaca Betim" é a unidade mãe (soma de todas as alas/ramos) e não existe como
        // linha própria na tabela — pedir por ela equivale a trazer todos os registros.
        if (UNIDADE_MAE.equalsIgnoreCase(unidadeTratada)) {
            return buscaTodasPrioridadesProfeticas();
        }

        String jpql = "SELECT p FROM PrioridadesProfeticasEntity p " +
                "WHERE UPPER(p.unidade) = UPPER(:unidade)";

        List<PrioridadesProfeticasEntity> prioridadesEntities = entityManager.createQuery(jpql, PrioridadesProfeticasEntity.class)
                .setParameter("unidade", unidadeTratada)
                .getResultList();

        return prioridadesEntities.stream()
                .filter(Objects::nonNull)
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private PrioridadesProfeticasDTO mapToDTO(PrioridadesProfeticasEntity entity) {
        return PrioridadesProfeticasDTO.builder()
                .id(entity.getId())
                .unidade(entity.getUnidade())
                .frequencia(entity.getFrequencia())
                .metaFrequencia(entity.getMetaFrequencia())
                .membrosParticipantes(entity.getMembrosParticipantes())
                .metaMembrosParticipantes(entity.getMetaMembrosParticipantes())
                .membrosRetornando(entity.getMembrosRetornando())
                .metaMembrosRetornando(entity.getMetaMembrosRetornando())
                .membrosJejuando(entity.getMembrosJejuando())
                .metaMembrosJejuando(entity.getMetaMembrosJejuando())
                .batismosConversos(entity.getBatismosConversos())
                .metaBatismosConversos(entity.getMetaBatismosConversos())
                .missionarios(entity.getMissionarios())
                .metaMissionarios(entity.getMetaMissionarios())
                .recomendacaoTemplo(entity.getRecomendacaoTemplo())
                .metaRecomendacaoTemplo(entity.getMetaRecomendacaoTemplo())
                .recomendacaoBatisterio(entity.getRecomendacaoBatisterio())
                .metaRecomendacaoBatisterio(entity.getMetaRecomendacaoBatisterio())
                .build();
    }
}
