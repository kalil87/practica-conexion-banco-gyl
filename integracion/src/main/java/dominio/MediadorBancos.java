package dominio;

import dominio.leo.AdaptadorABancoLeo;
import dominio.santi.AdaptadorABancoSanti;
import leo.ServicioDataBase.DataBase;
import santi.modelo.Banco;
import santi.modelo.Cuenta;
import santi.modelo.Sucursal;
import santi.modelo.Transaccion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MediadorBancos {
    private static final AdaptadorABancoLeo adapterLeo = new AdaptadorABancoLeo();
    private static final AdaptadorABancoSanti adapterSanti = new AdaptadorABancoSanti();
    private static final String PREFIJO_SUCURSAL_LEO_EN_SANTI = "[Banco Leo] ";
    private static final String PREFIJO_SUCURSAL_SANTI_EN_LEO = "[Banco Santi] ";
    private final DataBase bancoLeo;
    private final Banco bancoSanti;
    private final Map<String, List<TransaccionPersistida>> historialTransferenciasSanti = new HashMap<>();
    private final Map<String, List<TransaccionPersistida>> historialTransferenciasDesdeLeo = new HashMap<>();

    public MediadorBancos(DataBase bancoLeo, Banco bancoSanti){
        this.bancoLeo = bancoLeo;
        this.bancoSanti = bancoSanti;
    }

    public AdaptadorABancoLeo getAdapterLeo() {
        return adapterLeo;
    }

    public AdaptadorABancoSanti getAdapterSantiago() {
        return adapterSanti;
    }

    public void sincronizarBancos() {
        limpiarIntegracionAnterior();

        ArrayList<Sucursal> sucursalesAdaptadas = getAdapterSantiago().adaptarSucursalesDeLeo(bancoLeo.getSucursalList());
        agregarSucursalesAdaptadas(sucursalesAdaptadas);
        restaurarHistorialTransferenciasSanti();

        bancoLeo.getSucursalList().addAll(getAdapterLeo().adaptarSucursalesDeSanti(bancoSanti.getSucursales()));
    }

    private void limpiarIntegracionAnterior() {
        persistirHistorialTransferenciasSanti();
        persistirHistorialTransferenciasDesdeLeo();
        bancoSanti.eliminarSucursalesConPrefijo(PREFIJO_SUCURSAL_LEO_EN_SANTI);
        bancoLeo.getSucursalList().removeIf(sucursal -> sucursal.getNombre().startsWith(PREFIJO_SUCURSAL_SANTI_EN_LEO));
    }

    private void persistirHistorialTransferenciasSanti() {
        historialTransferenciasSanti.clear();

        for (Sucursal sucursal : bancoSanti.getSucursales()) {
            if (!sucursal.getNombre().startsWith(PREFIJO_SUCURSAL_LEO_EN_SANTI)) {
                continue;
            }

            for (Cuenta cuenta : sucursal.getCuentas()) {
                List<TransaccionPersistida> historialCuenta = historialTransferenciasSanti.computeIfAbsent(
                        cuenta.getEmail(),
                        ignored -> new ArrayList<>()
                );

                for (Transaccion transaccion : cuenta.getHistorialTransacciones()) {
                    historialCuenta.add(new TransaccionPersistida(
                            transaccion.getOrigen() != null ? transaccion.getOrigen().getEmail() : null,
                            transaccion.getDestino() != null ? transaccion.getDestino().getEmail() : null,
                            transaccion.getMonto(),
                            transaccion.getTipoTransaccion()
                    ));
                }
            }
        }
    }

    private void persistirHistorialTransferenciasDesdeLeo() {
        historialTransferenciasDesdeLeo.clear();

        for (leo.ModeloBanco.Sucursal sucursal : bancoLeo.getSucursalList()) {
            if (!sucursal.getNombre().startsWith(PREFIJO_SUCURSAL_SANTI_EN_LEO)) {
                continue;
            }

            for (leo.ModeloBanco.Transferencia.Transferencia transferencia : sucursal.auditor.getAuditoria()) {
                if (transferencia.getTransaccion() != leo.ModeloBanco.Transferencia.TipoTransaccion.TRANSFERENCIA) {
                    continue;
                }

                String emailOrigen = normalizarEmailLeo(transferencia.getEmisor().getUsername());
                String emailDestino = normalizarEmailLeo(transferencia.getReceptor().getUsername());
                double monto = transferencia.getMonto().doubleValue();

                agregarTransaccionPersistida(historialTransferenciasDesdeLeo, emailOrigen,
                        new TransaccionPersistida(emailOrigen, emailDestino, monto, santi.modelo.TipoTransaccion.TRANSFERENCIA_ENVIADA));
                agregarTransaccionPersistida(historialTransferenciasDesdeLeo, emailDestino,
                        new TransaccionPersistida(emailOrigen, emailDestino, monto, santi.modelo.TipoTransaccion.TRANSFERENCIA_RECIBIDA));
            }
        }
    }

    private void restaurarHistorialTransferenciasSanti() {
        Map<String, List<TransaccionPersistida>> historialCombinado = new HashMap<>();
        copiarHistorial(historialTransferenciasSanti, historialCombinado);
        copiarHistorial(historialTransferenciasDesdeLeo, historialCombinado);

        for (Map.Entry<String, List<TransaccionPersistida>> entry : historialCombinado.entrySet()) {
            Cuenta cuenta = bancoSanti.buscarCuentaBanco(entry.getKey());
            if (cuenta == null) {
                continue;
            }

            for (TransaccionPersistida transaccionPersistida : entry.getValue()) {
                if (cuentaYaTieneTransaccion(cuenta, transaccionPersistida)) {
                    continue;
                }

                Cuenta origen = transaccionPersistida.emailOrigen() != null
                        ? bancoSanti.buscarCuentaBanco(transaccionPersistida.emailOrigen())
                        : null;
                Cuenta destino = transaccionPersistida.emailDestino() != null
                        ? bancoSanti.buscarCuentaBanco(transaccionPersistida.emailDestino())
                        : null;

                cuenta.agregarTransaccionHistorial(
                        new Transaccion(origen, destino, transaccionPersistida.monto(), transaccionPersistida.tipo())
                );
            }
        }

        historialTransferenciasSanti.clear();
        historialTransferenciasDesdeLeo.clear();
    }

    private void copiarHistorial(Map<String, List<TransaccionPersistida>> origen, Map<String, List<TransaccionPersistida>> destino) {
        for (Map.Entry<String, List<TransaccionPersistida>> entry : origen.entrySet()) {
            for (TransaccionPersistida transaccionPersistida : entry.getValue()) {
                agregarTransaccionPersistida(destino, entry.getKey(), transaccionPersistida);
            }
        }
    }

    private void agregarTransaccionPersistida(
            Map<String, List<TransaccionPersistida>> historial,
            String emailCuenta,
            TransaccionPersistida transaccion
    ) {
        historial.computeIfAbsent(emailCuenta, ignored -> new ArrayList<>()).add(transaccion);
    }

    private String normalizarEmailLeo(String usernameLeo) {
        if (usernameLeo.contains("@")) {
            return usernameLeo;
        }
        return usernameLeo + "@bancoleo.com";
    }

    private boolean cuentaYaTieneTransaccion(Cuenta cuenta, TransaccionPersistida transaccionBuscada) {
        for (Transaccion transaccion : cuenta.getHistorialTransacciones()) {
            String emailOrigen = transaccion.getOrigen() != null ? transaccion.getOrigen().getEmail() : null;
            String emailDestino = transaccion.getDestino() != null ? transaccion.getDestino().getEmail() : null;

            if (emailsIguales(emailOrigen, transaccionBuscada.emailOrigen())
                    && emailsIguales(emailDestino, transaccionBuscada.emailDestino())
                    && transaccion.getTipoTransaccion() == transaccionBuscada.tipo()
                    && Double.compare(transaccion.getMonto(), transaccionBuscada.monto()) == 0) {
                return true;
            }
        }

        return false;
    }

    private boolean emailsIguales(String emailA, String emailB) {
        if (emailA == null || emailB == null) {
            return emailA == null && emailB == null;
        }
        return emailA.equalsIgnoreCase(emailB);
    }

    private void agregarSucursalesAdaptadas (ArrayList<santi.modelo.Sucursal> sucursalesAdaptadas) {
        for (santi.modelo.Sucursal sucursalIterada : sucursalesAdaptadas) {
            if (bancoSanti.buscarSucursal(sucursalIterada.getNombre()) == null) {
                bancoSanti.crearSucursal(sucursalIterada.getNombre());
            }

            Sucursal sucursalEspejo = bancoSanti.buscarSucursal(sucursalIterada.getNombre());

            for (Cuenta cuentaIterada : sucursalIterada.getCuentas()) {
                if (sucursalEspejo.buscarCuentaSucursal(cuentaIterada.getEmail()) == null) {
                    Cuenta cuentaEspejo = sucursalEspejo.crearCuenta(cuentaIterada.getNombre(), cuentaIterada.getEmail(), cuentaIterada.getPin(), cuentaIterada.isAdmin(), cuentaIterada.getTipoCuenta());

                    if (cuentaEspejo != null && cuentaIterada.getSaldo() > 0) {
                        cuentaEspejo.agregarSaldo(cuentaIterada.getSaldo());
                    }
                }
            }
        }
    }

    private record TransaccionPersistida(String emailOrigen, String emailDestino, double monto, santi.modelo.TipoTransaccion tipo) {}
}