package ModeloBanco.Cliente;

import java.math.BigDecimal;
import java.util.Map;
import java.util.HashMap;
import java.math.BigDecimal;


public class RegistroClientela implements InterfaceClientela {
    private HashMap<String, Cliente> clientelaMap;

    public RegistroClientela() {
        this.clientelaMap = new HashMap<>();
    }

    @Override
    public Cliente cargar(String username, Cliente cliente){
        return clientelaMap.put(username, cliente); // devuelve ultimo valor (o null si nuevo)
    }

    @Override
    public Cliente buscarUsername(String username){
        return clientelaMap.get(username);
    }

    @Override
    public BigDecimal getBalTotal(){
        return clientelaMap.values().stream()
                .map(Cliente::getSaldo)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public HashMap<String, Cliente> getClientelaMap(){
        return new HashMap<>(clientelaMap);
    }

    @Override
    public int getMapSize(){
        return clientelaMap.size();
    }
}
