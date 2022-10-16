package com.example.tabletop2000;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.tabletop2000.databinding.FragmentNotesBinding;
import com.google.android.material.textfield.TextInputEditText;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Notes#newInstance} factory method to
 * create an instance of this fragment.
 *
 * Receives .txt URL from Files Fragment
 * Loads .txt file information when onResume
 * Saves .txt file information when onPause
 *
 * To do: change up when loading notes because, although it works as intended, it's a bit choppy visually
 */
public class Notes extends Fragment {

    // Fields
    private FragmentNotesBinding binding;
    TextInputEditText notesContent; // notes shown on screen
    String notesURL;
    String dynamicURL = "none"; // altered by Files fragment
    String loadedURL = "none";
    String notesInformation;

    // Argument Keys
    private static final String ARG_NOTES_NEW_URL = "keyNotesNewURL"; // sent from Files fragment

    public Notes() {
        // Required empty public constructor
    }

    public static Notes newInstance(String param1, String param2) {
        Notes fragment = new Notes();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentNotesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        notesContent = view.findViewById(R.id.InputText);
        notesContent.setText(notesInformation);
    }

    @Override
    public void onPause() {
        saveNotesToTextFile(notesURL);
        super.onPause();
    }

    @Override
    public void onResume() {
        // get dynamic URL from Files fragment
        updateDynamicURL();
        updateTextContent();
        super.onResume();
    }

    private void updateDynamicURL() {
        if (getArguments() != null) {
            dynamicURL = getArguments().getString(ARG_NOTES_NEW_URL);
        }
    }

    private void updateTextContent() {
        if(!(dynamicURL.equals(loadedURL))){
            notesInformation = null;
            File dmNotesFile = new File(getContext().getFilesDir(), dynamicURL);
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
            notesContent.setText(notesInformation);
            loadedURL = dynamicURL;
        } else {
            System.out.println("did not need to load");
        }

    }

    private void saveNotesToTextFile(String notesURL) {
        // retrieve text and update notesInformation
        notesInformation = Objects.requireNonNull(notesContent.getText()).toString();
        // retrieve file
        File dmNotesFile = new File(Objects.requireNonNull(getContext()).getFilesDir(), dynamicURL);
        // delete the file
        dmNotesFile.delete();
        // create the file
        try {
            dmNotesFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // write to the file
        BufferedWriter bufferedWriter;
        try {
            bufferedWriter = new BufferedWriter(new FileWriter(dmNotesFile));
            bufferedWriter.write(notesInformation);
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}