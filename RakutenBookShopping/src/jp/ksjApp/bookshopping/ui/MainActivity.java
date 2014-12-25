package jp.ksjApp.bookshopping.ui;

import java.util.ArrayList;

import com.androidquery.AQuery;

import jp.ksjApp.bookshopping.Const;
import jp.ksjApp.bookshopping.Util;
import jp.ksjApp.bookshopping.task.BookGenreApiTask;
import jp.ksjApp.bookshopping.task.RakutenBookRankingApiTask;
import jp.ksjApp.bookshopping.R;
import jp.tksjApp.bookshopping.data.BookData;
import jp.tksjApp.bookshopping.data.GenreData;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {

	private static final String TAG = MainActivity.class.getSimpleName();

	// 検索するジャンル
	private String mSearchGenreId = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		getWindow().setSoftInputMode(
				LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (!Util.isNetworkAvailable(getApplicationContext())) {
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					this);
			alertDialogBuilder.setTitle("エラー");
			// アラートダイアログのメッセージを設定します
			alertDialogBuilder.setMessage("ネットワークに接続できません。¥n接続してから再度アプリを起動してください。");
			// アラートダイアログの肯定ボタンがクリックされた時に呼び出されるコールバックリスナーを登録します
			alertDialogBuilder.setPositiveButton("OK",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							finish();
						}
					});
			alertDialogBuilder.create().show();
		}

		setupView();
		requstGenre();
		requstRanking();
	}

	private void setupView() {
		final EditText editText = (EditText) findViewById(R.id.edit_word);
		editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (event != null
						&& event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
					if (event.getAction() == KeyEvent.ACTION_UP) {
						final String searchWord = editText.getText().toString();
						final Intent intent = new Intent(MainActivity.this,
								BookGridActivity.class);
						intent.putExtra(Const.INTENT_KEY_SEARCH_WORD,
								searchWord);
						startActivity(intent);
					}
					return true;
				}
				return false;
			}
		});

		findViewById(R.id.btn_search).setOnClickListener(this);
	}

	/**
	 * ジャンル一覧を取得する
	 */
	private void requstGenre() {
		final Spinner spn = (Spinner) findViewById(R.id.spn_genre);
		final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		adapter.add("ジャンル読み込み中・・・");
		spn.setAdapter(adapter);
		spn.setEnabled(false);

		final BookGenreApiTask task = new BookGenreApiTask() {

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
			}

			public void onPostExecute(final ArrayList<GenreData> result) {
				if (result == null) {
					final String[] list = { "通信失敗" };
					final ArrayAdapter<String> adapter = new ArrayAdapter<String>(
							MainActivity.this,
							android.R.layout.simple_spinner_item, list);
					spn.setAdapter(adapter);
				} else if (result.size() <= 0) {
					final String[] list = { "ジャンルが取得できませんでした" };
					final ArrayAdapter<String> adapter = new ArrayAdapter<String>(
							MainActivity.this,
							android.R.layout.simple_spinner_item, list);
					spn.setAdapter(adapter);
				} else {
					final String[] list = new String[result.size() + 1];
					list[0] = "すべて";
					int cont = 1;
					for (GenreData data : result) {
						list[cont] = data.name;
						cont++;
					}

					final ArrayAdapter<String> adapter = new ArrayAdapter<String>(
							MainActivity.this, R.layout.list_category_row, list);
					spn.setAdapter(adapter);
					spn.setEnabled(true);
					spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
						@Override
						public void onItemSelected(AdapterView<?> parent,
								View view, int position, long id) {
							// 選択されたアイテムを取得します
							if (position != 0) {
								mSearchGenreId = result.get(position).id;
							}
						}

						@Override
						public void onNothingSelected(AdapterView<?> arg0) {
						}
					});
				}
			}
		};
		task.execute("001");
	}

	/**
	 * ランキングの取得とView反映
	 */
	private void requstRanking() {
		final RakutenBookRankingApiTask task = new RakutenBookRankingApiTask() {

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
			}

			public void onPostExecute(final ArrayList<BookData> result) {
				if (result == null || result.size() <= 0) {
					if (Const._DEBUG_) {
						Log.e(TAG, "result = " + result);
					}
				} else {
					setRankingList(result);
				}
			}
		};
		task.execute();
	}

	/**
	 * ランキング表示を更新
	 * 
	 * @param result
	 */
	private void setRankingList(ArrayList<BookData> result) {
		final ArrayList<View> viewList = new ArrayList<View>();
		viewList.add(findViewById(R.id.ranking_item1));
		viewList.add(findViewById(R.id.ranking_item2));
		viewList.add(findViewById(R.id.ranking_item3));
		viewList.add(findViewById(R.id.ranking_item4));
		viewList.add(findViewById(R.id.ranking_item5));
		viewList.add(findViewById(R.id.ranking_item6));

		int i = 0;
		final int size = viewList.size();
		for (BookData data : result) {
			if (i < size) {
				setRankingView(data, viewList.get(i));
			}
			i++;
		}
	}

	private void setRankingView(final BookData data, View view) {
		final AQuery aq = new AQuery(view);
		aq.id(R.id.img_thumbnail).image(data.thumbnailUrl, true, true);
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Util.showItemDialog(MainActivity.this, data);
			}
		});
	}

	@Override
	public void onClick(View v) {
		final int id = v.getId();
		if (id == R.id.btn_search) {
			if (!Util.isNetworkAvailable(getApplicationContext())) {
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						this);
				alertDialogBuilder.setTitle("エラー");
				// アラートダイアログのメッセージを設定します
				alertDialogBuilder
						.setMessage("ネットワークに接続できません。¥n接続してから再度検索してください。");
				// アラートダイアログの肯定ボタンがクリックされた時に呼び出されるコールバックリスナーを登録します
				alertDialogBuilder.setPositiveButton("OK",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								return;
							}
						});
				alertDialogBuilder.create().show();
				return;
			}

			final EditText editText = (EditText) findViewById(R.id.edit_word);
			final String searchWord = editText.getText().toString();

			final Intent intent = new Intent(MainActivity.this,
					BookGridActivity.class);
			intent.putExtra(Const.INTENT_KEY_SEARCH_WORD, searchWord);
			intent.putExtra(Const.INTENT_KEY_SEARCH_GENRE, mSearchGenreId);
			startActivity(intent);

		}
	}
}
