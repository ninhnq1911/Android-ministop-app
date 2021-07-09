package hcmute.edu.vn.mssv18110332.adapter.address;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import hcmute.edu.vn.mssv18110332.R;
import hcmute.edu.vn.mssv18110332.databinding.ItemAddressCardBinding;
import hcmute.edu.vn.mssv18110332.model.Address;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.AddressViewHolder>{

    private LayoutInflater layoutInflater;
    List<Address> list;
    IEditAddressOnClickListener listener;

    public interface IEditAddressOnClickListener {
        void EditAddressOnClickListener (ImageView view, Address ad);
    }


    public AddressAdapter(LayoutInflater inflater) {
        layoutInflater = inflater;
    }

    public void setData(List<Address> lst, IEditAddressOnClickListener lis)
    {
        this.list = lst;
        this.listener = lis;
        notifyDataSetChanged();
    }

    @NonNull
    @NotNull
    @Override
    public AddressViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        final ItemAddressCardBinding binding;
        if (layoutInflater == null)
            layoutInflater = LayoutInflater.from(parent.getContext());
        binding = ItemAddressCardBinding.inflate(layoutInflater);
        return new AddressViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AddressViewHolder holder, int position) {
        Address a = list.get(position);
        if (a != null) {
            holder.binding.setAddress(a);
            holder.binding.imgEditAddressCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.EditAddressOnClickListener(holder.binding.imgEditAddressCard,holder.binding.getAddress());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (list != null ) return list.size();
        return 0;
    }

    public class AddressViewHolder extends RecyclerView.ViewHolder {
        private final ItemAddressCardBinding binding;
        public AddressViewHolder(final ItemAddressCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
