package dominio.leo;

import leo.ModeloBanco.Cliente.Cliente;
import leo.ModeloBanco.Transferencia.Transferencia;
import santi.modelo.Cuenta;
import santi.modelo.Sucursal;
import santi.modelo.TipoTransaccion;
import santi.modelo.Transaccion;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class AdaptadorABancoLeo {
    public ArrayList<leo.ModeloBanco.Sucursal> adaptarSucursalesDeSanti(List<Sucursal> sucursalesSanti) {
        ArrayList<leo.ModeloBanco.Sucursal> sucursalesTraducidas = new ArrayList<>();

        for (santi.modelo.Sucursal sucursalIterada : sucursalesSanti) {
            if (!sucursalIterada.getNombre().contains("[Banco Leo] ")) {
                leo.ModeloBanco.Sucursal sucursalTraducida = new leo.ModeloBanco.Sucursal("[Banco Santi] " + sucursalIterada.getNombre(), sucursalIterada.getNombre(), "Dato desconocido",
                        """
                                            ██████████████         \s
                                        ████              ████     \s
                                      ██                      ██   \s
                                    ██            ██            ██ \s
                                    ██        ██████████        ██ \s
                                ██▓▓        ██    ██    ██        ██
                                ████        ██    ██              ██
                                ████        ██    ██              ██
                                ██▓▓          ██████████          ██
                                ██▓▓              ██    ██        ██
                                ████              ██    ██        ██
                                ██▓▓        ██    ██    ██        ██
                                    ██        ██████████        ██ \s
                                    ██            ██            ██ \s
                                      ██                      ██   \s
                                        ████              ████     \s
                                            ██████████████         \s
                                [RED BANCO SANTIAGO]\s""" + sucursalIterada.getNombre() + System.lineSeparator());

                for (Cuenta cuentaIterada : sucursalIterada.getCuentas()) {
                    adaptarCuentaACliente(cuentaIterada, sucursalTraducida);

                    for (Transaccion transaccionIterada : cuentaIterada.getHistorialTransacciones()) {
                        adaptarTransaccionATransferencia(transaccionIterada, sucursalTraducida);
                    }
                }
                sucursalesTraducidas.add(sucursalTraducida);
            }
        }
        return sucursalesTraducidas;
    }

    public Cliente adaptarCuentaACliente(Cuenta cuentaATraducir, leo.ModeloBanco.Sucursal sucursalPortadora) {
        String usuarioTraducido = cuentaATraducir.getEmail();
        String contraseñaTraducida = String.valueOf(cuentaATraducir.getPin());
        String nombreTraducido = cuentaATraducir.getNombre();
        String tipoCuentaTraducido = switch (cuentaATraducir.getTipoCuenta()) {
            case CAJA_AHORRO -> "Ahorro";
            case CUENTA_CORRIENTE -> "Corriente";
            case BANCO_EXTERNO -> "Externa";
        };

        return new Cliente.Builder(usuarioTraducido, contraseñaTraducida, nombreTraducido, "", "Dato desconocido").tipoCuenta(tipoCuentaTraducido).permisos("").build(sucursalPortadora.registro);
    }

    public void adaptarTransaccionATransferencia(Transaccion transaccionATraducir, leo.ModeloBanco.Sucursal sucursalPortadora) {
        Boolean esDepositoTraducido = null;

        if (transaccionATraducir.getTipoTransaccion() == TipoTransaccion.DEPOSITO) {
            esDepositoTraducido = true;
        } else if (transaccionATraducir.getTipoTransaccion() == TipoTransaccion.RETIRO) {
            esDepositoTraducido = false;
        }
        Cliente destinoTraducido = adaptarCuentaACliente(transaccionATraducir.getDestino(), sucursalPortadora);

        if (esDepositoTraducido != null) {
            new Transferencia.Builder(esDepositoTraducido, destinoTraducido, BigDecimal.valueOf(transaccionATraducir.getMonto())).fecha("Dato desconocido").acreditar(sucursalPortadora.auditor);
        } else {
            Cliente origenTraducido = adaptarCuentaACliente(transaccionATraducir.getOrigen(), sucursalPortadora);
            new Transferencia.Builder(origenTraducido, destinoTraducido, BigDecimal.valueOf(transaccionATraducir.getMonto())).fecha("Dato desconocido").acreditar(sucursalPortadora.auditor);
        }
    }
}