package org.hplr.tournament.core.model.vo;

import java.util.Objects;

public record TournamentPairing(
        TournamentPlayer firstPlayer,
        TournamentPlayer secondPlayer
) {
    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        TournamentPairing that = (TournamentPairing) object;
        return Objects.equals(firstPlayer, that.firstPlayer) && Objects.equals(secondPlayer, that.secondPlayer)
                || Objects.equals(firstPlayer, that.secondPlayer) && Objects.equals(secondPlayer, that.firstPlayer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstPlayer, secondPlayer);
    }
}
