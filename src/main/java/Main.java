public class Main {
    public static void main(String[] args) {
        DataBase objDB = new DataBase();
        CUI objCUI = new CUI();
        while (true){
        UserLogin objUserLogin = new UserLogin(objDB);
        objCUI.setActiveUser(objUserLogin);

        if (objUserLogin.isAdmin()) {
            objCUI.mainMenu();
        } else {
            System.out.println("Solo administradores por el momento" + System.lineSeparator());
        }
}
    }
}