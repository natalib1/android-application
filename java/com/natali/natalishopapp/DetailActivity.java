//Show the product page with all relevant details

package com.natali.natalishopapp;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.natali.natalishopapp.FirebaseStorage.FirebaseHandler;
import com.natali.natalishopapp.Objects.Product;
import com.natali.natalishopapp.SharedPreference.SharedPref;
import com.natali.natalishopapp.Utils.Constants;
import com.natali.natalishopapp.Utils.Utils;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {

    private String userId;
    private int userType;
    Product selectedProduct;
    ArrayList<Product> products = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        Intent intent = getIntent();
        selectedProduct = (Product) getIntent().getExtras().getSerializable(Constants.PRODUCT_ID);

        TextView codeText = (TextView) findViewById(R.id.codeText);
        codeText.setText(selectedProduct.getCode());

        final ImageView ivp = (ImageView) findViewById(R.id.imageView);
        Picasso.with(getApplication()).load(selectedProduct.getImageUrl()).into(ivp);

        TextView nameText = (TextView) findViewById(R.id.nameText);
        nameText.setText(selectedProduct.getName());

        String price = (selectedProduct.getPrice() + " NIS");
        TextView priceText = (TextView) findViewById(R.id.priceText);
        priceText.setText(price);

        double sprice = selectedProduct.getShippingPrice();
        TextView spriceText = (TextView) findViewById(R.id.shipping_priceText);
        spriceText.setText(Utils.fixShippingPrice(sprice));

        //TODO: fill the spinner with colors from DB

        TextView descText = (TextView) findViewById(R.id.descriptionText);
        descText.setText(selectedProduct.getDescription());

        TextView sellerText = (TextView) findViewById(R.id.sellerText);
        String sellerId = Utils.fixEmailStringBack(selectedProduct.getSellerId());
        sellerText.setText(sellerId);

        ImageButton imgBtnSeller = (ImageButton) findViewById(R.id.conect_ImageButton);
        imgBtnSeller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String seller = Utils.fixEmailStringBack(SharedPref.shared().getUserId(DetailActivity.this));

                String[] addresses = {seller};
                String[] adressesCC = {Constants.EMAIL};
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:"));
                intent.putExtra(Intent.EXTRA_EMAIL, addresses);
                intent.putExtra(Intent.EXTRA_CC, adressesCC);
                intent.putExtra(Intent.EXTRA_SUBJECT, "Information request - item  \n" + selectedProduct.getCode());
                intent.putExtra(Intent.EXTRA_TEXT, "Please send some information \n\nBest regards,\n" + seller + "\nNataliShopApp");
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });

        Button btnBuy = (Button) findViewById(R.id.buyButton);
        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SharedPref.shared().getUserId(DetailActivity.this) != null) {
                    products.add(selectedProduct);

                    Intent intent = new Intent(DetailActivity.this, PaymentActivity.class);
                    intent.putExtra(Constants.PRODUCTS, products);
                    intent.putExtra(Constants.CLASS, "DetailActivity");
                    startActivity(intent);
                } else {
                    Utils.showAlertOk(DetailActivity.this, "Login!", "You must login in order to buy item");
                }
            }
        });

        final Button btnCart = (Button) findViewById(R.id.add_to_cartButton);
        final Button btnAfterCart = (Button) findViewById(R.id.add_to_cartButton);
        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SharedPref.shared().getUserId(DetailActivity.this) != null) {
                    //add item to shopping cart
                    FirebaseHandler.addItemToShoppingCart(getApplication(), selectedProduct, new FirebaseHandler.SuccessCallBack() {
                        @Override
                        public void SuccessCallBack(Boolean success) {
                            if (success){
                                btnAfterCart.setText(R.string.view_in_cart);
                                btnAfterCart.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(DetailActivity.this, ShoppingcartActivity.class);
                                        startActivity(intent);
                                    }
                                });
                            }
                        }
                    });
                } else {
                    Utils.showAlertOk(DetailActivity.this, "Login!", "You must login in order to add item to shopping cart");
                }
            }
        });

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    // Inflate the menu according to the user type; this adds items to the action bar if it is present.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);

        userId = SharedPref.shared().getUserId(DetailActivity.this);
        if (userId != null) {
            userType = SharedPref.shared().getUserType(DetailActivity.this);
        }

        if (userType == 1 || userType == 2) {
            menu.getItem(0).setVisible(false); //sign_in
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id){
            case R.id.action_sign_in:
                Intent intent100 = new Intent(this, LoginActivity.class);
                startActivity(intent100);
                return true;
            case R.id.action_home:
                Intent intent101 = new Intent(this, MainActivity.class);
                startActivity(intent101);
                return true;
            case R.id.action_about:
                Intent intent102 = new Intent(this, AboutActivity.class);
                startActivity(intent102);
        }

        return super.onOptionsItemSelected(item);
    }

}
