package cn.tedu.youbiliprojectbackend.modules.orders.service;

import cn.tedu.youbiliprojectbackend.common.security.CurrentPrincipal;
import cn.tedu.youbiliprojectbackend.modules.orders.pojo.entity.Order;
import cn.tedu.youbiliprojectbackend.modules.orders.pojo.param.OrdersParam;
import cn.tedu.youbiliprojectbackend.modules.orders.pojo.vo.OrderListVO;

import java.util.List;

public interface IOrdersService {

    /**
     * 发布文章
     *
     * @param ordersParam 新的文章数据
     */
    void addNew(CurrentPrincipal currentPrincipal, OrdersParam ordersParam);


    /**
     * 更新订单信息
     */
    void update(Order order);


    /**
     * 根据id删除订单
     *
     * @param orderID 订单ID
     * @return 受影响的行数
     */
    void deleteById(Long orderID);


    /**
     * 查询支付的订单
     */
    Order selectOne(String orderNumber);

    /**
     * 查询订单列表
     *
     * @return 订单列表
     */
    List<OrderListVO> list();


}
