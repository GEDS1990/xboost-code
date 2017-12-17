package com.mckinsey.sf.constants;

import com.xboost.websocket.SystemWebSocketHandler;

/**
*    
* Author：Alivia Chen   
* Email : alivia_chen@mckinsey.com
* Date  ：Apr 27, 2017 
* @version        
*/
public interface IConstants {
	
	//read from excel
//	int PALNS_NUM_ITERS =10;	
//	int DIST_MODE = 1 ;
//	String TRANSPORT_COST_DISTANCE = "src/main/resources/hangzhou/distance_new.csv";
//	String TRANSPORT_COST_DISTANCE2 = "src/main/resources/hangzhou_0918/69网点和经纬度.csv";
	
//	int TRANSPORT_COST_FIXED_STOP_TIME =2; //装卸货时间
	
	enum Score {  
		REJECTED,ACCEPTED,BETTER_THAN_CURRENT,NEWBEST
	} 
	
	int[] REGRET_INSERTION_K = {2,4,6};
	
	double NOISE_PORB = 0.9;
	double NOISE_LEVEL = 0.025;
	
	double PALNS_W = 0.1;
	double PALNS_ALPHA = 0.8;
	
	int PALNS_MAX_TIME = 50;
	double PALNS_DECAY = 0.2;
	int PALNS_CORES = 2;
	int PALNS_SEGMENT = 100;//目的地分拣耗时(min)
	
	int TIME_CONSTRAINT_WEIGHT = 1;
	
	double JOB_PACKER_INTERVAL = 15f;//上车装货时间(min)

	double VOL_PERCENT = 2;
	
	String TRANSPORT_COST_NEAREST = "";
	
	double SHAW_REMOVAL_WD = 9;
	double SHAW_REMOVAL_WC = 2;
	double SHAW_REMOVAL_K = 0.5;
	int SHAW_REMOVAL_MAX_K = 50;
	double SHAW_REMOVAL_P = 7;
	double SHAW_REMOVAL_WT = 4;
	double SHAW_REMOVAL_WS = 10;
	
	double RANDOM_REMOVAL_K = 0.5;
	int RANDOM_REMOVAL_MAX_K = 100;
	
	int COST_CALCULATOR_MAX = 5000;
	int COST_CALCULATOR_N = 5000;
	int COST_CALCULATOR_MIN = 2;
	
	double ROUTE_REMOVAL_K = 0.1;
	
	double WORST_REMOVAL_P = 4;
	double WORST_REMOVAL_K = 0.8;
	int WORST_REMOVAL_MAX_K = 100;
	
	double DISTANCE_CONSTRAINT_WEIGHT = 1;
	
	String DEFAULT_CONSTRAINTS = "DEFAULT_CONSTRAINTS";
	
	int POS_START = 1;
	int	POS_MIDDLE = 2;
	int POS_END = 3;
	
	String PICKUP = "PICKUP";
	String DELIVERY = "DELIVERY";
	String START = "START";
	String END = "END";
	
	double PROCESSTING_TIME =0;
	
	double DISTANCE_COST = 0;
	//接力模型变量 start
	//assumptions
	int truck_capacity = 400;
	double cost_per_km = 6.8;
	int shift_pct = 1;
	int truck_capacity2 = 35;
	int truck_capacity3 = 60;
	int truck_capacity4 = 35;
	int cost_per_km2 = 3;
	int shift_pct2 = 1;
	int speed1 = 15;
	int speed2 = 12;
	int speed3 = 20;
	int speed4 = 30;
	int dist_limit2 = 30;
	int dist_limit_bike = 5;
	int dist_limit_dada = 10;

	//接力模型变量 end

	SystemWebSocketHandler systemWebSocketHandler = new SystemWebSocketHandler();
}
