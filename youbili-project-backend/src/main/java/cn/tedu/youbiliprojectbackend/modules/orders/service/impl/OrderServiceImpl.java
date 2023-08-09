package cn.tedu.youbiliprojectbackend.modules.orders.service.impl;

import cn.tedu.youbiliprojectbackend.common.ex.ServiceException;
import cn.tedu.youbiliprojectbackend.common.security.CurrentPrincipal;
import cn.tedu.youbiliprojectbackend.common.web.response.ServiceCode;
import cn.tedu.youbiliprojectbackend.modules.orders.dao.repository.IOrdersRepository;
import cn.tedu.youbiliprojectbackend.modules.orders.pojo.entity.Order;
import cn.tedu.youbiliprojectbackend.modules.orders.pojo.param.OrdersParam;
import cn.tedu.youbiliprojectbackend.modules.orders.pojo.vo.OrderListVO;
import cn.tedu.youbiliprojectbackend.modules.orders.service.IOrdersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class OrderServiceImpl implements IOrdersService {

    @Autowired
    private IOrdersRepository ordersRepository;

    public OrderServiceImpl() {
        log.debug("创建业务类对象：OrderServiceImpl");
    }


    @Override
    public void addNew(CurrentPrincipal currentPrincipal, OrdersParam orderParam) {
        log.debug("开始处理【写入订单】的业务，参数：{}", orderParam);
        Order order = new Order();
        Date date = new Date();
        BeanUtils.copyProperties(orderParam, order);
        order.setOrderNumber(new SimpleDateFormat("yyyyMMdd").format(date) + System.currentTimeMillis());
//        order.setPayTime(LocalDateTime.now());
        order.setUserID(currentPrincipal.getUserID());
        ordersRepository.insertOrders(order);
    }

    /**
     * 更新订单信息
     *
     * @param order
     */
    @Override
    public void update(Order order) {
        ordersRepository.update(order);
    }


    @Override
    public List<OrderListVO> list() {
        List<OrderListVO> list = ordersRepository.list();
        for (OrderListVO Vo : list) {
            Vo.setStatus(Vo.getEnable() != null);
        }
        return list;
    }

    @Override
    public void deleteById(Long orderID) {

        int rows = ordersRepository.deleteById(orderID);
        if (rows == 0) {
            throw new ServiceException(ServiceCode.ERROR_CONFLICT, "没有相关数据!");
        }
        log.debug("{}", rows);
    }

    /**
     * 查询支付的订单
     *
     * @param orderNumber
     */
    @Override
    public Order selectOne(String orderNumber) {
        return ordersRepository.selectOne(orderNumber);
    }

}
