package somonitores.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import somonitores.dto.HomensPreparadosDTO;
import somonitores.entity.HomensPreparadosEntity;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@ApplicationScoped
public class HomensPreparadosService {

    private static final String UNIDADE_MAE = "ESTACA BETIM";

    @Inject
    EntityManager entityManager;

    @Transactional
    public List<HomensPreparadosDTO> buscaTodosHomensPreparados() throws Exception {
        try {
            List<HomensPreparadosEntity> homensEntities = entityManager.createQuery(
                            "SELECT h FROM HomensPreparadosEntity h ORDER BY h.unidade ASC, h.nome ASC",
                            HomensPreparadosEntity.class)
                    .getResultList();

            if (homensEntities.isEmpty()) {
                throw new Exception("Nenhum homem sendo preparado encontrado");
            }

            return homensEntities.stream()
                    .filter(Objects::nonNull)
                    .map(this::mapToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new Exception("Erro ao buscar homens sendo preparados: " + e.getMessage());
        }
    }

    @Transactional
    public List<HomensPreparadosDTO> buscaHomensPreparadosPorUnidade(String unidade) throws Exception {
        String unidadeTratada = unidade.trim();

        // "Estaca Betim" é a unidade mãe (soma de todas as alas/ramos) e não existe como
        // linha própria na tabela — pedir por ela equivale a trazer todos os registros.
        if (UNIDADE_MAE.equalsIgnoreCase(unidadeTratada)) {
            return buscaTodosHomensPreparados();
        }

        String jpql = "SELECT h FROM HomensPreparadosEntity h " +
                "WHERE UPPER(h.unidade) = UPPER(:unidade) " +
                "ORDER BY h.nome ASC";

        List<HomensPreparadosEntity> homensEntities = entityManager.createQuery(jpql, HomensPreparadosEntity.class)
                .setParameter("unidade", unidadeTratada)
                .getResultList();

        return homensEntities.stream()
                .filter(Objects::nonNull)
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private HomensPreparadosDTO mapToDTO(HomensPreparadosEntity entity) {
        return HomensPreparadosDTO.builder()
                .id(entity.getId())
                .unidade(entity.getUnidade())
                .nome(entity.getNome())
                .idade(entity.getIdade())
                .ativo(entity.getAtivo())
                .build();
    }
}
