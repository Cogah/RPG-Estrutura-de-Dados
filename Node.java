class Node {
    Personagem personagem;
    String nomePersonagem;
    Item item;
    Node next;

    public Node(Personagem personagem) {
        this.personagem = personagem;
        this.next = null;
    }

    public Node(String nomePersonagem) {
        this.nomePersonagem = nomePersonagem;
        this.next = null;
    }

    public Node(Item item) {
        this.item = item;
        this.next = null;
    }
}
