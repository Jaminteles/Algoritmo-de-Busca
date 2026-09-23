public class Aresta {
    No de;
    No para;
    int custo;
    boolean dupla; // so usado pra desenhar

    public Aresta(No de, No para, boolean dupla) {
        this.de = de;
        this.para = para;
        this.dupla = dupla;
        // as ruas sao todas retas entao o custo eh so a diferenca
        custo = Math.abs(de.x - para.x) + Math.abs(de.y - para.y);
    }
}
