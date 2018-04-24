package com.example.atuski.qiitaqlient;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.atuski.qiitaqlient.ui.searchhistory.SearchHistoryFragment;
import com.example.atuski.qiitaqlient.ui.toolbar.ToolbarFragment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;


public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_URL = "URL";

    public QiitaQlientApp app;
    private ViewFragmentPagerAdapter viewPagerAdapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // レイアウトファイルからFragmentを呼び込むようにした。
        setContentView(R.layout.main_activity);

        Log.v("MainActivity", "onCreate");

        ToolbarFragment toolbarFragment = new ToolbarFragment();
//        getFragmentManager()
//                .beginTransaction()
//                .replace(R.id.fragment_container, toolbarFragment)
//                .commit();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, toolbarFragment)
                .commit();


        // SearchHistory用の処理
        // todo ViewPagerの処理を他に譲渡するかどうか
        /**
         * ViewPagerFragmentを作って、
         *   そっちにBundle経由で渡すという方法がある。
         *   これをやるには他のFragmentの例を見てもう少し経験値が必要そう。
         */
//        Intent intent = getIntent();
//        String searchHistory = intent.getStringExtra(SearchHistoryActivity.FROM_SEARCH_HISTORY);
//        SearchFragment searchFragment = new SearchFragment();
//        if (searchHistory != null && searchHistory.length() != 0) {
//            Bundle bundle = new Bundle();
//            bundle.putString(SearchHistoryActivity.FROM_SEARCH_HISTORY, searchHistory);
//            searchFragment.setArguments(bundle);
//        }
//
//        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
//        viewPager = (ViewPager) findViewById(R.id.viewPager);
//        viewPagerAdapter = new ViewFragmentPagerAdapter(getSupportFragmentManager());
//        viewPagerAdapter.addFragments(searchFragment, "Search");
//        viewPagerAdapter.addFragments(new TrendFragment(), "Trend");
//        viewPagerAdapter.addFragments(new SubFragment(), "Sub");
//        viewPager.setAdapter(viewPagerAdapter);
//        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search_history:
                SearchHistoryFragment searchHistoryFragment = new SearchHistoryFragment();
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, searchHistoryFragment)
                        .addToBackStack(null)
                        .commit();
            case R.id.login:

                String mClientId = "dfd44c0b8c380894cac1ea43ff4b815a2661e461";
                String mScope = "read_qiita write_qiita";
                String mState = "bb17785d811bb1913ef54b0a7657de780defaa2d";//todo to be random
                String uri = "https://qiita.com/api/v2/oauth/authorize?" +
                        "client_id=" + mClientId +
                        "&scope=" + mScope +
                        "&state=" + mState;

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(intent);

            case R.id.login_check:

                Intent intent2 = getIntent();
                String action = intent2.getAction();
                Log.v("MainActivityonResume", action);
                //todo intent.getData()について調べる
                Uri uri2 = intent2.getData();
                if (uri2 != null) {
                    Log.v("MainActivityonResume", uri2.toString());
                    Log.v("MainActivityonResume", uri2.getQueryParameter("code").toString());
                }

                HttpAsync task = new HttpAsync();
                if (uri2 != null) {
                    task.execute(uri2.getQueryParameter("code").toString());
                }



            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onResume() {
        super.onResume();

//        Intent intent = getIntent();
//        String action = intent.getAction();
//        Log.v("MainActivityonResume", action);
//        //todo intent.getData()について調べる
//        Uri uri = intent.getData();
//
//        if (uri != null) {
//            Log.v("MainActivityonResume", uri.toString());
//        }
//


    }

    @Override
    public void onBackPressed() {
        Log.v("MainActivity", "onBackPressed");
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        super.onBackPressed();
    }

    private class HttpAsync extends AsyncTask<String, Void, Void> {

        public HttpAsync() {
        }

        @Override
        protected Void doInBackground(String... accessToken) {

            HttpURLConnection urlConnection1 = null;
            HttpURLConnection urlConnection = null;
            try {
                URL url1 = new URL("https://qiita.com/api/v2/access_tokens");

                urlConnection1 = (HttpURLConnection) url1.openConnection();
                urlConnection1.setRequestMethod("POST");
                urlConnection1.setDoInput(true);
                urlConnection1.setDoOutput(true);
                urlConnection1.addRequestProperty("Content-Type", "application/json");


                String json = "{\"client_id\":\"dfd44c0b8c380894cac1ea43ff4b815a2661e461\", \"client_secret\":\"093660d3c232d54d33c09b7c2d9465ad8bb60202\", \"code\":\""+ accessToken[0] + "\"}";
                Log.v("json", json);

//                String parameterString = new String("client_id=dfd44c0b8c380894cac1ea43ff4b815a2661e461&client_secret=093660d3c232d54d33c09b7c2d9465ad8bb60202&code=" + accessToken[0]);
                OutputStreamWriter out = new OutputStreamWriter(urlConnection1.getOutputStream());
                out.write(json);
                out.flush();

//                String getTokenStr = InputStreamToString(urlConnection1.getInputStream());
                urlConnection1.connect();

                Log.v("Response code", String.valueOf(urlConnection1.getResponseCode()));


                InputStream is = urlConnection1.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                br.close();
                String getTokenStr = sb.toString();


                Log.v("Accesstoken", getTokenStr);



//                URL url = new URL("https://qiita.com/api/v2/authenticated_user");
//
//                urlConnection = (HttpURLConnection) url.openConnection();
//                urlConnection.setRequestMethod("GET");
//                urlConnection.setRequestProperty("Authorization", "Bearer " + accessToken[0]);
//
//                Log.d("HTTP", accessToken[0]);

//                String str = InputStreamToString(urlConnection.getInputStream());
//                Log.d("HTTP", str);

//                urlConnection.setDoOutput(true);
//                urlConnection.setChunkedStreamingMode(0);
//                OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
//                writeStream(out);
//                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
//                readStream(in);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                urlConnection.disconnect();
            }
            return null;
        }

        private String InputStreamToString(InputStream is) throws IOException {
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            br.close();
            return sb.toString();
        }
    }

}
