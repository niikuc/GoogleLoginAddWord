package com.niikuc.googleloginaddword;

/**
 * Created by Николче on 09.5.2018.
 */

public class MacWord {
        public String userName;
        public String word;
        public String description;

        public MacWord(){

        }

        public MacWord(String userName,String word,String description){
            this.userName=userName;
            this.word=word;
            this.description=description;
        }

        public String getUserName() {
        return userName;
        }

        public String getWord() {
            return word;
        }

        public String getDescription() {
            return description;
        }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    public void setWord(String word) {
            this.word = word;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }


