package com.demo.xiao;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

/**
 * @filename CordinateSurface.java
 * @TODO Surface 画 XY坐标轴
 * @date 2014-6-18下午9:27:23
 * @Administrator 萧
 * 
 */
public class CordinateSurface extends SurfaceView implements Callback {
    
	private SurfaceHolder holder = null;
	private MyDrawThread drawThread = null;
	private int width;
	private int height;

	private float max;
	private float min;

	private float higher; // 较高限
	private float lower; // 较低限

	private int size; // 纵轴分为几等份
	private float ratio; // 纵轴实际长度与数据间隔比率

	private float preLength; // 数据间隔
	private float preRealLength; // 纵轴间隔长度

	private int Yvalue[] = new int[] { 0, 2, 4, 6, 8, 10, 12 };

	private float[] lenth = new float[] { 0.5f, 1, 1.5f, 2, 2.5f, 3, 3.5f, 4 };

	private List<Float> values = new ArrayList<Float>();

	public void setWidth(int width) {
		this.width = width;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public CordinateSurface(Context context) {
		super(context);
		init();
	}

	public CordinateSurface(Context context, AttributeSet attributeSet) {
		super(context, attributeSet);
		init();
	}

	public CordinateSurface(Context context, AttributeSet attributeSet, int arg2) {
		super(context, attributeSet, arg2);
		init();
	}

	int count = 0;

	private void init() {
		holder = getHolder();
		this.setZOrderOnTop(true);
		holder.setFormat(PixelFormat.TRANSLUCENT);
		holder.addCallback(this);
	}

	public void setHigher(float higher) {
		this.higher = higher;
	}

	public void setLower(float lower) {
		this.lower = lower;
	}

	public void setValues(List<Float> values) {
		this.values = values;
	}

	@Override
	public void setZOrderOnTop(boolean onTop) {
		if (isInEditMode()) {
			return;
		}
		super.setZOrderOnTop(onTop);
	}

	/**
	 * 
	 * @param value
	 *            实际数值
	 * @return
	 */
	private float calculateY(float value, int height) {
		System.out.println("CordinateSurface.calculateY() height = " + height
				+ "  value = " + value + "  ratio = " + ratio
				+ "  height-value/ratio = " + (height - value / ratio));
		return height - value / ratio;
	}

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int width,
			int height) {
	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		if (drawThread == null) {
			drawThread = new MyDrawThread(holder);
			drawThread.isRun = true;
			drawThread.start();
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		drawThread.isRun = false;
		drawThread = null;
	}

	class MyDrawThread extends Thread {

		private SurfaceHolder holder;
		private boolean isRun = false;

		public MyDrawThread(SurfaceHolder holder) {
			this.holder = holder;
			isRun = true;
		}

		@Override
		public void run() {
			Canvas canvas = holder.lockCanvas();
			width = getWidth();
			height = getHeight();
			preRealLength = height / Yvalue.length; // 计算得出纵轴实际每等分长度
			preLength = 2; // 数据间隔
			ratio = Float.valueOf(new DecimalFormat("0.00").format(preLength
					/ preRealLength)); // 比例尺
			// ratio = Float.valueOf(new
			// DecimalFormat("0.00").format(preLength/preRealLength)); // 比例尺
			if (values != null && values.size() >=1) {
				Collections.sort(values);
				max = values.get(values.size() - 1);
				min = values.get(0);
			}   

			while (isRun) {

				Paint paint = new Paint();
				paint.setFlags(Paint.ANTI_ALIAS_FLAG);
				paint.setAntiAlias(true);
				paint.setColor(Color.GRAY);
				paint.setStrokeWidth(3);
				paint.setStyle(Style.FILL);

				Point point = new Point();
				point.x = 40;
				point.y = height - 40;
				canvas.drawLine(point.x, point.y, width - 10, point.y, paint); // 画X轴
				canvas.drawLine(point.x, point.y, point.x, 10, paint); // 画Y轴

				float higherY = calculateY(higher, height);
				float lowerY = calculateY(lower, height);

				paint.setColor(Color.RED);
				canvas.drawLine(point.x, higherY, width - 10, higherY, paint); // 画高限
				paint.setColor(Color.GREEN);
				canvas.drawLine(point.x, lowerY, width - 10, lowerY, paint); // 画低限

				holder.unlockCanvasAndPost(canvas);
				postInvalidate();
				isRun = false;
				break;
			}
		}

	}

}
