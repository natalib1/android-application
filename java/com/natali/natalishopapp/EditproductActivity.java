//Seller can edit the details of his product
//Don't influence on purchases and selling history

package com.natali.natalishopapp;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.natali.natalishopapp.FirebaseStorage.FirebaseHandler;
import com.natali.natalishopapp.Objects.Product;
import com.natali.natalishopapp.Utils.Constants;
import com.natali.natalishopapp.Utils.Utils;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class EditproductActivity extends AppCompatActivity {

    private LinearLayout ll;
    private LayoutInflater inflater;
    private Product curProduct;
    private boolean flag = false; //false - user didn't pick new image, true - user pick new image
    private String curProdId;
    private String curImageUrl;

    LinearLayout codeLayout;
    TextView txtProductId;
    EditText txtName;
    EditText txtDescription;
    Spinner spiCategory;
    EditText txtPrice;
//    EditText txtShippingPrice;    //From the seller
    EditText txtColor;
//    EditText txtSellerId;
//    TextView txtCode;    //The id given in firebase
//    EditText txtImageUrl;

    String category;

//    private static final int PICK_PHOTO_FOR_AVATAR = 100;
    ImageButton btnPickImage;
    byte[] imageByteArray ;

    Button btnEditProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editproduct);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        Intent intent = getIntent();
        curProduct = (Product) getIntent().getExtras().getSerializable(Constants.PRODUCT_ID);

        ll = (LinearLayout) findViewById(R.id.add_editLayout);
        inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.add_edit_product, null);
        ll.addView(view);

        setupButtons(view);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupButtons(View view ){
        codeLayout = (LinearLayout) findViewById(R.id.product_codeLayout);
        txtProductId = (TextView) view.findViewById(R.id.product_codeText);
        txtProductId.setText(curProduct.getCode());
        codeLayout.setVisibility(View.VISIBLE);

        txtName = (EditText) view.findViewById(R.id.product_nameEditText);
        txtName.setText(curProduct.getName());

        txtDescription = (EditText) view.findViewById(R.id.product_descriptionEditText);
        txtDescription.setText(curProduct.getDescription());

        category = "";
        spiCategory = (Spinner) view.findViewById(R.id.product_categorySpinner);
        //set spinner to original item category
        ArrayList<String> categoryArr = Utils.editArrayList(curProduct.getCategory());
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, categoryArr);
        spiCategory.setAdapter(adapter);
        spiCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                category = parent.getItemAtPosition(pos).toString();
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        txtPrice = (EditText) view.findViewById(R.id.product_priceEditText);
        txtPrice.setText(Double.toString(curProduct.getPrice()));

        //TODO: change color to not edittext and receive color
//        txtColor = (EditText) view.findViewById(R.id.product_colorsLayout);
//        txtColor.setText(curProduct.getColor());

        btnPickImage = (ImageButton) findViewById(R.id.product_imageButton);
        Picasso.with(this).load(curProduct.getImageUrl()).into(btnPickImage);
        curProdId = curProduct.getProductId();
        curImageUrl = curProduct.getImageUrl();
        btnPickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImage();
                curProdId = Utils.generateUniqID();
                curImageUrl = null;
                flag = true;
            }
        });

        //edit product
        btnEditProduct = (Button) findViewById(R.id.edit_productButton);
        btnEditProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValidData(view)){
                    final Product prod = new Product(
                            curProdId, //Utils.generateUniqID(), //productId from the image id in firebase
                            txtName.getText().toString(),
                            txtDescription.getText().toString(),
                            category,
                            Double.parseDouble(txtPrice.getText().toString()),
                            curProduct.getShippingPrice(), //shippingPrice from seller
                            //TODO: get colors
                            null, //txtColor.getText().toString(),
                            curProduct.getSellerId(), //sellerId from seller
                            curProduct.getCode(), //code from the item's id in firebase
                            curImageUrl, //imageurl
                            null, //reservationId
                            null, //CostumerId
                            null //reservationDate
                    );

                    if (flag) {
                        FirebaseHandler.uploadItemImageByteArray(EditproductActivity.this, prod.getProductId(), imageByteArray, new FirebaseHandler.ImageUrlCallBack() {
                            @Override
                            public void ImageUrlCallBack(String strImage) {
                                prod.setImageUrl(strImage);
                                EditItem(prod);
                            }
                        });
                    }else {
                        EditItem(prod);
                    }
                }
            }});
    }

    private boolean isValidData(View view){
        if (txtName.getText().toString().trim().length() == 0 ||
                txtDescription.getText().toString().trim().length() == 0  ||
                category == "" ||
                txtPrice.getText().toString().trim().length() == 0
                //color
//                imageByteArray == null
                )
        {
            Snackbar.make(view, "One or more field missing", Snackbar.LENGTH_LONG)
                    .setAction("", null).show();
            return false;
        }
        return true;
    }

    public void EditItem(final Product prod){
        FirebaseHandler.editItemToDataBase(EditproductActivity.this, prod, new FirebaseHandler.SuccessCallBack() {
            @Override
            public void SuccessCallBack(Boolean success) {
                if (success) {
                    Utils.showAlertOk(EditproductActivity.this, "Edit success", "Product " + prod.getCode() + " edited!", new Utils.ShowAlert_CallBack() {
                        @Override
                        public void ShowAlert_UserClickedYes() {
                            //go to ProductmanagementActivity page
                            Intent intent = new Intent(EditproductActivity.this, ProductmanagementActivity.class);
                            startActivity(intent);
                        }
                    });
                }
                else{
                    Utils.showAlertOk(EditproductActivity.this,"Edit Failed","Could not edit the product");
                }
            }
        });
    }

    public void pickImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, Constants.PICK_PHOTO_FOR_AVATAR);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.PICK_PHOTO_FOR_AVATAR && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                //Display an error
                return;
            }
            try {
                InputStream inputStream = getContentResolver().openInputStream(data.getData());

                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();

                Bitmap bitmapNeedOrientation = BitmapFactory.decodeFile(picturePath);
                Bitmap bitmap = Utils.rotateImageToCorrectPosition(bitmapNeedOrientation,picturePath);;
                btnPickImage.setImageBitmap(bitmap);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
                imageByteArray = baos.toByteArray();

                //FirebaseHandler.uploadUserImageByteArray(getContext(),dataArr);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

}
