import java.math.BigDecimal;
import java.util.HashMap;

public interface InterfaceClientela {
    Cliente cargar(String username, Cliente cliente);

    Cliente buscarUsername(String username);

    BigDecimal getBalTotal();

    HashMap<String, Cliente> getClientelaMap();

    int getMapSize();
}
