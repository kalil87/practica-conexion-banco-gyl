package servicio;

import modelo.Cuenta;
import modelo.TipoTransaccion;
import modelo.Transaccion;

public class OperacionesBancarias {
    public static void depositar(Cuenta cuenta, double monto) {
        validarCuenta(cuenta);
        validarMonto(monto);

        cuenta.agregarSaldo(monto);

        Transaccion transaccionNueva = new Transaccion(null, cuenta, monto, TipoTransaccion.DEPOSITO);
        cuenta.agregarTransaccionHistorial(transaccionNueva);
    }

    public static void retirar(Cuenta cuenta, double monto) {
        validarCuenta(cuenta);
        validarMonto(monto);

        if (cuenta.getSaldo() < monto) {
            throw new IllegalArgumentException("(OperacionesBancarias, retirar) saldo insuficiente");
        }

        cuenta.restarSaldo(monto);

        Transaccion transaccionNueva = new Transaccion(cuenta, null, monto, TipoTransaccion.RETIRO);
        cuenta.agregarTransaccionHistorial(transaccionNueva);
    }

    public static void transferir(Cuenta transferente, Cuenta transferido, double monto) {
        validarCuenta(transferente);
        validarCuenta(transferido);
        validarMonto(monto);

        if (transferente.getSaldo() < monto) {
            throw new IllegalArgumentException("(OperacionesBancarias, transferir) el monto a transferir es menor al saldo disponible");
        }

        transferente.restarSaldo(monto);
        transferido.agregarSaldo(monto);
        Transaccion transaccionEnviada = new Transaccion(transferente, transferido, monto, TipoTransaccion.TRANSFERENCIA_ENVIADA);
        Transaccion transaccionRecibida = new Transaccion(transferente, transferido, monto, TipoTransaccion.TRANSFERENCIA_RECIBIDA);
        transferente.agregarTransaccionHistorial(transaccionEnviada);
        transferido.agregarTransaccionHistorial(transaccionRecibida);
    }

    private static void validarCuenta(Cuenta cuenta) {
        if (cuenta == null) {
            throw new IllegalArgumentException("(OperacionesBancarias) la cuenta es nula");
        }
    }

    private static void validarMonto(double monto) {
        if (monto < 1) {
            throw new IllegalArgumentException("(OperacionesBancarias) el monto es inválido");
        }
    }
}