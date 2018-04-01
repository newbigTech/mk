package com.hand.hmall.ws.entities;

import com.markor.map.framework.soapclient.entities.RequestBody;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * Created by qinzhipeng on 2017/11/13.
 */
@XmlRootElement(name = "ZMD_SD_001", namespace = "urn:sap-com:document:sap:rfc:functions")
@XmlAccessorType(XmlAccessType.FIELD)
public class OrderToEccRequestBody extends RequestBody {
    @XmlElement(name = "T_DATA")
    private TData tData;

    public TData gettData() {
        return tData;
    }

    public void settData(TData tData) {
        this.tData = tData;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class TData {
        @XmlElements(value = @XmlElement(name = "item"))
        private List<OrderToEccItem> items;

        public List<OrderToEccItem> getItems() {
            return items;
        }

        public void setItems(List<OrderToEccItem> items) {
            this.items = items;
        }


        @XmlAccessorType(XmlAccessType.FIELD)
        public static class OrderToEccItem {
            @XmlElement(name = "ESC_ORDER_CODE")
            private String escOrderCode;
            @XmlElement(name = "CODE")
            private String code;
            @XmlElement(name = "CODELINE")
            private String codeLine;
            @XmlElement(name = "ZMATNR")
            private String zmatnr;

            public String getEscOrderCode() {
                return escOrderCode;
            }

            public void setEscOrderCode(String escOrderCode) {
                this.escOrderCode = escOrderCode;
            }

            public String getCode() {
                return code;
            }

            public void setCode(String code) {
                this.code = code;
            }

            public String getCodeLine() {
                return codeLine;
            }

            public void setCodeLine(String codeLine) {
                this.codeLine = codeLine;
            }

            public String getZmatnr() {
                return zmatnr;
            }

            public void setZmatnr(String zmatnr) {
                this.zmatnr = zmatnr;
            }
        }
    }
}
