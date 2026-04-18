package leo.AppCUI;

import leo.ModeloBanco.Transferencia.InterfaceTransferencia;
import leo.ModeloBanco.Transferencia.Transferencia;
import leo.ModeloBanco.Cliente.Cliente;

import java.util.ArrayList;

public class AuditReader {
    InterfaceTransferencia source;
    public AuditReader(InterfaceTransferencia source){
        this.source = source;
    }

    public String logToString(Transferencia source){
        switch (source.getTransaccion()){
            case TRANSFERENCIA ->{
                return source.getEmisor().getNombreCompleto() + " -> "
                        + source.getReceptor().getNombreCompleto() + "| $"
                        + source.getMonto().toString() + " | "
                        + source.getFecha();
            }
            case RETIRO -> {
                return source.getEmisor().getNombreCompleto() + " Retira: $"
                        + source.getMonto().toString() + " | "
                        + source.getFecha();
            }
            case DEPOSITO -> {
                return source.getReceptor().getNombreCompleto() + " Deposita: $"
                        + source.getMonto().toString() + " | "
                        + source.getFecha();
            }

        }
        return "Error al leer Tipo de Transacción | " + source.getFecha();
    }

    public ArrayList<Transferencia> getAllTransferenciasOfCliente(Cliente cliente){
        ArrayList<Transferencia> transferenciasOfCliente = new ArrayList<>();
        source.getAuditoria().forEach(t -> {
            if (t.getEmisor() == cliente || t.getReceptor() == cliente) {transferenciasOfCliente.add(t);}
        });
        return transferenciasOfCliente;
    }

    public ArrayList<String> makeListString(ArrayList<Transferencia> audit){
        ArrayList<String> result = new ArrayList<>();
        if (audit == null) {return result;}
        audit.forEach(t -> result.add(logToString(t)));
        return result;
    }

    public ArrayList<String> getAuditToListString(){
        return makeListString(source.getAuditoria());
    }

    public int getDataMapSize(){
        return source.getLogSize();
    }
}
