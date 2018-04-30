package com.github.rcd27.stasyandex.dictionary;


import android.support.annotation.*;

import com.github.rcd27.stasyandex.common.*;

import io.reactivex.android.schedulers.*;
import io.reactivex.disposables.*;
import io.reactivex.schedulers.*;

public class DictionaryPresenter extends BasePresenter implements DictionaryContract.Presenter {

  private final DictionaryContract.View view;
  private final DictionaryContract.Api api;

  public DictionaryPresenter(DictionaryContract.View view, @NonNull DictionaryContract.Api api) {
    this.view = view;
    this.api = api;
  }

  public void getDictionaryResponseFor(@NonNull String direction, @NonNull String text) {
    addDisposable(getSubscriptionForDictionaryDefinition(direction, text));
  }

  @NonNull
  private Disposable getSubscriptionForDictionaryDefinition(String direction, String text) {
    return api.getDicResultFor(direction, text, "ru")
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnError(throwable -> view.showEmpty())
        .subscribe(dicResult -> {
          if (!dicResult.definitionListIsEmptyOrNull()) {
            view.showDefinition(dicResult.getDefinition());
            view.showDictionaryItems(dicResult.getElementsList());
          } else {
            view.showEmpty();
          }
        });
  }
}