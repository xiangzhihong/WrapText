package xzh.com.wraptext_master.view;

import android.text.InputFilter;
import android.text.Spanned;

public class LengthFilter implements InputFilter {
    private int maxLength;

    public LengthFilter(int maxLength) {
        this.maxLength = maxLength;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        int dindex = 0;
        int count = 0;

        while (count <= maxLength && dindex < dest.length()) {
            char c = dest.charAt(dindex++);
            if (c < 128) {
                count = count + 1;
            } else {
                count = count + 2;
            }
        }
        if (count > maxLength) {
            return dest.subSequence(0, dindex);
        }

        int sindex = 0;
        while (count <= maxLength && sindex < source.length()) {
            char c = source.charAt(sindex++);
            if (c < 128) {
                count = count + 1;
            } else {
                count = count + 2;
            }
        }

        if (count > maxLength) {
            sindex--;
        }

        return source.subSequence(0, sindex);
    }
}
