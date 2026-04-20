package leo.ServicioDataBase;

import leo.ModeloBanco.Cliente.Cliente;
import leo.ModeloBanco.Sucursal;
import leo.ModeloBanco.Transferencia.Transferencia;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/*Esta clase simula datos persistentes históricos a la fecha de inicializar el proyecto
Todas estas inyecciones son hard-codeadas a modo de ejemplo*/

public class DataBaseInjector extends DataBase {

    public DataBaseInjector(){
        this.DataBaseInjector(false);
    }
    public DataBaseInjector DataBaseInjector(boolean testMode){
        cargarSucursales();
        if (testMode) {cargarTests();}
        if (testMode) {cargarDuplicados();}
        cargarClientes(getCentral());
        return this;
    }

    public Sucursal getCentral(){
        return getSucursalList(0);
    }

    private Sucursal cargarCentral(){
        return new Sucursal("Central","Calle Central 5","14-04-1998");
    }
    private void cargarSucursales(){
        cargarSucursal(cargarCentral());
        cargarSucursal(new Sucursal("Parque Patricios", "MonteAgudo 255", "16-04-2026"));
    }

    private void cargarDuplicados(){
        Sucursal[] replica = {
                new Sucursal("Somos", "Anita lava la tina 808", "20-02-2002",
                        """
                                ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
                                ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣤⣶⣿⣿⣷⣤⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
                                ⠀⠀⠀⠀⠀⠀⠀⣀⣴⣾⣿⣿⣿⠋⣉⣉⠙⣿⣿⣿⣷⣦⣀⠀⠀⠀⠀⠀⠀⠀
                                ⠀⠀⠀⢀⣤⣶⣿⣿⣿⣿⣿⣿⣿⠀⣿⣿⠀⣿⣿⣿⣿⣿⣿⣿⣶⣤⡀⠀⠀⠀
                                ⠀⠀⠀⣤⣤⣤⣤⣤⡄⠀⠀⠀⢀⣠⣿⣿⣤⣤⣄⡀⠀⢠⣤⣤⣤⣤⣤⠀⠀⠀
                                ⠀⠀⠀⠀⣿⣿⣿⠀⠀⠀⢀⣴⣿⡿⠛⠛⠛⠛⠛⠇⠀⠀⠀⣿⣿⣿⠀⠀⠀⠀
                                ⠀⠀⠀⠀⣿⣿⣿⠀⠀⠀⣸⣿⠋⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣿⣿⣿⠀⠀⠀⠀
                                ⠀⠀⠀⠀⣿⣿⣿⠀⠀⠀⣿⣿⠀⠀⠀⠀⢠⣤⣤⣤⠀⠀⠀⣿⣿⣿⠀⠀⠀⠀
                                ⠀⠀⠀⠀⣿⣿⣿⠀⠀⠀⢹⣿⣆⠀⠀⠀⠀⠀⢸⣿⠀⠀⠀⣿⣿⣿⠀⠀⠀⠀
                                ⠀⠀⠀⠀⣿⣿⣿⠀⠀⠀⠀⠻⣿⣷⣤⣤⣤⣤⣾⣿⠀⠀⠀⣿⣿⣿⠀⠀⠀⠀
                                ⠀⠀⠀⠀⣿⣿⣿⠀⠀⠀⠀⠀⠈⠙⣿⣿⠛⠛⠉⠁⠀⠀⠀⣿⣿⣿⠀⠀⠀⠀
                                ⠀⠀⠀⠛⠛⠛⠛⠛⠃⠀⠀⠀⠀⠀⣿⣿⠀⠀⠀⠀⠀⠘⠛⠛⠛⠛⠛⠀⠀⠀
                                ⠀⠀⢸⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⠀⠉⠉⠀⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡇⠀⠀
                                ⠀⢰⣶⣶⣶⣶⣶⣶⣶⣶⣶⣶⣶⣶⣶⣶⣶⣶⣶⣶⣶⣶⣶⣶⣶⣶⣶⣶⡆⠀
                                ⠀⠈⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠁⠀
                                """),
                new Sucursal("somoS", "808 anit al aval atinA","2002-20-02",
                        """
                                ⠀⠁⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠈⠀
                                ⠀⡆⣶⣶⣶⣶⣶⣶⣶⣶⣶⣶⣶⣶⣶⣶⣶⣶⣶⣶⣶⣶⣶⣶⣶⣶⣶⣶⢰⠀
                                ⠀⠀⡇⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⠀⠉⠉⠀⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⢸⠀⠀
                                ⠀⠀⠀⠛⠛⠛⠛⠛⠘⠀⠀⠀⠀⠀⣿⣿⠀⠀⠀⠀⠀⠃⠛⠛⠛⠛⠛⠀⠀⠀
                                ⠀⠀⠀⠀⣿⣿⣿⠀⠀⠀⠁⠉⠛⠛⣿⣿⠙⠈⠀⠀⠀⠀⠀⣿⣿⣿⠀⠀⠀⠀
                                ⠀⠀⠀⠀⣿⣿⣿⠀⠀⠀⣿⣾⣤⣤⣤⣤⣷⣿⠻⠀⠀⠀⠀⣿⣿⣿⠀⠀⠀⠀
                                ⠀⠀⠀⠀⣿⣿⣿⠀⠀⠀⣿⢸⠀⠀⠀⠀⠀⣆⣿⢹⠀⠀⠀⣿⣿⣿⠀⠀⠀⠀
                                ⠀⠀⠀⠀⣿⣿⣿⠀⠀⠀⣤⣤⣤⢠⠀⠀⠀⠀⣿⣿⠀⠀⠀⣿⣿⣿⠀⠀⠀⠀
                                ⠀⠀⠀⠀⣿⣿⣿⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠋⣿⣸⠀⠀⠀⣿⣿⣿⠀⠀⠀⠀
                                ⠀⠀⠀⠀⣿⣿⣿⠀⠀⠀⠇⠛⠛⠛⠛⠛⡿⣿⣴⢀⠀⠀⠀⣿⣿⣿⠀⠀⠀⠀
                                ⠀⠀⠀⣤⣤⣤⣤⣤⢠⠀⡀⣄⣤⣤⣿⣿⣠⢀⠀⠀⠀⡄⣤⣤⣤⣤⣤⠀⠀⠀
                                ⠀⠀⠀⡀⣤⣶⣿⣿⣿⣿⣿⣿⣿⠀⣿⣿⠀⣿⣿⣿⣿⣿⣿⣿⣶⣤⢀⠀⠀⠀
                                ⠀⠀⠀⠀⠀⠀⠀⣀⣦⣷⣿⣿⣿⠙⣉⣉⠋⣿⣿⣿⣾⣴⣀⠀⠀⠀⠀⠀⠀⠀
                                ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣤⣷⣿⣿⣶⣤⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
                                ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡀⢀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
                                """)
        };

        for (Sucursal iSucursal: replica){
            new Cliente.Builder("dupli","dupli","Anita","Latina","Radar 69")
                    .saldo(BigDecimal.valueOf(8008))
                    .build(iSucursal.registro);

            new Cliente.Builder("adminadmin", "admin", "ADMIN", "DOBLE", "TESTSET")
                    .tipoCuenta("ADMIN")
                    .permisos("ADMIN")
                    .saldo(BigDecimal.valueOf(8888))
                    .build(iSucursal.registro);

            cargarSucursal(iSucursal);
        }
    }

    private void cargarTests() {
        Sucursal objSucursalTest =
                new Sucursal("Test", "Calle Test","01-01-1980",
                        """
                                TEST TEST TEST TEST TEST TEST TEST TEST
                                TEST TEST                     TEST TEST
                                TEST TEST                     TEST TEST
                                TEST TEST TEST           TEST TEST TEST
                                TEST TEST TEST           TEST TEST TEST
                                TEST TEST TEST           TEST TEST TEST
                                TEST TEST TEST           TEST TEST TEST
                                TEST TEST TEST TEST TEST TEST TEST TEST
                                """);

        new Cliente.Builder("testuser","123","Johnny","Test", "Area 51.1")
                .build(objSucursalTest.registro);
        new Cliente.Builder("test","123","Test","Admin", "QA")
                .saldo(BigDecimal.valueOf(50000))
                .permisos("ADMIN")
                .tipoCuenta("ADMIN")
                .build(objSucursalTest.registro);

        new Transferencia.Builder(objSucursalTest.registro.buscarUsername("test"), objSucursalTest.registro.buscarUsername("testuser"), BigDecimal.TEN)
                .acreditar(objSucursalTest.auditor);

        cargarSucursal(objSucursalTest);
    }

    private void cargarClientes(Sucursal sucursal) {
        Cliente objJuan=
                new Cliente.Builder("juanperez27", "juanelmaskpo","Juan","Perez","Calle Falsa 123")
                .build(sucursal.registro);
        Cliente objAhorrador=
                new Cliente.Builder("ahorrista", "colchon","Yoni","Gasto", "Calle Barata 123")
                .tipoCuenta("Ahorro")
                .build(sucursal.registro);
        new Cliente.Builder("cashdollar", "$$$$$$$$","Ricky", "Ricon", "Puerto Madero 1234")
                .tipoCuenta("Black")
                .saldo(BigDecimal.valueOf(Float.MAX_VALUE))
                .build(sucursal.registro);
        Cliente objEmpresa=
                new Cliente.Builder("islascaimansa", "islascaimansa","Islas Caiman", "S.A.", "Islas Caiman, Islas Caiman")
                .tipoCuenta("Empresa")
                .saldo(BigDecimal.valueOf(1000))
                .build(sucursal.registro);
        new Cliente.Builder("admin", "central", "Admin", "Admin", "Adminlandia")
                .tipoCuenta("ADMIN")
                .permisos("ADMIN")
                .build(sucursal.registro);

        new Transferencia.Builder(true, objEmpresa, BigDecimal.valueOf(5000))
                .fecha("12-04-2026 00:15")
                .acreditar(sucursal.auditor);

        new Transferencia.Builder(objEmpresa, objJuan,BigDecimal.valueOf(200))
                .fecha("13-04-2026 16:45")
                .acreditar(sucursal.auditor);
        new Transferencia.Builder(objEmpresa, objAhorrador, BigDecimal.valueOf(500))
                .fecha("13-04-2026 16:47")
                .acreditar(sucursal.auditor);
        new Transferencia.Builder(objJuan,objAhorrador, BigDecimal.TEN)
                .fecha("14-04-2026 08:08")
                .acreditar(sucursal.auditor);
    }
}


