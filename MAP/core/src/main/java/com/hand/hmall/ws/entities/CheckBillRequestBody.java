package com.hand.hmall.ws.entities;

import com.markor.map.framework.soapclient.entities.RequestBody;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author:zhangyanan
 * @Description:
 * @Date:Crated in 14:13 2017/10/17
 * @Modified By:
 */
@XmlRootElement(name = "ZMD_FI_001", namespace = "urn:sap-com:document:sap:rfc:functions")
@XmlAccessorType(XmlAccessType.FIELD)
public class CheckBillRequestBody extends RequestBody {

    @XmlElement(name = "item")
    @XmlElementWrapper(name = "T_INPUT")
    private List<CheckBillRequestItem> checkBillRequestItemList = new ArrayList<>();

    @XmlElement(name = "item")
    @XmlElementWrapper(name = "T_OUTPUT")
    private List<CheckBillResponseItem> checkBillResponseItemList = new ArrayList<>();

    public List<CheckBillRequestItem> getCheckBillRequestItemList() {
        return checkBillRequestItemList;
    }

    public void setCheckBillRequestItemList(List<CheckBillRequestItem> checkBillRequestItemList) {
        this.checkBillRequestItemList = checkBillRequestItemList;
    }

    public List<CheckBillResponseItem> getCheckBillResponseItemList() {
        return checkBillResponseItemList;
    }

    public void setCheckBillResponseItemList(List<CheckBillResponseItem> checkBillResponseItemList) {
        this.checkBillResponseItemList = checkBillResponseItemList;
    }
}
