package com.infyson.invitationcard.classes;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Html;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by si on 25-04-2016.
 */
public class CustomEditText extends EditText {

    private Typeface tfFirstFont = null, tfSecondFont = null, tfThirdFont = null, tfHindiFont = null;

    public CustomEditText(Context context) {
        super(context);
        init();
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        init();
    }

    public CustomEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
        init();
    }

    private void init() {
        tfFirstFont = Typeface.createFromAsset(getContext().getAssets(), Constants.FIRST_FONT);
        tfSecondFont = Typeface.createFromAsset(getContext().getAssets(), Constants.SECOND_FONT);
        tfThirdFont = Typeface.createFromAsset(getContext().getAssets(), Constants.THIRD_FONT);
        tfHindiFont = Typeface.createFromAsset(getContext().getAssets(), Constants.FOURTH_FONT);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        // TODO Auto-generated method stub
        super.setText(text, type);
    }

    @Override
    public void setTypeface(Typeface tf, int style) {
        // TODO Auto-generated method stub
        if (style == Typeface.BOLD) {
            super.setTypeface(tf, Typeface.BOLD);
        } else if (style == Typeface.ITALIC) {
            super.setTypeface(tf, Typeface.ITALIC);
        } else if (style == Typeface.BOLD_ITALIC) {
            super.setTypeface(tf, Typeface.BOLD_ITALIC);
        } else {
            super.setTypeface(tf, Typeface.NORMAL);
        }
    }

    //Text
    public void setCustomText(int textId, String fontName, int style) {
        // TODO Auto-generated method stub
        if (fontName.equalsIgnoreCase(Constants.FIRST_FONT)) {
            setTypeface(tfFirstFont, style);
            setText(getResources().getString(textId), null);
        } else if (fontName.equalsIgnoreCase(Constants.SECOND_FONT)) {
            setTypeface(tfSecondFont, style);
            setText(getResources().getString(textId), null);
        } else if (fontName.equalsIgnoreCase(Constants.FOURTH_FONT)) {
            setTypeface(tfHindiFont, style);
            setText(getResources().getString(textId), null);
        } else {
            setTypeface(tfThirdFont, style);
            setText(getResources().getString(textId), null);
        }
    }

    public void setCustomText(int textId, String fontName) {
        // TODO Auto-generated method stub
        if (fontName.equalsIgnoreCase(Constants.FIRST_FONT)) {
            setTypeface(tfFirstFont, Typeface.NORMAL);
            setText(getResources().getString(textId), null);
        } else if (fontName.equalsIgnoreCase(Constants.SECOND_FONT)) {
            setTypeface(tfSecondFont, Typeface.NORMAL);
            setText(getResources().getString(textId), null);
        } else if (fontName.equalsIgnoreCase(Constants.FOURTH_FONT)) {
            setTypeface(tfHindiFont, Typeface.NORMAL);
            setText(getResources().getString(textId), null);
        } else {
            setTypeface(tfThirdFont, Typeface.NORMAL);
            setText(getResources().getString(textId), null);
        }
    }

    public void setCustomHTMLText(int textId, String fontName, int style) {
        // TODO Auto-generated method stub
        if (fontName.equalsIgnoreCase(Constants.FIRST_FONT)) {
            setTypeface(tfFirstFont, style);
            setText(Html.fromHtml(getResources().getString(textId)), null);
        } else if (fontName.equalsIgnoreCase(Constants.SECOND_FONT)) {
            setTypeface(tfSecondFont, style);
            setText(Html.fromHtml(getResources().getString(textId)), null);
        } else if (fontName.equalsIgnoreCase(Constants.FOURTH_FONT)) {
            setTypeface(tfHindiFont, style);
            setText(getResources().getString(textId), null);
        } else {
            setTypeface(tfThirdFont, style);
            setText(Html.fromHtml(getResources().getString(textId)), null);
        }
    }

    public void setCustomHTMLText(int textId, String fontName) {
        // TODO Auto-generated method stub
        if (fontName.equalsIgnoreCase(Constants.FIRST_FONT)) {
            setTypeface(tfFirstFont, Typeface.NORMAL);
            setText(Html.fromHtml(getResources().getString(textId)), null);
        } else if (fontName.equalsIgnoreCase(Constants.SECOND_FONT)) {
            setTypeface(tfSecondFont, Typeface.NORMAL);
            setText(Html.fromHtml(getResources().getString(textId)), null);
        } else if (fontName.equalsIgnoreCase(Constants.FOURTH_FONT)) {
            setTypeface(tfHindiFont, Typeface.NORMAL);
            setText(getResources().getString(textId), null);
        } else {
            setTypeface(tfThirdFont, Typeface.NORMAL);
            setText(Html.fromHtml(getResources().getString(textId)), null);
        }
    }

    public void setSimpleCustomText(String text, String fontName, int style) {
        // TODO Auto-generated method stub
        if (fontName.equalsIgnoreCase(Constants.FIRST_FONT)) {
            setTypeface(tfFirstFont, style);
            setText(text, null);
        } else if (fontName.equalsIgnoreCase(Constants.SECOND_FONT)) {
            setTypeface(tfSecondFont, style);
            setText(text, null);
        } else if (fontName.equalsIgnoreCase(Constants.FOURTH_FONT)) {
            setTypeface(tfHindiFont, style);
            setText(text, null);
        } else {
            setTypeface(tfThirdFont, style);
            setText(text, null);
        }
    }

    public void setSimpleCustomText(String text, String fontName) {
        // TODO Auto-generated method stub
        if (fontName.equalsIgnoreCase(Constants.FIRST_FONT)) {
            setTypeface(tfFirstFont, Typeface.NORMAL);
            setText(text, null);
        } else if (fontName.equalsIgnoreCase(Constants.SECOND_FONT)) {
            setTypeface(tfSecondFont, Typeface.NORMAL);
            setText(text, null);
        } else if (fontName.equalsIgnoreCase(Constants.FOURTH_FONT)) {
            setTypeface(tfHindiFont, Typeface.NORMAL);
            setText(text, null);
        } else {
            setTypeface(tfThirdFont, Typeface.NORMAL);
            setText(text, null);
        }
    }

    public void setCustomHTMLText(String text, String fontName, int style) {
        // TODO Auto-generated method stub
        if (fontName.equalsIgnoreCase(Constants.FIRST_FONT)) {
            setTypeface(tfFirstFont, style);
            setText(Html.fromHtml(text), null);
        } else if (fontName.equalsIgnoreCase(Constants.SECOND_FONT)) {
            setTypeface(tfSecondFont, style);
            setText(Html.fromHtml(text), null);
        } else if (fontName.equalsIgnoreCase(Constants.FOURTH_FONT)) {
            setTypeface(tfHindiFont, style);
            setText(Html.fromHtml(text), null);
        } else {
            setTypeface(tfThirdFont, style);
            setText(Html.fromHtml(text), null);
        }
    }

    public void setCustomHTMLText(String text, String fontName) {
        // TODO Auto-generated method stub
        if (fontName.equalsIgnoreCase(Constants.FIRST_FONT)) {
            setTypeface(tfFirstFont, Typeface.NORMAL);
            setText(Html.fromHtml(text), null);
        } else if (fontName.equalsIgnoreCase(Constants.SECOND_FONT)) {
            setTypeface(tfSecondFont, Typeface.NORMAL);
            setText(Html.fromHtml(text), null);
        } else if (fontName.equalsIgnoreCase(Constants.FOURTH_FONT)) {
            setTypeface(tfHindiFont, Typeface.NORMAL);
            setText(Html.fromHtml(text), null);
        } else {
            setTypeface(tfThirdFont, Typeface.NORMAL);
            setText(Html.fromHtml(text), null);
        }
    }
    //end

    //hint
    public void setCustomHint(int textId, String fontName, int style) {
        // TODO Auto-generated method stub
        if (fontName.equalsIgnoreCase(Constants.FIRST_FONT)) {
            setTypeface(tfFirstFont, style);
            setHint(getResources().getString(textId));
        } else if (fontName.equalsIgnoreCase(Constants.SECOND_FONT)) {
            setTypeface(tfSecondFont, style);
            setHint(getResources().getString(textId));
        } else if (fontName.equalsIgnoreCase(Constants.FOURTH_FONT)) {
            setTypeface(tfHindiFont, style);
            setHint(getResources().getString(textId));
        } else {
            setTypeface(tfThirdFont, style);
            setHint(getResources().getString(textId));
        }
    }

    public void setCustomHint(int textId, String fontName) {
        // TODO Auto-generated method stub
        if (fontName.equalsIgnoreCase(Constants.FIRST_FONT)) {
            setTypeface(tfFirstFont, Typeface.NORMAL);
            setHint(getResources().getString(textId));
        } else if (fontName.equalsIgnoreCase(Constants.SECOND_FONT)) {
            setTypeface(tfSecondFont, Typeface.NORMAL);
            setHint(getResources().getString(textId));
        } else if (fontName.equalsIgnoreCase(Constants.FOURTH_FONT)) {
            setTypeface(tfHindiFont, Typeface.NORMAL);
            setHint(getResources().getString(textId));
        } else {
            setTypeface(tfThirdFont, Typeface.NORMAL);
            setHint(getResources().getString(textId));
        }
    }

    public void setSimpleCustomHint(String text, String fontName, int style) {
        // TODO Auto-generated method stub
        if (fontName.equalsIgnoreCase(Constants.FIRST_FONT)) {
            setTypeface(tfFirstFont, style);
            setHint(text);
        } else if (fontName.equalsIgnoreCase(Constants.SECOND_FONT)) {
            setTypeface(tfSecondFont, style);
            setHint(text);
        } else if (fontName.equalsIgnoreCase(Constants.FOURTH_FONT)) {
            setTypeface(tfHindiFont, style);
            setHint(text);
        } else {
            setTypeface(tfThirdFont, style);
            setHint(text);
        }
    }

    public void setSimpleCustomHint(String text, String fontName) {
        // TODO Auto-generated method stub
        if (fontName.equalsIgnoreCase(Constants.FIRST_FONT)) {
            setTypeface(tfFirstFont, Typeface.NORMAL);
            setHint(text);
        } else if (fontName.equalsIgnoreCase(Constants.SECOND_FONT)) {
            setTypeface(tfSecondFont, Typeface.NORMAL);
            setHint(text);
        } else if (fontName.equalsIgnoreCase(Constants.FOURTH_FONT)) {
            setTypeface(tfHindiFont, Typeface.NORMAL);
            setHint(text);
        } else {
            setTypeface(tfThirdFont, Typeface.NORMAL);
            setHint(text);
        }
    }

    //end

}