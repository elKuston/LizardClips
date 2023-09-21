package caponera.uned.tfm.lizardclips.constant;

import lombok.Getter;

public enum TabPaleta {
    BASIC("Basic"), SOURCES("Sources"), GATES("Gates"), TRISTATES("Tristates"),
    MULTIPLEXERS("Multiplexers"), UTILITIES("Utilities");
    @Getter
    private final String nombre;

    TabPaleta(String nombre) {
        this.nombre = nombre;
    }
}
