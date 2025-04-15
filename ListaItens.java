public class ListaItens {
    private Node cabeca;
    
    public ListaItens() {
        this.cabeca = null;
    }
    
    public void adicionarItem(Item item) {
        Node novoNode = new Node(item);
        if (cabeca == null) {
            cabeca = novoNode;
        } else {
            Node atual = cabeca;
            while (atual.next != null) {
                atual = atual.next;
            }
            atual.next = novoNode;
        }
    }
    
    public Item removerItem(String nomeItem) {
        if (cabeca == null) {
            return null;
        }
        
        if (cabeca.item != null && cabeca.item.getNome().equals(nomeItem)) {
            Item item = cabeca.item;
            cabeca = cabeca.next;
            return item;
        }
        
        Node atual = cabeca;
        while (atual.next != null && (atual.next.item == null || !atual.next.item.getNome().equals(nomeItem))) {
            atual = atual.next;
        }
        
        if (atual.next != null) {
            Item item = atual.next.item;
            atual.next = atual.next.next;
            return item;
        }
        
        return null;
    }
    
    public boolean estaVazia() {
        return cabeca == null;
    }
    
    public void exibirItens() {
        if (cabeca == null) {
            System.out.println("Inventário vazio");
            return;
        }
        
        System.out.println("Itens no inventário:");
        Node atual = cabeca;
        int i = 1;
        
        while (atual != null) {
            if (atual.item != null) {
                System.out.println(i + " - " + atual.item.getNome() + " (Custo: " + atual.item.getCusto() + 
                                  ", Recupera: Vida=" + atual.item.getRecoverVida() + 
                                  ", Mana=" + atual.item.getRecoverMana() + 
                                  ", Stamina=" + atual.item.getRecoverStamina() + ")");
                i++;
            }
            atual = atual.next;
        }
    }
    
    public Item buscarItem(int indice) {
        if (cabeca == null) {
            return null;
        }
        
        Node atual = cabeca;
        int contador = 1;
        
        while (atual != null && contador < indice) {
            atual = atual.next;
            contador++;
        }
        
        return (atual != null) ? atual.item : null;
    }
}

