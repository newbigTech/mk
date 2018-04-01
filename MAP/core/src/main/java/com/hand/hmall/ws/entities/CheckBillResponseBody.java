package com.hand.hmall.ws.entities;

import com.markor.map.framework.soapclient.entities.ResponseBody;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author:zhangyanan
 * @Description:
 * @Date:Crated in 16:28 2017/10/17
 * @Modified By:
 */
@XmlRootElement(name = "ZMD_FI_001Response", namespace = "rn:sap-com:document:sap:rfc:functions")
@XmlAccessorType(XmlAccessType.FIELD)
public class CheckBillResponseBody extends ResponseBody {

    @XmlElement(name = "item")
    @XmlElementWrapper(name = "T_OUTPUT")
    private List<CheckBillResponseItem> checkBillResponseItemList = new ArrayList<>();

    public List<CheckBillResponseItem> getCheckBillResponseItemList() {
        return checkBillResponseItemList;
    }

    public void setCheckBillResponseItemList(List<CheckBillResponseItem> checkBillResponseItemList) {
        this.checkBillResponseItemList = checkBillResponseItemList;
    }
}
