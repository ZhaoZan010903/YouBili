package cn.tedu.youbiliprojectbackend.modules.orders.dao.repository;

import cn.tedu.youbiliprojectbackend.modules.orders.pojo.entity.Order;
import cn.tedu.youbiliprojectbackend.modules.orders.pojo.vo.OrderListVO;

import java.util.List;

public interface IOrdersRepository {


    /**
     * 插入订单
     *
     * @param order
     * @return
     */
    int insertOrders(Order order);


    void update(Order order);

    /**
     * 根据id删除订单
     *
     * @param orderID 订单ID
     * @return 受影响的行数
     */
    int deleteById(Long orderID);


    /**
     * 查询支付的订单
     */
    Order selectOne(String orderNumber);

    /**
     * 查询订单列表
     *
     * @return 文章列表
     */
    List<OrderListVO> list();


}
