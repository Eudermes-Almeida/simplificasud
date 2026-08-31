package somonitores.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import somonitores.dto.FrequenciaSacramentalDTO;
import somonitores.entity.FrequenciaSacramentalEntity;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@ApplicationScoped
public class FrequenciaSacramentalService {

    @Inject
    EntityManager entityManager;

    private static final String UNIDADE_MAE = "ESTACA BETIM";

    @Transactional
    public List<FrequenciaSacramentalDTO> buscaTodasFrequenciasSacramentais() throws Exception {
        try {
            List<FrequenciaSacramentalEntity> frequenciasEntities = entityManager.createQuery(
                            "SELECT f FROM FrequenciaSacramentalEntity f ORDER BY f.unidade ASC, f.anomes ASC",
                            FrequenciaSacramentalEntity.class)
                    .getResultList();

            if (frequenciasEntities.isEmpty()) {
                throw new Exception("Nenhuma frequência sacramental encontrada");
            }

            return frequenciasEntities.stream()
                    .filter(Objects::nonNull)
                    .map(this::mapToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new Exception("Erro ao buscar frequências sacramentais: " + e.getMessage());
        }
    }

    @Transactional
    public List<FrequenciaSacramentalDTO> buscaFrequenciasSacramentaisPorUnidade(String unidade) throws Exception {
        String unidadeTratada = unidade.trim();

        // "Estaca Betim" é a unidade mãe (soma de todas as alas/ramos) e não existe como
        // linha própria na tabela — pedir por ela equivale a trazer todos os registros.
        if (UNIDADE_MAE.equalsIgnoreCase(unidadeTratada)) {
            return buscaTodasFrequenciasSacramentais();
        }

        String jpql = "SELECT f FROM FrequenciaSacramentalEntity f " +
                "WHERE UPPER(f.unidade) = UPPER(:unidade) " +
                "ORDER BY f.anomes ASC";

        List<FrequenciaSacramentalEntity> frequenciasEntities = entityManager.createQuery(jpql, FrequenciaSacramentalEntity.class)
                .setParameter("unidade", unidadeTratada)
                .getResultList();

        return frequenciasEntities.stream()
                .filter(Objects::nonNull)
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private FrequenciaSacramentalDTO mapToDTO(FrequenciaSacramentalEntity entity) {
        return FrequenciaSacramentalDTO.builder()
                .id(entity.getId())
                .unidade(entity.getUnidade())
                .anomes(entity.getAnomes())
                .frequencia(entity.getFrequencia())
                .build();
    }
}
