package rcd27.github.com.stasyandex.view.translation;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import rcd27.github.com.stasyandex.R;
import rcd27.github.com.stasyandex.di.DaggerTranslationComponent;
import rcd27.github.com.stasyandex.di.TranslationComponent;
import rcd27.github.com.stasyandex.di.TranslationModule;
import rcd27.github.com.stasyandex.presenter.BasePresenter;
import rcd27.github.com.stasyandex.presenter.translation.TranslationPresenter;
import rcd27.github.com.stasyandex.presenter.visualobject.Translation;
import rcd27.github.com.stasyandex.view.BaseFragment;
import rcd27.github.com.stasyandex.view.LanguagesActivity;

public class TranslationFragment extends BaseFragment implements TranslationView {

    private final String TAG = getClass().getSimpleName();

    @Bind(R.id.tv_languageFrom)
    TextView tvLanguageFrom;

    @Bind(R.id.ib_switch_direction)
    ImageButton ibSwitchDirection;

    @Bind(R.id.tv_languageTo)
    TextView tvLAnguageTo;

    @Bind(R.id.translation_editText)
    EditText editText;

    @Bind(R.id.bt_clearEditText)
    ImageButton btClearEditText;

    @Bind(R.id.tv_translation_result)
    TextView tvTranslationResult;

    @Inject
    TranslationPresenter presenter;

    private TranslateButtonListener listener;

    //TODO StartAndroid: объясняетя как такого избежать.
    private TranslationComponent component;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (TranslateButtonListener) context;
            Log.i(TAG, "onAttach: listener attached!");
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    " must implement TranslateButtonListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null == component) {
            component = DaggerTranslationComponent.builder()
                    .translationModule(new TranslationModule(this, listener))
                    .build();
        }
        component.inject(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        presenter.handleIntentForSelectedLanguages(data);
    }

    @OnClick(R.id.tv_languageFrom)
    public void onClickLanguageFrom() {
        presenter.onChooseLanguage(1);
    }

    @OnClick(R.id.tv_languageTo)
    public void onClickLanguageTo() {
        presenter.onChooseLanguage(2);
    }

    @OnClick(R.id.bt_clearEditText)
    public void onClearEditTextButtonClicked() {
        editText.setText("");
    }

    @OnTextChanged(R.id.translation_editText)
    public void onTextChanged() {
        presenter.onGetTranslation();
        listener.onTranslateButtonClicked(getTextFromEditText());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_translation, container, false);
        ButterKnife.bind(this, view);

        /*ДЛЯ ДЕФОЛТА*/
        showLanguageFrom("Русский");
        showLanguageTo("Сербский");

        return view;
    }

    private void makeToast(String text) {
        Snackbar.make(tvTranslationResult, text, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showError(String error) {
        makeToast(error);
    }


    @Override
    public String getTextFromEditText() {
        return editText.getText().toString();
    }

    @Override
    public void showTranslation(Translation resultTranslation) {
        tvTranslationResult.setText(resultTranslation.show());
    }

    @Override
    public void showEmptyResut() {
        tvTranslationResult.setText("");
    }

    @Override
    protected BasePresenter getPresenter() {
        return presenter;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    //TODO FIXME: теперь нифига не через кнопку
    public interface TranslateButtonListener {
        void onTranslateButtonClicked(String textFromEditText);
    }

    @Override
    public void chooseLanguage(final ArrayList<String> availableLangs, final int direction) {
        Intent intent = new Intent(getContext(), LanguagesActivity.class);
        intent.putExtra("direction", direction);
        intent.putExtra("availableLanguages", availableLangs);
        startActivityForResult(intent, 0);
    }

    @Override
    public void showLanguageFrom(String languageToShow) {
        tvLanguageFrom.setText(languageToShow);
    }

    @Override
    public void showLanguageTo(String languageToShow) {
        tvLAnguageTo.setText(languageToShow);
    }
}
