package dominio.santi;

import leo.ModeloBanco.Cliente.Cliente;
import leo.ModeloBanco.Transferencia.Transferencia;
import santi.modelo.Cuenta;
import santi.modelo.Sucursal;
import santi.modelo.TipoCuenta;
import santi.modelo.TipoTransaccion;
import santi.modelo.Transaccion;
import santi.servicio.OperacionesBancarias;

import java.math.BigDecimal;
import java.util.ArrayList;

public class AdaptadorABancoSantiago {
    public ArrayList<santi.modelo.Sucursal> adaptarSucursalesDeLeo(ArrayList<leo.ModeloBanco.Sucursal> bancoLeo) {
        ArrayList<santi.modelo.Sucursal> listaWrapper = new ArrayList<>();

        for (leo.ModeloBanco.Sucursal indexSucursal : bancoLeo) {
            santi.modelo.Sucursal wrapperSucursal = new Sucursal("[Banco Leo] " + indexSucursal.getNombre());

            for (Cliente indexCliente : indexSucursal.registro.getClientelaMap().values()){

                Cuenta wrapperCuenta = adaptarClienteDeLeo(wrapperSucursal, indexCliente);

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

    public Cuenta adaptarClienteDeLeo(santi.modelo.Sucursal sucursalDestino, Cliente clienteLeo) {
        Cuenta wrapperCliente = sucursalDestino.crearCuenta(clienteLeo.getNombre(),
                clienteLeo.getUsername() + "@bancoleo.com",
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

    public void adaptarTransferenciaDeLeo(Cuenta cuentaDestino, Transferencia transferenciaLeo) {
        double monto = transferenciaLeo.getMonto().doubleValue();
        String emailEmisorLeo = construirEmailLeo(transferenciaLeo.getEmisor().getUsername());
        String emailReceptorLeo = construirEmailLeo(transferenciaLeo.getReceptor().getUsername());

        switch (transferenciaLeo.getTransaccion()){
            case RETIRO -> OperacionesBancarias.retirar(cuentaDestino, monto);
            case DEPOSITO -> OperacionesBancarias.depositar(cuentaDestino, monto);
            case TRANSFERENCIA ->
                {
                    Cuenta transferente, transferido;

                    if (cuentaDestino.getEmail().equals(emailEmisorLeo)) {
                        transferente = cuentaDestino;
                        transferido = adaptarClienteDeLeo(new Sucursal("placeholder"), transferenciaLeo.getReceptor());

                        transferente.restarSaldo(monto);
                        Transaccion transaccionEnviada = new Transaccion(transferente, transferido, monto, TipoTransaccion.TRANSFERENCIA_ENVIADA);
                        transferente.agregarTransaccionHistorial(transaccionEnviada);
                    }
                    else if (cuentaDestino.getEmail().equals(emailReceptorLeo)){
                        transferente = adaptarClienteDeLeo(new Sucursal("placeholder"), transferenciaLeo.getEmisor());
                        transferido = cuentaDestino;

                        transferido.agregarSaldo(monto);
                        Transaccion transaccionRecibida = new Transaccion(transferente, transferido, monto, TipoTransaccion.TRANSFERENCIA_RECIBIDA);
                        transferido.agregarTransaccionHistorial(transaccionRecibida);
                    }
                }
        }
    }

    private String construirEmailLeo(String usernameLeo) {
        return usernameLeo + "@bancoleo.com";
    }
}
