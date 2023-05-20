package utils;

import client.Main;
import message.Message;

import java.io.*;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

public class Utils {
  public enum MessageTypes {
    ADMINISTRATIVE,
    COMMON,
  }
  static String configFileName = "config.txt";
  static String chatTimeFormat = "dd.MM.yyyy HH:mm:ss";

  public static String getHost() {
    String host = "localhost";
    try (InputStream inputStream = Main.class.getResourceAsStream("/" + configFileName)) {
      Properties properties = new Properties();
      properties.load(inputStream);
      host = properties.getProperty("host", host);
    } catch (Exception e) {
      System.out.println("Error during file reading: " + e.getMessage());
    }
    return host;
  }

  public static int getPort() {
    int port = 8000;
    try (InputStream inputStream = Main.class.getResourceAsStream("/" + configFileName)) {
      Properties properties = new Properties();
      properties.load(inputStream);
      port = Integer.parseInt(properties.getProperty("port", String.valueOf(port)));
    } catch (Exception e) {
      System.out.println("Error during file reading: " + e.getMessage());
    }
    return port;
  }

  public static String getErrorMessage(IOException e) {
    return "Error during file reading: " + e.getMessage();
  }

  public static String formatMessage(Message message) {
    return getFormattedDate(message.getDate()) + " | " + message.getName() + ": " + message.getMessage();
  }

  public static String getFormattedDate(Instant date) {
    ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(date, ZoneId.systemDefault());
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(chatTimeFormat);
    return zonedDateTime.format(formatter);
  }
}
