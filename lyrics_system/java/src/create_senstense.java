/**
The class is including interface , create sentence functions
 This class get the data from the bigram and trigram first
 This program will search the bigram and trigram hashmap
 and them generated a list based on the user input
 */

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.event.*;
import javafx.scene.input.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.io.*;
import java.util.*;
import javafx.scene.paint.Color;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.text.TextFlow;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.collections.FXCollections;
import java.util.stream.Stream;
import javafx.scene.control.ScrollPane;

public class create_senstense   extends Application {

    private static ArrayList<String> list = new ArrayList<String>();
    private static HashMap<String, HashMap<String, List<String>>> trigram_sentense = new HashMap<String, HashMap<String, List<String>>>();
    private static HashMap<String, List<String>> bigram_sentense = new HashMap<String, List<String>>();
    private static HashMap<String, List<String>> country_rhyme_map = new HashMap<String, List<String>>();
    private static String nextWord = new String();
    private static String currentWord = new String();
    private static LinkedHashSet<String> choosing_set = new LinkedHashSet();
    private static List<String> choosing_list = new ArrayList<>();
    private static TreeSet target_list_all_match = new TreeSet();
    private static TreeSet target_list_most_match = new TreeSet();
    private static TreeSet target_list_pre_match = new TreeSet();
    private static TreeSet end_word_set = new TreeSet();
    private static Boolean stopping_signal = false;
    private static String rhyme = "";
    private static StringBuilder sentence = new StringBuilder();
    private static Text title1 = new Text("This is the lyrics display");
    private static TextField nameField = new TextField();
    private static database_select_global app = new database_select_global();
    private static HashSet<String> end = app.selectAll_end_country();
    private static GridPane gridpane = new GridPane();
    private static TextFlow textFlowPane = new TextFlow();
    private static TableView<String> table = new TableView<>();
    private static ListView<String> listview = new ListView<String>();
    private static Text text_change = new Text();
    private static Text text_change_template = new Text();
    private static ScrollPane scrollPane = new ScrollPane();
    private static List<Integer> check_end_word = new ArrayList<>();
    private static Boolean template = true;

    //variable that need to set back to zero
    private static int counter = 0;
    private static Label hint_label = new Label("(Do You Want To Use The Template?(Y/N))");
    private static int word_number = 0;
    private static int rhyme_position = 0;
    private static int words_interval = 0;
    private static String end_word = "";
    private static ObservableList<String> items = FXCollections.observableArrayList();
    private static LinkedHashSet<String> rhyme_list = new LinkedHashSet();
    private static String[] array = new String[600];
    private static Label word_number_label = new Label("Number of words on this line:");
    private static Label rhyme_label = new Label("Rhyme:");
    //input variable,prescribed
    private static int verse_number=0;
    private static int chorus_number=0;
    private static int bridge_number=0;
    private static int[] input_size=new int[8];
    private static String modify="";
    private static Boolean pre_defined=false;
    private static  List<List<Integer>> rhyme_structure=new ArrayList<>();
    private static int adding_position=-1;
    private static Boolean rhyme_click=false;

    public void start(Stage stage) throws Exception {



        title1.setStyle("  -fx-font-size: 20pt");

        title1.setFill(Color.WHITESMOKE);
        //set the variable
        ObservableList text_list = textFlowPane.getChildren();
        text_list.addAll(title1);

        trigram_sentense = app.select_country_trigram();
        bigram_sentense = app.select_country_bigram_rhyme("country_bigram");
        country_rhyme_map = app.select_country_bigram_rhyme("country_end_rhyme_trigram");


        //layout for the program
        //Creating a Text object
        //set fit to screen size
        TabPane tabpane = new TabPane();

        table.setStyle("-fx-background-color: #33ccff");

        gridpane.setPadding(new Insets(5, 5, 5, 0));
        gridpane.setHgap(5);
        gridpane.setVgap(10);
        ColumnConstraints leftCol = new ColumnConstraints();
        leftCol.setHalignment(HPos.RIGHT);
        leftCol.setHgrow(Priority.NEVER);
        ColumnConstraints rightCol = new ColumnConstraints();
        rightCol.setHgrow(Priority.ALWAYS);
        gridpane.getColumnConstraints().addAll(leftCol, rightCol);


        listview.setStyle("-fx-background-color:  #0d0c0c");

        listview.setPrefSize(700, 940);
        listview.setItems(items);


        listview.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent click) {

                if (click.getClickCount() == 2) {
                    //Use ListView's getSelected Item
                    nameField.setText(listview.getSelectionModel().getSelectedItem());


                }
            }
        });


        //Textfield

        nameField.setMaxSize(200, 100);
        //TEXT



        //Tab
        Tab tab = new Tab("Lyrics Program");
        tabpane.getTabs().add(tab);

        //Button
        Button OKbtn = new Button("OK");
        OKbtn.setDefaultButton(true);
        //Download Button
        Button lyrics_btn = new Button("Download The Lyrics (Create Lyrics.txt)");
        // verse_btn
        Button verse_btn = new Button("Verse");
        // chorus_btn
        Button chorus_btn = new Button("Chorus");
        // bridge_btn
        Button bridge_btn = new Button("Bridge");
        // bridge_btn
        Button modify_btn = new Button("Modify");
        // repeat_btn
        Button repeat_btn = new Button("Repeat");
        // delete_btn
        Button delete_btn = new Button("Delete");

        //size of table
        table.setMinSize(1000, 800);
        // set the button position
        TilePane tile = new TilePane();
        tile.setPadding(new Insets(10, 10, 10, 10));
        tile.setPrefColumns(2);

        HBox hbox2 = new HBox(8);
        hbox2.getChildren().addAll(verse_btn,chorus_btn,bridge_btn,modify_btn,delete_btn,repeat_btn);

        //input label
        final Label input_label = new Label("Input:");
        input_label.setFont(new Font("Arial", 20));


        //word number

        word_number_label.setFont(new Font("Arial", 16));


        //hint label

        //Rhyme label
        rhyme_label.setFont(new Font("Arial", 16));


        //use this to do whatever you want to. Open Link etc.


        //search items
        nameField.textProperty().addListener(s -> {
                    if (nameField.textProperty().get().isEmpty()) {
                        listview.setItems(items);
                        return;
                    }
                    ObservableList<String> tableItems = FXCollections.observableArrayList();
                    //ObservableList<TableColumn<String, ?>> cols = table.getRowFactory();
                    for (int i = 0; i < items.size(); i++) {


                        String cellValue = items.get(i);

                        cellValue = cellValue.toLowerCase();
                        if (cellValue.contains(nameField.textProperty().get().toLowerCase())) {
                            tableItems.add(items.get(i));

                        }

                    }

                    listview.setItems(tableItems);
                }
        );


        //click the rhyme label to set the rhyme
        rhyme_label.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent click) {

                if (click.getClickCount() == 2) {
                    if (hint_label.textProperty().get().startsWith("Choice:") ||
                            hint_label.textProperty().get().startsWith("Use The Mouse To Click The Target Word")
                            ||hint_label.textProperty().get().startsWith("Please Click")) {
                        rhyme = nameField.textProperty().get().toLowerCase().replaceAll(" ", "");
                        rhyme_label.setText("Rhyme(Enter The Word On The Text Field\nAnd Double Click Here) :" + rhyme);
                        nameField.setText("");
                    }
                    rhyme_click=true;
                }
            }
        });


        //Click Function for clicking the modify button
        modify_btn.setOnAction(c ->
        {
            if(modify.startsWith("[Ver")){
                nameField.setText("");
                hint_label.setText("How Many Line For The Verse");
                return;
            }
            if(modify.startsWith("[C")){
                nameField.setText("");
                hint_label.setText("How Many Line For The Chorus");
                return;
            }
            if(modify.startsWith("[B")){
                nameField.setText("");
                hint_label.setText("How Many Line For The Bridge");
                return;
            }

        });
        //Click Function for clicking the lyrics button
        lyrics_btn.setOnAction(c ->
        {
            make_sentence();

            download_lyrics(sentence.toString().replaceAll("_", "").trim());

            sentence = new StringBuilder();
        });
        change_structure(verse_btn,"Verse");
        change_structure(chorus_btn,"Chorus");
        change_structure(bridge_btn,"Bridge");
        change_structure(delete_btn,"Are You Sure Want To Delete The Session?(Y/N)?");
        change_structure(repeat_btn,"Are You Sure Want To Repeat This Session(Y/N)?");


        //Click Function for clicking the ok button
        OKbtn.setOnAction(f ->
        {


            add_word_number("Verse");
            add_word_line("Verse");
            add_word_number("Chorus");
            add_word_line("Chorus");
            add_word_number("Bridge");
            add_word_line("Bridge");
            repeat_delete("Are You Sure Want To Repeat This Session(Y/N)?","repeat");
            repeat_delete("Are You Sure Want To Delete The Session?(Y/N)?","delete");



            if (hint_label.textProperty().get().equals("Input The Structure or Prescribed Pattern(I/ P(VVCV Structure))")) {
                if (nameField.textProperty().get().toLowerCase().equals("i")) {
                    rhyme_label.setText("Rhyme(Enter The Word On The Text Field\nAnd Double Click Here) :" + rhyme);
                    rhyme_label.setStyle("-fx-font-size: 14pt;" + "-fx-text-fill: orchid;");

                    textFlowPane = new TextFlow();
                    nameField.setText("");
                    hint_label.setText("Please Click The Buttons To Make The Structure");

                    word_number_label.setText("");

                    gridpane.add(hbox2, 1, 4);
                    return;

                }
                if (nameField.textProperty().get().toLowerCase().equals("p")) {
                    rhyme_label.setText("Rhyme(Enter The Word On The Text Field\nAnd Double Click Here) :" + rhyme);
                    rhyme_label.setStyle("-fx-font-size: 14pt;" + "-fx-text-fill: orchid;");

                    textFlowPane = new TextFlow();
                    pre_defined=true;
                    for (int i=0;i<4;i++){
                        if(i!=2) {
                            verse_number++;
                            list.add("[Verse"+verse_number+"]\n");
                            if(check_end_word.size()!=0)
                                check_end_word.add(check_end_word.get(check_end_word.size()-1)+1);
                            else
                                check_end_word.add(0);
                        }
                        else {
                            chorus_number++;
                            list.add("[Chorus"+chorus_number+"]\n");
                            check_end_word.add(check_end_word.get(check_end_word.size()-1)+1);
                        }
                        for (int j=0;j<4;j++){
                            for (int k=0;k<5;k++){
                                if(k!=4)
                                    list.add("_");
                                else {
                                    list.add("_\n");
                                    check_end_word.add(check_end_word.get(check_end_word.size()-1)+(k+1));
                                }
                            }
                        }

                    }
                    list.toArray(array);
                    System.out.println("check_end_word "+check_end_word);
                    make_sentence();


                    nameField.setText("");
                    hint_label.setText("The Rhyme Structure is AABB");

                    word_number_label.setText("");

                    gridpane.add(hbox2, 1, 4);


                    click_target_word();
                    return;
                }
            }
            //DO you want to use the template hint tag
            if (hint_label.textProperty().get().equals("(Do You Want To Use The Template?(Y/N))")) {
                if (nameField.textProperty().get().toLowerCase().equals("y")) {
                    hint_label.setText("Input The Structure or Prescribed Pattern(I/ P(VVCV Structure))");

                    textFlowPane = new TextFlow();
                    template = true;
                    nameField.setText("");

                    word_number_label.setText("");

                    return;

                }
                if (nameField.textProperty().get().toLowerCase().equals("n")) {
                    nameField.setText("");
                    hint_label.setText("(How may word in this line(less than 11 words)/Type 0 for finish)");
                    return;
                }
            }
            //set how many word in each line
            if (hint_label.textProperty().get().equals("(How may word in this line(less than 11 words)/Type 0 for finish)")) {
                if (Integer.parseInt(nameField.textProperty().get()) < 11) {

                    word_number = Integer.parseInt(nameField.textProperty().get());

                    if (word_number == 0) {

                        array = new String[600];
                        for (int i = 0; i < list.size(); i++) {
                            if (list.get(i).contains("\n"))
                                check_end_word.add(i);
                            array[i] = list.get(i);
                            sentence.append(list.get(i) + " ");
                        }
                        hint_label.setText("Do You Want To Modify The Lyrics?(Y/N)");
                        rhyme_label.setText("");
                        nameField.setText("");
                        return;
                    }
                    word_number_label.setText("Number of words on this line: " + word_number + " words left");

                    rhyme_label.setText("Rhyme: " + rhyme);
                    try {
                        Thread.sleep(500);
                        hint_label.setText("(Please Key In The First Word)");
                        nameField.setText("");
                    } catch (InterruptedException ex) {
                    }
                    return;

                }
            }


            //Clicking Enter First Word
            if (hint_label.textProperty().get().equals("(Please Key In The First Word)")) {

                currentWord = nameField.textProperty().get().toLowerCase().replace(" ", "");
                word_number_label.setText("Number of words on this line: " + (word_number - 1) + " words left");

                array[counter] = currentWord;
                list.add(currentWord);

                for (int i = 0; i < list.size(); i++) {
                    sentence.append(list.get(i) + " ");
                }
                title1.setText(sentence.toString());
                sentence = new StringBuilder();

                try {
                    Thread.sleep(500);
                    hint_label.setText("1.common word/2.specific country word/\n3.All possible word calculated previous word/4.Rhyme List)");
                    nameField.setText("");
                } catch (InterruptedException ex) {
                }




                try {

                    Thread.sleep(500);
                    hint_label.setText("");
                    nameField.setText("");
                    //set the Semi-Automatic  function

                    looping();
                    //release the option list


                } catch (InterruptedException ex) {
                }

            }


            if (hint_label.textProperty().get().equals("(Please choose the end words from the list)")) {

                end_word = nameField.textProperty().get() + "\n";
                if (end_word.equals(""))
                    return;

                array[counter + 1] = end_word;

                System.out.println("end_word " + end_word);

                if (words_interval == word_number) {
                    list.add(end_word);
                    for (int i = 0; i < list.size(); i++) {
                        sentence.append(list.get(i) + " ");
                    }

                    title1.setText(sentence.toString());
                    return;
                }

                if (counter == rhyme_position - 2)
                    rhyme = end_word;
                sentence = new StringBuilder();

                nameField.setText("");
                counter++;
                hint_label.setText("");
                items.clear();
                looping();

            }

            if (hint_label.textProperty().get().equals("1.common word/2.specific country word/\n3.All possible word calculated previous word/4.Rhyme List)")) {
                int mode = Integer.parseInt(nameField.textProperty().get());


                if (mode == 1) {

                    mode_choics(1);

                    return;
                }

                if (mode == 2) {

                    mode_choics(2);
                    return;
                }
                if (mode == 3) {


                    get_word_by_using_trigram();

                    mode_choics(3);

                    return;

                }


                if (mode == 4) {

                    if ((country_rhyme_map.get(rhyme).size() != 0)) {
                        get_word_by_using_trigram();



                        //rhyme full finction
                        for (int k=0;k<country_rhyme_map.get(rhyme).size();k++){
                            if ( choosing_list.contains(country_rhyme_map.get(rhyme).get(k))){
                                rhyme_list.add((country_rhyme_map.get(rhyme).get(k)));
                            }
                        }
                        if (rhyme_list.size()>0){

                            rhyme_list.add("-------------The Words Above Generated By Mechine Learning(Trigram or Bigram/Common Word)---" +
                                    "--------------");
                        }

                        rhyme_list.addAll(country_rhyme_map.get(rhyme));
                        rhyme_list.add("-------------The Words Above Generated By Mechine Learning(End Word)---" +
                                "--------------");
                    }


                    List<String> temp=new ArrayList<>();
                    temp=app.selectAll_rhyme().get(app.rhyme_matching(app.selectAll_rhyme(), rhyme));
                    Boolean add_temp=false;
                    for (int k=0;k<temp.size();k++){
                        if ( choosing_list.contains(temp.get(k))){
                            if(rhyme_list.add(temp.get(k))) {
                                rhyme_list.add(temp.get(k));
                                add_temp = true;
                            }
                        }
                    }
                    if(add_temp)
                        rhyme_list.add("-------------The Words Above Generated By Mechine Learning with online dictionary---" +
                                "--------------");

                    rhyme_list.addAll(temp);

                    items.addAll(rhyme_list);

                    nameField.setText("");
                    hint_label.setText("(Please choose the rhyme words from the list)");
                    //reset all the set
                    choosing_set = new LinkedHashSet();
                    choosing_list = new ArrayList<>();
                    target_list_all_match = new TreeSet();
                    target_list_most_match = new TreeSet();
                    target_list_pre_match = new TreeSet();
                    end_word_set = new TreeSet();
                    rhyme_list=new LinkedHashSet<>();
                    return;
                }


            }
            //choosing the word from  list
            if (hint_label.textProperty().get().equals("(Choose a word from the list)")) {

                choosing_word_from_list();

            }

            //choosing the word from rhyme list
            if (hint_label.textProperty().get().equals("(Please choose the rhyme words from the list)")) {
                //items.clear();



                if (!nameField.textProperty().get().equals("")) {
                    System.out.println("sfagjkeberjkbk");
                    currentWord = nameField.textProperty().get().replace(" ", "");
                    if(counter==word_number-2)
                    {
                        currentWord+="\n";
                    }


                    array[counter + 1] = currentWord;
                    list.add(currentWord);


                    System.out.println("reach the rhyme select");
                    items.clear();


                    for (int i = 0; i < list.size(); i++) {
                        sentence.append(list.get(i) + " ");
                    }
                    title1.setText(sentence.toString());
                    sentence = new StringBuilder();
                    word_number_label.setText("Number of words on this line: " + (word_number - counter - 2) + " words left");
                    counter++;
                    looping();



                }


            }
            //choose the word if cannot find any word from the previous word
            if (hint_label.textProperty().get().equals("(Can't the generate a word based on the previous word)")) {
                System.out.println("(Can't the generate a word based on the previous word)");
                choosing_word_from_list();


            }

            //function for the choosing word
            if (hint_label.textProperty().get().equals("Enter The rhyme word/Input -1 For No Rhyme need")) {

                if (!nameField.textProperty().get().equals("")) {
                    if (!nameField.textProperty().get().equals("-1")) {
                        rhyme = nameField.textProperty().get().replace(" ", "");
                    } else
                        rhyme = "";
                    rhyme_label.setText("Rhyme: " + rhyme);
                    choosing_set = new LinkedHashSet();
                    choosing_list = new ArrayList<>();
                    target_list_all_match = new TreeSet();
                    target_list_most_match = new TreeSet();
                    target_list_pre_match = new TreeSet();
                    end_word_set = new TreeSet();
                    counter = 0;
                    hint_label.setText("(How may word in this line(less than 11 words)/Type 0 for finish)");
                    word_number = 0;
                    rhyme_position = 0;
                    words_interval = 0;
                    end_word = "";
                    nameField.setText("");
                    rhyme_list = new LinkedHashSet<String> ();
                    array = new String[10];
                    word_number_label.setText("Number of words on this line:");
                    for (int i = 0; i < list.size(); i++) {
                        sentence.append(list.get(i) + " ");
                    }
                    title1.setText(sentence.toString());
                    sentence = new StringBuilder();

                }


            }

            //Modify lyrics
            if (hint_label.textProperty().get().equals("Do You Want To Modify The Lyrics?(Y/N)")) {
                if (nameField.textProperty().get().toLowerCase().equals("y")) {
                    template = false;
                    textFlowPane = new TextFlow();

                    rhyme_label.setText("Rhyme(Enter The Word On The Text Field\nAnd Double Click Here) :");
                    rhyme_label.setStyle("-fx-font-size: 14pt;" + "-fx-text-fill: orchid;");


                    hint_label.setText("Use The Mouse To Click The Target Word");
                    title1.setText("");
                    // Use ListView's getSelected Item


                    click_target_word();


                    return;
                }
                if (nameField.textProperty().get().toLowerCase().equals("n")) {

                    download_lyrics(sentence.toString().replaceAll("_", "").trim());

                    return;
                }
            }


            //change the word
            if (hint_label.textProperty().get().startsWith("Choice:")) {
                if (!nameField.textProperty().get().equals("")&&modify.equals("")) {
                    System.out.println(counter);
                    if (check_end_word.contains(counter + 1))
                        array[counter + 1] = nameField.textProperty().get() + "\n";
                    else
                        array[counter + 1] = nameField.textProperty().get();

                    text_change.setText(array[counter + 1] + " ");
                    text_change.setFill(Color.WHITESMOKE);
                    list.set(counter + 1, array[counter + 1]);
                    hint_label.setText("");
                    items.clear();
                    nameField.setText("");
                    if (template)
                        expand(text_change_template, textFlowPane);
                }
            }


        });
        //make split Pane
        SplitPane splitPane = new SplitPane();
        splitPane.getItems().addAll(gridpane, listview);

        textFlowPane.setStyle("-fx-background-color: #0d0c0c;");
        textFlowPane.setMinSize(950, 700);
        //        //scroll pane

        scrollPane.setStyle("-fx-background-color: #0d0c0c;" + "-fx-border-color: #CACACA;" +
                "-fx-border-width: 4;");
        scrollPane.setPadding(new Insets(5));
        scrollPane.setContent(textFlowPane);
        scrollPane.setPrefSize(700, 620);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        TilePane stock_search = new TilePane();
        stock_search.setPadding(new Insets(10, 10, 10, 0));
        stock_search.setPrefColumns(2);
        HBox hbox3 = new HBox(8);
        hbox3.getChildren().addAll(input_label, nameField, OKbtn);
        stock_search.getChildren().add(hbox3);

        hint_label.setStyle("-fx-text-fill:#ff9933;");
        //gridpane
        gridpane.add(scrollPane, 1, 0);
        gridpane.add(stock_search, 1, 1);
        gridpane.add(hint_label, 1, 2);
        gridpane.add(rhyme_label, 1, 3);
        gridpane.add(word_number_label, 1, 4);

        gridpane.add(tile, 1, 5);
        gridpane.add(lyrics_btn, 1, 6);
        gridpane.setStyle(" -fx-background-color: #0d0c0c;    -fx-border-width: 5;" +
                "    -fx-border-color:#ffff4d;");
        gridpane.setPrefSize(750, 940);

        tab.setContent(splitPane);
        tab.setClosable(false);
        //gridpane2
        GridPane gridpane2 = new GridPane();
        gridpane2.setPadding(new Insets(3, 10, 3, 10));
        gridpane2.getColumnConstraints().addAll(leftCol, rightCol);
        tabpane.setStyle(" -fx-background-color: #0d0c0c");


        gridpane2.add(tabpane, 1, 0);

        gridpane2.setStyle("-fx-background-color: #f3f2f2");
        Scene scene = new Scene(gridpane2, 1500, 1000);
        scene.getStylesheets().add("style.css");

        stage.setTitle("Lyrics program");
        stage.setScene(scene);
        stage.show();


    }

    private static void expand(Text text, TextFlow textFlowPane) {

        nameField.setText("");
//        String target_unmodify=textFlowPane.getChildren().get(0).getText();
        text_change.setFill(Color.WHITESMOKE);

        modify="";
        String target_unmodify = text.getText();
        String target = target_unmodify.substring(target_unmodify.indexOf("Text[text=\"") + 1, target_unmodify.indexOf(" "));

        System.out.println(target);
        if(target.startsWith("[Vers")||target.startsWith("[C")||target.startsWith("[B")) {
            items.clear();
            text.setFill(Color.RED);
            text_change = text;
            modify=target;
            return;
        }
        text_change = text;
        text.setFill(Color.RED);

        System.out.println(text);




        items.clear();
        hint_label.setText("Choice: " + target);
        choosing_set = new LinkedHashSet();
        choosing_list = new ArrayList<>();
        target_list_all_match = new TreeSet();
        target_list_most_match = new TreeSet();
        target_list_pre_match = new TreeSet();
        end_word_set = new TreeSet();


        for (int i = 0; i < textFlowPane.getChildren().size(); i++) {
            if (textFlowPane.getChildren().get(i).equals(text)) {
                if(textFlowPane.getChildren().size()>(i+1))
                    text_change_template = (Text) textFlowPane.getChildren().get(i + 1);
                counter = i - 1;
                //set rhyme structure
                if(pre_defined){
                    if (rhyme_click)
                        rhyme_click=false;
                    else{
                        rhyme_label.setText("Rhyme(Enter The Word On The Text Field\nAnd Double Click Here) :");
                        rhyme="";}
                    if(check_end_word.contains(i)){
                        for(int j=0;j<rhyme_structure.size();j++){
                            if(rhyme_structure.get(j).contains(i)){
                                if(rhyme_structure.get(j).get(0)==i){
                                    if (!list.get(rhyme_structure.get(j).get(1)).equals("_\n"))
                                        rhyme=list.get(rhyme_structure.get(j).get(1)).replaceAll("\n","");
                                }
                                else{
                                    if (!list.get(rhyme_structure.get(j).get(0)).equals("_\n"))
                                        rhyme=list.get(rhyme_structure.get(j).get(0)).replaceAll("\n","");
                                }
                            }
                        }
                    }
                    System.out.println("rhyme "+rhyme);
                }

                if (!check_end_word.contains(counter) && counter != -1) {
                    currentWord = array[counter].toLowerCase();
                    System.out.println("currentWord " + currentWord);
                }
                if (check_end_word.contains(counter + 2)) {
                    array[counter + 2] = array[counter + 2].replaceAll("\n", "").toLowerCase();
                }

                if (rhyme.equals("")) {
                    get_word_by_pre_post_trigram(counter + 2);
                    List<String> choics_list = app.select_choice(3);


                    for (int j = 0; j < choics_list.size(); j++) {
                        if (!choosing_list.contains(choics_list.get(j))) {
                            choosing_list.add(choics_list.get(j));
                        }
                    }


                    System.out.println(choosing_list.size());
                    items.addAll(choosing_list);
                    break;
                    //if there is rhyme
                } else {


                    if ((country_rhyme_map.get(rhyme) != null)) {

                        get_word_by_pre_post_trigram(counter + 2);



                        //rhyme full finction
                        for (int k=0;k<country_rhyme_map.get(rhyme).size();k++){
                            if ( choosing_list.contains(country_rhyme_map.get(rhyme).get(k))){
                                rhyme_list.add((country_rhyme_map.get(rhyme).get(k)));
                            }
                        }
                        if (rhyme_list.size()>0){

                            rhyme_list.add("-------------The Words Above Generated By Mechine Learning(Trigram or Bigram/Common Word)---" +
                                    "--------------");
                        }

                        rhyme_list.addAll(country_rhyme_map.get(rhyme));
                        rhyme_list.add("-------------The Words Above Generated By Mechine Learning(End Word)---" +
                                "--------------");
                    }


                    List<String> temp=new ArrayList<>();
                    temp=app.selectAll_rhyme().get(app.rhyme_matching(app.selectAll_rhyme(), rhyme));



                    rhyme_list.addAll(temp);
                    rhyme_list.add("-------------The Words Above Generated By Mechine Learning with online dictionary---" +
                            "--------------");
                    items.addAll(rhyme_list);
                    nameField.setText("");
                    rhyme_list=new LinkedHashSet<>();
                    break;
                }
            }
        }


    }








    //looping function
    public static void looping(){
        while (counter <word_number) {
            if (counter==word_number-1){


                //reset every thing
                items.clear();
                nameField.setText("");
                hint_label.setText("Enter The rhyme word/Input -1 For No Rhyme need");
                return;


            }


            if (stopping_signal)
                break;

            System.out.println("counter "+ counter);
            nameField.setText("");
            items.clear();
            choosing_set = new LinkedHashSet();
            choosing_list = new ArrayList<>();
            target_list_all_match = new TreeSet();
            target_list_most_match = new TreeSet();
            target_list_pre_match = new TreeSet();
            end_word_set=new TreeSet();




            if (trigram_sentense.get(currentWord) == null) {

                items.addAll(app.select_choice(3));
                System.out.println("app.select_choice(3)"+app.select_choice(3));
                hint_label.setText("(Can't the generate a word based on the previous word)");

                stopping_signal=true;
                return;
            }
            else {
                //choose the word due to the option made by the user
                items.clear();
                hint_label.setText("1.common word/2.specific country word/\n3.All possible word calculated previous word/4.Rhyme List)");
                return;
            }
        }
    }

    //choose word from teh list
    public static void choosing_word_from_list(){

        currentWord = nameField.textProperty().get().replace(" ","");

        if (currentWord.equals(""))
            return;
        if(counter==word_number-2)
        {
            currentWord+="\n";
        }

        System.out.println("here "+counter);
        array[counter + 1] = currentWord;

        System.out.println("currentWord "+currentWord);
        if(list.size()!=counter+2){
            System.out.println("counter is empty");
            list.add(currentWord);
            System.out.println(list);
        }
        else {
            list.set(counter+1, currentWord);
        }
        for(int i=0;i<list.size();i++){
            sentence.append(list.get(i)+" ");
        }
        title1.setText(sentence.toString());
        if (counter == rhyme_position - 2)
            rhyme = currentWord;
        sentence= new StringBuilder();

        nameField.setText("");
        word_number_label.setText("Number of words on this line: "+(word_number-counter-2)+" words left");

        counter++;
        if(hint_label.textProperty().get().equals("(Can't the generate a word based on the previous word)")) {

            stopping_signal = false;
        }
        hint_label.setText("");
        looping();

    }

    public static String random_end_word(HashSet<String> end_set){

        int rand = (int) (Math.random() * end_set.size());
        List<String> temp=new ArrayList<>();
        temp.addAll(end_set);
        return temp.get(rand);

    }

    public static void exact_common_items_end(String items){

        if(counter==word_number-2||check_end_word.contains(counter+1)) {

            for (int j = 0; j < end.size(); j++) {
                if (end.contains(items.substring(0, items.indexOf(" ")))) {
                    end_word_set.add(items.substring(items.indexOf(" ") + 1, items.length()) + " " + items.substring(0, items.indexOf(" ")));
                }

            }
        }




    }
    //Give the list of choics based on bigram
    public static void get_word_by_using_bigram() {
        //get the bigram
        try {
            int rand = (int) (Math.random() * trigram_sentense.get(currentWord).size());
            for (int i = 0; i < bigram_sentense.get(currentWord).size(); i++) {
                String items = bigram_sentense.get(currentWord).get(i);
                //get end word from the list
                exact_common_items_end(items);

                target_list_most_match.add(items.substring(items.indexOf(" ") + 1, items.length()) + " " + items.substring(0, items.indexOf(" ")));

            }

            Iterator it = end_word_set.descendingIterator();


            while (it.hasNext()) {

                String items = it.next().toString();

                choosing_set.add(items.substring(items.lastIndexOf(" ") + 1, items.length()));

            }
            if(counter==word_number-2||check_end_word.contains(counter+1)) {
                choosing_set.add("-----------------------The Words Above ALso Occur In The End Of the other Country Song---" +
                        "--------------");
            }
            it = target_list_most_match.descendingIterator();

//
//
            while (it.hasNext()) {
                String items = it.next().toString();

                choosing_set.add(items.substring(items.lastIndexOf(" ") + 1, items.length()));

            }

            choosing_set.add("-----------------------The Words Above Generated By Bigram---" +
                    "--------------");
            System.out.println("choosing_set " + choosing_set);
            choosing_list.addAll(choosing_set);
        }
        catch (NullPointerException e){

        }


        choosing_list.addAll(app.select_choice(3));
    }




    //Give the list of choics based on trigram
    public static void get_word_by_using_trigram(){


        System.out.println("counter " +counter);
        if(counter==0||end_word_set.contains(counter-2)){

            get_word_by_using_bigram();
            return;
        }


        if (trigram_sentense.get(array[counter - 1]) != null) {

            if (trigram_sentense.get(array[counter - 1]).get(array[counter]) != null) {

                int rand = (int) (Math.random() * trigram_sentense.get(array[counter - 1]).get(array[counter]).size());
                //set the choosing list


                for (int i = 0; i < trigram_sentense.get(array[counter - 1]).get(array[counter]).size(); i++) {
                    String items = trigram_sentense.get(array[counter - 1]).get(array[counter]).get(i);
                    exact_common_items_end(items);
                    target_list_all_match.add(items.substring(items.indexOf(" ") + 1, items.length()) + " " + items.substring(0, items.indexOf(" "))
                    );
                }



                if(!hint_label.textProperty().get().equals("(Can't the generate a word based on the previous word)")&&
                        !hint_label.textProperty().get().equals("1.common word/2.specific country word/\n3.All possible word calculated previous word/4.Rhyme List)")
                        &&!hint_label.textProperty().get().startsWith("Choice:") ) {

                    nextWord = trigram_sentense.get(array[counter - 1]).get(array[counter]).get(rand).substring(0, trigram_sentense.get(array[counter - 1]).get(array[counter]).get(rand).lastIndexOf(" ")).trim();
                    array[counter + 1] = nextWord;
                    list.add(nextWord);
                    System.out.println(nextWord);
                    currentWord = nextWord;

                }
            } else {

                get_word_by_using_bigram();
                return;
            }
        } else {

            get_word_by_using_bigram();
            return;
        }



        Iterator it = end_word_set.descendingIterator();


        while (it.hasNext()) {

            String items = it.next().toString();

            choosing_set.add(items.substring(items.lastIndexOf(" ") + 1, items.length()));

        }
        if(counter==word_number-2||check_end_word.contains(counter+1)) {
            choosing_set.add("-----------------------The Words Above ALso Occur In The End Of the other Country Song---" +
                    "--------------");
        }


        it = target_list_all_match.descendingIterator();


        while (it.hasNext()) {

            String items = it.next().toString();

            choosing_set.add(items.substring(items.lastIndexOf(" ") + 1, items.length()));

        }
        if(target_list_all_match.size()>0)
            choosing_set.add("-----------------------The Words Above Fit Both Previous Words---" +
                    "--------------");





        choosing_list.addAll(choosing_set);
    }



    //    //Give the list of choics based on both previous and next word
    public static void get_word_by_pre_post_trigram(int end_index){
        if(check_end_word.contains(counter+1)){



            get_word_by_using_trigram();
            return;
        }


        for (String items : bigram_sentense.keySet()) {

            //check the previous items
            if (bigram_sentense.get(items).toString().replaceAll("\\[", " ").contains(" " + array[end_index] + " ")) {

                for (int i = 0; i < bigram_sentense.get(items).size(); i++) {

                    if (bigram_sentense.get(items).get(i).contains(array[end_index] + " ")) {

                        String target = bigram_sentense.get(items).get(i);

                        target_list_pre_match.add(target.substring(target.indexOf(" ") + 1, target.length()) + " " + items);

                        break;
                    }


                }

                //check the post items
                if (counter !=-1&&!check_end_word.contains(counter)) {

                    if (bigram_sentense.get(array[counter]) != null) {

                        if (bigram_sentense.get(array[counter]).toString().replaceAll("\\[", " ").contains(" " + items + " ")) {
                            //check trigram

                            target_list_most_match.add(items);

                            if (trigram_sentense.get(array[counter]).get(items) != null) {

                                if (trigram_sentense.get(array[counter]).get(items).toString().replaceAll("\\[", " ").contains(" " + array[end_index] + " ")) {
                                    //target_list.add(items);


                                    for (int i = 0; i < trigram_sentense.get(array[counter]).get(items).size(); i++) {

                                        if (trigram_sentense.get(array[counter]).get(items).get(i).substring(0, trigram_sentense.get(array[counter]).get(items).get(i).indexOf(" ")).equals(array[end_index])) {
                                            String target = trigram_sentense.get(array[counter]).get(items).get(i);
                                            target_list_all_match.add(target.substring(items.indexOf(" ") + 1, target.length()) + " " + target.substring(0, target.indexOf(" ")) + " " + items);


                                            break;
                                        }
                                    }
                                }


                            }

                        }

                    }
                }

            }

        }
        //if no word for can find on above
        if(target_list_pre_match.size()==0&&counter!=-1&&!check_end_word.contains(counter)){
            //reduce counter by one to fit the

            get_word_by_using_trigram();
            return;
        }

        Iterator it = target_list_all_match.descendingIterator();


        while (it.hasNext()) {
            String items = it.next().toString();
            choosing_set.add(items.substring(items.lastIndexOf(" ") + 1, items.length()));

        }
        if(target_list_all_match.size()>0)
            choosing_set.add("-----------------------The Words Above Fit The Most With Meaning---" +
                    "--------------");

        it = target_list_most_match.descendingIterator();

        while (it.hasNext()) {
            String items = it.next().toString();
            choosing_set.add(items.substring(items.lastIndexOf(" ") + 1, items.length()));

        }
        if(target_list_most_match.size()>0)
            choosing_set.add("-----------------------The Words Above Fit The Most Without Meaning---" +
                    "--------------");


        it = target_list_pre_match.descendingIterator();

        while (it.hasNext()) {
            String items = it.next().toString();
            choosing_set.add(items.substring(items.lastIndexOf(" ") + 1, items.length()));
        }
        if(target_list_pre_match.size()>0)
            choosing_set.add("-----------------------The Words Above Fit With The Next Word Only--" +
                    "--------------");
        choosing_list.addAll(choosing_set);



    }

    public static void download_lyrics(String lyrics){
        try {
            PrintWriter writer = new PrintWriter("lyrics.txt", "UTF-8");
            writer.println(lyrics);
            writer.close();
            nameField.setText("");

        }catch (IOException e) {

        }



    }
    public static void click_target_word(){

        if(pre_defined){
            rhyme="";
            rhyme_structure=new ArrayList<>();
            List<Integer> rhyme_pair=new ArrayList<>();
            for(int j=0;j<check_end_word.size();j++){
                if(list.get(check_end_word.get(j)).contains("[")) {
                    if (j != 0) {
                        if(rhyme_pair.size()==2)
                            rhyme_structure.add(rhyme_pair);
                        rhyme_pair = new ArrayList<>();
                    }
                }
                else{
                    if(rhyme_pair.size()!=2)
                        rhyme_pair.add(check_end_word.get(j));
                    else {
                        rhyme_structure.add(rhyme_pair);
                        rhyme_pair = new ArrayList<>();
                        rhyme_pair.add(check_end_word.get(j));
                    }

                }

            }
            if(rhyme_pair.size()==2)
                rhyme_structure.add(rhyme_pair);
            System.out.println("rhyme_structure "+rhyme_structure);


        }


        nameField.setText("");
        String text1 = sentence.toString();
        String[] words = text1.split(" ");

        textFlowPane=new TextFlow();

        int temp_counter = 0;
        // textFlowPane.getChildren().addAll(title1);
        Stream.of(words)
                .map(s -> s.concat(" "))
                .map(Text::new)

                .peek(text -> text.setFill(Color.WHITESMOKE))
                .peek(text -> text.setStyle(" -fx-font-size: 20pt"))


                .peek(text -> text.setOnMousePressed(event -> expand(text, textFlowPane)))
                .forEach(textFlowPane.getChildren()::add);
        textFlowPane.setId("textFlowPane");


        textFlowPane.setStyle("-fx-background-color: #0d0c0c");
        textFlowPane.setMinSize(950, 700);
        //        //scroll pane

        scrollPane.setPadding(new Insets(5));
        scrollPane.setContent(textFlowPane);
        scrollPane.setPrefSize(700, 620);
        gridpane.add(scrollPane, 1, 0);




    }

    //adding verse,chorus,modify,bridge
    public static void input_lyrics(String structure){
        int j=0;

        //words number part
        if(!modify.equals("")){
            System.out.println("modify "+modify);
            int temp_int = list.indexOf(modify);

            //delete session part
            if (structure.equals("delete")){
                hint_label.setText("Use The Mouse To Click The Target Word");
                if (modify.contains("[V"))
                    verse_number--;
                if (modify.contains("[C"))
                    chorus_number--;
                if (modify.contains("[B"))
                    bridge_number--;


                int l=list.size();
                for(int i=temp_int;i<l;i++){
                    //secaond to prevent the first one check
                    if (list.get(i).contains("[")&&!list.get(i).equals(modify)){
                        for(int m=i;m<list.size();m++){
                            if(list.get(m).contains(modify.substring(0,3))){
                                String temp_string="";
                                int temp_integer=Integer.parseInt(list.get(m).substring(list.get(m).length()-3,list.get(m).length()-1).replaceAll("\\D",""))-1;
                                temp_string=modify.substring(0,modify.length()-3)+temp_integer+"]";
                                list.set(m,temp_string+"\n");
                            }
                        }
                        break;
                    }

                    else {
                        list.remove(i--);
                        l--;
                    }
                }
                add_end_index();

                System.out.println("list_de "+list);

                System.out.println(check_end_word);
                list.toArray(array);
                make_sentence();
                click_target_word();

                return;


            }

            if (structure.equals("repeat")){

                if (modify.contains("[V"))
                    list.add("[Verse"+verse_number+"]\n");
                if (modify.contains("[C"))
                    list.add("[Chorus"+chorus_number+"]\n");

                if (modify.contains("[B"))
                    list.add("[Bridge"+bridge_number+"]\n");

                for (int i=list.indexOf(modify)+1;i<list.size();i++){
                    if(list.get(i).contains("["))
                        break;
                    list.add(list.get(i));
                }
                add_end_index();
                modify="";
                System.out.println("list_de "+list);

                System.out.println(check_end_word);
                list.toArray(array);
                make_sentence();
                click_target_word();
                return;
            }


            //modify function
            System.out.println("temp_int "+temp_int);
            int original = input_size[6] + input_size[0];
            if(input_size[7]!=0) {


                System.out.println("nput_size[7] " + input_size[7]);
                //not check the first items
                int remove_index;
                int add_index;
                int counter = 0;
                for (int i = 0; i < original; i++) {
                    if (input_size[7] > 0) {
                        remove_index = check_end_word.get(check_end_word.indexOf(temp_int) + i + 1);
                        System.out.println("remove_index " + remove_index);

                        for (int k = 0; k < input_size[7]; k++) {
                            list.remove(remove_index - counter);
                            counter++;
                        }
                        list.set(remove_index - counter, list.get(remove_index - counter) + "\n");

                    }
                    if (input_size[7] < 0) {
                        add_index = check_end_word.get(check_end_word.indexOf(temp_int) + i + 1);
                        list.set(add_index+counter, list.get(add_index+counter).replace("\n",""));
                        for (int k = 0; k < (-1) * (input_size[7]); k++) {
                            if (k == (-1) * (input_size[7]) - 1)
                                list.add(add_index+counter+1, "_\n");
                            else
                                list.add(add_index+counter+1, "_");
                            counter++;

                        }
                    }
                }
                System.out.println("list temp "+list);
                add_end_index();
            }

            //line part
            int counter=0;
            int start_index;
            int end_index;
            if(input_size[6]!=0) {
                //reduce line
                if(input_size[6]>0){
                    start_index=check_end_word.get(check_end_word.indexOf(temp_int) + input_size[0]);
                    end_index=check_end_word.get(check_end_word.indexOf(temp_int) + original);
                    System.out.println("start_index "+start_index);
                    System.out.println("end_index "+end_index);
                    for(int i=start_index+1;i<end_index-counter+1;i++){
                        System.out.println(i);
                        list.remove(i--);
                        counter++;
                    }
                }
                //add  line
                else
                {
                    start_index=check_end_word.get(check_end_word.indexOf(temp_int) + original);
                    for(int i=0;i<(-input_size[6]);i++)
                        for(int k=0;k<input_size[1];k++) {
                            System.out.println(k);
                            if(k==0)
                                list.add(start_index+1, "_\n");
                            else
                                list.add(start_index+1, "_");


                        }
                }
                add_end_index();

            }


            System.out.println(check_end_word);
            list.toArray(array);

            make_sentence();
            click_target_word();

            return;
        }





        if(adding_position==-1) {
            list.add("[" + structure + "]\n");

            //  check_end_word.add(check_end_word.get(check_end_word.size() - 1) + 1);

            for (int k = 0; k < input_size[j]; k++) {
                for (int i = 0; i < input_size[j + 1]; i++) {
                    if (i == input_size[j + 1] - 1) {
                        list.add("_\n");

                    } else
                        list.add("_");
                }
            }
        }
        else {
            List<String> temp=new ArrayList<>();
            temp.add("[" + structure + "]\n");
            for (int i=0;i<input_size[0];i++) {
                for (int k = 0; k <input_size[1];k++){
                    if (k == input_size[1] - 1) {
                        temp.add("_\n");

                    } else
                        temp.add("_");
                }
            }
            list.addAll(adding_position,temp);
            int counter=1;
            for(int i=0;i<list.size();i++){
                if(list.get(i).contains(structure.substring(0,3))) {
                    list.set(i,"["+structure.replaceAll("\\d","")+counter+"]\n");
                    counter++;
                }
            }
            adding_position=-1;
        }
        add_end_index();
        System.out.println(check_end_word);
        list.toArray(array);

        make_sentence();

        click_target_word();


    }

    public static void change_structure(Button button,String noun){

        button.setOnAction(v ->
        {
            if (noun.equals("Are You Sure Want To Delete The Session?(Y/N)?")){
                if (!modify.equals("")) {
                    nameField.setText("");
                    hint_label.setText(noun);

                }
            }
            else if ((noun.equals("Are You Sure Want To Repeat This Session(Y/N)?"))){
                if (!modify.equals("")) {

                    if (modify.contains("Bridge"))
                        bridge_number++;
                    if (modify.contains("Verse"))
                        verse_number++;
                    if (modify.contains("Chorus"))
                        chorus_number++;
                    nameField.setText("");
                    hint_label.setText(noun);

                }
            }

            else {





                //zzzadding_position

                if(!modify.equals("")){

                    for(int i=list.indexOf(modify);i<list.size();i++){
                        if(list.get(i).contains("[")&&!list.get(i).equals(modify)){
                            adding_position=i;
                            break;
                        }

                    }
                }
                items.clear();
                modify = "";

                if (noun.equals("Bridge"))
                    bridge_number++;
                if (noun.equals("Verse"))
                    verse_number++;
                if (noun.equals("Chorus"))
                    chorus_number++;
                nameField.setText("");
                hint_label.setText("How Many Line For The " + noun);

            }
        });


    }


    public static void add_word_number(String noun) {
        if (hint_label.textProperty().get().equals("How Many Line For The " + noun)) {
            if (!nameField.textProperty().get().equals("")) {

                if (!modify.equals("")) {
                    int temp_int = list.indexOf(modify);
                    int original = 0;
                    for (int i = temp_int + 1; i < list.size(); i++) {
                        if (list.get(i).contains("["))
                            break;
                        if (list.get(i).contains("\n"))
                            original++;
                    }
                    System.out.println("original line" + original);
                    input_size[6] = original - Integer.parseInt(nameField.textProperty().get());
                }
                input_size[0] = Integer.parseInt(nameField.textProperty().get());
                hint_label.setText("How Many Words For Each Line in " + noun);
                nameField.setText("");

            }
        }
    }
    public static void add_word_line(String noun){
        //word in line
        if (hint_label.textProperty().get().equals("How Many Words For Each Line in "+noun)) {
            if (!nameField.textProperty().get().equals("")) {
                if(!modify.equals("")) {
                    int temp_int=list.indexOf(modify);
                    int original=0;
                    for(int i=temp_int+1;i<list.size();i++){
                        original++;

                        if (list.get(i).contains("\n"))
                            break;
                    }
                    System.out.println("original word"+original);
                    input_size[7] = original- Integer.parseInt(nameField.textProperty().get());
                }

                input_size[1] = Integer.parseInt(nameField.textProperty().get());
                hint_label.setText("Use The Mouse To Click The Target Word");
                nameField.setText("");
                if (modify.equals("")) {
                    if(noun.equals("Bridge"))
                        input_lyrics(noun + bridge_number);
                    if(noun.equals("Verse"))
                        input_lyrics(noun + verse_number);
                    if(noun.equals("Chorus"))
                        input_lyrics(noun + chorus_number);

                }
                else
                    input_lyrics(modify);

            }
        }

    }
    public static void mode_choics(int mode){
        List<String> choics_list = app.select_choice(mode);
        if(mode==3)
            choosing_list.add("-----------------------The Words Above Generated By Mechine Learning---" +
                    "--------------");
        for (int i = 0; i < choics_list.size(); i++) {
            if (!choosing_list.contains(choics_list.get(i))) {
                choosing_list.add(choics_list.get(i));
            }
        }


        items.addAll(choosing_list);

        nameField.setText("");
        hint_label.setText("(Choose a word from the list)");
        //reset the all the set
        choosing_set = new LinkedHashSet();
        choosing_list = new ArrayList<>();
        target_list_all_match = new TreeSet();
        target_list_most_match = new TreeSet();
        target_list_pre_match = new TreeSet();
        end_word_set = new TreeSet();
        return;
    }

    public static void add_end_index(){
        check_end_word = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).contains("\n")) {
                check_end_word.add(i);
            }

        }
    }

    public static void make_sentence(){
        sentence=new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sentence.append(list.get(i) + " ");
        }
    }

    public static void repeat_delete(String question,String mode){
        if (hint_label.textProperty().get().equals(question)) {
            if (nameField.textProperty().get().toLowerCase().equals("y")) {

                nameField.setText("");
                input_lyrics(mode);
                return;

            }
            if (nameField.textProperty().get().toLowerCase().equals("n")) {
                nameField.setText("");
                hint_label.setText("Use The Mouse To Click The Target Word");
                return;

            }
        }


    }
    public static void main(String[] args) throws Exception {


        launch(args);


    }
}