package com.nta.teabreakorder.service.impl;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.WaitUntilState;
import com.nta.teabreakorder.model.Photo;
import com.nta.teabreakorder.model.Store;
import com.nta.teabreakorder.repository.StoreRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class Crawler {

    Logger logger = LoggerFactory.getLogger(Crawler.class);

    private final String STORE_INFO_URL = "https://gappapi.deliverynow.vn/api/delivery/get_detail";
    private final String MENU_INFO_URL = "https://gappapi.deliverynow.vn/api/dish/get_delivery_dishes";
    private static final Page.NavigateOptions options;
    private static final ObjectMapper objectMapper;

    static {
        // Init options
        options = new Page.NavigateOptions();
        options.setWaitUntil(WaitUntilState.NETWORKIDLE);
        options.setTimeout(60000);

        // Init objectMapper
        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }


    @Autowired
    private StoreRepository storeRepository;


    @Transactional(rollbackOn = Exception.class)
    public Store crawler(String url) throws Exception {
        Store store = null;
        Store willSaveStore = null;
        try (Playwright playwright = Playwright.create()) {

            logger.info("Start crawling {}", url);
            Browser browser = playwright.webkit().launch();
            Page page = browser.newPage();
            AtomicReference<String> jsonMenu = new AtomicReference<>("");
            AtomicReference<String> jsonStore = new AtomicReference<>("");

            page.onResponse(response -> {
                if ("xhr".equals(response.request().resourceType()) && response.url().contains(MENU_INFO_URL)) {
                    jsonMenu.set(response.text());
                }
                if ("xhr".equals(response.request().resourceType()) && response.url().contains(STORE_INFO_URL)) {
                    jsonStore.set(response.text());
                }
            });

            page.navigate(url, options);

            Map<String, Object> mapData = objectMapper.readValue(jsonMenu.get(), Map.class);
            Store newStore = objectMapper.readValue(objectMapper.writeValueAsString(mapData.get("reply")), new TypeReference<Store>() {
            });

            mapData = objectMapper.readValue(jsonStore.get(), Map.class);
            mapData = (Map<String, Object>) mapData.get("reply");
            mapData = objectMapper.readValue(objectMapper.writeValueAsString(mapData.get("delivery_detail")), Map.class);

            if (store != null) {
                store.setCategoryList(newStore.getCategoryList());
                store.setAddress(mapData.get("address").toString());
                store.setName(mapData.get("name").toString());
                store.setUrl(url);
                store.setPhotos((List<Photo>) mapData.get("photos"));
                willSaveStore = store;
            } else {
                willSaveStore = new Store();
                willSaveStore.setId(0L);
                willSaveStore.setCategoryList(newStore.getCategoryList());
                willSaveStore.setAddress(mapData.get("address").toString());
                willSaveStore.setName(mapData.get("name").toString());
                willSaveStore.setPhotos(objectMapper.readValue(objectMapper.writeValueAsString(mapData.get("photos")), new TypeReference<List<Photo>>() {
                }));
                willSaveStore.setUrl(url);
            }
            logger.info("Finish crawling {}", url);
            return storeRepository.save(willSaveStore);

        } catch (JsonMappingException e) {
            logger.error("Error crawling {}", "Parse json");
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            logger.error("Error crawling {}", "Processing json");
            e.printStackTrace();
        }
        throw new Exception("Can not create new store");
    }


}
