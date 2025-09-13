import java.util.*;

// Produto
record Produto(String nome, double preco) {
}

// Flyweight: Usuario
record Usuario(String nome) {
}
class UsuarioFactory {
    private static final Map<String, Usuario> usuarios = new HashMap<>();
    public static Usuario getUsuario(String nome) {
        if (!usuarios.containsKey(nome)) {
            usuarios.put(nome, new Usuario(nome));
            System.out.println("Novo usuário criado: " + nome);
        }
        return usuarios.get(nome);
    }
}

// Pagamento
interface Pagamento {
    void pagar(double valor);
}
class PagamentoCartao implements Pagamento {
    public void pagar(double valor) {
        System.out.println("Pagamento de R$" + valor + " realizado no Cartão.");
    }
}
class PagamentoBoleto implements Pagamento {
    public void pagar(double valor) {
        System.out.println("Boleto no valor R$" + valor + " gerado para pagamento.");
    }
}
class PagamentoPix implements Pagamento {
    public void pagar(double valor) {
        System.out.println("Pagamento de R$" + valor + " realizado via Pix.");
    }
}
class PagamentoEspecie implements Pagamento {
    public void pagar(double valor) {
        System.out.println("Pagamento de R$" + valor + " realizado em Espécie.");
    }
}

// Facade
class PagamentoFacade {
    public void realizarPagamento(String tipo, double valor) {
        Pagamento pagamento = switch (tipo.toLowerCase()) {
            case "cartao" -> new PagamentoCartao();
            case "boleto" -> new PagamentoBoleto();
            case "pix" -> new PagamentoPix();
            case "especie" -> new PagamentoEspecie();
            default -> throw new IllegalArgumentException("Tipo de pagamento inválido!");
        };
        pagamento.pagar(valor);
    }
}

// Sacola de Compras
class SacolaCompras {
    private final List<Produto> produtos = new ArrayList<>();
    private final Usuario usuario;
    public SacolaCompras(Usuario usuario) {
        this.usuario = usuario;
    }
    public void adicionarProduto(Produto p) {
        produtos.add(p);
    }
    public double calcularTotal() {
        return produtos.stream().mapToDouble(Produto::preco).sum();
    }
    public void fecharCompra(String tipoPagamento) {
        double total = calcularTotal();
        System.out.println("\nUsuário " + usuario.nome() + " está finalizando a compra...");
        PagamentoFacade facade = new PagamentoFacade();
        facade.realizarPagamento(tipoPagamento, total);
    }
}

public class Main {
    public static void main(String[] args) {
        // Flyweight: usuários
        Usuario u1 = UsuarioFactory.getUsuario("Fernanda");
        Usuario u2 = UsuarioFactory.getUsuario("Carlos");
        Usuario u3 = UsuarioFactory.getUsuario("José");

        // Sacolas
        SacolaCompras sacola1 = new SacolaCompras(u1);
        sacola1.adicionarProduto(new Produto("Notebook", 3500.00));
        sacola1.adicionarProduto(new Produto("Mouse", 150.00));
        sacola1.fecharCompra("cartao");

        SacolaCompras sacola2 = new SacolaCompras(u2);
        sacola2.adicionarProduto(new Produto("Cadeira Gamer", 1200.00));
        sacola2.fecharCompra("pix");

        SacolaCompras sacola3 = new SacolaCompras(u3);
        sacola3.adicionarProduto(new Produto("Teclado Mecânico", 500.00));
        sacola3.fecharCompra("boleto");
    }
}

