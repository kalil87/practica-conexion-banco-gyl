package dominio.leo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import leo.ModeloBanco.Cliente.Cliente;
import leo.ModeloBanco.Transferencia.Transferencia;
import santi.modelo.Cuenta;
import santi.modelo.Sucursal;
import santi.modelo.TipoTransaccion;
import santi.modelo.Transaccion;

public class AdaptadorABancoLeo {
    public ArrayList<leo.ModeloBanco.Sucursal> adaptarSucursalesDeSanti(
            List<Sucursal> sucursalesSanti,
            List<leo.ModeloBanco.Sucursal> sucursalesLeo
    ) {
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
                    adaptarCuentaACliente(cuentaIterada, sucursalTraducida, sucursalesLeo);

                    for (Transaccion transaccionIterada : cuentaIterada.getHistorialTransacciones()) {
                        adaptarTransaccionATransferencia(transaccionIterada, sucursalTraducida, sucursalesLeo);
                    }
                }
                sucursalesTraducidas.add(sucursalTraducida);
            }
        }
        return sucursalesTraducidas;
    }

    public Cliente adaptarCuentaACliente(
            Cuenta cuentaATraducir,
            leo.ModeloBanco.Sucursal sucursalPortadora,
            List<leo.ModeloBanco.Sucursal> sucursalesLeo
    ) {
        Cliente clienteLeoExistente = buscarClienteLeoExistente(cuentaATraducir.getEmail(), sucursalesLeo);
        if (clienteLeoExistente != null) {
            return clienteLeoExistente;
        }

        String usuarioTraducido = cuentaATraducir.getEmail();
        String contraseñaTraducida = String.valueOf(cuentaATraducir.getPin());
        String nombreTraducido = cuentaATraducir.getNombre();
        String tipoCuentaTraducido = switch (cuentaATraducir.getTipoCuenta()) {
            case CAJA_AHORRO -> "Ahorro";
            case CUENTA_CORRIENTE -> "Corriente";
            case BANCO_EXTERNO -> "Externa";
        };
        BigDecimal saldoTraducido = BigDecimal.valueOf(cuentaATraducir.getSaldo());

        return new Cliente.Builder(usuarioTraducido, contraseñaTraducida, nombreTraducido, "", "Dato desconocido").saldo(saldoTraducido).tipoCuenta(tipoCuentaTraducido).permisos("").build(sucursalPortadora.registro);
    }

    public void adaptarTransaccionATransferencia(
            Transaccion transaccionATraducir,
            leo.ModeloBanco.Sucursal sucursalPortadora,
            List<leo.ModeloBanco.Sucursal> sucursalesLeo
    ) {
        Boolean esDepositoTraducido = null;

        if (transaccionATraducir.getTipoTransaccion() == TipoTransaccion.DEPOSITO) {
            esDepositoTraducido = true;
        } else if (transaccionATraducir.getTipoTransaccion() == TipoTransaccion.RETIRO) {
            esDepositoTraducido = false;
        }
        Cuenta cuentaDestino = transaccionATraducir.getDestino() != null
                ? transaccionATraducir.getDestino()
                : transaccionATraducir.getOrigen();
        Cliente destinoTraducido = adaptarCuentaACliente(cuentaDestino, sucursalPortadora, sucursalesLeo);

        if (esDepositoTraducido != null) {
            new Transferencia.Builder(esDepositoTraducido, destinoTraducido, BigDecimal.valueOf(transaccionATraducir.getMonto())).fecha("Dato desconocido").acreditar(sucursalPortadora.auditor);
        } else {
            Cliente origenTraducido = adaptarCuentaACliente(transaccionATraducir.getOrigen(), sucursalPortadora, sucursalesLeo);
            new Transferencia.Builder(origenTraducido, destinoTraducido, BigDecimal.valueOf(transaccionATraducir.getMonto())).fecha("Dato desconocido").acreditar(sucursalPortadora.auditor);
        }
    }

    private Cliente buscarClienteLeoExistente(String email, List<leo.ModeloBanco.Sucursal> sucursalesLeo) {
        String username = email;
        if (email.endsWith("@bancoleo.com")) {
            username = email.replace("@bancoleo.com", "");
        }

        for (leo.ModeloBanco.Sucursal sucursalLeo : sucursalesLeo) {
            if (sucursalLeo.getNombre().startsWith("[Banco Santi] ")) {
                continue;
            }

            Cliente cliente = sucursalLeo.registro.buscarUsername(username);
            if (cliente != null) {
                return cliente;
            }
        }

        return null;
    }
}