package com.example.clothes.Product;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.clothes.DataLogin;
import com.example.clothes.Model.Cart;
import com.example.clothes.Model.Product;
import com.example.clothes.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class ProductDetailsActivity extends AppCompatActivity {
    private String userId = DataLogin.id1;
    private String productId;

    private TextView textViewProductName, textViewProductPrice, textViewDescription, textViewCategory,
            ViewMaterial, textViewOrigin;
    private View viewColor;
    private EditText editTextQuantity;

    private ImageView imageViewProduct;
    private Button buttonDecreaseProduct, buttonIncreaseProduct,  buttonAddToCart;
    private RadioGroup sizeGroup;
    RadioButton M, L, XL, XXL;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        //Lấy id của sản phẩm đã chọn
        Bundle extras = getIntent().getExtras();
        String Id_details = extras.getString("Id_details");

        //Ánh xạ
        LinkDetails();

        //Lấy nhánh sản phẩm tương ứng
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("product");
        Query query = reference.orderByChild("id").equalTo(Id_details);

        //Xử lý dữ liệu và show
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Lấy DataSnapshot đầu tiên từ kết quả truy vấn.
                DataSnapshot childSnapshot = snapshot.getChildren().iterator().next();
                Product product = childSnapshot.getValue(Product.class);

                DatabaseReference prSizeRef = FirebaseDatabase.getInstance().getReference("productSize/"+product.getId());
                prSizeRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        int qM = 0, qL=0, qXL=0, qXXL=0;
                        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                            String key = childSnapshot.getKey();
                            int value = childSnapshot.getValue(Integer.class);
                            switch (key) {
                                case "M":
                                    qM = value;
                                    break;
                                case "L":
                                    qL = value;
                                    break;
                                case "XL":
                                    qXL = value;
                                    break;
                                case "XXL":
                                    qXXL = value;
                                    break;
                                default:
                                    break;
                            }
                        }
                        //Mở các size có số lượng khác 0
                        M.setEnabled(qM != 0);
                        L.setEnabled(qL != 0);
                        XL.setEnabled(qXL != 0);
                        XXL.setEnabled(qXXL != 0);

                        //Show dữ liệu
                        textViewProductName.setText(product.getName());
                        textViewProductPrice.setText(String.valueOf(product.getPrice()));
                        Glide.with(ProductDetailsActivity.this).load(product.getImage()).into(imageViewProduct);
                        textViewDescription.setText("   " + product.getDescription());
                        textViewCategory.setText(product.getCategory());
//                viewColor.setBackgroundColor(Color.parseColor(product.getColor())); //màu
                        ViewMaterial.setText(product.getMaterial());
                        textViewOrigin.setText(product.getOrigin());

                        int finalQM = qM;
                        int finalQL = qL;
                        int finalQXL = qXL;
                        int finalQXXL = qXXL;
                        buttonAddToCart.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                boolean isAnySelected = sizeGroup.getCheckedRadioButtonId() !=-1;
                                if(isAnySelected){
                                    AlertDialog.Builder builder = new AlertDialog.Builder(ProductDetailsActivity.this);
                                    builder.setTitle("Add to Cart");
                                    builder.setMessage("Would you like to add this product to your cart?");
                                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String productId = product.getId();
                                            //Lấy size được chọn
                                            int selectedSizeId = sizeGroup.getCheckedRadioButtonId();
                                            RadioButton selectedRadioButton = findViewById(selectedSizeId);
                                            String size = selectedRadioButton.getText().toString();

                                            int top = 0;
                                            switch (size) {
                                                case "M":
                                                    top = finalQM;
                                                    break;
                                                case "L":
                                                    top = finalQL;
                                                    break;
                                                case "XL":
                                                    top = finalQXL;
                                                    break;
                                                case "XXL":
                                                    top = finalQXXL;
                                                    break;
                                                default:
                                                    break;
                                            }
                                            String quantityString = editTextQuantity.getText().toString();
                                            int quantity = 1;
                                            if(!quantityString.isEmpty()){
                                                try {
                                                    quantity  = Integer.parseInt(quantityString);
                                                    if(quantity>0 && quantity<=top){
                                                        // Tạo node cart mới
                                                        DatabaseReference cartReference = database.getReference("cart");
                                                        Query usQuery = cartReference.orderByChild("user_id").equalTo(userId);

                                                        int finalQuantity = quantity;
                                                        int finalTop = top;
                                                        //Kiểm tra node trùng user_id, product_id, size
                                                        usQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                DatabaseReference cartRef = database.getReference("cart").push();

                                                                //Kiểm tra trùng product_id và size
                                                                for(DataSnapshot cartSnapshot : snapshot.getChildren()) {
                                                                    Cart existingCart = cartSnapshot.getValue(Cart.class);
                                                                    if(existingCart != null && existingCart.getProduct_id().equals(productId)
                                                                            && existingCart.getSize().equals(size)){

                                                                        int newQuantity = existingCart.getQuantity() + finalQuantity;
                                                                        if(newQuantity> finalTop) newQuantity = finalTop;
                                                                        existingCart.setQuantity(newQuantity);
                                                                        cartSnapshot.getRef().setValue(existingCart);

                                                                        Toast.makeText(ProductDetailsActivity.this, "Added to existing cart", Toast.LENGTH_LONG).show();
                                                                        return;
                                                                    }
                                                                }

                                                                //Nếu không có trùng thì thêm nhánh mới
                                                                // Gán dữ liệu cho node cart
                                                                //Nếu số lượng lớn hơn limit->sl = limit

                                                                Cart cart = new Cart(userId, productId , finalQuantity);
                                                                cart.setSize(size);
                                                                cartRef.setValue(cart);
                                                                Toast.makeText(ProductDetailsActivity.this, "Added new cart", Toast.LENGTH_LONG).show();

                                                            }
                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError error) {

                                                            }
                                                        });

//                                                Toast.makeText(ProductDetailsActivity.this, "Added product to cart with quantity: "+ quantity, Toast.LENGTH_LONG).show();
                                                    } else if(quantity<=0){
                                                        Toast.makeText(ProductDetailsActivity.this, "Product quantity must be more than 0! Value: "+ quantityString, Toast.LENGTH_LONG).show();
                                                    }else{
                                                        Toast.makeText(ProductDetailsActivity.this, "Quantity must be appropriate! Value: "+ quantityString, Toast.LENGTH_LONG).show();
                                                    }
                                                } catch (NumberFormatException e) {
                                                    Toast.makeText(ProductDetailsActivity.this, "Invalid quantity format! Value: " + quantityString, Toast.LENGTH_LONG).show();
                                                }
                                            }
                                            else{
                                                Toast.makeText(ProductDetailsActivity.this, "Quantity can't be null!" + quantityString, Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();

                                        }
                                    });
                                    AlertDialog dialog = builder.create();
                                    dialog.show();
                                } else {
                                    Toast.makeText(ProductDetailsActivity.this, "Need choose size first!", Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                        buttonDecreaseProduct.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                int currentQuantity = Integer.parseInt(editTextQuantity.getText().toString());
                                if (currentQuantity > 1) {
                                    editTextQuantity.setText(String.valueOf(currentQuantity - 1));
                                }
                            }
                        });


                        buttonIncreaseProduct.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                boolean isAnySelected = sizeGroup.getCheckedRadioButtonId() !=-1;
                                if(isAnySelected){
                                    sizeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                        @Override
                                        public void onCheckedChanged(RadioGroup group, int checkedId) {
                                            editTextQuantity.setText(String.valueOf(1));
                                        }
                                    });
                                    EditText editTextQuantity = findViewById(R.id.editTextQuantity);
                                    int currentQuantity = Integer.parseInt(editTextQuantity.getText().toString());
                                    //Lấy size được chọn
                                    int selectedSizeId = sizeGroup.getCheckedRadioButtonId();
                                    RadioButton selectedRadioButton = findViewById(selectedSizeId);
                                    String size = selectedRadioButton.getText().toString();
                                    int top = 0;
                                    switch (size) {
                                        case "M":
                                            top = finalQM;
                                            break;
                                        case "L":
                                            top = finalQL;
                                            break;
                                        case "XL":
                                            top = finalQXL;
                                            break;
                                        case "XXL":
                                            top = finalQXXL;
                                            break;
                                        default:
                                            break;
                                    }
                                    if(currentQuantity < top){
                                        editTextQuantity.setText(String.valueOf(currentQuantity + 1));
                                    }else{
                                        Toast.makeText(ProductDetailsActivity.this, "At limited!", Toast.LENGTH_LONG).show();
                                    }
                                }else{
                                    Toast.makeText(ProductDetailsActivity.this, "You have to select size first!", Toast.LENGTH_LONG).show();
                                }

                                
                            }
                        });


                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

    }

    private void LinkDetails() {
        //Thông tin sản phẩm
        textViewProductName = findViewById(R.id.textViewProductName);
        textViewProductPrice = findViewById(R.id.textViewProductPrice);
        imageViewProduct = findViewById(R.id.imageViewProduct);
        textViewDescription = findViewById(R.id.textViewDescription);
        textViewCategory = findViewById(R.id.textViewCategory);
        ViewMaterial = findViewById(R.id.ViewMaterial);
        viewColor = findViewById(R.id.viewColor);
        textViewOrigin = findViewById(R.id.textViewOrigin);
        editTextQuantity = findViewById(R.id.editTextQuantity);
        //nút tăng giảm số lượng
        buttonDecreaseProduct = findViewById(R.id.buttonDecreaseProduct);
        buttonIncreaseProduct = findViewById(R.id.buttonIncreaseProduct);
        //nút thêm vào giỏ hàng
        buttonAddToCart = findViewById(R.id.buttonAddToCart);
        sizeGroup = findViewById(R.id.sizeGroup);
        M = findViewById(R.id.M);
        L = findViewById(R.id.L);
        XL = findViewById(R.id.XL);
        XXL = findViewById(R.id.XXL);
    }
}
