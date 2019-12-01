package com.android.makeyousmile.ui.Utility;

import com.android.makeyousmile.ui.model.TransactionModel;

public interface TransactionItemListener {
    void onOrderItemClicked(TransactionModel orders, String status);
}
