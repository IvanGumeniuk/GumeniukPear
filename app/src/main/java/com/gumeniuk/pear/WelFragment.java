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

import com.gumeniuk.pear.Database.RecyclerItem;

import java.util.ArrayList;
import java.util.UUID;

import io.realm.Realm;


public class WelFragment extends Fragment {

    private RecyclerView recyclerView;
    private ArrayList<RecyclerItem> listItems;
    private MyApplicationClass app;
    private FloatingActionButton floatButton;
    private String dialogEditPressed = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        app = ((MyApplicationClass)getActivity().getApplicationContext());
        app.setRealm(Realm.getDefaultInstance());

        View view = inflater.inflate(R.layout.fragment_wel, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        floatButton = (FloatingActionButton)view.findViewById(R.id.float_btn);
        floatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPressFloatingButton();
            }
        });


        listItems = new ArrayList<>();
        listItems = app.getRealmData();

        //Set adapter
        app.setAdapter(new MyAdapter(listItems, getActivity(), app));
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
                app.logOut();
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
                listItems.add(new RecyclerItem(UUID.randomUUID().toString(),dialogEditPressed, app.getUserLogin()));
                app.getAdapter().notifyDataSetChanged();
                Toast.makeText(getContext(), getString(R.string.CreatedNewItem)+dialogEditPressed, Toast.LENGTH_SHORT).show();

                app.setRealmData(app.getAdapter().getListItems());
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
        app.getRealm().close();
    }
}
