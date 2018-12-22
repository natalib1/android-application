//Seller can add an item to his items' list

package com.natali.natalishopapp;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import android.widget.Spinner;
import android.widget.TextView;

import com.natali.natalishopapp.FirebaseStorage.FirebaseHandler;
import com.natali.natalishopapp.Objects.Product;
import com.natali.natalishopapp.Objects.User;
import com.natali.natalishopapp.SharedPreference.SharedPref;
import com.natali.natalishopapp.Utils.Utils;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class AddproductActivity extends AppCompatActivity {

    private LinearLayout ll;
    private LayoutInflater inflater;
    private User curUser;

//    EditText txtProductId;
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

    private static final int PICK_PHOTO_FOR_AVATAR = 100;
    ImageButton btnPickImage;
    byte[] imageByteArray ;

    Button btnAddProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addproduct);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ll = (LinearLayout) findViewById(R.id.add_editLayout);
        inflater = this.getLayoutInflater();

        View view = inflater.inflate(R.layout.add_edit_product, null);
        ll.addView(view);

        setupButtons(view);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupButtons(View view ){
        //txtProductId;
        txtName = (EditText) view.findViewById(R.id.product_nameEditText);
        txtDescription = (EditText) view.findViewById(R.id.product_descriptionEditText);

        category = "";
        spiCategory = (Spinner) view.findViewById(R.id.product_categorySpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.product_categoryArray, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spiCategory.setAdapter(adapter);
        spiCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                category = parent.getItemAtPosition(pos).toString();
                if (pos == 0) {
                    onNothingSelected(parent);
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {
                category="";
            }
        });

        txtPrice = (EditText) view.findViewById(R.id.product_priceEditText);

        //TODO: change color to not edittext and receive color
//        txtColor = (EditText) view.findViewById(R.id.product_colorsLayout);
//        public void onCheckboxClicked(View view) {
//            // Is the view now checked?
//            boolean checked = ((CheckBox) view).isChecked();
//
//            // Check which checkbox was clicked
//            switch(view.getId()) {
//                case R.id.greenCheckBox:
//                    if (checked){
//
//                    } else {
//
//                    }
//                    break;
//                case R.id.yellowheckBox:
//                    if (checked){
//
//                    } else {
//
//                    }
//                case R.id.blueCheckBox:
//                    if (checked){
//
//                    } else {
//
//                    }
//                case R.id.redCheckBox:
//                    if (checked){
//
//                    } else {
//
//                    }
//                case R.id.pinkCheckBox:
//                    if (checked){
//
//                    } else {
//
//                    }
//                case R.id.orangeCheckBox:
//                    if (checked){
//
//                    } else {
//
//                    }
//                case R.id.blackCheckBox:
//                    if (checked){
//
//                    } else {
//
//                    }
//                case R.id.whiteCheckBox:
//                    if (checked){
//
//                    } else {
//
//                    }
//            }
//        }

        final String userEmail = SharedPref.shared().getUserId(AddproductActivity.this);
        FirebaseHandler.getUser(userEmail, "", new FirebaseHandler.UserCallBack() {
            @Override
            public void UserCallBack(User user) {
                curUser = user;
            }
        });
        btnPickImage = (ImageButton) findViewById(R.id.product_imageButton);
        btnPickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImage();
            }
        });

        btnAddProduct = (Button) findViewById(R.id.add_productButton);
        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValidData(view)){
                    final Product prod = new Product(
                            Utils.generateUniqID(), //productId from the image id in firebase
                            txtName.getText().toString(),
                            txtDescription.getText().toString(),
                            category,
                            Double.parseDouble(txtPrice.getText().toString()),
                            curUser.getShippingCost(), //shippingPrice from seller
                            //TODO: get colors
                            null, //txtColor.getText().toString(),
                            curUser.getEmail(), //sellerId from seller
                            null, //code from the item's id in firebase
                            null, //imageurl
                            null, //reservationId
                            null, //CostumerId
                            null //reservationDate
                    );

                    FirebaseHandler.uploadItemImageByteArray(AddproductActivity.this, prod.getProductId(), imageByteArray, new FirebaseHandler.ImageUrlCallBack() {
                        @Override
                        public void ImageUrlCallBack(String strImage) {
                            prod.setImageUrl(strImage);
                            addItem(prod);
                        }
                    });
            }
        }});
    }

    private boolean isValidData(View view){
        //TODO: add conditions to color
        if (txtName.getText().toString().trim().length() == 0 ||
                txtDescription.getText().toString().trim().length() == 0  ||
                category == "" ||
                txtPrice.getText().toString().trim().length() == 0  ||
                //color
                imageByteArray == null
                )
        {
            Snackbar.make(view, "One or more field missing", Snackbar.LENGTH_LONG)
                    .setAction("", null).show();
//                    Toast.makeText(getActivity(), "One or more field missing", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    public void addItem(final Product prod){
        FirebaseHandler.addItemToDataBase(AddproductActivity.this, prod, new FirebaseHandler.SuccessCallBack() {
            @Override
            public void SuccessCallBack(Boolean success) {
                if (success){
                    Utils.showAlertOk(AddproductActivity.this,"Add success","New product added!\n" +
                            "The product code is:\n" + prod.getCode(), new Utils.ShowAlert_CallBack() {
                        @Override
                        public void ShowAlert_UserClickedYes() {
                            //go to ProductmanagementActivity page
                            Intent intent = new Intent(AddproductActivity.this, ProductmanagementActivity.class);
                            startActivity(intent);
                        }
                    });
                }
                else{
                    Utils.showAlertOk(AddproductActivity.this,"Add Failed","Could not create new product");
                }
            }
        });
    }

    public void pickImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_PHOTO_FOR_AVATAR);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PHOTO_FOR_AVATAR && resultCode == Activity.RESULT_OK) {
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
