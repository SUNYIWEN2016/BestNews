package com.dyx.bestnews.entity;

import java.util.List;

/**
 * Created by Administrator on 2016/8/31 0031.
 */
public class NetEaseType {
    private List<TList> tList;

    public NetEaseType(List<TList> tList) {
        this.tList = tList;
    }

    public List<TList> gettList() {
        return tList;
    }

    public void settList(List<TList> tList) {
        this.tList = tList;
    }

    public static class TList {
        private String tname;
        private String tid;

        public String getTname() {
            return tname;
        }

        public void setTname(String tname) {
            this.tname = tname;
        }

        public String getTid() {
            return tid;
        }

        public void setTid(String tid) {
            this.tid = tid;
        }

        public TList(String tname, String tid) {
            this.tname = tname;
            this.tid = tid;
        }
    }
}
