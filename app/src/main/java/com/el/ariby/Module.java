package com.el.ariby;

public class Module {
    public Boolean isNameCheck(String name) {
        if(name.matches("[0-9|a-z|A-Z|ㄱ-ㅎ|ㅏ-ㅣ|가-힝]*"))
            return true;
        else
            return false;
    }
}
