package com.pf.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pf.dao.OrdersMapper;
import com.pf.enums.OrderStatus;
import com.pf.pojo.*;
import com.pf.service.*;
import com.pf.utils.GenerateUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements IOrdersService {

    @Autowired
    private HttpSession session;

    @Autowired
    private IAddressBookService addressBookService;

    @Autowired
    private IShoppingCartService shoppingCartService;

    @Autowired
    private IOrderDetailService orderDetailService;

    @Autowired
    private IUserService userService;

    @Override
    @Transactional
    public void submit(Orders orders) {
//        Long userId = (String) session.getAttribute("userId");
        Long userId = 1L; // 测试数据

        // 设置用户ID
        orders.setUserId(userId);

        // 设置用户名
        User user = userService.getById(userId);
        String username = user.getName();
        if (username == null) username = "暂未设置用户名";
        orders.setUsername(username);

        // 设置订单状态
        orders.setStatus(OrderStatus.NO_PAYMENT);

        // 获取并设置地址信息
        Long addressBookId = orders.getAddressBookId();
        if (addressBookId == null) throw new RuntimeException("地址信息未输入！");
        AddressBook addressBook = addressBookService.getById(addressBookId);
        if (addressBook == null) throw new RuntimeException("无此地址信息！");
        orders.setConsignee(addressBook.getConsignee());
        orders.setPhone(addressBook.getPhone());
        String address =
                addressBook.getProvinceName() +
                        " " +
                        addressBook.getCityName() +
                        " " +
                        addressBook.getDistrictName() +
                        " " +
                        addressBook.getDetail();
        orders.setAddress(address);

        // 查询购物车信息
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId, userId);
        List<ShoppingCart> cartList = shoppingCartService.list(wrapper);
        if (cartList == null || cartList.size() == 0) throw new RuntimeException("无购物车信息！");

        // 从购物车中获取单价并设置订单总价
        BigDecimal amount = BigDecimal.valueOf(0);
        for (ShoppingCart shoppingCart : cartList) {
            BigDecimal decimal = shoppingCart.getAmount().multiply(BigDecimal.valueOf(shoppingCart.getNumber()));
            amount = amount.add(decimal);
        }
        orders.setAmount(amount);

        // 设置订单编号
        orders.setNumber(GenerateUtil.generateOrderNumber());

        // 提交订单信息
        this.save(orders);
        Long orderId = orders.getId();

        // 从购物车中获取并设置订单详情
        List<OrderDetail> orderDetailList = cartList.stream().map(e -> {
            OrderDetail orderDetail = new OrderDetail();
            BeanUtils.copyProperties(e, orderDetail, "id", "userId", "createTime");

            orderDetail.setOrderId(orderId);

            return orderDetail;
        }).toList();
        orderDetailService.saveBatch(orderDetailList);

        // 清空购物车
        shoppingCartService.clean();
    }
}
