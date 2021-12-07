package cn.mdm.masterui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ViewHolder {
	private final SparseArray<View> mViews;
	private int mPosition;
	private View mConvertView;

	private ViewHolder(Context context, ViewGroup parent, int layoutId,
                       int position) {
		this.mPosition = position;
		this.mViews = new SparseArray<>();
		mConvertView = LayoutInflater.from(context).inflate(layoutId, parent, false);
//		if(parent instanceof GridView) {
//
//			AbsListView.LayoutParams param = new AbsListView.LayoutParams(
//					android.view.ViewGroup.LayoutParams.FILL_PARENT,
//					parent.getMeasuredHeight() / (((GridView)parent).getAdapter().getCount()/((GridView)parent).getNumColumns()));
//			mConvertView.setLayoutParams(param);
//		}

		// setTag
		mConvertView.setTag(this);
	}

	/**
	 * 拿到一个ViewHolder对象
	 * 
	 * @param context
	 * @param convertView
	 * @param parent
	 * @param layoutId
	 * @param position
	 * @return
	 */
	public static ViewHolder get(Context context, View convertView,
                                 ViewGroup parent, int layoutId, int position) {
		if (convertView == null) {
			return new ViewHolder(context, parent, layoutId, position);
		}
		return (ViewHolder) convertView.getTag();
	}

	public View getConvertView() {
		return mConvertView;
	}

	public SparseArray<View> getViews() {
		return mViews;
	}

	/**
	 * 通过控件的Id获取对于的控件，如果没有则加入views
	 * 
	 * @param viewId
	 * @return
	 */
	public <T extends View> T getView(int viewId) {
		View view = mViews.get(viewId);
		if (view == null) {
			view = mConvertView.findViewById(viewId);
			mViews.put(viewId, view);
		}
		return (T) view;
	}

	/**
	 * 为TextView设置字符串
	 * 
	 * @param viewId
	 * @param text
	 * @return
	 */
	public ViewHolder setText(int viewId, String text) {
		TextView view = getView(viewId);
		view.setText(text);
		return this;
	}

	/**
	 * 设置进度条进度
	 * 
	 * @param viewId
	 * @param progress
	 * @return
	 */
	public ViewHolder setProgress(int viewId, int progress) {
		ProgressBar view = getView(viewId);
		view.setProgress(progress);
		return this;
	}

	/**
	 * 为ImageView设置图片
	 * 
	 * @param viewId
	 * @param drawableId
	 * @return
	 */
	public ViewHolder setImageResource(int viewId, int drawableId) {
		ImageView view = getView(viewId);
		view.setImageResource(drawableId);

		return this;
	}

	/**
	 * 为ImageView设置图片
	 * 
	 * @param viewId
	 * @param bm
	 * @return
	 */
	public ViewHolder setImageBitmap(int viewId, Bitmap bm) {
		ImageView view = getView(viewId);
		view.setImageBitmap(bm);
		return this;
	}

	/**
	 * 设置网络图片
	 * 
	 * @param id
	 * @param url
	 * @return
	 */
	public ViewHolder setImgNet(int id, String url) {
		// ImageLoader.getInstance(3, Type.LIFO).loadImage(url,
		// (ImageView) getView(viewId));
		return this;
	}

	public int getPosition() {
		return mPosition;
	}
}