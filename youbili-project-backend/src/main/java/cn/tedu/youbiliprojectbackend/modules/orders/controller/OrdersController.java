package cn.tedu.youbiliprojectbackend.modules.orders.controller;

import cn.tedu.youbiliprojectbackend.common.security.CurrentPrincipal;
import cn.tedu.youbiliprojectbackend.common.web.response.RestBean;
import cn.tedu.youbiliprojectbackend.modules.orders.pojo.param.OrdersParam;
import cn.tedu.youbiliprojectbackend.modules.orders.pojo.vo.OrderListVO;
import cn.tedu.youbiliprojectbackend.modules.orders.service.IOrdersService;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * 订单控制器
 *
 * @author 万佳兴
 */
@Slf4j
@RestController
@RequestMapping("/orders/details/")
public class OrdersController {


    @Autowired
    private IOrdersService ordersService;

    public OrdersController() {
        log.info("创建订单控制器对象:OrdersController");
    }


    @PostMapping("/pay")
    public RestBean addNew(@AuthenticationPrincipal CurrentPrincipal currentPrincipal, OrdersParam orderParam) {
        log.debug("开始处理【写入订单】的请求，参数：{}", orderParam);
        System.out.println(currentPrincipal.getUserID());
//        Date date = new Date();
//        orderParam.setOrderNumber(new SimpleDateFormat("yyyyMMdd").format(date) + System.currentTimeMillis());
//        orderParam.setPayTime(LocalDateTime.now());
//        orderParam.setUserID(currentPrincipal.getUserID());
        ordersService.addNew(currentPrincipal, orderParam);
        return RestBean.success();
    }


    @GetMapping("/list")
    public RestBean<List<OrderListVO>> listOrders() {
        log.debug("开始处理【订单列表】的请求");
        List<OrderListVO> list = ordersService.list();
        return RestBean.success(list);
    }

    @PostMapping("/delete")
    public RestBean<String> deleteOrders(@NotNull Long orderID) {
        log.debug("接收到的请求参数为!{}", orderID);
        log.debug("开始处理【删除订单列表】的请求");
        ordersService.deleteById(orderID);
        return RestBean.success();
    }
}
