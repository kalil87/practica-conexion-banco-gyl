public class Cliente {
    String nombre;
    String apellido;
    String direccion;
    String tipoCuenta;
    java.math.BigDecimal saldo;
    public Cliente (String nombre, String apellido, String direccion, String tipoCuenta){

    }

    public Cliente (String nombre, String apellido, String direccion, String tipoCuenta, java.math.BigDecimal saldo){
        this(nombre, apellido,direccion,tipoCuenta);
        this.saldo = saldo;
    }
}
