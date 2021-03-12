package com.company;

import javax.swing.plaf.synth.SynthOptionPaneUI;
import java.awt.geom.QuadCurve2D;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class DataService implements AutoCloseable {

    private Connection c;

    public DataService(String connection) throws DataServiceException {
        try {
            c = DriverManager.getConnection(connection);
        } catch (SQLException e) {
            throw new DataServiceException("fail to connect", e);
        }
    }

    public List<Party> getParties() throws DataServiceException {

        List<Party> parties = new ArrayList<>();

        try (PreparedStatement s = c.prepareStatement("SELECT id, name FROM Party ORDER BY id")) {
            ResultSet r = s.executeQuery();
            while (r.next()) {
                Party party = new Party();
                party.id = r.getInt("id");
                party.name = r.getString("name");
                parties.add(party);parties.size();
                System.out.println(party.id + party.name);
            }
        } catch (SQLException e) {
            throw new DataServiceException("getparties", e);
        }

        return parties;
    }

    public Party getParty(int id) throws DataServiceException {

        Party party = new Party();

        try (PreparedStatement s = c.prepareStatement("SELECT name FROM Party WHERE id = ?")) {
            s.setInt(1, id);
            ResultSet r = s.executeQuery();
            if (r.next()) {
                party.id = id;
                party.name = r.getString("name");
            } else {
                party = null;
                System.out.println("no such party of this id");
            }
        } catch (SQLException e) {
            throw new DataServiceException("getparty", e);
        }
        return party;
    }

    public void close() throws DataServiceException{

        try {
            c.close();
        } catch (SQLException e) {
            throw new DataServiceException("fail to close connection", e);
        }
        //System.out.println("close resource.");
    }

    class DataServiceException extends RuntimeException {
        DataServiceException(String errmsg, SQLException sqle) {
            System.out.println(errmsg + "wrong");
        }
    }
}
