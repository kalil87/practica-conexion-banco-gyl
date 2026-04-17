package dominio.leo;

import santiago.modelo.Cuenta;

import java.util.ArrayList;

public interface InterfaceABancoLeo {
    ArrayList<leo.ModeloBanco.Sucursal> adaptarSucursalesDeSanti(ArrayList<santiago.modelo.Sucursal> sucursalesSanti);
}