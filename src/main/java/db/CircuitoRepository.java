package db;

import modelo.Circuito;

public class CircuitoRepository extends AbstractRepository<Circuito> {
    @Override
    protected String getAllQuery() {
        return "select c from modelo.Circuito c";
    }

    @Override
    protected Integer getIdElemento(Circuito elemento) {
        return elemento.getIdCircuito();
    }

    @Override
    protected Class<Circuito> getObjectClass() {
        return Circuito.class;
    }
}
