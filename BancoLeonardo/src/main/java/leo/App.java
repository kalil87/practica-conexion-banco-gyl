package leo;

import leo.AppCUI.CUI;
import leo.AppCUI.UserLogin;
import leo.ServicioDataBase.DataBase;

public final class App {
    public App(DataBase dataBase) {
        boolean leoIsRunning = true;

        CUI objCUI = new CUI();
        while (leoIsRunning) {
            UserLogin objUserLogin = new UserLogin(dataBase);
            objCUI.setActiveUser(objUserLogin);
            objCUI.setSucursalList(dataBase.getSucursalList());

            objCUI.mainMenu();
            if (objCUI.getActiveUser() != null) {
                leoIsRunning=false;
            }
        }
    }
}
