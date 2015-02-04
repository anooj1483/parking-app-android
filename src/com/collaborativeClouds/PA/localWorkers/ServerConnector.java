package com.collaborativeClouds.PA.localWorkers;

/**
 * Created by anoojkrishnang on 14/12/14.
 */
public class ServerConnector {

	public static String BASE_URL		=	"http://192.168.0.113:8080";
    public static String PARKING_STATUS	=	BASE_URL+"/ParkingAppServer/parking/park/status";
    public static String BOOK_SLOT		=	BASE_URL+"/ParkingAppServer/parking/park/bookslot";
    public static String GET_SLOT		=	BASE_URL+"/ParkingAppServer/parking/park/getSlotofUser";
    public static String LOGIN			=	BASE_URL+"/ParkingAppServer/parking/park/login";
    public static String REMOVE_SLOT	=	BASE_URL+"/ParkingAppServer/parking/park/deleteslot";
}
