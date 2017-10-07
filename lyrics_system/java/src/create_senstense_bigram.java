import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

/**
 * extract bigram  from database and convert them into  hashmap
 */
public class create_senstense_bigram implements Runnable{


    public String file_bigram=new String();
    public Scanner bigram;
    private HashMap<String, List<String>> senstense_map = new HashMap();
    private static HashMap<String, List<String>> senstense_map2 = new HashMap();

    public create_senstense_bigram(String txt_name) throws Exception{
        this.file_bigram=txt_name;
        File file = new File(file_bigram);
        bigram=new Scanner(file);

    }

    public void getting_all_bigram(){

        database_select_global app = new database_select_global();

        String bigram_all_get=app.selectAll_lyrics_bigram()
                .toLowerCase();

        String[] bigram_all_array=bigram_all_get.split("\n");
        String bigram_all;


        for (int i=0;i<bigram_all_array.length;i++) {
            List<String> a = new ArrayList<>();
            //unique.add(sc.next().replaceAll(",","").replaceAll("'S'","").replaceAll("'E'",""));
            bigram_all = bigram_all_array[i]
                    .toLowerCase();
            String[] map=bigram_all.split(" ");

            if (senstense_map2.get(map[0])==null)
            {
                a.add(map[1]+" "+map[2]);
                senstense_map2.put(map[0],a);
            }
            else {
                if (senstense_map2.get(map[0]).contains(map[1]+" "+map[2]));
                else {
                    Boolean add=true;
                    for (int j=0;j<senstense_map2.get(map[0]).size();j++){
                        String[] item_split=senstense_map2.get(map[0]).get(j).split(" ");
                        if(item_split[0].equals(map[1])){
                            add=false;
                            if (Double.parseDouble(item_split[1])<Double.parseDouble(map[2])) {
                                senstense_map2.get(map[0]).set(j, map[1] + " " + map[2]);
                                break;
                            }
                        }


                    }
                    if(add)
                        senstense_map2.get(map[0]).add(map[1]+" "+map[2]);

                }

            }
        }

    }

    public void getting_country_bigram(){

        database_select_global app = new database_select_global();

        String bigram_all_get=app.selectAll_country_bigram()
                .toLowerCase();

        String[] bigram_all_array=bigram_all_get.split("\n");
        String bigram_all;


        for (int i=0;i<bigram_all_array.length;i++) {
            List<String> a = new ArrayList<>();
            //unique.add(sc.next().replaceAll(",","").replaceAll("'S'","").replaceAll("'E'",""));
            bigram_all = bigram_all_array[i]
                    .toLowerCase();
            String[] map=bigram_all.split(" ");

            if (senstense_map2.get(map[0])==null)
            {
                a.add(map[1]+" "+map[2]);
                senstense_map2.put(map[0],a);
            }
            else {
                if (senstense_map2.get(map[0]).contains(map[1]+" "+map[2]));
                else {
                    Boolean add=true;
                    for (int j=0;j<senstense_map2.get(map[0]).size();j++){
                        String[] item_split=senstense_map2.get(map[0]).get(j).split(" ");
                        if(item_split[0].equals(map[1])){
                            add=false;
                            if (Double.parseDouble(item_split[1])<Double.parseDouble(map[2])) {
                                senstense_map2.get(map[0]).set(j, map[1] + " " + map[2]);
                                break;
                            }
                        }


                    }
                    if(add)
                        senstense_map2.get(map[0]).add(map[1]+" "+map[2]);

                }

            }
        }

    }


    public void run()
    {
        try
    {


            bigram.useDelimiter("\\s");

            while (bigram.hasNext()) {
                List<String> a = new ArrayList<>();
                //unique.add(sc.next().replaceAll(",","").replaceAll("'S'","").replaceAll("'E'",""));
                String build = bigram.nextLine()
                        .toLowerCase();
                String[] map=build.split(" ");

                if (senstense_map.get(map[0])==null)
                {
                    a.add(map[1]+" "+map[2]);

                    senstense_map.put(map[0],a);
                }
                else {
                    if (senstense_map.get(map[0]).contains(map[1]+" "+map[2]));
                    else {
                        Boolean add=true;
                        for (int i=0;i<senstense_map.get(map[0]).size();i++){
                             String[] item_split=senstense_map.get(map[0]).get(i).split(" ");
                             if(item_split[0].equals(map[1])){
                                 add=false;
                                 if (Double.parseDouble(item_split[1])<Double.parseDouble(map[2])) {
                                     senstense_map.get(map[0]).set(i, map[1] + " " + map[2]);

                                     break;
                                 }
                             }


                        }
                        if(add)
                            senstense_map.get(map[0]).add(map[1]+" "+map[2]);
                    }


                }


            }
//            for (String cd:senstense_map.keySet()){
//                System.out.println(cd+" "+senstense_map.get(cd)+"   bigram");
//
//            }
        throw new InterruptedException("I am Exception Alpha!");
        }
        // exception generated if sleeping thread is
        // interrupted
        catch ( InterruptedException ie )
        {
            System.out.println(
                    ie.toString() );
        }
    }
    public HashMap<String, List<String>> getHashMap_bigram(){
        return senstense_map;
    }

    public static void main(String[] args) throws Exception{

        create_senstense_bigram bigram=new create_senstense_bigram("bigram.txt");
//        Thread thread_bigram = new Thread(bigram);
////        thread_bigram.start();
//        thread_bigram.run();
//
//        System.out.println(bigram.getHashMap_bigram());

        bigram.getting_country_bigram();
        System.out.println(bigram.senstense_map2);

        database_Insert_Global app = new database_Insert_Global();

        for (String key:senstense_map2.keySet()) {
            app.insert_country_bigram(key,senstense_map2.get(key).toString());
        }
    }
}


