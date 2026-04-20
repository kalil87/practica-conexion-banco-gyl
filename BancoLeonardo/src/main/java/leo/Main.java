package leo;

import leo.AppCUI.CUI;
import leo.AppCUI.UserLogin;
import leo.ServicioDataBase.DataBase;
import leo.ServicioDataBase.DataBaseInjector;

public class Main {
    public static void main(String[] args) {
        DataBase objDB = new DataBaseInjector();

        new App(objDB);
    }
}