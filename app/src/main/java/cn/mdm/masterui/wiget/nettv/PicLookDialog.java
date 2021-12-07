package cn.mdm.masterui.wiget.nettv;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.alibaba.fastjson.JSONArray;
import com.bin.david.form.data.format.draw.IDrawFormat;
import com.bin.david.form.data.table.ArrayTableData;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.net.URLEncoder;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.mdm.masterui.R;
import cn.mdm.masterui.databinding.DialogPicLookBinding;


/**
 * @date 创建时间 2020/2/25
 * @author qlzou
 * @Description  图片查看dialog
 * @Version 1.0
 */
public class PicLookDialog extends Dialog {

    private Context mContext;
    private DialogPicLookBinding mBinding;
    private IPicLookDialogListener listener;
    private String imgUrl;
    private BitmapDrawable imgBmd;
//    private RemotePDFViewPager pdfViewPager;

    public PicLookDialog(Context context, String imgUrl){
        this(context, R.style.DialogBg);
        this.mContext = context;
        this.imgUrl = imgUrl;
    }
    public PicLookDialog(Context context, BitmapDrawable bmd){
        this(context, R.style.DialogBg);
        this.mContext = context;
        this.imgBmd = bmd;
    }

    public PicLookDialog(@NonNull Context context) {
        this(context, R.style.DialogBg);
        this.mContext = context;
    }

    public PicLookDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;

    }
    protected PicLookDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        mBinding = DataBindingUtil.inflate(inflater,R.layout.dialog_pic_look,null,false);
        setContentView(mBinding.getRoot());
        init();
    }

    public interface IPicLookDialogListener{
        /**
         * 取消
         */
        void cancel();
        void onFailure(Exception e);
        void onSuccess();
        void onProgressUpdate(int progress, int total);
    }
    public PicLookDialog setListener(IPicLookDialogListener listener){
        this.listener = listener;
        return this;
    }

    /**
     * 初始化
     */
    private void init() {
        initWindow();
        // 启用图片缩放功能
        mBinding.ivPic.enable();
        mBinding.ivPicClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null)
                listener.cancel();
                dismiss();
            }
        });
        if(imgBmd != null){
            Glide.with(mContext)
                    .load(imgBmd)
                    .apply(new RequestOptions()
                    .skipMemoryCache(true))
                    .into(mBinding.ivPic);
        }else if(isImage(imgUrl)) {
            mBinding.ivPic.setVisibility(View.VISIBLE);
            mBinding.flPdf.setVisibility(View.GONE);
            Glide.with(mContext)
                    .load(imgUrl)
                    .apply(new RequestOptions()
//                            .placeholder(R.drawable.icon_img_error)
                            .skipMemoryCache(true))
//                    .diskCacheStrategy(DiskCacheStrategy.NONE)  //跳过磁盘缓存
                    .into(mBinding.ivPic);
        }else if(isPDF(imgUrl)){
            mBinding.ivPic.setVisibility(View.GONE);
            mBinding.flPdf.setVisibility(View.VISIBLE);
//            pdfViewPager = new RemotePDFViewPager(getContext(),changeChineseCode(imgUrl) , new DownloadFile.Listener() {
//                @Override
//                public void onSuccess(String url, String destinationPath) {
//                    PDFPagerAdapter adapter = new PDFPagerAdapter(getContext(),destinationPath);
//                    pdfViewPager.setBackground(new ColorDrawable(Color.BLUE));
//                    pdfViewPager.setAdapter(adapter);
//                    adapter.notifyDataSetChanged();
//                    mBinding.flPdf.removeAllViews();
//                    mBinding.flPdf.addView(pdfViewPager);
//                    if(listener != null) listener.onSuccess();
//                }
//
//                @Override
//                public void onFailure(Exception e) {
//                    if(listener != null) listener.onFailure(e);
//                }
//
//                @Override
//                public void onProgressUpdate(int progress, int total) {
//                    if(listener != null) listener.onProgressUpdate(progress,total);
//                }
//            });
        }
    }

    /**
     * 设置窗口大小
     */
    private void initWindow() {
        //设置dialog
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = mContext.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        lp.width = (int) (d.widthPixels * 1); // 宽度设置为屏幕的0.5
        lp.height = (int) (d.heightPixels * 1); // 高度设置为屏幕的0.5
        dialogWindow.setAttributes(lp);
        dialogWindow.setGravity(Gravity.CENTER);
    }

    /**
     * 是否是图片文件
     * @param url
     * @return
     */
    private boolean isImage(String url){
        if(url == null || url.isEmpty())return true;
        if(url.startsWith("http://") || url.startsWith("https://")) {
            Pattern pattern = Pattern.compile("bmp|jpg|png|tif|gif|pcx|tga|exif|fpx|svg|psd|cdr|pcd|dxf|ufo|eps|ai|raw|WMF|webp", Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(url);
            return matcher.find();
        }
        return false;
    }

    /**
     * 是否是pdf文件
     * @param url
     * @return
     */
    private boolean isPDF(String url){
        if(url == null || url.isEmpty())return true;
        if(url.startsWith("http://") || url.startsWith("https://")) {
            Pattern pattern = Pattern.compile("pdf", Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(url);
            return matcher.find();
        }
        return false;
    }

    /**
     * 转码 包含中文也进行转码
     * @param url
     * @return
     */
    private String changeChineseCode(String url){
        return URLEncoder.encode(url)
                .replace("%3A",":")
                .replace("%2F","/");

    }

    public static ArrayTableData<String> getArrayTableData(List<JSONArray> list, IDrawFormat drawFormat){
        if(list == null || list.size()<=0) {
            return ArrayTableData.create("",new String[]{""},ArrayTableData.transformColumnArray(new String[][]{{""}}),drawFormat);
        }
        String[] title = new String[list.get(0).size()];
        String[][] content = new String[list.size()==1?1:list.size()-1][title.length];
        for (int index = 0;index < list.size();index++){
            for (int itemIndex = 0;itemIndex < list.get(index).size();itemIndex++ ){
                if(index == 0){
                    title[itemIndex] = list.get(index).getString(itemIndex)==null?"":list.get(index).getString(itemIndex);
                    if(list.size() == 1)content[index][itemIndex] = "";
                }else{
                    content[index - 1][itemIndex] = list.get(index).getString(itemIndex)==null?"":list.get(index).getString(itemIndex);
                }
            }
        }
        //title是行数据，而content每个子集合需要的是对应title的列数据二维数组 titla[row] 对应 content[col][row]
        //我这里处理的是content[row][col] 用了转换方法ArrayTableData.transformColumnArray换成了content[col][row]
        return ArrayTableData.create("",title,ArrayTableData.transformColumnArray(content),drawFormat);
    }

}
