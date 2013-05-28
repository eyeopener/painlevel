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
	
	private int mov_start_x = 0;// �����������
	private int mov_start_y = 0;
	private int mov_end_x = 0;// �����������
	private int mov_end_y = 0;
	
	private Paint paint;// ��������
	private Canvas canvas;// ����
	private Bitmap bitmap;// λͼ
	
	
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
		paint = new Paint(Paint.DITHER_FLAG);// ����һ������
		bitmap = Bitmap.createBitmap(m_screenWith, m_PaitMarginTop+m_PaitHeigth+(m_LableMarginTop+m_LableHeigth)*2, Bitmap.Config.ARGB_8888); // ����λͼ�Ŀ��
		canvas = new Canvas();
		canvas.setBitmap(bitmap);
		
		//������ʹ��
		paint.setStyle(Style.FILL);// �������
		paint.setColor(Color.GRAY);// ����Ϊ��ɫ
		LinearGradient linearGradient = new LinearGradient(m_PaitMarginLeft+m_PaitWidth, m_PaitMarginTop+m_PaitHeigth, m_PaitMarginLeft, m_PaitMarginTop, 
                new int[]{Color.RED,Color.RED,Color.RED,Color.RED,Color.YELLOW,Color.GREEN,Color.BLUE} , null,
                Shader.TileMode.REPEAT);
		paint.setShader(linearGradient);
		Rect r =new Rect(m_PaitMarginLeft,m_PaitMarginTop,m_PaitMarginLeft+m_PaitWidth,m_PaitMarginTop+m_PaitHeigth);
		canvas.drawRect(r,  paint);
		paint.setShader(null);
		
		// draw text  
		//paint.setStyle(Style.STROKE);// ���÷����
		paint.setStrokeWidth((float)1);// �ʿ�5����
		paint.setFakeBoldText(false);
		paint.setColor(Color.BLACK); 
		paint.setTextSize(20);  
		paint.setAntiAlias(true); // �������  
		//paint.setFlags(Paint.ANTI_ALIAS_FLAG); // �������  
		        
		String strText = "��ʹ"; 
		float textSize = paint.getTextSize();  		           
		canvas.drawText(strText,  
		           r.left,  
		           r.bottom + m_LableMarginTop+ m_LableHeigth - (m_LableHeigth - textSize) / 2, // add offset to y position  
		           paint);
		
		String strText2 = "��ʹ"; 
		textSize = paint.getTextSize();  		           
		canvas.drawText(strText2,  
		           r.left+m_PaitWidth - 40,  
		           r.bottom + m_LableMarginTop+ m_LableHeigth - (m_LableHeigth - textSize) / 2, // add offset to y position  
		           paint);
		
		String strText3 = "�밴������ʹ�̶��ڱ���ϴ�ֱ����"; 
		textSize = paint.getTextSize();  		           
		canvas.drawText(strText3,  
		           r.left+m_PaitWidth/2 -160,  
		           r.bottom + (m_LableMarginTop+m_LableHeigth)*2 - (m_LableHeigth - textSize) / 2, // add offset to y position  
		           paint);
		
		paint.setStyle(Style.STROKE);// ���÷����
		paint.setStrokeWidth(5);// �ʿ�5����
		paint.setColor(Color.BLACK);// ����Ϊ���
		paint.setAntiAlias(true);// ��ݲ���ʾ
	
		
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub

	}

	// ��λͼ
	@Override
	protected void onDraw(Canvas canvas) {
		// super.onDraw(canvas);
		canvas.drawBitmap(bitmap, 0, 0, null);
	}

	// �����¼�
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_MOVE) {// ����϶�		
			ReDraw();
			mov_end_x = (int) event.getX();
			mov_end_y = (int) event.getY();	
			canvas.drawLine(mov_start_x, mov_start_y, mov_end_x, mov_end_y, paint);// ����
				
			invalidate();
		}
		if (event.getAction() == MotionEvent.ACTION_DOWN) {// ������
			mov_start_x = (int) event.getX();
			mov_start_y = (int) event.getY();
			/*canvas.drawPoint(mov_x, mov_y, paint);// ����
			invalidate();*/
		}
		
		return true;
	}
	
	public double GetPainLevel() {
		//�жϻ������Ƿ������н��㣬�н�ս�ſɻ�ȡ�����
		if((mov_start_y > m_PaitMarginTop && mov_start_y < (m_PaitMarginTop+m_PaitHeigth))
				|| (mov_end_y > m_PaitMarginTop  && mov_end_y < (m_PaitMarginTop+m_PaitHeigth))
				|| (mov_start_y < m_PaitMarginTop && mov_end_y > (m_PaitMarginTop+m_PaitHeigth))
				|| (mov_end_y < m_PaitMarginTop && mov_start_y > (m_PaitMarginTop+m_PaitHeigth)))
		{
			//��ȡ���������ߵĽ���
			double x  = 0.0;
			double StaffCenterY = m_PaitMarginTop+m_PaitHeigth/2.0;	
			//�жϻ������Ƿ񴩹�������
			if((mov_start_y > StaffCenterY && mov_end_y > StaffCenterY)
					|| (mov_start_y < StaffCenterY && mov_end_y < StaffCenterY))
			{
				//���û�������ĵ㣬ȡ�ڱ���ڵ��߶ε�x��ƽ��ֵΪ��ֵ,����ȡ����ֱ���ڱ���ڵ�ƽ��xֵΪ��Чֵ
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
						//����߶������������棬���������±�Ե�Ľ���
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
							 //�����߶������������棬���������ϱ�Ե�Ľ���
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
				//���������������ߣ��������������ߵĽ���Ϊ��Ч��
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
	  * ��Ļ�ı�ʱ�Զ�����  
	* @param widthMeasureSpec �ı��Ŀ��  
	* @param heightMeasureSpec �ı��ĸ߶�  
	*/  
	 protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)   
	 {   
	     /*���*/  
	     m_screenWith = View.MeasureSpec.getSize(widthMeasureSpec);   
	     m_PaitWidth =  (int)(m_screenWith*0.8);   
	     m_PaitMarginLeft =  (int)(m_screenWith*0.1); 
	     ReDraw(); 
	     super.onMeasure(widthMeasureSpec, heightMeasureSpec);   
	     
	 } 

}
