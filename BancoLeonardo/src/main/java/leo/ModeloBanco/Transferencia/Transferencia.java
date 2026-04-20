package leo.ModeloBanco.Transferencia;

import leo.ModeloBanco.Cliente.Cliente;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Transferencia {
    private final TipoTransaccion transaccion;
    private final Cliente emisor;
    private final Cliente receptor;
    private final BigDecimal monto;
    private final String fecha;

    private Transferencia(Builder builder) {
        this.transaccion = builder.transaccion;
        this.emisor = builder.emisor;
        this.receptor = builder.receptor;
        this.monto = builder.monto;
        this.fecha = builder.fecha;
    }

    public static class Builder {
        private final TipoTransaccion transaccion;
        private final Cliente emisor;
        private final Cliente receptor;
        private final BigDecimal monto;

        private String fecha = LocalDateTime.now().toString();

        public Builder(Cliente emisor, Cliente receptor, BigDecimal monto) {
            this.transaccion = TipoTransaccion.TRANSFERENCIA;
            this.emisor = emisor;
            this.receptor = receptor;
            this.monto = monto;
        }

        public Builder(Boolean isDeposito, Cliente cuenta, BigDecimal monto){
            this.emisor = cuenta;
            this.receptor = cuenta;
            this.monto = monto;
            this.transaccion = isDeposito? TipoTransaccion.DEPOSITO : TipoTransaccion.RETIRO;


        }

        public Transferencia.Builder fecha(String fecha) {
            this.fecha = fecha;
            return this;
        }

        public boolean validar(InterfaceTransferencia auditor){
            if (this.emisor == null) {return false;}
            if (this.receptor == null) {return false;}
            if (this.monto == null ||
            this.monto == BigDecimal.ZERO) {return false;}
            return true;
        }

        public Transferencia acreditar(InterfaceTransferencia auditor) {
            if (!this.validar(auditor)){
                System.out.println("Problema al validar transferencia, verifique los datos");
                return null;
            }
            if (this.transaccion != TipoTransaccion.RETIRO) {
                receptor.sumarSaldo(monto);
            }
            if (this.transaccion != TipoTransaccion.DEPOSITO) {
                emisor.restarSaldo(monto);
            }
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

    public TipoTransaccion getTransaccion() {
        return transaccion;
    }
}