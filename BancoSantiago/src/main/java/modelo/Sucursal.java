package modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public class Sucursal {
    private String nombre;
    private final ArrayList<Cuenta> cuentas;

    public Sucursal(String nombre) {
        setNombre(nombre);
        cuentas = new ArrayList<>();
    }

    public Cuenta crearCuenta(String nombre, String email, int pin, boolean permisosAdmin, TipoCuenta tipoCuenta) {
        Cuenta cuentaNueva = buscarCuentaSucursal(email);

        if (cuentaNueva == null) {
            cuentaNueva = new Cuenta(nombre, email, pin, permisosAdmin, this, tipoCuenta);
            cuentas.add(cuentaNueva);
        } else {
            cuentaNueva = null;
        }

        return cuentaNueva;
    }

    public void eliminarCuenta(Cuenta cuenta) {
        if (cuenta == null) {
            throw new IllegalArgumentException("(modelo.Sucursal, eliminarCuenta) la cuenta a eliminar es nula");
        }
        cuentas.remove(cuenta);
    }

    public Cuenta buscarCuentaSucursal(String email) {
        Cuenta cuentaBuscada = null;

        for (Cuenta cuenta : cuentas) {
            if (cuenta.getEmail().equalsIgnoreCase(email)) {
                cuentaBuscada = cuenta;
                break;
            }
        }
        return cuentaBuscada;
    }

    public String getNombre() {
        return nombre;
    }

    public List<Cuenta> getCuentas() {
        return Collections.unmodifiableList(cuentas);
    }

    private void setNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("(modelo.Sucursal, setNombre) el nombre de la sucursal está vacío");
        }
        this.nombre = nombre;
    }
}