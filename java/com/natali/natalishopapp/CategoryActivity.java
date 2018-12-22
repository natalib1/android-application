//Displays the items of the category that has been choose at the main page

package com.natali.natalishopapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;

import com.natali.natalishopapp.FirebaseStorage.FirebaseHandler;
import com.natali.natalishopapp.Objects.Product;
import com.natali.natalishopapp.Utils.Constants;

import java.util.ArrayList;

public class CategoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

//        Bundle bundle = getIntent().getExtras();
        String cat = getIntent().getExtras().getString(Constants.CATEGORY);

        switch (cat) {
            case "bikini":
                toolbar.setTitle("Bikini");

                FirebaseHandler.getItemByCategory("bikini", new FirebaseHandler.ProdsArrCallBack() {
                    @Override
                    public void ProdsArrCallBack(ArrayList<Product> arrList) {
                        setTableAdapetr(arrList);
                    }
                });
                break;
            case "one-piece":
                toolbar.setTitle("One-Piece");

                FirebaseHandler.getItemByCategory("one-piece", new FirebaseHandler.ProdsArrCallBack() {
                    @Override
                    public void ProdsArrCallBack(ArrayList<Product> arrList) {
                        setTableAdapetr(arrList);
                    }
                });
                break;
            case "brazilian":
                toolbar.setTitle("Brazilian");

                FirebaseHandler.getItemByCategory("brazilian", new FirebaseHandler.ProdsArrCallBack() {
                    @Override
                    public void ProdsArrCallBack(ArrayList<Product> arrList) {
                        setTableAdapetr(arrList);
                    }
                });
                break;
            case "dresses":
                toolbar.setTitle("Dresses");

                FirebaseHandler.getItemByCategory("dresses", new FirebaseHandler.ProdsArrCallBack() {
                    @Override
                    public void ProdsArrCallBack(ArrayList<Product> arrList) {
                        setTableAdapetr(arrList);
                    }
                });
                break;
        }

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setTableAdapetr (final ArrayList<Product> arrList){
        //create a list of items to show in the category
        ProductListAdapter adapter = new ProductListAdapter(
                this, R.layout.list_item, arrList);
        ListView lv = (ListView) findViewById(R.id.listViewC);
        lv.setAdapter(adapter);

        //handle with the navigate to the product details after click on the item
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CategoryActivity.this, DetailActivity.class);
                intent.putExtra(Constants.PRODUCT_ID, arrList.get(position));

                startActivity(intent);
            }
        });
    }

}


