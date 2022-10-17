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
import com.example.tabletop2000.databinding.FragmentFilesBinding;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.*;

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

    TreeSet<String> worldRawTreeSet = new TreeSet<>(); // only the names of the files found, does not include Default, Create New, and Delete File
    TreeSet<String> notesRawTreeSet = new TreeSet<>(); // only the names of the files found, does not include Default, Create New, and Delete File
    TreeSet<String> alertTreeSet = new TreeSet<>();

    ArrayList<String> worldCompleteList; // includes everything that will be visually represented
    ArrayList<String> notesCompleteList; // includes everything that will be visually represented
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

    int deleteObjectIndex = 0; // updated when a deletable object is selected, based on a TreeSet

    int notesObjectIndex = 0; // updated when an object is selected in the spinner, based on ArrayList
    int worldObjectIndex = 0; // updated when an object is selected in the spinner, based on ArrayList

    Notes notesFragment;

    private static final String ARG_NOTES_NEW_URL = "keyNotesNewURL";

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public Files() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
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
//        if (getArguments() != null) {
//            //notesID = getArguments().getInt(ARG_FRAGMENT_NOTES_ID);
//        }
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
                for (String s : arrayList) {
                    if (s.equals(toString)) {
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
                                // add name to TreeSet
                                alertTreeSet.add(name);
                                // update List
                                alertDialogList = new ArrayList<>(alertTreeSet);
                                // add "Default", "Create New", and "Delete File"
                                addToList(alertDialogList);
                                // if notes then update notesList, if world then update worldList
                                int positionNumber = alertDialogList.indexOf(name);
                                if(createSwitchNotes == true && createSwitchWorld == false){
                                    notesObjectIndex = positionNumber;
                                    notesCompleteList = alertDialogList;
                                    notesSpinnerArrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, notesCompleteList);
                                    notesSpinner.setAdapter(notesSpinnerArrayAdapter);
                                    notesSpinner.setSelection(positionNumber);
                                } else if(createSwitchNotes == false && createSwitchWorld == true){
                                    worldObjectIndex = positionNumber;
                                    worldCompleteList = alertDialogList;
                                    worldSpinnerArrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, worldCompleteList);
                                    worldSpinner.setAdapter(worldSpinnerArrayAdapter);
                                    worldSpinner.setSelection(positionNumber);
                                }
                                textInput.setText("");
                                //spinnerTemporary.setSelection(positionNumber);
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
                System.out.println("delete object at: " + position);
                deleteObjectIndex = position;
                alertBoxConfirmation.show();
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
                                notesRawTreeSet.remove(temporaryList.get(deleteObjectIndex));
                                temporaryList.remove(deleteObjectIndex);
                                notesCompleteList.remove(deleteObjectIndex + 1);
                                // update all adapters
                                temporaryAdapter.notifyDataSetChanged();
                                notesSpinnerArrayAdapter.notifyDataSetChanged();
                                System.out.println("delete: " + deleteObjectIndex + "same as: " + notesObjectIndex);
                                if(deleteObjectIndex == notesObjectIndex - 1){
                                    notesObjectIndex = 0;
                                    spinnerTemporary.setSelection(worldObjectIndex);
                                }
                            }

                        } else if(deleteSwitchWorld == true && deleteSwitchNotes == false){
                            File temporaryFile = new File(getContext().getFilesDir(), "world" + temporaryList.get(deleteObjectIndex) + ".xml");
                            if(temporaryFile.exists()){
                                temporaryFile.delete();
                                // remove from list
                                worldRawTreeSet.remove(temporaryList.get(deleteObjectIndex));
                                temporaryList.remove(deleteObjectIndex);
                                worldCompleteList.remove(deleteObjectIndex + 1);
                                // update all adapters
                                temporaryAdapter.notifyDataSetChanged();
                                worldSpinnerArrayAdapter.notifyDataSetChanged();
                                System.out.println("delete: " + deleteObjectIndex + "same as: " + worldObjectIndex);
                                if(deleteObjectIndex == worldObjectIndex - 1){
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
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentFilesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        scanFiles();
        createSpinners(view);
    }

    private void createSpinners(@NonNull @NotNull View view) {
        // spinners
        notesSpinner = view.findViewById(R.id.NotesSpinner);
        notesSpinnerArrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, notesCompleteList);
        notesSpinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        notesSpinner.setAdapter(notesSpinnerArrayAdapter);

        worldSpinner = view.findViewById(R.id.WorldSpinner);
        worldSpinnerArrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, worldCompleteList);
        worldSpinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        worldSpinner.setAdapter(worldSpinnerArrayAdapter);
        worldSpinner.setSelection(worldObjectIndex);

        // spinner item selectors
        notesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // if "Create New" is selected
                if(position == notesCompleteList.size() - 2){
                    createSwitchNotes = true;
                    createSwitchWorld = false;
                    alertFileSavePrefix = "notes";
                    alertFileSaveSuffix = ".txt";
                    alertDialogList = notesCompleteList;
                    alertTreeSet = notesRawTreeSet;
                    spinnerAdapterTemporary = notesSpinnerArrayAdapter;
                    spinnerTemporary = notesSpinner;
                    alertBoxCreateNewFile.show();
                }
                // if "Delete File" is selected
                else if(position == notesCompleteList.size() - 1){
                    // delete dialog box
                    spinnerTemporary = notesSpinner;
                    deleteSwitchNotes = true;
                    deleteSwitchWorld = false;
                    temporaryList = new ArrayList<>(notesRawTreeSet);
                    temporaryAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, temporaryList);
                    listViewOfDeletableFiles.setAdapter(temporaryAdapter);
                    alertBoxDeleteFile.show();
                }
                // if file name is selected
                else {
                    // save item selected
                    notesObjectIndex = position;
                    // create and send arguments
                }
            }



            // conflict
//            private void updateNotesFragment() {
//                Bundle bundle = new Bundle();
//                bundle.putString(ARG_NOTES_NEW_URL, "notes" + notesList.get(notesObjectIndex) + ".txt"); // updating the notes URL in notes fragment
//                notesFragment.setArguments(bundle);
//            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        worldSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // if "Create New" is selected
                if(position == worldCompleteList.size() - 2){
                    createSwitchNotes = false;
                    createSwitchWorld = true;
                    alertFileSavePrefix = "world";
                    alertFileSaveSuffix = ".xml";
                    alertDialogList = worldCompleteList;
                    alertTreeSet = worldRawTreeSet;
                    spinnerAdapterTemporary = worldSpinnerArrayAdapter;
                    spinnerTemporary = worldSpinner;
                    alertBoxCreateNewFile.show();
                }
                // if "Delete File" is selected
                else if(position == worldCompleteList.size() - 1){
                    // delete dialog box
                    spinnerTemporary = worldSpinner;
                    deleteSwitchNotes = false;
                    deleteSwitchWorld = true;
                    temporaryList = new ArrayList<>(worldRawTreeSet);
                    temporaryAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, temporaryList);
                    listViewOfDeletableFiles.setAdapter(temporaryAdapter);
                    alertBoxDeleteFile.show();
                }
                // if file name is selected
                else {
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

    /**
     * Optimize this: remove the use of Arrays and place straight into ArrayList
     */
    private void scanFiles() {
        String[] numberOfFiles = getContext().getFilesDir().list();
        // go through each item and place files into respective TreeSet (which will auto sort)
        // Fill Lists
        worldRawTreeSet = new TreeSet<>();
        notesRawTreeSet = new TreeSet<>();
        for (String numberOfFile : numberOfFiles) {
            if (numberOfFile.startsWith("world") && numberOfFile.endsWith(".xml")) {
                String newString = numberOfFile.substring(5, numberOfFile.length() - 4);
                worldRawTreeSet.add(newString);
            }
            if (numberOfFile.startsWith("notes") && numberOfFile.endsWith(".txt")) {
                String newString = numberOfFile.substring(5, numberOfFile.length() - 4);
                notesRawTreeSet.add(newString);
            }
        }
        // Remove Default from List
        worldRawTreeSet.remove("Default");
        notesRawTreeSet.remove("Default");
        // Insert Raw values into the Complete ArrayList
        worldCompleteList = new ArrayList<>(worldRawTreeSet);
        notesCompleteList = new ArrayList<>(notesRawTreeSet);
        // Insert "Default" at beginning and "Create New" and "Delete File" at the end
        addToList(worldCompleteList);
        addToList(notesCompleteList);
    }

    private void addToList(ArrayList<String> list) {
        list.add(0, "Default");
        list.add("Create New");
        list.add("Delete File");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onPause() {
        // send URL to notes fragment
        Bundle notesBundle = new Bundle();
        // attach prefix and suffix to notes file name
        /**
         * For some reason when i enter the fragment the notesIndex defaults to 0 for a split second which means that is what is sent to notes fragments,
         * so either i need to only have the URL sent when exiting the fragment and not when entering
         */
        System.out.println(notesObjectIndex);
        System.out.println("object to be sent: " + notesSpinnerArrayAdapter.getItem(notesObjectIndex));
        String newURL = "notes" + notesSpinnerArrayAdapter.getItem(notesObjectIndex) + ".txt";
        System.out.println(newURL);
        notesBundle.putString(ARG_NOTES_NEW_URL, newURL);
        notesFragment.setArguments(notesBundle);
        super.onPause();
    }
}