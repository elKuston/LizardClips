package caponera.uned.tfm.lizardclips.db;

import caponera.uned.tfm.lizardclips.modelo.Circuito;

public class CircuitoRepository extends AbstractRepository<Circuito> {

    @Override
    protected Integer getIdElemento(Circuito elemento) {
        return elemento.getIdCircuito();
    }

    @Override
    protected Class<Circuito> getObjectClass() {
        return Circuito.class;
    }
}
