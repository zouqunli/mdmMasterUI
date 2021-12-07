package cn.mdm.masterui.wiget.nettv;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Editable;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.util.Base64;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import org.xml.sax.XMLReader;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@SuppressLint("AppCompatCustomView")
public class NetTextView extends TextView {
    private Map<String,BitmapDrawable> map = new HashMap<>();
    public NetTextView(Context context) {
        super(context);
    }

    public NetTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public NetTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setHtmlText(String htmlText){
        setHtmlText(htmlText,false);
    }
    public void setHtmlText(String htmlText,boolean hasImgClick){
        if(hasImgClick){
            //设置这个才能点击
            this.setMovementMethod(LinkMovementMethod.getInstance());
            setText(Html.fromHtml( htmlText, new URLImageParser(this, getContext()), new URLTagHandler(getContext())));
        }
        else {
            setText(Html.fromHtml( htmlText, new URLImageParser(this, getContext()), null));
        }
    }

    public class URLDrawable extends BitmapDrawable {

        public Drawable drawable;
        public Bitmap bitmap;

        @Override
        public void draw(Canvas canvas) {
            if (drawable != null) {
                drawable.draw(canvas);
            }else if(bitmap != null){
                canvas.drawBitmap(bitmap, 0, 0, getPaint());
//                bitmap.recycle();
            }

        }
    }
    public class URLImageParser implements ImageGetter {

        Context context;
        View container;
        public URLImageParser(View container, Context context) {
            this.context = context;
            this.container = container;
        }


        public Drawable getDrawable(String source) {
            if (source.matches("data:image.*base64.*")) {
                String base_64_source = source.replaceAll("data:image.*base64", "");
                byte[] data = Base64.decode(base_64_source, Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                Drawable image = new BitmapDrawable(context.getResources(), bitmap);
                image.setBounds(0, 0, image.getIntrinsicWidth()>NetTextView.this.getWidth()?NetTextView.this.getWidth():image.getIntrinsicWidth()
                        , image.getIntrinsicHeight());
                map.put(base_64_source, (BitmapDrawable) image);
                return image;
            } else {
                URLDrawable urlDrawable = new URLDrawable();
                Glide.with(context)
                            .asBitmap()
                            .fitCenter()
                            .load(source)
                            .into(new BitmapTarget(urlDrawable));

                return urlDrawable;//return reference to URLDrawable where We will change with actual image from the src tag
            }
        }
    }
    private class BitmapTarget extends CustomTarget<Bitmap> {

        private final URLDrawable urlDrawable;

        public BitmapTarget(URLDrawable drawable) {
            this.urlDrawable = drawable;
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
            //计算屏幕宽高对图片进行缩放
//            float scaleWidth = ((float) mWidth) / resource.getWidth();
//            Matrix matrix = new Matrix();
//            matrix.postScale(scaleWidth, scaleWidth);
//            resource.setConfig(Bitmap.Config.ARGB_4444);
//            resource = Bitmap.createBitmap(resource, 0, 0,
//                    resource.getWidth(), resource.getHeight(),
//                    matrix, true);
            urlDrawable.bitmap = resource;
            urlDrawable.setBounds(0, 0, resource.getWidth()>NetTextView.this.getWidth()?NetTextView.this.getWidth():resource.getWidth(),
                    resource.getHeight());
            NetTextView.this.invalidate();
            NetTextView.this.setText(NetTextView.this.getText());
        }

        //清除
        @Override
        public void onLoadCleared(@Nullable Drawable placeholder) {
            if(placeholder == null)return;
            urlDrawable.drawable = placeholder;
            urlDrawable.setBounds(0, 0, placeholder.getIntrinsicWidth(), placeholder.getIntrinsicHeight());
            NetTextView.this.invalidate();
        }
    }


    //图片点击
    private class URLTagHandler implements Html.TagHandler{

        private Context mContext;

        public URLTagHandler(Context context) {
            mContext = context;
        }
        //标签会返回到这个方法
        @Override
        public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {
            //找出img的标签
            if(tag.toLowerCase(Locale.getDefault()).equals("img")){
                int len = output.length();
                ImageSpan[] imageSpan = output.getSpans(len-1,len, ImageSpan.class);
                String imgUrl = imageSpan[0].getSource();
                if(imgUrl.contains("https://")||imgUrl.contains("http://")) {
                    //图片是url的话
                    output.setSpan(new ClickableSpan() {
                        @Override
                        public void onClick(@NonNull View widget) {
                            new PicLookDialog(mContext, imgUrl).show();
                        }
                    }, len - 1, len, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }else if(imgUrl.matches("data:image.*base64.*")){
                    //base64的图片
                    String base64 = imgUrl.replaceAll("data:image.*base64", "");
                    output.setSpan(new ClickableSpan() {
                        @Override
                        public void onClick(@NonNull View widget) {
                            //取出图片
                            BitmapDrawable bmd= map.get(base64);
                            new PicLookDialog(mContext, bmd).show();
                        }
                    }, len - 1, len, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
        }
    }
}
