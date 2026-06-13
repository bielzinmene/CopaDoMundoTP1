package br.unb.cic.copa.controller.aluno1;

import br.unb.cic.copa.controller.aluno2.SelecaoController;
import br.unb.cic.copa.controller.aluno4.PartidaController;
import br.unb.cic.copa.controller.aluno5.IngressosController;
import br.unb.cic.copa.model.aluno1.*;
import br.unb.cic.copa.model.aluno2.Selecao;
import br.unb.cic.copa.model.aluno4.Partida;

import java.util.*;

public class RelatorioController {

    private static RelatorioController instancia;

    private final UsuarioController usuarioController;
    private final SelecaoController selecaoController;
    private final PartidaController partidaController;
    private final IngressosController ingressosController;

    private RelatorioController() {
        usuarioController   = UsuarioController.getInstancia();
        selecaoController   = new SelecaoController();
        partidaController   = new PartidaController();
        ingressosController = new IngressosController();
    }

    public static RelatorioController getInstancia() {
        if (instancia == null) {
            instancia = new RelatorioController();
        }
        return instancia;
    }

    public List<Usuario> getUsuarios() {
        return usuarioController.listarUsuarios();
    }

    public Map<String, Long> getResumoUsuarios() {
        List<Usuario> usuarios = usuarioController.listarUsuarios();
        Map<String, Long> resumo = new LinkedHashMap<>();
        resumo.put("Total", (long) usuarios.size());
        resumo.put("Administradores",
                usuarios.stream().filter(u -> u instanceof Administrador).count());
        resumo.put("Organizadores",
                usuarios.stream().filter(u -> u instanceof Organizador).count());
        resumo.put("Operadores",
                usuarios.stream().filter(u -> u instanceof Operador).count());
        resumo.put("Arbitros",
                usuarios.stream().filter(u -> u.getFuncao().equals("Arbitro")).count());
        return resumo;
    }

    public List<Object[]> getDesempenhoSelecoes() {
        List<Selecao> selecoes = selecaoController.listarTodas();
        List<Partida> partidas = partidaController.listarTodasPartidas();

        List<Object[]> resultado = new ArrayList<>();

        for (Selecao s : selecoes) {
            int golsFeitos = 0;
            int golsSofridos = 0;

            for (Partida p : partidas) {
                if (!p.isFinalizada()) continue;

                boolean s1 = p.getSelecao1().getNome().equals(s.getNome());
                boolean s2 = p.getSelecao2().getNome().equals(s.getNome());

                if (s1) {
                    golsFeitos   += p.getGolsSelecao1();
                    golsSofridos += p.getGolsSelecao2();
                } else if (s2) {
                    golsFeitos   += p.getGolsSelecao2();
                    golsSofridos += p.getGolsSelecao1();
                }
            }

            resultado.add(new Object[]{s.getNome(), golsFeitos, golsSofridos});
        }

        return resultado;
    }

    public List<Object[]> getResultadosPartidas() {
        List<Partida> partidas = partidaController.listarTodasPartidas();
        List<Object[]> resultado = new ArrayList<>();

        for (Partida p : partidas) {
            String placar;
            if (p.isFinalizada() && p.getResultado() != null) {
                placar = p.getGolsSelecao1() + " x " + p.getGolsSelecao2();
            } else if (p.isFinalizada()) {
                placar = "- x -";
            } else {
                placar = "- x -";
            }

            resultado.add(new Object[]{
                    p.getSelecao1().getNome(),
                    placar,
                    p.getSelecao2().getNome(),
                    p.getData(),
                    p.getEstadio().getNome(),
                    p.getStatus().toString()
            });
        }

        return resultado;
    }

    public Map<String, Integer> getResumoPartidas() {
        List<Partida> partidas = partidaController.listarTodasPartidas();
        int totalGols = 0;

        long finalizadas = partidas.stream()
                .filter(Partida::isFinalizada).count();
        long agendadas   = partidas.stream()
                .filter(p -> p.getStatus().toString().equals("AGENDADA")).count();
        long emAndamento = partidas.stream()
                .filter(p -> p.getStatus().toString().equals("EM_ANDAMENTO")).count();

        for (Partida p : partidas) {
            if (p.isFinalizada() && p.getResultado() != null) {
                totalGols += p.getGolsSelecao1() + p.getGolsSelecao2();
            }
        }

        Map<String, Integer> resumo = new LinkedHashMap<>();
        resumo.put("Total", partidas.size());
        resumo.put("Finalizadas", (int) finalizadas);
        resumo.put("Agendadas", (int) agendadas);
        resumo.put("Em Andamento", (int) emAndamento);
        resumo.put("Total de Gols", totalGols);
        return resumo;
    }

    public Map<String, String> getResumoIngressos() {
        Map<String, String> resumo = new LinkedHashMap<>();
        resumo.put("Ingressos Vendidos",
                String.valueOf(ingressosController.getTotalIngressosVendidos()));
        resumo.put("Total Arrecadado",
                String.format("R$ %.2f", ingressosController.getValorTotalArrecadado()));
        return resumo;
    }

    public String gerarRelatorioUsuarios() {
        List<Usuario> usuarios = usuarioController.listarUsuarios();
        long admins        = usuarios.stream().filter(u -> u instanceof Administrador).count();
        long organizadores = usuarios.stream().filter(u -> u instanceof Organizador).count();
        long operadores    = usuarios.stream().filter(u -> u instanceof Operador).count();
        long arbitros      = usuarios.stream().filter(u -> u.getFuncao().equals("Arbitro")).count();

        StringBuilder sb = new StringBuilder();
        sb.append("RELATORIO DE USUARIOS DO SISTEMA\n");
        sb.append("Copa do Mundo 2026\n");
        sb.append("-".repeat(65)).append("\n\n");
        sb.append(String.format("%-5s  %-20s  %-28s  %-14s  %-8s\n",
                "ID", "Nome", "Email", "Perfil", "Status"));
        sb.append("-".repeat(65)).append("\n");
        for (Usuario u : usuarios) {
            sb.append(String.format("%-5d  %-20s  %-28s  %-14s  %-8s\n",
                    u.getId(), u.getNome(), u.getEmail(), u.getFuncao(), u.getStatus()));
        }
        sb.append("-".repeat(65)).append("\n\n");
        sb.append("RESUMO\n").append("-".repeat(35)).append("\n");
        sb.append(String.format("  %-28s %d\n", "Total de usuarios:", usuarios.size()));
        sb.append(String.format("    %-26s %d\n", "Administradores:", admins));
        sb.append(String.format("    %-26s %d\n", "Organizadores:", organizadores));
        sb.append(String.format("    %-26s %d\n", "Operadores:", operadores));
        sb.append(String.format("    %-26s %d\n", "Arbitros:", arbitros));
        return sb.toString();
    }

    public String gerarRelatorioConsolidado() {
        StringBuilder sb = new StringBuilder();
        sb.append("RELATORIO CONSOLIDADO\n");
        sb.append("Copa do Mundo 2026\n");
        sb.append("-".repeat(65)).append("\n\n");

        List<Usuario> usuarios = usuarioController.listarUsuarios();
        sb.append("1. USUARIOS DO SISTEMA\n").append("-".repeat(35)).append("\n");
        sb.append(String.format("  %-28s %d\n", "Total de usuarios:", usuarios.size()));
        sb.append(String.format("    %-26s %d\n", "Administradores:",
                usuarios.stream().filter(u -> u instanceof Administrador).count()));
        sb.append(String.format("    %-26s %d\n", "Organizadores:",
                usuarios.stream().filter(u -> u instanceof Organizador).count()));
        sb.append(String.format("    %-26s %d\n", "Operadores:",
                usuarios.stream().filter(u -> u instanceof Operador).count()));
        sb.append("\n");

        List<Selecao> selecoes = selecaoController.listarTodas();
        int totalJogadores = 0;
        for (Selecao s : selecoes) totalJogadores += s.getJogadores().size();
        sb.append("2. SELECOES E JOGADORES\n").append("-".repeat(35)).append("\n");
        sb.append(String.format("  %-28s %d\n", "Total de selecoes:", selecoes.size()));
        sb.append(String.format("  %-28s %d\n", "Total de jogadores:", totalJogadores));
        sb.append("\n");

        List<Partida> partidas = partidaController.listarTodasPartidas();
        long finalizadas = partidas.stream().filter(Partida::isFinalizada).count();
        long agendadas   = partidas.stream()
                .filter(p -> p.getStatus().toString().equals("AGENDADA")).count();
        long emAndamento = partidas.stream()
                .filter(p -> p.getStatus().toString().equals("EM_ANDAMENTO")).count();
        int totalGols = 0;
        for (Partida p : partidas) {
            if (p.isFinalizada() && p.getResultado() != null)
                totalGols += p.getGolsSelecao1() + p.getGolsSelecao2();
        }
        sb.append("3. PARTIDAS\n").append("-".repeat(35)).append("\n");
        sb.append(String.format("  %-28s %d\n", "Total de partidas:", partidas.size()));
        sb.append(String.format("    %-26s %d\n", "Finalizadas:", finalizadas));
        sb.append(String.format("    %-26s %d\n", "Agendadas:", agendadas));
        sb.append(String.format("    %-26s %d\n", "Em andamento:", emAndamento));
        sb.append(String.format("  %-28s %d\n", "Total de gols:", totalGols));
        sb.append("\n");

        sb.append("4. INGRESSOS E PUBLICO\n").append("-".repeat(35)).append("\n");
        sb.append(String.format("  %-28s %d\n", "Ingressos vendidos:",
                ingressosController.getTotalIngressosVendidos()));
        sb.append(String.format("  %-28s R$ %.2f\n", "Total arrecadado:",
                ingressosController.getValorTotalArrecadado()));
        return sb.toString();
    }
}