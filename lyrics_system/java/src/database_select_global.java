import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 *
 * select from the database
 */
public class database_select_global {

    private HashMap<Integer, List<String>> get_data = new HashMap<>();
    private static HashMap<String, LinkedHashSet<String>> end_word_rhyme_map = new HashMap<>();

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


    /**
     * select all rows in the warehouses table
     */

    //get all the rhyme
    public HashMap<Integer, List<String>> selectAll_rhyme() {
        String sql = "SELECT id, rhyme_map FROM Global";

        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // loop through the result set
            while (rs.next()) {
                String strCopy = "";
                List get_data_list = new ArrayList();
                String get_data_value = rs.getString("rhyme_map").replaceAll(",", "");
                String[] rhyme_array = get_data_value.split(" ");
                List<String> rhyme_list = new ArrayList<>();
                Collections.addAll(rhyme_list, rhyme_array);

                get_data.put(rs.getInt("id"), rhyme_list);
//                System.out.println(rs.getInt("id") +  "\t" +
//                        rs.getString("rhyme_map"));

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


        return get_data;
    }

    //get all the bigram
    public String selectAll_lyrics_bigram() {
        String sql = "SELECT  Bigram FROM lyrics";
        String bigram_merge = "";
        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {


            // loop through the result set
            while (rs.next()) {


                bigram_merge += rs.getString("Bigram");

            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return bigram_merge;

    }

    //get all the bigram from country
    public String selectAll_country_bigram() {
        String sql = "SELECT  Bigram,Genre FROM lyrics";
        String bigram_merge = "";
        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {


            // loop through the result set
            while (rs.next()) {

                if (rs.getString("Genre").equals("Country"))
                bigram_merge += rs.getString("Bigram");

            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return bigram_merge;

    }


    //get all the word number in a line in a certain song
    public int[] selectline_number() {
        String sql = "SELECT  Name,Training FROM lyrics";
        String name = "";
        String[] training;

        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {


            // loop through the result set
            while (rs.next()) {

                training= rs.getString("Training").split("\n");
                int[] each_line_word=new int[training.length];
                for(int i=0;i<training.length;i++){
                    each_line_word[i]=training[i].replaceAll("<br>","").split(" ").length;

                }
                return each_line_word;

            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        int[] d=new int[6];
        return  d;

    }


    //get all the trigram
    public String selectAll_lyrics_trigram() {
        String sql = "SELECT  trigram FROM lyrics";
        String trigram_merge = "";
        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {


            // loop through the result set
            while (rs.next()) {


                trigram_merge += rs.getString("trigram");


            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return trigram_merge;

    }


    //get all the trigram from country
    public String selectAll_country_trigram() {
        String sql = "SELECT  Genre,trigram FROM lyrics";
        String trigram_merge = "";
        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {


            // loop through the result set
            while (rs.next()) {

                if (rs.getString("Genre").equals("Country"))
                    trigram_merge += rs.getString("trigram");


            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return trigram_merge;

    }



    //set all the bigram from global to hashmap
    public HashMap<String, List<String>> select_globol_bigram() {
        HashMap<String, List<String>> bigram = new HashMap<>();

        String sql = "SELECT  key,value FROM global_bigram";
        String bigram_value = "";

        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {


            // loop through the result set
            while (rs.next()) {

                bigram_value = rs.getString("value").replaceAll("\\[", "").replace("\\]", "");
                String[] bigram_value_array = bigram_value.split(",");
                List<String> value_list = new ArrayList<>();
                Collections.addAll(value_list,bigram_value_array);
                bigram.put(rs.getString("key"), value_list);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


        return bigram;

    }


    //set all the bigram from country to hashmap
    public HashMap<String, List<String>> select_country_bigram_rhyme(String target) {
        HashMap<String, List<String>> bigram = new HashMap<>();

        String sql = "SELECT  key,value FROM "+target;
        String bigram_value = "";

        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {


            // loop through the result set
            while (rs.next()) {
                List<String> value_list = new ArrayList<>();

                bigram_value = rs.getString("value").replaceAll("\\[", "").replace("]", "");
                String[] bigram_value_array = bigram_value.split(",");
                for (int i=0;i<bigram_value_array.length;i++){
                    value_list.add(bigram_value_array[i].trim());
                }



                bigram.put(rs.getString("key"), value_list);

            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


        return bigram;

    }



    //set all the bigram from global to hashmap
    public HashMap<String, HashMap<String,List<String>>> select_globol_trigram() {
        HashMap<String, HashMap<String,List<String>>> trigram = new HashMap<>();


        String sql = "SELECT  outkey,innerkey,value FROM global_trigram";
        String trigram_value = "";

        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {


            // loop through the result set
            while (rs.next()) {
                HashMap<String,List<String>> inner_map = new HashMap<>();
                List<String> value_list = new ArrayList<>();
                trigram_value = rs.getString("value").replaceAll("\\[", "").replace("\\]", "");
                String[] trigram_value_array = trigram_value.split(",");
                Collections.addAll(value_list,trigram_value_array);
                if (trigram.get(rs.getString("outkey")) == null) {
                    inner_map.put(rs.getString("innerkey"),value_list);
                    trigram.put(rs.getString("outkey"), inner_map);
                }
                else {
                    if (!trigram.get(rs.getString("outkey")).containsKey(rs.getString("innerkey"))) {

                        inner_map = (HashMap<String, List<String>>) trigram.get(rs.getString("outkey")).clone();
                        inner_map.put(rs.getString("innerkey"), value_list);
                        trigram.put(rs.getString("outkey"), inner_map);

                    }

                }

            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


        return trigram;

    }


    //set all the trigram from country to hashmap
    public HashMap<String, HashMap<String,List<String>>> select_country_trigram() {
        HashMap<String, HashMap<String,List<String>>> trigram = new HashMap<>();


        String sql = "SELECT  outkey,innerkey,value FROM country_trigram";
        String trigram_value = "";

        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {


            // loop through the result set
            while (rs.next()) {
                HashMap<String,List<String>> inner_map = new HashMap<>();
                List<String> value_list = new ArrayList<>();
                trigram_value = rs.getString("value").replaceAll("\\[", "").replace("]", "");
                String[] trigram_value_array = trigram_value.split(",");
                for(int i=0;i<trigram_value_array.length;i++){
                    value_list.add(trigram_value_array[i].trim());

                }
             //  Collections.addAll(value_list,trigram_value_array);
                if (trigram.get(rs.getString("outkey")) == null) {
                    inner_map.put(rs.getString("innerkey"),value_list);
                    trigram.put(rs.getString("outkey"), inner_map);
                }
                else {
                    if (!trigram.get(rs.getString("outkey")).containsKey(rs.getString("innerkey"))) {

                        inner_map = (HashMap<String, List<String>>) trigram.get(rs.getString("outkey")).clone();
                        inner_map.put(rs.getString("innerkey"), value_list);
                        trigram.put(rs.getString("outkey"), inner_map);

                    }

                }

            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


        return trigram;

    }


    //find the rhyme index
    public  int rhyme_matching(HashMap<Integer, List<String>> rhyme_matching, String target) {
        for (int map_index : rhyme_matching.keySet()) {

            if (rhyme_matching.get(map_index).contains(target)) {

                return map_index;
            }
        }
        return -1;
    }


    //get the choice list

    public List<String> select_choice(int option){

        String sql = "SELECT  id,choice FROM choice_country";
        String[] choice ;
        List<String> choice_list=new ArrayList<>();


        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {


            // loop through the result set
                switch (option) {
                        case (1):
                            while (rs.next()) {
                                if (rs.getString("id").equals("1")) {
                                    choice=rs.getString("choice").replaceAll("\\[","")
                                            .replaceAll("]","").replaceAll("\\{","")
                                            .replaceAll("}","").split(",");

                                    for(int i=0;i<choice.length;i++){
                                        choice_list.add(choice[i].substring(choice[i].lastIndexOf("=")+1,choice[i].length()));

                                    }
                                }
                    }
                    case (2):
                        while (rs.next()) {
                            if (rs.getString("id").equals("2")) {
                                choice=rs.getString("choice").replaceAll("\\[","")
                                        .replaceAll("]","").replaceAll("\\{","")
                                        .replaceAll("}","").split(",");

                                for(int i=0;i<choice.length;i++){
                                    choice_list.add(choice[i].substring(choice[i].lastIndexOf("=")+1,choice[i].length()));

                                }
                            }
                        }
                    case (3):
                        int counter=0;
                        while (rs.next()) {

                            choice=rs.getString("choice").replaceAll("\\[","")
                                    .replaceAll("]","").replaceAll("\\{","")
                                    .replaceAll("}","").split(",");

                            for(int i=0;i<choice.length;i++){
                                choice_list.add(choice[i].substring(choice[i].lastIndexOf("=")+1,choice[i].length()));

                            }
                            if (counter==0){
                                choice_list.add("---------------------The Common Words Above Generated According To The Frequency-----------------" +
                                        "--------------");
                            }
                            else
                                choice_list.add("---------------------The Special Words Above Generated According To TF-IDF algorithm-----------------" +
                                        "--------------");
                            counter++;
                        }
                }



        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return choice_list;


    }

    //get end word from country
    public HashSet<String> selectAll_end_country() {
        String sql = "SELECT  Genre,End FROM lyrics";
        HashSet<String> end = new HashSet<>();
        String[] end_array;

        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            database_select_global app = new database_select_global();
            // loop through the result set
            while (rs.next()) {

                if (rs.getString("Genre").equals("Country")) {
                    end_array=rs.getString("End").split(",");
                    for(String end_word:end_array) {

                        if(!end_word.replaceAll("\\d","").trim().equals("")&&!end_word.replaceAll("\\d","").trim().equals("'"))
                        {   //add the end word data
                            end.add(end_word.replaceAll("\\d","").trim());

                            //insert rhyme to the hashmap
//                            LinkedHashSet<String> rhyme_value=new LinkedHashSet<>();
//                            int inner_counter=0;
//
//                            int key_rhyme=app.rhyme_matching(app.selectAll_rhyme(), end_word.replaceAll("\\d", "").trim());
//                            if(key_rhyme!=-1) {
//                                for (int i = 1; i < end_array.length; i++) {
//                                    //check the rhyme word from the rhyme map
//                                    if (app.selectAll_rhyme().get(key_rhyme).contains(end_array[i].replaceAll("\\d", "").trim())) {
//
//                                        if (rhyme_value.add(end_array[i].replaceAll("\\d", "").trim())) {
//                                            rhyme_value.add(end_array[i].replaceAll("\\d", "").trim());
//                                            inner_counter++;
//                                        }
//                                        if (inner_counter == 2)
//                                            break;
//
//                                    }
//
//                                }
//
//                                //check duplicate of the key
//                                if (end_word_rhyme_map.containsKey(end_word.replaceAll("\\d", "").trim()))
//                                    end_word_rhyme_map.get(end_word.replaceAll("\\d", "").trim()).addAll(rhyme_value);
//                                else
//                                    end_word_rhyme_map.put(end_word.replaceAll("\\d", "").trim(), rhyme_value);
//                            }
                            //insert_end_rhyme()
                        }
                    }
                    }


            }


        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return end;

    }



    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        database_select_global app = new database_select_global();
    //    System.out.println(app.selectAll_rhyme().get(app.rhyme_matching(app.selectAll_rhyme(),"country")));

//        database_select_global app = new database_select_global();
//        //System.out.println(app.selectAll_bigram());
//        System.out.println(app.select_choice(1));


      // System.out.println(app.rhyme_matching(app.selectAll_rhyme(),"h"));
    }




}