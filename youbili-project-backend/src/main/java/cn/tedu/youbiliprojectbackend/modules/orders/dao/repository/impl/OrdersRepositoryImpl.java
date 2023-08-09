package cn.tedu.youbiliprojectbackend.modules.orders.dao.repository.impl;

import cn.tedu.youbiliprojectbackend.modules.orders.dao.mapper.OrdersMapper;
import cn.tedu.youbiliprojectbackend.modules.orders.dao.repository.IOrdersRepository;
import cn.tedu.youbiliprojectbackend.modules.orders.pojo.entity.Order;
import cn.tedu.youbiliprojectbackend.modules.orders.pojo.vo.OrderListVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
public class OrdersRepositoryImpl implements IOrdersRepository {
    @Autowired
    private OrdersMapper ordersMapper;

    public OrdersRepositoryImpl() {
        log.info("创建存储库对象：OrdersRepositoryImpl");
    }

    @Override
    public int insertOrders(Order order) {
        log.debug("开始执行【写入订单】的数据访问，参数：{}", order);
        return ordersMapper.insertOrders(order);
    }

    @Override
    public void update(Order order) {
        ordersMapper.update(order);
    }


    @Override
    public List<OrderListVO> list() {
        log.debug("开始执行【查询订单列表】的数据访问");
        List<OrderListVO> list = ordersMapper.list();
        return list;
    }


    @Override
    public int deleteById(Long id) {
        log.debug("开始执行【删除订单列表】的数据访问");
        int rows = ordersMapper.deleteById(id);
        return rows;
    }

    /**
     * 查询支付的订单
     *
     * @param orderNumber
     */
    @Override
    public Order selectOne(String orderNumber) {
        return ordersMapper.selectOne(orderNumber);
    }
}
