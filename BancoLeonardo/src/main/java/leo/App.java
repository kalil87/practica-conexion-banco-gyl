package leo;

import leo.AppCUI.CUI;
import leo.AppCUI.UserLogin;
import leo.ServicioDataBase.DataBaseInjector;

public final class App {
    public App(DataBaseInjector dataBase) {
        boolean leoIsRunning = true;

        CUI objCUI = new CUI();
        while (leoIsRunning) {
            UserLogin objUserLogin = new UserLogin(dataBase);
            objCUI.setActiveUser(objUserLogin);
            objCUI.setSucursalList(dataBase.getSucursalList());

            leoIsRunning = objCUI.mainMenu();
        }
    }
}
