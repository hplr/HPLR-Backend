package org.hplr.elo.core.usecases.port.dto;


import java.util.UUID;

public record PlayerRankingDto(UUID playerId, String playerName, Long score) implements Comparable<PlayerRankingDto> {

    @Override
    public boolean equals(Object obj) {
        return obj == this || obj != null && obj.getClass() == this.getClass();
    }

    @Override
    public int hashCode() {
        return this.playerId.hashCode() * this.score.hashCode();
    }

    @Override


    public int compareTo(PlayerRankingDto o) {
        return (o.score()).compareTo(score);
    }
}
