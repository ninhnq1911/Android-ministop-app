package hcmute.edu.vn.mssv18110332.adapter.address;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.auth.User;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import hcmute.edu.vn.mssv18110332.DAO.AddressDAO;
import hcmute.edu.vn.mssv18110332.R;
import hcmute.edu.vn.mssv18110332.databinding.ItemPurchaseAddressCardSelectedBinding;
import hcmute.edu.vn.mssv18110332.helper.AppUtils;
import hcmute.edu.vn.mssv18110332.model.Address;
import hcmute.edu.vn.mssv18110332.model.Useraccount;

public class PurchaseAddressAdapter  extends ArrayAdapter<Address> {

    public int default_address = 0;

    public PurchaseAddressAdapter(@NonNull Context context, int resource, @NonNull List<Address> objects) {
        super(context, resource, objects);
        Useraccount user = AppUtils.getCurrentUser();
        Log.d("XXXXXXXXXXXXXXXXXXXXXXXXX",user.getAddress() + "");
        for (Address a: objects) {
            if (AddressDAO.isDefault(a,user))
                break;
            default_address++;
        }
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_purchase_address_card_selected,parent,false);
        TextView tv_name = convertView.findViewById(R.id.txt_name_user_address_card);
        TextView tv_phone = convertView.findViewById(R.id.txt_phone_user_address_card);
        TextView tv_address_name = convertView.findViewById(R.id.txt_name_address_card_purchase);
        TextView tv_address = convertView.findViewById(R.id.txt_full_address_card_purchase);

        Useraccount user = AppUtils.getCurrentUser();
        tv_name.setText(user.getFullname());
        tv_phone.setText(user.getPhonenumber());

        Address address = getItem(position);
        tv_address_name.setText(address.getName());
        tv_address.setText(AddressDAO.getFull(address));

        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable @org.jetbrains.annotations.Nullable View convertView, @NonNull @NotNull ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_purchase_address_card_selected,parent,false);
        TextView tv_name = convertView.findViewById(R.id.txt_name_user_address_card);
        TextView tv_phone = convertView.findViewById(R.id.txt_phone_user_address_card);
        TextView tv_address_name = convertView.findViewById(R.id.txt_name_address_card_purchase);
        TextView tv_address = convertView.findViewById(R.id.txt_full_address_card_purchase);

        Useraccount user = AppUtils.getCurrentUser();
        tv_name.setText(user.getFullname());
        tv_phone.setText(user.getPhonenumber());

        Address address = getItem(position);
        tv_address_name.setText(address.getName());
        tv_address.setText(AddressDAO.getFull(address));

        return convertView;
    }
}
