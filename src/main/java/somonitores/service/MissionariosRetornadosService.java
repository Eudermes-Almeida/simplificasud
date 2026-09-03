package somonitores.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import somonitores.dto.MissionariosRetornadosDTO;
import somonitores.entity.MissionariosRetornadosEntity;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@ApplicationScoped
public class MissionariosRetornadosService {

    private static final String UNIDADE_MAE = "ESTACA BETIM";

    @Inject
    EntityManager entityManager;

    @Transactional
    public List<MissionariosRetornadosDTO> buscaTodosMissionariosRetornados() throws Exception {
        try {
            List<MissionariosRetornadosEntity> missionariosRetornadosEntities = entityManager.createQuery(
                            "SELECT m FROM MissionariosRetornadosEntity m ORDER BY m.unidade ASC, m.nome ASC",
                            MissionariosRetornadosEntity.class)
                    .getResultList();

            if (missionariosRetornadosEntities.isEmpty()) {
                throw new Exception("Nenhum missionário retornado encontrado");
            }

            return missionariosRetornadosEntities.stream()
                    .filter(Objects::nonNull)
                    .map(this::mapToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new Exception("Erro ao buscar missionários retornados: " + e.getMessage());
        }
    }

    @Transactional
    public List<MissionariosRetornadosDTO> buscaMissionariosRetornadosPorUnidade(String unidade) throws Exception {
        String unidadeTratada = unidade.trim();

        // "Estaca Betim" é a unidade mãe (soma de todas as alas/ramos) e não existe como
        // linha própria na tabela — pedir por ela equivale a trazer todos os registros.
        if (UNIDADE_MAE.equalsIgnoreCase(unidadeTratada)) {
            return buscaTodosMissionariosRetornados();
        }

        String jpql = "SELECT m FROM MissionariosRetornadosEntity m " +
                "WHERE UPPER(m.unidade) = UPPER(:unidade) " +
                "ORDER BY m.nome ASC";

        List<MissionariosRetornadosEntity> missionariosRetornadosEntities = entityManager.createQuery(jpql, MissionariosRetornadosEntity.class)
                .setParameter("unidade", unidadeTratada)
                .getResultList();

        return missionariosRetornadosEntities.stream()
                .filter(Objects::nonNull)
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private MissionariosRetornadosDTO mapToDTO(MissionariosRetornadosEntity entity) {
        return MissionariosRetornadosDTO.builder()
                .id(entity.getId())
                .unidade(entity.getUnidade())
                .nome(entity.getNome())
                .sexo(entity.getSexo())
                .idade(entity.getIdade())
                .solteiro(entity.getSolteiro())
                .selado(entity.getSelado())
                .matriculadoinstituto(entity.getMatriculadoinstituto())
                .recomendacaotemplo(entity.getRecomendacaotemplo())
                .chamado(entity.getChamado())
                .paismissao(entity.getPaismissao())
                .build();
    }
}
