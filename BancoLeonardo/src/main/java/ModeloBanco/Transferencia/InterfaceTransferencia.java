package ModeloBanco.Transferencia;

import java.util.ArrayList;
import java.util.HashMap;

public interface InterfaceTransferencia {
    boolean cargar(Transferencia log);

    ArrayList<Transferencia> getAuditoria();

    int getLogSize();
}
