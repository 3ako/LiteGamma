package ru.zako.litegamma;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import org.bukkit.plugin.java.JavaPlugin;
import ru.zako.litegamma.command.LiteGammaCommand;
import ru.zako.litegamma.database.DatabaseManager;
import ru.zako.litegamma.database.databases.UsersDatabase;
import ru.zako.litegamma.users.UserManager;

public final class LiteGamma extends JavaPlugin {

    private ProtocolManager protocolManager;
    private DatabaseManager databaseManager;
    private UserManager userManager;

    public void onLoad() {
        protocolManager = ProtocolLibrary.getProtocolManager();
        databaseManager = new DatabaseManager();
    }

    public void onEnable() {
        saveDefaultConfig();
        Config.load(this.getConfig());

        databaseManager.load(Config.databaseURL, this);
        UsersDatabase usersDatabase = databaseManager.getDatabase(UsersDatabase.class);
        userManager = new UserManager(usersDatabase);
        userManager.load(this);

        protocolManager.addPacketListener(new ProtocolPacketListener(this, protocolManager, userManager));

        getCommand("litegamma").setExecutor(new LiteGammaCommand(this, userManager));
    }

    public void onDisable() {
        protocolManager.removePacketListeners(this);
        userManager.shutdown();
        databaseManager.shutdown();
    }
}
