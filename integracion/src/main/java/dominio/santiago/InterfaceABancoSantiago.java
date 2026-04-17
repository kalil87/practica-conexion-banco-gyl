package dominio.santiago;

import leo.ModeloBanco.Cliente.Cliente;
import santiago.modelo.Cuenta;
import leo.ModeloBanco.Transferencia.Transferencia;

import java.util.ArrayList;

public interface InterfaceABancoSantiago {
    ArrayList<santiago.modelo.Sucursal> adaptarSucursalesDeLeo(ArrayList<leo.ModeloBanco.Sucursal> sucursalesLeo);
    Cuenta adaptarClienteDeLeo(santiago.modelo.Sucursal sucursalDestino, Cliente clienteLeo);
    void adaptarTransferenciaDeLeo(Cuenta cuentaDestino, Transferencia transferenciaLeo);
}