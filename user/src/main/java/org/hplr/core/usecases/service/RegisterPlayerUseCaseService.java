package org.hplr.core.usecases.service;

import lombok.RequiredArgsConstructor;
import org.hplr.core.model.Player;
import org.hplr.core.model.PlayerFullDataSnapshot;
import org.hplr.core.model.vo.PlayerSecurity;
import org.hplr.core.model.vo.UserId;
import org.hplr.core.usecases.port.dto.InitialPlayerSaveDataDto;
import org.hplr.core.usecases.port.in.RegisterPlayerUseCaseInterface;
import org.hplr.core.usecases.port.out.command.SavePlayerDataCommandInterface;
import org.hplr.core.usecases.service.dto.InitialPlayerSaveDto;
import org.hplr.exception.HPLRIllegalStateException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class RegisterPlayerUseCaseService implements RegisterPlayerUseCaseInterface {

    final SavePlayerDataCommandInterface savePlayerDataCommandInterface;
    @Override
    public UUID registerPlayer(InitialPlayerSaveDataDto initialPlayerSaveDataDto) {
        Player player;
        PlayerFullDataSnapshot playerFullDataSnapshot;
        UserId userId = new UserId(UUID.randomUUID());
        String pwHash = generatePasswordHash(initialPlayerSaveDataDto.password(), initialPlayerSaveDataDto.repeatedPassword());
        PlayerSecurity playerSecurity = new PlayerSecurity(
                pwHash,
                LocalDateTime.now(),
                null
        );
        try{
            InitialPlayerSaveDto initialPlayerSaveDto = new InitialPlayerSaveDto(
                    initialPlayerSaveDataDto,
                    userId,
                    playerSecurity
            );
            player = Player.fromDto(initialPlayerSaveDto);
            playerFullDataSnapshot = player.toFullSnapshot();
            savePlayerDataCommandInterface.savePlayer(playerFullDataSnapshot);
        } catch(HPLRIllegalStateException exception) {
            throw new HPLRIllegalStateException(exception.getMessage());
        }
        return playerFullDataSnapshot.userId().id();
    }

    private String generatePasswordHash(String password, String repeatedPassword) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        if(Objects.equals(password, repeatedPassword)){
            return bCryptPasswordEncoder.encode(password);
        }
        else throw new HPLRIllegalStateException("Provided passwords do not match");
    }
}