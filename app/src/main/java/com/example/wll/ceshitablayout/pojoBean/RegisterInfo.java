package com.example.wll.ceshitablayout.pojoBean;

/**
 * Created by wll on 2017/12/24.
 */

public class RegisterInfo {


    /**
     * code : 20000
     * msg : 登录成功
     * data : {"id":7,"name":"11111","email":"z@it1.me:1514130197","created_at":"2017-12-24 15:43:17","updated_at":"2017-12-24 15:43:17"}
     */

    private int code;
    private String msg;
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 7
         * name : 11111
         * email : z@it1.me:1514130197
         * created_at : 2017-12-24 15:43:17
         * updated_at : 2017-12-24 15:43:17
         */

        private int id;
        private String name;
        private String email;
        private String created_at;
        private String updated_at;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }
    }
}
