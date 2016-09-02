package com.example.hly.attrdemo;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by hly on 9/1/16.
 */
public class NavigationBar extends RelativeLayout {
    private static final String TAG = "NavigationBar";
    private ImageView leftImage;
    private LeftImageClickListner mLeftImageClickListner;
    private RightImageClickListner mRightImageClickListner;

    public void setOnLeftImageClickListner(LeftImageClickListner l) {
        mLeftImageClickListner = l;
    }

    public void setOnRightImageClickListner(RightImageClickListner l) {
        mRightImageClickListner = l;
    }

    public interface LeftImageClickListner{
        void naviBarLeftImageClicked();
    }

    public interface RightImageClickListner{
        void naviBarRightImageClicked();
    }

    public NavigationBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    private void testAttrs(Context context, AttributeSet attrs) {
        int attrsCount = attrs.getAttributeCount();
        Log.i(TAG, "AttributeSet size :" + attrsCount);
        for (int i = 0; i < attrsCount; i++) {
            String name = attrs.getAttributeName(i);
            String value = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto", name);
            if (TextUtils.isEmpty(value)) {
                value = attrs.getAttributeValue("http://schemas.android.com/apk/res/android", name);
            }

            if (name.equals("leftImage")) {
                int id = attrs.getAttributeResourceValue("http://schemas.android.com/apk/res-auto", "leftImage", -1);
                Drawable drawable = context.getResources().getDrawable(id);
                Log.i(TAG, "drawable:" + drawable);
            }
            Log.i(TAG, "Attribute name :" + name + " Attribute value:" + value );
        }
    }

    private void testFlag(TypedArray typedArray) {
        int flagValue = typedArray.getInt(R.styleable.NaviBar_flagTest, -1);
        int enumValue = typedArray.getInt(R.styleable.NaviBar_enumTest, -1);
        Log.i(TAG, "Attribute flagValue :" + flagValue + " enumValue:" + enumValue);
    }

    public NavigationBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        testAttrs(context, attrs);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.NaviBar);
        testFlag(typedArray);

        //left imageview
        Drawable leftDrawable =  typedArray.getDrawable(R.styleable.NaviBar_leftImage);
        if (leftDrawable != null) {
            leftImage = new ImageView(context);
            leftImage.setId(R.id.navibar_left_img);
            leftImage.setImageDrawable(leftDrawable);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
            lp.addRule(RelativeLayout.CENTER_VERTICAL);
            addView(leftImage,lp);

            leftImage.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mLeftImageClickListner != null) {
                        mLeftImageClickListner.naviBarLeftImageClicked();
                    }
                }
            });
        }

        //left textview
        String leftString =  typedArray.getString(R.styleable.NaviBar_leftText);
        if (leftString != null) {
            TextView tv = new TextView(context);
            tv.setId(R.id.navibar_left_tv);
            tv.setText(leftString);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
            lp.addRule(RelativeLayout.CENTER_VERTICAL);
            if (leftImage != null) {
                lp.addRule(RelativeLayout.RIGHT_OF, leftImage.getId());
            }

            int marginleft = (int) typedArray.getDimension(R.styleable.NaviBar_leftTextMarginLeft, 0);
            lp.setMargins(marginleft, 0, 0, 0);

            float size = typedArray.getDimension(R.styleable.NaviBar_leftTextSize, 24);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);

            int color = typedArray.getColor(R.styleable.NaviBar_leftTextColor, context.getResources().getColor(R.color.half_transparent));
            tv.setTextColor(color);

            tv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mLeftImageClickListner != null) {
                        mLeftImageClickListner.naviBarLeftImageClicked();
                    }
                }
            });

            addView(tv,lp);
        }

        //right imageview
        Drawable rightDrawable =  typedArray.getDrawable(R.styleable.NaviBar_rightImage);
        if (rightDrawable != null) {
            ImageView rightImage = new ImageView(context);
            rightImage.setId(R.id.navibar_right_img);
            rightImage.setImageDrawable(rightDrawable);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
            lp.addRule(RelativeLayout.CENTER_VERTICAL);
            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

            rightImage.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mRightImageClickListner != null) {
                        mRightImageClickListner.naviBarRightImageClicked();
                    }
                }
            });
            addView(rightImage,lp);
        }

        typedArray.recycle();
    }




}
