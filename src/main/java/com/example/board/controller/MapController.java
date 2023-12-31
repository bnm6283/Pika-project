package com.example.board.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.board.model.Station;
import com.example.board.model.Store;
import com.example.board.model.User;
import com.example.board.model.Restaurant;
import com.example.board.model.Coffee;
import com.example.board.model.Coupon;
import com.example.board.repository.StationRepository;
import com.example.board.repository.StoreRepository;
import com.example.board.repository.CoffeeRepository;
import com.example.board.repository.CouponRepository;
import com.example.board.repository.RestaurantRepository;

@Controller
public class MapController {
    
    @Autowired
    StationRepository stationRepository;
    @Autowired
    CoffeeRepository coffeeRepository;
    @Autowired
    StoreRepository storeRepository;
    @Autowired
    RestaurantRepository restaurantRepository;
    @Autowired
    CouponRepository couponRepository;
    @Autowired
    HttpSession session;
    

    @GetMapping("/tetest/data")
    @ResponseBody
    public List<Station> tetestData(){       
       List<Station> excels = stationRepository.findAll();
        return excels;
    }

    @GetMapping("/tetest")
    public String tetest(Model model){
       
       List<Restaurant> excels = restaurantRepository.findAll();
       model.addAttribute("excels", excels);
        return "tetest";
    }
    
   @PostMapping("/tetest")
   public String tetestPost(@ModelAttribute Station excel) {
    //   stationRepository.save(excel);
      return "redirect:/tetest";
   }

   @GetMapping("/map")
   public String map(Model model) {
      List<Station> excel14s = stationRepository.findAll();
      model.addAttribute("excel14s", excel14s);
      return "/map/map";
   }

   @PostMapping("/map")
   public String mapPost(@ModelAttribute Station excel14) {
      stationRepository.save(excel14);
      return "redirect:map/map";
   }
    @GetMapping("/coffee/data")
    @ResponseBody
    public List<Coffee> coffeeData(){       
       List<Coffee> shops = coffeeRepository.findAll();
        return shops;
    }
    @GetMapping("/store/data")
    @ResponseBody
    public List<Store> storeData(){       
       List<Store> shops = storeRepository.findAll();
        return shops;
    }
    @GetMapping("/restaurant/data")
    @ResponseBody
    public List<Restaurant> restaurantData(){       
       List<Restaurant> shops = restaurantRepository.findAll();
        return shops;
    }
   @GetMapping("/map/coffee")
   public String coffee(Model model, @ModelAttribute Coupon coupon){
      List<Coffee> coffeeshop = coffeeRepository.findAll();
      model.addAttribute("coffeeshop", coffeeshop);
      List<Store> storeshop = storeRepository.findAll();
      model.addAttribute("storeshop", storeshop);
      List<Restaurant> restaurant = restaurantRepository.findAll();
      model.addAttribute("restaurant", restaurant);
      // couponRepository.save(coupon);
      return"map/coffee";
   }
   @PostMapping("/couponsave")
   @ResponseBody
   public String saveCoupon(@RequestBody Coupon coupon){
      
      User user =(User) session.getAttribute("user_info");
      
      // 사용자가 이미 다운로드한 쿠폰의 수를 확인
      List<Coupon> userCoupons = couponRepository.findByUser(user);

      int totalCouponCount = 0;
      for(Coupon singleCoupon : userCoupons) {
         totalCouponCount += singleCoupon.getCount();
      }

      if (totalCouponCount > 3) {
        return "쿠폰 다운로드 횟수를 초과하였습니다.";
      } else {
         Coupon newCoupon = new Coupon();
         newCoupon.setUser(user);

         String uniqueCode = Long.toString(Math.abs(new Random().nextLong()),36).substring(0,12);
         newCoupon.setCode(uniqueCode);
         totalCouponCount += 1;
         newCoupon.setCount(totalCouponCount);
         newCoupon.setName(coupon.getName());
         newCoupon.setStartDate(LocalDate.now());
         newCoupon.setEndDate(LocalDate.now().plusMonths(1));

         couponRepository.save(newCoupon);
      
      }

      return "저장완료";
   }
   
   @GetMapping("/where")
   public String where(){
      return"where";
   }
   @GetMapping("/chargingSearch")
   public String chargingSearch() {
      return "map/chargingSearch";
   }

}