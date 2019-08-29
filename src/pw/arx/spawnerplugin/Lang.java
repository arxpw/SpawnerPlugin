package pw.arx.spawnerplugin;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

public enum Lang {
    /**
     * Messages
     */
    NO_PERMISSION("no_permission", "&f > &4You don't have permission to do that!"),
    NO_CONSOLE("no_console", "&f > &4You cannot run this command from the console!"),
    INVALID_MOB_NAME("invalid_mob_name", "&f > &4Invalid usage of /spawners alter <mobtype>, type a valid mob name."),
    INVALID_SPAWNER_TYPE("invalid_spawner_type", "&f > &4Invalid spawner type, was this picked up with a silk touch item?"),
    COMMANDS_TITLE("commands_title", " ---- Spawners - Commands ---- "),
    COMMANDS_COMMANDS("commands_commands", "/spawners alter <mobtype>."),
    SPAWNER_ALTER_SUCCESS("spawner_alter_success", "&f > Spawner type altered successfully, changed to &a{0}&f."),
    SPAWNER_ALTER_FAIL("spawner_alter_fail", "&f > A Spawner must be held in your hand!"),
    SPAWNER_PLACE_TO_PLAYER("spawner_placed_player", "You placed a &a{0} &fspawner."),
    SPAWNER_PLACE_TO_SERVER("spawner_placed_announce", "&a{0} &fplaced a &a{1} &fspawner."),
    SPAWNER_BREAK_TO_SERVER("spawner_broken_announce", "&a{0} &ffound a &a{1} &fspawner."),
    PLAYER_NOT_ONLINE("player_not_online", "&f{0} &cis not online!"),
    ;

    private String path, def;
    private static FileConfiguration LANG;

    Lang(final String path, final String start) {
        this.path = path;
        this.def = start;
    }

    public static void setFile(final FileConfiguration config) {
        LANG = config;
    }

    public String getDefault() {
        return this.def;
    }

    public String getPath() {
        return this.path;
    }
 
    public String getConfigValue(final String[] args) {
        String value = ChatColor.translateAlternateColorCodes('&',
                LANG.getString(this.path, this.def));

        if (args == null)
            return value;
        else {
            if (args.length == 0)
                return value;

            for (int i = 0; i < args.length; i++) {
                value = value.replace("{" + i + "}", args[i]);
            }
        }

        return value;
    }
}