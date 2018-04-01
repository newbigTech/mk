package com.hand.hmall.services.order;

import com.hand.hap.mam.dto.PinVcodeDto;
import com.hand.hmall.om.mapper.OrderEntryMapper;
import com.hand.hmall.services.order.dto.OrderEntry;
import com.hand.hmall.services.order.service.IHMallOrderService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author alaowan
 * Created at 2017/12/26 14:47
 * @description
 */
public class HMallOrderServiceImpl implements IHMallOrderService {

    @Autowired
    private OrderEntryMapper orderEntryMapper;

    @Override
    public List<OrderEntry> getSubOrderEntriesByPinCode(String pin) {
        List<OrderEntry> result = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("pinCode", pin);
        List<com.hand.hmall.om.dto.OrderEntry> entries = orderEntryMapper.selectOrderEntryByVCode(map);
        for (com.hand.hmall.om.dto.OrderEntry oe : entries) {
            OrderEntry entry = new OrderEntry();
            entry.setPin(oe.getPin());
            entry.setVproductCode(oe.getVproductCode());
            result.add(entry);
        }
        return result;
    }

    @Override
    public String getParentVCode(String pin, String vcode) {
        PinVcodeDto dto = new PinVcodeDto();
        dto.setPin(pin);
        dto.setVcode(vcode);
        com.hand.hmall.om.dto.OrderEntry orderEntry = orderEntryMapper.selectParentVCode(dto);
        if (orderEntry != null)
            return orderEntry.getVproductCode();
        return null;
    }
}
