package jp.ksjApp.bookshopping.ui;

import java.util.ArrayList;

import jp.ksjApp.bookshopping.R;
import jp.tksjApp.bookshopping.data.GenreData;

import com.androidquery.AQuery;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class CategoryAdapter extends ArrayAdapter<GenreData> {

	@SuppressWarnings("unused")
	private static final String TAG = ShopItemAdapter.class.getSimpleName();

	private LayoutInflater mInflater;
	private ArrayList<GenreData> mCategoryList;

	public CategoryAdapter(Context context, int resourceId,
			ArrayList<GenreData> categoryList) {
		super(context, resourceId, categoryList);
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mCategoryList = categoryList;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		AQuery aq = null;
		if (convertView == null || convertView.getId() != position) {
			convertView = mInflater.inflate(R.layout.list_category_row, parent, false);
			aq = new AQuery(convertView);
			convertView.setTag(aq);
		} else {
			aq = (AQuery) convertView.getTag();
		}

		final GenreData data = mCategoryList.get(position);
		if(data == null){
			return convertView;
		}
		
		final String category = data.name;
		if (category == null) {
			return convertView;
		}
//		aq.id(R.id.txt_category).text(category);
		return convertView;
	}

}
