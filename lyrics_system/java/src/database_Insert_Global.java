import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * insert to the database
 */
public class database_Insert_Global {

    /**
     * Connect to the test.db database
     *
     * @return the Connection object
     */
    private Connection connect() {
        // SQLite connection string
        String url = "jdbc:sqlite:db\\lyrics.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }
    public void insert_lyrics(String Name, String Singer, String Genre, String Bigram, String Trigram,String Training,String End,String Only) {
        String sql = "INSERT INTO lyrics(Name,Singer,Genre,Bigram,Trigram,Training,End,Only) VALUES(?,?,?,?,?,?,?,?)";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, Name);
            pstmt.setString(2, Singer);
            pstmt.setString(3, Genre);
            pstmt.setString(4, Bigram);
            pstmt.setString(5,Trigram);
            pstmt.setString(6, Training);
            pstmt.setString(7, End);
            pstmt.setString(8, Only);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void insert(String rhyme_map) {
        String sql = "INSERT INTO Global(rhyme_map) VALUES(?)";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, rhyme_map);


            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void insert_bigram(String key,String value) {
        String sql = "INSERT INTO global_bigram(key,value) VALUES(?,?)";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, key);
            pstmt.setString(2, value);


            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void insert_country_bigram(String key,String value) {
        String sql = "INSERT INTO country_bigram(key,value) VALUES(?,?)";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, key);
            pstmt.setString(2, value);


            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void insert_trigram(String outkey,String innerkey,String value) {
        String sql = "INSERT INTO global_trigram(outkey,innerkey,value) VALUES(?,?,?)";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, outkey);
            pstmt.setString(2, innerkey);
            pstmt.setString(3, value);


            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void insert_country_trigram(String outkey,String innerkey,String value) {
        String sql = "INSERT INTO country_trigram(outkey,innerkey,value) VALUES(?,?,?)";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, outkey);
            pstmt.setString(2, innerkey);
            pstmt.setString(3, value);


            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void insert_end_rhyme(String key,String value) {
        String sql = "INSERT INTO country_end_rhyme_trigram(key,value) VALUES(?,?)";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, key);
            pstmt.setString(2, value);



            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        database_Insert_Global app = new database_Insert_Global();
        // insert three new rows
        app.insert("Raw Materials");
        //app.insert_trigram("asd","asdasd","ass");

    }

}