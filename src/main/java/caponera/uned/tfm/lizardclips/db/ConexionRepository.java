package caponera.uned.tfm.lizardclips.db;

import caponera.uned.tfm.lizardclips.modelo.Conexion;

public class ConexionRepository extends AbstractRepository<Conexion> {

    @Override
    protected Integer getIdElemento(Conexion elemento) {
        return elemento.getIdConexion();
    }

    @Override
    protected Class<Conexion> getObjectClass() {
        return Conexion.class;
    }
}
