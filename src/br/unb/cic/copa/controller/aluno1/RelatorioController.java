package br.unb.cic.copa.controller.aluno1;

import br.unb.cic.copa.controller.aluno2.SelecaoController;
import br.unb.cic.copa.controller.aluno4.PartidaController;
import br.unb.cic.copa.controller.aluno5.IngressosController;
import br.unb.cic.copa.model.aluno1.Usuario;
import br.unb.cic.copa.model.aluno2.Selecao;
import br.unb.cic.copa.model.aluno4.Partida;
import br.unb.cic.copa.model.aluno4.StatusPartida;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class RelatorioController {

    private static RelatorioController instancia;

    private final PartidaController    partidaController    = new PartidaController();
    private final SelecaoController    selecaoController    = new SelecaoController();
    private final UsuarioController    usuarioController    = UsuarioController.getInstancia();
    private final IngressosController  ingressosController  = new IngressosController();

    private RelatorioController() {}

    public static RelatorioController getInstancia() {
        if (instancia == null) instancia = new RelatorioController();
        return instancia;
    }

    // Retorna lista de todos os usuarios
    public List<Usuario> getUsuarios() {
        return usuarioController.listarUsuarios();
    }

    // Retorna contagem de usuarios por perfil
    public Map<String, Long> getResumoUsuarios() {
        Map<String, Long> mapa = new LinkedHashMap<>();
        for (Usuario u : usuarioController.listarUsuarios()) {
            mapa.merge(u.getFuncao(), 1L, Long::sum);
        }
        return mapa;
    }

    // Retorna total e contagem por status das partidas
    public Map<String, Integer> getResumoPartidas() {
        List<Partida> partidas = partidaController.listarTodas();
        Map<String, Integer> mapa = new LinkedHashMap<>();
        mapa.put("Total de Partidas", partidas.size());
        int agendadas = 0, andamento = 0, finalizadas = 0;
        for (Partida p : partidas) {
            if (p.getStatus() == StatusPartida.AGENDADA)          agendadas++;
            else if (p.getStatus() == StatusPartida.EM_ANDAMENTO) andamento++;
            else if (p.getStatus() == StatusPartida.FINALIZADA)   finalizadas++;
        }
        mapa.put("Agendadas",    agendadas);
        mapa.put("Em Andamento", andamento);
        mapa.put("Finalizadas",  finalizadas);
        return mapa;
    }

    // Retorna dados reais de ingressos do IngressosController
    public Map<String, String> getResumoIngressos() {
        Map<String, String> mapa = new LinkedHashMap<>();
        try {
            int total = ingressosController.getTotalIngressosVendidos();
            double receita = ingressosController.getValorTotalArrecadado();
            mapa.put("Total de Ingressos Vendidos",
                    String.valueOf(total));
            mapa.put("Receita Total",
                    String.format("R$ %.2f", receita));
        } catch (Exception e) {
            mapa.put("Total de Ingressos Vendidos", "Erro ao carregar");
            mapa.put("Receita Total", "Erro ao carregar");
        }
        return mapa;
    }

    // Retorna todas as partidas finalizadas
    public List<Partida> getPartidasFinalizadas() {
        List<Partida> finalizadas = new ArrayList<>();
        for (Partida p : partidaController.listarTodas()) {
            if (p.getStatus() == StatusPartida.FINALIZADA) finalizadas.add(p);
        }
        return finalizadas;
    }

    // Retorna uma linha por selecao com gols feitos e sofridos
    public List<Object[]> getDesempenhoSelecoes() {
        List<Selecao> selecoes = selecaoController.listarTodas();
        List<Partida> partidas = partidaController.listarTodas();
        List<Object[]> resultado = new ArrayList<>();

        for (Selecao s : selecoes) {
            int golsFeitos = 0, golsSofridos = 0;

            for (Partida p : partidas) {
                if (p.getStatus() != StatusPartida.FINALIZADA
                        || p.getResultado() == null) continue;
                if (p.getSelecao1() == null
                        || p.getSelecao2() == null) continue;

                boolean eh1 = p.getSelecao1().getNome().equals(s.getNome());
                boolean eh2 = p.getSelecao2().getNome().equals(s.getNome());

                if (eh1) {
                    golsFeitos   += p.getResultado().getGols1();
                    golsSofridos += p.getResultado().getGols2();
                } else if (eh2) {
                    golsFeitos   += p.getResultado().getGols2();
                    golsSofridos += p.getResultado().getGols1();
                }
            }

            resultado.add(new Object[]{s.getNome(), golsFeitos, golsSofridos});
        }

        return resultado;
    }
}