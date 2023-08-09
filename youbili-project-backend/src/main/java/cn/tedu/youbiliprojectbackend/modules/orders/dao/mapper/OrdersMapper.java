package cn.tedu.youbiliprojectbackend.modules.orders.dao.mapper;

import cn.tedu.youbiliprojectbackend.modules.orders.pojo.entity.Order;
import cn.tedu.youbiliprojectbackend.modules.orders.pojo.vo.OrderListVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdersMapper extends BaseMapper<Order> {

    /**
     * 插入订单
     */
    int insertOrders(Order order);

    /**
     * 更新订单数据
     *
     * @param order
     * @return
     */
    int update(Order order);


    /**
     * 查询支付的订单
     */
    Order selectOne(String orderNumber);

    /**
     * 根据ID删除订单
     *
     * @return 受影响的行数
     */
    int deleteById(Long orderID);

    /**
     * 查询订单数据
     *
     * @return 订单数据列表
     */
    List<OrderListVO> list();
}
