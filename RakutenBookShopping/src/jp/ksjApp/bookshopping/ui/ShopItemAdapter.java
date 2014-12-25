package jp.ksjApp.bookshopping.ui;

import java.util.ArrayList;

import jp.ksjApp.bookshopping.R;
import jp.tksjApp.bookshopping.data.BookData;

import com.androidquery.AQuery;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;

public class ShopItemAdapter extends ArrayAdapter<BookData> {

	@SuppressWarnings("unused")
	private static final String TAG = ShopItemAdapter.class.getSimpleName();

	private Context mContext;
	private LayoutInflater mInflater;
	private ArrayList<BookData> mItemList;
	private int mResourceId;

	public ShopItemAdapter(Context context, int resourceId,
			ArrayList<BookData> itemList) {
		super(context, resourceId, itemList);
		mContext = context;
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mItemList = itemList;
		mResourceId = resourceId;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		AQuery aq = null;
		if (convertView == null || convertView.getId() != position) {
			convertView = mInflater.inflate(mResourceId, parent, false);
			aq = new AQuery(convertView);
			convertView.setTag(aq);
		} else {
			aq = (AQuery) convertView.getTag();
		}

		final BookData item = (BookData) mItemList.get(position);
		if (item == null) {
			return convertView;
		}
		aq.id(R.id.txt_title).text(item.name);
		aq.id(R.id.txt_author).text(item.author);
		aq.id(R.id.txt_publisher).text(item.publisherName);
		
		aq.id(R.id.txt_price).text(item.price + "円");

		if (mResourceId == R.layout.list_item_row) {
			final RatingBar bar = (RatingBar) convertView
					.findViewById(R.id.bar_review);
			final float reviewRat = item.reviewRate;
			if (0 <= reviewRat && reviewRat <= 5) {
				bar.setVisibility(View.VISIBLE);
				bar.setRating(reviewRat);
			} else {
				bar.setVisibility(View.GONE);
			}
		}

		final String imgUrl = item.thumbnailUrl;
		aq.id(R.id.img_thumbnail).image(imgUrl, true, true);
		if (!TextUtils.isEmpty(item.reviewCount)) {
			aq.id(R.id.txt_review_count).visible();
			aq.id(R.id.txt_review_count)
					.text("レビュー数：".concat(item.reviewCount));
		} else {
			aq.id(R.id.txt_review_count).gone();
		}

		convertView.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				final String url;
				if (TextUtils.isEmpty(mItemList.get(position).affiliateUrl)) {
					url = mItemList.get(position).url;
				} else {
					url = mItemList.get(position).affiliateUrl;
				}
				if (!TextUtils.isEmpty(url)) {
					final Uri uri = Uri.parse(url);
					final Intent intent = new Intent(Intent.ACTION_VIEW, uri);
					mContext.startActivity(intent);
				}
			}
		});
		return convertView;
	}

}
