import java.math.BigDecimal;
import java.util.Map;
import java.util.HashMap;
import java.math.BigDecimal;


public class RegistroClientela {
    private HashMap<String, Cliente> clientelaMap;

    public Cliente cargar(String username, Cliente cliente){
        return clientelaMap.put(username, cliente); // devuelve ultimo valor (o null si nuevo)
    }

    public Cliente buscarUsername(String username){
        return clientelaMap.get(username);
    }

    public BigDecimal getBalTotal(){
        return clientelaMap.values().stream()
                .map(Cliente::getSaldo)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
