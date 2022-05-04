package com.FBinggun;

import java.util.Arrays;

public class Constants {
    public static final String[] AttitudeList = Arrays.stream(new String[]{"Attackadd",
            "Attack",
            "CritChance",
            "CritDamage",
            "Life",
            "Lifedraw",
            "Armordamage",
            "Armorchance",
            "Realdamage",
            "PVPdamage",
            "Fatal",
            "Wither",
            "Poison",
            "Frozen",
            "Weakening",
            "Nausea",
            "Blindness",
            "Armoradd",
            "Armor",
            "Dogde",
            "ThornsChance",
            "ThornsValue",
            "Health",
            "MoveSpeed",
            "SecondsHealth",
            "Experience",
            "scoperange",
            "scope"}).sorted().toArray(String[]::new);
}



