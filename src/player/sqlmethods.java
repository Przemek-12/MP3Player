package player;

import java.sql.*;
import java.util.ArrayList;

public class sqlmethods {

    public void insert(GetAndSet gas2) throws Exception{
        sqlConnection scon = new sqlConnection();
        Connection conn = scon.getConnection();

        try{
            Statement stat = conn.createStatement();
            stat.executeUpdate("insert into playlist.music ( title, path) values ('"+gas2.getTitle()+"', '"+gas2.getPath()+"')");


        }
        catch(SQLException e){}
        finally{
            closeConnection(conn);
        }
    }

    public ArrayList<String> array()throws Exception{
        ArrayList<String> array = new ArrayList<>();

        sqlConnection scon = new sqlConnection();
        Connection conn = scon.getConnection();
        String sql = "select title from playlist.music";
        try{
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery(sql);
            while(rs.next()){
                String title= rs.getString("title");
                array.add(title);
            }
        }
        catch(Exception e){}
        finally{
            closeConnection(conn);
        }
        return array;
    }

    public String sqlPath (String title) throws Exception{
        String sqlPath=null;
        sqlConnection scon = new sqlConnection();
        Connection conn = scon.getConnection();
        String sql="select path from playlist.music where title='"+title+"' ";
        try{
            Statement stat = conn.createStatement();
            ResultSet set =stat.executeQuery(sql);
            if(set.next()){
                String sqlPath2=set.getString("path");
                sqlPath = sqlPath2.replace("+","\\");
            }
        }catch(Exception e){}
        finally{
            closeConnection(conn);
        }
        return sqlPath;
    }



    public void closeConnection(Connection conn) {
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
