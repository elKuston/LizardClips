package caponera.uned.tfm.lizardclips.constant;

import lombok.Getter;

public enum TabPaleta {
    BASIC("Basic"), SOURCES("Sources");
    @Getter
    private final String nombre;

    TabPaleta(String nombre) {
        this.nombre = nombre;
    }
}
