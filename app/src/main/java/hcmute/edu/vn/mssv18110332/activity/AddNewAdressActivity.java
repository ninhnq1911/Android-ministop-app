package hcmute.edu.vn.mssv18110332.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.QuickContactBadge;
import android.widget.Toast;

import java.util.List;

import hcmute.edu.vn.mssv18110332.DAO.AddressDAO;
import hcmute.edu.vn.mssv18110332.DAO.DistrictsDAO;
import hcmute.edu.vn.mssv18110332.DAO.ProvincesDAO;
import hcmute.edu.vn.mssv18110332.DAO.UserAccountDAO;
import hcmute.edu.vn.mssv18110332.DAO.WardsDAO;
import hcmute.edu.vn.mssv18110332.adapter.address.DisAdapter;
import hcmute.edu.vn.mssv18110332.adapter.address.ProAdapter;
import hcmute.edu.vn.mssv18110332.adapter.address.WarAdapter;
import hcmute.edu.vn.mssv18110332.databinding.ActivityAddNewAdressBinding;
import hcmute.edu.vn.mssv18110332.helper.AppUtils;
import hcmute.edu.vn.mssv18110332.helper.ConfirmDialog;
import hcmute.edu.vn.mssv18110332.helper.ProgressDialog;
import hcmute.edu.vn.mssv18110332.model.Address;
import hcmute.edu.vn.mssv18110332.model.Districts;
import hcmute.edu.vn.mssv18110332.model.Provinces;
import hcmute.edu.vn.mssv18110332.model.Useraccount;
import hcmute.edu.vn.mssv18110332.model.Wards;

public class AddNewAdressActivity extends AppCompatActivity {

    ActivityAddNewAdressBinding binding;
    ProAdapter proAdapter;
    DisAdapter disAdapter;
    WarAdapter warAdapter;
    Address mAddress;
    Intent mIntent;
    List<Provinces> mPro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAddNewAdressBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.disSpinner.setVisibility(View.GONE);
        binding.warSpinner.setVisibility(View.GONE);
        binding.btnNewAddress.setVisibility(View.GONE);
        binding.btnDeleteAddress.setVisibility(View.GONE);

        mPro = ProvincesDAO.get_all();

        proAdapter = new ProAdapter(AddNewAdressActivity.this,binding.proSpinner.getId(),mPro);
        binding.proSpinner.setAdapter(proAdapter);
        binding.proSpinner.setSelected(false);
        binding.proSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getContext(),"Selected provinces = " + proAdapter.getItem(position).getName(),Toast.LENGTH_SHORT).show();
                binding.disSpinner.setVisibility(View.VISIBLE);
                int proid = proAdapter.getItem(position).getId();
                disAdapter = new DisAdapter(getContext(),binding.disSpinner.getId(), DistrictsDAO.get_by_proid(proid));
                binding.disSpinner.setAdapter(disAdapter);
                binding.disSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        ///Toast.makeText(getContext(),"Selected district = " + disAdapter.getItem(position).getName(),Toast.LENGTH_SHORT).show();
                        binding.warSpinner.setVisibility(View.VISIBLE);
                        int disid = disAdapter.getItem(position).getId();
                        warAdapter = new WarAdapter(getContext(),binding.warSpinner.getId(), WardsDAO.get_by_disid(disid));
                        binding.warSpinner.setAdapter(warAdapter);
                        binding.btnNewAddress.setVisibility(View.VISIBLE);
                        binding.warSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                binding.btnNewAddress.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.btnNewAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog pd = new ProgressDialog();
                pd.show_progress_dialog(getContext());
                Useraccount u = AppUtils.getCurrentUser();
                Address a = new Address();
                a.setId(0);
                a.setName(binding.txtNameAddress.getText().toString());
                a.setPro(proAdapter.getItem(binding.proSpinner.getSelectedItemPosition()).getId());
                a.setDis(disAdapter.getItem(binding.disSpinner.getSelectedItemPosition()).getId());
                a.setUser(u.getId());
                a.setWar(warAdapter.getItem(binding.warSpinner.getSelectedItemPosition()).getId());
                a.setHome(binding.txtAddressHome.getText().toString());
                if (mAddress!=null)
                    AddressDAO.delete(mAddress);
                AddressDAO.insert(a);
                if (u.getAddress() == 0)
                {
                    binding.checkboxDefaultAddress.setChecked(true);
                }
                if (binding.checkboxDefaultAddress.isChecked())
                {
                    List<Address> la = AddressDAO.get_by_user(u.getId());
                    int i_max = -1;
                    for (Address ia: la)
                        if (ia.getId()>i_max)
                            i_max = ia.getId();
                    u.setAddress(i_max);
                    UserAccountDAO.update(u);
                }
                pd.hide_progress_dialog();
                Intent i = new Intent();
                i.putExtra("result","OK");
                setResult(Activity.RESULT_OK,i);
                finish();
            }
        });

        binding.btnDeleteAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfirmDialog dialog = new ConfirmDialog(getContext());
                dialog.setConfirmCLickListener(new ConfirmDialog.onConfirmCLickListener() {
                    @Override
                    public void onClick(boolean result) {
                        if (AddressDAO.isDefault(mAddress))
                        {
                            Toast.makeText(AddNewAdressActivity.this, "Không thể xóa địa chỉ mặc định", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (mAddress!=null)
                            AddressDAO.delete(mAddress);
                        Intent i = new Intent();
                        i.putExtra("result","OK");
                        setResult(Activity.RESULT_OK,i);
                        Toast.makeText(AddNewAdressActivity.this, "Bạn đã xóa địa chỉ thành công", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
                dialog.showDialog(Gravity.CENTER,"xác nhận xóa địa chỉ",
                        "Hãy chắc chắn rằng bạn muốn xóa địa chỉ này ra khỏi sổ địa chỉ của bạn nhé!");
            }
        });

        mIntent = getIntent();
        try
        {
            mAddress = AddressDAO.get_by_id((int)mIntent.getExtras().get("Address"));
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        if (mAddress != null)
        {
            for (int i=0; i<mPro.size(); i++)
            {
                if (mAddress.getPro() == mPro.get(i).getId())
                {
                    binding.proSpinner.setSelection(i);
                    List<Districts> mDis = DistrictsDAO.get_by_proid(mAddress.getPro());
                    for (int j=0; j<mDis.size(); j++)
                    {
                        if (mAddress.getDis() == mDis.get(j).getId())
                        {
                            List<Wards> mWar = WardsDAO.get_by_disid(mAddress.getDis());
                            binding.disSpinner.setSelection(j);
                            for (int k=0; k<mWar.size(); k++)
                            {
                                if (mAddress.getWar() == mWar.get(k).getId())
                                {
                                    binding.warSpinner.setSelection(k);
                                    break;
                                }
                            }
                            break;
                        }
                    }
                    break;
                }
            }
            binding.txtAddressHome.setText(mAddress.getHome());
            binding.txtNameAddress.setText(mAddress.getName());
            binding.btnDeleteAddress.setVisibility(View.VISIBLE);
            binding.checkboxDefaultAddress.setChecked(AddressDAO.isDefault(mAddress,AppUtils.getCurrentUser()));
            binding.checkboxDefaultAddress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (AddressDAO.isDefault(mAddress,AppUtils.getCurrentUser())){
                        binding.checkboxDefaultAddress.setChecked(true);
                        Toast.makeText(AddNewAdressActivity.this, "Đây đã là địa chỉ mặc định rồi!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public Context getContext() {
        return AddNewAdressActivity.this;
    }

}