package caponera.uned.tfm.lizardclips.db;

import caponera.uned.tfm.lizardclips.modelo.Pieza;

public class PiezaRepository extends AbstractRepository<Pieza> {
    @Override
    protected Integer getIdElemento(Pieza elemento) {
        return elemento.getIdPieza();
    }

    @Override
    protected Class<Pieza> getObjectClass() {
        return Pieza.class;
    }
}
