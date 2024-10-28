package org.hplr.user.core.model;

import lombok.Getter;
import org.hplr.user.core.model.vo.UserData;
import org.hplr.user.core.model.vo.UserId;

@Getter
public class User {

    private UserId userId;
    private UserData userData;

    public User(UserId userId, UserData userData) {
        this.userId = userId;
        this.userData = userData;
    }
}
