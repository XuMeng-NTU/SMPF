/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package support.database;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Meng
 */
public class DatabaseManager {
    private Connection connection;
    private Properties config;
    
    public static final String DB_CONFIG = "settings/database.properties";
    
    public DatabaseManager(){
        try {
            
            config = new Properties();
            config.load(new FileInputStream(DB_CONFIG));

            connection = DriverManager.getConnection(config.getProperty("jdbc"));
            
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void close(){
        try {
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
