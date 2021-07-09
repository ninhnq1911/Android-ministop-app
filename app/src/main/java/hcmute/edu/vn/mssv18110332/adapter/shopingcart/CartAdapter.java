package hcmute.edu.vn.mssv18110332.adapter.shopingcart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import hcmute.edu.vn.mssv18110332.DAO.CartDAO;
import hcmute.edu.vn.mssv18110332.DAO.ItemsDAO;
import hcmute.edu.vn.mssv18110332.R;
import hcmute.edu.vn.mssv18110332.databinding.ItemCartCardBinding;
import hcmute.edu.vn.mssv18110332.helper.AppUtils;
import hcmute.edu.vn.mssv18110332.model.Cart;
import hcmute.edu.vn.mssv18110332.model.Items;
import hcmute.edu.vn.mssv18110332.model.Useraccount;

public class CartAdapter extends  RecyclerView.Adapter<CartAdapter.CartViewHolder>{
    LayoutInflater inflater;
    List<Cart> cartList;
    Context context;
    int total_cost=0;
    int total_checked=0;

    TotalCostChangeListener listener;

    public CartAdapter(LayoutInflater inflater) {
        this.inflater = inflater;
    }

    public interface TotalCostChangeListener
    {
        void OnChange(int total);
        void OnChecked(boolean checked);
    }

    public boolean isEmpty() {return (cartList==null || cartList.isEmpty());}

    public void delete_from_cart()
    {
        int total  = 0;
        total_cost = 0;
        List<Cart> list = new ArrayList<>();
        list.clear();
        for (Cart c: cartList)
        {
            if (c.getCheck() == 1)
                CartDAO.delete(c);
            else
                list.add(c);
        }
        cartList = list;
        notifyDataSetChanged();
        if (listener!=null)
        {
            listener.OnChange(total);
            listener.OnChecked(false);
        }

    }

    public int getTotal_checked() {
        return total_checked;
    }

    public int getTotal_cost() {
        return total_cost;
    }

    public void setListener(TotalCostChangeListener listener) {
        this.listener = listener;
    }

    public void setCartList(List<Cart> cartList) {
        total_cost = 0;
        total_checked = 0;
        this.cartList = cartList;
        for (Cart c: cartList)
        {
            if (c.getCheck()==1) total_cost += c.getSl() * ItemsDAO.get_by_id(c.getItem()).getDiscounted();
            total_checked += c.getCheck();
        }
        notifyDataSetChanged();
    }

    public void setALlChecked()
    {
        total_cost = 0;
        total_checked = cartList.size();
        for (Cart c: cartList)
        {
            total_cost += c.getSl() * ItemsDAO.get_by_id(c.getItem()).getDiscounted();
            if (c.getCheck() == 0)
            {
                c.setCheck(1);
                CartDAO.update(c);
            }
        }
        if (listener!=null)
            listener.OnChange(total_cost);
    }

    public void setALlUnChecked()
    {
        total_cost = 0;
        total_checked = 0;
        for (Cart c: cartList)
        {
            if (c.getCheck() == 1)
            {
                c.setCheck(0);
                CartDAO.update(c);
            }
        }
        if (listener!=null)
            listener.OnChange(total_cost);
    }

    @NonNull
    @NotNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        if (inflater==null) inflater = LayoutInflater.from(parent.getContext());
        if (context==null) context = parent.getContext();
        final ItemCartCardBinding binding = ItemCartCardBinding.inflate(inflater);
        CartViewHolder viewHolder = new CartViewHolder(binding);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CartViewHolder holder, int position) {
        if (cartList==null) return;
        Cart cart = cartList.get(position);
        if (cart==null) return;
        Useraccount user = AppUtils.getCurrentUser();
        Items item = ItemsDAO.get_by_id(cart.getItem());
        Glide.with(context).load(item.getImage()).into(holder.binding.imgItemCartCard);
        holder.binding.txtNameItemCartCard.setText(item.getName());
        holder.binding.txtDiscountedCartCard.setText(AppUtils.getVietNamDongFormat(item.getDiscounted()));
        if (item.getPrice()!=item.getDiscounted())
            holder.binding.txtPriceItemCartCard.setText(AppUtils.getVietNamDongFormat(item.getPrice()));
        else
            holder.binding.txtPriceItemCartCard.setVisibility(View.INVISIBLE);
        holder.binding.txtQuantityCart.setText(cart.getSl()+"");
        holder.binding.checkItemCartCard.setChecked(cart.getCheck()==1);
        if (ItemsDAO.isFavorite(user,item))
            holder.binding.imgFavoriteCartCard.setImageResource(R.drawable.outline_favorite_black_24dp);
        holder.binding.imgFavoriteCartCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        holder.binding.checkItemCartCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cart.getCheck()==0)
                {
                    cart.setCheck(1);
                    total_checked++;
                    total_cost += cart.getSl() * item.getDiscounted();
                }
                else
                {
                    cart.setCheck(0);
                    total_checked--;
                    total_cost -= cart.getSl() * item.getDiscounted();
                }
                CartDAO.update(cart);
                if (listener!=null)
                {
                    listener.OnChange(total_cost);
                    listener.OnChecked(total_checked == cartList.size());
                }

            }
        });
        holder.binding.imgFavoriteCartCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ItemsDAO.isFavorite(user, item)){
                    ItemsDAO.unFavorite(user,item);
                    holder.binding.imgFavoriteCartCard.setImageResource(R.drawable.outline_favorite_border_black_24dp);
                }
                else {
                    holder.binding.imgFavoriteCartCard.setImageResource(R.drawable.outline_favorite_black_24dp);
                    ItemsDAO.setFavorite(user,item);
                }
            }
        });
        holder.binding.btnAddCartCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cart.setSl(Integer.min(cart.getSl()+1,item.getStock()));
                CartDAO.update(cart);
                if (cart.getCheck()==1)
                    total_cost += item.getDiscounted();
                if (listener!=null)
                    listener.OnChange(total_cost);
            }
        });
        holder.binding.btnRemoveCartCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cart.getSl()>1)
                {
                    cart.setSl(Integer.max(cart.getSl()-1,1));
                    if (cart.getCheck()==1)
                        total_cost -= item.getDiscounted();
                    if (cart.getSl()==0)
                        CartDAO.delete(cart);
                    else
                        CartDAO.update(cart);
                    if (listener!=null)
                        listener.OnChange(total_cost);
                }
            }
        });
    }
    @Override
    public int getItemCount() {
        if (cartList!=null)
            return cartList.size();
        return 0;
    }

    public class CartViewHolder extends RecyclerView.ViewHolder{
        final ItemCartCardBinding binding;
        public CartViewHolder(final ItemCartCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
