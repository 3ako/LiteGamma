package ru.zako.litegamma.database.databases;

import java.sql.ResultSet;

import lombok.SneakyThrows;
import org.bukkit.plugin.Plugin;
import ru.zako.litegamma.database.Database;
import ru.zako.litegamma.users.User;

public final class UsersDatabase extends Database {
   public UsersDatabase(String url, Plugin plugin) {
      super(url, plugin);
   }

   public void checkTables() {
      this.push("CREATE TABLE IF NOT EXISTS light(name VARCHAR(256), level INT, enable boolean, UNIQUE(name));", true);
   }

   @SneakyThrows
   public User load(String username) {

      ResultSet input = pushWithReturn("SELECT level, enable FROM light WHERE name = ?;", username);
      if (input == null) return null;

      if (input.next()) {
         int level = input.getInt("level");
         boolean enable = input.getBoolean("enable");

         return new User(username, level, enable);
      }

      return null;
   }

   public void unload(User user, boolean async) {
      push("INSERT INTO light(name, level, enable) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE level = ?, enable = ?;", async, user.getName(), user.getLevel(), user.isEnable(), user.getLevel(), user.isEnable());
   }
}
