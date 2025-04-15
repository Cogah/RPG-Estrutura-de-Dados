class PilhaPersonagens {
    private Node topo;
    
    public PilhaPersonagens() {
        this.topo = null;
    }
    
    public void empilhar(Personagem personagem) {
        Node novoNode = new Node(personagem);
        novoNode.next = topo;
        topo = novoNode;
    }
    
    public Personagem desempilhar() {
        if (topo == null) {
            return null;
        }
        
        Personagem personagemRemovido = topo.personagem;
        topo = topo.next;
        return personagemRemovido;
    }
    
    public boolean estaVazia() {
        return topo == null;
    }
    
    public void exibirPilha() {
        if (topo == null) {
            System.out.println("Ranking vazio");
            return;
        }
        
        System.out.println("=== RANKING ===");
        Node atual = topo;
        int posicao = 1;
        
        while (atual != null) {
            System.out.println(posicao + "º lugar: " + atual.personagem.getNome());
            atual = atual.next;
            posicao++;
        }
    }
}