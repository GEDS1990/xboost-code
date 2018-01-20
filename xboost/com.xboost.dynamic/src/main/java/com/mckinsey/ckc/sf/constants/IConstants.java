package com.mckinsey.ckc.sf.constants;

public interface IConstants {
	
	int CARRIER_SUPPLY = 9999; // unlimited carrier resource
	int CARRIER_CAPACITY = 20; // unlimited carrier capacity
	int TIME_UNIT = 5;
	int PICKUP_TIME = 0;
	int DROPOFF_TIME = 0;
	int PICKING_START_TIME = 10;
	int PICKING_END_TIME = 24;
	int DEMAND_CUT_TIME = 18;
	int SIMU_HOUR = 8;
	int SIMU_TIME_UNITS = SIMU_HOUR*60/TIME_UNIT;

	int DISTANCE_INFLATOR = 100*1000;
	int INTEREST_TOP_RANK = 10;
//	int MAX_ANGLE0 = 60;
	int REQUIRED_SHIPPING_TIME = 3;
	
	//how many hours before cut time should we loose the angle in decision function
	int OPEN_TIMING = 2;
	int[] OPT_IMPORTANCE_RANGE = {0};
	int N_CARRIER_RANGE = 500;
	int[] SPEED_RANGE = {20};
	int OPT_MAX_ANGLE = 45;

	//parameters for think method
	int PARA_A = 2; //importance for opportunity demand against currently shipping demand
	int PARA_B = 2; //importance for current location closeness against destination closeness
	int PARA_C = 1000;  //factor to compare remaining time against distance 

	//swot capacity
	int SWOT_CAPACITY = 3;
	double URGENCY_LEAD_TIME = 1.5;
	int N_SWOT_CARRIER = 50;
	double PICKING_START_TIME_SWOT = URGENCY_LEAD_TIME+PICKING_START_TIME;
	int SWOT_END_TIME = 20;
	
	int NORMAL_CARRIER = 1;
	int SWOT_CARRIER = 2;
	
	int UNDEMAND = -1;
	int UNATTENDED = 0;
	int DELIVERED = 2;
	int PICKUP = 1;
	
	double DATA_COMPLETE_RATE = 0.7755929;
	
	//add for hub
	int CARRIER_CAPACITY_PICKUP = 60; // hub carrier capacity
	int CARRIER_CAPACITY_DROPOFF = 8;
	int CARRIER_MEANCAPACITY = 30; //estimated carrier capacity in one hour to calculate hub carrier number


}
