package com.exzy.smartgadgetforyou;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private Typeface akrobatBoldExtra;
    private Typeface heavenMedCond;

    private ViewGroup product_layout;
    private List<View> temp_product_layout;
    private List<View> product_layout_wearable;
    private List<View> product_layout_speaker;
    private List<View> product_layout_headset;
    private List<View> product_layout_drone;
    private List<View> product_layout_other;
    private List<Product> tempProductList;
    private HorizontalScrollView productSlide;
    private HorizontalScrollView categorySlide;
    private ImageView fadeTypeLeft;
    private ImageView fadeTypeRight;
    private TextView noneProductText;

    // identifier of new content image
    private int product_content_new;
    // identifier of hot content image
    private int product_content_hot;

    // dimension of each device
    float dimension;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        FullScreencall();
        setContentView(R.layout.activity_main);
        initComponents();
    }

    /**
     * Initial all components in this activity.
     */
    public void initComponents() {
        dimension = getResources().getDimension(R.dimen.one_dp);
        productSlide = (HorizontalScrollView) findViewById(R.id.productSlide);
        categorySlide = (HorizontalScrollView) findViewById(R.id.typeSlide);
        tempProductList = initProduct();
        product_layout = (ViewGroup) findViewById(R.id.product_layout);
        temp_product_layout = new ArrayList<View>();
        product_layout_wearable = new ArrayList<View>();
        product_layout_speaker = new ArrayList<View>();
        product_layout_headset = new ArrayList<View>();
        product_layout_drone = new ArrayList<View>();
        product_layout_other = new ArrayList<View>();
        noneProductText = (TextView) findViewById(R.id.none_product);

        // initial component of view
        for (int i = 0; i < initProduct().size(); i++) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            LinearLayout lr = new LinearLayout(this);
            lr.setOrientation(LinearLayout.VERTICAL);
            product_layout.addView(lr);
            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View newRow = layoutInflater.inflate(R.layout.gadget_recycler_view, null, false);
            ImageView product_img = (ImageView) newRow.findViewById(R.id.product_img);
            ImageView product_content_img = (ImageView) newRow.findViewById(R.id.product_content_img);
            TextView product_price = (TextView) newRow.findViewById(R.id.product_price);
            TextView product_name = (TextView) newRow.findViewById(R.id.product_name);
            TextView product_viewing = (TextView) newRow.findViewById(R.id.product_viewing);
            ImageView bg = (ImageView) newRow.findViewById(R.id.product_bg);
            ImageView shadow = (ImageView) newRow.findViewById(R.id.product_shadow);
            TextView product_content = (TextView) newRow.findViewById(R.id.product_content_text);

            if (i == 0 || i == initProduct().size() - 1) {
                product_viewing.setVisibility(View.INVISIBLE);
                bg.setVisibility(View.INVISIBLE);
                product_img.setVisibility(View.INVISIBLE);
                product_name.setVisibility(View.INVISIBLE);
                product_price.setVisibility(View.INVISIBLE);
                product_content_img.setVisibility(View.INVISIBLE);
                product_content.setVisibility(View.INVISIBLE);
                shadow.setVisibility(View.INVISIBLE);
                if (i == 0) {
                    params.setMargins((int) (-170 * dimension), 0, (int) (-16 * dimension), 0);
                } else {
                    params.setMargins((int) (-16 * dimension), 0, (int) (-165 * dimension), 0);
                }
                lr.setLayoutParams(params);
            } else {
                params.setMargins((int) (-10 * dimension), 0, (int) (-10 * dimension), 0);
                lr.setLayoutParams(params);
                product_name.setText(initProduct().get(i).getProduct_name());
                product_price.setText(initProduct().get(i).getProduct_price());
                product_img.setImageResource(initProduct().get(i).getProduct_img());
                if (initProduct().get(i).getContent() != 0) {
                    if (initProduct().get(i).getContent() == product_content_new) {
                        product_content.setText("NEW");
                    } else if (initProduct().get(i).getContent() == product_content_hot) {
                        product_content.setText("HOT");
                    }
                    product_content_img.setImageResource(initProduct().get(i).getContent());
                    product_content_img.setVisibility(View.VISIBLE);
                }
                Typeface akrobatBoldExtra = Typeface.createFromAsset(getAssets(), "fonts/akrobatboldextra.otf");
                Typeface heavenMedCond = Typeface.createFromAsset(getAssets(), "fonts/heaventmedcond.ttf");
                product_name.setTypeface(akrobatBoldExtra);
                product_price.setTypeface(heavenMedCond);
                product_viewing.setTypeface(akrobatBoldExtra);
                product_content.setTypeface(akrobatBoldExtra);
            }
            lr.addView(newRow);
        }

        // add action in each product
        addActionInProduct();

        // assign value of product in each type to a list
        addProductToEachCategory();

        // set typeface of each product
        akrobatBoldExtra = Typeface.createFromAsset(getAssets(), "fonts/akrobatboldextra.otf");
        TextView myTextView = (TextView) findViewById(R.id.mainLabel);
        myTextView.setTypeface(akrobatBoldExtra);

        heavenMedCond = Typeface.createFromAsset(getAssets(), "fonts/heaventmedcond.ttf");
        TextView myTextViewThai = (TextView) findViewById(R.id.thaiLabel);
        myTextViewThai.setTypeface(heavenMedCond);
        myTextViewThai.setText("กดเลือกแกตเจ็ตที่คุณสนใจได้เลย");
        noneProductText.setTypeface(heavenMedCond);

        // initial fade image product
        final ImageView fadeProductLeft = (ImageView) findViewById(R.id.fadeProductLeft);
        final ImageView fadeProductRight = (ImageView) findViewById(R.id.fadeProductRight);

        // Set default product fade
        setProductFade(fadeProductLeft, fadeProductRight);

        // initial product type
        final TextView wearableText = (TextView) findViewById(R.id.product_type_font_1);
        final ImageView wearableType = (ImageView) findViewById(R.id.product_type_1);
        final ImageView wearableBorder = (ImageView) findViewById(R.id.product_type_border_1);
        final TextView speakerText = (TextView) findViewById(R.id.product_type_font_2);
        final ImageView speakerType = (ImageView) findViewById(R.id.product_type_2);
        final ImageView speakerBorder = (ImageView) findViewById(R.id.product_type_border_2);
        final TextView headsetText = (TextView) findViewById(R.id.product_type_font_3);
        final ImageView headsetType = (ImageView) findViewById(R.id.product_type_3);
        final ImageView headseteBorder = (ImageView) findViewById(R.id.product_type_border_3);
        final TextView droneText = (TextView) findViewById(R.id.product_type_font_4);
        final ImageView droneType = (ImageView) findViewById(R.id.product_type_4);
        final ImageView droneBorder = (ImageView) findViewById(R.id.product_type_border_4);
        final TextView otherText = (TextView) findViewById(R.id.product_type_font_5);
        final ImageView otherType = (ImageView) findViewById(R.id.product_type_5);
        final ImageView otherBorder = (ImageView) findViewById(R.id.product_type_border_5);

        // set size typeface and action listener of each product type
        wearableText.setTypeface(akrobatBoldExtra);
        wearableType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (wearableBorder.getVisibility() == View.VISIBLE) {
                    if (temp_product_layout.size() < 3) {
                        noneProductText.setVisibility(View.VISIBLE);
                    } else {
                        noneProductText.setVisibility(View.INVISIBLE);
                    }
                    setProductFade(fadeProductLeft, fadeProductRight);
                    wearableText.setTextColor(Color.WHITE);
                    wearableBorder.setVisibility(View.INVISIBLE);
                    product_layout.removeAllViews();
                    for (int i = 0; i < temp_product_layout.size(); i++) {
                        product_layout.addView(temp_product_layout.get(i));
                    }
                    clearAllView();
                    addActionInProduct();
                    fadeProductRight.setVisibility(View.VISIBLE);
                } else {
                    if (product_layout_wearable.size() < 3) {
                        noneProductText.setVisibility(View.VISIBLE);
                    } else {
                        noneProductText.setVisibility(View.INVISIBLE);
                    }
                    final ObjectAnimator transAnimation = ObjectAnimator.ofInt(categorySlide, "scrollX", 0);
                    transAnimation.setDuration(400);
                    transAnimation.setInterpolator(new DecelerateInterpolator());
                    transAnimation.start();
                    ImageView fadeTypeLeft = (ImageView) findViewById(R.id.fadeTypeLeft);
                    ImageView fadeTypeRight = (ImageView) findViewById(R.id.fadeTypeRight);
                    fadeTypeRight.setAlpha(1.0f);
                    fadeTypeLeft.setAlpha(0.0f);
                    setProductFade(fadeProductLeft, fadeProductRight);
                    wearableText.setTextColor(Color.parseColor("#8bc53e"));
                    wearableBorder.setVisibility(View.VISIBLE);
                    product_layout.removeAllViews();
                    for (int i = 0; i < product_layout_wearable.size(); i++) {
                        product_layout.addView(product_layout_wearable.get(i));
                    }
                    clearAllView();
                    addActionInProduct();
                    if(product_layout_wearable.size() < 9) {
                        fadeProductRight.setVisibility(View.INVISIBLE);
                    }
                }

                speakerText.setTextColor(Color.WHITE);
                speakerBorder.setVisibility(View.INVISIBLE);
                headsetText.setTextColor(Color.WHITE);
                headseteBorder.setVisibility(View.INVISIBLE);
                otherText.setTextColor(Color.WHITE);
                otherBorder.setVisibility(View.INVISIBLE);
                droneText.setTextColor(Color.WHITE);
                droneBorder.setVisibility(View.INVISIBLE);
            }
        });

        speakerText.setTypeface(akrobatBoldExtra);
        speakerType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (speakerBorder.getVisibility() == View.VISIBLE) {
                    if (temp_product_layout.size() < 3) {
                        noneProductText.setVisibility(View.VISIBLE);
                    } else {
                        noneProductText.setVisibility(View.INVISIBLE);
                    }
                    speakerText.setTextColor(Color.WHITE);
                    speakerBorder.setVisibility(View.INVISIBLE);
                    product_layout.removeAllViews();
                    for (int i = 0; i < temp_product_layout.size(); i++) {
                        product_layout.addView(temp_product_layout.get(i));
                    }
                    clearAllView();
                    addActionInProduct();
                    fadeProductRight.setVisibility(View.VISIBLE);
                } else {
                    if (product_layout_speaker.size() < 3) {
                        noneProductText.setVisibility(View.VISIBLE);
                    } else {
                        noneProductText.setVisibility(View.INVISIBLE);
                    }
                    speakerText.setTextColor(Color.parseColor("#8bc53e"));
                    speakerBorder.setVisibility(View.VISIBLE);
                    product_layout.removeAllViews();
                    for (int i = 0; i < product_layout_speaker.size(); i++) {
                        product_layout.addView(product_layout_speaker.get(i));

                    }
                    clearAllView();
                    addActionInProduct();
                    if(product_layout_speaker.size() < 9) {
                        fadeProductRight.setVisibility(View.INVISIBLE);
                    }
                }
                wearableText.setTextColor(Color.WHITE);
                wearableBorder.setVisibility(View.INVISIBLE);
                headsetText.setTextColor(Color.WHITE);
                headseteBorder.setVisibility(View.INVISIBLE);
                otherText.setTextColor(Color.WHITE);
                otherBorder.setVisibility(View.INVISIBLE);
                droneText.setTextColor(Color.WHITE);
                droneBorder.setVisibility(View.INVISIBLE);
            }
        });

        headsetText.setTypeface(akrobatBoldExtra);
        headsetType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (headseteBorder.getVisibility() == View.VISIBLE) {
                    if (temp_product_layout.size() < 3) {
                        noneProductText.setVisibility(View.VISIBLE);
                    } else {
                        noneProductText.setVisibility(View.INVISIBLE);
                    }
                    headsetText.setTextColor(Color.WHITE);
                    headseteBorder.setVisibility(View.INVISIBLE);
                    product_layout.removeAllViews();
                    for (int i = 0; i < temp_product_layout.size(); i++) {
                        product_layout.addView(temp_product_layout.get(i));
                    }
                    clearAllView();
                    addActionInProduct();
                    fadeProductRight.setVisibility(View.VISIBLE);
                } else {
                    if (product_layout_headset.size() < 3) {
                        noneProductText.setVisibility(View.VISIBLE);
                    } else {
                        noneProductText.setVisibility(View.INVISIBLE);
                    }
                    headsetText.setTextColor(Color.parseColor("#8bc53e"));
                    headseteBorder.setVisibility(View.VISIBLE);
                    product_layout.removeAllViews();
                    for (int i = 0; i < product_layout_headset.size(); i++) {
                        product_layout.addView(product_layout_headset.get(i));
                    }
                    clearAllView();
                    addActionInProduct();
                    if(product_layout_headset.size() < 9) {
                        fadeProductRight.setVisibility(View.INVISIBLE);
                    }
                }
                speakerText.setTextColor(Color.WHITE);
                speakerBorder.setVisibility(View.INVISIBLE);
                wearableText.setTextColor(Color.WHITE);
                wearableBorder.setVisibility(View.INVISIBLE);
                otherText.setTextColor(Color.WHITE);
                otherBorder.setVisibility(View.INVISIBLE);
                droneText.setTextColor(Color.WHITE);
                droneBorder.setVisibility(View.INVISIBLE);
            }
        });

        otherText.setTypeface(akrobatBoldExtra);
        otherType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (otherBorder.getVisibility() == View.VISIBLE) {
                    if (temp_product_layout.size() < 3) {
                        noneProductText.setVisibility(View.VISIBLE);
                    } else {
                        noneProductText.setVisibility(View.INVISIBLE);
                    }
                    otherText.setTextColor(Color.WHITE);
                    otherBorder.setVisibility(View.INVISIBLE);
                    product_layout.removeAllViews();
                    for (int i = 0; i < temp_product_layout.size(); i++) {
                        product_layout.addView(temp_product_layout.get(i));
                    }
                    clearAllView();
                    addActionInProduct();
                    fadeProductRight.setVisibility(View.VISIBLE);
                } else {
                    if (product_layout_other.size() < 3) {
                        noneProductText.setVisibility(View.VISIBLE);
                    } else {
                        noneProductText.setVisibility(View.INVISIBLE);
                    }
                    int maxCategoryScroll = categorySlide.getChildAt(0).getMeasuredWidth() -
                            getWindowManager().getDefaultDisplay().getWidth();
                    final ObjectAnimator transAnimation = ObjectAnimator.ofInt(categorySlide, "scrollX", maxCategoryScroll);
                    transAnimation.setDuration(400);
                    transAnimation.setInterpolator(new DecelerateInterpolator());
                    transAnimation.start();
                    ImageView fadeTypeLeft = (ImageView) findViewById(R.id.fadeTypeLeft);
                    ImageView fadeTypeRight = (ImageView) findViewById(R.id.fadeTypeRight);
                    fadeTypeRight.setAlpha(0.0f);
                    fadeTypeLeft.setAlpha(1.0f);
                    otherText.setTextColor(Color.parseColor("#8bc53e"));
                    otherBorder.setVisibility(View.VISIBLE);
                    product_layout.removeAllViews();
                    for (int i = 0; i < product_layout_other.size(); i++) {
                        product_layout.addView(product_layout_other.get(i));
                    }
                    clearAllView();
                    addActionInProduct();
                    if(product_layout_other.size() < 9) {
                        fadeProductRight.setVisibility(View.INVISIBLE);
                    }
                }
                speakerText.setTextColor(Color.WHITE);
                speakerBorder.setVisibility(View.INVISIBLE);
                headsetText.setTextColor(Color.WHITE);
                headseteBorder.setVisibility(View.INVISIBLE);
                wearableText.setTextColor(Color.WHITE);
                wearableBorder.setVisibility(View.INVISIBLE);
                droneText.setTextColor(Color.WHITE);
                droneBorder.setVisibility(View.INVISIBLE);
            }
        });

        droneText.setTypeface(akrobatBoldExtra);
        droneType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (droneBorder.getVisibility() == View.VISIBLE) {
                    if (temp_product_layout.size() < 3) {
                        noneProductText.setVisibility(View.VISIBLE);
                    } else {
                        noneProductText.setVisibility(View.INVISIBLE);
                    }
                    droneText.setTextColor(Color.WHITE);
                    droneBorder.setVisibility(View.INVISIBLE);
                    product_layout.removeAllViews();
                    for (int i = 0; i < temp_product_layout.size(); i++) {
                        product_layout.addView(temp_product_layout.get(i));
                    }
                    clearAllView();
                    addActionInProduct();
                    addActionInProduct();
                    fadeProductRight.setVisibility(View.VISIBLE);
                } else {
                    if (product_layout_drone.size() < 3) {
                        noneProductText.setVisibility(View.VISIBLE);
                    } else {
                        noneProductText.setVisibility(View.INVISIBLE);
                    }
                    droneText.setTextColor(Color.parseColor("#8bc53e"));
                    droneBorder.setVisibility(View.VISIBLE);
                    product_layout.removeAllViews();
                    for (int i = 0; i < product_layout_drone.size(); i++) {
                        product_layout.addView(product_layout_drone.get(i));
                    }
                    clearAllView();
                    addActionInProduct();
                    if(product_layout_drone.size() < 9) {
                        fadeProductRight.setVisibility(View.INVISIBLE);
                    }
                }
                speakerText.setTextColor(Color.WHITE);
                speakerBorder.setVisibility(View.INVISIBLE);
                headsetText.setTextColor(Color.WHITE);
                headseteBorder.setVisibility(View.INVISIBLE);
                otherText.setTextColor(Color.WHITE);
                otherBorder.setVisibility(View.INVISIBLE);
                wearableText.setTextColor(Color.WHITE);
                wearableBorder.setVisibility(View.INVISIBLE);
            }
        });

        setCategoryFade();

        /** use to test get getScrollX of productSlide */
//        productSlide.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                Log.i("Scrolling", productSlide.getScrollX()+"");
//                Log.e("pixel",getResources().getDimension(R.dimen.one_dp)+"");
//                Log.e("pixel",productSlide.getMaxScrollAmount()+"");
//                return false;
//            }
//        });

        /** use to test get getScrollX of categorySlide */
//        categorySlide.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                Log.i("Scrolling", categorySlide.getScrollX()+"");
//                Log.i("ScrollingMax", categorySlide.getWidth()+"");
//                Log.e("ScrollWidth",Integer.toString(categorySlide.getChildAt(0).getMeasuredWidth()-
//                        getWindowManager().getDefaultDisplay().getWidth()));

//                Log.e("pixel",getResources().getDimension(R.dimen.one_dp)+"");
//                Log.e("pixel",categorySlide.getMaxScrollAmount()+"");
//                return false;
//            }
//        });

    }

    /**
     * Set full screen.
     */
    public void FullScreencall() {
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    /**
     * Initial all product
     *
     * @return list of all product in application
     */
    private List<Product> initProduct() {
        int product_img_id = getResources().getIdentifier("wear_forerunner235", "mipmap", getPackageName());
        product_content_new = getResources().getIdentifier("new_tag", "mipmap", getPackageName());
        product_content_hot = getResources().getIdentifier("hot_tag", "mipmap", getPackageName());

        Product emptyItem = new Product("", "", 0, 0, "");

        Product forerunner235 = new Product("forerunner 235", "12,990", product_img_id, product_content_new, "wearable");

        product_img_id = getResources().getIdentifier("wear_forerunner35", "mipmap", getPackageName());
        Product forerunner35 = new Product("forerunner 35", "12,990", product_img_id, product_content_hot, "wearable");

        product_img_id = getResources().getIdentifier("forerunner_735xt", "mipmap", getPackageName());
        Product forerunner735xt = new Product("forerunner 735XT", "12,990", product_img_id, 0, "wearable");

        product_img_id = getResources().getIdentifier("garmin_vivosmart_hrplus", "mipmap", getPackageName());
        Product vivosmart_hrplus = new Product("vivosmart HR+", "12,990", product_img_id, 0, "wearable");

        product_img_id = getResources().getIdentifier("vivoactive_hr", "mipmap", getPackageName());
        Product vivoactive_hr = new Product("vivoactive HR", "12,990", product_img_id, 0, "wearable");

        product_img_id = getResources().getIdentifier("fenix5s", "mipmap", getPackageName());
        Product fenix5s = new Product("fenix 5S", "12,990", product_img_id, 0, "wearable");

        product_img_id = getResources().getIdentifier("garmin_fenix5", "mipmap", getPackageName());
        Product fenix5 = new Product("fenix 5", "12,990", product_img_id, 0, "wearable");

        product_img_id = getResources().getIdentifier("garmin_fenix5x", "mipmap", getPackageName());
        Product fenix5x = new Product("fenix 5X", "12,990", product_img_id, 0, "wearable");

        product_img_id = getResources().getIdentifier("pomo_waffle_black", "mipmap", getPackageName());
        Product pomowaffle = new Product("POMO Waffle", "5,990", product_img_id, 0, "wearable");

        product_img_id = getResources().getIdentifier("beoplay_a1", "mipmap", getPackageName());
        Product beoplayA1 = new Product("Beoplay A1", "10,500", product_img_id, 0, "speaker");

        product_img_id = getResources().getIdentifier("beoplay_a2", "mipmap", getPackageName());
        Product beoplayA2 = new Product("Beoplay A2", "16,500", product_img_id, 0, "speaker");

        product_img_id = getResources().getIdentifier("onyx_mini", "mipmap", getPackageName());
        Product onyx_mini = new Product("ONYX MINI", "5,990", product_img_id, 0, "speaker");

        product_img_id = getResources().getIdentifier("esquire2", "mipmap", getPackageName());
        Product esquire2 = new Product("ESQUIRE 2", "8,990", product_img_id, 0, "speaker");

        product_img_id = getResources().getIdentifier("beoplay_h9", "mipmap", getPackageName());
        Product beoplayH9 = new Product("Beoplay H9", "20,500", product_img_id, 0, "headset");

        product_img_id = getResources().getIdentifier("dobby_pocket_drone", "mipmap", getPackageName());
        Product dobbyPocketDrone = new Product("DOBBY POCKET DRONE", "15,500", product_img_id, 0, "drone");

        List<Product> dataset = new ArrayList<Product>();
        dataset.add(emptyItem);
        // size of wearable is 9
        dataset.add(forerunner235);
        dataset.add(forerunner35);
        dataset.add(forerunner735xt);
        dataset.add(vivoactive_hr);
        dataset.add(vivosmart_hrplus);
        dataset.add(fenix5s);
        dataset.add(fenix5);
        dataset.add(fenix5x);
        dataset.add(pomowaffle);
        // size of speaker is 4
        dataset.add(beoplayA1);
        dataset.add(beoplayA2);
        dataset.add(onyx_mini);
        dataset.add(esquire2);
        // size of headset is 1
        dataset.add(beoplayH9);
        // size of drone is 1
        dataset.add(dobbyPocketDrone);
        dataset.add(emptyItem);

        return dataset;
    }

    /**
     * add action in each product
     */
    public void addActionInProduct() {
        for (int i = 0; i < product_layout.getChildCount(); i++) {
            Log.e("count", product_layout.getChildCount() + "");
            final int tempIndex = i;
            final ImageView product_img = (ImageView) product_layout.getChildAt(i).findViewById(R.id.product_img);
            final ImageView product_content_img = (ImageView) product_layout.getChildAt(i).findViewById(R.id.product_content_img);
            final TextView product_price = (TextView) product_layout.getChildAt(i).findViewById(R.id.product_price);
            final TextView product_name = (TextView) product_layout.getChildAt(i).findViewById(R.id.product_name);
            final TextView product_viewing = (TextView) product_layout.getChildAt(i).findViewById(R.id.product_viewing);
            final ImageView bg = (ImageView) product_layout.getChildAt(i).findViewById(R.id.product_bg);
            final ImageView shadow = (ImageView) product_layout.getChildAt(i).findViewById(R.id.product_shadow);
            final TextView product_content = (TextView) product_layout.getChildAt(i).findViewById(R.id.product_content_text);

            product_layout.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("press index", tempIndex + "");
                    Rect rect = new Rect();
                    if (product_layout.getChildAt(tempIndex).getGlobalVisibleRect(rect)
                            && product_layout.getChildAt(tempIndex).getHeight() == rect.height()
                            && product_layout.getChildAt(tempIndex).getWidth() == rect.width()) {
                        if (tempIndex != 0 && tempIndex != product_layout.getChildCount() - 1) {
                            for (int i = 0; i < product_layout.getChildCount(); i++) {
                                if (i != tempIndex) {
                                    ImageView product_img = (ImageView) product_layout.getChildAt(i).findViewById(R.id.product_img);
                                    ImageView product_content_img = (ImageView) product_layout.getChildAt(i).findViewById(R.id.product_content_img);
                                    TextView product_price = (TextView) product_layout.getChildAt(i).findViewById(R.id.product_price);
                                    TextView product_name = (TextView) product_layout.getChildAt(i).findViewById(R.id.product_name);
                                    TextView product_viewing = (TextView) product_layout.getChildAt(i).findViewById(R.id.product_viewing);
                                    ImageView bg = (ImageView) product_layout.getChildAt(i).findViewById(R.id.product_bg);
                                    ImageView shadow = (ImageView) product_layout.getChildAt(i).findViewById(R.id.product_shadow);
                                    TextView product_content = (TextView) product_layout.getChildAt(i).findViewById(R.id.product_content_text);
                                    product_viewing.setVisibility(View.INVISIBLE);
                                    bg.setAlpha((float) 1.0);
                                    product_img.setAlpha((float) 1.0);
                                    product_name.setAlpha((float) 1.0);
                                    product_price.setAlpha((float) 1.0);
                                    product_content_img.setAlpha((float) 1.0);
                                    product_content.setAlpha((float) 1.0);
                                    shadow.setAlpha((float) 1.0);
                                    tempProductList.get(i).setClick(false);
                                }
                            }
                            if (!tempProductList.get(tempIndex).isClicked()) {
                                imgClick();
                            } else {
                                viewingClick();
                            }
                        }
                    } else {
                        Log.e("press", product_layout.getChildAt(tempIndex).getLeft() + "");
//                        Log.e("getLeft", productSlide.getScrollX() + "");
                        if (product_layout.getChildAt(tempIndex).getLeft() > productSlide.getScrollX()) {
                            productSlide.smoothScrollTo(((int) ((productSlide.getScrollX() / (int) (185 * dimension)) * 200 * dimension) + (int) ((productSlide.getScrollX() / 185 * dimension) * 1.1)) + (int) (221 * dimension * 0.9), 0);
                        } else {
                            productSlide.smoothScrollTo(((int) ((productSlide.getScrollX() / (int) (185 * dimension)) * 200 * dimension) + (int) ((productSlide.getScrollX() / 185 * dimension) * 1.1)) - (int) (230 * dimension * 0.9), 0);
                        }
                    }
                }

                public void imgClick() {
                    product_viewing.setVisibility(View.VISIBLE);
                    bg.setAlpha((float) 0.5);
                    product_img.setAlpha((float) 0.3);
                    product_name.setAlpha((float) 0.3);
                    product_price.setAlpha((float) 0.3);
                    product_content_img.setAlpha((float) 0.3);
                    product_content.setAlpha((float) 0.3);
                    shadow.setAlpha((float) 0.3);
                    product_viewing.setAlpha((float) 0.9);
                    tempProductList.get(tempIndex).setClick(true);
                }

                public void viewingClick() {
                    product_viewing.setVisibility(View.INVISIBLE);
                    bg.setAlpha((float) 1.0);
                    product_img.setAlpha((float) 1.0);
                    product_name.setAlpha((float) 1.0);
                    product_price.setAlpha((float) 1.0);
                    product_content_img.setAlpha((float) 1.0);
                    product_content.setAlpha((float) 1.0);
                    shadow.setAlpha((float) 1.0);
                    tempProductList.get(tempIndex).setClick(false);
                }
            });
        }
    }

    /**
     * Clear viewing every product.
     */
    public void clearAllView() {
        for (int i = 0; i < tempProductList.size(); i++) {
            if (tempProductList.get(i).isClicked()) {
                tempProductList.get(i).setClick(false);
            }
        }
        for (int i = 0; i < product_layout.getChildCount(); i++) {
            ImageView product_img = (ImageView) product_layout.getChildAt(i).findViewById(R.id.product_img);
            ImageView product_content_img = (ImageView) product_layout.getChildAt(i).findViewById(R.id.product_content_img);
            TextView product_price = (TextView) product_layout.getChildAt(i).findViewById(R.id.product_price);
            TextView product_name = (TextView) product_layout.getChildAt(i).findViewById(R.id.product_name);
            TextView product_viewing = (TextView) product_layout.getChildAt(i).findViewById(R.id.product_viewing);
            ImageView bg = (ImageView) product_layout.getChildAt(i).findViewById(R.id.product_bg);
            ImageView shadow = (ImageView) product_layout.getChildAt(i).findViewById(R.id.product_shadow);
            TextView product_content = (TextView) product_layout.getChildAt(i).findViewById(R.id.product_content_text);
            product_viewing.setVisibility(View.INVISIBLE);
            bg.setAlpha((float) 1.0);
            product_img.setAlpha((float) 1.0);
            product_name.setAlpha((float) 1.0);
            product_price.setAlpha((float) 1.0);
            product_content_img.setAlpha((float) 1.0);
            product_content.setAlpha((float) 1.0);
            shadow.setAlpha((float) 1.0);
        }
    }

    /**
     * Add view from product_layout to list of each category.
     */
    public void addProductToEachCategory() {
        for (int i = 0; i < product_layout.getChildCount(); i++) {
            temp_product_layout.add(product_layout.getChildAt(i));
            if (i == 0 || tempProductList.get(i).getCategory().equalsIgnoreCase("wearable") || i == product_layout.getChildCount() - 1) {
                product_layout_wearable.add(product_layout.getChildAt(i));
            }
            if (i == 0 || tempProductList.get(i).getCategory().equalsIgnoreCase("speaker") || i == product_layout.getChildCount() - 1) {
                product_layout_speaker.add(product_layout.getChildAt(i));
            }
            if (i == 0 || tempProductList.get(i).getCategory().equalsIgnoreCase("headset") || i == product_layout.getChildCount() - 1) {
                product_layout_headset.add(product_layout.getChildAt(i));
            }
            if (i == 0 || tempProductList.get(i).getCategory().equalsIgnoreCase("other") || i == product_layout.getChildCount() - 1) {
                product_layout_other.add(product_layout.getChildAt(i));
            }
            if (i == 0 || tempProductList.get(i).getCategory().equalsIgnoreCase("drone") || i == product_layout.getChildCount() - 1) {
                product_layout_drone.add(product_layout.getChildAt(i));
            }
        }
    }

    /**
     * Set distance to fade image in ScrollView of category
     */
    public void setCategoryFade() {
        // fade category type
        fadeTypeLeft = (ImageView) findViewById(R.id.fadeTypeLeft);
        fadeTypeRight = (ImageView) findViewById(R.id.fadeTypeRight);

        final ViewTreeObserver.OnScrollChangedListener onScrollChangedListener = new
                ViewTreeObserver.OnScrollChangedListener() {

                    @Override
                    public void onScrollChanged() {
                        int maxCategoryScroll = categorySlide.getChildAt(0).getMeasuredWidth() -
                                getWindowManager().getDefaultDisplay().getWidth();

                        if (categorySlide.getScrollX() <= 20) {
                            fadeTypeLeft.setAlpha(categorySlide.getScrollX() / 20f);
                        } else {
                            fadeTypeLeft.setAlpha(1f);
                        }
                        if (categorySlide.getScrollX() <= maxCategoryScroll && categorySlide.getScrollX() >= maxCategoryScroll - 20) {
                            fadeTypeRight.setAlpha((maxCategoryScroll - categorySlide.getScrollX()) / 20f);
                        } else {
                            fadeTypeRight.setAlpha(1f);
                        }
                    }
                };

        categorySlide.setOnTouchListener(new View.OnTouchListener() {
            private ViewTreeObserver observer;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (observer == null) {
                    observer = categorySlide.getViewTreeObserver();
                    observer.addOnScrollChangedListener(onScrollChangedListener);
                } else if (!observer.isAlive()) {
                    observer.removeOnScrollChangedListener(onScrollChangedListener);
                    observer = categorySlide.getViewTreeObserver();
                    observer.addOnScrollChangedListener(onScrollChangedListener);
                }

                return false;
            }
        });
    }

    /**
     * @param fadeProductLeft  is fade image of left side in HorizontalScrollView
     * @param fadeProductRight is fade image of right side in HorizontalScrollView
     */
    public void setProductFade(final ImageView fadeProductLeft, final ImageView fadeProductRight) {

        //fade product type
        final ViewTreeObserver.OnScrollChangedListener onScrollChangedListener = new
                ViewTreeObserver.OnScrollChangedListener() {

                    @Override
                    public void onScrollChanged() {
                        int maxProductScroll = productSlide.getChildAt(0).getMeasuredWidth() -
                                getWindowManager().getDefaultDisplay().getWidth();

                        if (productSlide.getScrollX() <= 60 * dimension) {
                            fadeProductLeft.setAlpha(productSlide.getScrollX() / 60f * dimension);
                        } else {
                            fadeProductLeft.setAlpha(1f);
                        }
                        if (productSlide.getScrollX() <= maxProductScroll && productSlide.getScrollX() >= maxProductScroll - 60 * dimension) {
                            fadeProductRight.setAlpha((maxProductScroll - productSlide.getScrollX()) / 60f * dimension);
                        } else {
                            fadeProductRight.setAlpha(1f);
                        }
                    }
                };

        // Set action listener of HorizontalScrollView
        productSlide.setOnTouchListener(new View.OnTouchListener() {
            private ViewTreeObserver observer;
            Timer timer;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (observer == null) {
                    observer = productSlide.getViewTreeObserver();
                    observer.addOnScrollChangedListener(onScrollChangedListener);
                } else if (!observer.isAlive()) {
                    observer.removeOnScrollChangedListener(onScrollChangedListener);
                    observer = productSlide.getViewTreeObserver();
                    observer.addOnScrollChangedListener(onScrollChangedListener);
                }
                if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
//                    Log.e("down :", "yes");
                    if (timer != null) {
//                        Log.e("timer :", "!null");
                        timer.cancel();
                        timer.purge();
                        timer = null;
                    }
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
//                    Log.e("up :", "yes");
                    if (timer == null) {
//                        Log.e("timer :", "null");
                        timer = new Timer();
                        timer.schedule(new TimerTask() {

                            @Override
                            public void run() {
                                if (productSlide.getScrollX() < 138 * dimension) {
                                    productSlide.smoothScrollTo(0, 0);
                                } else {
                                    productSlide.smoothScrollTo((int) ((productSlide.getScrollX() / (int) (185 * dimension)) * 200 * dimension) + (int) ((productSlide.getScrollX() / 185 * dimension) * 1.1), 0);
//                                    Log.i("scrollX", (int) ((productSlide.getScrollX() / (int) (170 * dimension)) * 200 * dimension) + (int) ((productSlide.getScrollX() / 170 * dimension) * 1.1) + "");
                                }
                            }
                        }, 1000);
                    }
                }

                return false;
            }
        });
    }
}
