import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Transferencia {
    private final Cliente emisor;
    private final Cliente receptor;
    private final BigDecimal monto;
    private final String fecha;

    private Transferencia(Builder builder) {
        this.emisor = builder.emisor;
        this.receptor = builder.receptor;
        this.monto = builder.monto;
        this.fecha = builder.fecha;
    }

    public static class Builder {
        private final Cliente emisor;
        private final Cliente receptor;
        private final BigDecimal monto;

        private String fecha = LocalDateTime.now().toString();

        public Builder(Cliente emisor, Cliente receptor, BigDecimal monto) {
            this.emisor = emisor;
            this.receptor = receptor;
            this.monto = monto;
        }

        public Transferencia.Builder fecha(String fecha) {
            this.fecha = fecha;
            return this;
        }

        public Transferencia acreditar(InterfaceTransferencia auditor) {
            //Validador?
            //Próximamente, solo en cines.
            receptor.sumarSaldo(monto);
            emisor.restarSaldo(monto);
            Transferencia builtTransferencia = new Transferencia(this);
            auditor.cargar(builtTransferencia);
            return builtTransferencia;
        }
    }

    public Cliente getEmisor() {
        return emisor;
    }

    public Cliente getReceptor() {
        return receptor;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public String getFecha() {
        return fecha;
    }
}