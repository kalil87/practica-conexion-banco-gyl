package dominio.leo;

import leo.ModeloBanco.Cliente.Cliente;
import leo.ModeloBanco.Transferencia.Transferencia;
import santiago.modelo.Cuenta;
import santiago.modelo.Transaccion;

import java.util.ArrayList;

public class AdaptadorABancoLeo implements InterfaceABancoLeo {
    @Override
    public ArrayList<leo.ModeloBanco.Sucursal> adaptarSucursalesDeSanti(ArrayList<santiago.modelo.Sucursal> sucursalesSanti) {
        ArrayList<leo.ModeloBanco.Sucursal> sucursalesTraducidas = new ArrayList<>();

        for (santiago.modelo.Sucursal sucursalIterada : sucursalesSanti) {
            leo.ModeloBanco.Sucursal sucursalTraducida = new leo.ModeloBanco.Sucursal(sucursalIterada.getNombre(), sucursalIterada.getNombre(), "Dato desconocido");

            for (Cuenta cuentaIterada : sucursalIterada.getCuentas()) {
                String tipoCuentaTraducido;
                switch (cuentaIterada.getTipoCuenta()) {
                    case CAJA_AHORRO -> tipoCuentaTraducido = "Ahorro";
                    case CUENTA_CORRIENTE -> tipoCuentaTraducido = "Corriente";
                    case BANCO_EXTERNO -> tipoCuentaTraducido = "Externa";
                    default -> tipoCuentaTraducido = "";
                }

                Cliente clienteTraducido = new Cliente.Builder(cuentaIterada.getNombre(),String.valueOf(cuentaIterada.getPin()), cuentaIterada.getNombre(), "", "Dato desconocido").tipoCuenta(tipoCuentaTraducido).permisos("").build(sucursalTraducida.registro);

                for (Transaccion transaccionIterada : cuentaIterada.getHistorialTransacciones()) {
                    boolean tipoTranferencia;
                    switch (transaccionIterada.getTipoTransaccion()) {
                        case DEPOSITO -> tipoTranferencia = true;
                        case RETIRO -> tipoTranferencia = false;
                        case TRANSFERENCIA_ENVIADA ->
                    }

                    Transferencia transferenciaTraducida = new Transferencia.Builder(null, null, )
                }
            }
            sucursalesTraducidas.add(sucursalTraducida);
        }
        return sucursalesTraducidas;
    }
}