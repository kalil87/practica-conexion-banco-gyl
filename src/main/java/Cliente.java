import java.math.BigDecimal;

public class Cliente {
    private  String nombre;
    private String apellido;
    private String direccion;
    private String tipoCuenta;
    private BigDecimal saldo;
    private Cliente(Builder builder) {
        this.nombre = builder.nombre;
        this.apellido = builder.apellido;
        this.direccion = builder.direccion;
        this.tipoCuenta = builder.tipoCuenta;
        this.saldo = builder.saldo;
    }

    public static class Builder {
        private final String nombre;
        private final String apellido;
        private final String direccion;
        private String tipoCuenta = "Caja Corriente";
        private BigDecimal saldo = BigDecimal.ZERO; // valor por defecto

        public Builder(String nombre, String apellido, String direccion) {
            this.nombre = nombre;
            this.apellido = apellido;
            this.direccion = direccion;
        }

        public Builder saldo(BigDecimal saldo) {
            this.saldo = saldo;
            return this;
        }

        public Builder tipoCuenta(String tipo) {
            this.tipoCuenta = tipo;
            return this;
        }
        public Cliente build() {
            return new Cliente(this);
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
}
