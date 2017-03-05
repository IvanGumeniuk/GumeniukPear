package com.gumeniuk.pear;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gumeniuk.pear.Database.RecyclerItem;

import java.util.ArrayList;
import java.util.Locale;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private ArrayList<RecyclerItem> listItems;
    private ArrayList<RecyclerItem> copyListItems;
    private Context mContext;
    private MyApplicationClass app;

    public MyAdapter(ArrayList<RecyclerItem> listItems, Context mContext, MyApplicationClass app) {
        this.listItems = listItems;
        this.mContext = mContext;
        this.copyListItems = this.listItems;
        this.app = app;
    }

    public ArrayList<RecyclerItem> getListItems() {
        return listItems;
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
        holder.txtPhoneNumber.setText(itemList.getItemPhoneNumber());
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
                            case R.id.pop_menu_item_call:
                                dialogClickCall(position);
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

    public void dialogClickCall(final int position) {
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE)
                == PackageManager.PERMISSION_GRANTED) {
            String contact_number = listItems.get(position).getItemPhoneNumber();
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + contact_number));
            try {
                callIntent.setPackage("com.android.phone");
                mContext.startActivity(callIntent);
            } catch (Exception e) {
                callIntent.setPackage("com.android.server.telecom");
                mContext.startActivity(callIntent);
            }
        } else Toast.makeText(mContext, "You don`t give permission. Call is imposible", Toast.LENGTH_SHORT).show();
    }

    public void dialogClickEdit(final int position) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(R.string.edit);

        LinearLayout layout = new LinearLayout(mContext);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText inputName = new EditText(mContext);
        final EditText inputPhoneNumber = new EditText(mContext);

        inputName.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
        inputName.setHint(R.string.newName);
        inputName.setHintTextColor(ContextCompat.getColor(mContext,R.color.grey));
        inputName.setText(listItems.get(position).getItemName());
        inputPhoneNumber.setInputType(InputType.TYPE_CLASS_PHONE);
        inputPhoneNumber.setHint(R.string.new_phone_number);
        inputPhoneNumber.setHintTextColor(ContextCompat.getColor(mContext,R.color.grey));
        inputPhoneNumber.setText(listItems.get(position).getItemPhoneNumber());

        layout.addView(inputName);
        layout.addView(inputPhoneNumber);
        builder.setView(layout);

        builder.setPositiveButton(R.string.Ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                app.updateRealmData(listItems.get(position), inputName.getText().toString().trim(), inputPhoneNumber.getText().toString().trim());
                listItems.set(position, app.getRecyclerItemFromRealmData(listItems.get(position).getId()));

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

    public void dialogClickDelete(final int position) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(mContext.getString(R.string.YouWantToDeleteItem) + listItems.get(position).getItemName().toString() + mContext.getString(R.string.AreYouSure));

        builder.setPositiveButton(mContext.getString(R.string.Ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                app.removeFromRealmData(listItems.get(position).getId());
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

    public void searchFilter(String charText) {
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

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtTitle;
        public TextView txtPhoneNumber;
        public ImageView imageOption;


        public ViewHolder(View itemView) {
            super(itemView);
            txtTitle = (TextView) itemView.findViewById(R.id.txtTitle);
            txtPhoneNumber = (TextView) itemView.findViewById(R.id.txtPhoneNumber);
            imageOption = (ImageView) itemView.findViewById(R.id.imageOption);
        }
    }

}
