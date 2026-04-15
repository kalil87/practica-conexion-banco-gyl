import java.math.BigDecimal;
import java.util.ArrayList;

public class RegistroTransferencia implements InterfaceTransferencia {
    private ArrayList<Transferencia> auditoria;

    public RegistroTransferencia() {this.auditoria = new ArrayList<>();}

    public boolean cargar(Transferencia log){
        return auditoria.add(log);
    }

    @Override
    public ArrayList<Transferencia> getAuditoria(){
        return auditoria;
    }

    @Override
    public int getLogSize(){
        return auditoria.size();
    }
}
