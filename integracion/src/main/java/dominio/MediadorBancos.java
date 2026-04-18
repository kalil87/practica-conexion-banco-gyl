package dominio;

import dominio.leo.AdaptadorABancoLeo;
import dominio.santi.AdaptadorABancoSantiago;

public class MediadorBancos {
    private final AdaptadorABancoLeo adapterLeo = new AdaptadorABancoLeo();
    private final AdaptadorABancoSantiago adapterSantiago = new AdaptadorABancoSantiago();

    public AdaptadorABancoLeo getAdapterLeo() {
        return adapterLeo;
    }

    public AdaptadorABancoSantiago getAdapterSantiago() {
        return adapterSantiago;
    }
}