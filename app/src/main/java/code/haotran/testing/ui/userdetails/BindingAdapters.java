package code.haotran.testing.ui.userdetails;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import code.haotran.testing.R;
import code.haotran.testing.utils.GlideApp;
import code.haotran.testing.utils.StringUtils;
import code.haotran.testing.utils.UiUtils;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import androidx.databinding.BindingAdapter;

/**
 * @author Hao Tran
 *
 */
public class BindingAdapters {

    /*
     * User details profile image
     */
    @BindingAdapter({"imageUrl"})
    public static void bindImage(ImageView imageView, String imagePath) {
        GlideApp.with(imageView.getContext())
                .load(imagePath)
                .placeholder(R.color.md_grey_200)
                .apply(new RequestOptions().transforms(new CenterCrop(),
                        new RoundedCorners((int) UiUtils.dipToPixels(imageView.getContext(), 8))))
                .into(imageView);
    }

    @BindingAdapter("visibleGone")
    public static void showHide(View view, Boolean show) {
        if (show) view.setVisibility(View.VISIBLE);
        else view.setVisibility(View.GONE);

    }

    @BindingAdapter("setDate")
    public static void setDate(TextView view, long time) {
        view.setText(StringUtils.getDate(time));

    }

}
