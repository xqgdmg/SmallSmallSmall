package com.example.administrator.myapplication.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/11/21.
 * description： 测试系统配置bean类
 */
public class Configures {

    private MessageEntity message;
    private boolean succeeded;
    private String error;
    private DataEntity data;

    public void setMessage(MessageEntity message) {
        this.message = message;
    }

    public void setSucceeded(boolean succeeded) {
        this.succeeded = succeeded;
    }

    public void setError(String error) {
        this.error = error;
    }

    public void setData(DataEntity data) {
        this.data = data;
    }

    public MessageEntity getMessage() {
        return message;
    }

    public boolean isSucceeded() {
        return succeeded;
    }

    public String getError() {
        return error;
    }

    public DataEntity getData() {
        return data;
    }

    public class MessageEntity {
        /**
         * descript : null
         * type : success
         */
        private String descript;
        private String type;

        public void setDescript(String descript) {
            this.descript = descript;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getDescript() {
            return descript;
        }

        public String getType() {
            return type;
        }
    }

    public class DataEntity {
        /**
         * id : 1
         * level : [{"min":100,"max":199,"_id":"576aa269905b4fab75127964","name":"lv1"},{"min":200,"max":299,"_id":"576aa269905b4fab75127963","name":"lv2"},{"min":300,"max":399,"_id":"576aa269905b4fab75127962","name":"lv3"},{"min":400,"max":499,"_id":"576aa269905b4fab75127961","name":"lv4"}]
         * carriage : {"amount":0,"condition":0}
         * storeDistance : 5
         * search : ["手机维修","iphone6","三星Note4","iphone5","保洁"]
         * isStoreOpen : true
         * aboutusUrl : http://baidu.com
         * exchange : [{"balance":1,"_id":"576aa269905b4fab7512796a","credit":100},{"balance":2,"_id":"576aa269905b4fab75127969","credit":200},{"balance":3,"_id":"576aa269905b4fab75127968","credit":300},{"balance":4,"_id":"576aa269905b4fab75127967","credit":400},{"balance":5,"_id":"576aa269905b4fab75127966","credit":500},{"balance":10,"_id":"576aa269905b4fab75127965","credit":1000}]
         */
        private String id;
        private List<LevelEntity> level;
        private CarriageEntity carriage;
        private int storeDistance;
//        private List<SearchEntity> search;  // 这个字段不用了
        private boolean isStoreOpen;
        private String aboutusUrl;
        private List<ExchangeEntity> exchange;

        public void setId(String id) {
            this.id = id;
        }

        public void setLevel(List<LevelEntity> level) {
            this.level = level;
        }

        public void setCarriage(CarriageEntity carriage) {
            this.carriage = carriage;
        }

        public void setStoreDistance(int storeDistance) {
            this.storeDistance = storeDistance;
        }

//        public void setSearch(List<SearchEntity> search) {
//            this.search = search;
//        }

        public void setIsStoreOpen(boolean isStoreOpen) {
            this.isStoreOpen = isStoreOpen;
        }

        public void setAboutusUrl(String aboutusUrl) {
            this.aboutusUrl = aboutusUrl;
        }

        public void setExchange(List<ExchangeEntity> exchange) {
            this.exchange = exchange;
        }

        public String getId() {
            return id;
        }

        public List<LevelEntity> getLevel() {
            return level;
        }

        public CarriageEntity getCarriage() {
            return carriage;
        }

        public int getStoreDistance() {
            return storeDistance;
        }

//        public List<SearchEntity> getSearch() {
//            return search;
//        }

        public boolean isIsStoreOpen() {
            return isStoreOpen;
        }

        public String getAboutusUrl() {
            return aboutusUrl;
        }

        public List<ExchangeEntity> getExchange() {
            return exchange;
        }

        public class LevelEntity {
            /**
             * min : 100
             * max : 199
             * _id : 576aa269905b4fab75127964
             * name : lv1
             */
            private int min;
            private int max;
            private String _id;
            private String name;

            public void setMin(int min) {
                this.min = min;
            }

            public void setMax(int max) {
                this.max = max;
            }

            public void set_id(String _id) {
                this._id = _id;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getMin() {
                return min;
            }

            public int getMax() {
                return max;
            }

            public String get_id() {
                return _id;
            }

            public String getName() {
                return name;
            }
        }

        public class CarriageEntity {
            /**
             * amount : 0
             * condition : 0
             */
            private int amount;
            private int condition;

            public void setAmount(int amount) {
                this.amount = amount;
            }

            public void setCondition(int condition) {
                this.condition = condition;
            }

            public int getAmount() {
                return amount;
            }

            public int getCondition() {
                return condition;
            }
        }

        public class ExchangeEntity {
            /**
             * balance : 1
             * _id : 576aa269905b4fab7512796a
             * credit : 100
             */
            private int balance;
            private String _id;
            private int credit;

            public void setBalance(int balance) {
                this.balance = balance;
            }

            public void set_id(String _id) {
                this._id = _id;
            }

            public void setCredit(int credit) {
                this.credit = credit;
            }

            public int getBalance() {
                return balance;
            }

            public String get_id() {
                return _id;
            }

            public int getCredit() {
                return credit;
            }
        }

        @Override
        public String toString() {
            return "DataEntity{" +
                    "id='" + id + '\'' +
                    ", level=" + level +
                    ", carriage=" + carriage +
                    ", storeDistance=" + storeDistance +
                    ", isStoreOpen=" + isStoreOpen +
                    ", aboutusUrl='" + aboutusUrl + '\'' +
                    ", exchange=" + exchange +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "Configures{" +
                "message=" + message +
                ", succeeded=" + succeeded +
                ", error='" + error + '\'' +
                ", data=" + data +
                '}';
    }
}
