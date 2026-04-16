package modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public final class Banco {
    private static Banco instancia;
    private final ArrayList<Sucursal> sucursales;

    private Banco() {
        sucursales = new ArrayList<>();
    }

    public void crearSucursal(String nombreNuevo) {
        if (nombreNuevo == null || nombreNuevo.isBlank()) {
            throw new IllegalArgumentException("(Banco, crearSucursal) el nombre de la sucursal está vacío");
        }
        if (buscarSucursal(nombreNuevo) != null) {
            throw new IllegalStateException("(Banco, crearSucursal) ya existe una sucursal con el nombre ingresado");
        }
        sucursales.add(new Sucursal(nombreNuevo));
    }

    public Sucursal buscarSucursal(String nombreBuscado) {
        Sucursal sucursalBuscada = null;

        if (nombreBuscado != null && !nombreBuscado.isBlank()) {
            for (Sucursal sucursalIterada : sucursales) {
                if (sucursalIterada.getNombre().equalsIgnoreCase(nombreBuscado)) {
                    sucursalBuscada = sucursalIterada;
                }
            }
        }

        return sucursalBuscada;
    }

    public Cuenta buscarCuentaBanco(String emailBuscado) {
        Cuenta cuentaBuscada = null;

        if (emailBuscado != null && !emailBuscado.isBlank()) {
            for (Sucursal sucursalIterada : sucursales) {
                cuentaBuscada = sucursalIterada.buscarCuentaSucursal(emailBuscado);
                if (cuentaBuscada != null) {
                    break;
                }
            }
        }

        return cuentaBuscada;
    }

    public static Banco getInstancia(){
        if (instancia == null) {
            instancia = new Banco();
        }
        return instancia;
    }

    public List<Sucursal> getSucursales() {
        return Collections.unmodifiableList(sucursales);
    }
}