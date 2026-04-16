import ModeloBanco.Cliente.Cliente;
import ModeloBanco.Cliente.InterfaceClientela;

import java.util.ArrayList;

public class DataReader {
    InterfaceClientela source;
    public DataReader(InterfaceClientela source){
        this.source = source;
    }

    public String clienteToString(Cliente source){
        return source.getUsername() + ": " + source.getNombreCompleto() + " $" + source.getSaldo() + " | " + source.getTipoCuenta();
    }

    public ArrayList<String> clientelaToListString(){
        ArrayList<Cliente> clientelaList = new ArrayList<>(source.getClientelaMap().values());
        ArrayList<String> lista = new ArrayList<>();
        clientelaList.forEach(c -> lista.add(clienteToString(c)));
        return lista;
    }

    public String getBalTotal(){
       return source.getBalTotal().toString();
    }

    public int getDataMapSize(){
        return source.getMapSize();
    }
}
