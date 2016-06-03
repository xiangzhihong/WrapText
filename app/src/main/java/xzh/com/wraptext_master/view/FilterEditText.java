/*
    The Android Not Open Source Project
    Copyright (c) 2014-11-24 wangzheng <iswangzheng@gmail.com>

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
    @author wangzheng  DateTime 2014-11-24
 */

package xzh.com.wraptext_master.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.InputFilter;
import android.util.AttributeSet;
import android.widget.EditText;

import xzh.com.wraptext_master.R;


public class FilterEditText extends EditText {
    private LengthFilter mLengthFilter = null;
    private boolean isDistinguishChineseEnglish = true;

    public FilterEditText(Context context) {
        this(context, null);
    }

    public FilterEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public FilterEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }
    private void init(AttributeSet attrs, int defStyle) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.FilterEditText, defStyle, 0);
        isDistinguishChineseEnglish = typedArray.getBoolean(R.styleable.FilterEditText_distinguish_chinese_english, true);
        int maxLength = typedArray.getInt(R.styleable.FilterEditText_limit_length, Integer.MAX_VALUE);
        typedArray.recycle();

        if(isDistinguishChineseEnglish){
            mLengthFilter = new LengthFilter(maxLength);
            setFilters(new InputFilter[]{mLengthFilter});
        }
    }

    @Override
    public void setFilters(InputFilter[] filters) {
        InputFilter[] superFilters = getFilters();
        int length = (superFilters == null ? 0 : superFilters.length) +
                (filters == null ? 0 : filters.length);
        InputFilter[] filterArray = new InputFilter[length];
        int index = 0;
        if (superFilters != null)
            for (InputFilter filter : superFilters) {
                filterArray[index] = filter;
                index++;
            }
        if (filters != null)
            for (InputFilter filter : filters) {
                filterArray[index] = filter;
                index++;
            }
        super.setFilters(filterArray);
    }

    public void setMaxLength(int maxLength) {
        if(mLengthFilter != null){
            mLengthFilter.setMaxLength(maxLength);
        }
    }

}


