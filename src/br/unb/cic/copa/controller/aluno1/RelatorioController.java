package br.unb.cic.copa.controller.aluno1;

import br.unb.cic.copa.controller.aluno2.SelecaoController;
import br.unb.cic.copa.controller.aluno4.PartidaController;
import br.unb.cic.copa.controller.aluno5.IngressosController;
import br.unb.cic.copa.model.aluno1.*;
import br.unb.cic.copa.model.aluno2.Selecao;
import br.unb.cic.copa.model.aluno4.Partida;

import java.util.List;

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

    public String gerarRelatorioUsuarios() {
        List<Usuario> usuarios = usuarioController.listarUsuarios();

        long admins        = usuarios.stream()
                .filter(u -> u instanceof Administrador).count();
        long organizadores = usuarios.stream()
                .filter(u -> u instanceof Organizador).count();
        long operadores    = usuarios.stream()
                .filter(u -> u instanceof Operador).count();
        long arbitros      = usuarios.stream()
                .filter(u -> u.getFuncao().equals("Arbitro")).count();

        StringBuilder sb = new StringBuilder();
        sb.append("RELATORIO DE USUARIOS DO SISTEMA\n");
        sb.append("Copa do Mundo 2026\n");
        sb.append("-".repeat(65)).append("\n\n");

        sb.append(String.format("%-5s  %-20s  %-28s  %-14s  %-8s\n",
                "ID", "Nome", "Email", "Perfil", "Status"));
        sb.append("-".repeat(65)).append("\n");

        for (Usuario u : usuarios) {
            sb.append(String.format("%-5d  %-20s  %-28s  %-14s  %-8s\n",
                    u.getId(), u.getNome(), u.getEmail(),
                    u.getFuncao(), u.getStatus()));
        }

        sb.append("-".repeat(65)).append("\n\n");
        sb.append("RESUMO\n");
        sb.append("-".repeat(35)).append("\n");
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
        sb.append("1. USUARIOS DO SISTEMA\n");
        sb.append("-".repeat(35)).append("\n");
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

        sb.append("2. SELECOES E JOGADORES\n");
        sb.append("-".repeat(35)).append("\n");
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
            if (p.isFinalizada())
                totalGols += p.getGolsSelecao1() + p.getGolsSelecao2();
        }

        sb.append("3. PARTIDAS\n");
        sb.append("-".repeat(35)).append("\n");
        sb.append(String.format("  %-28s %d\n", "Total de partidas:", partidas.size()));
        sb.append(String.format("    %-26s %d\n", "Finalizadas:", finalizadas));
        sb.append(String.format("    %-26s %d\n", "Agendadas:", agendadas));
        sb.append(String.format("    %-26s %d\n", "Em andamento:", emAndamento));
        sb.append(String.format("  %-28s %d\n", "Total de gols:", totalGols));
        sb.append("\n");


        if (!selecoes.isEmpty() && !partidas.isEmpty()) {
            sb.append("4. DESEMPENHO DAS SELECOES\n");
            sb.append("-".repeat(35)).append("\n");
            sb.append("  Colunas:\n");
            sb.append("    PJ = Partidas Jogadas\n");
            sb.append("    V  = Vitorias\n");
            sb.append("    E  = Empates\n");
            sb.append("    D  = Derrotas\n");
            sb.append("    GM = Gols Marcados\n");
            sb.append("    GS = Gols Sofridos\n");
            sb.append("    SG = Saldo de Gols\n");
            sb.append("    PT = Pontos totais\n\n");

            sb.append(String.format("  %-22s  %3s  %3s  %3s  %3s  %3s  %3s  %4s  %3s\n",
                    "Selecao", " PJ", " V", " E", " D", "GM", "GS", "  SG", "PT"));
            sb.append("  ").append("-".repeat(58)).append("\n");

            for (Selecao s : selecoes) {
                int pj = 0, v = 0, e = 0, d = 0, gm = 0, gs = 0, pts = 0;

                for (Partida p : partidas) {
                    if (!p.isFinalizada()) continue;
                    boolean s1 = p.getSelecao1().getNome().equals(s.getNome());
                    boolean s2 = p.getSelecao2().getNome().equals(s.getNome());
                    if (!s1 && !s2) continue;

                    pj++;
                    int golsPro = s1 ? p.getGolsSelecao1() : p.getGolsSelecao2();
                    int golsCon = s1 ? p.getGolsSelecao2() : p.getGolsSelecao1();
                    gm += golsPro;
                    gs += golsCon;

                    if (golsPro > golsCon)      { v++;  pts += 3; }
                    else if (golsPro == golsCon) { e++;  pts += 1; }
                    else                         { d++; }
                }

                sb.append(String.format("  %-22s  %3d  %3d  %3d  %3d  %3d  %3d  %4d  %3d\n",
                        s.getNome(), pj, v, e, d, gm, gs, (gm - gs), pts));
            }
            sb.append("\n");
        }


        sb.append("5. INGRESSOS E PUBLICO\n");
        sb.append("-".repeat(35)).append("\n");
        sb.append(String.format("  %-28s %d\n", "Ingressos vendidos:",
                ingressosController.getTotalIngressosVendidos()));
        sb.append(String.format("  %-28s R$ %.2f\n", "Total arrecadado:",
                ingressosController.getValorTotalArrecadado()));

        return sb.toString();
    }
}