package com.koit.capstonproject_version_1.View;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.koit.capstonproject_version_1.Adapter.AddProductQuantityAdapter;
import com.koit.capstonproject_version_1.Adapter.EditConvertRateAdapter;
import com.koit.capstonproject_version_1.Controller.AddProductQuantityController;
import com.koit.capstonproject_version_1.Controller.CreateProductController;
import com.koit.capstonproject_version_1.Controller.DetailProductController;
import com.koit.capstonproject_version_1.Controller.ListCategoryController;
import com.koit.capstonproject_version_1.Model.Category;
import com.koit.capstonproject_version_1.Model.Product;
import com.koit.capstonproject_version_1.Model.Unit;
import com.koit.capstonproject_version_1.R;
import com.koit.capstonproject_version_1.dao.CreateProductDAO;

import java.util.ArrayList;
import java.util.List;

public class ConvertRateActivity extends AppCompatActivity {

    public static final String IS_SUCCESS = "SUCCESS";

    private Toolbar toolbar;
    private TextView tvToolbarTitle;
    private Product currentProduct;
    private RecyclerView rvConvertRate;
    private RecyclerView rvUnitQuantity;
    private Button btnSuccess;
    private LinearLayout layoutConvertRate;
    private ArrayList<Unit> unitList;
    private EditConvertRateAdapter editConvertRateAdapter;
    private AddProductQuantityAdapter addProductQuantityAdapter;
    private CreateProductController createProductController;
    private AddProductQuantityController addProductQuantityController;
    private DetailProductController detailProductController;

    private ListCategoryController listCategoryController;
    private List<Category> categoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_convert_rate);

        initView();
        tvToolbarTitle.setText("Thêm sản phẩm");

        getProduct();
        buildRecyclerViewUnits();
        buildRecyclerUnitQuantity();

    }



    private void initView() {
        toolbar = findViewById(R.id.toolbarGeneral);
        tvToolbarTitle = toolbar.findViewById(R.id.tvToolbarTitle);
        btnSuccess = findViewById(R.id.btnSuccess);
        rvConvertRate = findViewById(R.id.rvConvertRate);
        rvUnitQuantity = findViewById(R.id.rvQuantityAdd);
        layoutConvertRate = findViewById(R.id.layoutConvertRate);

        createProductController = new CreateProductController(this);
        addProductQuantityController = new AddProductQuantityController();
        detailProductController = new DetailProductController();

        listCategoryController = new ListCategoryController(this);
//        listCategoryController.getListCategory(this);
//        //  CategoryDAO.getInstance().getListCategory(this, lvCategory);
//        (new Handler()).postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//                categoryList = listCategoryController.getCategories();
//                for (Category category : categoryList){
//                    Log.i("kiemtraCategory",category.getCategoryName());
//                }
//
//            }
//        }, 3000);

    }

    public void addProduct(View view){
        (new Handler()).postDelayed(new Runnable() {
            @Override
            public void run() {
                addProductToFirebase();
            }
        },2000)   ;
    }

    private void addProductToFirebase() {

        try{
            addUnitToFirebase();
            createProductController.addProductInFirebase(currentProduct);
            Toast.makeText(this.getApplicationContext(), "Thêm sản phẩm thành công!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra(IS_SUCCESS, true);
            intent.putExtra(CreateProductActivity.NEW_PRODUCT, currentProduct);
            startActivity(intent);
            this.finish();
        }catch (Exception e){
            Toast.makeText(this, "Thêm sản phẩm thất bại! Vui lòng thử lại...", Toast.LENGTH_SHORT).show();
        }

    }

    private void addUnitToFirebase() {
        if(unitList.size() >1){
            setConvertRateFromRv();
        }else{
            unitList.get(0).setConvertRate(1);
        }
        setUnitQuantityFromRV();
        currentProduct.setUnits(unitList);
        addProductQuantityController.addUnitsToFireBase(currentProduct,unitList);
    }


    private void getProduct() {
        Intent intent = getIntent();
        currentProduct = (Product) intent.getSerializableExtra(CreateProductActivity.NEW_PRODUCT);
        unitList = new ArrayList<>();
        unitList = (ArrayList<Unit>) currentProduct.getUnits();
        detailProductController.sortUnitByPrice(unitList);

    }

    private void buildRecyclerViewUnits() {
        if(unitList.size() >1){
            rvConvertRate.setHasFixedSize(true);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            rvConvertRate.setLayoutManager(linearLayoutManager);
            editConvertRateAdapter = new EditConvertRateAdapter( unitList,this);
            rvConvertRate.setAdapter(editConvertRateAdapter);
        }else{
            layoutConvertRate.setVisibility(View.GONE);
        }
    }

    private void buildRecyclerUnitQuantity() {
        rvUnitQuantity.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvUnitQuantity.setLayoutManager(linearLayoutManager);
        addProductQuantityAdapter = new AddProductQuantityAdapter( unitList,this);
        rvUnitQuantity.setAdapter(addProductQuantityAdapter);
    }

    private void setConvertRateFromRv() {
        for (int i = 0; i < editConvertRateAdapter.getItemCount(); i++) {
            EditConvertRateAdapter.ViewHolder viewHolder = (EditConvertRateAdapter.ViewHolder) rvConvertRate.findViewHolderForAdapterPosition(i);
            String convertRate = viewHolder.getEtConvertRate().getText().toString().trim();
            long rate = convertRate.isEmpty() ? 1 : Long.parseLong(convertRate);
            unitList.get(i).setConvertRate(rate);
        }
    }

    private void setUnitQuantityFromRV() {
        List<Unit> list = new ArrayList<>();
        for (int i = 0; i < addProductQuantityAdapter.getItemCount(); i++) {
            AddProductQuantityAdapter.ViewHolder viewHolder = (AddProductQuantityAdapter.ViewHolder) rvUnitQuantity.findViewHolderForAdapterPosition(i);
            String unitQuantity = viewHolder.getEtProductQuantity().getText().toString().trim();
            long quantity = unitQuantity.isEmpty() ? 0 : Long.parseLong(unitQuantity);
            unitList.get(i).setUnitQuantity(quantity);
        }
        Log.i("unitList", unitList.toString());
        addProductQuantityController.convertUnitList2(unitList);
    }

    public void back(View view){
        onBackPressed();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
