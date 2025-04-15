class FilaPersonagens {
    private Node frente, tras;

    public FilaPersonagens() {
        this.frente = this.tras = null;
    }

    // Método para enfileirar usando String (compatível com Jogador.java)
    public void enfileirar(String personagem) {
        Node novoNode = new Node(personagem);
        if (tras == null) {
            frente = tras = novoNode;
        } else {
            tras.next = novoNode;
            tras = novoNode;
        }
    }

    // Método para enfileirar usando Personagem (compatível com Arena.java)
    public void enfileirar(Personagem personagem) {
        Node novoNode = new Node(personagem);
        if (tras == null) {
            frente = tras = novoNode;
        } else {
            tras.next = novoNode;
            tras = novoNode;
        }
    }

    public String desenfileirar() {
        if (frente == null) {
            return null;
        }

        String personagemRemovido;
        if (frente.personagem != null) {
            personagemRemovido = frente.personagem.getNome();
        } else {
            personagemRemovido = frente.nomePersonagem;
        }

        frente = frente.next;
        if (frente == null) {
            tras = null;
        }
        return personagemRemovido;
    }

    public Personagem desenfileirarPersonagem() {
        if (frente == null || frente.personagem == null) {
            return null;
        }

        Personagem personagemRemovido = frente.personagem;
        frente = frente.next;
        if (frente == null) {
            tras = null;
        }
        return personagemRemovido;
    }

    public boolean estaVazia() {
        return frente == null;
    }

    public void exibirFila() {
        if (frente == null) {
            System.out.println("Nenhum personagem na fila");
            return;
        }

        Node atual = frente;
        while (atual != null) {
            if (atual.personagem != null) {
                System.out.print(atual.personagem.getNome() + " -> ");
            } else {
                System.out.print(atual.nomePersonagem + " -> ");
            }
            atual = atual.next;
        }
        System.out.println("fim");
    }
}
