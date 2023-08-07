package com.filament.measurement.Authentication.Permission;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Permission {

    CHANGER_USER_READ("changer:get"),
    CHANGER_USER_UPDATE("changer:update"),
    CHANGER_USER_CREATE("changer:create"),
    CHANGER_USER_DELETE("changer:delete"),
    MASTER_USER_READ("master:get"),
    MASTER_USER_UPDATE("master:update"),
    MASTER_USER_CREATE("master:create"),
    MASTER_USER_DELETE("master:delete"),
    OWNER_USER_READ("owner:read"),
    OWNER_USER_UPDATE("owner:update"),
    OWNER_USER_CREATE("owner:create"),
    OWNER_USER_DELETE("owner:delete"),
    ;
    @Getter
    private final String permission;
}
