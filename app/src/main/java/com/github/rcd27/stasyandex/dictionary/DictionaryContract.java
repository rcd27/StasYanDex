package com.github.rcd27.stasyandex.dictionary;

import com.github.rcd27.stasyandex.common.*;
import com.github.rcd27.stasyandex.data.dictionary.*;
import com.github.rcd27.stasyandex.dictionary.visual.*;

import java.util.*;

import io.reactivex.*;
import retrofit2.http.*;

public interface DictionaryContract {

  interface View extends BaseView {

    void showDefinition(DictionaryVisualDefinition definition);

    void showDictionaryItems(List<DictionaryVisualItem> items);
  }

  interface Presenter {

  }

  interface Api {
    //TODO прикрутить возможность получать укороченную pos(часть речи)
    //https://tech.yandex.ru/dictionary/doc/dg/reference/lookup-docpage/
    @GET("api/v1/dicservice.json/lookup")
    Single<DicResult> getDicResultFor(@Query("lang") String languageDirection,
                                      @Query("text") String text,
                                      @Query("ui") String inLanguage);
  }
}
