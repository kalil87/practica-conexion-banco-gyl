package leo.AppCUI;

import leo.ModeloBanco.Cliente.Cliente;
import leo.ModeloBanco.Sucursal;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class SucursalReader {
    ArrayList<Sucursal> source;
    public SucursalReader(ArrayList<Sucursal> source){
        this.source = source;
    }

    public String sucursalToString(Sucursal source){
        return "Sucursal: " + source.getNombre() + " | " + source.getDireccion() + " | Inaugurado: " + source.getInauguracion();
    }

    public ArrayList<String> makeSucursalToListString(ArrayList<Sucursal> sucursales){

        ArrayList<String> lista = new ArrayList<>();
        if (sucursales == null) {return lista;}
                sucursales.forEach(s -> lista.add(sucursalToString(s)));
        return lista;
    }

    public ArrayList<String> getSucursalesToListString(){
        return makeSucursalToListString(source);
    }

    public ArrayList<Sucursal> filterSucursalesExternas(){
        return (ArrayList<Sucursal>) source.stream()
                .filter(s -> !s.getNombre().contains("[Banco Santi]"))
                .collect(Collectors.toList());
    }


    public ArrayList<Sucursal> filterSucursalesInternas(){
        return (ArrayList<Sucursal>) source.stream()
                .filter(s -> s.getNombre().contains("[Banco Santi]"))
                .collect(Collectors.toList());
    }


    public ArrayList<String> getSucursalesPropiasToListString(){
        return makeSucursalToListString(filterSucursalesExternas());
    }

    public ArrayList<String> getSucursalesExternasToListString(){
        return makeSucursalToListString(filterSucursalesInternas());
    }

    public int getListSize(){
        return source.size();
    }

    public Cliente getClienteFromAny(String username) {
        for (Sucursal iSucursal : source) {
            Cliente iCliente = iSucursal.registro.buscarUsername(username);
            if (iCliente != null) {
                return iCliente;
            }

        }
        return null;
    }

    public Sucursal hasCliente(String username){
        for (Sucursal iSucursal : source) {
            Cliente iCliente = iSucursal.registro.buscarUsername(username);
            if (iCliente != null) {
                return iSucursal;
            }

        }
        return null;
    }
    }