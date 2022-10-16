package com.example.tabletop2000;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.util.Xml;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.viewpager2.widget.ViewPager2;
import org.jetbrains.annotations.NotNull;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import java.io.*;
import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DmViewPager#newInstance} factory method to
 * create an instance of this fragment.
 *
 * DmViewPager passes information to the fragments directly at start up before they are created: createFragments()
 * While the fragments pass information to each other via bundles and args during runtime
 *
 */
public class DmViewPager extends Fragment {

    Thread loadFilesThread;

    /**
     * There are 4 required files for upload:
     * dmDefaultInformationFile - always the same file, includes all the saved URL's of the other files
     * dmDefaultWorldsFile - always the same file, includes all the worlds information
     * dmNotesFile - can be any notes file in the directory, user can create and write to separate text files that are loadable
     * dmCreaturesFile - can be any creatures file in the directory, user can create and write separate xml files that are loadable
     */
    // ARGS: Default URLS for the default files that must always exist
    final static String DEFAULT_DM_INFORMATION_URL = "dmDefaultInformation.xml";
    final static String DEFAULT_DM_WORLDS_URL = "worldDefault.xml";
    final static String DEFAULT_DM_NOTES_URL = "notesDefault.txt";
    final static String DEFAULT_DM_CREATURES_URL = "creaturesDefault.xml";
    // ARGS: sent to fragments from this
    final static String ARG_SAVED_NOTES_URL = "savedNotesURL";

    // Saved URLS within the dmDefaultInformation.xml file
    String savedNotesURL;
    String savedWorldURL;




    String dmNotesURL; // informed by dmDefaultInformation.xml
    String dmCreaturesURL; // informed by dmDefaultInformation.xml
    // Values for Fragments
    String worldNameSelected = ""; // world currently selected by user
    ArrayList<String[]> worldsInformation; // all information of worlds
    String notesInformation = "none";
    ArrayList<String[]> creaturesInformation;


    File externalDirectory;

    // Values to be uploaded
    String notesURL = "defaultNotes"; // saved to file
    String worldURL; // saved to file
    String creaturesURL = ""; // saved to file

    ArrayList<String> worldNames; // this is retrieved from scanning xml file (default URL never change!)
    ArrayList<String> notesNames; // this is retrieved from scanning folder (user changes URL)
    ArrayList<String> creaturesNames; // this is retrieved from scanning folder (user changes URL)

    public static final String ARG_OBJECT = "object"; // i think i need this

    ViewPager2 viewPager;
    ViewPagerAdapter viewPagerAdapter;
    Fragment fragmentSixFake;
    Fragment fragmentOne;
    Fragment fragmentTwo;
    Fragment fragmentThree;
    Fragment fragmentFour;
    Fragment fragmentFive;
    Fragment fragmentSix;
    Fragment fragmentOneFake;
    static ArrayList<Fragment> fragments;

    /**
     * Information that needs to be uploaded.
     * Initially all fragments will be updated with the saved information.
     * Any changes made as to the selection of world, notes, and creatures
     * will be done in the Files fragment, and the Files Fragment will update
     * all other fragments.
     */

    XmlPullParser xmlPullParser;
    XmlPullParserFactory xmlPullParserFactory;

    public DmViewPager() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DmViewPager.
     */
    // TODO: Rename and change types and number of parameters
    public static DmViewPager newInstance(String param1, String param2) {
        DmViewPager fragment = new DmViewPager();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
        // Main Thread start
        setupDefaultFiles();
        initializeFields();
        retrieveDataFromDmDefaultInformation();
        createFragments();
        //sendArgumentsToNotesFragment();
        viewPager = new ViewPager2(Objects.requireNonNull(getContext()));
        viewPagerAdapter = new ViewPagerAdapter(this, viewPager, fragments);

//        // Main Thread end
//        loadFilesThread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                // other thread start
//                //retrieveDataFromWorldsFile();
//                //retrieveDataFromNotesFile();
//                //retrieveDataFromCreaturesFile();
//                // send arguments
//                //sendArgumentsToFilesFragment();
//
//                //sendArgumentsToWorldClockFragment();
//            }
//        });
//        loadFilesThread.start();






        // other thread end

        /**
         * Loading Files at start up
         */
        // loads the default values saved
        //loadDefaultValuesFile();
        // loads xml with world data, fileName remains the same
        //loadXMLWithArraysFile("worldsInformation.xml", worldsInformation);
        // loads a notes file, reference already loaded
        //loadNotesFile(notesURL);
        // loads a xml with arrays different of creatures
        //loadXMLWithArraysFile(creaturesURL, creaturesInformation);
        /**
         * Misc
         */

        //gatherNames(worldsInformation, worldNames);
        /**
         * Send Default information to fragments at start up
         */
        //sendArgumentsToFilesFragment();

//        sendArgumentsToWorldClockFragment();

    }

    private ViewPagerAdapter getViewPagerAdapter(){
        return viewPagerAdapter;
    }



    // do later
    private void retrieveDataFromCreaturesFile() {
//        creaturesURL;
//        creaturesInformation;

        // This must reflect the format of which the files are saved as in the Creatures Fragment
//        try {
//            XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory.newInstance();
//            xmlPullParser = xmlPullParserFactory.newPullParser();
//            InputStream inputStream = getContext().openFileInput(creaturesURL);
//            xmlPullParser.setInput(new InputStreamReader(inputStream));
//        } catch (XmlPullParserException | FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        int eventType = 0;
//        try {
//            eventType = xmlPullParser.getEventType();
//        } catch (XmlPullParserException e) {
//            e.printStackTrace();
//        }
//        String[] temporaryArray = new String[3];
//        String temporaryString = "";
//        while (eventType != XmlPullParser.END_DOCUMENT) {
//            String tag = null;
//            tag = xmlPullParser.getName();
//            if(tag != null){
//                // tags: worldName, days, time
//                if(tag.contains("worldName")){
//                    // a string of commands to collect all values
//                    for (int i = 0; i < 8; i++) {
//                        if(i == 1){
//                            temporaryArray[0] = xmlPullParser.getText();
//                        } else if(i == 4){
//                            temporaryArray[1] = xmlPullParser.getText();
//                        } else if(i == 7){
//                            temporaryArray[2] = xmlPullParser.getText();
//                            worldsInformation.add(temporaryArray);
//                        }
//                        try {
//                            eventType = xmlPullParser.next();
//                        } catch (IOException | XmlPullParserException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            }
//            try {
//                eventType = xmlPullParser.next();
//            } catch (IOException | XmlPullParserException e) {
//                e.printStackTrace();
//            }
//        }




    }


    // URL is not final
    private void retrieveDataFromNotesFile() {
        notesInformation = null;
        File dmNotesFile = new File(getContext().getFilesDir(), savedNotesURL);
        BufferedReader br = null;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            br = new BufferedReader(new FileReader(dmNotesFile));
            String result;
            while((result = br.readLine()) != null){
                stringBuilder.append(result);
                stringBuilder.append(System.getProperty("line.separator"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        notesInformation = stringBuilder.toString();
    }
    // URL is final
    private void retrieveDataFromWorldsFile() {
        try {
            XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory.newInstance();
            xmlPullParser = xmlPullParserFactory.newPullParser();
            InputStream inputStream = getContext().openFileInput(DEFAULT_DM_WORLDS_URL);
            xmlPullParser.setInput(new InputStreamReader(inputStream));
        } catch (XmlPullParserException | FileNotFoundException e) {
            e.printStackTrace();
        }
        int eventType = 0;
        try {
            eventType = xmlPullParser.getEventType();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        String[] temporaryArray = new String[3];
        String temporaryString = "";
        while (eventType != XmlPullParser.END_DOCUMENT) {
            String tag = null;
            tag = xmlPullParser.getName();
            if(tag != null){
                // tags: worldName, days, time
                if(tag.contains("worldName")){
                    // a string of commands to collect all values
                    for (int i = 0; i < 8; i++) {
                        if(i == 1){
                            temporaryArray[0] = xmlPullParser.getText();
                        } else if(i == 4){
                            temporaryArray[1] = xmlPullParser.getText();
                        } else if(i == 7){
                            temporaryArray[2] = xmlPullParser.getText();
                            worldsInformation.add(temporaryArray);
                        }
                        try {
                            eventType = xmlPullParser.next();
                        } catch (IOException | XmlPullParserException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            try {
                eventType = xmlPullParser.next();
            } catch (IOException | XmlPullParserException e) {
                e.printStackTrace();
            }
        }
    }

    // URL is final
    private void retrieveDataFromDmDefaultInformation() {
        // retrieve
        try {
            XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory.newInstance();
            xmlPullParser = xmlPullParserFactory.newPullParser();
            InputStream inputStream = getContext().openFileInput(DEFAULT_DM_INFORMATION_URL);
            xmlPullParser.setInput(new InputStreamReader(inputStream));
        } catch (XmlPullParserException | FileNotFoundException e) {
            e.printStackTrace();
        }
        int eventType = 0;
        try {
            eventType = xmlPullParser.getEventType();

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        int worldNameCounter = 0;
        int notesURLCounter = 0;
        int creaturesURLCounter = 0;
        while (eventType != XmlPullParser.END_DOCUMENT) {
            String tag = null;
            tag = xmlPullParser.getName();
            if(tag != null) {
                if (tag.equals("n0:worldName")) {
                    worldNameCounter++;
                } else if (tag.equals("n1:notesURL")) {
                    notesURLCounter++;
                } else if (tag.equals("n2:creaturesURL")) {
                    creaturesURLCounter++;
                }
            }
            // retrieve next item
            if(tag == null){
                if(worldNameCounter == 1){
                    savedWorldURL = xmlPullParser.getText();
                } else if(notesURLCounter == 1) {
                    savedNotesURL = xmlPullParser.getText();
                }
//                } else if(creaturesURLCounter == 1){
//                    dmCreaturesURL = xmlPullParser.getText();
//                }
            }
            try {
                eventType = xmlPullParser.next();
            } catch (IOException | XmlPullParserException e) {
                e.printStackTrace();
            }
        }
    }

    private void initializeFields() {
        worldNames = new ArrayList<>();
        notesNames = new ArrayList<>();
        creaturesNames = new ArrayList<>();
        worldsInformation = new ArrayList<>();
        creaturesInformation = new ArrayList<>();
    }

    /**
     * checks if default files - dmDefaultInformation.xml, worldsInformation.xml, defaultNotes.text, and defaultCreatures.xml -
     * exist and if not then they are created
     */
    private void setupDefaultFiles() {
        // App Files directory
        // newDefaultInformationFile
        File newDefaultInformationFile = new File(getContext().getFilesDir(), DEFAULT_DM_INFORMATION_URL);
        if (!newDefaultInformationFile.exists()) {
            try {
                newDefaultInformationFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            writeToNewDefaultInformationFile(newDefaultInformationFile);
        }
        /**
         * Testing purposes only: if you need to delete and create new file
         */
//        else {
//            newDefaultInformationFile.delete();
//            try {
//                newDefaultInformationFile.createNewFile();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            writeToNewDefaultInformationFile(newDefaultInformationFile);
//        }
        // newDefaultWorldsInformationFile
        File newDefaultWorldsInformationFile =  new File(getContext().getFilesDir(), DEFAULT_DM_WORLDS_URL);
        if(!newDefaultWorldsInformationFile.exists()){
            try {
                newDefaultWorldsInformationFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            writeToNewDefaultWorldsFile(newDefaultWorldsInformationFile);
        }
        /**
         * Testing purposes only: if you need to delete and create new file
         */
//        else {
//            newDefaultWorldsInformationFile.delete();
//            try {
//                newDefaultWorldsInformationFile.createNewFile();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            writeToNewDefaultWorldsFile(newDefaultWorldsInformationFile);
//        }
        // newDefaultNotesInformationFile
        File newDefaultNotesInformationFile =  new File(getContext().getFilesDir(), DEFAULT_DM_NOTES_URL);
        if(!newDefaultNotesInformationFile.exists()){
            try {
                newDefaultNotesInformationFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            BufferedWriter bufferedWriter;
            try {
                bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(newDefaultNotesInformationFile)));
                bufferedWriter.write("Hello, how are you adventurer?");
                bufferedWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            /**
             * Testing purposes only: if you need to delete and create new file
             */
//            try {
//                FileWriter fileWriter = new FileWriter(newDefaultNotesInformationFile);
//                fileWriter.write("hello");
//                fileWriter.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

        }

//        else {
//            newDefaultNotesInformationFile.delete();
//            try {
//                newDefaultNotesInformationFile.createNewFile();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

//            BufferedWriter bufferedWriter;
//            try {
//                bufferedWriter = new BufferedWriter(new FileWriter(newDefaultNotesInformationFile));
//                bufferedWriter.write("Hello, how are you adventurer?");
//                bufferedWriter.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

//            try {
//                FileWriter fileWriter = new FileWriter(DEFAULT_DM_NOTES_URL);
//                fileWriter.write("hello");
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

        // newDefaultCreaturesInformationFile
        File newDefaultCreaturesInformationFile =  new File(getContext().getFilesDir(), DEFAULT_DM_CREATURES_URL);
        if(!newDefaultCreaturesInformationFile.exists()){
            try {
                newDefaultCreaturesInformationFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//        else {
//            newDefaultCreaturesInformationFile.delete();
//            try {
//                newDefaultCreaturesInformationFile.createNewFile();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            System.out.println("xml file deleted and created");
//        }
    }

    private void writeToNewDefaultWorldsFile(File newDefaultWorldsInformation) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(newDefaultWorldsInformation);
            XmlSerializer xmlSerializer = Xml.newSerializer();
            StringWriter writer = new StringWriter();
            xmlSerializer.setOutput(writer);
            xmlSerializer.startDocument("UTF-8", true);

            // world default
            xmlSerializer.startTag("world", "worldDefault");

            xmlSerializer.startTag("item", "worldName");
            xmlSerializer.text("Default");
            xmlSerializer.endTag("item", "worldName");

            xmlSerializer.startTag("item","days");
            xmlSerializer.text("1");
            xmlSerializer.endTag("item", "days");

            xmlSerializer.startTag("item","time");
            xmlSerializer.text("Morning");
            xmlSerializer.endTag("item", "time");

            xmlSerializer.endTag("world", "worldDefault");

            // world One

            xmlSerializer.startTag("world", "worldOne");

            xmlSerializer.startTag("item", "worldName");
            xmlSerializer.text("Earth");
            xmlSerializer.endTag("item", "worldName");

            xmlSerializer.startTag("item","days");
            xmlSerializer.text("1");
            xmlSerializer.endTag("item", "days");

            xmlSerializer.startTag("item","time");
            xmlSerializer.text("Morning");
            xmlSerializer.endTag("item", "time");

            xmlSerializer.endTag("world", "worldOne");

            xmlSerializer.endDocument();
            xmlSerializer.flush();
            String dataWrite = writer.toString();
            fileOutputStream.write(dataWrite.getBytes());
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void writeToNewDefaultInformationFile(File newDefaultInformationFile) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(newDefaultInformationFile);
            XmlSerializer xmlSerializer = Xml.newSerializer();
            StringWriter writer = new StringWriter();
            xmlSerializer.setOutput(writer);
            xmlSerializer.startDocument("UTF-8", true);

            xmlSerializer.startTag("item", "worldName");
            xmlSerializer.text("worldDefault.xml");
            xmlSerializer.endTag("item", "worldName");

            xmlSerializer.startTag("item","notesURL");
            xmlSerializer.text("notesDefault.txt");
            xmlSerializer.endTag("item", "notesURL");

            xmlSerializer.startTag("item","creaturesURL");
            xmlSerializer.text("defaultCreatures.xml");
            xmlSerializer.endTag("item", "creaturesURL");

            xmlSerializer.endDocument();
            xmlSerializer.flush();
            String dataWrite = writer.toString();
            fileOutputStream.write(dataWrite.getBytes());
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // loads a xml with three items
    private void loadDefaultValuesFile() {
        try {
            xmlPullParserFactory = XmlPullParserFactory.newInstance();
            xmlPullParser = xmlPullParserFactory.newPullParser();
            InputStream inputStream = Objects.requireNonNull(getContext()).getAssets().open("values/dmDefaultInformation.xml");
            xmlPullParser.setInput(new InputStreamReader(inputStream));
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
        int eventType = 0;
        try {
            eventType = xmlPullParser.getEventType();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        int itemCounter = 3;
        boolean retrieveData = false;
        int itemCount = 0;
        // iterate through each element in the xml file
        while (eventType != XmlPullParser.END_DOCUMENT) {
            String tag = null;
            tag = xmlPullParser.getName();
            if(tag != null){
                if(tag.equals("item")){
                    if(retrieveData){
                        retrieveData = false;
                    }
                    itemCount++;
                    if(itemCount == 1){
                        // retrieve next text
                        retrieveData = true;
                    } else if(itemCount == 3){
                        // retrieve next text
                        retrieveData = true;
                    } else if(itemCount == 5){
                        // retrieve next text
                        retrieveData = true;
                    }
                }
            }
            // retrieve next item
            if(tag == null && retrieveData){
                if(itemCount == 1){
                    dmNotesURL = xmlPullParser.getText();
                } else if(itemCount == 3){
                    notesURL = xmlPullParser.getText();
                } else if(itemCount == 5){
                    creaturesURL = xmlPullParser.getText();
                }
            }
            try {
                eventType = xmlPullParser.next();
            } catch (IOException | XmlPullParserException e) {
                e.printStackTrace();
            }
        }
    }

    // Loads a text file
    private void loadNotesFile(String notesURL){
        StringBuilder sb = new StringBuilder();
        BufferedReader br = null;
        InputStream is = null;
        try {
            is = getContext().getAssets().open("defaultNotes");
            br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8 ));
            String str;
            while ((str = br.readLine()) != null) {
                sb.append(str);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        notesInformation = sb.toString();
    }

    // loads a xml with arrays containing files
    private void loadXMLWithArraysFile(String fileName, ArrayList<String[]> storageFile) {
        // Round One
        // calculate number of arrays and number of array items
        try {
            xmlPullParserFactory = XmlPullParserFactory.newInstance();
            xmlPullParser = xmlPullParserFactory.newPullParser();
            InputStream inputStream = Objects.requireNonNull(getContext()).getAssets().open(fileName);
            xmlPullParser.setInput(new InputStreamReader(inputStream));
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
        int eventType1 = 0;
        try {
            eventType1 = xmlPullParser.getEventType();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        int arrayCounter = 0;
        int itemCounter = 0;
        // iterate through each element in the xml file
        while (eventType1 != XmlPullParser.END_DOCUMENT) {
            String tag = null;
            tag = xmlPullParser.getName();
            if(tag != null){
                if(tag.equals("string-array")){
                    arrayCounter++;
                } else if (tag.equals("item")){
                    itemCounter++;
                }
            }
            try {
                eventType1 = xmlPullParser.next();
            } catch (IOException | XmlPullParserException e) {
                e.printStackTrace();
            }
        }
        arrayCounter /= 2;
        itemCounter /= 2;
        itemCounter /= arrayCounter;
        // Round Two
        // create and fill array, and then add to list
        try {
            xmlPullParserFactory = XmlPullParserFactory.newInstance();
            xmlPullParser = xmlPullParserFactory.newPullParser();
            InputStream inputStream = Objects.requireNonNull(getContext()).getAssets().open(fileName);
            xmlPullParser.setInput(new InputStreamReader(inputStream));
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
        int eventType2 = 0;
        try {
            eventType2 = xmlPullParser.getEventType();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        boolean arraySwitch = false;
        boolean retrieveData = false;
        String[] temporaryArray = new String[itemCounter];
        int itemCounterTemp = 0;
        // iterate through each element in the xml file
        while (eventType2 != XmlPullParser.END_DOCUMENT) {
            String tag = null;
            tag = xmlPullParser.getName();
            if (tag != null){
                if (tag.equals("item") && !arraySwitch){
                    arraySwitch = true;
                    retrieveData = true;

                } else if (tag.equals("item")){
                    arraySwitch = false;
                    retrieveData = false;
                }
            }
            // Store information retrieved
            if (tag == null && retrieveData){
                temporaryArray[itemCounterTemp] = xmlPullParser.getText();
                itemCounterTemp++;
                if(itemCounterTemp == itemCounter){
                    storageFile.add(temporaryArray);
                    temporaryArray = new String[itemCounter];
                    itemCounterTemp = 0;
                }
            }
            try {
                eventType2 = xmlPullParser.next();
            } catch (IOException | XmlPullParserException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * The arguments given to the Files, Notes, and Creatures Fragments at start up
     * The Files Fragment will determine which world and notes are selected.
     * Then send the updated worldIndex to the WorldClock Fragment and that fragment will use that index to retrieve the information it needs.
     * Then send the updated notesIndex to the Notes Fragment...
     */
    private void sendArgumentsToFilesFragment() {
//        Bundle filesBundle = new Bundle();
//        // send saved values; which world file is saved, which notes file is saved
//        filesBundle.putInt(ARG_FRAGMENT_NOTES_ID, fragmentTwo.getId());
//        fragmentSix.setArguments(filesBundle);
//        fragmentSixFake.setArguments(filesBundle);
    }

    private void sendArgumentsToWorldClockFragment() {
        Bundle worldClockBundle = new Bundle();
//        worldClockBundle.putStringArray(ARG_WORLDS_INFORMATION_LIST, worldsInformation.get(worldIndex));
        fragmentOne.setArguments(worldClockBundle);
        fragmentOneFake.setArguments(worldClockBundle);
    }

    private void sendArgumentsToNotesFragment() {
        Bundle notesBundle = new Bundle();
        System.out.println("URL:" + savedNotesURL);
        notesBundle.putString(ARG_SAVED_NOTES_URL, savedNotesURL);
//        notesBundle.putString(ARG_NOTES_CONTENT, notesInformation);
//        notesBundle.putString(ARG_NOTES_URL, savedNotesURL);
        fragmentTwo.setArguments(notesBundle);
//        System.out.println("anotes:" + notesInformation);

    }

    private void gatherNames(ArrayList<String[]> source, ArrayList<String> storage) {
        for (String[] strings : source) {
            storage.add(strings[0]);
        }
    }

    private void createFragments() {
        fragmentOne = new WorldClock(); // updates Files            xml file
        fragmentTwo = new Notes();  // updates Files                text file?
        fragmentThree = new TurnOrder();
        fragmentFour = new Players(); // updates TurnOrder
        fragmentFive = new Creatures(); // updates TurnOrder        xml file
        fragmentSix = new Files();  // updates WorldClock and Notes
        fragmentSixFake = new Files();
        fragmentOneFake = new WorldClock();

        fragments = new ArrayList<>();
        fragments.add(fragmentSixFake);
        fragments.add(fragmentOne);
        fragments.add(fragmentTwo);
        fragments.add(fragmentThree);
        fragments.add(fragmentFour);
        fragments.add(fragmentFive);
        fragments.add(fragmentSix);
        fragments.add(fragmentOneFake);

        /**
         * Directly passing information into fragments before they are created
         */
        Notes notesFragment = (Notes) fragmentTwo;
        notesFragment.dynamicURL = notesURL;
        // same for world clock
        // same for creatures
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dm_view_pager, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        viewPager = view.findViewById(R.id.DmViewer);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setCurrentItem(1);
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrollStateChanged(int state) {
                if(viewPager.getCurrentItem() == 0){
                    viewPager.setCurrentItem(6, false);

                } else if(viewPager.getCurrentItem() == 7){
                    viewPager.setCurrentItem(1, false);
                }
                super.onPageScrollStateChanged(state);
            }
        });
    }
}