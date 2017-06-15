package com.chant.chanttest.font;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chant.chanttest.util.QMUIDisplayHelper;

public class TestFontActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout layout = new LinearLayout(this);
        layout.setDividerPadding(10);
        layout.setDividerDrawable(null);
        layout.setOrientation(LinearLayout.VERTICAL);

        {
            TextView textView = createTextView();
            textView.setTypeface(FontUtil.TYPEFACE_SOURCE_HANSERIF);
            textView.setBackgroundColor(0x80ff0000);
            layout.addView(textView);
        }

        {
            FontMetricsView fontMetricsView = new FontMetricsView(this);
            fontMetricsView.setTypeface(FontUtil.TYPEFACE_SOURCE_HANSERIF);
            fontMetricsView.setBackgroundColor(0x30ff0000);
            layout.addView(fontMetricsView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }

        {
            TextView textView = createTextView();
            textView.setBackgroundColor(0x8000ff00);
            layout.addView(textView);
        }

        {
            FontMetricsView fontMetricsView = new FontMetricsView(this);
            fontMetricsView.setBackgroundColor(0x3000ff00);
            layout.addView(fontMetricsView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }

        setContentView(layout);
    }

    private static final String sTEXT= "1995年11月15日正式确定每年4月23日为“世界图书与版权日”，设立目的是推动更多的人去阅读和写作，希望所有人都能尊重和感谢为人类文明做出过巨大贡献的文学、文化、科学、思想大师们，保护知识产权。";

    private TextView createTextView() {
        TextView textView = new TextView(this);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 21);
        textView.setLineSpacing(QMUIDisplayHelper.dpToPx(4), 1);
        textView.setText(sTEXT);
        return textView;
    }

    private static class FontMetricsView extends View {
        private Paint mPaint;
        private Paint mLineTextPaint;
        private Paint mLinePaint;

        public FontMetricsView(Context context) {
            super(context);

            mPaint = new Paint();
            mPaint.setAntiAlias(true);
            mPaint.setTextSize(QMUIDisplayHelper.dpToPx(21));
            mPaint.setColor(Color.BLACK);

            mLineTextPaint = new Paint();
            mLineTextPaint.setAntiAlias(true);
            mLineTextPaint.setTextSize(QMUIDisplayHelper.dpToPx(9));
            mLineTextPaint.setColor(Color.GRAY);

            mLinePaint = new Paint();
            mLinePaint.setColor(Color.BLUE);
            mLinePaint.setAlpha(128);
            mLinePaint.setStyle(Paint.Style.STROKE);
            mLinePaint.setStrokeWidth(2);
        }

        public void setTypeface(Typeface typeface) {
            mPaint.setTypeface(typeface);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            setMeasuredDimension((int) mPaint.measureText(sTEXT), QMUIDisplayHelper.dpToPx(80));
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            Paint.FontMetricsInt fontMetricsInt = mPaint.getFontMetricsInt();
            int height = getHeight();
            int fontHeight = fontMetricsInt.bottom - fontMetricsInt.top;

            int leading = (height - fontHeight) / 2 - fontMetricsInt.top;
            int top = leading + fontMetricsInt.top;
            int ascent = leading + fontMetricsInt.ascent;
            int descent = leading + fontMetricsInt.descent;
            int bottom = leading + fontMetricsInt.bottom;

            canvas.drawLine(0, top, getWidth(), top, mLinePaint);
            canvas.drawText("top", 0, top, mLineTextPaint);
            canvas.drawLine(0, ascent, getWidth(), ascent, mLinePaint);
            canvas.drawText("ascent", 0, ascent, mLineTextPaint);
            canvas.drawLine(0, leading, getWidth(), leading, mLinePaint);
            canvas.drawText("leading", 0, leading, mLineTextPaint);
            canvas.drawLine(0, descent, getWidth(), descent, mLinePaint);
            canvas.drawText("descent", 0, descent, mLineTextPaint);
            canvas.drawLine(0, bottom, getWidth(), bottom, mLinePaint);
            canvas.drawText("bottom", 0, bottom, mLineTextPaint);

            canvas.drawText(sTEXT, 0, leading, mPaint);

        }
    }
}
