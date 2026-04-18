import java.util.ArrayList;
import java.util.Scanner;
import dominio.MediadorBancos;
import leo.ServicioDataBase.DataBaseInjector;
import santi.modelo.Banco;
import santi.modelo.Cuenta;
import santi.modelo.Sucursal;
import santi.servicio.InicializadorBanco;
import santi.ui.Menu;

public class Main {
    private static final Scanner TECLADO = new Scanner(System.in);
    private static final MediadorBancos MEDIADOR = new MediadorBancos();
    private static final DataBaseInjector BANCO_LEO = new DataBaseInjector();
    private static final Banco BANCO_SANTI = Banco.getInstancia();

    public static void main(String[] args) {
        boolean isRunning = true;

       procesarIntegracion();

        while (isRunning) {
            System.out.println("""
                ¿A qué banco le gustaría ingresar? Ingrese 0 para salir
                1) Banco Leonardo
                2) Banco Santiago""");
            int opcion = TECLADO.nextInt();

            switch (opcion) {
                case 1 -> {new leo.App(BANCO_LEO);}
                case 2 -> {new Menu(BANCO_SANTI).mostrarMenuBanco();}
                case 0 -> isRunning = false;
                default -> System.out.println("\nOpción inválida\n");
            }
        }
    }

    private static void procesarIntegracion() {
        /*Inicialización del banco de Santi*/
        InicializadorBanco.inicializarBanco(BANCO_SANTI);
        /*Se adaptan las sucursales del banco de Leo*/
        ArrayList<santi.modelo.Sucursal> sucursalesAdaptadas = MEDIADOR.getAdapterSantiago().adaptarSucursalesDeLeo(BANCO_LEO.getSucursalList());
        /*Se agregan las sucursales adaptadas al banco de Santi*/
        agregarSucursalesAdaptadas(sucursalesAdaptadas);
        /*Se adaptan las sucursales del banco de Santi y se agregan al de Leo*/
        BANCO_LEO.getSucursalList().addAll(MEDIADOR.getAdapterLeo().adaptarSucursalesDeSanti(BANCO_SANTI.getSucursales()));
    }

    private static void agregarSucursalesAdaptadas (ArrayList<santi.modelo.Sucursal> sucursalesAdaptadas) {
        for (santi.modelo.Sucursal sucursalIterada : sucursalesAdaptadas) {
            BANCO_SANTI.crearSucursal(sucursalIterada.getNombre());

            if (BANCO_SANTI.buscarSucursal(sucursalIterada.getNombre()) == null) {
                BANCO_SANTI.crearSucursal(sucursalIterada.getNombre());
            }

            Sucursal sucursalEspejo = BANCO_SANTI.buscarSucursal(sucursalIterada.getNombre());

            for (Cuenta cuentaIterada : sucursalIterada.getCuentas()) {
                if (sucursalEspejo.buscarCuentaSucursal(cuentaIterada.getEmail()) == null) {
                    Cuenta cuentaEspejo = sucursalEspejo.crearCuenta(cuentaIterada.getNombre(), cuentaIterada.getEmail(), cuentaIterada.getPin(), cuentaIterada.isAdmin(), cuentaIterada.getTipoCuenta());

                    if (cuentaEspejo != null && cuentaIterada.getSaldo() > 0) {
                        cuentaEspejo.agregarSaldo(cuentaIterada.getSaldo());
                    }
                }
            }
        }
    }
}