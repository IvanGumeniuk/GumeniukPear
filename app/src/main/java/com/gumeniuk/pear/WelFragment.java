package com.gumeniuk.pear;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;


public class WelFragment extends Fragment {

    private ArrayList<RecyclerItem> listItems;
    private MyApplicationClass app;
    private String dialogEditPressed = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_wel, container, false);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        FloatingActionButton floatButton = (FloatingActionButton) view.findViewById(R.id.float_btn);
        floatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPressFloatingButton();
            }
        });

        app = ((MyApplicationClass)getActivity().getApplicationContext());
        listItems = new ArrayList<>();
        listItems = app.JSONData(listItems,getActivity().getIntent().getStringExtra(getString(R.string.SPFileName)),false);

        //Set adapter
        app.setAdapter(new MyAdapter(listItems, getActivity()));
        recyclerView.setAdapter(app.getAdapter());

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Button btnQuit = (Button)getActivity().findViewById(R.id.btnQuit);
        btnQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
    }




    public void onPressFloatingButton(){

        final   AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getString(R.string.NewItem));

        final EditText input = new EditText(getContext());

        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
        builder.setView(input);

        builder.setPositiveButton(getString(R.string.Ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialogEditPressed = input.getText().toString().trim();
                listItems.add(new RecyclerItem(dialogEditPressed));
                app.getAdapter().notifyDataSetChanged();
                Toast.makeText(getContext(), getString(R.string.CreatedNewItem)+dialogEditPressed, Toast.LENGTH_SHORT).show();

                app.JSONData(listItems,getActivity().getIntent().getStringExtra(getString(R.string.SPFileName)),true);
            }
        });
        builder.setNegativeButton(getString(R.string.Cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    @Override
    public void onStop() {
        super.onStop();
        app.JSONData(app.getAdapter().getListItems(),getActivity().getIntent().getStringExtra(getString(R.string.SPFileName)),true);
    }
}
