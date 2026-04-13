public class Main {
    public static void main(String[] args) {
        CUI objCUI = new CUI();
        objCUI.printLogo();
        UserLogin objUserLogin = new UserLogin(); // Se pueden cargar argumento username y password para hacer validaciones

        objCUI.mainMenu();
        DataBase objDB = new DataBase();

    }
}