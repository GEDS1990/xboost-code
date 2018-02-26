package com.mckinsey.ckc.sf.main;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mckinsey.ckc.sf.constants.IConstants;
import com.mckinsey.ckc.sf.data.Carrier;
import com.mckinsey.ckc.sf.data.CarrierRecords;
import com.mckinsey.ckc.sf.data.Opportunity;
import com.mckinsey.ckc.sf.data.Parcel;
import com.mckinsey.ckc.sf.data.Points;
import com.mckinsey.ckc.sf.preparation.DataPreparation;
import com.mckinsey.ckc.sf.restful.data.DataVModel;
import com.mckinsey.ckc.sf.restful.data.FeiXianModel;
import com.mckinsey.ckc.sf.restful.data.Greeting;
import com.mckinsey.ckc.sf.restful.data.MoveRequest;
import com.mckinsey.ckc.sf.restful.data.MoveResponse;
import com.mckinsey.ckc.sf.restful.data.XYModel;

@RestController
@EnableAutoConfiguration
public class Application_RealTime implements Filter,IConstants,EmbeddedServletContainerCustomizer{

	//initialize timeID with start time
	private static Calendar cal=Calendar.getInstance();
	private static int h=cal.get(Calendar.HOUR_OF_DAY);
	private static int mi=cal.get(Calendar.MINUTE);
	private static int time=h*60+mi;
//	private static int timeID = time/TIME_UNIT-1;
	private static int timeID = PICKING_START_TIME*60/TIME_UNIT-1;
	private final AtomicLong counter = new AtomicLong(timeID);
	
	public HashMap<Integer, Carrier> carrierMap = new HashMap<Integer, Carrier>();
	List<Parcel> deliverParcelMap = new ArrayList<Parcel>();
	List<Opportunity> oppotunities = new ArrayList<Opportunity>();
	private Main main;
	private List<Points> pointsList ;
	double carrierSpeedPerMm;
	public HashMap<Integer,List<Parcel>> timeIdParcelList;
	
	public  HashMap<Integer, List<CarrierRecords>> dataVMap;
	public  HashMap<Integer, DataVModel> dataVStaticsticMap ;
	
	public Application_RealTime() {
		main = new Main();
		//set seed
		Random rand = new Random();
		rand.setSeed(N_CARRIER_RANGE);
		
		int carrierSpeedPerKmh = SPEED_RANGE[0];
		carrierSpeedPerMm = carrierSpeedPerKmh * 1000 / 60.0;

		// initialize parcel data
		timeIdParcelList = new HashMap<Integer,List<Parcel>>();
		DataPreparation db = new DataPreparation();
		pointsList = db.loadOriginalDemandFromDB(carrierSpeedPerMm,timeIdParcelList);
		
		//initialize carrier
		for (int i = 1; i <= N_CARRIER_RANGE; i++) {
			int seed;
			if(pointsList.size()==0)
			{
				seed=0;
			}else
			{
				seed=rand.nextInt(pointsList.size());
			}
			Points point = pointsList.get(seed);
	//		Points point = pointsList.get(rand.nextInt(pointsList.size()));
			Carrier carrier = new Carrier();
			carrier.setCarrierID(i);
			carrier.setCurrentPos(point.getPosition());
			carrier.setInitialPos(point.getPosition());
			carrier.setSpeed(carrierSpeedPerMm);
			// normal carrier
			carrier.setGroupID(0);
			carrierMap.put(i, carrier);
		}
		
		//initialize dashboard
		dataVMap = new HashMap<Integer,  List<CarrierRecords>>();
		dataVStaticsticMap = new HashMap<Integer,  DataVModel>();
	}


	@RequestMapping(value = "/dynamic/move", method = RequestMethod.POST)
	public ResponseEntity<MoveResponse> move(@RequestBody MoveRequest request) {
		MoveResponse response = new MoveResponse();
		if (request != null) {
			int timeID = request.getTimeID();
			System.out.println("time id :"+timeID);
			int carrierId = request.getCarrierID();
			System.out.println("carrier id :"+carrierId);
			if( Main.responseList.get(timeID) != null)
				response = Main.responseList.get(timeID).get(carrierId);
		}

		// TODO: call persistence layer to update
		return new ResponseEntity<MoveResponse>(response, HttpStatus.OK);
	}
	
	
	@RequestMapping("/dynamic/davav")
	public List<CarrierRecords> datav(@RequestParam(value = "carrierid", defaultValue = "World") String name) {
		timeID = (int)counter.incrementAndGet();
		if(timeID >  PICKING_END_TIME*60/TIME_UNIT){
			timeID =  PICKING_START_TIME*60/TIME_UNIT-1;
			counter.set(PICKING_START_TIME*60/TIME_UNIT-1);
		}
		long startTime = System.currentTimeMillis();
		dataVMap.clear();	
		main.decideCarrierAction(carrierMap,pointsList, deliverParcelMap, oppotunities, timeID, carrierSpeedPerMm,dataVMap);
		System.out.println("time spend is "+((System.currentTimeMillis()-startTime))+"ms");
		main.summarizeByTimeId(carrierMap,timeID, carrierSpeedPerMm,dataVStaticsticMap);
		return dataVMap.get(timeID);
	}
	
	@RequestMapping("/dynamic/date")
	public List<XYModel> date(@RequestParam(value = "carrierid", defaultValue = "World") String name) {
		List<XYModel> dvList = new ArrayList<XYModel>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		XYModel model = new XYModel();
		Date date = new Date(System.currentTimeMillis());
//		date.setDate(17);
//		date.setMonth(3);
//		date.setYear(117);
//		date.setHours(timeID*TIME_UNIT/60);
//		date.setMinutes(timeID*TIME_UNIT%60);
//		date.setSeconds(0);
		model.setX(sdf.format(date));
		dvList.add(model);
		return dvList;
	}
	
	
	@RequestMapping("/dynamic/davavstatistic")
	public List<DataVModel> datav_statistic(@RequestParam(value = "carrierid", defaultValue = "World") String name) {
		DataVModel dv = dataVStaticsticMap.get(timeID);
		List<DataVModel> dvList = new ArrayList<DataVModel>();
		dvList.add(dv);
		return dvList;
	}
	
	@RequestMapping("/dynamic/carrier")
	public List<XYModel> carrier(@RequestParam(value = "carrierid", defaultValue = "World") String name) {
		XYModel dv = new XYModel();
		List<XYModel> dvList = new ArrayList<XYModel>();
		dv.setX("普通骑士");
		dv.setY(N_CARRIER_RANGE);
		dvList.add(dv);
		XYModel dv2 = new XYModel();
		dv2.setX("扫尾骑士");
		dv2.setY(N_SWOT_CARRIER);
		dvList.add(dv2);
		return dvList;
	}
	
	@RequestMapping("/dynamic/parcel_feixian")
	public List<FeiXianModel> parcel_feixian(@RequestParam(value = "carrierid", defaultValue = "World") String name) {
		
		List<FeiXianModel> dvList = new ArrayList<FeiXianModel>();
		if(timeIdParcelList.containsKey(timeID)){
			for(Parcel p :timeIdParcelList.get(timeID)){
				FeiXianModel dv = new FeiXianModel();
				dv.setX(p.getOrigPos().getX()+","+p.getOrigPos().getY());
				dv.setY(p.getDestPos().getX()+","+p.getDestPos().getY());
				dvList.add(dv);
			}
		}
		
		return dvList;
	}
	
	@RequestMapping("/dynamic/one_hour")
	public List<XYModel> one_hour(@RequestParam(value = "carrierid", defaultValue = "World") String name) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		List<XYModel> dvList = new ArrayList<XYModel>();
		int sum = 0;
		int sum1 = 0;
		int sum2 = 0;
		int sum3 = 0;
		XYModel dv = new XYModel();
		XYModel dv1 = new XYModel();
		XYModel dv2 = new XYModel();
		XYModel dv3 = new XYModel();
		Date date = new Date(System.currentTimeMillis());
		date.setHours((int)timeID/12);
		date.setMinutes(0);
		date.setSeconds(0);
		dv.setX(sdf.format(date));
		Date date1 = new Date(System.currentTimeMillis());
		date1.setHours((int)timeID/12-1);
		date1.setMinutes(0);
		date1.setSeconds(0);
		Date date2 = new Date(System.currentTimeMillis());
		date2.setHours((int)timeID/12-2);
		date2.setMinutes(0);
		date2.setSeconds(0);
		Date date3 = new Date(System.currentTimeMillis());
		date3.setHours((int)timeID/12-3);
		date3.setMinutes(0);
		date3.setSeconds(0);

//		for(int index = timeID-60/TIME_UNIT;index <= timeID;index++){
//			if(timeIdParcelList.containsKey(index)){
//				sum += timeIdParcelList.get(index).size();
//			}
//		}

		for(int index = (int)timeID/12*60/TIME_UNIT-180/TIME_UNIT;index < (int)timeID/12*60/TIME_UNIT-120/TIME_UNIT;index++){
			if(timeIdParcelList.containsKey(index)){
				if(timeID>=0 &&timeID<=12){
					sum3 = 0;
				}
				sum3 += timeIdParcelList.get(index).size();

			}
		}
		for(int index = (int)timeID/12*60/TIME_UNIT-120/TIME_UNIT;index < (int)timeID/12*60/TIME_UNIT-60/TIME_UNIT;index++){
			if(timeIdParcelList.containsKey(index)){
				if(timeID>=0 &&timeID<=12){
					sum2 = 0;
				}else{
					sum2 += timeIdParcelList.get(index).size();
				}
			}
		}
		sum2=sum2+sum3;
		for(int index = (int)timeID/12*60/TIME_UNIT-60/TIME_UNIT;index < (int)timeID/12*60/TIME_UNIT;index++){
			if(timeIdParcelList.containsKey(index)){
				if(timeIdParcelList.containsKey(index)){
					if(timeID>=0 &&timeID<=12){
						sum1 = 0;
					}else{
						sum1 += timeIdParcelList.get(index).size();
					}
				}
			}
		}
		sum1=sum1+sum2;
		for(int index = (int)timeID/12*60/TIME_UNIT;index <= timeID;index++){
			if(timeIdParcelList.containsKey(index)){
				if(timeID>=0 &&timeID<=12){
					sum = 0;
				}else{
					sum += timeIdParcelList.get(index).size();
				}
			}
		}
		sum=sum+sum1;

		dv.setX(sdf.format(date));
		dv.setY(sum);
		dv1.setX(sdf.format(date1));
		dv1.setY(sum1);
		dv2.setX(sdf.format(date2));
		dv2.setY(sum2);
		dv3.setX(sdf.format(date3));
		dv3.setY(sum3);
		dvList.add(dv);
		dvList.add(dv1);
		dvList.add(dv2);
		dvList.add(dv3);
		return dvList;
	}
	
	
	@RequestMapping("/dynamic/pressure")
	public List<XYModel> pressure(@RequestParam(value = "carrierid", defaultValue = "World") String name) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		List<XYModel> dvList = new ArrayList<XYModel>();
		for(int index = timeID-60/TIME_UNIT;index <= timeID;index++){
			XYModel dv = new XYModel();
			if(main.timeIdPressure.containsKey(index)){
//				Calendar cal=Calendar.getInstance();
//				cal.set(Calendar.HOUR, timeID*TIME_UNIT/60);
//				cal.set(Calendar.MINUTE, timeID*TIME_UNIT%60);
//				cal.set(Calendar.SECOND, 0);
				Date date = new Date(System.currentTimeMillis());
				date.setHours(index*TIME_UNIT/60);
				date.setMinutes(index*TIME_UNIT%60);
				date.setSeconds(0);
				dv.setX(sdf.format(date));
				dv.setY(main.timeIdPressure.get(index));
				dvList.add(dv);
			}
		}
		return dvList;
	}
	
	
	@RequestMapping("/dynamic/realtime_parcel")
	public List<XYModel> realtime_parcel(@RequestParam(value = "carrierid", defaultValue = "World") String name) {
		XYModel dv = new XYModel();
		List<XYModel> dvList = new ArrayList<XYModel>();
		dv.setX("未处理快件");
		dv.setY(dataVStaticsticMap.get(timeID).getUnattended());
		dvList.add(dv);
		XYModel dv2 = new XYModel();
		dv2.setX("已取快件");
		dv2.setY(dataVStaticsticMap.get(timeID).getCurrentDelivery());
		dvList.add(dv2);
		XYModel dv3 = new XYModel();
		dv3.setX("已存快件");
		dv3.setY(dataVStaticsticMap.get(timeID).getPickup());
		dvList.add(dv3);
		return dvList;
	}

	public static void main(String[] args) throws Exception {
		SpringApplication.run(Application_RealTime.class, args);
	}

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) res;
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with");
        chain.doFilter(req, res);
    }

    public void init(FilterConfig filterConfig) {}

    public void destroy() {}


	@Override
	public void customize(ConfigurableEmbeddedServletContainer container) {
		container.setPort(8081);
		
	}

}