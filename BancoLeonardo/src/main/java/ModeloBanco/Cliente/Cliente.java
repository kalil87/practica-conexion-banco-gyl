package ModeloBanco.Cliente;

import java.math.BigDecimal;

public class Cliente {
    private String username;
    private String password;
    private String permisos;
    private String nombre;
    private String apellido;
    private String direccion;
    private String tipoCuenta;
    private BigDecimal saldo;
    private Cliente(Builder builder) {
        this.username = builder.username;
        this.password = builder.password;
        this.permisos = builder.permisos;
        this.nombre = builder.nombre;
        this.apellido = builder.apellido;
        this.direccion = builder.direccion;
        this.tipoCuenta = builder.tipoCuenta;
        this.saldo = builder.saldo;
    }

    public static class Builder {
        private final String username;
        private final String password;
        private final String nombre;
        private final String apellido;
        private final String direccion;


        private String permisos = "Cliente";
        private String tipoCuenta = "Caja Corriente";
        private BigDecimal saldo = BigDecimal.ZERO; // valor por defecto

        public Builder(String username, String password, String nombre, String apellido, String direccion) {
            this.username = username;
            this.password = password;
            this.nombre = nombre;
            this.apellido = apellido;
            this.direccion = direccion;
        }

        public Builder saldo(BigDecimal saldo) {
            this.saldo = saldo;
            return this;
        }

        public Builder permisos(String permisos){
            this.permisos = permisos;
            return this;
        }

        public Builder tipoCuenta(String tipo) {
            this.tipoCuenta = tipo;
            return this;
        }
        public Cliente build(InterfaceClientela registro)
        {
            Cliente builtCliente = new Cliente(this);
            registro.cargar((this.username), builtCliente);
            return builtCliente;
        }
    }


    public BigDecimal getSaldo() {
        return saldo;
    }

    public String getApellido() {
        return apellido;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getTipoCuenta() {
        return tipoCuenta;
    }

    public String getNombreCompleto(){
        return nombre + " " + apellido;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getPermisos() {
        return permisos;
    }

    public void sumarSaldo(BigDecimal monto){
        saldo = saldo.add(monto);
    }

    public void restarSaldo(BigDecimal monto){
        saldo = saldo.subtract(monto);
    }
}
