package dominio.santiago;

import leo.ModeloBanco.Cliente.Cliente;
import leo.ModeloBanco.Transferencia.Transferencia;
import santiago.modelo.*;
import santiago.servicio.OperacionesBancarias;

import java.math.BigDecimal;
import java.util.ArrayList;

public class AdaptadorABancoSantiago {
    public ArrayList<Sucursal> adaptarSucursalesDeLeo(ArrayList<leo.ModeloBanco.Sucursal> bancoLeo) {
        ArrayList<santiago.modelo.Sucursal> listaWrapper = new ArrayList<>();

        for (leo.ModeloBanco.Sucursal indexSucursal : bancoLeo) {
            santiago.modelo.Sucursal wrapperSucursal = new Sucursal("Banco LEO " + indexSucursal.getNombre());

            for (Cliente indexCliente : indexSucursal.registro.getClientelaMap().values()){

                Cuenta wrapperCuenta = adaptarClienteDeLeo(wrapperSucursal, indexCliente);

                //Logica de Historial Transacciones

                for (Transferencia indexTransferencia : indexSucursal.auditor.getAuditoria()){
                    if (indexTransferencia.getEmisor().getUsername().equals(indexCliente.getUsername()) || indexTransferencia.getReceptor().getUsername().equals(indexCliente.getUsername()))
                    {
                        adaptarTransferenciaDeLeo(wrapperCuenta, indexTransferencia);
                    }
                }

            }
            listaWrapper.add(wrapperSucursal);
        }
        return listaWrapper;
    }

    public Cuenta adaptarClienteDeLeo(santiago.modelo.Sucursal sucursalDestino, Cliente clienteLeo) {
        boolean cuentaAdmin = clienteLeo.getPermisos().equalsIgnoreCase("ADMIN") || clienteLeo.getTipoCuenta().equalsIgnoreCase("ADMIN");
        Cuenta wrapperCliente = sucursalDestino.crearCuenta(clienteLeo.getNombre(),
                clienteLeo.getUsername() + "@bancoleo.com",
                4040,
                cuentaAdmin,
                TipoCuenta.BANCO_EXTERNO);
        if (clienteLeo.getSaldo().compareTo(BigDecimal.ZERO) > 0) {
            wrapperCliente.agregarSaldo(
                    clienteLeo.getSaldo().doubleValue()
            );
        }

        return wrapperCliente;
    }

    public void adaptarTransferenciaDeLeo(Cuenta cuentaDestino, Transferencia transferenciaLeo) {
        double monto = transferenciaLeo.getMonto().doubleValue();
        switch (transferenciaLeo.getTransaccion()){
            case RETIRO -> OperacionesBancarias.retirar(cuentaDestino, monto);
            case DEPOSITO -> OperacionesBancarias.depositar(cuentaDestino, monto);
            case TRANSFERENCIA ->
                {
                    Cuenta transferente, transferido;

                    if (cuentaDestino.getEmail().equals(transferenciaLeo.getEmisor().getUsername())) {
                        transferente = cuentaDestino;
                        transferido = adaptarClienteDeLeo(new Sucursal("placeholder"), transferenciaLeo.getReceptor());

                        transferente.restarSaldo(monto);
                        Transaccion transaccionEnviada = new Transaccion(transferente, transferido, monto, TipoTransaccion.TRANSFERENCIA_ENVIADA);
                        transferente.agregarTransaccionHistorial(transaccionEnviada);
                    }
                    else if (cuentaDestino.getEmail().equals(transferenciaLeo.getReceptor().getUsername())){
                        transferente = adaptarClienteDeLeo(new Sucursal("placeholder"), transferenciaLeo.getEmisor());
                        transferido = cuentaDestino;

                        transferido.agregarSaldo(monto);
                        Transaccion transaccionRecibida = new Transaccion(transferente, transferido, monto, TipoTransaccion.TRANSFERENCIA_RECIBIDA);
                        transferido.agregarTransaccionHistorial(transaccionRecibida);
                    }
                }
        }
    }
}
