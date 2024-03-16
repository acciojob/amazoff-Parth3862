package com.driver;

import java.util.*;

import org.springframework.stereotype.Repository;

@Repository
public class OrderRepository {

    private HashMap<String, Order> orderMap;
    private HashMap<String, DeliveryPartner> partnerMap;
    private HashMap<String, HashSet<String>> partnerToOrderMap;
    private HashMap<String, String> orderToPartnerMap;

    public OrderRepository(){
        this.orderMap = new HashMap<String, Order>();
        this.partnerMap = new HashMap<String, DeliveryPartner>();
        this.partnerToOrderMap = new HashMap<String, HashSet<String>>();
        this.orderToPartnerMap = new HashMap<String, String>();
    }

    public void saveOrder(Order order){
        // your code here
        orderMap.put(order.getId(),order);
    }

    public void savePartner(String partnerId){
        // your code here
        // create a new partner with given partnerId and save it
        DeliveryPartner partner = new DeliveryPartner(partnerId);
        partnerMap.put(partnerId,partner);
    }

    public void saveOrderPartnerMap(String orderId, String partnerId){
        if(orderMap.containsKey(orderId) && partnerMap.containsKey(partnerId)){
            // your code here
            //add order to given partner's order list
            //increase order count of partner
            //assign partner to this order
            if(!partnerToOrderMap.containsKey(partnerId)){
                partnerToOrderMap.put(partnerId,new HashSet<>());
            }
            partnerToOrderMap.get(partnerId).add(orderId);
            DeliveryPartner partner = partnerMap.get(partnerId);
            partner.setNumberOfOrders(partner.getNumberOfOrders()+1);
            orderToPartnerMap.put(orderId,partnerId);
        }
    }

    public Order findOrderById(String orderId){
        // your code here
        return orderMap.get(orderId);
    }

    public DeliveryPartner findPartnerById(String partnerId){
        // your code here
        return partnerMap.get(partnerId);
    }

    public Integer findOrderCountByPartnerId(String partnerId){
        // your code here
        return partnerMap.get(partnerId).getNumberOfOrders();
    }

    public List<String> findOrdersByPartnerId(String partnerId){
        // your code here
        List<String> ans = new ArrayList<>();
        if(partnerToOrderMap.containsKey(partnerId)){
            for(String s:partnerToOrderMap.get(partnerId)){
                ans.add(s);
            }
        }
        return ans;
    }

    public List<String> findAllOrders(){
        // your code here
        // return list of all orders
        return new ArrayList<>(orderMap.keySet());
    }

    public void deletePartner(String partnerId){
        // your code here
        // delete partner by ID
        if(partnerMap.containsKey(partnerId)){
            partnerMap.remove(partnerId);
        }
    }

    public void deleteOrder(String orderId){
        // your code here
        // delete order by ID
        if(orderMap.containsKey(orderId)){
            orderMap.remove(orderId);
        }
    }

    public Integer findCountOfUnassignedOrders(){
        // your code here
        int ans=0;
        for(String s:orderMap.keySet()){
            if(!orderToPartnerMap.containsKey(s)){
                ans++;
            }
        }
        return ans;
    }

    public Integer findOrdersLeftAfterGivenTimeByPartnerId(String timeString, String partnerId){
        // your code here
        int deliveryTime = countDeliveryTime(timeString);
        int ans = 0;
        HashSet<String> set = partnerToOrderMap.getOrDefault(partnerId,new HashSet<>());
        for(String orderId : set){
            Order order1 = orderMap.get(orderId);
            if(order1.getDeliveryTime()>deliveryTime){
                ans++;
            }
        }
        return ans;
    }

    public String findLastDeliveryTimeByPartnerId(String partnerId){
        // your code here
        // code should return string in format HH:MM
        int lastDeliveryTime = Integer.MIN_VALUE;
        HashSet<String> set = partnerToOrderMap.getOrDefault(partnerId,new HashSet<>());
        for(String orderId:set){
            Order order = orderMap.get(orderId);
            lastDeliveryTime=Math.max(lastDeliveryTime,order.getDeliveryTime());
        }
        int hours = lastDeliveryTime/60;
        int minutes = lastDeliveryTime%60;
        return String.format("%02d:%02d",hours,minutes);
    }
    public int countDeliveryTime(String timeString){
        String[] strs = timeString.split(":");
        int hours = Integer.parseInt(strs[0]);
        int minutes = Integer.parseInt(strs[1]);
        return hours*60+minutes;
    }
}