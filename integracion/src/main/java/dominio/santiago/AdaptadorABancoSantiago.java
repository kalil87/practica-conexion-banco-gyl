package dominio.santiago;

import leo.ModeloBanco.Cliente.Cliente;
import leo.ModeloBanco.Transferencia.Transferencia;
import santiago.modelo.*;
import santiago.servicio.OperacionesBancarias;

import java.math.BigDecimal;
import java.util.ArrayList;

public class AdaptadorABancoSantiago implements InterfaceABancoSantiago {
    @Override
    public ArrayList<Sucursal> adaptarSucursalesDeLeo(ArrayList<leo.ModeloBanco.Sucursal> bancoLeo) {
        ArrayList<santiago.modelo.Sucursal> listaWrapper = new ArrayList<>();

        for (leo.ModeloBanco.Sucursal indexSucursal : bancoLeo) {
            santiago.modelo.Sucursal wrapperSucursal = new Sucursal(indexSucursal.getNombre());

            for (Cliente indexCliente : indexSucursal.registro.getClientelaMap().values()){

                Cuenta wrapperCuenta = adaptarClienteDeLeo(wrapperSucursal, indexCliente);

                //Logica de Historial Transacciones

                for (Transferencia indexTransferencia : indexSucursal.auditor.getAuditoria()){

                }

            }
            listaWrapper.add(wrapperSucursal);
        }
        return listaWrapper;
    }

    @Override
    public Cuenta adaptarClienteDeLeo(santiago.modelo.Sucursal sucursalDestino, Cliente clienteLeo) {
        Cuenta wrapperCliente = sucursalDestino.crearCuenta(clienteLeo.getNombre(),
                clienteLeo.getUsername(),
                4040,
                false,
                TipoCuenta.BANCO_EXTERNO);
        if (clienteLeo.getSaldo().compareTo(BigDecimal.ZERO) > 0) {
            wrapperCliente.agregarSaldo(
                    clienteLeo.getSaldo().doubleValue()
            );
        }

        return wrapperCliente;
    }

    @Override
    public void adaptarTransferenciaDeLeo(Cuenta cuentaDestino, Transferencia transferenciaLeo) {
        switch (transferenciaLeo.getTransaccion()){
            case RETIRO -> OperacionesBancarias.retirar(cuentaDestino, transferenciaLeo.getMonto().doubleValue());
            case DEPOSITO -> OperacionesBancarias.depositar(cuentaDestino, transferenciaLeo.getMonto().doubleValue());
            case TRANSFERENCIA -> {
                if (cuentaDestino.getEmail().equals(transferenciaLeo.getEmisor().getUsername())){
                    cuentaDestino.restarSaldo(monto);
                    Transaccion transaccionEnviada = new Transaccion(transferente, transferido, monto, TipoTransaccion.TRANSFERENCIA_ENVIADA);
                    transferente.agregarTransaccionHistorial(transaccionEnviada);
                }


                transferido.agregarSaldo(monto);

                Transaccion transaccionRecibida = new Transaccion(transferente, transferido, monto, TipoTransaccion.TRANSFERENCIA_RECIBIDA);

                transferido.agregarTransaccionHistorial(transaccionRecibida);
            };
        }
    }
}