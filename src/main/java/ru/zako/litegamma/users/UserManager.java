package ru.zako.litegamma.users;

import java.util.HashMap;
import java.util.Map;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import ru.zako.litegamma.database.databases.UsersDatabase;

@RequiredArgsConstructor
public class UserManager implements Listener {
   private final Map<String, User> users = new HashMap<>();
   private final UsersDatabase usersDatabase;

   public void load(Plugin plugin) {

      plugin.getServer().getPluginManager().registerEvents(this, plugin);
      Bukkit.getOnlinePlayers().forEach((player) -> this.loadUser(player.getName()));

   }

   public User getUser(String username) {
      for (User user : users.values())
         if (user.getName().equals(username)) return user;

      return null;
   }

   public byte getLevel(User user) {
      int userLevel = user.getLevel();
      return this.intToByteArray(userLevel)[3];
   }

   public void loadUser(String username) {
      User user = this.usersDatabase.load(username);
      if (user == null)
         user = new User(username, 0, false);

      this.users.put(username, user);
   }

   public void unloadUser(@NonNull User user, boolean async) {
       usersDatabase.unload(user, async);
       if (async) users.remove(user.getName());
   }

   public void shutdown() {
      this.users.values().forEach((user) -> this.unloadUser(user, false));
   }

   @EventHandler
   public void onPlayerJoin(PlayerJoinEvent event) {
      Player player = event.getPlayer();
      String username = player.getName();
      this.loadUser(username);
   }

   @EventHandler
   public void onPlayerQuit(PlayerQuitEvent event) {
      Player player = event.getPlayer();
      String username = player.getName();

      User user = this.getUser(username);
      if (user == null) return;

      this.unloadUser(user, true);
   }

   private byte[] intToByteArray(int value) {
      return new byte[]{(byte)(value >>> 24), (byte)(value >>> 16), (byte)(value >>> 8), (byte)value};
   }
}
