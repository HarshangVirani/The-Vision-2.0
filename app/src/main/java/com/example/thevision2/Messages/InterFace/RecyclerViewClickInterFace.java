package com.example.thevision2.Messages.InterFace;

import com.example.thevision2.Messages.Model.SMSModel;

public interface RecyclerViewClickInterFace {
    void onItemClick(SMSModel smsModel);
    void onDoubleItemClick(SMSModel smsModel);
    void onLongItemClick();
}
