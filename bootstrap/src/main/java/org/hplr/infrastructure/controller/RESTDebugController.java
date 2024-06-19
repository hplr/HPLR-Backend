package org.hplr.infrastructure.controller;

import lombok.AllArgsConstructor;
import org.hplr.infrastructure.dbadapter.repositories.PlayerRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/debug")
public class RESTDebugController {

    PlayerRepository playerRepository;

}
