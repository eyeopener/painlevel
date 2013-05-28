package com.example.painlevel;

import java.security.PublicKey;
import android.R.integer;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.Toast;
import android.widget.LinearLayout;

public class DrawLineView extends View {
	
	private int mov_start_x = 0;// 声明起点坐标
	private int mov_start_y = 0;
	private int mov_end_x = 0;// 声明起点坐标
	private int mov_end_y = 0;
	
	private Paint paint;// 声明画笔
	private Canvas canvas;// 画布
	private Bitmap bitmap;// 位图
	
	
	private int m_screenWith = 450;
	private int m_PaitMarginLeft = 0;
	private int m_PaitMarginTop = 60;
	private int m_PaitWidth = 450;
	private int m_PaitHeigth = 30;
	
	private int m_LableMarginTop= 20;
	private int m_LableHeigth = 25;

	public DrawLineView(Context context) {
		super(context);		
		ReDraw();
	}

	public DrawLineView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public DrawLineView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	public void ReDraw() {
		paint = new Paint(Paint.DITHER_FLAG);// 创建一个画笔
		bitmap = Bitmap.createBitmap(m_screenWith, m_PaitMarginTop+m_PaitHeigth+(m_LableMarginTop+m_LableHeigth)*2, Bitmap.Config.ARGB_8888); // 设置位图的宽高
		canvas = new Canvas();
		canvas.setBitmap(bitmap);
		
		//绘制疼痛条
		paint.setStyle(Style.FILL);// 设置填充
		paint.setColor(Color.GRAY);// 设置为灰色
		LinearGradient linearGradient = new LinearGradient(m_PaitMarginLeft+m_PaitWidth, m_PaitMarginTop+m_PaitHeigth, m_PaitMarginLeft, m_PaitMarginTop, 
                new int[]{Color.RED,Color.RED,Color.RED,Color.RED,Color.YELLOW,Color.GREEN,Color.BLUE} , null,
                Shader.TileMode.REPEAT);
		paint.setShader(linearGradient);
		Rect r =new Rect(m_PaitMarginLeft,m_PaitMarginTop,m_PaitMarginLeft+m_PaitWidth,m_PaitMarginTop+m_PaitHeigth);
		canvas.drawRect(r,  paint);
		paint.setShader(null);
		
		// draw text  
		//paint.setStyle(Style.STROKE);// 设置非填充
		paint.setStrokeWidth((float)1);// 笔宽5像素
		paint.setFakeBoldText(false);
		paint.setColor(Color.BLACK); 
		paint.setTextSize(20);  
		paint.setAntiAlias(true); // 消除锯齿  
		//paint.setFlags(Paint.ANTI_ALIAS_FLAG); // 消除锯齿  
		        
		String strText = "无痛"; 
		float textSize = paint.getTextSize();  		           
		canvas.drawText(strText,  
		           r.left,  
		           r.bottom + m_LableMarginTop+ m_LableHeigth - (m_LableHeigth - textSize) / 2, // add offset to y position  
		           paint);
		
		String strText2 = "最痛"; 
		textSize = paint.getTextSize();  		           
		canvas.drawText(strText2,  
		           r.left+m_PaitWidth - 40,  
		           r.bottom + m_LableMarginTop+ m_LableHeigth - (m_LableHeigth - textSize) / 2, // add offset to y position  
		           paint);
		
		String strText3 = "请按您的疼痛程度在标尺上垂直划线"; 
		textSize = paint.getTextSize();  		           
		canvas.drawText(strText3,  
		           r.left+m_PaitWidth/2 -160,  
		           r.bottom + (m_LableMarginTop+m_LableHeigth)*2 - (m_LableHeigth - textSize) / 2, // add offset to y position  
		           paint);
		
		paint.setStyle(Style.STROKE);// 设置非填充
		paint.setStrokeWidth(5);// 笔宽5像素
		paint.setColor(Color.BLACK);// 设置为红笔
		paint.setAntiAlias(true);// 锯齿不显示
	
		
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub

	}

	// 画位图
	@Override
	protected void onDraw(Canvas canvas) {
		// super.onDraw(canvas);
		canvas.drawBitmap(bitmap, 0, 0, null);
	}

	// 触摸事件
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_MOVE) {// 如果拖动		
			ReDraw();
			mov_end_x = (int) event.getX();
			mov_end_y = (int) event.getY();	
			canvas.drawLine(mov_start_x, mov_start_y, mov_end_x, mov_end_y, paint);// 画线
				
			invalidate();
		}
		if (event.getAction() == MotionEvent.ACTION_DOWN) {// 如果点击
			mov_start_x = (int) event.getX();
			mov_start_y = (int) event.getY();
			/*canvas.drawPoint(mov_x, mov_y, paint);// 画点
			invalidate();*/
		}
		
		return true;
	}
	
	public double GetPainLevel() {
		//判断画的线是否与标尺有交点，有交战才可获取到结果
		if((mov_start_y > m_PaitMarginTop && mov_start_y < (m_PaitMarginTop+m_PaitHeigth))
				|| (mov_end_y > m_PaitMarginTop  && mov_end_y < (m_PaitMarginTop+m_PaitHeigth))
				|| (mov_start_y < m_PaitMarginTop && mov_end_y > (m_PaitMarginTop+m_PaitHeigth))
				|| (mov_end_y < m_PaitMarginTop && mov_start_y > (m_PaitMarginTop+m_PaitHeigth)))
		{
			//获取所画线与标尺的交点
			double x  = 0.0;
			double StaffCenterY = m_PaitMarginTop+m_PaitHeigth/2.0;	
			//判断划的线是否穿过中心线
			if((mov_start_y > StaffCenterY && mov_end_y > StaffCenterY)
					|| (mov_start_y < StaffCenterY && mov_end_y < StaffCenterY))
			{
				//如果没穿过中心点，取在标尺内的线段的x的平均值为有值,否则取所绘直线在标尺内的平均x值为有效值
				if(mov_start_y < (m_PaitMarginTop+m_PaitHeigth) 
						&& mov_start_y > m_PaitMarginTop
						&& mov_end_y < (m_PaitMarginTop+m_PaitHeigth)
						&& mov_end_y > m_PaitMarginTop)
				 {
					 x = (mov_end_x + mov_start_x)/2.0;
				 }
				else
				{
					double xEage= 0.0;
					if(mov_start_y > StaffCenterY)
					{					
						//如果线断在中心线下面，计算与标尺下边缘的交点
						xEage = mov_start_x +(mov_end_x - mov_start_x)*(((double)m_PaitMarginTop+m_PaitHeigth -mov_start_y)/(mov_end_y -mov_start_y));	
						if(mov_start_y > mov_end_y)
						{
							x = (mov_end_x + xEage)/2.0;
						}
						else
						{
							x = (mov_start_x + xEage)/2.0;
						}
										 
					}
					 else
					 {					 
							 //发果线断在中心线上面，计算与标尺上边缘的交点
							 xEage = mov_start_x +(mov_end_x - mov_start_x)*(((double)m_PaitMarginTop -mov_start_y)/(mov_end_y -mov_start_y));
							 if(mov_start_y > mov_end_y)
							 {
								 x = (mov_start_x + xEage)/2.0;
							 }
							 else
							 {						 
								 x = (mov_end_x + xEage)/2.0;
							 }					
					 }				
				}
			}
			else
			{
				//如果穿过标尺中心线，划的线与中心线的交点为有效点
				x = mov_start_x +(mov_end_x - mov_start_x)*((StaffCenterY -mov_start_y)/(mov_end_y -mov_start_y));			
			}
			double score = (x-m_PaitMarginLeft)/(m_PaitWidth/10.0);
			if(score < 0.0)
			{
				score = 0.0;
			}
			else if(score > 10.0)
			{
				score = 10.0;
			}
			
			return score;
		}
		else
		{
			return 0.0;
		}
	}
	
	public void ClearnPainLevel() {
		mov_start_x = mov_end_x = m_PaitMarginLeft; 
	}
		
	/**  
	  * 屏幕改变时自动调用  
	* @param widthMeasureSpec 改变后的宽度  
	* @param heightMeasureSpec 改变后的高度  
	*/  
	 protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)   
	 {   
	     /*宽度*/  
	     m_screenWith = View.MeasureSpec.getSize(widthMeasureSpec);   
	     m_PaitWidth =  (int)(m_screenWith*0.8);   
	     m_PaitMarginLeft =  (int)(m_screenWith*0.1); 
	     ReDraw(); 
	     super.onMeasure(widthMeasureSpec, heightMeasureSpec);   
	     
	 } 

}
