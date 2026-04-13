import java.math.BigDecimal;

//Esta clase simula datos persistentes históricos a la fecha de inicializar el proyecto
//Todas estas inyecciones son hard-codeadas a modo de ejemplo

public class DataBase {
    public void DataBase(){
        cargarSucursales();
        cargarAdmin();
        cargarClientes();
    }

    private void cargarSucursales(){
        new Sucursal.Builder()
    }

    private void cargarClientes() {
        new Cliente.Builder("juanperez27", "juanelmaskpo","Juan","Perez","Calle Falsa 123")
                .build();
        new Cliente.Builder("ahorrista", "colchon","Joni","Gasto", "Calle Barata 123")
                .tipoCuenta("Ahorro")
                .build();
        new Cliente.Builder("cashdollar", "$$$$$$$$","Ricky", "Ricon", "Puerto Madero 1234")
                .tipoCuenta("Black")
                .saldo(BigDecimal.valueOf(999999999))
                .build();
        new Cliente.Builder("islascaimansa", "islascaimansa","Islas Caiman", "S.A.", "Islas Caiman, Islas Caiman")
                .tipoCuenta("Empresa")
                .saldo(BigDecimal.valueOf(1000))
                .build();
    }
}


