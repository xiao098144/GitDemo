package com.demo.xiao;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;

public class MainActivity extends Activity {

	CordinateSurface cordinateSurface;
	int width;
	int height;
	List<Float> values = new ArrayList<Float>();

	Button btn1;
	Button btn2;
	Button btn3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
		width = outMetrics.widthPixels;
		height = outMetrics.heightPixels;
		setContentView(R.layout.activity_main);

		cordinateSurface = (CordinateSurface) findViewById(R.id.cordinate);
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
				height / 5 * 2 + 20);
		params.setMargins(8, 8, 8, 8);
		cordinateSurface.setLayoutParams(params);
		values = generate();
		draw(6.1f, 4.3f);
		
		
		
		btn1 = (Button) findViewById(R.id.btn1);
		btn2 = (Button) findViewById(R.id.btn2);
		btn3 = (Button) findViewById(R.id.btn3);
		btn1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				values.clear();
				values = generate();
				draw(6.1f, 4.3f);
			}
		});

		btn2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				values.clear();
				values = generate();
				draw(9.5f, 6.3f);
			}
		});

		btn3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				values.clear();
				values = generate();
				draw(6.1f, 4.3f);
			}
		});

	}

	public void draw(float higher , float lower){
		cordinateSurface.setHigher(higher);
		cordinateSurface.setLower(lower);
		cordinateSurface.setValues(values);
		System.out.println("MainActivity.draw() higher = "+higher+" lower = "+lower +" values = "+values);
		cordinateSurface.postInvalidate();
	}
	
	public List<Float> generate() {
		for (int i = 0; i < 21; i++) {
			float v = 10 * new Random().nextFloat();
			values.add(Float.valueOf(new DecimalFormat("0.0").format(Math
					.abs((v > 10) ? v - 10 : v))));
		}
		return values;
	}

}
