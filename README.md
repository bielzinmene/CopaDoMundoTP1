# 🏆 Sistema de Gestão da Copa do Mundo 2026

Projeto desenvolvido para a disciplina de **Técnicas de Programação 1 (TP1)** na **Universidade de Brasília (UnB)**.

O sistema consiste em uma aplicação Orientada a Objetos em Java destinada ao gerenciamento completo de uma edição da Copa do Mundo, englobando desde a gestão de elencos e seleções até o agendamento de partidas, locais, arbitragem e bilheteria.

---

## 👥 Divisão da Equipe e Responsabilidades

* **Fernanda (Aluno 1):** Administração, Gestão do Fluxo Principal e Controladores 
* **Gabriel (Aluno 2):** Gestão de Seleções, Jogadores e Validações de Elenco 
* **Esthefany (Aluno 3):** Gestão de Estádios, Sede e Arbitragem
* **Erica (Aluno 4):** Gestão de Partidas, Fases e Resultados
* **Danilo (Aluno 5):** Gestão de Ingressos, Vendas e Público

---

## 🏗️ Arquitetura do Sistema

O projeto adota uma arquitetura baseada no padrão **MVC (Model-View-Controller)** adaptada para o ambiente acadêmico, promovendo forte desacoplamento e encapsulamento:

```text
src/
└── br.unb.cic.copa/
    ├── model/         # Entidades do domínio (Jogador, Selecao, Partida, Estadio...)
    ├── controller/    # Regras de fluxo e controle de navegação
    ├── persistence/   # Camada de Persistência DAO em arquivos TXT
    ├── view/          # Menus e interfaces com o usuário
    └── exception/     # Tratamento de exceções de negócio personalizadas
