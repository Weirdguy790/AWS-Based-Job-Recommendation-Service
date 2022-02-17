package com.laioffer.job.external;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.laioffer.job.entity.Item;
import org.apache.http.HttpEntity;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GitHubClient {
    private static final String URL_TEMPLATE = "https://jobs.github.com/positions.json?description=%s&lat=%s&long=%s";  //%s : String 占位符
    private static final String DEFAULT_KEYWORD = "engineer";

    public List<Item> search(double lat, double lon, String keyword) {
        if (keyword == null) {
            keyword = DEFAULT_KEYWORD;
        }


        // eg. “hello world” => “hello%20world”
        try {
            keyword = URLEncoder.encode(keyword, "UTF-8");    //transfer keyword to URL
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String url = String.format(URL_TEMPLATE, keyword, lat, lon); //format URL from above

        CloseableHttpClient httpclient = HttpClients.createDefault();

        // Create a custom response handler
        ResponseHandler<List<Item>> responseHandler = response -> {
            if (response.getStatusLine().getStatusCode() != 200) {
                return Collections.emptyList(); //no results
            }
            HttpEntity entity = response.getEntity();
            if (entity == null) {
                return Collections.emptyList(); //no results
            }
            //return EntityUtils.toString(entity); //entity type : application/json
            ObjectMapper mapper = new ObjectMapper();
            Item[] itemArray = mapper.readValue(entity.getContent(), Item[].class); //.class :
            return Arrays.asList(itemArray);
        };

        try {
            return httpclient.execute(new HttpGet(url), responseHandler);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }
}
