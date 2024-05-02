package io.github.bigcookie233.simpleshop.entities;

public class Action {
    private String remoteUuid;
    private String uuid;
    private String command;
    public Action(String name, String uuid, String command) {
        this.uuid = uuid;
        this.command = command;
        this.remoteUuid = name;
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

    public void buildCommand(String minecraft_id) {
        this.command = this.command.replace("{player_id}", minecraft_id);
    }
}
