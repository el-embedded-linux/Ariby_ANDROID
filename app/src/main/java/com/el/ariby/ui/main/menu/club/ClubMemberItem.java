package com.el.ariby.ui.main.menu.club;

class ClubMemberItem {
    String resId;
    String txtNickname;

    public ClubMemberItem(String res, String nick) {
        this.resId = res;
        this.txtNickname = nick;
    }

    public String getResId() {
        return resId;
    }

    public void setResId(String res) {
        this.resId = res;
    }

    public String getTxtNickname() {
        return txtNickname;
    }

    public void setTxtNickname(String nickname) {
        this.txtNickname = nickname;
    }
}