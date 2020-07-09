package com.koit.capstonproject_version_1.Model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.koit.capstonproject_version_1.Controller.Interface.ListProductInterface;
import com.koit.capstonproject_version_1.Controller.SharedPreferences.SharedPrefs;
import com.koit.capstonproject_version_1.View.LoginActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

public class Product implements Serializable {
    private String userId, productId, barcode, categoryName, productDescription, productImageUrl;
    private String productName;
    private boolean active;
    DatabaseReference nodeRoot;
    private List<Unit> units;
    private static List<Product> productListSearch;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    public Product(String userId, String productId, String barcode, String categoryName, String productDescription,
                   String productImageUrl, String productName, boolean active, List<Unit> units) {
        this.userId = userId;
        this.productId = productId;
        this.barcode = barcode;
        this.categoryName = categoryName;
        this.productDescription = productDescription;
        this.productImageUrl = productImageUrl;
        this.productName = productName;
        this.active = active;
        this.units = units;
    }

    public Product() {


    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getProductImageUrl() {
        return productImageUrl;
    }

    public void setProductImageUrl(String productImageUrl) {
        this.productImageUrl = productImageUrl;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<Unit> getUnits() {
        return units;
    }

    public void setUnits(List<Unit> units) {
        this.units = units;
    }

    public DatabaseReference getMyRef() {
        String curUser;
        User user = SharedPrefs.getInstance().getCurrentUser(LoginActivity.CURRENT_USER);
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//        if (user != null) {
//            curUser = user.getPhoneNumber();
//        } else {
//            curUser = firebaseUser.getUid();
//        }
        DatabaseReference myRef;
        //test
        myRef = FirebaseDatabase.getInstance().getReference("Products").child("0399271212");
        return myRef;
    }

    public static void show(Context c, String message) {
        Toast.makeText(c, message, Toast.LENGTH_SHORT).show();
    }


    public void getListProduct(final String searchText, final ListProductInterface
            listProductInterface, final TextView textView, final LinearLayout linearLayoutEmpty,
                               final ConstraintLayout layoutSearch, final LinearLayout layoutNotFoundItem, final Spinner category_Spinner) {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                getListProduct(dataSnapshot, listProductInterface, searchText, textView, linearLayoutEmpty, layoutSearch, layoutNotFoundItem, category_Spinner);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        nodeRoot = FirebaseDatabase.getInstance().getReference();
        nodeRoot.keepSynced(true);
        nodeRoot.addListenerForSingleValueEvent(valueEventListener);
    }

    @SuppressLint("WrongConstant")
    private void getListProduct(DataSnapshot dataSnapshot, ListProductInterface listProductInterface,
                                String searchText, TextView textView, LinearLayout linearLayoutEmpty,
                                ConstraintLayout constraintLayoutfound, LinearLayout layoutNotFoundItem, Spinner category_Spinner) {
        DataSnapshot dataSnapshotProduct;
        dataSnapshotProduct = dataSnapshot.child("Products").child("0399271212");
        DataSnapshot dataSnapshotUnits = dataSnapshot.child("Units").child("0399271212");
        boolean isFound = false;
        if (dataSnapshotProduct == null) {
            linearLayoutEmpty.setVisibility(View.VISIBLE);
            constraintLayoutfound.setVisibility(View.GONE);
            layoutNotFoundItem.setVisibility(View.GONE);
        } else {
            layoutNotFoundItem.setVisibility(View.GONE);
            linearLayoutEmpty.setVisibility(View.GONE);
            constraintLayoutfound.setVisibility(View.VISIBLE);
            //Lấy danh sách san pham
            for (DataSnapshot valueProduct : dataSnapshotProduct.getChildren()) {
                Product product = valueProduct.getValue(Product.class);
                product.setProductId(valueProduct.getKey());

                DataSnapshot dataSnapshotUnit = dataSnapshotUnits.child(product.getProductId());

                Log.d("kiemtraProductID", product.getProductId() + "");
                //lay unit theo ma san pham
                List<Unit> unitList = new ArrayList<>();
                for (DataSnapshot valueUnit : dataSnapshotUnit.getChildren()) {
                    Log.d("kiemtraUnit", valueUnit + "");
                    Unit unit = valueUnit.getValue(Unit.class);

                    unit.setUnitId(valueUnit.getKey());
                    unitList.add(unit);
                }
                product.setUnits(unitList);
                //user searched
                if (searchText == null) {
                    listProductInterface.getListProductModel(product);
                } else {
                    category_Spinner.setSelection(0);
                    if (searchText == "") {
                        //list product in first time or list all product
                        listProductInterface.getListProductModel(product);
                    } else {
                        //product contains searched characters or barcode
                        if (product.getProductName().toLowerCase().contains(searchText.toLowerCase()) || product.getBarcode().contains(searchText)) {
                            isFound = true;
                            listProductInterface.getListProductModel(product);
                        }
                        if (!isFound) {
                            textView.setText("0 sản phẩm");
                            layoutNotFoundItem.setVisibility(View.VISIBLE);
                        } else {
                            layoutNotFoundItem.setVisibility(View.GONE);

                        }
                    }
                }
            }

        }

    }


    //Cate Tu Beo
    public void getListProduct(final ListProductInterface listProductInterface, final String categoryName,
                               final LinearLayout linearLayoutEmpty, final ConstraintLayout constraintLayout, final LinearLayout layoutNotFoundItem, final TextView textView, Spinner category_Spinner) {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                getListProduct(dataSnapshot, listProductInterface, categoryName, linearLayoutEmpty, constraintLayout, layoutNotFoundItem, textView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        nodeRoot = FirebaseDatabase.getInstance().getReference();
        nodeRoot.keepSynced(true);
        nodeRoot.addListenerForSingleValueEvent(valueEventListener);
    }

    private void getListProduct(DataSnapshot dataSnapshot, ListProductInterface listProductInterface, String categoryName,
                                LinearLayout linearLayoutEmpty, ConstraintLayout constraintLayout, LinearLayout layoutNotFoundItem, TextView textView) {
        DataSnapshot dataSnapshotProduct = dataSnapshot.child("Products").child("0399271212");
        DataSnapshot dataSnapshotUnits = dataSnapshot.child("Units").child("0399271212");
        boolean isFound = false;
        if (dataSnapshotProduct == null) {
            linearLayoutEmpty.setVisibility(View.VISIBLE);
            constraintLayout.setVisibility(View.GONE);
            layoutNotFoundItem.setVisibility(View.GONE);
        } else {
            layoutNotFoundItem.setVisibility(View.GONE);
            linearLayoutEmpty.setVisibility(View.GONE);
            constraintLayout.setVisibility(View.VISIBLE);
            //Lấy danh sách san pham
            for (DataSnapshot valueProduct : dataSnapshotProduct.getChildren()) {
                Product product = valueProduct.getValue(Product.class);
                product.setProductId(valueProduct.getKey());

                DataSnapshot dataSnapshotUnit = dataSnapshotUnits.child(product.getProductId());

                Log.d("kiemtraProductID", product.getProductId() + "");
                //lay unit theo ma san pham
                List<Unit> unitList = new ArrayList<>();
                for (DataSnapshot valueUnit : dataSnapshotUnit.getChildren()) {
                    Log.d("kiemtraUnit", valueUnit + "");
                    Unit unit = valueUnit.getValue(Unit.class);

                    unit.setUnitId(valueUnit.getKey());
                    unitList.add(unit);
                }
                product.setUnits(unitList);
                if (product.getCategoryName().equals(categoryName)) {
                    isFound = true;
                    listProductInterface.getListProductModel(product);
                }
                if (!isFound) {
                    textView.setText("0 sản phẩm");
                    layoutNotFoundItem.setVisibility(View.VISIBLE);
                } else {
                    layoutNotFoundItem.setVisibility(View.GONE);

                }
            }
        }
    }

    public void removeProduct(String userId, String productId) {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("Products").child(userId).child(productId);
        databaseReference.removeValue();
    }

}
