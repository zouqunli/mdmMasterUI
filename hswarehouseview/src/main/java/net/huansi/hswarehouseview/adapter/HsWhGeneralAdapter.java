package net.huansi.hswarehouseview.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * 通用的适配器，支持多布局
 */
public abstract class HsWhGeneralAdapter<T> extends BaseAdapter {

	private List<T> mList = null;
	private Context mContext = null;
	private int mLayoutId;
	private int mFirstLayoutId;
	private int mSecondLayoutId;
	private int mThreeLayoutId;

	private int mTypeCount;

	public static final int SECOND_VIEW = 2;
	public static final int FIRST_VIEW = 1;
	public static final int THREE_VIEW = 3;


	public HsWhGeneralAdapter(Context context, List<T> list, int... layoutId) {
		this.mContext = context;
		this.mList = list;
		this.mTypeCount = layoutId.length;
		try {
			for (int i = 0; i < layoutId.length; i++) {
				switch (i) {
				case 0:
					this.mLayoutId = layoutId[i];
					break;
				case 1:
					this.mFirstLayoutId = layoutId[i];
					break;
				case 2:
					this.mSecondLayoutId = layoutId[i];
					break;
				case 3:
					this.mThreeLayoutId = layoutId[i];
					break;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public int getViewTypeCount() {
		return mTypeCount;
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public T getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		int currentType = getItemViewType(position);

		switch (currentType) {

		case FIRST_VIEW:
			final HsWhViewHolder hsWhViewHolder1 = getViewHolder(position, convertView, parent, mFirstLayoutId);
			initFirstView(hsWhViewHolder1, getItem(position), position);
			return hsWhViewHolder1.getConvertView();
		case SECOND_VIEW:
			final HsWhViewHolder hsWhViewHolder2 = getViewHolder(position, convertView, parent, mSecondLayoutId);
			initSecondView(hsWhViewHolder2, getItem(position), position);
			return hsWhViewHolder2.getConvertView();
		case THREE_VIEW:
			final HsWhViewHolder hsWhViewHolder3 = getViewHolder(position, convertView, parent, mThreeLayoutId);
			initThreeView(hsWhViewHolder3,getItem(position),position);
			return hsWhViewHolder3.getConvertView();
		default:
			final HsWhViewHolder hsWhViewHolder = getViewHolder(position, convertView, parent, mLayoutId);
			initItemView(hsWhViewHolder, getItem(position), position);
			return hsWhViewHolder.getConvertView();
		}

	}

	/**
	 * 初始化Item控件
	 */
	public abstract void initItemView(HsWhViewHolder hsWhViewHolder, T info,
									  int position);

	/**
	 * 初始化其他布局的第一个View
	 * 
	 * @param hsWhViewHolder
	 * @param info
	 * @param position
	 */
	public void initFirstView(HsWhViewHolder hsWhViewHolder, T info, int position) {

	}

	/**
	 * 初始化其他布局的第二个View
	 * 
	 * @param hsWhViewHolder
	 * @param info
	 * @param position
	 */
	public void initSecondView(HsWhViewHolder hsWhViewHolder, T info, int position) {

	}
	
	/**
	 * 初始化第三个布局
	 * @param hsWhViewHolder
	 * @param info
	 * @param position
	 */
	public void initThreeView(HsWhViewHolder hsWhViewHolder, T info, int position){}

	private HsWhViewHolder getViewHolder(int position, View convertView, ViewGroup parent, int layoutId) {
		return HsWhViewHolder.get(mContext, convertView, parent, layoutId, position);
	}

	public void setList(List<T> list) {
		this.mList = list;
		notifyDataSetChanged();
	}

}
