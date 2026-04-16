package dominio;

import modelo.Banco;
import ModeloBanco.Sucursal;
import java.util.ArrayList;

public class MediadorBancoDefault implements MediadorBanco{
    private final Banco bancoSanti;
    private final ArrayList<ModeloBanco.Sucursal> sucursalesLeo;

    public MediadorBancoDefault(Banco bancoSanti, ArrayList<ModeloBanco.Sucursal> sucursalesLeo) {
        this.bancoSanti = bancoSanti;
        this.sucursalesLeo = sucursalesLeo;
    }

    @Override
    public void transferir(String transferente, String transferido, double monto) {
    }

    @Override
    public void buscarCuenta(String nombreBuscado) {

    }
}