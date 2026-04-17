package leo;

import leo.AppCUI.CUI;
import leo.AppCUI.UserLogin;
import leo.ServicioDataBase.DataBaseInjector;

public class Main {
    public static void main(String[] args) {
        DataBaseInjector objDB = new DataBaseInjector();

        new App(objDB);
    }
}