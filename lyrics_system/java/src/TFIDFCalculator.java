import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.HashSet;
import java.util.TreeMap;
/**
 In this class , it is used to  generated the common words and
 specific country word by TFIDF., this class also insert the words into database
 */
public class TFIDFCalculator {
    private List<List<String>> document_merge=new ArrayList<>();
    private TreeMap<Double,String> common_list=new TreeMap<>();
    private TreeMap<Double,String> TFIDF_list=new TreeMap<>();
    private HashSet test_uniqle =new HashSet();
    private HashSet frequency_uniqle =new HashSet();

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



    public double tf(List<String> doc, String term) {
        double result = 0;
        for (String word : doc) {
            if (term.equalsIgnoreCase(word))
                result++;
        }
        return result / doc.size();
    }

    /**
     * @param docs list of list of strings represents the dataset
     * @param term String represents a term
     * @return the inverse term frequency of term in documents
     */
    public double idf(List<List<String>> docs, String term) {
        Double[] toRemove = new Double[1];
        double n = 0;
        for (List<String> doc : docs) {
            for (String word : doc) {
                if (term.equalsIgnoreCase(word)) {
                    n++;
                    break;
                }
            }
        }
//All for the get the common words part
            String clone="";
            if (common_list.size() <25) {
            if(frequency_uniqle.add(n)) {
                frequency_uniqle.add(n);
                common_list.put(n, term);
                System.out.println(common_list);
            }
            else{
                clone=common_list.get(n);
                common_list.put(n,clone+" "+term);
                System.out.println(common_list);
            }
            }
            else{
                for(double key:common_list.keySet()){

                    if(Double.compare(n,key)>0){
                        toRemove[0]=key;
                        common_list.put(n,term);
                        break;
                    }
                    else if(Double.compare(n,key)<0);
                    else{
                        clone=common_list.get(n);
                        common_list.put(n,clone+" "+term);
                        break;
                    }
                }
            }

        if (toRemove[0]!=null)
        common_list.remove(toRemove[0]);

        return Math.log(docs.size() / n);
    }


    public void insert_choics_of_the_words(String choice) {
        String sql = "INSERT INTO choics_country(choice) VALUES(?)";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, choice);


            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * @param doc  a text document
     * @param docs all documents
     * @param term term
     * @return the TF-IDF of term
     */
    public double tfIdf(List<String> doc, List<List<String>> docs, String term) {
        return tf(doc, term) * idf(docs, term);

    }

    public void select_only_country(){

        String sql = "SELECT  Only,Genre FROM lyrics";
        List<String> training_country = new ArrayList<>();
        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {




            // loop through the result set
            while (rs.next()) {
                training_country = new ArrayList<>();
                if (rs.getString("Genre").equals("Country")) {
                    Collections.addAll(training_country,rs.getString("Only").replaceAll("<br>","")
                            .replaceAll("\n"," ").trim().split(" "));


                    document_merge.add(training_country);

                }

            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }



    }

    public void select_training_country(TFIDFCalculator calculator){
        //set the

        select_only_country();


        String sql = "SELECT  Training,Genre FROM lyrics";
        List<String> training_country = new ArrayList<>();
        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            // loop through the result set
            while (rs.next()) {
                training_country = new ArrayList<>();
                if (rs.getString("Genre").equals("Country")) {
                    Collections.addAll(training_country,rs.getString("Training").replaceAll("<br>","")
                            .replaceAll("\n"," ").trim().split(" "));



                    for (int i=0;i<training_country.size();i++) {
                        Double[] toRemove = new Double[1];
                        if(test_uniqle.add(training_country.get(i))) {
                            test_uniqle.add(training_country.get(i));
                            //get idf only
                            //idf(calculator.get_merge_list(),training_country.get(i));
                            double tfidf_country = calculator.tfIdf(training_country, calculator.get_merge_list(), training_country.get(i));

                            String clone="";
                            if (TFIDF_list.size() <40) {
                                if(frequency_uniqle.add(tfidf_country)) {
                                    frequency_uniqle.add(tfidf_country);
                                    TFIDF_list.put(tfidf_country, training_country.get(i));

                                }
                                else{
                                    clone=TFIDF_list.get(tfidf_country);
                                    TFIDF_list.put(tfidf_country,clone+" "+training_country.get(i));
                                    System.out.println(TFIDF_list);
                                }
                            }
                            else{
                                for(double key:TFIDF_list.keySet()){

                                    if(Double.compare(tfidf_country,key)>0){
                                        toRemove[0]=key;
                                        TFIDF_list.put(tfidf_country,training_country.get(i));
                                        break;
                                    }
                                    else if(Double.compare(tfidf_country,key)<0);
                                    else{
                                        clone=TFIDF_list.get(tfidf_country);
                                        TFIDF_list.put(tfidf_country,clone+" "+training_country.get(i));
                                        break;
                                    }
                                }
                            }

                            if (toRemove[0]!=null)
                                TFIDF_list.remove(toRemove[0]);
                        }
                        }



                }


            }

            insert_choics_of_the_words(common_list.descendingMap().toString());
            insert_choics_of_the_words(TFIDF_list.descendingMap().toString());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }



    }

    public List<List<String>> get_merge_list(){
        return document_merge;
    }

    public TreeMap<Double,String> get_treeset(){
        return common_list;
    }

    public static void main(String[] args) {

        List<String> doc1 = Arrays.asList("I", "have", "a", "cat");
        List<String> doc2 = Arrays.asList("I", "have", "a", "dog", "dog", "dog", "dog");
        List<String> doc3 = Arrays.asList("I", "want", "a", "dog");
        List<String> doc4 = Arrays.asList("I", "want", "a", "dog");
        List<List<String>> documents = Arrays.asList(doc1, doc2, doc3,doc4);

        TFIDFCalculator calculator2 = new TFIDFCalculator();
        //double tfidf = calculator2.tfIdf(doc2, documents, "doto");
        //System.out.println("TF-IDF (ipsum) = " + tfidf);
        //System.out.println(calculator.select_training_country());
        calculator2.select_training_country(calculator2);
      //  double tfidf_country = calculator.tfIdf(doc1, calculator.select_only_country(), "dog");

    }


}