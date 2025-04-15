class FilaPersonagens {
    private Node frente, tras;

    public FilaPersonagens() {
        this.frente = this.tras = null;
    }

    public void enfileirar(Personagem personagem) {
        Node novoNode = new Node(personagem);
        if (tras == null) {
            frente = tras = novoNode;
        } else {
            tras.next = novoNode;
            tras = novoNode;
        }
    }

    public Personagem desenfileirar() {
        if (frente == null) {
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
        Node atual = frente;
        System.out.print("Fila de Personagens: ");
        while (atual != null) {
            System.out.print(atual.personagem + " -> ");
            atual = atual.next;
        }
        System.out.println("NULL");
    }
}
