package servicio;

import modelo.Banco;
import modelo.Sucursal;
import modelo.TipoCuenta;

public class InicializadorBanco {
    public static void inicializarBanco(Banco banco) {
        inicializarSucursales(banco);
        inicializarCuentas(banco);
    }

    private static void inicializarSucursales(Banco banco) {
        banco.crearSucursal("Parque Patricios");
        banco.crearSucursal("Boedo");
    }

    private static void inicializarCuentas(Banco banco) {
        for (Sucursal sucursal : banco.getSucursales()) {
            for (int i = 1; i <= 10; i++) {
                sucursal.crearCuenta("Cliente " + i, "cliente" + i + "@gmail.com", 999 + i, false, TipoCuenta.CUENTA_CORRIENTE);
            }
            sucursal.crearCuenta("Admin", "admin@gmail.com", 2007, true, TipoCuenta.CAJA_AHORRO);
        }
    }
}