package com.gumeniuk.pear;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.gumeniuk.pear.Database.RecyclerItem;

import java.util.ArrayList;
import java.util.UUID;

import io.realm.Realm;


public class WelFragment extends Fragment {

    private ArrayList<RecyclerItem> listItems;
    private MyApplicationClass app;
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        app = ((MyApplicationClass)getActivity().getApplicationContext());
        app.setRealm(Realm.getDefaultInstance());

        view  = inflater.inflate(R.layout.fragment_wel, container, false);

        initialization();

        return view;
    }

    public void initialization(){
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


        listItems = new ArrayList<>();
        listItems = app.getRealmData();
        app.setIsContacts(true);

        //Set adapter
        app.setAdapter(new MyAdapter(listItems, getActivity(), app));
        recyclerView.setAdapter(app.getAdapter());
    }


    public void onPressFloatingButton(){

        final   AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getString(R.string.NewItem));

        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText inputName = new EditText(getContext());
        final EditText inputPhoneNumber = new EditText(getContext());

        inputName.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
        inputName.setHint(R.string.name);
        inputName.setHintTextColor(ContextCompat.getColor(getActivity(),R.color.grey));
        inputPhoneNumber.setInputType(InputType.TYPE_CLASS_PHONE);
        inputPhoneNumber.setHint(R.string.phone_number);
        inputPhoneNumber.setHintTextColor(ContextCompat.getColor(getActivity(),R.color.grey));

        layout.addView(inputName);
        layout.addView(inputPhoneNumber);
        builder.setView(layout);

        builder.setPositiveButton(getString(R.string.Ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                RecyclerItem item = new RecyclerItem(UUID.randomUUID().toString(), inputName.getText().toString().trim(), inputPhoneNumber.getText().toString().trim(), app.getUserLogin());
                    listItems.add(item);
                    app.getAdapter().notifyDataSetChanged();
                    Toast.makeText(getContext(), getString(R.string.CreatedNewItem) + inputName.getText().toString().trim() +
                            getString(R.string.with_number) + inputPhoneNumber.getText().toString().trim(), Toast.LENGTH_SHORT).show();

                    app.setRealmData(new ArrayList<RecyclerItem>());
            }
        });
        builder.setNegativeButton(getString(R.string.Cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        final AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onStop() {
        super.onStop();
        app.getRealm().close();
    }
}
