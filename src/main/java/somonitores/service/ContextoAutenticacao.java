package somonitores.service;

import jakarta.enterprise.context.RequestScoped;
import somonitores.entity.LideresEntity;

// Guarda o líder autenticado da requisição atual (populado pelo AutorizacaoFilter, que já
// resolve o token pra unidade/escopo) pra qualquer service consultar o nível de acesso sem
// precisar re-resolver o token. @RequestScoped: uma instância por requisição HTTP.
//
// Nível B = "Conselho"/"Professores" (Estaca-B ou Ala-B, ver aba "matriz acesso" da
// planilha matriz de acesso chamados estaca inteira.xlsx e memória
// project-raiox-matriz-acesso-ab-design) — restringe campos de status de recomendação e
// alguns cards inteiros. Nível A (Presidência/Bispado) e Estaca continuam com acesso total
// nesses módulos; só muda a granularidade dentro do escopo de unidade que o filtro já
// aplicava (Ala vs Estaca não mudou).
@RequestScoped
public class ContextoAutenticacao {

    private LideresEntity liderAtual;

    public void setLiderAtual(LideresEntity liderAtual) {
        this.liderAtual = liderAtual;
    }

    public LideresEntity getLiderAtual() {
        return liderAtual;
    }

    public boolean isNivelB() {
        if (liderAtual == null || liderAtual.getEscopo() == null) {
            return false;
        }
        return liderAtual.getEscopo().trim().toUpperCase().endsWith("-B");
    }
}
