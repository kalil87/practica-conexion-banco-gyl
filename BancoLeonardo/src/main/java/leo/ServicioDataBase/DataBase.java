package leo.ServicioDataBase;

import leo.ModeloBanco.Sucursal;

import java.util.ArrayList;

public class DataBase {
    private final ArrayList<Sucursal> sucursalList = new ArrayList<>();

    public ArrayList<Sucursal> getSucursalList() {
        return sucursalList;
    }

    public Sucursal getSucursalList(int index){
        return sucursalList.get(index);
    }
}
