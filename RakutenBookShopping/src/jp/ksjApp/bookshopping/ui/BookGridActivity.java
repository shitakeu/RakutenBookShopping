package jp.ksjApp.bookshopping.ui;

import java.util.ArrayList;

import com.androidquery.AQuery;

import jp.ksjApp.bookshopping.Const;
import jp.ksjApp.bookshopping.Util;
import jp.ksjApp.bookshopping.task.RakutenBookApiTask;
import jp.ksjApp.bookshopping.R;
import jp.tksjApp.bookshopping.data.BookData;

import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.GridView;

public class BookGridActivity extends Activity {

	@SuppressWarnings("unused")
	private static final String TAG = BookGridActivity.class.getSimpleName();

	private GridView mGridView;
	private String mSearchWord = "";
	private String mSearchGenre = "";
	
	// スクロール中か
	 private Boolean mScrolling = false;
	 
	 private ImageAdapter mImgAdapter;
	
	 private ArrayList<BookData> mBookDataList = new ArrayList<BookData>();
	 
	 private static int mPageCount = 1;
	 
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_grid);

		final Bundle ext = getIntent().getExtras();
		if (ext != null) {
			mSearchWord = ext.getString(Const.INTENT_KEY_SEARCH_WORD);
			mSearchGenre = ext.getString(Const.INTENT_KEY_SEARCH_GENRE);
		}

		mGridView = (GridView) findViewById(R.id.gridView);
		mGridView.setOnScrollListener(new GridViewOnScrollListener());
		startApiTask(1);
	}

	private void startApiTask(int page) {
		final RakutenBookApiTask task = new RakutenBookApiTask(mSearchWord,
				mSearchGenre,page) {
			private ProgressDialog mDialog;

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				mDialog = new ProgressDialog(BookGridActivity.this);
				mDialog.setTitle("Please wait");
				mDialog.setMessage("Uploading...");
				mDialog.show();
			}

			public void onPostExecute(final ArrayList<BookData> result) {
				if (result == null) {
					if(Const._DEBUG_){
						Log.d(TAG, "通信失敗");
					}
				} else if (result.size() <= 0) {
					if(Const._DEBUG_){
						Log.d(TAG, "該当商品なし");
					}
				} else {
					// API取得成功
					for(BookData data : result){
						mBookDataList.add(data);
					}
					if(mImgAdapter == null){
						mImgAdapter = new ImageAdapter(
								getApplicationContext(), mBookDataList);
						mGridView.setAdapter(mImgAdapter);
					} else{
						// アダプターにリストデータ変更の通知
						mImgAdapter.notifyDataSetChanged();
						// GridViewの再描画
						mGridView.invalidateViews();
					}
				}

				if (mDialog != null) {
					mDialog.dismiss();
				}
			}

		};
		task.execute();
	}

	public class ImageAdapter extends BaseAdapter {
		private Context mContext;
		private ArrayList<BookData> mBookData;
		private LayoutInflater mInflater;

		public ImageAdapter(Context c, ArrayList<BookData> bookData) {
			mContext = c;
			mBookData = bookData;
			mInflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		public int getCount() {
			return mBookData.size();
		}

		public Object getItem(int position) {
			return mBookData.get(position);
		}

		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final AQuery aq;
			if (convertView == null || convertView.getId() != position) {
				convertView = mInflater.inflate(R.layout.list_item_grid,
						parent, false);
				aq = new AQuery(convertView);
				convertView.setTag(aq);
			} else {
				aq = (AQuery) convertView.getTag();
			}

			final BookData item = (BookData) mBookData.get(position);
			if (item == null) {
				return convertView;
			}
			final String imgUrl = item.thumbnailUrl;
			aq.id(R.id.img_thumbnail).image(imgUrl, true, true);

			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Util.showItemDialog(BookGridActivity.this, item);
				}
			});

			return convertView;
		}
	}

	/*
	 * スクローラーの状態検知
	 */
	public class GridViewOnScrollListener implements OnScrollListener {

		/*
		 * ステータスが変わった時
		 * 
		 * @see
		 * android.widget.AbsListView.OnScrollListener#onScrollStateChanged(
		 * android.widget.AbsListView, int)
		 */
		@Override
		public void onScrollStateChanged(AbsListView paramAbsListView,
				int scrollState) {
			switch (scrollState) {

			// スクロール停止
			case OnScrollListener.SCROLL_STATE_IDLE:

				// decode処理をqueueに登録して開始する記述
				mScrolling = false;

				// アダプターにデータ変更の通知
				mImgAdapter.notifyDataSetChanged();
				// GridViewの再描画
				mGridView.invalidateViews();

				break;

			// スクロール
			case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
				// decodeのqueueをキャンセルする処理を記述
				mScrolling = true;
				break;

			// フリック
			case OnScrollListener.SCROLL_STATE_FLING:
				// decodeのqueueをキャンセルする処理を記述
				mScrolling = true;
				break;

			default:
				break;
			}
		}

		/*
		 * スクロール中
		 * 
		 * @see
		 * android.widget.AbsListView.OnScrollListener#onScroll(android.widget
		 * .AbsListView, int, int, int)
		 */
		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {

			// 現在表示されているリストの末尾番号
			int displayCount = firstVisibleItem + visibleItemCount;

			/*
			 * 
			 * 初期でdisplayCountに数値が入ってるのに、totalItemCountが0の場合があるためスクロール中のみ判定するようにする
			 * 。
			 */
			if (displayCount < totalItemCount || !mScrolling) {
				return;
			}
			
			mPageCount++;

			// リストにデータ追加
			startApiTask(mPageCount);

		}

	}
}
