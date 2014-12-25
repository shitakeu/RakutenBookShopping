package jp.ksjApp.bookshopping.task;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import jp.ksjApp.bookshopping.Const;
import jp.ksjApp.bookshopping.parser.GenreParser;
import jp.ksjApp.bookshopping.parser.RakutenParser;
import jp.tksjApp.bookshopping.data.BookData;
import jp.tksjApp.bookshopping.data.GenreData;
import android.os.AsyncTask;
import android.util.Log;

public class BookGenreApiTask extends AsyncTask<String, Void, ArrayList<GenreData>> {

	@SuppressWarnings("unused")
	private static final String TAG = BookGenreApiTask.class.getSimpleName();

	public BookGenreApiTask() {
	}

	@Override
	protected ArrayList<GenreData> doInBackground(String... params) {
		return requestApi(params[0]);
	}

	private ArrayList<GenreData> requestApi(String genreId) {

		ArrayList<GenreData> data = null;
		final HttpClient httpClient = new DefaultHttpClient();
		try {
			final HttpGet request = new HttpGet(createRakutenBookApiUrl(genreId));
			final HttpResponse httpResponse = httpClient.execute(request);
			final InputStream in = httpResponse.getEntity().getContent();
			final GenreParser rakutenParser = new GenreParser();
			data = rakutenParser.xmlParser(in);
			in.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return data;
	}

	private String createRakutenBookApiUrl(String genreId) {
		final StringBuffer strbuf = new StringBuffer();
		strbuf.append(Const.RAKUTEN_BOOK_GENRE_API_URL);
		strbuf.append("booksGenreId=" + genreId);
		strbuf.append("&");
		strbuf.append("applicationId=" + Const.RAKUTEN_APP_ID);
		strbuf.append("&");
		strbuf.append("affiliateId=" + Const.RAKUTEN_AFFILIATEI_ID);
		strbuf.append("&");
		strbuf.append("format=xml");

		return strbuf.toString();
	}
}
