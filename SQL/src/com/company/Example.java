package com.company;

public class Example {

    public static String CS = "jdbc:mariadb://localhost:3306/elections?user=vagrant&localSocket=/var/run/mysqld/mysqld.sock";

    public static void main(String[] args) {
        Party example = new Party();
        try (DataService ds = new DataService(CS);){
            if(args[0] != null){
                int id =Integer.parseInt(args[0]);
                example = ds.getParty(id);
                System.out.println(example);
            }else{
                System.out.println(ds.getParties());
            }
        } catch (DataService.DataServiceException e) {
        }
    }
}


    GNU nano 4.3                                                         Example.java                                                                   package org.example;

        import java.sql.Connection;
        import java.sql.DriverManager;
        import java.sql.SQLException;
        import java.sql.PreparedStatement;
        import java.sql.ResultSet;

public class Example {

    public static String CS = "jdbc:mariadb://localhost:3306/elections?user=vagrant&localSocket=/var/run/mysqld/mysqld.sock";

    private void readData(Connection c, int args) {
        if(args==0){System.out.println("Args");return;}
        String SQL = "SELECT name FROM Party WHERE id = ? ";
        try (PreparedStatement s = c.prepareStatement(SQL)) {
            s.setInt(1,args);
            ResultSet r = s.executeQuery();
            if (r.next()) {

                String name = r.getString("name");
                System.out.println("Party #"+ " is: " + name);
            }else{
                System.out.println("no such");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        Example example = new Example();
        try (Connection c = DriverManager.getConnection(CS)) {
            example.readData(c, Integer.parseInt(args[0]));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
