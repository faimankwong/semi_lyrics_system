import java.io.File;
import java.lang.reflect.Array;
import java.util.*;

/**
 * extract trigram from database and convert them into  hashmap
 */
public class create_senstense_trigram implements Runnable{

    private HashMap<String, HashMap<String,List<String>>> senstense_map_trigram = new HashMap<String, HashMap<String,List<String>>>();
    private HashMap<String, HashMap<String,List<String>>> senstense_map2_trigram = new HashMap<String, HashMap<String,List<String>>>();

    public String file_trigram=new String();
    public Scanner trigram;

    public create_senstense_trigram(String txt_name) throws Exception{
        this.file_trigram=txt_name;
        File file = new File(file_trigram);
        trigram=new Scanner(file);

    }



    public void getting_country_trigram() {

        database_select_global app = new database_select_global();

        String trigram_all_get = app.selectAll_country_trigram()
                .toLowerCase();

        String[] trigram_all_array = trigram_all_get.split("\n");
        String trigram_all;


        for (int i = 0; i < trigram_all_array.length; i++) {

            List<String> small_array = new ArrayList<>();
            List<String> big_array = new ArrayList<>();
            HashMap<String, List<String>> inner_Map = new HashMap<>();

            //unique.add(sc.next().replaceAll(",","").replaceAll("'S'","").replaceAll("'E'",""));
            trigram_all = trigram_all_array[i]
                    .toLowerCase();

            String[] map = trigram_all.split(" ");

            if (senstense_map2_trigram.get(map[0]) == null) {
                small_array.add(map[2] + " " + map[3]);
                inner_Map.put(map[1], small_array);
                senstense_map2_trigram.put(map[0], inner_Map);

            } else {
                if (!senstense_map2_trigram.get(map[0]).containsKey(map[1])) {
                    small_array.add(map[2] + " " + map[3]);
                    inner_Map = (HashMap<String, List<String>>) senstense_map2_trigram.get(map[0]).clone();
                    inner_Map.put(map[1], small_array);
                    senstense_map2_trigram.put(map[0], inner_Map);
                } else if (!senstense_map2_trigram.get(map[0]).get(map[1]).contains(map[2] + " " + map[3])) {
                    Boolean add = true;

                    for (int j = 0;j < senstense_map2_trigram.get(map[0]).get(map[1]).size(); j++) {
                        String[] item_split = senstense_map2_trigram.get(map[0]).get(map[1]).get(j).split(" ");
                        if (item_split[0].equals(map[2])) {
                            add = false;
                            System.out.println((senstense_map2_trigram.get(map[0])));
                            if (Double.parseDouble(item_split[1].replaceAll("\\p{Lower}","")) < Double.parseDouble(map[3].replaceAll("\\p{Lower}",""))) {
                                senstense_map2_trigram.get(map[0]).get(map[1]).set(j, map[2] + " " + map[3]);

                                break;
                            }
                        }


                    }
                    if (add)
                        senstense_map2_trigram.get(map[0]).get(map[1]).add(map[2] + " " + map[3]);
                }

            }


        }
    }
    public void run() {
        try {
            trigram.useDelimiter("\\s");

            while (trigram.hasNext()) {
                //List<List<String>> big_array = new ArrayList<>();
                List<String> small_array = new ArrayList<>();
                List<String> big_array = new ArrayList<>();
                HashMap<String,List<String>> inner_Map = new HashMap<>();

                //unique.add(sc.next().replaceAll(",","").replaceAll("'S'","").replaceAll("'E'",""));
                String build = trigram.nextLine().toLowerCase();
                String[] map = build.split(" ");


                if (senstense_map_trigram.get(map[0]) == null) {
                    small_array.add(map[2] + " " + map[3]);
                    inner_Map.put(map[1], small_array);
                    senstense_map_trigram.put(map[0], inner_Map);

                } else {
                    if (!senstense_map_trigram.get(map[0]).containsKey(map[1])) {
                        small_array.add(map[2] + " " + map[3]);
                        inner_Map = (HashMap<String, List<String>>) senstense_map_trigram.get(map[0]).clone();
                        inner_Map.put(map[1], small_array);
                        senstense_map_trigram.put(map[0], inner_Map);
                    } else if (!senstense_map_trigram.get(map[0]).get(map[1]).contains(map[2] + " " + map[3])) {
                        Boolean add = true;

                        for (int j = 0;j < senstense_map_trigram.get(map[0]).get(map[1]).size(); j++) {
                            String[] item_split = senstense_map_trigram.get(map[0]).get(map[1]).get(j).split(" ");
                            if (item_split[0].equals(map[2])) {
                                add = false;
                                if (Double.parseDouble(item_split[1]) < Double.parseDouble(map[3])) {
                                    senstense_map_trigram.get(map[0]).get(map[1]).set(j, map[2] + " " + map[3]);

                                    break;
                                }
                            }


                        }
                        if(add)
                            senstense_map_trigram.get(map[0]).get(map[1]).add(map[2]+" "+map[3]);
                    }

                }


            }


//            for (String cd : senstense_map_trigram.keySet()) {
//                System.out.println(cd + " " + senstense_map_trigram.get(cd)+"   trigram");
//
//            }
            throw new InterruptedException("I am Exception Alpha!");
        } catch (InterruptedException ie) {
            System.out.println(
                    ie.toString() );
        }
    }

    public HashMap<String, HashMap<String,List<String>>>getHashMap_trigram(){
        return senstense_map_trigram;
    }


    public static void main(String[] args) throws Exception {
        create_senstense_trigram trigram = new create_senstense_trigram("tigram.txt");
        Thread thread_trigram = new Thread(trigram);
        //thread_trigram.start();
        //trigram.getting_all_trigram();
        trigram.getting_country_trigram();
       // trigram.run();


        //System.out.println(trigram.senstense_map_trigram);


        database_Insert_Global app = new database_Insert_Global();

//         insert data to the
        for(String outkey:trigram.senstense_map2_trigram.keySet()) {
            for(String inner:trigram.senstense_map2_trigram.get(outkey).keySet()){
                app.insert_country_trigram(outkey,inner,trigram.senstense_map2_trigram.get(outkey).get(inner).toString());


            }

        }
    }
}
