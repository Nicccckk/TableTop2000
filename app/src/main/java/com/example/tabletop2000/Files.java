package com.example.tabletop2000;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
import com.example.tabletop2000.databinding.FragmentFilesBinding;
import com.example.tabletop2000.databinding.FragmentTurnOrderBinding;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.ls.LSOutput;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Files#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Files extends Fragment {

    private FragmentFilesBinding binding;

    Spinner worldSpinner;
    Spinner notesSpinner;
    ArrayAdapter<String> notesSpinnerArrayAdapter;
    ArrayAdapter<String> worldSpinnerArrayAdapter;
    ArrayAdapter<String> temporaryAdapter;

    String[] worldArray;
    String[] notesArray;
    String[] creaturesArray;

    String[] notesArrayPrefixSuffixRemoved;
    String[] worldArrayPrefixSuffixRemoved;

    ArrayList<String> notesList;
    ArrayList<String> worldList;
    ArrayList<String> alertDialogList;
    ArrayList<String> temporaryList;

    AlertDialog.Builder dialogObjectCreateFile;
    AlertDialog alertBoxCreateNewFile;
    AlertDialog.Builder dialogObjectDeleteFile;
    AlertDialog alertBoxDeleteFile;
    AlertDialog.Builder dialogObjectConfirmation;
    AlertDialog alertBoxConfirmation;

    String alertFileSavePrefix;
    String alertFileSaveSuffix;
    ArrayAdapter<String> spinnerAdapterTemporary;
    Spinner spinnerTemporary;
    ListView listViewOfDeletableFiles;

    boolean deleteSwitchNotes = false;
    boolean deleteSwitchWorld = false;
    boolean createSwitchNotes = false;
    boolean createSwitchWorld = false;

    int deleteObjectIndex = 0;

    int notesObjectIndex = 0;
    int worldObjectIndex = 0;

    Notes notesFragment;
    WorldClock worldFragmentOne;
    WorldClock worldFragmentTwo;

    String notesTag;
    int notesID;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_WORLD_SPINNER_ARRAY = "keyWorldSpinnerArray";
    private static final String ARG_NOTES_SPINNER_ARRAY = "keyNotesSpinnerArray";
    private static final String ARG_CREATURES_SPINNER_ARRAY = "keyCreaturesSpinnerArray";
    final static String ARG_FRAGMENT_NOTES_ID = "keyFragmentNotesID";
    private static final String ARG_NOTES_URL = "keyNotesURL";
    private static final String ARG_NOTES_NEW_URL = "keyNotesNewURL";


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Files() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Files.
     */
    // TODO: Rename and change types and number of parameters
    public static Files newInstance(String param1, String param2) {
        Files fragment = new Files();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //notesID = getArguments().getInt(ARG_FRAGMENT_NOTES_ID);
        }
        /**
         * 3 Alert Boxes: create file, delete file, confirmation
         */
        createAlertBoxes();
        retrieveFragmentReferences();
    }

    private void retrieveFragmentReferences() {
        DmViewPager parent = (DmViewPager) getParentFragment();
        assert parent != null;
        notesFragment = (Notes) parent.fragmentTwo;
        //worldFragmentOne = (WorldClock) parent.fragmentSix;
        //worldFragmentTwo = (WorldClock) parent.fragmentSixFake;
    }

    private void createAlertBoxes() {
        // Alert Box: Create File
        dialogObjectCreateFile = new AlertDialog.Builder(getContext());
        EditText textInput = new EditText(getContext());
        textInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!(textInput.getText().toString().matches("[a-zA-Z]+"))) {
                    alertBoxCreateNewFile.setMessage("Letters Only");
                    alertBoxCreateNewFile.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                } else if(checkFileExists(textInput.getText().toString(), alertDialogList)){
                    alertBoxCreateNewFile.setMessage("File already exists");
                } else {
                    alertBoxCreateNewFile.setMessage("Create File Name");
                    alertBoxCreateNewFile.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                }
            }

            private boolean checkFileExists(String toString, ArrayList<String> arrayList) {
                for (int i = 0; i < arrayList.size(); i++) {
                    if(arrayList.get(i).equals(toString)){
                        return true;
                    }
                }
                return false;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        dialogObjectCreateFile.setMessage("Create File Name")
                .setCancelable(false)
                .setView(textInput)
                .setNegativeButton(R.string.back, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertBoxCreateNewFile.dismiss();
                        textInput.setText("");
                        // if world or notes then worldindex or notesindex
                        if(createSwitchNotes = true && createSwitchWorld == false){
                            spinnerTemporary.setSelection(notesObjectIndex);
                        } else if(createSwitchNotes == false && createSwitchWorld == true){
                            spinnerTemporary.setSelection(worldObjectIndex);
                        }
                    }
                })
                .setPositiveButton("create", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // create the file if it does not exist
                                // assemble file name
                                String name = textInput.getText().toString();
                                String fileName = alertFileSavePrefix + name + alertFileSaveSuffix;
                                File newFile = new File(getContext().getFilesDir(), fileName);
                                // create file
                                if(!newFile.exists()){
                                    try {
                                        newFile.createNewFile();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                                // add new item and reorder items
                                //alertDialogList
                                /**
                                 * Optimize by having a separate array/list for only the file names excluding Default, Create New, and Delete File
                                 */
                                int listSize = alertDialogList.size() + 1;
                                spinnerAdapterTemporary.remove("Default");
                                spinnerAdapterTemporary.remove("Create New");
                                spinnerAdapterTemporary.remove("Delete File");
                                spinnerAdapterTemporary.add(name);
                                spinnerAdapterTemporary.sort(new Comparator<String>() {
                                    @Override
                                    public int compare(String o1, String o2) {
                                        return o1.compareTo(o2);
                                    }
                                });
                                spinnerAdapterTemporary.insert("Default", 0);
                                spinnerAdapterTemporary.insert("Create New", listSize - 2);
                                spinnerAdapterTemporary.insert("Delete File", listSize - 1);

                                textInput.setText("");
                                int positionNumber = spinnerAdapterTemporary.getPosition(name);
                                spinnerTemporary.setSelection(positionNumber);
                                // whether it is notes or world
                                if(createSwitchNotes == true && createSwitchWorld == false){
                                    notesObjectIndex = positionNumber;
                                } else if(createSwitchNotes == false && createSwitchWorld == true){
                                    worldObjectIndex = positionNumber;
                                }
                            }
                        }
                );
        alertBoxCreateNewFile = dialogObjectCreateFile.create();

        // Alert Box: Delete File
        dialogObjectDeleteFile = new AlertDialog.Builder(getContext());
        listViewOfDeletableFiles = new ListView(getContext());
        listViewOfDeletableFiles.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // show confirmation dialog
                alertBoxConfirmation.show();
                deleteObjectIndex = position;
            }
        });

        dialogObjectDeleteFile.setMessage("Delete File")
                .setCancelable(false)
                .setView(listViewOfDeletableFiles)
                .setNegativeButton(R.string.done, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertBoxDeleteFile.dismiss();
                        if(deleteSwitchNotes == true && deleteSwitchWorld == false){
                            spinnerTemporary.setSelection(notesObjectIndex);
                        } else if(deleteSwitchNotes == false && deleteSwitchWorld == true){
                            spinnerTemporary.setSelection(worldObjectIndex);
                        }
                        deleteSwitchNotes = false;
                        deleteSwitchWorld = false;
                    }
                });
        alertBoxDeleteFile = dialogObjectDeleteFile.create();

        // Alert Box: Confirmation
        dialogObjectConfirmation = new AlertDialog.Builder(getContext());
        dialogObjectConfirmation.setMessage("Are You Sure?")
                .setCancelable(false)
                .setNegativeButton(R.string.back, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertBoxConfirmation.dismiss();
                    }
                })
                .setPositiveButton(R.string.done, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // if list == notes or world
                        if(deleteSwitchNotes == true && deleteSwitchWorld == false){
                            File temporaryFile = new File(getContext().getFilesDir(), "notes" + temporaryList.get(deleteObjectIndex) + ".txt");
                            if(temporaryFile.exists()){
                                temporaryFile.delete();
                                // remove from list
                                temporaryList.remove(deleteObjectIndex);
                                notesList.remove(deleteObjectIndex + 1);
                                // update all adapters
                                temporaryAdapter.notifyDataSetChanged();
                                notesSpinnerArrayAdapter.notifyDataSetChanged();
                                if(deleteObjectIndex == notesObjectIndex){
                                    notesObjectIndex = 0;
                                    spinnerTemporary.setSelection(notesObjectIndex);
                                }
                            }

                        } else if(deleteSwitchWorld == true && deleteSwitchNotes == false){
                            File temporaryFile = new File(getContext().getFilesDir(), "world" + temporaryList.get(deleteObjectIndex) + ".xml");
                            if(temporaryFile.exists()){
                                temporaryFile.delete();
                                // remove from list
                                temporaryList.remove(deleteObjectIndex);
                                worldList.remove(deleteObjectIndex + 1);
                                // update all adapters
                                temporaryAdapter.notifyDataSetChanged();
                                worldSpinnerArrayAdapter.notifyDataSetChanged();
                                if(deleteObjectIndex == worldObjectIndex){
                                    worldObjectIndex = 0;
                                    spinnerTemporary.setSelection(worldObjectIndex);
                                }
                            }
                        }
                    }
                });
        alertBoxConfirmation = dialogObjectConfirmation.create();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentFilesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        scanFiles();
        /**
         * Optimize this: use ArrayList
         */
        notesArrayPrefixSuffixRemoved = removePrefixSuffixFromNotesArray(notesArray, 5, 4);
        worldArrayPrefixSuffixRemoved = removePrefixSuffixFromNotesArray(worldArray, 5, 4);

        // removePrefixSuffixFromWorldArray(); add create file functionality
        notesArray = addCreateFileAndDeleteFileToArray(notesArrayPrefixSuffixRemoved);
        worldArray = addCreateFileAndDeleteFileToArray(worldArrayPrefixSuffixRemoved);
        createSpinners(view);

    }

    private void createSpinners(@NonNull @NotNull View view) {
        // spinners
        notesSpinner = view.findViewById(R.id.NotesSpinner);

        notesList = new ArrayList<>();
        notesList.addAll(Arrays.asList(notesArray));

        notesSpinnerArrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, notesList);
        notesSpinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        notesSpinner.setAdapter(notesSpinnerArrayAdapter);

        worldSpinner = view.findViewById(R.id.WorldSpinner);

        worldList = new ArrayList<>();
        worldList.addAll(Arrays.asList(worldArray));

        worldSpinnerArrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, worldList);
        worldSpinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        worldSpinner.setAdapter(worldSpinnerArrayAdapter);

        // spinner item selectors
        notesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == notesList.size() - 2){
                    createSwitchNotes = true;
                    createSwitchWorld = false;
                    alertFileSavePrefix = "notes";
                    alertFileSaveSuffix = ".txt";
                    alertDialogList = notesList;
                    spinnerAdapterTemporary = notesSpinnerArrayAdapter;
                    spinnerTemporary = notesSpinner;
                    alertBoxCreateNewFile.show();
                } else if(position == notesList.size() - 1){
                    // delete dialog box
                    spinnerTemporary = notesSpinner;
                    deleteSwitchNotes = true;
                    deleteSwitchWorld = false;
                    temporaryList = new ArrayList<>(notesList);
                    temporaryList.remove("Default");
                    temporaryList.remove("Create New");
                    temporaryList.remove("Delete File");
                    temporaryAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, temporaryList);
                    listViewOfDeletableFiles.setAdapter(temporaryAdapter);
                    alertBoxDeleteFile.show();
                } else {
                    // save item selected
                    notesObjectIndex = position;
                    // update dmDefaultInformation.xml
                    //updateDmDefaultInformation();
                    // update notes Fragment
                    updateNotesFragment();




                    // create and send arguments
                    // check file exists based on index
                    // if exists then send file name
                    /**
                     * check if the notes URL as already been sent
                     * if not, send URL to notes fragment
                     * Notes fragment checks if it has already loaded that URL
                     * if it hasn't then the notes file will load into the notes fragment as the user enters the notes fragment
                     */




                }
            }


            // conflict
            private void updateNotesFragment() {
                Bundle bundle = new Bundle();
                bundle.putString(ARG_NOTES_NEW_URL, "notes" + notesList.get(notesObjectIndex) + ".txt"); // updating the notes URL in notes fragment
                notesFragment.setArguments(bundle);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        worldSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == worldList.size() - 2){
                    createSwitchNotes = false;
                    createSwitchWorld = true;
                    alertFileSavePrefix = "world";
                    alertFileSaveSuffix = ".xml";
                    alertDialogList = worldList;
                    spinnerAdapterTemporary = worldSpinnerArrayAdapter;
                    spinnerTemporary = worldSpinner;
                    alertBoxCreateNewFile.show();
                } else if(position == worldList.size() - 1){
                    // delete dialog box
                    spinnerTemporary = worldSpinner;
                    deleteSwitchNotes = false;
                    deleteSwitchWorld = true;
                    temporaryList = new ArrayList<>(worldList);
                    temporaryList.remove("Default");
                    temporaryList.remove("Create New");
                    temporaryList.remove("Delete File");
                    temporaryAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, temporaryList);
                    listViewOfDeletableFiles.setAdapter(temporaryAdapter);
                    alertBoxDeleteFile.show();
                } else {
                    // save item selected
                    worldObjectIndex = position;
                    // create and send arguments
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private String[] addCreateFileAndDeleteFileToArray(String[] arrayPrefixSuffixRemoved) {
        String[] arrayResult = new String[arrayPrefixSuffixRemoved.length + 2];
        for (int i = 0; i < arrayResult.length; i++) {
            if(i < arrayPrefixSuffixRemoved.length){
                arrayResult[i] = arrayPrefixSuffixRemoved[i];
            } else if(i == arrayPrefixSuffixRemoved.length){
                arrayResult[i] = "Create New";
            } else if(i == arrayPrefixSuffixRemoved.length + 1){
                arrayResult[i] = "Delete File";
            }
        }
        return arrayResult;
    }

    private String[] removePrefixSuffixFromNotesArray(String[] arrayToBeProcessed, int beginSubString, int endSubString) {
        String[] arrayPrefixSuffixRemoved = new String[arrayToBeProcessed.length];
        for (int i = 0; i < arrayToBeProcessed.length; i++) {
            // remove prefix 'notes'
            String newWord = arrayToBeProcessed[i].substring(beginSubString, arrayToBeProcessed[i].length() - endSubString);
            arrayPrefixSuffixRemoved[i] = newWord;
        }
        return arrayPrefixSuffixRemoved;
    }

    /**
     * Optimize this: remove the use of Arrays and place straight into ArrayList
     */
    private void scanFiles() {
        // Notes file starts with 'notes' and ends with '.txt'
        String[] numberOfFiles = getContext().getFilesDir().list();
        // go through each item and place files into respective String[]s
        int worldCounter = 0;
        int notesCounter = 0;
        int creaturesCounter = 0;
        for (int i = 0; i < numberOfFiles.length; i++) {
            if(numberOfFiles[i].startsWith("world") && numberOfFiles[i].endsWith(".xml")){
                worldCounter++;
            }
            if(numberOfFiles[i].startsWith("notes") && numberOfFiles[i].endsWith(".txt")){
                notesCounter++;
            }
            if(numberOfFiles[i].startsWith("creatures") && numberOfFiles[i].endsWith(".xml")){
                creaturesCounter++;
            }
        }
        worldArray = new String[worldCounter];
        notesArray = new String[notesCounter];
        creaturesArray = new String[creaturesCounter];
        int worldArrayCounter = 0;
        int notesArrayCounter = 0;
        int creaturesArrayCounter = 0;
        for (int i = 0; i < numberOfFiles.length; i++) {
            if(numberOfFiles[i].startsWith("world") && numberOfFiles[i].endsWith(".xml")){
                worldArray[worldArrayCounter] = numberOfFiles[i];
                worldArrayCounter++;
            }
            if(numberOfFiles[i].startsWith("notes") && numberOfFiles[i].endsWith(".txt")){
                notesArray[notesArrayCounter] = numberOfFiles[i];
                notesArrayCounter++;
            }
            if(numberOfFiles[i].startsWith("creatures") && numberOfFiles[i].endsWith(".xml")){
                creaturesArray[creaturesArrayCounter] = numberOfFiles[i];
                creaturesArrayCounter++;
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}