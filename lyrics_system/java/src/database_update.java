import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * updata the database
 */
public class database_update {

    /**
     * Connect to the test.db database
     *
     * @return the Connection object
     */
    private Connection connect() {
        // SQLite connection string
        String url = "jdbc:sqlite:D:\\Desktop\\Master\\dissertation\\java\\db\\lyrics.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }


    public void update(int id, String Name, String Singer, String Genre, String Bigram, String Trigram, String Rhyme,String Training,String End,String Only) {
        String sql = "UPDATE lyrics SET Name = ? , "
                + "Singer = ? ,"
                + "Genre = ? ,"
                + "Bigram = ? ,"
                + "Trigram = ? ,"
                + "Rhyme = ? ,"
                + "Training = ? ,"
                + "End = ? ,"
                + "Only = ? "
                + "WHERE id = ?";




        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // set the corresponding param
            pstmt.setString(1, Name);
            pstmt.setString(2, Singer);
            pstmt.setString(3, Genre);
            pstmt.setString(4, Bigram);
            pstmt.setString(5,Trigram);
            pstmt.setString(6, Rhyme);
            pstmt.setString(7, Training);
            pstmt.setString(8, End);
            pstmt.setString(9, Only);
            pstmt.setInt(10, id);
            // update 
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        database_update app = new database_update();
        // update the warehouse with id 3
        app.update(1, "Finished Products","Finished Products","Finished Products","Finished Products","Finished Products","Finished Products","Finished Products","Finished Products"
                ,"Finished Products");
    }

}