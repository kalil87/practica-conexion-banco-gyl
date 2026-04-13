import java.math.BigDecimal;

public class DataBase {
    public DataBase() {
        new Cliente.Builder("Juan","Perez","Calle Falsa 123")
                .build();
        new Cliente.Builder("Joni","Gasto", "Calle Barata 123")
                .tipoCuenta("Ahorro")
                .build();
        new Cliente.Builder("Ricky", "Ricon", "Puerto Madero 1234")
                .tipoCuenta("Black")
                .saldo(BigDecimal.valueOf(99999))
                .build();
        new Cliente.Builder("Islas Caiman", "S.A.", "Islas Caiman, Islas Caiman")
                .tipoCuenta("Empresa")
                .saldo(BigDecimal.valueOf(1000))
                .build();
    }
}


