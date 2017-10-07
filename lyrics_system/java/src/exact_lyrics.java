import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;


/**
 * This class is used for extract the lyrics in the html file
 * Also, it also looping all the rhyme with rhyme brain
 *
 * */
public class exact_lyrics {
    public static HashMap<Integer,List<String>> ryhme_map=new HashMap<Integer,List<String>>();
    public static HashSet<String> check_rhyme_unique=new HashSet<String>() ;
    public static Boolean Add_database=true;

    public static void captureJavascript(String file_name) throws Exception {
        StringBuilder lyrics =new StringBuilder();
        Scanner file = new Scanner(new FileReader(file_name));
        //    BufferedReader in = new BufferedReader(new FileReader("D:\\Desktop\\Master\\dissertation\\testing sample\\Rap/2-of-amerikaz-most-wanted-lyrics.html"));
        String end = "";
        String singer="";
        String unique="";
        String song_name="";
        String line_corrected = new String();
        HashSet<String> token_set=new HashSet<String>() ;
        HashSet<String> unique_set=new HashSet<String>() ;

        System.out.println(file_name);
        int start_index;
        int end_index;




        StringBuilder contentBuf = new StringBuilder();

        while (file.hasNext()) {

//            if (file.nextLine().lastIndexOf("</strong><br>")!=-1) {
            try {
                String temp;
                String inString;


                temp = (file.nextLine());
                song_name=temp.substring(temp.indexOf("Lyrics, ")+8,temp.indexOf(" Lyrics\"><meta name=\"classification\" content="));
                singer=temp.substring(temp.indexOf("Lyrics - ")+9,temp.indexOf("</title>"));
                if (temp.contains("lyrics</strong><br><br>")) {
                    start_index = temp.lastIndexOf("lyrics</strong><br><br>") + 23;

                }
                else
                    start_index = temp.lastIndexOf("lyrics</strong><br>")+19;



                if (temp.contains("<br><br></div>"))
                    end_index = temp.lastIndexOf("<br><br></div>");


                else if (temp.contains("<br></div>"))
                    end_index = temp.lastIndexOf("<br></div>");
                else if (temp.contains("<br><br><em>"))
                    end_index = temp.lastIndexOf("<br><br><em>");
                else if (temp.contains("<br><em>"))
                    end_index = temp.lastIndexOf("<br><em>");
                else if (temp.contains("</PRE><em>"))
                    end_index = temp.lastIndexOf("</PRE><em>");
                else if (temp.contains("</PRE></div>"))
                    end_index = temp.lastIndexOf("</PRE></div>");
                else if (temp.contains("<br><br><br>")) {
                    end_index = temp.lastIndexOf("<br><br><br>");
                }



                else
                    end_index = start_index + 1;

                inString = temp.substring(start_index, end_index).trim();
                lyrics.append(inString.toLowerCase().replaceAll("(?!\')(?!\\>)(?!\\<)(?!\\[)(?!\\])(?!\\()(?!\\))\\p{Punct}", "")
                        .replaceAll("(\\<i>.*?\\<i>)","").replaceAll("\\s+", " ").replaceAll("<br>","<br>\n")
                        .replaceAll("(\\[.*?\\])", "").replaceAll("(\\(.*?\\))", "").replaceAll("(<em>.*?<em>)", "").replaceAll("�","\\'")
                        .replaceAll("8217","\\'").replaceAll("8216","").replaceAll("\\(","").replaceAll("\\)","")+"<br>");
                //System.out.println(lyrics);
                //end.append(line_corrected.substring(line_corrected.lastIndexOf(" ") + 1, line_corrected.indexOf('<')) + ",");

                String[] toke = lyrics.toString().replaceAll("<br>\n","<br>").split("<br>");

                for (int i=0;i<toke.length;i++)
                {
                    if (toke[i].equals("\n"));

                    else {
                        String trimed=(toke[i].trim());
                        if (!trimed.substring(trimed.lastIndexOf(" ") + 1, trimed.length()).equals("")) {
                            //add all the rhyme for the word
//                            if (check_rhyme_unique.add(trimed.substring(trimed.lastIndexOf(" ") + 1, trimed.length()))) {
//                                get_rhyme(trimed.substring(trimed.lastIndexOf(" ") + 1, trimed.length()).replaceAll("(?!\")\\p{Punct}",""));
//                                System.out.println(trimed.substring(trimed.lastIndexOf(" ") + 1, trimed.length()).replaceAll("(?!\")\\p{Punct}",""));
//                            }
                            end += trimed.substring(trimed.lastIndexOf(" ") + 1, trimed.length()) + ",";
                        }
                        //get unique set word
                        String[] unique_array=(trimed.split(" "));
                        Collections.addAll(unique_set,unique_array);
                        //collect all the lyrics
                        token_set.add(trimed);
                    }
                }

                break;
//            }
            } catch (Exception e) {
                Add_database=false;
            }

        }
        //System.out.print(toke);
        //need
            if (Add_database) {
                unique = unique_set.toString().replaceAll("\\[", "").replaceAll("\\]", "").replaceAll(",", "").trim();

                //System.out.println(unique);
                Bigram b = new Bigram(token_set);
                b.train();
                b.perplexity(token_set);
                //System.out.println(b.get_bigram());

                Ngram n = new Ngram(token_set, 3);
                n.train();
                n.perplexity(token_set);
                //System.out.println(n.get_trigram());

                //write_file("texting.txt",lyrics);

                database_Insert_Global app = new database_Insert_Global();
                // insert three new rows
                app.insert_lyrics(song_name,
                        singer, "Generic", b.get_bigram(), n.get_trigram(), lyrics.toString(), end, unique);

            }
        Add_database=true;

        //need


//            ////connect to the web
//
//        String line = "";
//        String line_corrected = new String();
//        String toke = new String();
//        StringBuilder contentBuf = new StringBuilder();
//        String strURL = "http://www.elyrics.net/read/e/ed-sheeran-lyrics/what-do-i-know?-lyrics.html";
//
//        URL url = new URL(strURL);
//        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
//        InputStreamReader input = new InputStreamReader(httpConn
//                .getInputStream(), "utf-8");
//        BufferedReader bufReader = new BufferedReader(input);
//        String line = "";
//        String line_corrected  =new String();
//        String toke  =new String();
//        StringBuilder contentBuf = new StringBuilder();
//        while ((line = bufReader.readLine()) != null) {
//            if ((line.endsWith("<br>")&&!line.startsWith("<"))||(line.startsWith("<div id='inlyr' style='font-size:16px;'>"))) {
//                if (!line.contains("[")) {
//
//                    line_corrected=line.replaceAll(",", "").replaceAll("'E'", "")
//                            .replaceAll("\\.", "").replaceAll("\\n", "")
//                            .toLowerCase().replaceAll("\\?","").replaceAll("!","").replaceAll("\"","");
//                    end.append(line_corrected.substring(line_corrected.lastIndexOf(" ")+1,line_corrected.indexOf('<'))+",");
//                    toke+=line_corrected.replaceAll("<br>","")+" ";
//                    System.out.print(end);
//                    lyrics.append(line_corrected + "\n");
//                }
//            }
//
//        }
//        token_set.add(toke);
//        Bigram b = new Bigram(token_set);
//        b.train();
//        b.perplexity(token_set);
//        System.out.println(b.get_bigram());
////
//        Ngram n = new Ngram(token_set, 3);
//        n.train();
//        n.perplexity(token_set);
//        System.out.println(n.get_trigram());
//        System.out.println(token_set);
//        write_file("texting.txt",lyrics);

    }

//get rhyme
public  void get_rhyme(String rhyme_search)  throws  Exception {

    String strURL =  "http://rhymebrain.com/talk?function=getRhymes&word="+rhyme_search ;

    URL url =  new  URL(strURL);
    HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
    InputStreamReader input =  new  InputStreamReader(httpConn
            .getInputStream(),  "utf-8" );
    BufferedReader bufReader =  new  BufferedReader(input);
    String line =  "" ;
    List<String>  contentBuf =  new ArrayList<>();
    String rhyme_string="";
    contentBuf.add(rhyme_search);
    Boolean control =true;

    for (int rhyme_index:ryhme_map.keySet()){
        if (ryhme_map.get(rhyme_index).contains(rhyme_search)) {
            control = false;

            break;
        }
    }
    if (control) {
        while ((line = bufReader.readLine()) != null) {

            if (Integer.parseInt(line.substring(line.indexOf("score\":") + 7, line.indexOf("\"flags\"") - 1)) > 250) {
                String rhyme_unit = (line.substring(line.indexOf(":") + 2, line.indexOf(",") - 1)).replaceAll(" ","");
                contentBuf.add(rhyme_unit);

            }
        }
        database_Insert_Global rhyme_database = new database_Insert_Global();
        rhyme_database.insert(contentBuf.toString().replaceAll("\\[","").replaceAll("\\]",""));
        ryhme_map.put(ryhme_map.size() + 1, contentBuf);
    }

//    contentBuf.add(rhyme_string);
//
//    String[] rhyme=new String[contentBuf.size()];
//    contentBuf.toArray(rhyme);

}


    public static void write_file(String name,StringBuilder item){

        try {
            FileWriter writer = new FileWriter( name );
            PrintWriter out = new PrintWriter( writer );


            out.println(item);

            out.close();



        }
        catch ( Exception e ) {
            System.out.println( e );
        }



    }

    public static void main(String []args) throws  Exception {
        long begin = System.currentTimeMillis();
        database_select_global select = new database_select_global();
        ryhme_map=select.selectAll_rhyme();

//        File[] files_outer = new File("../testing sample/html/Generic").listFiles();
//        for (int i=0;i<files_outer.length;i++){
//    //        File[] files = new File(files_outer[i].toString()).listFiles();
////            for (int j =0;j<files.length;j++){
//
//                captureJavascript(files_outer[i].toString());
//            }


        //captureJavascript("D:/Desktop/Master/dissertation/testing sample/html/Rap/becleve-in-yourself-lyrics.html");
       // }
//        File[] files = new File("../testing sample/html/Rap").listFiles();
//        for (int i =0;i<files.length;i++){
//            System.out.println(files[i]);
//            //captureJavascript(files[i].toString());
//        }

//        database_Insert_Global rhyme_database = new database_Insert_Global();
//        // insert three new rows


//        for(int rhyme_index:ryhme_map.keySet()) {
//                rhyme_database.insert(ryhme_map.get(rhyme_index).toString().replaceAll("\\[","").replaceAll("\\]",""));
//            }
        long over = System.currentTimeMillis();
        System.out.println("Finish91164");
        System.out.println(over-begin);
    }
}