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
        layout.setBackgroundColor(Color.WHITE);
        layout.setDividerPadding(10);
        layout.setDividerDrawable(null);
        layout.setOrientation(LinearLayout.VERTICAL);

        {
            String text = "思源宋体 Medium 在 Android 中行高特别高，所以显示时会导致 TextView 的上下内间距特别高。";
            TextView textView = createTextView(text);
            textView.setTypeface(FontUtil.SourceHanSerifCNMedium);
            textView.setBackgroundColor(0x80ff0000);
            layout.addView(textView);

            FontMetricsView fontMetricsView = new FontMetricsView(this, text);
            fontMetricsView.setTypeface(FontUtil.SourceHanSerifCNMedium);
            fontMetricsView.setBackgroundColor(0x30ff0000);
            layout.addView(fontMetricsView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }

        {
            String text = "使用 ADOBE OPENTYPE FONT DEVELOPER'S KIT(\"FDK\") 修改字体文件中的信息，解决该问题。";
            TextView textView = createTextView(text);
            textView.setTypeface(FontUtil.SourceHanSerifCNMedium_Modify);
            textView.setBackgroundColor(0x8000ff00);
            layout.addView(textView);

            FontMetricsView fontMetricsView = new FontMetricsView(this, text);
            fontMetricsView.setTypeface(FontUtil.SourceHanSerifCNMedium_Modify);
            fontMetricsView.setBackgroundColor(0x3000ff00);
            layout.addView(fontMetricsView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }

        setContentView(layout);
    }

    private TextView createTextView(String text) {
        TextView textView = new TextView(this);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 21);
        textView.setLineSpacing(QMUIDisplayHelper.dpToPx(4), 1);
        textView.setText(text);
        return textView;
    }

    private static class FontMetricsView extends View {
        private Paint mPaint;
        private Paint mLineTextPaint;
        private Paint mLinePaint;
        private String mText;

        public FontMetricsView(Context context, String text) {
            super(context);
            mText = text;

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
            setMeasuredDimension((int) mPaint.measureText(mText), QMUIDisplayHelper.dpToPx(80));
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

            canvas.drawText(mText, 0, leading, mPaint);

        }
    }
}
