package DAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Model.Account;
import Model.Message;
import Util.ConnectionUtil;

public class AccountDAO{

    public static boolean duplicateUsername(String username) {
        String sql = "SELECT  COUNT (*) FROM account WHERE username = ?";
        try(Connection conn = ConnectionUtil.getConnection();
            
                PreparedStatement ps = conn.prepareStatement(sql)){
                    ps.setString(1, username);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            return rs.getInt(1) > 0;
                        }
                    }
        
                } catch (SQLException e) {
                   e.printStackTrace();
                }
        return false;
    }
    public static Account createNewAccount(Account account){
        try(Connection conn = ConnectionUtil.getConnection()){
            String sql = "INSERT INTO account (username, password) VALUES (?,?)";
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

                
                ps.setString(1, account.getUsername());
                ps.setString(2, account.getPassword());
                
                ps.executeUpdate();
                ResultSet rs = ps.getGeneratedKeys();
                if(rs.next()){
                    int newUser = (int) rs.getLong(1);
                    return new Account(newUser, account.getUsername(), account.getPassword());
                }
            return account;
            }
        
        catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static Account userLogin(String username, String password){

        try(Connection conn = ConnectionUtil.getConnection()){
            String sql = "SELECT * FROM account WHERE username = ? AND password = ?";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                return new Account(
                    rs.getInt("account_id"),
                    rs.getString("username"),
                    rs.getString("password")
                );
            }

        }
        catch(Exception e){
            e.printStackTrace();
        }
        return null;

    }

    public static boolean accountExist(int accountId){
        try(Connection conn = ConnectionUtil.getConnection()){
            String sql = "SELECT COUNT (*) FROM account WHERE account_id = ?";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, accountId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()){
                return rs.getInt(1)>0;
            }
        }

        catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }
}