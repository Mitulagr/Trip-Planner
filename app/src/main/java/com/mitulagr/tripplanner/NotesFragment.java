package com.mitulagr.tripplanner;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NotesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotesFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NotesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NotesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NotesFragment newInstance(String param1, String param2) {
        NotesFragment fragment = new NotesFragment();
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
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private Button AddNote;
    private RecyclerView notes;
    private DBHandler db;
    private int id;
    Adapter_Notes adn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_notes, container, false);

        AddNote = (Button) rootview.findViewById(R.id.AddNote);
        notes = (RecyclerView) rootview.findViewById(R.id.Notes);

        db = new DBHandler(getContext());
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        id = sp.getInt("Current Trip", 0);

        /*
        =============================================================================
        Add Notes
        =============================================================================
         */

        AddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNote(new Note(),true);
            }
        });

        /*
        =============================================================================
        Notes Adapter
        =============================================================================
         */

        notes.setLayoutManager(new LinearLayoutManager(getActivity()));

        adn = new Adapter_Notes(getActivity());

        notes.setAdapter(adn);

        adn.setOnItemLongClickListener(new Adapter_Notes.LongClickListener() {
            @Override
            public void onItemLongClickListener(View view, int position) {
                showEdit(view,position);
            }
        });

        notes.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        return rootview;
    }

    void showNote(Note note, boolean isNew){
        Dialog curd = new Dialog(getActivity());
        curd.setContentView(R.layout.addnote);
        curd.getWindow();
        curd.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        curd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        curd.show();

        EditText NTitle = (EditText) curd.findViewById(R.id.NoteTitle);
        EditText NDesc = (EditText) curd.findViewById(R.id.NoteDesc);
        Button addNote = (Button) curd.findViewById(R.id.buttonaddnot);

        if(!isNew){
            addNote.setText("Update");
            NTitle.setText(note.title);
            NDesc.setText(note.desc);
        }

        MobileAds.initialize(getActivity(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {
            }
        });

        AdView mAdNot = curd.findViewById(R.id.adNot);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdNot.loadAd(adRequest);

        addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(NTitle.getText().toString().length()==0 && NDesc.getText().toString().length()==0){
                    NTitle.requestFocus();
                    NTitle.setError("Tite Mandatory");
                    return;
                }

                note.title = NTitle.getText().toString();
                note.desc = NDesc.getText().toString();

                if(isNew){
                    note.id = db.getNoteNewId();
                    note.fid = id;
                    db.addNote(note);
                }
                else db.updateNote(note);

                adn.localDataSet = db.getAllNotes(id);
                adn.notifyDataSetChanged();
                curd.dismiss();
            }
        });
    }

    void showEdit(View view, int pos){
        PopupMenu popup = new PopupMenu(getActivity(),view);
        popup.getMenuInflater()
                .inflate(R.menu.edit2, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                switch (menuItem.getItemId()) {

                    case R.id.e1:
                        db.deleteNote(adn.localDataSet.get(pos));
                        adn.localDataSet = db.getAllNotes(id);
                        adn.notifyDataSetChanged();
                        break;
                    case R.id.e2:
                        showNote(adn.localDataSet.get(pos),false);
                        break;
                    case R.id.e3:
                        if(pos==0) break;
                        Note n1 = adn.localDataSet.get(pos-1);
                        Note n2 = adn.localDataSet.get(pos);
                        int temp = n1.id;
                        n1.id = n2.id;
                        n2.id = temp;
                        db.updateNote(n1);
                        db.updateNote(n2);
                        adn.localDataSet = db.getAllNotes(id);
                        adn.notifyDataSetChanged();
                        break;
                    case R.id.e4:
                        if(pos== adn.getItemCount()-1) break;
                        Note n3 = adn.localDataSet.get(pos);
                        Note n4 = adn.localDataSet.get(pos+1);
                        int temp1 = n3.id;
                        n3.id = n4.id;
                        n4.id = temp1;
                        db.updateNote(n3);
                        db.updateNote(n4);
                        adn.localDataSet = db.getAllNotes(id);
                        adn.notifyDataSetChanged();
                        break;
                }

                return true;
            }
        });

        popup.show();
    }
}