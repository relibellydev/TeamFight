package fr.relibelly.game;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GameTypes {

    WAITING("§a✔ §2En attente §a✔ "),
    PLAYING("§4- §cEn jeu §4-"),
    FINISHING("§6= §eFin §6=");

    private String displayName;

    @Override
    public String toString() {
        return this.displayName;
    }
}
