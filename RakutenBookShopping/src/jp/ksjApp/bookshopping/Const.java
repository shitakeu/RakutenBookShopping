package jp.ksjApp.bookshopping;

public class Const {

	public static final boolean _DEBUG_ = true;
	
	public static final int API_TIMEOUT = 30000;

	// Google Suggest API
	public static final String SUGGEST_API = "http://www.google.com/complete/search?hl=jp&output=toolbar&q=";

	/**
	 * 楽天API関連
	 */
	public static final String RAKUTEN_APP_ID = "1064840937862996285";
	public static final String RAKUTEN_AFFILIATEI_ID = "117fe8b8.858f41ba.117fe8b9.90504b56";

	// 楽天ブックス書籍検索API
	// https://webservice.rakuten.co.jp/api/booksbooksearch2/
	public static final String RAKUTEN_BOOK_API_URL = "https://app.rakuten.co.jp/services/api/BooksBook/Search/20130522?";

	// 楽天ブックスジャンル検索API2
	// https://webservice.rakuten.co.jp/api/booksgenresearch2/
	public static final String RAKUTEN_BOOK_GENRE_API_URL = "https://app.rakuten.co.jp/services/api/BooksGenre/Search/20121128?";
	
	public static final String RAKUTEN_BOOK_GENRE_ID = "200162";

	public static final String RAKUTEN_RANLING_API_URL = "https://app.rakuten.co.jp/services/api/IchibaItem/Ranking/20120927?format=xml&applicationId="
			+ RAKUTEN_APP_ID + "&genreId=" + RAKUTEN_BOOK_GENRE_ID + "&affiliateId=" + RAKUTEN_AFFILIATEI_ID;

	// Intent Key
	public static final String INTENT_KEY_SEARCH_WORD = "search_word";
	public static final String INTENT_KEY_SEARCH_GENRE = "search_genre";

	// APIの種�?	// �?��天ブックス書籍検索API
	public static final int RAKUTEN_API_TYPE_BOOK = 0;
	// 楽天ブックスジャンル検索API
	public static final int RAKUTEN_API_TYPE_BOOK_GENRE = 1;

}
