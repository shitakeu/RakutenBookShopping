package jp.ksjApp.bookshopping.ui;

import java.util.ArrayList;

import jp.ksjApp.bookshopping.task.RakutenBookRankingApiTask;
import jp.ksjApp.bookshopping.R;
import jp.tksjApp.bookshopping.data.BookData;

import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class BookListActivity extends Activity {

	@SuppressWarnings("unused")
	private static final String TAG = BookListActivity.class.getSimpleName();

	private ListView mListView;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_list);
        
        final Bundle ext = getIntent().getExtras();
        if(ext != null){
//        	mSearchWord = ext.getString(Const.INTENT_KEY_SEARCH_WORD);
        }
        
        final String[] list = {"通信中"};
        mListView = (ListView)findViewById(R.id.listView1);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, 
        															android.R.layout.simple_list_item_1, 
        															list
        															);
        mListView.setAdapter(adapter);
        startApiTask();
    }
    
    private void startApiTask(){
    	final RakutenBookRankingApiTask task = new RakutenBookRankingApiTask() {
        	private ProgressDialog mDialog;
        	
        	@Override
        	protected void onPreExecute() {
        		super.onPreExecute();
        	    mDialog = new ProgressDialog(BookListActivity.this);
        	    mDialog.setTitle("Please wait");
        	    mDialog.setMessage("Uploading...");
        	    mDialog.show();
        	}
        	
        	public void onPostExecute(final ArrayList<BookData> result){
        		if(result == null){
        			final String[] list = {"通信失敗"};
        			final ArrayAdapter<String> adapter = new ArrayAdapter<String>(BookListActivity.this, 
            				android.R.layout.simple_list_item_1, list);
        			mListView.setAdapter(adapter);
        		}else if(result.size() <= 0){	
        			final String[] list = {"該当商品がありません"};
        			final ArrayAdapter<String> adapter = new ArrayAdapter<String>(BookListActivity.this, 
            				android.R.layout.simple_list_item_1, list);
        			mListView.setAdapter(adapter);
        		}else{
	        		final ShopItemAdapter adapter = new ShopItemAdapter(BookListActivity.this, 
	        				R.layout.list_item_row, result);
        			mListView.setAdapter(adapter);
        		}
        		
        		if(mDialog != null){
        			mDialog.dismiss();
        		}
        	}
        	
        };
        task.execute();
    }

}
