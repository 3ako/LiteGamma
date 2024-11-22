package ru.zako.litegamma.database;

import java.util.HashMap;

import lombok.SneakyThrows;
import org.bukkit.plugin.Plugin;
import ru.zako.litegamma.database.databases.UsersDatabase;

public final class DatabaseManager {
   private final HashMap<String, Database> databases = new HashMap<>();

   @SneakyThrows
   public void load(String file, Plugin plugin) {
      Database reputationDatabase = new UsersDatabase(file, plugin);
      registerDatabase(reputationDatabase);
   }

   @SneakyThrows
   private void registerDatabase(Database databaseClass) {
      databases.put(databaseClass.getClass().getSimpleName(), databaseClass);
   }

   @SneakyThrows
   public void shutdown() {
      databases.values().forEach(Database::close);
   }

   public <T extends Database> T getDatabase(Class<T> databaseClass) {
      String name = databaseClass.getSimpleName();
      if (!databases.containsKey(name))
         throw new RuntimeException("Database '" + name + "' not found!");

      Database database = databases.get(name);
      return databaseClass.cast(database);
   }
}
