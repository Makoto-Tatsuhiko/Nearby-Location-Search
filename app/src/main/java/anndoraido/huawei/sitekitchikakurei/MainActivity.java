package anndoraido.huawei.sitekitchikakurei;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.huawei.hms.site.api.SearchResultListener;
import com.huawei.hms.site.api.SearchService;
import com.huawei.hms.site.api.SearchServiceFactory;
import com.huawei.hms.site.api.model.AddressDetail;
import com.huawei.hms.site.api.model.Coordinate;
import com.huawei.hms.site.api.model.DetailSearchRequest;
import com.huawei.hms.site.api.model.DetailSearchResponse;
import com.huawei.hms.site.api.model.HwLocationType;
import com.huawei.hms.site.api.model.NearbySearchRequest;
import com.huawei.hms.site.api.model.NearbySearchResponse;
import com.huawei.hms.site.api.model.SearchStatus;
import com.huawei.hms.site.api.model.Site;
import com.huawei.hms.site.api.model.TextSearchRequest;
import com.huawei.hms.site.api.model.TextSearchResponse;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    double lat = 48.8566;
    double lng = 2.3522;


    private static final String TAG = "MainActivity";

    private SearchService searchService;
    private TextView resultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        try {
            searchService = SearchServiceFactory.create(this, URLEncoder.encode("CgB6e3x9/AnhFfLRcY+Wf7MY3zljwu09AiUn5Wuf+Tcvv1ksEqQuAKBq0ym+Q5U4MkwLayDlPpqgYVPrkMwRsdHX", "utf-8"));
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, "encode apikey error");
        }

        resultTextView = findViewById(R.id.response_text_search);
    }




    public void search(View view) {

        NearbySearchRequest request = new NearbySearchRequest();
        Coordinate location = new Coordinate(lat,lng);
        request.setLocation(location);
        request.setQuery("");
        request.setRadius(1000);
        request.setHwPoiType(HwLocationType.ADDRESS);
        //request.setLanguage("fr");
        request.setPageIndex(1);
        request.setPageSize(20);
        //request.setStrictBounds(false);
// Create a search result listener.
        SearchResultListener<NearbySearchResponse> resultListener = new SearchResultListener<NearbySearchResponse>() {
            // Return search results upon a successful search.
            @Override
            public void onSearchResult(NearbySearchResponse results) {
                if (results == null || results.getTotalCount() <= 0) {
                    resultTextView.setText("Result is Empty!");
                    return;
                }

                List<Site> sites = results.getSites();
                ArrayList<String> siteIDs = new ArrayList<>();

                if (sites == null || sites.size() == 0) {
                    resultTextView.setText("Result is Empty!");
                    return;
                }

                StringBuilder response = new StringBuilder("\n");
                response.append("success\n");
                int count = 1;
                AddressDetail addressDetail;
                for (Site site : sites) {

                    //detailSearch(site.getSiteId());

                    /*Toast.makeText(getApplicationContext(), "Latitude" + site.getLocation().getLat()
                            + "\n" + "Longitude" + site.getLocation().getLng() + site.getSiteId()+ "Business Status: " + site.getPoi().businessStatus+
                            "Opening HOur: "+site.getPoi().openingHours +
                            "Price Level: " + site.getPoi().priceLevel +
                            "Website: " + site.getPoi().websiteUrl,Toast.LENGTH_SHORT).show();*/

                    Log.i("TAG", String.format("siteId: '%s', name: %s\r\n", site.getSiteId(), site.getName()));

                    addressDetail = site.getAddress();
                    response
                            .append(String.format("[%s]  name: %s, formatAddress: %s, country: %s, countryCode: %s \r\n",
                                    "" + (count++), site.getName(), site.getFormatAddress(),
                                    //"\nlatitude" + site.getLocation().getLat(),
                                    //"\nLongitude" + site.getLocation().getLng(),
                                    //"Phone: " + site.getPoi().phone,
                                    "\n\nWebsite URL: " + site.getPoi().getWebsiteUrl()+"\n",
                                    "\nPhoto URL: " + site.getPoi().getPhotoUrls()+"\n\n",

                                    //"\n\n\nSite ID: " + site.getSiteId(),
                                    //"International Phone: " + site.getPoi().internationalPhone,

                                    (addressDetail == null ? "" : addressDetail.getCountry()),
                                    (addressDetail == null ? "" : addressDetail.getCountryCode())));

                    siteIDs.add(site.getSiteId());

                }

                //detailSearch(siteIDs);

                Log.d(TAG, "search result is : " + response);
                resultTextView.setText(response.toString());



            }

            // Return the result code and description upon a search exception.
            @Override
            public void onSearchError(SearchStatus status) {
                Log.i("TAG", "Error : " + status.getErrorCode() + " " + status.getErrorMessage());
            }
        };
// Call the nearby place search API.
        searchService.nearbySearch(request, resultListener);

    }

  /*  public void detailSearch(String siteID) {
        // Declare a SearchService object.
        SearchService searchService;
// Instantiate the SearchService object.
        searchService = SearchServiceFactory.create(this, "CgB6e3x9/AnhFfLRcY+Wf7MY3zljwu09AiUn5Wuf+Tcvv1ksEqQuAKBq0ym+Q5U4MkwLayDlPpqgYVPrkMwRsdHX");
// Create a request body.
        DetailSearchRequest request = new DetailSearchRequest();
        request.setSiteId(siteID);
        //request.setLanguage("fr");
        request.setChildren(false);
// Create a search result listener.
        SearchResultListener<DetailSearchResponse> resultListener = new SearchResultListener<DetailSearchResponse>() {
            // Return search results upon a successful search.
            @Override
            public void onSearchResult(DetailSearchResponse result) {
                Site site;

                if (result == null || (site = result.getSite()) == null) {

                    return;
                }
                Toast.makeText(getApplicationContext(),site.getName(),Toast.LENGTH_LONG).show();

                Log.i("TAG", String.format("siteId: '%s', name: %s\r\n", site.getSiteId(), site.getName()));
            }

            // Return the result code and description upon a search exception.
            @Override
            public void onSearchError(SearchStatus status) {


            }
        };
    }*/
}
