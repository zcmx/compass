package view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
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
    private float pitch = 0;
    private float roll = 0;

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
        circlePaint.setColor(Color.BLACK);
        circlePaint.setStrokeWidth(1);
        circlePaint.setStyle(Paint.Style.FILL_AND_STROKE);

        northString = resources.getString(R.string.cardinal_north);
        southString = resources.getString(R.string.cardinal_south);
        eastString = resources.getString(R.string.cardinal_east);
        westString = resources.getString(R.string.cardinal_west);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(18);
        textHeight = (int)textPaint.measureText("yY");

        markerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        markerPaint.setColor(Color.LTGRAY);
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
        int py = getMeasuredHeight()/2;

        int radius = Math.min(px, py);
        canvas.drawCircle(px, py, radius, circlePaint);
        canvas.save();
        canvas.rotate(-bearing, px, py);
        int textWidth = (int)textPaint.measureText(westString);
        int cardinalX = -2 + px-textWidth/2;
        int cardinalY = py-radius+textHeight;
        for(int i = 0; i < 24; i++){
            canvas.drawLine(px, py - radius, px, py - radius + 10, markerPaint);
            canvas.save();
            canvas.translate(0, textHeight);
            textPaint.setTextSize(30);
            if(i % 6 == 0){
                textPaint.setColor(Color.WHITE);
                String dirString = "";
                int arrowY = 2*textHeight;
                switch(i){
                    case 0:{
                        textPaint.setColor(Color.RED);
                        textPaint.setFakeBoldText(true);
                        dirString = northString;
                        canvas.drawText(dirString, cardinalX-1, cardinalY, textPaint);
                        markerPaint.setColor(Color.RED);
                        canvas.drawLine(px, arrowY, px-5, 3*textHeight, markerPaint);
                        canvas.drawLine(px, arrowY, px+5, 3*textHeight, markerPaint);
                        canvas.drawLine(px, arrowY, px, 5*textHeight,markerPaint);
                        markerPaint.setColor(Color.LTGRAY);
                        break;
                    }
                    case 6 : {
                        dirString = eastString;
                        canvas.drawLine(px, arrowY, px, 5*textHeight,markerPaint);
                        canvas.drawText(dirString, cardinalX, cardinalY, textPaint);
                        break;
                    }
                    case 12:{
                        dirString = southString;
                        canvas.drawLine(px, arrowY, px, 5*textHeight,markerPaint);
                        canvas.drawText(dirString, cardinalX, cardinalY, textPaint);
                        break;
                    }
                    case 18:{
                        dirString = westString;
                        canvas.drawLine(px, arrowY, px, 5*textHeight,markerPaint);
                        canvas.drawText(dirString, cardinalX-3, cardinalY, textPaint);
                        break;
                    }
                }
                textPaint.setFakeBoldText(false);
            }
            if(i % 3 == 0 && i != 0 && i != 6 && i != 12 && i != 18){
                textPaint.setColor(Color.LTGRAY);
                String angle = String.valueOf(i*15);
                float angleTextWidth = textPaint.measureText(angle);
                int angleTextX = (int)(px-angleTextWidth/2);
                int angleTextY = py-radius+textHeight;
                canvas.drawText(angle, angleTextX, angleTextY, textPaint);
            }
            else if(i != 0 && i != 6 && i != 12 && i != 18){
                textPaint.setColor(Color.GRAY);
                textPaint.setTextSize(18);
                String angle = String.valueOf(i*15);
                float angleTextWidth = textPaint.measureText(angle);
                int angleTextX = (int)(px-angleTextWidth/2);
                int angleTextY = py-radius+textHeight;
                canvas.drawText(angle, angleTextX, angleTextY, textPaint);
            }
            canvas.restore();
            canvas.rotate(15, px, py);
        }
        canvas.restore();

//        int mMeasureWidth = 100;
//        int mMeasureHeight = 100;
//        RectF rollOval = new RectF((mMeasureWidth/3)-mMeasureWidth/7,
//                                    (mMeasureHeight/2)-mMeasureWidth/7,
//                                    (mMeasureWidth/3)-mMeasureWidth/7,
//                                    (mMeasureHeight/2)-mMeasureWidth/7);
//        markerPaint.setStyle(Paint.Style.STROKE);
//        canvas.drawOval(rollOval, markerPaint);
//        markerPaint.setStyle(Paint.Style.FILL);
//        canvas.save();
//        canvas.rotate(roll, mMeasureWidth/3, mMeasureHeight/2);
//        canvas.drawArc(rollOval,0,180, false, markerPaint);
//        canvas.restore();

//        RectF pitchOval = new RectF((mMeasureWidth/3)-mMeasureWidth/7,
//                                    (2*mMeasureHeight/2)-mMeasureWidth/7,
//                                    (mMeasureWidth/3)-mMeasureWidth/7,
//                                    (2*mMeasureHeight/2)-mMeasureWidth/7);
//        markerPaint.setStyle(Paint.Style.STROKE);
//        canvas.drawOval(pitchOval, markerPaint);
//        markerPaint.setStyle(Paint.Style.FILL);
//        canvas.drawArc(pitchOval, 0-pitch/2, 180+(pitch), false, markerPaint);
//        markerPaint.setStyle(Paint.Style.STROKE);
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public float getRoll() {
        return roll;
    }

    public void setRoll(float roll) {
        this.roll = roll;
    }
}
