package ru.zako.litegamma.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public abstract class Database {
   private Connection connection;
   private final String url;
   private final Plugin plugin;

   @SneakyThrows
   protected Database(String url, Plugin plugin) {
      this.url = url;
      this.plugin = plugin;

      connect();
      checkTables();
   }

   @SneakyThrows
   private void connect() {

      Class.forName("com.mysql.cj.jdbc.Driver");
      connection = DriverManager.getConnection(this.url);

      if (!connection.isClosed()) {
         System.out.println("База данных " + this.getClass().getSimpleName() + " подключена!");
      }

   }

   public abstract void checkTables();

   public void push(String sql, boolean async, Object... values) {
      boolean isSync = Bukkit.isPrimaryThread();

      if (isSync && async) Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> pushInThisThread(sql, values));
      else pushInThisThread(sql, values);
   }

   @SneakyThrows
   private void pushInThisThread(String sql, Object... values) {
      PreparedStatement preparedStatement = this.prepareStatement(sql, values);
      preparedStatement.executeUpdate();
      preparedStatement.close();
   }

   @SneakyThrows
   public ResultSet pushWithReturn(String sql, Object... values) {
      PreparedStatement preparedStatement = this.prepareStatement(sql, values);
      return preparedStatement.executeQuery();
   }

   @SneakyThrows
   private PreparedStatement prepareStatement(String sql, Object... values) {
      if (connection == null || connection.isClosed()) this.connect();

      PreparedStatement preparedStatement = connection.prepareStatement(sql);

      for(int index = 0; index < values.length; ++index)
         setValueInPreparedStatement(preparedStatement, index + 1, values[index]);

      return preparedStatement;
   }

   @SneakyThrows
   private void setValueInPreparedStatement(PreparedStatement preparedStatement, int index, Object value) {
      if (value instanceof String v1) preparedStatement.setString(index, v1);
      else if (value instanceof Integer v1) preparedStatement.setInt(index, v1);
      else if (value instanceof Double v1) preparedStatement.setDouble(index, v1);
      else if (value instanceof Float v1) preparedStatement.setFloat(index, v1);
      else if (value instanceof Boolean v1) preparedStatement.setBoolean(index, v1);
      else if (value instanceof Byte v1) preparedStatement.setByte(index, v1);
      else if (value instanceof Short v1) preparedStatement.setShort(index, v1);
      else if (value instanceof Long v1) preparedStatement.setLong(index, v1);
      else if (value instanceof byte[] v1) preparedStatement.setBytes(index, v1);
      else preparedStatement.setString(index, value.toString());
   }

   @SneakyThrows
   public void close() {
      connection.close();
   }
}
