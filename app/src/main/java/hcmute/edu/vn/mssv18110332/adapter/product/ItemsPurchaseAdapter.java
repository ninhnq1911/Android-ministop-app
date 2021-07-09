package hcmute.edu.vn.mssv18110332.adapter.product;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import hcmute.edu.vn.mssv18110332.DAO.CartDAO;
import hcmute.edu.vn.mssv18110332.DAO.ItemsDAO;
import hcmute.edu.vn.mssv18110332.R;
import hcmute.edu.vn.mssv18110332.databinding.ItemProductPurchaseCardBinding;
import hcmute.edu.vn.mssv18110332.helper.AppUtils;
import hcmute.edu.vn.mssv18110332.model.Cart;
import hcmute.edu.vn.mssv18110332.model.Items;

public class ItemsPurchaseAdapter extends RecyclerView.Adapter<ItemsPurchaseAdapter.ItemsPurchaseViewHolder>{

    LayoutInflater inflater;
    Context context;
    List<Cart> cartList;
    AddItemClickListener addListener;
    RemoveItemClickListener removeListener;

    public void setAddListener(AddItemClickListener addListener) {
        this.addListener = addListener;
    }

    public void setRemoveListener(RemoveItemClickListener removeListener) {
        this.removeListener = removeListener;
    }

    public interface AddItemClickListener
    {
        void OnAdd(List<Cart> cartList);
    }

    public interface RemoveItemClickListener
    {
        void OnRemove(List<Cart> cartList);
    }

    public ItemsPurchaseAdapter(LayoutInflater inflater) {
        this.inflater = inflater;

    }

    public void setData(List<Cart> cartList)
    {
        this.cartList = cartList;
        notifyDataSetChanged();
    }

    @NonNull
    @NotNull
    @Override
    public ItemsPurchaseViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        if (inflater==null) inflater = LayoutInflater.from(parent.getContext());
        if (context==null) context = parent.getContext();
        final ItemProductPurchaseCardBinding binding = ItemProductPurchaseCardBinding.inflate(inflater);
        ItemsPurchaseViewHolder viewHolder = new ItemsPurchaseViewHolder(binding);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ItemsPurchaseViewHolder holder, int position) {
        Cart c = cartList.get(position);
        Items i = ItemsDAO.get_by_id(c.getItem());
        Glide.with(context).load(i.getImage()).into(holder.binding.imgItemCartCard);
        holder.binding.txtDiscountedCartCard.setText(AppUtils.getVietNamDongFormat(i.getDiscounted()));
        holder.binding.txtNameItemCartCard.setText(i.getName());
        if (i.getPrice()!=i.getDiscounted())
            holder.binding.txtPriceItemCartCard.setText(AppUtils.getVietNamDongFormat(i.getPrice()));
        else
            holder.binding.txtPriceItemCartCard.setVisibility(View.INVISIBLE);
        holder.binding.txtQuantityCart.setText(String.valueOf(c.getSl()));
        holder.binding.btnAddPurchaseCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c.setSl(Integer.min(c.getSl()+1,i.getStock()));
                //CartDAO.update(c);
                cartList.set(position,c);
                holder.binding.txtQuantityCart.setText(c.getSl()+"");
                if (addListener!=null)
                    addListener.OnAdd(cartList);
            }
        });

        holder.binding.btnRemovePurchaseCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (c.getSl()>1)
                {
                    c.setSl(c.getSl()-1);
                    //CartDAO.update(c);
                    cartList.set(position,c);
                    holder.binding.txtQuantityCart.setText(c.getSl()+"");
                   if (removeListener!=null)
                        removeListener.OnRemove(cartList);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        if (cartList!=null) return cartList.size();
        return 0;
    }

    public class ItemsPurchaseViewHolder extends RecyclerView.ViewHolder{
        final ItemProductPurchaseCardBinding binding;
        public ItemsPurchaseViewHolder(final ItemProductPurchaseCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
