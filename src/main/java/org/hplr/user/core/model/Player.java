package org.hplr.user.core.model;

import lombok.Getter;
import lombok.Setter;
import org.hplr.user.core.model.vo.UserData;
import org.hplr.user.core.model.vo.UserId;
import org.hplr.user.core.model.vo.PlayerRanking;
import org.hplr.user.core.model.vo.PlayerSecurity;
import org.hplr.user.core.usecases.port.dto.PlayerSelectDto;
import org.hplr.user.core.usecases.service.dto.InitialPlayerSaveDto;
import org.hplr.library.exception.HPLRValidationException;


import static org.hplr.library.core.util.ConstValues.INITIAL_ELO_VALUE;

@Getter
public class Player extends User {
    @Setter
    private PlayerRanking ranking;
    private PlayerSecurity security;

    private Player(UserId userId, PlayerRanking ranking, PlayerSecurity security, UserData userData) {
        super(userId, userData);
        this.ranking = ranking;
        this.security = security;
    }

    public static Player fromDto(PlayerSelectDto playerSelectDto) throws HPLRValidationException {
        Player player =  new Player(
                new UserId(playerSelectDto.playerId()),
                new PlayerRanking(
                        playerSelectDto.score()
                ),
                new PlayerSecurity(
                        playerSelectDto.pwHash(),
                        playerSelectDto.registrationTime(),
                        playerSelectDto.lastLogin()
                ),
                new UserData(
                        playerSelectDto.name(),
                        playerSelectDto.nickname(),
                        playerSelectDto.email(),
                        playerSelectDto.motto()
                )
        );
        PlayerValidator.validatePlayer(player);
        return player;
    }

    public static Player fromDto(InitialPlayerSaveDto initialPlayerSaveDto){
        Player player =  new Player(
                initialPlayerSaveDto.userId(),
                new PlayerRanking(
                        INITIAL_ELO_VALUE
                ),
                initialPlayerSaveDto.playerSecurity(),
                new UserData(
                        initialPlayerSaveDto.initialPlayerSaveDataDto().name(),
                        initialPlayerSaveDto.initialPlayerSaveDataDto().nickname(),
                        initialPlayerSaveDto.initialPlayerSaveDataDto().email(),
                        initialPlayerSaveDto.initialPlayerSaveDataDto().motto()
                )
        );
        PlayerValidator.validatePlayer(player);
        return player;
    }

    public PlayerSnapshot toSnapshot(){
        return new PlayerSnapshot(this);
    }
    public PlayerFullDataSnapshot toFullSnapshot(){
        return new PlayerFullDataSnapshot(this);
    }

}
