package com.zhuzhenkui.notepad.home.fragment;

import android.content.Context;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shengdan.base_lib.recylerview.BaseListFragmentV2;
import com.shengdan.base_lib.recylerview.ViewHolder;
import com.zhuzhenkui.notepad.home.entity.NoteContentEntity;
import com.zhuzhenkui.notepad.home.utils.BlankTouchListener;
import com.zhuzhenkui.notepad.home.vm.NotePadViewModel;
import com.zhuzhenkui.notepad.BR;
import com.zhuzhenkui.notepad.R;

import java.util.List;

public class NotePadFragment extends BaseListFragmentV2<NoteContentEntity, NotePadViewModel> {
    String title;

    @Override
    protected void initObserver() {
        mViewModel.noteListData.observe(getViewLifecycleOwner(), this::resNoteContentData);
        mViewModel.newNoteData.observe(getViewLifecycleOwner(), this::resNewNoteData);
        mViewModel.saveNoteEvent.observe(getViewLifecycleOwner(), this::saveEvent);
        //空白处锁定最后的焦点
        recyclerview.setOnTouchListener(new BlankTouchListener(recyclerview));
    }

    private void saveEvent(Integer noteId) {
        Log.d(TAG, "saveEvent: " + noteId + " " + title);
        //收集一下所有edittext的文本，第0项是标题，不需要处理
        String content = "";
        for (int i = 1; i < adapter.getData().size(); i++) {
            if (adapter.getData().get(i).getType() == 1 && TextUtils.isEmpty(content)) {
                EditText text = (EditText) adapter.getViewByPosition(recyclerview, i, R.id.content_text_et);
               if (text != null){
                   content = text.getText().toString();
                   break;
               }
            }

        }

        if (noteId == -1) {
            //insert a note
            mViewModel.insertNewNote(title, content, "杭州", 0, adapter.getData());
        } else {
            //update a note
            mViewModel.updateNoteAndContent(title, content, "杭州", 0, adapter.getData());
        }

    }

    private void resNewNoteData(NoteContentEntity noteContentEntity) {
        Log.d(TAG, "resNewNoteData() called with: noteContentEntity = [" + noteContentEntity + "]");
        adapter.addData(noteContentEntity);
    }

    private void resNoteContentData(List<NoteContentEntity> notePadEntities) {
        Log.d(TAG, "resNoteContentData: " + adapter.getData());
        //
        if (adapter.getData().size() == 0) {
            firstFetchComlpete(notePadEntities);
        } else {
            loadMoreComplete(notePadEntities);
            recyclerview.smoothScrollToPosition(adapter.getItemCount() - 1);
        }

    }


    @Override
    protected boolean isRefreshViewEnable() {
        return false;
    }

    @Override
    protected NotePadViewModel getViewModel() {
        return new ViewModelProvider(getActivity()).get(NotePadViewModel.class);
    }

    @Override
    protected void fetchDataByPage(int page) {
        Log.d(TAG, "fetchDataByPage: " + mViewModel.getNoteId());
        mViewModel.loadNote();
    }

    @Override
    protected View createFooterView() {
        return null;
    }

    @Override
    protected View createHeaderView() {
        return null;
    }

    @Override
    protected View createEmptyView() {
        return null;
    }

    @Override
    protected boolean isShowLoadMoreView() {
        return false;
    }

    @Override
    protected BaseQuickAdapter<NoteContentEntity, BaseViewHolder> createQuickAdapter() {
        setListBg(R.color.black);
        return new BaseQuickAdapter<NoteContentEntity, BaseViewHolder>(R.layout.item_content_string) {
            @Override
            protected void convert(@NonNull BaseViewHolder helper, NoteContentEntity item) {
                ((ViewHolder) helper).getBinding().setVariable(BR.item, item);
                ((ViewHolder) helper).getBinding().setVariable(BR.position, helper.getAdapterPosition());
                Log.d(TAG, "convert: " + helper.getAdapterPosition());
                if (helper.getItemViewType() == 1) {
                    EditText editText = helper.itemView.findViewById(R.id.content_text_et);

                    if (helper.getAdapterPosition() == 0) {
                        //标题栏
                        editText.setOnKeyListener((v, keyCode, event) -> {
                            if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                                mViewModel.addNewData();
                                editText.clearFocus();
                                nextFocus();
                                return true;
                            }
                            return false;
                        });
                        editText.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                            }

                            @Override
                            public void afterTextChanged(Editable s) {
                                title = s.toString();
                            }
                        });
                    } else {
                        Log.d(TAG, "convert: ddddddd");
                        editText.setOnKeyListener((v, keyCode, event) -> {
                            if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {
                                Editable text = editText.getText();
                                int length = text.length();
                                if (length <= 0 && helper.getAdapterPosition() != 0) {
                                    adapter.remove(helper.getAdapterPosition());
                                    return true;
                                }
                            }
                            Log.d(TAG, "convert: " + editText.getText());
                            return false;
                        });
                        editText.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {

                            }

                            @Override
                            public void afterTextChanged(Editable s) {
                                Log.d(TAG, "afterTextChanged: "+s+" "+helper.getAdapterPosition());
                                adapter.getData().get(helper.getAdapterPosition()).setContentString(String.valueOf(editText.getText()));
                            }
                        });



                    }
                }

                if (helper.getItemViewType() == 2) {
                    EditText editText = helper.itemView.findViewById(R.id.conntent_img_et);
                    editText.setOnKeyListener((v, keyCode, event) -> {
                        if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {
                            editText.setFilters(new InputFilter[]{});
                            adapter.remove(helper.getAdapterPosition());
                        }
                        return false;
                    });
                    editText.post(() -> editText.setSelection(editText.getText().length()));
                }

                if (helper.getItemViewType() == 4) {
                    EditText editText = helper.itemView.findViewById(R.id.conntent_audio_et);

                    editText.setOnKeyListener((v, keyCode, event) -> {
                        if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {
                            editText.setFilters(new InputFilter[]{});
                            adapter.remove(helper.getAdapterPosition());
                        }
                        return false;
                    });
                }
            }

            @Override
            protected int getDefItemViewType(int position) {
                return getData().get(position).getType();
            }

            @Override
            protected BaseViewHolder onCreateDefViewHolder(ViewGroup parent, int viewType) {
                ViewDataBinding binding = null;
                switch (viewType) {
                    case 1:
                        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_content_string, parent, false);
                        break;
                    case 2:
                        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_content_img, parent, false);
                        break;
                    case 3:
                        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_content_write, parent, false);
                        break;
                    case 4:
                        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_content_audio, parent, false);
                        break;
                }
                return new ViewHolder(binding);
            }
        };
    }

    private void nextFocus() {
        new Handler().postDelayed(() -> {
            LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerview.getLayoutManager();
            int lastPosition = layoutManager.getItemCount() - 1;
            ViewGroup lastItem = (ViewGroup) layoutManager.findViewByPosition(lastPosition);

            if (lastItem != null) {
                View view = lastItem.getChildAt(0);
                if (view instanceof EditText) {
                    view.requestFocus();
                    ((EditText) view).setSelection(((EditText) view).getText().length());
                    InputMethodManager imm = (InputMethodManager) recyclerview.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                }
            }
        }, 100);
    }

    @Override
    protected RecyclerView.LayoutManager createLayoutManager() {
        return new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
    }

    @Override
    protected int pageNum() {
        return Integer.MAX_VALUE;
    }
}
