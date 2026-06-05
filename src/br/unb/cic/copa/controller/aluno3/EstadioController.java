package br.unb.cic.copa.controller.aluno3;

import br.unb.cic.copa.model.aluno3.Estadio;
import br.unb.cic.copa.model.aluno3.Localizacao;
import br.unb.cic.copa.model.aluno3.PaisSede;
import br.unb.cic.copa.model.aluno3.exception.CapacidadeInvalidaException;
import br.unb.cic.copa.model.aluno3.repository.EstadioRepository;

import java.io.IOException;
import java.util.List;

// Centraliza as operações de estádio, fazendo a ponte entre a View e o Repositório
public class EstadioController {

    private final EstadioRepository estadioRepository;

    public EstadioController() {
        this.estadioRepository = new EstadioRepository("src/dados/estadios.json");
    }

    // Cadastra um estádio com ID gerado automaticamente
    public void cadastrar(String nome, int capacidade,
                          String cidade, String estado, PaisSede pais, String endereco)
            throws CapacidadeInvalidaException, IOException {

        int id = gerarNovoId();
        Localizacao localizacao = new Localizacao(cidade, estado, pais, endereco);
        Estadio estadio = new Estadio(id, nome, localizacao, capacidade);
        estadioRepository.salvar(estadio);
    }

    // Gera ID único: pega o maior ID existente e soma 1
    private int gerarNovoId() throws IOException {
        List<Estadio> lista = estadioRepository.listarTodos();
        int maior = 0;
        for (Estadio e : lista) {
            if (e.getId() > maior) maior = e.getId();
        }
        return maior + 1;
    }

    // Busca estádio pelo ID
    public Estadio buscarPorId(int id) throws IOException {
        return estadioRepository.buscarPorId(id);
    }

    // Retorna todos os estádios cadastrados
    public List<Estadio> listarTodos() throws IOException {
        return estadioRepository.listarTodos();
    }

    // Remove um estádio pelo ID
    public void excluir(int id) throws IOException {
        estadioRepository.remover(id);
    }
}