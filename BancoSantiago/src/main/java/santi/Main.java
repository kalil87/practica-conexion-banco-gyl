package santi;

import santi.modelo.Banco;
import santi.servicio.InicializadorBanco;
import santi.ui.Menu;

public class Main {
    static void main(String[] args) {
        Banco banco = Banco.getInstancia();
        InicializadorBanco.inicializarBanco(banco);
        Menu menu = new Menu(banco);

        menu.mostrarMenuBanco();
    }
}