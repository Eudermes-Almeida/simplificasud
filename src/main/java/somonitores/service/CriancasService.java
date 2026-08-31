package somonitores.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import somonitores.dto.CriancasDTO;
import somonitores.entity.CriancasEntity;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@ApplicationScoped
public class CriancasService {

    private static final String UNIDADE_MAE = "ESTACA BETIM";

    @Inject
    EntityManager entityManager;

    @Transactional
    public List<CriancasDTO> buscaTodasCriancas() throws Exception {
        try {
            List<CriancasEntity> criancasEntities = entityManager.createQuery(
                            "SELECT c FROM CriancasEntity c ORDER BY c.unidade ASC, c.nome ASC",
                            CriancasEntity.class)
                    .getResultList();

            if (criancasEntities.isEmpty()) {
                throw new Exception("Nenhuma criança encontrada");
            }

            return criancasEntities.stream()
                    .filter(Objects::nonNull)
                    .map(this::mapToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new Exception("Erro ao buscar crianças: " + e.getMessage());
        }
    }

    @Transactional
    public List<CriancasDTO> buscaCriancasPorUnidade(String unidade) throws Exception {
        String unidadeTratada = unidade.trim();

        // "Estaca Betim" é a unidade mãe (soma de todas as alas/ramos) e não existe como
        // linha própria na tabela — pedir por ela equivale a trazer todos os registros.
        if (UNIDADE_MAE.equalsIgnoreCase(unidadeTratada)) {
            return buscaTodasCriancas();
        }

        String jpql = "SELECT c FROM CriancasEntity c " +
                "WHERE UPPER(c.unidade) = UPPER(:unidade) " +
                "ORDER BY c.nome ASC";

        List<CriancasEntity> criancasEntities = entityManager.createQuery(jpql, CriancasEntity.class)
                .setParameter("unidade", unidadeTratada)
                .getResultList();

        return criancasEntities.stream()
                .filter(Objects::nonNull)
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private CriancasDTO mapToDTO(CriancasEntity entity) {
        return CriancasDTO.builder()
                .id(entity.getId())
                .unidade(entity.getUnidade())
                .nome(entity.getNome())
                .sexo(entity.getSexo())
                .idade(entity.getIdade())
                .build();
    }
}
