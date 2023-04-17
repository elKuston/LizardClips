package caponera.uned.tfm.lizardclips.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum AnguloRotacion {
    ROT_0(0), ROT_90(90), ROT_180(180), ROT_270(270);

    private final int angulo;
}
