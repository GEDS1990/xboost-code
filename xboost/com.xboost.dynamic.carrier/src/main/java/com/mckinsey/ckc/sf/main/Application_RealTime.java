package com.mckinsey.ckc.sf.main;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
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
			Points point = pointsList.get(rand.nextInt(pointsList.size()));
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


	@RequestMapping(value = "/move", method = RequestMethod.POST)
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
	
	
	@RequestMapping("/davav")
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
	
	@RequestMapping("/date")
	public List<XYModel> date(@RequestParam(value = "carrierid", defaultValue = "World") String name) {
		List<XYModel> dvList = new ArrayList<XYModel>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		XYModel model = new XYModel();
		Date date = new Date(System.currentTimeMillis());
		date.setDate(17);
		date.setMonth(3);
		date.setYear(117);
		date.setHours(timeID*TIME_UNIT/60);
		date.setMinutes(timeID*TIME_UNIT%60);
		date.setSeconds(0);
		model.setX(sdf.format(date));
		dvList.add(model);
		return dvList;
	}
	
	
	@RequestMapping("/davavstatistic")
	public List<DataVModel> datav_statistic(@RequestParam(value = "carrierid", defaultValue = "World") String name) {
		DataVModel dv = dataVStaticsticMap.get(timeID);
		List<DataVModel> dvList = new ArrayList<DataVModel>();
		dvList.add(dv);
		return dvList;
	}
	
	@RequestMapping("/carrier")
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
	
	@RequestMapping("/parcel_feixian")
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
	
	@RequestMapping("/one_hour")
	public List<XYModel> one_hour(@RequestParam(value = "carrierid", defaultValue = "World") String name) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		List<XYModel> dvList = new ArrayList<XYModel>();
		int sum = 0;
		XYModel dv = new XYModel();
		Date date = new Date(System.currentTimeMillis());
		date.setHours(timeID*TIME_UNIT/60);
		date.setMinutes(timeID*TIME_UNIT%60);
		date.setSeconds(0);
		dv.setX(sdf.format(date));
		for(int index = timeID-60/TIME_UNIT;index <= timeID;index++){
			if(timeIdParcelList.containsKey(index)){
				sum += timeIdParcelList.get(index).size();
			}
		}
		dv.setY(sum);
		dvList.add(dv);
		return dvList;
	}
	
	
	@RequestMapping("/pressure")
	public List<XYModel> pressure(@RequestParam(value = "carrierid", defaultValue = "World") String name) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		List<XYModel> dvList = new ArrayList<XYModel>();
		for(int index = timeID-60/TIME_UNIT;index <= timeID;index++){
			XYModel dv = new XYModel();
			if(main.timeIdPressure.containsKey(index)){
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
	
	
	@RequestMapping("/realtime_parcel")
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