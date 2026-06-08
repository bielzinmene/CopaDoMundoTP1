package br.unb.cic.copa.model.aluno5.repository;

import br.unb.cic.copa.model.aluno5.Venda;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class VendaRepository {

    private static final String ARQUIVO = "src/dados/vendas.json";

    public void salvar(Venda venda) {

        List<Venda> vendas = listarTodos();

        boolean atualizada = false;

        for (int i = 0; i < vendas.size(); i++) {

            if (vendas.get(i).getId() == venda.getId()) {
                vendas.set(i, venda);
                atualizada = true;
                break;
            }
        }

        if (!atualizada) {
            vendas.add(venda);
        }

        escreverJson(vendas);
    }

    public List<Venda> listarTodos() {

        File arquivo = new File(ARQUIVO);

        if (!arquivo.exists()) {
            return new ArrayList<>();
        }

        try {

            BufferedReader reader =
                    new BufferedReader(
                            new FileReader(arquivo));

            StringBuilder json = new StringBuilder();

            String linha;

            while ((linha = reader.readLine()) != null) {
                json.append(linha).append("\n");
            }

            reader.close();

            return parseJson(json.toString());

        } catch (Exception e) {

            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public Venda buscarPorId(int id) {

        for (Venda venda : listarTodos()) {

            if (venda.getId() == id) {
                return venda;
            }
        }

        return null;
    }

    public void remover(int id) {

        List<Venda> vendas = listarTodos();

        vendas.removeIf(v -> v.getId() == id);

        escreverJson(vendas);
    }

    private void escreverJson(List<Venda> vendas) {

        try {

            File arquivo = new File(ARQUIVO);

            arquivo.getParentFile().mkdirs();

            BufferedWriter writer =
                    new BufferedWriter(
                            new FileWriter(arquivo));

            writer.write("[\n");

            for (int i = 0; i < vendas.size(); i++) {

                Venda v = vendas.get(i);

                writer.write("  {\n");
                writer.write("    \"id\": " + v.getId() + ",\n");
                writer.write("    \"comprador\": \"" + v.getComprador() + "\",\n");
                writer.write("    \"dataVenda\": \"" + v.getDataVenda() + "\",\n");
                writer.write("    \"quantidadeIngressos\": " + v.getQuantidadeIngressos() + ",\n");
                writer.write("    \"valorTotal\": " + v.getValorTotal() + "\n");
                writer.write("  }");

                if (i < vendas.size() - 1) {
                    writer.write(",");
                }

                writer.write("\n");
            }

            writer.write("]");

            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<Venda> parseJson(String json) {

        List<Venda> vendas = new ArrayList<>();

        json = json.trim();

        if (json.isEmpty() || json.equals("[]")) {
            return vendas;
        }

        json = json.substring(1, json.length() - 1);

        String[] objetos = json.split("\\},\\s*\\{");

        for (String obj : objetos) {

            obj = obj.replace("{", "")
                    .replace("}", "")
                    .trim();

            int id = 0;
            String comprador = "";
            LocalDateTime dataVenda = LocalDateTime.now();

            int quantidadeIngressos = 0;
            double valorTotal = 0;

            String[] linhas = obj.split(",");

            for (String linha : linhas) {

                linha = linha.trim();

                if (linha.startsWith("\"id\"")) {

                    id = Integer.parseInt(
                            linha.split(":")[1].trim());

                } else if (linha.startsWith("\"comprador\"")) {

                    comprador =
                            linha.split(":", 2)[1]
                                    .trim()
                                    .replace("\"", "");

                } else if (linha.startsWith("\"dataVenda\"")) {

                    String data =
                            linha.split(":", 2)[1]
                                    .trim()
                                    .replace("\"", "");

                    dataVenda = LocalDateTime.parse(data);

                } else if (linha.startsWith("\"quantidadeIngressos\"")) {

                    quantidadeIngressos =
                            Integer.parseInt(
                                    linha.split(":")[1].trim());

                } else if (linha.startsWith("\"valorTotal\"")) {

                    valorTotal =
                            Double.parseDouble(
                                    linha.split(":")[1].trim());
                }
            }

            Venda venda =
                    new Venda(
                            id,
                            comprador,
                            dataVenda,
                            quantidadeIngressos,
                            valorTotal
                    );

            vendas.add(venda);
        }

        return vendas;
    }
}