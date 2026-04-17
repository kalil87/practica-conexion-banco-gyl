import dominio.MediadorBancos;
import leo.ServicioDataBase.DataBaseInjector;
import santiago.modelo.Banco;
import santiago.modelo.Cuenta;
import santiago.modelo.Sucursal;
import santiago.servicio.InicializadorBanco;
import santiago.ui.Menu;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        boolean isRunning = true;
        Scanner teclado = new Scanner(System.in);
        MediadorBancos mediador = new MediadorBancos();

        DataBaseInjector bancoLeo = new DataBaseInjector();
        Banco bancoSanti = Banco.getInstancia();

        InicializadorBanco.inicializarBanco(bancoSanti);
        ArrayList<santiago.modelo.Sucursal> sucursalesAdaptadas = mediador.getAdapterSantiago().adaptarSucursalesDeLeo(bancoLeo.getSucursalList());
        for (santiago.modelo.Sucursal sucursalIterada : sucursalesAdaptadas) {
            bancoSanti.crearSucursal(sucursalIterada.getNombre());

            if (bancoSanti.buscarSucursal(sucursalIterada.getNombre()) == null) {
                bancoSanti.crearSucursal(sucursalIterada.getNombre());
            }

            Sucursal sucursalEspejo = bancoSanti.buscarSucursal(sucursalIterada.getNombre());

            for (Cuenta cuentaIterada : sucursalIterada.getCuentas()) {
                if (sucursalEspejo.buscarCuentaSucursal(cuentaIterada.getEmail()) == null) {
                    Cuenta cuentaEspejo = sucursalEspejo.crearCuenta(cuentaIterada.getNombre(), cuentaIterada.getEmail(), cuentaIterada.getPin(), cuentaIterada.isAdmin(), cuentaIterada.getTipoCuenta());

                    if (cuentaEspejo != null && cuentaIterada.getSaldo() > 0) {
                        cuentaEspejo.agregarSaldo(cuentaIterada.getSaldo());
                    }
                }
            }
        }

        bancoLeo.getSucursalList().addAll(mediador.getAdapterLeo().adaptarSucursalesDeSanti(bancoSanti.getSucursales()));

        while (isRunning) {
            System.out.println("""
                ¿A qué banco le gustaría ingresar? Ingrese 0 para salir
                1) Banco Leonardo
                2) Banco Santiago""");
            int opcion = teclado.nextInt();

            switch (opcion) {
                case 1 -> {new leo.App(bancoLeo);}
                case 2 -> {new Menu(bancoSanti).mostrarMenuBanco();}
                case 0 -> isRunning = false;
                default -> System.out.println("\nOpción inválida\n");
            }
        }
    }
}