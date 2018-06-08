package kr.or.dgit.it.http_study.recyclerview;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import kr.or.dgit.it.http_study.R;

public class ItemFragment extends DialogFragment {
    private OnUpdateItem onUpdateItem;
    private OnInsertItem onInsertItem;

    private int position;
    private boolean isNewItem;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            onUpdateItem = (OnUpdateItem) context;
            onInsertItem = (OnInsertItem) context;
        } catch (ClassCastException e) {
            String msg = " must implement NoticeDialogListener";
            throw new ClassCastException(context.toString() + msg);
        }
    }

    public static ItemFragment getInstance(Item item, int position){
        ItemFragment frg = new ItemFragment();
        Bundle args = new Bundle();
        if (item == null) {
            item = new Item();
            item.setImg(R.drawable.android_image_2);
            args.putBoolean("newItem", true);
        }
        args.putSerializable("item", item);
        args.putInt("position", position);
        frg.setArguments(args);
        return frg;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle args = getArguments();
        final Item item = (Item) args.getSerializable("item");
        position = args.getInt("position");
        isNewItem = args.getBoolean("newItem");

        RelativeLayout layout = (RelativeLayout) getActivity().getLayoutInflater().inflate(R.layout.fragment_item, null);

        final  EditText etTitle = (EditText) layout.findViewById(R.id.item_title);
        etTitle.setText(item.getTitle());

        final EditText etDetail = (EditText) layout.findViewById(R.id.item_detail);
        etDetail.setText(item.getDetail());

        final ImageView image = (ImageView) layout.findViewById(R.id.item_image);
        image.setImageResource(item.getImg());

        return new AlertDialog.Builder(getActivity())
                .setIcon(R.mipmap.ic_launcher)
                .setTitle(isNewItem?"추가":item.getTitle() + " 수정")
                .setView(layout)
                .setPositiveButton(isNewItem?"추가":"수정", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Item newItem = new Item(etTitle.getText().toString(), etDetail.getText().toString(), item.getImg());
                        if (isNewItem){
                            onInsertItem.onInsertItem(newItem, position);
                        }else {
                            onUpdateItem.onUpdateItem(newItem, position);
                        }
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .create();
    }

    public interface OnUpdateItem{
        void onUpdateItem(Item item, int position);
    }

    public interface OnInsertItem{
        void onInsertItem(Item item, int position);
    }
}


