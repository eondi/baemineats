package com.sparta.baemineats.service;

import com.sparta.baemineats.dto.requestDto.*;
import com.sparta.baemineats.dto.responseDto.OrderResponse;
import com.sparta.baemineats.entity.*;
import com.sparta.baemineats.entity.Order;
import com.sparta.baemineats.repository.*;
import com.sparta.baemineats.security.UserDetailsImpl;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
class OrderServiceIntegrationTest {

    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private MenuRepository menuRepository;
    private User testUser;
    private Store testStore;
    private Menu testMenu;

    @BeforeAll
    void setUp() throws IOException {
        // 테스트에 사용될 사용자 생성
        String userName = "test";
        String password = "password";
        String address = "address";
        String email = "email@gmail.com";
        UserRoleEnum userRoleEnum = UserRoleEnum.USER;
        SignupRequestDto signuprequestDto = new SignupRequestDto(userName,password,address,email,userRoleEnum);
        testUser = userRepository.save(new User(signuprequestDto, ""));

        //테스트에 사용될 가게 생성
        String storeName = "홍콩반점";
        String storeDescription = "맛있는 가게";
        StoreRequest request = new StoreRequest(storeName,storeDescription);
        testStore = storeRepository.save(new Store(request, "사장"));

        //테스트에 사용될 메뉴 생성
        Long StoreId = 1L;
        String menuName = "탕수육";
        int menuPrice = 25000;
        String menuDescription = "돼지고기 : 국내산";

        MockMultipartFile image = new MockMultipartFile(
                "test",
                "icon-search.png",
                "image/png",
                new FileInputStream(new File("/Users/Ssumin/Desktop/icon-search.png")));

        MenuRequest requestDto = new MenuRequest(menuName, menuPrice, menuDescription, image);
        testMenu = menuRepository.save(new Menu(requestDto, testStore, ""));

    }
    @Test
    @DisplayName("주문 등록")
    void test1() throws IOException {
//         given
//         테스트에 사용될 장바구니 아이템을 생성 및 저장
//         이 부분은 실제 상황에 맞게 상점과 메뉴, 장바구니 아이템을 생성
        Long storeId = 1L;
        Long userId = 1L;
        Long menuId = 1L;
        Long quantity = 1L;
        int totalPrice = 1;

        CartRequest cartrequest = new CartRequest(storeId, userId, menuId, quantity ,totalPrice);
        cartRepository.save(new Cart(cartrequest, testUser,testStore,testMenu));

//        when - then
        // createOrderFromCart 메소드 호출
        List<Order> orders = orderService.createOrderFromCart(testUser);

        // 반환된 주문 목록이 비어 있지 않음을 확인
        assertFalse(orders.isEmpty());
    }
    @Test
    @DisplayName("주문 조회")
    void test2() throws IOException {
        // given
        // createOrderFromCart 메소드를 통해 주문 생성
        List<Order> createdOrders = orderService.createOrderFromCart(testUser);

        // when - then
        // getOrders 메소드 호출
        UserDetailsImpl userDetails = new UserDetailsImpl(testUser);
        List<OrderResponse> orders = orderService.getOrders(userDetails);

        // 반환된 주문 목록이 비어 있지 않음을 확인
        assertFalse(orders.isEmpty());

        // 반환된 주문 목록의 크기가 생성된 주문의 수와 같은지 확인
        assertEquals(createdOrders.size(), orders.size());
    }

    @Test
    @DisplayName("주문 상태 업데이트")
    void test3() throws IOException {
        // given
        // createOrderFromCart 메소드를 통해 주문 생성
        List<Order> createdOrders = orderService.createOrderFromCart(testUser);
        Order createdOrder = createdOrders.get(0);
        OrderUpdate orderUpdate = new OrderUpdate(Order.OrderStateEnum.DELIVERING);

        // when
        // updateOrderState 메소드 호출
        orderService.updateOrderState(createdOrder.getOrderId(), orderUpdate, testUser);
        Order updatedOrder = orderRepository.findById(createdOrder.getOrderId()).orElseThrow();

        // then
        // 주문 상태가 업데이트 되었는지 확인
        assertEquals(Order.OrderStateEnum.DELIVERING, updatedOrder.getOrderState());
    }

    @Test
    @DisplayName("주문 삭제")
    void test4() throws IOException {
        // given
        // createOrderFromCart 메소드를 통해 주문 생성
        List<Order> createdOrders = orderService.createOrderFromCart(testUser);
        Order createdOrder = createdOrders.get(0);

        // when
        // deleteOrder 메소드 호출
        orderService.deleteOrder(createdOrder.getOrderId(), testUser);

        // then
        // 주문이 삭제되었는지 확인
        assertFalse(orderRepository.existsById(createdOrder.getOrderId()));
    }



}