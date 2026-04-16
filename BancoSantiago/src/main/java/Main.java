import modelo.Banco;
import servicio.InicializadorBanco;
import ui.Menu;

public class Main {
    static void main(String[] args) {
        Banco banco = Banco.getInstancia();
        InicializadorBanco.inicializarBanco(banco);
        Menu menu = new Menu(banco);

        menu.mostrarMenuBanco();
    }
}