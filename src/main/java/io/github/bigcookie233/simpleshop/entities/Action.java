package io.github.bigcookie233.simpleshop.entities;

import java.util.UUID;

public class Action {
    private String remoteUuid;
    private String uuid;
    private String command;

    public Action(String remoteUuid, String uuid, String command) {
        this.uuid = uuid;
        this.command = command;
        this.remoteUuid = remoteUuid;
    }

    public String getUuid() {
        return uuid;
    }

    public String getRemoteUuid() {
        return remoteUuid;
    }

    public String getCommand() {
        return command;
    }

    public void buildCommand(String minecraft_id, int amount) {
        this.command = this.command.replace("{player_id}", minecraft_id).replace("{amount}", String.valueOf(amount));
    }
}
