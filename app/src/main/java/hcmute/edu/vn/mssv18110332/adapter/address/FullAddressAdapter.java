package hcmute.edu.vn.mssv18110332.adapter.address;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import hcmute.edu.vn.mssv18110332.DAO.AddressDAO;
import hcmute.edu.vn.mssv18110332.R;
import hcmute.edu.vn.mssv18110332.model.Address;

public class FullAddressAdapter extends ArrayAdapter<Address>{


    public FullAddressAdapter(@NonNull Context context, int resource, @NonNull List<Address> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_full_address_selected,parent,false);
        TextView tv = convertView.findViewById(R.id.txt_item_address_selected);
        TextView tv_id = convertView.findViewById(R.id.txt_item_address_id_selected);
        TextView tv_name = convertView.findViewById(R.id.txt_name_address_selected);

        Address a = this.getItem(position);
        if (a!=null)
        {
            tv_name.setText(a.getName());
            tv.setText(AddressDAO.getFull(a.getId()));
            tv_id.setText(a.getId()+"");
        }
        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable @org.jetbrains.annotations.Nullable View convertView, @NonNull @NotNull ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_full_address,parent,false);
        TextView tv = convertView.findViewById(R.id.txt_item_address);
        TextView tv_id = convertView.findViewById(R.id.txt_item_address_id);
        TextView tv_name = convertView.findViewById(R.id.txt_name_address);

        Address a = this.getItem(position);
        if (a!=null)
        {
            tv_name.setText(a.getName());
            tv.setText(AddressDAO.getFull(a.getId()));
            tv_id.setText(a.getId()+"");
        }
        return convertView;
    }
}
