package view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import com.example.Compass.R;


public class CompassView extends View {
    private float bearing;
    private Paint markerPaint;
    private Paint textPaint;
    private Paint circlePaint;
    private String northString;
    private String eastString;
    private String southString;
    private String westString;
    private int textHeight;

    public CompassView(Context context) {
        super(context);
        init();
    }

    public CompassView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CompassView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init(){
        setFocusable(true);
        Resources resources = getResources();
        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setColor(resources.getColor(R.color.background));
        circlePaint.setStrokeWidth(1);
        circlePaint.setStyle(Paint.Style.FILL_AND_STROKE);

        northString = resources.getString(R.string.cardinal_north);
        southString = resources.getString(R.string.cardinal_south);
        eastString = resources.getString(R.string.cardinal_east);
        westString = resources.getString(R.string.cardinal_west);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(resources.getColor(R.color.text));
        textHeight = (int)textPaint.measureText("yY");

        markerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        markerPaint.setColor(resources.getColor(R.color.marker));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = measure(heightMeasureSpec);
        int width = measure(widthMeasureSpec);
        int d = Math.min(height, width);
        setMeasuredDimension(d,d);
    }

    private int measure(int measureSpec){
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);
        int result = 200;
        if(mode == MeasureSpec.UNSPECIFIED){
            return result;
        }else {
            return size;
        }
    }

    public float getBearing() {
        return bearing;
    }

    public void setBearing(float bearing) {
        this.bearing = bearing;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int px = getMeasuredWidth()/2;
        int py =  getMeasuredHeight()/2;

        int radius = Math.min(px, py);
        canvas.drawCircle(px, py, radius, circlePaint);
        canvas.save();
        canvas.rotate(-bearing, px, py);
    }
}
