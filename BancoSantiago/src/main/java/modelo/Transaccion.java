package modelo;

public class Transaccion {
    private final Cuenta origen, destino;
    private final double monto;
    private final TipoTransaccion tipoTransaccion;

    public Transaccion(Cuenta origen, Cuenta destino, double monto, TipoTransaccion tipoTransaccion) {
        this.origen = origen;
        this.destino = destino;
        this.monto = monto;
        this.tipoTransaccion = tipoTransaccion;
    }

    @Override
    public String toString() {
        return "Tipo de transacción: " + tipoTransaccion + '\n' +
                "Monto: $" + monto + '\n' +
                "Origen: " + (origen != null ? origen.getEmail() : "Ninguno") + '\n' +
                "Destino: " + (destino != null ? destino.getEmail() : "Ninguno") + '\n';
    }
}