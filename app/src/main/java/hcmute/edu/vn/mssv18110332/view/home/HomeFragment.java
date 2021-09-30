package hcmute.edu.vn.mssv18110332.view.home;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.icu.util.ULocale;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.zip.Inflater;

import hcmute.edu.vn.mssv18110332.DAO.CategoryDAO;
import hcmute.edu.vn.mssv18110332.DAO.ItemsDAO;
import hcmute.edu.vn.mssv18110332.R;
import hcmute.edu.vn.mssv18110332.activity.HomeActivity;
import hcmute.edu.vn.mssv18110332.activity.ItemDetailActivity;
import hcmute.edu.vn.mssv18110332.adapter.category.CategoryAdapter;
import hcmute.edu.vn.mssv18110332.adapter.product.ItemsAdapter;
import hcmute.edu.vn.mssv18110332.adapter.viewfliper.Photo;
import hcmute.edu.vn.mssv18110332.adapter.viewfliper.PhotoAdapter;
import hcmute.edu.vn.mssv18110332.databinding.FragmentHomeBinding;
import hcmute.edu.vn.mssv18110332.helper.ProgressDialog;
import hcmute.edu.vn.mssv18110332.interface_define.IOnBackPressed;
import hcmute.edu.vn.mssv18110332.model.Category;
import hcmute.edu.vn.mssv18110332.model.Items;
import me.relex.circleindicator.CircleIndicator;
import hcmute.edu.vn.mssv18110332.R;

public class HomeFragment extends Fragment implements SearchView.OnQueryTextListener, MenuItem.OnActionExpandListener {
    ViewPager viewPager;
    CircleIndicator indicator;
    PhotoAdapter photoAdapter;
    CategoryAdapter categoryAdapter;
    List<Photo> photoList;
    private FragmentHomeBinding binding;
    Timer mTimer;
    RecyclerView recyclerView, categoryView;
    List<Items> itemsList;
    Context mContext;
    SearchView searchView;
    int backPress = 0;
    OnBackPressedCallback callback;
    long pressBackTime;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        setHasOptionsMenu(true);
        callback =
        new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (!searchView.isIconified())
                {
                    searchView.setIconified(true);
                    searchView.setQuery("", false);
                    searchView.clearFocus();
                    searchView.setIconified(true);
                    backPress = 0;
                }
                else
                {
                    if (System.currentTimeMillis() - pressBackTime < 2000)
                        getActivity().finish();
                    else
                        Toast.makeText(mContext, "Nhấn back một lần nữa để thoát!", Toast.LENGTH_SHORT).show();
                    pressBackTime = System.currentTimeMillis();
                }
            }
        };
        getActivity().getOnBackPressedDispatcher().addCallback(this,callback);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("Search");
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        super.onCreateOptionsMenu(menu, inflater);
    }

    ItemsAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Toolbar toolbar = root.findViewById(R.id.app_home_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        initial_ad_photo();
        photoAdapter =  new PhotoAdapter(getContext(),photoList);
        viewPager = binding.adPhotoViewPager;
        viewPager.setAdapter(photoAdapter);
        indicator = binding.adPhotoCircleIndicator;
        indicator.setViewPager(viewPager);
        photoAdapter.registerDataSetObserver(indicator.getDataSetObserver());
        auto_slide_image();
        recyclerView = root.findViewById(R.id.home_recyclerview_items);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(),2);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ItemsAdapter(getLayoutInflater());
        itemsList = ItemsDAO.get_all();
        adapter.setData(itemsList);
        adapter.setListener(new ItemsAdapter.ItemOnClickListener() {
            @Override
            public void onClick(Items i) {
                Intent intent = new Intent(getActivity(), ItemDetailActivity.class);
                Gson gson = new Gson();
                intent.putExtra("item",gson.toJson(i));
                startActivity(intent);
            }
        });

        recyclerView.setAdapter(adapter);
        categoryView = root.findViewById(R.id.home_recyclerview_category);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        categoryView.setLayoutManager(linearLayoutManager);
        categoryAdapter = new CategoryAdapter(getLayoutInflater());
        categoryAdapter.setData(CategoryDAO.get_all());
        categoryAdapter.setListener(new CategoryAdapter.CategoryOnClickListener() {
            @Override
            public void OnClick(List<Integer> list) {
                if (list==null || list.isEmpty())
                {
                    itemsList = ItemsDAO.get_all();
                    adapter.setData(itemsList);
                    binding.emptyLayoutViewHome.setVisibility(View.GONE);
                    binding.homeRecyclerviewItems.setVisibility(View.VISIBLE);
                    return;
                }
                ProgressDialog pd = new ProgressDialog();
                pd.show_progress_dialog(getContext());
                itemsList.clear();
                for (int id: list)
                    for (Items item: ItemsDAO.get_by_category(id))
                        itemsList.add(item);
                adapter.setData(itemsList);
                if (itemsList.isEmpty())
                {

                    binding.homeRecyclerviewItems.setVisibility(View.GONE);
                    binding.emptyLayoutViewHome.setVisibility(View.VISIBLE);
                }
                else
                {
                    binding.emptyLayoutViewHome.setVisibility(View.GONE);
                    binding.homeRecyclerviewItems.setVisibility(View.VISIBLE);
                }
                searchView.setIconified(true);
                searchView.setQuery("", false);
                searchView.clearFocus();
                searchView.setIconified(true);
                pd.hide_progress_dialog();
            }
        });
        categoryView.setAdapter(categoryAdapter);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mTimer!=null)
            mTimer.cancel();
        binding = null;
    }

    void auto_slide_image()
    {
        if (viewPager == null || photoList == null || photoList.isEmpty())
            return ;

        if (mTimer == null)
            mTimer = new Timer();

        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int cur = viewPager.getCurrentItem();
                        if (cur < photoList.size()-1)
                            viewPager.setCurrentItem(cur+1);
                        else
                            viewPager.setCurrentItem(0);
                    }
                });
            }
        }, 500, 3000);
    }

    void initial_ad_photo()
    {
        photoList = new ArrayList<>();

        Photo ad1 = new Photo(R.drawable.ad_1);
        Photo ad2 = new Photo(R.drawable.ad_2);
        Photo ad3 = new Photo(R.drawable.ad_3);
        Photo ad4 = new Photo(R.drawable.ad_4);
        Photo ad5 = new Photo(R.drawable.ad_6);
        Photo ad6 = new Photo(R.drawable.ad_7);

        photoList.add(ad1);
        photoList.add(ad2);
        photoList.add(ad3);
        photoList.add(ad4);
        photoList.add(ad5);
        photoList.add(ad6);
    }

    @Override
    public void onStart() {
        Log.d("HOME FRAGMENT --------------","On Start");
        super.onStart();
    }

    @Override
    public void onResume() {
        Log.d("HOME FRAGMENT --------------","On Resume");
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.d("HOME FRAGMENT --------------","On Pause");
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.d("HOME FRAGMENT --------------","On Stop");
        super.onStop();
    }

    @Override
    public void onDestroy() {
        Log.d("HOME FRAGMENT --------------","On Destroy");
        super.onDestroy();
    }

    @Override
    public boolean onMenuItemActionExpand(MenuItem item) {
        return true;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem item) {
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query){
        adapter.getFilter().filter(query);
        if (adapter.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            binding.emptyLayoutViewHome.setVisibility(View.VISIBLE);
        }
        else {
            recyclerView.setVisibility(View.VISIBLE);
            binding.emptyLayoutViewHome.setVisibility(View.GONE);
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapter.getFilter().filter(newText);
        if (adapter.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            binding.emptyLayoutViewHome.setVisibility(View.VISIBLE);
        }
        else {
            recyclerView.setVisibility(View.VISIBLE);
            binding.emptyLayoutViewHome.setVisibility(View.GONE);
        }
        return false;
    }
}