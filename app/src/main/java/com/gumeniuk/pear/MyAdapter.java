package com.gumeniuk.pear;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private ArrayList<RecyclerItem> listItems;
    private ArrayList<RecyclerItem> copyListItems;
    private Context mContext;

    private String dialogEditPressed = "";


    public MyAdapter(ArrayList<RecyclerItem> listItems, Context mContext) {
        this.listItems = listItems;
        this.mContext = mContext;
        this.copyListItems = this.listItems;
    }

    public ArrayList<RecyclerItem> getListItems() {
        return listItems;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView txtTitle;
        public ImageView imageOption;


        public ViewHolder(View itemView) {
            super(itemView);
            txtTitle = (TextView) itemView.findViewById(R.id.txtTitle);
            imageOption = (ImageView) itemView.findViewById(R.id.imageOption);
        }
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final RecyclerItem itemList = listItems.get(position);
        holder.txtTitle.setText(itemList.getItemName());
        holder.imageOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupMenu popupMenu = new PopupMenu(mContext, holder.imageOption);
                popupMenu.inflate(R.menu.option_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()) {
                            case R.id.pop_menu_item_edit:

                                dialogClickEdit(position);

                                break;
                            case R.id.pop_menu_item_delete:

                                dialogClickDelete(position);

                                break;
                            default:
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });


    }


    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public void dialogClickEdit(final int position){

        final   AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(R.string.newName);

        final EditText input = new EditText(mContext);

        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
        builder.setView(input);

        builder.setPositiveButton(R.string.Ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialogEditPressed = input.getText().toString().trim();
                listItems.set(position,new RecyclerItem(dialogEditPressed));
                notifyDataSetChanged();
                Toast.makeText(mContext, R.string.Edited, Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    public void dialogClickDelete(final int position){

        final   AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(mContext.getString(R.string.YouWantToDeleteItem)+listItems.get(position).getItemName().toString()+mContext.getString(R.string.AreYouSure));

        builder.setPositiveButton(mContext.getString(R.string.Ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                listItems.remove(position);
                notifyDataSetChanged();
                Toast.makeText(mContext, R.string.Deleted, Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton(mContext.getString(R.string.Cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        listItems = new ArrayList<>();
        if (charText.length() == 0) {
            listItems.addAll(copyListItems);
        } else {
            for (RecyclerItem item : copyListItems) {
                if (item.getItemName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    listItems.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }

}
