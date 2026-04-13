import java.math.BigDecimal;

public interface InterfaceClientela {
    Cliente cargar(String username, Cliente cliente);

    Cliente buscarUsername(String username);

    BigDecimal getBalTotal();
}
